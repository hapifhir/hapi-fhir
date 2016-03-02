package ca.uhn.fhir.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;

public class ModelDstu3Test {

	/**
	 * See #304
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testPopulateWrongGenericType() {
		Patient p = new Patient();
		List names = Arrays.asList("name");

		p.setName(null);
		try {
			p.setName(names);
			fail();
		} catch (ClassCastException e) {
			assertEquals("Failed to set invalid value, found element in list of type String but expected ca.uhn.fhir.model.dstu2.composite.HumanNameDt", e.getMessage());
		}
	}


}
