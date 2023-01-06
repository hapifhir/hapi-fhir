package ca.uhn.fhir.model.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.io.Serializable;
import java.util.List;

public interface IQueryParameterAnd<T extends IQueryParameterOr<?>> extends Serializable {

	/**
	 * 
	 * <p>
	 * See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 * </p>
	 * @param theContext TODO
	 * @param theParamName TODO
	 */
	void setValuesAsQueryTokens(FhirContext theContext, String theParamName, List<QualifiedParamList> theParameters) throws InvalidRequestException;

	/**
	 * 
	 * <p>
	 * See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 * </p>
	 */
	List<T> getValuesAsQueryTokens();

	
}
