package dk.lundogbendsen.model;

import jakarta.validation.constraints.NotNull;

import dk.lundogbendsen.constraints.ValidCpr;


public class Person {

	@ValidCpr
	private String cpr;

	@NotNull
	private String name;

	public Person(String cpr, String name) {
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
