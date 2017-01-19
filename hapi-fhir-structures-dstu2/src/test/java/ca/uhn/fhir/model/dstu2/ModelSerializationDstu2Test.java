package ca.uhn.fhir.model.dstu2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;

public class ModelSerializationDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	/**
	 * Verify that MaritalStatusCodeEnum (and, by extension, BoundCodeableConcepts in general) are serializable. Author: Nick Peterson (nrpeterson@gmail.com)
	 */
	@Test
	public void testBoundCodeableConceptSerialization() {
		MaritalStatusCodesEnum maritalStatus = MaritalStatusCodesEnum.M;
		byte[] bytes = SerializationUtils.serialize(maritalStatus);
		assertTrue(bytes.length > 0);

		MaritalStatusCodesEnum deserialized = SerializationUtils.deserialize(bytes);
		assertEquals(maritalStatus.getCode(), deserialized.getCode());
		assertEquals(maritalStatus.getSystem(), deserialized.getSystem());
	}

	@Test
	public void testBoundCodeSerialization() {
		Patient p = new Patient();
		p.setGender(AdministrativeGenderEnum.MALE);
		IdentifierDt identifier = p.addIdentifier();
		identifier.setType(IdentifierTypeCodesEnum.DL);

		Patient out = testIsSerializable(p);

		/*
		 * Make sure the binder still works for Code
		 */
		assertEquals(AdministrativeGenderEnum.MALE, out.getGenderElement().getValueAsEnum());
		out.getGenderElement().setValue("female");
		assertEquals(AdministrativeGenderEnum.FEMALE, out.getGenderElement().getValueAsEnum());

		assertEquals(IdentifierTypeCodesEnum.DL, out.getIdentifier().get(0).getType().getValueAsEnum().iterator().next());
		out.getIdentifier().get(0).getType().setValueAsEnum(IdentifierTypeCodesEnum.MR);
		assertEquals("MR", out.getIdentifier().get(0).getType().getCoding().get(0).getCode());
		assertEquals("http://hl7.org/fhir/v2/0203", out.getIdentifier().get(0).getType().getCoding().get(0).getSystem());
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> T testIsSerializable(T theObject) {
		byte[] bytes = SerializationUtils.serialize(theObject);
		assertTrue(bytes.length > 0);

		IBaseResource obj = SerializationUtils.deserialize(bytes);
		assertTrue(obj != null);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		assertEquals(p.encodeResourceToString(theObject), p.encodeResourceToString(obj));

		return (T) obj;
	}

	@Test
	public void testSerialization() throws Exception {
		String input = IOUtils.toString(ModelSerializationDstu2Test.class.getResourceAsStream("/diagnosticreport-examples-lab-text(72ac8493-52ac-41bd-8d5d-7258c289b5ea).xml"));

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		testIsSerializable(parsed);
	}

	/**
	 * Contributed by Travis from iSalus
	 */
	@Test
	public void testSerialization2() {
		Patient patient = new Patient().addName(new HumanNameDt().addGiven("George").addFamily("Washington")).addName(new HumanNameDt().addGiven("George2").addFamily("Washington2"))
				.addAddress(new AddressDt().addLine("line 1").addLine("line 2").setCity("city").setState("UT"))
				.addAddress(new AddressDt().addLine("line 1b").addLine("line 2b").setCity("cityb").setState("UT")).setBirthDate(new Date(), TemporalPrecisionEnum.DAY);

		testIsSerializable(patient);
	}

}
