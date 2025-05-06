/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class pulls existing methods from the BaseResourceReturningMethodBinding class used for taking
 * the results of a BundleProvider and turning it into a Bundle.  It is intended to be used only by the
 * HapiFhirRepository.
 */
@SuppressWarnings("java:S107")
public class BundleProviderUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BundleProviderUtil.class);

	private BundleProviderUtil() {}

	private record OffsetLimitInfo(Integer offset, Integer limit) {
		int addOffsetAndLimit() {
			return offsetOrZero() + limitOrZero();
		}

		int maxOfDifference() {
			return Math.max(offsetOrZero() - limitOrZero(), 0);
		}

		private int offsetOrZero() {
			return defaultZeroIfNull(offset);
		}

		private int limitOrZero() {
			return defaultZeroIfNull(offset);
		}

		private int defaultZeroIfNull(Integer value) {
			return defaultIfNull(value, 0);
		}
	}

	private record InitialPagingResults(
			int pageSize,
			List<IBaseResource> resourceList,
			int numToReturn,
			String searchId,
			Integer numTotalResults) {}

	public static IBaseResource createBundleFromBundleProvider(
			IRestfulServer<?> theServer,
			RequestDetails theRequest,
			Integer theLimit,
			String theLinkSelf,
			Set<Include> theIncludes,
			IBundleProvider theResult,
			int theOffset,
			BundleTypeEnum theBundleType,
			String theSearchId) {

		final OffsetLimitInfo offsetLimitInfo = extractOffsetPageInfo(theResult, theRequest, theLimit);

		final InitialPagingResults initialPagingResults =
				extractInitialPagingResults(theServer, theRequest, theResult, theOffset, theSearchId, offsetLimitInfo);

		removeNullIfNeeded(initialPagingResults.resourceList);
		validateAllResourcesHaveId(initialPagingResults.resourceList);

		final BundleLinks links = buildLinks(
				theServer,
				theRequest,
				theLinkSelf,
				theIncludes,
				theResult,
				theOffset,
				theBundleType,
				offsetLimitInfo,
				initialPagingResults);

		return buildBundle(theServer, theIncludes, theResult, theBundleType, links, initialPagingResults.resourceList);
	}

	@Nonnull
	private static BundleLinks buildLinks(
			IRestfulServer<?> theServer,
			RequestDetails theRequest,
			String theLinkSelf,
			Set<Include> theIncludes,
			IBundleProvider theResult,
			int theOffset,
			BundleTypeEnum theBundleType,
			OffsetLimitInfo theOffsetLimitInfo,
			InitialPagingResults theInitialPagingResults) {

		BundleLinks links = new BundleLinks(
				theRequest.getFhirServerBase(),
				theIncludes,
				RestfulServerUtils.prettyPrintResponse(theServer, theRequest),
				theBundleType);
		links.setSelf(theLinkSelf);

		if (theResult.getCurrentPageOffset() != null) {

			if (isNotBlank(theResult.getNextPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(
						links,
						theRequest.getRequestPath(),
						theRequest.getTenantId(),
						theOffsetLimitInfo.addOffsetAndLimit(),
						theOffsetLimitInfo.limit,
						theRequest.getParameters()));
			}
			if (isNotBlank(theResult.getPreviousPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(
						links,
						theRequest.getRequestPath(),
						theRequest.getTenantId(),
						theOffsetLimitInfo.maxOfDifference(),
						theOffsetLimitInfo.limit,
						theRequest.getParameters()));
			}
		}

		if (theOffsetLimitInfo.offset != null
				|| (!theServer.canStoreSearchResults() && !isEverythingOperation(theRequest))) {
			handleOffsetPage(theServer, theRequest, theOffset, theOffsetLimitInfo, theInitialPagingResults, links);
		} else if (isNotBlank(theResult.getCurrentPageId())) {
			handleCurrentPage(theRequest, theResult, theInitialPagingResults, links);
		} else if (theInitialPagingResults.searchId != null && !theInitialPagingResults.resourceList.isEmpty()) {
			handleSearchId(theRequest, theOffset, theInitialPagingResults, links);
		}
		return links;
	}

	private static void handleSearchId(
			RequestDetails theRequest,
			int theOffset,
			InitialPagingResults theInitialPagingResults,
			BundleLinks theLinks) {
		if (theInitialPagingResults.numTotalResults == null
				|| theOffset + theInitialPagingResults.numToReturn < theInitialPagingResults.numTotalResults) {
			theLinks.setNext((RestfulServerUtils.createPagingLink(
					theLinks,
					theRequest,
					theInitialPagingResults.searchId,
					theOffset + theInitialPagingResults.numToReturn,
					theInitialPagingResults.numToReturn,
					theRequest.getParameters())));
		}
		if (theOffset > 0) {
			int start = Math.max(0, theOffset - theInitialPagingResults.pageSize);
			theLinks.setPrev(RestfulServerUtils.createPagingLink(
					theLinks,
					theRequest,
					theInitialPagingResults.searchId,
					start,
					theInitialPagingResults.pageSize,
					theRequest.getParameters()));
		}
	}

	private static void handleCurrentPage(
			RequestDetails theRequest,
			IBundleProvider theResult,
			InitialPagingResults theInitialPagingResults,
			BundleLinks theLinks) {
		String searchIdToUse;
		// We're doing named pages
		searchIdToUse = theResult.getUuid();
		if (isNotBlank(theResult.getNextPageId())) {
			theLinks.setNext(RestfulServerUtils.createPagingLink(
					theLinks, theRequest, searchIdToUse, theResult.getNextPageId(), theRequest.getParameters()));
		}
		if (isNotBlank(theResult.getPreviousPageId())) {
			theLinks.setPrev(RestfulServerUtils.createPagingLink(
					theLinks,
					theRequest,
					theInitialPagingResults.searchId,
					theResult.getPreviousPageId(),
					theRequest.getParameters()));
		}
	}

	private static void handleOffsetPage(
			IRestfulServer<?> theServer,
			RequestDetails theRequest,
			int theOffset,
			OffsetLimitInfo theOffsetLimitInfo,
			InitialPagingResults theInitialPagingResults,
			BundleLinks theLinks) {
		// Paging without caching
		// We're doing offset pages
		int requestedToReturn = theInitialPagingResults.numToReturn;
		if (theServer.getPagingProvider() == null && theOffsetLimitInfo.offset != null) {
			// There is no paging provider at all, so assume we're querying up to all the results we
			// need every time
			requestedToReturn += theOffsetLimitInfo.offset;
		}
		if ((theInitialPagingResults.numTotalResults == null
						|| requestedToReturn < theInitialPagingResults.numTotalResults)
				&& !theInitialPagingResults.resourceList.isEmpty()) {
			theLinks.setNext(RestfulServerUtils.createOffsetPagingLink(
					theLinks,
					theRequest.getRequestPath(),
					theRequest.getTenantId(),
					defaultIfNull(theOffsetLimitInfo.offset, 0) + theInitialPagingResults.numToReturn,
					theInitialPagingResults.numToReturn,
					theRequest.getParameters()));
		}

		if (theOffsetLimitInfo.offset != null && theOffsetLimitInfo.offset > 0) {
			int start = Math.max(0, theOffset - theInitialPagingResults.pageSize);
			theLinks.setPrev(RestfulServerUtils.createOffsetPagingLink(
					theLinks,
					theRequest.getRequestPath(),
					theRequest.getTenantId(),
					start,
					theInitialPagingResults.pageSize,
					theRequest.getParameters()));
		}
	}

	private static OffsetLimitInfo extractOffsetPageInfo(
			IBundleProvider theResult, RequestDetails theRequest, Integer theLimit) {
		Integer offsetToUse;
		Integer limitToUse = theLimit;
		if (theResult.getCurrentPageOffset() != null) {
			offsetToUse = theResult.getCurrentPageOffset();
			limitToUse = theResult.getCurrentPageSize();
			Validate.notNull(
					limitToUse, "IBundleProvider returned a non-null offset, but did not return a non-null page size");
		} else {
			offsetToUse = RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_OFFSET);
		}
		return new OffsetLimitInfo(offsetToUse, limitToUse);
	}

	private static InitialPagingResults extractInitialPagingResults(
			IRestfulServer<?> theServer,
			RequestDetails theRequest,
			IBundleProvider theResult,
			int theOffset,
			String theSearchId,
			OffsetLimitInfo theOffsetLimitInfo) {

		if (theOffsetLimitInfo.offset != null || !theServer.canStoreSearchResults()) {
			return handleOffset(theServer, theResult, theOffsetLimitInfo);
		}

		return handleNonOffset(theServer, theRequest, theResult, theOffset, theSearchId, theOffsetLimitInfo);
	}

	@Nonnull
	private static InitialPagingResults handleNonOffset(
			IRestfulServer<?> theServer,
			RequestDetails theRequest,
			IBundleProvider theResult,
			int theOffset,
			String theSearchId,
			OffsetLimitInfo theOffsetLimitInfo) {

		Integer numTotalResults = theResult.size();
		List<IBaseResource> resourceList;
		int numToReturn;
		final int pageSize;
		IPagingProvider pagingProvider = theServer.getPagingProvider();

		if (theOffsetLimitInfo.limit == null || theOffsetLimitInfo.limit.equals(0)) {
			pageSize = pagingProvider.getDefaultPageSize();
		} else {
			pageSize = Math.min(pagingProvider.getMaximumPageSize(), theOffsetLimitInfo.limit);
		}
		numToReturn = pageSize;

		if (numTotalResults != null) {
			numToReturn = Math.min(numToReturn, numTotalResults - theOffset);
		}

		if (numToReturn > 0 || theResult.getCurrentPageId() != null) {
			resourceList = theResult.getResources(theOffset, numToReturn + theOffset);
		} else {
			resourceList = Collections.emptyList();
		}
		RestfulServerUtils.validateResourceListNotNull(resourceList);

		if (numTotalResults == null) {
			numTotalResults = theResult.size();
		}

		final String searchIdToUse =
				computeSearchId(theRequest, theResult, theSearchId, numTotalResults, numToReturn, pagingProvider);

		return new InitialPagingResults(pageSize, resourceList, numToReturn, searchIdToUse, numTotalResults);
	}

	@Nullable
	private static String computeSearchId(
			RequestDetails theRequest,
			IBundleProvider theResult,
			String theSearchId,
			Integer theNumTotalResults,
			int theNumToReturn,
			IPagingProvider thePagingProvider) {
		String searchIdToUse = null;
		if (theSearchId != null) {
			searchIdToUse = theSearchId;
		} else {
			if (theNumTotalResults == null || theNumTotalResults > theNumToReturn) {
				searchIdToUse = thePagingProvider.storeResultList(theRequest, theResult);
				if (isBlank(searchIdToUse)) {
					ourLog.info(
							"Found {} results but paging provider did not provide an ID to use for paging",
							theNumTotalResults);
					searchIdToUse = null;
				}
			}
		}
		return searchIdToUse;
	}

	@Nonnull
	private static InitialPagingResults handleOffset(
			IRestfulServer<?> theServer, IBundleProvider theResult, OffsetLimitInfo theOffsetLimitInfo) {
		String searchIdToUse = null;
		final int pageSize;
		int numToReturn;
		Integer numTotalResults = theResult.size();

		List<IBaseResource> resourceList;
		if (theOffsetLimitInfo.limit != null) {
			pageSize = theOffsetLimitInfo.limit;
		} else {
			if (theServer.getDefaultPageSize() != null) {
				pageSize = theServer.getDefaultPageSize();
			} else {
				pageSize = numTotalResults != null ? numTotalResults : Integer.MAX_VALUE;
			}
		}
		numToReturn = pageSize;

		if (theOffsetLimitInfo.offset != null || theResult.getCurrentPageOffset() != null) {
			// When offset query is done result already contains correct amount (+ ir includes
			// etc.) so return everything
			resourceList = theResult.getResources(0, Integer.MAX_VALUE);
		} else if (numToReturn > 0) {
			resourceList = theResult.getResources(0, numToReturn);
		} else {
			resourceList = Collections.emptyList();
		}
		RestfulServerUtils.validateResourceListNotNull(resourceList);

		return new InitialPagingResults(pageSize, resourceList, numToReturn, searchIdToUse, numTotalResults);
	}

	private static IBaseResource buildBundle(
			IRestfulServer<?> theServer,
			Set<Include> theIncludes,
			IBundleProvider theResult,
			BundleTypeEnum theBundleType,
			BundleLinks theLinks,
			List<IBaseResource> theResourceList) {
		IVersionSpecificBundleFactory bundleFactory = theServer.getFhirContext().newBundleFactory();

		bundleFactory.addRootPropertiesToBundle(
				theResult.getUuid(), theLinks, theResult.size(), theResult.getPublished());
		bundleFactory.addResourcesToBundle(
				new ArrayList<>(theResourceList),
				theBundleType,
				theLinks.serverBase,
				theServer.getBundleInclusionRule(),
				theIncludes);

		return bundleFactory.getResourceBundle();
	}

	private static void removeNullIfNeeded(List<IBaseResource> theResourceList) {
		/*
		 * Remove any null entries in the list - This generally shouldn't happen but can if data has
		 * been manually purged from the JPA database
		 */
		boolean hasNull = false;
		for (IBaseResource next : theResourceList) {
			if (next == null) {
				hasNull = true;
				break;
			}
		}
		if (hasNull) {
			theResourceList.removeIf(Objects::isNull);
		}
	}

	private static void validateAllResourcesHaveId(List<IBaseResource> theResourceList) {
		/*
		 * Make sure all returned resources have an ID (if not, this is a bug in the user server code)
		 */
		for (IBaseResource next : theResourceList) {
			if ((next.getIdElement() == null || next.getIdElement().isEmpty())
					&& !(next instanceof IBaseOperationOutcome)) {
				throw new InternalErrorException(Msg.code(2637)
						+ String.format(
								"Server method returned resource of type[%s] with no ID specified (IResource#setId(IdDt) must be called)",
								next.getIdElement()));
			}
		}
	}

	private static boolean isEverythingOperation(RequestDetails theRequest) {
		return (theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE
						|| theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE)
				&& theRequest.getOperation() != null
				&& theRequest.getOperation().equals("$everything");
	}
}
