package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu2.composite.AnnotationDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContainedDt;
import ca.uhn.fhir.model.dstu2.composite.DurationDt;
import ca.uhn.fhir.model.dstu2.composite.ElementDefinitionDt;
import ca.uhn.fhir.model.dstu2.composite.ElementDefinitionDt.Binding;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.DataElement;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.DiagnosticReportStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.MarkdownDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class XmlParserDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserDstu2Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu2();

	@BeforeEach
	public void before() {
		if (ourCtx == null) {
			ourCtx = FhirContext.forDstu2();
		}
	}

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnFhirContext() {
		try {
			String tmp = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><fullUrl value=\"http://lalaland.org/patient/pat1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patxuzos\"/></Patient></resource></entry></Bundle>";
			ourCtx.getParserOptions().setOverrideResourceIdWithBundleEntryFullUrl(false);
			ca.uhn.fhir.model.dstu2.resource.Bundle bundle = (ca.uhn.fhir.model.dstu2.resource.Bundle) ourCtx.newXmlParser().parseResource(tmp);
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
			ca.uhn.fhir.model.dstu2.resource.Bundle bundle = (ca.uhn.fhir.model.dstu2.resource.Bundle) ourCtx.newXmlParser().setOverrideResourceIdWithBundleEntryFullUrl(false).parseResource(tmp);
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
		assertEquals(1, parsed.getAllUndeclaredExtensions().size());
		assertEquals(null, parsed.getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", parsed.getAllUndeclaredExtensions().get(0).getValueAsPrimitive().getValueAsString());

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
		assertEquals(1, parsed.getAllUndeclaredExtensions().size());
		assertEquals(null, parsed.getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", parsed.getAllUndeclaredExtensions().get(0).getValueAsPrimitive().getValueAsString());

		try {
			parser = ourCtx.newXmlParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(input);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1822) + "Resource is missing required element 'url' in parent element 'modifierExtension'", e.getCause().getMessage());
		}

	}

	/**
	 * If a contained resource refers to a contained resource that comes after it, it should still be successfully
	 * woven together.
	 */
	@Test
	public void testParseWovenContainedResources() throws IOException {
		String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_woven_obs.xml"), StandardCharsets.UTF_8);

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new StrictErrorHandler());
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, string);

		DiagnosticReport resource = (DiagnosticReport) bundle.getEntry().get(0).getResource();
		Observation obs = (Observation) resource.getResult().get(1).getResource();
		assertEquals("#2", obs.getId().getValue());
		ResourceReferenceDt performerFirstRep = obs.getPerformer().get(0);
		Practitioner performer = (Practitioner) performerFirstRep.getResource();
		assertEquals("#3", performer.getId().getValue());
	}

	@Test
	public void testContainedResourceWithNoId() throws IOException {
		try {
			String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_contained_with_no_id.xml"), StandardCharsets.UTF_8);

			IParser parser = ourCtx.newXmlParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, string);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [49,11]]: " + Msg.code(1819) + "Resource has contained child resource with no ID", e.getMessage());
		}
	}

	@Test
	public void testIdOnComposite() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"   <name id=\"foo\">\n" +
			"     <family value=\"hello\"/>" +
			"   </name>" +
			"   <active value=\"true\"/>" +
			"</Patient>";

		IParser p = ourCtx.newXmlParser();

		Patient patient = p.parseResource(Patient.class, input);
		assertTrue(patient.getActive());
		assertEquals("foo", patient.getNameFirstRep().getElementSpecificId());

	}


	@Test
	public void testSetDontEncodeResourcesWithMetaSubPath() {
		Patient p = new Patient();
		ResourceMetadataKeyEnum.VERSION.put(p, "BBB");
		p.setId("AAA");
		p.getMeta().setVersionId("BBB");
		p.getMeta().setLastUpdated(new InstantDt("2011-01-01T00:00:00.000Z").getValue());
		p.getMeta().addTag().setSystem("SYS").setCode("CODE");
		p.addName().addFamily("FAMILY");

		IParser parser = ourCtx.newXmlParser();
		parser.setDontEncodeElements(Sets.newHashSet("id", "*.meta.versionId", "*.meta.lastUpdated"));
		String output = parser.encodeResourceToString(p);

		assertThat(output, containsString("FAMILY"));
		assertThat(output, containsString("SYS"));
		assertThat(output, containsString("CODE"));
		assertThat(output, not(containsString("AAA")));
		assertThat(output, not(containsString("BBB")));
		assertThat(output, not(containsString("2011")));
	}


	@Test()
	public void testContainedResourceWithNoIdLenient() throws IOException {
		String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_contained_with_no_id.xml"), StandardCharsets.UTF_8);

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, string);
	}

	@Test
	public void testParseWithInvalidLocalRef() throws IOException {
		try {
			String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_invalid_contained_ref.xml"), StandardCharsets.UTF_8);

			IParser parser = ourCtx.newXmlParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, string);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [47,7]]: " + Msg.code(1826) + "Resource has invalid reference: #1", e.getMessage());
		}
	}

	@Test()
	public void testParseWithInvalidLocalRefLenient() throws IOException {
		String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_invalid_contained_ref.xml"), StandardCharsets.UTF_8);

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, string);
	}

	@Test
	public void testBundleWithBinary() {
		//@formatter:off
		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"   <meta/>\n" +
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
		assertArrayEquals(new byte[]{1, 2, 3, 4}, bin.getContent());

	}

	@Test
	public void testChoiceTypeWithProfiledType() {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"        <extension url=\"http://example.com\">\n" +
			"          <valueMarkdown value=\"THIS IS MARKDOWN\"/>\n" +
			"        </extension>\n" +
			"</Patient>";
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, input);
		assertEquals(1, parsed.getUndeclaredExtensions().size());
		ExtensionDt ext = parsed.getUndeclaredExtensions().get(0);
		assertEquals("http://example.com", ext.getUrl());
		assertEquals("THIS IS MARKDOWN", ((MarkdownDt) ext.getValue()).getValue());

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		assertThat(encoded, containsString("<valueMarkdown value=\"THIS IS MARKDOWN\"/>"));
	}

	@Test
	public void testChoiceTypeWithProfiledType2() {
		Parameters par = new Parameters();
		par.addParameter().setValue((StringDt) new StringDt().setValue("ST"));
		par.addParameter().setValue((MarkdownDt) new MarkdownDt().setValue("MD"));

		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(par);
		ourLog.info(str);

		assertThat(str, stringContainsInOrder("<valueString value=\"ST\"/>", "<valueMarkdown value=\"MD\"/>"));

		par = ourCtx.newXmlParser().parseResource(Parameters.class, str);
		assertEquals(2, par.getParameter().size());
		assertEquals(StringDt.class, par.getParameter().get(0).getValue().getClass());
		assertEquals(MarkdownDt.class, par.getParameter().get(1).getValue().getClass());
	}

	@Test
	public void testContainedResourceInExtensionUndeclared() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		p.addUndeclaredExtension(new ExtensionDt(false, "urn:foo", new ResourceReferenceDt(o)));

		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(str);

		p = ourCtx.newXmlParser().parseResource(Patient.class, str);
		assertEquals("PATIENT", p.getName().get(0).getFamily().get(0).getValue());

		List<ExtensionDt> exts = p.getUndeclaredExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		ResourceReferenceDt rr = (ResourceReferenceDt) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());
	}

	/**
	 * See #308
	 */
	@Test
	public void testDeclaredExtensionsDontProduceWarning() {
		ReportObservation obs = new ReportObservation();
		obs.setReadOnly(true);

		IParser p = ourCtx.newJsonParser();
		p.setParserErrorHandler(mock(IParserErrorHandler.class, new ThrowsException(new IllegalStateException())));

		String encoded = p.encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = p.parseResource(ReportObservation.class, encoded);
		assertEquals(true, obs.getReadOnly().getValue().booleanValue());
	}

	@Test
	public void testDuration() {
		Encounter enc = new Encounter();
		DurationDt duration = new DurationDt();
		duration.setUnit("day").setValue(123L);
		enc.setLength(duration);

		String str = ourCtx.newXmlParser().encodeResourceToString(enc);
		ourLog.info(str);

		assertThat(str, not(containsString("meta")));
		assertThat(str, containsString("<length><value value=\"123\"/><unit value=\"day\"/></length>"));
	}

	@Test
	public void testEncodeAndParseBundleWithoutResourceIds() {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system").setValue("someval");

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(org);
		String str = ourCtx.newXmlParser().encodeResourceToString(bundle);
		ourLog.info(str);

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, str);
		assertThat(parsed.getEntry().get(0).getResource().getId().getValue(), emptyOrNullString());
		assertTrue(parsed.getEntry().get(0).getResource().getId().isEmpty());
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

		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
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

		bundle = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, encoded);
		pt = (Patient) bundle.getEntry().get(0).getResource();
		org = (Organization) bundle.getEntry().get(1).getResource();

		assertEquals("Organization/orgid", org.getId().getValue());
		assertEquals("Organization/orgid", pt.getManagingOrganization().getReference().getValue());
		assertSame(org, pt.getManagingOrganization().getResource());
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
		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
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
		assertEquals("#1", patient.getManagingOrganization().getReference().getValue());

		assertNotNull(patient.getManagingOrganization().getResource());
		org = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("#1", org.getId().getValue());
		assertEquals("Contained Test Organization", org.getName());

		// And re-encode a second time
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared
		patient.getContained().getContainedResources().clear();
		patient.getManagingOrganization().setReference((String) null);
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared and a manually set local ID
		patient.getContained().getContainedResources().clear();
		patient.getManagingOrganization().setReference((String) null);
		patient.getManagingOrganization().getResource().setId(("#333"));
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"333\"/>", "</Organization", "</contained>", "<reference value=\"#333\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));

	}

	@Test
	public void testEncodeAndParseContainedCustomTypes() {
		ourCtx = FhirContext.forDstu2();
		ourCtx.setDefaultTypeForProfile(CustomObservationDstu2.PROFILE, CustomObservationDstu2.class);
		ourCtx.setDefaultTypeForProfile(CustomDiagnosticReportDstu2.PROFILE, CustomDiagnosticReportDstu2.class);

		CustomObservationDstu2 obs = new CustomObservationDstu2();
		obs.setStatus(ObservationStatusEnum.FINAL);

		CustomDiagnosticReportDstu2 dr = new CustomDiagnosticReportDstu2();
		dr.setStatus(DiagnosticReportStatusEnum.FINAL);
		dr.addResult().setResource(obs);

		IParser parser = ourCtx.newXmlParser();
		parser.setPrettyPrint(true);

		String output = parser.encodeResourceToString(dr);
		ourLog.info(output);

		//@formatter:off
		assertThat(output, stringContainsInOrder(
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

		dr = (CustomDiagnosticReportDstu2) parser.parseResource(output);
		assertEquals(DiagnosticReportStatusEnum.FINAL, dr.getStatusElement().getValueAsEnum());

		assertEquals("#1", dr.getResult().get(0).getReference().getValueAsString());
		obs = (CustomObservationDstu2) dr.getResult().get(0).getResource();
		assertEquals(ObservationStatusEnum.FINAL, obs.getStatusElement().getValueAsEnum());

		ourCtx = null;
	}

	/**
	 * See #448
	 */
	@Test
	public void testParseWithMultipleProfiles() {
		ourCtx = FhirContext.forDstu2();
		ourCtx.setDefaultTypeForProfile(CustomObservationDstu2.PROFILE, CustomObservationDstu2.class);
		ourCtx.setDefaultTypeForProfile(CustomDiagnosticReportDstu2.PROFILE, CustomDiagnosticReportDstu2.class);

		//@formatter:off
		String input =
			"<DiagnosticReport xmlns=\"http://hl7.org/fhir\">" +
				"<meta>" +
				"<profile value=\"" + CustomDiagnosticReportDstu2.PROFILE + "\"/>" +
				"<profile value=\"http://custom_DiagnosticReport2\"/>" +
				"<profile value=\"http://custom_DiagnosticReport3\"/>" +
				"</meta>" +
				"<status value=\"final\"/>" +
				"</DiagnosticReport>";
		//@formatter:on

		IParser parser = ourCtx.newXmlParser();
		CustomDiagnosticReportDstu2 dr = (CustomDiagnosticReportDstu2) parser.parseResource(input);
		assertEquals(DiagnosticReportStatusEnum.FINAL, dr.getStatusElement().getValueAsEnum());

		List<IdDt> profiles = ResourceMetadataKeyEnum.PROFILES.get(dr);
		assertEquals(3, profiles.size());

		ourCtx = null;
	}

	@Test
	public void testEncodeAndParseContainedNonCustomTypes() {
		ourCtx = FhirContext.forDstu2();

		Observation obs = new Observation();
		obs.setStatus(ObservationStatusEnum.FINAL);

		DiagnosticReport dr = new DiagnosticReport();
		dr.setStatus(DiagnosticReportStatusEnum.FINAL);
		dr.addResult().setResource(obs);

		IParser parser = ourCtx.newXmlParser();
		parser.setPrettyPrint(true);

		String output = parser.encodeResourceToString(dr);
		ourLog.info(output);

		//@formatter:off
		assertThat(output, stringContainsInOrder(
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
		assertEquals(DiagnosticReportStatusEnum.FINAL, dr.getStatusElement().getValueAsEnum());

		assertEquals("#1", dr.getResult().get(0).getReference().getValueAsString());
		obs = (Observation) dr.getResult().get(0).getResource();
		assertEquals(ObservationStatusEnum.FINAL, obs.getStatusElement().getValueAsEnum());

		ourCtx = null;
	}

	/**
	 * See #350
	 */
	@Test
	public void testEncodeAndParseDateTimeDt() {
		FhirContext ctx = FhirContext.forDstu2();
		ctx.setDefaultTypeForProfile("http://hl7.org/fhir/profiles/custom-condition", CustomCondition.class);
		IParser parser = ctx.newXmlParser();

		CustomCondition condition = new CustomCondition();
		condition.setOurAbatement(new DateTimeDt(new Date()));

		String conditionXml = parser.encodeResourceToString(condition);

		ourLog.info(conditionXml);
		assertThat(conditionXml, containsString("abatementDateTime"));

		CustomCondition parsedCondition = (CustomCondition) parser.parseResource(conditionXml);
		assertNotNull(parsedCondition.getOurAbatement());
	}

	@Test
	public void testEncodeAndParseExtensionOnResourceReference() {
		DataElement de = new DataElement();
		Binding b = de.addElement().getBinding();
		b.setDescription("BINDING");

		Organization o = new Organization();
		o.setName("ORG");
		b.addUndeclaredExtension(new ExtensionDt(false, "urn:foo", new ResourceReferenceDt(o)));

		String str = ourCtx.newXmlParser().encodeResourceToString(de);
		ourLog.info(str);

		de = ourCtx.newXmlParser().parseResource(DataElement.class, str);
		b = de.getElement().get(0).getBinding();
		assertEquals("BINDING", b.getDescription());

		List<ExtensionDt> exts = b.getUndeclaredExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		ResourceReferenceDt rr = (ResourceReferenceDt) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());

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
	 * See #216
	 */
	@Test
	public void testEncodeAndParseIdentifierDstu2() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("SYS").setValue("VAL").setType(IdentifierTypeCodesEnum.MR);

		String out = xmlParser.encodeResourceToString(patient);
		ourLog.info(out);

		//@formatter:off
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
		//@formatter:on

		patient = ourCtx.newXmlParser().parseResource(Patient.class, out);
		assertThat(patient.getIdentifier().get(0).getType().getValueAsEnum(), contains(IdentifierTypeCodesEnum.MR));
		assertEquals("http://hl7.org/fhir/v2/0203", patient.getIdentifier().get(0).getType().getCoding().get(0).getSystem());
		assertEquals("MR", patient.getIdentifier().get(0).getType().getCoding().get(0).getCode());
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

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("<Patient xmlns=\"http://hl7.org/fhir\">",
			"<meta>",
			"<profile value=\"http://foo/Profile1\"/>",
			"<profile value=\"http://foo/Profile2\"/>",
			"<tag>",
			"<system value=\"scheme1\"/>",
			"<code value=\"term1\"/>",
			"<display value=\"label1\"/>",
			"</tag>",
			"<tag>",
			"<system value=\"scheme2\"/>",
			"<code value=\"term2\"/>",
			"<display value=\"label2\"/>",
			"</tag>",
			"</meta>",
			"<name>",
			"<family value=\"FAMILY\"/>",
			"</name>",
			"</Patient>"));
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
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
	public void testEncodeAndParseMetaProfiles() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		TagList tagList = new TagList();
		tagList.addTag("scheme1", "term1", "label1");
		tagList.addTag("scheme2", "term2", "label2");
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tagList);

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("<Patient xmlns=\"http://hl7.org/fhir\">",
			"<meta>",
			"<tag>",
			"<system value=\"scheme1\"/>",
			"<code value=\"term1\"/>",
			"<display value=\"label1\"/>",
			"</tag>",
			"<tag>",
			"<system value=\"scheme2\"/>",
			"<code value=\"term2\"/>",
			"<display value=\"label2\"/>",
			"</tag>",
			"</meta>",
			"<name>",
			"<family value=\"FAMILY\"/>",
			"</name>",
			"</Patient>"));
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		List<IdDt> gotLabels = ResourceMetadataKeyEnum.PROFILES.get(parsed);
		assertNull(gotLabels);

		tagList = ResourceMetadataKeyEnum.TAG_LIST.get(parsed);
		assertEquals(2, tagList.size());

		assertEquals(new Tag("scheme1", "term1", "label1"), tagList.get(0));
		assertEquals(new Tag("scheme2", "term2", "label2"), tagList.get(1));
	}

	/**
	 * See #336
	 */
	@Test
	public void testEncodeAndParseNullPrimitiveWithExtensions() {

		Patient p = new Patient();
		p.setId("patid");
		HumanNameDt name = p.addName();
		name.addFamily().setValue(null).addUndeclaredExtension(new ExtensionDt(false, "http://foo", new StringDt("FOOEXT0")));
		name.getFamily().get(0).setElementSpecificId("f0");
		name.addFamily().setValue("V1").addUndeclaredExtension((ExtensionDt) new ExtensionDt(false, "http://foo", new StringDt("FOOEXT1")));
		name.getFamily().get(1).setElementSpecificId("f1");
		name.getFamily().get(1).getUndeclaredExtensions().get(0).setElementSpecificId("ext1id");
		name.addFamily(); // this one shouldn't get encoded
		name.addFamily().setValue(null).addUndeclaredExtension(new ExtensionDt(false, "http://foo", new StringDt("FOOEXT3")));
		name.setElementSpecificId("nameid");

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);

		output = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(p);
		String expected = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patid\"/><name id=\"nameid\"><family id=\"f0\"><extension url=\"http://foo\"><valueString value=\"FOOEXT0\"/></extension></family><family id=\"f1\" value=\"V1\"><extension id=\"ext1id\" url=\"http://foo\"><valueString value=\"FOOEXT1\"/></extension></family><family><extension url=\"http://foo\"><valueString value=\"FOOEXT3\"/></extension></family></name></Patient>";
		assertEquals(expected, output);

		p = ourCtx.newXmlParser().parseResource(Patient.class, output);
		assertEquals("patid", p.getIdElement().getIdPart());

		name = p.getName().get(0);
		assertEquals("nameid", name.getElementSpecificId());
		assertEquals(3, name.getFamily().size());

		assertEquals(null, name.getFamily().get(0).getValue());
		assertEquals("V1", name.getFamily().get(1).getValue());
		assertEquals(null, name.getFamily().get(2).getValue());

		assertEquals("f0", name.getFamily().get(0).getElementSpecificId());
		assertEquals("f1", name.getFamily().get(1).getElementSpecificId());
		assertEquals(null, name.getFamily().get(2).getElementSpecificId());

		assertEquals(1, name.getFamily().get(0).getAllUndeclaredExtensions().size());
		assertEquals("http://foo", name.getFamily().get(0).getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("FOOEXT0", ((StringDt) name.getFamily().get(0).getAllUndeclaredExtensions().get(0).getValue()).getValue());
		assertEquals(null, name.getFamily().get(0).getAllUndeclaredExtensions().get(0).getElementSpecificId());

		assertEquals(1, name.getFamily().get(1).getAllUndeclaredExtensions().size());
		assertEquals("http://foo", name.getFamily().get(1).getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("FOOEXT1", ((StringDt) name.getFamily().get(1).getAllUndeclaredExtensions().get(0).getValue()).getValue());
		assertEquals("ext1id", name.getFamily().get(1).getAllUndeclaredExtensions().get(0).getElementSpecificId());

		assertEquals(1, name.getFamily().get(2).getAllUndeclaredExtensions().size());
		assertEquals("http://foo", name.getFamily().get(2).getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("FOOEXT3", ((StringDt) name.getFamily().get(2).getAllUndeclaredExtensions().get(0).getValue()).getValue());
		assertEquals(null, name.getFamily().get(2).getAllUndeclaredExtensions().get(0).getElementSpecificId());

	}

	/**
	 * Test for #233
	 */
	@Test
	public void testEncodeAndParseProfiledDatatype() {
		MedicationOrder mo = new MedicationOrder();
		mo.addDosageInstruction().getTiming().getRepeat().setBounds(new DurationDt().setCode("code"));
		String out = ourCtx.newXmlParser().encodeResourceToString(mo);
		ourLog.info(out);
		assertThat(out, containsString("</boundsQuantity>"));

		mo = ourCtx.newXmlParser().parseResource(MedicationOrder.class, out);
		DurationDt duration = (DurationDt) mo.getDosageInstruction().get(0).getTiming().getRepeat().getBounds();
		assertEquals("code", duration.getCode());
	}

	/**
	 * See #216 - Profiled datatypes should use their unprofiled parent type as the choice[x] name
	 * <p>
	 * Disabled because we reverted this change after a conversation with Grahame
	 */
	@Test
	public void testEncodeAndParseProfiledDatatypeChoice() throws Exception {
		IParser xmlParser = ourCtx.newXmlParser();

		String input = IOUtils.toString(XmlParser.class.getResourceAsStream("/medicationstatement_invalidelement.xml"), StandardCharsets.UTF_8);
		MedicationStatement ms = xmlParser.parseResource(MedicationStatement.class, input);
		SimpleQuantityDt q = (SimpleQuantityDt) ms.getDosage().get(0).getQuantity();
		assertEquals("1", q.getValueElement().getValueAsString());

		String output = xmlParser.encodeResourceToString(ms);
		assertThat(output, containsString("<quantityQuantity><value value=\"1\"/></quantityQuantity>"));
	}

	@Test
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<BaseCodingDt> labels = new ArrayList<BaseCodingDt>();
		labels.add(new CodingDt().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setUserSelected(true).setVersion("VERSION1"));
		labels.add(new CodingDt().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setUserSelected(false).setVersion("VERSION2"));

		ResourceMetadataKeyEnum.SECURITY_LABELS.put(p, labels);

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
			"<userSelected value=\"true\"/>",
			"</security>",
			"<security>",
			"<system value=\"SYSTEM2\"/>",
			"<version value=\"VERSION2\"/>",
			"<code value=\"CODE2\"/>",
			"<display value=\"DISPLAY2\"/>",
			"<userSelected value=\"false\"/>",
			"</security>",
			"</meta>",
			"<name>",
			"<family value=\"FAMILY\"/>",
			"</name>",
			"</Patient>"));
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		List<BaseCodingDt> gotLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(parsed);

		assertEquals(2, gotLabels.size());

		CodingDt label = (CodingDt) gotLabels.get(0);
		assertEquals("SYSTEM1", label.getSystem());
		assertEquals("CODE1", label.getCode());
		assertEquals("DISPLAY1", label.getDisplay());
		assertEquals(true, label.getUserSelected());
		assertEquals("VERSION1", label.getVersion());

		label = (CodingDt) gotLabels.get(1);
		assertEquals("SYSTEM2", label.getSystem());
		assertEquals("CODE2", label.getCode());
		assertEquals("DISPLAY2", label.getDisplay());
		assertEquals(false, label.getUserSelected());
		assertEquals("VERSION2", label.getVersion());
	}

	/**
	 * https://chat.fhir.org/#narrow/stream/implementers/topic/fullUrl.20and.20MessageHeader.2Eid
	 */
	@Test
	public void testEncodeAndParseUuid() {

		Patient p = new Patient();
		p.addName().addFamily("FAMILY");
		p.setId("urn:uuid:4f08cf3d-9f41-41bb-9e10-6e34c5b8f602");

		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		Entry be = b.addEntry();
		be.setResource(p);
		be.getRequest().setUrl(p.getId().getValue());

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(output);

		ca.uhn.fhir.model.dstu2.resource.Bundle parsedBundle = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, output);
		Entry parsedEntry = parsedBundle.getEntry().get(0);

		assertEquals("urn:uuid:4f08cf3d-9f41-41bb-9e10-6e34c5b8f602", parsedEntry.getRequest().getUrl());
		assertEquals("urn:uuid:4f08cf3d-9f41-41bb-9e10-6e34c5b8f602", parsedEntry.getResource().getId().getValue());
	}

	/**
	 * See #103
	 */
	@Test
	public void testEncodeAndReEncodeContainedJson() {
		Composition comp = new Composition();
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section0_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section1_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section2_Allergy0")));

		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);

		String string = parser.encodeResourceToString(comp);
		ourLog.info(string);

		Composition parsed = parser.parseResource(Composition.class, string);
		parsed.getSection().remove(0);

		string = parser.encodeResourceToString(parsed);
		ourLog.info(string);

		parsed = parser.parseResource(Composition.class, string);
		assertEquals(3, parsed.getContained().getContainedResources().size());
	}

	/**
	 * See #103
	 */
	@Test
	public void testEncodeAndReEncodeContainedXml() {
		Composition comp = new Composition();
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section0_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section1_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section2_Allergy0")));

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);

		String string = parser.encodeResourceToString(comp);
		ourLog.info(string);

		Composition parsed = parser.parseResource(Composition.class, string);
		parsed.getSection().remove(0);

		string = parser.encodeResourceToString(parsed);
		ourLog.info(string);

		parsed = parser.parseResource(Composition.class, string);
		assertEquals(3, parsed.getContained().getContainedResources().size());
	}

	@Test
	public void testEncodeBinaryWithNoContentType() {
		Binary b = new Binary();
		b.setContent(new byte[]{1, 2, 3, 4});

		String output = ourCtx.newXmlParser().encodeResourceToString(b);
		ourLog.info(output);

		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"><content value=\"AQIDBA==\"/></Binary>", output);
	}

	@Test
	public void testEncodeBundleOldStyleContainingResourceWithUuidBase() {
		Patient p = new Patient();
		p.setId(IdDt.newRandomUuid());
		p.addName().addFamily("PATIENT");

		Bundle b = new Bundle();
		b.addEntry().setFullUrl(p.getId().getValue()).setResource(p);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder("<Bundle", "<entry>", "<fullUrl value=\"" + p.getId().getValue() + "\"/>", "<Patient"));
		assertThat(encoded, not(containsString("<id value=\"" + p.getId().getIdPart() + "\"/>")));
	}

	@Test
	public void testEncodeBundleOldStyleContainingResourceWithUuidBaseBundleBaseIsSet() {
		Patient p = new Patient();
		p.setId(IdDt.newRandomUuid());
		p.addName().addFamily("PATIENT");

		Bundle b = new Bundle();
		b.getLinkOrCreate("base").setUrl("urn:uuid:");
		b.addEntry().setResource(p);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);
		// Base element has been removed!
		assertThat(encoded, not(stringContainsInOrder("<Bundle", "<entry>", "<base value=\"", "<Patient", "<id value=")));
	}

	@Test
	public void testEncodeBundleWithContained() {
		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addResult().setResource(new Observation().setCode(new CodeableConceptDt().setText("Sharp1")).setId("#1"));
		rpt.addResult().setResource(new Observation().setCode(new CodeableConceptDt().setText("Uuid1")).setId("urn:uuid:UUID1"));

		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		b.addEntry().setResource(rpt);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder("<DiagnosticReport", "<contained", "<Observation", "<text value=\"Sharp1\"", "</DiagnosticReport"));
		assertThat(encoded, not(stringContainsInOrder("<DiagnosticReport", "<contained", "<Observation", "<contained", "<Observation", "</DiagnosticReport")));
	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResources() {

		MedicationOrder medicationPrescript = new MedicationOrder();

		String medId = "123";
		CodeableConceptDt codeDt = new CodeableConceptDt("urn:sys", "code1");

		// Adding medication to Contained.
		Medication medResource = new Medication();
		medResource.setCode(codeDt);
		medResource.setId("#" + String.valueOf(medId));
		ArrayList<IResource> medResList = new ArrayList<IResource>();
		medResList.add(medResource);
		ContainedDt medContainedDt = new ContainedDt();
		medContainedDt.setContainedResources(medResList);
		medicationPrescript.setContained(medContainedDt);

		// Medication reference. This should point to the contained resource.
		ResourceReferenceDt medRefDt = new ResourceReferenceDt("#" + medId);
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
		CodeableConceptDt codeDt = new CodeableConceptDt("urn:sys", "code1");

		// Adding medication to Contained.
		Medication medResource = new Medication();
		// No ID set
		medResource.setCode(codeDt);

		// Medication reference. This should point to the contained resource.
		ResourceReferenceDt medRefDt = new ResourceReferenceDt();
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
		CodeableConceptDt codeDt = new CodeableConceptDt("urn:sys", "code1");

		// Adding medication to Contained.
		Medication medResource = new Medication();
		medResource.setCode(codeDt);
		medResource.setId(String.valueOf(medId)); // ID does not start with '#'
		ArrayList<IResource> medResList = new ArrayList<IResource>();
		medResList.add(medResource);
		ContainedDt medContainedDt = new ContainedDt();
		medContainedDt.setContainedResources(medResList);
		medicationPrescript.setContained(medContainedDt);

		// Medication reference. This should point to the contained resource.
		ResourceReferenceDt medRefDt = new ResourceReferenceDt("#" + medId);
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
		org.getText().setDiv("<div>FOOBAR</div>");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getText().setDiv("<div>BARFOO</div>");
		patient.getManagingOrganization().setResource(org);

		String encoded = parser.encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder("<Patient", "<text>", "<div xmlns=\"http://www.w3.org/1999/xhtml\">BARFOO</div>", "<contained>", "<Organization", "</Organization"));
		assertThat(encoded, stringContainsInOrder("<Patient", "<text>", "<contained>", "<Organization", "<text", "</Organization"));

		assertThat(encoded, (containsString("FOOBAR")));
		assertThat(encoded, (containsString("BARFOO")));

	}

	/**
	 * Make sure whitespace is preserved for pre tags
	 */
	@Test
	public void testEncodeDivWithPreNonPrettyPrint() {

		Patient p = new Patient();
		p.getText().setDiv("<div>\n\n<p>A P TAG</p><p><pre>line1\nline2\nline3  <b>BOLD</b></pre></p></div>");

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

	/**
	 * Make sure whitespace is preserved for pre tags
	 */
	@Test
	public void testEncodeDivWithPrePrettyPrint() {

		Patient p = new Patient();
		p.getText().setDiv("<div>\n\n<p>A P TAG</p><p><pre>line1\nline2\nline3  <b>BOLD</b></pre></p></div>");

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
		p.setId(new IdDt("urn:uuid:42795ed8-041f-4ebf-b6f4-78ef6f64c2f2"));
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
		TagList tagList = new TagList();
		tagList.addTag(null, null, null);
		tagList.addTag(null, null, "Label");

		Patient p = new Patient();
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tagList);

		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, not(containsString("tag")));
	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag2() {
		TagList tagList = new TagList();
		tagList.addTag("scheme", "code", null);
		tagList.addTag(null, null, "Label");

		Patient p = new Patient();
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tagList);

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
		ExtensionDt ext = obs.addUndeclaredExtension(false, "http://exturl");
		ext.setUrl("http://exturl").setValue(new StringDt("ext_url_value"));

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
		assertEquals(1, obs.getUndeclaredExtensions().size());
		assertEquals("http://exturl", obs.getUndeclaredExtensions().get(0).getUrl());
		assertEquals("ext_url_value", ((StringDt) obs.getUndeclaredExtensions().get(0).getValue()).getValue());
	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifierWithChildExtension() {
		Observation obs = new Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		ExtensionDt ext = obs.addUndeclaredExtension(false, "http://exturl");
		ext.setUrl("http://exturl");

		ExtensionDt subExt = ext.addUndeclaredExtension(false, "http://subext");
		subExt.setUrl("http://subext").setValue(new StringDt("sub_ext_value"));

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
		assertEquals(1, obs.getUndeclaredExtensions().size());
		assertEquals("http://exturl", obs.getUndeclaredExtensions().get(0).getUrl());
		assertEquals(1, obs.getUndeclaredExtensions().get(0).getExtension().size());
		assertEquals("http://subext", obs.getUndeclaredExtensions().get(0).getExtension().get(0).getUrl());
		assertEquals("sub_ext_value", ((StringDt) obs.getUndeclaredExtensions().get(0).getExtension().get(0).getValue()).getValue());
	}

	/**
	 * See #327
	 */
	@Test
	public void testEncodeExtensionWithContainedResource() {

		TestPatientFor327 patient = new TestPatientFor327();
		patient.setBirthDate(new DateDt("2016-04-17"));

		List<ResourceReferenceDt> conditions = new ArrayList<ResourceReferenceDt>();
		Condition condition = new Condition();
		condition.addBodySite().setText("BODY SITE");
		conditions.add(new ResourceReferenceDt(condition));
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
			"<birthDate value=\"2016-04-17\"/>",
			"</Patient>"
		));
		//@formatter:on
	}

	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.addUndeclaredExtension(false, "urn:foo", new ResourceReferenceDt("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueReference><reference value=\"Organization/123\"/></valueReference></extension>"));

		Patient actual = parser.parseResource(Patient.class, val);
		assertEquals(AddressUseEnum.HOME.getCode(), patient.getAddress().get(0).getUse());
		List<ExtensionDt> ext = actual.getUndeclaredExtensions();
		assertEquals(1, ext.size());
		ResourceReferenceDt ref = (ResourceReferenceDt) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReference().getValue());

	}

	@Test
	public void testEncodeForceResourceId() {
		Patient p = new Patient();
		p.setId("111");
		p.addName().addGiven("GIVEN");

		IParser parser = ourCtx.newXmlParser();
		parser.setEncodeForceResourceId(new IdDt("222"));
		String encoded = parser.encodeResourceToString(p);

		ourLog.info(encoded);

		assertThat(encoded, containsString("222"));
		assertThat(encoded, not(containsString("111")));
	}

	@Test
	public void testEncodeNarrativeSuppressed() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDiv("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(MaritalStatusCodesEnum.D);

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
		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
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

	/**
	 * See #312
	 */
	@Test
	public void testEncodeNullExtension() {
		Patient patient = new Patient();
		patient.getUndeclaredExtensions().add(null); // Purposely add null
		patient.getUndeclaredModifierExtensions().add(null); // Purposely add null
		patient.getUndeclaredExtensions().add(new ExtensionDt(false, "http://hello.world", new StringDt("Hello World")));
		patient.getName().add(null);
		patient.addName().getFamily().add(null);

		IParser parser = ourCtx.newXmlParser();
		String xml = parser.encodeResourceToString(patient);

		ourLog.info(xml);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://hello.world\"><valueString value=\"Hello World\"/></extension></Patient>", xml);
	}

	@Test
	public void testEncodeReferenceUsingUnqualifiedResourceWorksCorrectly() {

		Patient patient = new Patient();
		patient.setId("phitcc_pat_normal");
		patient.addName().addGiven("Patty").setUse(NameUseEnum.NICKNAME);
		patient.addTelecom().setSystem(ContactPointSystemEnum.EMAIL).setValue("patpain@ehealthinnovation.org");
		patient.setGender(AdministrativeGenderEnum.FEMALE);
		patient.setBirthDate(new DateDt("2001-10-13"));

		DateTimeDt obsEffectiveTime = new DateTimeDt("2015-04-11T12:22:01-04:00");

		Observation obsParent = new Observation();
		obsParent.setId("phitcc_obs_bp_parent");
		obsParent.getSubject().setResource(patient);
		obsParent.setStatus(ObservationStatusEnum.FINAL);
		obsParent.setEffective(obsEffectiveTime);

		Observation obsSystolic = new Observation();
		obsSystolic.setId("phitcc_obs_bp_dia");
		obsSystolic.getSubject().setResource(patient);
		obsSystolic.setEffective(obsEffectiveTime);
		obsParent.addRelated().setType(ObservationRelationshipTypeEnum.HAS_MEMBER).setTarget(new ResourceReferenceDt(obsSystolic));

		Observation obsDiastolic = new Observation();
		obsDiastolic.setId("phitcc_obs_bp_dia");
		obsDiastolic.getSubject().setResource(patient);
		obsDiastolic.setEffective(obsEffectiveTime);
		obsParent.addRelated().setType(ObservationRelationshipTypeEnum.HAS_MEMBER).setTarget(new ResourceReferenceDt(obsDiastolic));

		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(obsParent);
		ourLog.info(str);

		assertThat(str, containsString("<reference value=\"Patient/phitcc_pat_normal\"/>"));
		assertThat(str, containsString("<reference value=\"Observation/phitcc_obs_bp_dia\"/>"));
	}

	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDiv("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(MaritalStatusCodesEnum.D);

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
		patient.getText().setDiv("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(MaritalStatusCodesEnum.D);

		TagList tl = new TagList();
		tl.add(new Tag("foo", "bar"));
		ResourceMetadataKeyEnum.TAG_LIST.put(patient, tl);

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
	public void testEncodeWithDontEncodeElements() throws Exception {
		Patient patient = new Patient();
		patient.setId("123");

		ArrayList<IdDt> list = new ArrayList<IdDt>();
		list.add(new IdDt("http://profile"));
		ResourceMetadataKeyEnum.PROFILES.put(patient, list);
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
			p.setEncodeElements(new HashSet<String>(Arrays.asList("Patient.name")));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("id"));
			assertThat(out, not(containsString("address")));
			assertThat(out, not(containsString("meta")));
		}
	}

	@Test
	public void testEncodeWithEncodeElements() throws Exception {
		Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");
		patient.addAddress().addLine("LINE1");

		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		bundle.setTotal(100);
		bundle.addEntry().setResource(patient);

		{
			IParser p = ourCtx.newXmlParser();
			p.setEncodeElements(new HashSet<>(Arrays.asList("Patient.name", "Bundle.entry")));
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
			p.setEncodeElements(new HashSet<>(Arrays.asList("Patient.name")));
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
			p.setEncodeElements(new HashSet<>(Arrays.asList("Patient")));
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
	public void testEncodeWithEncodeElementsMandatory() throws Exception {
		MedicationOrder mo = new MedicationOrder();
		mo.getText().setDiv("<div>DIV</div>");
		mo.setNote("NOTE");
		mo.setMedication(new ResourceReferenceDt("Medication/123"));

		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		bundle.setTotal(100);
		bundle.addEntry().setResource(mo);

		{
			IParser p = ourCtx.newXmlParser();
			p.setEncodeElements(new HashSet<>(Arrays.asList("Bundle.entry", "*.text", "*.(mandatory)")));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(bundle);
			ourLog.info(out);
			assertThat(out, (containsString("DIV")));
			assertThat(out, (containsString("Medication/123")));
			assertThat(out, not(containsString("NOTE")));
		}
	}

	@Test
	public void testMoreExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUseEnum.OFFICIAL).setSystem("urn:example").setValue("7000135");

		ExtensionDt ext = new ExtensionDt();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeDt("2011-01-02T11:13:15"));

		// Add the extension to the resource
		patient.addUndeclaredExtension(ext);
		// END SNIPPET: resourceExtension

		// START SNIPPET: resourceStringExtension
		HumanNameDt name = patient.addName();
		name.addFamily("Shmoe");
		StringDt given = name.addGiven();
		given.setValue("Joe");
		ExtensionDt ext2 = new ExtensionDt().setUrl("http://examples.com#givenext").setValue(new StringDt("given"));
		given.addUndeclaredExtension(ext2);

		StringDt given2 = name.addGiven();
		given2.setValue("Shmoe");
		ExtensionDt given2ext = new ExtensionDt().setUrl("http://examples.com#givenext_parent");
		given2.addUndeclaredExtension(given2ext);
		ExtensionDt givenExtChild = new ExtensionDt();
		givenExtChild.setUrl("http://examples.com#givenext_child").setValue(new StringDt("CHILD"));
		given2ext.addUndeclaredExtension(givenExtChild);
		// END SNIPPET: resourceStringExtension

		// START SNIPPET: subExtension
		ExtensionDt parent = new ExtensionDt().setUrl("http://example.com#parent");
		patient.addUndeclaredExtension(parent);

		ExtensionDt child1 = new ExtensionDt().setUrl("http://example.com#child").setValue(new StringDt("value1"));
		parent.addUndeclaredExtension(child1);

		ExtensionDt child2 = new ExtensionDt().setUrl("http://example.com#child").setValue(new StringDt("value1"));
		parent.addUndeclaredExtension(child2);
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
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu2Test.class.getResourceAsStream("/bundle-example.xml"), StandardCharsets.UTF_8);

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals("1", parsed.getId().getVersionIdPart());
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getTypeElement().getValue());
		assertEquals(3, parsed.getTotalElement().getValue().intValue());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink("next").getUrl());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLink("self").getUrl());

		assertEquals(2, parsed.getEntry().size());
		// assertEquals("http://foo?search", parsed.getEntry().get(0).getLink("search").getUrl());
		// assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", parsed.getEntry().get(0).getLink("alternate").getUrl());

		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId().getValue());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertSame(((ResourceReferenceDt) p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		compareXml(content, reencoded);

	}

	@Test
	public void testParseAndEncodeBundleNewStyle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu2Test.class.getResourceAsStream("/bundle-example.xml"), StandardCharsets.UTF_8);

		IParser newXmlParser = ourCtx.newXmlParser();
		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = newXmlParser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getTypeElement().getValueAsString());
		assertEquals(3, parsed.getTotal().intValue());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink().get(0).getUrlElement().getValueAsString());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLink().get(1).getUrlElement().getValueAsString());

		assertEquals(2, parsed.getEntry().size());
		assertEquals("alternate", parsed.getEntry().get(0).getLink().get(0).getRelation());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", parsed.getEntry().get(0).getLink().get(0).getUrl());
		assertEquals("http://foo?search", parsed.getEntry().get(0).getRequest().getUrlElement().getValueAsString());

		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId().getValue());
		// assertEquals("3123", p.getId().getValue());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertSame(((ResourceReferenceDt) p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		compareXml(content, reencoded);

	}

	@Test
	public void testParseAndEncodeComments() throws IOException {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
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
			"</Patient>";
		//@formatter:off

		Patient res = ourCtx.newXmlParser().parseResource(Patient.class, input);
		res.getFormatCommentsPre();
		assertEquals("Patient/pat1", res.getId().getValue());
		assertEquals("654321", res.getIdentifier().get(0).getValue());
		assertEquals(true, res.getActive());

		assertThat(res.getIdentifier().get(0).getFormatCommentsPre(), contains("identifier comment 1", "identifier comment 2"));
		assertThat(res.getIdentifier().get(0).getUseElement().getFormatCommentsPre(), contains("use comment 1", "use comment 2"));

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
			"<!--identifier comment 1-->",
			"<!--identifier comment 2-->",
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
	public void testParseAndEncodeExtensionOnResourceReference() {
		//@formatter:off
		String input =
			"<DataElement>" +
				"<id value=\"gender\"/>" +
				"<contained>" +
				"<ValueSet>" +
				"<id value=\"2179414\"/>" +
				"<url value=\"2179414\"/>" +
				"<version value=\"1.0\"/>" +
				"<status value=\"active\"/>" +
				"<description value=\"All codes representing the gender of a person.\"/>" +
				"<compose>" +
				"<include>" +
				"<system value=\"http://ncit.nci.nih.gov\"/>" +
				"<concept>" +
				"<code value=\"C17998\"/>" +
				"<display value=\"Unknown\"/>" +
				"</concept>" +
				"<concept>" +
				"<code value=\"C20197\"/>" +
				"<display value=\"Male\"/>" +
				"</concept>" +
				"<concept>" +
				"<code value=\"C16576\"/>" +
				"<display value=\"Female\"/>" +
				"</concept>" +
				"<concept>" +
				"<code value=\"C38046\"/>" +
				"<display value=\"Not specified\"/>" +
				"</concept>" +
				"</include>" +
				"</compose>" +
				"</ValueSet>" +
				"</contained>" +
				"<contained>" +
				"<ValueSet>" +
				"<id value=\"2179414-permitted\"/>" +
				"<status value=\"active\"/>" +
				"<codeSystem>" +
				"<system value=\"http://example.org/fhir/2179414\"/>" +
				"<caseSensitive value=\"true\"/>" +
				"<concept>" +
				"<code value=\"0\"/>" +
				"</concept>" +
				"<concept>" +
				"<code value=\"1\"/>" +
				"</concept>" +
				"<concept>" +
				"<code value=\"2\"/>" +
				"</concept>" +
				"<concept>" +
				"<code value=\"3\"/>" +
				"</concept>" +
				"</codeSystem>" +
				"</ValueSet>" +
				"</contained>" +
				"<contained>" +
				"<ConceptMap>" +
				"<id value=\"2179414-cm\"/>" +
				"<status value=\"active\"/>" +
				"<sourceReference>" +
				"<reference value=\"#2179414\"/>" +
				"</sourceReference>" +
				"<targetReference>" +
				"<reference value=\"#2179414-permitted\"/>" +
				"</targetReference>" +
				"<element>" +
				"<code value=\"C17998\"/>" +
				"<target>" +
				"<code value=\"0\"/>" +
				"<equivalence value=\"equal\"/>" +
				"</target>" +
				"</element>" +
				"<element>" +
				"<code value=\"C20197\"/>" +
				"<target>" +
				"<code value=\"1\"/>" +
				"<equivalence value=\"equal\"/>" +
				"</target>" +
				"</element>" +
				"<element>" +
				"<code value=\"C16576\"/>" +
				"<target>" +
				"<code value=\"2\"/>" +
				"<equivalence value=\"equal\"/>" +
				"</target>" +
				"</element>" +
				"<element>" +
				"<code value=\"C38046\"/>" +
				"<target>" +
				"<code value=\"3\"/>" +
				"<equivalence value=\"equal\"/>" +
				"</target>" +
				"</element>" +
				"</ConceptMap>" +
				"</contained>" +
				"<identifier>" +
				"<value value=\"2179650\"/>" +
				"</identifier>" +
				"<version value=\"1.0\"/>" +
				"<name value=\"Gender Code\"/>" +
				"<status value=\"active\"/>" +
				"<publisher value=\"DCP\"/>" +
				"<element>" +
				"<extension url=\"http://hl7.org/fhir/StructureDefinition/minLength\">" +
				"<valueInteger value=\"1\"/>" +
				"</extension>" +
				"<extension url=\"http://hl7.org/fhir/StructureDefinition/elementdefinition-question\">" +
				"<valueString value=\"Gender\"/>" +
				"</extension>" +
				"<path value=\"Gender\"/>" +
				"<definition value=\"The code representing the gender of a person.\"/>" +
				"<type>" +
				"<code value=\"CodeableConcept\"/>" +
				"</type>" +
				"<maxLength value=\"13\"/>" +
				"<binding>" +
				"<strength value=\"required\"/>" +
				"<valueSetReference>" +
				"<extension url=\"http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset\">" +
				"<valueReference>" +
				"<reference value=\"#2179414-permitted\"/>" +
				"</valueReference>" +
				"</extension>" +
				"<extension url=\"http://hl7.org/fhir/StructureDefinition/11179-permitted-value-conceptmap\">" +
				"<valueReference>" +
				"<reference value=\"#2179414-cm\"/>" +
				"</valueReference>" +
				"</extension>" +
				"<reference value=\"#2179414\"/>" +
				"</valueSetReference>" +
				"</binding>" +
				"</element>" +
				"</DataElement>";
		//@formatter:on
		DataElement de = ourCtx.newXmlParser().parseResource(DataElement.class, input);
		String output = ourCtx.newXmlParser().encodeResourceToString(de).replace(" xmlns=\"http://hl7.org/fhir\"", "");

		ElementDefinitionDt elem = de.getElement().get(0);
		Binding b = elem.getBinding();
		// assertEquals("All codes representing the gender of a person.", b.getDescription());

		ResourceReferenceDt ref = (ResourceReferenceDt) b.getValueSet();
		assertEquals("#2179414", ref.getReference().getValue());

		assertEquals(2, ref.getUndeclaredExtensions().size());

		ExtensionDt ext = ref.getUndeclaredExtensions().get(0);
		assertEquals("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset", ext.getUrl());
		assertEquals(ResourceReferenceDt.class, ext.getValue().getClass());
		assertEquals("#2179414-permitted", ((ResourceReferenceDt) ext.getValue()).getReference().getValue());

		ext = ref.getUndeclaredExtensions().get(1);
		assertEquals("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-conceptmap", ext.getUrl());
		assertEquals(ResourceReferenceDt.class, ext.getValue().getClass());
		assertEquals("#2179414-cm", ((ResourceReferenceDt) ext.getValue()).getReference().getValue());

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(de));

		assertThat(output, containsString("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset"));

		ourLog.info("Expected: {}", input);
		ourLog.info("Actual  : {}", output);
		assertEquals(input, output);
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
		DateDt bd = p.getBirthDateElement();
		assertEquals("2005-03-04", bd.getValueAsString());

		List<ExtensionDt> exts = bd.getUndeclaredExtensionsByUrl("http://my.fancy.extension.url");
		assertEquals(1, exts.size());
		ExtensionDt ext = exts.get(0);
		assertEquals(null, ext.getValue());

		exts = ext.getUndeclaredExtensionsByUrl("http://my.fancy.extension.url");
		assertEquals(1, exts.size());
		ext = exts.get(0);
		assertEquals("myNestedValue", ((StringDt) ext.getValue()).getValue());

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

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getId().getValue());

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

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getId().getValue());
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

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getId().getValue());

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

		parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getId().getValue());

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

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, bundle);
		assertEquals(1, b.getEntry().size());

	}

	@Test
	public void testParseBundleWithBinary() {
		// TODO: implement this test, make sure we handle ID and meta correctly in Binary
	}

	/**
	 * See #191
	 */
	@Test
	public void testParseBundleWithLinksOfUnknownRelation() throws Exception {
		String input = IOUtils.toString(XmlParserDstu2Test.class.getResourceAsStream("/bundle_orion.xml"), StandardCharsets.UTF_8);
		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);

		Link link = parsed.getLink().get(0);
		assertEquals("just trying add link", link.getRelation());
		assertEquals("blarion", link.getUrl());

		Entry entry = parsed.getEntry().get(0);
		link = entry.getLink().get(0);
		assertEquals("orionhealth.edit", link.getRelation());
		assertEquals("Observation", link.getUrl());
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

		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/3", bundle.getEntry().get(0).getResource().getId().getValue());
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/2", bundle.getEntry().get(1).getResource().getId().getValue());
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/1", bundle.getEntry().get(2).getResource().getId().getValue());
	}

	/**
	 * see #144 and #146
	 */
	@Test
	public void testParseContained() {

		FhirContext c = FhirContext.forDstu2();
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
		assertEquals("patient family", p.getNameFirstRep().getFamilyAsSingleString());
	}

	/**
	 * Thanks to Alexander Kley!
	 */
	@Test
	public void testParseContainedBinaryResource() {
		byte[] bin = new byte[]{0, 1, 2, 3, 4};
		final Binary binary = new Binary();
		binary.setContentType("PatientConsent").setContent(bin);
		// binary.setId(UUID.randomUUID().toString());

		ca.uhn.fhir.model.dstu2.resource.DocumentManifest manifest = new ca.uhn.fhir.model.dstu2.resource.DocumentManifest();
		// manifest.setId(UUID.randomUUID().toString());
		CodeableConceptDt cc = new CodeableConceptDt();
		cc.addCoding().setSystem("mySystem").setCode("PatientDocument");
		manifest.setType(cc);
		manifest.setMasterIdentifier(new IdentifierDt().setSystem("mySystem").setValue(UUID.randomUUID().toString()));
		manifest.addContent().setP(new ResourceReferenceDt(binary));
		manifest.setStatus(DocumentReferenceStatusEnum.CURRENT);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(manifest);
		ourLog.info(encoded);
		assertThat(encoded, StringContainsInOrder.stringContainsInOrder(Arrays.asList("contained>", "<Binary", "</contained>")));

		ca.uhn.fhir.model.dstu2.resource.DocumentManifest actual = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.DocumentManifest.class, encoded);
		assertEquals(1, actual.getContained().getContainedResources().size());
		assertEquals(1, actual.getContent().size());
		assertNotNull(((ResourceReferenceDt) actual.getContent().get(0).getP()).getResource());

	}

	/**
	 * See #366
	 */
	@Test
	public void testParseInvalidBoolean() {
		try {
			String resource = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
				"   <active value=\"1\"/>\n" +
				"</Patient>";

			IParser parser = ourCtx.newXmlParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(resource);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [2,4]]: " + Msg.code(1821) + "[element=\"active\"] Invalid attribute value \"1\": " + Msg.code(1872) + "Invalid boolean string: '1'", e.getMessage());
		}
	}

	@Test
	public void testParseInvalidTextualNumber() {
		Observation obs = new Observation();
		obs.setValue(new QuantityDt().setValue(1234));
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
		assertThat(patient.getIdentifier().get(0).getType().getValueAsEnum(), IsEmptyCollection.empty());

		ArgumentCaptor<String> capt = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).unknownAttribute(ArgumentMatchers.nullable(IParseLocation.class), capt.capture());

		assertEquals("value", capt.getValue());
	}

	@Test
	public void testParseMetadata() throws Exception {
		//@formatter:off
		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"   <total value=\"1\"/>\n" +
			"   <link>\n" +
			"      <relation value=\"base\"/>\n" +
			"      <url value=\"http://foo/fhirBase1\"/>\n" +
			"   </link>\n" +
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
			"               <lastUpdated value=\"2001-02-22T11:22:33-05:00\"/>\n" +
			"            </meta>\n" +
			"            <birthDate value=\"2012-01-02\"/>\n" +
			"         </Patient>\n" +
			"      </resource>\n" +
			"   </entry>\n" +
			"</Bundle>";
		//@formatter:on

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, bundle);
		assertEquals(1, b.getEntry().size());

		Patient pt = (Patient) b.getEntry().get(0).getResource();
		assertEquals("http://foo/fhirBase2/Patient/1/_history/2", pt.getId().getValue());
		assertEquals("2012-01-02", pt.getBirthDateElement().getValueAsString());
		// assertEquals("0.123", ResourceMetadataKeyEnum.ENTRY_SCORE.get(pt).getValueAsString());
		// assertEquals("match", ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(pt).getCode());
		// assertEquals("POST", ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(pt).getCode());
		// assertEquals("http://foo/Patient?identifier=value", ResourceMetadataKeyEnum.LINK_SEARCH.get(pt));
		// assertEquals("2001-02-22T11:22:33-05:00", ResourceMetadataKeyEnum.UPDATED.get(pt).getValueAsString());

		Bundle toBundle = new Bundle();
		toBundle.getLinkOrCreate("base").setUrl("http://foo/fhirBase1");
		toBundle.getTotalElement().setValue(1);
		toBundle.getLinkOrCreate("self").setUrl("http://localhost:52788/Binary?_pretty=true");

		toBundle
			.addEntry()
			.setResource(pt)
			.setFullUrl("http://foo/fhirBase2/Patient/1/_history/2");

		String reEncoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(toBundle);

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
		ca.uhn.fhir.model.dstu2.resource.Bundle b = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);

		InstantDt updated = ResourceMetadataKeyEnum.UPDATED.get(b);
		assertEquals("2015-06-22T15:48:57.554-04:00", updated.getValueAsString());

	}

	@Test
	public void testParseNarrative() throws Exception {
		//@formatter:off
		String htmlNoNs = "<div>AAA<b>BBB</b>CCC</div>";
		String htmlNs = htmlNoNs.replace("<div>", "<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		String res = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
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
		patient.setId(new IdDt("Patient", patientId));
		patient.addName().addGiven("John").addFamily("Smith");
		patient.setGender(AdministrativeGenderEnum.MALE);
		patient.setBirthDate(new DateDt("1987-04-16"));

		// Bundle
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		bundle.setType(BundleTypeEnum.COLLECTION);
		bundle.addEntry().setResource(patient);

		String bundleText = xmlParser.encodeResourceToString(bundle);
		ourLog.info(bundleText);

		ca.uhn.fhir.model.dstu2.resource.Bundle reincarnatedBundle = xmlParser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, bundleText);
		Patient reincarnatedPatient = reincarnatedBundle.getAllPopulatedChildElementsOfType(Patient.class).get(0);

		assertEquals("Patient", patient.getId().getResourceType());
		assertEquals("Patient", reincarnatedPatient.getId().getResourceType());
	}

	/**
	 * Test for the url generated based on the server config
	 */
	@Test
	public void testGeneratedUrls() {
		final IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);
		xmlParser.setServerBaseUrl("http://myserver.com");

		final CustomPatientDstu2 patient = new CustomPatientDstu2();
		patient.setHomeless(new BooleanDt(true));

		final String parsedPatient = xmlParser.encodeResourceToString(patient);

		assertTrue(parsedPatient.contains("<profile value=\"http://myserver.com/StructureDefinition/Patient\"/>"));
		assertTrue(parsedPatient.contains("<extension url=\"http://myserver.com/StructureDefinition/homeless\">"));
	}

	/**
	 * Test for the url generated based on the server config
	 */
	@Test
	public void testCustomUrlExtension() {
		final String expected = "<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://www.example.com/petname\"><valueString value=\"myName\"/></extension></Patient>";

		final MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setPetName(new StringDt("myName"));

		final IParser xmlParser = ourCtx.newXmlParser();
		xmlParser.setServerBaseUrl("http://www.example.com");

		final String parsedPatient = xmlParser.encodeResourceToString(patient);
		System.out.println(parsedPatient);
		assertEquals(expected, parsedPatient);

		// Parse with string
		MyPatientWithCustomUrlExtension newPatient = xmlParser.parseResource(MyPatientWithCustomUrlExtension.class, parsedPatient);
		assertEquals("myName", newPatient.getPetName().getValue());

		// Parse with stream
		newPatient = xmlParser.parseResource(MyPatientWithCustomUrlExtension.class, new StringReader(parsedPatient));
		assertEquals("myName", newPatient.getPetName().getValue());

		// Check no NPE if base server not configure
		newPatient = ourCtx.newXmlParser().parseResource(MyPatientWithCustomUrlExtension.class, new StringReader(parsedPatient));
		assertNull(newPatient.getPetName().getValue());
		assertEquals("myName", ((StringDt) newPatient.getUndeclaredExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());
	}

	@Test
	public void testCustomUrlExtensionInBundle() {
		final String expected = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><resource><Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://www.example.com/petname\"><valueString value=\"myName\"/></extension></Patient></resource></entry></Bundle>";

		final MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setPetName(new StringDt("myName"));

		final Bundle bundle = new Bundle();
		final Entry entry = new Entry();
		entry.setResource(patient);
		bundle.addEntry(entry);

		final IParser xmlParser = ourCtx.newXmlParser();
		xmlParser.setServerBaseUrl("http://www.example.com");

		final String parsedBundle = xmlParser.encodeResourceToString(bundle);
		System.out.println(parsedBundle);
		assertEquals(expected, parsedBundle);

		// Parse with string
		Bundle newBundle = xmlParser.parseResource(Bundle.class, parsedBundle);
		assertNotNull(newBundle);
		assertEquals(1, newBundle.getEntry().size());
		Patient newPatient = (Patient) newBundle.getEntry().get(0).getResource();
		assertEquals("myName", ((StringDt) newPatient.getUndeclaredExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());

		// Parse with stream
		newBundle = xmlParser.parseResource(Bundle.class, new StringReader(parsedBundle));
		assertNotNull(newBundle);
		assertEquals(1, newBundle.getEntry().size());
		newPatient = (Patient) newBundle.getEntry().get(0).getResource();
		assertEquals("myName", ((StringDt) newPatient.getUndeclaredExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());

	}

	@Test
	public void testBaseUrlFooResourceCorrectlySerializedInExtensionValueReference() {
		String refVal = "http://my.org/FooBar";

		Patient fhirPat = new Patient();
		fhirPat.addUndeclaredExtension(false, "x1").setValue(new ResourceReferenceDt(refVal));

		IParser parser = ourCtx.newXmlParser();

		String output = parser.encodeResourceToString(fhirPat);
		System.out.println("output: " + output);

		// Deserialize then check that valueReference value is still correct
		fhirPat = parser.parseResource(Patient.class, output);

		List<ExtensionDt> extlst = fhirPat.getUndeclaredExtensionsByUrl("x1");
		assertEquals(1, extlst.size());
		assertEquals(refVal, ((ResourceReferenceDt) extlst.get(0).getValue()).getReference().getValue());
	}

	@ResourceDef(name = "Condition", profile = "http://hl7.org/fhir/profiles/custom-condition", id = "custom-condition")
	public static class CustomCondition extends Condition {
		private static final long serialVersionUID = 1L;

		@Child(name = "abatement", min = 0, max = 1, order = Child.REPLACE_PARENT, type = {DateTimeDt.class}, summary = true)
		private DateTimeDt myAbatement;

		public DateTimeDt getOurAbatement() {
			return myAbatement;
		}

		void setOurAbatement(DateTimeDt theAbatement) {
			this.myAbatement = theAbatement;
		}
	}

	@ResourceDef(name = "Patient")
	public static class TestPatientFor327 extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(name = "testCondition")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "testCondition", definedLocally = true, isModifier = false)
		private List<ResourceReferenceDt> testConditions = null;

		public List<ResourceReferenceDt> getConditions() {
			return this.testConditions;
		}

		public void setCondition(List<ResourceReferenceDt> ref) {
			this.testConditions = ref;
		}
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static void main(String[] args) {
		IGenericClient c = ourCtx.newRestfulGenericClient("http://fhir-dev.healthintersections.com.au/open");
		// c.registerInterceptor(new LoggingInterceptor(true));
		c.read().resource("Patient").withId("324").execute();
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

}
