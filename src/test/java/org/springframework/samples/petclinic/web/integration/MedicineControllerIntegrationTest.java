package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.web.MedicineController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MedicineControllerIntegrationTest {
	
	private final Integer TEST_MEDICINE_ID_TO_DELETE = 2;
	
	private final Integer TEST_MEDICINE_ID = 1;
	
	private final Integer TEST_MEDICINE_ID_TREATMENT = 1;

	
	@Autowired
	private MedicineController medicineController;
	
	@Autowired
	private MedicineService medicineService;
	
	@Test
	void testListadoMedicinesSuccess() throws Exception{
		ModelMap model = new ModelMap();
		String view = medicineController.listadoMedicines(model);
    	assertEquals(view,"medicines/listadoMedicamentos");

	}

	@Test
	void testInitUpdateMedicineForm() throws Exception{
		ModelMap model = new ModelMap();
		String view = this.medicineController.initUpdateMedicineForm(TEST_MEDICINE_ID, model);
    	assertEquals(view,"medicines/updateMedicinesForm");
	}
	
	@Test
	void testProcessUpdateMedicineFormSuccess() throws Exception{
		Medicine medicine = this.medicineService.findMedicineById(TEST_MEDICINE_ID);
    	BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
    	medicine.setName("Omega");
    	String view = this.medicineController.processUpdateMedicineForm(medicine, bindingResult, medicine.getId());
    	assertEquals(view,"redirect:/medicines");

	}
	//FUNCIONA, EL ERROR SE PRODUCE AL NO INTRODUCIRLE UNA DESCRIPCION
	@Test
	void testProcessUpdateMedicineFormError() throws Exception{
		Medicine medicine = this.medicineService.findMedicineById(TEST_MEDICINE_ID);
    	BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
    	medicine.setDescription(null);
    	bindingResult.reject("descpription", "no puede estar vacío");
    	String view = this.medicineController.processUpdateMedicineForm(medicine, bindingResult, medicine.getId());
    	assertEquals(view,"medicines/updateMedicinesForm");

	}
	
	
	@Test
	void testProcessDeleteSuccess() throws Exception{
		ModelMap model = new ModelMap();
		Medicine medicine = this.medicineService.findMedicineById(TEST_MEDICINE_ID_TO_DELETE);
		String view = this.medicineController.processDelete(medicine.getId(), model);
    	assertEquals(view,"medicines/listadoMedicamentos");

	}

	
	//FUNCIONA, EL FALLO SE PRODUCE PORQUE HAY UN TRATAMIENTO ACTIVO CON LA MEDICINA
	@Test
	void testProcessDeleteError() throws Exception{
		ModelMap model = new ModelMap();
		Medicine medicine = this.medicineService.findMedicineById(TEST_MEDICINE_ID_TREATMENT);
		String view = this.medicineController.processDelete(medicine.getId(), model);
    	assertEquals(view,"errors/errorTreatmentActive");
	} 
	
	@Test
	void testInitCreationForm() throws Exception {
		ModelMap model=new ModelMap();
		
		String view = medicineController.initCreationForm(model);
		
		assertEquals(view, "medicines/createMedicinesForm");
		assertNotNull(model.get("medicine"));		
	}
	
	@Test
	void testCreateMedicine() throws Exception{
		ModelMap model=new ModelMap();
    	Medicine newMedicine=new Medicine();
    	newMedicine.setName("Advantix Multiprotection");
    	newMedicine.setDescription("Effective against ticks that repels and eliminates them for 3-4 weeks avoiding serious health problems.");
    	newMedicine.setSideEffects("itchiness");   	
		BindingResult bindingResult=new MapBindingResult(Collections.emptyMap(),"");
		
		String view=medicineController.processCreationForm(newMedicine, bindingResult, model);
    	
		assertEquals(view,"redirect:/medicines");				
	}
	
	
	//Nombre duplicado
	@Test
	void testCreateMedicineErrors() throws Exception{
		ModelMap model=new ModelMap();
    	Medicine newMedicine=new Medicine();
    	newMedicine.setName("Ibuprofeno");
    	newMedicine.setDescription("Effective against ticks that repels and eliminates them for 3-4 weeks avoiding serious health problems.");
    	newMedicine.setSideEffects("itchiness");   	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		bindingResult.reject("name", "already exists");
		
		String view=medicineController.processCreationForm(newMedicine, bindingResult, model);
    	
		assertEquals(view, "medicines/createMedicinesForm");	
		
	}
	
	//Nombre vacío
	@Test
	void testCreateMedicineErrors2() throws Exception{
		ModelMap model=new ModelMap();
    	Medicine newMedicine=new Medicine();
    	newMedicine.setName("");
    	newMedicine.setDescription("Effective against ticks that repels and eliminates them for 3-4 weeks avoiding serious health problems.");
    	newMedicine.setSideEffects("itchiness");   	
		BindingResult bindingResult=new MapBindingResult(new HashMap(),"");
		bindingResult.reject("name", "Name must not be empty");
		
		String view=medicineController.processCreationForm(newMedicine, bindingResult, model);
    	
		assertEquals(view,"medicines/createMedicinesForm");				
	}
	

}
