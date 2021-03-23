package ca.uhn.fhir.mdm.rules.matcher;


import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtensionMatcherR4Test extends BaseMatcherR4Test {
	@Test
	public void testPatientWithMatchingExtension(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd",new StringType("Patient1"));
		patient2.addExtension("asd",new StringType("Patient1"));

		assertTrue(MdmMatcherEnum.EXTENSION_ANY_ORDER.match(ourFhirContext, patient1, patient2, false, null));
	}

	@Test
	public void testPatientWithoutMatchingExtension(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd",new StringType("Patient1"));
		patient2.addExtension("asd",new StringType("Patient2"));

		assertFalse(MdmMatcherEnum.EXTENSION_ANY_ORDER.match(ourFhirContext, patient1, patient2, false, null));
	}

	@Test
	public void testPatientSameValueDifferentUrl(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd",new StringType("Patient1"));
		patient2.addExtension("asd1",new StringType("Patient1"));

		assertFalse(MdmMatcherEnum.EXTENSION_ANY_ORDER.match(ourFhirContext, patient1, patient2, false, null));
	}

	@Test
	public void testPatientWithMultipleExtensionOneMatching(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd",new StringType("Patient1"));
		patient1.addExtension("url1", new StringType("asd"));
		patient2.addExtension("asd",new StringType("Patient1"));
		patient2.addExtension("asdasd", new StringType("some value"));

		assertTrue(MdmMatcherEnum.EXTENSION_ANY_ORDER.match(ourFhirContext, patient1, patient2, false, null));
	}

	@Test
	public void testPatientWithoutIntExtension(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		patient1.addExtension("asd", new IntegerType(123));
		patient2.addExtension("asd", new IntegerType(123));

		assertTrue(MdmMatcherEnum.EXTENSION_ANY_ORDER.match(ourFhirContext, patient1, patient2, false, null));
	}

	@Test
	public void testPatientWithNoExtension(){
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();

		assertFalse(MdmMatcherEnum.EXTENSION_ANY_ORDER.match(ourFhirContext, patient1, patient2, false, null));
	}

}
