package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
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
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.Extension;
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

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_RESULT_PATIENT;
import static org.assertj.core.api.Assertions.assertThat;
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
	IIdType myTaskId;
	IIdType myEncId1;
	IIdType myEncId2;
	ArrayList<IIdType> myObsIds;
	IIdType myTargetPatId;
	IIdType myTargetEnc1;
	Patient myResultPatient;

	@RegisterExtension
	static MyExceptionHandler ourExceptionHandler = new MyExceptionHandler();

	@BeforeEach
	public void beforeDisableResultReuse() {
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
	}

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
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);

		Organization org = new Organization();
		org.setName("an org");
		myOrgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		ourLog.info("OrgId: {}", myOrgId);

		Patient patient1 = new Patient();
		patient1.getManagingOrganization().setReferenceElement(myOrgId);
		patient1.addIdentifier(pat1IdentifierA);
		patient1.addIdentifier(pat1IdentifierB);
		patient1.addIdentifier(patBothIdentifierC);
		mySourcePatId = myClient.create().resource(patient1).execute().getId().toUnqualifiedVersionless();

		Patient patient2 = new Patient();
		patient2.addIdentifier(pat2IdentifierA);
		patient2.addIdentifier(pat2IdentifierB);
		patient2.addIdentifier(patBothIdentifierC);
		patient2.getManagingOrganization().setReferenceElement(myOrgId);
		myTargetPatId = myClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless();

		Encounter enc1 = new Encounter();
		enc1.setStatus(EncounterStatus.CANCELLED);
		enc1.getSubject().setReferenceElement(mySourcePatId);
		enc1.getServiceProvider().setReferenceElement(myOrgId);
		myEncId1 = myClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless();

		Encounter enc2 = new Encounter();
		enc2.setStatus(EncounterStatus.ARRIVED);
		enc2.getSubject().setReferenceElement(mySourcePatId);
		enc2.getServiceProvider().setReferenceElement(myOrgId);
		myEncId2 = myClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless();

		Task task = new Task();
		task.setStatus(Task.TaskStatus.COMPLETED);
		task.getOwner().setReferenceElement(mySourcePatId);
		myTaskId = myClient.create().resource(task).execute().getId().toUnqualifiedVersionless();

		Encounter targetEnc1 = new Encounter();
		targetEnc1.setStatus(EncounterStatus.ARRIVED);
		targetEnc1.getSubject().setReferenceElement(myTargetPatId);
		targetEnc1.getServiceProvider().setReferenceElement(myOrgId);
		this.myTargetEnc1 = myClient.create().resource(targetEnc1).execute().getId().toUnqualifiedVersionless();

		myObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(mySourcePatId);
			obs.setStatus(ObservationStatus.FINAL);
			IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();
			myObsIds.add(obsId);
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
		// withDelete, withInputResultPatient
		"true, true",
		"true, false",
		"false, true",
		"false, false",
	})
	public void testMergeWithoutResult(boolean withDelete, boolean withInputResultPatient) throws Exception {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatId);
		inParams.targetPatient = new Reference().setReferenceElement(myTargetPatId);
		inParams.deleteSource = withDelete;
		if (withInputResultPatient) {
			inParams.resultPatient = myResultPatient;
		}

		IGenericClient client = myFhirContext.newRestfulGenericClient(myServerBase);
		Parameters inParameters = inParams.asParametersResource();
		Parameters outParams = client.operation()
			.onType("Patient")
			.named(OPERATION_MERGE)
			.withParameters(inParameters)
			.returnResourceType(Parameters.class)
			.execute();

		assertThat(outParams.getParameter()).hasSize(3);

		// Assert income
		Parameters input = (Parameters) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_INPUT).getResource();
		if (withInputResultPatient)	{ // if the following assert fails, check that these two patients are identical
			Patient p1 = (Patient) inParameters.getParameter(OPERATION_MERGE_RESULT_PATIENT).getResource();
			Patient p2 = (Patient) input.getParameter(OPERATION_MERGE_RESULT_PATIENT).getResource();
			ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p1));
			ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p2));
		}
		assertTrue(input.equalsDeep(inParameters));

		// Assert outcome
		OperationOutcome outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		assertThat(outcome.getIssue())
			.hasSize(1)
			.element(0)
			.satisfies(issue -> {
				assertThat(issue.getDiagnostics()).isEqualTo("Merge operation completed successfully.");
				assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			});

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
		if (!withDelete) {
			// assert source has link to target
			Patient source = myPatientDao.read(mySourcePatId, mySrd);
			assertThat(source.getLink())
				.hasSize(1)
				.element(0)
				.extracting(link -> link.getOther().getReferenceElement())
				.isEqualTo(myTargetPatId);
		}

		Bundle bundle = fetchBundle(myServerBase + "/" + myTargetPatId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<IIdType> actual = new HashSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless());
		}

		ourLog.info("Found IDs: {}", actual);

		if (withDelete) {
			assertThat(actual).doesNotContain(mySourcePatId);
		}
		assertThat(actual).contains(myEncId1);
		assertThat(actual).contains(myEncId2);
		assertThat(actual).contains(myOrgId);
		assertThat(actual).contains(myTaskId);
		assertThat(actual).containsAll(myObsIds);
		assertThat(actual).contains(myTargetPatId);
		assertThat(actual).contains(myTargetEnc1);
	}

	@Test
	void test_MissingRequiredParameters_Returns400BadRequest() {
		Parameters inParams = new Parameters();

		IGenericClient client = myFhirContext.newRestfulGenericClient(myServerBase);

		InvalidRequestException thrown = assertThrows(InvalidRequestException.class, () -> client.operation()
			.onType("Patient")
			.named(OPERATION_MERGE)
			.withParameters(inParams)
			.returnResourceType(Parameters.class)
			.execute()
		);

		assertThat(thrown.getStatusCode()).isEqualTo(400);
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
			if (theThrowable instanceof InvalidRequestException ex) {
				String body = ex.getResponseBody();
				Parameters outParams = ourFhirContext.newJsonParser().parseResource(Parameters.class, body);
				OperationOutcome outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
				String message = outcome.getIssue().stream().map(issue -> issue.getDiagnostics()).collect(Collectors.joining(", "));
				throw InvalidRequestException.class.getDeclaredConstructor(String.class, Throwable.class).newInstance(message, ex);
			}
			throw theThrowable;
		}
	}
}
