package dk.lundogbendsen.model;

import jakarta.persistence.*;

@Entity
public class Engine {

	@Id
	@GeneratedValue
	private Integer id;

	private int cylinderCount;

	@OneToOne(mappedBy = "engine")
	private Car car;

	public Engine() {
	}

	public Engine(int cylinderCount) {
		this.cylinderCount = cylinderCount;
	}

	public Integer getId() {
		return id;
	}

	public void setCylinderCount(int cylinderCount) {
		this.cylinderCount = cylinderCount;
	}

	public int getCylinderCount() {
		return cylinderCount;
	}

	public Car getCar() {
		return car;
	}

	// Use only from Car.setEngine
	void setCar(Car car) {
		this.car = car;
	}

	@Override
	public String toString() {
		String msg = "Engine[id=" + id + ",cylinderCount=" + cylinderCount;
		msg += ",carId=" + (getCar() != null ? getCar().getId() : null) + "]";
		return msg;
	}
}
