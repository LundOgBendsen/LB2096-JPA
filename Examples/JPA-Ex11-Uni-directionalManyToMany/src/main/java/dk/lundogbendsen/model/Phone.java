package dk.lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Phone {

	@Id
	@GeneratedValue
	private Integer id;

	private String number;

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
	
	@Override
	public String toString() {
		return "Phone[id=" + id + ",number=" + this.number + "]";
	}
}
