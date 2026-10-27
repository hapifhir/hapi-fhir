package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc.MAX_DELETE_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by claude-fable-5
@ExtendWith(MockitoExtension.class)
class ResourceSearchUrlSvcTest {

	@RegisterExtension
	private final LogbackTestExtension myLogCapture = new LogbackTestExtension(ResourceSearchUrlSvc.class, Level.DEBUG);

	private static final int PAGE_SIZE = 1000;

	@Mock
	private IResourceSearchUrlDao myResourceSearchUrlDao;

	private final JpaStorageSettings myStorageSettings = new JpaStorageSettings();

	private ResourceSearchUrlSvc mySvc;

	@BeforeEach
	void beforeEach() {
		mySvc = new ResourceSearchUrlSvc(
				null, myResourceSearchUrlDao, null, null, null, myStorageSettings, new NoopTransactionManager());
	}

	@Test
	void deleteEntriesOlderThan_staleSetNeverDrains_stopsAtMaxPages() {
		// simulate a pathological non-draining table: every page deletes a full batch
		AtomicInteger deleteCount = new AtomicInteger();
		when(myResourceSearchUrlDao.deleteStaleEntries(any(Date.class), anyInt())).thenAnswer(invocation -> {
			if (deleteCount.incrementAndGet() > MAX_DELETE_PAGES) {
				throw new IllegalStateException("Loop did not stop at MAX_DELETE_PAGES");
			}
			return PAGE_SIZE;
		});

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao, times(MAX_DELETE_PAGES)).deleteStaleEntries(any(Date.class), anyInt());
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage("Reached the maximum of");
	}

	@Test
	void deleteEntriesOlderThan_staleSetDrains_deletesAllPagesWithoutWarning() {
		when(myResourceSearchUrlDao.deleteStaleEntries(any(Date.class), eq(PAGE_SIZE)))
				.thenReturn(PAGE_SIZE)
				.thenReturn(3);

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao, times(2)).deleteStaleEntries(any(Date.class), eq(PAGE_SIZE));
		assertThat(myLogCapture.getLogEvents(theEvent -> theEvent.getLevel() == Level.WARN)).isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture)
				.hasDebugMessage("Deleted page of")
				.hasInfoMessage("Deleted 1003 SearchUrls");
	}

	@Test
	void deleteEntriesOlderThan_nothingStale_logsAtDebugOnly() {
		when(myResourceSearchUrlDao.deleteStaleEntries(any(Date.class), anyInt())).thenReturn(0);

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao).deleteStaleEntries(any(Date.class), anyInt());
		assertThat(myLogCapture.getLogEvents(theEvent -> theEvent.getLevel() == Level.INFO))
				.isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasDebugMessage("Deleted 0 SearchUrls");
	}

	@Test
	void deleteEntriesOlderThan_batchSizeConfigured_usesItAsPageSize() {
		myStorageSettings.setSearchUrlCleanupBatchSize(600);
		when(myResourceSearchUrlDao.deleteStaleEntries(any(Date.class), eq(600)))
				.thenReturn(600)
				.thenReturn(1);

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao, times(2)).deleteStaleEntries(any(Date.class), eq(600));
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasInfoMessage("Deleted 601 SearchUrls");
	}

	private static class NoopTransactionManager extends AbstractPlatformTransactionManager {
		@Override
		protected Object doGetTransaction() {
			return new Object();
		}

		@Override
		protected void doBegin(Object theTransaction, TransactionDefinition theDefinition) {
			// nothing to begin
		}

		@Override
		protected void doCommit(DefaultTransactionStatus theStatus) {
			// nothing to commit
		}

		@Override
		protected void doRollback(DefaultTransactionStatus theStatus) {
			// nothing to roll back
		}
	}
}
