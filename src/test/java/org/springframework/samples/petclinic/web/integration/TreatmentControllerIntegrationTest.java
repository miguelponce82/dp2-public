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
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.web.TreatmentController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TreatmentControllerIntegrationTest {
	
	private static final int TEST_DIAGNOSIS_ID = 9;
	
	@Autowired
	private TreatmentController treatmentController;
	
	@Autowired
	private DiagnosisService diagnosisService;
	
	@Test
	void testInitCreationForm() throws Exception{
		Diagnosis diagnosis = diagnosisService.findDiagnosisById(TEST_DIAGNOSIS_ID);
		ModelMap model = new ModelMap();
		String view = treatmentController.initNewTreatmentForm(diagnosis.getId(), model);
		assertEquals(view,"vets/createTreatmentForm");
		assertNotNull(model.get("treatment"));	
	}
	
	@Test
	void testProcessNewTreatmentFormSuccess() throws Exception{
		ModelMap model = new ModelMap();
		Medicine medicine = new Medicine();
		medicine.setName("Qualitas");
		medicine.setDescription("Vitamines");
		medicine.setSideEffects("Overweight");
		Treatment treatment = new Treatment();
		treatment.setRecomendations("Keep running");
		treatment.setStartDate(LocalDate.now().minusYears(1));
		treatment.setEndDate(LocalDate.now().plusYears(1));
		treatment.setMedicine(medicine);
		
    	Diagnosis diagnosis = diagnosisService.findDiagnosisById(TEST_DIAGNOSIS_ID);

    	BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
		String view = treatmentController.processNewTreatmentForm(treatment, bindingResult, model, diagnosis.getId());
    	assertEquals(view,"redirect:/vets/{vetId}/diagnosis");

	}
	
	@Test
	void testProcessNewTreatmentFormError() throws Exception{
		ModelMap model = new ModelMap();
		Medicine medicine = new Medicine();
		medicine.setName("Qualitas");
		medicine.setDescription("Vitamines");
		medicine.setSideEffects("Overweight");
		Treatment treatment = new Treatment();
		treatment.setStartDate(LocalDate.now().minusYears(1));
		treatment.setEndDate(LocalDate.now().plusYears(1));
		treatment.setMedicine(medicine);
		
    	Diagnosis diagnosis = diagnosisService.findDiagnosisById(TEST_DIAGNOSIS_ID);

    	BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
    	bindingResult.reject("recomendations", "You must write a recomendation");
		String view = treatmentController.processNewTreatmentForm(treatment, bindingResult, model, diagnosis.getId());
    	assertEquals(view,"vets/createTreatmentForm");

	}
	
}
