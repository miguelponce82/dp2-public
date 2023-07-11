package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "treatments")
public class Treatment extends BaseEntity {
	
	@NotEmpty
	@Column(name = "recomendations")
	private String recomendations;
	
	
	@javax.validation.constraints.NotNull
	@Column(name = "start_date")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate startDate;
	
	@javax.validation.constraints.NotNull
	@Column(name = "end_date")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate endDate;

	@OneToOne(optional = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST} )
	@JoinColumn(name = "medicine_id")
	private Medicine medicine;

	public String getRecomendations() {
		return recomendations;
	}

	public void setRecomendations(String recomendations) {
		this.recomendations = recomendations;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	
	
	
}
