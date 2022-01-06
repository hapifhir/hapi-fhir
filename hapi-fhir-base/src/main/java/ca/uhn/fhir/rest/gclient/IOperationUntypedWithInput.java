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

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IOperationUntypedWithInput<T> extends IClientExecutable<IOperationUntypedWithInput<T>, T> {

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

	/**
	 * If this operation returns a single resource body as its return type instead of a <code>Parameters</code>
	 * resource, use this method to specify that resource type. This is useful for certain
	 * operations (e.g. <code>Patient/NNN/$everything</code>) which return a bundle instead of
	 * a Parameters resource.
	 */
	<R extends IBaseResource> IOperationUntypedWithInput<R> returnResourceType(Class<R> theReturnType);

	/**
	 * Request that the method chain returns a {@link MethodOutcome} object. This object
	 * will contain details
	 */
	IOperationUntypedWithInput<MethodOutcome> returnMethodOutcome();
}
