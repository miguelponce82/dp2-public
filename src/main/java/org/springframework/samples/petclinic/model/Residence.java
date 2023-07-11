
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Entity
@Data
@Table(name = "residences")
public class Residence extends BaseEntity {
	
	@Column(name = "date_start")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	dateStart;


	@Column(name = "date_end")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	dateEnd;
	

}
