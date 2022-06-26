package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;

public class IPrefixedNumberPredicateHelperImpl implements IPrefixedNumberPredicateHelper {

	private static final double APPROX_TOLERANCE_PERCENT = .10;
	private static final double TOLERANCE_PERCENT = .05;


	@Override
	public void addPredicate(SearchPredicateFactory theFactory, BooleanPredicateClausesStep<?> theBool,
									 ParamPrefixEnum thePrefix, double theValue, String thePropertyPath) {

		double approxTolerance = theValue * APPROX_TOLERANCE_PERCENT;
		double defaultTolerance = theValue * TOLERANCE_PERCENT;

		switch (thePrefix) {
			//	searches for resource quantity between passed param value +/- 10%
			case APPROXIMATE:
				theBool.must(theFactory.range()
					.field(thePropertyPath).between(theValue-approxTolerance, theValue+approxTolerance));
				break;

			// searches for resource quantity between passed param value +/- 5%
			case EQUAL:
				theBool.must(theFactory.range()
					.field(thePropertyPath).between(theValue-defaultTolerance, theValue+defaultTolerance));
				break;

			// searches for resource quantity > param value
			case GREATERTHAN:
			case STARTS_AFTER:  // treated as GREATERTHAN because search doesn't handle ranges
				theBool.must(theFactory.range()
					.field(thePropertyPath).greaterThan(theValue));
				break;

			// searches for resource quantity not < param value
			case GREATERTHAN_OR_EQUALS:
				theBool.must(theFactory.range()
					.field(thePropertyPath).atLeast(theValue));
				break;

			// searches for resource quantity < param value
			case LESSTHAN:
			case ENDS_BEFORE:  // treated as LESSTHAN because search doesn't handle ranges
				theBool.must(theFactory.range()
					.field(thePropertyPath).lessThan(theValue));
				break;

			// searches for resource quantity not > param value
			case LESSTHAN_OR_EQUALS:
				theBool.must(theFactory.range()
					.field(thePropertyPath).atMost(theValue));
				break;

			// NOT_EQUAL: searches for resource quantity not between passed param value +/- 5%
			case NOT_EQUAL:
				theBool.mustNot(theFactory.range()
					.field(thePropertyPath).between(theValue-defaultTolerance, theValue+defaultTolerance));
				break;
		}

	}
}
