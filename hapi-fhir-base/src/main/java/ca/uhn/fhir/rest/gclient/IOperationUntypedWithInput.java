package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

public interface IOperationUntypedWithInput<T extends IBaseParameters> extends IClientExecutable<IOperationUntypedWithInput<T>, T> {

	/**
	 * The client should invoke this method using an HTTP GET instead of an HTTP POST. Note that
	 * according the the FHIR specification, all methods must support using the POST method, but
	 * only certain methods may support the HTTP GET method, so it is generally not necessary
	 * to use this feature. 
	 * <p>
	 * If you have a specific reason for needing to use a GET however, this method will enable it.
	 * </p>
	 */
	IOperationUntypedWithInput<T> useHttpGet();

}
