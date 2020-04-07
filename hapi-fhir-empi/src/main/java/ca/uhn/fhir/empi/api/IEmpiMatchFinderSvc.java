package ca.uhn.fhir.empi.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public interface IEmpiMatchFinderSvc {
	@Nonnull
	List<MatchedTargetCandidate> getMatchedTargetCandidates(String theResourceType, IBaseResource theBaseResource);

	Collection<IBaseResource> findMatches(String theResourceType, IBaseResource theResource);
}
