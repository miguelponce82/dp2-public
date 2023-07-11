package org.springframework.samples.petclinic.web;

import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers=VetController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class VetControllerTests {
	
	private static final int TEST_VET_ID = 1;

	@Autowired
	private VetController vetController;

	@MockBean
	private VetService clinicService;
	
	@MockBean
	private DiagnosisService diagnosisService;
	
	@MockBean
	private VisitService visitService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {

		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james, helen));
	}
    
	//FUNCIONA
	
    @WithMockUser(value = "spring")
		@Test
	void testShowVetListHtml() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().isOk()).andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}	

    //FUNCIONA
    
	@WithMockUser(value = "spring")
        @Test
	void testShowVetListXml() throws Exception {
		mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
				.andExpect(content().node(hasXPath("/vets/vetList[id=1]/id")));
	}
	
	//FUNCIONA
	
	@WithMockUser(value = "spring")
    @Test
    void testShowVisits() throws Exception {
	mockMvc.perform(get("/vets/{vetId}/visits", TEST_VET_ID)).andExpect(status().isOk())
			.andExpect(model().attributeExists("visits")).andExpect(view().name("visits/visitList"));
	}
	
	//FUNCIONA
	@WithMockUser(value = "spring")
    @Test
    void testShowVets() throws Exception {
	mockMvc.perform(get("/vets/owners")).andExpect(status().isOk())
			.andExpect(model().attributeExists("selections")).andExpect(view().name("vets/ownersListVets"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindVetForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/diagnosis", TEST_VET_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("diagnosis"))
			.andExpect(view().name("diagnosis/diagnosisList"));
	}
	 @WithMockUser(value = "spring")
	 @Test
	 void testVetHistory() throws Exception {
		 mockMvc.perform(get("/vets/{vetId}/visitHistory",  TEST_VET_ID)
				 .with(csrf()))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("visits/visitList"))
		 	.andExpect(model().attributeExists("visits"));
	 }
	 //LA PRUEBA DE ERROR DEL METODO DE VET HISTORY ESTÁ HECHO EN LA PRUEBA DE INTEGRACIÓN CORRESPONDIENTE
	 

	 
	 
 

	 
}
