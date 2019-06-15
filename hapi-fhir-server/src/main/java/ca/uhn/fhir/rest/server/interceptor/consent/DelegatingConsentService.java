package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Implementation of {@link IConsentService} that forwards to another
 * implementation of {@link IConsentService}. This class is mostly
 * provided for testing purposes.
 */
public class DelegatingConsentService implements IConsentService {

	private IConsentService myTarget;

	@Override
	public ConsentOutcome startOperation(RequestDetails theRequestDetails) {
		return myTarget.startOperation(theRequestDetails);
	}

	@Override
	public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource) {
		return myTarget.canSeeResource(theRequestDetails, theResource);
	}

	@Override
	public ConsentOutcome seeResource(RequestDetails theRequestDetails, IBaseResource theResource) {
		return myTarget.seeResource(theRequestDetails, theResource);
	}

	@Override
	public void completeOperationSuccess(RequestDetails theRequestDetails) {
		myTarget.completeOperationSuccess(theRequestDetails);
	}

	@Override
	public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException) {
		myTarget.completeOperationFailure(theRequestDetails, theException);
	}

	public void setTarget(IConsentService theTarget) {
		myTarget = theTarget;
	}
}
