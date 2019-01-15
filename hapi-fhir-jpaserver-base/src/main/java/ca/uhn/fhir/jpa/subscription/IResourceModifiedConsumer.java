package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;

public interface IResourceModifiedConsumer {
	void submitResourceModified(ResourceModifiedMessage theMsg);
}
