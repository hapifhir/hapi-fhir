package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu2016may.model.Coding;
import org.hl7.fhir.dstu2016may.model.Extension;
import org.hl7.fhir.dstu2016may.model.Observation;
import org.hl7.fhir.dstu2016may.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Bill de Beaubien on 12/20/2015.
 */
public class EmptyElementWithExtensionDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(EmptyElementWithExtensionDstu3Test.class);
	private static FhirContext ctx = FhirContext.forDstu2_1();


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testNullFlavorCompositeJson() {
		Observation observation = new Observation();
		observation.getCode().addCoding().addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK")));
		IParser parser = ctx.newJsonParser().setPrettyPrint(true);
		String json = parser.encodeResourceToString(observation);

		ourLog.info(json);

		observation = (Observation) parser.parseResource(json);
		assertEquals(1, observation.getCode().getCoding().get(0).getExtension().size());
	}

	@Test
	public void testNullFlavorCompositeXml() {
		Observation observation = new Observation();
		observation.getCode().addCoding().addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK")));
		IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeResourceToString(observation);

		ourLog.info(xml);

		observation = (Observation) parser.parseResource(xml);
		assertEquals(1, observation.getCode().getCoding().get(0).getExtension().size());
	}

	@Test
	public void testNullFlavorPrimitiveJson() {
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
	public void testNullFlavorPrimitiveXml() {
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
