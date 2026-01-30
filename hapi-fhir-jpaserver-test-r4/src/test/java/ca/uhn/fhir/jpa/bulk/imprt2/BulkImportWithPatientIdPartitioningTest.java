package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportAppCtx;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportFileServlet;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test class has just one test. It can potentially be moved
 */
public class BulkImportWithPatientIdPartitioningTest extends BaseJpaR4Test {
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final BulkImportFileServlet ourBulkImportFileServlet = new BulkImportFileServlet(USERNAME, PASSWORD);

	@RegisterExtension
	public static HttpServletExtension myHttpServletExtension = new HttpServletExtension().withServlet(ourBulkImportFileServlet);

	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private IJobMaintenanceService myJobCleanerService;

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	private PatientIdPartitionInterceptor myPatientIdPartitionInterceptor;

	@BeforeEach
	@Override
	public void before() {
		myPatientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
		myInterceptorRegistry.registerInterceptor(myPatientIdPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(myPatientIdPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
	}

	@Test
	public void testBulkImport_withOneResource_successful() {
		// Setup

		Patient p = new Patient().setActive(true);
		p.setId("P1");
		String fileContents = getFhirContext().newJsonParser().encodeResourceToString(p);
		String id = ourBulkImportFileServlet.registerFileByContents(fileContents);

		BulkImportJobParameters parameters = new BulkImportJobParameters();
		parameters.setHttpBasicCredentials(USERNAME + ":" + PASSWORD);
		parameters.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + id);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(parameters);

		// Execute

		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		String instanceId = startResponse.getInstanceId();
		assertThat(instanceId).isNotBlank();
		ourLog.info("Execution got ID: {}", instanceId);

		// Verify

		await().atMost(120, TimeUnit.SECONDS).until(() -> {
			myJobCleanerService.runMaintenancePass();
			JobInstance instance = myJobCoordinator.getInstance(instanceId);
			return instance.getStatus() == StatusEnum.COMPLETED;
		});

		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
		});

		runInTransaction(() -> {
			JobInstance instance = myJobCoordinator.getInstance(instanceId);
			ourLog.info("Instance details:\n{}", JsonUtil.serialize(instance, true));
			assertEquals(0, instance.getErrorCount());
		});
	}
}
