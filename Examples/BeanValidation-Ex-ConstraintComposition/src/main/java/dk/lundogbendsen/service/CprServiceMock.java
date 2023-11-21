package dk.lundogbendsen.service;


public class CprServiceMock implements CprService {

	@Override
	public CprStatus getStatus(String cpr) {
		if(cpr.endsWith("55"))
			return CprStatus.DEAD;
			else{
				return CprStatus.ALIVE;
			}
	}

	
}
