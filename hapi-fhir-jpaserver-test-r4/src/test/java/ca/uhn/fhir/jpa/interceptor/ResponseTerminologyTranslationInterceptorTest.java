package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.BulkExportUtils;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationInterceptor;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ResponseTerminologyTranslationInterceptorTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseTerminologyTranslationInterceptorTest.class);
	public static final String TEST_OBV_FILTER = "Observation?status=amended";

	@Autowired
	private ResponseTerminologyTranslationInterceptor myResponseTerminologyTranslationInterceptor;

	@Autowired
	private IBatch2JobRunner myJobRunner;

	@BeforeEach
	public void beforeEach() {
		myConceptMapDao.create(createConceptMap());
		ourRestServer.registerInterceptor(myResponseTerminologyTranslationInterceptor);
	}

	@AfterEach
	public void afterEach() {
		myResponseTerminologyTranslationInterceptor.clearMappingSpecifications();
		ourRestServer.unregisterInterceptor(myResponseTerminologyTranslationInterceptor);
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

		assertThat(toCodeStrings(observation).toString(), toCodeStrings(observation), Matchers.contains(
			"[system=http://example.com/my_code_system, code=12345, display=null]",
			"[system=http://example.com/my_code_system2, code=34567, display=Target Code 34567]"
		));
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

		assertThat(toCodeStrings(observation).toString(), toCodeStrings(observation), Matchers.contains(
			"[system=http://example.com/my_code_system, code=12345, display=null]",
			"[system=http://example.com/my_code_system3, code=56789, display=Target Code 56789]",
			"[system=http://example.com/my_code_system3, code=67890, display=Target Code 67890]"
		));
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

		assertThat(toCodeStrings(observation).toString(), toCodeStrings(observation), Matchers.contains(
			"[system=http://example.com/my_code_system, code=12345, display=null]",
			"[system=http://example.com/my_code_system2, code=9999, display=Display 9999]"
		));
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

		assertThat(toCodeStrings(observation).toString(), toCodeStrings(observation), Matchers.contains(
			"[system=http://example.com/my_code_system, code=FOO, display=null]"
		));
	}

	@Test
	public void testBulkExport_TerminologyTranslation_MappingFound() {
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
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Observation"));
		options.setFilters(Sets.newHashSet(TEST_OBV_FILTER));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		Batch2JobStartResponse startResponse = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(options));

		assertNotNull(startResponse);

		// Run a scheduled pass to build the export
		myBatch2JobHelper.awaitJobCompletion(startResponse.getJobId());

		await().until(() -> myJobRunner.getJobInfo(startResponse.getJobId()).getReport() != null);

		// Iterate over the files
		String report = myJobRunner.getJobInfo(startResponse.getJobId()).getReport();
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
		for (Map.Entry<String, List<String>> file : results.getResourceTypeToBinaryIds().entrySet()) {
			String resourceTypeInFile = file.getKey();
			List<String> binaryIds = file.getValue();
			assertEquals(1, binaryIds.size());
			for (String binaryId : binaryIds) {
				Binary binary = myBinaryDao.read(new IdType(binaryId));
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
				String contents = new String(binary.getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Next contents for type {} :\n{}", binary.getResourceType(), contents);
				if ("Observation".equals(resourceTypeInFile)) {
					for (String code : codingList) {
						assertThat(contents, containsString(code));
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
