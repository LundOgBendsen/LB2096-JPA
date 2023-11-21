package dk.lundogbendsen.examples;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import dk.lundogbendsen.model.Person;

public class BeanValidationStandaloneClient {

	private static ValidatorFactory factory;

	public static void main(String[] args) {
		factory = Validation.buildDefaultValidatorFactory();

		Person sophie = new Person("1234567891", "Sophie Larsson");
		Person marge = new Person("1122334455", "Marge Jensen");

		validate(sophie);
		validate(marge);

	}

	public static void validate(Person person) {
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<Person>> violations = validator
				.validate(person);
		
		for (ConstraintViolation<Person> violation : violations) {
			System.out.println(violation.getRootBeanClass().getSimpleName()
					+ "[" + violation.getPropertyPath().toString() + "="
					+ violation.getInvalidValue() + "] : "
					+ violation.getMessage());
		}

	}
}
