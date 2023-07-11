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
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.ResidenceService;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.samples.petclinic.service.exceptions.NoStatusProperlyForResidenceException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class ResidenceController {

	private final ResidenceService residenceService;
	private final DiagnosisService diagnosisService;

	@Autowired
	public ResidenceController(DiagnosisService diagnosisService, ResidenceService residenceService) {
		this.residenceService = residenceService;
		this.diagnosisService = diagnosisService;
	}
	
	@InitBinder("diagnosis")
	public void initOwnerBinder1(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("residence")
	public void initDiagnosisBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ResidenceValidator());
	}
	@GetMapping(value="/vets/{vetId}/diagnosis/{diagnosisId}/residence/new")
	public String initNewResidenceForm(@PathVariable("diagnosisId") int diagnosisId, ModelMap model ) {
		Residence residence = new Residence();
		Diagnosis diagnosis = this.diagnosisService.findDiagnosisById(diagnosisId);
		diagnosis.setResidence(residence);
		model.put("residence", residence);
		model.put("diagnosis", diagnosis);
		return "vets/createResidenceForm";
	}
	
	@PostMapping(value="/vets/{vetId}/diagnosis/{diagnosisId}/residence/new")
	public String processNewResidenceForm(@Valid Residence residence, BindingResult result, ModelMap model,@PathVariable("diagnosisId") int diagnosisId ) {
		Diagnosis d = this.diagnosisService.findDiagnosisById(diagnosisId);
	
		if (result.hasErrors()) {
		model.put("diagnosis", d);
		return "vets/createResidenceForm";
		}
		
		if(d.getResidence()!=null) {
			result.rejectValue("dateStart", "It is no allowed to create more than one residence for each diagnosis",
					"It is no allowed to create more than one residence for each diagnosis");
			model.put("diagnosis", d);
			return "vets/createResidenceForm";
		}
		else {
			try {
				d.setResidence(residence);
				this.residenceService.saveResidence(residence, d);
			}
			catch(NoStatusProperlyForResidenceException ex) {
				result.rejectValue("dateStart", "It is no allowed to create a residence when the diagnosis status is not danger",
						"It is no allowed to create a residence when the diagnosis status is not danger");
				d.setResidence(null);
				model.put("diagnosis", d);
				return "vets/createResidenceForm";
			}

		}
		return "redirect:/vets/{vetId}/diagnosis";
	}

}
