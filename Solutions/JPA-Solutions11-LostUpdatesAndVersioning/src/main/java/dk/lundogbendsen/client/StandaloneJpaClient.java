package dk.lundogbendsen.client;

import dk.lundogbendsen.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
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
			EntityManager emOfClient1 = entityManagerFactory.createEntityManager();
			EntityManager emOfClient2 = entityManagerFactory.createEntityManager()){

			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("We make two different entity managers to simulate two different clients");

			// ///////////////////////////////////////////////////////////////////////
			emOfClient1.getTransaction().begin();
			StringUtil.prettyPrintHeadline("Make and persist a Person to work with");
			emOfClient1.persist(new Person("Hugo"));
			emOfClient1.getTransaction().commit();

			String jpaql = "select p from Person p";
			Person p = (Person)emOfClient1.createQuery(jpaql).getSingleResult();
			JpaUtil.prettyPrintQueryResult(jpaql, p);

			StringUtil.prettyPrintHeadline("Get the id of person for latter use");
			Integer personId = p.getId();
			System.out.println("The id of the Person is: " + personId);
			emOfClient1.clear();

			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("Let client-1 retrieve and detach copy of the person");
			Person personOwnedByClient1 = emOfClient1.find(Person.class, personId);
			System.out.println("Client-1 has retrieved " + personOwnedByClient1);

			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("Let client-2 retrieve and detach copy of the person");
			Person personOwnedByClient2 = emOfClient2.find(Person.class, personId);
			System.out.println("Client-2 has retrieved " + personOwnedByClient2);

			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("Let client-1 change his detached copy of person");
			personOwnedByClient1.setName("Hugo Jensen");
			System.out.println("Client-1 has changed in-memory copy of person " + personOwnedByClient1);

			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("Let client-2 change her detached copy of person");
			personOwnedByClient2.setName("Hugo Madsen");
			System.out.println("Client-2 has changed in-memory copy of person " + personOwnedByClient2);

			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("Let client-1 merge his changes to the database");
			emOfClient1.getTransaction().begin();
			emOfClient1.merge(personOwnedByClient1);
			emOfClient1.getTransaction().commit();


			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("The content of the database after client-1 has merged");
			// Remember to clear the cache of client-1's entity manager
			emOfClient1.clear();
			jpaql = "select p from Person p";
			JpaUtil.prettyPrintQueryResult(jpaql, emOfClient1.createQuery(jpaql).getSingleResult());

			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("Now let client-2 merge her changes to the database");
			try {
				emOfClient2.getTransaction().begin();
				emOfClient2.merge(personOwnedByClient2);
				emOfClient2.getTransaction().commit();  //throws exception due to version conflict
			} catch (RollbackException re) {
				/////start of solution to lab 11
				emOfClient2.getTransaction().begin();
					//load a fresh version of the person from database, incl a new version number
					personOwnedByClient2 = emOfClient2.find(Person.class, personId);
					emOfClient2.clear(); //simulate detachment
					// here you may check the state of the entity and decide not to change it.
					personOwnedByClient2.setName("Hugo Madsen");
					emOfClient2.merge(personOwnedByClient2);
				emOfClient2.getTransaction().commit();
				/////end of solution to lab 11
			}
			// ///////////////////////////////////////////////////////////////////////
			StringUtil.prettyPrintHeadline("The content of the database after client-2 has merged");
			// Remember to clear the cache of client-1's entity manager
			emOfClient1.clear();
			jpaql = "select p from Person p";
			JpaUtil.prettyPrintQueryResult(jpaql, emOfClient1.createQuery(jpaql).getSingleResult());
		}
	}
}