package dk.lundogbendsen.model;

import jakarta.validation.constraints.NotNull;

import dk.lundogbendsen.constraints.Cpr;
import dk.lundogbendsen.service.CprStatus;


public class Person {

	@Cpr(CprStatus.ALIVE)
	private String cpr;

	@NotNull
	private String name;

	public Person(String cpr, String name) {
		super();
		this.cpr = cpr;
		this.name = name;
	}

	public String getCpr() {
		return cpr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Person [cpr=" + cpr + ", name=" + name + "]";
	}

	
}
