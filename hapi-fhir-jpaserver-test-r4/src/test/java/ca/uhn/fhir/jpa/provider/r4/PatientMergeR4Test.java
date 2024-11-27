package ca.uhn.fhir.jpa.provider.r4;

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
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatientMergeR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientMergeR4Test.class);
	private String orgId;
	private String sourcePatId;
	private String taskId;
	private String encId1;
	private String encId2;
	private ArrayList<String> myObsIds;
	private String targetPatId;
	private String targetEnc1;

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
		orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless().getValue();
		ourLog.info("OrgId: {}", orgId);

		Patient patient = new Patient();
		patient.getManagingOrganization().setReference(orgId);
		sourcePatId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

		Patient patient2 = new Patient();
		patient2.getManagingOrganization().setReference(orgId);
		targetPatId = myClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc1 = new Encounter();
		enc1.setStatus(EncounterStatus.CANCELLED);
		enc1.getSubject().setReference(sourcePatId);
		enc1.getServiceProvider().setReference(orgId);
		encId1 = myClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc2 = new Encounter();
		enc2.setStatus(EncounterStatus.ARRIVED);
		enc2.getSubject().setReference(sourcePatId);
		enc2.getServiceProvider().setReference(orgId);
		encId2 = myClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless().getValue();

		Task task = new Task();
		task.setStatus(Task.TaskStatus.COMPLETED);
		task.getOwner().setReference(sourcePatId);
		taskId = myClient.create().resource(task).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter targetEnc1 = new Encounter();
		targetEnc1.setStatus(EncounterStatus.ARRIVED);
		targetEnc1.getSubject().setReference(targetPatId);
		targetEnc1.getServiceProvider().setReference(orgId);
		this.targetEnc1 = myClient.create().resource(targetEnc1).execute().getId().toUnqualifiedVersionless().getValue();

		myObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReference(sourcePatId);
			obs.setStatus(ObservationStatus.FINAL);
			String obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless().getValue();
			myObsIds.add(obsId);
		}

	}

	@Test
	public void testMerge() throws Exception {
		OperationParameters params = new OperationParameters();
		params.sourcePatient = new Reference().setReference(sourcePatId);
		params.targetPatient = new Reference().setReference(targetPatId);

		IGenericClient client = myFhirContext.newRestfulGenericClient(myServerBase);
		Parameters outParams = client.operation()
			.onType("Patient")
			.named(OPERATION_MERGE)
			.withParameters(params.asParametersResource())
			.returnResourceType(Parameters.class)
			.execute();

		// FIXME KHS validate outParams

		Bundle bundle = fetchBundle(myServerBase + "/" + targetPatId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		ourLog.info("Found IDs: {}", actual);

		assertThat(actual).doesNotContain(sourcePatId);
		assertThat(actual).contains(encId1);
		assertThat(actual).contains(encId2);
		assertThat(actual).contains(orgId);
		assertThat(actual).contains(taskId);
		assertThat(actual).contains(myObsIds.toArray(new String[0]));
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



	private static class OperationParameters {
		Type sourcePatient;
		Type sourcePatientIdentifier;
		Type targetPatient;
		Type targetPatientIdentifier;
		Patient resultResource;
		Boolean preview;

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
			return inParams;
		}
	}


}
