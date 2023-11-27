package dk.lundogbendsen.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity()
public class Document implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private LocalDate creationDate;

	// We have chosen a uni-directional ManyToOne mapping. Mostly because there
	// conceptionally is no reason that an address should have a reference to a
	// document (order or invoice). This allows us to reuse an address for an
	// order and an invoice.
	@ManyToOne(cascade=CascadeType.PERSIST)
	private Address address;

	public Document() {
	}

	public Document(LocalDate creationDate) {
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
