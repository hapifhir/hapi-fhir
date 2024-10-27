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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service to build a FHIR Bundle from a request and a Bundle Provider
 */
public class ResponseBundleBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(ResponseBundleBuilder.class);

	private final boolean myIsOffsetModeHistory;

	public ResponseBundleBuilder(boolean theIsOffsetModeHistory) {
		myIsOffsetModeHistory = theIsOffsetModeHistory;
	}

	IBaseBundle buildResponseBundle(ResponseBundleRequest theResponseBundleRequest) {
		final ResponsePage responsePage = buildResponsePage(theResponseBundleRequest);

		removeNulls(responsePage.getResourceList());
		validateIds(responsePage.getResourceList());

		BundleLinks links = buildLinks(theResponseBundleRequest, responsePage);

		return buildBundle(theResponseBundleRequest, responsePage, links);
	}

	private static IBaseBundle buildBundle(
			ResponseBundleRequest theResponseBundleRequest, ResponsePage pageResponse, BundleLinks links) {
		final IRestfulServer<?> server = theResponseBundleRequest.server;
		final IVersionSpecificBundleFactory bundleFactory =
				server.getFhirContext().newBundleFactory();
		final IBundleProvider bundleProvider = theResponseBundleRequest.bundleProvider;

		bundleFactory.addRootPropertiesToBundle(
				bundleProvider.getUuid(), links, bundleProvider.size(), bundleProvider.getPublished());
		bundleFactory.addResourcesToBundle(
				new ArrayList<>(pageResponse.getResourceList()),
				theResponseBundleRequest.bundleType,
				links.serverBase,
				server.getBundleInclusionRule(),
				theResponseBundleRequest.includes);

		return (IBaseBundle) bundleFactory.getResourceBundle();
	}

	private ResponsePage buildResponsePage(ResponseBundleRequest theResponseBundleRequest) {
		final IRestfulServer<?> server = theResponseBundleRequest.server;
		final IBundleProvider bundleProvider = theResponseBundleRequest.bundleProvider;
		final RequestedPage requestedPage = theResponseBundleRequest.requestedPage;
		final List<IBaseResource> resourceList;
		final int pageSize;

		ResponsePage.ResponsePageBuilder responsePageBuilder = new ResponsePage.ResponsePageBuilder();

		int numToReturn;
		String searchId = null;

		if (requestedPage.offset != null || !server.canStoreSearchResults()) {
			pageSize = offsetCalculatePageSize(server, requestedPage, bundleProvider.size());
			numToReturn = pageSize;

			resourceList = offsetBuildResourceList(bundleProvider, requestedPage, numToReturn, responsePageBuilder);
			RestfulServerUtils.validateResourceListNotNull(resourceList);
		} else {
			pageSize = pagingCalculatePageSize(requestedPage, server.getPagingProvider());

			Integer size = bundleProvider.size();
			if (size == null) {
				numToReturn = pageSize;
			} else {
				numToReturn = Math.min(pageSize, size.intValue() - theResponseBundleRequest.offset);
			}

			resourceList =
					pagingBuildResourceList(theResponseBundleRequest, bundleProvider, numToReturn, responsePageBuilder);
			RestfulServerUtils.validateResourceListNotNull(resourceList);

			searchId = pagingBuildSearchId(theResponseBundleRequest, numToReturn, bundleProvider.size());
		}

		// We should leave the IBundleProvider to populate these values (specifically resourceList).
		// But since we haven't updated all such providers, we will
		// build it here (this is at best 'duplicating' work).
		responsePageBuilder
				.setSearchId(searchId)
				.setPageSize(pageSize)
				.setNumToReturn(numToReturn)
				.setBundleProvider(bundleProvider)
				.setResources(resourceList);

		return responsePageBuilder.build();
	}

	private static String pagingBuildSearchId(
			ResponseBundleRequest theResponseBundleRequest, int theNumToReturn, Integer theNumTotalResults) {
		final IPagingProvider pagingProvider = theResponseBundleRequest.server.getPagingProvider();
		String retval = null;

		if (theResponseBundleRequest.searchId != null) {
			retval = theResponseBundleRequest.searchId;
		} else {
			if (theNumTotalResults == null || theNumTotalResults > theNumToReturn) {
				retval = pagingProvider.storeResultList(
						theResponseBundleRequest.requestDetails, theResponseBundleRequest.bundleProvider);
				if (StringUtils.isBlank(retval)) {
					ourLog.info(
							"Found {} results but paging provider did not provide an ID to use for paging",
							theNumTotalResults);
					retval = null;
				}
			}
		}
		return retval;
	}

	private static List<IBaseResource> pagingBuildResourceList(
			ResponseBundleRequest theResponseBundleRequest,
			IBundleProvider theBundleProvider,
			int theNumToReturn,
			ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
		final List<IBaseResource> retval;
		if (theNumToReturn > 0 || theBundleProvider.getCurrentPageId() != null) {
			retval = theBundleProvider.getResources(
					theResponseBundleRequest.offset,
					theNumToReturn + theResponseBundleRequest.offset,
					theResponsePageBuilder);
		} else {
			retval = Collections.emptyList();
		}
		return retval;
	}

	private static int pagingCalculatePageSize(RequestedPage theRequestedPage, IPagingProvider thePagingProvider) {
		if (theRequestedPage.limit == null || theRequestedPage.limit.equals(0)) {
			return thePagingProvider.getDefaultPageSize();
		} else {
			return Math.min(thePagingProvider.getMaximumPageSize(), theRequestedPage.limit);
		}
	}

	private List<IBaseResource> offsetBuildResourceList(
			IBundleProvider theBundleProvider,
			RequestedPage theRequestedPage,
			int theNumToReturn,
			ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
		final List<IBaseResource> retval;
		if ((theRequestedPage.offset != null && !myIsOffsetModeHistory)
				|| theBundleProvider.getCurrentPageOffset() != null) {
			// When offset query is done theResult already contains correct amount (+ their includes etc.) so return
			// everything
			retval = theBundleProvider.getResources(0, Integer.MAX_VALUE, theResponsePageBuilder);
		} else if (theNumToReturn > 0) {
			retval = theBundleProvider.getResources(0, theNumToReturn, theResponsePageBuilder);
		} else {
			retval = Collections.emptyList();
		}
		return retval;
	}

	private static int offsetCalculatePageSize(
			IRestfulServer<?> server, RequestedPage theRequestedPage, Integer theNumTotalResults) {
		final int retval;
		if (theRequestedPage.limit != null) {
			retval = theRequestedPage.limit;
		} else {
			if (server.getDefaultPageSize() != null) {
				retval = server.getDefaultPageSize();
			} else {
				retval = theNumTotalResults != null ? theNumTotalResults : Integer.MAX_VALUE;
			}
		}
		return retval;
	}

	private static void validateIds(List<IBaseResource> theResourceList) {
		/*
		 * Make sure all returned resources have an ID (if not, this is a bug
		 * in the user server code)
		 */
		for (IBaseResource next : theResourceList) {
			if (next.getIdElement() == null || next.getIdElement().isEmpty()) {
				if (!(next instanceof IBaseOperationOutcome)) {
					throw new InternalErrorException(Msg.code(435) + "Server method returned resource of type["
							+ next.getClass().getSimpleName()
							+ "] with no ID specified (IResource#setId(IdDt) must be called)");
				}
			}
		}
	}

	private static void removeNulls(List<IBaseResource> resourceList) {
		/*
		 * Remove any null entries in the list - This generally shouldn't happen but can if
		 * data has been manually purged from the JPA database
		 */
		boolean hasNull = false;
		for (IBaseResource next : resourceList) {
			if (next == null) {
				hasNull = true;
				break;
			}
		}
		if (hasNull) {
			resourceList.removeIf(Objects::isNull);
		}
	}

	private BundleLinks buildLinks(ResponseBundleRequest theResponseBundleRequest, ResponsePage theResponsePage) {
		final IRestfulServer<?> server = theResponseBundleRequest.server;
		final RequestedPage pageRequest = theResponseBundleRequest.requestedPage;

		BundleLinks retval = new BundleLinks(
				theResponseBundleRequest.requestDetails.getFhirServerBase(),
				theResponseBundleRequest.includes,
				RestfulServerUtils.prettyPrintResponse(server, theResponseBundleRequest.requestDetails),
				theResponseBundleRequest.bundleType);

		// set self link
		retval.setSelf(theResponseBundleRequest.linkSelf);

		// determine if we are using offset / uncached pages
		theResponsePage.setUseOffsetPaging(pageRequest.offset != null
				|| (!server.canStoreSearchResults() && !isEverythingOperation(theResponseBundleRequest.requestDetails))
				|| myIsOffsetModeHistory);
		theResponsePage.setResponseBundleRequest(theResponseBundleRequest);
		theResponsePage.setRequestedPage(pageRequest);

		// generate our links
		theResponsePage.setNextPageIfNecessary(retval);
		theResponsePage.setPreviousPageIfNecessary(retval);

		return retval;
	}

	private boolean isEverythingOperation(RequestDetails theRequest) {
		return (theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE
						|| theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE)
				&& theRequest.getOperation() != null
				&& theRequest.getOperation().equals("$everything");
	}
}
