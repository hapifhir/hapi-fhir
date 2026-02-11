package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Created by claude-opus-4-5
@ExtendWith(MockitoExtension.class)
class ExpungeEverythingServiceTest {

	@InjectMocks
	private ExpungeEverythingService myExpungeEverythingService;

	@Mock
	private HapiTransactionService myTxService;

	@Test
	void testExpungeBatch2Entities_retriesOnResourceVersionConflict() throws Exception {
		// Setup: first call throws ResourceVersionConflictException, second call returns 5, third returns 0
		AtomicInteger callCount = new AtomicInteger(0);
		IHapiTransactionService.IExecutionBuilder mockBuilder = mock(IHapiTransactionService.IExecutionBuilder.class);

		when(myTxService.withRequest(any())).thenReturn(mockBuilder);
		when(mockBuilder.withPropagation(any())).thenReturn(mockBuilder);
		when(mockBuilder.withRequestPartitionId(any())).thenReturn(mockBuilder);
		when(mockBuilder.execute(any(Callable.class))).thenAnswer(invocation -> {
			int call = callCount.incrementAndGet();
			if (call == 1) {
				throw new ResourceVersionConflictException("FK constraint violation");
			} else if (call == 2) {
				return 5;
			} else {
				return 0;
			}
		});

		// Execute
		int result = invokeExpungeBatch2EntitiesInSingleTransaction(null, RequestPartitionId.allPartitions());

		// Verify: retried once, then succeeded with 5 entities deleted
		assertThat(callCount.get()).isEqualTo(3);
		assertThat(result).isEqualTo(5);
	}

	@Test
	void testExpungeBatch2Entities_rethrowsAfterMaxRetries() {
		// Setup: always throw ResourceVersionConflictException
		IHapiTransactionService.IExecutionBuilder mockBuilder = mock(IHapiTransactionService.IExecutionBuilder.class);

		when(myTxService.withRequest(any())).thenReturn(mockBuilder);
		when(mockBuilder.withPropagation(any())).thenReturn(mockBuilder);
		when(mockBuilder.withRequestPartitionId(any())).thenReturn(mockBuilder);
		when(mockBuilder.execute(any(Callable.class)))
				.thenThrow(new ResourceVersionConflictException("FK constraint violation"));

		// Execute & Verify: should rethrow after 11 attempts (1 initial + 10 retries)
		assertThatThrownBy(() -> invokeExpungeBatch2EntitiesInSingleTransaction(null, RequestPartitionId.allPartitions()))
				.isInstanceOf(ResourceVersionConflictException.class)
				.hasMessageContaining("FK constraint violation");
	}

	@Test
	void testExpungeBatch2Entities_resetsRetryCounterOnSuccess() throws Exception {
		// Setup: fail once, succeed twice (returning 3 then 0), fail once, succeed twice (returning 2 then 0)
		AtomicInteger callCount = new AtomicInteger(0);
		IHapiTransactionService.IExecutionBuilder mockBuilder = mock(IHapiTransactionService.IExecutionBuilder.class);

		when(myTxService.withRequest(any())).thenReturn(mockBuilder);
		when(mockBuilder.withPropagation(any())).thenReturn(mockBuilder);
		when(mockBuilder.withRequestPartitionId(any())).thenReturn(mockBuilder);
		when(mockBuilder.execute(any(Callable.class))).thenAnswer(invocation -> {
			int call = callCount.incrementAndGet();
			switch (call) {
				case 1:
					throw new ResourceVersionConflictException("conflict 1");
				case 2:
					return 3;
				case 3:
					throw new ResourceVersionConflictException("conflict 2");
				case 4:
					return 2;
				case 5:
					return 0;
				default:
					return 0;
			}
		});

		// Execute
		int result = invokeExpungeBatch2EntitiesInSingleTransaction(null, RequestPartitionId.allPartitions());

		// Verify: total of 5 calls, 5 entities deleted (3 + 2)
		assertThat(callCount.get()).isEqualTo(5);
		assertThat(result).isEqualTo(5);
	}

	@SuppressWarnings("unchecked")
	private int invokeExpungeBatch2EntitiesInSingleTransaction(
			@SuppressWarnings("SameParameterValue") Object theRequest,
			RequestPartitionId theRequestPartitionId) throws Exception {

		Method method = ExpungeEverythingService.class.getDeclaredMethod(
				"expungeBatch2EntitiesInSingleTransaction",
				ca.uhn.fhir.rest.api.server.RequestDetails.class,
				RequestPartitionId.class);
		method.setAccessible(true);
		try {
			return (int) method.invoke(myExpungeEverythingService, theRequest, theRequestPartitionId);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			}
			throw e;
		}
	}
}
