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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ResidenceService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * <p/>
 * Also see how the bean of type 'SimpleMappingExceptionResolver' has been declared inside
 * /WEB-INF/mvc-core-config.xml
 */
@Controller
public class DashboardController {
		
	private final DiagnosisService diagnosisService;
	
	private final PetService petService;
	
	private final VetService vetService;
	
	private final ResidenceService residenceService;
	
	

	@Autowired
	public DashboardController(ResidenceService residenceService, DiagnosisService diagnosisService, PetService petService, VetService vetService) {
		this.diagnosisService= diagnosisService;
		this.petService= petService;
		this.vetService= vetService;
		this.residenceService= residenceService;
	}

	@GetMapping(value = "/admin")
	public String dashboard(Map<String, Object> model) {
			String vetWithMostVisit = getVetWithMostVisits();
			model.put("vetWithMostVisits", vetWithMostVisit);
			return "admin/dashboard";
		}
	
	@GetMapping("/pets/frequentPet")
	public String listFrequentPet(ModelMap modelMap) {
		PetType pet= petService.findFrequentPet().stream().findFirst().get();
		modelMap.addAttribute("frequentPet", pet);
		return "/pets/frequentPetList";
	}
	
	//Admin Dashboard
	@GetMapping(value = "admin/diagnosis")
	public String processAdminDiagnosisList(ModelMap model) {
		Collection<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
		try {
			diagnosis = this.diagnosisService.listDiagnosis();	
		}
		
		catch(NoElementOnListException ex){
                return "redirect:/errorNoElement";
            }
		
			model.put("diagnosis", diagnosis);
			return "diagnosis/diagnosisList";
	}
	
	@GetMapping(value = { "/vets/choose" })
	public String chooseVetList(Map<String, Object> model) {
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/chooseVet";
	}
	
	public String getVetWithMostVisits() {
		Vet vet = this.vetService.findVetWithMostVisit();
		String name = vet.getFirstName() + " " + vet.getLastName();
		return name;
	}
	
	@GetMapping("/residences")
    public String findAllResidencesNotEnded(ModelMap modelMap) {

        String vista = "residences/adminResidenceList";
        Collection<Pet> pet = new ArrayList<Pet>();
        try {
        	pet = residenceService.findAllResicenceNotEnded();
        }
        catch(NoElementOnListException ex) {
        	return "redirect:/errorNoElement";
        }
        modelMap.addAttribute("pet", pet);
        LocalDate date = LocalDate.now();
        modelMap.addAttribute("currentDate", date);
        return vista;
    }
	
}
