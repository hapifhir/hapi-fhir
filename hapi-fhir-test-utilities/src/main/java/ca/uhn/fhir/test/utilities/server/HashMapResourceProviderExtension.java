package ca.uhn.fhir.test.utilities.server;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class HashMapResourceProviderExtension<T extends IBaseResource> extends HashMapResourceProvider<T> implements BeforeEachCallback, AfterEachCallback {

	private final RestfulServerExtension myRestfulServerExtension;

	/**
	 * Constructor
	 *
	 * @param theResourceType The resource type to support
	 */
	public HashMapResourceProviderExtension(RestfulServerExtension theRestfulServerExtension, Class<T> theResourceType) {
		super(theRestfulServerExtension.getFhirContext(), theResourceType);

		myRestfulServerExtension = theRestfulServerExtension;
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		myRestfulServerExtension.getRestfulServer().unregisterProvider(HashMapResourceProviderExtension.this);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		clear();
		myRestfulServerExtension.getRestfulServer().registerProvider(HashMapResourceProviderExtension.this);
	}
}
