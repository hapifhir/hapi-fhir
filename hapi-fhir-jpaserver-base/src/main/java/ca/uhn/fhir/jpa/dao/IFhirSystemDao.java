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
import java.util.Map;

import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.IBundleProvider;

/**
 * @param <T>
 *           The bundle type
 */
public interface IFhirSystemDao<T, MT> extends IDao {

	/**
	 * Use with caution! This deletes everything!!
	 * @param theRequestDetails TODO
	 */
	void deleteAllTagsOnServer(RequestDetails theRequestDetails);

	TagList getAllTags(RequestDetails theRequestDetails);

	Map<String, Long> getResourceCounts();

	IBundleProvider history(Date theDate, RequestDetails theRequestDetails);

	/**
	 * Marks all indexes as needing fresh indexing
	 * 
	 * @return Returns the number of affected rows
	 */
	int markAllResourcesForReindexing();

	/**
	 * Not supported for DSTU1
	 * @param theRequestDetails TODO
	 */
	MT metaGetOperation(RequestDetails theRequestDetails);

	int performReindexingPass(Integer theCount, RequestDetails theRequestDetails);

	T transaction(RequestDetails theRequestDetails, T theResources);

}
