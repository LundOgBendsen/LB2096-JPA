package lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;

@Entity
public class Car {

	@PostPersist
	void onPostPersist() {
		System.out.println("--==<[Car.onPostPersist(id=" + this.id + ") ]>==--");
	}
	
	@Id
	@GeneratedValue
	private Integer id;

	private String make;

	@ManyToOne
	private Person owner;

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

	// This method is only used in Person.addOwnedCar(Car)
	void setOwner(Person person) {
		this.owner = person;
	}

	public Person getOwner() {
		return owner;
	}

	@Override
	public String toString() {
		return "Car[id=" + id + ",make=" + this.make + "]";
	}
}
