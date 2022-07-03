package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.util.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.DATE_LOWER;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.DATE_LOWER_ORD;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.DATE_UPPER;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.DATE_UPPER_ORD;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.SEARCH_PARAM_ROOT;

public class HSearchParamHelperDate extends HSearchParamHelper<DateParam> {

	private static final String LOWER_ORD_PATH = String.join(".", SEARCH_PARAM_ROOT, "*", "dt", DATE_LOWER_ORD);
	private static final String LOWER_PATH 	 = String.join(".", SEARCH_PARAM_ROOT, "*", "dt", DATE_LOWER);
	private static final String UPPER_ORD_PATH = String.join(".", SEARCH_PARAM_ROOT, "*", "dt", DATE_UPPER_ORD);
	private static final String UPPER_PATH 	 = String.join(".", SEARCH_PARAM_ROOT, "*", "dt", DATE_UPPER);

	private static final List<String> mySearchProperties = List.of( LOWER_ORD_PATH, LOWER_PATH, UPPER_ORD_PATH, UPPER_PATH );

	final List<TemporalPrecisionEnum> ordinalSearchPrecisions = List.of(
		TemporalPrecisionEnum.YEAR, TemporalPrecisionEnum.MONTH, TemporalPrecisionEnum.DAY);


	@Override
	public <P extends IQueryParameterType> void addOrClauses(SearchPredicateFactory theFactory,
					BooleanPredicateClausesStep<?> theBool, String theParamName, P theParam) {

		DateParam dateParam = (DateParam) theParam;

		String lowerOrdPathForParam = mergeParamIntoProperty(LOWER_ORD_PATH, theParamName);
		String upperOrdPathForParam = mergeParamIntoProperty(UPPER_ORD_PATH, theParamName);

		String lowerPathForParam = mergeParamIntoProperty(LOWER_PATH, theParamName);
		String upperPathForParam = mergeParamIntoProperty(UPPER_PATH, theParamName);

		boolean isOrdinalSearch = ordinalSearchPrecisions.contains(dateParam.getPrecision());
		PredicateFinalStep searchPredicate = isOrdinalSearch
			? generateDateOrdinalSearchTerms(dateParam, theFactory, lowerOrdPathForParam, upperOrdPathForParam)
			: generateDateInstantSearchTerms(dateParam, theFactory, lowerPathForParam, upperPathForParam);

		theBool.must(searchPredicate);
	}


