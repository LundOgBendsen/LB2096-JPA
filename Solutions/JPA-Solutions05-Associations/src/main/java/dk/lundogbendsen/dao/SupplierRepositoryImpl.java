package dk.lundogbendsen.dao;

import java.util.List;

import dk.lundogbendsen.model.Supplier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class SupplierRepositoryImpl implements SupplierRepository {

	private EntityManager entityManager;

	// Denne metode bør ikke være med i DAO'ens interface, da det er en
	// implementations-detalje, at der netop er tale om en JPA-baseret DAO, 
	// som skal bruge en EntityManager.
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void persist(Supplier supplier) {
		entityManager.persist(supplier);
	}

	public Supplier findSupplier(Integer id) {
		return entityManager.find(Supplier.class, id);
	}
	
	public void removeSupplier(Supplier supplier) {
		entityManager.remove(supplier);
	}

	public List<Supplier> findAllWithName(String name) {
		TypedQuery<Supplier> q = entityManager.createQuery("select s from Supplier s where s.name = :name", Supplier.class);
		q.setParameter("name", name);
		List<Supplier> result = q.getResultList();
		return result;
	}

	public long countSuppliers() {
		TypedQuery<Long> q = entityManager.createQuery("select count(s) from Supplier s", Long.class);
		Long count = q.getSingleResult();
		return count;
	}

	public List<Supplier> findAllSuppliers(int fromIndex, int count) {
		TypedQuery<Supplier> q = entityManager.createQuery("select s from Supplier s", Supplier.class);
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Supplier> result = q.getResultList();
		return result;
	}
}
