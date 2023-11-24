package dk.lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Engine {

	@Id
	@GeneratedValue
	private Integer id;

	private int cylinderCount;

	public Engine() {
	}

	public Engine(int cylinderCount) {
		this.cylinderCount = cylinderCount;
	}

	public Integer getId() {
		return id;
	}

	public void setCylinderCount(int cylinderCount) {
		this.cylinderCount = cylinderCount;
	}

	public int getCylinderCount() {
		return cylinderCount;
	}

	@Override
	public String toString() {
		return "Engine[id=" + id + ",cylinderCount=" + cylinderCount + "]";
	}
}
