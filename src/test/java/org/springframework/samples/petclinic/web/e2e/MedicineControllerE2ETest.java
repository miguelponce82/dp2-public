package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.PetclinicApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@TestPropertySource(
//  locations = "classpath:application-mysql.properties")
public class MedicineControllerE2ETest {
	
	private static final int TEST_MEDICINE_ID = 1;
	
	private static final int TEST_MEDICINE_WITHOUT_TREATMENT_ID = 5;

	@Autowired
	private MockMvc mockMvc;	

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/medicines/edit/{medicineId}", TEST_MEDICINE_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists("medicine"))
				.andExpect(view().name("medicines/updateMedicinesForm"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInitUpdateMedicineForm() throws Exception {
		mockMvc.perform(get("/medicines/edit/{medicineId}", TEST_MEDICINE_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("medicine"))
				.andExpect(model().attribute("medicine", hasProperty("name", is("Ibuprofeno"))))
				.andExpect(model().attribute("medicine", hasProperty("description", is("Farmaco que se usa para tratar la fiebre y/o el dolor"))))
				.andExpect(model().attribute("medicine", hasProperty("sideEffects", is("Estrenimiento, diarrea, gases y mareo"))))
				.andExpect(view().name("medicines/updateMedicinesForm"));
	}
	 
	@WithMockUser(username="admin1",authorities= {"admin"})
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
	
	@WithMockUser(username="admin1",authorities= {"admin"})
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
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessDelete() throws Exception {
		mockMvc.perform(get("/medicines/delete/{medicineId}", TEST_MEDICINE_WITHOUT_TREATMENT_ID)
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("medicines/listadoMedicamentos"));
	}
	 
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessDeleteErrors() throws Exception {
		mockMvc.perform(get("/medicines/delete/{medicineId}", TEST_MEDICINE_ID)
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/errorTreatmentActive"));
	}
	 
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testListaMedicamentos() throws Exception {
		mockMvc.perform(get("/medicines")
			.with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("medicines/listadoMedicamentos"))
		 	.andExpect(model().attributeExists("medicines"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testInitCreationForm() throws Exception {
		 mockMvc.perform(get("/medicines/new", TEST_MEDICINE_ID)).andExpect(status().isOk())
				.andExpect(view().name("medicines/createMedicinesForm")).andExpect(model().attributeExists("medicine"));
	 }
	 
	 @WithMockUser(username="admin1",authorities= {"admin"})
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
	 
	 @WithMockUser(username="admin1",authorities= {"admin"})
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
	 
	 @WithMockUser(username="admin1",authorities= {"admin"})
	 @Test
	 void testProcessCreationFormHasDuplicateNameError() throws Exception {
			mockMvc.perform(post("/medicines/new", TEST_MEDICINE_ID)
								.with(csrf())
								.param("name", "Ibuprofeno")
								.param("description", "Descripcion de prueba")
								.param("sideEffects", "Sin efectos secundarios"))
					.andExpect(model().attributeHasErrors("medicine"))
					.andExpect(status().isOk())
					.andExpect(view().name("medicines/createMedicinesForm"));
	 }

}