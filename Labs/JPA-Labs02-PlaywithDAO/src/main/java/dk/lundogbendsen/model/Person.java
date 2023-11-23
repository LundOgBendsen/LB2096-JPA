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

	/**
	 * Returns a brief description of this object. The exact
	 * details are unspecified and subject to change, but the 
	 * following may be regarded as typical:
	 * 
	 * Person[attribute list goes here]
	 *
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString(){
	    final String delim = ", ";
	    StringBuilder res = new StringBuilder();
	    res.append("Person[")
	        .append("id=").append(this.id).append(delim)
	        .append("name=").append(this.name)
	        .append("]");
	    
	    return res.toString();
	}
}
