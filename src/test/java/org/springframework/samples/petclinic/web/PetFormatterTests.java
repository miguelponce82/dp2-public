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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;

@ExtendWith(MockitoExtension.class)
public class PetFormatterTests {
	
	@Mock
	private PetService	petService;
	
	private PetFormatter petFormatter;
	
	@BeforeEach
	void setup() {
		petFormatter = new PetFormatter(petService);
	}
	
	@Test
	void testPrint() {
		Pet p = new Pet();
		p.setName("Rodolfo");
		String petName = petFormatter.print(p, Locale.ENGLISH);
		assertEquals("Rodolfo", petName);
	}
	
	@Test
	void shouldParse() throws ParseException {
		Mockito.when(petService.findAll()).thenReturn(makePet());
		Pet p = petFormatter.parse("Rodolfo", Locale.ENGLISH);
		assertEquals("Rodolfo", p.getName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(petService.findAll()).thenReturn(makePet());
		Assertions.assertThrows(ParseException.class, () -> 
		petFormatter.parse("Mikel", Locale.ENGLISH));
	}
	
	private Collection<Pet> makePet(){
		Collection<Pet> pets = new ArrayList<>();
		pets.add(new Pet() {
			{
				setName("Rodolfo");
			}
		});
		pets.add(new Pet() {
			{
				setName("Pepito");
			}
		});
		return pets;
	}

}
