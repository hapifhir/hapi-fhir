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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Iterator;
import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * This class leverages IBundleProvider Iterable to provide an iterator for processing bundle search results into manageable paginated chunks. This helped to avoid loading large result sets into lists
 **/
@NotThreadSafe
public class BundleIterable implements Iterable<IBaseResource> {

	private final IBundleProvider sourceBundleProvider;
	private final RequestDetails requestDetails;

	public BundleIterable(RequestDetails requestDetails, IBundleProvider bundleProvider) {
		this.sourceBundleProvider = bundleProvider;
		this.requestDetails = requestDetails;
	}

	@Override
	public Iterator<IBaseResource> iterator() {
		return new BundleIterator(this.requestDetails, this.sourceBundleProvider);
	}

	static class BundleIterator implements Iterator<IBaseResource> {

		private IBundleProvider bundleProvider;

		private int offset = 0;
		private int increment = 50;
		private List<IBaseResource> currentResourceList;

		private final RequestDetails requestDetails;

		private int currentResourceListIndex = 0;

		public BundleIterator(RequestDetails requestDetails, IBundleProvider bundleProvider) {
			this.bundleProvider = bundleProvider;
			this.requestDetails = requestDetails;
			initChunk();
		}

		private void initChunk() {
			this.currentResourceList = this.bundleProvider.getResources(offset, increment + offset);
			// next offset created
			offset += increment;
			// restart counter on new chunk
			currentResourceListIndex = 0;
		}

		private void loadNextChunk() {
			initChunk();
		}

		@Override
		public boolean hasNext() {
			// We still have things in the current chunk to return
			if (this.currentResourceListIndex < this.currentResourceList.size()) {
				return true;
			} else if (this.currentResourceList.size() == 0) {
				// no more resources!
				return false;
			}

			// We need our next chunk
			this.loadNextChunk();
			return this.hasNext();
		}

		@Override
		public IBaseResource next() {
			assert this.currentResourceListIndex < this.currentResourceList.size();

			var result = this.currentResourceList.get(this.currentResourceListIndex);
			this.currentResourceListIndex++;
			return result;
		}
	}
}
