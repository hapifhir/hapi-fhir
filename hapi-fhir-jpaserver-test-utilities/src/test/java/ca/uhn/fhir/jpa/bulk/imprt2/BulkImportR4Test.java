package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportAppCtx;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportFileServlet;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class BulkImportR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportR4Test.class);
	private final BulkImportFileServlet myBulkImportFileServlet = new BulkImportFileServlet();
	@RegisterExtension
	private final HttpServletExtension myHttpServletExtension = new HttpServletExtension()
		.withServlet(myBulkImportFileServlet);
	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private IJobMaintenanceService myJobCleanerService;
	@Autowired
	private IBatch2JobInstanceRepository myJobInstanceRepository;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;
	@Qualifier("batch2ProcessingChannelReceiver")
	@Autowired
	private IChannelReceiver myChannelReceiver;

	@AfterEach
	public void afterEach() {
		myBulkImportFileServlet.clearFiles();

		LinkedBlockingChannel channel = ProxyUtil.getSingletonTarget(myChannelReceiver, LinkedBlockingChannel.class);
		await().until(() -> channel.getQueueSizeForUnitTest() == 0);
	}

	@Test
	public void testRunBulkImport() {
		// Setup

		int fileCount = 100;
		List<String> indexes = addFiles(fileCount);

		BulkImportJobParameters parameters = new BulkImportJobParameters();
		for (String next : indexes) {
			String url = myHttpServletExtension.getBaseUrl() + "/download?index=" + next;
			parameters.addNdJsonUrl(url);
		}

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(parameters);

		// Execute

		String instanceId = myJobCoordinator.startInstance(request);
		assertThat(instanceId, not(blankOrNullString()));
		ourLog.info("Execution got ID: {}", instanceId);

		// Verify

		await().until(() -> {
			myJobCleanerService.runMaintenancePass();
			JobInstance instance = myJobCoordinator.getInstance(instanceId);
			return instance.getStatus();
		}, equalTo(StatusEnum.COMPLETED));

		runInTransaction(() -> {
			assertEquals(200, myResourceTableDao.count());
		});

		runInTransaction(() -> {
			JobInstance instance = myJobCoordinator.getInstance(instanceId);
			ourLog.info("Instance details:\n{}", JsonUtil.serialize(instance, true));
			assertEquals(0, instance.getErrorCount());
			assertNotNull(instance.getCreateTime());
			assertNotNull(instance.getStartTime());
			assertNotNull(instance.getEndTime());
			assertEquals(200, instance.getCombinedRecordsProcessed());
			assertThat(instance.getCombinedRecordsProcessedPerSecond(), greaterThan(5.0));
		});
	}

	@Test
	public void testRunBulkImport_StorageFailure() {
		// Setup

		int fileCount = 3;
		List<String> indexes = addFiles(fileCount);

		BulkImportJobParameters parameters = new BulkImportJobParameters();
		for (String next : indexes) {
			String url = myHttpServletExtension.getBaseUrl() + "/download?index=" + next;
			parameters.addNdJsonUrl(url);
		}

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(parameters);

		IAnonymousInterceptor anonymousInterceptor = (thePointcut, theArgs) -> {
			throw new NullPointerException("This is an exception");
		};
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, anonymousInterceptor);
		try {

			// Execute

			String instanceId = myJobCoordinator.startInstance(request);
			assertThat(instanceId, not(blankOrNullString()));
			ourLog.info("Execution got ID: {}", instanceId);

			// Verify

			await().until(() -> {
				myJobCleanerService.runMaintenancePass();
				JobInstance instance = myJobCoordinator.getInstance(instanceId);
				return instance.getStatus();
			}, equalTo(StatusEnum.ERRORED));

			String storageDescription = runInTransaction(() -> {
				assertEquals(0, myResourceTableDao.count());
				String storage = myJobInstanceRepository
					.findAll()
					.stream()
					.map(t -> "\n * " + t.toString())
					.collect(Collectors.joining(""));
				storage += myWorkChunkRepository
					.findAll()
					.stream()
					.map(t -> "\n * " + t.toString())
					.collect(Collectors.joining(""));
				ourLog.info("Stored entities:{}", storage);
				return storage;
			});

			await().until(() -> {
				myJobCleanerService.runMaintenancePass();
				JobInstance instance = myJobCoordinator.getInstance(instanceId);
				return instance.getErrorCount();
			}, equalTo(3));

			runInTransaction(() -> {
				JobInstance instance = myJobCoordinator.getInstance(instanceId);
				ourLog.info("Instance details:\n{}", JsonUtil.serialize(instance, true));
				assertEquals(3, instance.getErrorCount(), storageDescription);
				assertNotNull(instance.getCreateTime());
				assertNotNull(instance.getStartTime());
				assertNull(instance.getEndTime());
				assertThat(instance.getErrorMessage(), containsString("NullPointerException: This is an exception"));
			});

		} finally {

			myInterceptorRegistry.unregisterInterceptor(anonymousInterceptor);

		}
	}


	@Test
	public void testRunBulkImport_InvalidFileContents() {
		// Setup

		int fileCount = 3;
		List<String> indexes = addFiles(fileCount - 1);
		indexes.add(myBulkImportFileServlet.registerFileByContents("{\"resourceType\":\"Foo\"}"));

		BulkImportJobParameters parameters = new BulkImportJobParameters();
		for (String next : indexes) {
			String url = myHttpServletExtension.getBaseUrl() + "/download?index=" + next;
			parameters.addNdJsonUrl(url);
		}

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(parameters);

		// Execute

		String instanceId = myJobCoordinator.startInstance(request);
		assertThat(instanceId, not(blankOrNullString()));
		ourLog.info("Execution got ID: {}", instanceId);

		// Verify

		await().until(() -> {
			myJobCleanerService.runMaintenancePass();
			JobInstance instance = myJobCoordinator.getInstance(instanceId);
			return instance.getStatus();
		}, equalTo(StatusEnum.FAILED));

		JobInstance instance = myJobCoordinator.getInstance(instanceId);
		ourLog.info("Instance details:\n{}", JsonUtil.serialize(instance, true));
		assertEquals(1, instance.getErrorCount());
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertNotNull(instance.getCreateTime());
		assertNotNull(instance.getStartTime());
		assertNotNull(instance.getEndTime());
		assertThat(instance.getErrorMessage(), containsString("Unknown resource name \"Foo\""));
	}


	@Test
	public void testRunBulkImport_UnknownTargetFile() {
		// Setup

		BulkImportJobParameters parameters = new BulkImportJobParameters();
		String url = myHttpServletExtension.getBaseUrl() + "/download?index=FOO";
		parameters.addNdJsonUrl(url);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(parameters);

		IAnonymousInterceptor anonymousInterceptor = (thePointcut, theArgs) -> {
			throw new NullPointerException("This is an exception");
		};
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, anonymousInterceptor);
		try {

			// Execute

			String instanceId = myJobCoordinator.startInstance(request);
			assertThat(instanceId, not(blankOrNullString()));
			ourLog.info("Execution got ID: {}", instanceId);

			// Verify

			await().until(() -> {
				myJobCleanerService.runMaintenancePass();
				JobInstance instance = myJobCoordinator.getInstance(instanceId);
				return instance.getStatus();
			}, equalTo(StatusEnum.FAILED));

			runInTransaction(() -> {
				JobInstance instance = myJobCoordinator.getInstance(instanceId);
				ourLog.info("Instance details:\n{}", JsonUtil.serialize(instance, true));
				assertEquals(1, instance.getErrorCount());
				assertNotNull(instance.getCreateTime());
				assertNotNull(instance.getStartTime());
				assertNotNull(instance.getEndTime());
				assertThat(instance.getErrorMessage(), containsString("Received HTTP 404 from URL: http://"));
			});

		} finally {

			myInterceptorRegistry.unregisterInterceptor(anonymousInterceptor);

		}
	}

	@Test
	public void testStartInvalidJob_NoParameters() {
		// Setup

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);

		// Execute

		try {
			myJobCoordinator.startInstance(request);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertEquals("HAPI-2065: No parameters supplied", e.getMessage());

		}
	}

	@Test
	public void testStartInvalidJob_NoUrls() {
		// Setup

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(new BulkImportJobParameters());

		// Execute

		try {
			myJobCoordinator.startInstance(request);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			String expected = """
				HAPI-2039: Failed to validate parameters for job of type BULK_IMPORT_PULL:\s
				 * myNdJsonUrls - At least one NDJSON URL must be provided""";
			assertEquals(expected, e.getMessage());

		}
	}

	@Test
	public void testStartInvalidJob_InvalidUrls() {
		// Setup

		BulkImportJobParameters parameters = new BulkImportJobParameters();
		parameters.addNdJsonUrl("foo");

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(parameters);

		// Execute

		try {
			myJobCoordinator.startInstance(request);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			String expected = """
				HAPI-2039: Failed to validate parameters for job of type BULK_IMPORT_PULL:\s
				 * myNdJsonUrls[0].<list element> - Must be a valid URL""";
			assertEquals(expected, e.getMessage());

		}
	}

	private List<String> addFiles(int fileCount) {
		List<String> retVal = new ArrayList<>();
		for (int i = 0; i < fileCount; i++) {
			StringBuilder builder = new StringBuilder();

			Patient patient = new Patient();
			patient.setId("Patient/P" + i);
			patient.setActive(true);
			builder.append(myFhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(patient));
			builder.append("\n");

			Observation observation = new Observation();
			observation.setId("Observation/O" + i);
			observation.getSubject().setReference("Patient/P" + i);
			builder.append(myFhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(observation));
			builder.append("\n");
			builder.append("\n");

			String index = myBulkImportFileServlet.registerFileByContents(builder.toString());
			retVal.add(index);
		}
		return retVal;
	}
}
