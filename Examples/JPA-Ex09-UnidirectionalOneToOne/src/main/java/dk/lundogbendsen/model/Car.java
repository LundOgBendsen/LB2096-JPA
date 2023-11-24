package dk.lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

// Immutable class. The constructor creates an engine and the class Car encompass no setter 
// method for an engine, i.e. a cars engine is not subject for substitution. 

@Entity
public class Car {

	@Id
	@GeneratedValue
	private Integer id;

	private String make;

	@OneToOne
	private Engine engine;

	public Car() {
	}

	public Car(String make, int engineCylinderCount) {
		this.make = make;
		this.engine = new Engine(engineCylinderCount);
	}

	public Integer getId() {
		return id;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getMake() {
		return make;
	}

	public Engine getEngine() {
		return engine;
	}

	@Override
	public String toString() {
		String msg = "Car[id=" + id + ",make=" + this.make;
		msg += ",engineId=" + (getEngine() != null ? getEngine().getId() : null) + "]";
		return msg;
	}
}
