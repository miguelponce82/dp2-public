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
//	locations = "classpath:application-mysql.properties")
public class TreatmentControllerE2ETest {
	
	private static final int TEST_TREATMENT_ID = 1;
	
	private static final int TEST_VET_ID = 1;
	
	private static final int TEST_DIAGNOSIS_ID = 1;
	
	private static final int TEST_DIAGNOSIS_ID_C = 9;

	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInitNewTreatmentForm() throws Exception{
	 mockMvc.perform(get("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID_C))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("treatment"))
		.andExpect(view().name("vets/createTreatmentForm"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessNewTreatmentFormSuccess() throws Exception{
		 mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID_C)
		 .with(csrf())
		 .param("recomendations", "You should keep making E2E tests")
		 .param("startDate", "2020/11/11")
		 .param("endDate", "2021/11/11"))
		 .andExpect(status().is3xxRedirection())
		 .andExpect(view().name("redirect:/vets/{vetId}/diagnosis"));
	}
	//TEST QUE COMPRUEBA QUE SIN EL CAMPO RECOMENDATIONS NO SE PUEDE CREAR
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessNewTreatmentFormError1() throws Exception{
		 mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID_C)
		 .with(csrf())
		 .param("startDate", "2020/11/11")
		 .param("endDate", "2021/11/11"))
		 .andExpect(status().isOk())
		 .andExpect(model().attributeHasFieldErrors("treatment", "recomendations"))
		 .andExpect(view().name("vets/createTreatmentForm"));
	}
	
	//TEST QUE COMPRUEBA QUE NO PUEDES CREAR UN TRATAMIENTO, CUANDO EL DIAGNOSITICO ASOCIADO YA TIENE UNO
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessNewTreatmentFormError2() throws Exception{
		mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
		 .with(csrf())
		 .param("recomendations", "You should keep making E2E tests")
		 .param("startDate", "2020/11/11")
		 .param("endDate", "2021/11/11"))
		 .andExpect(status().isOk())
		 .andExpect(model().attributeHasErrors("treatment"))
		 .andExpect(model().attributeHasFieldErrors("treatment", "recomendations"))
		 .andExpect(view().name("vets/createTreatmentForm"));
	}
}
