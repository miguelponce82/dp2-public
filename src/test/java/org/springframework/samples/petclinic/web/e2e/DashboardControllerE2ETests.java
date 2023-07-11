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
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ResidenceService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.CrashController;
import org.springframework.samples.petclinic.web.DashboardController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Test class for {@link CrashController}
 *
 * @author Colin But
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@TestPropertySource(
//  locations = "classpath:application-mysql.properties")
public class DashboardControllerE2ETests {
	

	@Autowired
	private DashboardController dashboardController;
	
	@Autowired
	private DiagnosisService diagnosisService;
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private VetService vetService;
	
	@Autowired
	private ResidenceService residenceService;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	 @Test
	 void testListAllDiagnosisAdmin() throws Exception {
		 mockMvc.perform(get("/admin/diagnosis")
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("diagnosis/diagnosisList"))
		 	.andExpect(model().attributeExists("diagnosis"));
	 }
	
	@WithMockUser(username="admin1",authorities= {"admin"})
    @Test
	void testFindAllResidencesNotEndedSuccess() throws Exception {
		mockMvc.perform(get("/residences")
							.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("residences/adminResidenceList"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("currentDate"));
	}
	
	//HACER TEST petfrequent
	
	// IMPLEMENTADO EN DASHBOARDE2ECONTROLLER
	// PORQUE NO FUNCIONA LA RUTA /admin SOLA CON MOCK, DA COMO RESULTADO 404
		 
//		 @WithMockUser(value = "spring")
//		 @Test
//		 void testVetWithMostVisits() throws Exception {
//			 mockMvc.perform(get("/admin")
//					 .with(csrf()))
//			 	.andExpect(status().isOk())
//			 	.andExpect(model().attributeExists("vetWithMostVisits"));
//		 }
	//	

}
