package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.IRepositoryRestQueryBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.List;

/**
 * This class provides a rest-query builder over a plain Multimap.
 * It is used to convert {@link IRepository.IRepositoryRestQueryContributor} implementations
 * that are not Multimap-based so they can be used by IRepository implementations that are.
 */
public class MultiMapRepositoryRestQueryBuilder implements IRepositoryRestQueryBuilder {
	/**
	 * Our search parameters.
	 * We use a list multimap to maintain insertion order, and because most of IQueryParameterType don't
	 * provide a meaningful equals/hashCode implementation.
	 */
	private final Multimap<String, List<IQueryParameterType>> mySearchParameters = ArrayListMultimap.create();

	@Override
	public IRepositoryRestQueryBuilder addOrList(String theParamName, List<IQueryParameterType> theParameters) {
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

	/**
	 * Converts a {@link IRepository.IRepositoryRestQueryContributor} to a Multimap.
	 *
	 * @param theSearchQueryBuilder the contributor to convert
	 * @return a Multimap containing the search parameters contributed by the contributor
	 */
	public static Multimap<String, List<IQueryParameterType>> contributorToMultimap(
			IRepository.IRepositoryRestQueryContributor theSearchQueryBuilder) {
		MultiMapRepositoryRestQueryBuilder builder = new MultiMapRepositoryRestQueryBuilder();
		theSearchQueryBuilder.contributeToQuery(builder);
		return builder.toMultiMap();
	}
}
