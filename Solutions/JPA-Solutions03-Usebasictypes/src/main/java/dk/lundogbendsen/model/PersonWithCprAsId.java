package dk.lundogbendsen.model;

import jakarta.persistence.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@SuppressWarnings("serial")
@Entity
public class PersonWithCprAsId implements Serializable {

	@Id
	private String cprNo;

	private String name;
	private double height;
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

	public PersonWithCprAsId(String cprNo, String name) {
		this.cprNo = cprNo;
		this.name = name;
	}

	public PersonWithCprAsId() {
	}

	public PersonWithCprAsId(String name) {
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

	public void readPhotoFromFile(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			this.photo = bytes;
		} catch (IOException e) {
			String msg = "Could not read the file [" + file + "]";
			throw new RuntimeException(msg, e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
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

	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Address getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PersonWithCprAsId{" +
				"cprNo='" + cprNo + '\'' +
				", name='" + name + '\'' +
				", height=" + height +
				", address=" + address +
				", birthDate=" + birthDate +
				", gender=" + gender +
				'}';
	}

	public static enum Gender {
		MALE, FEMALE, OTHER
	};
}
