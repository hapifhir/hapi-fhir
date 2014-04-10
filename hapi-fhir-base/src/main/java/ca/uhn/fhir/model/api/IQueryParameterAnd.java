package ca.uhn.fhir.model.api;

import java.util.List;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public interface IQueryParameterAnd {

	/**
	 * 
	 * <p>
	 * See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 * </p>
	 */
	public void setValuesAsQueryTokens(List<List<String>> theParameters) throws InvalidRequestException;

	/**
	 * 
	 * <p>
	 * See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 * </p>
	 */
	public List<List<String>> getValuesAsQueryTokens();

	
}
