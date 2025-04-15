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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	private static class OffsetLimitInfo {
		private final Integer offset;
		private final Integer limit;

		OffsetLimitInfo(Integer offset, Integer limit) {
			this.offset = offset;
			this.limit = limit;
		}

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

	private static class InitialPagingResults {
		int pageSize;
		List<IBaseResource> resourceList;
		int numToReturn;
		String searchId;
		Integer numTotalResults;

		InitialPagingResults(
				int pageSize,
				List<IBaseResource> resourceList,
				int numToReturn,
				String searchId,
				Integer numTotalResults) {
			this.pageSize = pageSize;
			this.resourceList = resourceList;
			this.numToReturn = numToReturn;
			this.searchId = searchId;
			this.numTotalResults = numTotalResults;
		}
	}

	// Once this package language level allows for them these records can be used in place of the inner static classes
	//    private record OffsetLimitInfo(Integer offset, Integer limit) {
	//        int addOffsetAndLimit() {
	//            return offsetOrZero() + limitOrZero();
	//        }
	//
	//        int maxOfDifference() {
	//            return Math.max(offsetOrZero() - limitOrZero(), 0);
	//        }
	//
	//        private int offsetOrZero() {
	//            return defaultZeroIfNull(offset);
	//        }
	//
	//        private int limitOrZero() {
	//            return defaultZeroIfNull(offset);
	//        }
	//
	//        private int defaultZeroIfNull(Integer value) {
	//            return defaultIfNull(value, 0);
	//        }
	//    }
	//
	//    private record InitialPagingResults(
	//            int pageSize,
	//            List<IBaseResource> resourceList,
	//            int numToReturn,
	//            String searchId,
	//            Integer numTotalResults) {}

	public static IBaseResource createBundleFromBundleProvider(
			IRestfulServer<?> server,
			RequestDetails request,
			Integer limit,
			String linkSelf,
			Set<Include> includes,
			IBundleProvider result,
			int offset,
			BundleTypeEnum bundleType,
			String searchId) {

		final OffsetLimitInfo offsetLimitInfo = extractOffsetPageInfo(result, request, limit);

		final InitialPagingResults initialPagingResults =
				extractInitialPagingResults(server, request, result, offset, searchId, offsetLimitInfo);

		removeNullIfNeeded(initialPagingResults.resourceList);
		validateAllResourcesHaveId(initialPagingResults.resourceList);

		final BundleLinks links = buildLinks(
				server, request, linkSelf, includes, result, offset, bundleType, offsetLimitInfo, initialPagingResults);

		return buildBundle(server, includes, result, bundleType, links, initialPagingResults.resourceList);
	}

	@Nonnull
	private static BundleLinks buildLinks(
			IRestfulServer<?> server,
			RequestDetails request,
			String linkSelf,
			Set<Include> includes,
			IBundleProvider result,
			int offset,
			BundleTypeEnum bundleType,
			OffsetLimitInfo offsetLimitInfo,
			InitialPagingResults initialPagingResults) {

		BundleLinks links = new BundleLinks(
				request.getFhirServerBase(),
				includes,
				RestfulServerUtils.prettyPrintResponse(server, request),
				bundleType);
		links.setSelf(linkSelf);

		if (result.getCurrentPageOffset() != null) {

			if (isNotBlank(result.getNextPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(
						links,
						request.getRequestPath(),
						request.getTenantId(),
						offsetLimitInfo.addOffsetAndLimit(),
						offsetLimitInfo.limit,
						request.getParameters()));
			}
			if (isNotBlank(result.getPreviousPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(
						links,
						request.getRequestPath(),
						request.getTenantId(),
						offsetLimitInfo.maxOfDifference(),
						offsetLimitInfo.limit,
						request.getParameters()));
			}
		}

		if (offsetLimitInfo.offset != null || (!server.canStoreSearchResults() && !isEverythingOperation(request))) {
			handleOffsetPage(server, request, offset, offsetLimitInfo, initialPagingResults, links);
		} else if (isNotBlank(result.getCurrentPageId())) {
			handleCurrentPage(request, result, initialPagingResults, links);
		} else if (initialPagingResults.searchId != null && !initialPagingResults.resourceList.isEmpty()) {
			handleSearchId(request, offset, initialPagingResults, links);
		}
		return links;
	}

	private static void handleSearchId(
			RequestDetails request, int offset, InitialPagingResults initialPagingResults, BundleLinks links) {
		if (initialPagingResults.numTotalResults == null
				|| offset + initialPagingResults.numToReturn < initialPagingResults.numTotalResults) {
			links.setNext((RestfulServerUtils.createPagingLink(
					links,
					request,
					initialPagingResults.searchId,
					offset + initialPagingResults.numToReturn,
					initialPagingResults.numToReturn,
					request.getParameters())));
		}
		if (offset > 0) {
			int start = Math.max(0, offset - initialPagingResults.pageSize);
			links.setPrev(RestfulServerUtils.createPagingLink(
					links,
					request,
					initialPagingResults.searchId,
					start,
					initialPagingResults.pageSize,
					request.getParameters()));
		}
	}

	private static void handleCurrentPage(
			RequestDetails request,
			IBundleProvider result,
			InitialPagingResults initialPagingResults,
			BundleLinks links) {
		String searchIdToUse;
		// We're doing named pages
		searchIdToUse = result.getUuid();
		if (isNotBlank(result.getNextPageId())) {
			links.setNext(RestfulServerUtils.createPagingLink(
					links, request, searchIdToUse, result.getNextPageId(), request.getParameters()));
		}
		if (isNotBlank(result.getPreviousPageId())) {
			links.setPrev(RestfulServerUtils.createPagingLink(
					links,
					request,
					initialPagingResults.searchId,
					result.getPreviousPageId(),
					request.getParameters()));
		}
	}

	private static void handleOffsetPage(
			IRestfulServer<?> server,
			RequestDetails request,
			int offset,
			OffsetLimitInfo offsetLimitInfo,
			InitialPagingResults initialPagingResults,
			BundleLinks links) {
		// Paging without caching
		// We're doing offset pages
		int requestedToReturn = initialPagingResults.numToReturn;
		if (server.getPagingProvider() == null && offsetLimitInfo.offset != null) {
			// There is no paging provider at all, so assume we're querying up to all the results we
			// need every time
			requestedToReturn += offsetLimitInfo.offset;
		}
		if ((initialPagingResults.numTotalResults == null || requestedToReturn < initialPagingResults.numTotalResults)
				&& !initialPagingResults.resourceList.isEmpty()) {
			links.setNext(RestfulServerUtils.createOffsetPagingLink(
					links,
					request.getRequestPath(),
					request.getTenantId(),
					defaultIfNull(offsetLimitInfo.offset, 0) + initialPagingResults.numToReturn,
					initialPagingResults.numToReturn,
					request.getParameters()));
		}

		if (offsetLimitInfo.offset != null && offsetLimitInfo.offset > 0) {
			int start = Math.max(0, offset - initialPagingResults.pageSize);
			links.setPrev(RestfulServerUtils.createOffsetPagingLink(
					links,
					request.getRequestPath(),
					request.getTenantId(),
					start,
					initialPagingResults.pageSize,
					request.getParameters()));
		}
	}

	private static OffsetLimitInfo extractOffsetPageInfo(
			IBundleProvider result, RequestDetails request, Integer limit) {
		Integer offsetToUse;
		Integer limitToUse = limit;
		if (result.getCurrentPageOffset() != null) {
			offsetToUse = result.getCurrentPageOffset();
			limitToUse = result.getCurrentPageSize();
			Validate.notNull(
					limitToUse, "IBundleProvider returned a non-null offset, but did not return a non-null page size");
		} else {
			offsetToUse = RestfulServerUtils.tryToExtractNamedParameter(request, Constants.PARAM_OFFSET);
		}
		return new OffsetLimitInfo(offsetToUse, limitToUse);
	}

	private static InitialPagingResults extractInitialPagingResults(
			IRestfulServer<?> server,
			RequestDetails request,
			IBundleProvider result,
			int offset,
			String searchId,
			OffsetLimitInfo offsetLimitInfo) {

		if (offsetLimitInfo.offset != null || !server.canStoreSearchResults()) {
			return handleOffset(server, result, offsetLimitInfo);
		}

		return handleNonOffset(server, request, result, offset, searchId, offsetLimitInfo);
	}

	@Nonnull
	private static InitialPagingResults handleNonOffset(
			IRestfulServer<?> server,
			RequestDetails request,
			IBundleProvider result,
			int offset,
			String searchId,
			OffsetLimitInfo offsetLimitInfo) {

		Integer numTotalResults = result.size();
		List<IBaseResource> resourceList;
		int numToReturn;
		final int pageSize;
		IPagingProvider pagingProvider = server.getPagingProvider();

		if (offsetLimitInfo.limit == null || offsetLimitInfo.limit.equals(0)) {
			pageSize = pagingProvider.getDefaultPageSize();
		} else {
			pageSize = Math.min(pagingProvider.getMaximumPageSize(), offsetLimitInfo.limit);
		}
		numToReturn = pageSize;

		if (numTotalResults != null) {
			numToReturn = Math.min(numToReturn, numTotalResults - offset);
		}

		if (numToReturn > 0 || result.getCurrentPageId() != null) {
			resourceList = result.getResources(offset, numToReturn + offset);
		} else {
			resourceList = Collections.emptyList();
		}
		RestfulServerUtils.validateResourceListNotNull(resourceList);

		if (numTotalResults == null) {
			numTotalResults = result.size();
		}

		final String searchIdToUse =
				computeSearchId(request, result, searchId, numTotalResults, numToReturn, pagingProvider);

		return new InitialPagingResults(pageSize, resourceList, numToReturn, searchIdToUse, numTotalResults);
	}

	@Nullable
	private static String computeSearchId(
			RequestDetails request,
			IBundleProvider result,
			String searchId,
			Integer numTotalResults,
			int numToReturn,
			IPagingProvider pagingProvider) {
		String searchIdToUse = null;
		if (searchId != null) {
			searchIdToUse = searchId;
		} else {
			if (numTotalResults == null || numTotalResults > numToReturn) {
				searchIdToUse = pagingProvider.storeResultList(request, result);
				if (isBlank(searchIdToUse)) {
					ourLog.info(
							"Found {} results but paging provider did not provide an ID to use for paging",
							numTotalResults);
					searchIdToUse = null;
				}
			}
		}
		return searchIdToUse;
	}

	@Nonnull
	private static InitialPagingResults handleOffset(
			IRestfulServer<?> server, IBundleProvider result, OffsetLimitInfo offsetLimitInfo) {
		String searchIdToUse = null;
		final int pageSize;
		int numToReturn;
		Integer numTotalResults = result.size();

		List<IBaseResource> resourceList;
		if (offsetLimitInfo.limit != null) {
			pageSize = offsetLimitInfo.limit;
		} else {
			if (server.getDefaultPageSize() != null) {
				pageSize = server.getDefaultPageSize();
			} else {
				pageSize = numTotalResults != null ? numTotalResults : Integer.MAX_VALUE;
			}
		}
		numToReturn = pageSize;

		if (offsetLimitInfo.offset != null || result.getCurrentPageOffset() != null) {
			// When offset query is done result already contains correct amount (+ ir includes
			// etc.) so return everything
			resourceList = result.getResources(0, Integer.MAX_VALUE);
		} else if (numToReturn > 0) {
			resourceList = result.getResources(0, numToReturn);
		} else {
			resourceList = Collections.emptyList();
		}
		RestfulServerUtils.validateResourceListNotNull(resourceList);

		return new InitialPagingResults(pageSize, resourceList, numToReturn, searchIdToUse, numTotalResults);
	}

	private static IBaseResource buildBundle(
			IRestfulServer<?> server,
			Set<Include> includes,
			IBundleProvider result,
			BundleTypeEnum bundleType,
			BundleLinks links,
			List<IBaseResource> resourceList) {
		IVersionSpecificBundleFactory bundleFactory = server.getFhirContext().newBundleFactory();

		bundleFactory.addRootPropertiesToBundle(result.getUuid(), links, result.size(), result.getPublished());
		bundleFactory.addResourcesToBundle(
				new ArrayList<>(resourceList), bundleType, links.serverBase, server.getBundleInclusionRule(), includes);

		return bundleFactory.getResourceBundle();
	}

	private static void removeNullIfNeeded(List<IBaseResource> resourceList) {
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
	}

	private static void validateAllResourcesHaveId(List<IBaseResource> resourceList) {
		/*
		 * Make sure all returned resources have an ID (if not, this is a bug in the user server code)
		 */
		for (IBaseResource next : resourceList) {
			if ((next.getIdElement() == null || next.getIdElement().isEmpty())
					&& !(next instanceof IBaseOperationOutcome)) {
				throw new InternalErrorException(Msg.code(2637)
						+ String.format(
								"Server method returned resource of type[%s] with no ID specified (IResource#setId(IdDt) must be called)",
								next.getIdElement()));
			}
		}
	}

	private static boolean isEverythingOperation(RequestDetails request) {
		return (request.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE
						|| request.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE)
				&& request.getOperation() != null
				&& request.getOperation().equals("$everything");
	}
}
