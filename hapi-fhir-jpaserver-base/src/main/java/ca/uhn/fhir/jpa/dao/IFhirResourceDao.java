package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFhirResourceDao<T extends IBaseResource> extends IDao {

	void addTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel);

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
	DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing, RequestDetails theRequestDetails);

	DaoMethodOutcome create(T theResource, String theIfNoneExist, RequestDetails theRequestDetails);

	/**
	 * Delete a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors. Use only for internal system calls
	 */
	DaoMethodOutcome delete(IIdType theResource);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 *
	 * @param theRequestDetails TODO
	 */
	DaoMethodOutcome delete(IIdType theResource, List<DeleteConflict> theDeleteConflictsListToPopulate, RequestDetails theRequestDetails);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DaoMethodOutcome delete(IIdType theResource, RequestDetails theRequestDetails);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 */
	DeleteMethodOutcome deleteByUrl(String theUrl, List<DeleteConflict> theDeleteConflictsListToPopulate, RequestDetails theRequestDetails);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DeleteMethodOutcome deleteByUrl(String theString, RequestDetails theRequestDetails);

	ExpungeOutcome expunge(ExpungeOptions theExpungeOptions);

	ExpungeOutcome expunge(IIdType theIIdType, ExpungeOptions theExpungeOptions);

	TagList getAllResourceTags(RequestDetails theRequestDetails);

	<R extends IBaseResource> IFhirResourceDao<R> getDao(Class<R> theType);

	Class<T> getResourceType();

	TagList getTags(IIdType theResourceId, RequestDetails theRequestDetails);

	IBundleProvider history(Date theSince, Date theUntil, RequestDetails theRequestDetails);

	IBundleProvider history(IIdType theId, Date theSince, Date theUntil, RequestDetails theRequestDetails);

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

	DaoMethodOutcome patch(IIdType theId, PatchTypeEnum thePatchType, String thePatchBody, RequestDetails theRequestDetails);

	Set<Long> processMatchUrl(String theMatchUrl);

	/**
	 * Read a resource - Note that this variant of the method does not take in a {@link RequestDetails} and
	 * therefore can not fire any interceptors. Use only for internal system calls
	 */
	T read(IIdType theId);

	/**
	 * @param theId
	 * @param theRequestDetails TODO
	 * @throws ResourceNotFoundException If the ID is not known to the server
	 */
	T read(IIdType theId, RequestDetails theRequestDetails);

	BaseHasResource readEntity(IIdType theId);

	/**
	 * @param theCheckForForcedId If true, this method should fail if the requested ID contains a numeric PID which exists, but is
	 *                            obscured by a "forced ID" so should not exist as far as the outside world is concerned.
	 */
	BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId);

	/**
	 * Updates index tables associated with the given resource. Does not create a new
	 * version or update the resource's update time.
	 */
	void reindex(T theResource, ResourceTable theEntity);

	void removeTag(IIdType theId, TagTypeEnum theTagType, String theSystem, String theCode, RequestDetails theRequestDetails);

	void removeTag(IIdType theId, TagTypeEnum theTagType, String theSystem, String theCode);

	IBundleProvider search(SearchParameterMap theParams);

	IBundleProvider search(SearchParameterMap theParams, RequestDetails theRequestDetails);

	@Transactional(propagation = Propagation.SUPPORTS)
	IBundleProvider search(SearchParameterMap theParams, RequestDetails theRequestDetails, HttpServletResponse theServletResponse);

	Set<Long> searchForIds(SearchParameterMap theParams);

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
	DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, boolean theForceUpdateVersion, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 *
	 * @param theRequestDetails TODO
	 */
	MethodOutcome validate(T theResource, IIdType theId, String theRawResource, EncodingEnum theEncoding, ValidationModeEnum theMode, String theProfile, RequestDetails theRequestDetails);

	RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(String criteria);

	// /**
	// * Invoke the everything operation
	// */
	// IBundleProvider everything(IIdType theId);

}
