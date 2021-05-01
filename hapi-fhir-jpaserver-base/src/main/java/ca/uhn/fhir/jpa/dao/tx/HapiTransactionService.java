package ca.uhn.fhir.jpa.dao.tx;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;

public class HapiTransactionService {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiTransactionService.class);
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private PlatformTransactionManager myTransactionManager;
	private TransactionTemplate myTxTemplate;

	@VisibleForTesting
	public void setInterceptorBroadcaster(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@VisibleForTesting
	public void setTransactionManager(PlatformTransactionManager theTransactionManager) {
		myTransactionManager = theTransactionManager;
	}

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTransactionManager);
	}

	public <T> T execute(RequestDetails theRequestDetails, TransactionCallback<T> theCallback) {
		return execute(theRequestDetails, theCallback, null);
	}

	public <T> T execute(RequestDetails theRequestDetails, TransactionCallback<T> theCallback, Runnable theOnRollback) {

		for (int i = 0; ; i++) {
			try {

				try {
					return myTxTemplate.execute(theCallback);
				} catch (MyException e) {
					if (e.getCause() instanceof RuntimeException) {
						throw (RuntimeException) e.getCause();
					} else {
						throw new InternalErrorException(e);
					}
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
		 		 * annopying spurious failures for the client.
		 		 */
				if (e.getMessage().contains("HFJ_TAG_DEF")) {
					maxRetries = 3;
				}

				if (maxRetries == 0) {
					HookParams params = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
					ResourceVersionConflictResolutionStrategy conflictResolutionStrategy = (ResourceVersionConflictResolutionStrategy) JpaInterceptorBroadcaster.doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_VERSION_CONFLICT, params);
					if (conflictResolutionStrategy != null && conflictResolutionStrategy.isRetry()) {
						maxRetries = conflictResolutionStrategy.getMaxRetries();
					}
				}

				if (i <= maxRetries) {
					sleepAtLeast(250, false);
					continue;
				}

				if (maxRetries > 0) {
					ourLog.info("Max retries ({}) exceeded for version conflict", maxRetries);
				}

				throw e;
			}
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

	@SuppressWarnings("BusyWait")
	public static void sleepAtLeast(long theMillis, boolean theLogProgress) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() <= start + theMillis) {
			try {
				long timeSinceStarted = System.currentTimeMillis() - start;
				long timeToSleep = Math.max(0, theMillis - timeSinceStarted);
				if (theLogProgress) {
					ourLog.info("Sleeping for {}ms", timeToSleep);
				}
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				ourLog.error("Interrupted", e);
			}
		}
	}
}
