package ca.uhn.fhir.jpa.subscription.module.subscriber;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourceProvider {
	IBaseResource getResource(IIdType id);
}
