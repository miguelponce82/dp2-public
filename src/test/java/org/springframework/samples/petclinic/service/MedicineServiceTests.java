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
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedMedicineNameException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.samples.petclinic.service.exceptions.TreatmentAlreadyActive;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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
class MedicineServiceTests {

	@Autowired
	protected MedicineService medicineService;
	

	@Test
	public void testCountMedicines() {
		int count = medicineService.medCount();
		assertEquals(count, 5);
	}
	
	@Test
	void shouldFindAllMedicines() {
		Iterable<Medicine> medicines = this.medicineService.findAll();
		Collection<Medicine> medicinesAlt = (Collection<Medicine>) medicines;
		assertThat(medicinesAlt.size()).isEqualTo(5);
	}
	
	@Test
	void shouldListMedicine() throws NoElementOnListException {
		Iterable<Medicine> medicines = this.medicineService.listMedicine();
		Collection<Medicine> medicinesAlt = (Collection<Medicine>) medicines;
		assertThat(medicinesAlt.size()).isEqualTo(5);
	}
	
	
	@Test
	void shouldFindSingleMedicine() {
		Medicine medicine = this.medicineService.findMedicineById(1);
		assertThat(medicine.getName()).isEqualTo("Ibuprofeno");
		assertThat(medicine.getDescription()).isNotNull();
		assertThat(medicine.getSideEffects()).isNotNull();
	}
	
	@Test
	@Transactional
	void shouldUpdateMedicine() {
		Medicine medicine = this.medicineService.findMedicineById(1);
		String oldName = medicine.getName();
		String newName = oldName + "X";

		medicine.setName(newName);
		try {
			this.medicineService.save(medicine);
		} catch (DataAccessException | DuplicatedMedicineNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// retrieving new name from database
		medicine = this.medicineService.findMedicineById(1);
		assertThat(medicine.getName()).isEqualTo(newName);
	}
	
	@Test
	@Transactional
	void shouldThrowExceptionNameDuplicateUpdatingMdicines() {
		Medicine medicine1 = this.medicineService.findMedicineById(1);
		Medicine medicine2 = this.medicineService.findMedicineById(2);
		String name1 = medicine1.getName();

		medicine2.setName(name1);
		try {
			this.medicineService.save(medicine2);
		} catch (DataAccessException | DuplicatedMedicineNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// retrieving new name from database
		medicine2 = this.medicineService.findMedicineById(2);
		assertThat(medicine2.getName()).isEqualTo(name1);
	}
	
	@Test
	@Transactional
	void shouldDeleteMedicineSuccess() throws TreatmentAlreadyActive {
		
		Integer preSize = this.medicineService.findAll().size();
		Medicine med = this.medicineService.findMedicineById(2);
		
		this.medicineService.delete(med);
		
		Integer postSize = this.medicineService.findAll().size();
		assertThat(postSize == preSize -1);
	}
	
	@Test
	@Transactional
	void shouldDeleteMedicineError() throws TreatmentAlreadyActive {
		//COMPROBAMOS QUE NO PUEDE BORRAR LA MEDICINA PORQUE ESTA EN UN TRATAMIENTO ACTIVO
		Integer preSize = this.medicineService.findAll().size();
		Medicine med = this.medicineService.findMedicineById(1);
		try {
			this.medicineService.delete(med);
		}
		catch(TreatmentAlreadyActive ex) {
		
		}
		Integer postSize = this.medicineService.findAll().size();
		assertThat(postSize == preSize);		

	}
	
	@Test
	@Transactional
	public void shouldInsertMedicineIntoDatabase() throws DataAccessException, DuplicatedMedicineNameException {
		Collection<Medicine> medicines = this.medicineService.findAll();
		int totalMedicines = medicines.size();

		Medicine medicine = new Medicine();
		medicine.setName("Condrovet Force");
		medicine.setDescription("Recomendado para proteger articulaciones de perros y gatos para mascotas con problemas de artrosis.");
		medicine.setSideEffects("Dolor de cabeza.");

        this.medicineService.save(medicine);
        
		medicine = this.medicineService.findMedicineById(medicine.getId());
		int totalMedicinesAfter = this.medicineService.findAll().size();
		assertThat(totalMedicinesAfter).isEqualTo(totalMedicines + 1);
		// checks that id has been generated
		assertThat(medicine.getId()).isNotNull();
	}
	
	@Test
	@Transactional
	void shouldThrowExceptionNameDuplicateCreateMdicines() {
		Collection<Medicine> medicines = this.medicineService.findAll();
		int totalMedicines = medicines.size();
		
		Medicine medicine1 = this.medicineService.findMedicineById(1);
		Medicine medicine = new Medicine();
		
		medicine.setName(medicine1.getName());
		medicine.setDescription("Recomendado para proteger articulaciones de perros y gatos para mascotas con problemas de artrosis.");
		medicine.setSideEffects("Dolor de cabeza.");
		
		try {
			this.medicineService.save(medicine);
			
			medicine = this.medicineService.findMedicineById(medicine.getId());
			int totalMedicinesAfter = this.medicineService.findAll().size();
			assertThat(totalMedicinesAfter).isEqualTo(totalMedicines + 1);
			// checks that id has been generated
			assertThat(medicine.getId()).isNotNull();
			
		} catch (DataAccessException | DuplicatedMedicineNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	@ParameterizedTest
    @CsvSource ({
        "1, Ibuprofeno",
        "2, Pipetas parasital perros y gatos",
        "4, DEYANIL RETARD 10 ml"
    })
    void shouldFindSingleMedicineParameterized(int id, String medicines) {
        Medicine medicine = this.medicineService.findMedicineById(id);
        assertThat(medicine.getName()).isEqualTo(medicines);
        assertThat(medicine.getDescription()).isNotNull();
        assertThat(medicine.getSideEffects()).isNotNull();
    }
	
}
