package ca.uhn.fhir.jpa.api.dao;

/*
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Note that this interface is not considered a stable interface. While it is possible to build applications
 * that use it directly, please be aware that we may modify methods, add methods, or even remove methods from
 * time to time, even within minor point releases.
 */
public interface IFhirResourceDao<T extends IBaseResource> extends IDao {

	/**
	 * Create a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors. Use only for internal system calls
	 */
	DaoMethodOutcome create(T theResource);

	DaoMethodOutcome create(T theResource, RequestDetails theRequestDetails);

	/**
	 * Create a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors. Use only for internal system calls
	 */
	DaoMethodOutcome create(T theResource, String theIfNoneExist);

	/**
	 * @param thePerformIndexing Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *                           won't be indexed and searches won't work.
	 * @param theRequestDetails  TODO
	 */
	DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing, @Nonnull TransactionDetails theTransactionDetails, RequestDetails theRequestDetails);

	DaoMethodOutcome create(T theResource, String theIfNoneExist, RequestDetails theRequestDetails);

	/**
	 * Delete a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors. Use only for internal system calls
	 */
	DaoMethodOutcome delete(IIdType theResource);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 */
	DaoMethodOutcome delete(IIdType theResource, DeleteConflictList theDeleteConflictsListToPopulate, RequestDetails theRequestDetails, @Nonnull TransactionDetails theTransactionDetails);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DaoMethodOutcome delete(IIdType theResource, RequestDetails theRequestDetails);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 */
	DeleteMethodOutcome deleteByUrl(String theUrl, DeleteConflictList theDeleteConflictsListToPopulate, RequestDetails theRequestDetails);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DeleteMethodOutcome deleteByUrl(String theString, RequestDetails theRequestDetails);

	ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails);

	ExpungeOutcome expunge(IIdType theIIdType, ExpungeOptions theExpungeOptions, RequestDetails theRequest);

	ExpungeOutcome forceExpungeInExistingTransaction(IIdType theId, ExpungeOptions theExpungeOptions, RequestDetails theRequest);

	Class<T> getResourceType();

	IBundleProvider history(Date theSince, Date theUntil, Integer theOffset, RequestDetails theRequestDetails);

	IBundleProvider history(IIdType theId, Date theSince, Date theUntil, Integer theOffset, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails TODO
	 */
	<MT extends IBaseMetaType> MT metaAddOperation(IIdType theId1, MT theMetaAdd, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails TODO
	 */
	<MT extends IBaseMetaType> MT metaDeleteOperation(IIdType theId1, MT theMetaDel, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails TODO
	 */
	<MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails TODO
	 */
	<MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, RequestDetails theRequestDetails);

	DaoMethodOutcome patch(IIdType theId, String theConditionalUrl, PatchTypeEnum thePatchType, String thePatchBody, IBaseParameters theFhirPatchBody, RequestDetails theRequestDetails);

	/**
	 * Read a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors. Use only for internal system calls
	 */
	T read(IIdType theId);

	/**
	 * Read a resource by its internal PID
	 */
	T readByPid(ResourcePersistentId thePid);

	/**
	 * Read a resource by its internal PID
	 */
	default T readByPid(ResourcePersistentId thePid, boolean theDeletedOk) {
		throw new UnsupportedOperationException(Msg.code(571));
	}

	/**
	 * @param theRequestDetails TODO
	 * @throws ResourceNotFoundException If the ID is not known to the server
	 */
	T read(IIdType theId, RequestDetails theRequestDetails);

	/**
	 * Should deleted resources be returned successfully. This should be false for
	 * a normal FHIR read.
	 */
	T read(IIdType theId, RequestDetails theRequestDetails, boolean theDeletedOk);

	BaseHasResource readEntity(IIdType theId, RequestDetails theRequest);

	/**
	 * @param theCheckForForcedId If true, this method should fail if the requested ID contains a numeric PID which exists, but is
	 *                            obscured by a "forced ID" so should not exist as far as the outside world is concerned.
	 */
	BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId, RequestDetails theRequest);

	/**
	 * Updates index tables associated with the given resource. Does not create a new
	 * version or update the resource's update time.
	 */
	void reindex(T theResource, ResourceTable theEntity);

	void removeTag(IIdType theId, TagTypeEnum theTagType, String theSystem, String theCode, RequestDetails theRequestDetails);

	void removeTag(IIdType theId, TagTypeEnum theTagType, String theSystem, String theCode);

	IBundleProvider search(SearchParameterMap theParams);

	IBundleProvider search(SearchParameterMap theParams, RequestDetails theRequestDetails);

	IBundleProvider search(SearchParameterMap theParams, RequestDetails theRequestDetails, HttpServletResponse theServletResponse);

	/**
	 * Search for IDs for processing a match URLs, etc.
	 */
	default List<ResourcePersistentId> searchForIds(SearchParameterMap theParams, RequestDetails theRequest) {
		return searchForIds(theParams, theRequest, null);
	}

	/**
	 * Search for IDs for processing a match URLs, etc.
	 *
	 * @param theConditionalOperationTargetOrNull If we're searching for IDs in order to satisfy a conditional
	 *                                            create/update, this is the resource being searched for
	 * @since 5.5.0
	 */
	default List<ResourcePersistentId> searchForIds(SearchParameterMap theParams, RequestDetails theRequest, @Nullable IBaseResource theConditionalOperationTargetOrNull) {
		return searchForIds(theParams, theRequest);
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
	 * therefore can not fire any interceptors. Use only for internal system calls
	 */
	DaoMethodOutcome update(T theResource);

	DaoMethodOutcome update(T theResource, RequestDetails theRequestDetails);

	/**
	 * Update a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors. Use only for internal system calls
	 */
	DaoMethodOutcome update(T theResource, String theMatchUrl);

	/**
	 * @param thePerformIndexing Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *                           won't be indexed and searches won't work.
	 * @param theRequestDetails  TODO
	 */
	DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, RequestDetails theRequestDetails);

	DaoMethodOutcome update(T theResource, String theMatchUrl, RequestDetails theRequestDetails);

	/**
	 * @param theForceUpdateVersion Create a new version with the same contents as the current version even if the content hasn't changed (this is mostly useful for
	 *                              resources mapping to external content such as external code systems)
	 */
	DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, boolean theForceUpdateVersion, RequestDetails theRequestDetails, @Nonnull TransactionDetails theTransactionDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails TODO
	 */
	MethodOutcome validate(T theResource, IIdType theId, String theRawResource, EncodingEnum theEncoding, ValidationModeEnum theMode, String theProfile, RequestDetails theRequestDetails);

	RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(String criteria);

	/**
	 * Delete a list of resource Pids
	 *
	 * @param theUrl             the original URL that triggered the delete
	 * @param theResourceIds     the ids of the resources to be deleted
	 * @param theDeleteConflicts out parameter of conflicts preventing deletion
	 * @param theRequest         the request that initiated the request
	 * @return response back to the client
	 */
	DeleteMethodOutcome deletePidList(String theUrl, Collection<ResourcePersistentId> theResourceIds, DeleteConflictList theDeleteConflicts, RequestDetails theRequest);

	/**
	 * Returns the current version ID for the given resource
	 */
	default String getCurrentVersionId(IIdType theReferenceElement) {
		return read(theReferenceElement.toVersionless()).getIdElement().getVersionIdPart();
	}

	/**
	 * Reindex the given resource
	 *
	 * @param theResourcePersistentId The ID
	 */
	void reindex(ResourcePersistentId theResourcePersistentId, RequestDetails theRequest, TransactionDetails theTransactionDetails);
}
