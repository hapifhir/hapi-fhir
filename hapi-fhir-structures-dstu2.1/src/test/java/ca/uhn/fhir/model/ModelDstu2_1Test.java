package ca.uhn.fhir.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu2016may.model.Appointment;
import org.hl7.fhir.dstu2016may.model.CodeableConcept;
import org.hl7.fhir.dstu2016may.model.Element;
import org.hl7.fhir.dstu2016may.model.Enumerations;
import org.hl7.fhir.dstu2016may.model.HumanName;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.dstu2016may.model.Practitioner;
import org.hl7.fhir.dstu2016may.model.Practitioner.PractitionerPractitionerRoleComponent;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.TestUtil;

public class ModelDstu2_1Test {

	private static FhirContext ourCtx = FhirContext.forDstu2_1();

	@Test
	public void testElementHasInterface() {
		assertTrue(IBaseElement.class.isAssignableFrom(Element.class));
	}
	
	
	/**
	 * See #320
	 */
	@Test
	public void testDontUseBoundCodeForExampleBinding() {
		Practitioner p = new Practitioner();
		PractitionerPractitionerRoleComponent role = p.addPractitionerRole();
		CodeableConcept roleField = role.getRole();
		assertEquals(CodeableConcept.class, roleField.getClass());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	/**
	 * See #304
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPopulateWrongGenericType() {
		Patient p = new Patient();
		List names = Arrays.asList("name");

		List existingNames = p.getName();
		existingNames.addAll(names);

		try {
			ourCtx.newXmlParser().encodeResourceToString(p);
		} catch (ClassCastException e) {
			assertEquals("Found instance of class java.lang.String - Did you set a field value to the incorrect type? Expected org.hl7.fhir.instance.model.api.IBase", e.getMessage());
		}
	}
	
	
//	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelDstu2_1Test.class);

	
	/**
	 * See #325
	 */
	@Test
	@Ignore
	public void testEqualsDeep() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = dateFormat.parse("19920925");
		FhirContext context = FhirContext.forDstu2_1();

		Patient patient1 = new Patient();
		patient1.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").addFamily("family");

		Patient patient2 = context.newJsonParser().parseResource(Patient.class,
		        context.newJsonParser().encodeResourceToString(patient1));

		assertTrue(patient1.equalsDeep(patient2));
		assertTrue(patient1.equalsShallow(patient2));

		Patient patient3 = new Patient();
		patient3.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").addFamily("family");

		assertTrue(patient1.equalsDeep(patient3));
		assertTrue(patient1.equalsShallow(patient3));

		Patient patient4 = new Patient();
		patient4.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").addFamily("family2");

		assertTrue(patient1.equalsShallow(patient4));
		assertFalse(patient1.equalsDeep(patient4));

		Patient patient5 = new Patient();
		patient5.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.FEMALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").addFamily("family2");

		assertFalse(patient1.equalsShallow(patient5));
		assertFalse(patient1.equalsDeep(patient5));

	}

}
