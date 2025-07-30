package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.model.api.IQueryParameterType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.List;
import java.util.Map;

public class MultiMapSearchQueryBuilder implements ISearchQueryBuilder {
	private final Multimap<String, List<IQueryParameterType>> mySearchParameters = ArrayListMultimap.create();

	@Override
	public ISearchQueryBuilder addAll(Multimap<String, List<IQueryParameterType>> theSearchParameters) {
		mySearchParameters.putAll(theSearchParameters);
		return this;
	}

	@Override
	public ISearchQueryBuilder addAll(Map<String, List<IQueryParameterType>> theMap) {
		mySearchParameters.putAll(Multimaps.forMap(theMap));
		return this;
	}

	@Override
	public ISearchQueryBuilder addOrList(String theParamName, List<IQueryParameterType> theParameters) {
		validateHomogeneousList(theParamName, theParameters);
		mySearchParameters.put(theParamName, theParameters);
		return this;
	}


	private void validateHomogeneousList(String theName, List<IQueryParameterType> theValues) {
		if (theValues.isEmpty()) {
			return;
		}
		IQueryParameterType firstValue = theValues.get(0);
		for (IQueryParameterType nextValue : theValues) {
			if (!nextValue.getClass().equals(firstValue.getClass())) {
				throw new IllegalArgumentException("All parameters in a or-list must be of the same type. Found "
						+ firstValue.getClass().getSimpleName() + " and "
						+ nextValue.getClass().getSimpleName() + " in parameter '" + theName + "'");
			}
		}
	}

	public Multimap<String, List<IQueryParameterType>> toMultiMap() {
		return mySearchParameters;
	}

	public static Multimap<String, List<IQueryParameterType>> builderToMultimap(ISearchQueryContributor theSearchQueryBuilder) {
		MultiMapSearchQueryBuilder sb = new MultiMapSearchQueryBuilder();
		theSearchQueryBuilder.contributeToQuery(sb);
		return sb.toMultiMap();
	}
}
