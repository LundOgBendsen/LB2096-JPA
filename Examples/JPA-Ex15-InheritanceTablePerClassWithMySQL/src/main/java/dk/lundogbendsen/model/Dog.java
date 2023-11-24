package dk.lundogbendsen.model;

import jakarta.persistence.Entity;

@Entity
public class Dog extends Pet {

	private Boolean aggressive;
	
	public Dog() {
		super();
	}

	public Dog(int id, String name) {
		super(id, name);
	}
	
	public Dog(int id, String name, boolean aggressive) {
		this(id, name);
		this.aggressive = aggressive;
	}
	
	public Boolean isAggressive() {
		return aggressive;
	}
	
	public void setAggressive(Boolean aggressive) {
		this.aggressive = aggressive;
	}

	@Override
	public String toStringExtraFields() {
		return ",aggressive=" + aggressive;
	}
}
