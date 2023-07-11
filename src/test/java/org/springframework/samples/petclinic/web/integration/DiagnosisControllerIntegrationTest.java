package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.DiagnosisController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiagnosisControllerIntegrationTest {
	
	private final Integer TEST_VET_ID = 1;
	
	private final Integer TEST_OWNER_ID = 1;
	
	private final Integer TEST_VISIT_ID = 1;
	
	private final Integer TEST_PET_ID = 1;
	
	private final Integer TEST_PET_ID_F= 5;

	
	
	@Autowired
	private DiagnosisController diagnosisController;
	
	@Autowired 
	private VetService vetService;
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Test
	void testInitNewDiagnosisForm() throws Exception{
	ModelMap model = new ModelMap();
	String view = this.diagnosisController.initNewDiagnosisForm(TEST_VET_ID, TEST_VISIT_ID , model);
	assertEquals(view, "vets/createDiagnosisForm");
	assertNotNull(model.get("diagnosis"));	

	}
	
	@Test
	void testProcessNewDiagnosisFormSuccess() throws Exception{
		Pet pet = this.petService.findPetById(TEST_PET_ID);
		Vet vet = this.vetService.findVetById(TEST_VET_ID);
		Diagnosis diagnosis = new Diagnosis();
		diagnosis.setDescription("Stomach ache");
		diagnosis.setStatus(Status.NOT_DANGER);
		diagnosis.setVet(vet);
    
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
    	ModelMap model = new ModelMap();
    	model.addAttribute("pet", pet);
    	
    	String view = this.diagnosisController.processNewDiagnosisForm(TEST_VET_ID, pet, diagnosis, bindingResult, model);
    	assertEquals(view, "redirect:/vets/{vetId}/diagnosis");
	}
	//FUCIONA, FALLA AL TENER VACÍA LA DESCRICIÓN
	@Test
	void testProcessNewDiagnosisFormError() throws Exception{
		Pet pet = this.petService.findPetById(TEST_PET_ID);
		Vet vet = this.vetService.findVetById(TEST_VET_ID);
		Diagnosis diagnosis = new Diagnosis();
		diagnosis.setStatus(Status.NOT_DANGER);
		diagnosis.setVet(vet);
    
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
    	ModelMap model = new ModelMap();
    	model.addAttribute("pet", pet);
    	bindingResult.reject("description", "Description must not be empty");
    	String view = this.diagnosisController.processNewDiagnosisForm(TEST_VET_ID, pet, diagnosis, bindingResult, model);
    	assertEquals(view, "vets/createDiagnosisForm");
	}
	
	@Test
	void testListDiagnosisByPetIdSuccess() throws Exception{
		Owner owner = this.ownerService.findOwnerById(TEST_OWNER_ID);
		ModelMap model = new ModelMap();
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");

		String view = this.diagnosisController.listDiagnosisByPetId(TEST_PET_ID, owner, bindingResult, model);
    	assertEquals(view, "diagnosis/diagnosisList");

	}
	
	@Test
	void testListDiagnosisByPetIdError() throws Exception{
		Owner owner = this.ownerService.findOwnerById(TEST_OWNER_ID);
		ModelMap model = new ModelMap();
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");

		String view = this.diagnosisController.listDiagnosisByPetId(TEST_PET_ID_F, owner, bindingResult, model);
    	assertEquals(view, "redirect:/errorNoElement");

	}
	

	

}
