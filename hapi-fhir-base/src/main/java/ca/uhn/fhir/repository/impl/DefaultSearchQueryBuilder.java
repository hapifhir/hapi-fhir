package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.model.api.IQueryParameterType;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;

/**
 * A base class for search query builders that uses a {@link MultiMapSearchQueryBuilder} as its delegate.
 * The builder will collect all search parameters in a multimap format, which can be used to construct search queries.
 */
public abstract class DefaultSearchQueryBuilder implements ISearchQueryBuilder {
	@Override
	public ISearchQueryBuilder addAll(Multimap<String, List<IQueryParameterType>> theSearchParameters) {
		theSearchParameters.entries().forEach(e -> this.addOrList(e.getKey(), e.getValue()));
		return this;
	}

	@Override
	public ISearchQueryBuilder addAll(Map<String, List<IQueryParameterType>> theSearchParameters) {
		theSearchParameters.forEach(this::addOrList);
		return this;
	}
}
