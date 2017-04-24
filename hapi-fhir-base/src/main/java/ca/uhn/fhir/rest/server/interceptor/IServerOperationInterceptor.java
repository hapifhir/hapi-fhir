package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.method.RequestDetails;

/**
 * Server interceptor with added methods which can be called within the lifecycle of 
 * write operations (create/update/delete) or within transaction and batch
 * operations that call these sub-operations.
 * 
 * @see ServerOperationInterceptorAdapter
 */
public interface IServerOperationInterceptor extends IServerInterceptor {

	/**
	 * User code may call this method to indicate to an interceptor that
	 * a resource is being deleted
	 */
	void resourceDeleted(RequestDetails theRequest, IBaseResource theResource);
	
	/**
	 * User code may call this method to indicate to an interceptor that
	 * a resource is being created
	 */
	void resourceCreated(RequestDetails theRequest, IBaseResource theResource);

	/**
	 * User code may call this method to indicate to an interceptor that
	 * a resource is being updated
	 */
	void resourceUpdated(RequestDetails theRequest, IBaseResource theResource);

}
