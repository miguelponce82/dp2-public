package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.DiagnosisRepository;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiagnosisService {
	

	private DiagnosisRepository diagnosisRepository;
	private TreatmentService treatmentService;
	private ResidenceService residenceService;
	
	@Autowired
	public DiagnosisService(DiagnosisRepository diagnosisRepository, TreatmentService treatmentService, ResidenceService residenceService) {
		this.diagnosisRepository = diagnosisRepository;
		this.treatmentService = treatmentService;
		this.residenceService = residenceService;
	}	

	@Transactional
	public void deleteDiagnosis(Diagnosis diagnosis) {
		Residence r = diagnosis.getResidence();
		Treatment t = diagnosis.getTreatment();
		
		if(r != null) {
			diagnosis.setResidence(null);
			this.residenceService.deleteResidence(r);
		}
		
		if(t != null) {
			t.setMedicine(null);
			diagnosis.setTreatment(null);
			this.treatmentService.deleteTreatment(t);
		}
		
		diagnosisRepository.delete(diagnosis);
		
	}

	@Transactional(readOnly=true)
	@Cacheable("allDiagnosis")
	public Collection<Diagnosis> findAll() {
		// TODO Auto-generated method stub
		Collection<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
		for(Diagnosis d: this.diagnosisRepository.findAll()) {
			diagnosis.add(d);
		}
		return diagnosis;
	}
	
	@Transactional(readOnly= true, rollbackFor = NoElementOnListException.class)
	public Collection<Diagnosis> listDiagnosis() throws NoElementOnListException {
		Collection<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
		diagnosis = findAll();
		if(diagnosis.isEmpty()) {
			throw new NoElementOnListException();
		}
		else {
			return diagnosis;
		}

	}
	@Transactional(readOnly= true, rollbackFor = NoElementOnListException.class)
	public Collection<Diagnosis> findAllDiagnosisByPetId(int petId)  throws NoElementOnListException {
		Collection<Diagnosis> diagnosis = this.diagnosisRepository.findAllDiagnosisByPetId(petId);
		if(diagnosis.isEmpty()) {
			throw new NoElementOnListException();
		}
		else {
			return diagnosis;
		}
	}
	@Transactional(readOnly= true, rollbackFor = NoElementOnListException.class)
	public Collection<Diagnosis> findAllDiagnosisByVetId(Vet vet) throws NoElementOnListException {
		Collection<Diagnosis> diagnosis = this.diagnosisRepository.findAllDiagnosisByVetId(vet);
		if(diagnosis.isEmpty()) {
			throw new NoElementOnListException();
		}
		
		return diagnosis;
	}
	
	@Transactional(readOnly= true)
	public Diagnosis findDiagnosisById(int diagnosisId) {
		return this.diagnosisRepository.findById(diagnosisId).get();
	}

}
