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
		StringUtil.prettyPrintHeadline("Create owner of 3 cars");
		entityManager.getTransaction().begin();
		Person thomas = new Person("Thomas");
		entityManager.persist(thomas);

		Car skoda = new Car("Skoda");
		entityManager.persist(skoda);
		thomas.addOwnedCar(skoda);
		Car opel = new Car("Opel");
		entityManager.persist(opel);
		thomas.addOwnedCar(opel);
		Car fiat = new Car("Fiat");
		entityManager.persist(fiat);
		thomas.addOwnedCar(fiat);

		String jpaql = "select p from Person p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select c from Car c";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Clear entity context to make sure it contains no managed entities");
		System.out.println("Entities before clear: " + JpaUtil.getEntityCount(entityManager));
		entityManager.clear();
		System.out.println("Entities after clear: " + JpaUtil.getEntityCount(entityManager));

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Select Thomas and count entities in persistence context");
		entityManager.getTransaction().begin();

		loggingConfig.setLogSqlEnabled(true);
		JpaUtil.configureHibernateLogging(loggingConfig);

		System.out.println("Entities before selecting thomas: " + JpaUtil.getEntityCount(entityManager));

		jpaql = "select p from Person p where p.name = 'Thomas'";
		thomas = (Person) entityManager.createQuery(jpaql).getSingleResult();
		JpaUtil.prettyPrintQueryResult(jpaql, thomas);

		System.out.println("Entities after selecting thomas: " + JpaUtil.getEntityCount(entityManager));
		System.out.println("As we have set eager fetch on Person.ownedCars - so Thomas' Cars are read");

		loggingConfig.setLogSqlEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);
		// /////////////////////////////////////////////////////////////

		StringUtil.prettyPrintHeadline("Select Fiat and count entities in persistence context");
		entityManager.clear();

		System.out.println("Entities before selecting Fiat: " + JpaUtil.getEntityCount(entityManager));

		jpaql = "select c from Car c where c.make = 'Fiat'";
		fiat = (Car) entityManager.createQuery(jpaql).getSingleResult();
		JpaUtil.prettyPrintQueryResult(jpaql, fiat);

		System.out.println("Entities after selecting Fiat: " + JpaUtil.getEntityCount(entityManager));
		System.out.println("As Car.owner is @ManyToOne its default fetch type is EAGER - so Thomas is read eagerly");
		System.out.println("and as Person.ownedCars has fetchType set to EAGER Thomas collection of cars are also fetched.");


		entityManager.getTransaction().commit();
	}
}