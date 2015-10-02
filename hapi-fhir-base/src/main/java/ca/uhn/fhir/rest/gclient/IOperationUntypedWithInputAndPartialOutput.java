package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;

public interface IOperationUntypedWithInputAndPartialOutput<T extends IBaseParameters> extends IOperationUntypedWithInput<T> {

	/**
	 * Use chained method calls to construct a Parameters input. This form is a convenience
	 * in order to allow simple method chaining to be used to build up a parameters
	 * resource for the input of an operation without needing to manually construct one.
	 * 
	 * @param theName The first parameter name
	 * @param theValue The first parameter value
	 */
	IOperationUntypedWithInputAndPartialOutput<T> andParameter(String theName, IBase theValue);

}
