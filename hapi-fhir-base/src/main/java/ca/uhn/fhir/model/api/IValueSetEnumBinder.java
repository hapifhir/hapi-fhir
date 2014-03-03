package ca.uhn.fhir.model.api;

public interface IValueSetEnumBinder<T extends Enum<?>> {

	T fromCodeString(String theCodeString);
	
	String toCodeString(T theEnum);
	
}
