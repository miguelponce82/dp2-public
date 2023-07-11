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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Treatment;
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
class TreatmentServiceTest {

	@Autowired
	protected TreatmentService treatmentService;
	
	@Autowired
	protected MedicineService medicineService;
	
	@Autowired
	protected DiagnosisService diagnosisService;
	
	@Test
	void shouldFindAllTreatment() {
		Iterable<Treatment> treatment= this.treatmentService.findAll();
		Collection<Treatment> treatmentAlt = (Collection<Treatment>) treatment;
		assertThat(treatmentAlt.size()).isEqualTo(7);
	}
	
	@Test
	void shouldFindSingleTreatment() {
		Treatment treatment = this.treatmentService.findTreatmentById(2);
		assertThat(treatment.getStartDate()).isEqualTo("2020-03-01");
		assertThat(treatment.getEndDate()).isEqualTo("2020-04-01");
		assertThat(treatment.getRecomendations()).isNotNull();
		assertThat(treatment.getMedicine().getId()).isEqualTo(2);
	}
	
	@Test
	 void shouldDeleteTreatment() {
		Diagnosis d = this.diagnosisService.findDiagnosisById(1);
		Integer preSize = this.treatmentService.findAll().size();
		Treatment treatment = this.treatmentService.findTreatmentById(1);
		d.setTreatment(null);
		this.treatmentService.deleteTreatment(treatment);
		Integer postSize = this.treatmentService.findAll().size();
		assertThat(postSize == preSize-1);
		assertThat(d.getTreatment()).isNull();

	}
	
	@Test 
	void shouldDeleteMedicineById() throws DataAccessException {
		Medicine med = this.medicineService.findMedicineById(1);
		Treatment t = this.treatmentService.findTreatmentById(1);
		t.setMedicine(med);
		this.treatmentService.deleteMedicineById(med);
		assertThat(t.getMedicine() == null);
		
	}
	
	@Test
	@Disabled
	void shouldSaveTreatment() throws DataAccessException{
		Treatment t = new Treatment();
		t.setEndDate(LocalDate.now().plusYears(2));
		t.setStartDate(LocalDate.now());
		t.setRecomendations("EAT PROPERLY");
		t.setId(20);
		this.treatmentService.saveTreatment(t);
		Treatment test = this.treatmentService.findTreatmentById(20);
		assertThat(test != null);
		assertThat(test.getRecomendations().equals("EAT PROPERLY"));
		assertThat(test.getStartDate().equals(LocalDate.now()));
		assertThat(test.getEndDate().equals(LocalDate.now().plusYears(2)));
	}
	
	@Test 
	void shouldFindActiveTreatment() throws DataAccessException{
		Medicine m =  this.medicineService.findMedicineById(1);
		Collection<Treatment> test = this.treatmentService.findActiveTreatment(m);
		assertThat(test.size() == 4);
	}
	//findActiveTreatment
	
    @ParameterizedTest
    @CsvSource ({
    	"1, 2020-02-27, reposo y tomar las medicinas dos veces al día, 2020-02-25, 1 ",
    	"2, 2020-03-01, operación del pulmón izquierdo, 2020-04-01, 2", 
    	"3, 2020-03-02, solo reposo varios dias, 2020-03-05, 3 ",
    	"4, 2020-03-02, estancia durante unos dias y tomar medicación, 2020-03-11, 4 ",
    	"5, 2031-03-02, Absoluto reposo, 2032-03-11, 1",
    	"6, 2030-03-02, No dejar de mover el pico, 2031-03-11, 1 "
    })
    void shouldFindAllTreatmentsParameterized(Integer id, LocalDate startDate, String recomendations, LocalDate endDate, Integer medicineId) {
    	Collection <Treatment> treatments = this.treatmentService.findAll();
    	Treatment t = EntityUtils.getById(treatments, Treatment.class, id);
    	Medicine m = this.medicineService.findMedicineById(medicineId);
    	assertThat(t.getStartDate().equals(startDate));
    	assertThat(t.getRecomendations().equals(recomendations));
    	assertThat(t.getEndDate().equals(endDate));
    	assertThat(t.getMedicine().equals(m));

    }
	
}
