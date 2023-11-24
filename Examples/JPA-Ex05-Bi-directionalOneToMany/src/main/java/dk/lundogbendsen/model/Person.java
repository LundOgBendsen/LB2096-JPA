package dk.lundogbendsen.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@OneToMany(mappedBy = "owner")
	private Collection<Car> ownedCars = new LinkedList<Car>();

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	public void addOwnedCar(Car car) {
		Person previousOwner = car.getOwner();
		if (previousOwner != null) {
			boolean aCarWasRemoved = previousOwner.ownedCars.remove(car);
			assert aCarWasRemoved;
		}
		car.setOwner(this);
		this.ownedCars.add(car);
	}

	public boolean removeOwnedCar(Car car) {
		if (car.getOwner() == this) {
			car.setOwner(null);
		}
		boolean aCarWasRemoved = this.ownedCars.remove(car);
		return aCarWasRemoved;
	}

	public Collection<Car> getOwnedCars() {
		// We don't want anyone to edit the collection of owned cars directly
		// They must use addOwnedCar and removeOwnedCar (referential integrity)
		return Collections.unmodifiableCollection(this.ownedCars);
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
		msg += ",ownedCars=" + this.ownedCars + "]";
		return msg;
	}
}
