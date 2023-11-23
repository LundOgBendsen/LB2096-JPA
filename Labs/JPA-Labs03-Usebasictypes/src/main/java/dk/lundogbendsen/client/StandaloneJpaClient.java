package dk.lundogbendsen.client;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import dk.lundogbendsen.model.Address;
import dk.lundogbendsen.model.Person;
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

		//work in this method during the labs

		entityManager.getTransaction().begin();

		Person hobbit = new Person("Bilbo Baggins");


		entityManager.persist(hobbit);

		entityManager.getTransaction().commit();
		
		entityManager.clear();

		entityManager.getTransaction().begin();
		Person dbHobbit = entityManager.find(Person.class, 1);
		System.out.println(dbHobbit);
		entityManager.getTransaction().commit();


		Query q = entityManager.createQuery("from Person");
		List resultList = q.getResultList();
		System.out.println(resultList);
	}
}