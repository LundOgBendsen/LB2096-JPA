package dk.lundogbendsen.dao;

import dk.lundogbendsen.jpa.util.EntityPager;

import dk.lundogbendsen.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PersonDaoImpl implements PersonDao {

	private EntityManager entityManager;

	//no args constructor to be JavaBeans compliant
	public PersonDaoImpl() {}

	//constructor signals that an EntityManager is required
	public PersonDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	This method is not part of the DAO's interface as it an implementation detail specific for a JPA Based DAO.
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void persist(Person person) {
		entityManager.persist(person);
	}

	public Person findPerson(Integer id) {
		return entityManager.find(Person.class, id);
	}

	public void removePerson(Integer id) {
		Person person = findPerson(id);
		if (person != null) {
			entityManager.remove(person);	
		}
	}

	public List<Person> findAllWithName(String name) {
		TypedQuery<Person> q = entityManager.createQuery("select p from Person p where p.name = :name", Person.class);
		q.setParameter("name", name);
		List<Person> result = q.getResultList();
		return result;
	}

	public long countPersons() {
		TypedQuery<Long> q = entityManager.createQuery("select count(p) from Person p", Long.class);
		Long count = q.getSingleResult();
		return count;
	}

	public List<Person> findAllPersons(int fromIndex, int count) {
		TypedQuery<Person> q = entityManager.createQuery("select p from Person p", Person.class);
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Person> result = q.getResultList();
		return result;
	}
	public EntityPager<Person> findAllPersonsAsPager(int pageSize) {
		return new EntityPager<Person>(this.entityManager, Person.class, pageSize);
	}

	public long countPersonsWithName(String name) {
		TypedQuery<Long> q = entityManager.createQuery("select count(p) from Person p where p.name = :name", Long.class);
		q.setParameter("name", name);
		
		return q.getSingleResult();
	}

	public void removePersonWithId(int id) {
		Person personToRemove = entityManager.find(Person.class, id);
		
		if (personToRemove!= null) {
			entityManager.remove(personToRemove);
		}
	}

	public void removePerson(Person person) {
		if (person != null) {
			entityManager.remove(person);			
		}
		
	}
}
