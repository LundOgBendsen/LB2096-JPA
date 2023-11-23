package lundogbendsen.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

	@OneToMany(mappedBy = "owner", fetch=FetchType.EAGER)
	private Collection<Car> ownedCars = new LinkedList<Car>();

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	public void addOwnedCar(Car newCar) {
		Person newCarsPreviousOwner = newCar.getOwner();
		if (newCarsPreviousOwner != null) {
			boolean aCarWasRemoved = newCarsPreviousOwner.ownedCars.remove(newCar);
			assert aCarWasRemoved;
		}
		newCar.setOwner(this);
		this.ownedCars.add(newCar);
	}

	public boolean removeOwnedCar(Car carToRemove) {
		if (carToRemove.getOwner() == this) {
			carToRemove.setOwner(null);
		}
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
		return "Person[id=" + id + ",name=" + name + "]";
	}
}
