package ca.uhn.fhir.empi.api;

import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface IEmpiControllerSvc {
	IAnyResource mergePersons(String theFromPersonId, String theToPersonId, EmpiTransactionContext theEmpiTransactionContext);
	Stream<EmpiLinkJson> queryLinks(@Nullable String thePersonId, @Nullable String theTargetId, @Nullable String theMatchResult, @Nullable String theLinkSource, EmpiTransactionContext theEmpiContext);
	Stream<EmpiLinkJson> getPossibleDuplicates(EmpiTransactionContext theEmpiContext);
	IAnyResource updateLink(String thePersonId, String theTargetId, String theMatchResult, EmpiTransactionContext theEmpiContext);
	void notDuplicatePerson(String thePersonId, String theTargetPersonId, EmpiTransactionContext theEmpiContext);
}
