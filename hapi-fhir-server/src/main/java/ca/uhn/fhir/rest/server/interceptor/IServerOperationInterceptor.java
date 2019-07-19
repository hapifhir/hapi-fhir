package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Server interceptor with added methods which can be called within the lifecycle of
 * write operations (create/update/delete) or within transaction and batch
 * operations that call these sub-operations.
 *
 * @see ServerOperationInterceptorAdapter
 * @deprecated Ths interface is no longer neccessary as of HAPI FHIR 3.8.0 - You can create
 * interceptor methods that are declared using the {@link Hook} annotation without needing
 * to implement any interceptor
 */
@Deprecated
public interface IServerOperationInterceptor extends IServerInterceptor {

	/**
	 * This method is called by the server immediately after a resource has
	 * been created, within the database transaction scope of the operation.
	 * <p>
	 * If an exception is thrown by an interceptor during this method,
	 * the transaction will be rolled back.
	 * </p>
	 */
	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	void resourceCreated(RequestDetails theRequest, IBaseResource theResource);

	/**
	 * This method is called by the server immediately after a resource has
	 * been deleted, within the database transaction scope of the operation.
	 * <p>
	 * If an exception is thrown by an interceptor during this method,
	 * the transaction will be rolled back.
	 * </p>
	 */
	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	void resourceDeleted(RequestDetails theRequest, IBaseResource theResource);

	/**
	 * This method is called by the server immediately before a resource is about
	 * to be created, within the database transaction scope of the operation.
	 * <p>
	 * This method may be used to modify the resource
	 * </p>
	 * <p>
	 * If an exception is thrown by an interceptor during this method,
	 * the transaction will be rolled back.
	 * </p>
	 *
	 * @param theResource The resource that has been provided by the client as the payload
	 *                    to create. Interceptors may modify this
	 *                    resource, and modifications will affect what is saved in the database.
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	void resourcePreCreate(RequestDetails theRequest, IBaseResource theResource);

	/**
	 * This method is called by the server immediately before a resource is about
	 * to be deleted, within the database transaction scope of the operation.
	 * <p>
	 * If an exception is thrown by an interceptor during this method,
	 * the transaction will be rolled back.
	 * </p>
	 *
	 * @param theResource The resource which is about to be deleted
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED)
	void resourcePreDelete(RequestDetails theRequest, IBaseResource theResource);

	/**
	 * This method is called by the server immediately before a resource is about
	 * to be updated, within the database transaction scope of the operation.
	 * <p>
	 * This method may be used to modify the resource
	 * </p>
	 * <p>
	 * If an exception is thrown by an interceptor during this method,
	 * the transaction will be rolled back.
	 * </p>
	 *
	 * @param theOldResource The previous version of the resource, or <code>null</code> if this is not available. Interceptors should be able to handle situations where this is null, since it is not always
	 *                       convenient or possible to provide a value for this field, but servers should try to populate it.
	 * @param theNewResource The resource that has been provided by the client as the payload
	 *                       to update to the resource to. Interceptors may modify this
	 *                       resource, and modifications will affect what is saved in the database.
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	void resourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource);

	/**
	 * @deprecated Deprecated in HAPI FHIR 3.0.0 in favour of {@link #resourceUpdated(RequestDetails, IBaseResource, IBaseResource)}
	 */
	@Deprecated
	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	void resourceUpdated(RequestDetails theRequest, IBaseResource theResource);

	/**
	 * This method is called by the server immediately after a resource has
	 * been created, within the database transaction scope of the operation.
	 * <p>
	 * If an exception is thrown by an interceptor during this method,
	 * the transaction will be rolled back.
	 * </p>
	 *
	 * @param theOldResource The resource as it was before the update, or <code>null</code> if this is not available. Interceptors should be able to handle situations where this is null, since it is not always
	 *                       convenient or possible to provide a value for this field, but servers should try to populate it.
	 * @param theNewResource The resource as it will be after the update
	 */
	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource);

}
