package ca.uhn.fhir.empi.api;

import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

public interface IEmpiLinkUpdaterSvc {
	IAnyResource updateLink(IAnyResource thePerson, IAnyResource theTarget, EmpiMatchResultEnum theMatchResult, EmpiTransactionContext theEmpiContext);
}
