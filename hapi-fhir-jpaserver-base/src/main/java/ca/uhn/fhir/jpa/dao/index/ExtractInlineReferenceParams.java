package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ExtractInlineReferenceParams {
	private IBaseResource myResource;
	private TransactionDetails myTransactionDetails;
	private RequestDetails myRequestDetails;
	private boolean myFailOnInvalidReferences;

	public ExtractInlineReferenceParams(
		IBaseResource theResource,
		TransactionDetails theTransactionDetails,
		RequestDetails theRequest
	) {
		myResource = theResource;
		myTransactionDetails = theTransactionDetails;
		myRequestDetails = theRequest;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	public void setResource(IBaseResource theResource) {
		myResource = theResource;
	}

	public TransactionDetails getTransactionDetails() {
		return myTransactionDetails;
	}

	public void setTransactionDetails(TransactionDetails theTransactionDetails) {
		myTransactionDetails = theTransactionDetails;
	}

	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	public void setRequestDetails(RequestDetails theRequestDetails) {
		myRequestDetails = theRequestDetails;
	}

	public boolean isFailOnInvalidReferences() {
		return myFailOnInvalidReferences;
	}

	public void setFailOnInvalidReferences(boolean theFailOnInvalidReferences) {
		myFailOnInvalidReferences = theFailOnInvalidReferences;
	}
}
