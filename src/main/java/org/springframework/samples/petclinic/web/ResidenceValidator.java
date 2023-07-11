package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Residence;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ResidenceValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Residence.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		Residence residence = (Residence) obj;
		if(residence.getDateEnd() == null || residence.getDateStart() == null) {
			if(residence.getDateStart() == null) {
				errors.rejectValue("dateStart", "You must write a start date", "You must write a start date" );
			}
			else {
				errors.rejectValue("dateEnd", "You must write a finnish date", "You must write a finnish date" );
			}
		}
		
		else {
			if(residence.getDateStart().isBefore(LocalDate.now())) {
				errors.rejectValue("dateStart", "You must write a start date later than today", "You must write a start date later than today" );
			}
			if(residence.getDateStart().isAfter(residence.getDateEnd())) {
				errors.rejectValue("dateEnd", "You must write a finnish date later than the start date", "You must write a finnish date later than the start date" );
		}
	
	}
	

}
	
}
