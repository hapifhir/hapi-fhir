package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

public interface IMdmClearHelperSvc<T extends IResourcePersistentId<?>> {

	IDeleteExpungeSvc<T> getDeleteExpungeSvc();
}
