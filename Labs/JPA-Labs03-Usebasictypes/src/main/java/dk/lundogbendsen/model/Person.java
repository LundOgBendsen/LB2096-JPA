package dk.lundogbendsen.model;

import java.io.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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


	
	public Person() {
	}
	
	public Person(String name) {
		this.name = name;
	}

	//this is useful when adding the photo field
	/*
	public void readPhotoFromResource(String file) {
		this.photo = getBytesFromResource(file);
	}
	*/


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
	public void writePhotoToFile(File file, byte[] bytes) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(bytes);
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
				'}';
	}

}
