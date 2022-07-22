package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.FooMessageHeaderWithExplicitField.FooMessageSourceComponent;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.parser.PatientWithCustomCompositeExtension.FooParentExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.Address.AddressUseEnumFactory;
import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.DataElement;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.dstu3.model.DocumentManifest;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.Duration;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.EnumFactory;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.GuidanceResponse;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.SampledData;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
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
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class XmlParserDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserDstu3Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();

	@AfterEach
	public void after() {
		if (ourCtx == null) {
			ourCtx = FhirContext.forDstu3();
		}
		ourCtx.setNarrativeGenerator(null);
	}

	@Test
	public void testEncodedResourceWithIncorrectRepresentationOfDecimalTypeToXml() {
		DecimalType decimalType = new DecimalType();
		decimalType.setValueAsString(".5");
		MedicationRequest mr = new MedicationRequest();
		Dosage dosage = new Dosage();
		dosage.setDose(new SimpleQuantity()
			.setValue(decimalType.getValue())
			.setUnit("{tablet}")
			.setSystem("http://unitsofmeasure.org")
			.setCode("{tablet}"));
		mr.addDosageInstruction(dosage);
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(mr);
		ourLog.info(encoded);
		mr = ourCtx.newXmlParser().parseResource(MedicationRequest.class, encoded);
		assertEquals(BigDecimal.valueOf(0.5), mr.getDosageInstructionFirstRep().getDoseSimpleQuantity().getValue());
		assertTrue(encoded.contains("0.5"));
	}

	/**
	 * We specifically include extensions on CapabilityStatement even in
	 * summary mode, since this is behaviour that people depend on
	 */
	@Test
	public void testEncodeSummaryCapabilityStatementExtensions() {

		CapabilityStatement cs = new CapabilityStatement();
		CapabilityStatement.CapabilityStatementRestComponent rest = cs.addRest();
		rest.setMode(CapabilityStatement.RestfulCapabilityMode.CLIENT);
		rest.getSecurity()
			.addExtension()
			.setUrl("http://foo")
			.setValue(new StringType("bar"));

		cs.getVersionElement().addExtension()
			.setUrl("http://goo")
			.setValue(new StringType("ber"));

		String encoded = ourCtx.newXmlParser().setSummaryMode(true).setPrettyPrint(true).setPrettyPrint(true).encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded, Matchers.containsString("http://foo"));
		assertThat(encoded, Matchers.containsString("bar"));
		assertThat(encoded, Matchers.containsString("http://goo"));
		assertThat(encoded, Matchers.containsString("ber"));
	}


	@Test
	public void testEncodeInvalidMetaTime() {

		Patient p = new Patient();
		p.getMeta().getLastUpdatedElement().setValueAsString("2019-01-01");
		String output = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(output, containsString("lastUpdated value=\"2019-01-01\""));

	}

	@Test
	public void testEncodeExtensionWithNullUrl() {
		{
			Patient p = new Patient();
			p.addExtension().setValue(new StringType("foo"));
			String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
			assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><extension><valueString value=\"foo\"/></extension></Patient>", encoded);
		}
		{
			Patient p = new Patient();
			p.getActiveElement().addExtension().setValue(new StringType("foo"));
			String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
			assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><active><extension><valueString value=\"foo\"/></extension></active></Patient>", encoded);
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

	/**
	 * See #544
	 */
	@Test
	public void testBundleStitchReferencesByUuid() {
		Bundle bundle = new Bundle();

		DocumentManifest dm = new DocumentManifest();
		dm.getSubject().setReference("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3");
		bundle.addEntry().setResource(dm);

		Patient patient = new Patient();
		patient.addName().setFamily("FAMILY");
		bundle.addEntry().setResource(patient).setFullUrl("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(encoded);

		bundle = ourCtx.newXmlParser().parseResource(Bundle.class, encoded);
		dm = (DocumentManifest) bundle.getEntry().get(0).getResource();

		assertEquals("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3", dm.getSubject().getReference());

		Patient subject = (Patient) dm.getSubject().getResource();
		assertNotNull(subject);
		assertEquals("FAMILY", subject.getNameFirstRep().getFamily());
	}

	@Test
	public void testBundleWithBinary() {

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

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, bundle);
		assertEquals(1, b.getEntry().size());

		Binary bin = (Binary) b.getEntry().get(0).getResource();
		assertArrayEquals(new byte[]{1, 2, 3, 4}, bin.getContent());

	}

	@Test
	public void testUnknownAttributeInPrimitive() {

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"   <total value=\"1\" foo=\"bar\"/>\n" +
			"</Bundle>";

		Bundle b = ourCtx.newXmlParser().setParserErrorHandler(errorHandler).parseResource(Bundle.class, bundle);
		assertEquals(1, b.getTotal());

		ArgumentCaptor<String> attributeCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).unknownAttribute(any(), attributeCaptor.capture());
		assertEquals("foo", attributeCaptor.getValue());
	}

	@Test
	public void testUnknownElementInPrimitive() {

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"   <total value=\"1\">\n" +
			"      <foo/>" +
			"   </total>\n" +
			"</Bundle>";

		Bundle b = ourCtx.newXmlParser().setParserErrorHandler(errorHandler).parseResource(Bundle.class, bundle);
		assertEquals(1, b.getTotal());

		ArgumentCaptor<String> attributeCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).unknownElement(any(), attributeCaptor.capture());
		assertEquals("foo", attributeCaptor.getValue());
	}

	@Test
	public void testExtensionInInvalidSpot() {

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"   <extension url=\"http://foo\">" +
			"      <valueString value=\"blah\"/>" +
			"   </extension>" +
			"   <modifierExtension url=\"http://foo\">" +
			"      <valueString value=\"blah\"/>" +
			"   </modifierExtension>" +
			"   <total value=\"1\"/>\n" +
			"</Bundle>";

		Bundle b = ourCtx.newXmlParser().setParserErrorHandler(errorHandler).parseResource(Bundle.class, bundle);
		assertEquals(1, b.getTotal());

		ArgumentCaptor<String> attributeCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(2)).unknownElement(any(), attributeCaptor.capture());
		assertEquals("extension", attributeCaptor.getAllValues().get(0));
		assertEquals("modifierExtension", attributeCaptor.getAllValues().get(1));
	}


	@Test
	public void testContainedResourceInExtensionUndeclared() {
		Patient p = new Patient();
		p.addName().setFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		p.addExtension(new Extension("urn:foo", new Reference(o)));

		String str = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(str);

		p = ourCtx.newXmlParser().parseResource(Patient.class, str);
		assertEquals("PATIENT", p.getName().get(0).getFamily());

		List<Extension> exts = p.getExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		Reference rr = (Reference) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());
	}

	@Test
	public void testContainedResourceWithNoId() throws IOException {
		try {
			String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_contained_with_no_id.xml"), StandardCharsets.UTF_8);

			IParser parser = ourCtx.newXmlParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(Bundle.class, string);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [49,11]]: " + Msg.code(1819) + "Resource has contained child resource with no ID", e.getMessage());
		}
	}

	@Test
	public void testContainedResourceWithNoId2() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"   <contained>\n" +
			"      <Organization xmlns=\"http://hl7.org/fhir\">\n" +
			"         <name value=\"Contained Test Organization\"/>\n" +
			"      </Organization>\n" +
			"   </contained>" +
			"   <active value=\"true\"/>" +
			"</Patient>";

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		IParser p = ourCtx.newXmlParser();
		p.setParserErrorHandler(errorHandler);

		Patient patient = p.parseResource(Patient.class, input);
		assertTrue(patient.getActive());

		verify(errorHandler, times(1)).containedResourceWithNoId(nullable(IParseLocation.class));

	}


	@Test()
	public void testContainedResourceWithNoIdLenient() throws IOException {
		String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_contained_with_no_id.xml"), StandardCharsets.UTF_8);

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		parser.parseResource(Bundle.class, string);
	}

	/**
	 * Test for the url generated based on the server config
	 */
	@Test
	public void testCustomUrlExtension() {
		final String expected = "<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://www.example.com/petname\"><valueString value=\"myName\"/></extension></Patient>";

		final MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setPetName(new StringType("myName"));

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
		assertEquals("myName", ((StringType) newPatient.getExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());
	}

	@Test
	public void testCustomUrlExtensioninBundle() {
		final String expected = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><resource><Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://www.example.com/petname\"><valueString value=\"myName\"/></extension></Patient></resource></entry></Bundle>";

		final MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setPetName(new StringType("myName"));

		final Bundle bundle = new Bundle();
		final BundleEntryComponent entry = new BundleEntryComponent();
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
		assertEquals("myName", ((StringType) newPatient.getExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());

		// Parse with stream
		newBundle = xmlParser.parseResource(Bundle.class, new StringReader(parsedBundle));
		assertNotNull(newBundle);
		assertEquals(1, newBundle.getEntry().size());
		newPatient = (Patient) newBundle.getEntry().get(0).getResource();
		assertEquals("myName", ((StringType) newPatient.getExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());

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
		pt.addName().setFamily("PATIENT");

		Organization org = new Organization();
		org.setId("orgid");
		org.setName("ORG");
		pt.getManagingOrganization().setResource(org);

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(pt);
		bundle.addEntry().setResource(org);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">",
			"<managingOrganization>",
			"<reference value=\"Organization/orgid\"/>",
			"</managingOrganization>"));

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
		ourCtx = FhirContext.forDstu3();
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
		ourCtx = FhirContext.forDstu3();

		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);

		DiagnosticReport dr = new DiagnosticReport();
		dr.setStatus(DiagnosticReportStatus.FINAL);
		dr.addResult().setResource(obs);

		IParser parser = ourCtx.newXmlParser();
		parser.setPrettyPrint(true);

		String output = parser.encodeResourceToString(dr);
		ourLog.info(output);

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
	public void testEncodeAndParseExtensionOnReference() {
		DataElement de = new DataElement();
		ElementDefinitionBindingComponent b = de.addElement().getBinding();
		b.setDescription("BINDING");

		Organization o = new Organization();
		o.setName("ORG");
		b.addExtension(new Extension("urn:foo", new Reference(o)));

		String str = ourCtx.newXmlParser().encodeResourceToString(de);
		ourLog.info(str);

		de = ourCtx.newXmlParser().parseResource(DataElement.class, str);
		b = de.getElement().get(0).getBinding();
		assertEquals("BINDING", b.getDescription());

		List<Extension> exts = b.getExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		Reference rr = (Reference) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());

	}

	@Test
	public void testEncodeAndParseExtensions() {

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
		name.setFamily("Blah");
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
	public void testEncodeAndParseMedicationRequest() {
		MedicationRequest mo = new MedicationRequest();
		mo.getAuthoredOnElement().setValueAsString("2015-10-05");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(mo);
		ourLog.info(encoded);

		mo = ourCtx.newXmlParser().parseResource(MedicationRequest.class, encoded);
		assertEquals("2015-10-05", mo.getAuthoredOnElement().getValueAsString());
	}

	@Test
	public void testEncodeAndParseMetaProfileAndTags() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");

		p.getMeta().addProfile("http://foo/Profile1");
		p.getMeta().addProfile("http://foo/Profile2");

		p.getMeta().addTag().setSystem("scheme1").setCode("term1").setDisplay("label1");
		p.getMeta().addTag().setSystem("scheme2").setCode("term2").setDisplay("label2");

		p.getMeta().addSecurity().setSystem("sec_scheme1").setCode("sec_term1").setDisplay("sec_label1");
		p.getMeta().addSecurity().setSystem("sec_scheme2").setCode("sec_term2").setDisplay("sec_label2");

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

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

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		List<UriType> gotLabels = parsed.getMeta().getProfile();
		assertEquals(2, gotLabels.size());
		UriType label = gotLabels.get(0);
		assertEquals("http://foo/Profile1", label.getValue());
		label = gotLabels.get(1);
		assertEquals("http://foo/Profile2", label.getValue());

		List<Coding> tagList = parsed.getMeta().getTag();
		assertEquals(2, tagList.size());
		assertEquals("scheme1", tagList.get(0).getSystem());
		assertEquals("term1", tagList.get(0).getCode());
		assertEquals("label1", tagList.get(0).getDisplay());
		assertEquals("scheme2", tagList.get(1).getSystem());
		assertEquals("term2", tagList.get(1).getCode());
		assertEquals("label2", tagList.get(1).getDisplay());

		tagList = parsed.getMeta().getSecurity();
		assertEquals(2, tagList.size());
		assertEquals("sec_scheme1", tagList.get(0).getSystem());
		assertEquals("sec_term1", tagList.get(0).getCode());
		assertEquals("sec_label1", tagList.get(0).getDisplay());
		assertEquals("sec_scheme2", tagList.get(1).getSystem());
		assertEquals("sec_term2", tagList.get(1).getCode());
		assertEquals("sec_label2", tagList.get(1).getDisplay());
	}

	@Test
	public void testEncodeAndParseMetaProfiles() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");

		p.getMeta().addTag().setSystem("scheme1").setCode("term1").setDisplay("label1");
		p.getMeta().addTag().setSystem("scheme2").setCode("term2").setDisplay("label2");

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

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

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		assertThat(parsed.getMeta().getProfile(), empty());

		List<Coding> tagList = parsed.getMeta().getTag();
		assertEquals(2, tagList.size());
		assertEquals("scheme1", tagList.get(0).getSystem());
		assertEquals("term1", tagList.get(0).getCode());
		assertEquals("label1", tagList.get(0).getDisplay());
		assertEquals("scheme2", tagList.get(1).getSystem());
		assertEquals("term2", tagList.get(1).getCode());
		assertEquals("label2", tagList.get(1).getDisplay());
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
	public void testEncodeAndParseProfiledDatatype() {
		MedicationRequest mo = new MedicationRequest();
		mo.addDosageInstruction().getTiming().getRepeat().setBounds(new Duration().setCode("code"));
		String out = ourCtx.newXmlParser().encodeResourceToString(mo);
		ourLog.info(out);
		assertThat(out, containsString("</boundsDuration>"));

		mo = ourCtx.newXmlParser().parseResource(MedicationRequest.class, out);
		Duration duration = (Duration) mo.getDosageInstruction().get(0).getTiming().getRepeat().getBounds();
		assertEquals("code", duration.getCode());
	}

	/**
	 * See #216 - Profiled datatypes should use their unprofiled parent type as the choice[x] name
	 */
	@Test
	public void testEncodeAndParseProfiledDatatypeChoice() {
		IParser xmlParser = ourCtx.newXmlParser();

		MedicationStatement ms = new MedicationStatement();
		ms.addDosage().setDose(new SimpleQuantity().setValue(123));

		String output = xmlParser.encodeResourceToString(ms);
		assertThat(output, containsString("<doseQuantity><value value=\"123\"/></doseQuantity>"));
	}

	@Test
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");

		List<Coding> labels = new ArrayList<>();
		labels.add(new Coding().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setVersion("VERSION1"));
		labels.add(new Coding().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setVersion("VERSION2"));
		p.getMeta().getSecurity().addAll(labels);

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

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

	/**
	 * See #103
	 */
	@Test
	@Disabled
	public void testEncodeAndReEncodeContainedJson() {
		Composition comp = new Composition();
		comp.addSection().addEntry().setResource(new AllergyIntolerance().addNote(new Annotation().setText("Section0_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().addNote(new Annotation().setText("Section1_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().addNote(new Annotation().setText("Section2_Allergy0")));

		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);

		String string = parser.encodeResourceToString(comp);
		ourLog.info(string);

		Composition parsed = parser.parseResource(Composition.class, string);
		parsed.getSection().remove(0);

		string = parser.encodeResourceToString(parsed);
		ourLog.info(string);

		parsed = parser.parseResource(Composition.class, string);
		assertEquals(2, parsed.getContained().size());
	}

	/**
	 * See #103
	 */
	@Test
	@Disabled
	public void testEncodeAndReEncodeContainedXml() {
		Composition comp = new Composition();
		comp.addSection().addEntry().setResource(new AllergyIntolerance().addNote(new Annotation().setText("Section0_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().addNote(new Annotation().setText("Section1_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().addNote(new Annotation().setText("Section2_Allergy0")));

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);

		String string = parser.encodeResourceToString(comp);
		ourLog.info(string);

		Composition parsed = parser.parseResource(Composition.class, string);
		parsed.getSection().remove(0);

		string = parser.encodeResourceToString(parsed);
		ourLog.info(string);

		parsed = parser.parseResource(Composition.class, string);
		assertEquals(2, parsed.getContained().size());
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
	public void testEncodeBinaryWithSecurityContext() {
		Binary bin = new Binary();
		bin.setContentType("text/plain");
		bin.setContent("Now is the time".getBytes());
		Reference securityContext = new Reference();
		securityContext.setReference("DiagnosticReport/1");
		bin.setSecurityContext(securityContext);
		String encoded = ourCtx.newXmlParser().encodeResourceToString(bin);
		ourLog.info(encoded);
		assertThat(encoded, containsString("Binary"));
		assertThat(encoded, containsString("<contentType value=\"text/plain\"/>"));
		assertThat(encoded, containsString("<securityContext><reference value=\"DiagnosticReport/1\"/></securityContext>"));
		assertThat(encoded, containsString("<content value=\"Tm93IGlzIHRoZSB0aW1l\"/>"));
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

	/**
	 * See #402
	 */
	@Test
	public void testEncodeCompositionDoesntOverwriteNarrative() {
		FhirContext ctx = FhirContext.forDstu3();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		Composition composition  = new Composition();
		composition.getText().setDivAsString("<div>root</div>");
		composition.addSection().getText().setDivAsString("<div>section0</div>");
		composition.addSection().getText().setDivAsString("<div>section1</div>");

		String output = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(composition);
		ourLog.info(output);

		assertThat(output, containsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">root</div>"));
		assertThat(output, containsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">section0</div>"));
		assertThat(output, containsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">section1</div>"));

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
			"</Patient>"));

	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResources() {

		MedicationRequest medicationPrescript = new MedicationRequest();

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
			stringContainsInOrder("<MedicationRequest xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"123\"/>", "<code>", "<coding>",
				"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#123\"/>",
				"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationRequest>"));

	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResourcesAutomatic() {

		MedicationRequest medicationPrescript = new MedicationRequest();
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

		assertThat(encoded,
			stringContainsInOrder("<MedicationRequest xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"1\"/>", "<code>", "<coding>",
				"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#1\"/>",
				"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationRequest>"));

	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResourcesManualContainUsingNonLocalId() {

		MedicationRequest medicationPrescript = new MedicationRequest();

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

		assertThat(encoded,
			stringContainsInOrder("<MedicationRequest xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"123\"/>", "<code>", "<coding>",
				"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#123\"/>",
				"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationRequest>"));

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

		assertThat(encoded, containsString("FOOBAR"));
		assertThat(encoded, (containsString("BARFOO")));

	}

	@Test
	public void testEncodeContainedWithNonLocalId() throws Exception {

		Patient p = new Patient();
		p.setId("Patient1");
		p.setBirthDate(new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse("2016-04-15 10:15:30"));

		ProcedureRequest pr = new ProcedureRequest();
		pr.setId("1234567");
		pr.setSubject(new Reference(p));
		pr.setCode(new CodeableConcept().addCoding(new Coding("breastfeeding-readiness-assessment", "Breastfeeding Readiness Assessment", "Breastfeeding Readiness Assessment")));
		// pr.setReason(new StringType("Single Live Birth"));
		// pr.setScheduled(new DateType(new Date()));
		pr.setContext(new Reference("Live Birth Encounter"));
		pr.setPerformer(new Reference("Charge Nurse"));
		pr.setStatus(ProcedureRequest.ProcedureRequestStatus.DRAFT);

		GuidanceResponse.GuidanceResponseStatus status = GuidanceResponse.GuidanceResponseStatus.SUCCESS;

		GuidanceResponse gr = new GuidanceResponse()
			.setRequestId("123")
			.setModule(new Reference("Evaluate Operation"))
			.setStatus(status);

		gr.setReason(new Reference(pr));
		gr.getContained().add(p);
		gr.getContained().add(pr);

		IParser parser = ourCtx.newXmlParser();
		parser.setPrettyPrint(true);
		ourLog.info(parser.encodeResourceToString(gr));
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
		assertThat(encode, stringContainsInOrder("<dest", "<source"));
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

		assertThat(output, stringContainsInOrder(
			"<text><div",
			"<p>A P TAG</p><p>",
			"<pre>line1\nline2\nline3  <b>BOLD</b></pre>"));

	}

	@Test
	public void testEncodeDivWithPrePrettyPrint() {

		Patient p = new Patient();
		p.getText().setDivAsString("<div>\n\n<p>A P TAG</p><p><pre>line1\nline2\nline3  <b>BOLD</b></pre></p></div>");

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);

		assertThat(output, stringContainsInOrder(
			"   <text>",
			"      <div",
			"         <pre>line1\nline2\nline3  <b>BOLD</b></pre>"));

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
	public void testEncodeWithInvalidExtensionMissingUrl() {

		Patient p = new Patient();
		Extension root = p.addExtension();
		root.setValue(new StringType("ROOT_VALUE"));

		// Lenient error handler
		IParser parser = ourCtx.newXmlParser();
		String output = parser.encodeResourceToString(p);
		ourLog.info("Output: {}", output);
		assertThat(output, containsString("ROOT_VALUE"));

		// Strict error handler
		try {
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.encodeResourceToString(p);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1822) + "Resource is missing required element 'url' in parent element 'Patient(res).extension'", e.getMessage());
		}

	}


	@Test
	public void testEncodeWithInvalidExtensionContainingValueAndNestedExtensions() {

		Patient p = new Patient();
		Extension root = p.addExtension();
		root.setUrl("http://root");
		root.setValue(new StringType("ROOT_VALUE"));
		Extension child = root.addExtension();
		child.setUrl("http://child");
		child.setValue(new StringType("CHILD_VALUE"));

		// Lenient error handler
		IParser parser = ourCtx.newXmlParser();
		String output = parser.encodeResourceToString(p);
		ourLog.info("Output: {}", output);
		assertThat(output, containsString("http://root"));
		assertThat(output, containsString("ROOT_VALUE"));
		assertThat(output, containsString("http://child"));
		assertThat(output, containsString("CHILD_VALUE"));

		// Strict error handler
		try {
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.encodeResourceToString(p);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1827) + "[element=\"Patient(res).extension\"] Extension contains both a value and nested extensions", e.getMessage());
		}

	}

	@Test
	public void testEncodeExtensionOnRoot() {
		Patient p = new Patient();
		p.setId("Patient/B");
		p
			.addExtension()
			.setUrl("http://foo")
			.setValue(new Reference("Practitioner/A"));
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		parser.setDontEncodeElements(new HashSet<String>(Arrays.asList("*.id", "*.meta")));

		String encoded = parser.encodeResourceToString(p);
		ourLog.info(encoded);

		assertThat(encoded, containsString("http://foo"));
		assertThat(encoded, containsString("Practitioner/A"));
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

		assertThat(output, stringContainsInOrder(
			"<id value=\"1\"/>",
			"<meta>",
			"<profile value=\"http://profile\"/>",
			"<extension url=\"http://exturl\">",
			"<valueString value=\"ext_url_value\"/>",
			"<text value=\"CODE\"/>"));
		assertThat(output, not(stringContainsInOrder(
			"<url value=\"http://exturl\"/>")));

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

		assertThat(output, stringContainsInOrder(
			"<id value=\"1\"/>",
			"<meta>",
			"<profile value=\"http://profile\"/>",
			"<extension url=\"http://exturl\">",
			"<extension url=\"http://subext\">",
			"<valueString value=\"sub_ext_value\"/>",
			"<text value=\"CODE\"/>"));
		assertThat(output, not(stringContainsInOrder(
			"<url value=\"http://exturl\"/>")));

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
			"</Patient>"));

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
		ourCtx = FhirContext.forDstu3();

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
		patient.addName().setFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSuppressNarratives(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>",
			"<code value=\"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
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

	/**
	 * See #312
	 */
	@Test
	public void testEncodeNullElement() {
		Patient patient = new Patient();
		patient.addName().getGiven().add(null);

		IParser parser = ourCtx.newXmlParser();
		String xml = parser.encodeResourceToString(patient);

		ourLog.info(xml);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"/>", xml);
	}

	/**
	 * See #312
	 */
	@Test
	public void testEncodeNullExtension() {
		Patient patient = new Patient();
		patient.getExtension().add(null); // Purposely add null
		patient.getModifierExtension().add(null); // Purposely add null
		patient.getExtension().add(new Extension("http://hello.world", new StringType("Hello World")));
		patient.getName().add(null);
		patient.addName().getGiven().add(null);

		IParser parser = ourCtx.newXmlParser();
		String xml = parser.encodeResourceToString(patient);

		ourLog.info(xml);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://hello.world\"><valueString value=\"Hello World\"/></extension></Patient>", xml);
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
	public void testEncodeReferenceWithUuid() {

		Practitioner pract = new Practitioner();
		pract.setId(IdType.newRandomUuid());
		pract.addName().setFamily("PRACT FAMILY");

		Patient patient = new Patient();
		patient.addGeneralPractitioner().setResource(pract);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(pract.getId(), startsWith("urn:uuid:"));
		assertThat(encoded, containsString("<reference value=\"" + pract.getId() + "\"/>"));
	}

	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().setFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>",
			"<code value=\"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeSummary2() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().setFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		patient.getMeta().addTag().setSystem("foo").setCode("bar");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"foo\"/>", "<code value=\"bar\"/>", "</tag>"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>",
			"<code value=\"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeUndeclaredBlock() {
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
		assertThat(encode, stringContainsInOrder("<dest", "<source"));
	}

	@Test
	public void testEncodeUndeclaredExtensionWithEnumerationContent() {
		IParser parser = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		EnumFactory<AddressUse> fact = new AddressUseEnumFactory();
		PrimitiveType<AddressUse> enumeration = new Enumeration<>(fact).setValue(AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue(enumeration);

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueCode value=\"home\"/></extension>"));

		MyPatientWithOneDeclaredEnumerationExtensionDstu3 actual = parser.parseResource(MyPatientWithOneDeclaredEnumerationExtensionDstu3.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Enumeration<AddressUse> ref = actual.getFoo();
		assertEquals("home", ref.getValue().toCode());

	}

	@Test
	public void testEncodeWithContained() {
		List<Resource> contained = new ArrayList<>();

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
			"</Appointment>"));

		assertThat(encoded, not(containsString("#1002")));
	}

	@Test
	public void testEncodeWithDontEncodeElements() {
		Patient patient = new Patient();
		patient.setId("123");
		patient.getMeta().addProfile("http://profile");
		patient.addName().setFamily("FAMILY").addGiven("GIVEN");
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
	public void testEncodeWithEncodeElements() {
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://profile");
		patient.addName().setFamily("FAMILY");
		patient.addAddress().addLine("LINE1");

		Bundle bundle = new Bundle();
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
	public void testEncodeWithEncodeElementsAppliesToChildResourcesOnly() {
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://profile");
		patient.addName().setFamily("FAMILY");
		patient.addAddress().addLine("LINE1");

		Bundle bundle = new Bundle();
		bundle.setTotal(100);
		bundle.addEntry().setResource(patient);

		{
			IParser p = ourCtx.newXmlParser();
			p.setEncodeElements(new HashSet<>(Arrays.asList("Patient.name")));
			p.setEncodeElementsAppliesToChildResourcesOnly(true);
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(bundle);
			ourLog.info(out);
			assertThat(out, containsString("total"));
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, not(containsString("address")));
		}
	}

	@Test
	public void testEncodeWithNarrative() {
		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");

		ourCtx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		String output = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(output);

		assertThat(output, containsString("<text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\">John <b>SMITH </b>"));
	}

	/**
	 * Test for the url generated based on the server config
	 */
	@Test
	public void testGeneratedUrls() {
		final IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);
		xmlParser.setServerBaseUrl("http://myserver.com");

		final CustomPatientDstu3 patient = new CustomPatientDstu3();
		patient.setHomeless(new BooleanType(true));

		final String parsedPatient = xmlParser.encodeResourceToString(patient);

		assertTrue(parsedPatient.contains("<profile value=\"http://myserver.com/StructureDefinition/Patient\"/>"));
		assertTrue(parsedPatient.contains("<extension url=\"http://myserver.com/StructureDefinition/homeless\">"));
	}

	@Test
	public void testMoreExtensions() {

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
		name.setFamily("Shmoe");
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
		p.addName().setFamily("ABC");

		assertThat(ourCtx.newXmlParser().encodeResourceToString(p), stringContainsInOrder("123", "ABC"));
		assertThat(ourCtx.newXmlParser().setOmitResourceId(true).encodeResourceToString(p), containsString("ABC"));
		assertThat(ourCtx.newXmlParser().setOmitResourceId(true).encodeResourceToString(p), not(containsString("123")));
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

	@Test
	@Disabled
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu3Test.class.getResourceAsStream("/bundle-example.xml"), StandardCharsets.UTF_8);

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getIdElement().getValue());
		assertEquals("1", parsed.getMeta().getVersionId());
		assertEquals("1", parsed.getIdElement().getVersionIdPart());
		assertEquals(("2014-08-18T01:43:30Z"), parsed.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("searchset", parsed.getType().toCode());
		assertEquals(3, parsed.getTotal());
		assertEquals("https://example.com/base/MedicationRequest?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink("next").getUrl());
		assertEquals("https://example.com/base/MedicationRequest?patient=347&_include=MedicationRequest.medication", parsed.getLink("self").getUrl());

		assertEquals(2, parsed.getEntry().size());
		assertEquals("http://foo?search", parsed.getEntry().get(0).getLink("search").getUrl());

		assertEquals("http://example.com/base/MedicationRequest/3123/_history/1", parsed.getEntry().get(0).getLink("alternate").getUrl());
		MedicationRequest p = (MedicationRequest) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getSubject().getReference());
		assertEquals("2014-08-16T05:31:17Z", p.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("http://example.com/base/MedicationRequest/3123/_history/1", p.getId());

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
		String content = IOUtils.toString(XmlParserDstu3Test.class.getResourceAsStream("/bundle-example.xml"), StandardCharsets.UTF_8);

		IParser newXmlParser = ourCtx.newXmlParser();
		Bundle parsed = newXmlParser.parseResource(Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getIdElement().getValue());
		assertEquals("1", parsed.getIdElement().getVersionIdPart());
		assertEquals("2014-08-18T01:43:30Z", parsed.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("searchset", parsed.getType());
		assertEquals(3, parsed.getTotal());
		assertEquals("https://example.com/base/MedicationRequest?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink().get(0).getUrlElement().getValueAsString());
		assertEquals("https://example.com/base/MedicationRequest?patient=347&_include=MedicationRequest.medication", parsed.getLink().get(1).getUrlElement().getValueAsString());

		assertEquals(2, parsed.getEntry().size());
		assertEquals("alternate", parsed.getEntry().get(0).getLink().get(0).getRelation());
		assertEquals("http://example.com/base/MedicationRequest/3123/_history/1", parsed.getEntry().get(0).getLink().get(0).getUrl());
		assertEquals("http://foo?search", parsed.getEntry().get(0).getRequest().getUrlElement().getValueAsString());

		MedicationRequest p = (MedicationRequest) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getSubject().getReference());
		assertEquals("2014-08-16T05:31:17Z", p.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("http://example.com/base/MedicationRequest/3123/_history/1", p.getId());
		// assertEquals("3123", p.getId());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId());
		assertSame(((Reference) p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		compareXml(content, reencoded);

	}

	@Test
	public void testParseAndEncodeComments() {
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
			"\"type\""));

		encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

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
			"</Patient>"));

	}

	@Test
	public void testParseAndEncodeCommentsOnExtensions() {

		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
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
			"</Patient>"));

		output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pat);
		ourLog.info(output);

		assertThat(output, stringContainsInOrder("{",
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
			"}"));

	}

	@Test
	public void testParseAndEncodeExtensionOnReference() {

		String input = "<DataElement>" +
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
			"<useContext>" +
			"<coding>" +
			"<system value=\"http://example.org/FBPP\"/>" +
			"<display value=\"FBPP Pooled Database\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/PhenX\"/>" +
			"<display value=\"Demographics\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/EligibilityCriteria\"/>" +
			"<display value=\"Pt. Administrative\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/UAMSClinicalResearch\"/>" +
			"<display value=\"UAMS New CDEs\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/PhenX\"/>" +
			"<display value=\"Substance Abuse and \"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/Category\"/>" +
			"<display value=\"CSAERS Adverse Event\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/PhenX\"/>" +
			"<display value=\"Core: Tier 1\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/Category\"/>" +
			"<display value=\"Case Report Forms\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/Category\"/>" +
			"<display value=\"CSAERS Review Set\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/Demonstration%20Applications\"/>" +
			"<display value=\"CIAF\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/NIDA%20CTN%20Usage\"/>" +
			"<display value=\"Clinical Research\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/NIDA%20CTN%20Usage\"/>" +
			"<display value=\"Electronic Health Re\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/Condition\"/>" +
			"<display value=\"Barretts Esophagus\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/Condition\"/>" +
			"<display value=\"Bladder Cancer\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/Condition\"/>" +
			"<display value=\"Oral Leukoplakia\"/>" +
			"</coding>" +
			"<coding>" +
			"<system value=\"http://example.org/Condition\"/>" +
			"<display value=\"Sulindac for Breast\"/>" +
			"</coding>" +
			"</useContext>" +
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

		DataElement de = ourCtx.newXmlParser().parseResource(DataElement.class, input);
		String output = ourCtx.newXmlParser().encodeResourceToString(de).replace(" xmlns=\"http://hl7.org/fhir\"", "");

		ElementDefinition elem = de.getElement().get(0);
		ElementDefinitionBindingComponent b = elem.getBinding();
		// assertEquals("All codes representing the gender of a person.", b.getDescription());

		Reference ref = (Reference) b.getValueSet();
		assertEquals("#2179414", ref.getReference());

		assertEquals(2, ref.getExtension().size());
		Extension ext = ref.getExtension().get(0);
		assertEquals("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset", ext.getUrl());
		assertEquals(Reference.class, ext.getValue().getClass());
		assertEquals("#2179414-permitted", ((Reference) ext.getValue()).getReference());
		assertEquals(ValueSet.class, ((Reference) ext.getValue()).getResource().getClass());

		ext = ref.getExtension().get(1);
		assertEquals("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-conceptmap", ext.getUrl());
		assertEquals(Reference.class, ext.getValue().getClass());
		assertEquals("#2179414-cm", ((Reference) ext.getValue()).getReference());
		assertEquals(ConceptMap.class, ((Reference) ext.getValue()).getResource().getClass());

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(de));

		assertThat(output, containsString("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset"));

	}

	@Test
	public void testParseAndEncodeNestedExtensions() {

		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"   <birthDate value=\"2005-03-04\">\n" +
			"      <extension url=\"http://my.fancy.extension.url\">\n" +
			"         <extension url=\"http://my.fancy.extension.url\">\n" +
			"            <valueString value=\"myNestedValue\"/>\n" +
			"         </extension>\n" +
			"      </extension>\n" +
			"   </birthDate>\n" +
			"</Patient>";

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

	}

	@Test
	public void testParseBundleNewWithPlaceholderIds() {

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

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

	}

	@Test
	public void testParseBundleNewWithPlaceholderIdsInBase1() {

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

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());
	}

	@Test
	public void testParseBundleNewWithPlaceholderIdsInBase2() {

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

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

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

		parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

	}

	@Test
	public void testParseBundleOldStyleWithUnknownLinks() throws Exception {

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

		Bundle b = (Bundle) ourCtx.newXmlParser().parseResource(bundle);
		assertEquals(1, b.getEntry().size());

	}

	@Test
	public void testParseBundleOldWithPlaceholderIds() {

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

		Bundle parsed = (Bundle) ourCtx.newXmlParser().parseResource(input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getId());

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
		String input = IOUtils.toString(XmlParserDstu3Test.class.getResourceAsStream("/bundle_orion.xml"), StandardCharsets.UTF_8);
		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);

		BundleLinkComponent link = parsed.getLink().get(0);
		assertEquals("just trying add link", link.getRelation());
		assertEquals("blarion", link.getUrl());

		BundleEntryComponent entry = parsed.getEntry().get(0);
		link = entry.getLink().get(0);
		assertEquals("orionhealth.edit", link.getRelation());
		assertEquals("Observation", link.getUrl());
	}

	@Test
	public void testParseBundleWithResourceId() {

		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">"
			+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"3\"/><lastUpdated value=\"2015-09-11T23:35:43.273-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
			+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"2\"/><lastUpdated value=\"2015-09-11T23:35:42.849-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
			+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/><lastUpdated value=\"2015-09-11T23:35:42.295-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
			+ "</Bundle>\n";

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

		FhirContext c = FhirContext.forDstu3();
		IParser parser = c.newXmlParser().setPrettyPrint(true);

		Observation o = new Observation();
		o.getCode().setText("obs text");

		Patient p = new Patient();
		p.addName().setFamily("patient family");
		o.getSubject().setResource(p);

		String enc = parser.encodeResourceToString(o);
		ourLog.info(enc);

		assertThat(enc, stringContainsInOrder(
			"<Observation xmlns=\"http://hl7.org/fhir\">",
			"<contained>",
			"<Patient xmlns=\"http://hl7.org/fhir\">",
			"<id value=\"1\"/>",
			"</contained>",
			"<reference value=\"#1\"/>"));

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
		byte[] bin = new byte[]{0, 1, 2, 3, 4};
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

		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"    <extension url=\"http://aaa.ch/fhir/Patient#mangedcare\">\n" +
			"        <extension url=\"http://aaa.ch/fhir/Patient#mangedcare-aaa-id\">\n" +
			"           <valueId value=\"mc1\"/>\n" +
			"        </extension>\n" +
			"    </extension>\n" +
			"    <identifier>\n" +
			"        <value value=\"ais111\"/>\n" +
			"    </identifier>\n" +
			"</Patient>";

		Patient pt = ourCtx.newXmlParser().parseResource(Patient.class, input);

		List<Extension> extList = pt.getExtensionsByUrl("http://aaa.ch/fhir/Patient#mangedcare");
		extList = extList.get(0).getExtensionsByUrl("http://aaa.ch/fhir/Patient#mangedcare-aaa-id");
		Extension ext = extList.get(0);
		IdType value = (IdType) ext.getValue();
		assertEquals("mc1", value.getValueAsString());
	}

	/**
	 * See #426
	 * <p>
	 * Value type of FOO isn't a valid datatype
	 */
	@Test
	public void testParseExtensionWithInvalidType() {

		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"    <extension url=\"http://aaa.ch/fhir/Patient#mangedcare\">\n" +
			"        <extension url=\"http://aaa.ch/fhir/Patient#mangedcare-aaa-id\">\n" +
			"           <valueFOO value=\"mc1\"/>\n" +
			"        </extension>\n" +
			"    </extension>\n" +
			"    <identifier>\n" +
			"        <value value=\"ais111\"/>\n" +
			"    </identifier>\n" +
			"</Patient>";

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
			assertThat(e.getMessage(), containsString("Unexpected character 'F'"));
		}
	}

	/**
	 * See #366
	 */
	@Test()
	public void testParseInvalidBoolean() {

		String resource = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"   <active value=\"1\"/>\n" +
			"</Patient>";

		IParser p = ourCtx.newXmlParser();

		try {
			p.parseResource(resource);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [2,4]]: " + Msg.code(1821) +  "[element=\"active\"] Invalid attribute value \"1\": Invalid boolean string: '1'", e.getMessage());
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

		String out = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"   <identifier>\n" +
			"      <type value=\"MRN\"/>\n" +
			"      <system value=\"SYS\"/>\n" +
			"      <value value=\"VAL\"/>\n" +
			"   </identifier>\n" +
			"</Patient>";

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
	public void testParseMetaUpdatedDate() {

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

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, input);

		InstantType updated = b.getMeta().getLastUpdatedElement();
		assertEquals("2015-06-22T15:48:57.554-04:00", updated.getValueAsString());

	}

	@Test
	public void testParseMetadata() throws Exception {

		String content = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
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

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, content);
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

		compareXml(content, reEncoded);

	}

	// TODO: this should work
	@Test
	@Disabled
	public void testParseNarrative() throws Exception {

		String htmlNoNs = "<div>AAA<b>BBB</b>CCC</div>";
		String htmlNs = htmlNoNs.replace("<div>", "<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		String res = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"   <id value=\"1333\"/>\n" +
			"   <text>\n" +
			"      " + htmlNs + "\n" +
			"   </text>\n" +
			"</Patient>";

		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, res);
		assertEquals(htmlNs, p.getText().getDiv().getValueAsString());
	}

	@Test
	public void testParseNestedExtensionsInvalid() {

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
		patient.addName().addGiven("John").setFamily("Smith");
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

	@Test
	public void testParseWithInvalidLocalRef() throws IOException {
		try {
			String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_invalid_contained_ref.xml"), StandardCharsets.UTF_8);

			IParser parser = ourCtx.newXmlParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(Bundle.class, string);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [39,9]]: " + Msg.code(1825) +  "Unknown element 'encounter' found during parse", e.getMessage());
		}
	}

	@Test()
	public void testParseWithInvalidLocalRefLenient() throws IOException {
		String string = IOUtils.toString(getClass().getResourceAsStream("/bundle_with_invalid_contained_ref.xml"), StandardCharsets.UTF_8);

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		parser.parseResource(Bundle.class, string);
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
		org.hl7.fhir.dstu3.model.Bundle bundle = parser.parseResource(Bundle.class, string);

		DiagnosticReport resource = (DiagnosticReport) bundle.getEntry().get(0).getResource();
		Observation obs = (Observation) resource.getResult().get(1).getResource();
		assertEquals("#2", obs.getId());
		Reference performerFirstRep = obs.getPerformerFirstRep();
		Practitioner performer = (Practitioner) performerFirstRep.getResource();
		assertEquals("#3", performer.getId());
	}

	/**
	 * See #414
	 */
	@Test
	public void testParseXmlExtensionWithoutUrl() {

		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"        <extension>\n" +
			"          <valueDateTime value=\"2011-01-02T11:13:15\"/>\n" +
			"        </extension>\n" +
			"</Patient>";

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		Patient parsed = (Patient) parser.parseResource(input);
		assertEquals(1, parsed.getExtension().size());
		assertEquals(null, parsed.getExtension().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", parsed.getExtension().get(0).getValueAsPrimitive().getValueAsString());

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

		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"        <modifierExtension>\n" +
			"          <valueDateTime value=\"2011-01-02T11:13:15\"/>\n" +
			"        </modifierExtension>\n" +
			"</Patient>";

		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		Patient parsed = (Patient) parser.parseResource(input);
		assertEquals(1, parsed.getModifierExtension().size());
		assertEquals(null, parsed.getModifierExtension().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", parsed.getModifierExtension().get(0).getValueAsPrimitive().getValueAsString());

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
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [2,4]]: " + Msg.code(1825) +  "Unknown element 'valueSampleddata' found during parse", e.getMessage());
		}
	}

	/**
	 * See #11
	 */
	@Test
	public void testDuplicateContainedResources() {

		Observation resA = new Observation();
		resA.setComment("A");

		Observation resB = new Observation();
		resB.setComment("B");
		resB.addBasedOn(new Reference(resA));
		resB.addBasedOn(new Reference(resA));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resB);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Observation", "</Observation>", "</contained>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Observation", "</Observation>", "<Observation", "</contained>"))));
	}
	/**
	 * See #551
	 */
	@Test
	public void testXmlLargeAttribute() {
		String largeString = StringUtils.leftPad("", (int) FileUtils.ONE_MB, 'A');

		Patient p = new Patient();
		p.addName().setFamily(largeString);

		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);

		p = ourCtx.newXmlParser().parseResource(Patient.class, encoded);

		assertEquals(largeString, p.getNameFirstRep().getFamily());
	}

	/**
	 * See #339
	 * <p>
	 * https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Processing
	 */
	@Test
	public void testXxe() {

		String input = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
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

		ourLog.info(input);

		try {
			ourCtx.newXmlParser().parseResource(Patient.class, input);
			fail();
		} catch (DataFormatException e) {
			assertThat(e.toString(), containsString("Undeclared general entity"));
		}

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
			.ignoreWhitespace() // this is working with newest Saxon 9.8.0-2 (not worked with 9.7.0-15
			.ignoreComments() // this is not working even with newest Saxon 9.8.0-2
			.withComparisonController(ComparisonControllers.Default)
			.build();

		assertFalse(d.hasDifferences(), d.toString());
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
