package dk.lundogbendsen.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


import org.hibernate.LazyInitializationException;

@SuppressWarnings("serial")
@Entity
public class Supplier implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	private Collection<Product> products = new LinkedList<Product>();

	public Supplier() {
	}

	public Supplier(String name) {
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

	public void addProduct(Product product) {
		this.products.add(product);
		product.addSupplier(this);
	}

	public void removeProduct(Product product) {
		this.products.remove(product);
		product.removeSupplier(this);
	}

	@Override
	public String toString() {
		List<String> productNames = new LinkedList<String>();
		try {
			for (Product product : this.products) {
				productNames.add(product.getName());
			}
		} catch (LazyInitializationException e) {
			// ignore
		}

		return "Supplier[id=" + id + ",name=" + this.name + ",products=" + productNames + "]";
	}
}
