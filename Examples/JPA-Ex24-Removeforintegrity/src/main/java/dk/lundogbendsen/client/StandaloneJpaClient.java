package dk.lundogbendsen.client;

import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
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

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Create 2 owners of 3 cars");
		entityManager.getTransaction().begin();
		Person thomas = new Person("Thomas");
		entityManager.persist(thomas);
		Person lise = new Person("Lise");
		entityManager.persist(lise);

		Car skoda = new Car("Skoda");
		entityManager.persist(skoda);
		thomas.addOwnedCar(skoda);
		Car opel = new Car("Opel");
		entityManager.persist(opel);
		thomas.addOwnedCar(opel);
		Car fiat = new Car("Fiat");
		entityManager.persist(fiat);
		lise.addOwnedCar(fiat);

		String jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Removing Thomas (should set owner of his cars to null)");
		entityManager.getTransaction().begin();
		entityManager.remove(thomas);
		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////
		entityManager.getTransaction().begin();
		jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Removing Fiat (should remove ref from lises ownedCars collection)");
		entityManager.getTransaction().begin();
		entityManager.remove(fiat);
		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////
		entityManager.getTransaction().begin();
		jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		entityManager.getTransaction().commit();
	}
}