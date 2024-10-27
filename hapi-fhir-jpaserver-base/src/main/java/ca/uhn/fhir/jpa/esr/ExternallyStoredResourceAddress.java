/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.esr;

public class ExternallyStoredResourceAddress {

	private final String myProviderId;
	private final String myLocation;

	/**
	 * Constructor
	 *
	 * @param theProviderId The ID of the provider which will handle this address. Must match the ID returned by a registered {@link IExternallyStoredResourceService}.
	 * @param theLocation   The actual location for the provider to resolve. The format of this string is entirely up to the {@link IExternallyStoredResourceService} and only needs to make sense to it.
	 */
	public ExternallyStoredResourceAddress(String theProviderId, String theLocation) {
		myProviderId = theProviderId;
		myLocation = theLocation;
	}

	/**
	 * @return The ID of the provider which will handle this address. Must match the ID returned by a registered {@link IExternallyStoredResourceService}.
	 */
	public String getProviderId() {
		return myProviderId;
	}

	/**
	 * @return The actual location for the provider to resolve. The format of this string is entirely up to the {@link IExternallyStoredResourceService} and only needs to make sense to it.
	 */
	public String getLocation() {
		return myLocation;
	}
}
