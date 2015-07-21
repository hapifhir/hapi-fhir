package ca.uhn.fhir.model.dstu2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.IParser;

public class ModelSerializationTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2();

	@Test
	public void testSerialization() throws Exception {
		String input = IOUtils.toString(ModelSerializationTest.class.getResourceAsStream("/diagnosticreport-examples-lab-text(72ac8493-52ac-41bd-8d5d-7258c289b5ea).xml"));

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

	private void testIsSerializable(IBaseResource theObject) {
		byte[] bytes = SerializationUtils.serialize(theObject);
		assertTrue(bytes.length > 0);

		IBaseResource obj = SerializationUtils.deserialize(bytes);
		assertTrue(obj != null);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		assertEquals(p.encodeResourceToString(theObject), p.encodeResourceToString(obj));

	}

}
