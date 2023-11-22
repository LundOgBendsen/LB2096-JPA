package dk.lundogbendsen.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Instructor {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToMany(mappedBy = "instructor")
	private List<CourseInstance> courseInstances = new ArrayList<CourseInstance>();

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

	public List<CourseInstance> getCourseInstances() {
		return courseInstances;
	}

	public void setCourseInstances(List<CourseInstance> courseInstances) {
		this.courseInstances = courseInstances;
	}

	@Override
	public String toString() {
		return "Instructor [id=" + id + ", name=" + name + "]";
	}
}