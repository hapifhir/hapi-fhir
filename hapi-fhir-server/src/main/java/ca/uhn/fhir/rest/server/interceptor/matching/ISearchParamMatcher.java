package ca.uhn.fhir.rest.server.interceptor.matching;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ISearchParamMatcher {
	MatchResult match(String theCriteria, IBaseResource theResource);

	public static class MatchResult {

	}
}
