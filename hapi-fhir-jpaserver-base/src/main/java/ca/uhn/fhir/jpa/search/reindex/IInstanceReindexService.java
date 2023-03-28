package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IInstanceReindexService {

	/**
	 * Simulate a reindex and return the details about what would change
	 */
	IBaseParameters reindexDryRun(RequestDetails theRequestDetails, IIdType theResourceId);

	/**
	 * Perform a reindex on a single resource and return details about what changed
	 */
	IBaseParameters reindex(RequestDetails theRequestDetails, IIdType theResourceId);
}
