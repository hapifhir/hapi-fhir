package ca.uhn.fhir.empi.api;

import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IEmpiLinkQuerySvc {
	public IBaseParameters queryLinks(IIdType thePersonId, IIdType theTargetId, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource, EmpiTransactionContext theEmpiContext);
}
