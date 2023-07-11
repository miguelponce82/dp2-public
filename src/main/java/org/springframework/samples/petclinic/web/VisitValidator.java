/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.samples.petclinic.model.Visit;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class VisitValidator implements Validator {


	@Override
	public void validate(Object obj, Errors errors) {
		Visit visit = (Visit) obj;
		if(visit.getVet() == null) {
			errors.rejectValue("vet","You must select a vet", "You must select a vet");
		}
		
		if(visit.getDate() == null || visit.getTime() == null) {
		if(visit.getDate() == null ) {
			errors.rejectValue("date","You must select a date", "You must select a date");
		}
		if(visit.getTime() == null) {
			errors.rejectValue("time","You must select a time", "You must select a time");
		}
		
		}
		
		else {
		
		LocalDate d = visit.getDate();
		LocalTime t = visit.getTime();

		if(visit.getDescription().equals("")) {
			errors.rejectValue("description","You must write a description", "You must write a description");

		}
		
		if (d.getDayOfWeek().getValue() == 7) {
			errors.rejectValue("date","Our pet clinic does not work on Sunday", "Our pet clinic does not work on Sunday");}

	
		if(t.getHour() >= 14 && t.getHour() < 16) {
			errors.rejectValue("time","Our pet clinic does not work between 14:00 and 16:00", "Our pet clinic does not work between 14:00 and 16:00");}

		if( d.isBefore(LocalDate.now())) {	
			errors.rejectValue("date","Invalid date, the visist should not be before than now", "Invalid date, the visist should not be before than now");
		}
	
		if (d.equals(LocalDate.now()) && (t.equals(LocalTime.now()) || t.isBefore(LocalTime.now()))) {
			
			errors.rejectValue("time","Invalid date, the visist should not be before than now", "Invalid date, the visist should not be before than now");

		}
		}
	}

	public boolean supports(Class<?> clazz) {
		return Visit.class.isAssignableFrom(clazz);
	}

}
