package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.BulkExportUtils;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A test to poke at our job framework and induce errors.
 */
public class BulkDataErrorAbuseTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataErrorAbuseTest.class);

	@Autowired
	private IBatch2JobRunner myJobRunner;

	@AfterEach
	void afterEach() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
	}

	@Test
	public void testGroupBulkExportNotInGroup_DoesNotShowUp() throws InterruptedException, ExecutionException {
		duAbuseTest(100);
	}

	/**
	 * This test is disabled because it never actually exists. Run it if you want to ensure
	 * that changes to the Bulk Export Batch2 task haven't affected our ability to successfully
	 * run endless parallel jobs. If you run it for a few minutes, and it never stops on its own,
	 * you are good.
	 * <p>
	 * The enabled test above called {@link #testGroupBulkExportNotInGroup_DoesNotShowUp()} does
	 * run with the build and runs 100 jobs.
	 */
	@Test
	@Disabled
	public void testNonStopAbuseBatch2BulkExportStressTest() throws InterruptedException, ExecutionException {
		duAbuseTest(Integer.MAX_VALUE);
	}

	private void duAbuseTest(int taskExecutions) throws InterruptedException, ExecutionException {
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

		// set the export options
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId(new IdType("Group", "G2"));
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
		ExecutorService executorService = new ThreadPoolExecutor(10, 10,
			0L, TimeUnit.MILLISECONDS,
			workQueue);

		ourLog.info("Starting task creation");

		List<Future<Boolean>> futures = new ArrayList<>();
		for (int i = 0; i < taskExecutions; i++) {
			futures.add(executorService.submit(() -> {
				String instanceId = null;
				try {
					instanceId = startJob(options);

					// Run a scheduled pass to build the export
					myBatch2JobHelper.awaitJobCompletion(instanceId, 60);

					verifyBulkExportResults(instanceId, List.of("Patient/PING1", "Patient/PING2"), Collections.singletonList("Patient/PNING3"));

					return true;
				} catch (Throwable theError) {
					ourLog.error("Caught an error during processing instance {}", instanceId, theError);
					throw new InternalErrorException("Caught an error during processing instance " + instanceId, theError);
				}
			}));

			// Don't let the list of futures grow so big we run out of memory
			if (futures.size() > 200) {
				while (futures.size() > 100) {
					// This should always return true, but it'll throw an exception if we failed
					assertTrue(futures.remove(0).get());
				}
			}
		}

		ourLog.info("Done creating tasks, waiting for task completion");

		for (var next : futures) {
			// This should always return true, but it'll throw an exception if we failed
			assertTrue(next.get());
		}

		ourLog.info("Finished task execution");
	}


	private void verifyBulkExportResults(String theInstanceId, List<String> theContainedList, List<String> theExcludedList) {
		// Iterate over the files
		String report = myJobRunner.getJobInfo(theInstanceId).getReport();
		ourLog.debug("Export job {} report: {}", theInstanceId, report);
		if (!theContainedList.isEmpty()) {
			assertThat("report for instance " + theInstanceId + " is empty", report, not(emptyOrNullString()));
		}
		BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);

		Set<String> foundIds = new HashSet<>();
		for (Map.Entry<String, List<String>> file : results.getResourceTypeToBinaryIds().entrySet()) {
			String resourceType = file.getKey();
			List<String> binaryIds = file.getValue();
			for (var nextBinaryId : binaryIds) {

				Binary binary = myBinaryDao.read(new IdType(nextBinaryId), mySrd);
				assertEquals(Constants.CT_FHIR_NDJSON, binary.getContentType());

				String nextNdJsonFileContent = new String(binary.getContent(), Constants.CHARSET_UTF8);
				ourLog.trace("Export job {} file {} contents: {}", theInstanceId, nextBinaryId, nextNdJsonFileContent);

				List<String> lines = new BufferedReader(new StringReader(nextNdJsonFileContent))
					.lines().toList();
				ourLog.debug("Export job {} file {} line-count: {}", theInstanceId, nextBinaryId, lines.size());

				lines.stream()
					.map(line -> myFhirContext.newJsonParser().parseResource(line))
					.map(r -> r.getIdElement().toUnqualifiedVersionless())
					.forEach(nextId -> {
						if (!resourceType.equals(nextId.getResourceType())) {
							fail("Found resource of type " + nextId.getResourceType() + " in file for type " + resourceType);
						} else {
							if (!foundIds.add(nextId.getValue())) {
								fail("Found duplicate ID: " + nextId.getValue());
							}
						}
					});
			}
		}

		ourLog.debug("Export job {} exported resources {}", theInstanceId, foundIds);

		for (String containedString : theContainedList) {
			assertThat("export has expected ids", foundIds, hasItem(containedString));
		}
		for (String excludedString : theExcludedList) {
			assertThat("export doesn't have expected ids", foundIds, not(hasItem(excludedString)));
		}
	}

	private String startJob(BulkDataExportOptions theOptions) {
		Batch2JobStartResponse startResponse = myJobRunner.startNewJob(BulkExportUtils.createBulkExportJobParametersFromExportOptions(theOptions));
		assertNotNull(startResponse);
		return startResponse.getInstanceId();
	}

}
