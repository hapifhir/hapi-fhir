package ca.uhn.fhir.jpa.subscription.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nullable;

/**
 * This is used by "message" type subscriptions to provide a key to the message wrapper before submitting it to the channel
 */
public interface ISubscriptionMessageKeySvc {
	/**
	 *  Given an {@link IBaseResource}, return a key that can be used to identify the message. This key will be used to
	 *  partition the message into a queue.
	 *
	 * @param thePayloadResource the payload resource.
	 * @return the key or null.
	 */
	@Nullable
	String getMessageKeyOrNull(IBaseResource thePayloadResource);
}
