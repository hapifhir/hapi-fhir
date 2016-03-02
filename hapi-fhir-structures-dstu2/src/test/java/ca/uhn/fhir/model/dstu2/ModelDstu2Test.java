package ca.uhn.fhir.model.dstu2;

import static org.junit.Assert.assertEquals;

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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPopulateWrongGenericType() {
		Patient p = new Patient();
		List names = Arrays.asList("name");
		p.setName(names);

		try {
			ourCtx.newXmlParser().encodeResourceToString(p);
		} catch (ClassCastException e) {
			assertEquals("Found instance of class java.lang.String - Did you set a field value to the incorrect type? Expected org.hl7.fhir.instance.model.api.IBase", e.getMessage());
		}
	}


}
