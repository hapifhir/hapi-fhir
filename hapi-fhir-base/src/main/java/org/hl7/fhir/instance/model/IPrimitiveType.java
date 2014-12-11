package org.hl7.fhir.instance.model;


public interface IPrimitiveType<T> extends IBase {

	void setValueAsString(String theValue) throws IllegalArgumentException;

	String getValueAsString();

	T getValue();
	
	IPrimitiveType<T> setValue(T theValue) throws IllegalArgumentException;
	
}
