package lundogbendsen.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lundogbendsen.model.Person;
import dk.lundogbendsen.jpa.util.EntityPager;

public class PersonRepositoryImpl implements PersonRepository {

	private EntityManager entityManager;

	// Denne metode bør ikke være med i DAO'ens interface, da det er en
	// implementations-detalje, at der netop er tale om en JPA-baseret DAO,
	// som skal bruge en EntityManager.
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void persist(Person person) {
		entityManager.persist(person);
	}

	public Person findPerson(Integer id) {
		TypedQuery<Person> q = entityManager.createQuery(
				"select p from Person p where p.id = :id", Person.class);
		q.setParameter("id", id);
		List<Person> result = q.getResultList();
		return result.get(0);
	}

	public void removePerson(Person person) {
		entityManager.remove(person);
	}

	public List<Person> findAllWithName(String name) {
		TypedQuery<Person> q = entityManager.createQuery(
				"select p from Person p where p.name = :name", Person.class);
		q.setParameter("name", name);
		List<Person> result = q.getResultList();
		return result;
	}

	public List<Person> findAllWithNameAndCars(String name) {
		String queryStr = "select p from Person p join fetch p.ownedCars where p.name = :name";
		TypedQuery<Person> q = entityManager
				.createQuery(queryStr, Person.class);
		q.setParameter("name", name);
		List<Person> result = q.getResultList();
		return result;
	}

	public long countPersons() {
		TypedQuery<Long> q = entityManager.createQuery(
				"select count(p) from Person p", Long.class);
		Long count = q.getSingleResult();
		return count;
	}

	public List<Person> findAllPersons(int fromIndex, int count) {
		String query = "select p from Person p";
		TypedQuery<Person> q = entityManager.createQuery(query, Person.class);
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Person> result = (List<Person>) q.getResultList();
		return result;
	}

	public EntityPager<Person> findAllPersonsAsPager(int pageSize) {
		return new EntityPager<Person>(this.entityManager, Person.class,
				pageSize);
	}
}
