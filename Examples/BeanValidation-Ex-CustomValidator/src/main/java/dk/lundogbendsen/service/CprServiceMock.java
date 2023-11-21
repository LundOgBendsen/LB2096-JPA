package dk.lundogbendsen.service;


public class CprServiceMock implements CprService {

	@Override
	public CprStatus getStatus(String cpr) {
		if("1122334455".equals(cpr))
			return CprStatus.DEAD;
			else{
				return CprStatus.ALIVE;
			}
	}

	
}
