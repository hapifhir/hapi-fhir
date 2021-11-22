package ca.uhn.fhir.jpa.subscription.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nullable;

/**
 * This is used by "message" type subscriptions to provide a key to the message wrapper before submitting it to the channel
 */
public interface ISubscriptionMessageKeySvc {
	@Nullable
	String getMessageKeyOrNull(IBaseResource thePayloadResource);
}
