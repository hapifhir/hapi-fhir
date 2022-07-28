package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkExportJobParametersValidatorTest {

	@Mock
	private DaoRegistry myDaoRegistry;

	@InjectMocks
	private BulkExportJobParametersValidator myValidator;

	private BulkExportJobParameters createSystemExportParameters() {
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		return parameters;
	}

	@Test
	public void validate_validParametersForSystem_returnsEmptyList() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();

		// when
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void validate_validParametersForPatient_returnsEmptyList() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);

		// when
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void validate_invalidResourceType_returnsError() {
		// setup
		String resourceType = "notValid";
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		parameters.setResourceTypes(Collections.singletonList(resourceType));

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertTrue(result.get(0).contains("Resource type " + resourceType + " is not a supported resource type"));
	}

	@Test
	public void validate_validateParametersForGroup_returnsEmptyList() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		parameters.setGroupId("groupId");
		parameters.setExpandMdm(true);

		// when
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void validate_groupParametersWithoutGroupId_returnsError() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);

		// test
		List<String> result = myValidator.validate(parameters);

		// verify
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertTrue(result.contains("Group export requires a group id, but none provided."));
	}

	@Test
	public void validate_omittedResourceTypes_returnsErrorMessages() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setResourceTypes(null);

		// test
		List<String> results = myValidator.validate(parameters);

		// verify
		assertNotNull(results);
		assertEquals(1, results.size());
		assertTrue(results.contains("Resource Types are required for an export job."));
	}

	@Test
	public void validate_binaryExport_returnsErrors() {
		// setup
		String jobId = "jobId";
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setResourceTypes(Arrays.asList("Patient", "Binary"));
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);

		// test
		List<String> errors = myValidator.validate(parameters);

		// validate
		assertNotNull(errors);
		assertFalse(errors.isEmpty());
		assertTrue(errors.contains("Bulk export of Binary resources is forbidden"));
	}

	@Test
	public void validate_incorrectOutputFormat_returnsErrors() {
		// setup
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		parameters.setOutputFormat("json");

		// test
		List<String> errors = myValidator.validate(parameters);

		// validate
		assertNotNull(errors);
		assertFalse(errors.isEmpty());
		assertTrue(errors.contains("The only allowed format for Bulk Export is currently " + Constants.CT_FHIR_NDJSON));
	}
}
