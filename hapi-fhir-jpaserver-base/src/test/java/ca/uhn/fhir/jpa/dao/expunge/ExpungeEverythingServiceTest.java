package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by claude-opus-4-6
@ExtendWith(MockitoExtension.class)
class ExpungeEverythingServiceTest {

	@InjectMocks
	private ExpungeEverythingService myExpungeEverythingService;

	@Mock
	private HapiTransactionService myTxService;

	@Mock
	private IJobMaintenanceService myJobMaintenanceService;

	@Test
	void testExpungeBatch2_acquiresAndReleasesMaintenanceHold() throws Exception {
		// Setup
		Closeable mockHold = mock(Closeable.class);
		when(myJobMaintenanceService.holdMaintenanceForExpunge()).thenReturn(mockHold);

		IHapiTransactionService.IExecutionBuilder mockBuilder = mock(IHapiTransactionService.IExecutionBuilder.class);
		when(myTxService.withRequest(any())).thenReturn(mockBuilder);
		when(mockBuilder.withPropagation(any())).thenReturn(mockBuilder);
		when(mockBuilder.withRequestPartitionId(any())).thenReturn(mockBuilder);
		when(mockBuilder.execute(any(Callable.class))).thenReturn(5).thenReturn(0);

		// Execute
		int result = myExpungeEverythingService.expungeBatch2Entities(null, RequestPartitionId.allPartitions());

		// Verify hold was acquired and released
		verify(myJobMaintenanceService).holdMaintenanceForExpunge();
		verify(mockHold).close();
		assertThat(result).isEqualTo(5);
	}

	@Test
	void testExpungeBatch2_retriesOnFkViolationThenSucceeds() throws Exception {
		// Setup: first call throws FK violation, second call succeeds, third returns 0
		Closeable mockHold = mock(Closeable.class);
		when(myJobMaintenanceService.holdMaintenanceForExpunge()).thenReturn(mockHold);

		IHapiTransactionService.IExecutionBuilder mockBuilder = mock(IHapiTransactionService.IExecutionBuilder.class);
		when(myTxService.withRequest(any())).thenReturn(mockBuilder);
		when(mockBuilder.withPropagation(any())).thenReturn(mockBuilder);
		when(mockBuilder.withRequestPartitionId(any())).thenReturn(mockBuilder);
		when(mockBuilder.execute(any(Callable.class)))
				.thenThrow(new ResourceVersionConflictException("FK violation"))
				.thenReturn(5)
				.thenReturn(0);

		// Execute
		int result = myExpungeEverythingService.expungeBatch2Entities(null, RequestPartitionId.allPartitions());

		// Verify: retried after FK violation, then succeeded
		assertThat(result).isEqualTo(5);
		verify(mockHold).close();
	}

	@Test
	void testExpungeBatch2_throwsAfterMaxRetries() throws Exception {
		// Setup: always throw FK violation
		Closeable mockHold = mock(Closeable.class);
		when(myJobMaintenanceService.holdMaintenanceForExpunge()).thenReturn(mockHold);

		IHapiTransactionService.IExecutionBuilder mockBuilder = mock(IHapiTransactionService.IExecutionBuilder.class);
		when(myTxService.withRequest(any())).thenReturn(mockBuilder);
		when(mockBuilder.withPropagation(any())).thenReturn(mockBuilder);
		when(mockBuilder.withRequestPartitionId(any())).thenReturn(mockBuilder);
		when(mockBuilder.execute(any(Callable.class)))
				.thenThrow(new ResourceVersionConflictException("FK violation"));

		// Execute & Verify: throws after exhausting retries
		assertThatThrownBy(
						() -> myExpungeEverythingService.expungeBatch2Entities(null, RequestPartitionId.allPartitions()))
				.isInstanceOf(ResourceVersionConflictException.class);

		// Hold is still released even on exception
		verify(mockHold).close();
	}
}
