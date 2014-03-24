package ca.uhn.fhir.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.primitive.DecimalDt;

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

		ctx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

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
	
	@Test
	public void testSimpleParse() throws DataFormatException, IOException {
		
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/example-patient-general.json"));
		FhirContext ctx = new FhirContext(Patient.class);
		IParser p = ctx.newJsonParser();
//		ourLog.info("Reading in message: {}", msg);
		Patient res = p.parseResource(Patient.class, msg);
		
		assertEquals(2, res.getUndeclaredExtensions().size());
		assertEquals(1, res.getUndeclaredModifierExtensions().size());
		
		String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

	}	


	@Test
	public void testParseBundle() throws DataFormatException, IOException {
		
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/atom-document-large.json"));
		FhirContext ctx = new FhirContext(Patient.class);
		IParser p = ctx.newJsonParser();
		Bundle bundle = p.parseBundle(msg);
		
		String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeBundleToString(bundle);
		ourLog.info(encoded);

		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/_search?_format=application/json+fhir&search-id=46d5f0e7-9240-4d4f-9f51-f8ac975c65&search-sort=_id",bundle.getLinkSelf().getValue());
		assertEquals("urn:uuid:0b754ff9-03cf-4322-a119-15019af8a3",bundle.getBundleId().getValue());
		
		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/101",entry.getEntryId().getValue());
		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/101/_history/1", entry.getLinkSelf().getValue());
		assertEquals("2014-03-10T11:55:59Z", entry.getUpdated().getValueAsString());
		
		DiagnosticReport res = (DiagnosticReport)entry.getResource();
		assertEquals("Complete Blood Count", res.getName().getText().getValue());
		
	}	


	
	@Test
	public void testParseWithContained() throws DataFormatException, IOException {
		
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/diagnostic-report.json"));
		FhirContext ctx = new FhirContext(Patient.class);
		IParser p = ctx.newJsonParser();
//		ourLog.info("Reading in message: {}", msg);
		DiagnosticReport res = p.parseResource(DiagnosticReport.class, msg);
		
		String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

		ResourceReferenceDt reference = res.getResult().get(1);
		Observation obs = (Observation) reference.getResource();
		
		assertEquals("789-8", obs.getName().getCoding().get(0).getCode().getValue());
	}	

	
	@Test
	public void testEncodeContainedResourcesMore() throws IOException {

		DiagnosticReport rpt = new DiagnosticReport();
		Specimen spm = new Specimen();
		spm.getText().setDiv("AAA");
		rpt.addSpecimen().setResource(spm);
		
		IParser p = new FhirContext(DiagnosticReport.class).newJsonParser().setPrettyPrint(true);
		String str = p.encodeResourceToString(rpt);
		
		ourLog.info(str);
		assertThat(str, StringContains.containsString("<div>AAA</div>"));
		String substring = "\"resource\":\"#";
		assertThat(str, StringContains.containsString(substring));
		
		int idx = str.indexOf(substring) + substring.length();
		int idx2 = str.indexOf('"', idx+1);
		String id = str.substring(idx, idx2);
		assertThat(str, StringContains.containsString("\"id\":\"" + id + "\""));
		assertThat(str, IsNot.not(StringContains.containsString("<?xml version='1.0'?>")));
		
	}

	@Test
	public void testEncodeInvalidChildGoodException() throws IOException {
		Observation obs = new Observation();
		obs.setValue(new DecimalDt(112.22));
		
		IParser p = new FhirContext(Observation.class).newJsonParser();

		try {
			p.encodeResourceToString(obs);
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), StringContains.containsString("PeriodDt"));
		}
	}

}
