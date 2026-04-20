package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceTracingLogger {

	private static final Logger ourLog = LoggerFactory.getLogger(PerformanceTracingLogger.class);

	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	public PerformanceTracingLogger(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	public void firePerformanceInfo(RequestDetails theRequest, String theMessage) {
		// Only log at debug level since these messages aren't considered important enough
		// that we should be cluttering the system log, but they are important to the
		// specific query being executed to we'll INFO level them there
		ourLog.debug(theMessage);
		firePerformanceMessage(theRequest, theMessage, Pointcut.JPA_PERFTRACE_INFO);
	}

	public void firePerformanceWarning(RequestDetails theRequest, String theMessage) {
		ourLog.warn(theMessage);
		firePerformanceMessage(theRequest, theMessage, Pointcut.JPA_PERFTRACE_WARNING);
	}

	private void firePerformanceMessage(RequestDetails theRequest, String theMessage, Pointcut thePointcut) {
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);
		if (compositeBroadcaster.hasHooks(thePointcut)) {
			StorageProcessingMessage message = new StorageProcessingMessage();
			message.setMessage(theMessage);
			HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(StorageProcessingMessage.class, message);
			compositeBroadcaster.callHooks(thePointcut, params);
		}
	}
}
