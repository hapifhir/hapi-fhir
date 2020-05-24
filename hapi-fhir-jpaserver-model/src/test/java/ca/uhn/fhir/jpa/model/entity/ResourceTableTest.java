package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;

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


}
