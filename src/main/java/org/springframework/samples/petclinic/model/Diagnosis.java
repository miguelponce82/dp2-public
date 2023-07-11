package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "diagnosis")
public class Diagnosis extends BaseEntity{
	
	@NotBlank
	@Column(name = "description")
	private String description;
	
	@NotNull
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;
	
	@OneToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "treatment_id")
	private Treatment treatment;
	
	@OneToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "residence_id")
	private Residence residence;

	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Vet getVet() {
		return vet;
	}

	public void setVet(Vet vet) {
		this.vet = vet;
	}

	public Residence getResidence() {
		return residence;
	}


	public void setResidence(Residence residence) {
		this.residence = residence;
	}


	public Treatment getTreatment() {
		return treatment;
	}


	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}

	
	
	

}
