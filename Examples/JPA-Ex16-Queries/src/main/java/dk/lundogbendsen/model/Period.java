package dk.lundogbendsen.model;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Embeddable
public class Period {
	@Temporal(TemporalType.DATE)
	private Calendar fromDate;

	// null means still active
	@Temporal(TemporalType.DATE)
	private Calendar toDate;

	public Period() {
		// We must have a no-args constructor
	}

	public Period(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay) {
		this(fromYear, fromMonth, fromDay);
		setTo(toYear, toMonth, toDay);
	}

	public Period(int fromYear, int fromMonth, int fromDay) {
		setFrom(fromYear, fromMonth, fromDay);
	}

	public Period(int fromYear, int fromMonth, int fromDay, int noOfDays) {
		setFrom(fromYear, fromMonth, fromDay);
		Calendar to = (Calendar) getFrom().clone();
		to.add(Calendar.DATE, noOfDays - 1);
		this.toDate = to;
	}

	public void setFrom(Calendar from) {
		this.fromDate = from;
	}

	public Calendar getFrom() {
		return fromDate;
	}

	public void setTo(Calendar to) {
		this.toDate = to;
	}

	public Calendar getTo() {
		return toDate;
	}

	public boolean hasStart() {
		return fromDate != null;
	}

	public boolean hasEnd() {
		return toDate != null;
	}

	public String getFromAsString() {
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
		return fromDate == null ? "?" : dateFormat.format(new Date(fromDate.getTimeInMillis()));
	}

	public String getToAsString() {
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
		return toDate == null ? "?" : dateFormat.format(new Date(toDate.getTimeInMillis()));
	}

	public void setFrom(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		fromDate = calendar;
	}

	public void setTo(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		toDate = calendar;
	}

	public int getLenghInDaysRounded() {
		int millisPerSecond = 1000;
		int millisPerMinute = 60 * millisPerSecond;
		int millisPerHour = 60 * millisPerMinute;
		int millisPerDay = 24 * millisPerHour;
		return (int) Math.round((toDate.getTimeInMillis() - fromDate.getTimeInMillis()) / (double) millisPerDay + 1);
	}

	@Override
	public String toString() {
		String s = "Period[" + getFromAsString();
		s += " -> " + getToAsString() + "]";
		return s;
	}

	public static void main(String[] args) {
		System.out.println(new Period(2008, 12, 10, 2008, 12, 15).getLenghInDaysRounded());
		System.out.println(new Period(2008, 12, 10, 5).getLenghInDaysRounded());
	}
}
