package dk.lundogbendsen.client;

import java.util.SortedMap;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.log4j.Level;

import dk.lundogbendsen.model.Bird;
import dk.lundogbendsen.model.Dog;
import dk.lundogbendsen.model.Person;
import dk.lundogbendsen.model.Pet;
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
		
		JpaUtil.PersistenceConfig persistenceConfig = new JpaUtil.PersistenceConfig();
	/*
	persistenceConfig.useMySqlJdbcDriver();
		persistenceConfig.useMySqlDialect();
		persistenceConfig.createAndSetMySqlUrl("localhost", "mysql_test_db");
		persistenceConfig.setConnectionUsername("mysql_username");
		persistenceConfig.setConnectionPassword("mysql_password");
		SortedMap<String, String> configProperties = persistenceConfig.getConfigProperties();
		*/
		persistenceConfig.useDerbyJdbcClientDriver();
		persistenceConfig.useDerbyDialect();
		persistenceConfig.createAndSetDerbyUrl("localhost", 1527, "JpaTest", true); // jdbc:derby://localhost:1527/JpaTest
		persistenceConfig.setConnectionUsername("JpaTestUserName");
		persistenceConfig.setConnectionPassword("JpaTestPassword");
		SortedMap<String, String> configProperties = persistenceConfig.getConfigProperties();

		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("StandaloneJpaTestPersistenceUnit", configProperties);
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Create 2 owners and 4 pets");
		entityManager.getTransaction().begin();
		Person thomas = new Person("Thomas");
		entityManager.persist(thomas);
		Person lise = new Person("Lise");
		entityManager.persist(lise);

		Pet poppe = new Bird(1, "Poppe", false);
		entityManager.persist(poppe);
		Pet andrea = new Bird(2, "Andrea", true);
		entityManager.persist(andrea);
		Pet emma = new Dog(3, "Emma", false);
		entityManager.persist(emma);
		Pet anton = new Dog(4, "Anton", false);
		entityManager.persist(anton);

		entityManager.getTransaction().commit();

		String jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select p from Pet p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		
		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Associate owners and pets");
		entityManager.getTransaction().begin();
		thomas.addOwnedPet(poppe);
		lise.addOwnedPet(andrea);
		lise.addOwnedPet(emma);

		jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select p from Pet p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		
		entityManager.getTransaction().commit();
		
		// ///////////////////////////////////////////////////////////////////////
		// Free resources used by the JPA entity manager and entity manager factory
		// (should have been done in a finally block to make sure close is called 
		// even if we get an exception)
		entityManager.close();
		// Necessary with JPA 2.1 / Hibernate 5.2.2
		entityManagerFactory.close();
	}
}