package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * IConsentService combiner over several delegates with pluggable combination strategy
 */
public class MultiDelegateConsentService implements IConsentService {
	private final Collection<IConsentService> myDelegates;
	private final Function<Stream<ConsentOutcome>, ConsentOutcome> myVoteCombiner;

	/**
	 * Combine several consent services allowing any to veto.
	 */
	public static @Nonnull MultiDelegateConsentService withParallelVoting(
			@Nonnull List<IConsentService> theDelegateConsentServices) {
		return new MultiDelegateConsentService(ConsentOutcome::parallelReduce, theDelegateConsentServices);
	}

	/**
	 * Combine several consent services with first non-PROCEED vote win.
	 */
	public static @Nonnull MultiDelegateConsentService withSerialVoting(
			@Nonnull List<IConsentService> theDelegateConsentServices) {
		return new MultiDelegateConsentService(ConsentOutcome::serialReduce, theDelegateConsentServices);
	}

	private MultiDelegateConsentService(
			Function<Stream<ConsentOutcome>, ConsentOutcome> theVoteCombiner,
			Collection<IConsentService> theDelegates) {
		myVoteCombiner = theVoteCombiner;
		myDelegates = theDelegates;
	}

	@Override
	public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return myVoteCombiner.apply(myDelegates.stream()
				.map(nextDelegate -> nextDelegate.startOperation(theRequestDetails, theContextServices)));
	}

	/**
	 * @return true if any of the delegates return true.
	 */
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
		return myVoteCombiner.apply(myDelegates.stream()
				.map(nextDelegate -> nextDelegate.canSeeResource(theRequestDetails, theResource, theContextServices)));
	}

	@Override
	public ConsentOutcome willSeeResource(
			RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return myVoteCombiner.apply(myDelegates.stream()
				.map(nextDelegate -> nextDelegate.willSeeResource(theRequestDetails, theResource, theContextServices)));
	}
}
