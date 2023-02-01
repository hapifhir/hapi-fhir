package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkDataExportJobSchedulingHelperImplTest {
	@Mock
	private DaoConfig myDaoConfig;

	@Mock
	private TransactionTemplate myTxTemplate;

	@Mock
	private IBulkExportJobDao myBulkExportJobDao;

	private final BulkDataExportJobSchedulingHelperImpl myBulkDataExportJobSchedulingHelper = new BulkDataExportJobSchedulingHelperImpl();

	@BeforeEach
	public void setUp() {
		when(myDaoConfig.isEnableTaskBulkExportJobExecution()).thenReturn(true);

		// TODO:  need 2 answers 1. Optional<BulkExportJobEntity> 2. null
		final Answer<Optional<BulkExportJobEntity>> answer = theInvocationOnMock -> {
			final TransactionCallback<Optional<BulkExportJobEntity>> transactionCallback = theInvocationOnMock.getArgument(0);
			return transactionCallback.doInTransaction(null);
		};

		final Answer<Void> answer2 = theInvocationOnMock -> {
			final TransactionCallback<Optional<BulkExportJobEntity>> transactionCallback = theInvocationOnMock.getArgument(0);
			transactionCallback.doInTransaction(null);
			return null;
		};

		// TODO: mock myBulkExportJobDao.getOne(jobToDelete.get().getId())

		when(myTxTemplate.execute(any()))
			.thenAnswer(answer).thenAnswer(answer2);
		when(myBulkExportJobDao.findNotRunningByExpiry(any(Pageable.class), any(Date.class)))
			.thenReturn(getBulkExportSlice());

		myBulkDataExportJobSchedulingHelper.setDaoConfig(myDaoConfig);
		myBulkDataExportJobSchedulingHelper.setTxTemplate(myTxTemplate);
		myBulkDataExportJobSchedulingHelper.setBulkExportJobDao(myBulkExportJobDao);
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
	public void purgeExpiredFiles() {
		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();
	}

}
