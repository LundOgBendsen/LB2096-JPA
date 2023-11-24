package dk.lundogbendsen.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c"),
	@NamedQuery(name = "Course.findAllIds", query = "SELECT c.id FROM Course c") 
})
public class Course {

	@Id
	@GeneratedValue
	private Integer id;

	private Period period;

	@ManyToOne
	private CourseType courseType;

	@ManyToMany
	private Collection<StudentRole> students = new LinkedList<StudentRole>();

	@ManyToOne
	private InstructorRole instructor;

	@SuppressWarnings("unused")
	private Course() {
		// Must have no-args constructor
	}

	public Course(CourseType courseType, Period period) {
		if (courseType == null) {
			throw new NullPointerException("Parameter [courseType] must not be null");
		}
		if (period == null) {
			throw new NullPointerException("Parameter [period] must not be null");
		}
		this.courseType = courseType;
		this.period = period;
	}

	public void setInstructor(Person instructor) {
		Collection<Role> assignedRoles = instructor.getAssignedRoles();
		InstructorRole instructorRole = null;
		for (Role role : assignedRoles) {
			if (role instanceof InstructorRole) {
				instructorRole = (InstructorRole) role;
				break;
			}
		}
		if (instructorRole == null) {
			String msg = "The person [" + instructor + "] is not assigned an instructor role";
			throw new IllegalArgumentException(msg);
		}
		if (this.instructor != null) {
			this.instructor.removeCourse(this);
		}
		instructorRole.addCourse(this);
		this.instructor = instructorRole;
	}

	public void addStudent(Person student) {
		Collection<Role> assignedRoles = student.getAssignedRoles();
		StudentRole studentRole = null;
		for (Role role : assignedRoles) {
			if (role instanceof StudentRole) {
				studentRole = (StudentRole) role;
				break;
			}
		}
		if (studentRole == null) {
			String msg = "The person [" + student + "] is not assigned a student role";
			throw new IllegalArgumentException(msg);
		}
		studentRole.addCourse(this);
		this.students.add(studentRole);
	}

	public InstructorRole getInstructor() {
		return instructor;
	}

	public Period getPeriod() {
		return period;
	}

	public CourseType getCourseType() {
		return courseType;
	}

	public Integer getId() {
		return id;
	}

	public Collection<StudentRole> getStudents() {
		return Collections.unmodifiableCollection(students);
	}

	public String getInstructorName() {
		if (this.instructor == null) {
			return "UNKNOWN";
		} else {
			Person person = this.instructor.getPersonAssignedToRole();
			if (person != null) {
				return person.getName();
			} else {
				return "UNKNOWN";
			}
		}
	}

	public String getCourseNumber() {
		if (this.courseType != null) {
			return this.courseType.getNumber();
		} else {
			return null;
		}
	}

	public List<String> getNamesOfStudents() {
		List<String> studentNames = new LinkedList<String>();
		for (StudentRole studentRole : this.students) {
			Person personAssignedToRole = studentRole.getPersonAssignedToRole();
			String studentName = null;
			if (personAssignedToRole != null) {
				studentName = personAssignedToRole.getName();
			}
			studentNames.add(studentName == null ? "UNKNOWN" : studentName);
		}
		return studentNames;
	}

	@Override
	public String toString() {
		String s = "Course[";
		s += "courseNumber=" + getCourseNumber();
		s += ",period=" + this.period;
		s += ",instructorName=" + getInstructorName();
		s += ",namesOfStudents=" + getNamesOfStudents();
		s += "]";
		return s;
	}
}
