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
import org.apache.commons.lang3.ObjectUtils;
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

public class ResponseBundleBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(ResponseBundleBuilder.class);

	private final boolean myIsOffsetModeHistory;

	public ResponseBundleBuilder(boolean theIsOffsetModeHistory) {
		myIsOffsetModeHistory = theIsOffsetModeHistory;
	}

	IBaseBundle createBundleFromBundleProvider(ResponseBundleRequest theResponseBundleRequest) {
		final ResponsePage pageResponse = buildResponsePage(theResponseBundleRequest);
		removeNulls(pageResponse.resourceList);
		validateIds(pageResponse.resourceList);

		BundleLinks links = buildLinks(theResponseBundleRequest, pageResponse);

		IRestfulServer<?> server = theResponseBundleRequest.server;
		IVersionSpecificBundleFactory bundleFactory = server.getFhirContext().newBundleFactory();
		IBundleProvider bundleProvider = theResponseBundleRequest.bundleProvider;
		bundleFactory.addRootPropertiesToBundle(bundleProvider.getUuid(), links, bundleProvider.size(), bundleProvider.getPublished());
		bundleFactory.addResourcesToBundle(new ArrayList<>(pageResponse.resourceList), theResponseBundleRequest.bundleType, links.serverBase, server.getBundleInclusionRule(), theResponseBundleRequest.includes);

		return (IBaseBundle) bundleFactory.getResourceBundle();

	}


	private ResponsePage buildResponsePage(ResponseBundleRequest theResponseBundleRequest) {
		final IRestfulServer<?> server = theResponseBundleRequest.server;
		final IBundleProvider bundleProvider = theResponseBundleRequest.bundleProvider;
		final RequestedPage pageRequest = theResponseBundleRequest.requestedPage;
		final List<IBaseResource> resourceList;
		final int pageSize;

		int numToReturn;
		Integer numTotalResults = bundleProvider.size();
		String searchId = null;

		if (pageRequest.offset != null || !server.canStoreSearchResults()) {
			pageSize = offsetCalculatePageSize(server, pageRequest, numTotalResults);
			numToReturn = pageSize;

			resourceList = offsetBuildResourceList(bundleProvider, pageRequest, numToReturn);
			RestfulServerUtils.validateResourceListNotNull(resourceList);
		} else {
			pageSize = pagingCalculatePageSize(pageRequest, server.getPagingProvider());
			numToReturn = pageSize;

			if (numTotalResults != null) {
				numToReturn = Math.min(numToReturn, numTotalResults - theResponseBundleRequest.offset);
			}

			resourceList = pagingBuildResourceList(theResponseBundleRequest, bundleProvider, numToReturn);
			RestfulServerUtils.validateResourceListNotNull(resourceList);

			searchId = pagingBuildSearchId(theResponseBundleRequest, numToReturn, numTotalResults);
		}

		return new ResponsePage(searchId, resourceList, numToReturn, numTotalResults, pageSize);
	}

	private static String pagingBuildSearchId(ResponseBundleRequest theResponseBundleRequest, int numToReturn, Integer numTotalResults) {
		final IPagingProvider pagingProvider = theResponseBundleRequest.server.getPagingProvider();
		String retval = null;

		if (theResponseBundleRequest.searchId != null) {
			retval = theResponseBundleRequest.searchId;
		} else {
			if (numTotalResults == null || numTotalResults > numToReturn) {
				retval = pagingProvider.storeResultList(theResponseBundleRequest.requestDetails, theResponseBundleRequest.bundleProvider);
				if (StringUtils.isBlank(retval)) {
					ourLog.info("Found {} results but paging provider did not provide an ID to use for paging", numTotalResults);
					retval = null;
				}
			}
		}
		return retval;
	}

	private static List<IBaseResource> pagingBuildResourceList(ResponseBundleRequest theResponseBundleRequest, IBundleProvider bundleProvider, int numToReturn) {
		final List<IBaseResource> retval;
		if (numToReturn > 0 || bundleProvider.getCurrentPageId() != null) {
			retval = bundleProvider.getResources(theResponseBundleRequest.offset, numToReturn + theResponseBundleRequest.offset);
		} else {
			retval = Collections.emptyList();
		}
		return retval;
	}

	private static int pagingCalculatePageSize(RequestedPage pageRequest, IPagingProvider pagingProvider) {
		if (pageRequest.limit == null || pageRequest.limit.equals(0)) {
			return pagingProvider.getDefaultPageSize();
		} else {
			return Math.min(pagingProvider.getMaximumPageSize(), pageRequest.limit);
		}
	}

	private List<IBaseResource> offsetBuildResourceList(IBundleProvider bundleProvider, RequestedPage pageRequest, int numToReturn) {
		final List<IBaseResource> retval;
		if ((pageRequest.offset != null && !myIsOffsetModeHistory) || bundleProvider.getCurrentPageOffset() != null) {
			// When offset query is done theResult already contains correct amount (+ their includes etc.) so return everything
			retval = bundleProvider.getResources(0, Integer.MAX_VALUE);
		} else if (numToReturn > 0) {
			retval = bundleProvider.getResources(0, numToReturn);
		} else {
			retval = Collections.emptyList();
		}
		return retval;
	}

	private static int offsetCalculatePageSize(IRestfulServer<?> server, RequestedPage pageRequest, Integer numTotalResults) {
		final int retval;
		if (pageRequest.limit != null) {
			retval = pageRequest.limit;
		} else {
			if (server.getDefaultPageSize() != null) {
				retval = server.getDefaultPageSize();
			} else {
				retval = numTotalResults != null ? numTotalResults : Integer.MAX_VALUE;
			}
		}
		return retval;
	}

	private static void validateIds(List<IBaseResource> resourceList) {
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

	private BundleLinks buildLinks(ResponseBundleRequest theResponseBundleRequest, ResponsePage pageResponse) {
		final IRestfulServer<?> server = theResponseBundleRequest.server;
		final IBundleProvider bundleProvider = theResponseBundleRequest.bundleProvider;
		final RequestedPage pageRequest = theResponseBundleRequest.requestedPage;

		BundleLinks retval = new BundleLinks(theResponseBundleRequest.requestDetails.getFhirServerBase(), theResponseBundleRequest.includes, RestfulServerUtils.prettyPrintResponse(server, theResponseBundleRequest.requestDetails), theResponseBundleRequest.bundleType);
		retval.setSelf(theResponseBundleRequest.linkSelf);

		if (bundleProvider.getCurrentPageOffset() != null) {

			if (StringUtils.isNotBlank(bundleProvider.getNextPageId())) {
				retval.setNext(RestfulServerUtils.createOffsetPagingLink(retval, theResponseBundleRequest.requestDetails.getRequestPath(), theResponseBundleRequest.requestDetails.getTenantId(), pageRequest.offset + pageRequest.limit, pageRequest.limit, theResponseBundleRequest.getRequestParameters()));
			}
			if (StringUtils.isNotBlank(bundleProvider.getPreviousPageId())) {
				retval.setNext(RestfulServerUtils.createOffsetPagingLink(retval, theResponseBundleRequest.requestDetails.getRequestPath(), theResponseBundleRequest.requestDetails.getTenantId(), Math.max(pageRequest.offset - pageRequest.limit, 0), pageRequest.limit, theResponseBundleRequest.getRequestParameters()));
			}

		}

		if (pageRequest.offset != null || (!server.canStoreSearchResults() && !isEverythingOperation(theResponseBundleRequest.requestDetails)) || myIsOffsetModeHistory) {
			// Paging without caching
			// We're doing offset pages
			int requestedToReturn = pageResponse.numToReturn;
			if (server.getPagingProvider() == null && pageRequest.offset != null) {
				// There is no paging provider at all, so assume we're querying up to all the results we need every time
				requestedToReturn += pageRequest.offset;
			}
			if (pageResponse.numTotalResults == null || requestedToReturn < pageResponse.numTotalResults) {
				if (!pageResponse.resourceList.isEmpty()) {
					retval.setNext(RestfulServerUtils.createOffsetPagingLink(retval, theResponseBundleRequest.requestDetails.getRequestPath(), theResponseBundleRequest.requestDetails.getTenantId(), ObjectUtils.defaultIfNull(pageRequest.offset, 0) + pageResponse.numToReturn, pageResponse.numToReturn, theResponseBundleRequest.getRequestParameters()));
				}
			}
			if (pageRequest.offset != null && pageRequest.offset > 0) {
				int start = Math.max(0, pageRequest.offset - pageResponse.pageSize);
				retval.setPrev(RestfulServerUtils.createOffsetPagingLink(retval, theResponseBundleRequest.requestDetails.getRequestPath(), theResponseBundleRequest.requestDetails.getTenantId(), start, pageResponse.pageSize, theResponseBundleRequest.getRequestParameters()));
			}
		} else if (StringUtils.isNotBlank(bundleProvider.getCurrentPageId())) {
			// We're doing named pages
			final String uuid = bundleProvider.getUuid();
			if (StringUtils.isNotBlank(bundleProvider.getNextPageId())) {
				retval.setNext(RestfulServerUtils.createPagingLink(retval, theResponseBundleRequest.requestDetails, uuid, bundleProvider.getNextPageId(), theResponseBundleRequest.getRequestParameters()));
			}
			if (StringUtils.isNotBlank(bundleProvider.getPreviousPageId())) {
				retval.setPrev(RestfulServerUtils.createPagingLink(retval, theResponseBundleRequest.requestDetails, uuid, bundleProvider.getPreviousPageId(), theResponseBundleRequest.getRequestParameters()));
			}
		} else if (pageResponse.searchId != null) {
			/*
			 * We're doing offset pages - Note that we only return paging links if we actually
			 * included some results in the response. We do this to avoid situations where
			 * people have faked the offset number to some huge number to avoid them getting
			 * back paging links that don't make sense.
			 */
			if (pageResponse.size() > 0) {
				if (pageResponse.numTotalResults == null || theResponseBundleRequest.offset + pageResponse.numToReturn < pageResponse.numTotalResults) {
					retval.setNext((RestfulServerUtils.createPagingLink(retval, theResponseBundleRequest.requestDetails, pageResponse.searchId, theResponseBundleRequest.offset + pageResponse.numToReturn, pageResponse.numToReturn, theResponseBundleRequest.getRequestParameters())));
				}
				if (theResponseBundleRequest.offset > 0) {
					int start = Math.max(0, theResponseBundleRequest.offset - pageResponse.pageSize);
					retval.setPrev(RestfulServerUtils.createPagingLink(retval, theResponseBundleRequest.requestDetails, pageResponse.searchId, start, pageResponse.pageSize, theResponseBundleRequest.getRequestParameters()));
				}
			}
		}
		return retval;
	}


	private boolean isEverythingOperation(RequestDetails theRequest) {
		return (theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE
			|| theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE)
			&& theRequest.getOperation() != null && theRequest.getOperation().equals("$everything");
	}
}
