package ca.uhn.fhir.cr.repo;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.method.BaseResourceReturningMethodBinding;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class pulls existing methods from the BaseResourceReturningMethodBinding class used for taking
 * the results of a BundleProvider and turning it into a Bundle.  It is intended to be used only by the
 * HapiFhirRepository.
 */
public class BundleProviderUtil {
	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(BaseResourceReturningMethodBinding.class);

	public static IBaseResource createBundleFromBundleProvider(
			IRestfulServer<?> theServer,
			RequestDetails theRequest,
			Integer theLimit,
			String theLinkSelf,
			Set<Include> theIncludes,
			IBundleProvider theResult,
			int theOffset,
			BundleTypeEnum theBundleType,
			EncodingEnum theLinkEncoding,
			String theSearchId) {
		IVersionSpecificBundleFactory bundleFactory = theServer.getFhirContext().newBundleFactory();
		final Integer offset;
		Integer limit = theLimit;

		if (theResult.getCurrentPageOffset() != null) {
			offset = theResult.getCurrentPageOffset();
			limit = theResult.getCurrentPageSize();
			Validate.notNull(
					limit, "IBundleProvider returned a non-null offset, but did not return a non-null page size");
		} else {
			offset = RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_OFFSET);
		}

		int numToReturn;
		String searchId = null;
		List<IBaseResource> resourceList;
		Integer numTotalResults = theResult.size();

		int pageSize;
		if (offset != null || !theServer.canStoreSearchResults()) {
			if (limit != null) {
				pageSize = limit;
			} else {
				if (theServer.getDefaultPageSize() != null) {
					pageSize = theServer.getDefaultPageSize();
				} else {
					pageSize = numTotalResults != null ? numTotalResults : Integer.MAX_VALUE;
				}
			}
			numToReturn = pageSize;

			if (offset != null || theResult.getCurrentPageOffset() != null) {
				// When offset query is done theResult already contains correct amount (+ their includes
				// etc.) so return everything
				resourceList = theResult.getResources(0, Integer.MAX_VALUE);
			} else if (numToReturn > 0) {
				resourceList = theResult.getResources(0, numToReturn);
			} else {
				resourceList = Collections.emptyList();
			}
			RestfulServerUtils.validateResourceListNotNull(resourceList);

		} else {
			IPagingProvider pagingProvider = theServer.getPagingProvider();
			if (limit == null || ((Integer) limit).equals(0)) {
				pageSize = pagingProvider.getDefaultPageSize();
			} else {
				pageSize = Math.min(pagingProvider.getMaximumPageSize(), limit);
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

			if (theSearchId != null) {
				searchId = theSearchId;
			} else {
				if (numTotalResults == null || numTotalResults > numToReturn) {
					searchId = pagingProvider.storeResultList(theRequest, theResult);
					if (isBlank(searchId)) {
						ourLog.info(
								"Found {} results but paging provider did not provide an ID to use for paging",
								numTotalResults);
						searchId = null;
					}
				}
			}
		}

		/*
		 * Remove any null entries in the list - This generally shouldn't happen but can if data has
		 * been manually purged from the JPA database
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

		/*
		 * Make sure all returned resources have an ID (if not, this is a bug in the user server code)
		 */
		for (IBaseResource next : resourceList) {
			if (next.getIdElement() == null || next.getIdElement().isEmpty()) {
				if (!(next instanceof IBaseOperationOutcome)) {
					throw new InternalErrorException(Msg.code(2311)
							+ "Server method returned resource of type["
							+ next.getClass().getSimpleName()
							+ "] with no ID specified (IResource#setId(IdDt) must be called)");
				}
			}
		}

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
						offset + limit,
						limit,
						theRequest.getParameters()));
			}
			if (isNotBlank(theResult.getPreviousPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(
						links,
						theRequest.getRequestPath(),
						theRequest.getTenantId(),
						Math.max(offset - limit, 0),
						limit,
						theRequest.getParameters()));
			}
		}

		if (offset != null || (!theServer.canStoreSearchResults() && !isEverythingOperation(theRequest))) {
			// Paging without caching
			// We're doing offset pages
			int requestedToReturn = numToReturn;
			if (theServer.getPagingProvider() == null && offset != null) {
				// There is no paging provider at all, so assume we're querying up to all the results we
				// need every time
				requestedToReturn += offset;
			}
			if (numTotalResults == null || requestedToReturn < numTotalResults) {
				if (!resourceList.isEmpty()) {
					links.setNext(RestfulServerUtils.createOffsetPagingLink(
							links,
							theRequest.getRequestPath(),
							theRequest.getTenantId(),
							defaultIfNull(offset, 0) + numToReturn,
							numToReturn,
							theRequest.getParameters()));
				}
			}
			if (offset != null && offset > 0) {
				int start = Math.max(0, theOffset - pageSize);
				links.setPrev(RestfulServerUtils.createOffsetPagingLink(
						links,
						theRequest.getRequestPath(),
						theRequest.getTenantId(),
						start,
						pageSize,
						theRequest.getParameters()));
			}
		} else if (isNotBlank(theResult.getCurrentPageId())) {
			// We're doing named pages
			searchId = theResult.getUuid();
			if (isNotBlank(theResult.getNextPageId())) {
				links.setNext(RestfulServerUtils.createPagingLink(
						links, theRequest, searchId, theResult.getNextPageId(), theRequest.getParameters()));
			}
			if (isNotBlank(theResult.getPreviousPageId())) {
				links.setPrev(RestfulServerUtils.createPagingLink(
						links, theRequest, searchId, theResult.getPreviousPageId(), theRequest.getParameters()));
			}
		} else if (searchId != null) {
			/*
			 * We're doing offset pages - Note that we only return paging links if we actually included
			 * some results in the response. We do this to avoid situations where people have faked the
			 * offset number to some huge number to avoid them getting back paging links that don't
			 * make sense.
			 */
			if (resourceList.size() > 0) {
				if (numTotalResults == null || theOffset + numToReturn < numTotalResults) {
					links.setNext((RestfulServerUtils.createPagingLink(
							links,
							theRequest,
							searchId,
							theOffset + numToReturn,
							numToReturn,
							theRequest.getParameters())));
				}
				if (theOffset > 0) {
					int start = Math.max(0, theOffset - pageSize);
					links.setPrev(RestfulServerUtils.createPagingLink(
							links, theRequest, searchId, start, pageSize, theRequest.getParameters()));
				}
			}
		}

		bundleFactory.addRootPropertiesToBundle(theResult.getUuid(), links, theResult.size(), theResult.getPublished());
		bundleFactory.addResourcesToBundle(
				new ArrayList<>(resourceList),
				theBundleType,
				links.serverBase,
				theServer.getBundleInclusionRule(),
				theIncludes);

		return bundleFactory.getResourceBundle();
	}

	private static boolean isEverythingOperation(RequestDetails theRequest) {
		return (theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE
						|| theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE)
				&& theRequest.getOperation() != null
				&& theRequest.getOperation().equals("$everything");
	}
}
