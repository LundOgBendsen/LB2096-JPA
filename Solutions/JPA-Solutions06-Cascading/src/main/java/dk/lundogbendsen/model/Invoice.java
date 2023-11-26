package dk.lundogbendsen.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity
public class Invoice implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private LocalDate creationDate;

	private LocalDate dueDate;

	private boolean payed;

	//no cascading. Order is the group owner and initiator of cascading
	@OneToOne
	private Order order;

	public Invoice() {
	}

	public Invoice(Order order, LocalDate creationDate) {
		this.order = order; 
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

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public Order getOrder() {
		return order;
	}

	//we need this when removing orders while keeping invoice.
	public void detachFromOrder(){
		order = null;
	}
	@Override
	public String toString() {
		return "Invoice[id=" + id + ",creationDate=" + this.creationDate + ",dueDate="
				+ this.dueDate + ",payed=" + this.payed + ",order=" + (this.order==null ? "null" : this.order.getId()) + "]";
	}
}
