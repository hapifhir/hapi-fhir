package ca.uhn.fhir.model.dstu2;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelSerializationDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
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
		assertThat(deserialized.getCode()).isEqualTo(maritalStatus.getCode());
		assertThat(deserialized.getSystem()).isEqualTo(maritalStatus.getSystem());
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
		assertThat(out.getGenderElement().getValueAsEnum()).isEqualTo(AdministrativeGenderEnum.MALE);
		out.getGenderElement().setValue("female");
		assertThat(out.getGenderElement().getValueAsEnum()).isEqualTo(AdministrativeGenderEnum.FEMALE);

		assertThat(out.getIdentifier().get(0).getType().getValueAsEnum().iterator().next()).isEqualTo(IdentifierTypeCodesEnum.DL);
		out.getIdentifier().get(0).getType().setValueAsEnum(IdentifierTypeCodesEnum.MR);
		assertThat(out.getIdentifier().get(0).getType().getCoding().get(0).getCode()).isEqualTo("MR");
		assertThat(out.getIdentifier().get(0).getType().getCoding().get(0).getSystem()).isEqualTo("http://hl7.org/fhir/v2/0203");
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> T testIsSerializable(T theObject) {
		byte[] bytes = SerializationUtils.serialize(theObject);
		assertTrue(bytes.length > 0);

		IBaseResource obj = SerializationUtils.deserialize(bytes);
		assertTrue(obj != null);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		assertThat(p.encodeResourceToString(obj)).isEqualTo(p.encodeResourceToString(theObject));

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