	@Override
	public void processOrTerms(SearchPredicateFactory theFactory, BooleanPredicateClausesStep<?> theBool,
										List<IQueryParameterType> theOrTerms, String theParamName) {

		if (theOrTerms.size() > 1) {
			throw new IllegalArgumentException(Msg.code(2032) +
				"OR (,) searches on DATE search parameters are not supported for ElasticSearch/Lucene");
		}

		addOrClauses(theFactory, theBool, theParamName, theOrTerms.get(0));
	}


	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		return Optional.empty();
	}


	private PredicateFinalStep generateDateOrdinalSearchTerms(DateParam theDateParam, SearchPredicateFactory theFactory,
					String theLowerOrdPathForParam, String theUpperOrdPathForParam) {
		int lowerBoundAsOrdinal;
		int upperBoundAsOrdinal;
		ParamPrefixEnum prefix = theDateParam.getPrefix();

		// default when handling 'Day' temporal types
		lowerBoundAsOrdinal = upperBoundAsOrdinal = DateUtils.convertDateToDayInteger(theDateParam.getValue());
		TemporalPrecisionEnum precision = theDateParam.getPrecision();
		// complete the date from 'YYYY' and 'YYYY-MM' temporal types
		if (precision == TemporalPrecisionEnum.YEAR || precision == TemporalPrecisionEnum.MONTH) {
			Pair<String, String> completedDate = DateUtils.getCompletedDate(theDateParam.getValueAsString());
			lowerBoundAsOrdinal = Integer.parseInt(completedDate.getLeft().replace("-", ""));
			upperBoundAsOrdinal = Integer.parseInt(completedDate.getRight().replace("-", ""));
		}

		if (Objects.isNull(prefix) || prefix == ParamPrefixEnum.EQUAL) {
			// For equality prefix we would like the date to fall between the lower and upper bound
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				theFactory.range().field(theLowerOrdPathForParam).atLeast(lowerBoundAsOrdinal),
				theFactory.range().field(theUpperOrdPathForParam).atMost(upperBoundAsOrdinal)
			);
			BooleanPredicateClausesStep<?> booleanStep = theFactory.bool();
			predicateSteps.forEach(booleanStep::must);
			return booleanStep;
		} else if (ParamPrefixEnum.GREATERTHAN == prefix || ParamPrefixEnum.STARTS_AFTER == prefix) {
			// TODO JB: more fine tuning needed for STARTS_AFTER
			return theFactory.range().field(theUpperOrdPathForParam).greaterThan(upperBoundAsOrdinal);
		} else if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS == prefix) {
			return theFactory.range().field(theUpperOrdPathForParam).atLeast(upperBoundAsOrdinal);
		} else if (ParamPrefixEnum.LESSTHAN == prefix || ParamPrefixEnum.ENDS_BEFORE == prefix) {
			// TODO JB: more fine tuning needed for END_BEFORE
			return theFactory.range().field(theLowerOrdPathForParam).lessThan(lowerBoundAsOrdinal);
		} else if (ParamPrefixEnum.LESSTHAN_OR_EQUALS == prefix) {
			return theFactory.range().field(theLowerOrdPathForParam).atMost(lowerBoundAsOrdinal);
		} else if (ParamPrefixEnum.NOT_EQUAL == prefix) {
				List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
					theFactory.range().field(theUpperOrdPathForParam).lessThan(lowerBoundAsOrdinal),
					theFactory.range().field(theLowerOrdPathForParam).greaterThan(upperBoundAsOrdinal)
			);
			BooleanPredicateClausesStep<?> booleanStep = theFactory.bool();
			predicateSteps.forEach(booleanStep::should);
			booleanStep.minimumShouldMatchNumber(1);
			return booleanStep;
		}
		throw new IllegalArgumentException(Msg.code(2025) + "Date search param does not support prefix of type: " + prefix);
	}


	private PredicateFinalStep generateDateInstantSearchTerms(DateParam theDateParam, SearchPredicateFactory theFactory,
					String theLowerPathForParam, String theUpperPathForParam) {
		ParamPrefixEnum prefix = theDateParam.getPrefix();

		if (ParamPrefixEnum.NOT_EQUAL == prefix) {
			Instant dateInstant = theDateParam.getValue().toInstant();
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				theFactory.range().field(theUpperPathForParam).lessThan(dateInstant),
				theFactory.range().field(theLowerPathForParam).greaterThan(dateInstant)
			);
			BooleanPredicateClausesStep<?> booleanStep = theFactory.bool();
			predicateSteps.forEach(booleanStep::should);
			booleanStep.minimumShouldMatchNumber(1);
			return booleanStep;
		}

		// Consider lower and upper bounds for building range predicates
		DateRangeParam dateRange = new DateRangeParam(theDateParam);
		Instant lowerBoundAsInstant = Optional.ofNullable(dateRange.getLowerBound()).map(param -> param.getValue().toInstant()).orElse(null);
		Instant upperBoundAsInstant = Optional.ofNullable(dateRange.getUpperBound()).map(param -> param.getValue().toInstant()).orElse(null);

		if (Objects.isNull(prefix) || prefix == ParamPrefixEnum.EQUAL) {
			// For equality prefix we would like the date to fall between the lower and upper bound
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				theFactory.range().field(theLowerPathForParam).atLeast(lowerBoundAsInstant),
				theFactory.range().field(theUpperPathForParam).atMost(upperBoundAsInstant)
			);
			BooleanPredicateClausesStep<?> booleanStep = theFactory.bool();
			predicateSteps.forEach(booleanStep::must);
			return booleanStep;
		} else if (ParamPrefixEnum.GREATERTHAN == prefix || ParamPrefixEnum.STARTS_AFTER == prefix) {
			return theFactory.range().field(theUpperPathForParam).greaterThan(lowerBoundAsInstant);
		} else if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS == prefix) {
			return theFactory.range().field(theUpperPathForParam).atLeast(lowerBoundAsInstant);
		} else if (ParamPrefixEnum.LESSTHAN == prefix || ParamPrefixEnum.ENDS_BEFORE == prefix) {
			return theFactory.range().field(theLowerPathForParam).lessThan(upperBoundAsInstant);
		} else if (ParamPrefixEnum.LESSTHAN_OR_EQUALS == prefix) {
			return theFactory.range().field(theLowerPathForParam).atMost(upperBoundAsInstant);
		}

		throw new IllegalArgumentException(Msg.code(2026) + "Date search param does not support prefix of type: " + prefix);
	}


	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.DATE; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return false; }


}
