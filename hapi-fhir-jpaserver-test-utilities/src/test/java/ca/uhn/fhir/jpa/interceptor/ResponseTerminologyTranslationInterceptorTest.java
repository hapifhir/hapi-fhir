package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationInterceptor;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Sets;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ResponseTerminologyTranslationInterceptorTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseTerminologyTranslationInterceptorTest.class);
	public static final String TEST_OBV_FILTER = "Observation?status=amended";

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IInterceptorService myInterceptorBroadcaster;
	@Autowired
	private ResponseTerminologyTranslationInterceptor myResponseTerminologyTranslationInterceptor;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataExportJobSchedulingHelper;

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
		observation			.getCode()
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
		observation			.getCode()
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
		observation			.getCode()
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
		observation			.getCode()
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

		// Create a bulk job
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Observation"));
		options.setFilters(Sets.newHashSet(TEST_OBV_FILTER));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options, true, mySrd);
		assertNotNull(jobDetails.getJobId());

		// Check the status
		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.SUBMITTED, status.getStatus());
		assertEquals("/$export?_outputFormat=application%2Ffhir%2Bndjson&_type=Observation&_typeFilter=" + UrlUtil.escapeUrlParam(TEST_OBV_FILTER), status.getRequest());

		// Run a scheduled pass to build the export
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		// Fetch the job again
		status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.COMPLETE, status.getStatus());

		// Iterate over the files
		for (IBulkDataExportSvc.FileEntry next : status.getFiles()) {
			Binary nextBinary = myBinaryDao.read(next.getResourceId());
			assertEquals(Constants.CT_FHIR_NDJSON, nextBinary.getContentType());
			String nextContents = new String(nextBinary.getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Next contents for type {}:\n{}", next.getResourceType(), nextContents);
			if ("Observation".equals(next.getResourceType())) {
				assertThat(nextContents, containsString(
					"{\"system\":\"http://example.com/my_code_system\",\"code\":\"12345\"}"
				));
				assertThat(nextContents, containsString(
					"{\"system\":\"http://example.com/my_code_system2\",\"code\":\"34567\",\"display\":\"Target Code 34567\"}"
				));
			} else {
				fail(next.getResourceType());
			}
		}

		assertEquals(1, status.getFiles().size());
	}

	@Nonnull
	private List<String> toCodeStrings(Observation observation) {
		return observation.getCode().getCoding().stream().map(t -> "[system=" + t.getSystem() + ", code=" + t.getCode() + ", display=" + t.getDisplay() + "]").collect(Collectors.toList());
	}

	private void awaitAllBulkJobCompletions() {
		myBatchJobHelper.awaitAllBulkJobCompletions(
			BatchConstants.BULK_EXPORT_JOB_NAME,
			BatchConstants.PATIENT_BULK_EXPORT_JOB_NAME,
			BatchConstants.GROUP_BULK_EXPORT_JOB_NAME,
			BatchConstants.DELETE_EXPUNGE_JOB_NAME,
			BatchConstants.MDM_CLEAR_JOB_NAME
		);
	}
}
