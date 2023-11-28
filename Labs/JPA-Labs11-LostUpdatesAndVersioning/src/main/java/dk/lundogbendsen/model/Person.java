package dk.lundogbendsen.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@SuppressWarnings("serial")
@Entity
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	// Entities must have a no-args constructor!
	public Person() {
	}
	
	public Person(String name) {
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
	
	@Override
	public String toString() {
		return "Person[id=" + id + ",name=" + this.name + ", hashCode=" + this.hashCode() + "]";
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println(this + " was garbage collected");
	}
}
