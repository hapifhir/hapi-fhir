package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class AuthorizationSearchParamMatcher implements IAuthorizationSearchParamMatcher {
	private final SearchParamMatcher mySearchParamMatcher;

	public AuthorizationSearchParamMatcher(SearchParamMatcher mySearchParamMatcher) {
		this.mySearchParamMatcher = mySearchParamMatcher;
	}

	@Override
	public MatchResult match(String theCriteria, IBaseResource theResource) {
		mySearchParamMatcher.match(theCriteria, theResource, null);
		return null;
	}
}
