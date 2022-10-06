package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

// TODO LUKE should this be a service bean?
public class SafeDeleter {
	private static final Logger ourLog = LoggerFactory.getLogger(SafeDeleter.class);
	private final DaoRegistry myDaoRegistry;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	// TODO: LUKE:  get rid of this if this is no longer needed as part of the final solution
	private final PlatformTransactionManager myPlatformTransactionManager;
	private final TransactionTemplate myTxTemplate;

	private final RetryTemplate myRetryTemplate;

	public SafeDeleter(DaoRegistry theDaoRegistry, IInterceptorBroadcaster theInterceptorBroadcaster, PlatformTransactionManager thePlatformTransactionManager, RetryTemplate theRetryTemplate) {
		myDaoRegistry = theDaoRegistry;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myPlatformTransactionManager = thePlatformTransactionManager;
		myTxTemplate = new TransactionTemplate(thePlatformTransactionManager);
		myTxTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		myRetryTemplate = theRetryTemplate;
	}

	public Integer delete(RequestDetails theRequest, DeleteConflictList theConflictList, TransactionDetails theTransactionDetails) {
		Integer retVal = 0;

		List<String> cascadeDeleteIdCache = CascadingDeleteInterceptor.getCascadedDeletesList(theRequest, true);
		for (DeleteConflict next : theConflictList) {
			IdDt nextSource = next.getSourceId();
			String nextSourceId = nextSource.toUnqualifiedVersionless().getValue();

			if (!cascadeDeleteIdCache.contains(nextSourceId)) {
				cascadeDeleteIdCache.add(nextSourceId);
				retVal = handleNextSource(theRequest, theConflictList, theTransactionDetails, retVal, next, nextSource, nextSourceId);
			}
		}

		return retVal;
	}

	private Integer handleNextSource(RequestDetails theRequest, DeleteConflictList theConflictList, TransactionDetails theTransactionDetails, Integer retVal, DeleteConflict next, IdDt nextSource, String nextSourceId) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextSource.getResourceType());

		try {
			myTxTemplate.execute(s -> doDelete(theRequest, theConflictList, theTransactionDetails, nextSource, dao));
			++retVal;
		} catch (ResourceVersionConflictException exception) {
			ourLog.info("LUKE: ResourceVersionConflictException: {}", nextSourceId);
//			myHapiTransactionService.rollbackToSavepoint(savepoint);
//			- read the latest version, retry with latest version (max 3 retries then rethrow ResourceVersionConflictException)
			myRetryTemplate.execute(retryContext -> {
				ourLog.info("LUKE: ResourceVersionConflictException retry next: {}, retryCount: {} ", nextSourceId, retryContext.getRetryCount());
				// TODO:  try this first and
				return dao.delete(nextSource, theConflictList, theRequest, theTransactionDetails);
			});
		} catch (ResourceGoneException exception) {
			ourLog.info("LUKE: ResourceGoneException: {}", nextSourceId);
//			myHapiTransactionService.rollbackToSavepoint(savepoint);
			ourLog.info("{} is already deleted.  Skipping cascade delete of this resource", nextSourceId);
		}
		return retVal;
	}

	private DaoMethodOutcome doDelete(RequestDetails theRequest, DeleteConflictList theConflictList, TransactionDetails theTransactionDetails, IdDt nextSource, IFhirResourceDao<?> dao) {
		// Interceptor call: STORAGE_CASCADE_DELETE
		IBaseResource resource = dao.read(nextSource, theRequest);
		HookParams params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(DeleteConflictList.class, theConflictList)
			.add(IBaseResource.class, resource);
		CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_CASCADE_DELETE, params);

		return dao.delete(nextSource, theConflictList, theRequest, theTransactionDetails);
	}

	// TODO:  set this up in a Config class:  somewhere
	private RetryTemplate getRetryTemplate() {
		final long BACKOFF_PERIOD = 100L;
		final int MAX_ATTEMPTS = 4;

		RetryTemplate retryTemplate = new RetryTemplate();

		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(BACKOFF_PERIOD);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(MAX_ATTEMPTS);
		retryTemplate.setRetryPolicy(retryPolicy);

		return retryTemplate;
	}
}
