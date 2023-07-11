package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.model.Vet;

public interface DiagnosisRepository extends CrudRepository<Diagnosis, Integer>{
	
	@Query("Select DISTINCT p.diagnosis from Pet p where p.id=:id")
	Collection<Diagnosis> findAllDiagnosisByPetId(@Param("id") int id);
	
	@Query("Select d from Diagnosis d where d.vet = ?1")
	Collection<Diagnosis> findAllDiagnosisByVetId(Vet vet);
	
	@Query("Select d from Diagnosis d where d.residence = ?1")
	Diagnosis findDiagnosisByResidence(Residence residence);

}
