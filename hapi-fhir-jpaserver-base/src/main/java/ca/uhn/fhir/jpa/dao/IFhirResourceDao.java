package ca.uhn.fhir.jpa.dao;

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
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public interface IFhirResourceDao<T extends IResource> extends IDao {

	void addTag(IdDt theId, String theScheme, String theTerm, String theLabel);

	DaoMethodOutcome create(T theResource);

	DaoMethodOutcome create(T theResource, String theIfNoneExist);

	/**
	 * @param thePerformIndexing Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *                           won't be indexed and searches won't work.
	 */
	DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing);

	DaoMethodOutcome delete(IdDt theResource);

	DaoMethodOutcome deleteByUrl(String theString);

	TagList getAllResourceTags();

	Class<T> getResourceType();

	TagList getTags(IdDt theResourceId);

	IBundleProvider history(Date theSince);

	IBundleProvider history(IdDt theId, Date theSince);

	IBundleProvider history(Long theId, Date theSince);

	/**
	 * 
	 * @param theId
	 * @return
	 * @throws ResourceNotFoundException
	 *             If the ID is not known to the server
	 */
	T read(IdDt theId);

	BaseHasResource readEntity(IdDt theId);

	/**
	 * @param theCheckForForcedId If true, this method should fail if the requested ID contains a numeric PID which exists, but is
	 *                            obscured by a "forced ID" so should not exist as far as the outside world is concerned.
	 */
	BaseHasResource readEntity(IdDt theId, boolean theCheckForForcedId);

	void removeTag(IdDt theId, String theScheme, String theTerm);

	IBundleProvider search(Map<String, IQueryParameterType> theParams);

	IBundleProvider search(SearchParameterMap theMap);

	IBundleProvider search(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIds(Map<String, IQueryParameterType> theParams);

	Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIdsWithAndOr(SearchParameterMap theParams);

	DaoMethodOutcome update(T theResource);

	DaoMethodOutcome update(T theResource, String theMatchUrl);

	/**
	 * @param thePerformIndexing
	 *            Use with caution! If you set this to false, you need to manually perform indexing or your resources
	 *            won't be indexed and searches won't work.
	 */
	DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing);

}
