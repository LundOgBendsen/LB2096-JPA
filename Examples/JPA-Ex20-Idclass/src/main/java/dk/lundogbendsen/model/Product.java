package dk.lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(ProductId.class)
public class Product {

	@Id
	private String madeByCompany;

	@Id
	private Integer serial;

	private String productName;

	// Entities must have a no-args constructor!
	public Product() {
	}

	public Product(String madeByCompany, Integer serial, String productName) {
		this.madeByCompany = madeByCompany;
		this.serial = serial;
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "Product[madeByCompany=" + madeByCompany + ",serial=" + serial + ",productName=" + productName + "]";
	}
}
