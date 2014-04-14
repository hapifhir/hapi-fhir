package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
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
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;

public class JsonParserTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserTest.class);

	/**
	 * This sample has extra elements in <searchParam> that are not actually a
	 * part of the spec any more..
	 */
	@Test
	public void testParseFuroreMetadataWithExtraElements() throws IOException {
		String msg = IOUtils.toString(JsonParserTest.class.getResourceAsStream("/furore-conformance.json"));

		IParser p = new FhirContext(ValueSet.class).newJsonParser();
		Conformance conf = p.parseResource(Conformance.class, msg);
		RestResource res = conf.getRestFirstRep().getResourceFirstRep();
		assertEquals("_id", res.getSearchParam().get(1).getName().getValue());
	}

	@Test
	public void testEncodeResourceRef() throws DataFormatException, IOException {

		Patient patient = new Patient();
		patient.setManagingOrganization(new ResourceReferenceDt());

		IParser p = new FhirContext().newJsonParser();
		String str = p.encodeResourceToString(patient);
		assertThat(str, IsNot.not(StringContains.containsString("managingOrganization")));

		patient.setManagingOrganization(new ResourceReferenceDt(Organization.class, "123"));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("\"managingOrganization\":{\"resource\":\"Organization/123\"}"));

		Organization org = new Organization();
		org.addIdentifier().setSystem("foo").setValue("bar");
		patient.setManagingOrganization(new ResourceReferenceDt(org));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("\"contained\":[{\"resourceType\":\"Organization\""));

	}

	@Test
	public void testNarrativeGeneration() throws DataFormatException, IOException {

		Patient patient = new Patient();
		patient.addName().addFamily("Smith");
		Organization org = new Organization();
		patient.getManagingOrganization().setResource(org);

		INarrativeGenerator gen = mock(INarrativeGenerator.class);
		XhtmlDt xhtmlDt = new XhtmlDt("<div>help</div>");
		NarrativeDt nar = new NarrativeDt(xhtmlDt, NarrativeStatusEnum.GENERATED);
		when(gen.generateNarrative(eq("http://hl7.org/fhir/profiles/Patient"), eq(patient))).thenReturn(nar);

		FhirContext context = new FhirContext();
		context.setNarrativeGenerator(gen);
		IParser p = context.newJsonParser();
		p.encodeResourceToWriter(patient, new OutputStreamWriter(System.out));
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		assertThat(str, StringContains.containsString(",\"text\":{\"status\":\"generated\",\"div\":\"<div>help</div>\"},"));
	}

	@Test
	public void testSimpleResourceEncode() throws IOException {

		FhirContext ctx = new FhirContext(Observation.class);
		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));
		Patient obs = ctx.newXmlParser().parseResource(Patient.class, xmlString);

		List<ExtensionDt> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getUndeclaredExtensions();
		ExtensionDt undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl().getValue());

		ctx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

		IParser jsonParser = ctx.newJsonParser();
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"));

		JSON expected = JSONSerializer.toJSON(jsonString);
		JSON actual = JSONSerializer.toJSON(encoded.trim());

		ourLog.info("Expected: {}", expected);
		ourLog.info("Actual  : {}", actual);
		assertEquals(expected.toString(), actual.toString());

	}

	
	
	
	@Test
	public void testSimpleResourceEncodeWithCustomType() throws IOException {

		FhirContext ctx = new FhirContext(MyObservationWithExtensions.class);
		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));
		MyObservationWithExtensions obs = ctx.newXmlParser().parseResource(MyObservationWithExtensions.class, xmlString);

		assertEquals(0, obs.getAllUndeclaredExtensions().size());
		assertEquals("aaaa", obs.getExtAtt().getContentType().getValue());
		assertEquals("str1", obs.getMoreExt().getStr1().getValue());
		assertEquals("2011-01-02", obs.getModExt().getValueAsString());
		
		List<ExtensionDt> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getUndeclaredExtensions();
		ExtensionDt undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl().getValue());

		ctx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

		IParser jsonParser = ctx.newJsonParser();
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"));

		JSON expected = JSONSerializer.toJSON(jsonString);
		JSON actual = JSONSerializer.toJSON(encoded.trim());

		ourLog.info("Expected: {}", expected);
		ourLog.info("Actual  : {}", actual);
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
		// ourLog.info("Reading in message: {}", msg);
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

		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/_search?_format=application/json+fhir&search-id=46d5f0e7-9240-4d4f-9f51-f8ac975c65&search-sort=_id", bundle.getLinkSelf().getValue());
		assertEquals("urn:uuid:0b754ff9-03cf-4322-a119-15019af8a3", bundle.getBundleId().getValue());

		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/101", entry.getId().getValue());
		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/101/_history/1", entry.getLinkSelf().getValue());
		assertEquals("2014-03-10T11:55:59Z", entry.getUpdated().getValueAsString());

		DiagnosticReport res = (DiagnosticReport) entry.getResource();
		assertEquals("Complete Blood Count", res.getName().getText().getValue());

	}

	@Test
	public void testParseWithContained() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/diagnostic-report.json"));
		FhirContext ctx = new FhirContext(Patient.class);
		IParser p = ctx.newJsonParser();
		// ourLog.info("Reading in message: {}", msg);
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
		int idx2 = str.indexOf('"', idx + 1);
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

	@Test
	public void testExtensionOnComposite() throws Exception {

		Patient patient = new Patient();

		HumanNameDt name = patient.addName();
		name.addFamily().setValue("Shmoe");
		HumanNameDt given = name.addGiven("Joe");
		ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#givenext", new StringDt("Hello"));
		given.addUndeclaredExtension(ext2);
		String enc = new FhirContext().newJsonParser().encodeResourceToString(patient);
		ourLog.info(enc);
		//@formatter:off
		assertThat(enc, containsString(("{" + 
				"    \"resourceType\":\"Patient\"," + 
				"    \"name\":[" + 
				"        {" + 
				"            \"family\":[" + 
				"                \"Shmoe\"" + 
				"            ]," + 
				"            \"given\":[" + 
				"                \"Joe\"" + 
				"            ]" + 
				"        }" + 
				"    ]," + 
				"    \"_name\":[" + 
				"        {" + 
				"            \"extension\":[" + 
				"                {" + 
				"                    \"url\":\"http://examples.com#givenext\"," + 
				"                    \"valueString\":\"Hello\"" + 
				"                }" + 
				"            ]" + 
				"        }" + 
				"    ]" + 
				"}").replaceAll(" +", "")));
		//@formatter:on

		IParser newJsonParser = new FhirContext().newJsonParser();
		StringReader reader = new StringReader(enc);
		Patient parsed = newJsonParser.parseResource(Patient.class, reader);
		
		ourLog.info(new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed));
		
		assertEquals(1, parsed.getNameFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").size());
		ExtensionDt ext = parsed.getNameFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").get(0);
		assertEquals("Hello", ext.getValueAsPrimitive().getValue());
		
	}

	@Test
	public void testExtensionOnPrimitive() throws Exception {

		Patient patient = new Patient();

		HumanNameDt name = patient.addName();
		StringDt family = name.addFamily();
		family.setValue("Shmoe");

		ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#givenext", new StringDt("Hello"));
		family.addUndeclaredExtension(ext2);
		String enc = new FhirContext().newJsonParser().encodeResourceToString(patient);
		ourLog.info(enc);
		//@formatter:off
		assertThat(enc, containsString(("{\n" + 
				"    \"resourceType\":\"Patient\",\n" + 
				"    \"name\":[\n" + 
				"        {\n" + 
				"            \"family\":[\n" + 
				"                \"Shmoe\"\n" + 
				"            ],\n" + 
				"            \"_family\":[\n" + 
				"                {\n" + 
				"                    \"extension\":[\n" + 
				"                        {\n" + 
				"                            \"url\":\"http://examples.com#givenext\",\n" + 
				"                            \"valueString\":\"Hello\"\n" + 
				"                        }\n" + 
				"                    ]\n" + 
				"                }\n" + 
				"            ]\n" + 
				"        }\n" + 
				"    ]\n" + 
				"}").replace("\n", "").replaceAll(" +", "")));
		//@formatter:on

		Patient parsed = new FhirContext().newJsonParser().parseResource(Patient.class, new StringReader(enc));
		assertEquals(1, parsed.getNameFirstRep().getFamilyFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").size());
		ExtensionDt ext = parsed.getNameFirstRep().getFamilyFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").get(0);
		assertEquals("Hello", ext.getValueAsPrimitive().getValue());

	}

}
