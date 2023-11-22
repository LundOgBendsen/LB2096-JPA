package dk.lundogbendsen.client;

import dk.lundogbendsen.dao.PersonRepositoryImpl;
import dk.lundogbendsen.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.log4j.Level;

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
		// JPA supports AutoCloseable, so we use try-with-resources
		try(EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("StandaloneJpaTestPersistenceUnit");
			EntityManager entityManager = entityManagerFactory.createEntityManager()) {
			exerciseWithJPA(loggingConfig, entityManagerFactory, entityManager);
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
	}
}