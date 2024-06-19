package ca.uhn.fhir.jpa.bulk.imprt.svc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.ActivateJobResult;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ExecutorChannelInterceptor;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.batch2.config.BaseBatch2Config.CHANNEL_NAME;
import static ca.uhn.fhir.batch2.jobs.importpull.BulkImportPullConfig.BULK_IMPORT_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BulkDataImportR4Test extends BaseJpaR4Test implements ITestDataBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataImportR4Test.class);

	@Autowired
	private IBulkDataImportSvc mySvc;

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	@Autowired
	private JobDefinitionRegistry myJobDefinitionRegistry;

	@Autowired
	private IChannelFactory myChannelFactory;

	private LinkedBlockingChannel myWorkChannel;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myWorkChannel = (LinkedBlockingChannel) myChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, new ChannelConsumerSettings());
	}

	@AfterEach
	@Override
	public void afterResetInterceptors() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> {
			boolean response = t instanceof MyFailAfterThreeCreatesInterceptor;
			ourLog.info("Interceptor {} - {}", t, response);
			return response;
		});
		super.afterResetInterceptors();
	}

	/**
	 * Sets up a message retry interceptor for when errors are encountered
	 */
	private void setupRetryFailures() {
		myWorkChannel.addInterceptor(new ExecutorChannelInterceptor() {
			@Override
			public void afterMessageHandled(@Nonnull Message<?> message, @Nonnull MessageChannel channel, @Nonnull MessageHandler handler, Exception ex) {
				if (ex != null) {
					ourLog.info("Work channel received exception {}", ex.getMessage());
					channel.send(message);
				}
			}
		});
	}

	@Order(-1)
	@Test
	public void testFlow_ErrorDuringWrite() {
		myInterceptorRegistry.registerInterceptor(new MyFailAfterThreeCreatesInterceptor());

		int transactionsPerFile = 5;
		int fileCount = 5;
		List<BulkImportJobFileJson> files = createInputFiles(transactionsPerFile, fileCount);

		try {
			setupRetryFailures();

			BulkImportJobJson job = new BulkImportJobJson();
			job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
			job.setJobDescription("This is the job description");
			job.setBatchSize(3);
			String jobId = mySvc.createNewJob(job, files);
			mySvc.markJobAsReadyForActivation(jobId);

			ActivateJobResult activateJobOutcome = mySvc.activateNextReadyJob();
			assertTrue(activateJobOutcome.isActivated);

			JobInstance instance = myBatch2JobHelper.awaitJobHasStatus(activateJobOutcome.jobId,
				60,
				StatusEnum.FAILED);

			HashSet<StatusEnum> failed = new HashSet<>();
			failed.add(StatusEnum.FAILED);
			failed.add(StatusEnum.ERRORED);
			assertThat(failed.contains(instance.getStatus())).as(instance.getStatus() + " is the actual status").isTrue();
			String errorMsg = instance.getErrorMessage();
			assertThat(errorMsg).contains("Too many errors");
		} finally {
			myWorkChannel.clearInterceptorsForUnitTest();
		}
	}

	@Order(0)
	@Test
	public void testFlow_TransactionRows() {
		int transactionsPerFile = 10;
		int fileCount = 10;
		List<BulkImportJobFileJson> files = createInputFiles(transactionsPerFile, fileCount);

		BulkImportJobJson job = new BulkImportJobJson();
		job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		job.setJobDescription("testFlow_TransactionRows");
		job.setBatchSize(3);
		String jobId = mySvc.createNewJob(job, files);
		mySvc.markJobAsReadyForActivation(jobId);

		ActivateJobResult activateJobOutcome = mySvc.activateNextReadyJob();
		assertTrue(activateJobOutcome.isActivated);

		JobInstance instance = myBatch2JobHelper.awaitJobCompletion(activateJobOutcome.jobId, 60);
		assertNotNull(instance);
		assertEquals(StatusEnum.COMPLETED, instance.getStatus());

		IBundleProvider searchResults = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);
		assertEquals(transactionsPerFile * fileCount, searchResults.sizeOrThrowNpe());
	}

	@Order(1)
	@Test
	public void testFlow_WithTenantNamesInInput() {
		int transactionsPerFile = 5;
		int fileCount = 10;
		List<BulkImportJobFileJson> files = createInputFiles(transactionsPerFile, fileCount);
		for (int i = 0; i < fileCount; i++) {
			files.get(i).setTenantName("TENANT" + i);
		}

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, interceptor);
		try {
			BulkImportJobJson job = new BulkImportJobJson();
			job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
			job.setBatchSize(5);
			String jobId = mySvc.createNewJob(job, files);
			mySvc.markJobAsReadyForActivation(jobId);

			ActivateJobResult activateJobOutcome = mySvc.activateNextReadyJob();
			assertTrue(activateJobOutcome.isActivated);

			JobInstance instance = myBatch2JobHelper.awaitJobCompletion(activateJobOutcome.jobId);
			assertNotNull(instance);

			ArgumentCaptor<HookParams> paramsCaptor = ArgumentCaptor.forClass(HookParams.class);
			verify(interceptor, times(50)).invoke(any(), paramsCaptor.capture());
			List<String> tenantNames = paramsCaptor
				.getAllValues()
				.stream()
				.map(t -> t.get(RequestDetails.class).getTenantId())
				.distinct()
				.sorted()
				.collect(Collectors.toList());
			assertThat(tenantNames).containsExactlyInAnyOrder("TENANT0", "TENANT1", "TENANT2", "TENANT3", "TENANT4", "TENANT5", "TENANT6", "TENANT7", "TENANT8", "TENANT9");
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	@Nonnull
	private List<BulkImportJobFileJson> createInputFiles(int transactionsPerFile, int fileCount) {
		List<BulkImportJobFileJson> files = new ArrayList<>();
		int counter = 0;
		for (int fileIndex = 0; fileIndex < fileCount; fileIndex++) {
			StringBuilder fileContents = new StringBuilder();

			for (int transactionIdx = 0; transactionIdx < transactionsPerFile; transactionIdx++) {
				BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
				IBaseResource patient = buildPatient(withFamily("FAM " + fileIndex + " " + transactionIdx), withIdentifier(null, "patient" + counter++));
				bundleBuilder.addTransactionCreateEntry(patient);
				fileContents.append(myFhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(bundleBuilder.getBundle()));
				fileContents.append("\n");
			}

			BulkImportJobFileJson nextFile = new BulkImportJobFileJson();
			nextFile.setContents(fileContents.toString());
			nextFile.setDescription("File With Description " + fileIndex);
			files.add(nextFile);
		}
		return files;
	}

	@Test
	public void testJobsAreRegisteredWithJobRegistry() {
		Optional<JobDefinition<?>> jobDefinitionOp = myJobDefinitionRegistry.getLatestJobDefinition(BULK_IMPORT_JOB_NAME);

		assertThat(jobDefinitionOp).isPresent();
	}

	@Interceptor
	public static class MyFailAfterThreeCreatesInterceptor {

		public static final String ERROR_MESSAGE = "This is an error message";

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
		public void create(IBaseResource thePatient) {
			Patient patient = (Patient) thePatient;
			if (patient.getIdentifierFirstRep().getValue().equals("patient10")) {
				throw new InternalErrorException(ERROR_MESSAGE);
			}
		}
	}
}
