package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.i18n.Msg;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ResponseBundleBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(ResponseBundleBuilder.class);

	private final boolean myIsOffsetModeHistory;

	public ResponseBundleBuilder(boolean theIsOffsetModeHistory) {
		myIsOffsetModeHistory = theIsOffsetModeHistory;
	}

	IBaseBundle createBundleFromBundleProvider(ResponseBundleRequest theResponseBundleRequest) {
		IRestfulServer<?> server = theResponseBundleRequest.server;

		IVersionSpecificBundleFactory bundleFactory = server.getFhirContext().newBundleFactory();
		final Integer offset;
		Integer limit = theResponseBundleRequest.limit;

		IBundleProvider bundleProvider = theResponseBundleRequest.bundleProvider;
		if (bundleProvider.getCurrentPageOffset() != null) {
			offset = bundleProvider.getCurrentPageOffset();
			limit = bundleProvider.getCurrentPageSize();
			Validate.notNull(limit, "IBundleProvider returned a non-null offset, but did not return a non-null page size");
		} else {
			offset = RestfulServerUtils.tryToExtractNamedParameter(theResponseBundleRequest.requestDetails, Constants.PARAM_OFFSET);
		}

		int numToReturn;
		String searchId = null;
		List<IBaseResource> resourceList;
		Integer numTotalResults = bundleProvider.size();

		int pageSize;
		if (offset != null || !server.canStoreSearchResults()) {
			if (limit != null) {
				pageSize = limit;
			} else {
				if (server.getDefaultPageSize() != null) {
					pageSize = server.getDefaultPageSize();
				} else {
					pageSize = numTotalResults != null ? numTotalResults : Integer.MAX_VALUE;
				}
			}
			numToReturn = pageSize;

			if ((offset != null && !myIsOffsetModeHistory) || bundleProvider.getCurrentPageOffset() != null) {
				// When offset query is done theResult already contains correct amount (+ their includes etc.) so return everything
				resourceList = bundleProvider.getResources(0, Integer.MAX_VALUE);
			} else if (numToReturn > 0) {
				resourceList = bundleProvider.getResources(0, numToReturn);
			} else {
				resourceList = Collections.emptyList();
			}
			RestfulServerUtils.validateResourceListNotNull(resourceList);

		} else {
			IPagingProvider pagingProvider = server.getPagingProvider();
			if (limit == null || limit.equals(0)) {
				pageSize = pagingProvider.getDefaultPageSize();
			} else {
				pageSize = Math.min(pagingProvider.getMaximumPageSize(), limit);
			}
			numToReturn = pageSize;

			if (numTotalResults != null) {
				numToReturn = Math.min(numToReturn, numTotalResults - theResponseBundleRequest.offset);
			}

			if (numToReturn > 0 || bundleProvider.getCurrentPageId() != null) {
				resourceList = bundleProvider.getResources(theResponseBundleRequest.offset, numToReturn + theResponseBundleRequest.offset);
			} else {
				resourceList = Collections.emptyList();
			}
			RestfulServerUtils.validateResourceListNotNull(resourceList);

			if (theResponseBundleRequest.searchId != null) {
				searchId = theResponseBundleRequest.searchId;
			} else {
				if (numTotalResults == null || numTotalResults > numToReturn) {
					searchId = pagingProvider.storeResultList(theResponseBundleRequest.requestDetails, bundleProvider);
					if (StringUtils.isBlank(searchId)) {
						ourLog.info("Found {} results but paging provider did not provide an ID to use for paging", numTotalResults);
						searchId = null;
					}
				}
			}
		}

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

		/*
		 * Make sure all returned resources have an ID (if not, this is a bug
		 * in the user server code)
		 */
		for (IBaseResource next : resourceList) {
			if (next.getIdElement() == null || next.getIdElement().isEmpty()) {
				if (!(next instanceof IBaseOperationOutcome)) {
					throw new InternalErrorException(Msg.code(435) + "Server method returned resource of type[" + next.getClass().getSimpleName() + "] with no ID specified (IResource#setId(IdDt) must be called)");
				}
			}
		}

		BundleLinks links = new BundleLinks(theResponseBundleRequest.requestDetails.getFhirServerBase(), theResponseBundleRequest.includes, RestfulServerUtils.prettyPrintResponse(server, theResponseBundleRequest.requestDetails), theResponseBundleRequest.bundleType);
		links.setSelf(theResponseBundleRequest.linkSelf);

		if (bundleProvider.getCurrentPageOffset() != null) {

			if (StringUtils.isNotBlank(bundleProvider.getNextPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theResponseBundleRequest.requestDetails.getRequestPath(), theResponseBundleRequest.requestDetails.getTenantId(), offset + limit, limit, theResponseBundleRequest.requestDetails.getParameters()));
			}
			if (StringUtils.isNotBlank(bundleProvider.getPreviousPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theResponseBundleRequest.requestDetails.getRequestPath(), theResponseBundleRequest.requestDetails.getTenantId(), Math.max(offset - limit, 0), limit, theResponseBundleRequest.requestDetails.getParameters()));
			}

		}

		if (offset != null || (!server.canStoreSearchResults() && !isEverythingOperation(theResponseBundleRequest.requestDetails)) || myIsOffsetModeHistory) {
			// Paging without caching
			// We're doing offset pages
			int requestedToReturn = numToReturn;
			if (server.getPagingProvider() == null && offset != null) {
				// There is no paging provider at all, so assume we're querying up to all the results we need every time
				requestedToReturn += offset;
			}
			if (numTotalResults == null || requestedToReturn < numTotalResults) {
				if (!resourceList.isEmpty()) {
					links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theResponseBundleRequest.requestDetails.getRequestPath(), theResponseBundleRequest.requestDetails.getTenantId(), ObjectUtils.defaultIfNull(offset, 0) + numToReturn, numToReturn, theResponseBundleRequest.requestDetails.getParameters()));
				}
			}
			if (offset != null && offset > 0) {
				int start = Math.max(0, offset - pageSize);
				links.setPrev(RestfulServerUtils.createOffsetPagingLink(links, theResponseBundleRequest.requestDetails.getRequestPath(), theResponseBundleRequest.requestDetails.getTenantId(), start, pageSize, theResponseBundleRequest.requestDetails.getParameters()));
			}
		} else if (StringUtils.isNotBlank(bundleProvider.getCurrentPageId())) {
			// We're doing named pages
			searchId = bundleProvider.getUuid();
			if (StringUtils.isNotBlank(bundleProvider.getNextPageId())) {
				links.setNext(RestfulServerUtils.createPagingLink(links, theResponseBundleRequest.requestDetails, searchId, bundleProvider.getNextPageId(), theResponseBundleRequest.requestDetails.getParameters()));
			}
			if (StringUtils.isNotBlank(bundleProvider.getPreviousPageId())) {
				links.setPrev(RestfulServerUtils.createPagingLink(links, theResponseBundleRequest.requestDetails, searchId, bundleProvider.getPreviousPageId(), theResponseBundleRequest.requestDetails.getParameters()));
			}
		} else if (searchId != null) {
			/*
			 * We're doing offset pages - Note that we only return paging links if we actually
			 * included some results in the response. We do this to avoid situations where
			 * people have faked the offset number to some huge number to avoid them getting
			 * back paging links that don't make sense.
			 */
			if (resourceList.size() > 0) {
				if (numTotalResults == null || theResponseBundleRequest.offset + numToReturn < numTotalResults) {
					links.setNext((RestfulServerUtils.createPagingLink(links, theResponseBundleRequest.requestDetails, searchId, theResponseBundleRequest.offset + numToReturn, numToReturn, theResponseBundleRequest.requestDetails.getParameters())));
				}
				if (theResponseBundleRequest.offset > 0) {
					int start = Math.max(0, theResponseBundleRequest.offset - pageSize);
					links.setPrev(RestfulServerUtils.createPagingLink(links, theResponseBundleRequest.requestDetails, searchId, start, pageSize, theResponseBundleRequest.requestDetails.getParameters()));
				}
			}
		}

		bundleFactory.addRootPropertiesToBundle(bundleProvider.getUuid(), links, bundleProvider.size(), bundleProvider.getPublished());
		bundleFactory.addResourcesToBundle(new ArrayList<>(resourceList), theResponseBundleRequest.bundleType, links.serverBase, server.getBundleInclusionRule(), theResponseBundleRequest.includes);

		return (IBaseBundle) bundleFactory.getResourceBundle();

	}

	private boolean isEverythingOperation(RequestDetails theRequest) {
		return (theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE
			|| theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE)
			&& theRequest.getOperation() != null && theRequest.getOperation().equals("$everything");
	}
}
