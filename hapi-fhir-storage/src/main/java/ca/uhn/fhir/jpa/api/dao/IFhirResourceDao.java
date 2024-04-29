/*
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.HistorySearchDateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Note that this interface is not considered a stable interface. While it is possible to build applications
 * that use it directly, please be aware that we may modify methods, add methods, or even remove methods from
 * time to time, even within minor point releases.
 */
public interface IFhirResourceDao<T extends IBaseResource> extends IDao {

	/**
	 * Create a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors.
	 *
	 * @deprecated Use {@link #create(IBaseResource, RequestDetails)} instead
	 */
	DaoMethodOutcome create(T theResource);

	DaoMethodOutcome create(T theResource, RequestDetails theRequestDetails);

	/**
	 * Create a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors.
	 *
	 * @deprecated Use {@link #create(IBaseResource, String, RequestDetails)} instead
	 */
	DaoMethodOutcome create(T theResource, String theIfNoneExist);

	/**
	 * @param thePerformIndexing Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *                           won't be indexed and searches won't work.
	 * @param theRequestDetails  The request details including permissions and partitioning information
	 */
	DaoMethodOutcome create(
			T theResource,
			String theIfNoneExist,
			boolean thePerformIndexing,
			RequestDetails theRequestDetails,
			@Nonnull TransactionDetails theTransactionDetails);

	DaoMethodOutcome create(T theResource, String theIfNoneExist, RequestDetails theRequestDetails);

	/**
	 * Delete a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors.
	 *
	 * @deprecated Use {@link #delete(IIdType, RequestDetails)} instead
	 */
	DaoMethodOutcome delete(IIdType theResource);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 */
	DaoMethodOutcome delete(
			IIdType theResource,
			DeleteConflictList theDeleteConflictsListToPopulate,
			RequestDetails theRequestDetails,
			@Nonnull TransactionDetails theTransactionDetails);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DaoMethodOutcome delete(IIdType theResource, RequestDetails theRequestDetails);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 *
	 * @since 6.8.0
	 */
	DeleteMethodOutcome deleteByUrl(
			String theUrl,
			DeleteConflictList theDeleteConflictsListToPopulate,
			RequestDetails theRequestDetails,
			@Nonnull TransactionDetails theTransactionDetails);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DeleteMethodOutcome deleteByUrl(String theString, RequestDetails theRequestDetails);

	/**
	 * @deprecated Deprecated in 6.8.0 - Use and implement {@link #deletePidList(String, Collection, DeleteConflictList, RequestDetails, TransactionDetails)}
	 */
	default <P extends IResourcePersistentId> DeleteMethodOutcome deletePidList(
			String theUrl,
			Collection<P> theResourceIds,
			DeleteConflictList theDeleteConflicts,
			RequestDetails theRequest) {
		return deletePidList(theUrl, theResourceIds, theDeleteConflicts, theRequest, new TransactionDetails());
	}

