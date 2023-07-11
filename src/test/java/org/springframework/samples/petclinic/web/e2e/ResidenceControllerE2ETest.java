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
public class ResidenceControllerE2ETest {
	
	private static final int TEST_VET_ID = 1;
	
	private static final int TEST_DIAGNOSIS_ID_C = 9;
	
	private static final int TEST_DIAGNOSIS_ID = 1;

	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testInitCreationForm() throws Exception {
	mockMvc.perform(get("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID_C))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("residence"))
		.andExpect(view().name("vets/createResidenceForm"));
}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessNewResidenceFormSuccess() throws Exception {
	mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID_C).
			with(csrf())
			.param("dateStart", "2020/11/11")
			.param("dateEnd", "2021/11/11"))
	 		.andExpect(status().is3xxRedirection())
	 		.andExpect(view().name("redirect:/vets/{vetId}/diagnosis"));
	
	}
	//ERROR CUANDO INTENTAS AÑADIR UNA RESIDENCIA Y EL CAMPO DATESTART NO ES VALIDO
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessNewResidenceFormError1() throws Exception {
	mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID).
			with(csrf())
			.param("dateStart", "2019/11/11")
			.param("dateEnd", "2021/11/11"))
	 		.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("residence", "dateStart"))
	 		.andExpect(view().name("vets/createResidenceForm"));
	
	}
	
	//ERROR CUANDO INTENTAR AÑADIR UNA RESIDENCIA, CUANDO EL DIAGNOSIS YA TIENE UNA
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
    void testProcessNewResidenceFormError2() throws Exception {
	mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID).
			with(csrf())
			.param("dateStart", "2020/11/11")
			.param("dateEnd", "2021/11/11"))
	 		.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("residence", "dateStart"))
	 		.andExpect(view().name("vets/createResidenceForm"));
	
	}
	//FUNCIONA, PERO HAY QUE CREAR EL ESCENARIO NEGATIVO
	//HAY QUE USAR EL WHEN (PREGUNTAR A MULLER)
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
	void testFindAllResidencesNotEnded() throws Exception{
	mockMvc.perform(get("/residences"))
	.andExpect(status().isOk())
	.andExpect(view().name("residences/adminResidenceList"));

	}
	

}
