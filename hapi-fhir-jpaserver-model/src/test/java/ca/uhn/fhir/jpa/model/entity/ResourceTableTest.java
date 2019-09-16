package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.context.FhirContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTableTest {

	@Test
	public void testResourceLength() {
		for (String nextName : FhirContext.forR4().getResourceNames()) {
			if (nextName.length() > ResourceTable.RESTYPE_LEN) {
				fail("Name " + nextName + " length of " + nextName.length() + " is > " + ResourceTable.RESTYPE_LEN);
			}
		}
	}


}
