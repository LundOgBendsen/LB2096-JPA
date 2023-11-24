package dk.lundogbendsen.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class ProductId implements Serializable{
	
	private String madeByCompany;

	private Integer serial;

	public ProductId() {
	}

	public ProductId(String madeByCompany, Integer serial) {
		this.madeByCompany = madeByCompany;
		this.serial = serial;
	}

	public String getMadeByCompany() {
		return madeByCompany;
	}
	
	public Integer getSerial() {
		return serial;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((madeByCompany == null) ? 0 : madeByCompany.hashCode());
		result = prime * result + ((serial == null) ? 0 : serial.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProductId other = (ProductId) obj;
		if (madeByCompany == null) {
			if (other.madeByCompany != null)
				return false;
		} else if (!madeByCompany.equals(other.madeByCompany))
			return false;
		if (serial == null) {
			if (other.serial != null)
				return false;
		} else if (!serial.equals(other.serial))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProductId[madeByCompany=" + madeByCompany + ",serial=" + serial + "]";
	}
}
