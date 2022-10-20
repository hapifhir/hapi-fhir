package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.BulkExportUtils;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
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
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

public class BulkExportUseCaseTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkExportUseCaseTest.class);

	@Autowired
	private IBatch2JobRunner myJobRunner;

	@Nested
	public class SpecConformanceTests {
		@Test
		public void testPollingLocationContainsAllRequiredAttributesUponCompletion() throws IOException {

			//Given a patient exists
			Patient p = new Patient();
			p.setId("Pat-1");
			myClient.update().resource(p).execute();

			//And Given we start a bulk export job
			HttpGet httpGet = new HttpGet(myClient.getServerBase() + "/$export?_type=Patient");
			httpGet.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
			String pollingLocation;
			try (CloseableHttpResponse status = ourHttpClient.execute(httpGet)) {
				Header[] headers = status.getHeaders("Content-Location");
				pollingLocation = headers[0].getValue();
			}
			String jobId = pollingLocation.substring(pollingLocation.indexOf("_jobId=") + 7);
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
				HttpGet expandGet = new HttpGet(ourServerBase + "/" + replace);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test with the Accept Header set to application/fhir+ndjson should stream out the results.
				HttpGet expandGet = new HttpGet(ourServerBase + "/" + replace);
				expandGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_NDJSON);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test that demanding octet-stream will force it to whatever the Binary's content-type is set to.
				HttpGet expandGet = new HttpGet(ourServerBase + "/" + replace);
				expandGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_OCTET_STREAM);
				try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
					Header[] headers = status.getHeaders("Content-Type");
					String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
					logContentTypeAndResponse(headers, response);
					validateNdJsonResponse(headers, response, patientCount);
				}
			}

			{ //Test with the Accept Header set to application/fhir+json should simply return the Binary resource.
				HttpGet expandGet = new HttpGet(ourServerBase + "/" + replace);
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
			myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
		}

		@AfterEach
		public void after() {
			myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
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
	}

	@Nested
	public class GroupBulkExportTests {
		@Test
		public void testVeryLargeGroup() {

			Group group = new Group();
			group.setId("Group/G");
			group.setActive(true);

			for (int i = 0; i < 600; i++) {
				Patient patient = new Patient();
				patient.setId("PING-" + i);
				patient.setGender(Enumerations.AdministrativeGender.FEMALE);
				patient.setActive(true);
				myClient.update().resource(patient).execute();
				group.addMember().getEntity().setReference("Patient/PING-" + i);

				Observation obs = new Observation();
				obs.setId("obs-" + i);
				obs.setSubject(new Reference("Patient/PING-" + i));
				myClient.update().resource(obs).execute();
			}

			myClient.update().resource(group).execute();

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

	BulkExportJobResults startSystemBulkExportJobAndAwaitCompletion(HashSet<String> theResourceTypes, HashSet<String> theFilters) {
		return startBulkExportJobAndAwaitCompletion(BulkDataExportOptions.ExportStyle.SYSTEM, theResourceTypes, theFilters, null);
	}

	BulkExportJobResults startBulkExportJobAndAwaitCompletion(BulkDataExportOptions.ExportStyle theExportStyle, HashSet<String> theResourceTypes, HashSet<String> theFilters, String theGroupOrPatientId) {
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

		myBatch2JobHelper.awaitJobCompletion(startResponse.getJobId());

		await().until(() -> myJobRunner.getJobInfo(startResponse.getJobId()).getReport() != null);

		String report = myJobRunner.getJobInfo(startResponse.getJobId()).getReport();
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
		myBatch2JobHelper.awaitJobCompletion(startResponse.getJobId());

		await().until(() -> myJobRunner.getJobInfo(startResponse.getJobId()).getReport() != null);

		// Iterate over the files
		String report = myJobRunner.getJobInfo(startResponse.getJobId()).getReport();
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
