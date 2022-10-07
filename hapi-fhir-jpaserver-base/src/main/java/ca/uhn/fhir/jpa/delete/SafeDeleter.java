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

import java.util.Collections;
import java.util.List;

public class SafeDeleter {
	public static final long RETRY_BACKOFF_PERIOD = 100L;
	public static final int RETRY_MAX_ATTEMPTS = 4;

	private static final Logger ourLog = LoggerFactory.getLogger(SafeDeleter.class);
	private final DaoRegistry myDaoRegistry;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final TransactionTemplate myTxTemplate;

	private final RetryTemplate myRetryTemplate = getRetryTemplate();

	public SafeDeleter(DaoRegistry theDaoRegistry, IInterceptorBroadcaster theInterceptorBroadcaster, PlatformTransactionManager thePlatformTransactionManager) {
		myDaoRegistry = theDaoRegistry;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myTxTemplate = new TransactionTemplate(thePlatformTransactionManager);
		myTxTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
	}

	public Integer delete(RequestDetails theRequest, DeleteConflictList theConflictList, TransactionDetails theTransactionDetails) {
		Integer retVal = 0;

		List<String> cascadeDeleteIdCache = CascadingDeleteInterceptor.getCascadedDeletesList(theRequest, true);
		for (DeleteConflict next : theConflictList) {
			IdDt nextSource = next.getSourceId();
			String nextSourceId = nextSource.toUnqualifiedVersionless().getValue();

			if (!cascadeDeleteIdCache.contains(nextSourceId)) {
				cascadeDeleteIdCache.add(nextSourceId);
				retVal += handleNextSource(theRequest, theConflictList, theTransactionDetails, next, nextSource, nextSourceId);
			}
		}

		return retVal;
	}

	private Integer handleNextSource(RequestDetails theRequest, DeleteConflictList theConflictList, TransactionDetails theTransactionDetails, DeleteConflict next, IdDt nextSource, String nextSourceId) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextSource.getResourceType());

		return myRetryTemplate.execute(retryContext -> {
			try {
				if (retryContext.getRetryCount() > 0) {
					ourLog.info("Retrying delete of {} - Attempt #{}", nextSourceId, retryContext.getRetryCount());
				}
				myTxTemplate.execute(s -> doDelete(theRequest, theConflictList, theTransactionDetails, nextSource, dao));
				return 1;
			} catch (ResourceGoneException exception) {
				ourLog.info("{} is already deleted.  Skipping cascade delete of this resource", nextSourceId);
			}
			return 0;
		});
	}

	private DaoMethodOutcome doDelete(RequestDetails theRequest, DeleteConflictList
		theConflictList, TransactionDetails theTransactionDetails, IdDt nextSource, IFhirResourceDao<?> dao) {
		// Interceptor call: STORAGE_CASCADE_DELETE

		// Remove the version so we grab the latest version to delete
		IBaseResource resource = dao.read(nextSource.toVersionless(), theRequest);
		HookParams params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(DeleteConflictList.class, theConflictList)
			.add(IBaseResource.class, resource);
		CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_CASCADE_DELETE, params);

		return dao.delete(resource.getIdElement(), theConflictList, theRequest, theTransactionDetails);
	}

	private static RetryTemplate getRetryTemplate() {
		final RetryTemplate retryTemplate = new RetryTemplate();

		final FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(RETRY_BACKOFF_PERIOD);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(RETRY_MAX_ATTEMPTS, Collections.singletonMap(ResourceVersionConflictException.class, true));
		retryTemplate.setRetryPolicy(retryPolicy);

		return retryTemplate;
	}
}
