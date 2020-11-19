package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceVersionCacheTest {
	private static String RESOURCE_TYPE_PATIENT = "Patient";
	private static String RESOURCE_TYPE_PRACTITIONER = "Practitioner";
	public static long NUM_TEST_SEARCH_PARAMS = 5L;

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
// FIXME KHS test

	}

	@Test
	public void testAddOrUpdate1Patient() {
		ResourceVersionCache resourceVersionCache = new ResourceVersionCache();
		assertTrue(resourceVersionCache.keySet().isEmpty());
		Patient patientMale = createPatient(new BigDecimal(1), Enumerations.AdministrativeGender.MALE);
		resourceVersionCache.addOrUpdate(patientMale.getIdElement(), patientMale.getIdElement().getVersionIdPart());
		assertFalse(resourceVersionCache.keySet().isEmpty());
		assertEquals(1, resourceVersionCache.keySet().size());
		Map<IIdType, String> patientMap = resourceVersionCache.getMapForResourceName(RESOURCE_TYPE_PATIENT);
		assertEquals(1, patientMap.size());
		IIdType patientId = patientMap.keySet().iterator().next();
		assertEquals("1", patientId.getIdPart());
		String version = patientMap.get(patientId);
		assertEquals("1", version);
	}

	@Test
	public void testAddOrUpdate2DifferentResourceTypes() {
		// FIXME KHS Add Test Code
	}

	@Test
	public void testRemove() {
		// FIXME KHS Add Test Code
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
