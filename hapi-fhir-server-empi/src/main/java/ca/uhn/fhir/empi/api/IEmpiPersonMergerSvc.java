package ca.uhn.fhir.empi.api;

import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

public interface IEmpiPersonMergerSvc {
	/**
	 * Move all links from the thePersonToDelete to thePersonToKeep and then delete thePersonToDelete.  Merge all Person
	 * fields, with fields in thePersonToKeep overriding fields in thePersonToDelete
	 * @param thePersonToDelete the person we are merging from
	 * @param thePersonToKeep the person we are merging to
	 * @return updated thePersonToKeep with the merged fields and links.
	 */
	IAnyResource mergePersons(IAnyResource thePersonToDelete, IAnyResource thePersonToKeep, EmpiTransactionContext theEmpiTransactionContext);
}
