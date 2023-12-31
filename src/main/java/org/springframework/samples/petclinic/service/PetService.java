
/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.DiagnosisRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.AlreadyBookedVisitException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PetService {

	private PetRepository	petRepository;

	private VisitRepository	visitRepository;
	
	private DiagnosisRepository	diagnosisRepository;


	@Autowired
	public PetService(final PetRepository petRepository, final VisitRepository visitRepository,
			final DiagnosisRepository diagnosisRepository) {
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
		this.diagnosisRepository = diagnosisRepository;
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return this.petRepository.findPetTypes();
	}

	@Transactional(rollbackFor = AlreadyBookedVisitException.class)
	public void saveVisit(final Visit visit) throws DataAccessException, AlreadyBookedVisitException {
		Vet vet = visit.getVet();
		Collection<Visit> v = this.visitRepository.findByDateAndTime(visit.getDate(), visit.getTime(), vet);
		if (v.size() > 1) {
			throw new AlreadyBookedVisitException();
		} else {
			this.visitRepository.save(visit);
		}
	}
	
	@Transactional(rollbackFor = AlreadyBookedVisitException.class)
	public void saveDiagnosis(final Diagnosis diagnosis) throws DataAccessException {
			this.diagnosisRepository.save(diagnosis);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(final int id) throws DataAccessException {
		return this.petRepository.findById(id);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void savePet(final Pet pet) throws DataAccessException, DuplicatedPetNameException {
		Pet otherPet = pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
		if (StringUtils.hasLength(pet.getName()) && otherPet != null && otherPet.getId() != pet.getId()) {
			throw new DuplicatedPetNameException();
		} else {
			this.petRepository.save(pet);
		}
	}

	@Transactional(readOnly = true)
	public Collection<Visit> findVisitsByPetId(final int petId) {
		return this.visitRepository.findByPetId(petId);
	}
	
	@Transactional(readOnly = true)
	public Collection<Pet> findAll(){
		return this.petRepository.findAll();
	}

	@Transactional 
    public void deletePetById(final int petId) throws DataAccessException { 
        this.petRepository.deletePetById(petId); 
    }
	
	@Transactional(readOnly = true)
	public Collection<Diagnosis> findAllDiagnosisByPetId(Integer petId){
		return this.petRepository.findPetDiagnosis(petId);

	}
	
	@Transactional(readOnly = true)
	public Collection<PetType> findFrequentPet(){
		return this.petRepository.findFrequentPet();

	}

}

