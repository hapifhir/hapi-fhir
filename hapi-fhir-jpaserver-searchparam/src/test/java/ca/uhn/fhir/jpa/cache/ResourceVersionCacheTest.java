package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceVersionCacheTest {
	private static String RESOURCE_TYPE_PATIENT = "Patient";
	private static String RESOURCE_TYPE_PRACTITIONER = "Practitioner";
	public static long NUM_TEST_SEARCH_PARAMS = 5L;

	@Autowired
	IResourceVersionSvc myResourceVersionSvc;

	// FIXME KBD Use these Spring configured items as params to the initialize() method below
	@Configuration
	static class SpringConfig {
		@Bean
		FhirContext fhirContext() {
			return FhirContext.forR4();
		}

		@Bean
		IResourceVersionSvc resourceVersionSvc() {
			IResourceVersionSvc retval = mock(IResourceVersionSvc.class);

			List<ResourceTable> entities = new ArrayList<>();
			for (long id = 0; id < NUM_TEST_SEARCH_PARAMS; ++id) {
				entities.add(createEntity(id, 1));
			}
			ResourceVersionMap resourceVersionMap = ResourceVersionMap.fromResourceIds(entities);
			when(retval.getVersionMap(anyString(), any())).thenReturn(resourceVersionMap);
			return retval;
		}
	}

	@Nonnull
	private static ResourceTable createEntity(long theId, int theVersion) {
		ResourceTable searchParamEntity = new ResourceTable();
		searchParamEntity.setResourceType("SearchParameter");
		searchParamEntity.setId(theId);
		searchParamEntity.setVersion(theVersion);
		return searchParamEntity;
	}

	@Test
	public void testInitialize() {
		ResourceVersionCache resourceVersionCache = new ResourceVersionCache();
		// FIXME KBD Use the Spring configured items from above so we pass some data to this initialize() method
		//           The question is, how to we force that data to get used here ???
		resourceVersionCache.initialize(ResourceVersionMap.fromResourceIds(Collections.EMPTY_LIST));

	}

	@Test
	public void testAddOrUpdate1Patient() {
		ResourceVersionCache resourceVersionCache = new ResourceVersionCache();
		resourceVersionCache.initialize(ResourceVersionMap.fromResourceIds(Collections.EMPTY_LIST));
		assertTrue(resourceVersionCache.keySet().isEmpty());
		Patient patientMale = createPatient(new BigDecimal(1), Enumerations.AdministrativeGender.MALE);
		resourceVersionCache.addOrUpdate(patientMale.getIdElement(), patientMale.getIdElement().getVersionIdPart());
		assertFalse(resourceVersionCache.keySet().isEmpty());
		assertEquals(1, resourceVersionCache.keySet().size());
		Map<IdDt, String> patientMap = resourceVersionCache.getMap(RESOURCE_TYPE_PATIENT);
		assertEquals(1, patientMap.size());
		IdDt patientId = patientMap.keySet().iterator().next();
		assertEquals("1", patientId.getIdPart());
		String version = patientMap.get(patientId);
		assertEquals("1", version);
	}

	@Test
	public void testAddOrUpdate2DifferentResourceTypes() {
		// FIXME KBD Add Test Code
	}

	@Test
	public void testRemove() {
		// FIXME KBD Add Test Code
	}

	private Patient createPatient(BigDecimal id, Enumerations.AdministrativeGender theGender) {
		Patient patient = new Patient();
		IdDt patientId = new IdDt();
		patientId.setParts("", RESOURCE_TYPE_PATIENT, id.toString(), "1");
		patient.setId(patientId);
		if (theGender != null) {
			patient.setGender(theGender);
		}
		return patient;
	}
}
