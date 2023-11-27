package dk.lundogbendsen.dao;

import java.util.List;

import dk.lundogbendsen.dao.DocumentRepository;

import dk.lundogbendsen.model.Document;
import dk.lundogbendsen.model.Invoice;
import dk.lundogbendsen.model.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;



public class DocumentRepositoryImpl implements DocumentRepository {

	private EntityManager entityManager;

	// Denne metode bør ikke være med i DAO'ens interface, da det er en
	// implementations-detalje, at der netop er tale om en JPA-baseret DAO, 
	// som skal bruge en EntityManager.
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void persist(Document document) {
		entityManager.persist(document);
	}

	public Order findOrder(Integer id) {
		return entityManager.find(Order.class, id);
	}
	
	public void removeOrder(Order order) {
		entityManager.remove(order);
	}

	public long countOrders() {
		TypedQuery<Long> q = entityManager.createQuery("select count(o) from Order o", Long.class);
		Long count = q.getSingleResult();
		return count;
	}

	public List<Order> findAllOrders(int fromIndex, int count) {
		TypedQuery<Order> q = entityManager.createQuery("select o from Order o", Order.class);
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Order> result = q.getResultList();
		return result;
	}
	
	public List<Invoice> findAllInvoices(int fromIndex, int count) {
		TypedQuery<Invoice> q = entityManager.createQuery("select i from Invoice i", Invoice.class);
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Invoice> result = q.getResultList();
		return result;
	}

	public List<Document> findAllDocuments(int fromIndex, int count) {
		TypedQuery<Document> q = entityManager.createQuery("select d from Document d", Document.class);
		q.setFirstResult(fromIndex);
		q.setMaxResults(count);
		List<Document> result = q.getResultList();
		return result;
	}
}
