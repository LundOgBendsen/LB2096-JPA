package test.client.standalone;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.log4j.Level;

import test.dao.PersonRepositoryImpl;
import test.model.Person;
import dk.lundogbendsen.jpa.util.EntityPager;
import dk.lundogbendsen.jpa.util.JPAResourceHandler;
import dk.lundogbendsen.jpa.util.JpaUtil;

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
		
		// Instantiate DAO
		PersonRepositoryImpl personRepository = new PersonRepositoryImpl();

		// Tell the DAO which entity manager to use
		personRepository.setEntityManager(entityManager);

		// Start new TX on the entity manager the DAO uses. Notice how we can
		// control the transactions to span one or more DAO method invocations.
		entityManager.getTransaction().begin();
		personRepository.persist(new Person("Ole"));
		personRepository.persist(new Person("Charlotte"));
		personRepository.persist(new Person("Inga"));
		personRepository.persist(new Person("Oluf"));
		personRepository.persist(new Person("Søren"));
		personRepository.persist(new Person("Jesper"));
		personRepository.persist(new Person("Lisbeth"));
		entityManager.getTransaction().commit();

		// Find AND remove person in one tx
		entityManager.getTransaction().begin();
		Person p = personRepository.findPerson(2);
		personRepository.removePerson(p);
		entityManager.getTransaction().commit();
		
		// The DAO can support avanced helpers like the pager below
		entityManager.getTransaction().begin();
		EntityPager<Person> personPager = personRepository.findAllPersonsAsPager(2);
		System.out.println(personPager);
		System.out.println();
		while (personPager.hasNextPage()) {
			System.out.println(personPager.nextPage(entityManager));
			System.out.println(personPager);
			System.out.println();
		}
		entityManager.getTransaction().commit();
	}

}