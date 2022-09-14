package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;

public class PredicateBuilderCacheLookupResult<T extends BaseJoiningPredicateBuilder> {
	private final boolean myCacheHit;
	private final T myResult;

	public PredicateBuilderCacheLookupResult(boolean theCacheHit, T theResult) {
		myCacheHit = theCacheHit;
		myResult = theResult;
	}

	public boolean isCacheHit() {
		return myCacheHit;
	}

	public T getResult() {
		return myResult;
	}
}
