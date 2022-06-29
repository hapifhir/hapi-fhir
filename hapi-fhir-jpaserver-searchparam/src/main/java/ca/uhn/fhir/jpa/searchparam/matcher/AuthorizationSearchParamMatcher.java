package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class AuthorizationSearchParamMatcher implements IAuthorizationSearchParamMatcher {
	private final SearchParamMatcher mySearchParamMatcher;

	public AuthorizationSearchParamMatcher(SearchParamMatcher mySearchParamMatcher) {
		this.mySearchParamMatcher = mySearchParamMatcher;
	}

	@Override
	public MatchResult match(String theCriteria, IBaseResource theResource) {
		try {
			InMemoryMatchResult inMemoryMatchResult = mySearchParamMatcher.match(theCriteria, theResource, null);
			if (!inMemoryMatchResult.supported()) {
				return new MatchResult(Match.UNSUPPORTED, inMemoryMatchResult.getUnsupportedReason());
			}
			if (inMemoryMatchResult.matched()) {
				return new MatchResult(Match.MATCH, null);
			} else {
				return new MatchResult(Match.NO_MATCH, null);
			}
		} catch (MatchUrlService.UnrecognizedSearchParameterException e) {
			// wipmb revisit this design
			// The matcher treats a bad expression as InvalidRequestException because it assumes it is during SearchParameter storage.
			// Instead, we adapt this to UNSUPPORTED during authorization.  We may be applying to all types, and this filter won't match.
			return new MatchResult(Match.UNSUPPORTED, e.getMessage());
		}
	}
}
