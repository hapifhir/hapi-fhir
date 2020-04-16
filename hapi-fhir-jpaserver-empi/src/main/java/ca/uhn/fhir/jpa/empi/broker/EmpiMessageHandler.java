package ca.uhn.fhir.jpa.empi.broker;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchLinkSvc;
import ca.uhn.fhir.jpa.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class EmpiMessageHandler implements MessageHandler {
	private Logger ourLog = LoggerFactory.getLogger(EmpiMessageHandler.class);
	@Autowired
	private EmpiMatchLinkSvc myEmpiMatchLinkSvc;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private FhirContext myFhirContext;

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		ourLog.info("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		try {
			matchEmpiAndUpdateLinks(msg);
		} catch (Exception e) {
			ourLog.error("Failed to handle EMPI Matching Resource:", e);
			throw e;
		}
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
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(theMsg.getNewPayload(myFhirContext));
	}
}
