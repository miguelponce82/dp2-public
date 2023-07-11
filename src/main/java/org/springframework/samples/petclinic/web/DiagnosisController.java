 	package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DiagnosisController {
	
	private final PetService petService;
	private final VetService vetService;
	private final VisitService visitService;
	private final DiagnosisService diagnosisService;
	
	
	
	@Autowired
	public DiagnosisController(PetService petService, VetService vetService,
			 VisitService visitService, DiagnosisService diagnosisService) {
		this.petService = petService;
		this.vetService = vetService;
		this.visitService = visitService;
		this.diagnosisService = diagnosisService;
	}
	
	@InitBinder("pet")
	public void initOwnerBinder1(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	
	@InitBinder("diagnosis")
	public void initDiagnosisBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new DiagnosisValidator());
	}

	
	@GetMapping(value = "/vets/{vetId}/visits/{visitId}/diagnosis/new")
	public String initNewDiagnosisForm(@PathVariable("vetId") int vetId, @PathVariable("visitId") int visitId
			,ModelMap model) {
		Diagnosis diagnosis = new Diagnosis();
		Vet vet = this.vetService.findVetById(vetId);
		Pet pet = this.visitService.findVisitById(visitId).getPet();
		List<String> status = new ArrayList<String>();
		List<Vet> vetl = new ArrayList<Vet>();
		status.add("NOT_DANGER");
		status.add("OBSERVATION");
		status.add("SURGERY");
		diagnosis.setVet(vet);
		diagnosis.setTreatment(null);
		diagnosis.setResidence(null);
		pet.addDiagnosis(diagnosis);
		model.put("diagnosis", diagnosis);
		model.put("status2", status);
		vetl.add(vet);
		model.put("vet", vetl);
		model.put("pet", pet);
		
		return "vets/createDiagnosisForm";
		
	}
	
	@PostMapping(value ="/vets/{vetId}/visits/{visitId}/diagnosis/new")
	public String processNewDiagnosisForm(@PathVariable("vetId") int vetId, Pet pet, @Valid Diagnosis diagnosis, BindingResult result, ModelMap model) {
		Pet p = (Pet) model.get("pet");
		p.addDiagnosis(diagnosis);	
		
		if (result.hasErrors()) {
			List<String> status = new ArrayList<String>();
			List<Vet> vetl = new ArrayList<Vet>();
			if(diagnosis.getVet()!=null) {
				vetl.add(diagnosis.getVet());
			}
			status.add("NOT_DANGER");
			status.add("OBSERVATION");
			status.add("SURGERY");
			model.put("status2", status);
			model.put("vet", vetl);
			
			return "vets/createDiagnosisForm";
		}
		else {	
			
			this.petService.saveDiagnosis(diagnosis);
		}
		return "redirect:/vets/{vetId}/diagnosis";
	}
	
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/diagnosis")
	public String listDiagnosisByPetId(@PathVariable("petId") int petId, Owner owner, BindingResult result, ModelMap model) {
		Collection<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
		try {
			diagnosis = this.diagnosisService.findAllDiagnosisByPetId(petId);	
		}
		
		catch(NoElementOnListException ex){
                return "redirect:/errorNoElement";
            }
		
			model.put("diagnosis", diagnosis);
			return "diagnosis/diagnosisList";
		}
	
	@Transactional(readOnly = true)
	@GetMapping("/diagnosis")
	public String listAllDiagnosis(ModelMap modelMap) {
		String view= "diagnosis/diagnosisList";
		Iterable<Diagnosis> diagnosis= diagnosisService.findAll();
		modelMap.addAttribute("diagnosis", diagnosis);
		return view;
	}

}
