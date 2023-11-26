package dk.lundogbendsen.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity()
@Table(name = "Orders")
public class Order implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private LocalDate creationDate;

	private LocalDate dateShipped;

	@OneToOne(mappedBy = "order")
	private Invoice invoice;

	// We have chosen a bidirectional mapping because:
	// 1. It's a requirement that you can retrieve the orderLines from an Order
	// 2. It results in a default table layout with no link table (i.e. foreign keys in orderLine table)
	@OneToMany(mappedBy = "order")
	private Collection<OrderLine> orderLines = new LinkedList<OrderLine>();

	public Order() {
	}

	public Order(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getId() {
		return id;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDate getDateShipped() {
		return dateShipped;
	}

	public void setDateShipped(LocalDate dateShipped) {
		this.dateShipped = dateShipped;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Collection<OrderLine> getOrderLines() {
		return Collections.unmodifiableCollection(orderLines);
	}

	public void addProduct(int units, Product product) {
		OrderLine newOrderLine = new OrderLine(units, product);
		newOrderLine.setOrder(this);
		orderLines.add(newOrderLine);
	}

	@Override
	public String toString() {
		LinkedList<Integer> orderLineIds = new LinkedList<Integer>();
		for(OrderLine ol : this.orderLines){
			orderLineIds.add(ol.getId());
		}
		
		return "Order[id=" + id + ",creationDate=" + (this.creationDate != null ? this.creationDate : null)
				+ ",dateShipped=" + (this.dateShipped != null ? this.dateShipped : null) + ",orderLineIds="
				+ orderLineIds + "]";
	}
}
