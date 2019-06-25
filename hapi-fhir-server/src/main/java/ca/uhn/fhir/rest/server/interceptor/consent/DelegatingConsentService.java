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
	public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return myTarget.startOperation(theRequestDetails, theContextServices);
	}

	@Override
	public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return myTarget.canSeeResource(theRequestDetails, theResource, theContextServices);
	}

	@Override
	public ConsentOutcome seeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return myTarget.seeResource(theRequestDetails, theResource ,theContextServices);
	}

	@Override
	public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		myTarget.completeOperationSuccess(theRequestDetails, theContextServices);
	}

	@Override
	public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
		myTarget.completeOperationFailure(theRequestDetails, theException, theContextServices);
	}

	public void setTarget(IConsentService theTarget) {
		myTarget = theTarget;
	}
}
