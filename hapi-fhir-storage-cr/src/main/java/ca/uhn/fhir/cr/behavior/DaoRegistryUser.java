package ca.uhn.fhir.cr.behavior;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.TypedBundleProvider;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.cql.evaluator.fhir.behavior.FhirContextUser;

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
public interface DaoRegistryUser extends FhirContextUser {

	public DaoRegistry getDaoRegistry();

	public default FhirContext getFhirContext() {
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
	public default <T extends IBaseResource> Class<T> getClass(String theResourceName) {
		return (Class<T>) getFhirContext().getResourceDefinition(theResourceName).getImplementingClass();
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

		return read(theId, null);
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

		return create(theResource, null);
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

		return ((IFhirResourceDao<T>) getDaoRegistry().getResourceDao(theResource.fhirType())).create(theResource,
				requestDetails);
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

		return update(theResource, null);
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

		return ((IFhirResourceDao<T>) getDaoRegistry().getResourceDao(theResource.fhirType())).update(theResource,
				requestDetails);
	}

	/**
	 * Deletes the Resource with the given Id from the local server
	 *
	 * @param theIdType the Id of the Resource to delete.
	 * @return the outcome of the deletion
	 */
	default DaoMethodOutcome delete(IIdType theIdType) {
		checkNotNull(theIdType);

		return delete(theIdType, null);
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

		return transaction(theTransaction, null);
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

	/**
	 * Searches for a Resource on the local server using the Search Parameters
	 * specified
	 *
	 * @param <T>              the type of Resource to return
	 * @param theResourceClass the class of the Resource
	 * @param theSearchMap     the Search Parameters
	 * @return Bundle provider
	 */
	default <T extends IBaseResource> TypedBundleProvider<T> search(Class<T> theResourceClass,
																						 SearchParameterMap theSearchMap) {
		checkNotNull(theResourceClass);
		checkNotNull(theSearchMap);

		return search(theResourceClass, theSearchMap, null);
	}

	/**
	 * Searches for a Resource on the local server using the Search Parameters
	 * specified
	 *
	 * @param <T>               the type of Resource to return
	 * @param theResourceClass  the class of the Resource
	 * @param theSearchMap      the Search Parameters
	 * @param theRequestDetails multi-tenancy information
	 * @return Bundle provider
	 */
	default <T extends IBaseResource> TypedBundleProvider<T> search(Class<T> theResourceClass,
			SearchParameterMap theSearchMap,
			RequestDetails theRequestDetails) {
		checkNotNull(theResourceClass);
		checkNotNull(theSearchMap);

		return TypedBundleProvider.fromBundleProvider(
				getDaoRegistry().getResourceDao(theResourceClass).search(theSearchMap, theRequestDetails));
	}
}
