package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

/**
 * Server interceptor for JPA DAOs which adds methods that will be called at certain points
 * in the operation lifecycle for JPA operations.
 */
public interface IJpaServerInterceptor extends IServerInterceptor {

	/**
	 * This method is invoked by the JPA DAOs when a resource has been newly created in the database.
	 * It will be invoked within the current transaction scope. 
	 * <p>
	 * This method is called after the
	 * entity has been persisted and flushed to the database, so it is probably not a good
	 * candidate for security decisions.
	 * </p>
	 * 
	 * @param theDetails The request details
	 * @param theResourceTable The actual created entity
	 */
	void resourceCreated(ActionRequestDetails theDetails, ResourceTable theResourceTable);

	/**
	 * This method is invoked by the JPA DAOs when a resource has been updated in the database.
	 * It will be invoked within the current transaction scope.
	 * <p>
	 * This method is called after the
	 * entity has been persisted and flushed to the database, so it is probably not a good
	 * candidate for security decisions.
	 * </p>
	 * 
	 * @param theDetails The request details
	 * @param theResourceTable The actual updated entity
	 */
	void resourceUpdated(ActionRequestDetails theDetails, ResourceTable theResourceTable);

	/**
	 * This method is invoked by the JPA DAOs when a resource has been updated in the database.
	 * It will be invoked within the current transaction scope.
	 * <p>
	 * This method is called after the
	 * entity has been persisted and flushed to the database, so it is probably not a good
	 * candidate for security decisions.
	 * </p>
	 * 
	 * @param theDetails The request details
	 * @param theResourceTable The actual updated entity
	 */
	void resourceDeleted(ActionRequestDetails theDetails, ResourceTable theResourceTable);

}
