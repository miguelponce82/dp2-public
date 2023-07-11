/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.samples.petclinic.service.exceptions.NoStatusProperlyForResidenceException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
//@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class ResidenceServiceTests {

	@Autowired
	protected ResidenceService residenceService;
	
	@Autowired
	protected VetService vetService;
	
	@Autowired
	protected DiagnosisService diagnosisService;
	
	@Test
	void shouldFindResidenceById() throws Exception {
		
		Residence residence = residenceService.findResicenceById(1);	
		assertThat(residence.getDateStart()).isNotNull();
		assertThat(residence.getDateEnd()).isNotNull();
		
	}

	@Test
	void shouldFindResidencesByPet() throws Exception {
		
		Set<Residence> residences = residenceService.findResidences(1);
		
		assertThat(residences.size()).isEqualTo(3);
		Residence[] residenceArr = residences.toArray(new Residence[residences.size()]);
		assertThat(residenceArr[0].getDateEnd()).isNotNull();
		assertThat(residenceArr[0].getDateEnd()).isNotNull();
		
	}
	
	@Test
	void shouldFindAllResicenceNotEnded() throws Exception {
		
		Collection<Pet> pets = residenceService.findAllResicenceNotEnded();
		
		assertThat(pets.size()).isEqualTo(3);
		Pet[] petArr = pets.toArray(new Pet[pets.size()]);
		assertThat(petArr[0].getDiagnosis()).isNotNull();
		
	}
	
	@Test
	void shouldDeleteResindence() throws Exception {
		Residence r = residenceService.findResicenceById(1);
		residenceService.deleteResidence(r);
		Residence r2 = residenceService.findResicenceById(1);
		assertThat(r2).isNull();
	}
	
	@Test
	void shoudSaveResidence() throws DataAccessException, NoStatusProperlyForResidenceException {
		Residence r = new Residence();
		r.setId(10);
		r.setDateStart(LocalDate.now().plusMonths(4));
		r.setDateEnd(LocalDate.now().plusMonths(5));
		
		Diagnosis d = diagnosisService.findDiagnosisById(9);
		d.setResidence(r);
		
		residenceService.saveResidence(r, d);
		
		assertThat(d.getResidence()).isNotNull();
		
	}
	
	@Test
	void shoudSaveResidenceError() throws DataAccessException, NoStatusProperlyForResidenceException {
		boolean res = false;
		Residence r = new Residence();
		r.setId(10);
		r.setDateStart(LocalDate.now().plusMonths(4));
		r.setDateEnd(LocalDate.now().plusMonths(5));
		
		Diagnosis d = diagnosisService.findDiagnosisById(3);
		
		try {
			residenceService.saveResidence(r, d);
		}catch (NoStatusProperlyForResidenceException ex) {
			res = true;
		}
		
		
		assertThat(res == true);
		
	}
	
	@Test
	void shouldfindPetsWithActiveResidences() throws Exception{
		Collection<Pet> petWithResidences = this.residenceService.findAllResicenceNotEnded();
		assertThat(petWithResidences.size() == 4 );
	}
	
	
    @ParameterizedTest
    @CsvSource ({
    	"1, 2020-02-04, 2020-02-23 ",
    	"5, 2021-07-12, 2022-09-18 ", 
    	"6, 2021-01-12, 2022-01-18 "
    })
    void shouldFindResidencesParameterized(Integer id, LocalDate startDate, LocalDate endDate) throws DataAccessException, NoElementOnListException {
    	Collection<Residence> residences = this.residenceService.findResidences(1);
    	Residence r = EntityUtils.getById(residences, Residence.class, id);
    	assertThat(r.getDateEnd().equals(endDate));
    	assertThat(r.getDateStart().equals(startDate));
    	
    }

}
