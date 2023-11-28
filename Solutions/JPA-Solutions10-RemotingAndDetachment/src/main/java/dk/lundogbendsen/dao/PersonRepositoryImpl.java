package dk.lundogbendsen.dao;

import dk.lundogbendsen.model.Person;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;


import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonRepositoryImpl implements PersonRepository {

	private EntityManager entityManager;

	// Denne metode bør ikke være med i DAO'ens interface, da det er en
	// implementations-detalje, at der netop er tale om en JPA-baseret DAO, 
	// som skal bruge en EntityManager.
	@PersistenceContext(unitName = "primary")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Person persist(Person person) {
		entityManager.persist(person);
		return person;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Person findPerson(Integer id) {
		return entityManager.find(Person.class, id);
	}
	
	public void removePerson(Person person) {
		//person = entityManager.merge(person);
		entityManager.remove(person);
	}

	@Override
	public Person merge(Person person) {
		return entityManager.merge(person);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Person> findAllWithName(String name) {
		TypedQuery<Person> q = entityManager.createQuery("select p from Person p where p.name = :name", Person.class);
		q.setParameter("name", name);
		List<Person> result = q.getResultList();
		return result;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public long countPersons() {
		TypedQuery<Long> q = entityManager.createQuery("select count(p) from Person p", Long.class);
		Long count = q.getSingleResult();
		return count;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Person> findAllPersons(int fromIndex, int count) {
		TypedQuery<Person> q = entityManager.createQuery("select p from Person p", Person.class);
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Person> result = q.getResultList();
		return result;
	}
}
