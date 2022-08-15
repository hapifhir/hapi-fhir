package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import java.lang.reflect.Method;
import java.util.Collection;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public interface IParameter {

	/**
	 * This <b>server method</b> method takes the data received by the server in an incoming request, and translates that data into a single argument for a server method invocation. Note that all
	 * received data is passed to this method, but the expectation is that not necessarily that all data is used by every parameter.
	 * 
	 * @param theRequest
	 *            The incoming request object
	 * @param theRequestContents
	 *            The parsed contents of the incoming request. E.g. if the request was an HTTP POST with a resource in the body, this argument would contain the parsed {@link IResource} instance.
	 * @param theMethodBinding TODO
	 * @return Returns the argument object as it will be passed to the IResourceProvider method.
	 */
	Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException;

	void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType);

}
