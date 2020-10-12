package ca.uhn.fhir.jpa.empi.svc;

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
import ca.uhn.fhir.empi.api.IEmpiChannelSubmitterSvc;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;

import static ca.uhn.fhir.empi.api.IEmpiSettings.EMPI_CHANNEL_NAME;

/**
 * This class is responsible for manual submissions of {@link IAnyResource} resources onto the Empi Channel.
 */
public class EmpiChannelSubmitterSvcImpl implements IEmpiChannelSubmitterSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	private MessageChannel myEmpiChannelProducer;

	private FhirContext myFhirContext;

	private IChannelFactory myChannelFactory;

	@Override
	public void submitResourceToEmpiChannel(IBaseResource theResource) {
		ResourceModifiedJsonMessage resourceModifiedJsonMessage = new ResourceModifiedJsonMessage();
		ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, theResource, ResourceModifiedMessage.OperationTypeEnum.MANUALLY_TRIGGERED);
		resourceModifiedMessage.setOperationType(ResourceModifiedMessage.OperationTypeEnum.MANUALLY_TRIGGERED);
		resourceModifiedJsonMessage.setPayload(resourceModifiedMessage);
		boolean success = getEmpiChannelProducer().send(resourceModifiedJsonMessage);
		if (!success) {
			ourLog.error("Failed to submit {} to EMPI Channel.", resourceModifiedMessage.getPayloadId());
		}
	}

	@Autowired
	public EmpiChannelSubmitterSvcImpl(FhirContext theFhirContext, IChannelFactory theIChannelFactory) {
		myFhirContext = theFhirContext;
		myChannelFactory = theIChannelFactory;
	}

	private void init() {
		ChannelProducerSettings channelSettings = new ChannelProducerSettings();
		myEmpiChannelProducer= myChannelFactory.getOrCreateProducer(EMPI_CHANNEL_NAME, ResourceModifiedJsonMessage.class, channelSettings);
	}

	private MessageChannel getEmpiChannelProducer() {
		if (myEmpiChannelProducer == null) {
			init();
		}
		return myEmpiChannelProducer;
	}
}
