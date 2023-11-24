package dk.lundogbendsen.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class PersonWithCar implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@OneToMany
	private Map<Integer, Car> ownedCars = new HashMap<>();
	
	public PersonWithCar() {
	}

	public PersonWithCar(String name) {
		this.name = name;
	}

	public void addOwnedCar(Integer yearBought, Car car) {
		this.ownedCars.put(yearBought, car);
	}
	

	public boolean removeOwnedCar(Integer yearBought) {
		Car carRemoved = this.ownedCars.remove(yearBought);
		assert carRemoved != null;
		return carRemoved != null;
	}

	public Map<Integer, Car> getOwnedCars() {
		// We don't want anyone to edit the collection of owned cars directly
		// They must use addOwnedCar and removeOwnedCar (referential integrity)
		return Collections.unmodifiableMap(this.ownedCars);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String msg = "Person[id=" + id + ",name=" + name;
		msg += ",ownedCars=" + this.getOwnedCars() + "]";
		return msg;
	}
}
