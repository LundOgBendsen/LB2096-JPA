package dk.lundogbendsen.model;

import jakarta.persistence.*;
import dk.lundogbendsen.model.Engine;

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

	public Car(String make) {
		this.make = make;
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

	public void setEngine(Engine newEngine) {
		// If this car already had a ref to an Engine oldEngine,
		// then oldEngine.car will reference this Car.
		// The oldEngine.car must be set to null
		Engine oldEngine = this.engine;
		if (oldEngine != null) {
			oldEngine.setCar(null);
		}
		// If the new Engine newEngine already had a ref to another Car oldCar,
		// then oldCar will have a reference to newEngine.
		// The oldCar.engine must be set to null
		Car oldCar = newEngine.getCar();
		if (oldCar != null) {
			oldCar.engine = null;
		}
		this.engine = newEngine;
		newEngine.setCar(this);
	}

	@Override
	public String toString() {
		String msg = "Car[id=" + id + ",make=" + this.make;
		msg += ",engineId=" + (getEngine() != null ? getEngine().getId() : null) + "]";
		return msg;
	}
}
