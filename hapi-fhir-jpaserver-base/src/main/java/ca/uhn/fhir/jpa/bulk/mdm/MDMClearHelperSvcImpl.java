package ca.uhn.fhir.jpa.bulk.mdm;

import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.springframework.beans.factory.annotation.Autowired;

public class MDMClearHelperSvcImpl implements IMdmClearHelperSvc {

	@Autowired
	IDeleteExpungeSvc myDeleteExpungeSvc;

	@Override
	public IDeleteExpungeSvc<? extends IResourcePersistentId<?>> getDeleteExpungeService() {
		return myDeleteExpungeSvc;
	}
}
