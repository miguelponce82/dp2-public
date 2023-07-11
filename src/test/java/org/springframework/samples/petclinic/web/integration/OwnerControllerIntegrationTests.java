package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ResidenceService;
import org.springframework.samples.petclinic.web.OwnerController;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OwnerControllerIntegrationTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private OwnerController ownerController;


	@Autowired
	private OwnerService ownerService;
        
	@Autowired
	private PetService petService;
	
	
    @Test
	void testInitCreationForm() throws Exception {
		ModelMap model = new ModelMap();
		
		String view = ownerController.initCreationForm(model);
		
		assertEquals(view,"owners/createOrUpdateOwnerForm");
		assertNotNull(model.get("owner"));		
	}

	
    @Test
	void testProcessCreationFormSuccess() throws Exception {
    	Owner owner = new Owner();
    	User user = new User();
    	user.setUsername("bobgarci");
    	user.setPassword("bobpass");
    	user.setEnabled(true);
    	
    	owner.setFirstName("Bob");
    	owner.setLastName("García");
    	owner.setTelephone("635283929");
    	owner.setAddress("Calle San Jacinto");
    	owner.setCity("Sevilla");
    	owner.setUser(user);
    		
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(),"");
		
		String view = ownerController.processCreationForm(owner, bindingResult);
    	
		assertEquals(view, "redirect:/owners/" + owner.getId());				
	}

	
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
    	Owner owner = new Owner();
    	User user = new User();
    	user.setUsername("bobgarci");
    	user.setPassword("bobpass");
    	user.setEnabled(true);
    	
    	owner.setFirstName("Bob");
    	owner.setTelephone("635283929");
    	owner.setAddress("Calle San Jacinto");
    	owner.setCity("Sevilla");
    	owner.setUser(user);   	
    	
		BindingResult bindingResult = new MapBindingResult(new HashMap(),"");
		bindingResult.reject("lastName", "Requied!");
		
		String view = ownerController.processCreationForm(owner, bindingResult);
		
		assertEquals(view, "owners/createOrUpdateOwnerForm");		
	}
    
    @Test
   	void testInitFindForm() throws Exception {
   		ModelMap model = new ModelMap();
   		
   		String view = ownerController.initFindForm(model);
   		
   		assertEquals(view, "owners/findOwners");
   		assertNotNull(model.get("owner"));		
   	}

    @Test
  	void testProcessFindAllFormSuccess() throws Exception {
    	ModelMap model = new ModelMap();
      	Owner owner = new Owner();
  		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(),""); 		
  		String view = ownerController.processFindForm(owner, bindingResult, model);
      	
  		assertEquals(view, "owners/ownersList");				
  	}

    @Test
  	void testProcessFindByLastNameFormSuccess() throws Exception {
    	ModelMap model = new ModelMap();
      	Owner owner = ownerService.findOwnerById(TEST_OWNER_ID);
  		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(),""); 		
  		String view = ownerController.processFindForm(owner, bindingResult, model);
      	
  		assertEquals(view, "redirect:/owners/" + owner.getId());				
  	}
    
    @Test
  	void testProcessFindByLastNameFormErrors() throws Exception {
    	ModelMap model = new ModelMap();
      	
    	Owner owner = new Owner();
      	owner.setLastName("djkfhds");
  		
      	BindingResult bindingResult = new MapBindingResult(new HashMap(),""); 		
      	bindingResult.reject("lastName", "Not found.");
      	
  		String view = ownerController.processFindForm(owner, bindingResult, model);	
  		
  		assertEquals(view, "owners/findOwners");				
  	}

	@Test
	void testInitUpdateForm() throws Exception {		
		ModelMap model = new ModelMap();
		
		String view = ownerController.initUpdateOwnerForm(TEST_OWNER_ID, model);
		
		assertEquals(view, "owners/createOrUpdateOwnerForm");
		assertNotNull(model.get("owner"));
	}
	
	@Test
	void testProcessUpdateFormHasSuccess() throws Exception {
    	Owner owner = ownerService.findOwnerById(TEST_OWNER_ID);
    	owner.setLastName("Cordero");
    	
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(),"");
		
		String view = ownerController.processUpdateOwnerForm(owner, bindingResult, TEST_OWNER_ID);
		
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
    
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		Owner owner = ownerService.findOwnerById(TEST_OWNER_ID);
    	owner.setLastName(null);
    	
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(),"");
		bindingResult.reject("lastName", "Requied!");
		
		String view = ownerController.processUpdateOwnerForm(owner, bindingResult, TEST_OWNER_ID);
				
		assertEquals(view, "owners/createOrUpdateOwnerForm");
	}
	
	@Test
	void testShowOwnerHasSuccess() throws Exception {
		ModelAndView view = ownerController.showOwner(TEST_OWNER_ID);	
		assertEquals(view.getViewName(), "owners/ownerDetails");
	}
	
	@Test
	void testShowResidenceHasSuccess() throws Exception {
		ModelMap model = new ModelMap();
		
		String view = ownerController.showResidence(TEST_PET_ID, model);	
		assertEquals(view, "residences/residenceList");
		assertNotNull(model.get("residences"));
	}
	
	@Test
	void testShowResidenceHaserrors() throws Exception {
		ModelMap model = new ModelMap();	
		
		Pet pet = new Pet();
    	PetType petType = petService.findPetTypes().iterator().next();
    	pet.setName("Betty");
    	pet.setType(petType);
    	pet.setBirthDate(LocalDate.now());
    	pet.setId(100);
    	
		String view = ownerController.showResidence(pet.getId(), model);
		assertEquals(view, "redirect:/errorNoElement");
		assertNull(model.get("residences"));
	}

}