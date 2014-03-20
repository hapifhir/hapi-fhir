package ca.uhn.fhir.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class JsonParserTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserTest.class);

	@Test
	public void testSimpleResourceEncode() throws IOException {

		FhirContext ctx = new FhirContext(Observation.class);
		// String name = "/observation-example-eeg.xml";
		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));
		Patient obs = ctx.newXmlParser().parseResource(Patient.class, xmlString);

		List<UndeclaredExtension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getUndeclaredExtensions();
		UndeclaredExtension undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());

		String encoded = ctx.newJsonParser().encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"));

		JSON expected = JSONSerializer.toJSON(jsonString);
		JSON actual = JSONSerializer.toJSON(encoded.trim());

		assertEquals(expected.toString(), actual.toString());

	}

	@Test
	public void testSimpleBundleEncode() throws IOException {

		FhirContext ctx = new FhirContext(Observation.class, Patient.class);
		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/atom-document-large.xml"), Charset.forName("UTF-8"));
		Bundle obs = ctx.newXmlParser().parseBundle(xmlString);

		String encoded = ctx.newJsonParser().encodeBundleToString(obs);
		ourLog.info(encoded);


	}
	
	
	@Test
	public void testEncodeContainedResources() throws IOException {
		
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/contained-diagnosticreport.xml"));
		FhirContext ctx = new FhirContext(DiagnosticReport.class);
		IParser p = ctx.newXmlParser();
		DiagnosticReport res = p.parseResource(DiagnosticReport.class, msg);
		
		String encoded = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);
		
	}
	
	public void testSimpleParse() throws DataFormatException, IOException {
		
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/example-patient-general.json"));
		FhirContext ctx = new FhirContext(Patient.class);
		IParser p = ctx.newJsonParser();
		Patient res = p.parseResource(Patient.class, msg);
		
		String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

	}


}
