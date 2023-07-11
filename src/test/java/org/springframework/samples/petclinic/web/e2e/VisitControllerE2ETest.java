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
public class VisitControllerE2ETest {
	
	private final Integer TEST_VISIT_ID = 1;
	
	private final Integer TEST_PET_ID = 1;
	
	private final Integer TEST_OWNER_ID = 1;
		
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username="owner1", authorities = {"owner"})
	@Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"))
				.andExpect(model().attributeExists("visit"))
				.andExpect(model().attributeExists("vets"));
	}
	
	@WithMockUser(username="owner1", authorities = {"owner"})
	@Test
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
							.with(csrf())
							.param("description", "Visit Description")
							.param("date", "2020/08/28")
							.param("time", "20:00")
							.param("vet", "James"))
	            .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(username="owner1", authorities = {"owner"})
	@Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
							.with(csrf())
							.param("name", "George")
							.param("date", "2022/07/09")
							.param("description", "Prueba de visita"))
				.andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}
	
	@WithMockUser(username="owner1", authorities = {"owner"})
	@Test
	void testProcessNewVisitFormHasSundayErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
							.with(csrf())
							.param("description", "Visit Description")
							.param("date", "2020/08/30")
							.param("time", "20:00")
							.param("vet", "James"))
				.andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}
	
	@WithMockUser(username="owner1", authorities = {"owner"})
	@Test
	void testProcessNewVisitFormHasTimeErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
							.with(csrf())
							.param("description", "Visit Description")
							.param("date", "2020/08/28")
							.param("time", "14:00")
							.param("vet", "James"))
				.andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}
		
	@WithMockUser(username="owner1", authorities = {"owner"})
	@Test
	void testShowVisits() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("visits")).andExpect(view().name("visits/visitList"));
	}
	
}
