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
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;

@ExtendWith(MockitoExtension.class)
public class VetFormatterTests {
	
	@Mock
	private VetService vetService;
	
	private VetFormatter vetFormatter;
	
	@BeforeEach
	void setup() {
		vetFormatter = new VetFormatter(vetService);
	}
	
	@Test
	void testPrint() {
		Vet vet = new Vet();
		vet.setFirstName("Maquina");
		String vetName = vetFormatter.print(vet, Locale.ENGLISH);
		assertEquals("Maquina", vetName);
	}
	
	@Test
	void shouldParse() throws ParseException{
		Mockito.when(vetService.findVets()).thenReturn(makeVets());
		Vet vet = vetFormatter.parse("Maquina", Locale.ENGLISH);
		assertEquals("Maquina", vet.getFirstName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(vetService.findVets()).thenReturn(makeVets());
		Assertions.assertThrows(ParseException.class, () ->
		vetFormatter.parse("Leon", Locale.ENGLISH));
	}
	
	private Collection<Vet> makeVets(){
		Collection<Vet> vets = new ArrayList<>();
		vets.add(new Vet() {
			{
				setFirstName("Maquina");
			}
		});
		vets.add(new Vet() {
			{
				setFirstName("Fiera");
			}
		});
		return vets;
	}

}
