package ca.uhn.fhir.rest.server.messaging;

public interface IMessageDeliveryContext {
	int getRetryCount();
}
