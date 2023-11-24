package dk.lundogbendsen.dao;

import dk.lundogbendsen.model.Person;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;


import java.util.List;

@Stateless
public class PersonServiceImpl implements PersonService {

	@EJB
	private PersonRepository repository;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persist(List<Person> persons) {
		for(Person person : persons){
			repository.persist(person);			
		}
	}

}
