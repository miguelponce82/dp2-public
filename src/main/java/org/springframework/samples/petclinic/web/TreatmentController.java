package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Diagnosis;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Treatment;
import org.springframework.samples.petclinic.service.DiagnosisService;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TreatmentController {
	
	private final MedicineService medicineService;
	private final TreatmentService treatmentService;
	private final DiagnosisService diagnosisService;
	
	@Autowired
	public TreatmentController(MedicineService medicineService, TreatmentService treatmentService, DiagnosisService diagnosisService) {
		this.medicineService = medicineService;
		this.treatmentService = treatmentService;
		this.diagnosisService = diagnosisService;
	}
	
	@InitBinder("treatment")
	public void initTreatmentBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new TreatmentValidator());
	}
	
	@GetMapping(value="/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new")
	public String initNewTreatmentForm(@PathVariable("diagnosisId") int diagnosisId, ModelMap model ) {
		Treatment treatment = new Treatment();
		Diagnosis diagnosis = this.diagnosisService.findDiagnosisById(diagnosisId);
		Collection<Medicine> medicines = this.medicineService.findAll();
		diagnosis.setTreatment(treatment);
		model.put("medicine", medicines);
		model.put("treatment", treatment);
		model.put("diagnosis", diagnosis);
		return "vets/createTreatmentForm";
	}
	
	@PostMapping(value="/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new")
	public String processNewTreatmentForm(@Valid Treatment treatment, BindingResult result, ModelMap model, @PathVariable("diagnosisId") int  diagnosisId ) {
		Diagnosis d = this.diagnosisService.findDiagnosisById(diagnosisId);
		if (result.hasErrors()) {
		Collection<Medicine> medicines = this.medicineService.findAll();
		model.put("medicine", medicines);
		model.put("treatment", treatment);
		model.put("diagnosis", d);
		return "vets/createTreatmentForm";
		}
		else {
			if(d.getTreatment() != null) {
				result.rejectValue("recomendations", "It is no allowed to create more than one treatment for each diagnosis",
						"It is no allowed to create more than one treatment for each diagnosis");
				Collection<Medicine> medicines = this.medicineService.findAll();
				model.put("medicine", medicines);
				model.put("treatment", treatment);
				model.put("diagnosis", d);
				return "vets/createTreatmentForm";
				
			}
			else {
			d.setTreatment(treatment);
			this.treatmentService.saveTreatment(treatment);
			}
		}
		return "redirect:/vets/{vetId}/diagnosis";

	}
	
	
}
