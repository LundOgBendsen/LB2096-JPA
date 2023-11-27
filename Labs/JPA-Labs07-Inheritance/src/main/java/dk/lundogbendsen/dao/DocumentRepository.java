package dk.lundogbendsen.dao;

import dk.lundogbendsen.model.Document;
import dk.lundogbendsen.model.Invoice;
import dk.lundogbendsen.model.Order;

import java.util.List;



public interface DocumentRepository {

	public void persist(Document order);

	public Order findOrder(Integer id);

	public long countOrders();

	public void removeOrder(Order order);

	public List<Order> findAllOrders(int fromIndex, int count);
	
	public List<Invoice> findAllInvoices(int fromIndex, int count);


}