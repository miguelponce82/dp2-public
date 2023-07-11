package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
//@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class DiagnosisServiceTests {

	@Autowired
	protected DiagnosisService diagnosisService;
	
    @Autowired
	protected ResidenceService residenceService;
    
    @Autowired
	protected TreatmentService treatmentService;
	
    @Autowired
	protected PetService petService;
    
    @Autowired
	protected VetService vetService;
 
    @Test
	@Transactional
    void shouldDeleteDiagnosis() {
    	Integer preSize = this.diagnosisService.findAll().size();
    	Diagnosis d = this.diagnosisService.findDiagnosisById(1);
    	Pet p = this.petService.findPetById(1);
    	
    	this.diagnosisService.deleteDiagnosis(d);
    	p.removeDiagnosis(d);
      	
    	Integer postSize = this.diagnosisService.findAll().size();
    	assertThat(postSize == preSize -1);
    }
    
    @Test
    @Transactional
    void shouldFindAllDiagnosis() {
    	Collection<Diagnosis> diagnosis = this.diagnosisService.findAll();
    	assertThat(diagnosis.size()).isEqualTo(9);
    }
    
    
    @Test
    @Transactional
    void shouldFindAllDiagnosisByPetId() throws NoElementOnListException {
    	Integer petId = 1;
    	Collection<Diagnosis> diagnosis = this.diagnosisService.findAllDiagnosisByPetId(petId);
    	assertThat(diagnosis.size()).isEqualTo(3);
    }
    
    @Test
    @Transactional
    void shouldFindAllDiagnosisByPetIdEmpty() throws NoElementOnListException {
    	boolean res = false;
    	Integer petId = 8;
    	try {
    		Collection<Diagnosis> diagnosis = this.diagnosisService.findAllDiagnosisByPetId(petId);
    	}catch(NoElementOnListException ex) {
    		res = true;
    	}
    	
    	assertThat(res == true);
    }
    
    @Test
    @Transactional
    void shouldFindAllDiagnosisByVetId() throws NoElementOnListException {
    	Vet vet = this.vetService.findVetById(1);
    	Collection<Diagnosis> diagnosis = this.diagnosisService.findAllDiagnosisByVetId(vet);
    	assertThat(diagnosis.size()).isEqualTo(2);
    }
    
    @Test
    @Transactional
    void shouldFindAllDiagnosisByVetIdEmpty() throws NoElementOnListException {
    	Vet vet = this.vetService.findVetById(3);
    	boolean res = true;
    	try {
    		Collection<Diagnosis> diagnosis = this.diagnosisService.findAllDiagnosisByVetId(vet);
    	}catch(NoElementOnListException ex) {
    		res = true;
    	}
    	
    	assertThat(res == true);
    }
    
    @Test
    @Transactional
    void shouldFindDiagnosisById() {
    	Diagnosis d = this.diagnosisService.findDiagnosisById(1);
    	Residence r = this.residenceService.findResicenceById(1);
    	Treatment t = this.treatmentService.findTreatmentById(1);
    	assertThat(d.getResidence().equals(r));
    	assertThat(d.getTreatment().equals(t));
    	assertThat(d.getDescription().equals("Broken leg"));
    	assertThat(d.getStatus().equals(Status.SURGERY));
    }
    
    @ParameterizedTest
    @CsvSource ({
    	"1,'Broken leg', 1, 1,1,1 ",
    	"2,'Sore throat', 2, 1,2,2", 
    	"4,'Coronavirus', 1, 2,4, 3 ",
    	"5,'Minor injuries', 1, 2 ,5, 5",
    	"6,'Leg injury', 1, 4 ,6 , 6"
    })
    void shouldFindAllDiagnosisParametrized(Integer id, String description, Integer statusId, Integer vetId,
    		Integer treatmentId, Integer residenceId) {
    	Collection<Diagnosis> diagnosis = this.diagnosisService.findAll();
    	Diagnosis d = EntityUtils.getById(diagnosis, Diagnosis.class, id);
    	Treatment t = this.treatmentService.findTreatmentById(treatmentId);
    	Residence r = this.residenceService.findResicenceById(residenceId);
    	Vet v = this.vetService.findVetById(vetId);
    	assertThat(d.getDescription().equals(description));
    	assertThat(d.getVet().equals(v));
    	assertThat(d.getResidence().equals(r));
    	assertThat(d.getTreatment().equals(t));
    }
    
    @ParameterizedTest
    @CsvSource ({
    	"1,'Broken leg', 1, 1,1,1 ",
    	"2,'Sore throat', 2, 1,2,2"
    })
    void shouldFindAllDiagnosisByVetIdParametrized(Integer id, String description, Integer statusId, Integer vetId,
    		Integer treatmentId, Integer residenceId) throws NoElementOnListException {
    	Vet v = this.vetService.findVetById(1);
    	Collection<Diagnosis> diagnosis = this.diagnosisService.findAllDiagnosisByVetId(v);
    	Diagnosis d = EntityUtils.getById(diagnosis, Diagnosis.class, id);
    	Treatment t = this.treatmentService.findTreatmentById(treatmentId);
    	Residence r = this.residenceService.findResicenceById(residenceId);
    	
    	assertThat(d.getDescription().equals(description));
    	assertThat(d.getResidence().equals(r));
    	assertThat(d.getTreatment().equals(t));
    }
    
    @ParameterizedTest
    @CsvSource ({
    	"1,'Broken leg', 1, 1,1,1 ",
    	"5,'Minor injuries', 1, 2 ,5, 5",
    	"6,'Leg injury', 1, 4 ,6 , 6"
    })
    void shouldFindAllDiagnosisByPetIdParametrized(Integer id, String description, Integer statusId, Integer vetId,
    		Integer treatmentId, Integer residenceId) throws NoElementOnListException {
    	Collection<Diagnosis> diagnosis = this.diagnosisService.findAllDiagnosisByPetId(1);
    	Diagnosis d = EntityUtils.getById(diagnosis, Diagnosis.class, id);
    	Treatment t = this.treatmentService.findTreatmentById(treatmentId);
    	Residence r = this.residenceService.findResicenceById(residenceId);
    	Vet v = this.vetService.findVetById(vetId);
    	
    	assertThat(d.getDescription().equals(description));
    	assertThat(d.getResidence().equals(r));
    	assertThat(d.getTreatment().equals(t));
    	assertThat(d.getVet().equals(v));

    }
    
}
