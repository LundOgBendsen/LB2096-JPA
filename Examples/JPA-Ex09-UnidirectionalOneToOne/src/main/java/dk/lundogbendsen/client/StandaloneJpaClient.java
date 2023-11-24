package dk.lundogbendsen.client;

import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import dk.lundogbendsen.model.Car;
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

		StringUtil.prettyPrintHeadline("Connect toyota to 4-cyl engine and opel to 6-cyl engine");

		entityManager.getTransaction().begin();

		Car toyota = new Car("Toyota", 4);
		entityManager.persist(toyota);
		// Vi er nødt til også at gemme engine
		// Alternativt kunne man annotere Car.engine med
		// @OneToOne(cascade=CascadeType.PERSIST)
		// Hvilket ville propagere save af car til dens engine
		entityManager.persist(toyota.getEngine());
		Car opel = new Car("Opel", 6);
		entityManager.persist(opel);
		entityManager.persist(opel.getEngine());
		entityManager.getTransaction().commit();

		String jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select e from Engine e";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		entityManager.clear();
	}
}