package lundogbendsen.client;

import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import lundogbendsen.model.Car;
import lundogbendsen.model.PersonWithCar;
import lundogbendsen.model.PersonWithPhone;
import lundogbendsen.model.Phone;
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
		StringUtil.prettyPrintHeadline("Create 2 owners and 3 cars");
		entityManager.getTransaction().begin();

		PersonWithCar thomas = new PersonWithCar("Thomas");
		entityManager.persist(thomas);
		PersonWithCar lise = new PersonWithCar("Lise");
		entityManager.persist(lise);

		Car skoda = new Car("Skoda");
		entityManager.persist(skoda);
		Car opel = new Car("Opel");
		entityManager.persist(opel);
		Car fiat = new Car("Fiat");
		entityManager.persist(fiat);

		String jpaql = "select p from PersonWithCar p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Associate owners and cars");
		entityManager.getTransaction().begin();

		loggingConfig.setLogSqlEnabled(true);
		JpaUtil.configureHibernateLogging(loggingConfig);

		thomas.addOwnedCar(2006, skoda);
		thomas.addOwnedCar(2010, opel);
		lise.addOwnedCar(2008, fiat);

		jpaql = "select p from PersonWithCar p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		entityManager.getTransaction().commit();
		loggingConfig.setLogSqlEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);

		// /////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////


		StringUtil.prettyPrintHeadline("Create new PersonWithPhone and give him 2 phones");
		entityManager.getTransaction().begin();

		loggingConfig.setLogSqlEnabled(true);
		loggingConfig.setLogParameterBindingEnabled(true);
		JpaUtil.configureHibernateLogging(loggingConfig);

		PersonWithPhone klaus = new PersonWithPhone("Klaus");

		entityManager.persist(klaus);
		entityManager.flush();

		Phone phone1 = new Phone("01234567");
		Phone phone2 = new Phone("765432d10");

		entityManager.persist(phone1);
		entityManager.persist(phone2);

		klaus.addOwnedPhone("private", phone1);
		klaus.addOwnedPhone("work", phone2);

		entityManager.getTransaction().commit();

		loggingConfig.setLogDdlEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);

		entityManager.clear();

		jpaql = "select p from PersonWithPhone p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select p from Phone p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
	}
}