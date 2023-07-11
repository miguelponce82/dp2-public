package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.MedicineService;

@ExtendWith(MockitoExtension.class)
public class MedicineFormatterTests {
	
	@Mock
	private MedicineService medicineService;
	
	private MedicineFormatter medicineFormatter;
	
	@BeforeEach
	void setup() {
		medicineFormatter = new MedicineFormatter(medicineService);
	}
	
	@Test
	void testPrint() {
		Medicine med = new Medicine();
		med.setName("Paracetamol");
		String medName = medicineFormatter.print(med, Locale.ENGLISH);
		assertEquals("Paracetamol", medName);
	}
	
	@Test
	void shouldParse() throws ParseException {
		Mockito.when(medicineService.findAll()).thenReturn(makeMedicines());
		Medicine med = medicineFormatter.parse("Ibuprofeno", Locale.ENGLISH);
		assertEquals("Ibuprofeno", med.getName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(medicineService.findAll()).thenReturn(makeMedicines());
		Assertions.assertThrows(ParseException.class, () -> 
		medicineFormatter.parse("Paracetamol", Locale.ENGLISH));
	}
	
	private Collection<Medicine> makeMedicines(){
	Collection<Medicine> medicines = new ArrayList<>();
	
	medicines.add(new Medicine() {
		{
			setName("Ibuprofeno");
		}
	});
	
	medicines.add(new Medicine() {
		{
			setName("Nolotil");
		}
	});
	return medicines;
	}

}
