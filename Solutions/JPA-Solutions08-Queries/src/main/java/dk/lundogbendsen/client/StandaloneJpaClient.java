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

import jakarta.persistence.TypedQuery;
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

			entityManager.getTransaction().begin();
			createManyEntities(entityManager);

			// We make each query in it own code block to prevent name clashes.
			{
				String comment = "The 10 most expensive products";
				String jpaql = "SELECT p FROM Product p ORDER BY p.cost DESC";
				TypedQuery<Product> q = entityManager.createQuery(jpaql, Product.class);
				q.setMaxResults(10);
				List<Product> result = q.getResultList();
				JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
			}
			{
				// Fourth order does not have an address. Instead null values will
				// be returned for address.name
				String comment = "Order data";
				String jpaql = "SELECT o.id, a.name, ol.unitCount, ol.product.name, ol.product.cost "
						+ " FROM OrderLine ol JOIN ol.order o LEFT JOIN o.address a";
				List<Object[]> result = entityManager.createQuery(jpaql, Object[].class).getResultList();
				JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
			}
			{
				String comment = "Invoice selection - paid and not due";
				String jpaql = "SELECT i FROM Invoice i WHERE i.payed=true AND i.dueDate < CURRENT_DATE";
				List<Invoice> result = entityManager.createQuery(jpaql, Invoice.class).getResultList();
				JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
			}
			{
				// The cartesian product can be used to find the documents with same address.

				String comment = "Documents sent to the same address";
				String jpaql = "SELECT DISTINCT d1 FROM Document d1, Document d2 ";
				jpaql += " WHERE d1 <> d2 AND d1.address.name = d2.address.name ";
				jpaql += "AND d1.address.street = d2.address.street AND d1.address.zipCode = d2.address.zipCode";
				List<Document> result = entityManager.createQuery(jpaql, Document.class).getResultList();
				JpaUtil.prettyPrintQueryResult(comment, jpaql, result);
			}

			entityManager.getTransaction().commit();
		}	
	}
	


	private static void createManyEntities(final EntityManager entityManager) {
		StringUtil.prettyPrintHeadline("Create and persist suppliers and products");
		// Start new TX on the entity manager the DAO uses. Notice how we can
		// control the transactions to span one or more DAO method invocations.
		Supplier glostrup = new Supplier("Glostrup EDBHandel");
		Supplier birgers = new Supplier("Birgers InternetFupHandel A/S");
		entityManager.persist(glostrup);
		entityManager.persist(birgers);

		Product labtop = new Product("Laptop", new BigDecimal(12000.0));
		Product harddisk = new Product("External harddisk", new BigDecimal(720.0));
		Product ram = new Product("4 GB RAM", new BigDecimal(2200.0));
		Product floppy = new Product("Floppy disk (antik)", new BigDecimal(7200.0));
		Product keyboard = new Product("Keyboard", new BigDecimal(120.0));

		Product grafikkort256 = new Product("Grafikkort 256 MB", new BigDecimal(218.0));
		Product grafikkort1000 = new Product("Grafikkort 1 GB", new BigDecimal(2982.0));
		Product bigTower = new Product("Big tower kabinet", new BigDecimal(520.0));
		Product midiTower = new Product("Midi tower", new BigDecimal(270.0));
		Product processor = new Product("Processor", new BigDecimal(700.0));
		Product megaProcessor = new Product("Mega super processor", new BigDecimal(5134.0));

		entityManager.persist(labtop);
		entityManager.persist(harddisk);
		entityManager.persist(ram);
		entityManager.persist(floppy);
		entityManager.persist(keyboard);
		entityManager.persist(grafikkort256);
		entityManager.persist(grafikkort1000);
		entityManager.persist(bigTower);
		entityManager.persist(midiTower);
		entityManager.persist(processor);
		entityManager.persist(megaProcessor);

		glostrup.addProduct(labtop);
		glostrup.addProduct(harddisk);
		glostrup.addProduct(ram);

		birgers.addProduct(ram);
		birgers.addProduct(floppy);
		birgers.addProduct(keyboard);


		Order firstOrder = new Order(LocalDate.now());
		firstOrder.addProduct(4, labtop);
		firstOrder.addProduct(4, ram);
		firstOrder.addProduct(2, ram);

		Address john = new Address("John Doe", "el street", "YL-1828 US");
		firstOrder.setAddress(john);

		Order secondOrder = new Order();
		secondOrder.addProduct(1, megaProcessor);
		secondOrder.addProduct(1, grafikkort1000);

		Address hugo = new Address("Hugo Chavez", "la via ", "xxx");
		secondOrder.setAddress(hugo);

		Order thirdOrder = new Order();
		thirdOrder.addProduct(1, keyboard);
		thirdOrder.addProduct(1, bigTower);
		thirdOrder.addProduct(1, processor);

		Address bill = new Address("Bill Gates", "microsoft lane", "silicon valley zipcode");
		thirdOrder.setAddress(bill);

		Order fourthOrder = new Order();
		fourthOrder.addProduct(1, ram);
		fourthOrder.addProduct(1, grafikkort1000);

		// Address bill = new Address("Bill Gates", "microsoft lane", "silicon
		// valey zipcode");
		// fourthOrder.setAddress(hugo);

		entityManager.persist(firstOrder);
		entityManager.persist(secondOrder);
		entityManager.persist(thirdOrder);
		entityManager.persist(fourthOrder);

		// Due date is 15 dates from shipping date
		LocalDate shippingDate = LocalDate.now().plusDays(3);
		LocalDate dueDate = shippingDate.plusDays(15);
		LocalDate dueDate2 = dueDate.plusDays(7);


		Invoice firstInvoice = new Invoice(firstOrder, shippingDate);
		firstInvoice.setDueDate(dueDate);
		firstInvoice.setAddress(john);
		firstOrder.setDateShipped(shippingDate);


		Address hugo2 = new Address("Hugo Chavez", "la via ", "xxx");
		Invoice secondInvoice = new Invoice(secondOrder, shippingDate);
		secondInvoice.setDueDate(dueDate2);
		secondInvoice.setAddress(hugo2);
		secondInvoice.setPayed(true);
		secondOrder.setDateShipped(shippingDate);

		// Persist invoice notice that order is automaticly updated
		entityManager.persist(firstInvoice);
		entityManager.persist(secondInvoice);
	}

	private static void printAllEntities(final EntityManager entityManager) {
		String jpaql = "select s from Supplier s";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select p from Product p";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select o from Order o";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select d from Document d";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());
		jpaql = "select a from Address a";
		JpaUtil.prettyPrintQueryResult(jpaql, entityManager.createQuery(jpaql).getResultList());

	}

}