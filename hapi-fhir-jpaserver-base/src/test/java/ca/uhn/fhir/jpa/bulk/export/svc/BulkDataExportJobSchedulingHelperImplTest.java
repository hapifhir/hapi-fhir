package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.JsonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkDataExportJobSchedulingHelperImplTest {
	@Mock
	private DaoConfig myDaoConfig;

	@Mock
	private PlatformTransactionManager myTxManager;

	@Mock
	private TransactionTemplate myTxTemplate;

	@Mock
	private IJobPersistence myJpaJobPersistence;

	@Mock
	private BulkExportHelperService myBulkExportHelperSvc;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IFhirResourceDao<IBaseBinary> myBinaryDao;

	@Captor
	private ArgumentCaptor<Date> myCutoffCaptor;

	private BulkDataExportJobSchedulingHelperImpl myBulkDataExportJobSchedulingHelper;
	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@Test
	public void testPurgeExpiredFilesDisabledDoesNothing() {
		setupTestDisabled();

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		verify(myJpaJobPersistence, never()).fetchInstance(anyString());
		verify(myBulkExportHelperSvc, never()).toId(anyString());
		verify(myJpaJobPersistence, never()).deleteInstanceAndChunks(anyString());
	}

	@Test
	public void purgeExpiredFilesNothingToDeleteOneHourRetention() {
		final int expectedRetentionHours = 1;

		setupTestEnabled(expectedRetentionHours, List.of());

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();


		verify(myJpaJobPersistence, never()).fetchInstance(anyString());
		verify(myBulkExportHelperSvc, never()).toId(anyString());
		verify(myBinaryDao, never()).delete(any(IIdType.class), any(SystemRequestDetails.class));
		verify(myJpaJobPersistence, never()).deleteInstanceAndChunks(anyString());

		final Date cutoffDate = myCutoffCaptor.getValue();
		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.SECOND), DateUtils.truncate(cutoffDate, Calendar.SECOND));
	}

	@Test
	public void purgeExpiredFilesSingleJobSingleBinaryOneHourRetention_NULL_reportString() {
		final int expectedRetentionHours = 1;
		final int numBinariesPerJob = 1;
		final List<JobInstance> jobInstances = getJobInstances(numBinariesPerJob, StatusEnum.COMPLETED);

		jobInstances.get(0).setReport(null);

		setupTestEnabledNoBinaries(expectedRetentionHours, jobInstances);

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		for (JobInstance jobInstance : jobInstances) {
			verify(myJpaJobPersistence).fetchInstance(jobInstance.getInstanceId());
			verify(myBulkExportHelperSvc, never()).toId(anyString());
			verify(myBinaryDao, never()).delete(any(IIdType.class), any(SystemRequestDetails.class));
			verify(myJpaJobPersistence).deleteInstanceAndChunks(jobInstance.getInstanceId());
		}

		final Date cutoffDate = myCutoffCaptor.getValue();
		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.MINUTE), DateUtils.truncate(cutoffDate, Calendar.MINUTE));
	}

	@Test
	public void purgeExpiredFilesSingleJobSingleBinaryOneHourRetention_BAD_reportString() {
		final int expectedRetentionHours = 1;
		final int numBinariesPerJob = 1;
		final List<JobInstance> jobInstances = getJobInstances(numBinariesPerJob, StatusEnum.COMPLETED);

		jobInstances.get(0).setReport("{garbage}");

		setupTestEnabledNoBinaries(expectedRetentionHours, jobInstances);

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		for (JobInstance jobInstance : jobInstances) {
			verify(myJpaJobPersistence).fetchInstance(jobInstance.getInstanceId());
			verify(myBulkExportHelperSvc, never()).toId(anyString());
			verify(myBinaryDao, never()).delete(any(IIdType.class), any(SystemRequestDetails.class));
			verify(myJpaJobPersistence).deleteInstanceAndChunks(jobInstance.getInstanceId());
		}

		final Date cutoffDate = myCutoffCaptor.getValue();
		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.MINUTE), DateUtils.truncate(cutoffDate, Calendar.MINUTE));
	}

	@Test
	public void purgeExpiredFilesSingleJobSingleBinaryOneHourRetention() {
		final int expectedRetentionHours = 1;
		final int numBinariesPerJob = 1;
		final List<JobInstance> jobInstances = getJobInstances(numBinariesPerJob, StatusEnum.COMPLETED);

		setupTestEnabled(expectedRetentionHours, jobInstances);

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		for (JobInstance jobInstance : jobInstances) {
			verify(myJpaJobPersistence).fetchInstance(jobInstance.getInstanceId());
			for (int index = 0; index < numBinariesPerJob; index++) {
				verify(myBulkExportHelperSvc).toId(jobInstance.getInstanceId() + "-binary-" + index);
				verify(myBinaryDao).delete(eq(toId(jobInstance.getInstanceId() + "-binary-" + index)), any(SystemRequestDetails.class));
			}
			verify(myJpaJobPersistence).deleteInstanceAndChunks(jobInstance.getInstanceId());
		}

		final Date cutoffDate = myCutoffCaptor.getValue();
		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.MINUTE), DateUtils.truncate(cutoffDate, Calendar.MINUTE));
	}

	@Test
	public void purgeExpiredFilesSingleJobSingleBinaryOneHourRetentionStatusFailed() {
		final int expectedRetentionHours = 1;
		final int numBinariesPerJob = 1;
		final List<JobInstance> jobInstances = getJobInstances(numBinariesPerJob, StatusEnum.COMPLETED);

		setupTestEnabled(expectedRetentionHours, jobInstances);

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		for (JobInstance jobInstance : jobInstances) {
			verify(myJpaJobPersistence).fetchInstance(jobInstance.getInstanceId());
			for (int index = 0; index < numBinariesPerJob; index++) {
				verify(myBulkExportHelperSvc).toId(jobInstance.getInstanceId() + "-binary-" + index);
				verify(myBinaryDao).delete(eq(toId(jobInstance.getInstanceId() + "-binary-" + index)), any(SystemRequestDetails.class));
			}
			verify(myJpaJobPersistence).deleteInstanceAndChunks(jobInstance.getInstanceId());
		}

		final Date cutoffDate = myCutoffCaptor.getValue();
		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.MINUTE), DateUtils.truncate(cutoffDate, Calendar.MINUTE));
	}

	@Test
	public void purgeExpiredFilesSingleJobSingleBinaryTwoHourRetention() {
		final int expectedRetentionHours = 2;
		final int numBinariesPerJob = 1;
		final List<JobInstance> jobInstances = getJobInstances(numBinariesPerJob, StatusEnum.COMPLETED);

		setupTestEnabled(expectedRetentionHours, jobInstances);

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		for (JobInstance jobInstance : jobInstances) {
			verify(myJpaJobPersistence).fetchInstance(jobInstance.getInstanceId());
			for (int index = 0; index < numBinariesPerJob; index++) {
				verify(myBulkExportHelperSvc).toId(jobInstance.getInstanceId() + "-binary-" + index);
				verify(myBinaryDao).delete(eq(toId(jobInstance.getInstanceId() + "-binary-" + index)), any(SystemRequestDetails.class));
			}
			verify(myJpaJobPersistence).deleteInstanceAndChunks(jobInstance.getInstanceId());
		}

		final Date cutoffDate = myCutoffCaptor.getValue();

		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.MINUTE), DateUtils.truncate(cutoffDate, Calendar.MINUTE));
	}

	@Test
	public void purgeExpiredFilesMultipleJobsMultipleBinariesTwoHourRetention() {
		final int expectedRetentionHours = 2;
		final int numBinariesPerJob = 3;
		final List<JobInstance> jobInstances = getJobInstances( numBinariesPerJob, StatusEnum.COMPLETED, StatusEnum.COMPLETED, StatusEnum.COMPLETED);

		setupTestEnabled(expectedRetentionHours, jobInstances);

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		for (JobInstance jobInstance : jobInstances) {
			verify(myJpaJobPersistence).fetchInstance(jobInstance.getInstanceId());
			for (int index = 0; index < numBinariesPerJob; index++) {
				verify(myBulkExportHelperSvc).toId(jobInstance.getInstanceId() + "-binary-" + index);
				verify(myBinaryDao).delete(eq(toId(jobInstance.getInstanceId() + "-binary-" + index)), any(SystemRequestDetails.class));
			}
			verify(myJpaJobPersistence).deleteInstanceAndChunks(jobInstance.getInstanceId());
		}

		final Date cutoffDate = myCutoffCaptor.getValue();

		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.MINUTE), DateUtils.truncate(cutoffDate, Calendar.MINUTE));
	}

	@Test
	public void purgeExpiredFilesMultipleJobsMultipleBinariesTwoHourRetentionMixedStatuses() {
		final int expectedRetentionHours = 2;
		final int numBinariesPerJob = 3;
		final List<JobInstance> jobInstances = getJobInstances( numBinariesPerJob, StatusEnum.COMPLETED, StatusEnum.FAILED, StatusEnum.COMPLETED);

		setupTestEnabled(expectedRetentionHours, jobInstances);

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		for (JobInstance jobInstance : jobInstances) {
			verify(myJpaJobPersistence).fetchInstance(jobInstance.getInstanceId());
			if (StatusEnum.FAILED != jobInstance.getStatus()) {
				for (int index = 0; index < numBinariesPerJob; index++) {
					verify(myBulkExportHelperSvc).toId(jobInstance.getInstanceId() + "-binary-" + index);
					verify(myBinaryDao).delete(eq(toId(jobInstance.getInstanceId() + "-binary-" + index)), any(SystemRequestDetails.class));
				}

				verify(myJpaJobPersistence).deleteInstanceAndChunks(jobInstance.getInstanceId());
			}
		}

		final Date cutoffDate = myCutoffCaptor.getValue();

		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.MINUTE), DateUtils.truncate(cutoffDate, Calendar.MINUTE));
	}

	@Nonnull
	private static List<JobInstance> getJobInstances(int theNumBinaries, StatusEnum... theStatusEnums) {
		return IntStream.range(0, theStatusEnums.length)
			.mapToObj(index -> Pair.of(index, theStatusEnums[index]))
			.map(pair -> {
				final JobInstance jobInstance = new JobInstance();
				final StatusEnum status = pair.getSecond();
				final String instanceId = status.name() + pair.getFirst();
				jobInstance.setInstanceId(instanceId);
				jobInstance.setReport(serialize(getBulkExportJobResults(instanceId, theNumBinaries)));
				jobInstance.setStatus(status);
				return jobInstance;
		}).toList();
	}

	private static String serialize(BulkExportJobResults theBulkExportJobResults) {
		return JsonUtil.serialize(theBulkExportJobResults);
	}

	@Nonnull
	private static BulkExportJobResults getBulkExportJobResults(String theInstanceId, int theNumBinaries) {
		final BulkExportJobResults bulkExportJobResults = new BulkExportJobResults();
		bulkExportJobResults.setResourceTypeToBinaryIds(Map.of("Patient",
			IntStream.range(0, theNumBinaries)
				.mapToObj(theInt -> theInstanceId + "-binary-" + theInt)
				.toList()));
		return bulkExportJobResults;
	}

	@Nonnull
	private Date computeDateFromConfig(int theExpectedRetentionHours) {
		return Date.from(LocalDateTime.now()
			.minusHours(theExpectedRetentionHours)
			.atZone(ZoneId.systemDefault())
			.toInstant());
	}

	private void setupTestDisabled() {
		setupTest(false, -1, List.of(), false);
	}

	private void setupTestEnabled(int theRetentionHours, List<JobInstance> theJobInstances) {
		setupTest(true, theRetentionHours, theJobInstances, true);
	}

	private void setupTestEnabledNoBinaries(int theRetentionHours, List<JobInstance> theJobInstances) {
		setupTest(true, theRetentionHours, theJobInstances, false);
	}

	private void setupTest(boolean theIsEnabled, int theRetentionHours, List<JobInstance> theJobInstances, boolean theIsEnableBinaryMocks) {
		myBulkDataExportJobSchedulingHelper = new BulkDataExportJobSchedulingHelperImpl(myDaoRegistry, myTxManager, myDaoConfig, myBulkExportHelperSvc, myJpaJobPersistence, myTxTemplate);

		when(myDaoConfig.isEnableTaskBulkExportJobExecution()).thenReturn(theIsEnabled);

		if (!theIsEnabled) {
			return;
		}


		final Answer<List<JobInstance>> fetchInstancesAnswer = theInvocationOnMock -> {
			final TransactionCallback<List<JobInstance>> transactionCallback = theInvocationOnMock.getArgument(0);
			return transactionCallback.doInTransaction(null);
		};

		final Answer<Void> purgeExpiredJobsAnswer = theInvocationOnMock -> {
			final TransactionCallback<Optional<JobInstance>> transactionCallback = theInvocationOnMock.getArgument(0);
			transactionCallback.doInTransaction(null);
			return null;
		};

		when(myJpaJobPersistence.fetchInstances(eq(Batch2JobDefinitionConstants.BULK_EXPORT),
			eq(StatusEnum.getEndedStatuses()),
			myCutoffCaptor.capture(),
			any(PageRequest.class)))
				.thenReturn(theJobInstances);

		when(myTxTemplate.execute(any()))
			.thenAnswer(fetchInstancesAnswer).thenAnswer(purgeExpiredJobsAnswer);

		when(myDaoConfig.getBulkExportFileRetentionPeriodHours())
			.thenReturn(theRetentionHours);

		if (theJobInstances.isEmpty()) {
			return;
		}

		OngoingStubbing<Optional<JobInstance>> when = when(myJpaJobPersistence.fetchInstance(anyString()));

		for (JobInstance jobInstance : theJobInstances) {
			when = when.thenReturn(Optional.of(jobInstance));
		}

		if (!theIsEnableBinaryMocks) {
			return;
		}

		when(myBulkExportHelperSvc.toId(anyString()))
			.thenAnswer(theInvocationOnMock -> toId(theInvocationOnMock.getArgument(0)));

		when(myDaoRegistry.getResourceDao(Binary.class.getSimpleName())).thenReturn(myBinaryDao);
	}

	private IIdType toId(String theResourceId) {
		final IIdType retVal = myFhirContext.getVersion().newIdType();
		retVal.setValue(theResourceId);
		return retVal;
	}
}
