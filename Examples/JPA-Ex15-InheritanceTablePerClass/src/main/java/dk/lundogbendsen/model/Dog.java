package dk.lundogbendsen.model;

import jakarta.persistence.Entity;

@Entity
public class Dog extends Pet {

	private Boolean aggressive;
	
	public Dog() {
		super();
	}

	public Dog(String name) {
		super(name);
	}
	
	public Dog(String name, boolean aggressive) {
		this(name);
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
