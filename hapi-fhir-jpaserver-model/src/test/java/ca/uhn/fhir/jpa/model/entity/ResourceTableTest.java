package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceTableTest {

	@Test
	public void testResourceLength() {
		for (String nextName : FhirContext.forR4().getResourceTypes()) {
			if (nextName.length() > ResourceTable.RESTYPE_LEN) {
				fail("Name " + nextName + " length of " + nextName.length() + " is > " + ResourceTable.RESTYPE_LEN);
			}
		}
	}

	@ParameterizedTest
	@CsvSource(value={
		"123, null, Patient/123/_history/1",
		"123, 123, Patient/123/_history/1",
		"123, 456, Patient/456/_history/1"
	},nullValues={"null"})
	public void testPopulateId(Long theResId, String theFhirId, String theExpected) {
		// Given
		ResourceTable t = new ResourceTable();
		t.setId(theResId);
		t.setFhirId(theFhirId);
		t.setResourceType(new Patient().getResourceType().name());
		t.setVersionForUnitTest(1);

		// When
		IdDt actual = t.getIdDt();

		// Then
		assertEquals(theExpected, actual.getValueAsString());
	}
}
