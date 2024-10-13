package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;

/**
 * Consent Service that returns a fixed verdict.
 */
public class ConstantConsentService implements IConsentService {
	final ConsentOperationStatusEnum myResult;

	public static ConstantConsentService constantService(ConsentOperationStatusEnum theResult) {
		return new ConstantConsentService(theResult);
	}

	public ConstantConsentService(ConsentOperationStatusEnum theResult) {
		myResult = theResult;
	}

	private @Nonnull ConsentOutcome getOutcome() {
		switch (myResult) {
			case REJECT:
				return ConsentOutcome.REJECT;
			case PROCEED:
				return ConsentOutcome.PROCEED;
			case AUTHORIZED:
				return ConsentOutcome.AUTHORIZED;
			default:
				return new ConsentOutcome(myResult);
		}
	}

	@Override
	public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return getOutcome();
	}

	@Override
	public boolean shouldProcessCanSeeResource(
			RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return myResult.isActiveVote();
	}

	@Override
	public ConsentOutcome canSeeResource(
			RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return getOutcome();
	}

	@Override
	public ConsentOutcome willSeeResource(
			RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return getOutcome();
	}
}
