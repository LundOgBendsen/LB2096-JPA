package dk.lundogbendsen.dao;

import java.util.List;


import dk.lundogbendsen.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class ProductRepositoryImpl implements ProductRepository {

	private EntityManager entityManager;

	@PersistenceContext(unitName = "ServerJpaTestPersistenceUnit")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void persist(Product product) {
		entityManager.persist(product);
	}
	
	public long countProducts() {
		TypedQuery<Long> q = entityManager.createQuery("select count(p) from Product p", Long.class);
		Long count = q.getSingleResult();
		return count;
	}
	
	public Product findProduct(Integer id) {
		return entityManager.find(Product.class, id);
	}

	public List<Product> findAllProducts(int fromIndex, int count) {
		TypedQuery<Product> q = entityManager.createQuery("select p from Product p", Product.class);
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Product> result = q.getResultList();
		return result;
	}

}
