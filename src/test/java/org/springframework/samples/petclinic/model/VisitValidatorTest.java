package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class VisitValidatorTest {
	

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotValidateWhenPetIsEmpty() {
		
		Vet vet = new Vet();
		vet.setFirstName("Joan");
		vet.setLastName("Neuman");
		vet.setId(1);
		
		Visit v = new Visit();
		v.setId(1);
		v.setDate(LocalDate.now().plusYears(1));
		v.setTime(LocalTime.now());
		v.setDescription("Take a coffee");
		v.setVet(vet);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Visit>> constraintViolations = validator.validate(v);
		ConstraintViolation<Visit> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("pet");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}

	@Test
	void shouldNotValidateWhenVetIsEmpty() {
		PetType pt = new PetType();
		pt.setName("Elephant");
		pt.setId(1);
		
		Pet p = new Pet();
		p.setBirthDate(LocalDate.now().minusYears(1));
		p.setName("Flopy");
		p.setType(pt);
		p.setId(1);
		
		Visit v = new Visit();
		v.setId(1);
		v.setDate(LocalDate.now().plusYears(1));
		v.setTime(LocalTime.now());
		v.setDescription("Take a coffee");
		v.setPet(p);

		
		Validator validator = createValidator();
		Set<ConstraintViolation<Visit>> constraintViolations = validator.validate(v);
		ConstraintViolation<Visit> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("vet");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	

}
