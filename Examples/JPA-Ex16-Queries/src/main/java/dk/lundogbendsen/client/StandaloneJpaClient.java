package dk.lundogbendsen.client;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.log4j.Level;

import dk.lundogbendsen.model.Course;
import dk.lundogbendsen.model.CourseType;
import dk.lundogbendsen.model.CourseDto;
import dk.lundogbendsen.model.InstructorRole;
import dk.lundogbendsen.model.Period;
import dk.lundogbendsen.model.Person;
import dk.lundogbendsen.model.Role;
import dk.lundogbendsen.model.StudentRole;
import dk.lundogbendsen.jpa.util.JPAResourceHandler;
import dk.lundogbendsen.jpa.util.JpaUtil;

public class StandaloneJpaClient {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		JpaUtil.HibernateLoggingConfig loggingConfig = new JpaUtil.HibernateLoggingConfig();
		loggingConfig.setHibernateRootLogLevel(Level.OFF);
		loggingConfig.setLogDdlEnabled(true);
		loggingConfig.setLogSqlEnabled(false);
		loggingConfig.setLogParameterBindingEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);

		try(EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("StandaloneJpaTestPersistenceUnit");
			EntityManager entityManager = entityManagerFactory.createEntityManager()) {
			exerciseWithJPA(loggingConfig, entityManagerFactory, entityManager);
		}
	}
	
	public static void exerciseWithJPA(JpaUtil.HibernateLoggingConfig loggingConfig, 
			EntityManagerFactory entityManagerFactory, EntityManager entityManager){
		
		entityManager.getTransaction().begin();
		createManyEntities(entityManager);
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		printAllEntities(entityManager);
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();

		// Vi laver queries i blokke for ikke at må name clashes på vore
		// variabelnavne
		{
			String comment = "Hvis vi vil have hele objekter ud skriver vi";
			String jpaql = "SELECT c FROM Course c";
			List<Course> result = (List<Course>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "Det samme som ovenfor bare vha. named query (se annotationen på Course-klassen)";
			String queryName = "Course.findAll";
			List<Course> result = (List<Course>) entityManager.createNamedQuery(queryName).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, queryName, result);
		}
		{
			String comment = "Men vi kan også godt pille enkeltfelter ud (projection)";
			String jpaql = "SELECT c.id FROM Course c";
			List<Integer> result = (List<Integer>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "Det samme som ovenfor bare vha. named query (se annotation på Course-klassen)";
			String queryName = "Course.findAllIds";
			List<Integer> result = (List<Integer>) entityManager.createNamedQuery(queryName).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, queryName, result);
		}
		{
			String comment = "Man kan også traversere @OneToOne og @ManyToOne i SELECT-delen";
			String jpaql = "SELECT c.instructor.personAssignedToRole.name, c.courseType.name, c.period FROM Course c";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result, true);
		}
		{
			String comment = "WHERE understøtter DISTINCT (fjernelse af dubletter)";
			String jpaql = "SELECT DISTINCT t.lengthInDays FROM CourseType t";
			List<Integer> result = (List<Integer>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "WHERE understøtter LIKE";
			String jpaql = "SELECT p.id, p.name FROM Person p WHERE name LIKE '%e%'";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "WHERE understøtter IN";
			String jpaql = "SELECT p.id, p.name FROM Person p WHERE name IN ('Lise', 'Johan', 'Jørgen')";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "WHERE understøtter BETWEEN";
			String jpaql = "SELECT ct FROM CourseType ct WHERE ct.lengthInDays BETWEEN 0 AND 3";
			List<CourseType> result = entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "JPA understøtter funktioner som SUBSTRING og CONCAT";
			String jpaql = "SELECT ct.id, CONCAT( SUBSTRING(ct.name, 1, 10), '...') FROM CourseType ct";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "JPA understøtter funktionen LENGTH";
			String jpaql = "SELECT ct, LENGTH(ct.name) FROM CourseType ct WHERE LENGTH(ct.name) < 30";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "Hvis man vil 'joine' entities over collection relationships skal det gøres i FROM-delen";
			String jpaql = "";
			jpaql += "SELECT courseType.id, course.period" + "\n ";
			jpaql += "FROM CourseType courseType, Course course" + "\n ";
			jpaql += "WHERE courseType.id = course.courseType.id";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
			System.out.println("NB: Ikke noget specielt pænt eksempel - men det illustrerer pointen.");
			System.out.println("Eksemplet laver et join vha. det kartetiske produkt (som det også kendes fra SQL).");
		}
		{
			String comment = "JPA QL understøtter inner joins der følger en relation. Bemærk højresiden af JOIN'et.";
			String jpaql = "";
			jpaql += "SELECT t.id, c.period" + "\n ";
			jpaql += "FROM CourseType t JOIN t.instances c";
			// Man må også inkludere et INNER (det gør dog ingen forskel)
			// jpaql += "FROM CourseType t INNER JOIN t.instances c";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
			System.out.println("NB: FROM-delen kan læses som: vælg alle CourseTypes og vælg alle");
			System.out.println("de Courses, som de refererer til via CourseType.instances.");
			System.out.println("'Inner' udelader CourseTypes der ikke refererer til et Course.");
		}
		{
			String comment = "JPA QL understøtter også outer left joins der følger en relation. Bemærk højresiden af JOIN'et.";
			String jpaql = "";
			jpaql += "SELECT t.id, c.period" + "\n ";
			jpaql += "FROM CourseType t LEFT JOIN t.instances c";
			// Man må også inkludere et OUTER (det gør dog ingen forskel)
			// jpaql += "FROM CourseType t LEFT OUTER JOIN t.instances c";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
			System.out.println("NB: FROM-delen kan læses som: vælg alle CourseTypes og vælg alle");
			System.out.println("de Courses, som de refererer til via CourseType.instances.");
			System.out.println("'Outer left' medtager CouseTypes der ikke refererer til et Course.");
		}
		{
			String comment = "JPA QL understøtter en række aggregate functions - fx SUM(i) og COUNT(i)";
			String jpaql = "";
			jpaql += "SELECT SUM(t.lengthInDays), COUNT(t)" + "\n ";
			jpaql += "FROM CourseType t";
			Object[] result = (Object[]) entityManager.createQuery(jpaql).getSingleResult();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			String comment = "JPA QL understøtter GROUP BY";
			String jpaql = "";
			jpaql += "SELECT c.courseType.number, COUNT(s)" + "\n ";
			jpaql += "FROM Course c JOIN c.students s" + "\n ";
			jpaql += "GROUP BY c.courseType.number";
			List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
			System.out.println("NB: Undersåger hvor mange kursister der totalt set har taget hver kursustype");
		}
		{
			String comment = "JPA understøtter mapping til custom objekter";
			String jpaql = "";
			jpaql += "SELECT NEW dk.lundogbendsen.model.CourseDto(c.courseType.number, c.period, c.instructor.personAssignedToRole.name)";
			jpaql += "\n ";
			jpaql += "FROM Course c" + "\n ";
			List<CourseDto> result = (List<CourseDto>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
		}
		{
			/* create table Course (
			 * 		id integer not null, 
			 * 		fromDate date, 
			 * 		toDate date, 
			 * 		courseType_number varchar(255), 
			 * 		instructor_id integer, 
			 * 		primary key (id)
			 * )
			 * create table CourseType (
			 * 		number varchar(255) not null, 
			 * 		name varchar(255), 
			 * 		lengthInDays integer, 
			 *		primary key (number)
			 * )
			 */
			String comment = "JPA understøtter native SQL";
			String sql = "";
			sql += "SELECT t.number, c.fromDate" + "\n ";
			sql += "FROM CourseType t, Course c" + "\n ";
			sql += "WHERE t.number = c.courseType_number";
			List<Object[]> result = (List<Object[]>) entityManager.createNativeQuery(sql).getResultList();
			JpaUtil.prettyPrintQueryResult(comment, sql, result);
		}
		{
			// clean for at tømme cache, så vi kan påvise at relation ikke indlæses 
			entityManager.clear();
			/* create table Person (
			 * 		id integer not null, 
			 * 		name varchar(255), 
			 * 		primary key (id)
			 * )
			 */
			String comment = "JPA kan mappe native SQL-resultater til entities (se koden)";
			String sql = "";
			sql += "SELECT p.id, p.name" + "\n ";
			sql += "FROM Person p";
			Class entityType = Person.class;
			List<Person> result = entityManager.createNativeQuery(sql, entityType).getResultList();

			// Detach indlæste resultater, så relationerne ikke indlæses i toString
			entityManager.clear();
			JpaUtil.prettyPrintQueryResult(comment, sql, result);
		}
		
		entityManager.getTransaction().commit();
		// Man bør køre bulk updates i separat TX, da ænderinger ikke skrives til
		// in-memory entities. Af samme grund bør man også slette alle entities
		// i sin entityContext efter man har lavet en bulk update (se nedenfor)
		entityManager.getTransaction().begin();
		{
			String comment = "JPA understøtter bulk updates vha. Query.executeUpdate (return value = rows affected)";
			String jpaql = "UPDATE Person p SET p.name = 'Bilbo Baggins'";
			int rowsAffected = entityManager.createQuery(jpaql).executeUpdate();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, rowsAffected);
		}
		entityManager.getTransaction().commit();
		// Vi sletter persistence context cachen, da bulk updates _ikke_
		// afspejler sig i in memory objects
		entityManager.clear();
		entityManager.getTransaction().begin();
		{
			String jpaql = "SELECT p FROM Person p";
			List<Course> result = (List<Course>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(jpaql, result);
		}
		entityManager.getTransaction().commit();
		// Man bør køre bulk updates i separat TX, da ænderinger ikke skrives til
		// in-memory entities. Af samme grund bør man også slette alle entities
		// i sin entityContext efter man har lavet en bulk update (se nedenfor)
		entityManager.getTransaction().begin();
		{
			String comment = "JPA understøtter bulk deletes vha. query.executeUpdate (return value = rows affected)";
			String jpaql = "DELETE CourseType t WHERE t.name LIKE '%Spring%'";
			int rowsAffected = entityManager.createQuery(jpaql).executeUpdate();
			JpaUtil.prettyPrintQueryResult(comment, jpaql, rowsAffected);
		}
		entityManager.getTransaction().commit();
		// Vi sletter persistence context cachen, da bulk updates _ikke_
		// afspejler sig i in memory objects
		entityManager.clear();
		entityManager.getTransaction().begin();
		{
			String jpaql = "SELECT t FROM CourseType t";
			List<Course> result = (List<Course>) entityManager.createQuery(jpaql).getResultList();
			JpaUtil.prettyPrintQueryResult(jpaql, result);
		}
		entityManager.getTransaction().commit();
	}

	private static void createManyEntities(EntityManager entityManager) {
		Person thomas = new Person("Thomas");
		entityManager.persist(thomas);
		Person lise = new Person("Lise");
		entityManager.persist(lise);
		Person johan = new Person("Johan");
		entityManager.persist(johan);
		Person sidsel = new Person("Sidsel");
		entityManager.persist(sidsel);

		Role thomasInstructorRole = new InstructorRole(thomas, new Period(1998, 4, 12), InstructorRole.Seniority.SENIOR);
		entityManager.persist(thomasInstructorRole);
		Role lise_instructorRole = new InstructorRole(lise, new Period(2005, 1, 1), InstructorRole.Seniority.JUNIOR);
		entityManager.persist(lise_instructorRole);

		Role lise_studentRole = new StudentRole(lise, new Period(2005, 1, 1));
		entityManager.persist(lise_studentRole);
		Role johan_studentRole = new StudentRole(johan, new Period(2004, 23, 5));
		entityManager.persist(johan_studentRole);
		Role sidsel_studentRole = new StudentRole(sidsel, new Period(2006, 12, 11));
		entityManager.persist(sidsel_studentRole);

		CourseType oop = new CourseType("LB1378", "Objektorienteret programmering med Java 5", 5);
		entityManager.persist(oop);
		CourseType ejb = new CourseType("LB1898", "Udvikling af Enterprise JavaBeans, EJB 3.0", 5);
		entityManager.persist(ejb);
		CourseType spring = new CourseType("LB2091", "The Spring Framework", 3);
		entityManager.persist(spring);

		Course oopSeptember = oop.createCourseInstance(2009, 9, 9);
		oopSeptember.setInstructor(lise);
		oopSeptember.addStudent(johan);
		oopSeptember.addStudent(sidsel);
		entityManager.persist(oopSeptember);

		Course ejbOktober = ejb.createCourseInstance(2009, 10, 12);
		ejbOktober.setInstructor(thomas);
		ejbOktober.addStudent(lise);
		ejbOktober.addStudent(johan);
		entityManager.persist(ejbOktober);

		Course ejbNovember = ejb.createCourseInstance(2009, 11, 29);
		ejbNovember.addStudent(sidsel);
		entityManager.persist(ejbNovember);
	}

	private static void printAllEntities(EntityManager entityManager) {
		String jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select r from Role r";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select ct from CourseType ct";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Course c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
	}
}