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

		Person sophie = new Person("a122334455", "Sophie Jensen");
		Person nn = new Person("1234567891", null);

		validate(nn);
		validate(sophie);
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
