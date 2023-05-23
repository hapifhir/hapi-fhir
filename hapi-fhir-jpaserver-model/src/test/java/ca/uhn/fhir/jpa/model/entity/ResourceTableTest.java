package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.measure.quantity.Force;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
		"123, 123, Patient/123/_history/1",
		", 123, Patient/123/_history/1",
		"null, 456, Patient/456/_history/1"
	},nullValues={"null"})
	public void testPopulateId(String theFhirId, String theForcedId, String theExpected) {
		// Given
		ResourceTable t = new ResourceTable();
		t.setFhirId(theFhirId);
		ForcedId forcedId = new ForcedId();
		forcedId.setForcedId(theForcedId);
		t.setForcedId(forcedId);
		t.setResourceType(new Patient().getResourceType().name());
		t.setVersionForUnitTest(1);

		// When
		IdDt actual = t.getIdDt();

		// Then
		assertTrue(actual.equals(theExpected));
	}
}
