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

	/**
	 * Isolation: REQUIRED
	 */
	protected TransactionTemplate myTxTemplate;
	private boolean myCustomIsolationSupported;

	@VisibleForTesting
	public void setInterceptorBroadcaster(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTransactionManager);
		myCustomIsolationSupported = isCustomIsolationSupported();
	}

	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull TransactionCallback<T> theCallback) {
		return execute(theRequestDetails, theTransactionDetails, theCallback, null);
	}

	public void execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation, @Nonnull Runnable theCallback) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				theCallback.run();
			}
		};
		execute(theRequestDetails, theTransactionDetails, callback, null, thePropagation, theIsolation);
	}

	@Override
	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation, @Nonnull ICallable<T> theCallback) {
		TransactionCallback<T> callback = tx -> theCallback.call();
		return execute(theRequestDetails, theTransactionDetails, callback, null, thePropagation, theIsolation);
	}

	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull TransactionCallback<T> theCallback, @Nullable Runnable theOnRollback) {
		return execute(theRequestDetails, theTransactionDetails, theCallback, theOnRollback, Propagation.REQUIRED, Isolation.DEFAULT);
	}

	@SuppressWarnings("ConstantConditions")
	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull TransactionCallback<T> theCallback, @Nullable Runnable theOnRollback, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation) {
		assert theCallback != null;

		final RequestPartitionId requestPartitionId;
		if (theRequestDetails != null) {
			requestPartitionId = myRequestPartitionHelperSvc.determineGenericPartitionForRequest(theRequestDetails);
		} else {
			requestPartitionId = null;
		}
		return execute(theRequestDetails, theTransactionDetails, theCallback, theOnRollback, thePropagation, theIsolation, requestPartitionId);

	}

	public <T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull TransactionCallback<T> theCallback, @Nullable Runnable theOnRollback, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation, RequestPartitionId theRequestPartitionId) {
		RequestPartitionId previousRequestPartitionId = null;
		if (theRequestPartitionId != null) {
			previousRequestPartitionId = ourRequestPartition.get();
			ourRequestPartition.set(theRequestPartitionId);
		}

		try {
			for (int i = 0; ; i++) {
				try {

					if (thePropagation == Propagation.REQUIRED && theIsolation == Isolation.DEFAULT) {
						return doExecuteCallback(theCallback);
					} else {
						return doExecuteCallbackReqNew(theCallback, thePropagation, theIsolation);
					}

				} catch (ResourceVersionConflictException | DataIntegrityViolationException e) {
					ourLog.debug("Version conflict detected", e);

					if (theOnRollback != null) {
						theOnRollback.run();
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
							.add(RequestDetails.class, theRequestDetails)
							.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
						ResourceVersionConflictResolutionStrategy conflictResolutionStrategy = (ResourceVersionConflictResolutionStrategy) CompositeInterceptorBroadcaster.doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_VERSION_CONFLICT, params);
						if (conflictResolutionStrategy != null && conflictResolutionStrategy.isRetry()) {
							maxRetries = conflictResolutionStrategy.getMaxRetries();
						}
					}

					if (i < maxRetries) {
						if (theTransactionDetails != null) {
							theTransactionDetails.getRollbackUndoActions().forEach(t -> t.run());
							theTransactionDetails.clearRollbackUndoActions();
							theTransactionDetails.clearResolvedItems();
							theTransactionDetails.clearUserData(XACT_USERDATA_KEY_RESOLVED_TAG_DEFINITIONS);
							theTransactionDetails.clearUserData(XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS);
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
			if (theRequestPartitionId != null) {
				ourRequestPartition.set(previousRequestPartitionId);
			}
		}
	}

	/**
	 * Execute the callback in a transaction with REQUIRED propagation level
	 */
	@Nullable
	protected <T> T doExecuteCallback(TransactionCallback<T> theCallback) {
		try {
			return myTxTemplate.execute(theCallback);
		} catch (MyException e) {
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			} else {
				throw new InternalErrorException(Msg.code(2145) + e);
			}
		}
	}

	/**
	 * Execute the callback in a transaction with REQUIRES_NEW propagation level
	 */
	@Nullable
	protected <T> T doExecuteCallbackReqNew(TransactionCallback<T> theCallback, Propagation thePropagation, Isolation theIsolation) {
		try {
			TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
			txTemplate.setPropagationBehavior(thePropagation.value());
			if (myCustomIsolationSupported && theIsolation != Isolation.DEFAULT) {
				txTemplate.setIsolationLevel(theIsolation.value());
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

	/**
	 * This is just an unchecked exception so that we can catch checked exceptions inside TransactionTemplate
	 * and rethrow them outside of it
	 */
	static class MyException extends RuntimeException {

		public MyException(Throwable theThrowable) {
			super(theThrowable);
		}
	}
}
