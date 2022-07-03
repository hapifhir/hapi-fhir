package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.UriParam;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.URI_VALUE;


public class HSearchParamHelperUri extends HSearchParamHelper<UriParam> {

	private static final String PATH = String.join(".", SEARCH_PARAM_ROOT, "*", URI_VALUE);

	private static final List<String> mySearchProperties = List.of( PATH );


	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		UriParam param = (UriParam) theParam;
		return Optional.of(param.getValue());
	}


	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.URI; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return PATH.startsWith(NESTED_SEARCH_PARAM_ROOT); }


}
