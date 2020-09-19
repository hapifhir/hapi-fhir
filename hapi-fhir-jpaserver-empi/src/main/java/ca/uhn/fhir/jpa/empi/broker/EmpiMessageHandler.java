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
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchLinkSvc;
import ca.uhn.fhir.jpa.empi.svc.EmpiResourceFilteringSvc;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class EmpiMessageHandler implements MessageHandler {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private EmpiMatchLinkSvc myEmpiMatchLinkSvc;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private EmpiResourceFilteringSvc myEmpiResourceFileringSvc;

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		ourLog.info("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		try {
			if (myEmpiResourceFileringSvc.shouldBeProcessed(getResourceFromPayload(msg))) {
				matchEmpiAndUpdateLinks(msg);
			}
		} catch (Exception e) {
			ourLog.error("Failed to handle EMPI Matching Resource:", e);
			throw e;
		}
	}
	public void matchEmpiAndUpdateLinks(ResourceModifiedMessage theMsg) {
		String resourceType = theMsg.getId(myFhirContext).getResourceType();
		validateResourceType(resourceType);
		EmpiTransactionContext empiContext =  createEmpiContext(theMsg);
		try {
			switch (theMsg.getOperationType()) {
				case CREATE:
					handleCreatePatientOrPractitioner(theMsg, empiContext);
					break;
				case UPDATE:
				case MANUALLY_TRIGGERED:
					handleUpdatePatientOrPractitioner(theMsg, empiContext);
					break;
				case DELETE:
				default:
					ourLog.trace("Not processing modified message for {}", theMsg.getOperationType());
			}
		}catch (Exception e) {
			log(empiContext, "Failure during EMPI processing: " + e.getMessage(), e);
		} finally {
			// Interceptor call: EMPI_AFTER_PERSISTED_RESOURCE_CHECKED
			HookParams params = new HookParams()
				.add(ResourceModifiedMessage.class, theMsg)
				.add(TransactionLogMessages.class, empiContext.getTransactionLogMessages());
			myInterceptorBroadcaster.callHooks(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED, params);
		}
	}

	private EmpiTransactionContext createEmpiContext(ResourceModifiedMessage theMsg) {
		TransactionLogMessages transactionLogMessages = TransactionLogMessages.createFromTransactionGuid(theMsg.getParentTransactionGuid());
		EmpiTransactionContext.OperationType empiOperation;
		switch (theMsg.getOperationType()) {
			case CREATE:
				empiOperation = EmpiTransactionContext.OperationType.CREATE;
				break;
			case UPDATE:
				empiOperation = EmpiTransactionContext.OperationType.UPDATE;
				break;
			case MANUALLY_TRIGGERED:
				empiOperation = EmpiTransactionContext.OperationType.BATCH;
				break;
			case DELETE:
			default:
				ourLog.trace("Not creating an EmpiTransactionContext for {}", theMsg.getOperationType());
				throw new InvalidRequestException("We can't handle non-update/create operations in EMPI");
		}
		return new EmpiTransactionContext(transactionLogMessages, empiOperation);
	}

	private void validateResourceType(String theResourceType) {
		if (!EmpiUtil.supportedTargetType(theResourceType)) {
			throw new IllegalStateException("Unsupported resource type submitted to EMPI matching queue: " + theResourceType);
		}
	}

	private void handleCreatePatientOrPractitioner(ResourceModifiedMessage theMsg, EmpiTransactionContext theEmpiTransactionContext) {
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(getResourceFromPayload(theMsg), theEmpiTransactionContext);
	}

	private IAnyResource getResourceFromPayload(ResourceModifiedMessage theMsg) {
		return (IAnyResource) theMsg.getNewPayload(myFhirContext);
	}

	private void handleUpdatePatientOrPractitioner(ResourceModifiedMessage theMsg, EmpiTransactionContext theEmpiTransactionContext) {
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(getResourceFromPayload(theMsg), theEmpiTransactionContext);
	}

	private void log(EmpiTransactionContext theEmpiContext, String theMessage) {
		theEmpiContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}

	private void log(EmpiTransactionContext theEmpiContext, String theMessage, Exception theException) {
		theEmpiContext.addTransactionLogMessage(theMessage);
		ourLog.error(theMessage, theException);
	}
}
