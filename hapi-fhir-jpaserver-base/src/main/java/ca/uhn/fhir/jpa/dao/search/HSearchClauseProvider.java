package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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


	public PredicateFinalStep getAndPlusOrClauses(String theResourceTypeName, 
			String theParamName, List<List<IQueryParameterType>> theAndOrTerms) {

		HSearchParamHelper<?> paramHelper = getParamHelper(theResourceTypeName, theParamName);
		boolean isPropertyNested = paramHelper.isNested();

		BooleanPredicateClausesStep<?> topBool = myFactory.bool();
		// need an extra bool level for nested properties (embedding it under topBool before leaving method)
		BooleanPredicateClausesStep<?> activeBool = isPropertyNested ? myFactory.bool()  : topBool;

		for (List<IQueryParameterType> orTerms : theAndOrTerms) {
			if (orTerms.size() == 1) {
				paramHelper.addOrClauses(myFactory, activeBool, theParamName, orTerms.get(0));
				continue;
			}

			// multiple or predicates must be in must group with multiple should(s) with a minimumShouldMatchNumber(1)
			activeBool.must(myFactory.bool(b2 -> {
				b2.minimumShouldMatchNumber(1);

				for (IQueryParameterType orTerm : orTerms) {
					var paramBool = myFactory.bool();
					paramHelper.addOrClauses(myFactory, paramBool, theParamName, orTerm);
					b2.should(paramBool);
				}
			}));
		}

		if ( isPropertyNested ) {
			topBool.must(myFactory.nested().objectField(NESTED_PREFIX + theParamName).nest(activeBool));
		}

		return topBool;
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



}
