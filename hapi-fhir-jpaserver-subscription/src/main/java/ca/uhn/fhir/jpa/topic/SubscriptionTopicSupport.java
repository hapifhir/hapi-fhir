package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;

public class SubscriptionTopicSupport {
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final SearchParamMatcher mySearchParamMatcher;

	public SubscriptionTopicSupport(FhirContext theFhirContext, DaoRegistry theDaoRegistry, SearchParamMatcher theSearchParamMatcher) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		mySearchParamMatcher = theSearchParamMatcher;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	public SearchParamMatcher getSearchParamMatcher() {
		return mySearchParamMatcher;
	}
}
