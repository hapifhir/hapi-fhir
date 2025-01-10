package org.hl7.fhir.r4b.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.test.utilities.LoggingExtension;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.MockValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4b.conformance.ProfileUtilities;
import org.hl7.fhir.r4b.context.IWorkerContext;
import org.hl7.fhir.r4b.fhirpath.FHIRPathEngine;
import org.hl7.fhir.r4b.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4b.model.AllergyIntolerance;
import org.hl7.fhir.r4b.model.Base;
import org.hl7.fhir.r4b.model.Base64BinaryType;
import org.hl7.fhir.r4b.model.BooleanType;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4b.model.CodeType;
import org.hl7.fhir.r4b.model.Consent;
import org.hl7.fhir.r4b.model.ContactPoint;
import org.hl7.fhir.r4b.model.DateTimeType;
import org.hl7.fhir.r4b.model.Enumerations;
import org.hl7.fhir.r4b.model.Extension;
import org.hl7.fhir.r4b.model.Media;
import org.hl7.fhir.r4b.model.Narrative;
import org.hl7.fhir.r4b.model.Observation;
import org.hl7.fhir.r4b.model.OperationOutcome;
import org.hl7.fhir.r4b.model.Patient;
import org.hl7.fhir.r4b.model.Period;
import org.hl7.fhir.r4b.model.Practitioner;
import org.hl7.fhir.r4b.model.Procedure;
import org.hl7.fhir.r4b.model.QuestionnaireResponse;
import org.hl7.fhir.r4b.model.Reference;
import org.hl7.fhir.r4b.model.StringType;
import org.hl7.fhir.r4b.model.StructureDefinition;
import org.hl7.fhir.r4b.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4b.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r5.test.utils.ClassesLoadedFlags;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.IValidatorResourceFetcher;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.hl7.fhir.r5.utils.validation.constants.ContainedReferenceValidationPolicy;
import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FhirInstanceValidatorR4BTest extends BaseTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorR4BTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4BCached();
	@RegisterExtension
	public LoggingExtension myLoggingExtension = new LoggingExtension();
	@Mock
	IValidatorResourceFetcher resourceFetcher;
	@Mock
	IValidationPolicyAdvisor policyAdvisor;
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myFhirValidator;
	private IValidationSupport myValidationSupport;
	private final MockValidationSupport myMockSupport = new MockValidationSupport(FhirContext.forR4BCached());

	/**
	 * An invalid local reference should not cause a ServiceException.
	 */
	@Test
	public void testInvalidLocalReference() {
		QuestionnaireResponse resource = new QuestionnaireResponse();
		resource.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		resource.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		resource.setSubject(new Reference("#invalid-ref"));

		ValidationResult output = myFhirValidator.validateWithResult(resource);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo).hasSize(2);
	}

	@BeforeEach
	public void before() {
		buildValidationSupportWithLogicalAndSupport(false);
	}

	private Object defaultString(Integer theLocationLine) {
		return theLocationLine != null ? theLocationLine.toString() : "";
	}

	private StructureDefinition loadStructureDefinition(IValidationSupport theDefaultValSupport, String theResName) throws IOException, FHIRException {
		StructureDefinition derived = loadResource(ourCtx, StructureDefinition.class, theResName);
		StructureDefinition base = (StructureDefinition) theDefaultValSupport.fetchStructureDefinition(derived.getBaseDefinition());
		Validate.notNull(base);

		IWorkerContext worker = new HapiWorkerContext(ourCtx, theDefaultValSupport);
		List<ValidationMessage> issues = new ArrayList<>();
		ProfileUtilities profileUtilities = new ProfileUtilities(worker, issues, null);
		profileUtilities.generateSnapshot(base, derived, "", "", "");

		return derived;
	}

	private List<SingleValidationMessage> logResultsAndReturnAll(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {}:{} {} - {}",
				index, next.getSeverity(), defaultString(next.getLocationLine()), defaultString(next.getLocationCol()), next.getLocationString(), next.getMessage());
			index++;

			retVal.add(next);
		}
		return retVal;
	}

	@Test
	public void testValidateStorageResponseCode() {
		String input = """
			{
			  "resourceType": "OperationOutcome",
			  "text": {
			    "status": "generated",
			    "div": "<div xmlns=\\"http://www.w3.org/1999/xhtml\\"><h1>Operation Outcome</h1><table border=\\"0\\"><tr><td style=\\"font-weight: bold;\\">INFORMATION</td><td>[]</td><td><pre>Successfully conditionally patched resource with no changes detected. Existing resource Patient/A/_history/1 matched URL: Patient?birthdate=2022-01-01. Took 6ms.</pre></td> </tr> </table> </div>"
			  },
			  "issue": [ {
			    "severity": "information",
			    "code": "informational",
			    "details": {
			      "coding": [ {
			        "system": "https://hapifhir.io/fhir/CodeSystem/hapi-fhir-storage-response-code",
			        "code": "SUCCESSFUL_CONDITIONAL_PATCH_NO_CHANGE",
			        "display": "Conditional patch succeeded: No changes were detected so no action was taken."
			      } ]
			    },
			    "diagnostics": "Successfully conditionally patched resource with no changes detected. Existing resource Patient/A/_history/1 matched URL: Patient?birthdate=2022-01-01. Took 6ms."
			  } ]
			}""";
		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnErrorOnes(result);
		assertThat(result.isSuccessful()).as(all.toString()).isTrue();

	}


	@Test
	public void testValidateCodeWithTailingSpace() {

		Patient p = new Patient();
		p.getMaritalStatus()
			.addCoding()
			.setSystem("http://foo")
			.setCode("AA  ");

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(p);
		List<SingleValidationMessage> all = logResultsAndReturnErrorOnes(result);
		assertFalse(result.isSuccessful());
		assertEquals("The code 'AA  ' is not valid (whitespace rules)", all.get(0).getMessage());

	}

	/**
	 * See #938
	 */
	@Test
	public void testValidateEmptyElement() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"  <active value=\"\"/>" +
			"</Patient>";

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertThat(all.get(0).getMessage()).contains("All FHIR elements must have a @value or children");
	}

	/**
	 * See #1740
	 */
	@Test
	public void testValidateScalarInRepeatableField() {
		String operationDefinition = "{\n" +
			"  \"resourceType\": \"OperationDefinition\",\n" +
			"  \"name\": \"Questionnaire\",\n" +
			"  \"status\": \"draft\",\n" +
			"  \"kind\" : \"operation\",\n" +
			"  \"code\": \"populate\",\n" +
			"  \"resource\": \"Patient\",\n" + // should be array
			"  \"system\": false,\n" + " " +
			" \"type\": false,\n" +
			"  \"instance\": true\n" +
			"}";

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(operationDefinition);
		List<SingleValidationMessage> all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertEquals("The property resource must be a JSON Array, not a Primitive property (at OperationDefinition)", all.get(0).getMessage());
	}

	@Test
	public void testValidateMeta() {
		String input = "{" +
			"   \"resourceType\": \"Parameters\"," +
			"   \"parameter\": [" +
			"      {" +
			"         \"name\": \"meta\"," +
			"         \"valueMeta\": {" +
			"            \"tag\": [" +
			"               {" +
			"                  \"system\": \"urn:test-fake-system\"," +
			"                  \"code\": \"420\"" +
			"               }" +
			"            ]" +
			"         }" +
			"      }" +
			"   ]" +
			"}";
		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnAll(result);
		assertTrue(result.isSuccessful());
	}

	/**
	 * TODO: unignore when https://github.com/hapifhir/org.hl7.fhir.core/pull/201 is merged
	 */
	@Test
	public void testValidateFixedBindingOnQuantity() throws IOException {
		StructureDefinition sd = loadResource(ourCtx, StructureDefinition.class, "/r4/bbl-fixed-binding-structuredef.json");
		myMockSupport.addStructureDefinition("http://example.org/fhir/StructureDefinition/MyObservation", sd);

		Observation obs = loadResource(ourCtx, Observation.class, "/r4/bbl-fixed-binding-observation.json");

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));
		ValidationResult result = val.validateWithResult(obs);
		logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
	}


	/**
	 * See #1676 - We should ignore schema location
	 */
	@Test
	public void testValidateResourceWithSchemaLocation() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://hl7.org/fhir ../../schema/foo.xsd\">" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"  <active value=\"true\"/>" +
			"</Patient>";

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(input);
		logResultsAndReturnAll(result);
		assertTrue(result.isSuccessful());
	}

	/**
	 * See #942
	 */
	@Test
	public void testValidateDoesntEnforceBestPracticesByDefault() {
		myMockSupport.addValidConcept("http://loinc.org", "1234");

		Observation input = new Observation();
		input.addPerformer(new Reference("Practitioner/124"));
		input.setEffective(new DateTimeType("2023-01-01T11:22:33Z"));

		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(Enumerations.ObservationStatus.AMENDED);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234").setDisplay("FOO");

		FhirInstanceValidator instanceModule;
		FhirValidator val;
		ValidationResult result;
		List<SingleValidationMessage> all;

		// With BPs disabled
		val = ourCtx.newValidator();
		instanceModule = new FhirInstanceValidator(myValidationSupport);
		val.registerValidatorModule(instanceModule);
		result = val.validateWithResult(input);
		all = logResultsAndReturnAll(result);
		assertTrue(result.isSuccessful());
		assertThat(all).hasSize(1);
		assertEquals("Best Practice Recommendation: In general, all observations should have a subject", all.get(0).getMessage());
		assertEquals(ResultSeverityEnum.WARNING, all.get(0).getSeverity());

		// With BPs enabled
		val = ourCtx.newValidator();
		instanceModule = new FhirInstanceValidator(myValidationSupport);
		BestPracticeWarningLevel level = BestPracticeWarningLevel.Error;
		instanceModule.setBestPracticeWarningLevel(level);
		val.registerValidatorModule(instanceModule);
		result = val.validateWithResult(input);
		all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertEquals("Best Practice Recommendation: In general, all observations should have a subject", all.get(0).getMessage());
	}


	private List<SingleValidationMessage> logResultsAndReturnNonInformationalOnes(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {} - {}", index, next.getSeverity(), next.getLocationString(), next.getMessage());
			index++;

			if (next.getSeverity() != ResultSeverityEnum.INFORMATION) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	private List<SingleValidationMessage> logResultsAndReturnErrorOnes(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {} - {}", index, next.getSeverity(), next.getLocationString(), next.getMessage());
			index++;

			if (next.getSeverity().ordinal() > ResultSeverityEnum.WARNING.ordinal()) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	@Test
	public void testBase64Invalid() {
		Base64BinaryType value = new Base64BinaryType(new byte[]{2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		Media med = new Media();
		med.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		med.getContent().setContentType(Constants.CT_OCTET_STREAM);
		med.getContent().setDataElement(value);
		med.getContent().setTitle("bbbb syst");
		med.setStatus(Enumerations.EventStatus.NOTDONE);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(med);

		encoded = encoded.replace(value.getValueAsString(), "%%%2@()()");

		ourLog.info("Encoded: {}", encoded);

		ValidationResult output = myFhirValidator.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors).hasSize(1);
		assertEquals("The value '%%%2@()()' is not a valid Base64 value", errors.get(0).getMessage());

	}

	@Test
	public void testValidateBundleWithNoFullUrl() throws IOException {
		String encoded = loadResource("/r4/r4-caredove-bundle.json");

		ValidationResult output = myFhirValidator.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		errors = errors
			.stream()
			.filter(t -> t.getMessage().contains("Bundle entry missing fullUrl"))
			.collect(Collectors.toList());
		assertThat(errors).hasSize(5);
	}

	@Test
	public void testBase64Valid() {
		Base64BinaryType value = new Base64BinaryType(new byte[]{2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		Media med = new Media();
		med.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		med.getContent().setContentType(Constants.CT_OCTET_STREAM);
		med.getContent().setDataElement(value);
		med.getContent().setTitle("bbbb syst");
		med.setStatus(Enumerations.EventStatus.NOTDONE);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(med);

		ourLog.info("Encoded: {}", encoded);

		ValidationResult output = myFhirValidator.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors).isEmpty();

	}

	/**
	 * See #873
	 */
	@Test
	public void testCompareTimesWithDifferentTimezones() {
		Procedure procedure = new Procedure();
		procedure.setStatus(Enumerations.EventStatus.COMPLETED);
		procedure.getSubject().setReference("Patient/1");
		procedure.getCode().setText("Some proc");

		Period period = new Period();
		period.setStartElement(new DateTimeType("2000-01-01T00:00:01+05:00"));
		period.setEndElement(new DateTimeType("2000-01-01T00:00:00+04:00"));
		assertThat(period.getStart().getTime()).isLessThan(period.getEnd().getTime());
		procedure.setPerformed(period);

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(myInstanceVal);

		ValidationResult result = val.validateWithResult(procedure);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
	}

	/**
	 * See #531
	 */
	@Test
	public void testContactPointSystemUrlWorks() {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		ContactPoint t = p.addTelecom();
		t.setSystem(ContactPoint.ContactPointSystem.URL);
		t.setValue("http://infoway-inforoute.ca");

		ValidationResult results = myFhirValidator.validateWithResult(p);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome).isEmpty();

	}

	/**
	 * See #872
	 */
	@Test
	public void testExtensionUrlWithHl7Url() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidator.class.getResourceAsStream("/bug872-ext-with-hl7-url.json"), Charsets.UTF_8);
		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo).isEmpty();
	}

	@Test
	public void testIsNoTerminologyChecks() {
		assertFalse(myInstanceVal.isNoTerminologyChecks());
		myInstanceVal.setNoTerminologyChecks(true);
		assertTrue(myInstanceVal.isNoTerminologyChecks());
	}

	@Test
	public void testLargeBase64() throws IOException {
		myMockSupport.addValidValueSet("http://hl7.org/fhir/ValueSet/report-codes");
		myMockSupport.addValidConcept("http://loinc.org", "1-8");

		String input = IOUtils.toString(FhirInstanceValidatorR4BTest.class.getResourceAsStream("/r4/diagnosticreport-example-gingival-mass.json"), Constants.CHARSET_UTF8);
		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> messages = logResultsAndReturnAll(output);
		assertThat(messages).hasSize(1);
		assertEquals("Base64 encoded values SHOULD not contain any whitespace (per RFC 4648). Note that non-validating readers are encouraged to accept whitespace anyway", messages.get(0).getMessage());
	}

	@Test
	@Disabled
	public void testValidateBigRawJsonResource() throws Exception {
		String input = super.loadCompressedResource("/conformance.json.gz");

		long start = System.currentTimeMillis();
		ValidationResult output = null;
		int passes = 1;
		for (int i = 0; i < passes; i++) {
			ourLog.info("Pass {}", i + 1);
			output = myFhirValidator.validateWithResult(input);
		}

		long delay = System.currentTimeMillis() - start;
		long per = delay / passes;

		logResultsAndReturnAll(output);

		ourLog.info("Took {} ms -- {}ms / pass", delay, per);
	}

	@Test
	@Disabled
	public void testValidateBuiltInProfiles() throws Exception {
		Bundle bundle;
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String vsContents = loadResource("/org/hl7/fhir/r4b/model/profile/" + name + ".xml");

		TreeSet<String> ids = new TreeSet<>();

		bundle = ourCtx.newXmlParser().parseResource(Bundle.class, vsContents);
		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.r4b.model.Resource next = i.getResource();
			ids.add(next.getId());

			if (next instanceof StructureDefinition) {
				StructureDefinition sd = (StructureDefinition) next;
				if (sd.getKind() == StructureDefinitionKind.LOGICAL) {
					ourLog.info("Skipping logical type: {}", next.getId());
					continue;
				}
			}

			ourLog.info("Validating {}", next.getId());
			ourLog.trace(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(next));

			ValidationResult output = myFhirValidator.validateWithResult(next);
			List<SingleValidationMessage> results = logResultsAndReturnAll(output);

			// This isn't a validator problem but a definition problem.. it should get fixed at some point and
			// we can remove this. Tracker #17207 was filed about this
			// https://gforge.hl7.org/gf/project/fhir/tracker/?action=TrackerItemEdit&tracker_item_id=17207
			if (next.getId().equalsIgnoreCase("http://hl7.org/fhir/OperationDefinition/StructureDefinition-snapshot")) {
				assertThat(results).hasSize(1);
				assertEquals("A search type can only be specified for parameters of type string [searchType.exists() implies type = 'string']", results.get(0).getMessage());
				continue;
			}


			List<SingleValidationMessage> errors = results
				.stream()
				.filter(t -> t.getSeverity() != ResultSeverityEnum.INFORMATION)
				.collect(Collectors.toList());

			assertThat(errors).as("Failed to validate " + i.getFullUrl() + " - " + errors).isEmpty();
		}

		ourLog.info("Validated the following:\n{}", ids);
	}

	@Test
	public void testValidateBundleWithNoType() throws Exception {
		String vsContents = loadResource("/r4/bundle-with-no-type.json");

		ValidationResult output = myFhirValidator.validateWithResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertThat(output.getMessages().toString()).contains("Bundle.type: minimum required = 1, but only found 0");
	}

	/**
	 * See #848
	 */
	@Test
	public void testValidateJsonWithDuplicateEntries() {
		String patient = "{" +
			"\"resourceType\":\"Patient\", " +
			"\"active\": true, " +
			"\"name\":[ {\"family\":\"foo\"} ]," +
			"\"name\":[ {\"family\":\"bar\"} ]" +
			"}";

		ValidationResult output = myFhirValidator.validateWithResult(patient);
		logResultsAndReturnNonInformationalOnes(output);
		assertThat(output.getMessages().toString()).contains("The JSON property 'name' is a duplicate and will be ignored");
	}

	@Test
	@Disabled
	public void testValidateBundleWithObservations() throws Exception {
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String inputString;
		inputString = loadResource("/brian_reinhold_bundle.json");
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, inputString);

		FHIRPathEngine fp = new FHIRPathEngine(new HapiWorkerContext(ourCtx, myValidationSupport));
		List<Base> fpOutput;
		BooleanType bool;

		fpOutput = fp.evaluate(bundle.getEntry().get(0).getResource(), "component.where(code = %resource.code).empty()");
		assertThat(fpOutput).hasSize(1);
		bool = (BooleanType) fpOutput.get(0);
		assertTrue(bool.getValue());
		//
		// fpOutput = fp.evaluate(bundle, "component.where(code = %resource.code).empty()");
		// assertEquals(1, fpOutput.size());
		// bool = (BooleanType) fpOutput.get(0);
		// assertTrue(bool.getValue());

		ValidationResult output = myFhirValidator.validateWithResult(inputString);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors).isEmpty();

	}

	@Test
	@Disabled
	public void testValidateDocument() throws Exception {
		String vsContents = loadResource("/sample-document.xml");

		ValidationResult output = myFhirValidator.validateWithResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertTrue(output.isSuccessful());
	}

	@Test
	public void testValidateProfileWithExtension() throws IOException, FHIRException {
		PrePopulatedValidationSupport valSupport = new PrePopulatedValidationSupport(ourCtx);
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(ourCtx);
		ValidationSupportChain support = new ValidationSupportChain(defaultSupport, valSupport, new InMemoryTerminologyServerValidationSupport(ourCtx)).setCodeableConceptValidationSuccessfulIfNotAllCodingsAreValid(false);

		// Prepopulate SDs
		valSupport.addStructureDefinition(loadStructureDefinition(defaultSupport, "/r4/myconsent-profile.xml"));
		valSupport.addStructureDefinition(loadStructureDefinition(defaultSupport, "/r4/myconsent-ext.xml"));

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(support));

		Consent input = super.loadResource(ourCtx, Consent.class, "/r4/myconsent-resource.json");

		input.getPolicyFirstRep().setAuthority("http://foo");
		//input.setScope(Consent.ConsentScope.ADR);
		input.getScope()
			.getCodingFirstRep()
			.setSystem("http://terminology.hl7.org/CodeSystem/consentscope")
			.setCode("adr");
		input.addCategory()
			.getCodingFirstRep()
			.setSystem("http://terminology.hl7.org/CodeSystem/consentcategorycodes")
			.setCode("acd");


		// Should pass
		ValidationResult output = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnErrorOnes(output);
		assertThat(all.size()).as(all.toString()).isEqualTo(0);

		// Now with the wrong datatype
		input.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/PruebaExtension").get(0).setValue(new CodeType("AAA"));

		// Should fail
		output = val.validateWithResult(input);
		all = logResultsAndReturnErrorOnes(output);
		assertThat(all.toString()).contains("definition allows for the types [string] but found type code");

	}

	@Test
	@Disabled
	public void testValidateQuestionnaireResponse() throws IOException {
		String input = loadResource("/qr_jon.xml");

		ValidationResult output = myFhirValidator.validateWithResult(input);
		logResultsAndReturnAll(output);

		assertThat(output.getMessages().toString()).contains("Items not of type group should not have items - Item with linkId 5.1 of type BOOLEAN has 1 item(s)");
	}

	@Test
	public void testValidateRawJsonResource() {
		String input = "{" +
			"  \"resourceType\":\"Patient\"," +
			"  \"text\": {\n" +
			"    \"status\": \"generated\",\n" +
			"    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">HELLO</div>\"\n" +
			"  },\n" +
			"  \"id\":\"123\"" +
			"}";

		ValidationResult output = myFhirValidator.validateWithResult(input);
		assertThat(output.getMessages().size()).as(output.toString()).isEqualTo(0);
	}

	@Test
	public void testValidateRawJsonResourceBadAttributes() {
		String input =
			"{" +
				"\"resourceType\":\"Patient\"," +
				"  \"text\": {\n" +
				"    \"status\": \"generated\",\n" +
				"    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">HELLO</div>\"\n" +
				"  },\n" +
				"\"id\":\"123\"," +
				"\"foo\":\"123\"" +
				"}";

		ValidationResult output = myFhirValidator.validateWithResult(input);
		assertThat(output.getMessages().size()).as(output.toString()).isEqualTo(1);
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("Patient", output.getMessages().get(0).getLocationString());
		assertEquals("Unrecognized property 'foo'", output.getMessages().get(0).getMessage());

		OperationOutcome operationOutcome = (OperationOutcome) output.toOperationOutcome();
		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
		assertEquals("Unrecognized property 'foo'", operationOutcome.getIssue().get(0).getDiagnostics());
		assertEquals("Patient", operationOutcome.getIssue().get(0).getLocation().get(0).getValue());
		assertEquals("Line[5] Col[23]", operationOutcome.getIssue().get(0).getLocation().get(1).getValue());
	}

	@Test
	@Disabled
	public void testValidateRawJsonResourceFromExamples() throws Exception {
		String input = loadResource("/testscript-search.json");

		ValidationResult output = myFhirValidator.validateWithResult(input);
		logResultsAndReturnNonInformationalOnes(output);
		// assertEquals(output.toString(), 1, output.getMessages().size());
		// ourLog.info(output.getMessages().get(0).getLocationString());
		// ourLog.info(output.getMessages().get(0).getMessage());
		// assertEquals("/foo", output.getMessages().get(0).getLocationString());
		// assertEquals("Element is unknown or does not match any slice", output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateRawJsonResourceWithUnknownExtension() {

		Patient patient = new Patient();
		patient.setId("1");
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		Extension ext = patient.addExtension();
		ext.setUrl("http://hl7.org/fhir/v3/ethnicity");
		ext.setValue(new CodeType("Hispanic or Latino"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		/*
		 * {
		 * "resourceType": "Patient",
		 * "id": "1",
		 * "extension": [
		 * {
		 * "url": "http://hl7.org/fhir/v3/ethnicity",
		 * "valueCode": "Hispanic or Latino"
		 * }
		 * ]
		 * }
		 */

		ValidationResult output = myFhirValidator.validateWithResult(encoded);
		assertThat(output.getMessages().size()).as(output.toString()).isEqualTo(1);

		assertEquals("Unknown extension http://hl7.org/fhir/v3/ethnicity", output.getMessages().get(0).getMessage());
		assertEquals(ResultSeverityEnum.INFORMATION, output.getMessages().get(0).getSeverity());
	}

	@Test
	public void testValidateRawJsonResourceWithUnknownExtensionNotAllowed() {

		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.setId("1");

		Extension ext = patient.addExtension();
		ext.setUrl("http://hl7.org/fhir/v3/ethnicity");
		ext.setValue(new CodeType("Hispanic or Latino"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		/*
		 * {
		 * "resourceType": "Patient",
		 * "id": "1",
		 * "extension": [
		 * {
		 * "url": "http://hl7.org/fhir/v3/ethnicity",
		 * "valueCode": "Hispanic or Latino"
		 * }
		 * ]
		 * }
		 */

		myInstanceVal.setAnyExtensionsAllowed(false);
		ValidationResult output = myFhirValidator.validateWithResult(encoded);
		assertThat(output.getMessages().size()).as(output.toString()).isEqualTo(1);

		assertEquals("The extension http://hl7.org/fhir/v3/ethnicity could not be found so is not allowed here", output.getMessages().get(0).getMessage());
		assertEquals(ResultSeverityEnum.ERROR, output.getMessages().get(0).getSeverity());
	}

	@Test
	public void testValidateRawXmlResource() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" +
			"<id value=\"123\"/>" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"</Patient>";

		ValidationResult output = myFhirValidator.validateWithResult(input);
		assertThat(output.getMessages().size()).as(output.toString()).isEqualTo(0);
	}

	@Test
	public void testValidateRawXmlResourceBadAttributes() {
		String input =
			"<Patient xmlns=\"http://hl7.org/fhir\">" +
				"<id value=\"123\"/>" +
				"  <text>\n" +
				"    <status value=\"generated\"/>\n" +
				"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
				"  </text>" +
				"<foo value=\"222\"/>" +
				"</Patient>";

		ValidationResult output = myFhirValidator.validateWithResult(input);
		assertThat(output.getMessages().size()).as(output.toString()).isEqualTo(1);
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("/f:Patient", output.getMessages().get(0).getLocationString());
		assertEquals("Undefined element 'foo' at /f:Patient", output.getMessages().get(0).getMessage());
		assertEquals(28, output.getMessages().get(0).getLocationCol().intValue());
		assertEquals(4, output.getMessages().get(0).getLocationLine().intValue());
	}

	@Test
	public void testValidateRawXmlResourceWithEmptyPrimitive() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"<name><given/></name>" +
			"</Patient>";

		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> messages = logResultsAndReturnNonInformationalOnes(output);
		assertThat(messages.size()).as(output.toString()).isEqualTo(4);
		assertThat(messages.get(0).getMessage()).contains("Element must have some content");
		assertThat(messages.get(2).getMessage()).contains("Primitive types must have a value or must have child extensions");
	}

	@Test
	public void testValidateRawXmlResourceWithPrimitiveContainingOnlyAnExtension() {
		String input = "<ActivityDefinition xmlns=\"http://hl7.org/fhir\">\n" +
			"                        <id value=\"referralToMentalHealthCare\"/>\n" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"                        <name value=\"AAAAA\"/>\n" +
			"                        <status value=\"draft\"/>\n" +
			"                        <description value=\"refer to primary care mental-health integrated care program for evaluation and treatment of mental health conditions now\"/>\n" +
			"                        <code>\n" +
			"                                <coding>\n" +
			"                                        <!-- Error: Connection to http://localhost:960 refused -->\n" +
			"                                        <!--<system value=\"http://snomed.info/sct\"/>-->\n" +
			"                                        <code value=\"306206005\"/>\n" +
			"                                </coding>\n" +
			"                        </code>\n" +
			"                        <!-- Specifying this this way results in a null reference exception in the validator -->\n" +
			"                        <timingTiming>\n" +
			"                                <event>\n" +
			"                                        <extension url=\"http://fhir.org/cql-expression\">\n" +
			"                                                <valueString value=\"Now()\"/>\n" +
			"                                        </extension>\n" +
			"                                </event>\n" +
			"                        </timingTiming>\n" +
			"                </ActivityDefinition>";

		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> res = logResultsAndReturnNonInformationalOnes(output);
		assertThat(res.size()).as(output.toString()).isEqualTo(1);
		assertEquals("Coding has no system. A code with no system has no defined meaning, and it cannot be validated. A system should be provided", output.getMessages().get(0).getMessage());
	}

	/**
	 * A reference with only an identifier should be valid
	 */
	@Test
	public void testValidateReferenceWithDisplayValid() {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.getManagingOrganization().setDisplay("HELLO");

		ValidationResult output = myFhirValidator.validateWithResult(p);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo).isEmpty();
	}

	/**
	 * A reference with only an identifier should be valid
	 */
	@Test
	public void testValidateReferenceWithIdentifierValid() {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.getManagingOrganization().getIdentifier().setSystem("http://acme.org");
		p.getManagingOrganization().getIdentifier().setValue("foo");

		ValidationResult output = myFhirValidator.validateWithResult(p);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo).isEmpty();
	}

	@Test
	public void testValidateResourceContainingLoincCode() {
		myMockSupport.addValidConcept("http://loinc.org", "1234567");

		Observation input = new Observation();
		// input.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/devicemetricobservation");

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);

		assertEquals(ResultSeverityEnum.ERROR, errors.get(0).getSeverity());
		assertEquals("Unknown code (for 'http://loinc.org#12345')", errors.get(0).getMessage());
	}

	@Test
	public void testValidateResourceContainingProfileDeclaration() {
		myMockSupport.addValidConcept("http://loinc.org", "12345");

		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/devicemetricobservation");

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors.toString()).contains("Observation.subject: minimum required = 1, but only found 0");
		assertThat(errors.toString()).contains("Observation.encounter: max allowed = 0, but found 1");
		assertThat(errors.toString()).contains("Observation.device: minimum required = 1, but only found 0");
	}

	private Observation createObservationWithDefaultSubjectPerfomerEffective() {
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient/123"));
		observation.addPerformer(new Reference("Practitioner/124"));
		observation.setEffective(new DateTimeType("2023-01-01T11:22:33Z"));
		return observation;
	}

	@Test
	public void testValidateResourceContainingProfileDeclarationDoesntResolve() {
		myMockSupport.addValidConcept("http://loinc.org", "12345");

		Observation input = createObservationWithDefaultSubjectPerfomerEffective();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.getMeta().addProfile("http://foo/structuredefinition/myprofile");

		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setStatus(Enumerations.ObservationStatus.FINAL);

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors).hasSize(1);
		assertEquals("Profile reference 'http://foo/structuredefinition/myprofile' has not been checked because it could not be found", errors.get(0).getMessage());
		assertEquals(ResultSeverityEnum.ERROR, errors.get(0).getSeverity());
	}

	@Test
	public void testValidateResourceFailingInvariant() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		// Has a value, but not a status (which is required)
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setValue(new StringType("AAA"));

		ValidationResult output = myFhirValidator.validateWithResult(input);
		assertThat(output.getMessages().size()).isGreaterThan(0);
		assertThat(output.getMessages().get(0).getMessage()).contains("Observation.status: minimum required = 1, but only found 0");

	}

	@Test
	public void testValidateResourceWithDefaultValueset() {
		Observation input = createObservationWithDefaultSubjectPerfomerEffective();

		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().setText("No code here!");

		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(input));

		ValidationResult output = myFhirValidator.validateWithResult(input);
		assertEquals(output.getMessages().size(), 0);
	}

	@Test
	public void testValidateResourceWithDefaultValuesetBadCode() {
		String input =
			"<Observation xmlns=\"http://hl7.org/fhir\">\n" +
				"  <text>\n" +
				"    <status value=\"generated\"/>\n" +
				"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
				"  </text>" +
				"   <status value=\"notvalidcode\"/>\n" +
				"   <code>\n" +
				"      <text value=\"No code here!\"/>\n" +
				"   </code>\n" +
				"</Observation>";
		ValidationResult output = myFhirValidator.validateWithResult(input);
		logResultsAndReturnAll(output);
		assertThat(output.getMessages().get(0).getMessage()).contains("Unknown code 'http://hl7.org/fhir/observation-status#notvalidcode'");
		assertThat(output.getMessages().get(1).getMessage()).contains("The value provided ('notvalidcode') was not found in the value set 'ObservationStatus' (http://hl7.org/fhir/ValueSet/observation-status|4.3.0), and a code is required from this value set  (error message = Unknown code 'http://hl7.org/fhir/observation-status#notvalidcode' for in-memory expansion of ValueSet 'http://hl7.org/fhir/ValueSet/observation-status')");
	}

	@Test
	@Disabled
	public void testValidateDecimalWithTrailingDot() {
		String input = "{" +
			" \"resourceType\": \"Observation\"," +
			" \"status\": \"final\"," +
			" \"subject\": {\"reference\":\"Patient/123\"}," +
			" \"code\": { \"coding\": [{ \"system\":\"http://foo\", \"code\":\"123\" }] }," +
			"        \"referenceRange\": [\n" +
			"          {\n" +
			"            \"low\": {\n" +
			"              \"value\": 210.0,\n" +
			"              \"unit\": \"pg/mL\"\n" +
			"            },\n" +
			"            \"high\": {\n" +
			"              \"value\": 925.,\n" +
			"              \"unit\": \"pg/mL\"\n" +
			"            },\n" +
			"            \"text\": \"210.0-925.\"\n" +
			"          }\n" +
			"        ]" +
			"}";
		ourLog.info(input);
		ValidationResult output = myFhirValidator.validateWithResult(input);
		logResultsAndReturnAll(output);
		assertEquals("", output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailing() {
		myMockSupport.addValidConcept("http://loinc.org", "12345");

		Observation input = createObservationWithDefaultSubjectPerfomerEffective();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors.size()).as(errors.toString()).isEqualTo(0);

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailingNonLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		myMockSupport.addValidConcept("http://acme.org", "12345");

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("9988877");

		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertThat(errors.size()).as(errors.toString()).isGreaterThan(0);
		assertEquals("Unknown code (for 'http://acme.org#9988877')", errors.get(0).getMessage());

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingLoinc() {
		Observation input = createObservationWithDefaultSubjectPerfomerEffective();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		myMockSupport.addValidConcept("http://loinc.org", "12345");

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors.size()).as(errors.toString()).isEqualTo(0);
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingLoincWithExpansion() {
		Observation input = createObservationWithDefaultSubjectPerfomerEffective();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		ValueSetExpansionComponent expansionComponent = new ValueSetExpansionComponent();
		expansionComponent.addContains().setSystem("http://loinc.org").setCode("12345").setDisplay("Some display code");

		myInstanceVal.setValidationSupport(myValidationSupport);
		myMockSupport.addValidConcept("http://loinc.org", "12345");

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234");

		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors).hasSize(1);
		assertEquals("Unknown code (for 'http://loinc.org#1234')", errors.get(0).getMessage());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingNonLoinc() {
		Observation input = createObservationWithDefaultSubjectPerfomerEffective();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		myMockSupport.addValidConcept("http://acme.org", "12345");

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("12345");

		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertThat(errors.size()).as(errors.toString()).isEqualTo(0);
	}

	@Test
	public void testValidateResourceWithValuesetExpansionBad() {

		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.addIdentifier().setSystem("http://example.com/").setValue("12345").getType().addCoding().setSystem("http://example.com/foo/bar").setCode("bar");

		ValidationResult output = myFhirValidator.validateWithResult(p);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertThat(all).hasSize(1);
		assertEquals("Patient.identifier[0].type", all.get(0).getLocationString());
		assertThat(all.get(0).getMessage()).contains("None of the codings provided are in the value set 'IdentifierType'");
		assertEquals(ResultSeverityEnum.WARNING, all.get(0).getSeverity());

	}


	@Test
	public void testValidateWithUcum() throws IOException {
		myMockSupport.addValidValueSet("http://hl7.org/fhir/ValueSet/observation-vitalsignresult");
		myMockSupport.addValidValueSet("http://hl7.org/fhir/ValueSet/report-codes");
		myMockSupport.addValidConcept("http://loinc.org", "8310-5");

		Observation input = loadResource(ourCtx, Observation.class, "/r4/observation-with-body-temp-ucum.json");
		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnNonInformationalOnes(output);
		assertThat(all).isEmpty();

		// Change the unit to something not supported
		input.getValueQuantity().setCode("Heck");
		output = myFhirValidator.validateWithResult(input);
		all = logResultsAndReturnNonInformationalOnes(output);
		assertThat(all).hasSize(3);
		// validate first error, in R4B (as opposed to R4) Observation.value.ofType(Quantity) has ValueSet binding,
		// so first error has `Unknown code for ValueSet` error message
		assertThat(all.get(0).getMessage()).contains("The Coding provided (http://unitsofmeasure.org#Heck) was not found in the value set 'Vital Signs Units' " +
			"(http://hl7.org/fhir/ValueSet/ucum-vitals-common|4.3.0), and a code should come from this value set unless it has no suitable code (note that the validator cannot judge what is suitable). " +
			" (error message = Unknown code 'http://unitsofmeasure.org#Heck' for in-memory expansion of ValueSet 'http://hl7.org/fhir/ValueSet/ucum-vitals-common'");
		assertThat(all.get(0).getLocationString()).contains("Observation.value.ofType(Quantity)");
		// validate second error
		assertThat(all.get(1).getMessage()).contains("Error processing unit 'Heck': The unit 'Heck' is unknown' at position 0 (for 'http://unitsofmeasure.org#Heck')");
		assertThat(all.get(1).getLocationString()).contains("Observation.value.ofType(Quantity).code");
		// validate third error
		assertThat(all.get(2).getMessage()).contains("The value provided ('Heck') was not found in the value set 'Body Temperature Units'");
		assertThat(all.get(2).getLocationString()).contains("Observation.value.ofType(Quantity).code");
	}

	@Test
	public void testMultiplePerformer() {
		Observation o = new Observation();
		Practitioner p1 = new Practitioner();
		Practitioner p2 = new Practitioner();

		o.addPerformer(new Reference(p1));
		o.addPerformer(new Reference(p2));

		ValidationResult output = myFhirValidator.validateWithResult(o);
		List<SingleValidationMessage> valMessages = logResultsAndReturnAll(output);
		for (SingleValidationMessage msg : valMessages) {
			assertThat(msg.getMessage()).doesNotContain("have a performer");
		}
	}

	@Test
	public void testValidateResourceWithValuesetExpansionGood() {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient
			.addIdentifier()
			.setSystem("http://system")
			.setValue("12345")
			.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");

		ValidationResult output = myFhirValidator.validateWithResult(patient);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertThat(all).isEmpty();
	}

	@Test
	public void testInvocationOfValidatorFetcher() throws IOException {

		String encoded = loadResource("/r4/r4-caredove-bundle.json");

		when(policyAdvisor.policyForElement(any(), any(), any(), any(), any())).thenReturn(EnumSet.allOf(IValidationPolicyAdvisor.ElementValidationAction.class));
		when(policyAdvisor.policyForCodedContent(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(EnumSet.allOf(IValidationPolicyAdvisor.CodedContentValidationAction.class));

		when(policyAdvisor.policyForContained(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(ContainedReferenceValidationPolicy.CHECK_TYPE);
		myInstanceVal.setValidatorResourceFetcher(resourceFetcher);
		myInstanceVal.setValidatorPolicyAdvisor(policyAdvisor);
		myFhirValidator.validateWithResult(encoded);

		verify(resourceFetcher, times(12)).resolveURL(any(), any(), anyString(), anyString(), anyString(), anyBoolean());
		verify(policyAdvisor, times(12)).policyForContained(any(), any(), any(), any(), any(), any(), any(), any(), any());
	}

	@Test
	@Disabled
	public void testValidateStructureDefinition() throws IOException {
		String input = loadResource("/sdc-questionnaire.profile.xml");

		ValidationResult output = myFhirValidator.validateWithResult(input);
		logResultsAndReturnAll(output);

		assertThat(output.getMessages().size()).as(output.toString()).isEqualTo(3);
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateCurrency() {
		String input = """
			{
			 "resourceType": "Invoice",
			 "text": {
			 	"status" : "generated",
			 	"div" : "<div xmlns=\\"http://www.w3.org/1999/xhtml\\">Dummy text</div>"
			 },
			 "status": "draft",
			 "date": "2020-01-08",
			 "totalGross": {
			  "value": 150,
			  "currency": "USD"
			 }
			}""";
		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors.size()).as(errors.toString()).isEqualTo(0);


	}

	@Test
	public void testValidateCurrency_Wrong() {
		String input = """
			{
			 "resourceType": "Invoice",
			 "text": {
			 	"status" : "generated",
			 	"div" : "<div xmlns=\\"http://www.w3.org/1999/xhtml\\">Dummy text</div>"
			 },
			 "status": "draft",
			 "date": "2020-01-08",
			 "totalGross": {
			  "value": 150,
			  "currency": "BLAH"
			 }
			}""";
		ValidationResult output = myFhirValidator.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors.size()).as(errors.toString()).isEqualTo(3);
		assertThat(errors.get(1).getMessage()).contains("Unknown code 'urn:iso:std:iso:4217#BLAH'");
		assertThat(errors.get(2).getMessage()).contains("The value provided ('BLAH') was not found in the value set 'CurrencyCode' (http://hl7.org/fhir/ValueSet/currencies|4.3.0), and a code is required from this value set");


	}

	@Test
	public void testValidateReferenceTargetType_Correct() {

		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		allergy.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("active").setDisplay("Active");
		allergy.getVerificationStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification").setCode("confirmed").setDisplay("Confirmed");
		allergy.setPatient(new Reference("Patient/123"));

		allergy.addNote()
			.setText("This is text")
			.setAuthor(new Reference("Patient/123"));

		ValidationResult output = myFhirValidator.validateWithResult(allergy);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors.size()).as(errors.toString()).isEqualTo(0);

	}

	@Test
	@Disabled
	public void testValidateReferenceTargetType_Incorrect() {

		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("active").setDisplay("Active");
		allergy.getVerificationStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification").setCode("confirmed").setDisplay("Confirmed");
		allergy.setPatient(new Reference("Patient/123"));

		allergy.addNote()
			.setText("This is text")
			.setAuthor(new Reference("CodeSystems/123"));

		ValidationResult output = myFhirValidator.validateWithResult(allergy);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors.size()).as(errors.toString()).isEqualTo(0);
		assertThat(errors.get(0).getMessage()).contains("The value provided ('BLAH') is not in the value set http://hl7.org/fhir/ValueSet/currencies");

	}

	@Disabled
	@Test
	public void testValidateBundleMultithreaded() throws IOException {
		// setup
		StructureDefinition sd = loadStructureDefinition(myValidationSupport, "/r4/concurrent-bundle/StructureDefinitionPatientV1.json");

		myMockSupport.addStructureDefinition("https://example.com/StructureDefinition/Patient-v1", sd);

		int entriesCount = 300;

		// We deliberately create an invalid bundle to confirm we are indeed running multithreaded
		Bundle bundle = buildBundle(entriesCount, false);
		assertThat(bundle.getEntry()).hasSize(entriesCount);

		try {
			// RED-GREEN set ConcurrentBundleValidation to false to see the test fail
			myFhirValidator.setConcurrentBundleValidation(true);
			myFhirValidator.setExecutorService(Executors.newFixedThreadPool(4));
			// Run once to exclude initialization from time
			myFhirValidator.validateWithResult(bundle);

			// execute
			StopWatch stopwatch = new StopWatch();
			ValidationResult output = myFhirValidator.validateWithResult(bundle);
			ourLog.info("Validation time: {}", stopwatch);
			// assert that validation messages include the bundle entry path
			assertTrue(output.getMessages().stream().anyMatch(message -> message.getLocationString().contains("Bundle.entry[0].resource.ofType(Patient)")));
			assertTrue(output.getMessages().stream().anyMatch(message -> message.getLocationString().contains("Bundle.entry[1].resource.ofType(Patient)")));
			// validate
			List<SingleValidationMessage> all = logResultsAndReturnErrorOnes(output);
			assertThat(output.getMessages()).hasSize(entriesCount * 2);
			// This assert proves that we did a multi-threaded validation since the outer bundle fails validation
			// due to lack of unique fullUrl values on the entries.  If you setConcurrentBundleValidation(false)
			// above this test will fail.
			assertThat(all.size()).as(all.toString()).isEqualTo(0);
		} finally {
			myFhirValidator.setConcurrentBundleValidation(false);
			myFhirValidator.setExecutorService(null);
		}
	}

	@Test
	public void testPatientSingleCommunicationLanguage_en() throws IOException {
		final String encoded = loadResource("patient-with-single-comm-lang-en.json");

		final ValidationResult output = myFhirValidator.validateWithResult(encoded);
		final List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors).isEmpty();
	}

	@Test
	public void testPatientSingleCommunicationLanguage_en_US_UNDERSCORE_config_false() throws IOException {
		final String encoded = loadResource("patient-with-single-comm-lang-en_US-UNDERSCORE.json");

		final ValidationResult output = myFhirValidator.validateWithResult(encoded);
		final List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertFalse(output.isSuccessful());
	}

	@Test
	public void testPatientSingleCommunicationLanguage_en_US_DASH() throws IOException {
		final String encoded = loadResource("patient-with-single-comm-lang-en-US-DASH.json");

		final ValidationResult output = myFhirValidator.validateWithResult(encoded);
		final List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors).isEmpty();
	}

	@Test
	public void testPatientMultipleCommunicationLanguages_en_US_and_en_UNDERSCORE_config_true() throws IOException {
		buildValidationSupportWithLogicalAndSupport(true);
		final String encoded = loadResource("patient-with-multiple-comm-langs-en_US-and-en-UNDERSCORE.json");

		final ValidationResult output = myFhirValidator.validateWithResult(encoded);
		final List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors).isEmpty();
	}

	@Test
	public void testPatientMultipleCommunicationLanguages_en_US_and_en_UNDERSCORE_config_false() throws IOException {
		final String encoded = loadResource("patient-with-multiple-comm-langs-en_US-and-en-UNDERSCORE.json");

		final ValidationResult output = myFhirValidator.validateWithResult(encoded);
		final List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors).isNotEmpty();
	}

	@Test
	public void testPatientMultipleCommunicationLanguages_en_US_and_en_DASH() throws IOException {
		final String encoded = loadResource("patient-with-multiple-comm-langs-en-US-and-en-DASH.json");

		final ValidationResult output = myFhirValidator.validateWithResult(encoded);
		final List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors).isEmpty();
	}

	private Bundle buildBundle(int theSize, boolean theValidBundle) throws IOException {
		BundleBuilder bundleBuilder = new BundleBuilder(ourCtx);
		Patient p = ourCtx.newJsonParser().parseResource(Patient.class, loadResource("/r4/concurrent-bundle/patient.json"));
		for (int i = 0; i < theSize; ++i) {
			bundleBuilder.addTransactionCreateEntry(p);
		}
		if (theValidBundle) {
			Bundle retval = (Bundle) bundleBuilder.getBundle();
			AtomicInteger count = new AtomicInteger(1);
			retval.getEntry().stream().forEach(entry -> entry.setFullUrl("urn:uuid:" + count.getAndIncrement()));
			return retval;
		} else {
			return (Bundle) bundleBuilder.getBundle();
		}
	}

	private void buildValidationSupportWithLogicalAndSupport(boolean theLogicalAnd) {
		myFhirValidator = ourCtx.newValidator();
		myFhirValidator.setValidateAgainstStandardSchema(false);
		myFhirValidator.setValidateAgainstStandardSchematron(false);
		// This is only used if the validation is performed with validationOptions.isConcurrentBundleValidation = true
		myFhirValidator.setExecutorService(Executors.newFixedThreadPool(4));

		ValidationSupportChain chain = new ValidationSupportChain(
			myMockSupport,
			new CommonCodeSystemsTerminologyService(ourCtx),
			ourCtx.getValidationSupport(),
			new InMemoryTerminologyServerValidationSupport(ourCtx),
			new SnapshotGeneratingValidationSupport(ourCtx));
		myValidationSupport = chain.setCodeableConceptValidationSuccessfulIfNotAllCodingsAreValid(theLogicalAnd);
		myInstanceVal = new FhirInstanceValidator(myValidationSupport);
		myFhirValidator.registerValidatorModule(myInstanceVal);
	}

	@AfterAll
	public static void verifyFormatParsersNotLoaded() {
		/*
		 * If this fails, it means we're classloading the
		 * generated XmlParser/JsonParser classes, which are
		 * huge and unnecessary.
		 *
		 * To figure out why, put a breakpoint
		 * in the static method in the class
		 *   org.hl7.fhir.r5.formats.XmlParser
		 * and then re-run the test.
		 */
		assertFalse(ClassesLoadedFlags.ourJsonParserBaseLoaded);
		assertFalse(ClassesLoadedFlags.ourXmlParserBaseLoaded);
	}

	@AfterAll
	public static void afterClassClearContext() throws IOException, NoSuchFieldException {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
