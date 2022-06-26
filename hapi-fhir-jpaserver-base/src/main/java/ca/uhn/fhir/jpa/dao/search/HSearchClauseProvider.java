package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.DateParam;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;

import java.util.List;

public class HSearchClauseProvider {

	private static final String NESTED_PREFIX = "nsp.";

	private final SearchPredicateFactory myFactory;
	private final BooleanPredicateClausesStep<?> myRootBool;
	private final IHSearchParamHelperProvider myHSearchParamHelperProvider;


	public HSearchClauseProvider(IHSearchParamHelperProvider theHSearchParamHelperProvider,
										  SearchPredicateFactory theFactory, BooleanPredicateClausesStep<?> theRootBoolean) {
		myFactory = theFactory;
		myRootBool = theRootBoolean;
		myHSearchParamHelperProvider = theHSearchParamHelperProvider;
	}


	public void addAndConsumeAndPlusOrClauses(String theResourceTypeName,
															String theParamName, List<List<IQueryParameterType>> theAndOrTerms) {

		HSearchParamHelper<?> paramHelper = getParamHelper(theResourceTypeName, theParamName);
		boolean isPropertyNested = paramHelper.isNested();

		BooleanPredicateClausesStep<?> topBool = myFactory.bool();

		for (List<IQueryParameterType> orTerms : theAndOrTerms) {
			// need an extra bool level for nested properties (embedding it under topBool before leaving loop)
			BooleanPredicateClausesStep<?> activeBool = isPropertyNested ? myFactory.bool()  : topBool;

			processOrTerms(orTerms, activeBool, theParamName, paramHelper);

			if ( isPropertyNested ) {
				topBool.must(myFactory.nested().objectField(NESTED_PREFIX + theParamName).nest(activeBool));
			}
		}

		myRootBool.must(topBool);
	}


 	private void processOrTerms(List<IQueryParameterType> theOrTerms, BooleanPredicateClausesStep<?> theBool,
										 String theParamName, HSearchParamHelper<?> theParamHelper) {

		if (theOrTerms.size() == 1) {
			theParamHelper.addOrClauses(myFactory, theBool, theParamName, theOrTerms.get(0));
			return;
		}

		// comma separated list of dates(OR list) on a date param is not applicable
		if (theOrTerms.get(0) instanceof DateParam) {
			throw new IllegalArgumentException(Msg.code(2032) +
				"OR (,) searches on DATE search parameters are not supported for ElasticSearch/Lucene");
		}

		// multiple or predicates must be in must group with multiple should(s) with a minimumShouldMatchNumber(1)
		theBool.must(myFactory.bool(b2 -> {
			b2.minimumShouldMatchNumber(1);

			for (IQueryParameterType orTerm : theOrTerms) {
				var paramBool = myFactory.bool();
				theParamHelper.addOrClauses(myFactory, paramBool, theParamName, orTerm);
				b2.should(paramBool);
			}
		}));
	}



	/**
	 * Adding an OR clause doesn't necessarily mean one must clause, because for parameters with more than
	 * one property, a must clause must be added for each property present
	 */
	private void addOrClause(HSearchParamHelper<?> theParamHelper, BooleanPredicateClausesStep<?> theBool,
				String theParamName, IQueryParameterType theParam) {

		theParamHelper.addOrClauses(myFactory, theBool, theParamName, theParam);
	}


	private HSearchParamHelper<?> getParamHelper(String theResourceTypeName, String theParamName) {
		return myHSearchParamHelperProvider.provideHelper(theResourceTypeName, theParamName);
	}


	public void addResourceTypeClause(String theResourceType) {
		myRootBool.must(myFactory.match().field("myResourceType").matching(theResourceType));

	}
}
