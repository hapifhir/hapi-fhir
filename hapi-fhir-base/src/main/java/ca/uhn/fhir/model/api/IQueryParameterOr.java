package ca.uhn.fhir.model.api;

import java.util.List;

public interface IQueryParameterOr {

	/**
	 * Sets the value of this type using the <b>token</b> format. This 
	 * format is used in HTTP queries as a parameter format.
	 * <p>
	 * See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 * </p>
	 */
	public void setValuesAsQueryTokens(List<String> theParameters);

	/**
	 * Returns the value of this type using the <b>token</b> format. This 
	 * format is used in HTTP queries as a parameter format.
	 * 
	 * <p>
	 * See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 * </p>
	 */
	public List<String> getValuesAsQueryTokens();

	
}
