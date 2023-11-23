package dk.lundogbendsen.dao;

import java.util.List;

import dk.lundogbendsen.model.Person;
import dk.lundogbendsen.jpa.util.EntityPager;

public interface PersonRepository {

	public void persist(Person person);

	public Person findPerson(Integer id);

	public List<Person> findAllWithName(String name);

	public long countPersons();

	public List<Person> findAllPersons(int fromIndex, int count);

	public EntityPager<Person> findAllPersonsAsPager(int pageSize);

	public void removePerson(Person person);
}