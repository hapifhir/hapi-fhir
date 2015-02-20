package org.hl7.fhir.instance.model.api;

public interface IBaseEnumFactory<T extends Enum<?>> {

	/**
	 * Read an enumeration value from the string that represents it on the XML or JSON
	 * 
	 * @param codeString
	 *            the value found in the XML or JSON
	 * @return the enumeration value
	 * @throws IllegalArgumentException
	 *             is the value is not known
	 */
	public T fromCode(String codeString) throws IllegalArgumentException;

	/**
	 * Get the XML/JSON representation for an enumerated value
	 * 
	 * @param code
	 *            - the enumeration value
	 * @return the XML/JSON representation
	 */
	public String toCode(T code);

}
