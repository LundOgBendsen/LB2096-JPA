package dk.lundogbendsen.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@SuppressWarnings("serial")
@Entity
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@ManyToMany
	private Collection<Phone> ownedPhones = new LinkedList<Phone>();

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	public void addOwnedPhone(Phone newPhone) {
		this.ownedPhones.add(newPhone);
		newPhone.addOwner(this);
	}

	public void removeOwnedPhone(Phone phone) {
		this.ownedPhones.remove(phone);
		phone.removeOwner(this);
	}

	public Collection<Phone> getOwnedPhones() {
		return Collections.unmodifiableCollection(this.ownedPhones);
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
		List<String> phoneNumbers = new LinkedList<String>();
		for (Phone phone : this.ownedPhones) {
			phoneNumbers.add(phone.getNumber());
		}
		String msg = "Person[id=" + id + ",name=" + name;
		msg += ",phoneNumbers=" + phoneNumbers + "]";
		return msg;
	}
}
