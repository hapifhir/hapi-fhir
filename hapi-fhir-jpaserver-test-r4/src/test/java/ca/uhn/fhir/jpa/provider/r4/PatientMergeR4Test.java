package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.assertj.core.api.Assertions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Type;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER_RESPOND_ASYNC;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_RESULT_PATIENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatientMergeR4Test extends BaseResourceProviderR4Test {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientMergeR4Test.class);

	static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	static final Identifier pat1IdentifierA = new Identifier().setSystem("SYS1A").setValue("VAL1A");
	static final Identifier pat1IdentifierB = new Identifier().setSystem("SYS1B").setValue("VAL1B");
	static final Identifier pat2IdentifierA = new Identifier().setSystem("SYS2A").setValue("VAL2A");
	static final Identifier pat2IdentifierB = new Identifier().setSystem("SYS2B").setValue("VAL2B");
	static final Identifier patBothIdentifierC = new Identifier().setSystem("SYSC").setValue("VALC");

	IIdType myOrgId;
	IIdType mySourcePatId;
	IIdType mySourceTaskId;
	IIdType mySourceEncId1;
	IIdType mySourceEncId2;
	ArrayList<IIdType> mySourceObsIds;
	IIdType myTargetPatId;
	IIdType myTargetEnc1;
	Patient myResultPatient;

	@RegisterExtension
	static MyExceptionHandler ourExceptionHandler = new MyExceptionHandler();

	IGenericClient myFhirClient;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStorageSettings.setAllowMultipleDelete(true);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myFhirClient = myFhirContext.newRestfulGenericClient(myServerBase);

		Organization org = new Organization();
		org.setName("an org");
		myOrgId = myFhirClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		ourLog.info("OrgId: {}", myOrgId);

		Patient patient1 = new Patient();
		patient1.getManagingOrganization().setReferenceElement(myOrgId);
		patient1.addIdentifier(pat1IdentifierA);
		patient1.addIdentifier(pat1IdentifierB);
		patient1.addIdentifier(patBothIdentifierC);
		mySourcePatId = myFhirClient.create().resource(patient1).execute().getId().toUnqualifiedVersionless();

		Patient patient2 = new Patient();
		patient2.addIdentifier(pat2IdentifierA);
		patient2.addIdentifier(pat2IdentifierB);
		patient2.addIdentifier(patBothIdentifierC);
		patient2.getManagingOrganization().setReferenceElement(myOrgId);
		myTargetPatId = myFhirClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless();

		Encounter enc1 = new Encounter();
		enc1.setStatus(EncounterStatus.CANCELLED);
		enc1.getSubject().setReferenceElement(mySourcePatId);
		enc1.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId1 = myFhirClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless();

		Encounter enc2 = new Encounter();
		enc2.setStatus(EncounterStatus.ARRIVED);
		enc2.getSubject().setReferenceElement(mySourcePatId);
		enc2.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId2 = myFhirClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless();

		Task task = new Task();
		task.setStatus(Task.TaskStatus.COMPLETED);
		task.getOwner().setReferenceElement(mySourcePatId);
		mySourceTaskId = myFhirClient.create().resource(task).execute().getId().toUnqualifiedVersionless();

		Encounter targetEnc1 = new Encounter();
		targetEnc1.setStatus(EncounterStatus.ARRIVED);
		targetEnc1.getSubject().setReferenceElement(myTargetPatId);
		targetEnc1.getServiceProvider().setReferenceElement(myOrgId);
		this.myTargetEnc1 = myFhirClient.create().resource(targetEnc1).execute().getId().toUnqualifiedVersionless();

		mySourceObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(mySourcePatId);
			obs.setStatus(ObservationStatus.FINAL);
			IIdType obsId = myFhirClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();
			mySourceObsIds.add(obsId);
		}

		myResultPatient = new Patient();
		myResultPatient.setIdElement((IdType) myTargetPatId);
		myResultPatient.addIdentifier(pat1IdentifierA);
		Patient.PatientLinkComponent link = myResultPatient.addLink();
		link.setOther(new Reference(mySourcePatId));
		link.setType(Patient.LinkType.REPLACES);
	}

	@ParameterizedTest
	@CsvSource({
		// withDelete, withInputResultPatient, withPreview, isAsync
		"true, true, true, false",
		"true, false, true, false",
		"false, true, true, false",
		"false, false, true, false",
		"true, true, false, false",
		"true, false, false, false",
		"false, true, false, false",
		"false, false, false, false",

		"true, true, true, true",
		"true, false, true, true",
		"false, true, true, true",
		"false, false, true, true",
		"true, true, false, true",
		"true, false, false, true",
		"false, true, false, true",
		"false, false, false, true",
	})
	public void testMergeWithoutResult(boolean withDelete, boolean withInputResultPatient, boolean withPreview, boolean isAsync) throws Exception {
		// setup

		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatId);
		inParams.targetPatient = new Reference().setReferenceElement(myTargetPatId);
		inParams.deleteSource = withDelete;
		if (withInputResultPatient) {
			inParams.resultPatient = myResultPatient;
		}
		if (withPreview) {
			inParams.preview = true;
		}

		Parameters inParameters = inParams.asParametersResource();

		// exec
		Parameters outParams = callMergeOperation(inParameters, isAsync);

		// validate
		assertThat(outParams.getParameter()).hasSize(3);

		// Assert input
		Parameters input = (Parameters) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_INPUT).getResource();
		if (withInputResultPatient) { // if the following assert fails, check that these two patients are identical
			Patient p1 = (Patient) inParameters.getParameter(OPERATION_MERGE_RESULT_PATIENT).getResource();
			Patient p2 = (Patient) input.getParameter(OPERATION_MERGE_RESULT_PATIENT).getResource();
			ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p1));
			ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p2));
		}
		assertTrue(input.equalsDeep(inParameters));

		// Assert outcome
		OperationOutcome outcome;

		// Assert Task
		if (isAsync) {
			Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
			await().until(() -> task.getStatus() == Task.TaskStatus.COMPLETED);

			Task taskWithOutput = myTaskDao.read(task.getIdElement(), mySrd);

			Task.TaskOutputComponent taskOutput = taskWithOutput.getOutputFirstRep();

			// Assert on the output type
			Coding taskType = taskOutput.getType().getCodingFirstRep();
			assertEquals("http://hl7.org/fhir/ValueSet/resource-types", taskType.getSystem());
			assertEquals("OperationOutcome", taskType.getCode());

			List<Resource> containedResources = taskWithOutput.getContained();
			assertThat(containedResources)
				.hasSize(1)
				.element(0)
				.isInstanceOf(OperationOutcome.class);

			OperationOutcome containedOutcome = (OperationOutcome) containedResources.get(0);

			Reference outputRef = (Reference) taskOutput.getValue();
			outcome = (OperationOutcome) outputRef.getResource();
			assertTrue(containedOutcome.equalsDeep(outcome));
		} else {
			outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		}

		if (withPreview) {
			assertThat(outcome.getIssue())
				.hasSize(1)
				.element(0)
				.satisfies(issue -> {
					assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
					assertThat(issue.getDetails().getText()).isEqualTo("Preview only merge operation - no issues detected");
					assertThat(issue.getDiagnostics()).isEqualTo("Merge would update 25 resources");
				});
		} else {
			assertThat(outcome.getIssue())
				.hasSize(1)
				.element(0)
				.satisfies(issue -> {
					assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
					assertThat(issue.getDiagnostics()).isEqualTo("Merge operation completed successfully.");
				});
		}

		// Assert Merged Patient
		Patient mergedPatient = (Patient) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_RESULT).getResource();
		List<Identifier> identifiers = mergedPatient.getIdentifier();
		if (withInputResultPatient) {
			assertThat(identifiers).hasSize(1);
			assertThat(identifiers.get(0).getSystem()).isEqualTo("SYS1A");
			assertThat(identifiers.get(0).getValue()).isEqualTo("VAL1A");
		} else {
			assertThat(identifiers).hasSize(5);
			assertThat(identifiers)
				.extracting(Identifier::getSystem)
				.containsExactlyInAnyOrder("SYS1A", "SYS1B", "SYS2A", "SYS2B", "SYSC");
			assertThat(identifiers)
				.extracting(Identifier::getValue)
				.containsExactlyInAnyOrder("VAL1A", "VAL1B", "VAL2A", "VAL2B", "VALC");
		}
		if (!withPreview && !withDelete) {
			// assert source has link to target
			Patient source = myPatientDao.read(mySourcePatId, mySrd);
			assertThat(source.getLink())
				.hasSize(1)
				.element(0)
				.extracting(link -> link.getOther().getReferenceElement())
				.isEqualTo(myTargetPatId);
		}

		// Check that the linked resources were updated

		Bundle bundle = fetchBundle(myServerBase + "/" + myTargetPatId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<IIdType> actual = new HashSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless());
		}

		ourLog.info("Found IDs: {}", actual);

		if (withPreview) {
			assertThat(actual).doesNotContain(mySourcePatId);
			assertThat(actual).doesNotContain(mySourceEncId1);
			assertThat(actual).doesNotContain(mySourceEncId2);
			assertThat(actual).contains(myOrgId);
			assertThat(actual).doesNotContain(mySourceTaskId);
			assertThat(actual).doesNotContainAnyElementsOf(mySourceObsIds);
			assertThat(actual).contains(myTargetPatId);
			assertThat(actual).contains(myTargetEnc1);
		} else {
			if (withDelete) {
				assertThat(actual).doesNotContain(mySourcePatId);
			}
			assertThat(actual).contains(mySourceEncId1);
			assertThat(actual).contains(mySourceEncId2);
			assertThat(actual).contains(myOrgId);
			assertThat(actual).contains(mySourceTaskId);
			assertThat(actual).containsAll(mySourceObsIds);
			assertThat(actual).contains(myTargetPatId);
			assertThat(actual).contains(myTargetEnc1);
		}
	}

	@ParameterizedTest
	@CsvSource({
		// withDelete, withInputResultPatient, withPreview
		"true, true, true",
		"true, false, true",
		"false, true, true",
		"false, false, true",
		"true, true, false",
		"true, false, false",
		"false, true, false",
		"false, false, false",
	})
	public void testMultipleTargetMatchesFails(boolean withDelete, boolean withInputResultPatient, boolean withPreview) throws Exception {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatId);
		inParams.targetPatientIdentifier = patBothIdentifierC;
		inParams.deleteSource = withDelete;
		if (withInputResultPatient) {
			inParams.resultPatient = myResultPatient;
		}
		if (withPreview) {
			inParams.preview = true;
		}


		Parameters inParameters = inParams.asParametersResource();

		assertUnprocessibleEntityWithMessage(inParameters, "Multiple resources found matching the identifier(s) specified in 'target-patient-identifier'");
	}


	@ParameterizedTest
	@CsvSource({
		// withDelete, withInputResultPatient, withPreview
		"true, true, true",
		"true, false, true",
		"false, true, true",
		"false, false, true",
		"true, true, false",
		"true, false, false",
		"false, true, false",
		"false, false, false",
	})
	public void testMultipleSourceMatchesFails(boolean withDelete, boolean withInputResultPatient, boolean withPreview) throws Exception {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatientIdentifier = patBothIdentifierC;
		inParams.targetPatient = new Reference().setReferenceElement(mySourcePatId);
		inParams.deleteSource = withDelete;
		if (withInputResultPatient) {
			inParams.resultPatient = myResultPatient;
		}
		if (withPreview) {
			inParams.preview = true;
		}

		Parameters inParameters = inParams.asParametersResource();

		assertUnprocessibleEntityWithMessage(inParameters, "Multiple resources found matching the identifier(s) specified in 'source-patient-identifier'");
	}

	private void assertUnprocessibleEntityWithMessage(Parameters inParameters, String theExpectedMessage) {
		assertThatThrownBy(() ->
			callMergeOperation(inParameters))
			.isInstanceOf(UnprocessableEntityException.class)
			.extracting(e -> extractFailureMessage((UnprocessableEntityException) e))
			.isEqualTo(theExpectedMessage);
	}

	private Parameters callMergeOperation(Parameters inParameters) {
		return this.callMergeOperation(inParameters, false);
	}

	private Parameters callMergeOperation(Parameters inParameters, boolean isAsync) {
		IOperationUntypedWithInput<Parameters> request = myClient.operation()
			.onType("Patient")
			.named(OPERATION_MERGE)
			.withParameters(inParameters);

		if (isAsync) {
			request.withAdditionalHeader(HEADER_PREFER, HEADER_PREFER_RESPOND_ASYNC);
		}

		return request
			.returnResourceType(Parameters.class)
			.execute();
	}

	@Test
	void test_MissingRequiredParameters_Returns400BadRequest() {
		assertThatThrownBy(() -> callMergeOperation(new Parameters())
		).isInstanceOf(InvalidRequestException.class)
			.extracting(e -> ((InvalidRequestException) e).getStatusCode())
			.isEqualTo(400);
	}

	// FIXME KHS look at PatientEverythingR4Test for ideas for other tests

	private Bundle fetchBundle(String theUrl, EncodingEnum theEncoding) throws IOException {
		Bundle bundle;
		HttpGet get = new HttpGet(theUrl);
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			assertEquals(theEncoding.getResourceContentTypeNonLegacy(), resp.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue().replaceAll(";.*", ""));
			bundle = theEncoding.newParser(myFhirContext).parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8));
		} finally {
			IOUtils.closeQuietly(resp);
		}

		return bundle;
	}


	private static class PatientMergeInputParameters {
		Type sourcePatient;
		Type sourcePatientIdentifier;
		Type targetPatient;
		Type targetPatientIdentifier;
		Patient resultPatient;
		Boolean preview;
		Boolean deleteSource;

		public Parameters asParametersResource() {
			Parameters inParams = new Parameters();
			if (sourcePatient != null) {
				inParams.addParameter().setName("source-patient").setValue(sourcePatient);
			}
			if (sourcePatientIdentifier != null) {
				inParams.addParameter().setName("source-patient-identifier").setValue(sourcePatientIdentifier);
			}
			if (targetPatient != null) {
				inParams.addParameter().setName("target-patient").setValue(targetPatient);
			}
			if (targetPatientIdentifier != null) {
				inParams.addParameter().setName("target-patient-identifier").setValue(targetPatientIdentifier);
			}
			if (resultPatient != null) {
				inParams.addParameter().setName("result-patient").setResource(resultPatient);
			}
			if (preview != null) {
				inParams.addParameter().setName("preview").setValue(new BooleanType(preview));
			}
			if (deleteSource != null) {
				inParams.addParameter().setName("delete-source").setValue(new BooleanType(deleteSource));
			}
			return inParams;
		}
	}


	static class MyExceptionHandler implements TestExecutionExceptionHandler {
		@Override
		public void handleTestExecutionException(ExtensionContext theExtensionContext, Throwable theThrowable) throws Throwable {
			if (theThrowable instanceof BaseServerResponseException ex) {
				String message = extractFailureMessage(ex);
				throw ex.getClass().getDeclaredConstructor(String.class, Throwable.class).newInstance(message, ex);
			}
			throw theThrowable;
		}
	}

	private static @NotNull String extractFailureMessage(BaseServerResponseException ex) {
		String body = ex.getResponseBody();
		Parameters outParams = ourFhirContext.newJsonParser().parseResource(Parameters.class, body);
		OperationOutcome outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		String message = outcome.getIssue().stream().map(issue -> issue.getDiagnostics()).collect(Collectors.joining(", "));
		return message;
	}
}
