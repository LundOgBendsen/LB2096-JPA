package dk.lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Car {

	@Id
	@GeneratedValue
	private Integer id;

	private String make;

	public Car() {
	}

	public Car(String make) {
		this.make = make;
	}

	public Integer getId() {
		return id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	@Override
	public String toString() {
		return "Car[id=" + id + ",make=" + this.make + "]";
	}

}
