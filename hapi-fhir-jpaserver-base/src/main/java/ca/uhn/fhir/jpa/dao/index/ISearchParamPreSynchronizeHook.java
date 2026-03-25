package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;

import java.util.Collection;

/**
 * Used by {@link DaoSearchParamSynchronizer} to provide a callback for specific
 * param index types.
 * <b>THIS IS NOT A PUBLIC API</b>
 */
interface ISearchParamPreSynchronizeHook<T extends BaseResourceIndex> {

	void preSave(
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			Collection<T> theParamsToRemove,
			Collection<T> theParamsToAdd);
}
