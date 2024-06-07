/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class HashMapResourceProviderExtension<T extends IBaseResource> extends HashMapResourceProvider<T> implements BeforeEachCallback, AfterEachCallback {

	private final RestfulServerExtension myRestfulServerExtension;
	private boolean myClearBetweenTests = true;
	private final List<T> myUpdates = new ArrayList<>();

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
	@Search(allowUnknownParams = true)
	public synchronized IBundleProvider searchAll(RequestDetails theRequestDetails) {
		return super.searchAll(theRequestDetails);
	}

	@Override
	public synchronized void clear() {
		super.clear();
		if (myUpdates != null) {
			myUpdates.clear();
		}
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		if (myClearBetweenTests) {
			clear();
			clearCounts();
		}
		myRestfulServerExtension.getRestfulServer().registerProvider(HashMapResourceProviderExtension.this);
	}

	@Override
	public synchronized MethodOutcome update(T theResource, String theConditional, RequestDetails theRequestDetails) {
		T resourceClone = getFhirContext().newTerser().clone(theResource);
		myUpdates.add(resourceClone);
		return super.update(theResource, theConditional, theRequestDetails);
	}

	public HashMapResourceProviderExtension<T> dontClearBetweenTests() {
		myClearBetweenTests = false;
		return this;
	}

	public void waitForUpdateCount(long theCount) {
		assertThat(theCount).isGreaterThanOrEqualTo(getCountUpdate());
		await().until(() -> this.getCountUpdate() == theCount);
	}

	public void waitForCreateCount(long theCount) {
		assertThat(theCount).isGreaterThanOrEqualTo(getCountCreate());
		await().until(() -> this.getCountCreate() == theCount);
	}

	public void waitForDeleteCount(long theCount) {
		assertThat(theCount).isGreaterThanOrEqualTo(getCountDelete());
		await().until(() -> this.getCountDelete() == theCount);
	}

	public List<T> getResourceUpdates() {
		return Collections.unmodifiableList(myUpdates);
	}
}
