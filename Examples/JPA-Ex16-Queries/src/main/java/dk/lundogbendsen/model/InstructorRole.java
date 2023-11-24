package dk.lundogbendsen.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class InstructorRole extends Role {

	public static enum Seniority {JUNIOR, SENIOR};
	
	private Seniority seniority;
	
	public InstructorRole() {
		super();
	}

	public InstructorRole(Person personAssignedToRole, Period period, Seniority seniority) {
		super(personAssignedToRole, period);
		this.seniority = seniority;
	}
	
	@OneToMany(mappedBy="instructor")
	private Collection<Course> coursesInstructed = new LinkedList<Course>();

	// Only used by Course.setInstructor(..)
	void addCourse(Course course) {
		this.coursesInstructed.add(course);
	}
	
	// Only used by Course.removeInstructor(..)
	void removeCourse(Course course) {
		this.coursesInstructed.remove(course);
	}
	
	public Collection<Course> getCoursesInstructed() {
		return Collections.unmodifiableCollection(this.coursesInstructed);
	}

	@Override
	public String toStringExtraFields() {
		List<Integer> idsOfCoursesInstructed = new LinkedList<Integer>();
		for(Course course : this.coursesInstructed) {
			idsOfCoursesInstructed.add(course.getId());
		}
		return ",seniority=" + seniority + ",idsOfCoursesInstructed=" + idsOfCoursesInstructed.toString();
	}
	
}
