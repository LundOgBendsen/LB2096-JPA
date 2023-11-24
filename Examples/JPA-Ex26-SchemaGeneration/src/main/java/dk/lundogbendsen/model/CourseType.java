package dk.lundogbendsen.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class CourseType {

	@Id
	private String number;

	private String name;

	private Integer lengthInDays;

	@OneToMany(mappedBy="courseType")
	private Collection<Course> instances = new LinkedList<Course>();
	
	@SuppressWarnings("unused")
	private CourseType() {
		// Must have no-args constructor
	}

	public String getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLengthInDays() {
		return lengthInDays;
	}

	public void setLengthInDays(Integer lengthInDays) {
		this.lengthInDays = lengthInDays;
	}
	
	public Course createCourseInstance(int startYear, int startMonth, int startDay) {
		Period period = new Period(startYear, startMonth, startDay, this.lengthInDays);
		Course instance = new Course(this, period);
		this.instances.add(instance);
		return instance;
	}

	public CourseType(String number, String name, int lengthInDays) {
		this.number = number;
		this.name = name;
		this.lengthInDays = lengthInDays;
	}

	public String toString() {
		List<Integer> idsOfInstances = new LinkedList<Integer>();
		for(Course instance : instances) {
			idsOfInstances.add(instance.getId());
		}
		String s = "CourseType[number=" + number + ",name='" + name + "',lengthInDays=" + lengthInDays;
		s += ",idsOfInstances=" + idsOfInstances + "]";
		return s;
	}
}
