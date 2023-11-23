package dk.lundogbendsen.unittest;

import java.util.LinkedList;
import java.util.List;

import dk.lundogbendsen.dao.PersonRepository;
import dk.lundogbendsen.model.Person;

// This Class makes use of our DAO class
public class TeamHelper {
	
	// It expects to get an instance of our DAO injected
	private PersonRepository personRepository;
	
	public void setPersonRepository(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	public List<String> createTeamNameList(int... idsOfTeamMembers) {
		List<String> teamNameList = new LinkedList<String>();
		for(int id : idsOfTeamMembers) {
			Person person = personRepository.findPerson(id);
			String name = person.getName();
			teamNameList.add(name);
		}
		return teamNameList;
	}
}
