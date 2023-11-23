package lundogbendsen.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class PersonWithPhone implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@OneToMany(mappedBy="owner")
	private Map<String, Phone> ownedPhones = new HashMap<>();

	public PersonWithPhone() {
	}

	public PersonWithPhone(String name) {
		this.name = name;
	}

	public void addOwnedPhone(String type, Phone newPhone) {
		this.ownedPhones.put(type, newPhone);
		newPhone.setOwner(this);
	}

	public void removeOwnedPhone(Phone phone) {
		this.ownedPhones.remove(phone);
		phone.setOwner(null);
	}

	public Map<String, Phone> getOwnedPhones() {
		return Collections.unmodifiableMap(this.ownedPhones);
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
		List<String> phoneNumbers = new LinkedList<String>();
		for (Map.Entry<String, Phone> entry : this.ownedPhones.entrySet()) {
			phoneNumbers.add("[" + entry.getKey() + " " + entry.getValue().getNumber() + "]");
		}
		String msg = "Person[id=" + id + ",name=" + name;
		msg += ",phoneNumbers=" + phoneNumbers + "]";
		return msg;
	}
}
