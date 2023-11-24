package dk.lundogbendsen.model;

import jakarta.persistence.Entity;

@Entity
public class Bird extends Pet {

	private Boolean talking;
	
	public Bird() {
		super();
	}

	public Bird(String name) {
		super(name);
	}
	
	public Bird(String name, boolean talking) {
		this(name);
		this.talking = talking;
	}
	
	public Boolean isTalking() {
		return talking;
	}
	
	public void setTalking(Boolean talking) {
		this.talking = talking;
	}

	@Override
	public String toStringExtraFields() {
		return ",talking=" + talking;
	}
}
