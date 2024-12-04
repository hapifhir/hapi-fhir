package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.base.Charsets;
import jakarta.persistence.Id;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatientMergeR4Test extends BaseResourceProviderR4Test {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientMergeR4Test.class);

	static final Identifier pat1IdentifierA = new Identifier().setSystem("SYS1A").setValue("VAL1A");
	static final Identifier pat1IdentifierB = new Identifier().setSystem("SYS1B").setValue("VAL1B");
	static final Identifier pat2IdentifierA = new Identifier().setSystem("SYS2A").setValue("VAL2A");
	static final Identifier pat2IdentifierB = new Identifier().setSystem("SYS2B").setValue("VAL2B");
	static final Identifier patBothIdentifierC = new Identifier().setSystem("SYSC").setValue("VALC");

	IIdType orgId;
	IIdType sourcePatId;
	IIdType taskId;
	IIdType encId1;
	IIdType encId2;
	ArrayList<IIdType> myObsIds;
	IIdType targetPatId;
	IIdType targetEnc1;

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
		orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		ourLog.info("OrgId: {}", orgId);

		Patient patient1 = new Patient();
		patient1.getManagingOrganization().setReferenceElement(orgId);
		patient1.addIdentifier(pat1IdentifierA);
		patient1.addIdentifier(pat1IdentifierB);
		patient1.addIdentifier(patBothIdentifierC);
		sourcePatId = myClient.create().resource(patient1).execute().getId().toUnqualifiedVersionless();

		Patient patient2 = new Patient();
		patient2.addIdentifier(pat2IdentifierA);
		patient2.addIdentifier(pat2IdentifierB);
		patient1.addIdentifier(patBothIdentifierC);
		patient2.getManagingOrganization().setReferenceElement(orgId);
		targetPatId = myClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless();

		Encounter enc1 = new Encounter();
		enc1.setStatus(EncounterStatus.CANCELLED);
		enc1.getSubject().setReferenceElement(sourcePatId);
		enc1.getServiceProvider().setReferenceElement(orgId);
		encId1 = myClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless();

		Encounter enc2 = new Encounter();
		enc2.setStatus(EncounterStatus.ARRIVED);
		enc2.getSubject().setReferenceElement(sourcePatId);
		enc2.getServiceProvider().setReferenceElement(orgId);
		encId2 = myClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless();

		Task task = new Task();
		task.setStatus(Task.TaskStatus.COMPLETED);
		task.getOwner().setReferenceElement(sourcePatId);
		taskId = myClient.create().resource(task).execute().getId().toUnqualifiedVersionless();

		Encounter targetEnc1 = new Encounter();
		targetEnc1.setStatus(EncounterStatus.ARRIVED);
		targetEnc1.getSubject().setReferenceElement(targetPatId);
		targetEnc1.getServiceProvider().setReferenceElement(orgId);
		this.targetEnc1 = myClient.create().resource(targetEnc1).execute().getId().toUnqualifiedVersionless();

		myObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(sourcePatId);
			obs.setStatus(ObservationStatus.FINAL);
			IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();
			myObsIds.add(obsId);
		}

	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testMergeWithoutResult(boolean withDelete) throws Exception {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatient = new Reference().setReferenceElement(sourcePatId);
		inParams.targetPatient = new Reference().setReferenceElement(targetPatId);
		inParams.deleteSource = withDelete;

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
		assertTrue(input.equalsDeep(inParameters));


		// Assert outcome
		OperationOutcome outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		List<OperationOutcome.OperationOutcomeIssueComponent> issues = outcome.getIssue();
		assertThat(issues).hasSize(1);
		OperationOutcome.OperationOutcomeIssueComponent issue = issues.get(0);
		assertEquals("Merge operation completed successfully.", issue.getDiagnostics());
		assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);

		// Assert Merged Patient
		Patient mergedPatient = (Patient) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_RESULT).getResource();
		List<Identifier> identifiers = mergedPatient.getIdentifier();
		assertThat(identifiers).hasSize(5);

		// FIXME KHS assert on identifier contents

		if (!withDelete) {
			// assert source has link to target
			Patient source = myPatientDao.read(sourcePatId, mySrd);
			List<Patient.PatientLinkComponent> links = source.getLink();
			assertThat(links).hasSize(1);
			Patient.PatientLinkComponent link = links.get(0);
			assertThat(link.getOther().getReferenceElement()).isEqualTo(targetPatId);
		}


		// FIXME KHS assert on these three

		Bundle bundle = fetchBundle(myServerBase + "/" + targetPatId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<IIdType> actual = new HashSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless());
		}

		ourLog.info("Found IDs: {}", actual);

		if (withDelete) {
			assertThat(actual).doesNotContain(sourcePatId);
		}
		assertThat(actual).contains(encId1);
		assertThat(actual).contains(encId2);
		assertThat(actual).contains(orgId);
		assertThat(actual).contains(taskId);
		assertThat(actual).containsAll(myObsIds);
		assertThat(actual).contains(targetPatId);
		assertThat(actual).contains(targetEnc1);
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
		Patient resultResource;
		Boolean preview;
		Boolean deleteSource;

		public Parameters asParametersResource() {
			Parameters inParams = new Parameters();
			if (sourcePatient != null) {
				inParams.addParameter().setName("source-patient").setValue(sourcePatient);
			}
			if (sourcePatientIdentifier!= null) {
				inParams.addParameter().setName("source-patient-identifier").setValue(sourcePatientIdentifier);
			}
			if (targetPatient != null) {
				inParams.addParameter().setName("target-patient").setValue(targetPatient);
			}
			if (targetPatientIdentifier != null) {
				inParams.addParameter().setName("target-patient-identifier").setValue(targetPatientIdentifier);
			}
			if (resultResource != null) {
				inParams.addParameter().setName("result-patient").setResource(resultResource);
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


}
