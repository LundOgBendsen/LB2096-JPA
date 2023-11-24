package dk.lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Pet {

	@Id
	//@GeneratedValue
	private Integer id;

	private String name;

	@ManyToOne
	private Person owner;
	
	public Pet() {
	}

	public Pet(int id, String name) {
		this.id = id;
		this.name = name;
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
	
	// This method is only used in Person.addOwnedPet(Pet)
	void setOwner(Person person) {
		this.owner = person;
	}
	
	public Person getOwner() {
		return owner;
	}
	
	@Override
	public String toString() {
		String msg = getClass().getSimpleName();
		msg += "[id=" + id + ",name=" + this.name;
		msg += ",ownerId=" + (getOwner() != null ? getOwner().getId() : null);
		msg += toStringExtraFields() + "]";
		return msg;
	}
	
	public abstract String toStringExtraFields();
}
