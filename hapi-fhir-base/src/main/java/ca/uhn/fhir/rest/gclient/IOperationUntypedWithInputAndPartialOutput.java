package ca.uhn.fhir.rest.gclient;

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

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;

import ca.uhn.fhir.model.api.IQueryParameterType;

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

	/**
	 * Adds a URL parameter to the request.
	 *
	 * Use chained method calls to construct a Parameters input. This form is a convenience
	 * in order to allow simple method chaining to be used to build up a parameters
	 * resource for the input of an operation without needing to manually construct one.
	 * 
	 * @param theName The first parameter name
	 * @param theValue The first parameter value
	 */
	IOperationUntypedWithInputAndPartialOutput<T> andSearchParameter(String theName, IQueryParameterType theValue);

}
