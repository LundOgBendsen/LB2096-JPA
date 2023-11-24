package dk.lundogbendsen.client;

import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import dk.lundogbendsen.model.Product;
import dk.lundogbendsen.model.ProductId;
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

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Create and persist a few products");
		entityManager.getTransaction().begin();
		Product samsungScreen19 = new Product("Samsung", 2341, "19\" Screen");
		entityManager.persist(samsungScreen19);
		Product samsungScreen32 = new Product("Samsung", 23412, "32\" Screen");
		entityManager.persist(samsungScreen32);
		Product sonyScreen32 = new Product("Sony", 23412, "32\" Screen");
		entityManager.persist(sonyScreen32);
		entityManager.getTransaction().commit();
		entityManager.clear();

		// ///////////////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Find a product using its id");
		entityManager.getTransaction().begin();

		Product result = entityManager.find(Product.class, new ProductId("Samsung", 2341));
		JpaUtil.prettyPrintQueryResult("find(Product.class, new ProductId(\"Samsung\", 2341))", result);
		entityManager.getTransaction().commit();
		entityManager.clear();
	}
}