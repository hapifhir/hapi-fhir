package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTransactionManager);
	}

	public <T> T execute(RequestDetails theRequestDetails, TransactionCallback<T> theCallback) {

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

			} catch (ResourceVersionConflictException e) {
				ourLog.debug("Version conflict detected: {}", e.toString());

				HookParams params = new HookParams()
					.add(theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				ResourceVersionConflictResolutionStrategy conflictResolutionStrategy = (ResourceVersionConflictResolutionStrategy) myInterceptorBroadcaster.callHooksAndReturnObject(Pointcut.STORAGE_VERSION_CONFLICT, params);
				if (conflictResolutionStrategy.isRetry()) {
					if (i <= conflictResolutionStrategy.getMaxRetries()) {
						try {
							Thread.sleep(100 * i);
						} catch (InterruptedException e2) {
							// ignore
						}
						continue;
					}

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

}
