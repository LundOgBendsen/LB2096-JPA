package dk.lundogbendsen.model;

import jakarta.persistence.Entity;

@Entity
public class Bird extends Pet {

	private Boolean talking;
	
	public Bird() {
		super();
	}

	public Bird(int id, String name) {
		super(id, name);
	}
	
	public Bird(int id, String name, boolean talking) {
		this(id, name);
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
