package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.util.ResourceUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullWriter;
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Specimen;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.codesystems.DataAbsentReason;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class JsonParserR4Test extends BaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(JsonParserR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4();
	@Captor
	private ArgumentCaptor<IParserErrorHandler.IParseLocation> myParseLocationCaptor;
	@Mock
	private IParserErrorHandler myMockErrorHandler;

	private Bundle createBundleWithPatient() {
		Bundle b = new Bundle();
		b.setId("BUNDLEID");
		b.getMeta().addProfile("http://FOO");

		Patient p = new Patient();
		p.setId("PATIENTID");
		p.getMeta().addProfile("http://BAR");
		p.addName().addGiven("GIVEN");
		b.addEntry().setResource(p);
		return b;
	}

	@AfterEach
	public void afterEach() {
		ourCtx.getParserOptions().setAutoContainReferenceTargetsWithNoId(true);
		ourCtx.setStoreRawJson(false);
	}

	@ParameterizedTest
	@MethodSource("patientStrs")
	public void parseResource_withStoreRawJsonTrue_willStoreTheRawJsonOnTheResource(String thePatientStr) {
		ourCtx.setStoreRawJson(true);
		IParser parser = ourCtx.newJsonParser();

		// test
		Patient patient = parser.parseResource(Patient.class, thePatientStr);

		// verify
		String rawJson = ResourceUtil.getRawStringFromResourceOrNull(patient);
		assertEquals(thePatientStr, rawJson);
	}

	/**
	 * The following construct isn't actually valid FHIR, but we shouldn't barf when parsing it:
	 * <pre>
	 *    "name": "Test Org",
	 * 	"_name": {
	 * 		"extension": null
	 *   },
	 * </pre>
	 */
	@Test
	public void testNullExtension() throws IOException {
		// Setup
		String input = loadResource("/failing-json-file.json");
		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(myMockErrorHandler);

		// Test
		Organization org = parser.parseResource(Organization.class, input);

		// Verify
		assertEquals("Test Org", org.getName());
		assertEquals(0, org.getNameElement().getExtension().size());
		verify(myMockErrorHandler, times(3)).missingRequiredElement(myParseLocationCaptor.capture(), eq("url"));
		assertEquals("extension", myParseLocationCaptor.getAllValues().get(0).getParentElementName());
		assertEquals("extension", myParseLocationCaptor.getAllValues().get(1).getParentElementName());
		assertEquals("extension", myParseLocationCaptor.getAllValues().get(2).getParentElementName());
	}

	@Test
	public void testNonDomainResourcesHaveIdResourceTypeParsed() {
		//Test a non-domain resource
		String binaryPayload = "{\n" +
			"  \"resourceType\": \"Binary\",\n" +
			"  \"id\": \"b123\"\n" +
			"}\n";
		IBaseResource iBaseResource = ourCtx.newJsonParser().parseResource(binaryPayload);
		String resourceType = iBaseResource.getIdElement().getResourceType();
		assertEquals("Binary", resourceType);

		//Test a domain resource.
		String observationPayload = "{\n" +
			"  \"resourceType\": \"Observation\",\n" +
			"  \"id\": \"o123\"\n" +
			"}\n";
		IBaseResource obs = ourCtx.newJsonParser().parseResource(observationPayload);
		resourceType = obs.getIdElement().getResourceType();
		assertEquals("Observation", resourceType);
	}

	@Test
	public void testEntitiesNotConverted() throws IOException {
		Device input = loadResource(ourCtx, Device.class, "/entities-from-cerner.json");
		String narrative = input.getText().getDivAsString();
		ourLog.info(narrative);
	}

	@Test
	public void testEncodeExtensionWithUnknownType() throws IOException {

		Patient p = new Patient();
		p.addExtension("http://foo", new MyUnknownPrimitiveType());

		try {
			ourCtx.newJsonParser().encodeResourceToString(p);
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(1844) + "Unable to encode extension, unrecognized child element type: ca.uhn.fhir.parser.JsonParserR4Test.MyUnknownPrimitiveType", e.getMessage());
		}
	}

	@Test
	public void testNamespacePrefixTrimmedFromNarrative() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">" +
			 "<text>" +
			 "<xhtml:div>" +
			 "<xhtml:img src=\"foo\"/>" +
			 "@fhirabend" +
			 "</xhtml:div>" +
			 "</text>" +
			 "</Patient>";
		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, input);

		String expected = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><img src=\"foo\"/>@fhirabend</div>";
		assertEquals(expected, parsed.getText().getDiv().getValueAsString());

		String encoded = ourCtx.newJsonParser().encodeResourceToString(parsed);
		ourLog.info(encoded);
		assertThat(encoded).contains("\"div\":\"" + expected.replace("\"", "\\\"") + "\"");
	}

	@Test
	public void testNamespacePrefixStrippedOnJsonParse() {
		String input = "{\"resourceType\":\"Patient\",\"text\":{\"div\":\"<xhtml:div xmlns:xhtml=\\\"http://www.w3.org/1999/xhtml\\\"><xhtml:img src=\\\"foo\\\"/>@fhirabend</xhtml:div>\"}}";
		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, input);
		XhtmlNode div = parsed.getText().getDiv();

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"><img src=\"foo\"/>@fhirabend</div>", div.getValueAsString());

		String encoded = ourCtx.newXmlParser().encodeResourceToString(parsed);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\"><img src=\"foo\"/>@fhirabend</div></text></Patient>", encoded);
	}

	@Test
	public void testDontEncodeEmptyExtensionList() {
		String asXml = """
			 <Bundle xmlns="http://hl7.org/fhir">
			      <entry>
			         <resource>
			             <Composition>
			                 <section>
			                     <entry>
			                         <!--  Referenz auf PractitionerRole  -->
			                         <reference value="PractitionerRole/8f1ba38d-c4c1-4c49-ac2a-7ff0e56bc150" />
			                     </entry>
			                 </section>
			             </Composition>
			         </resource>
			     </entry>
			 </Bundle>
			 """;

		ourLog.info(asXml);

		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, asXml);

		String asString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(asString);
		assertThat(asString).doesNotContain("{ }");
	}

	@Test
	public void testEncodeExtensionOnBinaryData() {
		Binary b = new Binary();
		b.getDataElement().addExtension("http://foo", new StringType("AAA"));

		String output = ourCtx.newJsonParser().setSummaryMode(true).encodeResourceToString(b);
		assertEquals("{\"resourceType\":\"Binary\",\"meta\":{\"tag\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-ObservationValue\",\"code\":\"SUBSETTED\",\"display\":\"Resource encoded in summary mode\"}]}}", output);

		output = ourCtx.newJsonParser().setDontEncodeElements(Sets.newHashSet("*.id", "*.meta")).encodeResourceToString(b);
		assertEquals("{\"resourceType\":\"Binary\",\"_data\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}", output);
	}

	@Test
	public void testDontStripVersions() {
		FhirContext ctx = FhirContext.forR4();
		ctx.getParserOptions().setDontStripVersionsFromReferencesAtPaths("QuestionnaireResponse.questionnaire");

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.getQuestionnaireElement().setValueAsString("Questionnaire/123/_history/456");

		String output = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(qr);
		ourLog.info(output);

		assertThat(output).contains("\"Questionnaire/123/_history/456\"");
	}

	@Test
	public void testPrettyPrint() {
		ourCtx.getParserOptions().setDontStripVersionsFromReferencesAtPaths("QuestionnaireResponse.questionnaire");

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.getQuestionnaireElement().setValueAsString("Questionnaire/123/_history/456");

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(qr);
		ourLog.info(output);

		assertThat(output).contains("\n  \"resourceType\"");
	}

	/**
	 * See #814
	 */
	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwice() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		md.setMedication(new Reference(med));
		mr.setMedication(new Reference(med));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(md);
		ourLog.info(encoded);

		int idx = encoded.indexOf("\"Medication\"");
		assertThat(idx).isNotEqualTo(-1);

		idx = encoded.indexOf("\"Medication\"", idx + 1);
		assertEquals(-1, idx);
	}

	@Test
	public void testDuplicateContainedResourcesAcrossABundleAreReplicated() {
		Bundle b = new Bundle();
		Specimen specimen = new Specimen();
		Practitioner practitioner = new Practitioner();
		DiagnosticReport report = new DiagnosticReport();
		report.addSpecimen(new Reference(specimen));
		b.addEntry().setResource(report).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("/DiagnosticReport");

		Observation obs = new Observation();
		obs.addPerformer(new Reference(practitioner));
		obs.setSpecimen(new Reference(specimen));

		b.addEntry().setResource(obs).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("/Observation");

		/*
		 * Pass 1
		 */

		String encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(b);
		//Then: Diag should contain one local contained specimen
		assertThat(encoded).contains("[{\"resource\":{\"resourceType\":\"DiagnosticReport\",\"contained\":[{\"resourceType\":\"Specimen\",\"id\":\"" + specimen.getId().replaceFirst("#", "") + "\"}]");
		//Then: Obs should contain one local contained specimen, and one local contained pract
		assertThat(encoded).contains("\"resource\":{\"resourceType\":\"Observation\",\"contained\":[{\"resourceType\":\"Specimen\",\"id\":\"" + specimen.getId() + "\"},{\"resourceType\":\"Practitioner\",\"id\":\"" + practitioner.getId() + "\"}]");
		assertThat(encoded).contains("\"performer\":[{\"reference\":\"#" + practitioner.getId() + "\"}],\"specimen\":{\"reference\":\"#" + specimen.getId() + "\"}");

		/*
		 * Pass 2 - Make sure that multiple encode passes work correctly
		 */

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(b);
		//Then: Diag should contain one local contained specimen
		assertThat(encoded).contains("[{\"resource\":{\"resourceType\":\"DiagnosticReport\",\"contained\":[{\"resourceType\":\"Specimen\",\"id\":\"" + specimen.getId().replaceFirst("#", "") + "\"}]");
		//Then: Obs should contain one local contained specimen, and one local contained pract
		assertThat(encoded).contains("\"resource\":{\"resourceType\":\"Observation\",\"contained\":[{\"resourceType\":\"Specimen\",\"id\":\"" + specimen.getId() + "\"},{\"resourceType\":\"Practitioner\",\"id\":\"" + practitioner.getId() + "\"}]");
		assertThat(encoded).contains("\"performer\":[{\"reference\":\"#" + practitioner.getId() + "\"}],\"specimen\":{\"reference\":\"#" + specimen.getId() + "\"}");

		//Also, reverting the operation should work too!
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, encoded);
		IBaseResource resource1 = ((DiagnosticReport) bundle.getEntry().get(0).getResource()).getSpecimenFirstRep().getResource();
		IBaseResource resource = ((Observation) bundle.getEntry().get(1).getResource()).getSpecimen().getResource();
		assertThat(resource1.getIdElement().getIdPart()).isEqualTo(resource.getIdElement().getIdPart());
		assertThat(resource1).isNotSameAs(resource);

	}

	@Test
	public void testContainedResourcesNotAutoContainedWhenConfiguredNotToDoSo() {
		MedicationDispense md = new MedicationDispense();
		md.addIdentifier().setValue("DISPENSE");

		Medication med = new Medication();
		med.getCode().setText("MED");
		md.setMedication(new Reference(med));

		ourCtx.getParserOptions().setAutoContainReferenceTargetsWithNoId(false);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(md);
		assertEquals("{\"resourceType\":\"MedicationDispense\",\"identifier\":[{\"value\":\"DISPENSE\"}],\"medicationReference\":{}}", encoded);

		ourCtx.getParserOptions().setAutoContainReferenceTargetsWithNoId(true);
		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(md);
		String withoutHash = med.getId();
		String guidWithHash = "#" + withoutHash;
		assertThat(encoded).contains("{\"resourceType\":\"MedicationDispense\",\"contained\":[{\"resourceType\":\"Medication\",\"id\":\"" + withoutHash + "\",\"code\":{\"text\":\"MED\"}}],\"identifier\":[{\"value\":\"DISPENSE\"}],\"medicationReference\":{\"reference\":\"" + guidWithHash + "\"}}"); //Note we dont check exact ID since its a GUID
	}

	@Test
	public void testParseBundleWithMultipleNestedContainedResources() throws Exception {
		String text = loadResource("/bundle-with-two-patient-resources.json");

		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, text);
		assertEquals(Boolean.TRUE, bundle.getUserData(BaseParser.RESOURCE_CREATED_BY_PARSER));
		assertEquals(Boolean.TRUE, bundle.getEntry().get(0).getResource().getUserData(BaseParser.RESOURCE_CREATED_BY_PARSER));
		assertEquals(Boolean.TRUE, bundle.getEntry().get(1).getResource().getUserData(BaseParser.RESOURCE_CREATED_BY_PARSER));

		assertEquals("12346", getPatientIdValue(bundle, 0));
		assertEquals("12345", getPatientIdValue(bundle, 1));
	}

	private String getPatientIdValue(Bundle input, int entry) {
		final DocumentReference documentReference = (DocumentReference) input.getEntry().get(entry).getResource();
		final Patient patient = (Patient) documentReference.getSubject().getResource();
		return patient.getIdentifier().get(0).getValue();
	}

	/**
	 * See #814
	 */
	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwiceWithManualIds() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		mr.setId("#MR");
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		med.setId("#MED");
		md.setMedication(new Reference(med));
		mr.setMedication(new Reference(med));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(md);
		ourLog.info(encoded);

		int idx = encoded.indexOf("\"Medication\"");
		assertThat(idx).isNotEqualTo(-1);

		idx = encoded.indexOf("\"Medication\"", idx + 1);
		assertEquals(-1, idx);

	}

	/*
	 * See #814
	 */
	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwiceWithManualIdsAndManualAddition() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		mr.setId("#MR");
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		med.setId("#MED");

		Reference medRef = new Reference();
		medRef.setReference("#MED");
		md.setMedication(medRef);
		mr.setMedication(medRef);

		md.getContained().add(mr);
		md.getContained().add(med);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(md);
		ourLog.info(encoded);

		int idx = encoded.indexOf("\"Medication\"");
		assertThat(idx).isNotEqualTo(-1);

		idx = encoded.indexOf("\"Medication\"", idx + 1);
		assertEquals(-1, idx);

	}

	/**
	 * Make sure we can perform parallel parse/encodes against the same
	 * FhirContext instance when deferred model scanning is enabled without
	 * running into threading issues.
	 */
	@Test
	public void testEncodeAndDecodeMultithreadedWithDeferredModelScanning() throws IOException, ExecutionException, InterruptedException {
		String input = loadResource("/multi-thread-parsing-issue-bundle.json");

		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int pass = 0; pass < 10; pass++) {
			ourLog.info("Starting pass: {}", pass);

			FhirContext parseCtx = FhirContext.forR4();
			parseCtx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
			List<Future<Bundle>> bundleFutures = new ArrayList<>();
			for (int readIdx = 0; readIdx < 10; readIdx++) {
				bundleFutures.add(executor.submit(() -> parseCtx.newJsonParser().parseResource(Bundle.class, input)));
			}

			List<Bundle> parsedBundles = new ArrayList<>();
			for (Future<Bundle> nextFuture : bundleFutures) {
				Bundle nextBundle = nextFuture.get();
				parsedBundles.add(nextBundle);
			}

			FhirContext encodeCtx = FhirContext.forR4();
			encodeCtx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
			List<Future<String>> encodeFutures = new ArrayList<>();
			for (Bundle nextBundle : parsedBundles) {
				encodeFutures.add(executor.submit(() -> encodeCtx.newJsonParser().encodeResourceToString(nextBundle)));
			}

			List<String> outputs = new ArrayList<>();
			for (Future<String> nextFuture : encodeFutures) {
				outputs.add(nextFuture.get());
			}

			assertEquals(outputs.get(0), outputs.get(1));
		}
	}

	@Test
	public void testEncodeAndParseUnicodeCharacterInNarrative() {
		Patient p = new Patient();
		p.getText().getDiv().setValueAsString("<div>Copy © 1999</div>");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
		ourLog.info(encoded);

		p = (Patient) ourCtx.newJsonParser().parseResource(encoded);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">Copy © 1999</div>", p.getText().getDivAsString());
	}

	@Test
	public void testEncodeAndParseBundleWithFullUrlAndResourceIdMismatch() {

		MessageHeader header = new MessageHeader();
		header.setId("1.1.1.1");
		header.setDefinition("Hello");

		Bundle input = new Bundle();
		input
			 .addEntry()
			 .setFullUrl("urn:uuid:0.0.0.0")
			 .setResource(header);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);

		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded).containsSubsequence(
			 "\"fullUrl\": \"urn:uuid:0.0.0.0\"",
			 "\"id\": \"1.1.1.1\""
		);

		input = ourCtx.newJsonParser().parseResource(Bundle.class, encoded);
		assertEquals("urn:uuid:0.0.0.0", input.getEntry().get(0).getFullUrl());
		assertEquals("MessageHeader/1.1.1.1", input.getEntry().get(0).getResource().getId());

	}

	@Test
	public void testParseSingleQuotes() {
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, "{ 'resourceType': 'Bundle', 'id': '123' }");
		assertEquals("123", bundle.getIdElement().getIdPart());
	}

	@Test
	public void testEncodeBinary() {
		Binary b = new Binary();
		b.setContent(new byte[]{0, 1, 2, 3, 4});
		b.setContentType("application/octet-stream");

		IParser parser = ourCtx.newJsonParser().setPrettyPrint(false);
		String output = parser.encodeResourceToString(b);
		assertEquals("{\"resourceType\":\"Binary\",\"contentType\":\"application/octet-stream\",\"data\":\"AAECAwQ=\"}", output);
	}

	@Test
	public void testAlwaysUseUnixNewlines() {
		Patient p = new Patient();
		p.setId("1");
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		assertThat(encoded).isEqualTo("{\n" +
			 "  \"resourceType\": \"Patient\",\n" +
			 "  \"id\": \"1\"\n" +
			 "}");
	}

	@Test
	public void testEncodeWithInvalidExtensionMissingUrl() {

		Patient p = new Patient();
		Extension root = p.addExtension();
		root.setValue(new StringType("ROOT_VALUE"));

		// Lenient error handler
		IParser parser = ourCtx.newJsonParser();
		String output = parser.encodeResourceToString(p);
		ourLog.info("Output: {}", output);
		assertThat(output).contains("ROOT_VALUE");

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

		// According to issue4129, all error handlers should reject malformed resources
		// Lenient error handler
		IParser parser = ourCtx.newJsonParser();
		try {
			parser.encodeResourceToString(p);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1827) + "[element=\"Patient(res).extension\"] Extension contains both a value and nested extensions", e.getMessage());
		}

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
	public void testEncodeWithInvalidExtensionContainingValueAndNestedExtensions_withDisableAllErrorsShouldSucceed() {

		Patient p = new Patient();
		Extension root = p.addExtension();
		root.setUrl("http://root");
		root.setValue(new StringType("ROOT_VALUE"));
		Extension child = root.addExtension();
		child.setUrl("http://child");
		child.setValue(new StringType("CHILD_VALUE"));

		// Lenient error handler - should parse successfully with no error
		LenientErrorHandler errorHandler = new LenientErrorHandler(true).disableAllErrors();
		IParser parser = ourCtx.newJsonParser().setParserErrorHandler(errorHandler);
		String output = parser.encodeResourceToString(p);
		ourLog.info("Output: {}", output);
		assertThat(output).contains("http://root");
		assertThat(output).contains("ROOT_VALUE");
		assertThat(output).contains("http://child");
		assertThat(output).contains("CHILD_VALUE");
		assertEquals(false, errorHandler.isErrorOnInvalidExtension());
		assertEquals(false, errorHandler.isErrorOnInvalidValue());
	}

	@Test
	public void testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalFirst() {

		Observation obs = new Observation();

		Patient pt = new Patient();
		pt.setId("1");
		pt.addName().setFamily("FAM");
		obs.getSubject().setReference("#1");
		obs.getContained().add(pt);

		Encounter enc = new Encounter();
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		obs.getEncounter().setResource(enc);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = ourCtx.newJsonParser().parseResource(Observation.class, encoded);
		assertEquals("1", obs.getContained().get(0).getId());
		assertEquals(enc.getId(), obs.getContained().get(1).getId());

		pt = (Patient) obs.getSubject().getResource();
		assertEquals("FAM", pt.getNameFirstRep().getFamily());

		enc = (Encounter) obs.getEncounter().getResource();
		assertEquals(Encounter.EncounterStatus.ARRIVED, enc.getStatus());
	}

	@Test
	public void testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalLast() {

		Observation obs = new Observation();

		Patient pt = new Patient();
		pt.addName().setFamily("FAM");
		obs.getSubject().setResource(pt);

		Encounter enc = new Encounter();
		enc.setId("#1");
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		obs.getEncounter().setReference("#1");
		obs.getContained().add(enc);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = ourCtx.newJsonParser().parseResource(Observation.class, encoded);
		assertEquals("1", obs.getContained().get(0).getId());
		assertEquals(pt.getId(), obs.getContained().get(1).getId());

		pt = (Patient) obs.getSubject().getResource();
		assertEquals("FAM", pt.getNameFirstRep().getFamily());

		enc = (Encounter) obs.getEncounter().getResource();
		assertEquals(Encounter.EncounterStatus.ARRIVED, enc.getStatus());
	}

	@Test
	public void testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalLast2() {

		MedicationRequest mr = new MedicationRequest();
		Practitioner pract = new Practitioner().setActive(true);
		mr.getRequester().setResource(pract);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(mr);
		ourLog.info(encoded);
		mr = ourCtx.newJsonParser().parseResource(MedicationRequest.class, encoded);

		Medication med = new Medication().setStatus(Medication.MedicationStatus.ACTIVE);
		mr.setMedication(new Reference(med));
		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(mr);
		ourLog.info(encoded);
		mr = ourCtx.newJsonParser().parseResource(MedicationRequest.class, encoded);

		assertEquals(pract.getId(),mr.getContained().get(0).getId());
		assertEquals(med.getId(), mr.getContained().get(1).getId());

	}

	@Test
	public void testExcludeNothing() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
