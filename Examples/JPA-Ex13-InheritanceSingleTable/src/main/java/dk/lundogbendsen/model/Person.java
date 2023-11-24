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
	private Collection<Pet> ownedPets = new LinkedList<Pet>();

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	public void addOwnedPet(Pet pet) {
		Person previousOwner = pet.getOwner();
		if (previousOwner != null) {
			boolean aPetWasRemoved = previousOwner.ownedPets.remove(pet);
			assert aPetWasRemoved;
		}
		pet.setOwner(this);
		this.ownedPets.add(pet);
	}

	public boolean removeOwnedPet(Pet pet) {
		if (pet.getOwner() == this) {
			pet.setOwner(null);
		}
		boolean aPetWasRemoved = this.ownedPets.remove(pet);
		return aPetWasRemoved;
	}

	public Collection<Pet> getOwnedPets() {
		// We don't want anyone to edit the collection of owned pets directly
		// They must use addOwnedPet and removeOwnedPet (referential integrity)
		return Collections.unmodifiableCollection(this.ownedPets);
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
		msg += ",ownedPets=" + this.ownedPets + "]";
		return msg;
	}
}
