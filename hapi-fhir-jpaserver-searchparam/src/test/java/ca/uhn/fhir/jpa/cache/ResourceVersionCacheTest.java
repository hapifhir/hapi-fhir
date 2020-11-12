package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceVersionCacheTest {
	private static String RESOURCE_TYPE_PATIENT = "Patient";
	private static String RESOURCE_TYPE_PRACTITIONER = "Practitioner";

	@Test
	public void testInitialize() {
		// FIXME KBD Add Test Code
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
