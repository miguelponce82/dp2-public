package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TreatmentValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Treatment.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Treatment treatment = (Treatment) obj;
		if(treatment.getRecomendations() == "" || treatment.getRecomendations() == null) {
			errors.rejectValue("recomendations","You must write a recomendation", "You must write a recomendation");
		}
		
		if(treatment.getStartDate() == null || treatment.getEndDate() == null) {
			if(treatment.getStartDate() == null) {
			errors.rejectValue("startDate","You must write a start date", "You must write a start date");
			}
			else {
			errors.rejectValue("endDate","You must write a finish date", "You must write a finish date");

			}

		}
		else {
		
		if(treatment.getStartDate().isBefore(LocalDate.now())) {
			errors.rejectValue("startDate","You must write a start date later than today", "You must write a start date later than today");
		}
		if(treatment.getStartDate().isAfter(treatment.getEndDate())){
			errors.rejectValue("endDate","You must write a finish date later than the start date", "You must write a finish date later than start date");
		}
		}
	}

}
