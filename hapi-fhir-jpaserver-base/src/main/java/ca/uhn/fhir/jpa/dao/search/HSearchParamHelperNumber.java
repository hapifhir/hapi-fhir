package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NUMBER_VALUE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.SEARCH_PARAM_ROOT;

public class HSearchParamHelperNumber extends HSearchParamHelper<NumberParam> {

	private static final String PATH = String.join(".", SEARCH_PARAM_ROOT, "*", NUMBER_VALUE);

	private static final List<String> mySearchProperties = List.of( PATH );


	private final IPrefixedNumberPredicateHelperImpl myPrefixedNumberPredicateHelper;

	public HSearchParamHelperNumber(IPrefixedNumberPredicateHelperImpl thePrefixedNumberPredicateHelper) {
		myPrefixedNumberPredicateHelper = thePrefixedNumberPredicateHelper;
	}


	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		NumberParam numberParam = (NumberParam) theParam;
		return Optional.of(numberParam.getValue().doubleValue());
	}

	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.NUMBER; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return PATH.startsWith(NESTED_SEARCH_PARAM_ROOT); }

	@Override
	public <P extends IQueryParameterType> void addOrClauses(SearchPredicateFactory theFactory,
			BooleanPredicateClausesStep<?> theBool, String theParamName, P theParam) {

		NumberParam numberParam = (NumberParam) theParam;

		ParamPrefixEnum activePrefix = numberParam.getPrefix() == null ? ParamPrefixEnum.EQUAL : numberParam.getPrefix();
		double paramValue = numberParam.getValue().doubleValue();
		String propertyPath = getParamPropertiesForParameter(theParamName, theParam).iterator().next();

		myPrefixedNumberPredicateHelper.addPredicate(theFactory, theBool, activePrefix, paramValue, propertyPath);
	}




}
