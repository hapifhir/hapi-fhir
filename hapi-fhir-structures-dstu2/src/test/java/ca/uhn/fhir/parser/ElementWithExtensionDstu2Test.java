package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sébastien Rivière 12/04/2017
 */
public class ElementWithExtensionDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ca.uhn.fhir.parser.ElementWithExtensionDstu2Test.class);
	private static final FhirContext ctx = FhirContext.forDstu2();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testExtensionOnPrimitiveExtensionJson() throws Exception {
		MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setId("1");
		patient.getPetName().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
		final String json = parser.encodeResourceToString(patient);

		ourLog.info(json);

		patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
		assertEquals(1, patient.getPetName().getUndeclaredExtensions().size());
	}

	@Test
	public void testExtensionOnPrimitiveExtensionXml() throws Exception {
		MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setId("1");
		patient.getPetName().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		final String xml = parser.encodeResourceToString(patient);

		ourLog.info(xml);

		patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
		assertEquals(1, patient.getPetName().getUndeclaredExtensions().size());
	}

	@Test
	public void testExtensionOnIDDatatypeJson() throws Exception {
		MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setId("1");
		patient.getId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
		final String json = parser.encodeResourceToString(patient);

		ourLog.info(json);

		patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
		assertEquals(1, patient.getId().getUndeclaredExtensions().size());
	}

	@Test
	public void testExtensionOnIDDatatypeXml() throws Exception {
		MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setId("1");
		patient.getId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		final String xml = parser.encodeResourceToString(patient);

		ourLog.info(xml);

		patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
		assertEquals(1, patient.getId().getUndeclaredExtensions().size());
	}

	@Test
	public void testExtensionOnIDDatatypeExtensionJson() throws Exception {
		MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setId("1");
		patient.getCustomId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
		final String json = parser.encodeResourceToString(patient);

		ourLog.info(json);

		patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
		assertEquals(1, patient.getCustomId().getUndeclaredExtensions().size());
	}

	@Test
	public void testExtensionOnIDDatatypeExtensionXml() throws Exception {
		MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setId("1");
		patient.getCustomId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		final String xml = parser.encodeResourceToString(patient);

		ourLog.info(xml);

		patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
		assertEquals(1, patient.getCustomId().getUndeclaredExtensions().size());
	}
}

