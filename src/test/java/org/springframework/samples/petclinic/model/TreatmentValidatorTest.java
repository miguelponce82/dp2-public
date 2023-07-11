package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class TreatmentValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotValidateWhenRecomendationsIsEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Treatment treatment = new Treatment();
		treatment.setRecomendations("");
		treatment.setStartDate(LocalDate.now().plusYears(1));
		treatment.setEndDate(LocalDate.now().plusYears(2));
		treatment.setId(1);
		

		Validator validator = createValidator();
		
		Set<ConstraintViolation<Treatment>> constraintViolations = validator.validate(treatment);
		ConstraintViolation<Treatment> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("recomendations");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
		assertThat(constraintViolations.size()).isEqualTo(1);

	}
	
	@Test
	void shouldNotValidateWhenStartDateIsEmpty() {
		Treatment treatment = new Treatment();
		treatment.setRecomendations("Hi");
		treatment.setEndDate(LocalDate.now().plusYears(2));
		treatment.setId(1);
		
		Validator validator = createValidator();
		
		Set<ConstraintViolation<Treatment>> constraintViolations = validator.validate(treatment);
		ConstraintViolation<Treatment> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("startDate");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	
	@Test
	void shouldNotValidateWhenEndDateIsEmpty() {
		Treatment treatment = new Treatment();
		treatment.setRecomendations("Hi");
		treatment.setStartDate(LocalDate.now().plusYears(1));
		treatment.setId(1);
		
		Validator validator = createValidator();
		
		Set<ConstraintViolation<Treatment>> constraintViolations = validator.validate(treatment);
		ConstraintViolation<Treatment> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("endDate");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	

	


	

}
