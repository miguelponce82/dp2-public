package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=TreatmentController.class,
includeFilters = @ComponentScan.Filter(value = MedicineFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class TreatmentControllerTest {
	
	private static final int TEST_TREATMENT_ID = 1;
	
	private static final int TEST_VET_ID = 1;
	
	private static final int TEST_DIAGNOSIS_ID = 1;
	
	private static final int TEST_MEDICINE_ID = 1;
	@Autowired
	private TreatmentController treatmentController;
	
	@MockBean
	private TreatmentService treatmentService;
	
	@MockBean
	private MedicineService medicineService;
	
	@MockBean
	private DiagnosisService diagnosisService;
	
	@Autowired
	private MockMvc mockMvc;

	private Treatment treatment;
	
	@BeforeEach
	void setup() {
		LocalDate d = LocalDate.now();
		Medicine m = new Medicine();
		m.setDescription("Mano de Santo");
		m.setId(1);
		m.setName("MDS");
		m.setSideEffects("Posibilidad de volar");
		treatment = new Treatment();
		treatment.setId(TEST_TREATMENT_ID);
		treatment.setRecomendations("Keep training");
		treatment.setStartDate(d.minusDays(1));
		treatment.setEndDate(d);
		given(this.treatmentService.findTreatmentById(TEST_TREATMENT_ID)).willReturn(treatment);
		given(this.diagnosisService.findDiagnosisById(TEST_DIAGNOSIS_ID)).willReturn(new Diagnosis());
		given(this.medicineService.findAll()).willReturn(Lists.newArrayList(m));
	}
	
	//TEST PARA COMPROBAR QUE INICIALIZA EL TRATAMIENTO CORRECTAMENTE
	//FUNCIONA
	 @WithMockUser(value = "spring")
	 @Test
	 void testInitNewTreatmentForm() throws Exception{
		 mockMvc.perform(get("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("treatment"))
			.andExpect(view().name("vets/createTreatmentForm"));
	 }
	//TEST PARA COMPROBAR QUE CREA EL TRATAMIENTO SIN MEDICINA CORRECTAMENTE
	//FUNCIONA
	 @WithMockUser(value = "spring")
	 @Test
	 void testProcessNewTreatmentForm1() throws Exception {
			mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
					.with(csrf())
					.param("recomendations", "Keep Training")
					.param("startDate", "2021/08/28")
					.param("endDate", "2021/08/28"))
					.andExpect(status().is3xxRedirection())
					.andExpect(view().name("redirect:/vets/{vetId}/diagnosis"));
					
		
		 }
	 
	 @WithMockUser(value = "spring")
	 @Test
	 void testProcessNewTreatmentForm2() throws Exception {
			mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
					.with(csrf())
					.param("recomendations", "Keep Training")
					.param("startDate", "2021/08/28")
					.param("endDate", "2021/08/28")
					.param("medicine", "MDS"))
					.andExpect(status().is3xxRedirection())
					.andExpect(view().name("redirect:/vets/{vetId}/diagnosis"));
					
		
		 }
	 
	 // TEST PARA COMPROBAR QUE NO SE PUEDE CREAR UN TRATAMIENTO SI LA FECHA DE FIN ES ANTERIOR A LA FECHA DE INICIO
	 //FUNCIONA
	 @WithMockUser(value = "spring")
	 @Test
	 void testProcessNewTreatmentForm3() throws Exception {
	 mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
			 .with(csrf())
			 .param("recomendations", "Keep Training")
			 .param("startDate", "2023/11/28")
			 .param("endDate", "2021/08/28"))
	 		 .andExpect(status().isOk())
	 		 .andExpect(model().attributeHasErrors("treatment"))
	 		 .andExpect(model().attributeHasFieldErrors("treatment", "endDate"))
	 		 .andExpect(view().name("vets/createTreatmentForm"));
	 }
	 // TEST PARA COMPROBAR QUE NO SE PUEDE CREAR UN TRATAMIENTO SI NO EXISTE RECOMENDACIÓN
	 //FUNCIONA
	 @WithMockUser(value = "spring")
	 @Test
	 void testProcessNewTreatmentForm4() throws Exception {
	 mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
			 .with(csrf())
			 .param("recomendations", "")
			 .param("startDate", "2022/08/28")
			 .param("endDate", "2024/08/28"))
	 		 .andExpect(status().isOk())
	 		 .andExpect(model().attributeHasErrors("treatment"))
	 		 .andExpect(model().attributeHasFieldErrors("treatment", "recomendations"))
	 		 .andExpect(view().name("vets/createTreatmentForm"));
	 }
	 
	 // TEST PARA COMPROBAR QUE NO SE PUEDE CREAR UN TRATAMIENTO SI NO EXISTE LA FECHA DE FIN
	 //FUNCIONA
	 @WithMockUser(value = "spring")
	 @Test
	 void testProcessNewTreatmentForm5() throws Exception {
	 mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
			 .with(csrf())
			 .param("recomendations", "Keep training")
			 .param("startDate", "2024/08/28")
			 .param("endDate", ""))
	 		 .andExpect(status().isOk())
	 		 .andExpect(model().attributeHasErrors("treatment"))
	 		 .andExpect(model().attributeHasFieldErrors("treatment", "endDate"))
	 		 .andExpect(view().name("vets/createTreatmentForm"));
	 }
	 
	 // TEST PARA COMPROBAR QUE NO SE PUEDE CREAR UN TRATAMIENTO SI NO EXISTE LA FECHA DE INICIO
	 //FUNCIONA
	 @WithMockUser(value = "spring")
	 @Test
	 void testProcessNewTreatmentForm6() throws Exception {
	 mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
			 .with(csrf())
			 .param("recomendations", "Keep training")
			 .param("startDate", "")
			 .param("endDate", "2024/08/28"))
	 		 .andExpect(status().isOk())
	 		 .andExpect(model().attributeHasErrors("treatment"))
	 		 .andExpect(model().attributeHasFieldErrors("treatment", "startDate"))
	 		 .andExpect(view().name("vets/createTreatmentForm"));
	 }
	
	 @WithMockUser(value = "spring")
	 @ParameterizedTest
     @CsvSource ({
         "keep training, 2020/11/28, 2020/08/28",
         "medicines, 2020/12/29, 2019/03/28",
     })
	  void testProcessNewTreatmentForm3(String recomendations, String startDate, String endDate) throws Exception {
	  mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
          .with(csrf())
          .param("recomendations", recomendations)
          .param("startDate", startDate)
          .param("endDate", endDate))
           .andExpect(status().isOk())
           .andExpect(model().attributeHasErrors("treatment"))
           .andExpect(model().attributeHasFieldErrors("treatment", "endDate"));
	 }

}
