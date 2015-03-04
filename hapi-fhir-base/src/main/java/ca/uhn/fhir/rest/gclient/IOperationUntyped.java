package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseParameters;

public interface IOperationUntyped {

	<T extends IBaseParameters> IOperationUntypedWithInput<T> withParameters(T theParameters);

}
