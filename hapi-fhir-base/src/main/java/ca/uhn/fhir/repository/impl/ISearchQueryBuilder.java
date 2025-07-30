package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.NumberParam;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;

public interface ISearchQueryBuilder {
	ISearchQueryBuilder addAll(Multimap<String, List<IQueryParameterType>> theSearchParameters);

	ISearchQueryBuilder addAll(Map<String, List<IQueryParameterType>> theMap);

	default ISearchQueryBuilder addOrList(String theParamName, IQueryParameterType... theParameterValues) {
		return addOrList(theParamName, List.of(theParameterValues));
	}

	default ISearchQueryBuilder addNumericParameter(String theCount, int theInteger) {
		return addOrList(theCount, new NumberParam(theInteger));
	}

	ISearchQueryBuilder addOrList(String theParamName, List<IQueryParameterType> theParameters);

	@FunctionalInterface
	interface ISearchQueryContributor {
		void contributeToQuery(ISearchQueryBuilder theBuilder);
	}
}
