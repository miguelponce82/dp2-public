package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MedicineValidator implements Validator{
	
	private static final String REQUIRED = "Required";

    @Override
    public boolean supports(Class<?> clazz) {
        return Medicine.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        Medicine med = (Medicine) obj;
        String name= med.getName();

        if (!StringUtils.hasLength(name) || name.length() > 50 || name.length() < 3) {
            errors.rejectValue("name", REQUIRED + " and between 3 and 50 characters", REQUIRED + " and between 3 and 50 character");
        }

        if(med.getName() == null || med.getName()=="") {
            errors.rejectValue("name", "Name must not be empty", "Name must not be empty");
        }
        
        if(med.getDescription() == null || med.getDescription() == "") {
            errors.rejectValue("description", "Description must not be empty", "Description must not be empty");
        }
        
        if(med.getSideEffects() == null || med.getSideEffects() == "") {
            errors.rejectValue("sideEffects", "Side effects must not be empty", "Side effects must not be empty");
        }
    }
	
}
