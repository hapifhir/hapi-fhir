package ca.uhn.fhir.empi.api;

import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IEmpiLinkQuerySvc {
	IBaseParameters queryLinks(IIdType thePersonId, IIdType theTargetId, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource, EmpiTransactionContext theEmpiContext);

	IBaseParameters getPossibleDuplicates(EmpiTransactionContext theEmpiContext);
}
