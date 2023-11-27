package dk.lundogbendsen.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@SuppressWarnings("serial")
@Entity
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String name;
	
	private BigDecimal cost;
	
	@ManyToMany
	private Collection<Supplier> suppliers = new LinkedList<Supplier>();
	
	public Product() {
	}
	
	public Product(String name, BigDecimal cost) {
		this.name = name;
		this.cost = cost;
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
	
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	// Only used by Supplier.addProduct(..)
	void addSupplier(Supplier supplier) {
		this.suppliers.add(supplier);
	}
	
	// Only used by Supplier.removeProduct(..)
	void removeSupplier(Supplier supplier) {
		this.suppliers.remove(supplier);
	}

	@Override
	public String toString() {
		return "Product[id=" + id + ",name=" + this.name + ",cost=" + this.cost + "]";
	}

}
