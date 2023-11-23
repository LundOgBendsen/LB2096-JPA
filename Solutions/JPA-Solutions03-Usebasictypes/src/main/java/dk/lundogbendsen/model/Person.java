package dk.lundogbendsen.model;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;
	private double height;

	@Column(length = 11)
	private String cprNo;
	private Address address;

	@Temporal(TemporalType.DATE)
	private LocalDate birthDate;

	// @Enumerated(EnumType.ORDINAL) // Default
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length = 8 * 1024 * 1024)
	private byte[] photo;
	

	
	public Person() {
	}
	
	public Person(String name) {
		this.name = name;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	public void setCprNo(String cprNo) {
		this.cprNo = cprNo;
	}

	public String getCprNo() {
		return cprNo;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Gender getGender() {
		return gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getBirthDateAsString() {
		DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
		return birthDate.format(pattern);
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public void setBirthDate(int year, int month, int day) {
		this.birthDate = LocalDate.of(year, month, day);
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public byte[] getPhoto() {
		return photo;
	}


	public void readPhotoFromResource(String file) {
		this.photo = getBytesFromResource(file);
	}

	public byte[] getBytesFromResource(String file) {
		//FileInputStream fis = null;
		try (InputStream fis = ClassLoader.getSystemClassLoader().getResourceAsStream(file)) {
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			return bytes;
		} catch (IOException e) {
			String msg = "Could not read the file [" + file + "]";
			throw new RuntimeException(msg, e);
		}
	}
	public void writePhotoToFile(File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(this.photo);
		} catch (IOException e) {
			String msg = "Could note write to the file [" + file + "]";
			throw new RuntimeException(msg, e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Address getAddress() {
		return address;
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

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", height=" + height +
				", cprNo='" + cprNo + '\'' +
				", address=" + address +
				", birthDate=" + birthDate +
				", gender=" + gender +
				'}';
	}

	public static enum Gender {
		MALE, FEMALE, OTHER
	};
}
