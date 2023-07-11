package org.springframework.samples.petclinic.web;

/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

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
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.ResidenceService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */

//FUNCIONA COMPLETO

@WebMvcTest(value = ResidenceController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class ResidenceControllerTests {

	private static final int TEST_RESIDENCE_ID = 1;
		
	private static final int TEST_VET_ID = 1;
	
	private static final int TEST_DIAGNOSIS_ID = 1;


	@MockBean
	private ResidenceService residenceService;
	
	@MockBean
	private DiagnosisService diagnosisService;

	@Autowired
	private MockMvc mockMvc;
	
	private Residence residence;

	@BeforeEach
	void setup() {
		
		LocalDate dateStart = LocalDate.now();
		LocalDate dateEnd = LocalDate.of(2020, 8, 15);
		
		residence = new Residence();
		residence.setId(TEST_RESIDENCE_ID);
		residence.setDateStart(dateStart);
		residence.setDateEnd(dateEnd);
		
		given(this.residenceService.findResicenceById(TEST_RESIDENCE_ID)).willReturn(residence);
		given(this.diagnosisService.findDiagnosisById(TEST_DIAGNOSIS_ID)).willReturn(new Diagnosis());
	}

	@WithMockUser(value = "spring")
        @Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID))
			.andExpect(status().isOk()).andExpect(model().attributeExists("residence"))
			.andExpect(view().name("vets/createResidenceForm"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
							.with(csrf())
							.param("dateStart", "2020/08/08")
							.param("dateEnd", "2020/09/09"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/{vetId}/diagnosis"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
							.with(csrf())
							.param("dateStart", "2020/08/08"))
				.andExpect(model().attributeHasErrors("residence"))
				.andExpect(model().attributeHasFieldErrors("residence", "dateEnd"))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/createResidenceForm"));
	}
    
	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors1() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
							.with(csrf())
							.param("dateStart", "2020/09/09")
							.param("dateEnd", "2020/08/08"))
				.andExpect(model().attributeHasErrors("residence"))
				.andExpect(model().attributeHasFieldErrors("residence", "dateEnd"))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/createResidenceForm"));
	}
	
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors3() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
							.with(csrf()))
				.andExpect(model().attributeHasErrors("residence"))
				.andExpect(model().attributeHasFieldErrors("residence", "dateStart"))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/createResidenceForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors4() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
							.with(csrf())
							.param("dateEnd", "2020/08/08"))
				.andExpect(model().attributeHasErrors("residence"))
				.andExpect(model().attributeHasFieldErrors("residence", "dateStart"))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/createResidenceForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors5() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
							.with(csrf())
							.param("dateStart", "2020/08/08"))
				.andExpect(model().attributeHasErrors("residence"))
				.andExpect(model().attributeHasFieldErrors("residence", "dateEnd"))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/createResidenceForm"));
	}
	
	@WithMockUser(value = "spring")
	@ParameterizedTest
    @CsvSource ({
        "2020/09/09, 2020/08/08",
        "2020/03/29, 2020/03/28",
    })
    void testProcessCreationFormHasErrors1(String dateStart, String dateEnd) throws Exception {
        mockMvc.perform(post("/vets/{vetId}/diagnosis/{diagnosisId}/residence/new", TEST_VET_ID, TEST_DIAGNOSIS_ID)
                            .with(csrf())
                            .param("dateStart", dateStart)
                            .param("dateEnd", dateEnd))
                .andExpect(model().attributeHasErrors("residence"))
                .andExpect(model().attributeHasFieldErrors("residence", "dateEnd"))
                .andExpect(status().isOk())
                .andExpect(view().name("vets/createResidenceForm"));
    }
	

	
	
}
