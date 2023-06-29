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
		IRestfulServer<?> server = theResponseBundleRequest.getServer();

		IVersionSpecificBundleFactory bundleFactory = server.getFhirContext().newBundleFactory();
		final Integer offset;
		Integer limit = theResponseBundleRequest.getLimit();

		IBundleProvider bundleProvider = theResponseBundleRequest.getBundleProvider();
		if (bundleProvider.getCurrentPageOffset() != null) {
			offset = bundleProvider.getCurrentPageOffset();
			limit = bundleProvider.getCurrentPageSize();
			Validate.notNull(limit, "IBundleProvider returned a non-null offset, but did not return a non-null page size");
		} else {
			offset = RestfulServerUtils.tryToExtractNamedParameter(theResponseBundleRequest.getRequest(), Constants.PARAM_OFFSET);
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
				numToReturn = Math.min(numToReturn, numTotalResults - theResponseBundleRequest.getOffset());
			}

			if (numToReturn > 0 || bundleProvider.getCurrentPageId() != null) {
				resourceList = bundleProvider.getResources(theResponseBundleRequest.getOffset(), numToReturn + theResponseBundleRequest.getOffset());
			} else {
				resourceList = Collections.emptyList();
			}
			RestfulServerUtils.validateResourceListNotNull(resourceList);

			if (numTotalResults == null) {
				numTotalResults = bundleProvider.size();
			}

			if (theResponseBundleRequest.getSearchId() != null) {
				searchId = theResponseBundleRequest.getSearchId();
			} else {
				if (numTotalResults == null || numTotalResults > numToReturn) {
					searchId = pagingProvider.storeResultList(theResponseBundleRequest.getRequest(), bundleProvider);
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

		BundleLinks links = new BundleLinks(theResponseBundleRequest.getRequest().getFhirServerBase(), theResponseBundleRequest.getIncludes(), RestfulServerUtils.prettyPrintResponse(server, theResponseBundleRequest.getRequest()), theResponseBundleRequest.getBundleType());
		links.setSelf(theResponseBundleRequest.getLinkSelf());

		if (bundleProvider.getCurrentPageOffset() != null) {

			if (StringUtils.isNotBlank(bundleProvider.getNextPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theResponseBundleRequest.getRequest().getRequestPath(), theResponseBundleRequest.getRequest().getTenantId(), offset + limit, limit, theResponseBundleRequest.getRequest().getParameters()));
			}
			if (StringUtils.isNotBlank(bundleProvider.getPreviousPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theResponseBundleRequest.getRequest().getRequestPath(), theResponseBundleRequest.getRequest().getTenantId(), Math.max(offset - limit, 0), limit, theResponseBundleRequest.getRequest().getParameters()));
			}

		}

		if (offset != null || (!server.canStoreSearchResults() && !isEverythingOperation(theResponseBundleRequest.getRequest())) || myIsOffsetModeHistory) {
			// Paging without caching
			// We're doing offset pages
			int requestedToReturn = numToReturn;
			if (server.getPagingProvider() == null && offset != null) {
				// There is no paging provider at all, so assume we're querying up to all the results we need every time
				requestedToReturn += offset;
			}
			if (numTotalResults == null || requestedToReturn < numTotalResults) {
				if (!resourceList.isEmpty()) {
					links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theResponseBundleRequest.getRequest().getRequestPath(), theResponseBundleRequest.getRequest().getTenantId(), ObjectUtils.defaultIfNull(offset, 0) + numToReturn, numToReturn, theResponseBundleRequest.getRequest().getParameters()));
				}
			}
			if (offset != null && offset > 0) {
				int start = Math.max(0, offset - pageSize);
				links.setPrev(RestfulServerUtils.createOffsetPagingLink(links, theResponseBundleRequest.getRequest().getRequestPath(), theResponseBundleRequest.getRequest().getTenantId(), start, pageSize, theResponseBundleRequest.getRequest().getParameters()));
			}
		} else if (StringUtils.isNotBlank(bundleProvider.getCurrentPageId())) {
			// We're doing named pages
			searchId = bundleProvider.getUuid();
			if (StringUtils.isNotBlank(bundleProvider.getNextPageId())) {
				links.setNext(RestfulServerUtils.createPagingLink(links, theResponseBundleRequest.getRequest(), searchId, bundleProvider.getNextPageId(), theResponseBundleRequest.getRequest().getParameters()));
			}
			if (StringUtils.isNotBlank(bundleProvider.getPreviousPageId())) {
				links.setPrev(RestfulServerUtils.createPagingLink(links, theResponseBundleRequest.getRequest(), searchId, bundleProvider.getPreviousPageId(), theResponseBundleRequest.getRequest().getParameters()));
			}
		} else if (searchId != null) {
			/*
			 * We're doing offset pages - Note that we only return paging links if we actually
			 * included some results in the response. We do this to avoid situations where
			 * people have faked the offset number to some huge number to avoid them getting
			 * back paging links that don't make sense.
			 */
			if (resourceList.size() > 0) {
				if (numTotalResults == null || theResponseBundleRequest.getOffset() + numToReturn < numTotalResults) {
					links.setNext((RestfulServerUtils.createPagingLink(links, theResponseBundleRequest.getRequest(), searchId, theResponseBundleRequest.getOffset() + numToReturn, numToReturn, theResponseBundleRequest.getRequest().getParameters())));
				}
				if (theResponseBundleRequest.getOffset() > 0) {
					int start = Math.max(0, theResponseBundleRequest.getOffset() - pageSize);
					links.setPrev(RestfulServerUtils.createPagingLink(links, theResponseBundleRequest.getRequest(), searchId, start, pageSize, theResponseBundleRequest.getRequest().getParameters()));
				}
			}
		}

		bundleFactory.addRootPropertiesToBundle(bundleProvider.getUuid(), links, bundleProvider.size(), bundleProvider.getPublished());
		bundleFactory.addResourcesToBundle(new ArrayList<>(resourceList), theResponseBundleRequest.getBundleType(), links.serverBase, server.getBundleInclusionRule(), theResponseBundleRequest.getIncludes());

		return (IBaseBundle) bundleFactory.getResourceBundle();

	}

	private boolean isEverythingOperation(RequestDetails theRequest) {
		return (theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE
			|| theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE)
			&& theRequest.getOperation() != null && theRequest.getOperation().equals("$everything");
	}
}
