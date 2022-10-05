package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;

// TODO LUKE should this be a service bean?
public class SafeDeleter {
	private static final Logger ourLog = LoggerFactory.getLogger(SafeDeleter.class);
	private final DaoRegistry myDaoRegistry;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final HapiTransactionService myHapiTransactionService;

	public SafeDeleter(DaoRegistry theDaoRegistry, IInterceptorBroadcaster theInterceptorBroadcaster, HapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myHapiTransactionService = theHapiTransactionService;
	}

	public Integer delete(RequestDetails theRequest, DeleteConflictList theConflictList, TransactionDetails theTransactionDetails) {
		Integer retVal = 0;

		List<String> cascadeDeleteIdCache = CascadingDeleteInterceptor.getCascadedDeletesList(theRequest, true);
		for (DeleteConflict next : theConflictList) {
			IdDt nextSource = next.getSourceId();
			String nextSourceId = nextSource.toUnqualifiedVersionless().getValue();

			if (!cascadeDeleteIdCache.contains(nextSourceId)) {
				cascadeDeleteIdCache.add(nextSourceId);

				IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextSource.getResourceType());

				// Interceptor call: STORAGE_CASCADE_DELETE
				IBaseResource resource = dao.read(nextSource, theRequest);
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(DeleteConflictList.class, theConflictList)
					.add(IBaseResource.class, resource);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_CASCADE_DELETE, params);

				// Actually perform the delete
				ourLog.info("Have delete conflict {} - Cascading delete", next);
				// TODO:  add a transaction checkpoint here and then try-catch to handle this
//				final TransactionStatus savepoint = myHapiTransactionService.savepoint();
				// FIXME LUKE: do we need this?
//				theTransactionDetails.setSavepoint(savepoint);

				final RetryTemplate retryTemplate = getRetryTemplate();

				try {
					final DaoMethodOutcome result = retryTemplate.execute(retryContext -> {
						ourLog.info("LUKE: retry next: {}, retryCount: {} ", nextSourceId, retryContext.getRetryCount());
						try {
							final DaoMethodOutcome outcome = dao.delete(nextSource, theConflictList, theRequest, theTransactionDetails);
							dao.flush();
							return outcome;
						} catch (Throwable exception) {
							// TODO:  LUKE:  clean this up once testing is complete
							ourLog.error("LUKE RETRY: " + exception.getMessage(), exception);
							throw exception;
						}
					});
					ourLog.info("LUKE: past retry next: {}", nextSourceId);
				} catch (Throwable exception) {
					// TODO:  LUKE:  clean this up once testing is complete
					ourLog.error("LUKE OUTSIDE RETRY: " + exception.getMessage(), exception);
					throw exception;
				}
				++retVal;
			}
		}

		return retVal;
	}

	// TODO:  set this up in a Config class:  somewhere
	private RetryTemplate getRetryTemplate() {
		final long BACKOFF_PERIOD = 100L;
		final int MAX_ATTEMPTS = 2;

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
