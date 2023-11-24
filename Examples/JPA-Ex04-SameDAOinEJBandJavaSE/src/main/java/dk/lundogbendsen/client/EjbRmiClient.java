package test.client.rmi;

import java.util.ArrayList;
import java.util.List;

import dk.lundogbendsen.dao.PersonRepository;
import dk.lundogbendsen.dao.PersonService;
import dk.lundogbendsen.ejb.util.JndiEjbUtil;
import dk.lundogbendsen.model.Person;

public class EjbRmiClient {
	
	// Husk at køre Ant-scrit (deployer bønnen til JBoss)
	
	public static void main(String[] args) {
		// Use this on JBoss (6x? and) 7.x
		JndiEjbUtil jndiUtil = new JndiEjbUtil(true);
		
		// Use this on JBoss 4.x - 5.x
		//JndiEjbUtil jndiUtil = new JndiEjbUtil(true, true);
		PersonService personService = jndiUtil.lookupRemoteEjbInterface(PersonService.class);
		PersonRepository personRepository = jndiUtil.lookupRemoteEjbInterface(PersonRepository.class);
		
		List<Person> persons = new ArrayList<>();
		
		persons.add(new Person("Rikke"));
		persons.add(new Person("Kurt"));
		persons.add(new Person("Jens"));
		
		personService.persist(persons);
		
		System.out.println("personRepository.countPersons(): " + personRepository.countPersons());
		System.out.println("personRepository.findPerson(1): " + personRepository.findPerson(1));
		System.out.println("personRepository.findAllWithName(\"Kurt\"): " + personRepository.findAllWithName("Kurt"));
	}

}
