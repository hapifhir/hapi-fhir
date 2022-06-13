package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.List;

public interface IFreetextSortPropertyFilter {

	boolean accepts(RestSearchParameterTypeEnum theParamPropType);

	List<String> filter(List<String> theFieldPathList);
}
