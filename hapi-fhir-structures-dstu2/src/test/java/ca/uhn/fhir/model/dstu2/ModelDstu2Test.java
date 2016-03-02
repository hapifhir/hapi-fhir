package ca.uhn.fhir.model.dstu2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class ModelDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	
	/**
	 * See #304
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testPopulateWrongGenericType() {
		Patient p = new Patient();
		List names = Arrays.asList("name");

		p.setName(null);

		ourCtx.newXmlParser().encodeResourceToString(p);
		
		try {
			p.setName(names);
			fail();
		} catch (ClassCastException e) {
			assertEquals("Failed to set invalid value, found element in list of type String but expected ca.uhn.fhir.model.dstu2.composite.HumanNameDt", e.getMessage());
		}
	}


}
