package dk.lundogbendsen.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Product {

	@EmbeddedId
	private ProductId id;

	private String productName;

	// Entities must have a no-args constructor!
	public Product() {
	}

	public Product(String madeByCompany, Integer serial, String productName) {
		id = new ProductId(madeByCompany, serial);
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "Product[madeByCompany=" + id.getMadeByCompany() + ",serial=" + id.getSerial() + ",productName=" + productName + "]";
	}
}
