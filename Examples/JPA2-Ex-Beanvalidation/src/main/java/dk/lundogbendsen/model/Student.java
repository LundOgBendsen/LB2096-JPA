package dk.lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

import dk.lundogbendsen.validationgroups.Admin;

@Entity
public class Student {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull(groups = { Admin.class, }, message="Du skal udfylde et navn")
	private String name;

	@Pattern(regexp = ".*@.*\\.dk")
	@NotNull(groups = Default.class)
	private String email;

	@ManyToOne
	@Valid
	private CourseInstance courseInstance;

	public Student(String name, String email, CourseInstance courseInstance) {
		this.name = name;
		this.email = email;
		this.courseInstance = courseInstance;
	}

	public Student() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CourseInstance getCourseInstance() {
		return courseInstance;
	}

	public void setCourseInstance(CourseInstance courseInstance) {
		this.courseInstance = courseInstance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", email=" + email
				+ ", courseInstance.id=" + getCourseInstanceId() + "]";
	}

	private Long getCourseInstanceId() {
		if (courseInstance != null) {
			return courseInstance.getId();
		}
		return null;
	}
}
