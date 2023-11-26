package dk.lundogbendsen.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@SuppressWarnings("serial")
@Entity
public class Invoice implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private LocalDate creationDate;

	private LocalDate dueDate;

	private boolean payed;


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

	@Override
	public String toString() {
		return "Invoice[id=" + id + ",creationDate=" + this.creationDate + ",dueDate="
				+ this.dueDate + ",payed=" + this.payed + ",order=" + this.order.getId() + "]";
	}
}
