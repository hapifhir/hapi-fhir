package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourceVersionMap {
	String getVersion(IIdType theResourceId);

	int size();

	long populateInto(ResourceVersionCache theResourceVersionCache, IVersionChangeListener theConsumer);
}
