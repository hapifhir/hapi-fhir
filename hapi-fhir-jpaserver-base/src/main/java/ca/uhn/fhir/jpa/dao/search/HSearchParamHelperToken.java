package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.SEARCH_PARAM_ROOT;

public class HSearchParamHelperToken extends HSearchParamHelper<TokenParam> {

	private static final String CODE 	= "code";
	private static final String SYSTEM 	= "system";
	private static final String CODE_SYSTEM 	= "code-system";

	private static final String CODE_PATH   		= String.join(".", SEARCH_PARAM_ROOT, "*", "token", CODE);
	private static final String SYSTEM_PATH 		= String.join(".", SEARCH_PARAM_ROOT, "*", "token", SYSTEM );
	private static final String CODE_SYSTEM_PATH = String.join(".", SEARCH_PARAM_ROOT, "*", "token", CODE_SYSTEM );

	private static final List<String> mySearchProperties = List.of( CODE_PATH, SYSTEM_PATH, CODE_SYSTEM_PATH );



	@Override
	public void processOrTerms(SearchPredicateFactory theFactory, BooleanPredicateClausesStep<?> theBool,
										List<IQueryParameterType> theOrTerms, String theParamName) {

		if (theOrTerms.isEmpty()) { return; }

		TokenParam tokenParam = (TokenParam) theOrTerms.iterator().next();
		if ( tokenParam.getQueryParameterQualifier() != null &&
							tokenParam.getQueryParameterQualifier().equals(Constants.PARAMQUALIFIER_TOKEN_TEXT) ) {

			Set<String> terms = extractOrTokenParams(theOrTerms);
			var predicates = getPredicatesForTextQualifier(theFactory, terms, theParamName);
			theBool.must( predicates );
			return;
		}

		// normal processing applies for other than TEXT token params
		super.processOrTerms(theFactory, theBool, theOrTerms, theParamName);
	}



	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		TokenParam tokenParam = (TokenParam) theParam;

		if (thePropName.endsWith(CODE)) {
			return Optional.ofNullable( tokenParam.getValue() );
		}

		if (thePropName.endsWith("." + SYSTEM)) {
			return Optional.ofNullable( tokenParam.getSystem() );
		}

//		fixme jm: when?
//		if (thePropName.endsWith(CODE_SYSTEM)) {
//			return Optional.ofNullable( tokenParam.getSystem() + "|" + tokenParam.getValue() );
//		}

		return Optional.empty();
	}


	private Set<String> extractOrTokenParams(List<IQueryParameterType> theOrTerms) {
		return theOrTerms.stream()
			.map( TokenParam.class::cast )
			.map( p -> StringUtils.defaultString(p.getValue()) )
			.map( String::trim )
			.collect(Collectors.toSet());
	}


	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.TOKEN; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return false; }



}
