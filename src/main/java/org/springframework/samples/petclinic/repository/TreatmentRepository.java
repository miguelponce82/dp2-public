package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Treatment;

public interface TreatmentRepository extends CrudRepository<Treatment, Integer>{
	
	
	@Query("Select t from Treatment t where t.medicine.id=:id")
	Collection<Treatment> findTreatmentWithMedicine(@Param("id") int medicineId);
	
	@Query("Select t from Treatment t where t.medicine.id=:id and t.endDate >= current_date")
	Collection<Treatment> findActiveTreatment(@Param("id") int medicineId);
}
