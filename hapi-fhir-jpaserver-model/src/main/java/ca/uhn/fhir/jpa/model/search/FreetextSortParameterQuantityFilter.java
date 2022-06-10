package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.List;

public class FreetextSortParameterQuantityFilter implements FreetextSortParameterFilter {

	@Override
	public List<String> filter(List<String> thePropertyPath) {
		return null;
	}
}
