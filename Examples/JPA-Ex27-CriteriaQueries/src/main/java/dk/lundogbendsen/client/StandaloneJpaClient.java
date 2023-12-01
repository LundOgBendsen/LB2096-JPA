package dk.lundogbendsen.client;

import java.util.List;

import dk.lundogbendsen.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.log4j.Level;

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
		//criteria query example
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		Root<Course> from = criteriaQuery.from(Course.class);

		//select all records
		System.out.println("Select all courses with no instructor sorted by type");
		CriteriaQuery<Object> select = criteriaQuery.select(from);
		select.orderBy(criteriaBuilder.asc(from.get(Course_.COURSE_TYPE))); //order by coursetype
		select.where(criteriaBuilder.isNull(from.get(Course_.instructor)));
		TypedQuery<Object> typedQuery = entityManager.createQuery(select);
		List<Object> resultlist = typedQuery.getResultList();
		System.out.println(resultlist);
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