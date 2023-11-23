package dk.lundogbendsen.model;

import java.io.Serializable;

public class Address implements Serializable{
	
	// Included as we implement Serializable
	private static final long serialVersionUID = 1L;
	
	private String street;
	private int zipCode;

	public Address(String street, int zipCode) {
		super();
		this.street = street;
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return "Address[street=" + street + ",zipCode=" + zipCode + "]";
	}
}
