package dk.lundogbendsen.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class OrderLine implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private int unitCount;
	
	@ManyToOne
	private Order order;

	@ManyToOne
	private Product product;
	

	public OrderLine() {}
	
	public OrderLine(int unitCount, Product product) {
		this.unitCount = unitCount;
		this.product = product;
	}

	public Integer getId() {
		return id;
	}

	public int getUnitCount() {
		return unitCount;
	}

	public void setUnitCount(int unitCount) {
		this.unitCount = unitCount;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	Order getOrder() {
		return order;
	}

	/*
	 * This method is assumed only used by Order instances to ensure referential integrity
	 */
	void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderLine[id=" + id + ",unitCount=" + this.unitCount + ",product=" + this.product.getName() +"]";
	}

}
