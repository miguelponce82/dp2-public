package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ResidenceService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.assertj.core.util.Lists;

/**
 * Test class for {@link CrashController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers=DashboardController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class DashboardControllerTests {
	
	private static final int TEST_DIAGNOSIS_ID = 1;
	private static final int TEST_VET_ID = 1;
	private static final int TEST_VISIT_ID = 5;
	private static final int TEST_PET_ID = 1;

	@Autowired
	private DashboardController dashboardController;
	
	@MockBean
	private DiagnosisService diagnosisService;
	
	@MockBean
	private PetService petService;
	
	@MockBean
	private VetService vetService;
	
	@MockBean
	private ResidenceService residenceService;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@WithMockUser(value = "spring")
	 @Test
	 void testListAllDiagnosisAdmin() throws Exception {
		 mockMvc.perform(get("/admin/diagnosis")
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("diagnosis/diagnosisList"))
		 	.andExpect(model().attributeExists("diagnosis"));
	 }
	
	@WithMockUser(value = "spring")
    @Test
	void testFindAllResidencesNotEndedSuccess() throws Exception {
		mockMvc.perform(get("/residences")
							.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("residences/adminResidenceList"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("currentDate"));
	}
		
	// IMPLEMENTADO EN DASHBOARDE2ECONTROLLER testPetfrequent() y testVetWithMostVisits()
	// PORQUE NO FUNCIONA LA RUTA /admin SOLA CON MOCK, DA COMO RESULTADO 404
	
	 @WithMockUser(value = "spring")
	 @Test
	 void chooseVetList() throws Exception {
		 mockMvc.perform(get("/vets/choose")
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("vets/chooseVet"))
		 	.andExpect(model().attributeExists("vets"));
	 }

}
