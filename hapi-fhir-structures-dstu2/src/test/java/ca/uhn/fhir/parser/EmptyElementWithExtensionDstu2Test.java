package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Bill de Beaubien on 12/20/2015.
 */
public class EmptyElementWithExtensionDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(EmptyElementWithExtensionDstu2Test.class);
	private static FhirContext ctx = FhirContext.forDstu2();

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testNullFlavorCompositeJson() throws Exception {
		Observation observation = new Observation();
		observation.getCode().addCoding().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		IParser parser = ctx.newJsonParser().setPrettyPrint(true);
		String json = parser.encodeResourceToString(observation);

		ourLog.info(json);

		observation = (Observation) parser.parseResource(json);
		assertEquals(1, observation.getCode().getCoding().get(0).getUndeclaredExtensions().size());
	}

	@Test
	public void testNullFlavorCompositeXml() throws Exception {
		Observation observation = new Observation();
		observation.getCode().addCoding().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeResourceToString(observation);

		ourLog.info(xml);

		observation = (Observation) parser.parseResource(xml);
		assertEquals(1, observation.getCode().getCoding().get(0).getUndeclaredExtensions().size());
	}

	@Test
	public void testNullFlavorPrimitiveJson() throws Exception {
		Observation observation = new Observation();
		observation.getCode().getCoding().add(new CodingDt().setSystem("http://loinc.org").setCode("3141-9"));
		observation.getStatusElement().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		IParser parser = ctx.newJsonParser().setPrettyPrint(true);
		String json = parser.encodeResourceToString(observation);

		ourLog.info(json);

		observation = (Observation) parser.parseResource(json);
		assertEquals(1, observation.getStatusElement().getUndeclaredExtensions().size());
	}

	@Test
	public void testNullFlavorPrimitiveXml() throws Exception {
		Observation observation = new Observation();
		observation.getCode().getCoding().add(new CodingDt().setSystem("http://loinc.org").setCode("3141-9"));
		observation.getStatusElement().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
		IParser parser = ctx.newXmlParser().setPrettyPrint(true);
		String xml = parser.encodeResourceToString(observation);

		ourLog.info(xml);

		observation = (Observation) parser.parseResource(xml);
		assertEquals(1, observation.getStatusElement().getUndeclaredExtensions().size());
	}

}
