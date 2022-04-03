package ca.uhn.fhir.model;

import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Element;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Practitioner.PractitionerQualificationComponent;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ModelDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();

	@Test
	public void testElementHasInterface() {
		assertTrue(IBaseElement.class.isAssignableFrom(Element.class));
	}
	
	/**
	 * See #354
	 */
	@Test
	public void testSetters() {
		Claim claim = new Claim();
		claim.setIdentifier(new ArrayList<>()).setCareTeam(new ArrayList<>());
	}

	@Test
	public void testbase64BinaryName() {
		assertEquals("base64Binary", ourCtx.getElementDefinition("base64binary").getName());
		assertEquals("base64Binary", ourCtx.getElementDefinition("base64Binary").getName());
	}

	@Test
	public void testModelBindings() {
		FhirTerser t = ourCtx.newTerser();
		RuntimeResourceDefinition def = ourCtx.getResourceDefinition(Patient.class);
		assertEquals("http://hl7.org/fhir/ValueSet/administrative-gender", ((BaseRuntimeDeclaredChildDefinition)def.getChildByName("gender")).getBindingValueSet());
		assertEquals("http://hl7.org/fhir/ValueSet/link-type", ((BaseRuntimeDeclaredChildDefinition)t.getDefinition(Patient.class, "Patient.link.type")).getBindingValueSet());

		def = ourCtx.getResourceDefinition(Appointment.class);
		assertEquals("http://hl7.org/fhir/ValueSet/appointmentstatus", ((BaseRuntimeDeclaredChildDefinition)def.getChildByName("status")).getBindingValueSet());
	}

	/**
	 * See #320
	 */
	@Test
	public void testDontUseBoundCodeForExampleBinding() {
		Practitioner p = new Practitioner();
		PractitionerQualificationComponent qualification = p.addQualification();
		CodeableConcept roleField = qualification.getCode();
		assertEquals(CodeableConcept.class, roleField.getClass());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
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
			assertEquals(Msg.code(1748) + "Found instance of class java.lang.String - Did you set a field value to the incorrect type? Expected org.hl7.fhir.instance.model.api.IBase", e.getMessage());
		}
	}
	
	
//	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelDstu3Test.class);

	
	/**
	 * See #325
	 */
	@Test
	public void testEqualsDeep() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = dateFormat.parse("19920925");
		FhirContext context = FhirContext.forDstu3();

		Patient patient1 = new Patient();
		patient1.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").setFamily("family");

		Patient patient2 = context.newJsonParser().parseResource(Patient.class,
		        context.newJsonParser().encodeResourceToString(patient1));

		assertTrue(patient1.equalsDeep(patient2));
		assertTrue(patient1.equalsShallow(patient2));

		Patient patient3 = new Patient();
		patient3.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").setFamily("family");

		assertTrue(patient1.equalsDeep(patient3));
		assertTrue(patient1.equalsShallow(patient3));

		Patient patient4 = new Patient();
		patient4.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.MALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").setFamily("family2");

		assertTrue(patient1.equalsShallow(patient4));
		assertFalse(patient1.equalsDeep(patient4));

		Patient patient5 = new Patient();
		patient5.setBirthDate(date)
		        .setGender(Enumerations.AdministrativeGender.FEMALE)
		        .addName().setUse(HumanName.NameUse.OFFICIAL).addGiven("first").addGiven("second").setFamily("family2");

		assertFalse(patient1.equalsShallow(patient5));
		assertFalse(patient1.equalsDeep(patient5));

	}

	@Test
	public void testInstantPrecision() {
		new InstantType("2019-01-01T00:00:00Z");
		new InstantType("2019-01-01T00:00:00.0Z");
		new InstantType("2019-01-01T00:00:00.000Z");
		try {
			new InstantType("2019-01-01T00:00Z");
			fail();
		} catch (DataFormatException e) {
			// good
		}
	}


}
