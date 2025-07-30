package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.impl.DefaultSearchQueryBuilder;
import ca.uhn.fhir.repository.impl.ISearchQueryBuilder;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.BaseOrListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONTAINED;
import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_OFFSET;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SEARCH_TOTAL_MODE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SUMMARY;
import static com.google.common.base.Strings.isNullOrEmpty;

// fixme working here
public class SearchParameterMapQueryBuilder extends DefaultSearchQueryBuilder {
	public static final BinaryOperator<?> KEEP_LAST_REDUCER = (l, r) -> r;
	// fixme we won't need a fhir context after https://github.com/hapifhir/hapi-fhir/pull/7136 merges.
	@Nonnull
	private final FhirContext myFhirContext;
	private final SearchParameterMap myResult = new SearchParameterMap();

	public SearchParameterMapQueryBuilder(@Nonnull FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}


	static final Set<String> ourUnsupportedSearchParameters = Set.of(
		"_sort", // fixme sort is worth doing - reduce by list append.
		"_include",
		"_revinclude",
		"_elements", // implemented at the FHIR endpoint.
		"_containedType" // unsupported
	);
	final Map<String, Consumer<List<IQueryParameterType>>> mySpecialParamHandlers = Map.of(
		PARAM_COUNT, v -> lastValueWins(v, Integer::parseInt, myResult::setCount),
		PARAM_OFFSET, v -> lastValueWins(v, Integer::parseInt, myResult::setOffset),
		PARAM_SEARCH_TOTAL_MODE, v -> lastValueWins(v, SearchTotalModeEnum::fromCode, myResult::setSearchTotalMode),
		PARAM_SUMMARY, v -> lastValueWins(v, SummaryEnum::fromCode, myResult::setSummaryMode),
		PARAM_CONTAINED, v -> lastValueWins(v, SearchContainedModeEnum::fromCode, myResult::setSearchContainedMode)
	);

	public static SearchParameterMap buildFromQueryContributor(FhirContext theFhirContext, ISearchQueryContributor theQueryContributor) {
		SearchParameterMap searchParameterMap;
		if (theQueryContributor instanceof SearchParameterMap theSp) {
			searchParameterMap = theSp;
		} else {
			// Build a SearchParameterMap using the provided contributor
			SearchParameterMapQueryBuilder builder = new SearchParameterMapQueryBuilder(theFhirContext);
			theQueryContributor.contributeToQuery(builder);
			searchParameterMap = builder.build();
		}
		return searchParameterMap;
	}

	@Override
	public ISearchQueryBuilder addOrList(String theParamName, List<IQueryParameterType> theOrList) {
		if (ourUnsupportedSearchParameters.contains(theParamName)) {
			throw new IllegalArgumentException("Parameter '" + theParamName + "' is not currently supported by SearchParameterMapQueryBuilder. ");
		}
		Consumer<List<IQueryParameterType>> processor = mySpecialParamHandlers.get(theParamName);
		if (processor == null) {
			myResult.addOrList(theParamName, theOrList);
		} else {
			processor.accept(theOrList);
		}
		return this;
	}


	static <T> void lastValueWins(
		List<IQueryParameterType> theValues,
		Function<String, T> theConverter,
		Consumer<T> theSPMapUpdater) {

		T value = theValues.stream()
			.map(p->p.getValueAsQueryToken(null))
			.map(s-> isNullOrEmpty(s)?null:theConverter.apply(s))
			.reduce(null,keepLast());

		theSPMapUpdater.accept(value);
	}

	@Nonnull
	private static <T> BinaryOperator<T> keepLast() {
		return (BinaryOperator<T>) KEEP_LAST_REDUCER;
	}


	static <T extends IQueryParameterType> Collection<T> validateAndCast(Class<T> theReferenceParamClass, Collection<IQueryParameterType> theParameterValues) {
		theParameterValues.forEach(nextValue -> Validate.isTrue(theReferenceParamClass.isInstance(nextValue)));
		//noinspection unchecked
		return (Collection<T>) theParameterValues;
	}

	void validateHomogeneousList(String theKey, List<IQueryParameterType> theOrList) {
		// verify modifiers and types are all the same
		// fixme test and impl
	}

	public SearchParameterMap build() {
		return myResult;
	}
}
