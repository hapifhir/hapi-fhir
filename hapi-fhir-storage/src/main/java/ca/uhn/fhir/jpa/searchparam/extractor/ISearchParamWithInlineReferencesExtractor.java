package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ISearchParamWithInlineReferencesExtractor {

	void extractInlineReferences(RequestDetails theRequestDetails, IBaseResource theResource, TransactionDetails theTransactionDetails);
}
