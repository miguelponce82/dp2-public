package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=MedicineController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class MedicineControllerTests {
	
	private static final int TEST_MEDICINE_ID = 1;

	@Autowired
	private MedicineController medicineController;

	@MockBean
	private MedicineService medicineService;
	
	@MockBean
	private TreatmentService treatmentService;
	
	@Autowired
	private MockMvc mockMvc;

	private Medicine medicine;

	@BeforeEach
	void setup() {

		medicine = new Medicine();
		medicine.setId(TEST_MEDICINE_ID);
		medicine.setName("Ibudol");
		medicine.setDescription("Headheach");
		medicine.setSideEffects("Muscle heach");
		given(this.medicineService.findMedicineById(TEST_MEDICINE_ID)).willReturn(medicine);

	}
	
	//FUNCIONA
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/medicines/edit/{medicineId}", TEST_MEDICINE_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists("medicine"))
				.andExpect(view().name("medicines/updateMedicinesForm"));
	}
	
	//FUNCIONA
	 @WithMockUser(value = "spring")
		@Test
		void testInitUpdateMedicineForm() throws Exception {
			mockMvc.perform(get("/medicines/edit/{medicineId}", TEST_MEDICINE_ID)).andExpect(status().isOk())
					.andExpect(model().attributeExists("medicine"))
					.andExpect(model().attribute("medicine", hasProperty("name", is("Ibudol"))))
					.andExpect(model().attribute("medicine", hasProperty("description", is("Headheach"))))
					.andExpect(model().attribute("medicine", hasProperty("sideEffects", is("Muscle heach"))))
					.andExpect(view().name("medicines/updateMedicinesForm"));
		}
	 
	 //FUNCIONA
	 @WithMockUser(value = "spring")
		@Test
		void testProcessUpdateMedicineFormHasErrors() throws Exception {
			mockMvc.perform(post("/medicines/edit/{medicineId}", TEST_MEDICINE_ID)
								.with(csrf())
								.param("name", "Ibuprofeno")
								.param("sideEffects", "Muscle heach"))
					.andExpect(status().isOk())
					.andExpect(model().attributeHasErrors("medicine"))
					.andExpect(model().attributeHasFieldErrors("medicine", "description"))
					.andExpect(view().name("medicines/updateMedicinesForm"));
		}
	 
	 @WithMockUser(value = "spring")
		@Test
		void testProcessUpdateMedicineFormHasErrors2() throws Exception {
			mockMvc.perform(post("/medicines/edit/{medicineId}", TEST_MEDICINE_ID)
								.with(csrf())				
								.param("name", "Ibuprofeno"))
					.andExpect(status().isOk())
					.andExpect(model().attributeHasErrors("medicine"))
					.andExpect(model().attributeHasFieldErrors("medicine", "sideEffects"))
					.andExpect(model().attributeHasFieldErrors("medicine", "description"))
					.andExpect(view().name("medicines/updateMedicinesForm"));
		}
	 
	 @WithMockUser(value = "spring")
	 @Test
	 void testProcessDelete() throws Exception {
		 mockMvc.perform(get("/medicines/delete/{medicineId}", TEST_MEDICINE_ID)
				 .with(csrf()))
		 	.andExpect(status().isOk())
			.andExpect(view().name("medicines/listadoMedicamentos"));
	 }
	 
	 @WithMockUser(value = "spring")
	 @Test
	 void testListaMedicamentos() throws Exception {
		 mockMvc.perform(get("/medicines")
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("medicines/listadoMedicamentos"))
		 	.andExpect(model().attributeExists("medicines"));
	 }
	 
	 @WithMockUser(value = "spring")
     @Test
     void testInitCreationForm() throws Exception {
		 mockMvc.perform(get("/medicines/new", TEST_MEDICINE_ID)).andExpect(status().isOk())
				.andExpect(view().name("medicines/createMedicinesForm")).andExpect(model().attributeExists("medicine"));
	 }
	 
	 @WithMockUser(value = "spring")
     @Test
     void testProcessCreationFormSuccess() throws Exception {
		 mockMvc.perform(post("/medicines/new", TEST_MEDICINE_ID)
							.with(csrf())
							.param("name", "Wistrol")
							.param("description", "Descripcion de prueba")
							.param("sideEffects", "Sin efectos secundarios"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/medicines"));
	 }
	 
	 @WithMockUser(value = "spring")
	 @Test
	 void testProcessCreationFormHasErrors() throws Exception {
			mockMvc.perform(post("/medicines/new", TEST_MEDICINE_ID)
								.with(csrf())
								.param("name", "Wistrol")
								.param("description", "Descripcion de prueba"))
					.andExpect(model().attributeHasErrors("medicine"))
					.andExpect(status().isOk())
					.andExpect(view().name("medicines/createMedicinesForm"));
	 }
	 
}
