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

	@OneToMany
	private Collection<Car> ownedCars = new LinkedList<Car>();

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	// Note: This method does not secure referential intregrity as the
	// added car will still be contained with in the previous owners collection of owned cars
	public void addOwnedCar(Car newCar) {
		if(! this.ownedCars.contains(newCar)){
			this.ownedCars.add(newCar);
		}
	}

	public boolean removeOwnedCar(Car carToRemove) {
		boolean aCarWasRemoved = this.ownedCars.remove(carToRemove);
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
