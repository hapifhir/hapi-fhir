package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

import java.util.Date;

public interface IResourceSearchUrlSvc {
	void saveResourceSearchUrl(String theCanonicalizedUrlForStorage, IResourcePersistentId theResourcePersistentId);

	void deleteEntriesOlderThan(Date theCutoffDate);
}
