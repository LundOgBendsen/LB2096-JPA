package dk.lundogbendsen.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Car implements Serializable{
	
	@Id
	@GeneratedValue
	private Integer id;
	private String make;
	private String model;
	private int price;
	
	
	/*
	 * Default Constructor
	 * Entities must supply a no-args constructor!
	 */
	public Car() {
	}

	public Car(String make, String model, int price) {
		this.make = make;
		this.model = model;
		this.price = price;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * <p>Returns a brief description of this object. The exact
	 * details are unspecified and subject to change, but the 
	 * following may be regarded as typical:</p>
	 * 
	 * <p>Car[id=42 make=Austin, model=Morris Mascot, price=25000]</p>
	 *
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString(){
	    final String delimit = ", ";
	
	    StringBuilder res = new StringBuilder();
	    
	    res.append("Car[")
	        .append("id=").append(this.id).append(delimit)
	        .append("make=").append(this.make).append(delimit)
	        .append("model=").append(this.model).append(delimit)
	        .append("price=").append(this.price).append(delimit)
	        .append("]");
	    
	    return res.toString();
	}

}
