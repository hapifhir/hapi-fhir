/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This is an intermediate record object that holds all the fields required to make the final bundle that will be returned to the client.
 */
public class ResponsePage {
	private static final Logger ourLog = LoggerFactory.getLogger(ResponsePage.class);

	/**
	 * The id of the search used to page through search results
	 */
	private final String mySearchId;
	/**
	 * The list of resources that will be used to create the bundle
	 */
	private final List<IBaseResource> myResourceList;
	/**
	 * The total number of results that matched the search
	 */
	private final Integer myNumTotalResults;
	/**
	 * The number of resources that should be returned in each page
	 */
	private final int myPageSize;
	/**
	 * The number of resources that should be returned in the bundle.
	 * Can be smaller than pageSize when the bundleProvider
	 * has fewer results than the page size.
	 */
	private final int myNumToReturn;

	/**
	 * The count of resources included from the _include filter.
	 * These _include resources are otherwise included in the resourceList.
	 */
	private final int myIncludedResourceCount;
	/**
	 * This is the count of resources that have been omitted from results
	 * (typically because of consent interceptors).
	 * We track these because they shouldn't change paging results,
	 * even though it will change number of resources returned.
	 */
	private final int myOmittedResourceCount;
	/**
	 * This is the total count of requested resources
	 * (ie, non-omitted, non-_include'd resource count).
	 * We typically fetch (for offset queries) 1 more than
	 * we need so we know if there is an additional page
	 * to fetch.
	 * But this is determined by the implementers of
	 * IBundleProvider.
	 */
	private final int myTotalRequestedResourcesFetched;

	/**
	 * The bundle provider.
	 */
	private final IBundleProvider myBundleProvider;

	// Properties below here are set for calculation of pages;
	// not part of the response pages in and of themselves

	/**
	 * The response bundle request object
	 */
	private ResponseBundleRequest myResponseBundleRequest;

	/**
	 * Whether or not this page uses (non-cached) offset paging
	 */
	private boolean myIsUsingOffsetPages = false;

	/**
	 * The requested page object (should not be null for proper calculations)
	 */
	private RequestedPage myRequestedPage;

	/**
	 * The paging style being used.
	 * This is determined by a number of conditions,
	 * including what the bundleprovider provides.
	 */
	private PagingStyle myPagingStyle;

	ResponsePage(
			String theSearchId,
			List<IBaseResource> theResourceList,
			int thePageSize,
			int theNumToReturn,
			int theIncludedResourceCount,
			int theOmittedResourceCount,
			int theTotalRequestedResourcesFetched,
			IBundleProvider theBundleProvider) {
		mySearchId = theSearchId;
		myResourceList = theResourceList;
		myPageSize = thePageSize;
		myNumToReturn = theNumToReturn;
		myIncludedResourceCount = theIncludedResourceCount;
		myOmittedResourceCount = theOmittedResourceCount;
		myTotalRequestedResourcesFetched = theTotalRequestedResourcesFetched;
		myBundleProvider = theBundleProvider;

		myNumTotalResults = myBundleProvider.size();
	}

	public int size() {
		return myResourceList.size();
	}

	public List<IBaseResource> getResourceList() {
		return myResourceList;
	}

	private boolean isBundleProviderOffsetPaging() {
		if (myBundleProvider != null) {
			if (myBundleProvider.getCurrentPageOffset() != null) {
				// it's not enough that currentpageoffset is not null
				// (sometimes it's 0, even if it's not a currentpageoffset search)
				// so we have to make sure either next or prev links are not null
				return (StringUtils.isNotBlank(myBundleProvider.getNextPageId())
						|| StringUtils.isNotBlank(myBundleProvider.getPreviousPageId()));
			}
		}

		return false;
	}

