package lundogbendsen.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CourseDto implements Serializable {
	private String courseNumber;
	private Period period;
	private String instructorName;

	public CourseDto(String courseNumber, Period period, String instructorName) {
		super();
		this.courseNumber = courseNumber;
		this.period = period;
		this.instructorName = instructorName;
	}

	@Override
	public String toString() {
		String s = "CourseDto[";
		s += "courseNumber=" + courseNumber;
		s += ",period=" + period;
		s += ",instructorName=" + instructorName;
		s += "]";
		return s;
	}

}
