package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class DiagnosisValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotValidateWhenDescriptionIsEmpty() {
		Vet v = new Vet();
		v.setId(1);
		v.setFirstName("John");
		v.setLastName("Neuman");
		
		Diagnosis d = new Diagnosis();
		d.setId(1);
		d.setStatus(Status.NOT_DANGER);
		d.setVet(v);

		
		
		Validator validator = createValidator();
		
		Set<ConstraintViolation<Diagnosis>> constraintViolations = validator.validate(d);
		ConstraintViolation<Diagnosis> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	
	@Test
	void shouldNotValidateWhenStatusIsEmpty() {
		Vet v = new Vet();
		v.setId(1);
		v.setFirstName("John");
		v.setLastName("Neuman");
		
		Diagnosis d = new Diagnosis();
		d.setId(1);
		d.setDescription("Take a coffee");
		d.setVet(v);

		
		
		Validator validator = createValidator();
		
		Set<ConstraintViolation<Diagnosis>> constraintViolations = validator.validate(d);
		ConstraintViolation<Diagnosis> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("status");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	
	@Test
	void shouldNotValidateWhenVetIsEmpty() {

		
		Diagnosis d = new Diagnosis();
		d.setId(1);
		d.setStatus(Status.NOT_DANGER);
		d.setDescription("Take a coffee");

		
		
		Validator validator = createValidator();
		
		Set<ConstraintViolation<Diagnosis>> constraintViolations = validator.validate(d);
		ConstraintViolation<Diagnosis> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("vet");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	
	

}
