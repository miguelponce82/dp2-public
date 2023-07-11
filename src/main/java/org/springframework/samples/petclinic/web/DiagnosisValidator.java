package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DiagnosisValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Diagnosis.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		
		Diagnosis diagnosis = (Diagnosis) obj;
		if(diagnosis.getDescription() == null || diagnosis.getDescription() == "") {
			errors.rejectValue("description", "Description must not be empty", "Description must not be empty");
		}
		if(diagnosis.getVet() == null) {
			errors.rejectValue("vet","You must select a vet", "You must select a vet");
		}
		
		if(diagnosis.getStatus() == null) {
			errors.rejectValue("status","You must select a status", "You must select a status");
		}
		


//		if(diagnosis.getResidence().getDateStart() != null) {
//			
//			 if( diagnosis.getResidence().getDateStart().isBefore(LocalDate.now())) {
//					errors.rejectValue("residence.dateStart", "You must write a start date later than today", "You must write a start date later than today");
//
//			 }
//			 
//			 if(diagnosis.getResidence().getDateEnd() == null) {
//				errors.rejectValue("residence.dateStart", "You must write a finnish date", "You must write a finnish date");
//
//			 }
//			 else {
//				 
//			 if(diagnosis.getResidence().getDateEnd().isBefore(diagnosis.getResidence().getDateStart())) {
//					errors.rejectValue("residence.dateEnd", "You must write a finnish date later than the start date", 
//							"You must write a finnish date later than the start date");
//			 }
//		}
//		}
//		if(diagnosis.getResidence().getDateEnd() != null && diagnosis.getResidence().getDateStart() == null) {
//			errors.rejectValue("residence.dateEnd","You can not write a finnish date if you do not write a start date",
//					"You can not write a finnish date if you do not write a start date");
//		}
//		
//		if(diagnosis.getStatus() != null && diagnosis.getStatus().equals(Status.NOT_DANGER) && diagnosis.getResidence().getDateEnd() != null
//				&& diagnosis.getResidence().getDateStart() != null ) {
//			errors.rejectValue("residence.dateStart","You can not add a residence if the pet is not in danger",
//					"You can not add a residence if the pet is not in danger");
//		}
		
	
	}

}
