package dk.lundogbendsen.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import org.hibernate.LazyInitializationException;

@SuppressWarnings("serial")
@Entity
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@OneToMany(mappedBy = "personAssignedToRole")
	private Collection<Role> assignedRoles = new LinkedList<Role>();

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	// Only used by Role.Role()
	void addRole(Role role) {
		this.assignedRoles.add(role);
	}

	public Collection<Role> getAssignedRoles() {
		// We don't want anyone to edit the collection of roles directly
		return Collections.unmodifiableCollection(this.assignedRoles);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		List<Integer> idsOfAssignedRoles = new LinkedList<Integer>();
		try{
			for(Role role : assignedRoles) {
				idsOfAssignedRoles.add(role.getId());
			}
		}catch(LazyInitializationException e){
			//ignored
		}
		String msg = "Person[id=" + id + ",name=" + name;
		msg += ",idsO" +
				"fAssignedRoles=" + idsOfAssignedRoles + "]";
		return msg;
	}
}
