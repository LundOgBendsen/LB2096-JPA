package dk.lundogbendsen.dao;

import dk.lundogbendsen.model.Invoice;
import dk.lundogbendsen.model.Order;
import dk.lundogbendsen.model.OrderLine;

import java.util.List;


public interface DocumentRepository {

	public void persist(Order order);
	
	public void persist(OrderLine orderLine);

	public void persist(Invoice invoice);

	public Order findOrder(Integer id);

	public long countOrders();

	public List<Order> findAllOrders(int fromIndex, int count);
	
	public List<Invoice> findAllInvoices(int fromIndex, int count);
	
	public void removeOrder(Order order);

}