package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBase;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.hl7.fhir.instance.model.api.IBaseParameters;

import ca.uhn.fhir.model.api.IQueryParameterType;

public interface IOperationUntyped {

	/**
	 * Use the given parameters resource as the input to the operation
	 * 
	 * @param theParameters The parameters to use as input. May also be <code>null</code> if the operation
	 * does not require any input parameters.
	 */
	<T extends IBaseParameters> IOperationUntypedWithInputAndPartialOutput<T> withParameters(T theParameters);

	/**
	 * The operation does not require any input parameters
	 * 
	 * @param theOutputParameterType The type to use for the output parameters (this should be set to
	 * <code>Parameters.class</code> drawn from the version of the FHIR structures you are using)
	 */
	<T extends IBaseParameters> IOperationUntypedWithInput<T> withNoParameters(Class<T> theOutputParameterType);

	/**
	 * Use chained method calls to construct a Parameters input. This form is a convenience
	 * in order to allow simple method chaining to be used to build up a parameters
	 * resource for the input of an operation without needing to manually construct one.
	 * <p>
	 * A sample invocation of this class could look like:<br/>
	 * <pre>Bundle bundle = client.operation()
	 *   .onInstance(new IdType("Patient/A161443"))
	 *   .named("everything")
	 *   .withParameter(Parameters.class, "_count", new IntegerType(50))
	 *   .useHttpGet()
	 *   .returnResourceType(Bundle.class)
	 *   .execute();
	 * </pre>
	 * </p>
	 * 
	 * @param theParameterType The type to use for the output parameters (this should be set to
	 * <code>Parameters.class</code> drawn from the version of the FHIR structures you are using)
	 * @param theName The first parameter name
	 * @param theValue The first parameter value
	 */
	<T extends IBaseParameters> IOperationUntypedWithInputAndPartialOutput<T> withParameter(Class<T> theParameterType, String theName, IBase theValue);

	/**
	 * Use chained method calls to construct a Parameters input. This form is a convenience
	 * in order to allow simple method chaining to be used to build up a parameters
	 * resource for the input of an operation without needing to manually construct one.
	 * 
	 * @param theParameterType The type to use for the output parameters (this should be set to
	 * <code>Parameters.class</code> drawn from the version of the FHIR structures you are using)
	 * @param theName The first parameter name
	 * @param theValue The first parameter value
	 */
	<T extends IBaseParameters> IOperationUntypedWithInputAndPartialOutput<T> withSearchParameter(Class<T> theParameterType, String theName, IQueryParameterType theValue);

}
