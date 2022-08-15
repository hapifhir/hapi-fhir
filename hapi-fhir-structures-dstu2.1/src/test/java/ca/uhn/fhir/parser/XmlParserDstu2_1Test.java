package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.FooMessageHeaderWithExplicitField.FooMessageSourceComponent;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.parser.PatientWithCustomCompositeExtension.FooParentExtension;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.hl7.fhir.dstu2016may.model.Address.AddressUse;
import org.hl7.fhir.dstu2016may.model.Appointment;
import org.hl7.fhir.dstu2016may.model.AuditEvent;
import org.hl7.fhir.dstu2016may.model.Binary;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleType;
import org.hl7.fhir.dstu2016may.model.CodeType;
import org.hl7.fhir.dstu2016may.model.CodeableConcept;
import org.hl7.fhir.dstu2016may.model.Coding;
import org.hl7.fhir.dstu2016may.model.Condition;
import org.hl7.fhir.dstu2016may.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu2016may.model.DateTimeType;
import org.hl7.fhir.dstu2016may.model.DateType;
import org.hl7.fhir.dstu2016may.model.DiagnosticReport;
import org.hl7.fhir.dstu2016may.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.dstu2016may.model.DocumentManifest;
import org.hl7.fhir.dstu2016may.model.Duration;
import org.hl7.fhir.dstu2016may.model.Encounter;
import org.hl7.fhir.dstu2016may.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu2016may.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.dstu2016may.model.Extension;
import org.hl7.fhir.dstu2016may.model.HumanName;
import org.hl7.fhir.dstu2016may.model.HumanName.NameUse;
import org.hl7.fhir.dstu2016may.model.IdType;
import org.hl7.fhir.dstu2016may.model.Identifier;
import org.hl7.fhir.dstu2016may.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu2016may.model.InstantType;
import org.hl7.fhir.dstu2016may.model.Location;
import org.hl7.fhir.dstu2016may.model.Medication;
import org.hl7.fhir.dstu2016may.model.MedicationOrder;
import org.hl7.fhir.dstu2016may.model.Observation;
import org.hl7.fhir.dstu2016may.model.Observation.ObservationRelationshipType;
import org.hl7.fhir.dstu2016may.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu2016may.model.Organization;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.dstu2016may.model.PrimitiveType;
import org.hl7.fhir.dstu2016may.model.Quantity;
import org.hl7.fhir.dstu2016may.model.Reference;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.dstu2016may.model.SampledData;
import org.hl7.fhir.dstu2016may.model.SimpleQuantity;
import org.hl7.fhir.dstu2016may.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class XmlParserDstu2_1Test {
	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserDstu2_1Test.class);

	@AfterEach
	public void after() {
		if (ourCtx == null) {
			ourCtx = FhirContext.forDstu2_1();
		}
		ourCtx.setNarrativeGenerator(null);
	}
	
	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnFhirContext() {
		try {
			String tmp = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><fullUrl value=\"http://lalaland.org/patient/pat1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patxuzos\"/></Patient></resource></entry></Bundle>";
			ourCtx.getParserOptions().setOverrideResourceIdWithBundleEntryFullUrl(false);
			Bundle bundle = (Bundle) ourCtx.newXmlParser().parseResource(tmp);
			assertEquals(1, bundle.getEntry().size());
			{
				Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
				IIdType o1Id = o1.getIdElement();
				assertFalse(o1Id.hasBaseUrl());
				assertEquals("Patient", o1Id.getResourceType());
				assertEquals("patxuzos", o1Id.getIdPart());
				assertFalse(o1Id.hasVersionIdPart());
			}
		} finally {
			// ensure we cleanup ourCtx so other tests continue to work
			ourCtx = null;
		}
	}
	
	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnParser() {
		try {
			String tmp = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><fullUrl value=\"http://lalaland.org/patient/pat1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patxuzos\"/></Patient></resource></entry></Bundle>";
			Bundle bundle = (Bundle) ourCtx.newXmlParser().setOverrideResourceIdWithBundleEntryFullUrl(false).parseResource(tmp);
			assertEquals(1, bundle.getEntry().size());
			{
				Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
				IIdType o1Id = o1.getIdElement();
				assertFalse(o1Id.hasBaseUrl());
				assertEquals("Patient", o1Id.getResourceType());
				assertEquals("patxuzos", o1Id.getIdPart());
				assertFalse(o1Id.hasVersionIdPart());
			}
		} finally {
			// ensure we cleanup ourCtx so other tests continue to work
			ourCtx = null;
		}
	}

	/**
	 * See #414
	 */
	@Test
	public void testParseXmlExtensionWithoutUrl() {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"        <extension>\n" + 
			"          <valueDateTime value=\"2011-01-02T11:13:15\"/>\n" + 
			"        </extension>\n" + 
			"</Patient>";
		//@formatter:on

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		Patient parsed = (Patient) parser.parseResource(input);
		assertEquals(1, parsed.getExtension().size());
		assertEquals(null, parsed.getExtension().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", ((PrimitiveType<?>)parsed.getExtension().get(0).getValue()).getValueAsString());

		try {
			parser = ourCtx.newXmlParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(input);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1822) + "Resource is missing required element 'url' in parent element 'extension'", e.getCause().getMessage());
		}
		
	}

	
	/**
	 * See #414
	 */
	@Test
	public void testParseXmlModifierExtensionWithoutUrl() {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"        <modifierExtension>\n" + 
			"          <valueDateTime value=\"2011-01-02T11:13:15\"/>\n" + 
			"        </modifierExtension>\n" + 
			"</Patient>";
		//@formatter:on

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		Patient parsed = (Patient) parser.parseResource(input);
		assertEquals(1, parsed.getModifierExtension().size());
		assertEquals(null, parsed.getModifierExtension().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", ((PrimitiveType<?>)parsed.getModifierExtension().get(0).getValue()).getValueAsString());

		try {
			parser = ourCtx.newXmlParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(input);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1822) + "Resource is missing required element 'url' in parent element 'modifierExtension'", e.getCause().getMessage());
		}
		
	}

	@Test
	public void testEncodeChainedContainedResourcer() {
		Organization gp = new Organization();
		gp.setName("grandparent");

		Organization parent = new Organization();
		parent.setName("parent");
		parent.getPartOf().setResource(gp);

		Organization child = new Organization();
		child.setName("child");
		child.getPartOf().setResource(parent);

		Patient patient = new Patient();
		patient.getManagingOrganization().setResource(child);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		patient = ourCtx.newXmlParser().parseResource(Patient.class, encoded);

		child = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("child", child.getName());

		parent = (Organization) child.getPartOf().getResource();
		assertEquals("parent", parent.getName());

		gp = (Organization) parent.getPartOf().getResource();
		assertEquals("grandparent", gp.getName());
	}





	

	@Test
	public void testBundleWithBinary() {
		//@formatter:off
		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <base value=\"http://localhost:52788\"/>\n" + 
			"   <total value=\"1\"/>\n" + 
			"   <link>\n" + 
			"      <relation value=\"self\"/>\n" + 
			"      <url value=\"http://localhost:52788/Binary?_pretty=true\"/>\n" + 
			"   </link>\n" + 
			"   <entry>\n" + 
			"      <resource>\n" + 
			"         <Binary xmlns=\"http://hl7.org/fhir\">\n" + 
			"            <id value=\"1\"/>\n" + 
			"            <meta/>\n" + 
			"            <contentType value=\"text/plain\"/>\n" + 
			"            <content value=\"AQIDBA==\"/>\n" + 
			"         </Binary>\n" + 
			"      </resource>\n" + 
			"   </entry>\n" + 
			"</Bundle>";
		//@formatter:on

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, bundle);
		assertEquals(1, b.getEntry().size());

		Binary bin = (Binary) b.getEntry().get(0).getResource();
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bin.getContent());

	}

	@Test
	public void testContainedResourceInExtensionUndeclared() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		p.addExtension(new Extension("urn:foo", new Reference(o)));

		String str = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(str);

		p = ourCtx.newXmlParser().parseResource(Patient.class, str);
		assertEquals("PATIENT", p.getName().get(0).getFamilyAsSingleString());

		List<Extension> exts = p.getExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		Reference rr = (Reference) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());
	}

	@Test
	public void testDuration() {
		Encounter enc = new Encounter();
		Duration duration = new Duration();
		duration.setUnit("day").setValue(123L);
		enc.setLength(duration);

		String str = ourCtx.newXmlParser().encodeResourceToString(enc);
		ourLog.info(str);

		assertThat(str, not(containsString("meta")));
		assertThat(str, containsString("<length><value value=\"123\"/><unit value=\"day\"/></length>"));
	}

	@Test
	public void testEncodeAndParseBundleWithResourceRefs() {

		Patient pt = new Patient();
		pt.setId("patid");
		pt.addName().addFamily("PATIENT");

		Organization org = new Organization();
		org.setId("orgid");
		org.setName("ORG");
		pt.getManagingOrganization().setResource(org);

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(pt);
		bundle.addEntry().setResource(org);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">",
			"<managingOrganization>",
			"<reference value=\"Organization/orgid\"/>", 
			"</managingOrganization>"
		));
		//@formatter:on

		bundle = ourCtx.newXmlParser().parseResource(Bundle.class, encoded);
		pt = (Patient) bundle.getEntry().get(0).getResource();
		org = (Organization) bundle.getEntry().get(1).getResource();

		assertEquals("Organization/orgid", org.getIdElement().getValue());
		assertEquals("Organization/orgid", pt.getManagingOrganization().getReferenceElement().getValue());
		assertSame(org, pt.getManagingOrganization().getResource());
	}

	@Test
	public void testEncodeAndParseCompositeExtension() {
		PatientWithCustomCompositeExtension pat = new PatientWithCustomCompositeExtension();
		pat.setId("123");
		pat.setFooParentExtension(new FooParentExtension());
		pat.getFooParentExtension().setChildA(new StringType("ValueA"));
		pat.getFooParentExtension().setChildB(new StringType("ValueB"));

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(pat);
		ourLog.info(enc);

		pat = ourCtx.newXmlParser().parseResource(PatientWithCustomCompositeExtension.class, enc);

		assertEquals("ValueA", pat.getFooParentExtension().getChildA().getValue());
		assertEquals("ValueB", pat.getFooParentExtension().getChildB().getValue());
	}

	@Test
	public void testEncodeAndParseContained() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);

		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getNameElement().setValue("Contained Test Organization");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");

		// Put the organization as a reference in the patient resource
		patient.getManagingOrganization().setResource(org);

		String encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, containsString("<contained>"));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// Create a bundle with just the patient resource
		Bundle b = new Bundle();
		b.addEntry().setResource(patient);

		// Encode the bundle
		encoded = xmlParser.encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<id value=\"1\"/>", "</contained>")));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<entry>", "</entry>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<entry>", "</entry>", "<entry>"))));

		// Re-parse the bundle
		patient = (Patient) xmlParser.parseResource(xmlParser.encodeResourceToString(patient));
		assertEquals("#1", patient.getManagingOrganization().getReference());

		assertNotNull(patient.getManagingOrganization().getResource());
		org = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("#1", org.getIdElement().getValue());
		assertEquals("Contained Test Organization", org.getName());

		// And re-encode a second time
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared and a manually set local ID
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		patient.getManagingOrganization().getResource().setId(("#333"));
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"333\"/>", "</Organization", "</contained>", "<reference value=\"#333\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));

	}

	@Test
	public void testEncodeAndParseContainedCustomTypes() {
		ourCtx = FhirContext.forDstu2_1();
		ourCtx.setDefaultTypeForProfile(CustomObservation.PROFILE, CustomObservation.class);
		ourCtx.setDefaultTypeForProfile(CustomDiagnosticReport.PROFILE, CustomDiagnosticReport.class);

		CustomObservation obs = new CustomObservation();
		obs.setStatus(ObservationStatus.FINAL);

		CustomDiagnosticReport dr = new CustomDiagnosticReport();
		dr.setStatus(DiagnosticReportStatus.FINAL);
		dr.addResult().setResource(obs);

		IParser parser = ourCtx.newXmlParser();
		parser.setPrettyPrint(true);

		String output = parser.encodeResourceToString(dr);
		ourLog.info(output);

		//@formatter:off
		assertThat(output,stringContainsInOrder(
			"<DiagnosticReport xmlns=\"http://hl7.org/fhir\">",
				"<meta>",
				"<profile value=\"http://custom_DiagnosticReport\"/>",
				"</meta>",
				"<contained>",
					"<Observation xmlns=\"http://hl7.org/fhir\">",
						"<id value=\"1\"/>",
						"<meta>",
							"<profile value=\"http://custom_Observation\"/>",
						"</meta>",
						"<status value=\"final\"/>",
					"</Observation>",
				"</contained>",
				"<status value=\"final\"/>",
				"<result>",
					"<reference value=\"#1\"/>",
				"</result>",
			"</DiagnosticReport>"));
		//@formatter:on

		/*
		 * Now PARSE!
		 */

		dr = (CustomDiagnosticReport) parser.parseResource(output);
		assertEquals(DiagnosticReportStatus.FINAL, dr.getStatus());

		assertEquals("#1", dr.getResult().get(0).getReference());
		obs = (CustomObservation) dr.getResult().get(0).getResource();
		assertEquals(ObservationStatus.FINAL, obs.getStatus());

		ourCtx = null;
	}

	@Test
	public void testEncodeAndParseContainedNonCustomTypes() {
		ourCtx = FhirContext.forDstu2_1();

		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);

		DiagnosticReport dr = new DiagnosticReport();
		dr.setStatus(DiagnosticReportStatus.FINAL);
		dr.addResult().setResource(obs);

		IParser parser = ourCtx.newXmlParser();
		parser.setPrettyPrint(true);

		String output = parser.encodeResourceToString(dr);
		ourLog.info(output);

		//@formatter:off
		assertThat(output,stringContainsInOrder(
			"<DiagnosticReport xmlns=\"http://hl7.org/fhir\">",
				"<contained>",
					"<Observation xmlns=\"http://hl7.org/fhir\">",
						"<id value=\"1\"/>",
						"<status value=\"final\"/>",
					"</Observation>",
				"</contained>",
				"<status value=\"final\"/>",
				"<result>",
					"<reference value=\"#1\"/>",
				"</result>",
			"</DiagnosticReport>"));
		//@formatter:on

		/*
		 * Now PARSE!
		 */

		dr = (DiagnosticReport) parser.parseResource(output);
		assertEquals(DiagnosticReportStatus.FINAL, dr.getStatus());

		assertEquals("#1", dr.getResult().get(0).getReference());
		obs = (Observation) dr.getResult().get(0).getResource();
		assertEquals(ObservationStatus.FINAL, obs.getStatus());

		ourCtx = null;
	}

	@Test
	public void testEncodeAndParseExtensionOnCode() {
		Organization o = new Organization();
		o.setName("ORG");
		o.addExtension(new Extension("urn:foo", new CodeType("acode")));

		String str = ourCtx.newXmlParser().encodeResourceToString(o);
		ourLog.info(str);
		assertThat(str, containsString("<valueCode value=\"acode\"/>"));

		o = ourCtx.newXmlParser().parseResource(Organization.class, str);

		List<Extension> exts = o.getExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		CodeType code = (CodeType) exts.get(0).getValue();
		assertEquals("acode", code.getValue());

	}


	@Test
	public void testEncodeAndParseExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

		Extension ext = new Extension();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeType("2011-01-02T11:13:15"));
		patient.addExtension(ext);

		Extension parent = new Extension().setUrl("http://example.com#parent");
		patient.addExtension(parent);
		Extension child1 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
		parent.addExtension(child1);
		Extension child2 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value2"));
		parent.addExtension(child2);

		Extension modExt = new Extension();
		modExt.setUrl("http://example.com/extensions#modext");
		modExt.setValue(new DateType("1995-01-02"));
		patient.addModifierExtension(modExt);

		HumanName name = patient.addName();
		name.addFamily("Blah");
		StringType given = name.addGivenElement();
		given.setValue("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("given"));
		given.addExtension(ext2);

		StringType given2 = name.addGivenElement();
		given2.setValue("Shmoe");
		Extension given2ext = new Extension().setUrl("http://examples.com#givenext_parent");
		given2.addExtension(given2ext);
		given2ext.addExtension(new Extension().setUrl("http://examples.com#givenext_child").setValue(new StringType("CHILD")));

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://example.com/extensions#someext\"><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(enc, containsString("<modifierExtension url=\"http://example.com/extensions#modext\"><valueDate value=\"1995-01-02\"/></modifierExtension>"));
		assertThat(enc, containsString(
				"<extension url=\"http://example.com#parent\"><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension><extension url=\"http://example.com#child\"><valueString value=\"value2\"/></extension></extension>"));
		assertThat(enc, containsString("<given value=\"Joe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"given\"/></extension></given>"));
		assertThat(enc, containsString(
				"<given value=\"Shmoe\"><extension url=\"http://examples.com#givenext_parent\"><extension url=\"http://examples.com#givenext_child\"><valueString value=\"CHILD\"/></extension></extension></given>"));

		/*
		 * Now parse this back
		 */

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		ext = parsed.getExtension().get(0);
		assertEquals("http://example.com/extensions#someext", ext.getUrl());
		assertEquals("2011-01-02T11:13:15", ((DateTimeType) ext.getValue()).getValueAsString());

		parent = patient.getExtension().get(1);
		assertEquals("http://example.com#parent", parent.getUrl());
		assertNull(parent.getValue());
		child1 = parent.getExtension().get(0);
		assertEquals("http://example.com#child", child1.getUrl());
		assertEquals("value1", ((StringType) child1.getValue()).getValueAsString());
		child2 = parent.getExtension().get(1);
		assertEquals("http://example.com#child", child2.getUrl());
		assertEquals("value2", ((StringType) child2.getValue()).getValueAsString());

		modExt = parsed.getModifierExtension().get(0);
		assertEquals("http://example.com/extensions#modext", modExt.getUrl());
		assertEquals("1995-01-02", ((DateType) modExt.getValue()).getValueAsString());

		name = parsed.getName().get(0);

		ext2 = name.getGiven().get(0).getExtension().get(0);
		assertEquals("http://examples.com#givenext", ext2.getUrl());
		assertEquals("given", ((StringType) ext2.getValue()).getValueAsString());

		given2ext = name.getGiven().get(1).getExtension().get(0);
		assertEquals("http://examples.com#givenext_parent", given2ext.getUrl());
		assertNull(given2ext.getValue());
		Extension given2ext2 = given2ext.getExtension().get(0);
		assertEquals("http://examples.com#givenext_child", given2ext2.getUrl());
		assertEquals("CHILD", ((StringType) given2ext2.getValue()).getValue());

	}

	/**
	 * See #216
	 */
	@Test
	public void testEncodeAndParseIdentifierDstu2() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("SYS").setValue("VAL").setType(new CodeableConcept().addCoding(new Coding().setSystem("http://hl7.org/fhir/v2/0203").setCode("MR")));

		String out = xmlParser.encodeResourceToString(patient);
		ourLog.info(out);

		assertThat(out, stringContainsInOrder("<identifier>",
				"<type>",
				"<coding>",
				"<system value=\"http://hl7.org/fhir/v2/0203\"/>", 
				"<code value=\"MR\"/>", 
				"</coding>", 
				"</type>",
				"<system value=\"SYS\"/>", 
				"<value value=\"VAL\"/>", 
				"</identifier>"));

		patient = ourCtx.newXmlParser().parseResource(Patient.class, out);
		assertEquals("http://hl7.org/fhir/v2/0203", patient.getIdentifier().get(0).getType().getCoding().get(0).getSystem());
		assertEquals("MR", patient.getIdentifier().get(0).getType().getCoding().get(0).getCode());
	}

	/**
	 * See #347
	 */
	@Test
	public void testEncodeAndParseMedicationOrder() {
		MedicationOrder mo = new MedicationOrder();
		mo.getDateWrittenElement().setValueAsString("2015-10-05");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(mo);
		ourLog.info(encoded);

		mo = ourCtx.newXmlParser().parseResource(MedicationOrder.class, encoded);
		assertEquals("2015-10-05", mo.getDateWrittenElement().getValueAsString());
	}

	/**
	 * See #336
	 */
	@Test
	public void testEncodeAndParseNullPrimitiveWithExtensions() {

		Patient p = new Patient();
		p.setId("patid");
		HumanName name = p.addName();
		name.addGivenElement().setValue(null).setId("f0").addExtension(new Extension("http://foo", new StringType("FOOEXT0")));
		name.addGivenElement().setValue("V1").setId("f1").addExtension((Extension) new Extension("http://foo", new StringType("FOOEXT1")).setId("ext1id"));
		name.addGivenElement(); // this one shouldn't get encoded
		name.addGivenElement().setValue(null).addExtension(new Extension("http://foo", new StringType("FOOEXT3")));
		name.setId("nameid");

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);

		output = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(p);
		String expected = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patid\"/><name id=\"nameid\"><given id=\"f0\"><extension url=\"http://foo\"><valueString value=\"FOOEXT0\"/></extension></given><given id=\"f1\" value=\"V1\"><extension id=\"ext1id\" url=\"http://foo\"><valueString value=\"FOOEXT1\"/></extension></given><given><extension url=\"http://foo\"><valueString value=\"FOOEXT3\"/></extension></given></name></Patient>";

		ourLog.info("Expected: {}", expected);
		ourLog.info("Actual  : {}", output);

		assertEquals(expected, output);

		p = ourCtx.newXmlParser().parseResource(Patient.class, output);
		assertEquals("patid", p.getIdElement().getIdPart());

		name = p.getName().get(0);
		assertEquals("nameid", name.getId());
		assertEquals(3, name.getGiven().size());

		assertEquals(null, name.getGiven().get(0).getValue());
		assertEquals("V1", name.getGiven().get(1).getValue());
		assertEquals(null, name.getGiven().get(2).getValue());

		assertEquals("f0", name.getGiven().get(0).getId());
		assertEquals("f1", name.getGiven().get(1).getId());
		assertEquals(null, name.getGiven().get(2).getId());

		assertEquals(1, name.getGiven().get(0).getExtension().size());
		assertEquals("http://foo", name.getGiven().get(0).getExtension().get(0).getUrl());
		assertEquals("FOOEXT0", ((StringType) name.getGiven().get(0).getExtension().get(0).getValue()).getValue());
		assertEquals(null, name.getGiven().get(0).getExtension().get(0).getId());

		assertEquals(1, name.getGiven().get(1).getExtension().size());
		assertEquals("http://foo", name.getGiven().get(1).getExtension().get(0).getUrl());
		assertEquals("FOOEXT1", ((StringType) name.getGiven().get(1).getExtension().get(0).getValue()).getValue());
		assertEquals("ext1id", name.getGiven().get(1).getExtension().get(0).getId());

		assertEquals(1, name.getGiven().get(2).getExtension().size());
		assertEquals("http://foo", name.getGiven().get(2).getExtension().get(0).getUrl());
		assertEquals("FOOEXT3", ((StringType) name.getGiven().get(2).getExtension().get(0).getValue()).getValue());
		assertEquals(null, name.getGiven().get(2).getExtension().get(0).getId());

	}




	@Test
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<Coding> labels = new ArrayList<Coding>();
		labels.add(new Coding().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setVersion("VERSION1"));
		labels.add(new Coding().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setVersion("VERSION2"));
		p.getMeta().getSecurity().addAll(labels);

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("<Patient xmlns=\"http://hl7.org/fhir\">", 
			"<meta>", 
			"<security>", 
			"<system value=\"SYSTEM1\"/>", 
			"<version value=\"VERSION1\"/>", 
			"<code value=\"CODE1\"/>", 
			"<display value=\"DISPLAY1\"/>", 
			"</security>", 
			"<security>", 
			"<system value=\"SYSTEM2\"/>", 
			"<version value=\"VERSION2\"/>", 
			"<code value=\"CODE2\"/>", 
			"<display value=\"DISPLAY2\"/>", 
			"</security>",
			"</meta>", 
			"<name>", 
			"<family value=\"FAMILY\"/>", 
			"</name>", 
			"</Patient>"));
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		List<Coding> gotLabels = parsed.getMeta().getSecurity();

		assertEquals(2, gotLabels.size());

		Coding label = gotLabels.get(0);
		assertEquals("SYSTEM1", label.getSystem());
		assertEquals("CODE1", label.getCode());
		assertEquals("DISPLAY1", label.getDisplay());
		assertEquals("VERSION1", label.getVersion());

		label = gotLabels.get(1);
		assertEquals("SYSTEM2", label.getSystem());
		assertEquals("CODE2", label.getCode());
		assertEquals("DISPLAY2", label.getDisplay());
		assertEquals("VERSION2", label.getVersion());
	}


	@Test
	public void testEncodeBinaryWithNoContentType() {
		Binary b = new Binary();
		b.setContent(new byte[] { 1, 2, 3, 4 });

		String output = ourCtx.newXmlParser().encodeResourceToString(b);
		ourLog.info(output);

		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"><content value=\"AQIDBA==\"/></Binary>", output);
	}

	@Test
	public void testEncodeBundleWithContained() {
		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addResult().setResource(new Observation().setCode(new CodeableConcept().setText("Sharp1")).setId("#1"));
		rpt.addResult().setResource(new Observation().setCode(new CodeableConcept().setText("Uuid1")).setId("urn:uuid:UUID1"));

		Bundle b = new Bundle();
		b.addEntry().setResource(rpt);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder("<DiagnosticReport", "<contained", "<Observation", "<text value=\"Sharp1\"", "</DiagnosticReport"));
		assertThat(encoded, not(stringContainsInOrder("<DiagnosticReport", "<contained", "<Observation", "<contained", "<Observation", "</DiagnosticReport")));
	}

	/**
	 * See #326
	 */
	@Test
	public void testEncodeContainedResource() {
		Patient patient = new Patient();
		patient.getBirthDateElement().setValueAsString("2016-04-05");
		patient.addExtension().setUrl("test").setValue(new Reference(new Condition()));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">", 
				"<contained>", 
					"<Condition xmlns=\"http://hl7.org/fhir\">", 
					"<id value=\"1\"/>",
					"</Condition>",
				"</contained>",
				"<extension url=\"test\">",
					"<valueReference>",
					"<reference value=\"#1\"/>", 
					"</valueReference>",
				"</extension>",
				"<birthDate value=\"2016-04-05\"/>", 
			"</Patient>"
		));
		//@formatter:on
	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResources() {

		MedicationOrder medicationPrescript = new MedicationOrder();

		String medId = "123";
		CodeableConcept codeDt = new CodeableConcept().addCoding(new Coding().setSystem("urn:sys").setCode("code1"));

		// Adding medication to Contained.
		Medication medResource = new Medication();
		medResource.setCode(codeDt);
		medResource.setId("#" + medId);
		medicationPrescript.getContained().add(medResource);

		// Medication reference. This should point to the contained resource.
		Reference medRefDt = new Reference("#" + medId);
		medRefDt.setDisplay("MedRef");
		medicationPrescript.setMedication(medRefDt);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(medicationPrescript);
		ourLog.info(encoded);

		// @formatter:on
		assertThat(encoded,
				stringContainsInOrder("<MedicationOrder xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"123\"/>", "<code>", "<coding>",
						"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#123\"/>",
						"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationOrder>"));
		//@formatter:off

	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResourcesAutomatic() {
		
		MedicationOrder medicationPrescript = new MedicationOrder();
		String nameDisp = "MedRef";
		CodeableConcept codeDt = new CodeableConcept().addCoding(new Coding("urn:sys", "code1", null));
		
		// Adding medication to Contained.
		Medication medResource = new Medication();
		// No ID set
		medResource.setCode(codeDt);

		// Medication reference. This should point to the contained resource.
		Reference medRefDt = new Reference();
		medRefDt.setDisplay(nameDisp);
		// Resource reference set, but no ID
		medRefDt.setResource(medResource);
		medicationPrescript.setMedication(medRefDt);
		
		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(medicationPrescript);
		ourLog.info(encoded);
		
		//@formatter:on
		assertThat(encoded,
				stringContainsInOrder("<MedicationOrder xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"1\"/>", "<code>", "<coding>",
						"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#1\"/>",
						"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationOrder>"));
		//@formatter:off
	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResourcesManualContainUsingNonLocalId() {
		
		MedicationOrder medicationPrescript = new MedicationOrder();
		
		String medId = "123";
		CodeableConcept codeDt = new CodeableConcept().addCoding(new Coding("urn:sys", "code1", null));

		// Adding medication to Contained.
		Medication medResource = new Medication();
		medResource.setCode(codeDt);
		medResource.setId(medId); // ID does not start with '#'
		medicationPrescript.getContained().add(medResource);

		// Medication reference. This should point to the contained resource.
		Reference medRefDt = new Reference("#" + medId);
		medRefDt.setDisplay("MedRef");
		medicationPrescript.setMedication(medRefDt);
		
		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(medicationPrescript);
		ourLog.info(encoded);
		
		//@formatter:on
		assertThat(encoded,
				stringContainsInOrder("<MedicationOrder xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"123\"/>", "<code>", "<coding>",
						"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#123\"/>",
						"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationOrder>"));
		//@formatter:off

	}

	@Test
	public void testEncodeContainedWithNarrative() throws Exception {
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);

		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getNameElement().setValue("Contained Test Organization");
		org.getText().setDivAsString("<div>FOOBAR</div>");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getText().setDivAsString("<div>BARFOO</div>");
		patient.getManagingOrganization().setResource(org);

		String encoded = parser.encodeResourceToString(patient);
		ourLog.info(encoded);
		
		assertThat(encoded, stringContainsInOrder("<Patient", "<text>", "<div xmlns=\"http://www.w3.org/1999/xhtml\">BARFOO</div>", "<contained>", "<Organization", "</Organization"));
		assertThat(encoded, stringContainsInOrder("<Patient", "<text>", "<contained>", "<Organization", "<text", "</Organization"));
		
		assertThat(encoded, (containsString("FOOBAR")));
		assertThat(encoded, (containsString("BARFOO")));

	}


	@Test
	public void testEncodeDeclaredBlock() throws Exception {
		FooMessageSourceComponent source = new FooMessageHeaderWithExplicitField.FooMessageSourceComponent();
		source.getMessageHeaderApplicationId().setValue("APPID");
		source.setName("NAME");

		FooMessageHeaderWithExplicitField header = new FooMessageHeaderWithExplicitField();
		header.setSourceNew(source);

		header.addDestination().setName("DEST");

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(header);

		IParser p = ourCtx.newXmlParser();
		p.setPrettyPrint(true);

		String encode = p.encodeResourceToString(bundle);
		ourLog.info(encode);

		assertThat(encode, containsString("<value value=\"APPID\"/>"));
		assertThat(encode, stringContainsInOrder("<source", "<dest"));
	}

	/**
	 * Make sure whitespace is preserved for pre tags
	 */
	@Test
	public void testEncodeDivWithPreNonPrettyPrint() {

		Patient p = new Patient();
		p.getText().setDivAsString("<div>\n\n<p>A P TAG</p><p><pre>line1\nline2\nline3  <b>BOLD</b></pre></p></div>");

		String output = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(p);
		ourLog.info(output);

		//@formatter:off
		assertThat(output, stringContainsInOrder(
			"<text><div",
			"<p>A P TAG</p><p>",
			"<pre>line1\nline2\nline3  <b>BOLD</b></pre>"
		));
		//@formatter:on

	}

	@Test
	public void testEncodeDivWithPrePrettyPrint() {

		Patient p = new Patient();
		p.getText().setDivAsString("<div>\n\n<p>A P TAG</p><p><pre>line1\nline2\nline3  <b>BOLD</b></pre></p></div>");

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);

		//@formatter:off
		assertThat(output, stringContainsInOrder(
			"   <text>",
			"      <div",
			"         <pre>line1\nline2\nline3  <b>BOLD</b></pre>"
		));
		//@formatter:on

	}

	@Test
	public void testEncodeDoesntIncludeUuidId() {
		Patient p = new Patient();
		p.setId(new IdType("urn:uuid:42795ed8-041f-4ebf-b6f4-78ef6f64c2f2"));
		p.addIdentifier().setSystem("ACME");

		String actual = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		assertThat(actual, not(containsString("78ef6f64c2f2")));
	}

	@Test
	public void testEncodeEmptyBinary() {
		String output = ourCtx.newXmlParser().encodeResourceToString(new Binary());
		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"/>", output);
	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag() {
		ArrayList<Coding> tagList = new ArrayList<Coding>();
		tagList.add(new Coding());
		tagList.add(new Coding().setDisplay("Label"));

		Patient p = new Patient();
		p.getMeta().getTag().addAll(tagList);

		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, not(containsString("tag")));
	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag2() {
		ArrayList<Coding> tagList = new ArrayList<Coding>();
		tagList.add(new Coding().setSystem("scheme").setCode("code"));
		tagList.add(new Coding().setDisplay("Label"));

		Patient p = new Patient();
		p.getMeta().getTag().addAll(tagList);

		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, containsString("tag"));
		assertThat(encoded, containsString("scheme"));
		assertThat(encoded, not(containsString("Label")));
	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifier() {
		Observation obs = new Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		Extension ext = obs.addExtension();
		ext.setUrl("http://exturl").setValue(new StringType("ext_url_value"));

		obs.getCode().setText("CODE");

		IParser parser = ourCtx.newXmlParser();

		String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(output);

		//@formatter:off
		assertThat(output, stringContainsInOrder(
			"<id value=\"1\"/>",
			"<meta>",
			"<profile value=\"http://profile\"/>",
			"<extension url=\"http://exturl\">",
			"<valueString value=\"ext_url_value\"/>",
			"<text value=\"CODE\"/>"
		));
		assertThat(output, not(stringContainsInOrder(
			"<url value=\"http://exturl\"/>"
		)));
		//@formatter:on

		obs = parser.parseResource(Observation.class, output);
		assertEquals(1, obs.getExtension().size());
		assertEquals("http://exturl", obs.getExtension().get(0).getUrl());
		assertEquals("ext_url_value", ((StringType) obs.getExtension().get(0).getValue()).getValue());
	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifierWithChildExtension() {
		Observation obs = new Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		Extension ext = obs.addExtension();
		ext.setUrl("http://exturl");

		Extension subExt = ext.addExtension();
		subExt.setUrl("http://subext").setValue(new StringType("sub_ext_value"));

		obs.getCode().setText("CODE");

		IParser parser = ourCtx.newXmlParser();

		String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(output);

		//@formatter:off
		assertThat(output, stringContainsInOrder(
			"<id value=\"1\"/>",
			"<meta>",
			"<profile value=\"http://profile\"/>",
			"<extension url=\"http://exturl\">",
			"<extension url=\"http://subext\">",
			"<valueString value=\"sub_ext_value\"/>",
			"<text value=\"CODE\"/>"
		));
		assertThat(output, not(stringContainsInOrder(
			"<url value=\"http://exturl\"/>"
		)));
		//@formatter:on

		obs = parser.parseResource(Observation.class, output);
		assertEquals(1, obs.getExtension().size());
		assertEquals("http://exturl", obs.getExtension().get(0).getUrl());
		assertEquals(1, obs.getExtension().get(0).getExtension().size());
		assertEquals("http://subext", obs.getExtension().get(0).getExtension().get(0).getUrl());
		assertEquals("sub_ext_value", ((StringType) obs.getExtension().get(0).getExtension().get(0).getValue()).getValue());
	}

	/**
	 * See #327
	 */
	@Test
	public void testEncodeExtensionWithContainedResource() {

		TestPatientFor327 patient = new TestPatientFor327();
		patient.setBirthDateElement(new DateType("2016-04-14"));

		List<Reference> conditions = new ArrayList<Reference>();
		Condition condition = new Condition();
		condition.addBodySite().setText("BODY SITE");
		conditions.add(new Reference(condition));
		patient.setCondition(conditions);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">", 
				"<contained>", 
					"<Condition xmlns=\"http://hl7.org/fhir\">", 
						"<id value=\"1\"/>", 
						"<bodySite>", 
							"<text value=\"BODY SITE\"/>", 
						"</bodySite>", 
					"</Condition>", 
				"</contained>", 
				"<extension url=\"testCondition\">",
					"<valueReference>", 
						"<reference value=\"#1\"/>", 
					"</valueReference>", 
				"</extension>", 
				"<birthDate value=\"2016-04-14\"/>", 
			"</Patient>"
		));
		//@formatter:on
	}

	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.addExtension(new Extension("urn:foo", new Reference("Organization/123")));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueReference><reference value=\"Organization/123\"/></valueReference></extension>"));

		Patient actual = parser.parseResource(Patient.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		List<Extension> ext = actual.getExtension();
		assertEquals(1, ext.size());
		Reference ref = (Reference) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReference());

	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPath3() {
		ourCtx = FhirContext.forDstu2_1();

		assertNull(ourCtx.newXmlParser().getStripVersionsFromReferences());

		AuditEvent auditEvent = new AuditEvent();
		auditEvent.addEntity().setReference(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newXmlParser();

		parser.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc, containsString("<reference value=\"http://foo.com/Organization/2/_history/1\"/>"));

		parser.setDontStripVersionsFromReferencesAtPaths(new ArrayList<String>());
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc, containsString("<reference value=\"http://foo.com/Organization/2\"/>"));

		parser.setDontStripVersionsFromReferencesAtPaths((String[]) null);
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc, containsString("<reference value=\"http://foo.com/Organization/2\"/>"));

		parser.setDontStripVersionsFromReferencesAtPaths((List<String>) null);
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc, containsString("<reference value=\"http://foo.com/Organization/2\"/>"));

	}

	@Test
	public void testEncodeNarrativeSuppressed() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSuppressNarratives(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>", "<code value=\"" + Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
		assertThat(encoded, not(containsString("text")));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, containsString("maritalStatus"));
	}

	@Test
	public void testEncodeNonContained() {
		// Create an organization
		Organization org = new Organization();
		org.setId("Organization/65546");
		org.getNameElement().setValue("Contained Test Organization");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getManagingOrganization().setResource(org);

		// Create a list containing both resources. In a server method, you might just
		// return this list, but here we will create a bundle to encode.
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		resources.add(org);
		resources.add(patient);

		// Create a bundle with both
		Bundle b = new Bundle();
		b.addEntry().setResource(org);
		b.addEntry().setResource(patient);

		// Encode the buntdle
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("<contained>")));
		assertThat(encoded, stringContainsInOrder("<Organization", "<id value=\"65546\"/>", "</Organization>"));
		assertThat(encoded, containsString("<reference value=\"Organization/65546\"/>"));
		assertThat(encoded, stringContainsInOrder("<Patient", "<id value=\"1333\"/>", "</Patient>"));

		encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("<contained>")));
		assertThat(encoded, containsString("<reference value=\"Organization/65546\"/>"));

	}





	@Test
	public void testEncodeReferenceUsingUnqualifiedResourceWorksCorrectly() {

		Patient patient = new Patient();
		patient.setId("phitcc_pat_normal");
		patient.addName().addGiven("Patty").setUse(NameUse.NICKNAME);
		patient.addTelecom().setSystem(ContactPointSystem.EMAIL).setValue("patpain@ehealthinnovation.org");
		patient.setGender(AdministrativeGender.FEMALE);
		patient.setBirthDateElement(new DateType("2001-10-13"));

		DateTimeType obsEffectiveTime = new DateTimeType("2015-04-11T12:22:01-04:00");

		Observation obsParent = new Observation();
		obsParent.setId("phitcc_obs_bp_parent");
		obsParent.getSubject().setResource(patient);
		obsParent.setStatus(ObservationStatus.FINAL);
		obsParent.setEffective(obsEffectiveTime);

		Observation obsSystolic = new Observation();
		obsSystolic.setId("phitcc_obs_bp_dia");
		obsSystolic.getSubject().setResource(patient);
		obsSystolic.setEffective(obsEffectiveTime);
		obsParent.addRelated().setType(ObservationRelationshipType.HASMEMBER).setTarget(new Reference(obsSystolic));

		Observation obsDiastolic = new Observation();
		obsDiastolic.setId("phitcc_obs_bp_dia");
		obsDiastolic.getSubject().setResource(patient);
		obsDiastolic.setEffective(obsEffectiveTime);
		obsParent.addRelated().setType(ObservationRelationshipType.HASMEMBER).setTarget(new Reference(obsDiastolic));

		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(obsParent);
		ourLog.info(str);

		assertThat(str, containsString("<reference value=\"Patient/phitcc_pat_normal\"/>"));
		assertThat(str, containsString("<reference value=\"Observation/phitcc_obs_bp_dia\"/>"));
	}


	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>", "<code value=\"" + Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeSummary2() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		patient.getMeta().addTag().setSystem("foo").setCode("bar");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"foo\"/>", "<code value=\"bar\"/>", "</tag>"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>", "<code value=\"" + Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeUndeclaredBlock() throws Exception {
		FooMessageHeader.FooMessageSourceComponent source = new FooMessageHeader.FooMessageSourceComponent();
		source.getMessageHeaderApplicationId().setValue("APPID");
		source.setName("NAME");

		FooMessageHeader header = new FooMessageHeader();
		header.setSource(source);

		header.addDestination().setName("DEST");

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(header);

		IParser p = ourCtx.newXmlParser();
		p.setPrettyPrint(true);

		String encode = p.encodeResourceToString(bundle);
		ourLog.info(encode);

		assertThat(encode, containsString("<value value=\"APPID\"/>"));
		assertThat(encode, stringContainsInOrder("<source", "<dest"));
	}


	@Test
	public void testEncodeWithContained() {
		List<Resource> contained = new ArrayList<Resource>();

		// Will be added by reference
		Patient p = new Patient();
		p.setId("#" + "1000");
		contained.add(p);

		// Will be added by direct resource object
		Location l = new Location();
		l.setId("#" + "1001");
		contained.add(l);

		// Will not be referred to (and therefore shouldn't appear in output)
		Location l2 = new Location();
		l2.setId("#1002");
		contained.add(l2);

		Appointment appointment = new Appointment();
		appointment.setId("1234");
		appointment.getContained().addAll(contained);

		appointment.addParticipant().getActor().setReference("#1000");
		appointment.addParticipant().getActor().setResource(l);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(appointment);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
			"<Appointment xmlns=\"http://hl7.org/fhir\">", 
			"<id value=\"1234\"/>", 
			"<contained>", 
			"<Patient xmlns=\"http://hl7.org/fhir\">", 
			"<id value=\"1000\"/>", 
			"</Patient>", 
			"</contained>", 
			"<contained>", 
			"<Location xmlns=\"http://hl7.org/fhir\">", 
			"<id value=\"1001\"/>", 
			"</Location>", 
			"</contained>", 
			"<participant>", 
			"<actor>", 
			"<reference value=\"#1000\"/>", 
			"</actor>", 
			"</participant>", 
			"<participant>", 
			"<actor>", 
			"<reference value=\"#1001\"/>", 
			"</actor>", 
			"</participant>", 
			"</Appointment>"
		));
		//@formatter:on

		assertThat(encoded, not(containsString("#1002")));
	}

	@Test
	public void testEncodeWithDontEncodeElements() throws Exception {
		Patient patient = new Patient();
		patient.setId("123");
		patient.getMeta().addProfile("http://profile");
		patient.addName().addFamily("FAMILY").addGiven("GIVEN");
		patient.addAddress().addLine("LINE1");

		{
			IParser p = ourCtx.newXmlParser();
			p.setDontEncodeElements(Sets.newHashSet("*.meta", "*.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("address"));
			assertThat(out, not(containsString("id")));
			assertThat(out, not(containsString("meta")));
		}
		{
			IParser p = ourCtx.newXmlParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.meta", "Patient.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("address"));
			assertThat(out, not(containsString("id")));
			assertThat(out, not(containsString("meta")));
		}
		{
			IParser p = ourCtx.newXmlParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.name.family"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("GIVEN"));
			assertThat(out, not(containsString("FAMILY")));
		}
		{
			IParser p = ourCtx.newXmlParser();
			p.setDontEncodeElements(Sets.newHashSet("*.meta", "*.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("address"));
			assertThat(out, not(containsString("id")));
			assertThat(out, not(containsString("meta")));
		}
		{
			IParser p = ourCtx.newXmlParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.meta"));
			p.setEncodeElements(new HashSet<>(Arrays.asList("Patient.name")));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("id"));
			assertThat(out, not(containsString("address")));
			assertThat(out, not(containsString("meta")));
			assertThat(out, not(containsString("SUBSETTED")));
		}
	}

	@Test
	public void testEncodeWithEncodeElements() throws Exception {
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://profile");
		patient.addName().addFamily("FAMILY");
		patient.addAddress().addLine("LINE1");

		Bundle bundle = new Bundle();
		bundle.setTotal(100);
		bundle.addEntry().setResource(patient);

		{
			IParser p = ourCtx.newXmlParser();
			p.setEncodeElements(new HashSet<String>(Arrays.asList("Patient.name", "Bundle.entry")));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(bundle);
			ourLog.info(out);
			assertThat(out, not(containsString("total")));
			assertThat(out, (containsString("Patient")));
			assertThat(out, (containsString("name")));
			assertThat(out, not(containsString("address")));
		}
		{
			IParser p = ourCtx.newXmlParser();
			p.setEncodeElements(new HashSet<String>(Arrays.asList("Patient.name")));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(bundle);
			ourLog.info(out);
			assertThat(out, (containsString("total")));
			assertThat(out, (containsString("Patient")));
			assertThat(out, (containsString("name")));
			assertThat(out, not(containsString("address")));
		}
		{
			IParser p = ourCtx.newXmlParser();
			p.setEncodeElements(new HashSet<String>(Arrays.asList("Patient")));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(bundle);
			ourLog.info(out);
			assertThat(out, (containsString("total")));
			assertThat(out, (containsString("Patient")));
			assertThat(out, (containsString("name")));
			assertThat(out, (containsString("address")));
		}

	}

	@Test
	public void testEncodeWithNarrative() {
		Patient p = new Patient();
		p.addName().addFamily("Smith").addGiven("John");

		ourCtx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		String output = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(output);

		assertThat(output, containsString("<text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\">John <b>SMITH </b>"));
	}

	

	@Test
	public void testMoreExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

		Extension ext = new Extension();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeType("2011-01-02T11:13:15"));

		// Add the extension to the resource
		patient.addExtension(ext);
		// END SNIPPET: resourceExtension

		// START SNIPPET: resourceStringExtension
		HumanName name = patient.addName();
		name.addFamily("Shmoe");
		StringType given = name.addGivenElement();
		given.setValue("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("given"));
		given.addExtension(ext2);

		StringType given2 = name.addGivenElement();
		given2.setValue("Shmoe");
		Extension given2ext = new Extension().setUrl("http://examples.com#givenext_parent");
		given2.addExtension(given2ext);
		Extension givenExtChild = new Extension();
		givenExtChild.setUrl("http://examples.com#givenext_child").setValue(new StringType("CHILD"));
		given2ext.addExtension(givenExtChild);
		// END SNIPPET: resourceStringExtension

		// START SNIPPET: subExtension
		Extension parent = new Extension().setUrl("http://example.com#parent");
		patient.addExtension(parent);

		Extension child1 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
		parent.addExtension(child1);

		Extension child2 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
		parent.addExtension(child2);
		// END SNIPPET: subExtension

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://example.com/extensions#someext\"><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(enc, containsString(
				"<extension url=\"http://example.com#parent\"><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension></extension>"));
		assertThat(enc, containsString("<given value=\"Joe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"given\"/></extension></given>"));
		assertThat(enc, containsString(
				"<given value=\"Shmoe\"><extension url=\"http://examples.com#givenext_parent\"><extension url=\"http://examples.com#givenext_child\"><valueString value=\"CHILD\"/></extension></extension></given>"));
	}

	@Test
	public void testOmitResourceId() {
		Patient p = new Patient();
		p.setId("123");
		p.addName().addFamily("ABC");

		assertThat(ourCtx.newXmlParser().encodeResourceToString(p), stringContainsInOrder("123", "ABC"));
		assertThat(ourCtx.newXmlParser().setOmitResourceId(true).encodeResourceToString(p), containsString("ABC"));
		assertThat(ourCtx.newXmlParser().setOmitResourceId(true).encodeResourceToString(p), not(containsString("123")));
	}

	@Test
	@Disabled
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu2_1Test.class.getResourceAsStream("/bundle-example.xml"), StandardCharsets.UTF_8);

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getIdElement().getValue());
		assertEquals("1", parsed.getMeta().getVersionId());
		assertEquals("1", parsed.getIdElement().getVersionIdPart());
		assertEquals(("2014-08-18T01:43:30Z"), parsed.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("searchset", parsed.getType().toCode());
		assertEquals(3, parsed.getTotal());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink("next").getUrl());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLink("self").getUrl());

		assertEquals(2, parsed.getEntry().size());
		assertEquals("http://foo?search", parsed.getEntry().get(0).getLink("search").getUrl());

		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", parsed.getEntry().get(0).getLink("alternate").getUrl());
		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference());
		assertEquals("2014-08-16T05:31:17Z", p.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId());
		assertSame(((Reference) p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		compareXml(content, reencoded);

	}

	@Test
	@Disabled
	public void testParseAndEncodeBundleNewStyle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu2_1Test.class.getResourceAsStream("/bundle-example.xml"), StandardCharsets.UTF_8);

		IParser newXmlParser = ourCtx.newXmlParser();
		Bundle parsed = newXmlParser.parseResource(Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getIdElement().getValue());
		assertEquals("1", parsed.getIdElement().getVersionIdPart());
		assertEquals("2014-08-18T01:43:30Z", parsed.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("searchset", parsed.getType());
		assertEquals(3, parsed.getTotal());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink().get(0).getUrlElement().getValueAsString());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLink().get(1).getUrlElement().getValueAsString());

		assertEquals(2, parsed.getEntry().size());
		assertEquals("alternate", parsed.getEntry().get(0).getLink().get(0).getRelation());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", parsed.getEntry().get(0).getLink().get(0).getUrl());
		assertEquals("http://foo?search", parsed.getEntry().get(0).getRequest().getUrlElement().getValueAsString());

		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference());
		assertEquals("2014-08-16T05:31:17Z", p.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId());
		// assertEquals("3123", p.getId());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId());
		assertSame(((Reference) p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		compareXml(content, reencoded);
	}

	@Test
	public void testParseAndEncodeComments() throws IOException {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"  <!--pre resource comment-->" +
				"  <id value=\"pat1\"/>\n" + 
				"  <text>\n" + 
				"    <status value=\"generated\"/>\n" + 
				"    <div xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
				"\n" + 
				"      <p>Patient Donald DUCK @ Acme Healthcare, Inc. MR = 654321</p>\n" + 
				"\n" + 
				"    </div>\n" + 
				"  </text>\n" + 
				"  <!--identifier comment 1-->\n" +
				"  <!--identifier comment 2-->\n" +
				"  <identifier>\n" + 
				"    <!--use comment 1-->\n" +
				"    <!--use comment 2-->\n" +
				"    <use value=\"usual\"/>\n" + 
				"    <type>\n" + 
				"      <coding>\n" + 
				"        <system value=\"http://hl7.org/fhir/v2/0203\"/>\n" + 
				"        <code value=\"MR\"/>\n" + 
				"      </coding>\n" + 
				"    </type>\n" + 
				"    <system value=\"urn:oid:0.1.2.3.4.5.6.7\"/>\n" + 
				"    <value value=\"654321\"/>\n" + 
				"  </identifier>\n" + 
				"  <active value=\"true\"/>" +
				"  <!--post resource comment-->" +
				"</Patient>";
		//@formatter:off

		Patient res = ourCtx.newXmlParser().parseResource(Patient.class, input);
		res.getFormatCommentsPre();
		assertEquals("Patient/pat1", res.getId());
		assertEquals("654321", res.getIdentifier().get(0).getValue());
		assertEquals(true, res.getActive());
		
		assertThat(res.getIdElement().getFormatCommentsPre(), contains("pre resource comment"));
		assertThat(res.getIdentifier().get(0).getFormatCommentsPre(), contains("identifier comment 1", "identifier comment 2"));
		assertThat(res.getIdentifier().get(0).getUseElement().getFormatCommentsPre(), contains("use comment 1", "use comment 2"));
		assertThat(res.getActiveElement().getFormatCommentsPost(), contains("post resource comment"));
		
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);
		
		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
				"\"identifier\": [", 
				"{",
				"\"fhir_comments\":",
				"[",
				"\"identifier comment 1\"",
				",",
				"\"identifier comment 2\"",
				"]",
				"\"use\": \"usual\",", 
				"\"_use\": {", 
				"\"fhir_comments\":",
				"[",
				"\"use comment 1\"",
				",",
				"\"use comment 2\"",
				"]",
				"},",
				"\"type\"" 
		));
		//@formatter:off
		
		encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);
		
		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
				"<Patient xmlns=\"http://hl7.org/fhir\">", 
				"<id value=\"pat1\"/>", 
				"<text>", 
				"<status value=\"generated\"/>", 
				"<div xmlns=\"http://www.w3.org/1999/xhtml\"> ", 
				"<p>Patient Donald DUCK @ Acme Healthcare, Inc. MR = 654321</p> ", 
				"</div>", 
				"</text>",
				" <!--identifier comment 1-->\n",
				" <!--identifier comment 2-->", 
				"<identifier>",
				"<!--use comment 1-->",
				"<!--use comment 2-->", 
				"<use value=\"usual\"/>", 
				"<type>", 
				"<coding>", 
				"<system value=\"http://hl7.org/fhir/v2/0203\"/>", 
				"<code value=\"MR\"/>", 
				"</coding>", 
				"</type>", 
				"<system value=\"urn:oid:0.1.2.3.4.5.6.7\"/>", 
				"<value value=\"654321\"/>", 
				"</identifier>", 
				"<active value=\"true\"/>", 
				"</Patient>" 
		));
		//@formatter:off

	}

	@Test
	public void testParseAndEncodeCommentsOnExtensions() {
		//@formatter:off
		String input = 
				"<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"  <!-- comment 1 -->\n" +
				"  <id value=\"someid\"/>\n" + 
				"  <!-- comment 2 -->\n" +
				"  <extension url=\"urn:patientext:att\">\n" + 
				"    <!-- comment 3 -->\n" +
				"    <valueAttachment>\n" + 
				"      <!-- comment 4 -->\n" +
				"      <contentType value=\"aaaa\"/>\n" + 
				"      <data value=\"AAAA\"/>\n" + 
				"      <!-- comment 5 -->\n" +
				"    </valueAttachment>\n" + 
				"    <!-- comment 6 -->\n" +
				"  </extension>\n" + 
				"  <!-- comment 7 -->\n" +
				"</Patient>";
		
		Patient pat = ourCtx.newXmlParser().parseResource(Patient.class, input);
		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(pat);
		ourLog.info(output);
		
		assertThat(output, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">", 
			"  <!-- comment 1 -->",
			"  <id value=\"someid\"/>", 
			"  <!-- comment 2 -->",
			"  <extension url=\"urn:patientext:att\">", 
			"    <!-- comment 3 -->",
			"    <valueAttachment>", 
			"      <!-- comment 4 -->",
			"      <contentType value=\"aaaa\"/>", 
			"      <data value=\"AAAA\"/>", 
			"      <!-- comment 5 -->",
			"    </valueAttachment>", 
			"    <!-- comment 6 -->",
			"  </extension>", 
			"  <!-- comment 7 -->",
			"</Patient>"
		));
		
		output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pat);
		ourLog.info(output);

		assertThat(output, stringContainsInOrder(
			"{", 
				"  \"resourceType\": \"Patient\",", 
				"  \"id\": \"someid\",", 
				"  \"_id\": {", 
				"    \"fhir_comments\": [ \" comment 1 \" ]", 
				"  },", 
				"  \"extension\": [ {", 
				"    \"fhir_comments\": [ \" comment 2 \", \" comment 7 \" ],", 
				"    \"url\": \"urn:patientext:att\",", 
				"    \"valueAttachment\": {", 
				"      \"fhir_comments\": [ \" comment 3 \", \" comment 6 \" ],", 
				"      \"contentType\": \"aaaa\",", 
				"      \"_contentType\": {", 
				"        \"fhir_comments\": [ \" comment 4 \" ]", 
				"      },", 
				"      \"data\": \"AAAA\",", 
				"      \"_data\": {", 
				"        \"fhir_comments\": [ \" comment 5 \" ]", 
				"      }", 
				"    }", 
				"  } ]", 
				"}"
		));
		
		//@formatter:on
	}


	@Test
	public void testParseAndEncodeNestedExtensions() {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <birthDate value=\"2005-03-04\">\n" + 
			"      <extension url=\"http://my.fancy.extension.url\">\n" + 
			"         <extension url=\"http://my.fancy.extension.url\">\n" + 
			"            <valueString value=\"myNestedValue\"/>\n" + 
			"         </extension>\n" + 
			"      </extension>\n" + 
			"   </birthDate>\n" + 
			"</Patient>";
		//@formatter:on

		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, input);
		DateType bd = p.getBirthDateElement();
		assertEquals("2005-03-04", bd.getValueAsString());

		List<Extension> exts = bd.getExtensionsByUrl("http://my.fancy.extension.url");
		assertEquals(1, exts.size());
		Extension ext = exts.get(0);
		assertEquals(null, ext.getValue());

		exts = ext.getExtensionsByUrl("http://my.fancy.extension.url");
		assertEquals(1, exts.size());
		ext = exts.get(0);
		assertEquals("myNestedValue", ((StringType) ext.getValue()).getValue());

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">", 
			"<birthDate value=\"2005-03-04\">",
			"<extension url=\"http://my.fancy.extension.url\">", 
			"<extension url=\"http://my.fancy.extension.url\">", 
			"<valueString value=\"myNestedValue\"/>",
			"</extension>",
			"</extension>", 
			"</birthDate>", 
			"</Patient>"));
		//@formatter:on

	}

	@Test
	public void testParseBundleNewWithPlaceholderIds() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:oid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

	}

	@Test
	public void testParseBundleNewWithPlaceholderIdsInBase1() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:oid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());
	}

	@Test
	public void testParseBundleNewWithPlaceholderIdsInBase2() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:uuid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

		//@formatter:off
		input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:uuid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

	}

	@Test
	public void testParseBundleOldStyleWithUnknownLinks() throws Exception {
		//@formatter:off
		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <base value=\"http://foo/fhirBase1\"/>\n" + 
			"   <total value=\"1\"/>\n" + 
			"   <link>\n" + 
			"      <relation value=\"foo\"/>\n" + 
			"      <url value=\"http://localhost:52788/Binary?_pretty=true\"/>\n" + 
			"   </link>\n" + 
			"   <entry>\n" + 
			"   <link>\n" + 
			"      <relation value=\"bar\"/>\n" + 
			"      <url value=\"http://localhost:52788/Binary?_pretty=true\"/>\n" + 
			"   </link>\n" + 
			"      <resource>\n" + 
			"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"            <id value=\"1\"/>\n" + 
			"            <meta>\n" +
			"               <versionId value=\"2\"/>\n" +
			"               <lastUpdated value=\"2001-02-22T11:22:33-05:00\"/>\n" +
			"            </meta>\n" + 
			"            <birthDate value=\"2012-01-02\"/>\n" + 
			"         </Patient>\n" + 
			"      </resource>\n" + 
			"   </entry>\n" + 
			"</Bundle>";
		//@formatter:on

		Bundle b = (Bundle) ourCtx.newXmlParser().parseResource(bundle);
		assertEquals(1, b.getEntry().size());

	}

	@Test
	public void testParseBundleOldWithPlaceholderIds() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:oid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = (Bundle) ourCtx.newXmlParser().parseResource(input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getId());

	}

	@Test
	public void testParseBundleWithBinary() {
		// TODO: implement this test, make sure we handle ID and meta correctly in Binary
	}



	@Test
	public void testParseBundleWithResourceId() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">"
				+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"3\"/><lastUpdated value=\"2015-09-11T23:35:43.273-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
				+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"2\"/><lastUpdated value=\"2015-09-11T23:35:42.849-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
				+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/><lastUpdated value=\"2015-09-11T23:35:42.295-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
				+ "</Bundle>\n";
		//@formatter:on

		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/3", bundle.getEntry().get(0).getResource().getIdElement().getValue());
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/2", bundle.getEntry().get(1).getResource().getIdElement().getValue());
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/1", bundle.getEntry().get(2).getResource().getIdElement().getValue());
	}

	/**
	 * see #144 and #146
	 */
	@Test
	@Disabled
	public void testParseContained() {

		FhirContext c = FhirContext.forDstu2_1();
		IParser parser = c.newXmlParser().setPrettyPrint(true);

		Observation o = new Observation();
		o.getCode().setText("obs text");

		Patient p = new Patient();
		p.addName().addFamily("patient family");
		o.getSubject().setResource(p);

		String enc = parser.encodeResourceToString(o);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder(
			"<Observation xmlns=\"http://hl7.org/fhir\">",
			"<contained>",
			"<Patient xmlns=\"http://hl7.org/fhir\">",
			"<id value=\"1\"/>",
			"</contained>",
			"<reference value=\"#1\"/>"
			));
		//@formatter:on

		o = parser.parseResource(Observation.class, enc);
		assertEquals("obs text", o.getCode().getText());

		assertNotNull(o.getSubject().getResource());
		p = (Patient) o.getSubject().getResource();
		assertEquals("patient family", p.getName().get(0).getFamily());
	}

	/**
	 * Thanks to Alexander Kley!
	 */
	@Test
	public void testParseContainedBinaryResource() {
		byte[] bin = new byte[] { 0, 1, 2, 3, 4 };
		final Binary binary = new Binary();
		binary.setContentType("PatientConsent").setContent(bin);

		DocumentManifest manifest = new DocumentManifest();
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setSystem("mySystem").setCode("PatientDocument");
		manifest.setType(cc);
		manifest.setMasterIdentifier(new Identifier().setSystem("mySystem").setValue(UUID.randomUUID().toString()));
		manifest.addContent().setP(new Reference(binary));
		manifest.setStatus(DocumentReferenceStatus.CURRENT);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(manifest);
		ourLog.info(encoded);
		assertThat(encoded, StringContainsInOrder.stringContainsInOrder(Arrays.asList("contained>", "<Binary", "</contained>")));

		DocumentManifest actual = ourCtx.newXmlParser().parseResource(DocumentManifest.class, encoded);
		assertEquals(1, actual.getContained().size());
		assertEquals(1, actual.getContent().size());

		/*
		 * If this fails, it's possibe the DocumentManifest structure is wrong: It should be
		 * 
		 * @Child(name = "p", type = {Attachment.class, ValueSet.class}, order=1, min=1, max=1, modifier=false, summary=true)
		 */
		assertNotNull(((Reference) actual.getContent().get(0).getP()).getResource());
	}

	/**
	 * See #426
	 */
	@Test
	public void testParseExtensionWithIdType() {
		//@formatter:off
		String input = 
				"<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <extension url=\"http://aaa.ch/fhir/Patient#mangedcare\">\n" + 
				"        <extension url=\"http://aaa.ch/fhir/Patient#mangedcare-aaa-id\">\n" + 
				"           <valueId value=\"mc1\"/>\n" + 
				"        </extension>\n" + 
				"    </extension>\n" + 
				"    <identifier>\n" + 
				"        <value value=\"ais111\"/>\n" + 
				"    </identifier>\n" +
				"</Patient>";
		//@formatter:on
		Patient pt = ourCtx.newXmlParser().parseResource(Patient.class, input);

		List<Extension> extList = pt.getExtensionsByUrl("http://aaa.ch/fhir/Patient#mangedcare");
		extList = extList.get(0).getExtensionsByUrl("http://aaa.ch/fhir/Patient#mangedcare-aaa-id");
		Extension ext = extList.get(0);
		IdType value = (IdType) ext.getValue();
		assertEquals("mc1", value.getValueAsString());
	}

	/**
	 * See #426
	 * 
	 * Value type of FOO isn't a valid datatype
	 */
	@Test
	public void testParseExtensionWithInvalidType() {
		//@formatter:off
		String input = 
				"<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <extension url=\"http://aaa.ch/fhir/Patient#mangedcare\">\n" + 
				"        <extension url=\"http://aaa.ch/fhir/Patient#mangedcare-aaa-id\">\n" + 
				"           <valueFOO value=\"mc1\"/>\n" + 
				"        </extension>\n" + 
				"    </extension>\n" + 
				"    <identifier>\n" + 
				"        <value value=\"ais111\"/>\n" + 
				"    </identifier>\n" +
				"</Patient>";
		//@formatter:on
		Patient pt = ourCtx.newXmlParser().parseResource(Patient.class, input);

		List<Extension> extList = pt.getExtensionsByUrl("http://aaa.ch/fhir/Patient#mangedcare");
		extList = extList.get(0).getExtensionsByUrl("http://aaa.ch/fhir/Patient#mangedcare-aaa-id");
		Extension ext = extList.get(0);
		IdType value = (IdType) ext.getValue();
		assertEquals(null, value);
	}

	/**
	 * See #342
	 */
	@Test
	public void testParseInvalid() {
		try {
			ourCtx.newXmlParser().parseResource("FOO");
			fail();
		} catch (DataFormatException e) {
			// good
		}
	}

	/**
	 * See #366
	 */
	@Test()
	public void testParseInvalidBoolean() {
		//@formatter:off
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <active value=\"1\"/>\n" + 
			"</Patient>";
		//@formatter:on

		IParser p = ourCtx.newXmlParser();
		
		try {
			p.parseResource(resource);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [2,4]]: " + Msg.code(1821) + "[element=\"active\"] Invalid attribute value \"1\": Invalid boolean string: '1'", e.getMessage());
		}
		
		LenientErrorHandler errorHandler = new LenientErrorHandler();
		assertEquals(true, errorHandler.isErrorOnInvalidValue());
		errorHandler.setErrorOnInvalidValue(false);
		p.setParserErrorHandler(errorHandler);
	}

	@Test
	public void testParseInvalidTextualNumber() {
		Observation obs = new Observation();
		obs.setValue(new Quantity().setValue(1234));
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
		encoded = encoded.replace("1234", "\"1234\"");
		ourLog.info(encoded);
		ourCtx.newJsonParser().parseResource(encoded);
	}

	/**
	 * See #216
	 */
	@Test
	public void testParseMalformedIdentifierDstu2() {

		// This was changed from 0.5 to 1.0.0

		//@formatter:off
		String out = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <identifier>\n" + 
				"      <type value=\"MRN\"/>\n" + 
				"      <system value=\"SYS\"/>\n" + 
				"      <value value=\"VAL\"/>\n" + 
				"   </identifier>\n" + 
				"</Patient>";
		//@formatter:on

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		IParser p = ourCtx.newXmlParser();
		p.setParserErrorHandler(errorHandler);

		Patient patient = p.parseResource(Patient.class, out);
		assertThat(patient.getIdentifier().get(0).getType().getCoding(), IsEmptyCollection.empty());

		ArgumentCaptor<String> capt = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).unknownAttribute(nullable(IParseLocation.class), capt.capture());

		assertEquals("value", capt.getValue());
	}

	@Test
	public void testParseMetadata() throws Exception {
		//@formatter:off
		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <total value=\"1\"/>\n" + 
			"   <link>\n" + 
			"      <relation value=\"self\"/>\n" + 
			"      <url value=\"http://localhost:52788/Binary?_pretty=true\"/>\n" + 
			"   </link>\n" + 
			"   <entry>\n" + 
			"      <fullUrl value=\"http://foo/fhirBase2/Patient/1/_history/2\"/>\n" + 
			"      <resource>\n" + 
			"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"            <id value=\"1\"/>\n" + 
			"            <meta>\n" +
			"               <versionId value=\"2\"/>\n" +
			"               <lastUpdated value=\"2001-02-22T09:22:33-07:00\"/>\n" +
			"            </meta>\n" + 
			"            <birthDate value=\"2012-01-02\"/>\n" + 
			"         </Patient>\n" + 
			"      </resource>\n" + 
			"      <search>\n" +
			"         <mode value=\"match\"/>\n" +
			"         <score value=\"0.123\"/>\n" +
			"      </search>\n" +
			"      <request>\n" +
			"         <method value=\"POST\"/>\n" +
			"         <url value=\"http://foo/Patient?identifier=value\"/>\n" +
			"      </request>\n" +
			"   </entry>\n" + 
			"</Bundle>";
		//@formatter:on

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, bundle);
		assertEquals(1, b.getEntry().size());

		BundleEntryComponent entry = b.getEntry().get(0);
		Patient pt = (Patient) entry.getResource();
		assertEquals("http://foo/fhirBase2/Patient/1/_history/2", pt.getIdElement().getValue());
		assertEquals("2012-01-02", pt.getBirthDateElement().getValueAsString());
		assertEquals("0.123", entry.getSearch().getScore().toString());
		assertEquals("match", entry.getSearch().getMode().toCode());
		assertEquals("POST", entry.getRequest().getMethod().toCode());
		assertEquals("http://foo/Patient?identifier=value", entry.getRequest().getUrl());
		assertEquals("2001-02-22T09:22:33-07:00", pt.getMeta().getLastUpdatedElement().getValueAsString());

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String reEncoded = p.encodeResourceToString(b);
		ourLog.info(reEncoded);

		compareXml(bundle, reEncoded);

	}

	@Test
	public void testParseMetaUpdatedDate() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <id value=\"e2ee823b-ee4d-472d-b79d-495c23f16b99\"/>\n" + 
				"   <meta>\n" + 
				"      <lastUpdated value=\"2015-06-22T15:48:57.554-04:00\"/>\n" + 
				"   </meta>\n" + 
				"   <type value=\"searchset\"/>\n" + 
				"   <base value=\"http://localhost:58109/fhir/context\"/>\n" + 
				"   <total value=\"0\"/>\n" + 
				"   <link>\n" + 
				"      <relation value=\"self\"/>\n" + 
				"      <url value=\"http://localhost:58109/fhir/context/Patient?_pretty=true\"/>\n" + 
				"   </link>\n" + 
				"</Bundle>";
		//@formatter:on
		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, input);

		InstantType updated = b.getMeta().getLastUpdatedElement();
		assertEquals("2015-06-22T15:48:57.554-04:00", updated.getValueAsString());

	}

	// TODO: this should work
	@Test
	@Disabled
	public void testParseNarrative() throws Exception {
		//@formatter:off
		String htmlNoNs = "<div>AAA<b>BBB</b>CCC</div>";
		String htmlNs = htmlNoNs.replace("<div>", "<div xmlns=\"http://www.w3.org/1999/xhtml\">"); 
		String res= "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <id value=\"1333\"/>\n" + 
				"   <text>\n" + 
				"      " + htmlNs + "\n" +
				"   </text>\n" + 
				"</Patient>";
		//@formatter:on

		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, res);
		assertEquals(htmlNs, p.getText().getDiv().getValueAsString());
	}

	@Test
	public void testParseNestedExtensionsInvalid() {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <birthDate value=\"2005-03-04\">\n" +
			"      <extension url=\"http://my.fancy.extension.url\">\n" + 
			"         <valueString value=\"myvalue\"/>\n" +
			"         <extension url=\"http://my.fancy.extension.url\">\n" + 
			"            <valueString value=\"myNestedValue\"/>\n" + 
			"         </extension>\n" + 
			"      </extension>\n" + 
			"   </birthDate>\n" + 
			"</Patient>";
		//@formatter:on

		try {
			ourCtx.newXmlParser().parseResource(Patient.class, input);
			fail();
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), containsString("Extension (URL='http://my.fancy.extension.url') must not have both a value and other contained extensions"));
		}
	}

	/**
	 * See #163
	 */
	@Test
	public void testParseResourceType() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);

		// Patient
		Patient patient = new Patient();
		String patientId = UUID.randomUUID().toString();
		patient.setId(new IdType("Patient", patientId));
		patient.addName().addGiven("John").addFamily("Smith");
		patient.setGender(AdministrativeGender.MALE);
		patient.setBirthDateElement(new DateType("1987-04-16"));

		// Bundle
		Bundle bundle = new Bundle();
		bundle.setType(BundleType.COLLECTION);
		bundle.addEntry().setResource(patient);

		String bundleText = xmlParser.encodeResourceToString(bundle);
		ourLog.info(bundleText);

		Bundle reincarnatedBundle = xmlParser.parseResource(Bundle.class, bundleText);
		Patient reincarnatedPatient = (Patient) reincarnatedBundle.getEntry().get(0).getResource();

		assertEquals("Patient", patient.getIdElement().getResourceType());
		assertEquals("Patient", reincarnatedPatient.getIdElement().getResourceType());
	}

	/**
	 * See #344
	 */
	@Test
	public void testParserIsCaseSensitive() {
		Observation obs = new Observation();
		SampledData data = new SampledData();
		data.setData("1 2 3");
		data.setOrigin((SimpleQuantity) new SimpleQuantity().setValue(0L));
		data.setPeriod(1000L);
		obs.setValue(data);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true).setParserErrorHandler(new StrictErrorHandler());
		String encoded = p.encodeResourceToString(obs);
		ourLog.info(encoded);

		p.parseResource(encoded);

		try {
			p.parseResource(encoded.replace("Observation", "observation"));
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [1,1]]: " + Msg.code(1815) + "Unknown resource type 'observation': Resource names are case sensitive, found similar name: 'Observation'",
					e.getMessage());
		}

		try {
			p.parseResource(encoded.replace("valueSampledData", "valueSampleddata"));
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [2,4]]: " + Msg.code(1825) + "Unknown element 'valueSampleddata' found during parse", e.getMessage());
		}
	}

	/**
	 * See #339
	 * 
	 * https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Processing
	 */
	@Test
	public void testXxe() {
		//@formatter:off
		String input =
			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + 
			"<!DOCTYPE foo [  " + 
			"<!ELEMENT foo ANY >" + 
			"<!ENTITY xxe SYSTEM \"file:///etc/passwd\" >]>" +
			"<Patient xmlns=\"http://hl7.org/fhir\">" +
				"<text>" + 
					"<div xmlns=\"http://www.w3.org/1999/xhtml\">TEXT &xxe; TEXT</div>" + 
				"</text>" +
				"<address>" + 
					"<line value=\"FOO\"/>" + 
				"</address>" +
			"</Patient>";
		//@formatter:on

		ourLog.info(input);

		try {
			ourCtx.newXmlParser().parseResource(Patient.class, input);
			fail();
		} catch (DataFormatException e) {
			assertThat(e.toString(), containsString("Undeclared general entity"));
		}

	}

	@Test
	public void testBaseUrlFooResourceCorrectlySerializedInExtensionValueReference() {
		String refVal = "http://my.org/FooBar";

		Patient fhirPat = new Patient();
		fhirPat.addExtension().setUrl("x1").setValue(new Reference(refVal));

		IParser parser = ourCtx.newXmlParser();

		String output = parser.encodeResourceToString(fhirPat);
		System.out.println("output: " + output);

		// Deserialize then check that valueReference value is still correct
		fhirPat = parser.parseResource(Patient.class, output);

		List<Extension> extlst = fhirPat.getExtensionsByUrl("x1");
		assertEquals(1, extlst.size());
		assertEquals(refVal, ((Reference) extlst.get(0).getValue()).getReference());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static void compareXml(String content, String reEncoded) {
		Diff d = DiffBuilder.compare(Input.fromString(content))
				.withTest(Input.fromString(reEncoded))
				.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
				.checkForSimilar()
				.ignoreWhitespace()
				.ignoreComments()
				.withComparisonController(ComparisonControllers.Default)
				.build();

		assertTrue(!d.hasDifferences(), d.toString());
	}

	@ResourceDef(name = "Patient")
	public static class TestPatientFor327 extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(name = "testCondition")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "testCondition", definedLocally = true, isModifier = false)
		private List<Reference> testConditions = null;

		public List<Reference> getConditions() {
			return this.testConditions;
		}

		public void setCondition(List<Reference> ref) {
			this.testConditions = ref;
		}
	}

}
