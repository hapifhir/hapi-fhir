package ca.uhn.fhir.jpa.bulk.mdm;

import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IMdmClearHelperSvc;
import ca.uhn.fhir.jpa.model.dao.JpaPid;

public class MdmClearHelperSvcImpl implements IMdmClearHelperSvc<JpaPid> {

	private final IDeleteExpungeSvc<JpaPid> myDeleteExpungeSvc;

	public MdmClearHelperSvcImpl(IDeleteExpungeSvc<JpaPid> theDeleteExpungeSvc) {
		myDeleteExpungeSvc = theDeleteExpungeSvc;
	}

	@Override
	public IDeleteExpungeSvc<JpaPid> getDeleteExpungeSvc() {
		return myDeleteExpungeSvc;
	}
}
