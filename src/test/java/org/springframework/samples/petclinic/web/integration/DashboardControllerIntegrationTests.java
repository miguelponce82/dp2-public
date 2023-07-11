package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.DashboardController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DashboardControllerIntegrationTests {
	

	@Autowired
	private DashboardController dashboardController;
	
	
	@Test
	void testProcessfindAllResidencesNotEndedSuccess() throws Exception{
    	ModelMap model=new ModelMap();
    	String view = dashboardController.findAllResidencesNotEnded(model);
    	
    	assertEquals(view,"residences/adminResidenceList");
    	
	}
	@Test
	void testChooseVetList() throws Exception{
		Map<String, Object> model = new HashMap<String,Object>();
		
		String view = dashboardController.chooseVetList(model);
		
		assertEquals(view, "vets/chooseVet");
		assertNotNull(model.get("vets"));
	}
	
	@Test
	void testGetVetWithMostVisits(){
		String name = dashboardController.getVetWithMostVisits();
		assertEquals(name, "James Carter");
		}
	
	@Test
	void testProcessAdminDiagnosisListSuccess() throws Exception{
		ModelMap model = new ModelMap();
		
		String view = this.dashboardController.processAdminDiagnosisList(model);
    	assertEquals(view, "diagnosis/diagnosisList");
	}
	
	@Test
	void testProcessAdminFrequentPet() throws Exception{
		ModelMap model = new ModelMap();
		
		String view = this.dashboardController.listFrequentPet(model);
    	assertEquals(view, "/pets/frequentPetList");
    	
	}
	
	

}
