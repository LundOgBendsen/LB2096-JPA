package dk.lundogbendsen.client;

import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import dk.lundogbendsen.model.Car;
import dk.lundogbendsen.model.Person;
import org.apache.log4j.Level;

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

		Person thomas = new Person("Thomas");
		Person lise = new Person("Lise");

		Car skoda = new Car("Skoda");
		Car opel = new Car("Opel");
		Car fiat = new Car("Fiat");

		thomas.addOwnedCar(skoda);
		thomas.addOwnedCar(opel);
		lise.addOwnedCar(fiat);

		StringUtil.prettyPrintHeadline("Begynder persistering af personer");

		// Slå logging af SQL til
		loggingConfig.setLogSqlEnabled(true);
		JpaUtil.configureHibernateLogging(loggingConfig);

		entityManager.getTransaction().begin();

		entityManager.persist(thomas);
		entityManager.persist(lise);
		// We don't need these calls anymore:
		// entityManager.persist(skoda);
		// entityManager.persist(opel);
		// entityManager.persist(fiat);

		entityManager.getTransaction().commit();

		// Slå logging af SQL fra igen
		loggingConfig.setLogSqlEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);

		StringUtil.prettyPrintHeadline("Færdig med persistering af personer");

		// Vi sletter lige alle in-memory cachede objekter
		entityManager.clear();
		entityManager.getTransaction().begin();

		String jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		entityManager.getTransaction().commit();
	}
}