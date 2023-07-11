package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.web.VisitController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitControllerIntegrationTests {
	
	private static final int TEST_VET_ID = 1;
	private static final int TEST_PET_ID = 1;

	@Autowired
	private VisitController visitController;

	@Autowired
	private VisitService visitService;
	
	@Autowired
	private PetService petService;
        
	@Autowired
	private VetService vetService;	
	
	
	@Test
	void testInitNewVisitForm() throws Exception {
		Pet pet=petService.findPetById(TEST_PET_ID);
		
		ModelMap model=new ModelMap();
		
		String view=visitController.initNewVisitForm(pet.getId(), model);
		
		assertEquals(view,"pets/createOrUpdateVisitForm");
		
	}

	
	
	
	 @Test
		void processNewVisitForm() throws Exception {
			Pet pet=petService.findPetById(TEST_PET_ID);
			Vet vet=vetService.findVetById(TEST_VET_ID);
			ModelMap model=new ModelMap();
			Visit newVisit = new Visit();
			LocalDate date = LocalDate.now();
			newVisit.setDate(date);
			LocalTime time = LocalTime.now();
			newVisit.setTime(time);
			newVisit.setDescription("description");
			newVisit.setPet(pet);
			newVisit.setVet(vet);
			BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
	
			String view=visitController.processNewVisitForm(newVisit, bindingResult, model);
			
			assertEquals(view,"redirect:/owners/{ownerId}");
		}
	
	 @Test
		void testShowVisitList() throws Exception{
		 Map<String, Object> model = new HashMap<String, Object>();
		 String view = visitController.showAllVisits(TEST_PET_ID, model);
		 assertEquals(view, "visits/visitList");
		
	 }
}