	private void determinePagingStyle() {
		if (myPagingStyle != null) {
			// already assigned
			return;
		}

		if (isBundleProviderOffsetPaging()) {
			myPagingStyle = PagingStyle.BUNDLE_PROVIDER_OFFSETS;
		} else if (myIsUsingOffsetPages) {
			myPagingStyle = PagingStyle.NONCACHED_OFFSET;
		} else if (myBundleProvider != null && StringUtils.isNotBlank(myBundleProvider.getCurrentPageId())) {
			myPagingStyle = PagingStyle.BUNDLE_PROVIDER_PAGE_IDS;
		} else if (StringUtils.isNotBlank(mySearchId)) {
			myPagingStyle = PagingStyle.SAVED_SEARCH;
		} else {
			myPagingStyle = PagingStyle.NONE;
			// only end up here if no paging is desired
			ourLog.debug(
					"No accurate paging will be generated."
							+ " If accurate paging is desired, ResponsePageBuilder must be provided with additioanl information.");
		}
	}

	public void setRequestedPage(RequestedPage theRequestedPage) {
		myRequestedPage = theRequestedPage;
	}

	public IBundleProvider getBundleProvider() {
		return myBundleProvider;
	}

	public void setUseOffsetPaging(boolean theIsUsingOffsetPaging) {
		myIsUsingOffsetPages = theIsUsingOffsetPaging;
	}

	public void setResponseBundleRequest(ResponseBundleRequest theRequest) {
		myResponseBundleRequest = theRequest;
	}

	private boolean hasNextPage() {
		determinePagingStyle();
		switch (myPagingStyle) {
			case BUNDLE_PROVIDER_OFFSETS:
			case BUNDLE_PROVIDER_PAGE_IDS:
				return StringUtils.isNotBlank(myBundleProvider.getNextPageId());
			case NONCACHED_OFFSET:
				if (myNumTotalResults == null) {
					if (hasNextPageWithoutKnowingTotal()) {
						return true;
					}
				} else if (myNumTotalResults > myNumToReturn + ObjectUtils.defaultIfNull(myRequestedPage.offset, 0)) {
					return true;
				}
				break;
			case SAVED_SEARCH:
				if (myNumTotalResults == null) {
					if (hasNextPageWithoutKnowingTotal()) {
						return true;
					}
				} else if (myResponseBundleRequest.offset + myNumToReturn < myNumTotalResults) {
					return true;
				}
				break;
		}

		// fallthrough
		return false;
	}

	/**
	 * If myNumTotalResults is null, it typically means we don't
	 * have an accurate total.
	 *
	 * Ie, we're in the middle of a set of pages (of non-named page results),
	 * and _total=accurate was not passed.
	 *
	 * This typically always means that a
	 * 'next' link definitely exists.
	 *
	 * But there are cases where this might not be true:
	 * * the last page of a search that also has an _include
	 * 	query parameter where the total of resources + _include'd
	 * 	resources is > the page size expected to be returned.
	 * * the last page of a search that returns the exact number
	 * 	of resources requested
	 *
	 * In these case, we must check to see if the returned
	 * number of *requested* resources.
	 * If our bundleprovider has fetched > requested,
	 * we'll know that there are more resources already.
	 * But if it hasn't, we'll have to check pagesize compared to
	 * _include'd count, omitted count, and resource count.
	 */
	private boolean hasNextPageWithoutKnowingTotal() {
		// if we have totalRequestedResource count, and it's not equal to pagesize,
		// then we can use this, alone, to determine if there are more pages
		if (myTotalRequestedResourcesFetched >= 0) {
			if (myPageSize < myTotalRequestedResourcesFetched) {
				return true;
			}
		} else {
			// otherwise we'll try and determine if there are next links based on the following
			// calculation:
			// resourceList.size - included resources + omitted resources == pagesize
			// -> we (most likely) have more resources
			if (myPageSize == myResourceList.size() - myIncludedResourceCount + myOmittedResourceCount) {
				ourLog.warn(
						"Returning a next page based on calculated resource count."
								+ " This could be inaccurate if the exact number of resources were fetched is equal to the pagesize requested. "
								+ " Consider setting ResponseBundleBuilder.setTotalResourcesFetchedRequest after fetching resources.");
				return true;
			}
		}
		return false;
	}

