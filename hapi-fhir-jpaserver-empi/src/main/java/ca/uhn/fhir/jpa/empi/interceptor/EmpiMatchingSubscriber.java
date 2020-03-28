package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchLinkSvc;
import ca.uhn.fhir.jpa.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceModifiedJsonMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class EmpiMatchingSubscriber implements MessageHandler {
	private Logger ourLog = LoggerFactory.getLogger(EmpiMatchingSubscriber.class);
	@Autowired
	private EmpiMatchLinkSvc myEmpiMatchLinkSvc;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private FhirContext myFhirContext;

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		ourLog.trace("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		matchEmpiAndUpdateLinks(msg);
	}

	public void matchEmpiAndUpdateLinks(ResourceModifiedMessage theMsg) {
		String resourceType = theMsg.getId(myFhirContext).getResourceType();
		validateResourceType(resourceType);
		try {
			switch (theMsg.getOperationType()) {
				case CREATE:
					handleCreatePatientOrPractitioner(theMsg);
					break;
				case UPDATE:
					//FIXME EMPI implement updates.
					break;
				case DELETE:
				default:
					ourLog.trace("Not processing modified message for {}", theMsg.getOperationType());
			}
		} finally {
			// Interceptor call: EMPI_AFTER_PERSISTED_RESOURCE_CHECKED
			HookParams params = new HookParams()
				.add(ResourceModifiedMessage.class, theMsg);
			myInterceptorBroadcaster.callHooks(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED, params);
		}
	}

	private void validateResourceType(String theResourceType) {
		if (!EmpiUtil.supportedResourceType(theResourceType)) {
			throw new IllegalStateException("Unsupported resource type submitted to EMPI matching queue: " + theResourceType);
		}
	}

	private void handleCreatePatientOrPractitioner(ResourceModifiedMessage theMsg) {
		myEmpiMatchLinkSvc.updateEmpiLinksForPatient(theMsg.getNewPayload(myFhirContext));
	}
}
