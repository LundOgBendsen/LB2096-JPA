package dk.lundogbendsen.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

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
		return "Person[id=" + id + ",name=" + name + "]";
	}
}
