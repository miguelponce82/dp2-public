package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@TestPropertySource(
//		  locations = "classpath:application-mysql.properties")
public class VetControllerE2ETest {
	
	private final Integer TEST_VET_ID = 1;
	
	private final Integer TEST_VET_ID_F = 5;
	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShowVetList() throws Exception{
		mockMvc.perform(get("/vets"))
		.andExpect(status().isOk())
		.andExpect(view().name("vets/vetList"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testOrderVisitsByVetSuccess() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/visits", TEST_VET_ID))
		.andExpect(status().isOk())
		.andExpect(view().name("visits/visitList"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testOrderVisitsByVetError() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/visits", TEST_VET_ID_F))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/errorNoElement"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testlistOwner() throws Exception{
		mockMvc.perform(get("/vets/owners"))
		.andExpect(status().isOk())
		.andExpect(view().name("vets/ownersListVets"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessFindVetFormSuccess() throws Exception{
		mockMvc.perform(get("/vets/{vetId}/diagnosis", TEST_VET_ID))
		.andExpect(status().isOk())
		.andExpect(view().name("diagnosis/diagnosisList"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessFindVetFormError() throws Exception{
		mockMvc.perform(get("/vets/{vetId}/diagnosis", TEST_VET_ID_F))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/errorNoElement"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testVetHistorySuccess() throws Exception{
	mockMvc.perform(get("/vets/{vetId}/visitHistory", TEST_VET_ID))
	.andExpect(status().isOk())
	.andExpect(view().name("visits/visitList"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testVetHistoryError() throws Exception{
	mockMvc.perform(get("/vets/{vetId}/visitHistory", TEST_VET_ID_F))
	.andExpect(status().is3xxRedirection())
	.andExpect(view().name("redirect:/errorNoElement"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testChooseVetList() throws Exception{
		mockMvc.perform(get("/vets/choose"))
		.andExpect(status().isOk())
		.andExpect(view().name("vets/chooseVet"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	 void testVetWithMostVisits() throws Exception {
		 mockMvc.perform(get("/admin")
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(model().attributeExists("vetWithMostVisits"));
	 }
}
