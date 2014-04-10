package ca.uhn.fhir.model.api;

public interface IQueryParameterType {

	/**
	 * Sets the value of this type using the <b>token</b> format. This 
	 * format is used in HTTP queries as a parameter format.
	 * 
	 * <p>See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 *    </p>
	 */
	public void setValueAsQueryToken(String theParameter);

	/**
	 * Returns the value of this type using the <b>token</b> format. This 
	 * format is used in HTTP queries as a parameter format.
	 * 
	 * <p>See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search SearchParameter Types</a>
	 *    for information on the <b>token</b> format
	 *   </p>
	 */
	public String getValueAsQueryToken();
	
}