	public void setNextPageIfNecessary(BundleLinks theLinks) {
		if (hasNextPage()) {
			String next;
			switch (myPagingStyle) {
				case BUNDLE_PROVIDER_OFFSETS:
					next = RestfulServerUtils.createOffsetPagingLink(
							theLinks,
							myResponseBundleRequest.requestDetails.getRequestPath(),
							myResponseBundleRequest.requestDetails.getTenantId(),
							myRequestedPage.offset + myRequestedPage.limit,
							myRequestedPage.limit,
							myResponseBundleRequest.getRequestParameters());
					break;
				case NONCACHED_OFFSET:
					next = RestfulServerUtils.createOffsetPagingLink(
							theLinks,
							myResponseBundleRequest.requestDetails.getRequestPath(),
							myResponseBundleRequest.requestDetails.getTenantId(),
							ObjectUtils.defaultIfNull(myRequestedPage.offset, 0) + myNumToReturn,
							myNumToReturn,
							myResponseBundleRequest.getRequestParameters());
					break;
				case BUNDLE_PROVIDER_PAGE_IDS:
					next = RestfulServerUtils.createPagingLink(
							theLinks,
							myResponseBundleRequest.requestDetails,
							myBundleProvider.getUuid(),
							myBundleProvider.getNextPageId(),
							myResponseBundleRequest.getRequestParameters());
					break;
				case SAVED_SEARCH:
					next = RestfulServerUtils.createPagingLink(
							theLinks,
							myResponseBundleRequest.requestDetails,
							mySearchId,
							myResponseBundleRequest.offset + myNumToReturn,
							myNumToReturn,
							myResponseBundleRequest.getRequestParameters());
					break;
				default:
					next = null;
					break;
			}

			if (StringUtils.isNotBlank(next)) {
				theLinks.setNext(next);
			}
		}
	}

	private boolean hasPreviousPage() {
		determinePagingStyle();
		switch (myPagingStyle) {
			case BUNDLE_PROVIDER_OFFSETS:
			case BUNDLE_PROVIDER_PAGE_IDS:
				return StringUtils.isNotBlank(myBundleProvider.getPreviousPageId());
			case NONCACHED_OFFSET:
				if (myRequestedPage != null && myRequestedPage.offset != null && myRequestedPage.offset > 0) {
					return true;
				}
				break;
			case SAVED_SEARCH:
				return myResponseBundleRequest.offset > 0;
		}

		// fallthrough
		return false;
	}

	public void setPreviousPageIfNecessary(BundleLinks theLinks) {
		if (hasPreviousPage()) {
			String prev;
			switch (myPagingStyle) {
				case BUNDLE_PROVIDER_OFFSETS:
					prev = RestfulServerUtils.createOffsetPagingLink(
							theLinks,
							myResponseBundleRequest.requestDetails.getRequestPath(),
							myResponseBundleRequest.requestDetails.getTenantId(),
							Math.max(ObjectUtils.defaultIfNull(myRequestedPage.offset, 0) - myRequestedPage.limit, 0),
							myRequestedPage.limit,
							myResponseBundleRequest.getRequestParameters());
					break;
				case NONCACHED_OFFSET:
					{
						int start = Math.max(0, ObjectUtils.defaultIfNull(myRequestedPage.offset, 0) - myPageSize);
						prev = RestfulServerUtils.createOffsetPagingLink(
								theLinks,
								myResponseBundleRequest.requestDetails.getRequestPath(),
								myResponseBundleRequest.requestDetails.getTenantId(),
								start,
								myPageSize,
								myResponseBundleRequest.getRequestParameters());
					}
					break;
				case BUNDLE_PROVIDER_PAGE_IDS:
					prev = RestfulServerUtils.createPagingLink(
							theLinks,
							myResponseBundleRequest.requestDetails,
							myBundleProvider.getUuid(),
							myBundleProvider.getPreviousPageId(),
							myResponseBundleRequest.getRequestParameters());
					break;
				case SAVED_SEARCH:
					{
						int start = Math.max(0, myResponseBundleRequest.offset - myPageSize);
						prev = RestfulServerUtils.createPagingLink(
								theLinks,
								myResponseBundleRequest.requestDetails,
								mySearchId,
								start,
								myPageSize,
								myResponseBundleRequest.getRequestParameters());
					}
					break;
				default:
					prev = null;
			}

			if (StringUtils.isNotBlank(prev)) {
				theLinks.setPrev(prev);
			}
		}
	}

