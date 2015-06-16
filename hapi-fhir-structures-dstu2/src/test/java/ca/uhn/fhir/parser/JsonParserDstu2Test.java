package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.QuestionnaireAnswers;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationReliabilityEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;

public class JsonParserDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserDstu2Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu2();

	@Test
	public void testOmitResourceId() {
		Patient p = new Patient();
		p.setId("123");
		p.addName().addFamily("ABC");
		
		assertThat(ourCtx.newJsonParser().encodeResourceToString(p), stringContainsInOrder("123", "ABC"));
		assertThat(ourCtx.newJsonParser().setOmitResourceId(true).encodeResourceToString(p), containsString("ABC"));
		assertThat(ourCtx.newJsonParser().setOmitResourceId(true).encodeResourceToString(p), not(containsString("123")));
	}

	@Test
	public void testParseAndEncodeBundleWithUuidBase() {
		//@formatter:off
		String input = 
				"{\n" + 
				"    \"resourceType\":\"Bundle\",\n" + 
				"    \"type\":\"document\",\n" + 
				"    \"entry\":[\n" + 
				"        {\n" + 
				"            \"base\":\"urn:uuid:\",\n" + 
				"            \"resource\":{\n" + 
				"                \"resourceType\":\"Composition\",\n" + 
				"                \"id\":\"180f219f-97a8-486d-99d9-ed631fe4fc57\",\n" + 
				"                \"meta\":{\n" + 
				"                    \"lastUpdated\":\"2013-05-28T22:12:21Z\"\n" + 
				"                },\n" + 
				"                \"text\":{\n" + 
				"                    \"status\":\"generated\",\n" + 
				"                    \"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: 180f219f-97a8-486d-99d9-ed631fe4fc57</p><p><b>meta</b>: </p><p><b>date</b>: Feb 1, 2013 12:30:02 PM</p><p><b>type</b>: Discharge Summary from Responsible Clinician <span>(Details : {LOINC code '28655-9' = 'Physician attending Discharge summary)</span></p><p><b>status</b>: final</p><p><b>confidentiality</b>: N</p><p><b>author</b>: <a>Doctor Dave. Generated Summary: 23; Adam Careful </a></p><p><b>encounter</b>: <a>http://fhir.healthintersections.com.au/open/Encounter/doc-example</a></p></div>\"\n" + 
				"                },\n" + 
				"                \"date\":\"2013-02-01T12:30:02Z\",\n" + 
				"                \"type\":{\n" + 
				"                    \"coding\":[\n" + 
				"                        {\n" + 
				"                            \"system\":\"http://loinc.org\",\n" + 
				"                            \"code\":\"28655-9\"\n" + 
				"                        }\n" + 
				"                    ],\n" + 
				"                    \"text\":\"Discharge Summary from Responsible Clinician\"\n" + 
				"                },\n" + 
				"                \"status\":\"final\",\n" + 
				"                \"confidentiality\":\"N\",\n" + 
				"                \"subject\":{\n" + 
				"                    \"reference\":\"http://fhir.healthintersections.com.au/open/Patient/d1\",\n" + 
				"                    \"display\":\"Eve Everywoman\"\n" + 
				"                },\n" + 
				"                \"author\":[\n" + 
				"                    {\n" + 
				"                        \"reference\":\"Practitioner/example\",\n" + 
				"                        \"display\":\"Doctor Dave\"\n" + 
				"                    }\n" + 
				"                ],\n" + 
				"                \"encounter\":{\n" + 
				"                    \"reference\":\"http://fhir.healthintersections.com.au/open/Encounter/doc-example\"\n" + 
				"                },\n" + 
				"                \"section\":[\n" + 
				"                    {\n" + 
				"                        \"title\":\"Reason for admission\",\n" + 
				"                        \"content\":{\n" + 
				"                            \"reference\":\"urn:uuid:d0dd51d3-3ab2-4c84-b697-a630c3e40e7a\"\n" + 
				"                        }\n" + 
				"                    },\n" + 
				"                    {\n" + 
				"                        \"title\":\"Medications on Discharge\",\n" + 
				"                        \"content\":{\n" + 
				"                            \"reference\":\"urn:uuid:673f8db5-0ffd-4395-9657-6da00420bbc1\"\n" + 
				"                        }\n" + 
				"                    },\n" + 
				"                    {\n" + 
				"                        \"title\":\"Known allergies\",\n" + 
				"                        \"content\":{\n" + 
				"                            \"reference\":\"urn:uuid:68f86194-e6e1-4f65-b64a-5314256f8d7b\"\n" + 
				"                        }\n" + 
				"                    }\n" + 
				"                ]\n" + 
				"            }\n" + 
				"        }" +
				"    ]" +
				"}";
		//@formatter:on
		
		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);
		
		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", parsed.getEntry().get(0).getResource().getId().getValue());
		assertEquals("urn:uuid:", parsed.getEntry().get(0).getResource().getId().getBaseUrl());
		assertEquals("180f219f-97a8-486d-99d9-ed631fe4fc57", parsed.getEntry().get(0).getResource().getId().getIdPart());
		assertThat(encoded, containsString("\"id\":\"180f219f-97a8-486d-99d9-ed631fe4fc57\""));
	}

	
	@Test
	public void testParseBundleWithBinary() {
		Binary patient = new Binary();
		patient.setId(new IdDt("http://base/Binary/11/_history/22"));
		patient.setContentType("foo");
		patient.setContent(new byte[] { 1, 2, 3, 4 });

		String val = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertEquals("{\"resourceType\":\"Binary\",\"id\":\"11\",\"meta\":{\"versionId\":\"22\"},\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}", val);
	}

	
	@Test
	public void testEncodingNullExtension() {
		Patient p = new Patient();
		ExtensionDt extension = new ExtensionDt(false, "http://foo#bar");
		p.addUndeclaredExtension(extension);
		String str = ourCtx.newJsonParser().encodeResourceToString(p);

		assertEquals("{\"resourceType\":\"Patient\"}", str);

		extension.setValue(new StringDt());

		str = ourCtx.newJsonParser().encodeResourceToString(p);
		assertEquals("{\"resourceType\":\"Patient\"}", str);

		extension.setValue(new StringDt(""));

		str = ourCtx.newJsonParser().encodeResourceToString(p);
		assertEquals("{\"resourceType\":\"Patient\"}", str);

	}

	/**
	 * See #163
	 */
	@Test
	public void testParseResourceType() {
		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);

		// Patient
		Patient patient = new Patient();
		String patientId = UUID.randomUUID().toString();
		patient.setId(new IdDt("Patient", patientId));
		patient.addName().addGiven("John").addFamily("Smith");
		patient.setGender(AdministrativeGenderEnum.MALE);
		patient.setBirthDate(new DateDt("1987-04-16"));

		// Bundle
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		bundle.setType(BundleTypeEnum.COLLECTION);
		bundle.addEntry().setResource(patient);

		String bundleText = jsonParser.encodeResourceToString(bundle);
		ourLog.info(bundleText);
		
		ca.uhn.fhir.model.dstu2.resource.Bundle reincarnatedBundle = jsonParser.parseResource (ca.uhn.fhir.model.dstu2.resource.Bundle.class, bundleText);
		Patient reincarnatedPatient = (Patient) reincarnatedBundle.getEntry().get(0).getResource(); 
		
		assertEquals("Patient", patient.getId().getResourceType());
		assertEquals("Patient", reincarnatedPatient.getId().getResourceType());
	}

	/**
	 * See #144 and #146
	 */
	@Test
	public void testReportSerialize() {

		ReportObservation obsv = new ReportObservation();
		obsv.getCode().addCoding().setCode("name");
		obsv.setValue(new StringDt("value test"));
		obsv.setStatus(ObservationStatusEnum.FINAL);
		obsv.setReliability(ObservationReliabilityEnum.OK);
		obsv.addIdentifier().setSystem("System").setValue("id value");

		DiagnosticReport report = new DiagnosticReport();
		report.getContained().getContainedResources().add(obsv);
		report.addResult().setResource(obsv);

		IParser parser = new FhirContext().newXmlParser().setPrettyPrint(true);
		String message = parser.encodeResourceToString(report);
		ourLog.info(message);
		Assert.assertThat(message, containsString("contained"));
	}

	@Test
	public void testEncodeBundleOldBundleNoText() {

		Bundle b = new Bundle();

		BundleEntry e = b.addEntry();
		e.setResource(new Patient());
		b.addCategory("scheme", "term", "label");

		String val = new FhirContext().newJsonParser().setPrettyPrint(false).encodeBundleToString(b);
		ourLog.info(val);

		assertThat(val, not(containsString("text")));

		b = new FhirContext().newJsonParser().parseBundle(val);
		assertEquals(1, b.getEntries().size());

	}

	@Test
	public void testEncodeBundleNewBundleNoText() {

		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		b.getText().setDiv("");
		b.getText().getStatus().setValueAsString("");
		;

		Entry e = b.addEntry();
		e.setResource(new Patient());

		String val = new FhirContext().newJsonParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);
		assertThat(val, not(containsString("text")));

		val = new FhirContext().newXmlParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);
		assertThat(val, not(containsString("text")));

	}

	/**
	 * See #144 and #146
	 */
	@Test
	public void testReportSerializeWithMatchingId() {

		ReportObservation obsv = new ReportObservation();
		obsv.getCode().addCoding().setCode("name");
		obsv.setValue(new StringDt("value test"));
		obsv.setStatus(ObservationStatusEnum.FINAL);
		obsv.setReliability(ObservationReliabilityEnum.OK);
		obsv.addIdentifier().setSystem("System").setValue("id value");

		DiagnosticReport report = new DiagnosticReport();
		report.getContained().getContainedResources().add(obsv);

		obsv.setId("#123");
		report.addResult().setReference("#123");

		IParser parser = new FhirContext().newXmlParser().setPrettyPrint(true);
		String message = parser.encodeResourceToString(report);
		ourLog.info(message);
		Assert.assertThat(message, containsString("contained"));
	}

	/**
	 * see #144 and #146
	 */
	@Test
	public void testParseContained() {

		FhirContext c = FhirContext.forDstu2();
		IParser parser = c.newJsonParser().setPrettyPrint(true);

		Observation o = new Observation();
		o.getCode().setText("obs text");

		Patient p = new Patient();
		p.addName().addFamily("patient family");
		o.getSubject().setResource(p);

		String enc = parser.encodeResourceToString(o);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder(
			"\"resourceType\":\"Observation\"",
			"\"contained\":[",
			"\"resourceType\":\"Patient\",",
			"\"id\":\"1\"",
			"\"reference\":\"#1\""
			));
		//@formatter:on

		o = parser.parseResource(Observation.class, enc);
		assertEquals("obs text", o.getCode().getText());

		assertNotNull(o.getSubject().getResource());
		p = (Patient) o.getSubject().getResource();
		assertEquals("patient family", p.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testEncodeAndParseMetaProfileAndTags() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<IdDt> profiles = new ArrayList<IdDt>();
		profiles.add(new IdDt("http://foo/Profile1"));
		profiles.add(new IdDt("http://foo/Profile2"));
		ResourceMetadataKeyEnum.PROFILES.put(p, profiles);

		TagList tagList = new TagList();
		tagList.addTag("scheme1", "term1", "label1");
		tagList.addTag("scheme2", "term2", "label2");
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tagList);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("\"meta\":{", 
				"\"profile\":[", 
				"\"http://foo/Profile1\",", 
				"\"http://foo/Profile2\"", 
				"],", 
				"\"tag\":[", 
				"{", 
				"\"system\":\"scheme1\",", 
				"\"code\":\"term1\",", 
				"\"display\":\"label1\"", 
				"},", 
				"{", 
				"\"system\":\"scheme2\",", 
				"\"code\":\"term2\",", 
				"\"display\":\"label2\"", 
				"}", 
				"]", 
				"},"));
		//@formatter:on

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
		List<IdDt> gotLabels = ResourceMetadataKeyEnum.PROFILES.get(parsed);

		assertEquals(2, gotLabels.size());

		IdDt label = (IdDt) gotLabels.get(0);
		assertEquals("http://foo/Profile1", label.getValue());
		label = (IdDt) gotLabels.get(1);
		assertEquals("http://foo/Profile2", label.getValue());

		tagList = ResourceMetadataKeyEnum.TAG_LIST.get(parsed);
		assertEquals(2, tagList.size());

		assertEquals(new Tag("scheme1", "term1", "label1"), tagList.get(0));
		assertEquals(new Tag("scheme2", "term2", "label2"), tagList.get(1));
	}

	@Test
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<BaseCodingDt> labels = new ArrayList<BaseCodingDt>();
		labels.add(new CodingDt().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setPrimary(true).setVersion("VERSION1"));
		labels.add(new CodingDt().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setPrimary(false).setVersion("VERSION2"));

		ResourceMetadataKeyEnum.SECURITY_LABELS.put(p, labels);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertEquals("{\n" + 
			"    \"resourceType\":\"Patient\",\n" + 
			"    \"meta\":{\n" + 
			"        \"security\":[\n" + 
			"            {\n" + 
			"                \"system\":\"SYSTEM1\",\n" + 
			"                \"version\":\"VERSION1\",\n" + 
			"                \"code\":\"CODE1\",\n" + 
			"                \"display\":\"DISPLAY1\",\n" + 
			"                \"primary\":true\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"system\":\"SYSTEM2\",\n" + 
			"                \"version\":\"VERSION2\",\n" + 
			"                \"code\":\"CODE2\",\n" + 
			"                \"display\":\"DISPLAY2\",\n" + 
			"                \"primary\":false\n" + 
			"            }\n" + 
			"        ]\n" + 
			"    },\n" + 
			"    \"name\":[\n" + 
			"        {\n" + 
			"            \"family\":[\n" + 
			"                \"FAMILY\"\n" + 
			"            ]\n" + 
			"        }\n" + 
			"    ]\n" + 
			"}", enc.trim());
		//@formatter:on

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
		List<BaseCodingDt> gotLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(parsed);

		assertEquals(2, gotLabels.size());

		CodingDt label = (CodingDt) gotLabels.get(0);
		assertEquals("SYSTEM1", label.getSystem());
		assertEquals("CODE1", label.getCode());
		assertEquals("DISPLAY1", label.getDisplay());
		assertEquals(true, label.getPrimary());
		assertEquals("VERSION1", label.getVersion());

		label = (CodingDt) gotLabels.get(1);
		assertEquals("SYSTEM2", label.getSystem());
		assertEquals("CODE2", label.getCode());
		assertEquals("DISPLAY2", label.getDisplay());
		assertEquals(false, label.getPrimary());
		assertEquals("VERSION2", label.getVersion());
	}

	/**
	 * Fixing #89
	 */
	@Test
	public void testEncodeBundleWithDeletedEntry() throws ConfigurationException, DataFormatException, IOException {
		Bundle b = ourCtx.newXmlParser().parseBundle(IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/xml-bundle.xml")));
		String val = ourCtx.newJsonParser().encodeBundleToString(b);

		ourLog.info(val);

		//@formatter:off
		assertThat(val, containsString("\"deleted\":{" + 
				"\"type\":\"Patient\"," + 
				"\"resourceId\":\"4384\"," + 
				"\"instant\":\"2015-01-15T11:04:43.054-05:00\"" + 
				"}"));
		//@formatter:on
	}

	@Test
	public void testEncodeAndParseExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUseEnum.OFFICIAL).setSystem("urn:example").setValue("7000135");

		ExtensionDt ext = new ExtensionDt();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeDt("2011-01-02T11:13:15"));
		patient.addUndeclaredExtension(ext);

		ExtensionDt parent = new ExtensionDt().setUrl("http://example.com#parent");
		patient.addUndeclaredExtension(parent);
		ExtensionDt child1 = new ExtensionDt().setUrl("http://example.com#child").setValue(new StringDt("value1"));
		parent.addUndeclaredExtension(child1);
		ExtensionDt child2 = new ExtensionDt().setUrl("http://example.com#child").setValue(new StringDt("value2"));
		parent.addUndeclaredExtension(child2);

		ExtensionDt modExt = new ExtensionDt();
		modExt.setUrl("http://example.com/extensions#modext");
		modExt.setValue(new DateDt("1995-01-02"));
		modExt.setModifier(true);
		patient.addUndeclaredExtension(modExt);

		HumanNameDt name = patient.addName();
		name.addFamily("Blah");
		StringDt given = name.addGiven();
		given.setValue("Joe");
		ExtensionDt ext2 = new ExtensionDt().setUrl("http://examples.com#givenext").setValue(new StringDt("given"));
		given.addUndeclaredExtension(ext2);

		StringDt given2 = name.addGiven();
		given2.setValue("Shmoe");
		ExtensionDt given2ext = new ExtensionDt().setUrl("http://examples.com#givenext_parent");
		given2.addUndeclaredExtension(given2ext);
		given2ext.addUndeclaredExtension(new ExtensionDt().setUrl("http://examples.com#givenext_child").setValue(new StringDt("CHILD")));

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertThat(enc, Matchers.stringContainsInOrder("{\"resourceType\":\"Patient\",", "\"extension\":[{\"url\":\"http://example.com/extensions#someext\",\"valueDateTime\":\"2011-01-02T11:13:15\"}",
				"{\"url\":\"http://example.com#parent\",\"extension\":[{\"url\":\"http://example.com#child\",\"valueString\":\"value1\"},{\"url\":\"http://example.com#child\",\"valueString\":\"value2\"}]}"));
		assertThat(enc, Matchers.stringContainsInOrder("\"modifierExtension\":[" + "{" + "\"url\":\"http://example.com/extensions#modext\"," + "\"valueDate\":\"1995-01-02\"" + "}" + "],"));
		assertThat(enc, containsString("\"_given\":[" + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext\"," + "\"valueString\":\"given\"" + "}" + "]" + "}," + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext_parent\"," + "\"extension\":[" + "{"
				+ "\"url\":\"http://examples.com#givenext_child\"," + "\"valueString\":\"CHILD\"" + "}" + "]" + "}" + "]" + "}"));

		/*
		 * Now parse this back
		 */

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
		ext = parsed.getUndeclaredExtensions().get(0);
		assertEquals("http://example.com/extensions#someext", ext.getUrl());
		assertEquals("2011-01-02T11:13:15", ((DateTimeDt) ext.getValue()).getValueAsString());

		parent = patient.getUndeclaredExtensions().get(1);
		assertEquals("http://example.com#parent", parent.getUrl());
		assertNull(parent.getValue());
		child1 = parent.getExtension().get(0);
		assertEquals("http://example.com#child", child1.getUrl());
		assertEquals("value1", ((StringDt) child1.getValue()).getValueAsString());
		child2 = parent.getExtension().get(1);
		assertEquals("http://example.com#child", child2.getUrl());
		assertEquals("value2", ((StringDt) child2.getValue()).getValueAsString());

		modExt = parsed.getUndeclaredModifierExtensions().get(0);
		assertEquals("http://example.com/extensions#modext", modExt.getUrl());
		assertEquals("1995-01-02", ((DateDt) modExt.getValue()).getValueAsString());

		name = parsed.getName().get(0);

		ext2 = name.getGiven().get(0).getUndeclaredExtensions().get(0);
		assertEquals("http://examples.com#givenext", ext2.getUrl());
		assertEquals("given", ((StringDt) ext2.getValue()).getValueAsString());

		given2ext = name.getGiven().get(1).getUndeclaredExtensions().get(0);
		assertEquals("http://examples.com#givenext_parent", given2ext.getUrl());
		assertNull(given2ext.getValue());
		ExtensionDt given2ext2 = given2ext.getExtension().get(0);
		assertEquals("http://examples.com#givenext_child", given2ext2.getUrl());
		assertEquals("CHILD", ((StringDt) given2ext2.getValue()).getValue());

	}

	/**
	 * #65
	 */
	@Test
	public void testJsonPrimitiveWithExtensionEncoding() {

		QuestionnaireAnswers parsed = new QuestionnaireAnswers();
		parsed.getGroup().setLinkId("value123");
		parsed.getGroup().getLinkIdElement().addUndeclaredExtension(false, "http://123", new StringDt("HELLO"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(parsed);
		ourLog.info(encoded);
		assertThat(encoded, containsString("{\"linkId\":\"value123\",\"_linkId\":{\"extension\":[{\"url\":\"http://123\",\"valueString\":\"HELLO\"}]}}"));

	}

	@Test
	public void testParseMetadata() throws Exception {
		//@formatter:off
		String bundle = "{\n" + 
			"  \"resourceType\" : \"Bundle\",\n" + 
			"  \"base\" : \"http://foo/fhirBase1\",\n" + 
			"  \"total\" : 1,\n" + 
			"   \"link\": [{\n" + 
			"      \"relation\" : \"self\",\n" + 
			"      \"url\" : \"http://localhost:52788/Binary?_pretty=true\"\n" + 
			"   }],\n" + 
			"   \"entry\" : [{\n" + 
			"      \"base\" : \"http://foo/fhirBase2\",\n" +
			"      \"resource\" : {\n" + 
			"         \"resourceType\" : \"Patient\",\n" + 
			"         \"id\" : \"1\",\n" + 
			"         \"meta\" : {\n" +
			"            \"versionId\" : \"2\",\n" +
			"            \"lastUpdated\" : \"2001-02-22T11:22:33-05:00\"\n" +
			"         },\n" + 
			"         \"birthDate\" : \"2012-01-02\"\n" + 
			"      },\n" + 
			"      \"search\" : {\n" +
			"         \"mode\" : \"match\",\n" +
			"         \"score\" : 0.123\n" +
			"      },\n" +
			"      \"transaction\" : {\n" +
			"         \"method\" : \"POST\",\n" +
			"         \"url\" : \"http://foo/Patient?identifier=value\"\n" +
			"      }\n" +
			"   }]\n" + 
			"}";
		//@formatter:on

		Bundle b = ourCtx.newJsonParser().parseBundle(bundle);
		assertEquals(1, b.getEntries().size());

		Patient pt = (Patient) b.getEntries().get(0).getResource();
		assertEquals("http://foo/fhirBase2/Patient/1/_history/2", pt.getId().getValue());
		assertEquals("2012-01-02", pt.getBirthDateElement().getValueAsString());
		assertEquals("0.123", ResourceMetadataKeyEnum.ENTRY_SCORE.get(pt).getValueAsString());
		assertEquals("match", ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(pt).getCode());
		assertEquals("POST", ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(pt).getCode());
		assertEquals("http://foo/Patient?identifier=value", ResourceMetadataKeyEnum.LINK_SEARCH.get(pt));
		assertEquals("2001-02-22T11:22:33-05:00", ResourceMetadataKeyEnum.UPDATED.get(pt).getValueAsString());

		Bundle toBundle = new Bundle();
		toBundle.getLinkBase().setValue("http://foo/fhirBase1");
		toBundle.getTotalResults().setValue(1);
		toBundle.getLinkSelf().setValue("http://localhost:52788/Binary?_pretty=true");

		toBundle.addResource(pt, ourCtx, "http://foo/fhirBase1");
		String reEncoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(toBundle);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(bundle.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reEncoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}

	@Test
	public void testParsePatientInBundle() {

		String text = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";
		FhirContext ctx = FhirContext.forDstu2();
		Bundle b = ctx.newJsonParser().parseBundle(text);

		IResource patient = b.getEntries().get(0).getResource();
		assertEquals(Patient.class, patient.getClass());

		assertNull(ResourceMetadataKeyEnum.TAG_LIST.get(patient));
		assertNull(ResourceMetadataKeyEnum.PROFILES.get(patient));
	}

	/**
	 * Test for #146
	 */
	@Test
	public void testParseAndEncodeBundleFromXmlToJson() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-example2.xml"));

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);

		MedicationPrescription p = (MedicationPrescription) parsed.getEntry().get(0).getResource();
		assertEquals("#med", p.getMedication().getReference().getValue());

		Medication m = (Medication) p.getMedication().getResource();
		assertNotNull(m);
		assertEquals("#med", m.getId().getValue());
		assertEquals(1, p.getContained().getContainedResources().size());
		assertSame(m, p.getContained().getContainedResources().get(0));

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);
		assertThat(reencoded, containsString("contained"));

		reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);
		assertThat(reencoded, containsString("contained"));
	}

	@Test
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-example.json"));

		Bundle parsed = ourCtx.newJsonParser().parseBundle(content);
		assertEquals("http://example.com/base/Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals("1", parsed.getId().getVersionIdPart());
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getType().getValue());
		assertEquals(3, parsed.getTotalResults().getValue().intValue());
		assertEquals("http://example.com/base", parsed.getLinkBase().getValue());
		assertEquals("https://example.com/base/MedicationPrescription?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLinkNext().getValue());
		assertEquals("https://example.com/base/MedicationPrescription?patient=347&_include=MedicationPrescription.medication", parsed.getLinkSelf().getValue());

		assertEquals(2, parsed.getEntries().size());

		MedicationPrescription p = (MedicationPrescription) parsed.getEntries().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationPrescription/3123/_history/1", p.getId().getValue());

		Medication m = (Medication) parsed.getEntries().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertSame(p.getMedication().getResource(), m);

		String reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(parsed);
		ourLog.info(reencoded);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(content.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reencoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}

	@Test
	public void testParseAndEncodeBundleOldStyle() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-example.json"));

		Bundle parsed = ourCtx.newJsonParser().parseBundle(content);

		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getType().getValue());
		assertEquals(3, parsed.getTotalResults().getValue().intValue());

		assertEquals(2, parsed.getEntries().size());

		MedicationPrescription p = (MedicationPrescription) parsed.getEntries().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationPrescription/3123/_history/1", p.getId().getValue());

		Medication m = (Medication) parsed.getEntries().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertEquals("Medication/example", p.getMedication().getReference().getValue());
		assertSame(p.getMedication().getResource(), m);

		String reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(parsed);
		ourLog.info(reencoded);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(content.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reencoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		exp = exp.replace(",\"ifNoneExist\":\"Patient?identifier=234234\"", "");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}

	@Test
	public void testParseAndEncodeBundleNewStyle() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-example.json"));

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);
		assertEquals("http://example.com/base/Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals("1", parsed.getId().getVersionIdPart());
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getType());
		assertEquals(3, parsed.getTotal().intValue());
		assertEquals("http://example.com/base", parsed.getBaseElement().getValueAsString());
		assertEquals("https://example.com/base/MedicationPrescription?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink().get(0).getUrlElement().getValueAsString());
		assertEquals("https://example.com/base/MedicationPrescription?patient=347&_include=MedicationPrescription.medication", parsed.getLink().get(1).getUrlElement().getValueAsString());

		assertEquals(2, parsed.getEntry().size());

		MedicationPrescription p = (MedicationPrescription) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationPrescription/3123/_history/1", p.getId().getValue());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertEquals("Medication/example", p.getMedication().getReference().getValue());
		assertSame(p.getMedication().getResource(), m);

		String reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(content.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reencoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}

	@Test
	public void testParseAndEncodeBundleWithDeletedEntry() {

		Patient res = new Patient();
		res.setId(new IdDt("Patient", "111", "222"));
		ResourceMetadataKeyEnum.DELETED_AT.put(res, new InstantDt("2011-01-01T12:12:22Z"));

		Bundle bundle = new Bundle();
		bundle.addResource(res, ourCtx, "http://foo/base");

		String actual = ourCtx.newJsonParser().encodeBundleToString(bundle);
		ourLog.info(actual);

		String expected = "{\"resourceType\":\"Bundle\",\"entry\":[{\"deleted\":{\"type\":\"Patient\",\"resourceId\":\"111\",\"versionId\":\"222\",\"instant\":\"2011-01-01T12:12:22Z\"}}]}";
		assertEquals(expected, actual);

	}

}
