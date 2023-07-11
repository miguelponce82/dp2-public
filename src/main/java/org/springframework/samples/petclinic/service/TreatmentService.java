package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TreatmentService {

	private TreatmentRepository treatmentRepository;	
	
	@Autowired
	public TreatmentService(TreatmentRepository treatmentRepository) {
		this.treatmentRepository = treatmentRepository;
	}	

	
	@Transactional(readOnly = true)	
	public Collection<Treatment> findAll()  throws DataAccessException {
		Collection<Treatment> treatment = new ArrayList<Treatment>();
		for(Treatment t: this.treatmentRepository.findAll()) {
			treatment.add(t);
		}
		return treatment;
		
	}
	
	public void deleteMedicineById(Medicine med)  throws DataAccessException{
		for(Treatment t: this.treatmentRepository.findTreatmentWithMedicine(med.getId())) {
			t.setMedicine(null);
		}
			
	}
		
	@Transactional(readOnly = true)
	public Treatment findTreatmentById(int treatmentId) throws DataAccessException {
		return treatmentRepository.findById(treatmentId).get();
		
	}


	public void saveTreatment(Treatment treatment) {
		this.treatmentRepository.save(treatment);
		
	}


	public void deleteTreatment(Treatment t) {
		this.treatmentRepository.delete(t);
	}


	public Collection<Treatment> findActiveTreatment(Medicine med) {
		return treatmentRepository.findActiveTreatment(med.getId());
	}

}
