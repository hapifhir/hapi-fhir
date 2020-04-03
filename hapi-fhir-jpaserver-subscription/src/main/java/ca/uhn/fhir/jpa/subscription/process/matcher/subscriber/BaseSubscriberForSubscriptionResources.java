package ca.uhn.fhir.jpa.subscription.process.matcher.subscriber;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;

public abstract class BaseSubscriberForSubscriptionResources implements MessageHandler {

	@Autowired
	protected FhirContext myFhirContext;

	protected boolean isSubscription(ResourceModifiedMessage theNewResource) {
		IIdType payloadId = theNewResource.getId(myFhirContext);
		String payloadIdType = payloadId.getResourceType();
		return payloadIdType.equals(ResourceTypeEnum.SUBSCRIPTION.getCode());
	}

}
