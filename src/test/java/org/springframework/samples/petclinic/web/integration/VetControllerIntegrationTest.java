package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.samples.petclinic.web.VetController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VetControllerIntegrationTest {
	
	private static final int TEST_VET_ID = 1;
	
	private static final int TEST_VET_ID_NO_VISITS = 2;
	
	private static final int TEST_VET_ID_NO_VISITS_HISTORY = 4;
	
	private static final int TEST_VET_ID_NO_DIAGNOSIS = 3;
	
	private static final int TEST_OWNER_ID = 1;
	
	@Autowired
	private VetController vetController;
	
	@Autowired
	private VetService vetService;
	
	@Autowired
	private OwnerService ownerService;
	
	
	@Test
	void testShowVetList() throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		String view = vetController.showVetList(model);
		assertEquals(view, "vets/vetList");
		assertNotNull(model.get("vets"));
	}
	
	@Test
	void testOrderVisitByVet() throws Exception{
		ModelMap model = new ModelMap();
		Vet vet = this.vetService.findVetById(TEST_VET_ID);
		String view = vetController.orderVisitsByVet(vet.getId(), model);
		assertEquals(view, "visits/visitList");
		assertNotNull(model.get("visits"));
	}
	
	@Test
	void testOrderVisitByVetNoVisits() throws Exception{
		ModelMap model = new ModelMap();
		Vet vet = this.vetService.findVetById(TEST_VET_ID_NO_VISITS);
		String view = vetController.orderVisitsByVet(vet.getId(), model);
		assertEquals(view, "redirect:/errorNoElement");
	}
	
	@Test
	void testListOwners() throws Exception{
		Map<String, Object> model = new HashMap<String,Object>();
		String view = vetController.listOwners(model);
		assertEquals(view, "vets/ownersListVets");
		assertNotNull(model.get("selections"));
	}
	
	@Test
	void testProcessFindDiagnosisByVet() throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		BindingResult bindRes = new MapBindingResult(Collections.emptyMap(),"");
		Owner owner = ownerService.findOwnerById(TEST_OWNER_ID);
		Vet vet = vetService.findVetById(TEST_VET_ID);
		
		String view = vetController.processFindVetForm(vet.getId(), owner, bindRes, model);
		assertEquals(view, "diagnosis/diagnosisList");
		assertNotNull(model.get("diagnosis"));
		
	}
	
	@Test
	void testProcessFindDiagnosisByVetNoElementOnList() throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		BindingResult bindRes = new MapBindingResult(Collections.emptyMap(),"");
		Owner owner = ownerService.findOwnerById(TEST_OWNER_ID);
		
		String view = vetController.processFindVetForm(TEST_VET_ID_NO_DIAGNOSIS, owner, bindRes, model);
		assertEquals(view, "redirect:/errorNoElement");
	}

	@Test
	void testVetHistory() throws NoElementOnListException {
		ModelMap model = new ModelMap();
		String view = vetController.vetHistory(TEST_VET_ID, model);
		
		assertEquals(view, "visits/visitList");
		assertNotNull(model.get("visits"));
	}
	
	@Test
	void testVetHistoryNoElement() throws NoElementOnListException {
		ModelMap model = new ModelMap();
		String view = vetController.vetHistory(TEST_VET_ID_NO_VISITS_HISTORY, model);
		
		assertEquals(view, "redirect:/errorNoElement");
		
	}
	

}
