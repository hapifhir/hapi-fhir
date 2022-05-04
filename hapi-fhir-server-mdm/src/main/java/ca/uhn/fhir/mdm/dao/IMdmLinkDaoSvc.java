package ca.uhn.fhir.mdm.dao;

import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nullable;

public interface IMdmLinkDaoSvc {
	public IMdmLink createOrUpdateLinkEntity(IBaseResource theGoldenResource, IBaseResource theSourceResource, MdmMatchOutcome theMatchOutcome, MdmLinkSourceEnum theLinkSource, @Nullable MdmTransactionContext theMdmTransactionContext);

}
