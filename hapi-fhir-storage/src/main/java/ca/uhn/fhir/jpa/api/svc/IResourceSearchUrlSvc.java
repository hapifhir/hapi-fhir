package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

public interface IResourceSearchUrlSvc {
	void saveResourceSearchUrl(String theCanonicalizedUrlForStorage, IResourcePersistentId theResourcePersistentId);
}
