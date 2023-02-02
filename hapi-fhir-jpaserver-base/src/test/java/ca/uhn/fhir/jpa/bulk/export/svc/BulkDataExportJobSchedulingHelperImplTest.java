package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	private TransactionTemplate myTxTemplate;

	// TODO: get rid of this:
	@Mock
	private IBulkExportJobDao myBulkExportJobDao;

	@Mock
	private IJobPersistence myJpaJobPersistence;

	@Mock
	private BulkExportHelperService myBulkExportHelperSvc;

	@Captor
	private ArgumentCaptor<Date> myCutoffCaptor;

	private final BulkDataExportJobSchedulingHelperImpl myBulkDataExportJobSchedulingHelper = new BulkDataExportJobSchedulingHelperImpl();

	@BeforeEach
	public void setUp() {
		when(myDaoConfig.isEnableTaskBulkExportJobExecution()).thenReturn(true);

		// TODO:  need 2 answers 1. Optional<BulkExportJobEntity> 2. null
		final Answer<List<JobInstance>> fetchInstancesAnswer = theInvocationOnMock -> {
			final TransactionCallback<List<JobInstance>> transactionCallback = theInvocationOnMock.getArgument(0);
			return transactionCallback.doInTransaction(null);
		};

		final Answer<Void> purgeExpiredJobsAnswer = theInvocationOnMock -> {
			final TransactionCallback<Optional<BulkExportJobEntity>> transactionCallback = theInvocationOnMock.getArgument(0);
			transactionCallback.doInTransaction(null);
			return null;
		};

		// TODO: mock myBulkExportJobDao.getOne(jobToDelete.get().getId())
		when(myTxTemplate.execute(any()))
			.thenAnswer(fetchInstancesAnswer).thenAnswer(purgeExpiredJobsAnswer);

		myBulkDataExportJobSchedulingHelper.setDaoConfig(myDaoConfig);
		myBulkDataExportJobSchedulingHelper.setTxTemplate(myTxTemplate);
		myBulkDataExportJobSchedulingHelper.setJpaJobPersistence(myJpaJobPersistence);
		myBulkDataExportJobSchedulingHelper.setBulkExportHelperSvc(myBulkExportHelperSvc);
	}

	@Nonnull
	private static SliceImpl<BulkExportJobEntity> getBulkExportSlice() {
		return new SliceImpl<>(List.of(getJob()));
	}

	@Nonnull
	private static BulkExportJobEntity getJob() {
		final BulkExportJobEntity bulkExportJobEntity = new BulkExportJobEntity();

		final Collection<BulkExportCollectionEntity> collections = bulkExportJobEntity.getCollections();

		collections.add(getCollectionEntity());
		collections.add(getCollectionEntity());

		return bulkExportJobEntity;
	}

	@Nonnull
	private static BulkExportCollectionEntity getCollectionEntity() {
		return new BulkExportCollectionEntity();
	}

	@Test
	public void purgeExpiredFilesNothingToDelete() {
		final int expectedRetentionHours = 1;

		setupTest(expectedRetentionHours);

		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		final Date cutoffDate = myCutoffCaptor.getValue();

		// TODO:  hard-coded job instance ID(s) and verify these
		verify(myJpaJobPersistence, never()).fetchInstance(anyString());
		verify(myBulkExportHelperSvc, never()).toId(anyString());
		verify(myJpaJobPersistence, never()).deleteInstanceAndChunks(anyString());

		assertEquals(DateUtils.truncate(computeDateFromConfig(expectedRetentionHours), Calendar.SECOND), DateUtils.truncate(cutoffDate, Calendar.SECOND));
	}

	private Date computeDateFromConfig(int theExpectedRetentionHours) {
		return Date.from(LocalDateTime.now()
			.minusHours(theExpectedRetentionHours)
			.atZone(ZoneId.systemDefault())
			.toInstant());
	}

	private void setupTest(int theRetentionHours, JobInstance... theJobInstances) {
		when(myDaoConfig.getBulkExportFileRetentionPeriodHours()).thenReturn(theRetentionHours);

		when(myJpaJobPersistence.fetchInstances(eq(Batch2JobDefinitionConstants.BULK_EXPORT), eq(StatusEnum.getEndedStatuses()), myCutoffCaptor.capture(), eq(PageRequest.of(0, 100))))
			.thenReturn(Arrays.asList(theJobInstances));
	}
}
