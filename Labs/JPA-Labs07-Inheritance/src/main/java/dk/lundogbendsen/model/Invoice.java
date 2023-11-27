package dk.lundogbendsen.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import static jakarta.persistence.CascadeType.*;

@SuppressWarnings("serial")
@Entity
public class Invoice implements Serializable {

	private LocalDate dueDate;

	private boolean payed;

	// Det er konceptuelt ikke så smart at benytte cascade persist på denne
	// relation. Men vi er desperate for at må rmi klienten til at virke bare
	// lidt. Senere skal vi se, hvordan cascade persist kan undgås
	@OneToOne(cascade=PERSIST)
	private Order order;

	public Invoice() {
	}

	public Invoice(Order order, LocalDate creationDate) {
		//super(creationDate);
		this.order = order;
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

	//after implementing inheritance you should update this toString to include all fields.
	@Override
	public String toString() {
		return "Invoice{" +
				"dueDate=" + dueDate +
				", payed=" + payed +
				", order=" + order +
				'}';
	}
}
