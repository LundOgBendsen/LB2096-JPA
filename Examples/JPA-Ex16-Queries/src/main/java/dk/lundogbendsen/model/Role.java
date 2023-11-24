package dk.lundogbendsen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Role {

	@Id
	@GeneratedValue
	private Integer id;

	private Period period;
	
	@ManyToOne
	private Person personAssignedToRole;
	
	public Role() {
	}
	
	public Role(Person personAssignedToRole, Period period) {
		personAssignedToRole.addRole(this);
		this.personAssignedToRole = personAssignedToRole;
		this.period = period;
	}
	
	public Integer getId() {
		return id;
	}

	public Person getPersonAssignedToRole() {
		return personAssignedToRole;
	}
	
	public Period getPeriod() {
		return period;
	}
	
	@Override
	public String toString() {
		String msg = getClass().getSimpleName();
		msg += "[id=" + id + ",period=" + this.period;
		msg += ",idOfPersonAssignedToRole=" + (personAssignedToRole != null ? personAssignedToRole.getId() : null);
		msg += toStringExtraFields() + "]";
		return msg;
	}
	
	public abstract String toStringExtraFields();
}
