package org.springframework.samples.petclinic.web;



import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.TreatmentService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedMedicineNameException;
import org.springframework.samples.petclinic.service.exceptions.NoElementOnListException;
import org.springframework.samples.petclinic.service.exceptions.TreatmentAlreadyActive;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medicines")
public class MedicineController {
	
	@Autowired
	private MedicineService medicineServ;
	
	@Autowired
	private TreatmentService treatmentServ;
	
	@InitBinder("admin")
	public void initAdminBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("medicine")
	public void initMedicineBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new MedicineValidator());
	}
	
	@GetMapping()
	@Transactional(readOnly = true)
	public String listadoMedicines(ModelMap modelMap) {
		Collection<Medicine> medicines = new ArrayList<Medicine>();
		try {
		medicines= medicineServ.listMedicine();}
		catch (NoElementOnListException ex) {
			return "redirect:/errorNoElement";
		}
		String vista= "medicines/listadoMedicamentos";
		modelMap.addAttribute("medicines", medicines);
		return vista;
	}
	
	@GetMapping(path="/delete/{medicineId}")
    public String processDelete( @PathVariable("medicineId") final int medicineId, final ModelMap model) {
        Medicine medicine = this.medicineServ.findMedicineById(medicineId);
        try {
        	this.medicineServ.delete(medicine);
        }
        catch(TreatmentAlreadyActive ex){
        	return "errors/errorTreatmentActive";
        }
        Iterable<Medicine> medicines= medicineServ.findAll();
        model.addAttribute("medicines", medicines);

        return "medicines/listadoMedicamentos";
    }
	
	@GetMapping(value = "/edit/{medicineId}")
	//@Transactional(readOnly = true)  PREGUNTAR
	public String initUpdateMedicineForm(@PathVariable("medicineId") int medicineId,ModelMap model) {
		Medicine med = this.medicineServ.findMedicineById(medicineId);
		model.addAttribute(med);
		return "medicines/updateMedicinesForm";
	}

	@PostMapping(value = "/edit/{medicineId}")
	public String processUpdateMedicineForm(@Valid Medicine med, BindingResult result,
			@PathVariable("medicineId") int medicineId) {
		if (result.hasErrors()) {
			return "medicines/updateMedicinesForm";
		}
		else {
			med.setId(medicineId);
			try{
            	this.medicineServ.save(med);
            }catch(DuplicatedMedicineNameException ex){
                result.rejectValue("name", "duplicate", "already exists");
                return "medicines/createMedicinesForm";
            }
			return "redirect:/medicines";
		}
	}
	
	@GetMapping(value = "/new")
	//@Transactional(readOnly = true) preguntar
	public String initCreationForm(ModelMap model) {
		Medicine medicine = new Medicine();
		model.put("medicine", medicine);
		return "medicines/createMedicinesForm";
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Medicine medicine, BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			model.put("medicine", medicine);
			return "medicines/createMedicinesForm";
		}
		else {
                    try{
                    	this.medicineServ.save(medicine);
                    }catch(DuplicatedMedicineNameException ex){
                        result.rejectValue("name", "duplicate", "already exists");
                        return "medicines/createMedicinesForm";
                    }
                    return "redirect:/medicines";
		}
	}
	

}
