package dk.lundogbendsen.client;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import dk.lundogbendsen.dao.PersonRepository;
import dk.lundogbendsen.dao.PersonRepositoryImpl;
import dk.lundogbendsen.dao.PersonService;
import dk.lundogbendsen.dao.PersonServiceImpl;
import dk.lundogbendsen.ejb.util.JndiEjbUtil;
import dk.lundogbendsen.model.Person;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//See README.txt for how to run

public class EjbRmiClient {
	

	private static Context createInitialContext() throws NamingException {
		Hashtable<String, Object> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		env.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
		env.put("jboss.naming.client.ejb.context", true);
		InitialContext ctx = new InitialContext(env);
		return ctx;
	}

	public static void main(String[] args) throws NamingException {
		// Use this on JBoss (6x? and) 7.x
		JndiEjbUtil jndiUtil = new JndiEjbUtil(true);

		String jndi1 = "java:jboss/exported/JPA-Ex04-SameDAOinEJBandJavaSE-1.0-SNAPSHOT/PersonRepositoryImpl!dk.lundogbendsen.dao.PersonRepository";
		String jndi2 = "java:jboss/exported/JPA-Ex04-SameDAOinEJBandJavaSE-1.0-SNAPSHOT/PersonServiceImpl!dk.lundogbendsen.dao.PersonService";

		// Use the JNDI tree to retrieve the resources
		final Context ctx = createInitialContext();

		String appName = "";
		String moduleName = "/JPA-Ex04-SameDAOinEJBandJavaSE-1.0-SNAPSHOT";
		String distinctName = "";

		String beanName = PersonRepositoryImpl.class.getSimpleName();
		String viewClassName = PersonRepository.class.getName();
		String jndiNamePersonRepository="ejb:"
				+ appName + "/" + moduleName
				+ "/" + distinctName + "/" + beanName + "!" + viewClassName;

		beanName = PersonServiceImpl.class.getSimpleName();
		viewClassName = PersonService.class.getName();
		String jndiNamePersonService="ejb:"
				+ appName + "/" + moduleName
				+ "/" + distinctName + "/" + beanName + "!" + viewClassName;


		System.out.println("EJB 1: " + jndiNamePersonRepository);
		System.out.println("EJB 2: " + jndiNamePersonService);
		PersonRepository personRepository = (PersonRepository) ctx.lookup(jndiNamePersonRepository);
		PersonService personService = (PersonService) ctx.lookup(jndiNamePersonService);


		List<Person> persons = new ArrayList<>();
		
		persons.add(new Person("Rikke"));
		persons.add(new Person("Kurt"));
		persons.add(new Person("Jens"));
		personService.persist(persons);

		
		System.out.println("personRepository.countPersons(): " + personRepository.countPersons());
		System.out.println("personRepository.findPerson(1): " + personRepository.findPerson(1));
		System.out.println("personRepository.findAllWithName(\"Kurt\"): " + personRepository.findAllWithName("Kurt"));
		Person person = personRepository.findPerson(1);
		personRepository.removePerson(person);
		System.out.println("Removed!");


	}

}
