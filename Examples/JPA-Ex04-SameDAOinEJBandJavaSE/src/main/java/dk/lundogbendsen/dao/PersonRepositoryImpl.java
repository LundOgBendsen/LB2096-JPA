package dk.lundogbendsen.dao;

import dk.lundogbendsen.jpa.util.EntityPager;
import dk.lundogbendsen.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
public class PersonRepositoryImpl implements PersonRepository {

	private EntityManager entityManager;

	// This method should not be in the DAO's interface since it is a
// implementation detail that this is precisely a JPA-based DAO,
// which must use an EntityManager.
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void persist(Person person) {
		entityManager.persist(person);
	}

	public Person findPerson(Integer id) {
		return entityManager.find(Person.class, id);
	}

	public void removePerson(Person person) {
		entityManager.remove(person);
	}

	@SuppressWarnings("unchecked")
	public List<Person> findAllWithName(String name) {
		Query q = entityManager.createQuery("select p from Person p where p.name = :name");
		q.setParameter("name", name);
		List<Person> result = (List<Person>) q.getResultList();
		return result;
	}

	public long countPersons() {
		Query q = entityManager.createQuery("select count(p) from Person p");
		Long count = (Long) q.getSingleResult();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Person> findAllPersons(int fromIndex, int count) {
		Query q = entityManager.createQuery("select p from Person p");
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Person> result = (List<Person>) q.getResultList();
		return result;
	}

	public EntityPager<Person> findAllPersonsAsPager(int pageSize) {
		return new EntityPager<Person>(this.entityManager, Person.class, pageSize);
	}
}
