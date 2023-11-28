package dk.lundogbendsen.dao;

import dk.lundogbendsen.model.Person;
import jakarta.ejb.Remote;


import java.util.List;

@Remote
public interface PersonRepository {

	public Person persist(Person person);

	public Person findPerson(Integer id);

	public List<Person> findAllWithName(String name);

	public long countPersons();

	public List<Person> findAllPersons(int fromIndex, int count);

	public void removePerson(Person person);

	public Person merge(Person person);
}