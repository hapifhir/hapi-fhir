package ca.uhn.fhir.batch2.jobs.export;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.rest.api.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkExportJobParametersValidatorTest {

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IBinaryStorageSvc myIBinaryStorageSvc;

	@InjectMocks
	private BulkExportJobParametersValidator myValidator;

	private BulkExportJobParameters createSystemExportParameters() {
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		return parameters;
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		Patient/A              , Patient/B , true  , Patient/A , Patient/B
		A                      ,           , true  , Patient/A , Patient/B
		Organization/A         ,           , false , Invalid Patient ID (values should use the format "Patient/[id]"): Organization/A ,
		Patient/A/_history/1   ,           , false , Invalid Patient ID: Patient/A/_history/1 ,
		Patient/A B            ,           , false , Invalid Patient ID: Patient/A B ,
		A B                    ,           , false , Invalid Patient ID (values should use the format "Patient/[id]"): A B ,
		""")
	public void validate_PatientIdParameters(String theInput0, String theInput1, boolean theExpectedPass, String theExpectedOutput0, String theExpectedOutput1) {
		// setup
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);

		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);

		List<String> patientIds = new ArrayList<>();
		if (isNotBlank(theInput0)) {
			patientIds.add(theInput0.trim());
		}
		if (isNotBlank(theInput1)) {
			patientIds.add(theInput1.trim());
		}
		parameters.setPatientIds(patientIds);

		// Test
		List<String> errors = myValidator.validate(null, parameters);

		// Verify
		if (theExpectedPass) {
			assertThat(errors).isEmpty();
			if (isNotBlank(theInput0)) {
				assertEquals(theExpectedOutput0, parameters.getPatientIds().get(0));
			}
			if (isNotBlank(theInput1)) {
				assertEquals(theExpectedOutput1, parameters.getPatientIds().get(1));
			}
		} else {
			assertThat(errors).isNotEmpty();
			if (isNotBlank(theInput0)) {
				assertEquals(theExpectedOutput0, errors.get(0));
			}
			if (isNotBlank(theInput1)) {
				assertEquals(theExpectedOutput1, errors.get(1));
			}
		}
		assertEquals(theExpectedPass, errors.isEmpty());
	}

	@Test
	public void validate_validParametersForSystem_returnsEmptyList() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();

		// when
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);

		// test
		List<String> result = myValidator.validate(null, parameters);

		// verify
		assertNotNull(result);
		assertThat(result).isEmpty();
	}


	@Test
	public void validate_exportId_illegal_characters() {
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportIdentifier("exportId&&&");
		// when
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);
		when(myIBinaryStorageSvc.isValidBinaryContentId(any())).thenReturn(false);
		List<String> errors = myValidator.validate(null, parameters);

		// verify
		assertNotNull(errors);
		assertThat(errors).hasSize(1);
		assertEquals(errors.get(0), "Export ID does not conform to the current blob storage implementation's limitations.");
	}

	@Test
	public void validate_exportId_legal_characters() {
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportIdentifier("HELLO!/WORLD/");
		// when
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);

		when(myIBinaryStorageSvc.isValidBinaryContentId(any())).thenReturn(true);
		List<String> errors = myValidator.validate(null, parameters);

		// verify
		assertNotNull(errors);
		assertThat(errors).isEmpty();
	}
	@Test
	public void validate_validParametersForPatient_returnsEmptyList() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);

		// when
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);

		// test
		List<String> result = myValidator.validate(null, parameters);

		// verify
		assertNotNull(result);
		assertThat(result).isEmpty();
	}

	@Test
	public void validate_invalidResourceType_returnsError() {
		// setup
		String resourceType = "notValid";
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		parameters.setResourceTypes(Collections.singletonList(resourceType));

		// test
		List<String> result = myValidator.validate(null, parameters);

		// verify
		assertNotNull(result);
		assertThat(result).isNotEmpty();
		assertThat(result.get(0)).contains("Resource type " + resourceType + " is not a supported resource type");
	}

	@Test
	public void validate_validateParametersForGroup_returnsEmptyList() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		parameters.setGroupId("groupId");
		parameters.setExpandMdm(true);

		// when
		when(myDaoRegistry.isResourceTypeSupported(anyString()))
			.thenReturn(true);

		// test
		List<String> result = myValidator.validate(null, parameters);

		// verify
		assertNotNull(result);
		assertThat(result).isEmpty();
	}

	@Test
	public void validate_groupParametersWithoutGroupId_returnsError() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);

		// test
		List<String> result = myValidator.validate(null, parameters);

		// verify
		assertNotNull(result);
		assertThat(result).isNotEmpty();
		assertThat(result).contains("Group export requires a group id, but none provided.");
	}

	@Test
	public void validate_omittedResourceTypes_returnsNoErrorMessages() {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setResourceTypes(null);

		// test
		List<String> results = myValidator.validate(null, parameters);

		// verify
		assertNotNull(results);
		assertThat(results).isEmpty();
	}

	@Test
	public void validate_binaryExport_returnsErrors() {
		// setup
		String jobId = "jobId";
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setResourceTypes(Arrays.asList("Patient", "Binary"));
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);

		// test
		List<String> errors = myValidator.validate(null, parameters);

		// validate
		assertNotNull(errors);
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Bulk export of Binary resources is forbidden");
	}

	@Test
	public void validate_incorrectOutputFormat_returnsErrors() {
		// setup
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setResourceTypes(Arrays.asList("Patient", "Observation"));
		parameters.setOutputFormat("json");

		// test
		List<String> errors = myValidator.validate(null, parameters);

		// validate
		assertNotNull(errors);
		assertThat(errors)
			.isNotEmpty()
			.contains("The allowed formats for Bulk Export are %s, %s and %s"
				.formatted(Constants.CT_FHIR_NDJSON, Constants.CT_APP_NDJSON, Constants.CT_NDJSON));
	}

	@ParameterizedTest
	@ValueSource(strings = {Constants.CT_FHIR_NDJSON, Constants.CT_APP_NDJSON, Constants.CT_NDJSON})
	public void validate_allowedOutputFormat_returnsEmptyList(String outputFormat) {
		// setup
		BulkExportJobParameters parameters = createSystemExportParameters();
		parameters.setOutputFormat(outputFormat);
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		parameters.setGroupId("groupId");

		when(myDaoRegistry.isResourceTypeSupported(anyString())).thenReturn(true);

		// execute
		List<String> result = myValidator.validate(null, parameters);

		// verify
		assertNotNull(result);
		assertThat(result).isEmpty();
	}
}
