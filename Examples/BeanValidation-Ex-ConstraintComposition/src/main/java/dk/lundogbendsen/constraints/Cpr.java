package dk.lundogbendsen.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import dk.lundogbendsen.service.CprStatus;

@Pattern(regexp="\\d{10}")
@Documented
@Constraint(validatedBy = CprValidator.class)
@Target({ METHOD, FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Cpr {

	public CprStatus[] value();
	
	public String message() default "Invalid CPR number";
	
	public Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
	

}
