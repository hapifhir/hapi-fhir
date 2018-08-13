package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.server.IBundleProvider;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

public interface IPagingProvider {

	int getDefaultPageSize();

	int getMaximumPageSize();

	/**
	 * Retrieve a result list by ID
	 */
	IBundleProvider retrieveResultList(String theSearchId);

	/**
	 * Retrieve a result list by ID
	 */
	default IBundleProvider retrieveResultList(String theSearchId, String thePageId) {
		return null;
	}

	/**
	 * Stores a result list and returns an ID with which that list can be returned
	 */
	String storeResultList(IBundleProvider theList);

}
