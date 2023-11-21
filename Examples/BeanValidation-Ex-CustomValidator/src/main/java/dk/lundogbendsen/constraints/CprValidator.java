package dk.lundogbendsen.constraints;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import dk.lundogbendsen.service.CprService;
import dk.lundogbendsen.service.CprServiceMock;
import dk.lundogbendsen.service.CprStatus;

public class CprValidator implements ConstraintValidator<Cpr, String> {

	private List<CprStatus> allowedStatuses;
	
	private CprService service = new CprServiceMock();
	
	@Override
	public void initialize(Cpr constraint) {
		allowedStatuses = Arrays.asList(constraint.value());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null){
			// JSR 303 practice is to return true upon null values.
			// It is thereby up to the @NotNull annotation to invalidate
			// a null value 			
			return true;
		}
		
		CprStatus status =  service.getStatus(value);
		if(status == null){
			// not existing
			return false;
		}
		
		return allowedStatuses.contains(status);
	}

	
}
