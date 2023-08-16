/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.method;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

/**
 * This is an intermediate record object that holds all the fields required to make the final bundle that will be returned to the client.
 */
public class ResponsePage {
	/**
	 * The id of the search used to page through search results
	 */
	public final String searchId;
	/**
	 * The list of resources that will be used to create the bundle
	 */
	public final List<IBaseResource> resourceList;
	/**
	 * The total number of results that matched the search
	 */
	public final Integer numTotalResults;
	/**
	 * The number of resources that should be returned in each page
	 */
	public final int pageSize;
	/**
	 * The number of resources that should be returned in the bundle.  Can be smaller than pageSize when the bundleProvider
	 * has fewer results than the page size.
	 */
	public final int numToReturn;

	/**
	 * The count of resources included from the _include filter.
	 * These _include resources are otherwise included in the resourceList.
	 */
	private final int includedResourceCount;

	ResponsePage(
		String theSearchId,
		List<IBaseResource> theResourceList,
		int thePageSize,
		int theNumToReturn,
		Integer theNumTotalResults,
		int theIncludedResourceCount
	) {
		searchId = theSearchId;
		resourceList = theResourceList;
		pageSize = thePageSize;
		numToReturn = theNumToReturn;
		numTotalResults = theNumTotalResults;
		includedResourceCount = theIncludedResourceCount;
	}

	public int size() {
		return resourceList.size();
	}

	/**
	 * A builder for constructing ResponsePage objects.
	 */
	public static class ResponsePageBuilder {

		private String mySearchId;
		private List<IBaseResource> myResources;
		private Integer myNumTotalResults;
		private int myPageSize;
		private int myNumToReturn;
		private int myIncludedResourceCount;

		public ResponsePageBuilder setIncludedResourceCount(int theIncludedResourceCount) {
			myIncludedResourceCount = theIncludedResourceCount;
			return this;
		}

		public ResponsePageBuilder setNumToReturn(int theNumToReturn) {
			myNumToReturn = theNumToReturn;
			return this;
		}

		public ResponsePageBuilder setPageSize(int thePageSize) {
			myPageSize = thePageSize;
			return this;
		}

		public ResponsePageBuilder setNumTotalResults(Integer theNumTotalResults) {
			myNumTotalResults = theNumTotalResults;
			return this;
		}

		public ResponsePageBuilder setResources(List<IBaseResource> theResources) {
			myResources = theResources;
			return this;
		}

		public ResponsePageBuilder setSearchId(String theSearchId) {
			mySearchId = theSearchId;
			return this;
		}

		public ResponsePage build() {
			return new ResponsePage(
				mySearchId, // search id
				myResources, // resource list
				myPageSize, // page size
				myNumToReturn, // num to return
				myNumTotalResults, // total results
				myIncludedResourceCount // included count
			);
		}
	}
}
