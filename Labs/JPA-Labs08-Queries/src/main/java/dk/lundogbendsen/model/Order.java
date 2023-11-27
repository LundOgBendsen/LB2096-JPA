package dk.lundogbendsen.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import org.hibernate.LazyInitializationException;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.CascadeType.DETACH;

@SuppressWarnings("serial")
@Entity()
public class Order extends Document implements Serializable {

	private LocalDate dateShipped;

	@OneToOne(mappedBy = "order", cascade = {MERGE, PERSIST, REFRESH, DETACH })
	private Invoice invoice;


	@OneToMany(mappedBy = "order", cascade=PERSIST)
	private Collection<OrderLine> orderLines = new LinkedList<OrderLine>();

	public Order() {
	}

	public Order(LocalDate creationDate) {
		super(creationDate);
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
		try{
		for(OrderLine ol : this.orderLines){
			orderLineIds.add(ol.getId());
		}
		}catch(LazyInitializationException e){
			//ignore
		}
		
		return "Order[id=" + getId() + ",creationDate=" + (getCreationDate() != null ? getCreationDate() : null)
				+ ",dateShipped=" + (this.dateShipped != null ? this.dateShipped : null) + ",orderLineIds="
				+ orderLineIds + "]";
	}
}
