package dk.lundogbendsen.client;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import dk.lundogbendsen.dao.PersonRepository;
import dk.lundogbendsen.dao.PersonRepositoryImpl;
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


		// Use the JNDI tree to retrieve the resources
		final Context ctx = createInitialContext();

		String appName = "";
		String moduleName = "/JPA-Solutions10-RemotingAndDetachment-1.0-SNAPSHOT";
		String distinctName = "";

		String beanName = PersonRepositoryImpl.class.getSimpleName();
		String viewClassName = PersonRepository.class.getName();
		String jndiNamePersonRepository="ejb:"
				+ appName + "/" + moduleName
				+ "/" + distinctName + "/" + beanName + "!" + viewClassName;



		System.out.println("EJB (jndi name): " + jndiNamePersonRepository);
		PersonRepository personRepository = (PersonRepository) ctx.lookup(jndiNamePersonRepository);

		//personRepository is ready for use

		//work here



	}
}
