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

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class ExternallyStoredResourceServiceRegistry {

	private static final String VALID_ID_PATTERN = "[a-zA-Z0-9_.-]+";
	private final Map<String, IExternallyStoredResourceService> myIdToProvider = new HashMap<>();

	/**
	 * Registers a new provider. Do not call this method after the server has been started.
	 *
	 * @param theProvider The provider to register.
	 */
	public void registerProvider(@Nonnull IExternallyStoredResourceService theProvider) {
		String id = defaultString(theProvider.getId());
		Validate.isTrue(
				id.matches(VALID_ID_PATTERN),
				"Invalid provider ID (must match pattern " + VALID_ID_PATTERN + "): %s",
				id);
		Validate.isTrue(!myIdToProvider.containsKey(id), "Already have a provider with ID: %s", id);

		myIdToProvider.put(id, theProvider);
	}

	/**
	 * Do we have any providers registered?
	 */
	public boolean hasProviders() {
		return !myIdToProvider.isEmpty();
	}

	/**
	 * Clears all registered providers. This method is mostly intended for unit tests.
	 */
	public void clearProviders() {
		myIdToProvider.clear();
	}

	@Nonnull
	public IExternallyStoredResourceService getProvider(@Nonnull String theProviderId) {
		IExternallyStoredResourceService retVal = myIdToProvider.get(theProviderId);
		Validate.notNull(retVal, "Invalid ESR provider ID: %s", theProviderId);
		return retVal;
	}
}
