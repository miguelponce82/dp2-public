package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class MedicineValidatorTest {


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Disabled
	@Test
	void shouldNotValidateWhenDescriptionIsEmpty() {
		Medicine m = new Medicine();
		m.setId(1);
		m.setSideEffects("Keep working");

		Validator validator = createValidator();
		Set<ConstraintViolation<Medicine>> constraintViolations = validator.validate(m);
		ConstraintViolation<Medicine> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	
	@Disabled
	@Test
	void shouldNotValidateWhenSideEffectsIsEmpty() {
		Medicine m = new Medicine();
		m.setId(1);
		m.setDescription("Take a coffee");

		Validator validator = createValidator();
		Set<ConstraintViolation<Medicine>> constraintViolations = validator.validate(m);
		ConstraintViolation<Medicine> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("sideEffects");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	
}
