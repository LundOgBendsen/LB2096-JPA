package lundogbendsen.client;

import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import lundogbendsen.model.Car;
import lundogbendsen.model.Person;
import org.apache.log4j.Level;

import java.util.Collection;
import java.util.List;

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

		StringUtil.prettyPrintHeadline("Create owner of 3 cars");
		entityManager.getTransaction().begin();
		Person thomas = new Person("Thomas");
		entityManager.persist(thomas);
		Person joe = new Person("Joe");
		entityManager.persist(joe);
		Person lars = new Person("Lars");
		entityManager.persist(lars);


		Car skoda = new Car("Skoda");
		entityManager.persist(skoda);
		lars.addOwnedCar(skoda);
		Car opel = new Car("Opel Kadett");
		entityManager.persist(opel);
		thomas.addOwnedCar(opel);
		Car fiat = new Car("Fiat");
		entityManager.persist(fiat);
		joe.addOwnedCar(fiat);

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

		System.out.println("Entities before selecting thomas: " + JpaUtil.getEntityCount(entityManager));

		jpaql = "select p from Person p where p.name = 'Thomas'";
		thomas = (Person) entityManager.createQuery(jpaql).getSingleResult();
		JpaUtil.prettyPrintQueryResult(jpaql, thomas);

		System.out.println("Entities after selecting thomas: " + JpaUtil.getEntityCount(entityManager));
		System.out.println("As Person.ownedCars is @OneToMany its default fetch type is LAZY - so no Cars is read");

		entityManager.getTransaction().commit();

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Clear entity context to make sure thomas is detached");
		System.out.println("Entities before clear: " + JpaUtil.getEntityCount(entityManager));
		entityManager.clear();
		System.out.println("Entities after clear: " + JpaUtil.getEntityCount(entityManager));

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Try to access detached Thomas' cars (which wasn't materialized)");

		try {
			Collection<Car> ownedCars = thomas.getOwnedCars();
			for (Car car : ownedCars) {
				System.out.println(car);
			}
		} catch (Exception e) {
			System.out.println("We got an exception as expected");
		}

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Select Fiat and count entities in persistence context");
		entityManager.getTransaction().begin();

		loggingConfig.setLogSqlEnabled(true);
		JpaUtil.configureHibernateLogging(loggingConfig);

		System.out.println("Entities before selecting Fiat: " + JpaUtil.getEntityCount(entityManager));

		jpaql = "select c from Car c";
		List<Car> cars = entityManager.createQuery(jpaql, Car.class).getResultList();
		JpaUtil.prettyPrintQueryResult(jpaql, cars);

		System.out.println("Entities after selecting Fiat: " + JpaUtil.getEntityCount(entityManager));
		System.out.println("As Car.owner is @ManyToOne its default fetch type is EAGER - so Thomas is read eagerly - but in separate select");

		entityManager.getTransaction().commit();

		loggingConfig.setLogSqlEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Clear entity context to make sure fiat is detached");
		System.out.println("Entities before clear: " + JpaUtil.getEntityCount(entityManager));
		entityManager.clear();
		System.out.println("Entities after clear: " + JpaUtil.getEntityCount(entityManager));

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Try to access detached fiat'a owner (which was eagerly read)");

		try {
			System.out.println("We were able to access fiat's owner: " + fiat.getOwner());
		} catch (Exception e) {
			// Should not happen
			System.out.println("We got an unexpected expected");
		}

		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Clear entity context to make sure fiat is detached");
		System.out.println("Entities before clear: " + JpaUtil.getEntityCount(entityManager));
		entityManager.clear();
		System.out.println("Entities after clear: " + JpaUtil.getEntityCount(entityManager));
		// /////////////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Find Fiat and count entities in persistence context");
		entityManager.getTransaction().begin();

		loggingConfig.setLogSqlEnabled(true);
		JpaUtil.configureHibernateLogging(loggingConfig);

		System.out.println("Entities before finding Fiat: " + JpaUtil.getEntityCount(entityManager));

		fiat = entityManager.find(Car.class, 3);
		JpaUtil.prettyPrintQueryResult(jpaql, fiat);

		System.out.println("Entities after finding Fiat: " + JpaUtil.getEntityCount(entityManager));
		System.out.println("As Car.owner is @ManyToOne its default fetch type is EAGER - so Thomas is read eagerly - this time by SQL joins!!!!");

		entityManager.getTransaction().commit();

		loggingConfig.setLogSqlEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);
	}
}