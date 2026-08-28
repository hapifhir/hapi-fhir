// Created by claude-opus-5
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import jakarta.annotation.Nullable;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for the exception-routing contract of {@link BaseBulkModifyResourcesStep#run(StepExecutionDetails, IJobDataSink)}.
 * <p>
 * <code>ReindexV3ModifyResourcesStep</code> extends
 * {@link BaseBulkModifyResourcesStep} directly and throws {@link RetryChunkLaterException} from its
 * {@link BaseBulkModifyResourcesStep#processPidsOutsideTransaction} override, so the fake step below
 * subclasses the base class directly rather than {@link BaseBulkModifyResourcesIndividuallyStep}.
 * </p>
 * <p>
 * Note: the hooks below never assert - the production code wraps them in a <code>catch (Throwable)</code>
 * which would swallow an {@link AssertionError} and turn a failing test green. Observations are recorded
 * into fields and asserted after <code>run()</code> returns.
 * </p>
 */
@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
class BaseBulkModifyResourcesStepTest {

	private static final String RESOURCE_TYPE = "Patient";
	private static final String RESOURCE_ID_VALUE = "Patient/ABC/_history/123";
	private static final String FALLBACK_ID_VALUE = "Patient/ABC";

	@Spy
	private IHapiTransactionService myTransactionService = new MyMockTxService();

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	@Mock
	private IJobDataSink<BulkModifyResourcesChunkOutcomeJson> mySink;

	@Captor
	private ArgumentCaptor<BulkModifyResourcesChunkOutcomeJson> myDataCaptor;

	@InjectMocks
	private MySvc mySvc = new MySvc();

	/**
	 * Optional action run by {@link MySvc#processPidsOutsideTransaction} - typically throws.
	 */
	private IHookAction myOutsideTransactionAction;

	/**
	 * Optional action run by {@link MySvc#processPidsInTransaction} before the default body - typically throws.
	 */
	private IHookAction myInTransactionAction;

	private int myOutsideTransactionInvocationCount;
	private int myInTransactionInvocationCount;
	private Boolean myTransactionActiveInOutsideTransactionHook;
	private Boolean myTransactionActiveInInTransactionHook;

	/**
	 * P1: A {@link RetryChunkLaterException} raised by the pre-flight hook is a <b>retry signal</b>, not a
	 * failure. It must escape <code>run()</code> so that
	 * <code>StepExecutor</code> can move the work chunk to POLL_WAITING.
	 */
	@Test
	void testRun_preFlightThrowsRetryChunkLater_propagatesToStepExecutor() {
		// Setup
		stubIdHelperForUnresolvedPid();
		myOutsideTransactionAction =
				(theState, thePids) -> {
					throw new RetryChunkLaterException(Msg.code(2830), Duration.ofSeconds(10));
				};
		StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> details = createDetails();

		// Test & Verify
		assertThatThrownBy(() -> mySvc.run(details, mySink)).isInstanceOf(RetryChunkLaterException.class);

		verifyNoInteractions(mySink);
		assertThat(myOutsideTransactionInvocationCount).isEqualTo(1);
	}

	/**
	 * A1 (control): a {@link JobExecutionFailedException} raised by the pre-flight hook already escapes
	 * <code>run()</code> today, and must continue to.
	 */
	@Test
	void testRun_preFlightThrowsJobExecutionFailed_stillPropagates() {
		// Setup
		myOutsideTransactionAction =
				(theState, thePids) -> {
					throw new JobExecutionFailedException("unrecoverable-marker");
				};
		StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> details = createDetails();

		// Test & Verify
		assertThatThrownBy(() -> mySvc.run(details, mySink))
				.isInstanceOf(JobExecutionFailedException.class)
				.hasMessageContaining("unrecoverable-marker");

		verifyNoInteractions(mySink);
		assertThat(myOutsideTransactionInvocationCount).isEqualTo(1);
	}

	/**
	 * A2: the same retry signal raised from inside the transactional body must also escape <code>run()</code>.
	 * Hoisting the pre-flight call out of the try/catch does not cover this position - the rethrow arm itself
	 * has to recognise {@link RetryChunkLaterException}.
	 */
	@Test
	void testRun_inTransactionThrowsRetryChunkLater_propagatesToStepExecutor() {
		// Setup
		stubIdHelperForUnresolvedPid();
		myInTransactionAction =
				(theState, thePids) -> {
					throw new RetryChunkLaterException(Msg.code(2830), Duration.ofSeconds(10));
				};
		StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> details = createDetails();

		// Test & Verify
		assertThatThrownBy(() -> mySvc.run(details, mySink)).isInstanceOf(RetryChunkLaterException.class);

		verifyNoInteractions(mySink);
		assertThat(myInTransactionInvocationCount).isEqualTo(1);
	}

	/**
	 * A3 (control): a generic failure inside the transaction is a per-resource failure and must stay swallowed
	 * into the emitted outcome. If this ever turns red, the escape hatch has been over-broadened.
	 */
	@Test
	void testRun_inTransactionThrowsGenericException_recordedAsPerResourceFailure() {
		// Setup
		myInTransactionAction = (theState, thePids) -> {
			for (TypedPidAndVersionJson pid : thePids) {
				theState.setResourceIdForPid(pid, new IdType(RESOURCE_ID_VALUE));
			}
			throw new MyTestFailureException("in-transaction boom");
		};
		StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> details = createDetails();

		// Test
		RunOutcome outcome = mySvc.run(details, mySink);

		// Verify
		assertThat(outcome.getRecordsProcessed()).isEqualTo(1);
		verify(mySink, times(1)).accept(myDataCaptor.capture());
		BulkModifyResourcesChunkOutcomeJson outputData = myDataCaptor.getValue();
		assertThat(outputData.getFailures()).containsOnlyKeys(RESOURCE_ID_VALUE);
		assertThat(outputData.getFailures().get(RESOURCE_ID_VALUE)).contains("in-transaction boom");
		assertThat(outputData.getChangedIds()).isEmpty();
	}

	/**
	 * A4: <b>deliberate behaviour change, not a regression.</b> A generic failure in the pre-flight hook is a
	 * whole-chunk guard failure, so it must escape <code>run()</code> and let
	 * <code>StepExecutor</code> mark the chunk retriable ERRORED, rather than being
	 * mis-attributed to each individual resource. This test asserts the post-fix semantics.
	 */
	@Test
	void testRun_preFlightThrowsGenericException_propagatesSoChunkCanBeRetried() {
		// Setup
		stubIdHelperForUnresolvedPid();
		myOutsideTransactionAction =
				(theState, thePids) -> {
					throw new MyTestFailureException("pre-flight boom");
				};
		StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> details = createDetails();

		// Test & Verify
		assertThatThrownBy(() -> mySvc.run(details, mySink))
				.isInstanceOf(MyTestFailureException.class)
				.hasMessageContaining("pre-flight boom");

		verifyNoInteractions(mySink);
		assertThat(myOutsideTransactionInvocationCount).isEqualTo(1);
	}

	/**
	 * A5: invariant guard for the hoist - the pre-flight hook must keep running exactly once, outside any
	 * transaction, before the transactional body runs inside one.
	 */
	@Test
	void testRun_happyPath_preFlightRunsOutsideTransactionAndBodyRunsInside() {
		// Setup
		StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> details = createDetails();

		// Test
		RunOutcome outcome = mySvc.run(details, mySink);

		// Verify
		assertThat(myTransactionActiveInOutsideTransactionHook).isFalse();
		assertThat(myTransactionActiveInInTransactionHook).isTrue();
		assertThat(myOutsideTransactionInvocationCount).isEqualTo(1);
		assertThat(myInTransactionInvocationCount).isEqualTo(1);
		assertThat(outcome.getRecordsProcessed()).isEqualTo(1);

		verify(mySink, times(1)).accept(myDataCaptor.capture());
		BulkModifyResourcesChunkOutcomeJson outputData = myDataCaptor.getValue();
		assertThat(outputData.getUnchangedIds()).containsExactly(RESOURCE_ID_VALUE);
		assertThat(outputData.getFailures()).isEmpty();
	}

	private StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> createDetails() {
		TypedPidAndVersionListWorkChunkJson data = new TypedPidAndVersionListWorkChunkJson();
		data.addTypedPidWithNullPartitionForUnitTest(RESOURCE_TYPE, 1L, null);
		return new StepExecutionDetails<>(
				new MyParameters(),
				data,
				new JobInstance(),
				new WorkChunk().setId("my-chunk-id"),
				myJobStepExecutionServices);
	}

	/**
	 * A PID that fails before its resource has been fetched has no ID in the {@link BaseBulkModifyResourcesStep.State},
	 * so <code>BaseBulkModifyResourcesStep#toId</code> falls back to the ID helper. Without this stub the pre-fix
	 * failure surfaces as a {@link NullPointerException} from
	 * {@link BulkModifyResourcesChunkOutcomeJson#addFailure} instead of an honest assertion failure. It is
	 * {@link org.mockito.Mockito#lenient()} because it is consumed only on the (defective) swallow path.
	 */
	private void stubIdHelperForUnresolvedPid() {
		lenient()
				.when(myIdHelperService.translatePidIdToForcedId(any(), any(), any()))
				.thenReturn(new IdType(FALLBACK_ID_VALUE));
	}

	@FunctionalInterface
	private interface IHookAction {
		void execute(BaseBulkModifyResourcesStep.State theState, List<TypedPidAndVersionJson> thePids);
	}

	/**
	 * Marker type so that an incidental {@link NullPointerException} can not satisfy an
	 * <code>isInstanceOf(...)</code> assertion about a "generic" failure.
	 */
	private static class MyTestFailureException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		MyTestFailureException(String theMessage) {
			super(theMessage);
		}
	}

	private static class MyParameters extends BaseBulkModifyJobParameters {
		// nothing
	}

	private static class MyMockTxService extends HapiTransactionService {

		@Nullable
		@Override
		public <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
			boolean initialState = TransactionSynchronizationManager.isActualTransactionActive();
			try {
				if (!initialState) {
					TransactionSynchronizationManager.setActualTransactionActive(true);
				}
				return theCallback.doInTransaction(new SimpleTransactionStatus());
			} finally {
				if (!initialState) {
					TransactionSynchronizationManager.setActualTransactionActive(false);
				}
			}
		}
	}

	/**
	 * Mirrors <code>ReindexV3ModifyResourcesStep</code>, which extends
	 * {@link BaseBulkModifyResourcesStep} directly and overrides both hooks.
	 */
	private class MySvc extends BaseBulkModifyResourcesStep<MyParameters, Void> {

		@Override
		protected void processPidsOutsideTransaction(
				StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails,
				MyParameters theJobParameters,
				State theState,
				List<TypedPidAndVersionJson> thePids,
				TransactionDetails theTransactionDetails,
				IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink) {
			myOutsideTransactionInvocationCount++;
			myTransactionActiveInOutsideTransactionHook = TransactionSynchronizationManager.isActualTransactionActive();
			if (myOutsideTransactionAction != null) {
				myOutsideTransactionAction.execute(theState, thePids);
			}
		}

		@Override
		protected void processPidsInTransaction(
				StepExecutionDetails<MyParameters, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails,
				State theState,
				List<TypedPidAndVersionJson> thePids,
				TransactionDetails theTransactionDetails,
				IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink) {
			myInTransactionInvocationCount++;
			myTransactionActiveInInTransactionHook = TransactionSynchronizationManager.isActualTransactionActive();
			if (myInTransactionAction != null) {
				myInTransactionAction.execute(theState, thePids);
			}
			for (TypedPidAndVersionJson pid : thePids) {
				theState.setResourceIdForPid(pid, new IdType(RESOURCE_ID_VALUE));
				theState.moveToState(pid, StateEnum.UNCHANGED);
			}
		}

		@Override
		protected String getJobNameForLogging() {
			return "TEST-STEP";
		}
	}
}
