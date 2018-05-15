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

import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Map;

/**
 * @param <T>  The bundle type
 * @param <MT> The Meta datatype type
 */
public interface IFhirSystemDao<T, MT> extends IDao {

	ExpungeOutcome expunge(ExpungeOptions theExpungeOptions);

	@SuppressWarnings("unchecked")
	<R extends IBaseResource> IFhirResourceDao<R> getDao(Class<R> theType);

	Map<String, Long> getResourceCounts();

	/**
	 * Returns a cached count of resources using a cache that regularly
	 * refreshes in the background. This method will never
	 */
	@Nullable
	Map<String, Long> getResourceCountsFromCache();


	IBundleProvider history(Date theDate, Date theUntil, RequestDetails theRequestDetails);

	/**
	 * Marks all indexes as needing fresh indexing
	 *
	 * @return Returns the number of affected rows
	 */
	int markAllResourcesForReindexing();

	/**
	 * Not supported for DSTU1
	 *
	 * @param theRequestDetails TODO
	 */
	MT metaGetOperation(RequestDetails theRequestDetails);

	Integer performReindexingPass(Integer theCount);

	T transaction(RequestDetails theRequestDetails, T theResources);

}
