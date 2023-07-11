package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
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
public class DiagnosisControllerE2ETest {
	
	private static final int TEST_PET_ID = 1;
	private static final int TEST_VET_ID = 1;
	private static final int TEST_VISIT_ID = 1;

	@Autowired
	private MockMvc mockMvc;	
	
	@Autowired
	private PetService petService;
	@Autowired
	private VetService vetService;
	@Autowired
	private VisitService visitService;
	@Autowired
	private DiagnosisService diagnosisService;
	

	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/visits/{visitId}/diagnosis/new",TEST_VET_ID, TEST_VISIT_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/createDiagnosisForm"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessNewDiagnosisFormSuccess() throws Exception {
	mockMvc.perform(post("/vets/{vetId}/visits/{visitId}/diagnosis/new", TEST_VET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("description", "Diagnosis description")
						.param("status", "OBSERVATION")
						.param("vet", "Pedro"))   
            .andExpect(model().attributeExists("diagnosis"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testListaDiagnosis() throws Exception {
		mockMvc.perform(get("/diagnosis")
			.with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("diagnosis/diagnosisList"))
		 	.andExpect(model().attributeExists("diagnosis"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testListaDiagnosisByPetId() throws Exception{
		 mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/diagnosis", 1, TEST_PET_ID)
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("diagnosis/diagnosisList"))
		 	.andExpect(model().attributeExists("diagnosis"));
		 }
}	
