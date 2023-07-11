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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.model.Status;
import org.springframework.samples.petclinic.repository.DiagnosisRepository;
import org.springframework.samples.petclinic.repository.ResidenceRepository;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.samples.petclinic.service.exceptions.NoStatusProperlyForResidenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ResidenceService {

	@Autowired
	private ResidenceRepository residenceRepository;
	
	@Autowired
	private DiagnosisRepository diagnosisRepository;
	


	@Autowired
	public ResidenceService(final ResidenceRepository residenceRepository, DiagnosisRepository diagnosisRepository) {
		this.residenceRepository = residenceRepository;
		this.diagnosisRepository = diagnosisRepository;
	}		
	
	@Transactional(readOnly = true)
	public Residence findResicenceById(final int id) throws DataAccessException {
		return this.residenceRepository.findById(id);
	}

	@Transactional(readOnly = true,  rollbackFor = NoElementOnListException.class)
	public Set<Residence> findResidences(final int petId) throws DataAccessException, NoElementOnListException {
		Set<Residence> residences = new HashSet<Residence>();
		for(Diagnosis d: this.diagnosisRepository.findAllDiagnosisByPetId(petId)) {
			residences.add(d.getResidence());
		}
		if(residences.isEmpty() || (residences.size() == 1 && residences.contains(null))) {
			throw new NoElementOnListException();
		}
		else {
		return residences;
		}
	}
	
	@Transactional(rollbackFor = NoStatusProperlyForResidenceException.class)
	public void saveResidence(Residence residence, Diagnosis d) throws DataAccessException, NoStatusProperlyForResidenceException {
		if(d.getStatus() == Status.NOT_DANGER) {
			throw new NoStatusProperlyForResidenceException();
		}
		else {
			this.residenceRepository.save(residence);
		}
	}

	public void deleteResidence(Residence r) {
		this.residenceRepository.delete(r);
	}
	
	@Transactional(readOnly= true, rollbackFor = NoElementOnListException.class)
	@Cacheable("activeResidences")
    public Collection<Pet> findAllResicenceNotEnded() throws DataAccessException, NoElementOnListException{
		Collection<Pet> res = new ArrayList<Pet>();
		res = this.residenceRepository.findAllNotEnded();
		if(res.isEmpty()) {
			throw new NoElementOnListException();
		}
        return res;
    }

}