package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.BulkExportUtils;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.SearchParameterUtil;
import com.google.common.collect.Sets;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class BulkExportUseCaseTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkExportUseCaseTest.class);

	@Autowired
	private IBatch2JobRunner myJobRunner;

	@Autowired
	private IJobPersistence myJobPersistence;
	@Autowired
	private IJobMaintenanceService myJobMaintenanceService;
	@Autowired
	private IBatch2JobInstanceRepository myJobInstanceRepository;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;

	@BeforeEach
	public void beforeEach() {
		myStorageSettings.setJobFastTrackingEnabled(false);
	}


	@Nested
	public class SpecConformanceTests {

		@Test
		public void testBatchJobsAreOnlyReusedIfInProgress() throws IOException {
			//Given a patient exists
			Patient p = new Patient();
			p.setId("Pat-1");
			myClient.update().resource(p).execute();

			//And Given we start a bulk export job
			String pollingLocation = submitBulkExportForTypes("Patient");
			String jobId = getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			//When we execute another batch job, it should not have the same job id.
			String secondPollingLocation = submitBulkExportForTypes("Patient");
			String secondJobId = getJobIdFromPollingLocation(secondPollingLocation);

			//Then the job id should be different
			assertThat(secondJobId, not(equalTo(jobId)));


			myBatch2JobHelper.awaitJobCompletion(secondJobId);
		}

		@Test
		public void testPollingLocationContainsAllRequiredAttributesUponCompletion() throws IOException {

			//Given a patient exists
			Patient p = new Patient();
			p.setId("Pat-1");
			myClient.update().resource(p).execute();

			//And Given we start a bulk export job
			String pollingLocation = submitBulkExportForTypes("Patient");
			String jobId = getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			//Then: When the poll shows as complete, all attributes should be filled.
			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export?_type=Patient";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);

				ourLog.info(responseContent);

				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertThat(result.getRequest(), is(equalTo(expectedOriginalUrl)));
				assertThat(result.getRequiresAccessToken(), is(equalTo(true)));
				assertThat(result.getTransactionTime(), is(notNullValue()));
				assertThat(result.getOutput(), is(not(empty())));

				//We assert specifically on content as the deserialized version will "helpfully" fill in missing fields.
				assertThat(responseContent, containsString("\"error\" : [ ]"));
			}
		}

		@Nonnull
		private String getJobIdFromPollingLocation(String pollingLocation) {
			return pollingLocation.substring(pollingLocation.indexOf("_jobId=") + 7);
		}

		@Test
		public void export_shouldExportPatientResource_whenTypeParameterOmitted() throws IOException {

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
			String jobId = getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			//Then: When the poll shows as complete, all attributes should be filled.
			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				assertEquals(200, status.getStatusLine().getStatusCode());
				String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
				assertTrue(isNotBlank(responseContent), responseContent);

				ourLog.info(responseContent);

				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertThat(result.getRequest(), is(equalTo(expectedOriginalUrl)));
				assertThat(result.getRequiresAccessToken(), is(equalTo(true)));
				assertThat(result.getTransactionTime(), is(notNullValue()));
				assertThat(result.getOutput(), is(not(empty())));

				//We assert specifically on content as the deserialized version will "helpfully" fill in missing fields.
				assertThat(responseContent, containsString("\"error\" : [ ]"));
			}
		}

		@Test
		public void export_shouldExportPatientAndObservationAndEncounterResources_whenTypeParameterOmitted() throws IOException {

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
			String jobId = getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertThat(result.getRequest(), is(equalTo(expectedOriginalUrl)));
				assertThat(result.getRequiresAccessToken(), is(equalTo(true)));
				assertThat(result.getTransactionTime(), is(notNullValue()));
				assertEquals(result.getOutput().size(), 3);
				assertEquals(1, result.getOutput().stream().filter(o -> o.getType().equals("Patient")).collect(Collectors.toList()).size());
				assertEquals(1, result.getOutput().stream().filter(o -> o.getType().equals("Observation")).collect(Collectors.toList()).size());
				assertEquals(1, result.getOutput().stream().filter(o -> o.getType().equals("Encounter")).collect(Collectors.toList()).size());

				//We assert specifically on content as the deserialized version will "helpfully" fill in missing fields.
				assertThat(responseContent, containsString("\"error\" : [ ]"));
			}
		}

		@Test
		public void export_shouldNotExportBinaryResource_whenTypeParameterOmitted() throws IOException {

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
			String jobId = getJobIdFromPollingLocation(pollingLocation);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			HttpGet statusGet = new HttpGet(pollingLocation);
			String expectedOriginalUrl = myClient.getServerBase() + "/$export";
			try (CloseableHttpResponse status = ourHttpClient.execute(statusGet)) {
				String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
				BulkExportResponseJson result = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
				assertThat(result.getRequest(), is(equalTo(expectedOriginalUrl)));
				assertThat(result.getRequiresAccessToken(), is(equalTo(true)));
				assertThat(result.getTransactionTime(), is(notNullValue()));
				assertEquals(result.getOutput().size(), 1);
				assertEquals(1, result.getOutput().stream().filter(o -> o.getType().equals("Patient")).collect(Collectors.toList()).size());
				assertEquals(0, result.getOutput().stream().filter(o -> o.getType().equals("Binary")).collect(Collectors.toList()).size());

				//We assert specifically on content as the deserialized version will "helpfully" fill in missing fields.
				assertThat(responseContent, containsString("\"error\" : [ ]"));
			}
		}

	}

	private String submitBulkExportForTypes(String... theTypes) throws IOException {
		String typeString = String.join(",", theTypes);
		HttpGet httpGet = new HttpGet(myClient.getServerBase() + "/$export?_type=" + typeString);
		httpGet.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		String pollingLocation;
		try (CloseableHttpResponse status = ourHttpClient.execute(httpGet)) {
			Header[] headers = status.getHeaders("Content-Location");
			pollingLocation = headers[0].getValue();
		}
		return pollingLocation;
	}

	@Nested
	public class SystemBulkExportTests {

		@Test
		public void testBinariesAreStreamedWithRespectToAcceptHeader() throws IOException {
			int patientCount = 5;
			for (int i = 0; i < patientCount; i++) {
				Patient patient = new Patient();
				patient.setId("pat-" + i);
				myPatientDao.update(patient);
			}

			HashSet<String> types = Sets.newHashSet("Patient");
			BulkExportJobResults bulkExportJobResults = startSystemBulkExportJobAndAwaitCompletion(types, new HashSet<String>());
			Map<String, List<String>> resourceTypeToBinaryIds = bulkExportJobResults.getResourceTypeToBinaryIds();
			assertThat(resourceTypeToBinaryIds.get("Patient"), hasSize(1));
			String patientBinaryId = resourceTypeToBinaryIds.get("Patient").get(0);
			String replace = patientBinaryId.replace("_history/1", "");

			{ // Test with the Accept Header omitted should stream out the results.
				HttpGet expandGet = new HttpGet(myServerBase + "/" + replace);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test with the Accept Header set to application/fhir+ndjson should stream out the results.
				HttpGet expandGet = new HttpGet(myServerBase + "/" + replace);
				expandGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_NDJSON);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test that demanding octet-stream will force it to whatever the Binary's content-type is set to.
				HttpGet expandGet = new HttpGet(myServerBase + "/" + replace);
				expandGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_OCTET_STREAM);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test with the Accept Header set to application/fhir+json should simply return the Binary resource.
				HttpGet expandGet = new HttpGet(myServerBase + "/" + replace);
				expandGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON);

				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
					logContentTypeAndResponse(headers, response);

					assertThat(headers[0].getValue(), containsString(Constants.CT_FHIR_JSON));
					assertThat(response, is(not(containsString("\n"))));
					Binary binary = myFhirContext.newJsonParser().parseResource(Binary.class, response);
					assertThat(binary.getIdElement().getValue(), is(equalTo(patientBinaryId)));
				}
			}
		}

		@Test
		public void testResourceCountIsCorrect() {
			int patientCount = 5;
			for (int i = 0; i < patientCount; i++) {
				Patient patient = new Patient();
				patient.setId("pat-" + i);
				myPatientDao.update(patient);
			}

			BulkDataExportOptions options = new BulkDataExportOptions();
			options.setResourceTypes(Collections.singleton("Patient"));
			options.setFilters(Collections.emptySet());
			options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			myCaptureQueriesListener.clear();

			Batch2JobStartResponse startResponse = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(options));

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
					.map(t -> t.toString())
					.collect(Collectors.joining("\n * "));
				ourLog.info("Entities:\n * " + entities);
			});

			final Optional<JobInstance> optJobInstance = myJobPersistence.fetchInstance(jobId);

			assertNotNull(optJobInstance);
			assertTrue(optJobInstance.isPresent());

			final JobInstance jobInstance = optJobInstance.get();

			assertEquals(patientCount, jobInstance.getCombinedRecordsProcessed());
		}

		private void logContentTypeAndResponse(Header[] headers, String response) {
			ourLog.info("**************************");
			ourLog.info("Content-Type is: {}", headers[0]);
			ourLog.info("Response is: {}", response);
			ourLog.info("**************************");
		}

		private void validateNdJsonResponse(Header[] headers, String response, int theExpectedCount) {
			assertThat(headers[0].getValue(), containsString(Constants.CT_FHIR_NDJSON));
			assertThat(response, is(containsString("\n")));
			Bundle bundle = myFhirContext.newNDJsonParser().parseResource(Bundle.class, response);
			assertThat(bundle.getEntry(), hasSize(theExpectedCount));
		}
	}


	@Nested
	public class PatientBulkExportTests {

		@BeforeEach
		public void before() {
			myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		}

		@AfterEach
		public void after() {
			myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
			myStorageSettings.setBulkExportFileMaximumCapacity(JpaStorageSettings.DEFAULT_BULK_EXPORT_FILE_MAXIMUM_CAPACITY);
		}

		@Test
		public void testPatientExportIgnoresResourcesNotInPatientCompartment() {
			Patient patient = new Patient();
			patient.setId("pat-1");
			myPatientDao.update(patient);
			Observation obs = new Observation();

			obs.setId("obs-included");
			obs.setSubject(new Reference("Patient/pat-1"));
			myObservationDao.update(obs);

			Observation obs2 = new Observation();
			obs2.setId("obs-excluded");
			myObservationDao.update(obs2);

			HashSet<String> types = Sets.newHashSet("Patient", "Observation");
			BulkExportJobResults bulkExportJobResults = startPatientBulkExportJobAndAwaitResults(types, new HashSet<String>(), "ha");
			Map<String, List<IBaseResource>> typeToResources = convertJobResultsToResources(bulkExportJobResults);
			assertThat(typeToResources.get("Patient"), hasSize(1));
			assertThat(typeToResources.get("Observation"), hasSize(1));

			Map<String, String> typeToContents = convertJobResultsToStringContents(bulkExportJobResults);
			assertThat(typeToContents.get("Observation"), containsString("obs-included"));
			assertThat(typeToContents.get("Observation"), not(containsString("obs-excluded")));
		}

		@Test
		public void testBulkExportWithLowMaxFileCapacity() {
			final int numPatients = 250;
			myStorageSettings.setBulkExportFileMaximumCapacity(1);
			myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

			RequestDetails details = new SystemRequestDetails();
			List<String> patientIds = new ArrayList<>();
			for(int i = 0; i < numPatients; i++){
				String id = "p-"+i;
				Patient patient = new Patient();
				patient.setId(id);
				myPatientDao.update(patient, details);
				patientIds.add(id);
			}

			int patientsCreated = myPatientDao.search(SearchParameterMap.newSynchronous(), details).size();
			assertEquals(numPatients, patientsCreated);

			BulkDataExportOptions options = new BulkDataExportOptions();
			options.setResourceTypes(Sets.newHashSet("Patient"));
			options.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);
			options.setOutputFormat(Constants.CT_FHIR_NDJSON);

			Batch2JobStartResponse job = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(options));
			myBatch2JobHelper.awaitJobCompletion(job.getInstanceId(), 60);
			ourLog.debug("Job status after awaiting - {}", myJobRunner.getJobInfo(job.getInstanceId()).getStatus());
			await()
				.atMost(300, TimeUnit.SECONDS)
				.until(() -> {
					BulkExportJobStatusEnum status = myJobRunner.getJobInfo(job.getInstanceId()).getStatus();
					if (!BulkExportJobStatusEnum.COMPLETE.equals(status)) {
						fail("Job status was changed from COMPLETE to " + status);
					}
					return myJobRunner.getJobInfo(job.getInstanceId()).getReport() != null;
				});

			String report = myJobRunner.getJobInfo(job.getInstanceId()).getReport();
			BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
			List<String> binaryUrls = results.getResourceTypeToBinaryIds().get("Patient");

			IParser jsonParser = myFhirContext.newJsonParser();
			for(String url : binaryUrls){
				Binary binary = myClient.read().resource(Binary.class).withUrl(url).execute();
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
				String resourceContents = new String(binary.getContent(), Constants.CHARSET_UTF8);
				String resourceId = jsonParser.parseResource(resourceContents).getIdElement().getIdPart();
				assertTrue(patientIds.contains(resourceId));
			}
		}
	}


	@Nested
	public class GroupBulkExportTests {
		@Test
		public void testVeryLargeGroup() {

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
			Map<String, List<IBaseResource>> firstMap = convertJobResultsToResources(bulkExportJobResults);
			assertThat(firstMap.keySet(), hasSize(3));
			assertThat(firstMap.get("Group"), hasSize(1));
			assertThat(firstMap.get("Patient"), hasSize(600));
			assertThat(firstMap.get("Observation"), hasSize(600));
		}

		@Test
		public void testGroupBulkExportMembershipShouldNotExpandIntoOtherGroups() {
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
			Map<String, List<IBaseResource>> firstMap = convertJobResultsToResources(bulkExportJobResults);
			assertThat(firstMap.get("Patient"), hasSize(1));
			assertThat(firstMap.get("Group"), hasSize(1));
		}

		@Test
		public void testDifferentTypesDoNotUseCachedResults() {
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
			Map<String, List<IBaseResource>> firstMap = convertJobResultsToResources(bulkExportJobResults);
			assertThat(firstMap.get("Patient"), hasSize(1));
			assertThat(firstMap.get("Observation"), hasSize(1));

			HashSet<String> otherResourceTypes = Sets.newHashSet("Coverage", "Patient");
			BulkExportJobResults altBulkExportResults = startGroupBulkExportJobAndAwaitCompletion(otherResourceTypes, new HashSet<>(), "G2");
			Map<String, List<IBaseResource>> secondMap = convertJobResultsToResources(altBulkExportResults);
			assertThat(secondMap.get("Patient"), hasSize(1));
			assertThat(secondMap.get("Coverage"), hasSize(1));

			runInTransaction(() -> {
				List<Batch2JobInstanceEntity> instances = myJobInstanceRepository.findAll();
				ourLog.info("Job instance states:\n * {}", instances.stream().map(Object::toString).collect(Collectors.joining("\n * ")));
				List<Batch2WorkChunkEntity> workChunks = myWorkChunkRepository.findAll();
				ourLog.info("Work chunks instance states:\n * {}", workChunks.stream().map(Object::toString).collect(Collectors.joining("\n * ")));
			});
		}


		@Test
		public void testGroupBulkExportNotInGroup_DoeNotShowUp() {
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
		public void testTwoConsecutiveBulkExports() {

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
		public void testGroupExport_includesObservationsAndEncountersOfPatientsInExportedGroup_whenLuceneIdexingEnabled() {

			// Enable Lucene indexing
			myStorageSettings.setAllowContainsSearches(true);
			myStorageSettings.setAdvancedHSearchIndexing(true);

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

			Map<String, List<IBaseResource>> typeToResources = convertJobResultsToResources(bulkExportJobResults);
			assertThat(typeToResources.get("Patient"), hasSize(2));
			assertThat(typeToResources.get("Group"), hasSize(1));
			assertThat(typeToResources.get("Observation"), hasSize(2));
			assertThat(typeToResources.get("Encounter"), hasSize(2));

			Map<String, String> typeToContents = convertJobResultsToStringContents(bulkExportJobResults);
			assertThat(typeToContents.get("Patient"), containsString("A1"));
			assertThat(typeToContents.get("Patient"), containsString("A2"));

			assertThat(typeToContents.get("Observation"), containsString("obs-a1"));
			assertThat(typeToContents.get("Observation"), containsString("obs-a2"));

			assertThat(typeToContents.get("Encounter"), containsString("enc-a1"));
			assertThat(typeToContents.get("Encounter"), containsString("enc-a2"));

		}


		@Test
		public void testGroupExportPatientAndOtherResources() {
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

			Map<String, List<IBaseResource>> typeToResources = convertJobResultsToResources(bulkExportJobResults);
			assertThat(typeToResources.get("Patient"), hasSize(1));
			assertThat(typeToResources.get("Observation"), hasSize(1));

			Map<String, String> typeToContents = convertJobResultsToStringContents(bulkExportJobResults);
			assertThat(typeToContents.get("Patient"), containsString("PING1"));
			assertThat(typeToContents.get("Patient"), not(containsString("POG2")));

			assertThat(typeToContents.get("Observation"), containsString("obs-included"));
			assertThat(typeToContents.get("Observation"), not(containsString("obs-excluded")));

		}

		@Test
		public void testGroupBulkExportWithTypeFilter() {
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

			HashSet<String> resourceTypes = Sets.newHashSet("Observation", "Patient");
			HashSet<String> filters = Sets.newHashSet("Patient?gender=female");
			BulkExportJobResults results = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, filters, "G");
			Map<String, List<IBaseResource>> stringListMap = convertJobResultsToResources(results);
			assertThat(stringListMap.get("Observation"), hasSize(1));
			assertThat(stringListMap.get("Patient"), hasSize(1));

			Map<String, String> typeToContents = convertJobResultsToStringContents(results);
			assertThat(typeToContents.get("Observation"), not(containsString("obs-male")));
			assertThat(typeToContents.get("Observation"), containsString("obs-female"));
		}

		@Test
		public void testGroupExportOmitResourceTypesFetchesAll() {
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

			HashSet<String> resourceTypes = Sets.newHashSet(SearchParameterUtil.getAllResourceTypesThatAreInPatientCompartment(myFhirContext));

			HashSet<String> filters = Sets.newHashSet();
			BulkExportJobResults results = startGroupBulkExportJobAndAwaitCompletion(resourceTypes, filters, "G");
			Map<String, List<IBaseResource>> typeToResource = convertJobResultsToResources(results);
			assertThat(typeToResource.keySet(), hasSize(4));
			assertThat(typeToResource.get("Group"), hasSize(1));
			assertThat(typeToResource.get("Observation"), hasSize(2));
			assertThat(typeToResource.get("Coverage"), hasSize(2));
			assertThat(typeToResource.get("Patient"), hasSize(2));
		}

		@Test
		public void testGroupExportPatientOnly() {
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

			Map<String, List<IBaseResource>> typeToResources = convertJobResultsToResources(bulkExportJobResults);
			assertThat(typeToResources.get("Patient"), hasSize(1));

			Map<String, String> typeToContents = convertJobResultsToStringContents(bulkExportJobResults);
			assertThat(typeToContents.get("Patient"), containsString("PING1"));
			assertThat(typeToContents.get("Patient"), not(containsString("POG2")));
		}

		@Test
		public void testGroupBulkExportMultipleResourceTypes() {
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

			Map<String, List<IBaseResource>> typeToResources = convertJobResultsToResources(bulkExportJobResults);
			assertThat(typeToResources.get("Observation"), hasSize(1));
			assertThat(typeToResources.get("Coverage"), hasSize(1));

			Map<String, String> typeToContents = convertJobResultsToStringContents(bulkExportJobResults);
			assertThat(typeToContents.get("Observation"), containsString("obs-included"));
			assertThat(typeToContents.get("Coverage"), containsString("coverage-included"));
		}

		@Test
		public void testGroupBulkExportOverLargeDataset() {
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

			Map<String, List<IBaseResource>> typeToResources = convertJobResultsToResources(bulkExportJobResults);
			assertThat(typeToResources.get("Observation"), hasSize(1000));

			Map<String, String> typeToContents = convertJobResultsToStringContents(bulkExportJobResults);
			assertThat(typeToContents.get("Observation"), not(containsString("not-included")));
			assertThat(typeToContents.get("Observation"), containsString("obs-included-0"));
			assertThat(typeToContents.get("Observation"), containsString("obs-included-999"));
		}

		@Nested
		public class WithClientIdStrategyEnumANYTest {

			@BeforeEach
			void setUp() {
				myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
			}

			@AfterEach
			void tearDown() {
				myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);
			}

			@Test
			public void testGroupExportPatientOnly() {
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

				Map<String, List<IBaseResource>> typeToResources = convertJobResultsToResources(bulkExportJobResults);
				assertThat(typeToResources.get("Patient"), hasSize(1));

				Map<String, String> typeToContents = convertJobResultsToStringContents(bulkExportJobResults);
				assertThat(typeToContents.get("Patient"), containsString("PING1"));
				assertThat(typeToContents.get("Patient"), not(containsString("POG2")));
			}
		}

	}

	private Map<String, String> convertJobResultsToStringContents(BulkExportJobResults theResults) {
		Map<String, String> typeToResources = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : theResults.getResourceTypeToBinaryIds().entrySet()) {
			typeToResources.put(entry.getKey(), "");
			StringBuilder sb = new StringBuilder();
			List<String> binaryIds = entry.getValue();
			for (String binaryId : binaryIds) {
				String contents = getBinaryContentsAsString(binaryId);
				if (!contents.endsWith("\n")) {
					contents = contents + "\n";
				}
				sb.append(contents);
			}
			typeToResources.put(entry.getKey(), sb.toString());
		}
		return typeToResources;
	}

	Map<String, List<IBaseResource>> convertJobResultsToResources(BulkExportJobResults theResults) {
		Map<String, String> stringStringMap = convertJobResultsToStringContents(theResults);
		Map<String, List<IBaseResource>> typeToResources = new HashMap<>();
		stringStringMap.entrySet().forEach(entry -> typeToResources.put(entry.getKey(), convertNDJSONToResources(entry.getValue())));
		return typeToResources;
	}

	private List<IBaseResource> convertNDJSONToResources(String theValue) {
		IParser iParser = myFhirContext.newJsonParser();
		return theValue.lines()
			.map(iParser::parseResource)
			.toList();
	}

	private String getBinaryContentsAsString(String theBinaryId) {
		Binary binary = myBinaryDao.read(new IdType(theBinaryId));
		assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
		String contents = new String(binary.getContent(), Constants.CHARSET_UTF8);
		return contents;
	}

	BulkExportJobResults startGroupBulkExportJobAndAwaitCompletion(HashSet<String> theResourceTypes, HashSet<String> theFilters, String theGroupId) {
		return startBulkExportJobAndAwaitCompletion(BulkDataExportOptions.ExportStyle.GROUP, theResourceTypes, theFilters, theGroupId);
	}

	BulkExportJobResults startSystemBulkExportJobAndAwaitCompletion(Set<String> theResourceTypes, Set<String> theFilters) {
		return startBulkExportJobAndAwaitCompletion(BulkDataExportOptions.ExportStyle.SYSTEM, theResourceTypes, theFilters, null);
	}

	BulkExportJobResults startBulkExportJobAndAwaitCompletion(BulkDataExportOptions.ExportStyle theExportStyle, Set<String> theResourceTypes, Set<String> theFilters, String theGroupOrPatientId) {
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(theResourceTypes);
		options.setFilters(theFilters);
		options.setExportStyle(theExportStyle);
		if (theExportStyle == BulkDataExportOptions.ExportStyle.GROUP) {
			options.setGroupId(new IdType("Group", theGroupOrPatientId));
		}
		if (theExportStyle == BulkDataExportOptions.ExportStyle.PATIENT && theGroupOrPatientId != null) {
			//TODO add support for this actual processor.
			//options.setPatientId(new IdType("Patient", theGroupOrPatientId));
		}
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		Batch2JobStartResponse startResponse = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(options));

		assertNotNull(startResponse);

		myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId(), 60);

		await().atMost(300, TimeUnit.SECONDS).until(() -> myJobRunner.getJobInfo(startResponse.getInstanceId()).getReport() != null);

		String report = myJobRunner.getJobInfo(startResponse.getInstanceId()).getReport();
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
		return results;
	}

	private void verifyBulkExportResults(String theGroupId, HashSet<String> theFilters, List<String> theContainedList, List<String> theExcludedList) {
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId(new IdType("Group", theGroupId));
		options.setFilters(theFilters);
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		Batch2JobStartResponse startResponse = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(options));

		assertNotNull(startResponse);

		// Run a scheduled pass to build the export
		myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId());

		await().until(() -> myJobRunner.getJobInfo(startResponse.getInstanceId()).getReport() != null);

		// Iterate over the files
		String report = myJobRunner.getJobInfo(startResponse.getInstanceId()).getReport();
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
		for (Map.Entry<String, List<String>> file : results.getResourceTypeToBinaryIds().entrySet()) {
			List<String> binaryIds = file.getValue();
			assertEquals(1, binaryIds.size());
			for (String binaryId : binaryIds) {
				Binary binary = myBinaryDao.read(new IdType(binaryId));
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());
				String contents = new String(binary.getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Next contents for type {} :\n{}", binary.getResourceType(), contents);
				for (String containedString : theContainedList) {
					assertThat(contents, Matchers.containsString(containedString));

				}
				for (String excludedString : theExcludedList) {
					assertThat(contents, not(Matchers.containsString(excludedString)));
				}
			}
		}
	}

	private BulkExportJobResults startPatientBulkExportJobAndAwaitResults(HashSet<String> theTypes, HashSet<String> theFilters, String thePatientId) {
		return startBulkExportJobAndAwaitCompletion(BulkDataExportOptions.ExportStyle.PATIENT, theTypes, theFilters, thePatientId);
	}
}
