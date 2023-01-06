package ca.uhn.fhir.model.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;

import java.io.Serializable;
import java.util.List;

public interface IQueryParameterOr<T extends IQueryParameterType> extends Serializable {

	void setValuesAsQueryTokens(FhirContext theContext, String theParamName, QualifiedParamList theParameters);

	List<T> getValuesAsQueryTokens();

}
