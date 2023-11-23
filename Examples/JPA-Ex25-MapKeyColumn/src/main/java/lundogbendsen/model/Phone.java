package lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Phone {

	@Id
	@GeneratedValue
	private Integer id;

	private String number;
	
	@ManyToOne
	private PersonWithPhone owner;

	public Phone() {
	}

	public Phone(String number) {
		this.number = number;
	}

	public Integer getId() {
		return id;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}
	
	// Only used by Person.addPhone(..)
	void setOwner(PersonWithPhone person) {
		this.owner = person;
	}
	
	public PersonWithPhone getOwner(){
		return owner;
	}
	
	@Override
	public String toString() {
		return "Phone[id=" + id + ",number=" + this.number + "]";
	}
}
