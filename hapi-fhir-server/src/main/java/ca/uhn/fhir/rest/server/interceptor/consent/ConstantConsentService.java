package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Consent Service that returns a fixed verdict.
 */
public class ConstantConsentService implements IConsentService {
	@Nonnull
	final ConsentOutcome myResult;

	public static ConstantConsentService constantService(ConsentOperationStatusEnum theResult) {
		return new ConstantConsentService(new ConsentOutcome(theResult));
	}

	public ConstantConsentService(@Nonnull ConsentOutcome theResult) {
		myResult = theResult;
	}

	private @Nonnull ConsentOutcome getOutcome() {
		return myResult;
	}

	@Override
	public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return getOutcome();
	}

	@Override
	public boolean shouldProcessCanSeeResource(
			RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return myResult.getStatus().isActiveVote();
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
