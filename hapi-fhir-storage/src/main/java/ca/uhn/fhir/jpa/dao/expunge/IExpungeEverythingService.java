package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.rest.api.server.RequestDetails;

import javax.annotation.Nullable;

public interface IExpungeEverythingService {
	void expungeEverything(@Nullable RequestDetails theRequest);

	int expungeEverythingByType(Class<?> theEntityType);
}
