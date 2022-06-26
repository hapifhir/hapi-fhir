package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.URI_VALUE;

public class HSearchParamHelperReference extends HSearchParamHelper<ReferenceParam> {

	private static final String PATH = String.join(".", SEARCH_PARAM_ROOT, "*", "reference", "value");

	private static final List<String> mySearchProperties = List.of( PATH );



	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		ReferenceParam referenceParam = (ReferenceParam) theParam;

		String valueTrimmed = referenceParam.getValue();
		if (valueTrimmed.contains("/_history")) {
			valueTrimmed = valueTrimmed.substring(0, valueTrimmed.indexOf("/_history"));
		}

		return Optional.ofNullable(valueTrimmed);
	}


	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.REFERENCE; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return false; }

}
