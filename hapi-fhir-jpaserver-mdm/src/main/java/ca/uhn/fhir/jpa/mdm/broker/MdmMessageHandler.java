/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
package ca.uhn.fhir.jpa.mdm.broker;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.mdm.svc.IMdmModelConverterSvc;
import ca.uhn.fhir.jpa.mdm.svc.MdmMatchLinkSvc;
import ca.uhn.fhir.jpa.mdm.svc.MdmResourceFilteringSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.TooManyCandidatesException;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicUtil;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkEvent;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class MdmMessageHandler implements MessageHandler {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmMatchLinkSvc myMdmMatchLinkSvc;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private MdmResourceFilteringSvc myMdmResourceFilteringSvc;

	@Autowired
	private IMdmSettings myMdmSettings;

	@Autowired
	private IMdmModelConverterSvc myModelConverter;

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		ourLog.trace("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		try {
			IBaseResource sourceResource = extractSourceResource(msg);

			boolean toProcess = myMdmResourceFilteringSvc.shouldBeProcessed((IAnyResource) sourceResource);
			if (toProcess) {
				matchMdmAndUpdateLinks(sourceResource, msg);
			}
		} catch (Exception e) {
			ourLog.error("Failed to handle MDM Matching Resource:", e);
			throw e;
		}
	}

	private IBaseResource extractSourceResource(ResourceModifiedMessage theResourceModifiedMessage) {
		IBaseResource sourceResource = theResourceModifiedMessage.getNewPayload(myFhirContext);
		if (myFhirContext.getVersion().getVersion() == FhirVersionEnum.R5 && sourceResource instanceof IBaseBundle) {
			return SubscriptionTopicUtil.extractResourceFromBundle(myFhirContext, (IBaseBundle) sourceResource);
		} else {
			return sourceResource;
		}
	}

	private void matchMdmAndUpdateLinks(IBaseResource theSourceResource, ResourceModifiedMessage theMsg) {

		String resourceType = theSourceResource.getIdElement().getResourceType();
		validateResourceType(resourceType);

		if (myInterceptorBroadcaster.hasHooks(Pointcut.MDM_BEFORE_PERSISTED_RESOURCE_CHECKED)) {
			HookParams params = new HookParams().add(IBaseResource.class, theSourceResource);
			myInterceptorBroadcaster.callHooks(Pointcut.MDM_BEFORE_PERSISTED_RESOURCE_CHECKED, params);
		}

		theSourceResource.setUserData(Constants.RESOURCE_PARTITION_ID, theMsg.getPartitionId());

		MdmTransactionContext mdmContext = createMdmContext(theMsg, resourceType);
		try {
			switch (theMsg.getOperationType()) {
				case CREATE:
					handleCreateResource(theSourceResource, mdmContext);
					break;
				case UPDATE:
				case MANUALLY_TRIGGERED:
					handleUpdateResource(theSourceResource, mdmContext);
					break;
				case DELETE:
				default:
					ourLog.trace("Not processing modified message for {}", theMsg.getOperationType());
			}
		} catch (Exception e) {
			if (e instanceof TooManyCandidatesException) {
				ourLog.debug(
						"Failed to handle MDM Matching for resource: {} since candidate matches exceeded the "
								+ "candidate search limit",
						theSourceResource.getIdElement());
			}
			log(mdmContext, "Failure during MDM processing: " + e.getMessage(), e);
			mdmContext.addTransactionLogMessage(e.getMessage());
		} finally {
			// Interceptor call: MDM_AFTER_PERSISTED_RESOURCE_CHECKED
			HookParams params = new HookParams()
					.add(ResourceOperationMessage.class, getOutgoingMessage(theMsg))
					.add(TransactionLogMessages.class, mdmContext.getTransactionLogMessages())
					.add(MdmLinkEvent.class, buildLinkChangeEvent(mdmContext));

			myInterceptorBroadcaster.callHooks(Pointcut.MDM_AFTER_PERSISTED_RESOURCE_CHECKED, params);
		}
	}

	private MdmTransactionContext createMdmContext(ResourceModifiedMessage theMsg, String theResourceType) {
		TransactionLogMessages transactionLogMessages =
				TransactionLogMessages.createFromTransactionGuid(theMsg.getTransactionId());
		MdmTransactionContext.OperationType mdmOperation;
		switch (theMsg.getOperationType()) {
			case CREATE:
				mdmOperation = MdmTransactionContext.OperationType.CREATE_RESOURCE;
				break;
			case UPDATE:
				mdmOperation = MdmTransactionContext.OperationType.UPDATE_RESOURCE;
				break;
			case MANUALLY_TRIGGERED:
				mdmOperation = MdmTransactionContext.OperationType.SUBMIT_RESOURCE_TO_MDM;
				break;
			case DELETE:
			default:
				ourLog.trace("Not creating an MdmTransactionContext for {}", theMsg.getOperationType());
				throw new InvalidRequestException(
						Msg.code(734) + "We can't handle non-update/create operations in MDM");
		}
		return new MdmTransactionContext(transactionLogMessages, mdmOperation, theResourceType);
	}

	private void validateResourceType(String theResourceType) {
		if (!myMdmSettings.isSupportedMdmType(theResourceType)) {
			throw new IllegalStateException(
					Msg.code(735) + "Unsupported resource type submitted to MDM matching queue: " + theResourceType);
		}
	}

	private void handleCreateResource(IBaseResource theResource, MdmTransactionContext theMdmTransactionContext) {
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource((IAnyResource) theResource, theMdmTransactionContext);
	}

	private void handleUpdateResource(IBaseResource theResource, MdmTransactionContext theMdmTransactionContext) {
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource((IAnyResource) theResource, theMdmTransactionContext);
	}

	private void log(MdmTransactionContext theMdmContext, String theMessage, Exception theException) {
		theMdmContext.addTransactionLogMessage(theMessage);
		ourLog.error(theMessage, theException);
	}

	private MdmLinkEvent buildLinkChangeEvent(MdmTransactionContext theMdmContext) {
		MdmLinkEvent linkChangeEvent = new MdmLinkEvent();
		theMdmContext.getMdmLinks().stream().forEach(l -> {
			linkChangeEvent.addMdmLink(myModelConverter.toJson(l));
		});

		return linkChangeEvent;
	}

	private ResourceOperationMessage getOutgoingMessage(ResourceModifiedMessage theMsg) {
		IBaseResource targetResource = theMsg.getPayload(myFhirContext);
		ResourceOperationMessage outgoingMsg =
				new ResourceOperationMessage(myFhirContext, targetResource, theMsg.getOperationType());
		outgoingMsg.setTransactionId(theMsg.getTransactionId());

		return outgoingMsg;
	}
}
