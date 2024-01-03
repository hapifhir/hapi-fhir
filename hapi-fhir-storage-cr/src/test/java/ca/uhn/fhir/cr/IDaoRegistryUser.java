/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Simulate FhirDal operations until that's fully baked. This interface is
 * trying to be small and have a reasonable path forwards towards
 * model independence in the future. The method overloads with "RequestDetails"
 * will eventually go away once we're able to make that a
 * cross-cutting concern. There are some ramifications to not using the
 * "RequestDetails" such as not firing hooks on the server, so the
 * overloads with that parameter should generally be preferred for the
 * short-term.
 */
public interface IDaoRegistryUser {

	DaoRegistry getDaoRegistry();

	default FhirContext getFhirContext() {
		return getDaoRegistry().getSystemDao().getContext();
	}

	/**
	 * Get the class of the given Resource. FHIR version aware. For example, if the
	 * server is running in DSTU3 mode
	 * this will return the DSTU3 Library class when invoked with "Library".
	 *
	 * @param <T>             the type of resource to return
	 * @param theResourceName the name of the Resource to get the class for
	 * @return the class of the resource
	 */
	@SuppressWarnings("unchecked")
	default <T extends IBaseResource> Class<T> getClass(String theResourceName) {
		return (Class<T>)
				getFhirContext().getResourceDefinition(theResourceName).getImplementingClass();
	}

	/**
	 * Reads a Resource with the given Id from the local server. Throws an error if
	 * the Resource is not present
	 * <p>
	 * NOTE: Use {@code search} if a null result is preferred over an error.
	 *
	 * @param <T>   the Resource type to read
	 * @param theId the id to read
	 * @return the FHIR Resource
	 * @throws ResourceNotFoundException if the Id is not known
	 */
	default <T extends IBaseResource> T read(IIdType theId) {
		checkNotNull(theId);

		return read(theId, new SystemRequestDetails());
	}

	/**
	 * Reads a Resource with the given Id from the local server. Throws an error if
	 * the Resource is not present
	 * <p>
	 * NOTE: Use {@code search} if a null result is preferred over an error.
	 *
	 * @param <T>            the Resource type to read
	 * @param theId          the id to read
	 * @param requestDetails multi-tenancy information
	 * @return the FHIR Resource
	 * @throws ResourceNotFoundException if the Id is not known
	 */
	@SuppressWarnings("unchecked")
	default <T extends IBaseResource> T read(IIdType theId, RequestDetails requestDetails) {
		checkNotNull(theId);

		return (T) getDaoRegistry().getResourceDao(theId.getResourceType()).read(theId, requestDetails);
	}

	/**
	 * Creates the given Resource on the local server
	 *
	 * @param <T>         The Resource type
	 * @param theResource the resource to create
	 * @return the outcome of the creation
	 */
	default <T extends IBaseResource> DaoMethodOutcome create(T theResource) {
		checkNotNull(theResource);

		return create(theResource, new SystemRequestDetails());
	}

	/**
	 * Creates the given Resource on the local server
	 *
	 * @param <T>            The Resource type
	 * @param theResource    the resource to create
	 * @param requestDetails multi-tenancy information
	 * @return the outcome of the creation
	 */
	@SuppressWarnings("unchecked")
	default <T extends IBaseResource> DaoMethodOutcome create(T theResource, RequestDetails requestDetails) {
		checkNotNull(theResource);

		return ((IFhirResourceDao<T>) getDaoRegistry().getResourceDao(theResource.fhirType()))
				.create(theResource, requestDetails);
	}

	/**
	 * Updates the given Resource on the local server
	 *
	 * @param <T>         The Resource type
	 * @param theResource the resource to update
	 * @return the outcome of the creation
	 */
	default <T extends IBaseResource> DaoMethodOutcome update(T theResource) {
		checkNotNull(theResource);

		return update(theResource, new SystemRequestDetails());
	}

	/**
	 * Updates the given Resource on the local server
	 *
	 * @param <T>            The Resource type
	 * @param theResource    the resource to update
	 * @param requestDetails multi-tenancy information
	 * @return the outcome of the creation
	 */
	@SuppressWarnings("unchecked")
	default <T extends IBaseResource> DaoMethodOutcome update(T theResource, RequestDetails requestDetails) {
		checkNotNull(theResource);

		return ((IFhirResourceDao<T>) getDaoRegistry().getResourceDao(theResource.fhirType()))
				.update(theResource, requestDetails);
	}

	/**
	 * Deletes the Resource with the given Id from the local server
	 *
	 * @param theIdType the Id of the Resource to delete.
	 * @return the outcome of the deletion
	 */
	default DaoMethodOutcome delete(IIdType theIdType) {
		checkNotNull(theIdType);

		return delete(theIdType, new SystemRequestDetails());
	}

	/**
	 * Deletes the Resource with the given Id from the local server
	 *
	 * @param theIdType      the Id of the Resource to delete.
	 * @param requestDetails multi-tenancy information
	 * @return the outcome of the deletion
	 */
	default DaoMethodOutcome delete(IIdType theIdType, RequestDetails requestDetails) {
		checkNotNull(theIdType);

		return getDaoRegistry().getResourceDao(theIdType.getResourceType()).delete(theIdType, requestDetails);
	}

	/**
	 * NOTE: This is untested as of the time I'm writing this so it may need to be
	 * reworked.
	 * Executes a given transaction Bundle on the local server
	 *
	 * @param <T>            the type of Bundle
	 * @param theTransaction the transaction to process
	 * @return the transaction outcome
	 */
	default <T extends IBaseBundle> T transaction(T theTransaction) {
		checkNotNull(theTransaction);

		return transaction(theTransaction, new SystemRequestDetails());
	}

	/**
	 * NOTE: This is untested as of the time I'm writing this so it may need to be
	 * reworked.
	 * Executes a given transaction Bundle on the local server
	 *
	 * @param <T>               the type of Bundle
	 * @param theTransaction    the transaction to process
	 * @param theRequestDetails multi-tenancy information
	 * @return the transaction outcome
	 */
	@SuppressWarnings("unchecked")
	default <T extends IBaseBundle> T transaction(T theTransaction, RequestDetails theRequestDetails) {
		checkNotNull(theTransaction);

		return (T) getDaoRegistry().getSystemDao().transaction(theRequestDetails, theTransaction);
	}
}
