/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.mdm.api.IMdmChannelSubmitterSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.IoUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.mdm.api.IMdmSettings.EMPI_CHANNEL_NAME;

/**
 * This class is responsible for manual submissions of {@link IAnyResource} resources onto the MDM Channel.
 */
public class MdmChannelSubmitterSvcImpl implements IMdmChannelSubmitterSvc, AutoCloseable {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	private IChannelProducer<ResourceModifiedMessage> myMdmChannelProducer;

	private final FhirContext myFhirContext;

	private final IBrokerClient myBrokerClient;

	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	public MdmChannelSubmitterSvcImpl(FhirContext theFhirContext, IBrokerClient theBrokerClient, IInterceptorBroadcaster theInterceptorBroadcaster) {
		myFhirContext = theFhirContext;
		myBrokerClient = theBrokerClient;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@Override
	public void submitResourceToMdmChannel(IBaseResource theResource) {
		ResourceModifiedJsonMessage resourceModifiedJsonMessage = new ResourceModifiedJsonMessage();
		ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(
				myFhirContext,
				theResource,
				ResourceModifiedMessage.OperationTypeEnum.MANUALLY_TRIGGERED,
				null,
				(RequestPartitionId) theResource.getUserData(Constants.RESOURCE_PARTITION_ID));
		resourceModifiedMessage.setOperationType(ResourceModifiedMessage.OperationTypeEnum.MANUALLY_TRIGGERED);
		resourceModifiedJsonMessage.setPayload(resourceModifiedMessage);
		if (myInterceptorBroadcaster.hasHooks(Pointcut.MDM_SUBMIT_PRE_MESSAGE_DELIVERY)) {
			final HookParams params =
					new HookParams().add(ResourceModifiedJsonMessage.class, resourceModifiedJsonMessage);
			myInterceptorBroadcaster.callHooks(Pointcut.MDM_SUBMIT_PRE_MESSAGE_DELIVERY, params);
		}
		boolean success =
				getMdmChannelProducer().send(resourceModifiedJsonMessage).isSuccessful();
		if (!success) {
			ourLog.error("Failed to submit {} to MDM Channel.", resourceModifiedMessage.getPayloadId());
		}
	}

	protected ChannelProducerSettings getChannelProducerSettings() {
		return new ChannelProducerSettings();
	}

	private void init() {
		ChannelProducerSettings channelSettings = getChannelProducerSettings();
		channelSettings.setProducerNameSuffix("mdm-submit");
		myMdmChannelProducer = myBrokerClient.getOrCreateProducer(
				EMPI_CHANNEL_NAME, ResourceModifiedJsonMessage.class, channelSettings);
	}

	private IChannelProducer<ResourceModifiedMessage> getMdmChannelProducer() {
		if (myMdmChannelProducer == null) {
			init();
		}
		return myMdmChannelProducer;
	}

	@Override
	public void close() throws Exception {
		if (myMdmChannelProducer instanceof AutoCloseable) {
			IoUtils.closeQuietly((AutoCloseable) myMdmChannelProducer, ourLog);
		}
	}
}
