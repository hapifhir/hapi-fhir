/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.annotation.Propagation;

import java.util.Collections;
import java.util.List;

/**
 * Used by {@link CascadingDeleteInterceptor} to handle {@link DeleteConflictList}s in a thead-safe way.
 * <p>
 * Specifically, this class spawns an inner transaction for each {@link DeleteConflictList}.  This class is meant to handle any potential delete collisions (ex {@link ResourceGoneException} or {@link ResourceVersionConflictException}.  In the former case, we swallow the Exception in the inner transaction then continue.  In the latter case, we retry according to the RETRY_BACKOFF_PERIOD and RETRY_MAX_ATTEMPTS before giving up.
 */
public class ThreadSafeResourceDeleterSvc {

	public static final long RETRY_BACKOFF_PERIOD = 100L;
	public static final int RETRY_MAX_ATTEMPTS = 4;
	private static final String REQ_DET_KEY_IN_NEW_TRANSACTION =
			ThreadSafeResourceDeleterSvc.class.getName() + "REQ_DET_KEY_IN_NEW_TRANSACTION";
	private static final Logger ourLog = LoggerFactory.getLogger(ThreadSafeResourceDeleterSvc.class);
	private final DaoRegistry myDaoRegistry;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	private final RetryTemplate myRetryTemplate = getRetryTemplate();
	private final IHapiTransactionService myTransactionService;

	public ThreadSafeResourceDeleterSvc(
			DaoRegistry theDaoRegistry,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			IHapiTransactionService theTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myTransactionService = theTransactionService;
	}

	/**
	 * @return number of resources that were successfully deleted
	 */
	public Integer delete(
			RequestDetails theRequest, DeleteConflictList theConflictList, TransactionDetails theTransactionDetails) {
		Integer retVal = 0;

		List<String> cascadeDeleteIdCache = CascadingDeleteInterceptor.getCascadedDeletesList(theRequest, true);
		for (DeleteConflict next : theConflictList) {
			IdDt nextSource = next.getSourceId();
			String nextSourceId = nextSource.toUnqualifiedVersionless().getValue();

			if (!cascadeDeleteIdCache.contains(nextSourceId)) {
				cascadeDeleteIdCache.add(nextSourceId);
				retVal += handleNextSource(
						theRequest, theConflictList, theTransactionDetails, next, nextSource, nextSourceId);
			}
		}

		return retVal;
	}

	/**
	 * @return number of resources that were successfully deleted
	 */
	private Integer handleNextSource(
			RequestDetails theRequest,
			DeleteConflictList theConflictList,
			TransactionDetails theTransactionDetails,
			DeleteConflict next,
			IdDt nextSource,
			String nextSourceId) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextSource.getResourceType());

		// We will retry deletes on any occurrence of ResourceVersionConflictException up to RETRY_MAX_ATTEMPTS
		return myRetryTemplate.execute(retryContext -> {
			String previousNewTransactionValue = null;
			if (theRequest != null) {
				previousNewTransactionValue = (String) theRequest.getUserData().get(REQ_DET_KEY_IN_NEW_TRANSACTION);
			}

			try {
				if (retryContext.getRetryCount() > 0) {
					ourLog.info("Retrying delete of {} - Attempt #{}", nextSourceId, retryContext.getRetryCount());
				}

				// Avoid nesting multiple new transactions deep. This can easily cause
				// thread pools to get exhausted.
				Propagation propagation;
				if (theRequest == null || previousNewTransactionValue != null) {
					propagation = Propagation.REQUIRED;
				} else {
					theRequest.getUserData().put(REQ_DET_KEY_IN_NEW_TRANSACTION, REQ_DET_KEY_IN_NEW_TRANSACTION);
					propagation = Propagation.REQUIRES_NEW;
				}

				myTransactionService
						.withRequest(theRequest)
						.withTransactionDetails(theTransactionDetails)
						.withPropagation(propagation)
						.execute(() -> doDelete(theRequest, theConflictList, theTransactionDetails, nextSource, dao));

				return 1;
			} catch (ResourceGoneException exception) {
				ourLog.info("{} is already deleted.  Skipping cascade delete of this resource", nextSourceId);
			} finally {
				if (theRequest != null) {
					theRequest.getUserData().put(REQ_DET_KEY_IN_NEW_TRANSACTION, previousNewTransactionValue);
				}
			}

			return 0;
		});
	}

	private DaoMethodOutcome doDelete(
			RequestDetails theRequest,
			DeleteConflictList theConflictList,
			TransactionDetails theTransactionDetails,
			IdDt nextSource,
			IFhirResourceDao<?> dao) {
		// Interceptor call: STORAGE_CASCADE_DELETE

		// Remove the version so we grab the latest version to delete
		IBaseResource resource = dao.read(nextSource.toVersionless(), theRequest);
		HookParams params = new HookParams()
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(DeleteConflictList.class, theConflictList)
				.add(IBaseResource.class, resource);
		CompositeInterceptorBroadcaster.doCallHooks(
				myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_CASCADE_DELETE, params);

		return dao.delete(resource.getIdElement(), theConflictList, theRequest, theTransactionDetails);
	}

	private static RetryTemplate getRetryTemplate() {
		final RetryTemplate retryTemplate = new RetryTemplate();

		final FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(RETRY_BACKOFF_PERIOD);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(
				RETRY_MAX_ATTEMPTS, Collections.singletonMap(ResourceVersionConflictException.class, true));
		retryTemplate.setRetryPolicy(retryPolicy);

		return retryTemplate;
	}
}
