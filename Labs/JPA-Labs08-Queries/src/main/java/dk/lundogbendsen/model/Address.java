package dk.lundogbendsen.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Address implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	private String street;

	private String zipCode;

	public Address() {
	}

	public Address(String name, String street, String zipCode) {
		this.name = name;
		this.street = street;
		this.zipCode = zipCode;
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
		return "Address[id=" + id + ",name=" + this.name + ",street=" + this.street + ",zipCode=" + this.zipCode + "]";
	}

}
