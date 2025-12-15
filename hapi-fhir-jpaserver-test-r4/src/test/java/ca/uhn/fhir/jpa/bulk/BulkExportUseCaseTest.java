package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.jobs.export.BulkExportJobParametersBuilder;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.jpa.test.BulkExportJobHelper;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.svc.MdmExpandersHolder;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.assertj.core.api.AssertionsForClassTypes;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.PARAM_EXPORT_INCLUDE_HISTORY;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.mapping;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class BulkExportUseCaseTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkExportUseCaseTest.class);

	private static final  String TEST_PATIENT_EID_SYS = "http://patient-eid-sys";
	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Autowired
	private IJobPersistence myJobPersistence;
	@Autowired
	private IJobMaintenanceService myJobMaintenanceService;
	@Autowired
	private IBatch2JobInstanceRepository myJobInstanceRepository;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;
	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired
	private MdmRuleValidator myMdmRulesValidator;
	@Autowired
	private MdmExpandersHolder myMdmExpandersHolder;
	private BulkExportJobHelper myBulkExportJobHelper;

	@BeforeEach
	void beforeEach() {
		myStorageSettings.setJobFastTrackingEnabled(false);
		myBulkExportJobHelper = new BulkExportJobHelper(myClient);
	}


	@Nested
	class SpecConformanceTests {

		@Test
		void testBulkExportJobsAreMetaTaggedWithJobIdAndExportId() throws IOException {
			//Given a patient exists
			Patient p = new Patient();
			p.setId("Pat-1");
			myClient.update().resource(p).execute();

			//And Given we start a bulk export job with a specific export id
			String pollingLocation = submitBulkExportForTypesWithExportId("im-an-export-identifier", "Patient");
			String jobId = Batch2JobHelper.getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			//Then: When the poll shows as complete, all attributes should be filled.
			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export?_type=Patient&_exportId=im-an-export-identifier";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				assertEquals(200, status.getStatusLine().getStatusCode());
				String responseContent = IOUtils.toString(status.getEntity().getContent(), UTF_8);
				assertThat(isNotBlank(responseContent)).as(responseContent).isTrue();

				ourLog.info(responseContent);

				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertEquals(expectedOriginalUrl, result.getRequest());
				assertThat(result.getOutput()).isNotEmpty();
				String binaryUrl = result.getOutput().get(0).getUrl();
				Binary binaryResource = myClient.read().resource(Binary.class).withUrl(binaryUrl).execute();

				List<Extension> extension = binaryResource.getMeta().getExtension();
				assertThat(extension).hasSize(3);

				assertEquals(JpaConstants.BULK_META_EXTENSION_EXPORT_IDENTIFIER, extension.get(0).getUrl());
				assertEquals("im-an-export-identifier", extension.get(0).getValue().toString());

				assertEquals(JpaConstants.BULK_META_EXTENSION_JOB_ID, extension.get(1).getUrl());
				assertEquals(jobId, extension.get(1).getValue().toString());

				assertEquals(JpaConstants.BULK_META_EXTENSION_RESOURCE_TYPE, extension.get(2).getUrl());
				assertEquals("Patient", extension.get(2).getValue().toString());
			}
		}

		@Test
		void testBatchJobsAreOnlyReusedIfInProgress() throws IOException {
			//Given a patient exists
			Patient p = new Patient();
			p.setId("Pat-1");
			myClient.update().resource(p).execute();

			//And Given we start a bulk export job
			String pollingLocation = submitBulkExportForTypesWithExportId("my-export-id-", "Patient");
			String jobId = Batch2JobHelper.getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			//When we execute another batch job, it should not have the same job id.
			String secondPollingLocation = submitBulkExportForTypes("Patient");
			String secondJobId = Batch2JobHelper.getJobIdFromPollingLocation(secondPollingLocation);

			//Then the job id should be different
			assertThat(secondJobId).isNotEqualTo(jobId);


			myBatch2JobHelper.awaitJobCompletion(secondJobId);
		}

		@Test
		void testPollingLocationContainsAllRequiredAttributesUponCompletion() throws IOException {

			//Given a patient exists
			Patient p = new Patient();
			p.setId("Pat-1");
			myClient.update().resource(p).execute();

			//And Given we start a bulk export job
			String pollingLocation = submitBulkExportForTypes("Patient");
			String jobId = Batch2JobHelper.getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			//Then: When the poll shows as complete, all attributes should be filled.
			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export?_type=Patient";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				String responseContent = IOUtils.toString(status.getEntity().getContent(), UTF_8);

				ourLog.info(responseContent);

				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertEquals(expectedOriginalUrl, result.getRequest());
				assertEquals(true, result.getRequiresAccessToken());
				assertNotNull(result.getTransactionTime());
				assertThat(result.getOutput()).isNotEmpty();

				//We assert specifically on content as the deserialized version will "helpfully" fill in missing fields.
				assertThat(responseContent).contains("\"error\" : [ ]");
			}
		}

		@Test
		void export_shouldExportPatientResource_whenTypeParameterOmitted() throws IOException {

			//Given a patient exists
			Patient p = new Patient();
			p.setId("Pat-1");
			myClient.update().resource(p).execute();

			//And Given we start a bulk export job
			HttpGet httpGet = new HttpGet(myClient.getServerBase() + "/$export");
			httpGet.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
			String pollingLocation;
			try (CloseableHttpResponse status = ourHttpClient.execute(httpGet)) {
				Header[] headers = status.getHeaders("Content-Location");
				pollingLocation = headers[0].getValue();
			}
			String jobId = Batch2JobHelper.getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			//Then: When the poll shows as complete, all attributes should be filled.
			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				assertEquals(200, status.getStatusLine().getStatusCode());
				String responseContent = IOUtils.toString(status.getEntity().getContent(), UTF_8);
				assertThat(isNotBlank(responseContent)).as(responseContent).isTrue();

				ourLog.info(responseContent);

				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertEquals(expectedOriginalUrl, result.getRequest());
				assertEquals(true, result.getRequiresAccessToken());
				assertNotNull(result.getTransactionTime());
				assertThat(result.getOutput()).isNotEmpty();

				//We assert specifically on content as the deserialized version will "helpfully" fill in missing fields.
				assertThat(responseContent).contains("\"error\" : [ ]");
			}
		}

		@Test
		void export_shouldExportPatientAndObservationAndEncounterResources_whenTypeParameterOmitted() throws IOException {

			Patient patient = new Patient();
			patient.setId("Pat-1");
			myClient.update().resource(patient).execute();

			Observation observation = new Observation();
			observation.setId("Obs-1");
			myClient.update().resource(observation).execute();

			Encounter encounter = new Encounter();
			encounter.setId("Enc-1");
			myClient.update().resource(encounter).execute();

			HttpGet httpGet = new HttpGet(myClient.getServerBase() + "/$export");
			httpGet.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
			String pollingLocation;
			try (CloseableHttpResponse status = ourHttpClient.execute(httpGet)) {
				Header[] headers = status.getHeaders("Content-Location");
				pollingLocation = headers[0].getValue();
			}
			String jobId = Batch2JobHelper.getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				String responseContent = IOUtils.toString(status.getEntity().getContent(), UTF_8);
				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertEquals(expectedOriginalUrl, result.getRequest());
				assertEquals(true, result.getRequiresAccessToken());
				assertNotNull(result.getTransactionTime());
				assertEquals(3, result.getOutput().size());
				assertThat(result.getOutput().stream().filter(o -> o.getType().equals("Patient")).collect(Collectors.toList())).hasSize(1);
				assertThat(result.getOutput().stream().filter(o -> o.getType().equals("Observation")).collect(Collectors.toList())).hasSize(1);
				assertThat(result.getOutput().stream().filter(o -> o.getType().equals("Encounter")).collect(Collectors.toList())).hasSize(1);

				//We assert specifically on content as the deserialized version will "helpfully" fill in missing fields.
				assertThat(responseContent).contains("\"error\" : [ ]");
			}
		}

		@Test
		void export_shouldNotExportBinaryResource_whenTypeParameterOmitted() throws IOException {

			Patient patient = new Patient();
			patient.setId("Pat-1");
			myClient.update().resource(patient).execute();

			Binary binary = new Binary();
			binary.setId("Bin-1");
			myClient.update().resource(binary).execute();

			HttpGet httpGet = new HttpGet(myClient.getServerBase() + "/$export");
			httpGet.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
			String pollingLocation;
			try (CloseableHttpResponse status = ourHttpClient.execute(httpGet)) {
				Header[] headers = status.getHeaders("Content-Location");
				pollingLocation = headers[0].getValue();
			}
			String jobId = Batch2JobHelper.getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				String responseContent = IOUtils.toString(status.getEntity().getContent(), UTF_8);
				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertEquals(expectedOriginalUrl, result.getRequest());
				assertEquals(true, result.getRequiresAccessToken());
				assertNotNull(result.getTransactionTime());
				assertEquals(1, result.getOutput().size());
				assertThat(result.getOutput().stream().filter(o -> o.getType().equals("Patient")).collect(Collectors.toList())).hasSize(1);
				assertThat(result.getOutput().stream().filter(o -> o.getType().equals("Binary")).collect(Collectors.toList())).isEmpty();

				//We assert specifically on content as the deserialized version will "helpfully" fill in missing fields.
				assertThat(responseContent).contains("\"error\" : [ ]");
			}
		}

	}

	private String submitBulkExportForTypes(String... theTypes) throws IOException {
		return submitBulkExportForTypesWithExportId(null, theTypes);
	}

	private String submitBulkExportForTypesWithExportId(String theExportId, String... theTypes) throws IOException {
		String typeString = String.join(",", theTypes);
		String uri = myClient.getServerBase() + "/$export?_type=" + typeString;
		if (!StringUtils.isBlank(theExportId)) {
			uri += "&_exportId=" + theExportId;
		}

		HttpGet httpGet = new HttpGet(uri);
		httpGet.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		String pollingLocation;
		try (CloseableHttpResponse status = ourHttpClient.execute(httpGet)) {
			Header[] headers = status.getHeaders("Content-Location");
			pollingLocation = headers[0].getValue();
		}
		return pollingLocation;
	}

	@Nested
	class SystemBulkExportTests {

		@Test
		void testBinariesAreStreamedWithRespectToAcceptHeader() throws IOException {
			int patientCount = 5;
			for (int i = 0; i < patientCount; i++) {
				Patient patient = new Patient();
				patient.setId("pat-" + i);
				myPatientDao.update(patient, mySrd);
			}

			HashSet<String> types = Sets.newHashSet("Patient");
			BulkExportJobResults bulkExportJobResults = startSystemBulkExportJobAndAwaitCompletion(types, new HashSet<>());
			Map<String, List<String>> resourceTypeToBinaryIds = bulkExportJobResults.getResourceTypeToBinaryIds();
			assertThat(resourceTypeToBinaryIds.get("Patient")).hasSize(1);
			String patientBinaryId = resourceTypeToBinaryIds.get("Patient").get(0);
			String replace = patientBinaryId.replace("_history/1", "");

			{ // Test with the Accept Header omitted should stream out the results.
				HttpGet expandGet = new HttpGet(myServerBase + "/" + replace);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test with the Accept Header set to application/fhir+ndjson should stream out the results.
				HttpGet expandGet = new HttpGet(myServerBase + "/" + replace);
				expandGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_NDJSON);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test that demanding octet-stream will force it to whatever the Binary's content-type is set to.
				HttpGet expandGet = new HttpGet(myServerBase + "/" + replace);
				expandGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_OCTET_STREAM);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test with the Accept Header set to application/fhir+json should simply return the Binary resource.
				HttpGet expandGet = new HttpGet(myServerBase + "/" + replace);
				expandGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON);

				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), UTF_8);
					logContentTypeAndResponse(headers, response);

					assertThat(headers[0].getValue()).contains(Constants.CT_FHIR_JSON);
					assertThat(response).doesNotContain("\n");
					Binary binary = myFhirContext.newJsonParser().parseResource(Binary.class, response);
					assertEquals(patientBinaryId, binary.getIdElement().getValue());
				}
			}
		}

		@Test
		void testResourceCountIsCorrect() {
			int patientCount = 5;
			for (int i = 0; i < patientCount; i++) {
				Patient patient = new Patient();
				patient.setId("pat-" + i);
				myPatientDao.update(patient, mySrd);
			}

			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Collections.singleton("Patient"));
			options.setFilters(Collections.emptySet());
			options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			myCaptureQueriesListener.clear();

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			startRequest.setParameters(options);
			Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);

			assertNotNull(startResponse);

			final String jobId = startResponse.getInstanceId();

			// Run a scheduled pass to build the export
			myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId());

			String queries = myCaptureQueriesListener
				.getUpdateQueries()
				.stream()
				.filter(t -> t.getSql(false, false).toUpperCase().contains(" BT2_JOB_INSTANCE "))
				.map(t -> new InstantType(new Date(t.getQueryTimestamp())) + " - " + t.getSql(true, false))
				.collect(Collectors.joining("\n * "));
			ourLog.info("Update queries:\n * " + queries);

			runInTransaction(() -> {
				String entities = myJobInstanceRepository
					.findAll()
					.stream()
					.map(Batch2JobInstanceEntity::toString)
					.collect(Collectors.joining("\n * "));
				ourLog.info("Entities:\n * " + entities);
			});

			final Optional<JobInstance> optJobInstance = myJobPersistence.fetchInstance(jobId);

			assertNotNull(optJobInstance);
			assertThat(optJobInstance).isPresent();

			final JobInstance jobInstance = optJobInstance.get();

			assertEquals(patientCount, jobInstance.getCombinedRecordsProcessed());
		}

		@Test
		void testEmptyExport() {
			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Collections.singleton("Patient"));
			options.setFilters(Collections.emptySet());
			options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			startRequest.setParameters(options);
			Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);

			assertNotNull(startResponse);

			final String jobId = startResponse.getInstanceId();

			// Run a scheduled pass to build the export
			myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId());
			runInTransaction(() -> {
				String entities = myJobInstanceRepository
					.findAll()
					.stream()
					.map(Batch2JobInstanceEntity::toString)
					.collect(Collectors.joining("\n * "));
				ourLog.info("Entities:\n * " + entities);
			});

			final Optional<JobInstance> optJobInstance = myJobPersistence.fetchInstance(jobId);
			assertNotNull(optJobInstance);
			assertTrue(optJobInstance.isPresent());
			assertThat(optJobInstance.get().getReport()).
				contains("Export complete, but no data to generate report for job instance:");
		}

		private void logContentTypeAndResponse(Header[] headers, String response) {
			ourLog.info("**************************");
			ourLog.info("Content-Type is: {}", headers[0]);
			ourLog.info("Response is: {}", response);
			ourLog.info("**************************");
		}

		private void validateNdJsonResponse(Header[] headers, String response, int theExpectedCount) {
			assertThat(headers[0].getValue()).contains(Constants.CT_FHIR_NDJSON);
			assertThat(response).contains("\n");
			Bundle bundle = myFhirContext.newNDJsonParser().parseResource(Bundle.class, response);
			assertThat(bundle.getEntry()).hasSize(theExpectedCount);
		}
	}


	@Nested
	class PatientBulkExportTests {

		@BeforeEach
		void before() {
			myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		}

		@AfterEach
		void after() {
			myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
			myStorageSettings.setBulkExportFileMaximumCapacity(JpaStorageSettings.DEFAULT_BULK_EXPORT_FILE_MAXIMUM_CAPACITY);
		}

		// TODO reenable 4637
		// Reenable when bulk exports that return no results work as expected
		@Disabled
		@Test
		void testPatientExportIgnoresResourcesNotInPatientCompartment() {
			Patient patient = new Patient();
			patient.setId("pat-1");
			myPatientDao.update(patient, mySrd);
			Observation obs = new Observation();

			obs.setId("obs-included");
			obs.setSubject(new Reference("Patient/pat-1"));
			myObservationDao.update(obs, mySrd);

			Observation obs2 = new Observation();
			obs2.setId("obs-excluded");
			myObservationDao.update(obs2, mySrd);

			// test
			HashSet<String> types = Sets.newHashSet("Patient", "Observation");
			BulkExportJobResults bulkExportJobResults = startPatientBulkExportJobAndAwaitResults(types, new HashSet<>(), "ha");
			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertEquals(1, contents.countResources("Patient"));
			assertEquals(1, contents.countResources("Observation"));

			assertThat(contents.getResourceIdPartsForType("Observation")).contains("obs-included");
			assertThat(contents.getResourceIdPartsForType("Observation")).doesNotContain("obs-excluded");
		}

		@Test
		void testBulkExportWithLowMaxFileCapacity() {
			final int numPatients = 250;
			myStorageSettings.setBulkExportFileMaximumCapacity(1);
			myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

			RequestDetails details = new SystemRequestDetails();
			List<String> patientIds = new ArrayList<>();
			for (int i = 0; i < numPatients; i++) {
				String id = "p-" + i;
				Patient patient = new Patient();
				patient.setId(id);
				myPatientDao.update(patient, details);
				patientIds.add(id);
			}

			Integer patientsCreated = myPatientDao.search(SearchParameterMap.newSynchronous(), details).size();
			assertEquals(numPatients, patientsCreated);

			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Sets.newHashSet("Patient"));
			options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			startRequest.setParameters(options);
			Batch2JobStartResponse job = myJobCoordinator.startInstance(mySrd, startRequest);
			myBatch2JobHelper.awaitJobCompletion(job.getInstanceId(), 60);
			ourLog.debug("Job status after awaiting - {}", myJobCoordinator.getInstance(job.getInstanceId()).getStatus());
			await()
				.atMost(300, TimeUnit.SECONDS)
				.until(() -> {
					StatusEnum status = myJobCoordinator.getInstance(job.getInstanceId()).getStatus();
					if (!StatusEnum.COMPLETED.equals(status)) {
						fail("Job status was changed from COMPLETE to " + status);
					}
					return myJobCoordinator.getInstance(job.getInstanceId()).getReport() != null;
				});

			String report = myJobCoordinator.getInstance(job.getInstanceId()).getReport();
			BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
			List<String> binaryUrls = results.getResourceTypeToBinaryIds().get("Patient");

			IParser jsonParser = myFhirContext.newJsonParser();
			for (String url : binaryUrls) {
				Binary binary = myClient.read().resource(Binary.class).withUrl(url).execute();
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
				String resourceContents = new String(binary.getContent(), Constants.CHARSET_UTF8);
				String resourceId = jsonParser.parseResource(resourceContents).getIdElement().getIdPart();
				assertThat(patientIds).contains(resourceId);
			}
		}

		@Test
		void testExportEmptyResult() {
			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Sets.newHashSet("Patient"));
			options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			startRequest.setParameters(options);
			Batch2JobStartResponse job = myJobCoordinator.startInstance(mySrd, startRequest);
			myBatch2JobHelper.awaitJobCompletion(job.getInstanceId(), 60);
			ourLog.debug("Job status after awaiting - {}", myJobCoordinator.getInstance(job.getInstanceId()).getStatus());
			await()
				.atMost(300, TimeUnit.SECONDS)
				.until(() -> {
					StatusEnum status = myJobCoordinator.getInstance(job.getInstanceId()).getStatus();
					if (!StatusEnum.COMPLETED.equals(status)) {
						fail("Job status was changed from COMPLETE to " + status);
					}
					return myJobCoordinator.getInstance(job.getInstanceId()).getReport() != null;
				});

			String report = myJobCoordinator.getInstance(job.getInstanceId()).getReport();
			assertThat(report).
				contains("Export complete, but no data to generate report for job instance:");
		}
	}


	@Nested
	class GroupBulkExportTests {

		@AfterEach
		void tearDown() {
			restoreMdmSettingsToDefault();
		}

		@Test
		void testGroupExportSuccessfulyExportsPatientForwardReferences() {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Group group = new Group();
			group.setId("Group/G");
			group.setActive(true);
			bb.addTransactionUpdateEntry(group);

			Practitioner pract = new Practitioner();
			pract.setId("PRACT-IN-GROUP");
			bb.addTransactionUpdateEntry(pract);

			Organization organization = new Organization();
			organization.setId("ORG-IN-GROUP");
			bb.addTransactionUpdateEntry(organization);

			Patient patient = new Patient();
			patient.setId("PAT-IN-GROUP");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			patient.setManagingOrganization(new Reference("Organization/ORG-IN-GROUP"));
			patient.setGeneralPractitioner(List.of(new Reference("Practitioner/PRACT-IN-GROUP")));
			bb.addTransactionUpdateEntry(patient);

			group.addMember().getEntity().setReference("Patient/PAT-IN-GROUP");

			myClient.transaction().withBundle(bb.getBundle()).execute();

			HashSet<String> resourceTypes = Sets.newHashSet();
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G");
			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);

			assertThat(contents.getResourceTypes()).hasSize(4);
			assertThat(contents.getResourceIdPartsForType("Group")).hasSize(1);
			assertThat(contents.getResourceIdPartsForType("Patient")).hasSize(1);
			assertThat(contents.getResourceIdPartsForType("Practitioner")).hasSize(1);
			assertThat(contents.getResourceIdPartsForType("Organization")).hasSize(1);
		}

		@Test
		void testVeryLargeGroup() {

			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Group group = new Group();
			group.setId("Group/G");
			group.setActive(true);
			bb.addTransactionUpdateEntry(group);

			for (int i = 0; i < 600; i++) {
				Patient patient = new Patient();
				patient.setId("PING-" + i);
				patient.setGender(Enumerations.AdministrativeGender.FEMALE);
				patient.setActive(true);
				bb.addTransactionUpdateEntry(patient);
				group.addMember().getEntity().setReference("Patient/PING-" + i);

				Observation obs = new Observation();
				obs.setId("obs-" + i);
				obs.setSubject(new Reference("Patient/PING-" + i));
				bb.addTransactionUpdateEntry(obs);
			}

			myClient.transaction().withBundle(bb.getBundle()).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Group", "Patient", "Observation");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G");
			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(contents.getResourceTypes()).hasSize(3);
			assertThat(contents.getResourceIdPartsForType("Group")).hasSize(1);
			assertThat(contents.getResourceIdPartsForType("Patient")).hasSize(600);
			assertThat(contents.getResourceIdPartsForType("Observation")).hasSize(600);
		}

		@Test
		void testGroupBulkExportMembershipShouldNotExpandIntoOtherGroups() {
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			Group group = new Group();
			group.setId("Group/G1");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();

			patient = new Patient();
			patient.setId("PING2");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			group.addMember().getEntity().setReference("Patient/PING2");
			myClient.update().resource(group).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Group", "Patient");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G1");
			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(contents.getResourceIdPartsForType("Patient")).hasSize(1);
			assertThat(contents.getResourceIdPartsForType("Group")).hasSize(1);
		}

		@Test
		void testDifferentTypesDoNotUseCachedResults() {
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();

			Observation o = new Observation();
			o.setSubject(new Reference("Patient/PING1"));
			o.setId("obs-included");
			myClient.update().resource(o).execute();

			Coverage c = new Coverage();
			c.setBeneficiary(new Reference("Patient/PING1"));
			c.setId("cov-included");
			myClient.update().resource(c).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Observation", "Patient");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G2");
			BulkExportJobHelper.BulkExportContents firstMap = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(firstMap.getResourceIdPartsForType("Patient")).hasSize(1);
			assertThat(firstMap.getResourceIdPartsForType("Observation")).hasSize(1);

			HashSet<String> otherResourceTypes = Sets.newHashSet("Coverage", "Patient");
			BulkExportJobResults altBulkExportResults = startGroupBulkExportJobAndAwaitCompletion(otherResourceTypes, new HashSet<>(), "G2");
			BulkExportJobHelper.BulkExportContents secondMap = myBulkExportJobHelper.fetchJobResults(altBulkExportResults);
			assertThat(secondMap.getResourceIdPartsForType("Patient")).hasSize(1);
			assertThat(secondMap.getResourceIdPartsForType("Coverage")).hasSize(1);

			runInTransaction(() -> {
				List<Batch2JobInstanceEntity> instances = myJobInstanceRepository.findAll();
				ourLog.info("Job instance states:\n * {}", instances.stream().map(Object::toString).collect(Collectors.joining("\n * ")));
				List<Batch2WorkChunkEntity> workChunks = myWorkChunkRepository.findAll();
				ourLog.info("Work chunks instance states:\n * {}", workChunks.stream().map(Object::toString).collect(Collectors.joining("\n * ")));
			});
		}


		@Test
		void testGroupBulkExportNotInGroup_DoeNotShowUp() {
			// Create some resources
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			patient = new Patient();
			patient.setId("PING2");
			patient.setGender(Enumerations.AdministrativeGender.MALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			patient = new Patient();
			patient.setId("PNING3");
			patient.setGender(Enumerations.AdministrativeGender.MALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			group.addMember().getEntity().setReference("Patient/PING2");
			myClient.update().resource(group).execute();

			verifyBulkExportResults("G2", new HashSet<>(), List.of("\"PING1\"", "\"PING2\""), Collections.singletonList("\"PNING3\""));
		}

		@Test
		void testTwoConsecutiveBulkExports() {

			// Create some resources
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();
			myCaptureQueriesListener.clear();
			verifyBulkExportResults("G2", new HashSet<>(), List.of("\"PING1\""), Collections.singletonList("\"PNING3\""));
			myCaptureQueriesListener.logSelectQueries();
			ourLog.error("************");
			myCaptureQueriesListener.clear();
			try {
				verifyBulkExportResults("G2", new HashSet<>(), List.of("\"PING1\""), Collections.singletonList("\"PNING3\""));
			} finally {
				myCaptureQueriesListener.logSelectQueries();

			}
		}

		@Test
		void testGroupExport_includesObservationsAndEncountersOfPatientsInExportedGroup_whenLuceneIdexingEnabled() {

			// Enable Lucene indexing
			myStorageSettings.setAllowContainsSearches(true);
			myStorageSettings.setHibernateSearchIndexSearchParams(true);
			mySearchParamRegistry.forceRefresh();

			Patient patient = new Patient();
			patient.setId("A1");
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			Patient patient2 = new Patient();
			patient2.setId("A2");
			patient2.setActive(true);
			myClient.update().resource(patient2).execute();

			Patient patient3 = new Patient();
			patient3.setId("A3");
			patient3.setActive(true);
			myClient.update().resource(patient3).execute();

			Group group1 = new Group();
			group1.setActual(true);
			group1.setType(Group.GroupType.PERSON);
			group1.setId("G1");
			group1.setActive(true);
			group1.addMember().getEntity().setReference("Patient/A1");
			group1.addMember().getEntity().setReference("Patient/A2");
			myClient.update().resource(group1).execute();

			Group group2 = new Group();
			group2.setActual(true);
			group2.setType(Group.GroupType.PERSON);
			group2.setId("G2");
			group2.setActive(true);
			group2.addMember().getEntity().setReference("Patient/A1");
			group2.addMember().getEntity().setReference("Patient/A3");
			myClient.update().resource(group2).execute();

			Observation o = new Observation();
			o.setSubject(new Reference("Patient/A1"));
			o.setId("obs-a1");
			myClient.update().resource(o).execute();

			Observation o2 = new Observation();
			o2.setSubject(new Reference("Patient/A2"));
			o2.setId("obs-a2");
			myClient.update().resource(o2).execute();

			Encounter e = new Encounter();
			e.setSubject(new Reference("Patient/A1"));
			e.setId("enc-a1");
			myClient.update().resource(e).execute();

			Encounter e2 = new Encounter();
			e2.setSubject(new Reference("Patient/A2"));
			e2.setId("enc-a2");
			myClient.update().resource(e2).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Observation", "Patient", "Encounter", "Group");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G1");

			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(contents.getResourceIdPartsForType("Group")).hasSize(1);

			assertThat(contents.getResourceIdPartsForType("Patient"))
				.containsExactlyInAnyOrder("A1", "A2");
			assertThat(contents.getResourceIdPartsForType("Observation"))
				.containsExactlyInAnyOrder("obs-a1", "obs-a2");
			assertThat(contents.getResourceIdPartsForType("Encounter"))
				.containsExactlyInAnyOrder("enc-a1", "enc-a2");

		}


		@Test
		void testGroupExportPatientAndOtherResources() {
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			//Other patient not in group
			Patient patient2 = new Patient();
			patient2.setId("POG2");
			patient2.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient2.setActive(true);
			myClient.update().resource(patient2).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();

			Observation o = new Observation();
			o.setSubject(new Reference("Patient/PING1"));
			o.setId("obs-included");
			myClient.update().resource(o).execute();

			Observation o2 = new Observation();
			o2.setSubject(new Reference("Patient/POG2"));
			o2.setId("obs-excluded");
			myClient.update().resource(o2).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Observation", "Patient");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G2");

			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(contents.getResourceIdPartsForType("Patient")).containsExactlyInAnyOrder("PING1");
			assertThat(contents.getResourceIdPartsForType("Observation")).containsExactlyInAnyOrder("obs-included");
		}

		@Test
		void testGroupExportPatientAndOtherResources_withHistory() {
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			//Other patient not in group
			Patient patient2 = new Patient();
			patient2.setId("POG2");
			patient2.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient2.setActive(true);
			myClient.update().resource(patient2).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();

			Observation o = new Observation();
			o.setSubject(new Reference("Patient/PING1"));
			o.setId("obs-included");
			myClient.update().resource(o).execute();

			Observation o2 = new Observation();
			o2.setSubject(new Reference("Patient/POG2"));
			o2.setId("obs-excluded");
			myClient.update().resource(o2).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Observation", "Patient");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobWithHistoryAndAwaitCompletion(resourceTypes, new HashSet<>(), "G2");

			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(contents.getResourceIdPartsForType("Patient")).containsExactlyInAnyOrder("PING1");
			assertThat(contents.getResourceIdPartsForType("Observation")).containsExactlyInAnyOrder("obs-included");
		}

		@Test
		void testGroupBulkExportWithTypeFilter_ReturnsOnlyResourcesInTypeFilter() {
			// setup
			IParser parser = myFhirContext.newJsonParser();
			{
				String patientStr = """
						{
					   "resourceType": "Patient",
					   "id": "f201"
					 }
					""";
				Patient patient = parser.parseResource(Patient.class, patientStr);
				myClient.update().resource(patient).execute();
			}
			{
				String practitionerStr = """
						{
						  "resourceType": "Practitioner",
						  "id": "f201"
						}
					""";
				Practitioner practitioner = parser.parseResource(Practitioner.class, practitionerStr);
				myClient.update().resource(practitioner).execute();
			}
			{
				String orgString = """
						{
						  "resourceType": "Organization",
						  "id": "f201"
						}
					""";
				Organization organization = parser.parseResource(Organization.class, orgString);
				myClient.update().resource(organization).execute();
			}
			{
				String bundleStr = """
						{
						"resourceType": "Bundle",
						"id": "bundle-transaction",
						"meta": {
						  "lastUpdated": "2021-04-19T20:24:48.194+00:00"
						},
						"type": "transaction",
						"entry": [
						  {
							"fullUrl": "http://example.org/fhir/Encounter/E1",
							"resource": {
							  "resourceType": "Encounter",
							  "id": "E1",
							  "subject": {
								"reference": "Patient/f201",
								"display": "Roel"
							  },
							  "participant": [
								{
								  "individual": {
									"reference": "Practitioner/f201"
								  }
								}
							  ],
							  "serviceProvider": {
								"reference": "Organization/f201"
							  }
							},
							"request": {
							  "method": "PUT",
							  "url": "Encounter/E1"
							}
						  },
						  {
							"fullUrl": "http://example.org/fhir/Encounter/E2",
							"resource": {
							  "resourceType": "Encounter",
							  "id": "E2",
							  "subject": {
								"reference": "Patient/f201",
								"display": "Roel"
							  },
							  "participant": [
								{
								  "individual": {
									"reference": "Practitioner/f201"
								  }
								}
							  ],
							  "serviceProvider": {
								"reference": "Organization/f201"
							  }
							},
							"request": {
							  "method": "PUT",
							  "url": "Encounter/A2"
							}
						  },
						  {
							"fullUrl": "http://example.org/fhir/Group/G3",
							"resource": {
							  "resourceType": "Group",
							  "id": "G3",
							  "text": {
								"status": "additional"
							  },
							  "type": "person",
							  "actual": true,
							  "member": [
								{
								  "entity": {
									"reference": "Patient/f201"
								  },
								  "period": {
									"start": "2021-01-01"
								  }
								},
								{
								  "entity": {
									"reference": "Patient/f201"
								  },
								  "period": {
									"start": "2021-01-01"
								  }
								}
							  ]
							},
							"request": {
							  "method": "PUT",
							  "url": "Group/G3"
							}
						  }
						]
					  }
					""";
				Bundle bundle = parser.parseResource(Bundle.class, bundleStr);
				myClient.transaction().withBundle(bundle).execute();
			}

			// test
			HashSet<String> resourceTypes = Sets.newHashSet("Encounter");
			BulkExportJobResults results = startGroupBulkExportJobAndAwaitCompletion(
				resourceTypes,
				new HashSet<>(),
				"G3" // ID from Transaction Bundle
			);

			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(results);
			assertThat(contents.getResourceIdPartsForType("Organization")).isEmpty();
			assertThat(contents.getResourceIdPartsForType("Patient")).isEmpty();
			assertThat(contents.getResourceIdPartsForType("Encounter")).hasSize(2);
		}

		@Test
		void testGroupBulkExportWithTypeFilter() {
			// Create some resources
			Group g = createGroupWithPatients();
			String groupId = g.getIdPart();

			//Create an observation for each patient
			Observation femaleObs = new Observation();
			femaleObs.setSubject(new Reference("Patient/PF"));
			femaleObs.setId("obs-female");
			myClient.update().resource(femaleObs).execute();

			Observation maleObs = new Observation();
			maleObs.setSubject(new Reference("Patient/PM"));
			maleObs.setId("obs-male");
			myClient.update().resource(maleObs).execute();

			// test
			HashSet<String> resourceTypes = Sets.newHashSet("Observation", "Patient");
			HashSet<String> filters = Sets.newHashSet("Patient?gender=female");
			BulkExportJobResults results = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, filters, groupId);

			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(results);
			assertThat(contents.getResourceIdPartsForType("Patient")).containsExactlyInAnyOrder("PF");
			assertThat(contents.getResourceIdPartsForType("Observation")).containsExactlyInAnyOrder("obs-female");
		}

		@Test
		void testGroupExportOmitResourceTypesFetchesAll() {
			// Create some resources
			Patient patient = new Patient();
			patient.setId("PF");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			patient = new Patient();
			patient.setId("PM");
			patient.setGender(Enumerations.AdministrativeGender.MALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			Group group = new Group();
			group.setId("Group/G");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PF");
			group.addMember().getEntity().setReference("Patient/PM");
			myClient.update().resource(group).execute();

			//Create an observation for each patient
			Observation femaleObs = new Observation();
			femaleObs.setSubject(new Reference("Patient/PF"));
			femaleObs.setId("obs-female");
			myClient.update().resource(femaleObs).execute();

			Observation maleObs = new Observation();
			maleObs.setSubject(new Reference("Patient/PM"));
			maleObs.setId("obs-male");
			myClient.update().resource(maleObs).execute();

			Coverage coverage = new Coverage();
			coverage.setBeneficiary(new Reference("Patient/PM"));
			coverage.setId("coverage-male");
			myClient.update().resource(coverage).execute();

			coverage = new Coverage();
			coverage.setBeneficiary(new Reference("Patient/PF"));
			coverage.setId("coverage-female");
			myClient.update().resource(coverage).execute();

			// test
			HashSet<String> filters = Sets.newHashSet();
			BulkExportJobResults results = startGroupBulkExportJobAndAwaitCompletion(new HashSet<>(), filters, "G");
			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(results);
			assertThat(contents.getResourceTypes()).hasSize(4);
			assertThat(contents.getResourceIdPartsForType("Group")).hasSize(1);
			assertThat(contents.getResourceIdPartsForType("Observation")).hasSize(2);
			assertThat(contents.getResourceIdPartsForType("Coverage")).hasSize(2);
			assertThat(contents.getResourceIdPartsForType("Patient")).hasSize(2);
		}

		@Test
		void testGroupExportPatientOnly() {
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			//Other patient not in group
			Patient patient2 = new Patient();
			patient2.setId("POG2");
			patient2.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient2.setActive(true);
			myClient.update().resource(patient2).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Patient");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G2");

			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(contents.getResourceIdPartsForType("Patient")).containsExactlyInAnyOrder("PING1");
		}

		@Test
		void testExportEmptyResult() {
			Group group = new Group();
			group.setId("Group/G-empty");
			group.setActive(true);
			myClient.update().resource(group).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Patient");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(
				resourceTypes, new HashSet<>(), "G-empty");

			assertThat(bulkExportJobResults.getReportMsg()).
				startsWith("Export complete, but no data to generate report for job instance:");
		}

		@Test
		void testGroupBulkExportMultipleResourceTypes() {
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			//Other patient not in group
			Patient patient2 = new Patient();
			patient2.setId("POG2");
			patient2.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient2.setActive(true);
			myClient.update().resource(patient2).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();

			Observation o = new Observation();
			o.setSubject(new Reference("Patient/PING1"));
			o.setId("obs-included");
			myClient.update().resource(o).execute();

			Coverage coverage = new Coverage();
			coverage.setBeneficiary(new Reference("Patient/PING1"));
			coverage.setId("coverage-included");
			myClient.update().resource(coverage).execute();

			HashSet<String> resourceTypes = Sets.newHashSet("Observation", "Coverage");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G2");

			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(contents.getResourceIdPartsForType("Observation"))
				.containsExactlyInAnyOrder("obs-included");
			assertThat(contents.getResourceIdPartsForType("Coverage"))
				.containsExactlyInAnyOrder("coverage-included");
		}

		@Test
		void testGroupBulkExportOverLargeDataset() {
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			//Other patient not in group
			Patient patient2 = new Patient();
			patient2.setId("POG2");
			patient2.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient2.setActive(true);
			myClient.update().resource(patient2).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();

			for (int i = 0; i < 1000; i++) {
				Observation o = new Observation();
				o.setSubject(new Reference("Patient/PING1"));
				o.setId("obs-included-" + i);
				myClient.update().resource(o).execute();
			}
			for (int i = 0; i < 100; i++) {
				Observation o = new Observation();
				o.setSubject(new Reference("Patient/POG2"));
				o.setId("obs-not-included-" + i);
				myClient.update().resource(o).execute();
			}

			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(Sets.newHashSet("Observation"), new HashSet<>(), "G2");

			BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
			assertThat(contents.getResourceIdPartsForType("Observation")).hasSize(1000);

			assertThat(contents.getResourceIdPartsForType("Observation")).doesNotContain("not-included");
			assertThat(contents.getResourceIdPartsForType("Observation")).contains("obs-included-0");
			assertThat(contents.getResourceIdPartsForType("Observation")).contains("obs-included-999");
		}

		@Nested
		class WithClientIdStrategyEnumANYTest {

			@BeforeEach
			void setUp() {
				myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
			}

			@AfterEach
			void tearDown() {
				myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);
			}

			@Test
			void testGroupExportPatientOnly() {
				Patient patient = new Patient();
				patient.setId("PING1");
				patient.setGender(Enumerations.AdministrativeGender.FEMALE);
				patient.setActive(true);
				myClient.update().resource(patient).execute();

				//Other patient not in group
				Patient patient2 = new Patient();
				patient2.setId("POG2");
				patient2.setGender(Enumerations.AdministrativeGender.FEMALE);
				patient2.setActive(true);
				myClient.update().resource(patient2).execute();

				Group group = new Group();
				group.setId("Group/G2");
				group.setActive(true);
				group.addMember().getEntity().setReference("Patient/PING1");
				myClient.update().resource(group).execute();

				HashSet<String> resourceTypes = Sets.newHashSet("Patient");
				BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, new HashSet<>(), "G2");

				BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);
				assertThat(contents.getResourceIdPartsForType("Patient")).containsExactlyInAnyOrder("PING1");
			}
		}
	}

	@Nested
	class IncludeHistoryTests {

		@Test
		void testPatientBulkExport() {
			myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

			// versions list size indicate number of patients to create
			Map<String, Set<String>> patientVersionsMap = createPatientsWithHistory(List.of(2, 3, 5, 2, 1, 4, 2, 3, 2, 3));

			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Sets.newHashSet("Patient"));
			options.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);
			options.setIncludeHistory(true);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			startRequest.setParameters(options);
			Batch2JobStartResponse job = myJobCoordinator.startInstance(mySrd, startRequest);
			myBatch2JobHelper.awaitJobCompletion(job.getInstanceId(), 60);
			ourLog.debug("Job status after awaiting - {}", myJobCoordinator.getInstance(job.getInstanceId()).getStatus());
			waitForCompletion(job);

			Map<String, Set<String>> exportedPatientVersionsMap = extractExportedResourceVersionsByTypeMap(job).get("Patient");
			assertThat(exportedPatientVersionsMap).isEqualTo(patientVersionsMap);
		}

		@Test
		void testGroupBulkExportMultipleResourceTypes() {
			Patient patient = new Patient();
			patient.setId("PING1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.setActive(true);
			myClient.update().resource(patient).execute();

			//Other patient not in group
			Patient patient2 = new Patient();
			patient2.setId("PONG2");
			patient2.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient2.setActive(true);
			myClient.update().resource(patient2).execute();

			Group group = new Group();
			group.setId("Group/G2");
			group.setActive(true);
			group.addMember().getEntity().setReference("Patient/PING1");
			myClient.update().resource(group).execute();

			// versions list size indicate number of resources to create
			Map<String, Set<String>> observationVersionsMap = createObservationWithHistory(3, "Patient/PING1");
			// observations which versions should not be included (to validate history id filtering)
			createObservationWithHistory(3, "Patient/PONG2");

			Map<String, Set<String>> coverageVersionsMap = createCoverageWithHistory(2, "Patient/PING1");
			// coverages which versions should not be included (to validate history id filtering)
			createCoverageWithHistory(2, "Patient/PONG2");

			HashSet<String> resourceTypes = Sets.newHashSet("Observation", "Coverage");
			BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletionForHistory(resourceTypes, new HashSet<>(), "G2", true);

			Map<String, Map<String, Set<String>>> typeToResourceVersionsMap = myBulkExportJobHelper.convertJobResultsToResourceVersionMap(bulkExportJobResults);
			assertThat(typeToResourceVersionsMap).containsEntry("Observation", observationVersionsMap)
				.containsEntry("Coverage", coverageVersionsMap);
		}

		@Test
		void testSystemBulkExport() {
			// versions list size indicate number of patients to create
			Map<String, Set<String>> patientVersionsMap = createPatientsWithHistory(List.of(3, 5, 2, 1, 2));

			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Collections.singleton("Patient"));
			options.setFilters(Collections.emptySet());
			options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
			options.setIncludeHistory(true);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			startRequest.setParameters(options);
			Batch2JobStartResponse job = myJobCoordinator.startInstance(mySrd, startRequest);
			myBatch2JobHelper.awaitJobCompletion(job.getInstanceId(), 60);
			ourLog.debug("Job status after awaiting - {}", myJobCoordinator.getInstance(job.getInstanceId()).getStatus());
			waitForCompletion(job);

			Map<String, Set<String>> exportedPatientVersionsMap = extractExportedResourceVersionsByTypeMap(job).get("Patient");
			assertThat(exportedPatientVersionsMap).isEqualTo(patientVersionsMap);
		}

		@Test
		void testSystemBulkExport_withResourcesExceedingPageSizes() {
			// given

			int exportFileMaxCapacity = 27;
			int initialExportFileMaxCapacity = myStorageSettings.getBulkExportFileMaximumCapacity();
			myStorageSettings.setBulkExportFileMaximumCapacity(exportFileMaxCapacity);

			try {
				int patientCount = 100;
				int versionCount = 10;
				Map<String, Set<String>> patientVersionsMap = createPatientsWithHistory(patientCount, versionCount);
				// validate test data
				assertThat(patientVersionsMap).hasSize(patientCount);
				int totalVersions = patientVersionsMap.values().stream().mapToInt(Set::size).sum();
				assertThat(totalVersions).isEqualTo(patientCount * versionCount);

				BulkExportJobParameters options = new BulkExportJobParameters();
				options.setResourceTypes(Collections.singleton("Patient"));
				options.setFilters(Collections.emptySet());
				options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
				options.setIncludeHistory(true);
				options.setOutputFormat(Constants.CT_FHIR_NDJSON);

				// when

				JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
				startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
				startRequest.setParameters(options);
				Batch2JobStartResponse job = myJobCoordinator.startInstance(mySrd, startRequest);
				myBatch2JobHelper.awaitJobCompletion(job.getInstanceId(), 60);
				ourLog.debug("Job status after awaiting - {}", myJobCoordinator.getInstance(job.getInstanceId()).getStatus());
				waitForCompletion(job);

				// then

				Map<String, Set<String>> exportedPatientVersionsMap = extractExportedResourceVersionsByTypeMap(job).get("Patient");
				assertThat(exportedPatientVersionsMap).isEqualTo(patientVersionsMap);

				String report = myJobCoordinator.getInstance(job.getInstanceId()).getReport();
				BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
				int expectedExportOutputFiles = (int) Math.ceil((double) patientCount * versionCount / exportFileMaxCapacity);
				assertThat(results.getResourceTypeToBinaryIds().get("Patient")).hasSize(expectedExportOutputFiles);

			} finally {
				myStorageSettings.setBulkExportFileMaximumCapacity(initialExportFileMaxCapacity);
			}
		}

		@Test
		void testShouldIncludeOnlyCurrentVersions() {
			// Given - Create a patient with multiple versions
			Patient patient = new Patient();
			patient.setId("Patient-NoHistory-Test");
			patient.addName().setFamily("InitialVersion");
			myClient.update().resource(patient).execute();

			// Update the patient to create version 2
			patient.getNameFirstRep().setFamily("FinalVersion");
			MethodOutcome aa = myClient.update().resource(patient).execute();
			String expectedExportedPatientVersionId = aa.getResource().getIdElement().getValueAsString();

			// When - Start bulk export without _includeHistory parameter (default behavior)
			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Collections.singleton("Patient"));
			options.setFilters(Collections.emptySet());
			options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			startRequest.setParameters(options);
			Batch2JobStartResponse job = myJobCoordinator.startInstance(mySrd, startRequest);
			myBatch2JobHelper.awaitJobCompletion(job.getInstanceId(), 60);
			ourLog.debug("Job status after awaiting - {}", myJobCoordinator.getInstance(job.getInstanceId()).getStatus());
			waitForCompletion(job);

			Map<String, Set<String>> exportedPatientVersionsMap = extractExportedResourceVersionsByTypeMap(job).get("Patient");

			// validate only current patient version was exported
			assertThat(exportedPatientVersionsMap.values()).containsOnly(Set.of(expectedExportedPatientVersionId));

		}

		@Test
		void test_includeHistoryParameter_defaultsToFalse() {
			// Given
			BulkExportJobParametersBuilder builder = new BulkExportJobParametersBuilder();

			// When
			BulkExportJobParameters parameters = builder.build();

			// Then
			assertFalse(parameters.isIncludeHistory());
		}

		@Test
		void testBulkExportJobParametersBuilder_includeHistoryParameter_setsToTrue() {
			// Given
			BulkExportJobParametersBuilder builder = new BulkExportJobParametersBuilder();
			BooleanType includeHistoryParam = new BooleanType(true);

			// When
			BulkExportJobParameters parameters = builder.includeHistory(includeHistoryParam).build();

			// Then
			assertTrue(parameters.isIncludeHistory());
		}

		@Test
		void testBulkExportJobParametersBuilder_includeHistoryParameter_setsToFalse() {
			// Given
			BulkExportJobParametersBuilder builder = new BulkExportJobParametersBuilder();
			BooleanType includeHistoryParam = new BooleanType(false);

			// When
			BulkExportJobParameters parameters = builder.includeHistory(includeHistoryParam).build();

			// Then
			assertFalse(parameters.isIncludeHistory());
		}

		@Test
		void testBulkExportJobParametersBuilder_includeHistoryParameter_handlesNullParameter() {
			// Given
			BulkExportJobParametersBuilder builder = new BulkExportJobParametersBuilder();

			// When
			BulkExportJobParameters parameters = builder.includeHistory(null).build();

			// Then
			assertFalse(parameters.isIncludeHistory());
		}

		@Test
		void testSystemBulkExportWithHistory_WithClientAssignedIds() {
			// Given - Create patients with client-assigned IDs (forced IDs) and multiple versions
			Patient patient1 = new Patient();
			patient1.setId("Patient/client-assigned-id-1");
			patient1.addName().setFamily("FamilyV1");
			myClient.update().resource(patient1).execute();

			// Create version 2
			patient1.getNameFirstRep().setFamily("FamilyV2");
			myClient.update().resource(patient1).execute();

			// Create version 3
			patient1.getNameFirstRep().setFamily("FamilyV3");
			myClient.update().resource(patient1).execute();

			Patient patient2 = new Patient();
			patient2.setId("Patient/client-assigned-id-2");
			patient2.addName().setFamily("SmithV1");
			myClient.update().resource(patient2).execute();

			// Create version 2
			patient2.getNameFirstRep().setFamily("SmithV2");
			myClient.update().resource(patient2).execute();

			// When - Start bulk export with history
			BulkExportJobParameters options = new BulkExportJobParameters();
			options.setResourceTypes(Collections.singleton("Patient"));
			options.setFilters(Collections.emptySet());
			options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
			options.setIncludeHistory(true);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			startRequest.setParameters(options);
			Batch2JobStartResponse job = myJobCoordinator.startInstance(mySrd, startRequest);
			myBatch2JobHelper.awaitJobCompletion(job.getInstanceId(), 60);
			ourLog.debug("Job status after awaiting - {}", myJobCoordinator.getInstance(job.getInstanceId()).getStatus());
			waitForCompletion(job);

			// Then - Verify all versions are exported for resources with forced IDs
			Map<String, Set<String>> exportedPatientVersionsMap = extractExportedResourceVersionsByTypeMap(job).get("Patient");

			assertThat(exportedPatientVersionsMap).containsKey("client-assigned-id-1");
			assertThat(exportedPatientVersionsMap.get("client-assigned-id-1")).hasSize(3);

			assertThat(exportedPatientVersionsMap).containsKey("client-assigned-id-2");
			assertThat(exportedPatientVersionsMap.get("client-assigned-id-2")).hasSize(2);
		}

	}



	private Map<String, Set<String>> createObservationWithHistory(
		@SuppressWarnings("SameParameterValue") int theVersionCount,
		@SuppressWarnings("SameParameterValue") String thePatientId) {

		Map<String, Set<String>> retVal = new HashMap<>();

		IIdType id = createObservation(List.of(
			withResourcePrimitiveAttribute("valueString", "version-1"),
			withReference("subject", thePatientId)));

		Set<String> versionIds = new HashSet<>();
		retVal.put(id.getValueAsString(), versionIds);

		// create indicated additional versions

		int additionalVersionCount = theVersionCount - 1;
		IIdType lastVersonId = myObservationDao.read(id, mySrd).getIdElement();
		versionIds.add(lastVersonId.getValueAsString());

		for (int vCount = 0; vCount < additionalVersionCount; vCount++) {
			Parameters patch = getObservationPatch(vCount);
			DaoMethodOutcome patchOutcome = myObservationDao.patch(lastVersonId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
			AssertionsForClassTypes.assertThat(patchOutcome).isNotNull();
			lastVersonId = patchOutcome.getId();
			versionIds.add(lastVersonId.getValueAsString());
		}
		return retVal;
	}

	private Map<String, Set<String>> createCoverageWithHistory(
		@SuppressWarnings("SameParameterValue") int theVersionCount,
		@SuppressWarnings("SameParameterValue") String thePatientId) {

		Map<String, Set<String>> retVal = new HashMap<>();

		IIdType id = createCoverage(
			withResourcePrimitiveAttribute("dependent", "version-1"),
			withReference("beneficiary", thePatientId));

		Set<String> versionIds = new HashSet<>();
		retVal.put(id.getValueAsString(), versionIds);

		// create indicated additional versions

		int additionalVersionCount = theVersionCount - 1;
		IIdType lastVersonId = myCoverageDao.read(id, mySrd).getIdElement();
		versionIds.add(lastVersonId.getValueAsString());

		for (int vCount = 0; vCount < additionalVersionCount; vCount++) {
			Parameters patch = getCoveragePatch(vCount);
			DaoMethodOutcome patchOutcome = myCoverageDao.patch(lastVersonId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
			AssertionsForClassTypes.assertThat(patchOutcome).isNotNull();
			lastVersonId = patchOutcome.getId();
			versionIds.add(lastVersonId.getValueAsString());
		}
		return retVal;
	}

	private Parameters getObservationPatch(int theVersion) {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Observation.value"));
		op.addPart().setName("value").setValue(new StringType("version-" + theVersion + 1));
		return patch;
	}

	private Parameters getCoveragePatch(int theVersion) {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Coverage.dependent"));
		op.addPart().setName("value").setValue(new StringType("version-" + theVersion + 1));
		return patch;
	}

	private void waitForCompletion(Batch2JobStartResponse job) {
		await()
			.atMost(300, TimeUnit.SECONDS)
			.until(() -> {
				StatusEnum status = myJobCoordinator.getInstance(job.getInstanceId()).getStatus();
				if (!StatusEnum.COMPLETED.equals(status)) {
					fail("Job status was changed from COMPLETE to " + status);
				}
				return myJobCoordinator.getInstance(job.getInstanceId()).getReport() != null;
			});
	}

		private Map<String, Map<String, Set<String>>> extractExportedResourceVersionsByTypeMap(Batch2JobStartResponse theJob) {
		String report = myJobCoordinator.getInstance(theJob.getInstanceId()).getReport();
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
		List<String> binaryUrls = results.getResourceTypeToBinaryIds().get("Patient");

			Map<String, Map<String, Set<String>>> retVal = new HashMap<>();

		IParser jsonParser = myFhirContext.newNDJsonParser();
		for (String url : binaryUrls) {
			Binary binary = myClient.read().resource(Binary.class).withUrl(url).execute();
			assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
			String resourceContents = new String(binary.getContent(), Constants.CHARSET_UTF8);

			Bundle bundle = (Bundle) jsonParser.parseResource(resourceContents);
			for (IBaseResource resource : BundleUtil.toListOfResources(myFhirContext, bundle)) {
				String resourceType = resource.getIdElement().getResourceType();
				IIdType resourceId = resource.getIdElement();
				String resourceVersion = resource.getIdElement().getValueAsString();
				retVal.computeIfAbsent(resourceType, k -> new HashMap<>())
					.computeIfAbsent(
						resourceId.getIdPart(),
							k -> new HashSet<>()).add(resourceVersion);
			}
		}

		return retVal;
	}

	private Parameters getPatch(int theVersion) {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.name.given"));
		op.addPart().setName("value").setValue(new StringType("given-v" + theVersion + 1));
		return patch;
	}

	/**
	 * Create the indicated number of patients with the indicated number of total versions each
	 */
	private Map<String, Set<String>> createPatientsWithHistory(
		@SuppressWarnings("SameParameterValue") int thePatientCount, @SuppressWarnings("SameParameterValue") int theVersionCount) {

		List<Integer> versionCounts = IntStream.range(0, thePatientCount).mapToObj(i -> theVersionCount).toList();
		return createPatientsWithHistory(versionCounts);
	}

	private Map<String, Set<String>> createPatientsWithHistory(List<Integer> versionCounts) {
			Map<String, Set<String>> retVal = new HashMap<>();
		for (int i = 0; i < versionCounts.size(); i++) {


		Integer theVersionCount = versionCounts.get(i);
			IIdType id = createPatient(i);
			Set<String> patientVersionIds = new HashSet<>();
			retVal.put(id.getIdPart(), patientVersionIds);

			// create indicated additional versions

			int patientAdditionalVersionCount = theVersionCount - 1;
			IIdType lastVersonId = id;
			patientVersionIds.add(lastVersonId.getValueAsString());

			for (int vCount = 0; vCount < patientAdditionalVersionCount; vCount++) {
				Parameters patch = getPatch(vCount);
				DaoMethodOutcome patchOutcome = myPatientDao.patch(lastVersonId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
				AssertionsForClassTypes.assertThat(patchOutcome).isNotNull();
				lastVersonId = patchOutcome.getId();
				patientVersionIds.add(lastVersonId.getValueAsString());
			}
		}
		return retVal;
	}

	private IIdType createPatient(int i) {
		Patient p = new Patient();
		p.setId("pat-"+ i);
		p.addName()
			.setGiven(List.of(new StringType("given")))
			.setFamily("lastname");
		return myPatientDao.create(p, mySrd).getId();
	}

	@Test
	void testGroupExportWithMdmEnabled_EidMatchOnly() {

		createAndSetMdmSettingsForEidMatchOnly();
		BundleBuilder bb = new BundleBuilder(myFhirContext);

		//In this test, we create two patients with the same Eid value for the eid system specified in mdm rules
		//and 2 observations referencing one of each of these patients
		//Create a group that contains one of the patients.
		//When we export the group, we should get both patients and the 2 observations
		//in the export as the other patient should be mdm expanded
		//based on having the same eid value
		Patient pat1 = new Patient();
		pat1.setId("pat-1");
		pat1.addIdentifier(new Identifier().setSystem(TEST_PATIENT_EID_SYS).setValue("the-patient-eid-value"));
		bb.addTransactionUpdateEntry(pat1);

		Observation obs1 = new Observation();
		obs1.setId("obs-1");
		obs1.setSubject(new Reference("Patient/pat-1"));
		bb.addTransactionUpdateEntry(obs1);

		Patient pat2 = new Patient();
		pat2.setId("pat-2");
		pat2.addIdentifier(new Identifier().setSystem(TEST_PATIENT_EID_SYS).setValue("the-patient-eid-value"));
		bb.addTransactionUpdateEntry(pat2);

		Observation obs2 = new Observation();
		obs2.setId("obs-2");
		obs2.setSubject(new Reference("Patient/pat-2"));
		bb.addTransactionUpdateEntry(obs2);

		Group group = new Group();
		group.setId("Group/mdm-group");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/pat-1");
		bb.addTransactionUpdateEntry(group);

		myClient.transaction().withBundle(bb.getBundle()).execute();

		BulkExportJobResults bulkExportJobResults = startGroupBulkExportJobAndAwaitCompletionForMdmExpand(new HashSet<>(), new HashSet<>(), "mdm-group", true);
		BulkExportJobHelper.BulkExportContents contents = myBulkExportJobHelper.fetchJobResults(bulkExportJobResults);

		assertThat(contents.getResourceTypes()).hasSize(3);
		assertThat(contents.getResourceIdPartsForType("Group"))
			.containsExactlyInAnyOrder("mdm-group");

		assertThat(contents.getResourceIdPartsForType("Patient"))
			.containsExactlyInAnyOrder("pat-1", "pat-2");

		assertThat(contents.getResourceIdPartsForType("Observation"))
			.containsExactlyInAnyOrder("obs-1", "obs-2");

	}


	private void createAndSetMdmSettingsForEidMatchOnly() {
		MdmSettings mdmSettings = new MdmSettings(myMdmRulesValidator);
		mdmSettings.setEnabled(true);
		mdmSettings.setMdmMode(MdmModeEnum.MATCH_ONLY);
		MdmRulesJson rules = new MdmRulesJson();
		rules.setMdmTypes(List.of("Patient"));
		rules.addEnterpriseEIDSystem("Patient", TEST_PATIENT_EID_SYS);
		mdmSettings.setMdmRules(rules);

		myMdmExpandersHolder.setMdmSettings(mdmSettings);
	}

	private void restoreMdmSettingsToDefault() {
		myMdmExpandersHolder.setMdmSettings(new MdmSettings(myMdmRulesValidator));
	}

	private Group createGroupWithPatients() {
		Patient patient = new Patient();
		patient.setId("PF");
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		patient = new Patient();
		patient.setId("PM");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		patient.setActive(true);
		myClient.update().resource(patient).execute();

		Group group = new Group();
		group.setId("Group/G");
		group.setActive(true);
		group.addMember().getEntity().setReference("Patient/PF");
		group.addMember().getEntity().setReference("Patient/PM");
		myClient.update().resource(group).execute();

		return group;
	}


	@SuppressWarnings("SameParameterValue")
	BulkExportJobResults startGroupBulkExportJobAndAwaitCompletionForMdmExpand(HashSet<String> theResourceTypes, HashSet<String> theFilters, String theGroupId, boolean theMdmExpand) {
		return startBulkExportJobAndAwaitCompletion(BulkExportJobParameters.ExportStyle.GROUP, theResourceTypes, theFilters, theGroupId, theMdmExpand, false);
	}

	@SuppressWarnings("SameParameterValue")
	BulkExportJobResults startGroupBulkExportJobAndAwaitCompletionForHistory(HashSet<String> theResourceTypes, HashSet<String> theFilters, String theGroupId, boolean theIncludeHistory) {
		return startBulkExportJobAndAwaitCompletion(BulkExportJobParameters.ExportStyle.GROUP, theResourceTypes, theFilters, theGroupId, false, theIncludeHistory);
	}

	BulkExportJobResults startGroupBulkExportJobAndAwaitCompletion(HashSet<String> theResourceTypes, HashSet<String> theFilters, String theGroupId) {
		return startBulkExportJobAndAwaitCompletion(BulkExportJobParameters.ExportStyle.GROUP, theResourceTypes, theFilters, theGroupId, false, false);
	}
	BulkExportJobResults startGroupBulkExportJobWithHistoryAndAwaitCompletion(HashSet<String> theResourceTypes, HashSet<String> theFilters, String theGroupId) {
		return startBulkExportJobAndAwaitCompletion(BulkExportJobParameters.ExportStyle.GROUP, theResourceTypes, theFilters, theGroupId, false, true);
	}
	BulkExportJobResults startSystemBulkExportJobAndAwaitCompletion(Set<String> theResourceTypes, Set<String> theFilters) {
		return startBulkExportJobAndAwaitCompletion(BulkExportJobParameters.ExportStyle.SYSTEM, theResourceTypes, theFilters, null, false, false);
	}

	@SuppressWarnings("SameParameterValue")
	BulkExportJobResults startBulkExportJobAndAwaitCompletion(
		BulkExportJobParameters.ExportStyle theExportStyle,
		Set<String> theResourceTypes,
		Set<String> theFilters,
		String theGroupOrPatientId,
		boolean theMdmExpandEnabled
	) {
		return startBulkExportJobAndAwaitCompletion(theExportStyle, theResourceTypes, theFilters, theGroupOrPatientId, theMdmExpandEnabled, false);
	}

	@SuppressWarnings("SameParameterValue")
	BulkExportJobResults startBulkExportJobAndAwaitCompletion(
		BulkExportJobParameters.ExportStyle theExportStyle,
		Set<String> theResourceTypes,
		Set<String> theFilters,
		String theGroupOrPatientId,
		boolean theMdmExpandEnabled,
		boolean theIncludeHistory
	) {
		Parameters parameters = new Parameters();
		if (theIncludeHistory) {
			parameters.addParameter(PARAM_EXPORT_INCLUDE_HISTORY, true);
		}

		parameters.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, Constants.CT_FHIR_NDJSON);
		if (theFilters != null && !theFilters.isEmpty()) {
			for (String typeFilter : theFilters) {
				parameters.addParameter(
					JpaConstants.PARAM_EXPORT_TYPE_FILTER,
					typeFilter
				);
			}
		}

		if (theResourceTypes != null && !theResourceTypes.isEmpty()) {
			parameters.addParameter(
				JpaConstants.PARAM_EXPORT_TYPE,
				String.join(",", theResourceTypes)
			);
		}

		if (theMdmExpandEnabled) {
			parameters.addParameter(JpaConstants.PARAM_EXPORT_MDM, theMdmExpandEnabled);
		}


		MethodOutcome outcome;
		if (theExportStyle == BulkExportJobParameters.ExportStyle.GROUP) {

			outcome = myClient
				.operation()
				.onInstance("Group/" + theGroupOrPatientId)
				.named(ProviderConstants.OPERATION_EXPORT)
				.withParameters(parameters)
				.returnMethodOutcome()
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
				.execute();
		} else if (theExportStyle == BulkExportJobParameters.ExportStyle.PATIENT && theGroupOrPatientId != null) {
			//TODO add support for this actual processor.
			fail("Bulk Exports that return no data do not return");
			outcome = myClient
				.operation()
				.onInstance("Patient/" + theGroupOrPatientId)
				.named(ProviderConstants.OPERATION_EXPORT)
				.withParameters(parameters)
				.returnMethodOutcome()
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
				.execute();
		} else {
			// system request
			outcome = myClient
				.operation()
				.onServer()
				.named(ProviderConstants.OPERATION_EXPORT)
				.withParameters(parameters)
				.returnMethodOutcome()
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
				.execute();
		}
		assertNotNull(outcome);
		assertEquals(202, outcome.getResponseStatusCode());
		String pollLocation = null;
		for (String header : outcome.getResponseHeaders().keySet()) {
			// headers are in lowercase
			// constants are in Pascal Case
			// :(
			if (header.equalsIgnoreCase(Constants.HEADER_CONTENT_LOCATION)) {
				pollLocation = outcome.getResponseHeaders().get(header).get(0);
				break;
			}
		}
		assertNotNull(pollLocation);
		UrlUtil.UrlParts parts = UrlUtil.parseUrl(pollLocation);
		assertTrue(isNotBlank(parts.getParams()));
		Map<String, String[]> queryParams = UrlUtil.parseQueryString(parts.getParams());
		assertThat(queryParams).containsKey(JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID);
		String jobInstanceId = queryParams.get(JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID)[0];

		assertNotNull(jobInstanceId);

		myBatch2JobHelper.awaitJobCompletion(jobInstanceId, 60);

		await().atMost(300, TimeUnit.SECONDS).until(() -> myJobCoordinator.getInstance(jobInstanceId).getReport() != null);

		String report = myJobCoordinator.getInstance(jobInstanceId).getReport();
		return JsonUtil.deserialize(report, BulkExportJobResults.class);
	}

	private void verifyBulkExportResults(@SuppressWarnings("SameParameterValue") String theGroupId, HashSet<String> theFilters, List<String> theContainedList, List<String> theExcludedList) {
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId("Group/" + theGroupId);
		options.setFilters(theFilters);
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
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
			List<String> binaryIds = file.getValue();
			assertThat(binaryIds).hasSize(1);
			for (String binaryId : binaryIds) {
				Binary binary = myBinaryDao.read(new IdType(binaryId), mySrd);
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
				String contents = new String(binary.getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Next contents for type {} :\n{}", binary.getResourceType(), contents);
				for (String containedString : theContainedList) {
					assertThat(contents).contains(containedString);

				}
				for (String excludedString : theExcludedList) {
					assertThat(contents).doesNotContain(excludedString);
				}
			}
		}
	}

	private BulkExportJobResults startPatientBulkExportJobAndAwaitResults(HashSet<String> theTypes, HashSet<String> theFilters, @SuppressWarnings("SameParameterValue") String thePatientId) {
		return startBulkExportJobAndAwaitCompletion(BulkExportJobParameters.ExportStyle.PATIENT, theTypes, theFilters, thePatientId, false);
	}
}
