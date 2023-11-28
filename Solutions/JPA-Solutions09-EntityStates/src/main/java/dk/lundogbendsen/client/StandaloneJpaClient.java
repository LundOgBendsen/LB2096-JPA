package dk.lundogbendsen.client;

import dk.lundogbendsen.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.log4j.Level;
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

			//work here!
			StringUtil.prettyPrintHeadline("Not exist -> New");
			Person person = new Person("Alice");
			boolean isPersonManaged = entityManager.contains(person);
			System.out.println("isPersonManaged: " + isPersonManaged);

			StringUtil.prettyPrintHeadline("New -> Managed");
			entityManager.getTransaction().begin();
			entityManager.persist(person);
			entityManager.getTransaction().commit();
			isPersonManaged = entityManager.contains(person);
			System.out.println("isPersonManaged: " + isPersonManaged);
			// We'll need the ID below
			Integer personId = person.getId();


			StringUtil.prettyPrintHeadline("Managed -> Detached");
			entityManager.clear();
			isPersonManaged = entityManager.contains(person);
			System.out.println("isPersonManaged: " + isPersonManaged);


			StringUtil.prettyPrintHeadline("Detached -> Managed");
			person = entityManager.merge(person); //different object, technically:-)
			isPersonManaged = entityManager.contains(person);
			System.out.println("isPersonManaged: " + isPersonManaged);


			StringUtil.prettyPrintHeadline("Managed -> Detached again");
			entityManager.clear();
			isPersonManaged = entityManager.contains(person);
			System.out.println("isPersonManaged: " + isPersonManaged);


			StringUtil.prettyPrintHeadline("Person only exists in database now");
			person = null;
			// Ask jvm to garbage collect
			System.gc();

			StringUtil.prettyPrintHeadline("Only in database -> Managed");
			person = entityManager.find(Person.class, personId);
			isPersonManaged = entityManager.contains(person);
			System.out.println("isPersonManaged: " + isPersonManaged);

			StringUtil.prettyPrintHeadline("Managed -> Removed");
			entityManager.getTransaction().begin();
			entityManager.remove(person);
			entityManager.getTransaction().commit();
			Person person2 = person; //save for later
			person = entityManager.find(Person.class, personId);
			System.out.println("Found person: = " + person);

			StringUtil.prettyPrintHeadline("Removed -> Managed");
			entityManager.getTransaction().begin();
			//we cannot use the previously loaded person (person2) since it already has and Id thus cannot be persisted.
			//entityManager.persist(person2);   //throws jakarta.persistence.EntityExistsException
			entityManager.persist(new Person("Alice")); //the best we can do in order to restore Alice
			entityManager.getTransaction().commit();
			person = entityManager.find(Person.class, personId);
			System.out.println("Found person: = " + person);
		}
	}
}