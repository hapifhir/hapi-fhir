package ca.uhn.fhir.jpa.dao.tx;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy;
import ca.uhn.fhir.jpa.dao.DaoFailureUtil;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.ICallable;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;

public class HapiTransactionService implements IHapiTransactionService {

	public static final String XACT_USERDATA_KEY_RESOLVED_TAG_DEFINITIONS = HapiTransactionService.class.getName() + "_RESOLVED_TAG_DEFINITIONS";
	public static final String XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS = HapiTransactionService.class.getName() + "_EXISTING_SEARCH_PARAMS";
	private static final Logger ourLog = LoggerFactory.getLogger(HapiTransactionService.class);
	private static final ThreadLocal<RequestPartitionId> ourRequestPartition = new ThreadLocal<>();
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	protected PlatformTransactionManager myTransactionManager;
	@Autowired
	protected IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private boolean myCustomIsolationSupported;

	@VisibleForTesting
	public void setInterceptorBroadcaster(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@PostConstruct
	public void start() {
		myCustomIsolationSupported = isCustomIsolationSupported();
	}

	@Override
	public IExecutionBuilder execute(@Nullable RequestDetails theRequestDetails) {
		return new ExecutionBuilder(theRequestDetails);
	}


	/**
	 * @deprecated Use {@link #execute(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull TransactionCallback<T> theCallback) {
		return execute(theRequestDetails, theTransactionDetails, theCallback, null);
	}

	/**
	 * @deprecated Use {@link #execute(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public void execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation, @Nonnull Runnable theCallback) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				theCallback.run();
			}
		};
		execute(theRequestDetails, theTransactionDetails, callback, null, thePropagation, theIsolation);
	}

	/**
	 * @deprecated Use {@link #execute(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	@Override
	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation, @Nonnull ICallable<T> theCallback) {

		TransactionCallback<T> callback = tx -> theCallback.call();
		return execute(theRequestDetails, theTransactionDetails, callback, null, thePropagation, theIsolation);
	}

	/**
	 * @deprecated Use {@link #execute(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull TransactionCallback<T> theCallback, @Nullable Runnable theOnRollback) {
		return execute(theRequestDetails, theTransactionDetails, theCallback, theOnRollback, null, null);
	}

	@SuppressWarnings("ConstantConditions")
	/**
	 * @deprecated Use {@link #execute(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull TransactionCallback<T> theCallback, @Nullable Runnable theOnRollback, @Nullable Propagation thePropagation, @Nullable Isolation theIsolation) {
		return execute(theRequestDetails)
			.withTransactionDetails(theTransactionDetails)
			.withPropagation(thePropagation)
			.withIsolation(theIsolation)
			.onRollback(theOnRollback)
			.task(theCallback);
	}

	/**
	 * @deprecated Use {@link #execute(RequestDetails)} with fluent call instead
	 */
	@Deprecated
	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull TransactionCallback<T> theCallback, @Nullable Runnable theOnRollback, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation, RequestPartitionId theRequestPartitionId) {
		return execute(theRequestDetails)
			.withTransactionDetails(theTransactionDetails)
			.withPropagation(thePropagation)
			.withIsolation(theIsolation)
			.withRequestPartitionId(theRequestPartitionId)
			.onRollback(theOnRollback)
			.task(theCallback);
	}

