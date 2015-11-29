package ca.uhn.fhir.jpa.dao;

import java.util.Collection;

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
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public interface IFhirResourceDao<T extends IBaseResource> extends IDao {

	void addTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel);

	DaoMethodOutcome create(T theResource);

	DaoMethodOutcome create(T theResource, String theIfNoneExist);

	/**
	 * @param thePerformIndexing
	 *           Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *           won't be indexed and searches won't work.
	 */
	DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DaoMethodOutcome delete(IIdType theResource);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 */
	ResourceTable delete(IIdType theResource, List<DeleteConflict> theDeleteConflictsListToPopulate);

	/**
	 * This method throws an exception if there are delete conflicts
	 */
	DaoMethodOutcome deleteByUrl(String theString);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 * @return 
	 */
	List<ResourceTable> deleteByUrl(String theUrl, List<DeleteConflict> theDeleteConflictsListToPopulate);

	TagList getAllResourceTags();

	Class<T> getResourceType();

	TagList getTags(IIdType theResourceId);

	IBundleProvider history(Date theSince);

	IBundleProvider history(IIdType theId, Date theSince);

	IBundleProvider history(Long theId, Date theSince);

	/**
	 * Not supported in DSTU1!
	 */
	<MT extends IBaseMetaType> MT metaAddOperation(IIdType theId1, MT theMetaAdd);

	/**
	 * Not supported in DSTU1!
	 */
	<MT extends IBaseMetaType> MT metaDeleteOperation(IIdType theId1, MT theMetaDel);

	/**
	 * Not supported in DSTU1!
	 */
	<MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType);

	/**
	 * Not supported in DSTU1!
	 */
	<MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId);

	Set<Long> processMatchUrl(String theMatchUrl);

	/**
	 * 
	 * @param theId
	 * @return
	 * @throws ResourceNotFoundException
	 *            If the ID is not known to the server
	 */
	T read(IIdType theId);

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

	void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm);

	IBundleProvider search(Map<String, IQueryParameterType> theParams);

	IBundleProvider search(SearchParameterMap theMap);

	IBundleProvider search(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIds(Map<String, IQueryParameterType> theParams);

	Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIdsWithAndOr(SearchParameterMap theParams, Collection<Long> theInitialPids, DateRangeParam theLastUpdated);

	DaoMethodOutcome update(T theResource);

	DaoMethodOutcome update(T theResource, String theMatchUrl);

	/**
	 * @param thePerformIndexing
	 *           Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *           won't be indexed and searches won't work.
	 */
	DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing);

	/**
	 * Not supported in DSTU1!
	 */
	MethodOutcome validate(T theResource, IIdType theId, String theRawResource, EncodingEnum theEncoding, ValidationModeEnum theMode, String theProfile);

//	/**
//	 * Invoke the everything operation
//	 */
//	IBundleProvider everything(IIdType theId);

}
