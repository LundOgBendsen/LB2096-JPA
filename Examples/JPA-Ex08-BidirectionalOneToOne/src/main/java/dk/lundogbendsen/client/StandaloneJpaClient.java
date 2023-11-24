package dk.lundogbendsen.client;

import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import dk.lundogbendsen.model.Car;
import dk.lundogbendsen.model.Engine;
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

		Car toyota = new Car("Toyota");
		entityManager.persist(toyota);
		Car opel = new Car("Opel");
		entityManager.persist(opel);

		Engine engineWith4cyls = new Engine(4);
		entityManager.persist(engineWith4cyls);
		Engine engineWith6cyls = new Engine(6);
		entityManager.persist(engineWith6cyls);

		toyota.setEngine(engineWith4cyls);
		opel.setEngine(engineWith6cyls);
		entityManager.getTransaction().commit();

		String jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select e from Engine e";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

		entityManager.getTransaction().begin();
		StringUtil.prettyPrintHeadline("Connect toyota to 6-cyl engine");
		toyota.setEngine(engineWith6cyls);
		entityManager.getTransaction().commit();
		// entityManager.clear();

		entityManager.getTransaction().begin();
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select e from Engine e";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		entityManager.getTransaction().commit();

		entityManager.clear();
	}
}