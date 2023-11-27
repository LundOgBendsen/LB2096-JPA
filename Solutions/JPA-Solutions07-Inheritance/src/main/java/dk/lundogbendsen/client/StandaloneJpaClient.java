package dk.lundogbendsen.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.List;

import dk.lundogbendsen.dao.*;
import dk.lundogbendsen.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.apache.log4j.Level;

import dk.lundogbendsen.jpa.util.JpaUtil;
import dk.lundogbendsen.string.util.StringUtil;

public class StandaloneJpaClient {

	public static void main(String[] args) {

		JpaUtil.HibernateLoggingConfig loggingConfig = new JpaUtil.HibernateLoggingConfig();
		loggingConfig.setHibernateRootLogLevel(Level.WARN);
		loggingConfig.setLogDdlEnabled(true);
		loggingConfig.setLogSqlEnabled(false);
		loggingConfig.setLogParameterBindingEnabled(false);
		JpaUtil.configureHibernateLogging(loggingConfig);
		
		try(EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("StandaloneJpaTestPersistenceUnit");
			EntityManager entityManager = entityManagerFactory.createEntityManager()){
			// Create new JPA entity manager (similar to JDBC Connection)
			
			exerciseWithJPA(loggingConfig, entityManagerFactory, entityManager);
		}	
	}
	
	public static void exerciseWithJPA(JpaUtil.HibernateLoggingConfig loggingConfig, 
			EntityManagerFactory entityManagerFactory, EntityManager entityManager){
		
		// Instantiate DAOs
		SupplierRepository supplierRepository = new SupplierRepositoryImpl();
		ProductRepository productRepository = new ProductRepositoryImpl();
		DocumentRepository documentRepository = new DocumentRepositoryImpl();

		// Tell the DAO which entity manager to use
		((SupplierRepositoryImpl)supplierRepository).setEntityManager(entityManager);
		((ProductRepositoryImpl)productRepository).setEntityManager(entityManager);
		((DocumentRepositoryImpl)documentRepository).setEntityManager(entityManager);

		StringUtil.prettyPrintHeadline("Create and persist suppliers and products");
		// Start new TX on the entity manager the DAO uses. Notice how we can
		// control the transactions to span one or more DAO method invocations.
		entityManager.getTransaction().begin();
		supplierRepository.persist(new Supplier("Ajax hardware store"));
		supplierRepository.persist(new Supplier("Acme IT equipment"));
		
		Product laptop = new Product("Laptop", new BigDecimal(12000.0));
		Product harddisk = new Product("External harddisk", new BigDecimal(720.0));
		Product ram = new Product("4 GB RAM", new BigDecimal(2200.0));
		Product floppy = new Product("Floppy disk (antique)", new BigDecimal(7200.0));
		Product keyboard = new Product("Keyboard", new BigDecimal(120.0));
		
		productRepository.persist(laptop);
		productRepository.persist(harddisk);
		productRepository.persist(ram);
		productRepository.persist(floppy);
		productRepository.persist(keyboard);

		String executing = "supplierRepository.findAllSuppliers(0, 10)"; 
		JpaUtil.prettyPrintQueryResult(executing, supplierRepository.findAllSuppliers(0, 10));
		executing = "productRepository.findAllProducts(0, 10)";
		JpaUtil.prettyPrintQueryResult(executing, productRepository.findAllProducts(0, 10));
		
		entityManager.getTransaction().commit();

		//////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Add products to suppliers");
		entityManager.getTransaction().begin();

		Supplier sup1 = supplierRepository.findSupplier(1);
		sup1.addProduct(laptop);
		sup1.addProduct(harddisk);
		sup1.addProduct(ram);

		Supplier sup2 = supplierRepository.findSupplier(2);
		sup2.addProduct(ram);
		sup2.addProduct(floppy);
		sup2.addProduct(keyboard);
		
		executing = "supplierRepository.findAllSuppliers(0, 10)"; 
		JpaUtil.prettyPrintQueryResult(executing, supplierRepository.findAllSuppliers(0, 10));
		executing = "productRepository.findAllProducts(0, 10)";
		JpaUtil.prettyPrintQueryResult(executing, productRepository.findAllProducts(0, 10));

		entityManager.getTransaction().commit();

		//////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Order products");
		entityManager.getTransaction().begin();

		LocalDate orderDate = LocalDate.now();
		Order order = new Order(orderDate);
		order.addProduct(4, laptop);
		order.addProduct(4, ram);
		order.addProduct(2, ram);

		Address address = new Address("John Doe", "el street", "YL-1828 US");
		order.setAddress(address);
		
		// Persist order
		documentRepository.persist(order);
		// We at this point (of our knowledge) have to persist each orderline separately
		//orderlines are persisted by cascading
		/* for(OrderLine ol : order.getOrderLines()){
			documentRepository.persist(ol);
		}*/
		
		executing = "orderRepository.findAllOrders(0, 10)"; 
		JpaUtil.prettyPrintQueryResult(executing, documentRepository.findAllOrders(0, 10));

		entityManager.getTransaction().commit();
		
		//////////////////////////////////////////////////////
		StringUtil.prettyPrintHeadline("Ship order and send invoice");
		entityManager.getTransaction().begin();

		// Send order the day after creation (not taking care of new years))
		LocalDate shippingDate = LocalDate.now().plusDays(1);

		// Due date is 15 dates from shipping date
		LocalDate dueDate = shippingDate.plusDays(15);

		Invoice invoice = new Invoice(order, shippingDate);
		invoice.setDueDate(dueDate);
		order.setDateShipped(shippingDate);
		order.setInvoice(invoice);
		invoice.setAddress(address);
		
		//invoice is persisted by cascading
		// documentRepository.persist(invoice);

		executing = "orderRepository.findAllOrders(0, 10)"; 
		JpaUtil.prettyPrintQueryResult(executing, documentRepository.findAllOrders(0, 10));

		executing = "invoiceRepository.findAllInvoices(0, 10)"; 
		JpaUtil.prettyPrintQueryResult(executing, documentRepository.findAllInvoices(0, 10));

		entityManager.getTransaction().commit();

		executing = "orderRepository.findAllOrders(0, 10)";
		entityManager.getTransaction().begin();
		JpaUtil.prettyPrintQueryResult(executing, documentRepository.findAllOrders(0, 10));

		executing = "before removal of one order: invoiceRepository.findAllInvoices(0, 10)";
		JpaUtil.prettyPrintQueryResult(executing, documentRepository.findAllInvoices(0, 10));

		executing = "orderRepository.findAllDocuments(0, 10)";
		JpaUtil.prettyPrintQueryResult(executing, documentRepository.findAllDocuments(0, 10));
	}
}