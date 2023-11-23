package dk.lundogbendsen.client;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import dk.lundogbendsen.model.Address;
import dk.lundogbendsen.model.Person;
import dk.lundogbendsen.model.PersonWithCprAsId;
import jakarta.persistence.*;

import org.apache.log4j.Level;

import dk.lundogbendsen.jpa.util.JpaUtil;

public class StandaloneJpaClient {

	public static void main(String[] args) {
		System.out.println(new File(".").getAbsolutePath());
		JpaUtil.HibernateLoggingConfig loggingConfig = new JpaUtil.HibernateLoggingConfig();
		loggingConfig.setHibernateRootLogLevel(Level.WARN);
		loggingConfig.setLogDdlEnabled(true);
		loggingConfig.setLogSqlEnabled(false);
		loggingConfig.setLogParameterBindingEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);

		try(EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("StandaloneJpaTestPersistenceUnit");
			EntityManager entityManager = entityManagerFactory.createEntityManager()){
			// Create new JPA entity manager (similar to JDBC Connection)
			
			exerciseWithJPA(loggingConfig, entityManagerFactory, entityManager);
		}
			
	}
	
	public static void exerciseWithJPA(JpaUtil.HibernateLoggingConfig loggingConfig, 
			EntityManagerFactory entityManagerFactory, EntityManager entityManager){
		
		entityManager.getTransaction().begin();

		Person hobbit = new Person("Bilbo Baggins");
		hobbit.setHeight(0.79);
		hobbit.setAddress(new Address("Bag End", 124));
		hobbit.setGender(Person.Gender.MALE);
		hobbit.setCprNo("210190-0111");



		hobbit.setBirthDate(LocalDate.of(1202,1,23));
		
		hobbit.readPhotoFromResource("./photo.jpg");
		entityManager.persist(hobbit);
		entityManager.getTransaction().commit();
		
		entityManager.clear();
		entityManager.getTransaction().begin();
		Person dbHobbit = entityManager.find(Person.class, 1);
		System.out.println(dbHobbit);
		dbHobbit.writePhotoToFile(new File("dbPhoto.jpg"));
		entityManager.getTransaction().commit();
		
		entityManager.getTransaction().begin();

		// Test Person with manually set CPR-no. as id
		PersonWithCprAsId personWithCprAsId = new PersonWithCprAsId("101189-2340", "Lise");
		entityManager.persist(personWithCprAsId);
		System.out.println(entityManager.find(PersonWithCprAsId.class, "101189-2340"));

		entityManager.getTransaction().commit();

		//print all persons, verify all properties are there
		Query q = entityManager.createQuery("from Person");
		List resultList = q.getResultList();
		System.out.println(resultList);


		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();

		try {
			PersonWithCprAsId personWithNullAsId = new PersonWithCprAsId(null, "Lise");
			entityManager.persist(personWithNullAsId);
			tx.commit();
		} catch (PersistenceException e) {
			System.out.println("We got an expected PersistenceException because id was null");
			// When entity manager throws an exception, it will automatically
			// rollback the tx
			System.out.println("Was the tx automatically set to rollback? " + tx.getRollbackOnly());
		}
	}
}