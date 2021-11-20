package ca.uhn.fhir.jpa.subscription.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ISubscriptionMessageHasher {
	Integer getMessageHashOrNull(IBaseResource thePayloadResource);
}
