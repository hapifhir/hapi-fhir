package ca.uhn.fhir.fluentpath;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBase;

public interface IFluentPath {

	/**
	 * Apply the given FluentPath expression against the given input and return
	 * all results in a list
	 * 
	 * @param theInput The input object (generally a resource or datatype)
	 * @param thePath The fluent path expression
	 * @param theReturnType The type to return (in order to avoid casting)
	 */
	<T extends IBase> List<T> evaluate(IBase theInput, String thePath, Class<T> theReturnType);

	
	
}
