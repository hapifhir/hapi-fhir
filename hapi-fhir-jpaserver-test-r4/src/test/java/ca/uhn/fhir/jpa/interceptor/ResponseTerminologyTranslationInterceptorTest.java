package ca.uhn.fhir.jpa.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationInterceptor;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;


public class ResponseTerminologyTranslationInterceptorTest extends BaseResourceProviderR4Test {

	public static final String TEST_OBV_FILTER = "Observation?status=amended";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseTerminologyTranslationInterceptorTest.class);
	@Autowired
	private ResponseTerminologyTranslationInterceptor myResponseTerminologyTranslationInterceptor;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@BeforeEach
	public void beforeEach() {
		myConceptMapDao.create(createConceptMap());
		myServer.registerInterceptor(myResponseTerminologyTranslationInterceptor);
	}

	@AfterEach
	public void afterEach() {
		myResponseTerminologyTranslationInterceptor.clearMappingSpecifications();
		myServer.unregisterInterceptor(myResponseTerminologyTranslationInterceptor);
		myStorageSettings.setNormalizeTerminologyForBulkExportJobs(new StorageSettings().isNormalizeTerminologyForBulkExportJobs());
	}

	@Test
	public void testMapConcept_MappingFound() {
		myResponseTerminologyTranslationInterceptor.addMappingSpecification(CS_URL, CS_URL_2);

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.getCode()
			.addCoding(new Coding(CS_URL, "12345", null));
		IIdType id = myObservationDao.create(observation).getId();

		// Read it back
		observation = myClient.read().resource(Observation.class).withId(id).execute();

		assertThat(toCodeStrings(observation)).as(toCodeStrings(observation).toString()).containsExactly("[system=http://example.com/my_code_system, code=12345, display=null]", "[system=http://example.com/my_code_system2, code=34567, display=Target Code 34567]");
	}

	@Test
	public void testMapConcept_MultipleMappingsFound() {
		myResponseTerminologyTranslationInterceptor.addMappingSpecification(CS_URL, CS_URL_3);

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.getCode()
			.addCoding(new Coding(CS_URL, "12345", null));
		IIdType id = myObservationDao.create(observation).getId();

		// Read it back
		observation = myClient.read().resource(Observation.class).withId(id).execute();

		assertThat(toCodeStrings(observation)).as(toCodeStrings(observation).toString()).containsExactly("[system=http://example.com/my_code_system, code=12345, display=null]", "[system=http://example.com/my_code_system3, code=56789, display=Target Code 56789]", "[system=http://example.com/my_code_system3, code=67890, display=Target Code 67890]");
	}

	/**
	 * Don't map if we already have a code in the desired target
	 */
	@Test
	public void testMapConcept_MappingNotNeeded() {
		myResponseTerminologyTranslationInterceptor.addMappingSpecification(CS_URL, CS_URL_2);

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.getCode()
			.addCoding(new Coding(CS_URL, "12345", null))
			.addCoding(new Coding(CS_URL_2, "9999", "Display 9999"));
		IIdType id = myObservationDao.create(observation).getId();

		// Read it back
		observation = myClient.read().resource(Observation.class).withId(id).execute();

		assertThat(toCodeStrings(observation)).as(toCodeStrings(observation).toString()).containsExactly("[system=http://example.com/my_code_system, code=12345, display=null]", "[system=http://example.com/my_code_system2, code=9999, display=Display 9999]");
	}

	@Test
	public void testMapConcept_NoMappingExists() {
		myResponseTerminologyTranslationInterceptor.addMappingSpecification(CS_URL, CS_URL_2);

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.getCode()
			.addCoding(new Coding(CS_URL, "FOO", null));
		IIdType id = myObservationDao.create(observation).getId();

		// Read it back
		observation = myClient.read().resource(Observation.class).withId(id).execute();

		assertThat(toCodeStrings(observation)).as(toCodeStrings(observation).toString()).containsExactly("[system=http://example.com/my_code_system, code=FOO, display=null]");
	}

	@Test
	public void testBulkExport_TerminologyTranslation_MappingFound() {
		myStorageSettings.setNormalizeTerminologyForBulkExportJobs(true);

		// Create some resources to load
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.getCode()
			.addCoding(new Coding(CS_URL, "12345", null));
		myObservationDao.create(observation);

		// set mapping specs
		myResponseTerminologyTranslationInterceptor.addMappingSpecification(CS_URL, CS_URL_2);
		List<String> codingList = Arrays.asList(
			"{\"system\":\"http://example.com/my_code_system\",\"code\":\"12345\"}",
			"{\"system\":\"http://example.com/my_code_system2\",\"code\":\"34567\",\"display\":\"Target Code 34567\"}");

		createBulkJobAndCheckCodingList(codingList);
	}

	@Test
	public void testBulkExport_TerminologyTranslation_MappingNotNeeded() {
		myStorageSettings.setNormalizeTerminologyForBulkExportJobs(true);

		// Create some resources to load
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.getCode()
			.addCoding(new Coding(CS_URL, "12345", null))
			.addCoding(new Coding(CS_URL_2, "9999", "Display 9999"));
		myObservationDao.create(observation);

		// set mapping specs
		myResponseTerminologyTranslationInterceptor.addMappingSpecification(CS_URL, CS_URL_2);
		List<String> codingList = Arrays.asList(
			"{\"system\":\"http://example.com/my_code_system\",\"code\":\"12345\"}",
			"{\"system\":\"http://example.com/my_code_system2\",\"code\":\"9999\",\"display\":\"Display 9999\"}");

		createBulkJobAndCheckCodingList(codingList);
	}

	@Test
	public void testBulkExport_TerminologyTranslation_NoMapping() {
		myStorageSettings.setNormalizeTerminologyForBulkExportJobs(true);

		// Create some resources to load
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.getCode()
			.addCoding(new Coding(CS_URL, "12345", null));
		myObservationDao.create(observation);

		List<String> codingList = List.of(
			"{\"system\":\"http://example.com/my_code_system\",\"code\":\"12345\"}");

		createBulkJobAndCheckCodingList(codingList);
	}

	private void createBulkJobAndCheckCodingList(List<String> codingList) {
		// Create a bulk job
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Observation"));
		options.setFilters(Sets.newHashSet(TEST_OBV_FILTER));
		options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		startRequest.setParameters(options);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);

		assertNotNull(startResponse);

		// Run a scheduled pass to build the export
		myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId());

		await().until(() -> myJobCoordinator.getInstance(startResponse.getInstanceId()).getReport() != null);

		// Iterate over the files
		String report = myJobCoordinator.getInstance(startResponse.getInstanceId()).getReport();
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
		for (Map.Entry<String, List<String>> file : results.getResourceTypeToBinaryIds().entrySet()) {
			String resourceTypeInFile = file.getKey();
			List<String> binaryIds = file.getValue();
			assertThat(binaryIds).hasSize(1);
			for (String binaryId : binaryIds) {
				Binary binary = myBinaryDao.read(new IdType(binaryId));
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
				String contents = new String(binary.getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Next contents for type {} :\n{}", binary.getResourceType(), contents);
				if ("Observation".equals(resourceTypeInFile)) {
					for (String code : codingList) {
						assertThat(contents).contains(code);
					}
				} else {
					fail(resourceTypeInFile);
				}
			}
		}
	}

	@Nonnull
	private List<String> toCodeStrings(Observation observation) {
		return observation.getCode().getCoding().stream().map(t -> "[system=" + t.getSystem() + ", code=" + t.getCode() + ", display=" + t.getDisplay() + "]").collect(Collectors.toList());
	}
}
