package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.IRepository.IRepositoryRestQueryContributor;
import ca.uhn.fhir.repository.IRepositoryRestQueryBuilder;
import ca.uhn.fhir.rest.param.ParameterUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * This class provides a rest-query builder over a plain Multimap.
 * It is used to convert {@link IRepositoryRestQueryContributor} implementations
 * that are not Multimap-based so they can be used by IRepository implementations that are.
 */
public class MultiMapRepositoryRestQueryBuilder implements IRepositoryRestQueryBuilder {
	/**
	 * Our search parameters.
	 * We use a list multimap to maintain insertion order, and because most of IQueryParameterType don't
	 * provide a meaningful equals/hashCode implementation.
	 */
	private final Multimap<String, List<IQueryParameterType>> mySearchParameters = ArrayListMultimap.create();

	@Nonnull
	public static Map<String, String[]> toFlatMap(IRepositoryRestQueryContributor searchParameterMap) {
		MultiMapRepositoryRestQueryBuilder builder = new MultiMapRepositoryRestQueryBuilder();
		searchParameterMap.contributeToQuery(builder);
		Multimap<String, List<IQueryParameterType>> m = builder.toMultiMap();
		return flattenMultimap(m);
	}

	/**
	 * Converts a Multimap of search parameters to a flat Map of Strings.
	 */
	@Nonnull
	static Map<String, String[]> flattenMultimap(Multimap<String, List<IQueryParameterType>> theQueryMap) {
		Map<String, String[]> result = new HashMap<>();

		theQueryMap.asMap().forEach((key, value) -> result.put(key, flattenValues(value)));

		return result;
	}

	/**
	 * Flatten the and/or lists of IQueryParameterType into a String array.
	 */
	private static @Nonnull String[] flattenValues(Collection<List<IQueryParameterType>> theOrLists) {
		// hacky - this ignores modifiers.  Those should probably move back to the keys for this legacy api.
		// But this is a dead api.
		return theOrLists.stream()
				.map(MultiMapRepositoryRestQueryBuilder::flattenOrList)
				.toArray(String[]::new);
	}

	/** Build an or-list string */
	private static String flattenOrList(List<IQueryParameterType> theOrList) {
		return ParameterUtil.escapeAndJoinOrList(
				Lists.transform(theOrList, p -> requireNonNull(p).getValueAsQueryToken()));
	}

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
				throw new IllegalArgumentException(
						Msg.code(2833) + "All parameters in a or-list must be of the same type. Found "
								+ firstValue.getClass().getSimpleName() + " and "
								+ nextValue.getClass().getSimpleName() + " in parameter '" + theName + "'");
			}
		}
	}

	public Multimap<String, List<IQueryParameterType>> toMultiMap() {
		return mySearchParameters;
	}

	/**
	 * Converts a {@link IRepositoryRestQueryContributor} to a Multimap.
	 *
	 * @param theSearchQueryBuilder the contributor to convert
	 * @return a Multimap containing the search parameters contributed by the contributor
	 */
	public static Multimap<String, List<IQueryParameterType>> contributorToMultimap(
			IRepositoryRestQueryContributor theSearchQueryBuilder) {
		MultiMapRepositoryRestQueryBuilder builder = new MultiMapRepositoryRestQueryBuilder();
		theSearchQueryBuilder.contributeToQuery(builder);
		return builder.toMultiMap();
	}
}
