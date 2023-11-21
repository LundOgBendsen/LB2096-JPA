package test.client.standalone;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import org.apache.log4j.Level;

import test.model.Person;
import dk.lundogbendsen.jpa.util.JPAResourceHandler;
import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;

public class StandaloneJpaClient {

	public static void main(String[] args) {
		
		JpaUtil.HibernateLoggingConfig loggingConfig = new JpaUtil.HibernateLoggingConfig();
		loggingConfig.setHibernateRootLogLevel(Level.WARN);
		loggingConfig.setLogDdlEnabled(true);
		loggingConfig.setLogSqlEnabled(false);
		loggingConfig.setLogParameterBindingEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);
		
		// ///////////////////////////////////////////////////////////////////////
		// Create new JPA entity manager (similar to JDBC Connection)
		EntityManagerFactory entityManagerFactory = null;
		EntityManager entityManager = null;
		try{
			entityManagerFactory = Persistence.createEntityManagerFactory("StandaloneJpaTestPersistenceUnit");
			entityManager = entityManagerFactory.createEntityManager();
			exerciseWithJPA(loggingConfig, entityManagerFactory, entityManager);
		}finally{
			// ///////////////////////////////////////////////////////////////////////
			// Free resources used by the JPA entity manager and entity manager factory
			JPAResourceHandler.close(entityManager);
			// Necessary with JPA 2.1 / Hibernate 5.2.2
			JPAResourceHandler.close(entityManagerFactory);
		}		
	}
	
	public static void exerciseWithJPA(JpaUtil.HibernateLoggingConfig loggingConfig, 
			EntityManagerFactory entityManagerFactory, EntityManager entityManager){
		
		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Create and persist some persons");
		// Begin a new JTA transaction
				
		entityManager.getTransaction().begin();
		
		// Persist is used to save an entity instance
		Person person = new Person("Bilbo");
		
		// At this moment Bilbo is in-memory only (will dissappear at shutdown)
		entityManager.persist(person);
		
		// Now Bilbo is made persistent, which means, that he will stay in the
		// database after the Java program has shutdown.
		// Let's make more person entities and save them to the database.
		entityManager.persist(new Person("Frodo"));
		entityManager.persist(new Person("Sam"));
		entityManager.persist(new Person("Merry"));
		entityManager.persist(new Person("Pippin"));
		
		// Try to commit the current transaction (the commit might fail which will
		// result in the changes made in the transaction will be rolled back and an
		// exception will be thrown)
		entityManager.getTransaction().commit();
		
		// Clear cached entities from the entityManager (called managed entities
		// in JPA). We will talk more about what "managed" means later on...
		entityManager.clear();

		
		loggingConfig.setLogSqlEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);
		
		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Find all persistent persons");
		entityManager.getTransaction().begin();
		
		// Notice the similarity to SQL. Also notice that we can select the
		// full entity object, and not just its fields one by one (as in SQL).
		String jpaql = "select p from Person p";
		
		// A Query is similar to a Statements in JDBC
		TypedQuery<Person> query = entityManager.createQuery(jpaql, Person.class);
		
		// Execute the query and get the result.
		// The returned list is like getting a ResultSet in JDBC.
		List<Person> resultList = query.getResultList();
		JpaUtil.prettyPrintQueryResult(jpaql, resultList);
		entityManager.getTransaction().commit();
		entityManager.clear();

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Use JPA QL count aggregate function");
		entityManager.getTransaction().begin();
		
		// Notice the use of the count-function
		// Notice the TypedQuery, which allows for type safe code without downcasts
		jpaql = "select count(p) from Person p";
		TypedQuery<Long> countQuery = entityManager.createQuery(jpaql, Long.class);
		Long personCount = countQuery.getSingleResult();

		JpaUtil.prettyPrintQueryResult(jpaql, personCount);
		entityManager.getTransaction().commit();
		entityManager.clear();
		

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Use like in select");
		entityManager.getTransaction().begin();
		
		// % is any number of chars and _ is a single char (as in SQL)
		jpaql = "select p from Person p where p.name like 'M%y'";
		query = entityManager.createQuery(jpaql, Person.class);
		resultList = query.getResultList();
		JpaUtil.prettyPrintQueryResult(jpaql, resultList);
		entityManager.getTransaction().commit();
		entityManager.clear();

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Use named parameter in JPA QL");
		entityManager.getTransaction().begin();
		
		// Use colon to define a named parameter
		jpaql = "select p from Person p where p.name like :namePattern";
		query = entityManager.createQuery(jpaql, Person.class);
		
		// Set the value of the named parameter (don't include the colon)
		query.setParameter("namePattern", "%o%");
		System.out.println("Has set JPA QL parameter [:namePattern] to [%o%]");
		resultList = query.getResultList();
		JpaUtil.prettyPrintQueryResult(jpaql, resultList);
		entityManager.getTransaction().commit();
		entityManager.clear();

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Find by entity type and entity primary key");
		entityManager.getTransaction().begin();
		
		// We can find an entity without any JPA QL if we know its type
		// (e.g. Person.class) and the value of its primary key (here 2 using
		// autoboxing to convert from int to Integer which is the pk type of
		// Person ).
		Person personJens = entityManager.find(Person.class, 4);
		JpaUtil.prettyPrintQueryResult("Using find(entityType, entityPrimaryKey)", personJens);
		entityManager.getTransaction().commit();
		entityManager.clear();

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Removing an entity");
		entityManager.getTransaction().begin();
		Person personToDelete = entityManager.find(Person.class, 4);
		
		// The remove method is one way of deleting entity instances from the db
		entityManager.remove(personToDelete);
		System.out.println("Has removed Merry");
		jpaql = "select p from Person p";
		query = entityManager.createQuery(jpaql, Person.class);
		resultList = query.getResultList();
		JpaUtil.prettyPrintQueryResult(jpaql, resultList);
		entityManager.getTransaction().commit();
		entityManager.clear();

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Updating an entity");
		entityManager.getTransaction().begin();
		Person personBeforeEdit = entityManager.find(Person.class, 1);
		JpaUtil.prettyPrintQueryResult("Person before edit", personBeforeEdit);
		
		// One way of editing an entity is to load it from the database
		// and then edit the object directly. JPA will automatically
		// detect the change and write the changed entity state to the
		// database at commit time. This only works if the entity is
		// managed (we will talk about that later on).
		personBeforeEdit.setName("Bilbo Baggins");
		Person personAfterEdit = entityManager.find(Person.class, 1);
		JpaUtil.prettyPrintQueryResult("Person after edit", personAfterEdit);
		entityManager.getTransaction().commit();
		entityManager.clear();

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Use paging to go through huge result chunk by chunk");
		entityManager.getTransaction().begin();
		
		// Make more persons to make paging more fun!
		entityManager.persist(new Person("Aragorn"));
		entityManager.persist(new Person("Legolas"));
		entityManager.persist(new Person("Gimli"));
		entityManager.persist(new Person("Gandalf"));
		entityManager.getTransaction().commit();
		entityManager.clear();

		// How many persons exist in the database?
		entityManager.getTransaction().begin();
		jpaql = "select count(p) from Person p";
		countQuery = entityManager.createQuery(jpaql, Long.class);
		personCount = countQuery.getSingleResult();
		entityManager.getTransaction().commit();
		entityManager.clear();

		// How many pages should we split the result in?
		int totalSize = personCount.intValue();
		int pageSize = 3;
		double noOfPagesAsDouble = ((double) totalSize) / pageSize;
		int noOfPages = (int) Math.ceil(noOfPagesAsDouble);
		
		// Go through all the persons chunk by chunk
		jpaql = "select p from Person p";
		query = entityManager.createQuery(jpaql, Person.class);
		
		// We only want 3 persons at a time
		query.setMaxResults(pageSize);
		for (int n = 0; n < noOfPages; n++) {
			entityManager.getTransaction().begin();
			
			// The index of the first of the 3 persons we want
			query.setFirstResult(n * pageSize);
			resultList = query.getResultList();
			System.out.println("Page[" + n + "] -> " + resultList);
			entityManager.getTransaction().commit();
			entityManager.clear();
		}		

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("You might want to take a look at the database.");
		System.out.println("1) Connect to JpaTest database by starting the Squill Sql Tool.");
		System.out.println("2) Make a connention based on the connect info in the persistence.xml");
		System.out.println("3) Expand the tree under JPATESTUSERNAME");
		System.out.println("4) Navigate to the Person table and select the contents pane.");
		
	}
}