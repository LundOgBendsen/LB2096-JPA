package dk.lundogbendsen.client;

import java.util.List;

import dk.lundogbendsen.dao.PersonDaoImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.log4j.Level;

import dk.lundogbendsen.model.Person;
import dk.lundogbendsen.jpa.util.EntityPager;
import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;

public class StandaloneJpaClient {

	public static void main(String[] args) {

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
		
		// Instantiate DAO
		PersonDaoImpl personRepository = new PersonDaoImpl();

		// Tell the DAO which entity manager to use
		personRepository.setEntityManager(entityManager);

		// Start new TX on the entity manager the DAO uses. Notice how we can
		// control the transactions to span one or more DAO method invocations.
		entityManager.getTransaction().begin();
		personRepository.persist(new Person("Bilbo"));
		personRepository.persist(new Person("Frodo"));
		personRepository.persist(new Person("Sam"));
		personRepository.persist(new Person("Gandalf"));
		personRepository.persist(new Person("Aragorn"));
		personRepository.persist(new Person("Legolas"));
		personRepository.persist(new Person("Gimli"));
		entityManager.getTransaction().commit();

		// Find AND remove person in one tx
		entityManager.getTransaction().begin();
		Person p = personRepository.findPerson(2);
		personRepository.removePerson(p);
		entityManager.getTransaction().commit();
		
		// The DAO can support avanced helpers like the pager below
		entityManager.getTransaction().begin();
		EntityPager<Person> personPager = personRepository.findAllPersonsAsPager(2);
		while (personPager.hasNextPage()) {
			System.out.println(personPager.nextPage(entityManager));
//			System.out.println(personPager);
			System.out.println();
		}
		entityManager.getTransaction().commit();

		
		// Lab02 Utilize existing PersonRepositoryDAO
		long personCount = personRepository.countPersons();
		StringUtil.prettyPrintHeadline("Number of persons: " + personCount);
		
		List<Person> name = personRepository.findAllWithName("Gandalf");
		StringUtil.prettyPrintHeadline("Persons named Gandalf:" + name);
		
		Person no1 = personRepository.findPerson(1);
		StringUtil.prettyPrintHeadline("Person with id 1:" + no1);
		
		//Lab02 countPersonsWithName
		long noOfWizzards = personRepository.countPersonsWithName("Gandalf");
		StringUtil.prettyPrintHeadline("No of persons named Gandalf: " + noOfWizzards);
		
		entityManager.getTransaction().begin();
		
		//Lab02 removePersonWithId
		StringUtil.prettyPrintHeadline("Removing Gandalf");
		personRepository.removePersonWithId(4);
		
		List<Person> allPersons = personRepository.findAllPersons(1, (int)personCount);
		StringUtil.prettyPrintHeadline("All persons: " + allPersons);
		
		entityManager.getTransaction().commit();
		
		
		//Lab02 Testing CarDAO
		
		}
}