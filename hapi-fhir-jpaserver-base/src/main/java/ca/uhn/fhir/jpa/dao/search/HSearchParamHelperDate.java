package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NUMBER_VALUE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.SEARCH_PARAM_ROOT;

public class HSearchParamHelperDate extends HSearchParamHelper<DateParam> {

	private static final String PATH = String.join(".", SEARCH_PARAM_ROOT, "*", NUMBER_VALUE);

	private static final List<String> mySearchProperties = List.of( PATH );



	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		DateParam numberParam = (DateParam) theParam;

//		fixme jm: how ???


		return Optional.empty();
	}


	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.DATE; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return PATH.startsWith(NESTED_SEARCH_PARAM_ROOT); }


}
