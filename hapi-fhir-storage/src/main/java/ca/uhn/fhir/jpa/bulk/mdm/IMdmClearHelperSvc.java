package ca.uhn.fhir.jpa.bulk.mdm;

import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

public interface IMdmClearHelperSvc {

	IDeleteExpungeSvc<? extends IResourcePersistentId<?>> getDeleteExpungeService();
}
