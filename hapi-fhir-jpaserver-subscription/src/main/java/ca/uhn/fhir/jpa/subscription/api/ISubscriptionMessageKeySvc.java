package ca.uhn.fhir.jpa.subscription.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nullable;

public interface ISubscriptionMessageKeySvc {
	@Nullable
	String getMessageKeyOrNull(IBaseResource thePayloadResource);
}
