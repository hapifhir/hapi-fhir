package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

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
	 * @param thePerformIndexing
	 *           Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *           won't be indexed and searches won't work.
	 * @param theRequestDetails TODO
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
	 * @param theRequestDetails TODO
	 */
	ResourceTable delete(IIdType theResource, List<DeleteConflict> theDeleteConflictsListToPopulate, RequestDetails theRequestDetails);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DaoMethodOutcome delete(IIdType theResource, RequestDetails theRequestDetails);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 */
	List<ResourceTable> deleteByUrl(String theUrl, List<DeleteConflict> theDeleteConflictsListToPopulate, RequestDetails theRequestDetails);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DaoMethodOutcome deleteByUrl(String theString, RequestDetails theRequestDetails);

	TagList getAllResourceTags(RequestDetails theRequestDetails);

	Class<T> getResourceType();

	TagList getTags(IIdType theResourceId, RequestDetails theRequestDetails);

	IBundleProvider history(Date theSince, Date theUntil, RequestDetails theRequestDetails);

	IBundleProvider history(IIdType theId, Date theSince, Date theUntil, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 * @param theRequestDetails TODO
	 */
	<MT extends IBaseMetaType> MT metaAddOperation(IIdType theId1, MT theMetaAdd, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 * @param theRequestDetails TODO
	 */
	<MT extends IBaseMetaType> MT metaDeleteOperation(IIdType theId1, MT theMetaDel, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 * @param theRequestDetails TODO
	 */
	<MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 * @param theRequestDetails TODO
	 */
	<MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, RequestDetails theRequestDetails);

	Set<Long> processMatchUrl(String theMatchUrl);

	/**
	 * Read a resource - Note that this variant of the method does not take in a {@link RequestDetails} and 
	 * therefore can not fire any interceptors. Use only for internal system calls 
	 */
	T read(IIdType theId);

	/**
	 * 
	 * @param theId
	 * @param theRequestDetails TODO
	 * @return
	 * @throws ResourceNotFoundException
	 *            If the ID is not known to the server
	 */
	T read(IIdType theId, RequestDetails theRequestDetails);

	BaseHasResource readEntity(IIdType theId);

	/**
	 * @param theCheckForForcedId
	 *           If true, this method should fail if the requested ID contains a numeric PID which exists, but is
	 *           obscured by a "forced ID" so should not exist as far as the outside world is concerned.
	 */
	BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId);

	/**
	 * Updates index tables associated with the given resource. Does not create a new
	 * version or update the resource's update time.
	 */
	void reindex(T theResource, ResourceTable theEntity);

	void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, RequestDetails theRequestDetails);

	IBundleProvider search(Map<String, IQueryParameterType> theParams);

	IBundleProvider search(SearchParameterMap theMap);

	IBundleProvider search(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIds(Map<String, IQueryParameterType> theParams);

	Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIdsWithAndOr(SearchParameterMap theParams);

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
	 * @param thePerformIndexing
	 *           Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *           won't be indexed and searches won't work.
	 * @param theRequestDetails TODO
	 */
	DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, RequestDetails theRequestDetails);

	DaoMethodOutcome update(T theResource, String theMatchUrl, RequestDetails theRequestDetails);

	/**
	 * Not supported in DSTU1!
	 * @param theRequestDetails TODO
	 */
	MethodOutcome validate(T theResource, IIdType theId, String theRawResource, EncodingEnum theEncoding, ValidationModeEnum theMode, String theProfile, RequestDetails theRequestDetails);

	DaoMethodOutcome patch(IIdType theId, PatchTypeEnum thePatchType, String thePatchBody, RequestDetails theRequestDetails);

//	/**
//	 * Invoke the everything operation
//	 */
//	IBundleProvider everything(IIdType theId);

}
