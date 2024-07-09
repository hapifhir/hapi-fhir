/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy;
import ca.uhn.fhir.jpa.dao.DaoFailureUtil;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.ICallable;
import ca.uhn.fhir.util.SleepUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @see IHapiTransactionService for an explanation of this class
 */
public class HapiTransactionService implements IHapiTransactionService {

	public static final String XACT_USERDATA_KEY_RESOLVED_TAG_DEFINITIONS =
			HapiTransactionService.class.getName() + "_RESOLVED_TAG_DEFINITIONS";
	public static final String XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS =
			HapiTransactionService.class.getName() + "_EXISTING_SEARCH_PARAMS";
	private static final Logger ourLog = LoggerFactory.getLogger(HapiTransactionService.class);
	private static final ThreadLocal<RequestPartitionId> ourRequestPartitionThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<HapiTransactionService> ourExistingTransaction = new ThreadLocal<>();

	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	protected PlatformTransactionManager myTransactionManager;

	@Autowired
	protected IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	protected PartitionSettings myPartitionSettings;

	private Propagation myTransactionPropagationWhenChangingPartitions = Propagation.REQUIRED;

	private SleepUtil mySleepUtil = new SleepUtil();

	@VisibleForTesting
	public void setInterceptorBroadcaster(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@VisibleForTesting
	public void setSleepUtil(SleepUtil theSleepUtil) {
		mySleepUtil = theSleepUtil;
	}

	@Override
	public IExecutionBuilder withRequest(@Nullable RequestDetails theRequestDetails) {
		return buildExecutionBuilder(theRequestDetails);
	}

	@Override
	public IExecutionBuilder withSystemRequest() {
		return buildExecutionBuilder(null);
	}

	protected IExecutionBuilder buildExecutionBuilder(@Nullable RequestDetails theRequestDetails) {
		return new ExecutionBuilder(theRequestDetails);
	}

	/**
	 * @deprecated Use {@link #withRequest(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public <T> T execute(
			@Nullable RequestDetails theRequestDetails,
			@Nullable TransactionDetails theTransactionDetails,
			@Nonnull TransactionCallback<T> theCallback) {
		return execute(theRequestDetails, theTransactionDetails, theCallback, null);
	}

	/**
	 * @deprecated Use {@link #withRequest(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public void execute(
			@Nullable RequestDetails theRequestDetails,
			@Nullable TransactionDetails theTransactionDetails,
			@Nonnull Propagation thePropagation,
			@Nonnull Isolation theIsolation,
			@Nonnull Runnable theCallback) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				theCallback.run();
			}
		};
		execute(theRequestDetails, theTransactionDetails, callback, null, thePropagation, theIsolation);
	}

	/**
	 * @deprecated Use {@link #withRequest(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	@Override
	public <T> T withRequest(
			@Nullable RequestDetails theRequestDetails,
			@Nullable TransactionDetails theTransactionDetails,
			@Nonnull Propagation thePropagation,
			@Nonnull Isolation theIsolation,
			@Nonnull ICallable<T> theCallback) {

		TransactionCallback<T> callback = tx -> theCallback.call();
		return execute(theRequestDetails, theTransactionDetails, callback, null, thePropagation, theIsolation);
	}

	/**
	 * @deprecated Use {@link #withRequest(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public <T> T execute(
			@Nullable RequestDetails theRequestDetails,
			@Nullable TransactionDetails theTransactionDetails,
			@Nonnull TransactionCallback<T> theCallback,
			@Nullable Runnable theOnRollback) {
		return execute(theRequestDetails, theTransactionDetails, theCallback, theOnRollback, null, null);
	}

	@SuppressWarnings("ConstantConditions")
	/**
	 * @deprecated Use {@link #withRequest(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public <T> T execute(
			@Nullable RequestDetails theRequestDetails,
			@Nullable TransactionDetails theTransactionDetails,
			@Nonnull TransactionCallback<T> theCallback,
			@Nullable Runnable theOnRollback,
			@Nullable Propagation thePropagation,
			@Nullable Isolation theIsolation) {
		return withRequest(theRequestDetails)
				.withTransactionDetails(theTransactionDetails)
				.withPropagation(thePropagation)
				.withIsolation(theIsolation)
				.onRollback(theOnRollback)
				.execute(theCallback);
	}

	/**
	 * @deprecated Use {@link #withRequest(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public <T> T execute(
			@Nullable RequestDetails theRequestDetails,
			@Nullable TransactionDetails theTransactionDetails,
			@Nonnull TransactionCallback<T> theCallback,
			@Nullable Runnable theOnRollback,
			@Nonnull Propagation thePropagation,
			@Nonnull Isolation theIsolation,
			RequestPartitionId theRequestPartitionId) {
		return withRequest(theRequestDetails)
				.withTransactionDetails(theTransactionDetails)
				.withPropagation(thePropagation)
				.withIsolation(theIsolation)
				.withRequestPartitionId(theRequestPartitionId)
				.onRollback(theOnRollback)
				.execute(theCallback);
	}

	public boolean isCustomIsolationSupported() {
		return false;
	}

	@VisibleForTesting
	public void setRequestPartitionSvcForUnitTest(IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	public PlatformTransactionManager getTransactionManager() {
		return myTransactionManager;
	}

	@VisibleForTesting
	public void setTransactionManager(PlatformTransactionManager theTransactionManager) {
		myTransactionManager = theTransactionManager;
	}

	@VisibleForTesting
	public void setPartitionSettingsForUnitTest(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	@Nullable
	protected <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
		final RequestPartitionId requestPartitionId = theExecutionBuilder.getEffectiveRequestPartitionId();
		RequestPartitionId previousRequestPartitionId = null;
		if (requestPartitionId != null) {
			previousRequestPartitionId = ourRequestPartitionThreadLocal.get();
			ourRequestPartitionThreadLocal.set(requestPartitionId);
		}

		ourLog.trace("Starting doExecute for RequestPartitionId {}", requestPartitionId);
		if (!myPartitionSettings.isPartitioningEnabled()
				|| Objects.equals(previousRequestPartitionId, requestPartitionId)) {
			if (ourExistingTransaction.get() == this && canReuseExistingTransaction(theExecutionBuilder)) {
				/*
				 * If we're already in an active transaction, and it's for the right partition,
				 * and it's not a read-only transaction, we don't need to open a new transaction
				 * so let's just add a method to the stack trace that makes this obvious.
				 */
				return executeInExistingTransaction(theCallback);
			}
		}

		HapiTransactionService previousExistingTransaction = ourExistingTransaction.get();
		try {
			ourExistingTransaction.set(this);

			if (myTransactionPropagationWhenChangingPartitions == Propagation.REQUIRES_NEW) {
				return executeInNewTransactionForPartitionChange(
						theExecutionBuilder, theCallback, requestPartitionId, previousRequestPartitionId);
			} else {
				return doExecuteInTransaction(
						theExecutionBuilder, theCallback, requestPartitionId, previousRequestPartitionId);
			}
		} finally {
			ourExistingTransaction.set(previousExistingTransaction);
		}
	}

	@Nullable
	private <T> T executeInNewTransactionForPartitionChange(
			ExecutionBuilder theExecutionBuilder,
			TransactionCallback<T> theCallback,
			RequestPartitionId requestPartitionId,
			RequestPartitionId previousRequestPartitionId) {
		ourLog.trace("executeInNewTransactionForPartitionChange");
		theExecutionBuilder.myPropagation = myTransactionPropagationWhenChangingPartitions;
		return doExecuteInTransaction(theExecutionBuilder, theCallback, requestPartitionId, previousRequestPartitionId);
	}

	private boolean isThrowableOrItsSubclassPresent(Throwable theThrowable, Class<? extends Throwable> theClass) {
		return ExceptionUtils.indexOfType(theThrowable, theClass) != -1;
	}

	private boolean isThrowablePresent(Throwable theThrowable, Class<? extends Throwable> theClass) {
		return ExceptionUtils.indexOfThrowable(theThrowable, theClass) != -1;
	}

	private boolean isRetriable(Throwable theThrowable) {
		return isThrowablePresent(theThrowable, ResourceVersionConflictException.class)
				|| isThrowablePresent(theThrowable, DataIntegrityViolationException.class)
				|| isThrowablePresent(theThrowable, ConstraintViolationException.class)
				|| isThrowablePresent(theThrowable, ObjectOptimisticLockingFailureException.class)
				// calling isThrowableOrItsSubclassPresent instead of isThrowablePresent for
				// PessimisticLockingFailureException, because we want to retry on its subclasses as well,  especially
				// CannotAcquireLockException, which is thrown in some deadlock situations which we want to retry
				|| isThrowableOrItsSubclassPresent(theThrowable, PessimisticLockingFailureException.class);
	}

	@Nullable
	private <T> T doExecuteInTransaction(
			ExecutionBuilder theExecutionBuilder,
			TransactionCallback<T> theCallback,
			RequestPartitionId requestPartitionId,
			RequestPartitionId previousRequestPartitionId) {
		ourLog.trace("doExecuteInTransaction");
		try {
			for (int i = 0; ; i++) {
				try {

					return doExecuteCallback(theExecutionBuilder, theCallback);

				} catch (Exception e) {
					if (!isRetriable(e)) {
						ourLog.debug("Unexpected transaction exception. Will not be retried.", e);
						throw e;
					} else {

						ourLog.debug("Version conflict detected", e);

						if (theExecutionBuilder.myOnRollback != null) {
							theExecutionBuilder.myOnRollback.run();
						}

						int maxRetries = 0;

						/*
						 * If two client threads both concurrently try to add the same tag that isn't
						 * known to the system already, they'll both try to create a row in HFJ_TAG_DEF,
						 * which is the tag definition table. In that case, a constraint error will be
						 * thrown by one of the client threads, so we auto-retry in order to avoid
						 * annoying spurious failures for the client.
						 */
						if (DaoFailureUtil.isTagStorageFailure(e)) {
							maxRetries = 3;
						}

						if (maxRetries == 0) {
							HookParams params = new HookParams()
									.add(RequestDetails.class, theExecutionBuilder.myRequestDetails)
									.addIfMatchesType(
											ServletRequestDetails.class, theExecutionBuilder.myRequestDetails);
							ResourceVersionConflictResolutionStrategy conflictResolutionStrategy =
									(ResourceVersionConflictResolutionStrategy)
											CompositeInterceptorBroadcaster.doCallHooksAndReturnObject(
													myInterceptorBroadcaster,
													theExecutionBuilder.myRequestDetails,
													Pointcut.STORAGE_VERSION_CONFLICT,
													params);
							if (conflictResolutionStrategy != null && conflictResolutionStrategy.isRetry()) {
								maxRetries = conflictResolutionStrategy.getMaxRetries();
							}
						}

						if (i < maxRetries) {
							if (theExecutionBuilder.myTransactionDetails != null) {
								theExecutionBuilder
										.myTransactionDetails
										.getRollbackUndoActions()
										.forEach(Runnable::run);
								theExecutionBuilder.myTransactionDetails.clearRollbackUndoActions();
								theExecutionBuilder.myTransactionDetails.clearResolvedItems();
								theExecutionBuilder.myTransactionDetails.clearUserData(
										XACT_USERDATA_KEY_RESOLVED_TAG_DEFINITIONS);
								theExecutionBuilder.myTransactionDetails.clearUserData(
										XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS);
							}
							double sleepAmount = (250.0d * i) * Math.random();
							long sleepAmountLong = (long) sleepAmount;
							mySleepUtil.sleepAtLeast(sleepAmountLong, false);

							ourLog.info(
									"About to start a transaction retry due to conflict or constraint error. Sleeping {}ms first.",
									sleepAmountLong);
							continue;
						}

						IBaseOperationOutcome oo = null;
						if (e instanceof ResourceVersionConflictException) {
							oo = ((ResourceVersionConflictException) e).getOperationOutcome();
						}

						if (maxRetries > 0) {
							String msg =
									"Max retries (" + maxRetries + ") exceeded for version conflict: " + e.getMessage();
							ourLog.info(msg, maxRetries);
							throw new ResourceVersionConflictException(Msg.code(549) + msg);
						}

						throw new ResourceVersionConflictException(Msg.code(550) + e.getMessage(), e, oo);
					}
				}
			}
		} finally {
			if (requestPartitionId != null) {
				ourRequestPartitionThreadLocal.set(previousRequestPartitionId);
			}
		}
	}

	public void setTransactionPropagationWhenChangingPartitions(
			Propagation theTransactionPropagationWhenChangingPartitions) {
		Validate.notNull(theTransactionPropagationWhenChangingPartitions);
		myTransactionPropagationWhenChangingPartitions = theTransactionPropagationWhenChangingPartitions;
	}

	@Nullable
	protected <T> T doExecuteCallback(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
		try {
			TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);

			if (theExecutionBuilder.myPropagation != null) {
				txTemplate.setPropagationBehavior(theExecutionBuilder.myPropagation.value());
			}

			if (isCustomIsolationSupported()
					&& theExecutionBuilder.myIsolation != null
					&& theExecutionBuilder.myIsolation != Isolation.DEFAULT) {
				txTemplate.setIsolationLevel(theExecutionBuilder.myIsolation.value());
			}

			if (theExecutionBuilder.myReadOnly) {
				txTemplate.setReadOnly(true);
			}

			return txTemplate.execute(theCallback);
		} catch (MyException e) {
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			} else {
				throw new InternalErrorException(Msg.code(551) + e);
			}
		}
	}

	protected class ExecutionBuilder implements IExecutionBuilder, TransactionOperations, Cloneable {

		private final RequestDetails myRequestDetails;
		private Isolation myIsolation;
		private Propagation myPropagation;
		private boolean myReadOnly;
		private TransactionDetails myTransactionDetails;
		private Runnable myOnRollback;
		protected RequestPartitionId myRequestPartitionId;

		protected ExecutionBuilder(RequestDetails theRequestDetails) {
			myRequestDetails = theRequestDetails;
		}

		@Override
		public ExecutionBuilder withIsolation(Isolation theIsolation) {
			assert myIsolation == null;
			myIsolation = theIsolation;
			return this;
		}

		@Override
		public ExecutionBuilder withTransactionDetails(TransactionDetails theTransactionDetails) {
			assert myTransactionDetails == null;
			myTransactionDetails = theTransactionDetails;
			return this;
		}

		@Override
		public ExecutionBuilder withPropagation(Propagation thePropagation) {
			assert myPropagation == null;
			myPropagation = thePropagation;
			return this;
		}

		@Override
		public ExecutionBuilder withRequestPartitionId(RequestPartitionId theRequestPartitionId) {
			assert myRequestPartitionId == null;
			myRequestPartitionId = theRequestPartitionId;
			return this;
		}

		@Override
		public ExecutionBuilder readOnly() {
			myReadOnly = true;
			return this;
		}

		@Override
		public ExecutionBuilder onRollback(Runnable theOnRollback) {
			assert myOnRollback == null;
			myOnRollback = theOnRollback;
			return this;
		}

		@Override
		public void execute(Runnable theTask) {
			TransactionCallback<Void> task = tx -> {
				theTask.run();
				return null;
			};
			execute(task);
		}

		@Override
		public <T> T execute(Callable<T> theTask) {
			TransactionCallback<T> callback = tx -> invokeCallableAndHandleAnyException(theTask);
			return execute(callback);
		}

		@Override
		public <T> T execute(@Nonnull TransactionCallback<T> callback) {
			assert callback != null;

			return doExecute(this, callback);
		}

		@VisibleForTesting
		public RequestPartitionId getRequestPartitionIdForTesting() {
			return myRequestPartitionId;
		}

		@VisibleForTesting
		public RequestDetails getRequestDetailsForTesting() {
			return myRequestDetails;
		}

		public Propagation getPropagation() {
			return myPropagation;
		}

		@Nullable
		protected RequestPartitionId getEffectiveRequestPartitionId() {
			final RequestPartitionId requestPartitionId;
			if (myRequestPartitionId != null) {
				requestPartitionId = myRequestPartitionId;
			} else if (myRequestDetails != null) {
				requestPartitionId = myRequestPartitionHelperSvc.determineGenericPartitionForRequest(myRequestDetails);
			} else {
				requestPartitionId = null;
			}
			return requestPartitionId;
		}
	}

	/**
	 * This is just an unchecked exception so that we can catch checked exceptions inside TransactionTemplate
	 * and rethrow them outside of it
	 */
	static class MyException extends RuntimeException {

		public MyException(Throwable theThrowable) {
			super(theThrowable);
		}
	}

	/**
	 * Returns true if we alreadyt have an active transaction associated with the current thread, AND
	 * either it's non-read-only or we only need a read-only transaction, AND
	 * the newly requested transaction has a propagation of REQUIRED
	 */
	private static boolean canReuseExistingTransaction(ExecutionBuilder theExecutionBuilder) {
		return TransactionSynchronizationManager.isActualTransactionActive()
				&& (!TransactionSynchronizationManager.isCurrentTransactionReadOnly() || theExecutionBuilder.myReadOnly)
				&& (theExecutionBuilder.myPropagation == null
						|| theExecutionBuilder.myPropagation == Propagation.REQUIRED);
	}

	@Nullable
	private static <T> T executeInExistingTransaction(@Nonnull TransactionCallback<T> theCallback) {
		ourLog.trace("executeInExistingTransaction");
		// TODO we could probably track the TransactionStatus we need as a thread local like we do our partition id.
		return theCallback.doInTransaction(null);
	}

	/**
	 * Invokes {@link Callable#call()} and rethrows any exceptions thrown by that method.
	 * If the exception extends {@link BaseServerResponseException} it is rethrown unmodified.
	 * Otherwise, it's wrapped in a {@link InternalErrorException}.
	 */
	public static <T> T invokeCallableAndHandleAnyException(Callable<T> theTask) {
		try {
			return theTask.call();
		} catch (BaseServerResponseException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException(Msg.code(2223) + e.getMessage(), e);
		}
	}

	public static <T> T executeWithDefaultPartitionInContext(@Nonnull ICallable<T> theCallback) {
		RequestPartitionId previousRequestPartitionId = ourRequestPartitionThreadLocal.get();
		ourRequestPartitionThreadLocal.set(RequestPartitionId.defaultPartition());
		try {
			return theCallback.call();
		} finally {
			ourRequestPartitionThreadLocal.set(previousRequestPartitionId);
		}
	}

	public static RequestPartitionId getRequestPartitionAssociatedWithThread() {
		return ourRequestPartitionThreadLocal.get();
	}

	/**
	 * Throws an {@link IllegalArgumentException} if a transaction is active
	 */
	public static void noTransactionAllowed() {
		Validate.isTrue(
				!TransactionSynchronizationManager.isActualTransactionActive(),
				"Transaction must not be active but found an active transaction");
	}

	/**
	 * Throws an {@link IllegalArgumentException} if no transaction is active
	 */
	public static void requireTransaction() {
		Validate.isTrue(
				TransactionSynchronizationManager.isActualTransactionActive(),
				"Transaction required here but no active transaction found");
	}
}
