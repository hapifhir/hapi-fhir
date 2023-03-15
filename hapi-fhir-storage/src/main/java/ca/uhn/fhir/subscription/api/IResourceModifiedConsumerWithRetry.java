package ca.uhn.fhir.subscription.api;

import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntityPK;

public interface IResourceModifiedConsumerWithRetry {

	boolean processResourceModified(ResourceModifiedEntityPK theResourceModifiedEntityPK);

}
