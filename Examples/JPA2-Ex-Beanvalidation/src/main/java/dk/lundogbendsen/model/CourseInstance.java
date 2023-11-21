package dk.lundogbendsen.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class CourseInstance {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Date date;

	@ManyToOne
	private CourseDescription courseDescription;

	@ManyToOne
	private Instructor instructor;

	@OneToMany(mappedBy = "courseInstance")
	private List<Student> students = new ArrayList<Student>();
	
	private int price;

	public CourseInstance(Instructor i, Date d) {
		instructor = i;
		date = d;
	}

	public CourseInstance() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CourseDescription getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(CourseDescription courseDescription) {
		this.courseDescription = courseDescription;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "CourseInstance [id=" + id + ", date=" + date + ", instructor="
				+ instructor + ", price=" + price + ",courseDescription=" + courseDescription
				+ ", students=" + students + "]";
	}

}