	/**
	 * A builder for constructing ResponsePage objects.
	 */
	public static class ResponsePageBuilder {

		private String mySearchId;
		private List<IBaseResource> myResources;
		private int myPageSize;
		private int myNumToReturn;
		private int myIncludedResourceCount;
		private int myOmittedResourceCount;
		private IBundleProvider myBundleProvider;
		private int myTotalRequestedResourcesFetched = -1;

		public ResponsePageBuilder setOmittedResourceCount(int theOmittedResourceCount) {
			myOmittedResourceCount = theOmittedResourceCount;
			return this;
		}

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

		public ResponsePageBuilder setBundleProvider(IBundleProvider theBundleProvider) {
			myBundleProvider = theBundleProvider;
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

		public ResponsePageBuilder setTotalRequestedResourcesFetched(int theTotalRequestedResourcesFetched) {
			myTotalRequestedResourcesFetched = theTotalRequestedResourcesFetched;
			return this;
		}

		/**
		 * Combine this builder with a second buider.
		 * Useful if a second page is requested, but you do not wish to
		 * overwrite the current values.
		 *
		 * Will not replace searchId, nor IBundleProvider (which should be
		 * the exact same for any subsequent searches anyways).
		 *
		 * Will also not copy pageSize nor numToReturn, as these should be
		 * the same for any single search result set.
		 *
		 * @param theSecondBuilder - a second builder (cannot be this one)
		 */
		public void combineWith(ResponsePageBuilder theSecondBuilder) {
			assert theSecondBuilder != this; // don't want to combine with itself

			if (myTotalRequestedResourcesFetched != -1 && theSecondBuilder.myTotalRequestedResourcesFetched != -1) {
				myTotalRequestedResourcesFetched += theSecondBuilder.myTotalRequestedResourcesFetched;
			}

			// primitives can always be added
			myIncludedResourceCount += theSecondBuilder.myIncludedResourceCount;
			myOmittedResourceCount += theSecondBuilder.myOmittedResourceCount;
		}

		public ResponsePage build() {
			return new ResponsePage(
					mySearchId, // search id
					myResources, // resource list
					myPageSize, // page size
					myNumToReturn, // num to return
					myIncludedResourceCount, // included count
					myOmittedResourceCount, // omitted resources
					myTotalRequestedResourcesFetched, // total count of requested resources
					myBundleProvider // the bundle provider
					);
		}
	}

	/**
	 * First we determine what kind of paging we use:
	 * * Bundle Provider Offsets - the bundle provider has offset counts that it uses
	 * 							to determine the page. For legacy reasons, it's not enough
	 * 							that the bundle provider has a currentOffsetPage. Sometimes
	 * 							this value is provided (often as a 0), but no nextPageId nor previousPageId
	 * 							is available. Typically this is the case in UnitTests.
	 * * non-cached offsets - if the server is not storing the search results (and it's not
	 * 							an everything operator) OR the Requested Page has an initial offset
	 * 							OR it is explicitly set to use non-cached offset
	 * 							(ResponseBundleBuilder.myIsOffsetModeHistory)
	 * * Bundle Provider Page Ids - the bundle provider knows the page ids and will
	 * 							provide them. bundle provider will have a currentPageId
	 * * Saved Search			- the server has a saved search object with an id that it
	 * 							uses to page through results.
	 */
	private enum PagingStyle {
		/**
		 * Paging is done by offsets; pages are not cached
		 */
		NONCACHED_OFFSET,
		/**
		 * Paging is done by offsets, but
		 * the bundle provider provides the offsets
		 */
		BUNDLE_PROVIDER_OFFSETS,
		/**
		 * Paging is done by page ids,
		 * but bundle provider provides the page ids
		 */
		BUNDLE_PROVIDER_PAGE_IDS,
		/**
		 * The server has a saved search object with an id
		 * that is used to page through results.
		 */
		SAVED_SEARCH,
		/**
		 * No paging is done at all.
		 * No previous nor next links will be available, even if previous or next
		 * links exist.
		 * If paging is required, a different paging method must be specified.
		 */
		NONE;
	}
}