//		excludes.add("*.id");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded).contains("BUNDLEID");
		assertThat(encoded).contains("http://FOO");
		assertThat(encoded).contains("PATIENTID");
		assertThat(encoded).contains("http://BAR");
		assertThat(encoded).contains("GIVEN");

		b = parser.parseResource(Bundle.class, encoded);

		assertEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertEquals("Patient/PATIENTID", b.getEntry().get(0).getResource().getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	@Disabled
	public void testExcludeRootStuff() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("id");
		excludes.add("meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded).doesNotContain("BUNDLEID");
		assertThat(encoded).doesNotContain("http://FOO");
		assertThat(encoded).contains("PATIENTID");
		assertThat(encoded).contains("http://BAR");
		assertThat(encoded).contains("GIVEN");

		b = parser.parseResource(Bundle.class, encoded);

		assertThat(b.getIdElement().getIdPart()).isNotEqualTo("BUNDLEID");
		assertEquals("Patient/PATIENTID", b.getEntry().get(0).getResource().getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testExcludeStarDotStuff() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("*.id");
		excludes.add("*.meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded).doesNotContain("BUNDLEID");
		assertThat(encoded).doesNotContain("http://FOO");
		assertThat(encoded).doesNotContain("PATIENTID");
		assertThat(encoded).doesNotContain("http://BAR");
		assertThat(encoded).contains("GIVEN");

		b = parser.parseResource(Bundle.class, encoded);

		assertThat(b.getIdElement().getIdPart()).isNotEqualTo("BUNDLEID");
		assertThat(b.getEntry().get(0).getResource().getId()).isNotEqualTo("Patient/PATIENTID");
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	/**
	 * Test that long JSON strings don't get broken up
	 */
	@Test
	public void testNoBreakInLongString() {
		String longString = StringUtils.leftPad("", 100000, 'A');

		Patient p = new Patient();
		p.addName().setFamily(longString);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);

		assertThat(encoded).contains(longString);
	}

	@Test
	public void testParseAndEncodeExtensionWithValueWithExtension() {
		String input = "{\n" +
			 "  \"resourceType\": \"Patient\",\n" +
			 "  \"extension\": [\n" +
			 "    {\n" +
			 "      \"url\": \"https://purl.org/elab/fhir/network/StructureDefinition/1/BirthWeight\",\n" +
			 "      \"_valueDecimal\": {\n" +
			 "        \"extension\": [\n" +
			 "          {\n" +
			 "            \"url\": \"http://www.hl7.org/fhir/extension-data-absent-reason.html\",\n" +
			 "            \"valueCoding\": {\n" +
			 "              \"system\": \"http://hl7.org/fhir/ValueSet/birthweight\",\n" +
			 "              \"code\": \"Underweight\",\n" +
			 "              \"userSelected\": false\n" +
			 "            }\n" +
			 "          }\n" +
			 "        ]\n" +
			 "      }\n" +
			 "    }\n" +
			 "  ],\n" +
			 "  \"identifier\": [\n" +
			 "    {\n" +
			 "      \"system\": \"https://purl.org/elab/fhir/network/StructureDefinition/1/EuroPrevallStudySubjects\",\n" +
			 "      \"value\": \"1\"\n" +
			 "    }\n" +
			 "  ],\n" +
			 "  \"gender\": \"female\"\n" +
			 "}";

		IParser jsonParser = ourCtx.newJsonParser();
		IParser xmlParser = ourCtx.newXmlParser();
		jsonParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));
		xmlParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));

		Patient parsed = jsonParser.parseResource(Patient.class, input);

		ourLog.info(jsonParser.setPrettyPrint(true).encodeResourceToString(parsed));
		assertThat(xmlParser.encodeResourceToString(parsed)).contains("Underweight");
		assertThat(jsonParser.encodeResourceToString(parsed)).contains("Underweight");

	}

	/**
	 * See #1793
	 */
	@Test
	public void testParseEmptyAttribute() {
		String input = "{\n" +
			 "  \"resourceType\": \"Patient\",\n" +
			 "  \"identifier\": [\n" +
			 "    {\n" +
			 "      \"system\": \"https://example.com\",\n" +
			 "      \"value\": \"\"\n" +
			 "    }\n" +
			 "  ]\n" +
			 "}";

		IParser jsonParser = ourCtx.newJsonParser();
		jsonParser.setParserErrorHandler(new StrictErrorHandler());
		try {
			jsonParser.parseResource(Patient.class, input);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1821) + "[element=\"value\"] Invalid attribute value \"\": Attribute value must not be empty (\"\")", e.getMessage());
		}

	}

	/**
	 * See #3890
	 */
	@Test
	public void testEncodeExtensionWithReferenceObjectValue() {

		Appointment appointment = new Appointment();
		appointment.setId("123");

		Meta meta = new Meta();
		Extension extension = new Extension();
		extension.setUrl("http://example-source-team.com");
		extension.setValue(new Reference(new Organization().setId("546")));
		meta.addExtension(extension);
		appointment.setMeta(meta);

		var parser = ourCtx.newJsonParser();
		String output = parser.encodeResourceToString(appointment);
		ourLog.info("Output: {}", output);

		Appointment input = parser.parseResource(Appointment.class, output);

		assertNotNull(input.getMeta().getExtensionByUrl("http://example-source-team.com"));
	}

	@Test
	public void testParseExtensionWithUriValue_BuiltInStructure() {
		String input = "{\n" +
			 "\"resourceType\": \"Basic\",\n" +
			 "\"meta\": {\n" +
			 "\"profile\": [ \"http://mycustom.url\" ]\n" +
			 "},\n" +
			 "\"extension\": [ {\n" +
			 "\"url\": \"http://myValue.url\",\n" +
			 "\"valueUuid\": \"ae644c07-1d4b-4ca4-bbf3-bd2023e294e5\"\n" +
			 "} ]\n" +
			 "}";

		IParser jsonParser = ourCtx.newJsonParser();
		Basic parsed = jsonParser.parseResource(Basic.class, input);
		assertEquals("ae644c07-1d4b-4ca4-bbf3-bd2023e294e5", parsed.getExtensionByUrl("http://myValue.url").getValueAsPrimitive().getValueAsString());
	}

	@Test
	public void testParseExtensionWithUriValue_CustomStructure() {
		String input = "{\n" +
			 "\"resourceType\": \"Basic\",\n" +
			 "\"meta\": {\n" +
			 "\"profile\": [ \"http://mycustom.url\" ]\n" +
			 "},\n" +
			 "\"extension\": [ {\n" +
			 "\"url\": \"http://myValue.url\",\n" +
			 "\"valueUuid\": \"ae644c07-1d4b-4ca4-bbf3-bd2023e294e5\"\n" +
			 "} ]\n" +
			 "}";

		IParser jsonParser = ourCtx.newJsonParser();
		MyCustom parsed = jsonParser.parseResource(MyCustom.class, input);
		assertEquals("ae644c07-1d4b-4ca4-bbf3-bd2023e294e5", parsed.getValue().getValue());
	}

	@Test
	public void testParseExtensionOnPrimitive() throws IOException {
		String input = IOUtils.toString(JsonParserR4Test.class.getResourceAsStream("/extension-on-line.txt"), Constants.CHARSET_UTF8);
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Patient pt = parser.parseResource(Patient.class, input);

		StringType line0 = pt.getAddressFirstRep().getLine().get(0);
		assertEquals("535 Sheppard Avenue West, Unit 1907", line0.getValue());
		Extension houseNumberExt = line0.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-houseNumber");
		assertEquals("535", ((StringType) houseNumberExt.getValue()).getValue());

	}

	private Composition createComposition(String sectionText) {
		Composition c = new Composition();
		Narrative compositionText = new Narrative().setStatus(Narrative.NarrativeStatus.GENERATED);
		compositionText.setDivAsString("Composition");
		Narrative compositionSectionText = new Narrative().setStatus(Narrative.NarrativeStatus.GENERATED);
		compositionSectionText.setDivAsString(sectionText);
		c.setText(compositionText);
		c.addSection().setText(compositionSectionText);
		return c;
	}

	/**
	 * See #402 (however JSON is fine)
	 */
	@Test
	public void testEncodingTextSection() {

		String sectionText = "sectionText";
		Composition composition = createComposition(sectionText);

		String encoded = ourCtx.newJsonParser().encodeResourceToString(composition);
		ourLog.info(encoded);

		int idx = encoded.indexOf(sectionText);
		assertThat(idx).isNotEqualTo(-1);
	}

	/**
	 * 2019-09-19 - Pre #1489
	 * 18:24:48.548 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:483] - Encoded 200 passes - 50ms / pass - 19.7 / second
	 * 18:24:52.472 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:483] - Encoded 300 passes - 47ms / pass - 21.3 / second
	 * 18:24:56.428 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:483] - Encoded 400 passes - 45ms / pass - 22.2 / second
	 * 18:25:00.463 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:483] - Encoded 500 passes - 44ms / pass - 22.6 / second
	 * <p>
	 * 2019-09-19 - Post #1489
	 * 15:20:30.134 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:574] - Encoded 800 passes - 28ms / pass - 34.5 / second
	 * 15:20:32.986 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:574] - Encoded 900 passes - 28ms / pass - 34.6 / second
	 * 15:20:35.865 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:574] - Encoded 1000 passes - 28ms / pass - 34.6 / second
	 * 15:20:38.797 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:574] - Encoded 1100 passes - 28ms / pass - 34.6 / second
	 * 15:20:41.708 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:574] - Encoded 1200 passes - 28ms / pass - 34.5 / second
	 * 15:20:44.722 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:574] - Encoded 1300 passes - 29ms / pass - 34.4 / second
	 * 15:20:47.716 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:574] - Encoded 1400 passes - 29ms / pass - 34.4 / second
	 * <p>
	 * 2020-02-27 - Post #1673
	 * 21:27:25.817 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:609] - Encoded 1100 passes - 28ms / pass - 35.5 / second
	 * 21:27:28.598 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:609] - Encoded 1200 passes - 28ms / pass - 35.5 / second
	 * 21:27:31.436 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:609] - Encoded 1300 passes - 28ms / pass - 35.5 / second
	 * 21:27:34.246 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:609] - Encoded 1400 passes - 28ms / pass - 35.5 / second
	 * 21:27:37.013 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:609] - Encoded 1500 passes - 28ms / pass - 35.6 / second
	 * 21:27:39.874 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:609] - Encoded 1600 passes - 28ms / pass - 35.5 / second
	 */
	@Test
	@Disabled
	public void testTimingsOutput() throws IOException {

		Bundle b = createBigBundle();

		IParser parser = ourCtx.newJsonParser();

		for (int i = 0; i < 500; i++) {
			parser.encodeResourceToWriter(b, new NullWriter());
			if (i % 100 == 0) {
				ourLog.info("Warm-up Encoded {} passes", i);
			}
		}

		StopWatch sw = new StopWatch();
		for (int i = 0; ; i++) {
			parser.encodeResourceToWriter(b, new NullWriter());
			if (i % 100 == 0) {
				ourLog.info("Encoded {} passes - {} / pass - {} / second", i, sw.formatMillisPerOperation(i), sw.formatThroughput(i, TimeUnit.SECONDS));
			}
		}

	}

	/**
	 * 2019-09-19 - Pre #1489
	 * 18:33:08.720 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:495] - Encoded 200 passes - 47ms / pass - 21.2 / second
	 * 18:33:12.453 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:495] - Encoded 300 passes - 43ms / pass - 22.7 / second
	 * 18:33:16.195 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:495] - Encoded 400 passes - 42ms / pass - 23.6 / second
	 * 18:33:19.912 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:495] - Encoded 500 passes - 41ms / pass - 24.2 / second
	 * <p>
	 * 2019-09-19 - Post #1489
	 * 20:44:38.557 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:500] - Encoded 200 passes - 37ms / pass - 27.0 / second
	 * 20:44:41.459 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:500] - Encoded 300 passes - 34ms / pass - 29.1 / second
	 * 20:44:44.434 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:500] - Encoded 400 passes - 33ms / pass - 30.1 / second
	 * 20:44:47.372 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:500] - Encoded 500 passes - 32ms / pass - 30.8 / second
	 */
	@Test
	@Disabled
	public void testTimingsOutputXml() throws IOException {

		Bundle b = createBigBundle();

		IParser parser = ourCtx.newXmlParser();
		StopWatch sw = new StopWatch();
		for (int i = 0; ; i++) {
			parser.encodeResourceToWriter(b, new NullWriter());
			if (i % 100 == 0) {
				ourLog.info("Encoded {} passes - {} / pass - {} / second", i, sw.formatMillisPerOperation(i), sw.formatThroughput(i, TimeUnit.SECONDS));
			}
		}

	}

	/**
	 * 2019-09-19
	 * 15:22:30.758 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 1700 passes - 12ms / pass - 79.3 / second
	 * 15:22:31.968 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 1800 passes - 12ms / pass - 79.5 / second
	 * 15:22:33.223 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 1900 passes - 12ms / pass - 79.5 / second
	 * 15:22:34.459 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 2000 passes - 12ms / pass - 79.6 / second
	 * 15:22:35.696 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 2100 passes - 12ms / pass - 79.7 / second
	 * 15:22:36.983 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 2200 passes - 12ms / pass - 79.6 / second
	 * 15:22:38.203 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 2300 passes - 12ms / pass - 79.7 / second
	 * 15:22:39.456 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 2400 passes - 12ms / pass - 79.7 / second
	 * 15:22:40.699 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 2500 passes - 12ms / pass - 79.7 / second
	 * 15:22:42.135 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:638] - Parsed 2600 passes - 12ms / pass - 79.3 / second
	 * <p>
	 * 2020-02-27 - Post #1673
	 * 21:29:38.157 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:687] - Parsed 2200 passes - 11ms / pass - 83.4 / second
	 * 21:29:39.374 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:687] - Parsed 2300 passes - 12ms / pass - 83.3 / second
	 * 21:29:40.576 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:687] - Parsed 2400 passes - 12ms / pass - 83.3 / second
	 * 21:29:41.778 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:687] - Parsed 2500 passes - 12ms / pass - 83.3 / second
	 * 21:29:42.999 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:687] - Parsed 2600 passes - 12ms / pass - 83.3 / second
	 */
	@Test
	@Disabled
	public void testTimingsInput() {
		Bundle b = createBigBundle();
		IParser parser = ourCtx.newJsonParser();
		String input = parser.encodeResourceToString(b);

		for (int i = 0; i < 500; i++) {
			parser.parseResource(input);
			if (i % 100 == 0) {
				ourLog.info("Warm up parsed {} passes", i);
			}
		}


		StopWatch sw = new StopWatch();
		for (int i = 0; ; i++) {
			parser.parseResource(input);
			if (i % 100 == 0) {
				ourLog.info("Parsed {} passes - {} / pass - {} / second", i, sw.formatMillisPerOperation(i), sw.formatThroughput(i, TimeUnit.SECONDS));
			}
		}

	}

	/**
	 * 2019-09-19
	 * 18:32:04.518 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:513] - Parsed 200 passes - 37ms / pass - 26.8 / second
	 * 18:32:07.829 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:513] - Parsed 300 passes - 35ms / pass - 27.8 / second
	 * 18:32:11.087 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:513] - Parsed 400 passes - 35ms / pass - 28.5 / second
	 * 18:32:14.357 [main] INFO  ca.uhn.fhir.parser.JsonParserR4Test [JsonParserR4Test.java:513] - Parsed 500 passes - 34ms / pass - 28.9 / second
	 */
	@Test
	@Disabled
	public void testTimingsInputXml() throws IOException {
		Bundle b = createBigBundle();
		IParser parser = ourCtx.newXmlParser();
		String input = parser.encodeResourceToString(b);

		StopWatch sw = new StopWatch();
		for (int i = 0; ; i++) {
			parser.parseResource(input);
			if (i % 100 == 0) {
				ourLog.info("Parsed {} passes - {} / pass - {} / second", i, sw.formatMillisPerOperation(i), sw.formatThroughput(i, TimeUnit.SECONDS));
			}
		}

	}

	private Bundle createBigBundle() {
		Observation obs = new Observation();

		Bundle b = new Bundle();

		for (int i = 0; i < 100; i++) {

			Patient pt = new Patient();
			pt.addName().setFamily("FAM");
			obs.getSubject().setResource(pt);

			Encounter enc = new Encounter();
			enc.setId("#1");
			enc.setStatus(Encounter.EncounterStatus.ARRIVED);
			obs.getEncounter().setReference("#1");
			obs.getContained().add(enc);
			obs.setEffective(new DateTimeType(new Date()));
			obs.addIdentifier()
				 .setSystem("http://foo")
				 .setValue("blah");
			obs.setValue(new Quantity().setSystem("UCUM").setCode("mg/L").setUnit("mg/L").setValue(123.567d));

			b.addEntry().setResource(obs);

		}
		return b;
	}

	/**
	 * Ensure that a contained bundle doesn't cause a crash
	 */
	@Test
	public void testEncodeContainedBundle() {
		String auditEvent = "{\n" +
			 "  \"resourceType\": \"AuditEvent\",\n" +
			 "  \"contained\": [ {\n" +
			 "    \"resourceType\": \"Bundle\",\n" +
			 "    \"id\": \"REASONS\",\n" +
			 "    \"entry\": [ {\n" +
			 "      \"resource\": {\n" +
			 "        \"resourceType\": \"Condition\",\n" +
			 "        \"id\": \"123\"\n" +
			 "      }\n" +
			 "    } ]\n" +
			 "  }, {\n" +
			 "    \"resourceType\": \"MeasureReport\",\n" +
			 "    \"id\": \"MRPT5000602611RD\",\n" +
			 "    \"evaluatedResource\": [ {\n" +
			 "      \"reference\": \"#REASONS\"\n" +
			 "    } ]\n" +
			 "  } ],\n" +
			 "  \"entity\": [ {\n" +
			 "    \"what\": {\n" +
			 "      \"reference\": \"#MRPT5000602611RD\"\n" +
			 "    }\n" +
			 "  } ]\n" +
			 "}";
		AuditEvent ae = ourCtx.newJsonParser().parseResource(AuditEvent.class, auditEvent);
		String auditEventAsString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(ae);
		assertEquals(auditEvent, auditEventAsString);
	}

	/**
	 * Ensure that a contained bundle doesn't cause a crash
	 */
	@Test
	public void testParseAndEncodePreservesContainedResourceOrder() {
		String auditEvent = "{\n" +
			 "  \"resourceType\": \"AuditEvent\",\n" +
			 "  \"contained\": [ {\n" +
			 "    \"resourceType\": \"Observation\",\n" +
			 "    \"id\": \"A\",\n" +
			 "    \"identifier\": [ {\n" +
			 "      \"value\": \"A\"\n" +
			 "    } ]\n" +
			 "  }, {\n" +
			 "    \"resourceType\": \"Observation\",\n" +
			 "    \"id\": \"B\",\n" +
			 "    \"identifier\": [ {\n" +
			 "      \"value\": \"B\"\n" +
			 "    } ]\n" +
			 "  } ],\n" +
			 "  \"entity\": [ {\n" +
			 "    \"what\": {\n" +
			 "      \"reference\": \"#B\"\n" +
			 "    }\n" +
			 "  }, {\n" +
			 "    \"what\": {\n" +
			 "      \"reference\": \"#A\"\n" +
			 "    }\n" +
			 "  } ]\n" +
			 "}";

		ourLog.info("Input: {}", auditEvent);
		AuditEvent ae = ourCtx.newJsonParser().parseResource(AuditEvent.class, auditEvent);
		assertEquals("A", ae.getContained().get(0).getId());
		assertEquals("B", ae.getContained().get(1).getId());
		assertEquals("#B", ae.getEntity().get(0).getWhat().getReference());
		assertEquals("#A", ae.getEntity().get(1).getWhat().getReference());

		String serialized = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(ae);
		assertEquals(auditEvent, serialized);

	}

	@Test
	public void testEncodeToString_PrimitiveDataType() {
		DecimalType object = new DecimalType("123.456000");
		String expected = "123.456000";
		String actual = ourCtx.newJsonParser().encodeToString(object);
		assertEquals(expected, actual);
	}

	@Test
	public void testEncodeToString_CompoundTypeWithReference() {
		Identifier identifier = new Identifier();
		identifier.setSystem("http://system.org");
		identifier.setValue("123");
		Reference reference = new Reference("Organization/1");
		identifier.setAssigner(reference);
		String expected = "{\"system\":\"http://system.org\",\"value\":\"123\",\"assigner\":{\"reference\":\"Organization/1\"}}";
		String actual = ourCtx.newJsonParser().encodeToString(identifier);
		assertEquals(expected, actual);
	}

	@Test
	public void testEncodeToString_Resource() {
		Patient p = new Patient();
		p.setId("Patient/123");
		p.setActive(true);
		String expected = "{\"resourceType\":\"Patient\",\"id\":\"123\",\"active\":true}";
		String actual = ourCtx.newJsonParser().encodeToString(p);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseIntoObject() {
		String expected = "{\"system\":\"http://system.org\",\"value\":\"123\",\"assigner\":{\"reference\":\"Organization/1\"}}";

		// Test
		Identifier target = new Identifier();
		ourCtx.newJsonParser().parseInto(expected, target);

		// Verify
		assertEquals("http://system.org", target.getSystem());
		assertEquals("123", target.getValue());
		assertEquals("Organization/1", target.getAssigner().getReference());
	}

	@Test
	public void testParseIntoObject_InvalidValue() {
		// Test
		Patient target = new Patient();
		DataFormatException e = assertThrows(DataFormatException.class, () -> ourCtx.newJsonParser().parseInto("2020-01-01", target));

		// Verify
		assertThat(e.getMessage()).contains("Failed to parse JSON encoded FHIR content");
	}


	@Test
	public void testParseIntoPrimitive() {
		String expected = "2020-02-20T12:12:01.123-05:00";

		// Test
		DateTimeType target = new DateTimeType();
		ourCtx.newJsonParser().parseInto(expected, target);

		// Verify
		assertEquals(expected, target.getValueAsString());
		assertThat(target.getValue()).isAfter(Instant.parse("2020-02-19T12:12:01.123-05:00"));
		assertThat(target.getValue()).isBefore(Instant.parse("2020-02-21T12:12:01.123-05:00"));
	}

	@Test
	public void testParseIntoPrimitive_InvalidValue() {
		// Test
		DateTimeType target = new DateTimeType();
		DataFormatException e = assertThrows(DataFormatException.class, () -> ourCtx.newJsonParser().parseInto("\"birthDate\":\"2020-01-01\"", target));

		// Verify
		assertThat(e.getMessage()).contains("Invalid date/time");
	}




	@Test
	public void testObjectWithBothPrimitiverAndArrayAlternatives() {
		String resource = "{\n" +
			 "    \"resourceType\": \"Practitioner\",\n" +
			 "    \"id\": \"1\",\n" +
			 "    \"name\": [{\n" +
			 "            \"_family\": {\n" +
			 "                \"extension\": [{\n" +
			 "                        \"url\": \"http://hl7.org/fhir/StructureDefinition/data-absent-reason\",\n" +
			 "                        \"valueString\": \"masked\"\n" +
			 "                    }\n" +
			 "                ]\n" +
			 "            },\n" +
			 "            \"given\": [\n" +
			 "                null\n" +
			 "            ],\n" +
			 "            \"_given\": [{\n" +
			 "                    \"extension\": [{\n" +
			 "                            \"url\": \"http://hl7.org/fhir/StructureDefinition/data-absent-reason\",\n" +
			 "                            \"valueString\": \"masked\"\n" +
			 "                        }\n" +
			 "                    ]\n" +
			 "                }\n" +
			 "            ]\n" +
			 "        }\n" +
			 "    ]\n" +
			 "}\n";
		Practitioner practitioner = ourCtx.newJsonParser().parseResource(Practitioner.class, resource);
		HumanName humanName = practitioner.getNameFirstRep();
		StringType given = humanName.getGiven().get(0);
		assertTrue(given.getExtension().stream().allMatch(ext -> DataAbsentReason.MASKED.toCode().equals(ext.getValue().primitiveValue())));
		assertTrue(humanName.getFamilyElement().getExtension().stream().allMatch(ext -> DataAbsentReason.MASKED.toCode().equals(ext.getValue().primitiveValue())));
	}

	@Test
	public void testEncodeToString_GeneralPurposeDataType() {
		HumanName name = new HumanName();
		name.setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		name.addExtension("http://foo", new StringType("bar"));

		String expected = "{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"bar\"}],\"family\":\"Simpson\",\"given\":[\"Homer\",\"Jay\"]}";
		String actual = ourCtx.newJsonParser().encodeToString(name);
		assertEquals(expected, actual);
	}

	@Test
	public void testEncodeToString_BackboneElement() {
		Patient.PatientCommunicationComponent communication = new Patient().addCommunication();
		communication.setPreferred(true);
		communication.getLanguage().setText("English");

		String expected = "{\"language\":{\"text\":\"English\"},\"preferred\":true}";
		String actual = ourCtx.newJsonParser().encodeToString(communication);
		assertEquals(expected, actual);
	}

	@Test
	public void testEncodeBundleWithCrossReferenceFullUrlsAndNoIds() {
		Bundle bundle = createBundleWithCrossReferenceFullUrlsAndNoIds();

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(output);

		assertThat(output).doesNotContain("\"contained\"");
		assertThat(output).doesNotContain("\"id\"");
		assertThat(output).containsSubsequence(
			 "\"fullUrl\": \"urn:uuid:9e9187c1-db6d-4b6f-adc6-976153c65ed7\",",
			 "\"resourceType\": \"Patient\"",
			 "\"fullUrl\": \"urn:uuid:71d7ab79-a001-41dc-9a8e-b3e478ce1cbb\"",
			 "\"resourceType\": \"Observation\"",
			 "\"reference\": \"urn:uuid:9e9187c1-db6d-4b6f-adc6-976153c65ed7\""
		);

	}

	@Test
	public void testEncodeBundleWithCrossReferenceFullUrlsAndNoIds_NestedInParameters() {
		Parameters parameters = createBundleWithCrossReferenceFullUrlsAndNoIds_NestedInParameters();

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parameters);
		ourLog.info(output);

		assertThat(output).doesNotContain("\"contained\"");
		assertThat(output).doesNotContain("\"id\"");
		assertThat(output).containsSubsequence(
			 "\"resourceType\": \"Parameters\"",
			 "\"name\": \"resource\"",
			 "\"fullUrl\": \"urn:uuid:9e9187c1-db6d-4b6f-adc6-976153c65ed7\",",
			 "\"resourceType\": \"Patient\"",
			 "\"fullUrl\": \"urn:uuid:71d7ab79-a001-41dc-9a8e-b3e478ce1cbb\"",
			 "\"resourceType\": \"Observation\"",
			 "\"reference\": \"urn:uuid:9e9187c1-db6d-4b6f-adc6-976153c65ed7\""
		);

	}

	@Test
	public void testParseBundleWithCrossReferenceFullUrlsAndNoIds() {
		Bundle bundle = createBundleWithCrossReferenceFullUrlsAndNoIds();
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);

		Bundle parsedBundle = ourCtx.newJsonParser().parseResource(Bundle.class, encoded);
		assertEquals("urn:uuid:9e9187c1-db6d-4b6f-adc6-976153c65ed7", parsedBundle.getEntry().get(0).getFullUrl());
		assertEquals("urn:uuid:9e9187c1-db6d-4b6f-adc6-976153c65ed7", parsedBundle.getEntry().get(0).getResource().getId());
		assertEquals("urn:uuid:71d7ab79-a001-41dc-9a8e-b3e478ce1cbb", parsedBundle.getEntry().get(1).getFullUrl());
		assertEquals("urn:uuid:71d7ab79-a001-41dc-9a8e-b3e478ce1cbb", parsedBundle.getEntry().get(1).getResource().getId());
	}

	@Test
	public void testPreCommentsToFhirComments() {
		final Patient patient = new Patient();

		final Identifier identifier = new Identifier();
		identifier.setValue("myId");
		identifier.getFormatCommentsPre().add("This is a comment");
		patient.getIdentifier().add(identifier);

		final HumanName humanName1 = new HumanName();
		humanName1.addGiven("given1");
		humanName1.getFormatCommentsPre().add("This is another comment");
		patient.getName().add(humanName1);

		final HumanName humanName2 = new HumanName();
		humanName2.addGiven("given1");
		humanName2.getFormatCommentsPre().add("This is yet another comment");
		patient.getName().add(humanName2);

		final String patientString = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertThat(patientString).doesNotContain("fhir_comment");
	}

	@Test
	public void testNestedModifierExtensions() {
		// Claim: modifier extensions may have nested extensions. Even if they are
		// automatically treated as modifier extensions themselves, their key in serialized
		// output must be "extension" and not "modifierExtension".
		final Extension nestedExtension = new Extension();
		nestedExtension.setUrl("http://example.com/nested-extension");
		nestedExtension.setValue(new StringType("value"));

		final Extension extension = new Extension();
		extension.setUrl("http://example.com/extension");
		extension.addExtension(nestedExtension);

		final Patient patient = new Patient();
		patient.addModifierExtension(extension);

		final String patientStringJSON = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertThat(patientStringJSON).containsOnlyOnce("\"modifierExtension\"");

		final String patientStringXML = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(patientStringXML).containsOnlyOnce("<modifierExtension");
	}

	// this test is to ensure contained resources that are referenced by the
	// entire resource will still be able to be processed in a transaction
	@Test
	public void encodeResourceToString_withContainedResource_shouldAddContainedReferenceIdEvenIfNotPresent() {
		// setup
		Patient patient = new Patient();
		{
			Practitioner practitioner = new Practitioner();
			practitioner.addName()
				 .setFamily("Hibert");
			patient.addName()
				 .setFamily("Simpson");
			patient.getContained().add(practitioner);
			patient.addGeneralPractitioner(new Reference(practitioner));
		}

		IParser parser = ourCtx.newJsonParser();

		// test
		String encoded = parser.encodeResourceToString(patient);
		ourLog.info(encoded);

		// verify
		assertNotNull(encoded);
		assertTrue(isNotBlank(encoded));
		assertFalse(patient.getGeneralPractitioner().isEmpty());
		Reference ref = patient.getGeneralPractitioner().get(0);
		assertNotNull(ref.getResource());
		assertNotNull(ref.getReference());
		assertEquals("#" + ref.getResource().getIdElement().getValue(), ref.getReference());
	}

	static List<String> patientStrs() {
		List<String> resources = new ArrayList<>();

		@Language("JSON")
		String patientStr;
		// 1 valid simple
		{
			patientStr = """
				 {
				 	"resourceType": "Patient",
				 	"id": "P1212",
				 	"contact": [{
				 		"name": [{
				 			"use": "official",
				 			"family": "Simpson",
				 			"given": ["Homer" ]
				 		}]
				 	}],
				 	"text": {
				 		"status": "additional",
				 		"div": "<div>a div element</div>"
				 	}
				 }
				 """;
		}
		resources.add(patientStr);

		// 2 invalid simple
		{
			patientStr = """
				 {
				 	"resourceType": "Patient",
				 	"id": "P1212",
				 	"contact": [{
				 		"name": [{
				 			"use": "official",
				 			"family": "Simpson",
				 			"given": ["Homer" ]
				 		}]
				 	}, {
				 		"name": [{
				 			"use": "official",
				 			"family": "Flanders",
				 			"given": ["Ned"]
				 		}]
				 	}],
				 	"text": {
				 		"status": "additional",
				 		"div": "<div>a div element</div>"
				 	}
				 }
				 """;
		}
		resources.add(patientStr);

		// 3 invalid complex
		{
			patientStr = """
				 {
				       "resourceType" : "Patient",
				       "id" : "P12312",
				       "meta" : {
				         "profile" : ["http://hl7.org/fhir/StructureDefinition/Patient"]
				       },
				       "extension" : [ {
				         "url" : "http://hl7.org/fhir/StructureDefinition/us-core-ethnicity",
				         "extension" : [ {
				           "url" : "ombCategory",
				           "valueCoding" : {
				             "code" : "2186-5",
				             "display" : "Not Hispanic or Latino",
				             "system" : "urn:oid:2.16.840.1.113883.6.238"
				           }
				         }, {
				           "url" : "text",
				           "valueString" : "Non-Hisp"
				         } ]
				       }, {
				         "url" : "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race",
				         "extension" : [ {
				           "url" : "ombCategory",
				           "valueCoding" : {
				             "code" : "2054-5",
				             "display" : "Black or African American",
				             "system" : "urn:oid:2.16.840.1.113883.6.238"
				           }
				         }, {
				           "url" : "text",
				           "valueString" : "Black"
				         } ]
				       }, {
				         "url" : "http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex",
				         "valueCode" : "M"
				       } ],
				       "communication" : [ {
				         "language" : {
				           "coding" : [ {
				             "code" : "en",
				             "display" : "English",
				             "system" : "urn:ietf:bcp:47"
				           }, {
				             "code" : "ENG",
				             "display" : "English",
				             "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-language-cs"
				           } ],
				           "text" : "EN"
				         },
				         "preferred" : true
				       } ],
				       "telecom" : [ {
				         "system" : "phone",
				         "value" : "393-342-2312"
				       } ],
				       "identifier" : [ {
				         "system" : "http://hl7.org/fhir/sid/us-ssn",
				         "type" : {
				           "coding" : [ {
				             "system" : "http://terminology.hl7.org/CodeSystem/v2-0203",
				             "code" : "SS",
				             "display" : "Social Security Number"
				           } ],
				           "text" : "Social Security Number"
				         },
				         "value" : "12133121"
				       }, {
				         "system" : "urn:oid:2.16.840.1.113883.3.7418.2.1",
				         "type" : {
				           "coding" : [ {
				             "system" : "http://terminology.hl7.org/CodeSystem/v2-0203",
				             "code" : "MR",
				             "display" : "Medical record number"
				           } ],
				           "text" : "Medical record number"
				         },
				         "value" : "12312"
				       } ],
				       "name" : [ {
				         "use" : "official",
				         "family" : "WEIHE",
				         "given" : [ "FLOREZ,A" ],
				         "period" : {
				           "start" : "2020-12-16T00:00:00-04:00"
				         }
				       } ],
				       "gender" : "male",
				       "birthDate" : "1955-09-19",
				       "active" : true,
				       "address" : [ {
				         "type" : "postal",
				         "line" : [ "1553 SUMMIT STREET" ],
				         "city" : "DAVENPORT",
				         "state" : "IA",
				         "postalCode" : "52809",
				         "country" : "USA",
				         "period" : {
				           "start" : "2020-12-16T00:00:00-04:00"
				         }
				       }, {
				         "type" : "physical",
				         "use" : "home",
				         "line" : [ "1553 SUMMIT STREET" ],
				         "city" : "DAVENPORT",
				         "state" : "IA",
				         "postalCode" : "52809",
				         "country" : "USA",
				         "period" : {
				           "start" : "2020-12-16T00:00:00-04:00"
				         }
				       } ],
				       "maritalStatus" : [ {
				         "coding" : [ {
				           "code" : "S",
				           "display" : "Never Married",
				           "system" : "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus"
				         } ],
				         "text" : "S"
				       } ],
				       "contact" : [
				         {
				         "relationship" : [ {
				           "coding" : [ {
				             "code" : "PRN",
				             "display" : "parent",
				             "system" : "http://terminology.hl7.org/CodeSystem/v3-RoleCode"
				           } ],
				           "text" : "Parnt"
				         } ],
				         "name" : [ {
				           "use" : "official",
				           "family" : "PRESTIDGE",
				           "given" : [ "HEINEMAN" ]
				         } ],
				         "address" : [ {
				           "type" : "postal",
				           "line" : [ "1553 SUMMIT STREET" ],
				           "city" : "DAVENPORT",
				           "state" : "IA",
				           "postalCode" : "52809",
				           "country" : "USA",
				           "period" : {
				             "start" : "2020-12-16T00:00:00-04:00"
				           }
				         }, {
				           "type" : "physical",
				           "use" : "home",
				           "line" : [ "1553 SUMMIT STREET" ],
				           "city" : "DAVENPORT",
				           "state" : "IA",
				           "postalCode" : "52809",
				           "country" : "USA",
				           "period" : {
				             "start" : "2020-12-16T00:00:00-04:00"
				           }
				         } ],
				         "extension" : [ {
				           "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-type",
				           "valueCodeableConcept" : {
				             "coding" : [ {
				               "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-patient-contact-type-cs",
				               "code" : "PRIMARY",
				               "display" : "Primary Contact"
				             } ],
				             "text" : "Emergency"
				           }
				         } ]
				       },
				       {
				         "relationship" : [ {
				           "coding" : [ {
				             "code" : "E",
				             "display" : "Employer",
				             "system" : "http://terminology.hl7.org/CodeSystem/v2-0131"
				           } ],
				           "text" : "EMP"
				         } ],
				         "address" : [ {
				           "type" : "postal",
				           "line" : [ "1553 SUMMIT STREET" ],
				           "city" : "DAVENPORT",
				           "state" : "IA",
				           "postalCode" : "52809",
				           "country" : "USA",
				           "period" : {
				             "start" : "2020-12-16T00:00:00-04:00"
				           }
				         }, {
				           "type" : "physical",
				           "use" : "home",
				           "line" : [ "1553 SUMMIT STREET" ],
				           "city" : "DAVENPORT",
				           "state" : "IA",
				           "postalCode" : "52809",
				           "country" : "USA",
				           "period" : {
				             "start" : "2020-12-16T00:00:00-04:00"
				           }
				         } ],
				         "extension" : [ {
				           "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-type",
				           "valueCodeableConcept" : {
				             "coding" : [ {
				               "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-patient-contact-type-cs",
				               "code" : "EMPLOYER",
				               "display" : "Employer"
				             } ]
				           }
				         }, {
				           "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-primary-emp-ind",
				           "valueBoolean" : false
				         }, {
				           "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-emp-status",
				           "valueString" : "jobStatus"
				         }]
				       } ]
				     }
				 """;
		}
		resources.add(patientStr);

		// 3 valid complex
		{
			patientStr = """
				 {
				       "resourceType" : "Patient",
				       "id" : "P12312",
				       "meta" : {
				         "profile" : ["http://hl7.org/fhir/StructureDefinition/Patient"]
				       },
				       "extension" : [ {
				         "url" : "http://hl7.org/fhir/StructureDefinition/us-core-ethnicity",
				         "extension" : [ {
				           "url" : "ombCategory",
				           "valueCoding" : {
				             "code" : "2186-5",
				             "display" : "Not Hispanic or Latino",
				             "system" : "urn:oid:2.16.840.1.113883.6.238"
				           }
				         }, {
				           "url" : "text",
				           "valueString" : "Non-Hisp"
				         } ]
				       }, {
				         "url" : "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race",
				         "extension" : [ {
				           "url" : "ombCategory",
				           "valueCoding" : {
				             "code" : "2054-5",
				             "display" : "Black or African American",
				             "system" : "urn:oid:2.16.840.1.113883.6.238"
				           }
				         }, {
				           "url" : "text",
				           "valueString" : "Black"
				         } ]
				       }, {
				         "url" : "http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex",
				         "valueCode" : "M"
				       } ],
				       "communication" : [ {
				         "language" : {
				           "coding" : [ {
				             "code" : "en",
				             "display" : "English",
				             "system" : "urn:ietf:bcp:47"
				           }, {
				             "code" : "ENG",
				             "display" : "English",
				             "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-language-cs"
				           } ],
				           "text" : "EN"
				         },
				         "preferred" : true
				       } ],
				       "telecom" : [ {
				         "system" : "phone",
				         "value" : "393-342-2312"
				       } ],
				       "identifier" : [ {
				         "system" : "http://hl7.org/fhir/sid/us-ssn",
				         "type" : {
				           "coding" : [ {
				             "system" : "http://terminology.hl7.org/CodeSystem/v2-0203",
				             "code" : "SS",
				             "display" : "Social Security Number"
				           } ],
				           "text" : "Social Security Number"
				         },
				         "value" : "12133121"
				       }, {
				         "system" : "urn:oid:2.16.840.1.113883.3.7418.2.1",
				         "type" : {
				           "coding" : [ {
				             "system" : "http://terminology.hl7.org/CodeSystem/v2-0203",
				             "code" : "MR",
				             "display" : "Medical record number"
				           } ],
				           "text" : "Medical record number"
				         },
				         "value" : "12312"
				       } ],
				       "name" : [ {
				         "use" : "official",
				         "family" : "WEIHE",
				         "given" : [ "FLOREZ,A" ],
				         "period" : {
				           "start" : "2020-12-16T00:00:00-04:00"
				         }
				       } ],
				       "gender" : "male",
				       "birthDate" : "1955-09-19",
				       "active" : true,
				       "address" : [ {
				         "type" : "postal",
				         "line" : [ "1553 SUMMIT STREET" ],
				         "city" : "DAVENPORT",
				         "state" : "IA",
				         "postalCode" : "52809",
				         "country" : "USA",
				         "period" : {
				           "start" : "2020-12-16T00:00:00-04:00"
				         }
				       }, {
				         "type" : "physical",
				         "use" : "home",
				         "line" : [ "1554 SUMMIT STREET" ],
				         "city" : "DAVENPORT",
				         "state" : "IA",
				         "postalCode" : "52809",
				         "country" : "USA",
				         "period" : {
				           "start" : "2020-12-16T00:00:00-04:00"
				         }
				       } ],
				       "maritalStatus" : [ {
				         "coding" : [ {
				           "code" : "S",
				           "display" : "Never Married",
				           "system" : "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus"
				         } ],
				         "text" : "S"
				       } ],
				       "contact" : [
				         {
				         "relationship" : [ {
				           "coding" : [ {
				             "code" : "PRN",
				             "display" : "parent",
				             "system" : "http://terminology.hl7.org/CodeSystem/v3-RoleCode"
				           } ],
				           "text" : "Parnt"
				         } ],
				         "name" : [ {
				           "use" : "official",
				           "family" : "PRESTIDGE",
				           "given" : [ "HEINEMAN" ]
				         } ],
				         "address" : [ {
				           "type" : "postal",
				           "line" : [ "1555 SUMMIT STREET" ],
				           "city" : "DAVENPORT",
				           "state" : "IA",
				           "postalCode" : "52809",
				           "country" : "USA",
				           "period" : {
				             "start" : "2020-12-16T00:00:00-04:00"
				           }
				         } ],
				         "extension" : [ {
				           "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-type",
				           "valueCodeableConcept" : {
				             "coding" : [ {
				               "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-patient-contact-type-cs",
				               "code" : "PRIMARY",
				               "display" : "Primary Contact"
				             } ],
				             "text" : "Emergency"
				           }
				         } ]
				       },
				       {
				         "relationship" : [ {
				           "coding" : [ {
				             "code" : "E",
				             "display" : "Employer",
				             "system" : "http://terminology.hl7.org/CodeSystem/v2-0131"
				           } ],
				           "text" : "EMP"
				         } ],
				         "address" : [ {
				           "type" : "postal",
				           "line" : [ "1557 SUMMIT STREET" ],
				           "city" : "DAVENPORT",
				           "state" : "IA",
				           "postalCode" : "52809",
				           "country" : "USA",
				           "period" : {
				             "start" : "2020-12-16T00:00:00-04:00"
				           }
				         } ],
				         "extension" : [ {
				           "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-type",
				           "valueCodeableConcept" : {
				             "coding" : [ {
				               "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-patient-contact-type-cs",
				               "code" : "EMPLOYER",
				               "display" : "Employer"
				             } ]
				           }
				         }, {
				           "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-primary-emp-ind",
				           "valueBoolean" : false
				         }, {
				           "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-emp-status",
				           "valueString" : "jobStatus"
				         }]
				       } ]
				     }
				 """;
		}
		resources.add(patientStr);

		return resources;
	}

	@Nonnull
	public static Bundle createBundleWithCrossReferenceFullUrlsAndNoIds() {
		Bundle bundle = new Bundle();

		Patient patient = new Patient();
		patient.setActive(true);
		bundle
			 .addEntry()
			 .setResource(patient)
			 .setFullUrl("urn:uuid:9e9187c1-db6d-4b6f-adc6-976153c65ed7");

		Observation observation = new Observation();
		observation.getSubject().setReference("urn:uuid:9e9187c1-db6d-4b6f-adc6-976153c65ed7").setResource(patient);
		bundle
			 .addEntry()
			 .setResource(observation)
			 .setFullUrl("urn:uuid:71d7ab79-a001-41dc-9a8e-b3e478ce1cbb");
		return bundle;
	}

	@Nonnull
	public static Parameters createBundleWithCrossReferenceFullUrlsAndNoIds_NestedInParameters() {
		Parameters retVal = new Parameters();
		retVal
			 .addParameter()
			 .setName("resource")
			 .setResource(createBundleWithCrossReferenceFullUrlsAndNoIds());
		return retVal;
	}

	@Test
	void encodeResourceToString_withWhitespaceOnlyPrimitiveValue_shouldPreserveValueAfterRoundTrip() {
		// setup - create a Patient with a given name that is a single space " " (no extension)
		Patient patient = new Patient();
		patient.setActive(true);

		HumanName name = patient.addName();
		name.setFamily("Smith");
		name.addGiven(" ");

		// test - encode to JSON and parse back
		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);
		String encoded = jsonParser.encodeResourceToString(patient);
		ourLog.info("Encoded JSON:\n{}", encoded);

		Patient parsed = jsonParser.parseResource(Patient.class, encoded);

		// verify - the given name value " " should survive the round-trip
		assertThat(parsed.getNameFirstRep().getGiven()).hasSize(1);
		assertThat(parsed.getNameFirstRep().getGiven().get(0).getValue()).isEqualTo(" ");
	}

	@Test
	void encodeResourceToString_withWhitespaceOnlyPrimitiveValueWithExtension_shouldPreserveValueAfterRoundTrip() {
		// setup - create a Patient with a given name that is a single space " " plus an extension
		Patient patient = new Patient();
		patient.setActive(true);

		HumanName name = patient.addName();
		name.setFamily("Smith");
		StringType givenElement = name.addGivenElement();
		givenElement.setValue(" ");
		givenElement.addExtension(
			new Extension("http://example.com/ext", new BooleanType(true))
		);

		// test - encode to JSON and parse back
		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);
		String encoded = jsonParser.encodeResourceToString(patient);
		ourLog.info("Encoded JSON:\n{}", encoded);

		Patient parsed = jsonParser.parseResource(Patient.class, encoded);

		// verify - the given name value " " should survive the round-trip
		assertThat(parsed.getNameFirstRep().getGiven()).hasSize(1);
		assertThat(parsed.getNameFirstRep().getGiven().get(0).getValue()).isEqualTo(" ");
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@DatatypeDef(
		 name = "UnknownPrimitiveType"
	)
	private static class MyUnknownPrimitiveType extends PrimitiveType<Object> {
		@Override
		public Object getValue() {
			return "AAA";
		}

		@Override
		public String getValueAsString() {
			return "AAA";
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Type copy() {
			return this;
		}

		@Override
		protected String encode(Object theO) {
			return "AAA";
		}

		@Override
		protected Object parse(String theS) {
			return new Object();
		}
	}

}