	public boolean isCustomIsolationSupported() {
		if (myTransactionManager instanceof JpaTransactionManager) {
			JpaDialect jpaDialect = ((JpaTransactionManager) myTransactionManager).getJpaDialect();
			return (jpaDialect instanceof HibernateJpaDialect);
		}
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

	@Nullable
	protected <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
		final RequestPartitionId requestPartitionId;
		if (theExecutionBuilder.myRequestPartitionId != null) {
			requestPartitionId = theExecutionBuilder.myRequestPartitionId;
		} else if (theExecutionBuilder.myRequestDetails != null) {
			requestPartitionId = myRequestPartitionHelperSvc.determineGenericPartitionForRequest(theExecutionBuilder.myRequestDetails);
		} else {
			requestPartitionId = null;
		}
		RequestPartitionId previousRequestPartitionId = null;
		if (requestPartitionId != null) {
			previousRequestPartitionId = ourRequestPartition.get();
			ourRequestPartition.set(requestPartitionId);
		}

		try {
			for (int i = 0; ; i++) {
				try {

					return doExecuteCallback(theExecutionBuilder, theCallback);

				} catch (ResourceVersionConflictException | DataIntegrityViolationException e) {
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
							.addIfMatchesType(ServletRequestDetails.class, theExecutionBuilder.myRequestDetails);
						ResourceVersionConflictResolutionStrategy conflictResolutionStrategy = (ResourceVersionConflictResolutionStrategy) CompositeInterceptorBroadcaster.doCallHooksAndReturnObject(myInterceptorBroadcaster, theExecutionBuilder.myRequestDetails, Pointcut.STORAGE_VERSION_CONFLICT, params);
						if (conflictResolutionStrategy != null && conflictResolutionStrategy.isRetry()) {
							maxRetries = conflictResolutionStrategy.getMaxRetries();
						}
					}

					if (i < maxRetries) {
						if (theExecutionBuilder.myTransactionDetails != null) {
							theExecutionBuilder.myTransactionDetails.getRollbackUndoActions().forEach(Runnable::run);
							theExecutionBuilder.myTransactionDetails.clearRollbackUndoActions();
							theExecutionBuilder.myTransactionDetails.clearResolvedItems();
							theExecutionBuilder.myTransactionDetails.clearUserData(XACT_USERDATA_KEY_RESOLVED_TAG_DEFINITIONS);
							theExecutionBuilder.myTransactionDetails.clearUserData(XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS);
						}
						double sleepAmount = (250.0d * i) * Math.random();
						long sleepAmountLong = (long) sleepAmount;
						TestUtil.sleepAtLeast(sleepAmountLong, false);

						ourLog.info("About to start a transaction retry due to conflict or constraint error. Sleeping {}ms first.", sleepAmountLong);
						continue;
					}

					IBaseOperationOutcome oo = null;
					if (e instanceof ResourceVersionConflictException) {
						oo = ((ResourceVersionConflictException) e).getOperationOutcome();
					}

					if (maxRetries > 0) {
						String msg = "Max retries (" + maxRetries + ") exceeded for version conflict: " + e.getMessage();
						ourLog.info(msg, maxRetries);
						throw new ResourceVersionConflictException(Msg.code(549) + msg);
					}

					throw new ResourceVersionConflictException(Msg.code(550) + e.getMessage(), e, oo);
				}
			}
		} finally {
			if (requestPartitionId != null) {
				ourRequestPartition.set(previousRequestPartitionId);
			}
		}
	}

	@Nullable
	protected <T> T doExecuteCallback(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
		try {
			TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);

			if (theExecutionBuilder.myPropagation != null) {
				txTemplate.setPropagationBehavior(theExecutionBuilder.myPropagation.value());
			}

			if (myCustomIsolationSupported && theExecutionBuilder.myIsolation != null && theExecutionBuilder.myIsolation != Isolation.DEFAULT) {
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

	protected class ExecutionBuilder implements IExecutionBuilder {

		private final RequestDetails myRequestDetails;
		private Isolation myIsolation;
		private Propagation myPropagation;
		private boolean myReadOnly;
		private TransactionDetails myTransactionDetails;
		private Runnable myOnRollback;
		private RequestPartitionId myRequestPartitionId;

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
		public void task(Runnable theTask) {
			ICallable<Void> task = () -> {
				theTask.run();
				return null;
			};
			task(task);
		}

		@Override
		public <T> T task(ICallable<T> theTask) {
			TransactionCallback<T> callback = tx -> theTask.call();
			return task(callback);
		}

		@Override
		public <T> T task(TransactionCallback<T> callback) {
			assert callback != null;

			return doExecute(this, callback);
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

	public static <T> T executeWithDefaultPartitionInContext(@Nonnull ICallable<T> theCallback) {
		RequestPartitionId previousRequestPartitionId = ourRequestPartition.get();
		ourRequestPartition.set(RequestPartitionId.defaultPartition());
		try {
			return theCallback.call();
		} finally {
			ourRequestPartition.set(previousRequestPartitionId);
		}
	}

	public static RequestPartitionId getRequestPartitionAssociatedWithThread() {
		return ourRequestPartition.get();
	}
}
