package dk.lundogbendsen.dao;

import dk.lundogbendsen.model.Person;
import jakarta.ejb.Remote;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;


import java.util.List;

@Remote
public interface PersonService {

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persist(List<Person> persons);

}