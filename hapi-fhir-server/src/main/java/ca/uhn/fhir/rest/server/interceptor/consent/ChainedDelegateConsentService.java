package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;

/**
 * IConsentService combiner over several delegates.
 */
public class ChainedDelegateConsentService implements IConsentService {
	private final Collection<IConsentService> myDelegates;

	public ChainedDelegateConsentService(Collection<IConsentService> theDelegates) {
		myDelegates = theDelegates;
	}

	@Override
	public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return ConsentOutcome.parallelReduce(myDelegates.stream()
				.map(nextDelegate -> nextDelegate.startOperation(theRequestDetails, theContextServices)));
	}

	@Override
	public boolean shouldProcessCanSeeResource(
			RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return myDelegates.stream()
				.map(nextDelegate -> nextDelegate.shouldProcessCanSeeResource(theRequestDetails, theContextServices))
				.filter(nextShould -> nextShould)
				.findFirst()
				.orElse(Boolean.FALSE);
	}

	@Override
	public ConsentOutcome canSeeResource(
			RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return ConsentOutcome.parallelReduce(myDelegates.stream()
				.map(nextDelegate -> nextDelegate.canSeeResource(theRequestDetails, theResource, theContextServices)));
	}

	@Override
	public ConsentOutcome willSeeResource(
			RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return ConsentOutcome.parallelReduce(myDelegates.stream()
				.map(nextDelegate -> nextDelegate.willSeeResource(theRequestDetails, theResource, theContextServices)));
	}
}