	/**
	 * Delete a list of resource Pids
	 * <p>
	 * CAUTION: This list does not throw an exception if there are delete conflicts.  It should always be followed by
	 * a call to DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(fhirContext, conflicts);
	 * to actually throw the exception.  The reason this method doesn't do that itself is that it is expected to be
	 * called repeatedly where an earlier conflict can be removed in a subsequent pass.
	 *
	 * @param theUrl             the original URL that triggered the deletion
	 * @param theResourceIds     the ids of the resources to be deleted
	 * @param theDeleteConflicts out parameter of conflicts preventing deletion
	 * @param theRequestDetails         the request that initiated the request
	 * @return response back to the client
	 * @since 6.8.0
	 */
	<P extends IResourcePersistentId> DeleteMethodOutcome deletePidList(
			String theUrl,
			Collection<P> theResourceIds,
			DeleteConflictList theDeleteConflicts,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails);

	ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails);

	ExpungeOutcome expunge(IIdType theIIdType, ExpungeOptions theExpungeOptions, RequestDetails theRequest);

	<P extends IResourcePersistentId> void expunge(Collection<P> theResourceIds, RequestDetails theRequest);

	ExpungeOutcome forceExpungeInExistingTransaction(
			IIdType theId, ExpungeOptions theExpungeOptions, RequestDetails theRequest);

	@Nonnull
	Class<T> getResourceType();

	IBundleProvider history(Date theSince, Date theUntil, Integer theOffset, RequestDetails theRequestDetails);

	/**
	 * @deprecated Use {@link #history(IIdType, HistorySearchDateRangeParam, RequestDetails)} instead
	 */
	@Deprecated(since = "6.2")
	IBundleProvider history(
			IIdType theId, Date theSince, Date theUntil, Integer theOffset, RequestDetails theRequestDetails);

	IBundleProvider history(
			IIdType theId,
			HistorySearchDateRangeParam theHistorySearchDateRangeParam,
			RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails The request details including permissions and partitioning information
	 */
	<MT extends IBaseMetaType> MT metaAddOperation(IIdType theId1, MT theMetaAdd, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails The request details including permissions and partitioning information
	 */
	<MT extends IBaseMetaType> MT metaDeleteOperation(IIdType theId1, MT theMetaDel, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails The request details including permissions and partitioning information
	 */
	<MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails The request details including permissions and partitioning information
	 */
	<MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, RequestDetails theRequestDetails);

	/**
	 * Opens a new transaction and performs a patch operation
	 */
	DaoMethodOutcome patch(
			IIdType theId,
			String theConditionalUrl,
			PatchTypeEnum thePatchType,
			String thePatchBody,
			IBaseParameters theFhirPatchBody,
			RequestDetails theRequestDetails);

	/**
	 * Execute a patch operation within the existing database transaction
	 */
	DaoMethodOutcome patchInTransaction(
			IIdType theId,
			String theConditionalUrl,
			boolean thePerformIndexing,
			PatchTypeEnum thePatchType,
			String thePatchBody,
			IBaseParameters theFhirPatchBody,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails);

	/**
	 * Read a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors.
	 *
	 * @deprecated Use {@link #read(IIdType, RequestDetails)} instead
	 */
	T read(IIdType theId);

	/**
	 * Read a resource by its internal PID
	 *
	 * @throws ResourceNotFoundException If the ID is not known to the server
	 * @throws ResourceGoneException     If the resource has been deleted
	 */
	T readByPid(IResourcePersistentId thePid);

	/**
	 * Read a resource by its internal PID
	 *
	 * @throws ResourceNotFoundException If the ID is not known to the server
	 * @throws ResourceGoneException     If the resource has been deleted and theDeletedOk is true
	 */
	default T readByPid(IResourcePersistentId thePid, boolean theDeletedOk) {
		throw new UnsupportedOperationException(Msg.code(571));
	}

	/**
	 * @param theRequestDetails The request details including permissions and partitioning information
	 * @throws ResourceNotFoundException If the ID is not known to the server
	 * @throws ResourceGoneException     If the resource has been deleted
	 */
	T read(IIdType theId, RequestDetails theRequestDetails);

	/**
	 * Should deleted resources be returned successfully. This should be false for
	 * a normal FHIR read.
	 *
	 * @throws ResourceNotFoundException If the ID is not known to the server
	 * @throws ResourceGoneException     If the resource has been deleted and theDeletedOk is true
	 */
	T read(IIdType theId, RequestDetails theRequestDetails, boolean theDeletedOk);

	/**
	 * Read an entity from the database, and return it. Note that here we're talking about whatever the
	 * native database representation is, not the parsed {@link IBaseResource} instance.
	 *
	 * @param theId      The resource ID to fetch
	 * @param theRequest The request details object associated with the request
	 */
	IBasePersistedResource readEntity(IIdType theId, RequestDetails theRequest);

	/**
	 * Updates index tables associated with the given resource. Does not create a new
	 * version or update the resource's update time.
	 *
	 * @param theResource The FHIR resource object corresponding to the entity to reindex
	 * @param theEntity   The storage entity to reindex
	 * @deprecated Use {@link #reindex(IResourcePersistentId, ReindexParameters, RequestDetails, TransactionDetails)}
	 */
	@Deprecated
	void reindex(T theResource, IBasePersistedResource theEntity);

	/**
	 * Reindex the given resource
	 *
	 * @param theResourcePersistentId The ID
	 * @return
	 */
	ReindexOutcome reindex(
			IResourcePersistentId theResourcePersistentId,
			ReindexParameters theReindexParameters,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails);

	void removeTag(
			IIdType theId, TagTypeEnum theTagType, String theSystem, String theCode, RequestDetails theRequestDetails);

	void removeTag(IIdType theId, TagTypeEnum theTagType, String theSystem, String theCode);

	/**
	 * @deprecated Use {@link #search(SearchParameterMap, RequestDetails)} instead
	 * @throws InvalidRequestException If a SearchParameter is not known to the server
	 */
	IBundleProvider search(SearchParameterMap theParams) throws InvalidRequestException;

	/**
	 * *
	 * @throws InvalidRequestException If a SearchParameter is not known to the server
	 */
	IBundleProvider search(SearchParameterMap theParams, RequestDetails theRequestDetails)
			throws InvalidRequestException;

	/**
	 * *
	 * @throws InvalidRequestException If a SearchParameter is not known to the server
	 */
	IBundleProvider search(
			SearchParameterMap theParams, RequestDetails theRequestDetails, HttpServletResponse theServletResponse)
			throws InvalidRequestException;

	/**
	 * Search for IDs for processing a match URLs, etc.
	 */
	default <PT extends IResourcePersistentId> List<PT> searchForIds(
			SearchParameterMap theParams, RequestDetails theRequest) {
		return searchForIds(theParams, theRequest, null);
	}

	/**
	 * Search for IDs for processing a match URLs, etc.
	 *
	 * @param theConditionalOperationTargetOrNull If we're searching for IDs in order to satisfy a conditional
	 *                                            create/update, this is the resource being searched for
	 * @since 5.5.0
	 */
	default <PT extends IResourcePersistentId> List<PT> searchForIds(
			SearchParameterMap theParams,
			RequestDetails theRequest,
			@Nullable IBaseResource theConditionalOperationTargetOrNull) {
		return searchForIds(theParams, theRequest);
	}

	/**
	 * Search results matching theParams.
	 * This call does not currently invoke any interceptors, so should only be used for infrastructure that
	 * will not need to participate in the consent services, or caching.
	 * The Stream MUST be closed to avoid leaking resources.
	 * If called within a transaction, the Stream will fail if passed outside the tx boundary.
	 * @param theParams the search
	 * @param theRequest for partition target info
	 * @return a Stream that MUST only be used within the calling transaction.
	 */
	default <PID extends IResourcePersistentId<?>> Stream<PID> searchForIdStream(
			SearchParameterMap theParams,
			RequestDetails theRequest,
			@Nullable IBaseResource theConditionalOperationTargetOrNull) {
		List<PID> iResourcePersistentIds = searchForIds(theParams, theRequest);
		return iResourcePersistentIds.stream();
	}

	/**
	 * Return all search results matching theParams.
	 * Will load all resources into ram, so not appropriate for large data sets.
	 * This call invokes both preaccess and preshow interceptors.
	 * @param theParams the search
	 * @param theRequest for partition target info
	 */
	default List<T> searchForResources(SearchParameterMap theParams, RequestDetails theRequest) {
		IBundleProvider provider = search(theParams, theRequest);
		//noinspection unchecked
		return (List<T>) provider.getAllResources();
	}

	/**
	 * Return the FHIR Ids matching theParams.
	 * This call does not currently invoke any interceptors, so should only be used for infrastructure that
	 * will not need to participate in the consent services, or caching.
	 * @param theParams the search
	 * @param theRequest for partition target info
	 */
	default List<IIdType> searchForResourceIds(SearchParameterMap theParams, RequestDetails theRequest) {
		return searchForResources(theParams, theRequest).stream()
				.map(IBaseResource::getIdElement)
				.collect(Collectors.toList());
	}

	/**
	 * Takes a map of incoming raw search parameters and translates/parses them into
	 * appropriate {@link IQueryParameterType} instances of the appropriate type
	 * for the given param
	 *
	 * @throws InvalidRequestException If any of the parameters are not known
	 */
	void translateRawParameters(Map<String, List<String>> theSource, SearchParameterMap theTarget);

	/**
	 * Update a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors.
	 *
	 * @deprecated Use {@link #update(T, RequestDetails)} instead
	 */
	DaoMethodOutcome update(T theResource);

	DaoMethodOutcome update(T theResource, RequestDetails theRequestDetails);

	/**
	 * Update a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors.
	 *
	 * @deprecated Use {@link #update(T, String, RequestDetails)} instead
	 */
	DaoMethodOutcome update(T theResource, String theMatchUrl);

	/**
	 * @param thePerformIndexing Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *                           won't be indexed and searches won't work.
	 * @param theRequestDetails  The request details including permissions and partitioning information
	 */
	DaoMethodOutcome update(
			T theResource, String theMatchUrl, boolean thePerformIndexing, RequestDetails theRequestDetails);

	DaoMethodOutcome update(T theResource, String theMatchUrl, RequestDetails theRequestDetails);

	/**
	 * @param theForceUpdateVersion Create a new version with the same contents as the current version even if the content hasn't changed (this is mostly useful for
	 *                              resources mapping to external content such as external code systems)
	 */
	DaoMethodOutcome update(
			T theResource,
			String theMatchUrl,
			boolean thePerformIndexing,
			boolean theForceUpdateVersion,
			RequestDetails theRequestDetails,
			@Nonnull TransactionDetails theTransactionDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails The request details including permissions and partitioning information
	 * @return MethodOutcome even if the resource fails validation it should still successfully return with a response status of 200
	 */
	MethodOutcome validate(
			T theResource,
			IIdType theId,
			String theRawResource,
			EncodingEnum theEncoding,
			ValidationModeEnum theMode,
			String theProfile,
			RequestDetails theRequestDetails);

	RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(String criteria);

	/**
	 * @deprecated use #read(IIdType, RequestDetails) instead
	 */
	default String getCurrentVersionId(IIdType theReferenceElement) {
		return read(theReferenceElement.toVersionless()).getIdElement().getVersionIdPart();
	}
}
