package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.TokenParam;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;

public class HSearchParamHelperToken extends HSearchParamHelper<TokenParam> {

	private static final String SYSTEM 	= "system";
	private static final String CODE 	= "code";

	private static final String SYSTEM_PATH = String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", "token", SYSTEM );
	private static final String CODE_PATH   = String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", "token", CODE);

	private static final List<String> mySearchProperties = List.of( SYSTEM_PATH, CODE_PATH );



	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		TokenParam tokenParam = (TokenParam) theParam;

		if (thePropName.endsWith(SYSTEM)) {
			return Optional.of( tokenParam.getSystem() );
		}

		if (thePropName.endsWith(CODE)) {
			return Optional.of( tokenParam.getValue() );
		}

		return Optional.empty();
	}


	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.TOKEN; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return SYSTEM_PATH.startsWith(NESTED_SEARCH_PARAM_ROOT); }



}
