package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
	 * <p>
	 * Note that the <code>theRequest</code> parameter was added to this
	 * method in HAPI FHIR 4.0.0. Existing implementations may choose to
	 * add this parameter and not use it if needed.
	 * </p>
	 */
	IBundleProvider retrieveResultList(@Nullable RequestDetails theRequestDetails, @Nonnull String theSearchId);

	/**
	 * Retrieve a result list by ID
	 * <p>
	 * Note that the <code>theRequest</code> parameter was added to this
	 * method in HAPI FHIR 4.0.0. Existing implementations may choose to
	 * add this parameter and not use it if needed.
	 * </p>
	 */
	default IBundleProvider retrieveResultList(@Nullable RequestDetails theRequestDetails, @Nonnull String theSearchId, String thePageId) {
		return null;
	}

	/**
	 * Stores a result list and returns an ID with which that list can be returned
	 *
	 * @param theRequestDetails The server request being made (may be null)
	 */
	String storeResultList(@Nullable RequestDetails theRequestDetails, IBundleProvider theList);

}
