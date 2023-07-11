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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private final VetService vetService;
	private final VisitService visitService;
	private final DiagnosisService diagnosisService;

	@Autowired
	public VetController(VetService clinicService, VisitService visitService, DiagnosisService diagnosisService) {
		this.vetService = clinicService;
		this.visitService = visitService;
		this.diagnosisService = diagnosisService;
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}
	
	@GetMapping(value = { "/vets.xml"})
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}
	
	@GetMapping(value = "/vets/{vetId}/visits")
	public String orderVisitsByVet(@PathVariable("vetId") final int vetId, ModelMap modelMap) {
		String vista= "visits/visitList";
		Vet vet = this.vetService.findVetById(vetId);
		Collection<Visit> visits = new ArrayList<Visit>();
		try {
		visits = this.visitService.findVisitsByVet(vet);
		}
		catch(NoElementOnListException ex) {
			return "redirect:/errorNoElement";
		}
		modelMap.addAttribute("visits", visits);
		return vista;
	}
	
	@Transactional(readOnly= true)
	@GetMapping(value = "/vets/owners")
	public String listOwners(Map<String, Object> model) {
		Collection<Owner> result = this.vetService.findAllOwners();
		model.put("selections", result);
		return "vets/ownersListVets";
	}
	
	@GetMapping(value = "/vets/{vetId}/diagnosis")
	public String processFindVetForm(@PathVariable("vetId") int vetId, Owner owner, BindingResult result, Map<String, Object> model) {
		Vet vet = vetService.findVetById(vetId);
		Collection<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
		try{
			diagnosis = this.diagnosisService.findAllDiagnosisByVetId(vet);
		}
		catch(NoElementOnListException ex){
			return "redirect:/errorNoElement";
		}
			model.put("diagnosis", diagnosis);
			return "diagnosis/diagnosisList";
		}
	
	@GetMapping(value = "/vets/{vetId}/visitHistory")
	public String vetHistory(@PathVariable("vetId") final int vetId, ModelMap modelMap) throws NoElementOnListException {
		String vista= "visits/visitList";
		Vet vet = this.vetService.findVetById(vetId);
		Collection<Visit> visits = new ArrayList<Visit>();
		try {
		visits =this.visitService.findVisitHistoryByVet(vet);
		}
		catch(NoElementOnListException ex){
			vista =  "redirect:/errorNoElement";
		}
		modelMap.addAttribute("visits", visits);
		return vista;
	}

}
