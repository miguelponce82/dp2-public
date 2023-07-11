package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = VisitController.class,
			includeFilters = @ComponentScan.Filter(value = VetFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class VisitControllerTests {

	private static final int TEST_PET_ID = 1;

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private VisitController visitController;

	@MockBean
	private PetService petService;
	
	@MockBean
	private OwnerService clinicService;
	
	@MockBean
	private VisitService visitService;
	
	@MockBean
	private VetService vetService;

	@Autowired
	private MockMvc mockMvc;
	
	private Pet rodolfo;

	@BeforeEach
	void setup() {
		LocalDate date = LocalDate.now();
		PetType type = new PetType();
		type.setId(10);
		type.setName("parrot");
		rodolfo = new Pet();
		rodolfo.setId(TEST_PET_ID);
		rodolfo.setName("Rodolfo");
		rodolfo.setBirthDate(date);
		rodolfo.setType(type);
		
		Vet vet1 = new Vet();
		vet1.setId(2);
		vet1.setFirstName("maquina");
		vet1.setLastName("Fiera");
		
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(rodolfo);
		given(this.clinicService.findOwnerById(TEST_OWNER_ID)).willReturn(new Owner());
		given(this.vetService.findVets()).willReturn(Lists.newArrayList(vet1));
		
		
	}

	//FUNCIONA
        @WithMockUser(value = "spring")
        @Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"))
				.andExpect(model().attributeExists("visit"))
				.andExpect(model().attributeExists("vets"));
	}

    //FUNCIONA
	@WithMockUser(value = "spring")
    @Test
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
							.with(csrf())
							.param("description", "Visit Description")
							.param("date", "2020/08/28")
							.param("time", "20:00")
							.param("vet", "maquina"))
                .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	//FUNCIONA
	@WithMockUser(value = "spring")
        @Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
							.with(csrf())
							.param("name", "George")
							.param("date", "2020/07/09")
							.param("description", "hello"))
				.andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}
	
	//FUNCIONA
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormHasErrors() throws Exception {
	mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
						.with(csrf())
						.param("date", "2020/07/09")
						.param("description", "hello"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("visit"))
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
}
	
	

	//FUNCIONA
	
	@WithMockUser(value = "spring")
        @Test
	void testShowVisits() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("visits")).andExpect(view().name("visits/visitList"));
	}

}
