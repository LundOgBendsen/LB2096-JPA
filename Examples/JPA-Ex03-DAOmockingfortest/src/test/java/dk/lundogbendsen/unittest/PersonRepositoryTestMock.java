package dk.lundogbendsen.unittest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;

import dk.lundogbendsen.dao.PersonRepository;
import dk.lundogbendsen.model.Person;
import dk.lundogbendsen.jpa.util.EntityPager;

public class PersonRepositoryTestMock implements PersonRepository {

	private Map<Integer, Person> idToPersonMap = new HashMap<Integer, Person>();
	
	public void addPerson(int id, Person person) {
		idToPersonMap.put(id, person);
	}
	
	public long countPersons() {
		return idToPersonMap.size();
	}

	public List<Person> findAllPersons(int fromIndex, int count) {
		// intentionally no implementation
		return null;
	}

	public EntityPager<Person> findAllPersonsAsPager(int pageSize) {
		// intentionally no implementation
		return null;
	}

	public List<Person> findAllWithName(String name) {
		// intentionally no implementation
		return null;
	}

	public Person findPerson(Integer id) {
		return this.idToPersonMap.get(id);
	}

	public void persist(Person person) {
		// intentionally no implementation
	}

	public void removePerson(Person person) {
		// intentionally no implementation
	}

	public void setEntityManager(EntityManager entityManager) {
		// intentionally no implementation
	}
}
