package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiQueueSubmitterSvc;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;

import javax.annotation.PostConstruct;

import static ca.uhn.fhir.empi.api.IEmpiSettings.EMPI_CHANNEL_NAME;

/**
 * This class is responsible for manual submissions of {@link IAnyResource} resources onto the Empi Queue.
 */
public class EmpiQueueSubmitterSvcImpl implements IEmpiQueueSubmitterSvc {
	@Autowired
	private IChannelNamer myChannelNamer;

	private MessageChannel myEmpiChannelProducer;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IChannelFactory myChannelFactory;

	@Override
	public void manuallySubmitResourceToEmpi(IBaseResource theResource) {
		ResourceModifiedJsonMessage resourceModifiedJsonMessage = new ResourceModifiedJsonMessage();
		ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, theResource, ResourceModifiedMessage.OperationTypeEnum.MANUALLY_TRIGGERED);
		resourceModifiedMessage.setOperationType(ResourceModifiedMessage.OperationTypeEnum.MANUALLY_TRIGGERED);
		resourceModifiedJsonMessage.setPayload(resourceModifiedMessage);
		myEmpiChannelProducer.send(resourceModifiedJsonMessage);
	}

	@PostConstruct
	private void init() {
		ChannelProducerSettings channelSettings = new ChannelProducerSettings();
		String channelName = myChannelNamer.getChannelName(EMPI_CHANNEL_NAME, channelSettings);
		myEmpiChannelProducer= myChannelFactory.getOrCreateProducer(channelName, ResourceModifiedJsonMessage.class, channelSettings);
	}
}
