package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.dao.IMdmLinkImplFactory;

public class JpaMdmLinkImplFactory implements IMdmLinkImplFactory {
	@Override
	public IMdmLink newMdmLinkImpl() {
		return new MdmLink();
	}
}
