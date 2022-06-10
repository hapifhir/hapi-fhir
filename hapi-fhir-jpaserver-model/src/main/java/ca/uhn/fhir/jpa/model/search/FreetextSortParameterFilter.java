package ca.uhn.fhir.jpa.model.search;

import java.util.List;

public interface FreetextSortParameterFilter {

	List<String> filter(List<String> thePropertyPath);


}
