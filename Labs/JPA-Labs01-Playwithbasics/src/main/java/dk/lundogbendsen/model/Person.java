package dk.lundogbendsen.model;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;
	
	//Lab01: Added dat of birth 
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;


	
	// Entities must have a no-args constructor!
	public Person() {
	}
	
	public Person(String name) {
		this.name = name;
		dateOfBirth = getRandomDate();
	}
	
	private static Date getRandomDate() {
		Random r = new Random();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(r.nextLong() % System.currentTimeMillis());
		
		return cal.getTime();
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
	
	Date getDateOfBirth() {
		return dateOfBirth;
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
	public String toString() {
		return "Person[id=" + id + ",name=" + this.name + ", " + dateOfBirth + "]";
	}
}
