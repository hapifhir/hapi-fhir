package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.jpa.api.model.BulkExportJobInfo;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkExportJobParametersValidatorTest {

	@Mock
	private IBulkExportProcessor myProcessor;

	@InjectMocks
	private BulkExportJobParametersValidator myValidator;

	private BulkExportJobParameters createSystemExportParameters(String theJobId) {
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setJobId(theJobId);
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		return parameters;
	}

	@Test
	public void validate_validParametersForSystem_returnsEmptyList() {
		// setup
		String jobId = "jobId";
		BulkExportJobParameters parameters = createSystemExportParameters(jobId);

		// when
		when(myProcessor.getJobInfo(eq(jobId)))
			.thenReturn(getJobInfoForParameters(parameters));

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void validate_validParametersForPatient_returnsEmptyList() {
		// setup
		String jobId = "jobid";
		BulkExportJobParameters parameters = createSystemExportParameters(jobId);
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);

		// when
		when(myProcessor.getJobInfo(eq(jobId)))
			.thenReturn(getJobInfoForParameters(parameters));

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void validate_validateParametersForGroup_returnsEmptyList() {
		// setup
		String jobId = "jobid";
		BulkExportJobParameters parameters = createSystemExportParameters(jobId);
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		parameters.setGroupId("groupId");
		parameters.setExpandMdm(true);

		// when
		when(myProcessor.getJobInfo(eq(jobId)))
			.thenReturn(getJobInfoForParameters(parameters));

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void validate_groupParametersWithoutGroupId_returnsError() {
		// setup
		String jobId = "jobid";
		BulkExportJobParameters parameters = createSystemExportParameters(jobId);
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);

		// when
		when(myProcessor.getJobInfo(eq(jobId)))
			.thenReturn(getJobInfoForParameters(parameters));

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertTrue(result.contains("Group export requires a group id, but none provided for job " + jobId));
	}

	@Test
	public void validate_omittedJobIdAndResourceType_returnsErrorMessages() {
		// setup
		BulkExportJobParameters parameters = new BulkExportJobParameters();

		// test
		List<String> results = myValidator.validate(parameters);

		// verify
		assertNotNull(results);
		assertEquals(2, results.size());
		assertTrue(results.contains("JobId is required to start an export job."));
		assertTrue(results.contains("Resource Types are required for an export job."));
	}

	@Test
	public void validate_noPersistedJob_returnsErrors() {
		// setup
		String jobId = "jobid";
		BulkExportJobParameters jobParameters = new BulkExportJobParameters();
		jobParameters.setJobId(jobId);
		jobParameters.setResourceTypes(Arrays.asList("Patient", "Observation"));

		// test
		List<String> errors = myValidator.validate(jobParameters);

		// verify
		assertNotNull(errors);
		assertEquals(1, errors.size());
		assertTrue(errors.contains("Invalid jobId " + jobId));
	}

	@Test
	public void validate_moreResourceTypesInPersistedJobThanParameters_returnsErrors() {
		// setup
		String jobid = "jobid";
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setJobId(jobid);
		parameters.setResourceTypes(Collections.singletonList("Patient"));
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);

		BulkExportJobInfo info = getJobInfoForParameters(parameters);
		info.getResourceTypes().add("Observation");

		// when
		when(myProcessor.getJobInfo(eq(jobid)))
			.thenReturn(info);

		// test
		List<String> errors = myValidator.validate(parameters);

		// validate
		assertNotNull(errors);
		assertFalse(errors.isEmpty());
		assertTrue(errors.contains("Resource types for job " + jobid + " do not match input parameters."));
	}

	@Test
	public void validate_moreResourceTypesInParametersThanJobInfo_returnsErrors() {
		// setup
		String jobid = "jobid";
		ArrayList<String> resourceTypes = new ArrayList<>();
		resourceTypes.add("Patient");
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setJobId(jobid);
		parameters.setResourceTypes(resourceTypes);
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);

		BulkExportJobInfo info = getJobInfoForParameters(parameters);
		resourceTypes.add("Observation");

		// when
		when(myProcessor.getJobInfo(eq(jobid)))
			.thenReturn(info);

		// test
		List<String> errors = myValidator.validate(parameters);

		// validate
		assertNotNull(errors);
		assertFalse(errors.isEmpty());
		assertTrue(errors.contains("Resource types for job " + jobid + " do not match input parameters."));
		assertTrue(errors.contains("Job must include resource type Observation"));
	}

	@Test
	public void validate_binaryExport_returnsErrors() {
		// setup
		String jobId = "jobId";
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setJobId(jobId);
		parameters.setResourceTypes(Arrays.asList("Patient", "Binary"));
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);

		// when
		when(myProcessor.getJobInfo(eq(jobId)))
			.thenReturn(getJobInfoForParameters(parameters));

		// test
		List<String> errors = myValidator.validate(parameters);

		// validate
		assertNotNull(errors);
		assertFalse(errors.isEmpty());
		assertTrue(errors.contains("Bulk export of Binary resources is verboten"));
	}

	@Test
	public void validate_incorrectOutputFormat_returnsErrors() {
		// setup
		String jobId = "jobId";
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setJobId(jobId);
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		parameters.setOutputFormat("json");

		// when
		when(myProcessor.getJobInfo(eq(jobId)))
			.thenReturn(getJobInfoForParameters(parameters));

		// test
		List<String> errors = myValidator.validate(parameters);

		// validate
		assertNotNull(errors);
		assertFalse(errors.isEmpty());
		assertTrue(errors.contains("The only allowed format for Bulk Export is currently " + Constants.CT_FHIR_NDJSON));
	}

	private BulkExportJobInfo getJobInfoForParameters(BulkExportJobParameters theParameters) {
		BulkExportJobInfo jobInfo = new BulkExportJobInfo();
		jobInfo.setJobId(theParameters.getJobId());
		jobInfo.setResourceTypes(new ArrayList<>(theParameters.getResourceTypes()));
		return jobInfo;
	}
}
