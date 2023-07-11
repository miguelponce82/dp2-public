package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.repository.DiagnosisRepository;

public interface SpringDataDiagnosisRepository extends DiagnosisRepository, Repository<Diagnosis, Integer>{

}
