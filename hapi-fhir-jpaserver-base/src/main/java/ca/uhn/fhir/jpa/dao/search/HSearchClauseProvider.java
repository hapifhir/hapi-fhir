package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
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

		BooleanPredicateClausesStep<?> topBool = myFactory.bool();

		for (List<IQueryParameterType> orTerms : theAndOrTerms) {
			// need an extra bool level for nested properties (embedding it under topBool before leaving loop)
			BooleanPredicateClausesStep<?> activeBool = paramHelper.isNested() ? myFactory.bool()  : topBool;

			paramHelper.processOrTerms(myFactory, activeBool, orTerms, theParamName, paramHelper);

			if ( paramHelper.isNested() ) {
				topBool.must(myFactory.nested().objectField(NESTED_PREFIX + theParamName).nest(activeBool));
			}
		}

		myRootBool.must(topBool);
	}


	private HSearchParamHelper<?> getParamHelper(String theResourceTypeName, String theParamName) {
		return myHSearchParamHelperProvider.provideHelper(theResourceTypeName, theParamName);
	}


	public void addResourceTypeClause(String theResourceType) {
		myRootBool.must(myFactory.match().field("myResourceType").matching(theResourceType));

	}
}
