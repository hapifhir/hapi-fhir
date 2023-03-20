/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class TypedBundleProvider<T extends IBaseResource> implements IBundleProvider {

	private final IBundleProvider myInnerProvider;

	private TypedBundleProvider(IBundleProvider theInnerProvider) {
		checkState(theInnerProvider.getNextPageId() == null,
			"TypedBundleProvider does not support paging and theInnerProvider has a next page.");
		myInnerProvider = checkNotNull(theInnerProvider);
	}

	public static <T extends IBaseResource> TypedBundleProvider<T> fromBundleProvider(
		IBundleProvider theBundleProvider) {
		return new TypedBundleProvider<>(theBundleProvider);
	}

	@Override
	public IPrimitiveType<Date> getPublished() {
		return myInnerProvider.getPublished();
	}

	@Override
	public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
		return myInnerProvider.getResources(theFromIndex, theToIndex);
	}

	@Override
	public String getUuid() {
		return myInnerProvider.getUuid();
	}

	@Override
	public Integer preferredPageSize() {
		return myInnerProvider.preferredPageSize();
	}

	@Override
	public Integer size() {
		return myInnerProvider.size();
	}

	@Override
	public List<IBaseResource> getAllResources() {
		var size = size();
		if (size == null) {
			return getResources(0, Integer.MAX_VALUE);
		} else {
			return getResources(0, size);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> getResourcesTyped(int theFromIndex, int theToIndex) {
		return myInnerProvider.getResources(theFromIndex, theToIndex).stream().map(x -> (T) x)
			.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllResourcesTyped() {
		return myInnerProvider.getAllResources().stream().map(x -> (T) x).collect(Collectors.toList());
	}

	/**
	 * Returns exactly one Resource. Throws an error if zero or more than one
	 * resource is found or if zero resources are found
	 *
	 * @return the Resource found.
	 */
	public T single() {
		List<T> resources = getResourcesTyped(0, 2);
		checkState(!resources.isEmpty(), "No resources found");
		checkState(resources.size() == 1, "More than one resource found");
		return resources.get(0);
	}

	/**
	 * Returns the first Resource found, or null if no resources are found.
	 *
	 * @return the first Resource found or null
	 */
	public T firstOrNull() {
		List<T> resources = getResourcesTyped(0, 1);

		if (resources.isEmpty()) {
			return null;
		}

		return resources.get(0);
	}
}
