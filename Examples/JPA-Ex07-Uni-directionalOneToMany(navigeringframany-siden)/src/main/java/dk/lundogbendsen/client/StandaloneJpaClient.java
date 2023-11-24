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

		StringUtil.prettyPrintHeadline("Create 2 owners and 3 cars");
		entityManager.getTransaction().begin();
		Person thomas = new Person("Thomas");
		entityManager.persist(thomas);
		Person lise = new Person("Lise");
		entityManager.persist(lise);

		Car skoda = new Car("Skoda");
		entityManager.persist(skoda);
		Car opel = new Car("Opel");
		entityManager.persist(opel);
		Car fiat = new Car("Fiat");
		entityManager.persist(fiat);

		String jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Associate owners and cars");
		entityManager.getTransaction().begin();
		skoda.setOwner(thomas);
		opel.setOwner(thomas);
		fiat.setOwner(lise);

		jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////

		StringUtil.prettyPrintHeadline("Here it is not even possible to violate referential integrity");
		entityManager.getTransaction().begin();
		skoda.setOwner(lise);

		jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		entityManager.getTransaction().commit();
	}
}