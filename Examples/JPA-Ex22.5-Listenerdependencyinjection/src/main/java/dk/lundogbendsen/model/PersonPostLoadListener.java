package dk.lundogbendsen.model;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;

public class PersonPostLoadListener {

	@PostLoad
	public void onPersonPostLoad(Person person) {
		System.out.println("--==<[PersonPostLoadListener.onPersonPostLoad(id=" + person.getId() + ") ]>==--");
	}

	@PostPersist
	public void onPersonPostPersist(Person person) {
		System.out.println("--==<[PersonPostLoadListener.onPersonPostPersist(id=" + person.getId() + ") ]>==--");
	}

}
