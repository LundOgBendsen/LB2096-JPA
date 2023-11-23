package lundogbendsen.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class StudentRole extends Role {

	public StudentRole() {
		super();
	}

	public StudentRole(Person personAssignedToRole, Period period) {
		super(personAssignedToRole, period);
	}
	
	@ManyToMany(mappedBy="students")
	private Collection<Course> coursesAttended = new LinkedList<Course>();

	// Only used by Course.addStudent(..)
	void addCourse(Course course) {
		this.coursesAttended.add(course);
	}
	
	// Only used by Course.removeStudent(..)
	void removeCourse(Course course) {
		this.coursesAttended.remove(course);
	}
	
	public Collection<Course> getCoursesAttended() {
		return Collections.unmodifiableCollection(this.coursesAttended);
	}

	@Override
	public String toStringExtraFields() {
		List<Integer> idsOfCoursesAttended = new LinkedList<Integer>();
		for(Course course : this.coursesAttended) {
			idsOfCoursesAttended.add(course.getId());
		}
		return ",idsOfCoursesAttended=" + idsOfCoursesAttended.toString();
	}
}
