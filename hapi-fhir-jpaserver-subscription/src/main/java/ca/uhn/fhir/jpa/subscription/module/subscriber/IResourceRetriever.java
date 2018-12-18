package ca.uhn.fhir.jpa.subscription.module.subscriber;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourceRetriever {
	IBaseResource getResource(IIdType id);
}
