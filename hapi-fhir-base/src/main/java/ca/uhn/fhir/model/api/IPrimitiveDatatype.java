package ca.uhn.fhir.model.api;

import ca.uhn.fhir.parser.DataFormatException;

public interface IPrimitiveDatatype<T> extends IDatatype {

	void setValueAsString(String theValue) throws DataFormatException;

	String getValueAsString() throws DataFormatException;

	T getValue();
	
	void setValue(T theValue) throws DataFormatException;
}
