package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.assertj.core.util.Lists;
import org.hamcrest.collection.IsEmptyCollection;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;



@WebMvcTest(controllers = DiagnosisController.class,
includeFilters = @ComponentScan.Filter(value = VetFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class DiagnosisControllerTests {
	
	private static final int TEST_DIAGNOSIS_ID = 1;
	private static final int TEST_VET_ID = 1;
	private static final int TEST_VISIT_ID = 5;
	private static final int TEST_PET_ID = 1;
	
	@Autowired
	private DiagnosisController diagnosisController;
	
	@MockBean
	private PetService petService;
	
	@MockBean
	private VisitService visitService;
	
	@MockBean
	private DiagnosisService diagnosisService;
	
	@MockBean
	private VetService vetService;

	@Autowired
	private MockMvc mockMvc;
	
	private Vet vet1;
	private Diagnosis diagnosis;
	
	@BeforeEach
	void setup() {
		vet1 = new Vet();
		vet1.setId(1);
		vet1.setFirstName("Manuel");
		vet1.setLastName("Fuentes");
		
		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");
		
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Rodolfo");
		pet.setBirthDate(LocalDate.now().minusYears(1));
		pet.setType(cat);
		
		given(this.diagnosisService.findDiagnosisById(TEST_PET_ID)).willReturn(diagnosis);
		given(this.vetService.findVets()).willReturn(Lists.newArrayList(vet1));
//		given(this.visitService.findVisitById(TEST_VISIT_ID).getPet()).willReturn(pet);
		given(this.vetService.findVetById(TEST_VET_ID)).willReturn(vet1);
	}
	
	//El testInitNewDiagnosisForm y testListDiagnosisByPetIdError están probados en los tests de integración, ya que necesita sí o sí acceder a la BD
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessNewDiagnosisFormSuccess() throws Exception {
	mockMvc.perform(post("/vets/{vetId}/visits/{visitId}/diagnosis/new", TEST_VET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("description", "Diagnosis description")
						.param("status", "OBSERVATION")
						.param("vet", "Manuel"))   
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/vets/{vetId}/diagnosis"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessNewDiagnosisFormHasErrors() throws Exception {
	mockMvc.perform(post("/vets/{vetId}/visits/{visitId}/diagnosis/new", TEST_VET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("description", "Diagnosis description")
						.param("status", "OBSERVATION"))  
			.andExpect(model().attributeHasErrors("diagnosis")).andExpect(status().isOk())
			.andExpect(view().name("vets/createDiagnosisForm"));
}
	
	
	@WithMockUser(value = "spring")
	 @Test
	 void testListDiagnosis() throws Exception {
		 mockMvc.perform(get("/diagnosis")
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("diagnosis/diagnosisList"))
		 	.andExpect(model().attributeExists("diagnosis"));
	 }
	
	@WithMockUser(value = "spring")
	@Test
	void testListDiagnosisByPetId() throws Exception{
		 mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/diagnosis", 1, TEST_PET_ID)
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("diagnosis/diagnosisList"));
		 }
		


}
