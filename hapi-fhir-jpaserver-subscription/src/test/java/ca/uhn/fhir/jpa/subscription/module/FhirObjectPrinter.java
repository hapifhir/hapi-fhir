package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.function.Function;

public class FhirObjectPrinter implements Function<Object, String> {
	@Override
	public String apply(Object object) {
		if (object instanceof IBaseResource) {
			IBaseResource resource = (IBaseResource) object;
			return "Resource " + resource.getIdElement().getValue();
		} else if (object instanceof ResourceDeliveryMessage) {
			ResourceDeliveryMessage resourceDeliveryMessage = (ResourceDeliveryMessage) object;
			// TODO KHS move this to ResourceModifiedMessage.toString()
			return "ResourceDelivery Message { " + resourceDeliveryMessage.getPayloadId() + "}";
		} else if (object instanceof ResourceDeliveryJsonMessage) {
			ResourceDeliveryJsonMessage resourceDeliveryJsonMessage = (ResourceDeliveryJsonMessage) object;
			// TODO KHS move this to ResourceModifiedMessage.toString()
			ResourceDeliveryMessage resourceDeliveryMessage = resourceDeliveryJsonMessage.getPayload();
			return "ResourceDeliveryJsonMessage Message { " + resourceDeliveryMessage.getPayloadId() + "}";
		} else {
			return object.toString();
		}
	}
}
