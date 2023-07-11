package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {
	

	@Autowired 
	private VisitRepository visitRepository;
	
	@Transactional(readOnly = true,  rollbackFor = NoElementOnListException.class)
	public Collection<Visit> findVisitsByVet(Vet vet) throws NoElementOnListException {
		Collection<Visit> visits = visitRepository.findByVetOrderDate(vet);
		if(visits.isEmpty()) {
			throw new NoElementOnListException();
		}
		return visits;
		
	}
	@Transactional(readOnly = true)
	public Visit findVisitById(Integer visitId) {
		
		return visitRepository.findVisitById(visitId);
		
	}
	
	@Transactional
	public Collection<Visit> findAll() {
		
		return visitRepository.findAll();
		
	}
	
	@Transactional(readOnly = true,  rollbackFor = NoElementOnListException.class)
	public Collection<Visit> findVisitHistoryByVet(Vet vet) throws NoElementOnListException {
		Collection<Visit> res = visitRepository.findHistoryByVet(vet);
		if(res.isEmpty()) {
			throw new NoElementOnListException();
		}
	return res;
		
	}
	

	@Transactional
	public void deleteVisitById(Integer visitId) {
		visitRepository.deleteVisitById(visitId);
		
	}

}
