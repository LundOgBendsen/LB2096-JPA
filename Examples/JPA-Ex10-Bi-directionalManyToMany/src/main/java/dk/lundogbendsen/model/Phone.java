package dk.lundogbendsen.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import dk.lundogbendsen.model.Person;

@Entity
public class Phone {

	@Id
	@GeneratedValue
	private Integer id;

	private String number;

	@ManyToMany(mappedBy = "ownedPhones")
	private Collection<Person> owners = new LinkedList<Person>();
	
	public Phone() {
	}

	public Phone(String number) {
		this.number = number;
	}

	public Integer getId() {
		return id;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}
	
	// Only used by Person.addPhone(..)
	void addOwner(Person person) {
		this.owners.add(person);
	}
	
	// Only used by Person.removePhone(..)
	void removeOwner(Person person) {
		this.owners.remove(person);
	}
	
	public Collection<Person> getOwners() {
		return Collections.unmodifiableCollection(this.owners);
	}

	@Override
	public String toString() {
		List<String> ownerNames = new LinkedList<String>();
		for(Person owner : this.owners) {
			ownerNames.add(owner.getName());
		}
		String msg = "Phone[id=" + id + ",number=" + this.number;
		msg += ",ownerNames=" + ownerNames + "]";
		return msg;
	}
}
