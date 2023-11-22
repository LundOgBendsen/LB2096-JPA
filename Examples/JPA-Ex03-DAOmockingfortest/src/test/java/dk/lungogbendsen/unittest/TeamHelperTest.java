package dk.lungogbendsen.unittest;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import dk.lundogbendsen.string.util.StringUtil;

import dk.lungogbendsen.model.Person;

public class TeamHelperTest {

	@Test
	public void testCreateTeamNameList() {
		// The class TeamHelper that we are testing expects to get an instance
		// of a class, that implements PersonRepository injected. We don't want
		// to use the database in out tests, so we use our Mock-DAO instead.
		PersonRepositoryTestMock mock = new PersonRepositoryTestMock();
		// We put the data teamHelper expects to find in our test mock
		mock.addPerson(34, new Person("Lise"));
		mock.addPerson(243, new Person("Berit"));

		TeamHelper teamHelper = new TeamHelper();
		// Now we inject our mock-DAO in teamHelper instead of a real DAO with
		// connection to the database.
		teamHelper.setPersonRepository(mock);
		
		// Now test that the teamHelper reacts correctly when we use the method
		// createTeamNameList(int...).
		List<String> nameList = teamHelper.createTeamNameList(34, 243);
		assertTrue(nameList.get(0).equals("Lise"));
		StringUtil.prettyPrintHeadline("nameList.get(0)=" + nameList.get(0));
		
		assertTrue(nameList.get(1).equals("Berit"));
		StringUtil.prettyPrintHeadline("nameList.get(1)=" + nameList.get(1));
	}
}
