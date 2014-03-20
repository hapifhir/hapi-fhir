package ca.uhn.fhir.model.api;

import java.util.List;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public interface IQueryParameterAnd {

	/**
	 * 
	 * @see See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 */
	public void setValuesAsQueryTokens(List<List<String>> theParameters) throws InvalidRequestException;

	/**
	 * 
	 * @see See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 */
	public List<List<String>> getValuesAsQueryTokens();

	
}
