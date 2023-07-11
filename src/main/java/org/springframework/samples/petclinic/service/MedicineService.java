package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.repository.MedicineRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedMedicineNameException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.samples.petclinic.service.exceptions.TreatmentAlreadyActive;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MedicineService {
	

	@Autowired 
	private MedicineRepository medicineRepo;
	
	@Autowired
	private TreatmentService treatmentService;
	
	@Transactional
	public int medCount() {
		
		return (int)medicineRepo.count();
		
	}
	
	public Collection<Medicine> findAll() {
		Collection<Medicine> res = new ArrayList<Medicine>();
		for(Medicine m: medicineRepo.findAll()) {
			res.add(m);
		}
		return res;		
	}

	@Transactional(readOnly= true, rollbackFor = NoElementOnListException.class)
	public Collection<Medicine> listMedicine() throws NoElementOnListException {
		Collection<Medicine> res = new ArrayList<Medicine>();
		res = findAll();
		if(res.isEmpty()) {
			throw new NoElementOnListException();
		}
		else {
		return res;
		}
		
	}
	@Transactional(rollbackFor = TreatmentAlreadyActive.class)
	public void delete(Medicine med) throws TreatmentAlreadyActive {
		Collection<Treatment> treatment = this.treatmentService.findActiveTreatment(med);
		if(!treatment.isEmpty()) {
			throw new TreatmentAlreadyActive();
		}
		else {
			this.treatmentService.deleteMedicineById(med);
			this.medicineRepo.delete(med);
		}

		
	}

	@Transactional(readOnly= true)
	public Medicine findMedicineById(int medicineId) {
		return this.medicineRepo.findById(medicineId).get();
	}
	
	/*public void save(Medicine medicine) {
		this.medicineRepo.save(medicine);
	}*/
	
	@Transactional(rollbackFor = DuplicatedMedicineNameException.class)
	public void save(final Medicine medicine) throws DataAccessException, DuplicatedMedicineNameException {
		String name = medicine.getName().toLowerCase();
		Collection<Medicine> medicines = (Collection<Medicine>) this.medicineRepo.findAll();
		Boolean except = false;
		for(Medicine m: medicines) {
			if(m.getName().toLowerCase().equals(name)) {
				except = true;
			}
		}
		
		if (except) {
			throw new DuplicatedMedicineNameException();
		} else {
			this.medicineRepo.save(medicine);
		}
	}

}
