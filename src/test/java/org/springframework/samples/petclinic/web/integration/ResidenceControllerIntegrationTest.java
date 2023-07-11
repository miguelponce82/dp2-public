package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.web.ResidenceController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResidenceControllerIntegrationTest {
	
	private static final int TEST_DIAGNOSIS_ID_STATUS_NOT_DANGER = 8;
	
	private static final int TEST_DIAGNOSIS_ID = 9;
	
	@Autowired
	private ResidenceController residenceController;
	
	@Autowired
	private DiagnosisService diagnosisService;
	
	@Test
	void testInitCreationForm() throws Exception{
	Diagnosis diagnosis = diagnosisService.findDiagnosisById(TEST_DIAGNOSIS_ID);
	ModelMap model=new ModelMap();
	String view = residenceController.initNewResidenceForm(diagnosis.getId(), model);
	assertEquals(view,"vets/createResidenceForm");
	assertNotNull(model.get("residence"));	
	}
	
	@Test
	void testProcessCreationFormSuccess() throws Exception {
    	ModelMap model=new ModelMap();
    	Diagnosis diagnosis = diagnosisService.findDiagnosisById(TEST_DIAGNOSIS_ID);
    	Residence residence = new Residence();
    	residence.setDateEnd(LocalDate.now().plusYears(1));
    	residence.setDateStart(LocalDate.now().minusYears(1));
    	residence.setId(10);
    	BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");

    	String view=residenceController.processNewResidenceForm(residence, bindingResult, model, diagnosis.getId());
    	assertEquals(view,"redirect:/vets/{vetId}/diagnosis");
	}
	
	//ERROR CUANDO EL STATUS ES = NOT DANGER
	@Test
	void testProcessCreationFormError1() throws Exception {
    	ModelMap model=new ModelMap();
    	Diagnosis diagnosis = diagnosisService.findDiagnosisById(TEST_DIAGNOSIS_ID_STATUS_NOT_DANGER);
    	Residence residence = new Residence();
    	BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");

    	String view=residenceController.processNewResidenceForm(residence, bindingResult, model, diagnosis.getId());
    	assertEquals(view,"vets/createResidenceForm");
	}
	
	


}
