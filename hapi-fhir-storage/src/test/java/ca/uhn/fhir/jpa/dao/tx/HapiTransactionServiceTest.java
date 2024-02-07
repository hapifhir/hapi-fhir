package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.SleepUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class HapiTransactionServiceTest {

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcasterMock;

	@Mock
	private PlatformTransactionManager myTransactionManagerMock;

	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvcMock;
	@Mock
	private PartitionSettings myPartitionSettingsMock;

	@Mock
	private SleepUtil mySleepUtilMock;

	private HapiTransactionService myHapiTransactionService;


	@BeforeEach
	public void beforeEach() {
		myHapiTransactionService = new HapiTransactionService();
		myHapiTransactionService.setTransactionManager(myTransactionManagerMock);
		myHapiTransactionService.setInterceptorBroadcaster(myInterceptorBroadcasterMock);
		myHapiTransactionService.setPartitionSettingsForUnitTest(myPartitionSettingsMock);
		myHapiTransactionService.setRequestPartitionSvcForUnitTest(myRequestPartitionHelperSvcMock);
		myHapiTransactionService.setSleepUtil(mySleepUtilMock);
		mockInterceptorBroadcaster();
	}

	private void mockInterceptorBroadcaster() {
		lenient().when(myInterceptorBroadcasterMock.callHooksAndReturnObject(eq(Pointcut.STORAGE_VERSION_CONFLICT),
			isA(HookParams.class)))
			.thenAnswer(invocationOnMock -> {
				HookParams hookParams = (HookParams) invocationOnMock.getArguments()[1];
				//answer with whatever retry settings passed in as HookParam
				RequestDetails requestDetails = hookParams.get(RequestDetails.class);
				ResourceVersionConflictResolutionStrategy answer = new ResourceVersionConflictResolutionStrategy();
				answer.setRetry(requestDetails.isRetry());
				answer.setMaxRetries(requestDetails.getMaxRetries());
				return answer;
			});
	}


	/**
	 * A helper method to test retry logic on exceptions
	 * TransactionCallback interface allows only throwing RuntimeExceptions,
	 * that's why the parameter type is RunTimeException
	 */
	private Exception testRetriesOnException(RuntimeException theException,
											 boolean theRetryEnabled,
											 int theMaxRetries,
											 int theExpectedNumberOfCallsToTransactionCallback) {
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRetry(theRetryEnabled);
		requestDetails.setMaxRetries(theMaxRetries);

		HapiTransactionService.IExecutionBuilder executionBuilder = myHapiTransactionService
			.withRequest(requestDetails)
			.withTransactionDetails(new TransactionDetails());

		AtomicInteger numberOfCalls = new AtomicInteger();
		TransactionCallback<Void> transactionCallback = (TransactionStatus theStatus) -> {
			numberOfCalls.incrementAndGet();
			throw theException;
		};

		Exception theExceptionThrownByDoExecute = assertThrows(Exception.class, () -> {
			myHapiTransactionService.doExecute((HapiTransactionService.ExecutionBuilder) executionBuilder, transactionCallback);
		});

		assertEquals(theExpectedNumberOfCallsToTransactionCallback, numberOfCalls.get());
		verify(mySleepUtilMock, times(theExpectedNumberOfCallsToTransactionCallback - 1))
			.sleepAtLeast(anyLong(), anyBoolean());
		return theExceptionThrownByDoExecute;
	}

	private static Stream<Arguments> provideRetriableExceptionParameters() {
		String exceptionMessage = "failed!";
		return Stream.of(
			arguments(new ResourceVersionConflictException(exceptionMessage)),
			arguments(new DataIntegrityViolationException(exceptionMessage)),
			arguments(new ConstraintViolationException(exceptionMessage, new SQLException(""), null)),
			arguments(new ObjectOptimisticLockingFailureException(exceptionMessage, new Exception())),
			//CannotAcquireLockException is a subclass of
			//PessimisticLockingFailureException which we treat as a retriable exception
			arguments(new CannotAcquireLockException(exceptionMessage))
		);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource(value = "provideRetriableExceptionParameters")
	void testDoExecute_WhenRetryEnabled_RetriesOnRetriableExceptions(RuntimeException theException) {
		testRetriesOnException(theException, true, 2, 3);
	}


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource(value = "provideRetriableExceptionParameters")
	void testDoExecute_WhenRetryEnabled_RetriesOnRetriableInnerExceptions(RuntimeException theException) {
		//in this test we wrap the retriable exception to test that nested exceptions are covered as well
		RuntimeException theWrapperException = new RuntimeException("this is the wrapper", theException);
		testRetriesOnException(theWrapperException, true, 2, 3);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource(value = "provideRetriableExceptionParameters")
	void testDoExecute_WhenRetryIsDisabled_DoesNotRetryExceptions(RuntimeException theException) {
		testRetriesOnException(theException, false, 10, 1);
	}

	@Test
	void testDoExecute_WhenRetryEnabled_DoesNotRetryOnNonRetriableException() {
		RuntimeException nonRetriableException = new RuntimeException("should not be retried");
		Exception exceptionThrown = testRetriesOnException(nonRetriableException, true, 10, 1);
		assertEquals(nonRetriableException, exceptionThrown);
		verifyNoInteractions(myInterceptorBroadcasterMock);
	}

	@Test
	void testDoExecute_WhenRetyEnabled_StopsRetryingWhenARetryIsSuccessfull() {
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRetry(true);
		requestDetails.setMaxRetries(10);

		HapiTransactionService.IExecutionBuilder executionBuilder = myHapiTransactionService
			.withRequest(requestDetails)
			.withTransactionDetails(new TransactionDetails());

		AtomicInteger numberOfCalls = new AtomicInteger();
		TransactionCallback<Void> transactionCallback = (TransactionStatus theStatus) -> {
			int currentCallNum = numberOfCalls.incrementAndGet();
			//fail for the first two calls then succeed on the third
			if (currentCallNum < 3) {
				// using ResourceVersionConflictException, since it is a retriable exception
				throw new ResourceVersionConflictException("failed");
			}
			return null;
		};

		myHapiTransactionService.doExecute((HapiTransactionService.ExecutionBuilder) executionBuilder, transactionCallback);

		assertEquals(3, numberOfCalls.get());
		verify(mySleepUtilMock, times(2))
			.sleepAtLeast(anyLong(), anyBoolean());
	}
}
