package ca.uhn.fhir.jpa.batch2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * A test to poke at our job framework and induce errors.
 */
public class BulkDataErrorAbuseTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataErrorAbuseTest.class);

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@BeforeEach
	void beforeEach() {
		ourLog.info("BulkDataErrorAbuseTest.beforeEach");
		afterPurgeDatabase();
	}

	@AfterEach
	void afterEach() {
		ourLog.info("BulkDataErrorAbuseTest.afterEach()");
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
	}

	@Test
	public void testGroupBulkExportNotInGroup_DoesNotShowUp() throws InterruptedException, ExecutionException {
		duAbuseTest(100);
	}

	/**
	 * This test is disabled because it never actually exits. Run it if you want to ensure
	 * that changes to the Bulk Export Batch2 task haven't affected our ability to successfully
	 * run endless parallel jobs. If you run it for a few minutes, and it never stops on its own,
	 * you are good.
	 * <p>
	 * The enabled test above called {@link #testGroupBulkExportNotInGroup_DoesNotShowUp()} does
	 * run with the build and runs 100 jobs.
	 */
	@Test
	@Disabled("for manual debugging")
	public void testNonStopAbuseBatch2BulkExportStressTest() throws InterruptedException, ExecutionException {
		duAbuseTest(Integer.MAX_VALUE);
	}

	private void duAbuseTest(int taskExecutions) {
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
		BulkExportJobParameters options = new BulkExportJobParameters();
		options.setResourceTypes(Sets.newHashSet("Patient"));
		options.setGroupId("Group/G2");
		options.setFilters(new HashSet<>());
		options.setExportStyle(BulkExportJobParameters.ExportStyle.GROUP);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
		int workerCount = TestR4Config.ourMaxThreads - 1; // apply a little connection hunger, but not starvation.
		ExecutorService executorService = new ThreadPoolExecutor(workerCount, workerCount,
			0L, TimeUnit.MILLISECONDS,
			workQueue);
		CompletionService<Boolean> completionService = new ExecutorCompletionService<>(executorService);

		ourLog.info("Starting task creation");

		int maxFuturesToProcess = 500;
		for (int i = 0; i < taskExecutions; i++) {
			completionService.submit(() -> {
				String instanceId = null;
				try {
					instanceId = startJob(options);

					// Run a scheduled pass to build the export
					myBatch2JobHelper.awaitJobCompletion(instanceId, 10);

					verifyBulkExportResults(instanceId, List.of("Patient/PING1", "Patient/PING2"), Collections.singletonList("Patient/PNING3"));

					return true;
				} catch (Throwable theError) {
					ourLog.error("Caught an error during processing instance {}", instanceId, theError);
					throw new InternalErrorException("Caught an error during processing instance " + instanceId, theError);
				}
			});

			// Don't let the list of futures grow so big we run out of memory
			if (i != 0 && i % maxFuturesToProcess == 0) {
				executeFutures(completionService, maxFuturesToProcess);
			}
		}

		ourLog.info("Done creating tasks, waiting for task completion");

		// wait for completion to avoid stranding background tasks.
		executorService.shutdown();
		await()
			.atMost(60, TimeUnit.SECONDS)
			.until(() -> {
				return executorService.isTerminated() && executorService.isShutdown();
			});

		// verify that all requests succeeded
		ourLog.info("All tasks complete.  Verify results.");
		executeFutures(completionService, taskExecutions % maxFuturesToProcess);

		executorService.shutdown();
		await()
			.atMost(60, TimeUnit.SECONDS)
				.until(() -> {
					return executorService.isTerminated() && executorService.isShutdown();
				});

		ourLog.info("Finished task execution");
	}

	private void executeFutures(CompletionService<Boolean> theCompletionService, int theTotal) {
		List<String> errors = new ArrayList<>();
		int count = 0;

		while (count + errors.size() < theTotal) {
			try {
				Future<Boolean> future = theCompletionService.take();
				boolean r = future.get();
				assertTrue(r);
				count++;
			} catch (Exception ex) {
				// we will run all the threads to completion, even if we have errors;
				// this is so we don't have background threads kicking around with
				// partial changes.
				// we either do this, or shutdown the completion service in an
				// "inelegant" manner, dropping all threads (which we aren't doing)
				ourLog.error("Failed after checking " + count + " futures");
				errors.add(ex.getMessage());
			}
		}

		if (!errors.isEmpty()) {
			fail(String.format("Failed to execute futures. Found %d errors :\n", errors.size())
				+ String.join(", ", errors));
		}
	}


	private void verifyBulkExportResults(String theInstanceId, List<String> theContainedList, List<String> theExcludedList) {
		// Iterate over the files
		JobInstance jobInfo = myJobCoordinator.getInstance(theInstanceId);
		String report = jobInfo.getReport();
		ourLog.debug("Export job {} report: {}", theInstanceId, report);
		if (!theContainedList.isEmpty()) {
			assertThat(report).as("report for instance " + theInstanceId + " is empty").isNotBlank();
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

				for (String line : lines) {
					IBaseResource resource = myFhirContext.newJsonParser().parseResource(line);
					IIdType nextId = resource.getIdElement().toUnqualifiedVersionless();
					if (!resourceType.equals(nextId.getResourceType())) {
						fail("Found resource of type " + nextId.getResourceType() + " in file for type " + resourceType);
					} else {
						if (!foundIds.add(nextId.getValue())) {
							fail("Found duplicate ID: " + nextId.getValue());
						}
					}
				}
			}
		}

		ourLog.debug("Export job {} exported resources {}", theInstanceId, foundIds);

		for (String containedString : theContainedList) {
			assertThat(foundIds).as("export has expected ids").contains(containedString);
		}
		for (String excludedString : theExcludedList) {
			assertThat(foundIds).as("export doesn't have expected ids").doesNotContain(excludedString);
		}

		assertEquals(2, jobInfo.getCombinedRecordsProcessed());

		ourLog.info("Job {} ok", theInstanceId);
	}

	private String startJob(BulkExportJobParameters theOptions) {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		startRequest.setUseCache(false);
		startRequest.setParameters(theOptions);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		assertNotNull(startResponse);
		return startResponse.getInstanceId();
	}

}
