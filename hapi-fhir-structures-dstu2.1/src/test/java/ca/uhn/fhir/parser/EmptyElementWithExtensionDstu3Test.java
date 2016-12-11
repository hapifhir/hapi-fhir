package ca.uhn.fhir.parser;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.dstu2016may.model.Coding;
import org.hl7.fhir.dstu2016may.model.Extension;
import org.hl7.fhir.dstu2016may.model.Observation;
import org.hl7.fhir.dstu2016may.model.StringType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by Bill de Beaubien on 12/20/2015.
 */
public class EmptyElementWithExtensionDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(EmptyElementWithExtensionDstu3Test.class);
	private static FhirContext ctx = FhirContext.forDstu2_1();


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testNullFlavorCompositeJson() throws Exception {
		Observation observation = new Observation();
		observation.getCode().addCoding().addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK")));
		IParser parser = ctx.newJsonParser().setPrettyPrint(true);
		String json = parser.encodeResourceToString(observation);

		ourLog.info(json);

		observation = (Observation) parser.parseResource(json);
		assertEquals(1, observation.getCode().getCoding().get(0).getExtension().size());
	}

	@Test
	public void testNullFlavorCompositeXml() throws Exception {
		Observation observation = new Observation();
		observation.getCode().addCoding().addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK")));
		IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeResourceToString(observation);

		ourLog.info(xml);

		observation = (Observation) parser.parseResource(xml);
		assertEquals(1, observation.getCode().getCoding().get(0).getExtension().size());
	}

	@Test
	public void testNullFlavorPrimitiveJson() throws Exception {
		Observation observation = new Observation();
		observation.getCode().getCoding().add(new Coding().setSystem("http://loinc.org").setCode("3141-9"));
		observation.getStatusElement().addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK")));
		IParser parser = ctx.newJsonParser().setPrettyPrint(true);
		String json = parser.encodeResourceToString(observation);

		ourLog.info(json);

		observation = (Observation) parser.parseResource(json);
		assertEquals(1, observation.getStatusElement().getExtension().size());
	}

	@Test
	public void testNullFlavorPrimitiveXml() throws Exception {
		Observation observation = new Observation();
		observation.getCode().getCoding().add(new Coding().setSystem("http://loinc.org").setCode("3141-9"));
		observation.getStatusElement().addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK")));
		IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeResourceToString(observation);

		ourLog.info(xml);

		observation = (Observation) parser.parseResource(xml);
		assertEquals(1, observation.getStatusElement().getExtension().size());
	}

}
