package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.r4.model.Patient.SP_IDENTIFIER;

/// This interceptor looks at searches which combine chained identifier parameters
/// with at least one other parameter, e.g.:
/// `Observation?status=final&subject.identifier=http://foo|123`
/// and pre-resolves the chained identifier parameter(s), so that the search is
/// performed using a direct resource ID search, e.g.
/// `Observation?status=final&subject=Patient/ABC`.
///
/// This should generally improve performance for this kind of search at
/// large-scale, since the resulting query has fewer joins and is therefore
/// easier for the database query planner to execute efficiently.
///
/// @since 8.14.0
@Interceptor
public class PreResolveChainedSearchesInterceptor {
	public static final int IDENTIFIER_CAP = 100;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final FhirContext myContext;
	private final DaoRegistry myDaoRegistry;
	private final IInterceptorService myInterceptorService;

	/**
	 * Constructor
	 */
	public PreResolveChainedSearchesInterceptor(
			@Nonnull FhirContext theContext,
			@Nonnull IInterceptorService theInterceptorService,
			@Nonnull ISearchParamRegistry theSearchParamRegistry,
			@Nonnull DaoRegistry theDaoRegistry) {
		Validate.notNull(theContext, "theContext must not be null");
		Validate.notNull(theSearchParamRegistry, "theSearchParamRegistry must not be null");
		Validate.notNull(theDaoRegistry, "theDaoRegistry must not be null");
		Validate.notNull(theInterceptorService, "theInterceptorService must not be null");

		myContext = theContext;
		mySearchParamRegistry = theSearchParamRegistry;
		myDaoRegistry = theDaoRegistry;
		myInterceptorService = theInterceptorService;
	}

	@Hook(Pointcut.STORAGE_PRESEARCH_EXECUTED)
	void preResolveChainedSearches(
			ICachedSearchDetails theSearchDetails,
			SearchParameterMap theParamMap,
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId) {
		if (theParamMap.size() < 2) {
			return;
		}

		IInterceptorBroadcaster compositeBroker =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorService, theRequestDetails);

		for (Map.Entry<String, List<List<IQueryParameterType>>> paramAndList : theParamMap.entrySet()) {
			String paramName = paramAndList.getKey();
			for (List<IQueryParameterType> paramOrList : paramAndList.getValue()) {

				if (!paramOrList.isEmpty()
						&& paramOrList.get(0) instanceof ReferenceParam firstParam
						&& SP_IDENTIFIER.equals(firstParam.getChain())) {

					Set<String> targetResourceTypes = null;
					Set<TokenParam> tokens = new HashSet<>();

					for (IQueryParameterType param : paramOrList) {
						if (param instanceof ReferenceParam refParam) {
							Validate.isTrue("identifier".equals(refParam.getChain()));

							if (targetResourceTypes == null) {
								String resourceType = refParam.getResourceType();
								if (isNotBlank(resourceType)) {
									targetResourceTypes = Set.of(resourceType);
								} else {
									RuntimeSearchParam sourceParam = mySearchParamRegistry.getActiveSearchParam(
											theSearchDetails.getResourceType(),
											paramName,
											ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
									if (sourceParam == null) {
										break;
									}
									targetResourceTypes = sourceParam.getTargets();
									if (targetResourceTypes.isEmpty() || targetResourceTypes.size() > 100) {
										targetResourceTypes = null;
										break;
									}
								}
							}

							TokenParam token = new TokenParam();
							token.setValueAsQueryToken(myContext, refParam.getChain(), null, refParam.getValue());
							tokens.add(token);
						}
					}

					if (targetResourceTypes != null) {
						if (targetResourceTypes.size() == 1) {
							/*
							 * If the chained parameter can only resolve to a single target type, try to
							 * pre-resolve it
							 */
							String resourceTypeToTry =
									targetResourceTypes.iterator().next();
							preResolveIdentifiersForType(
									theRequestDetails,
									compositeBroker,
									tokens,
									paramOrList,
									paramName,
									firstParam.getChain(),
									resourceTypeToTry,
									theRequestPartitionId);
						} else if (targetResourceTypes.size() < 20 && targetResourceTypes.contains("Patient")) {
							/*
							 * If the list of target resource types is small enough, and includes Patient, we'll check if the identifiers
							 * can be resolved as Patient identifiers. We do this because in most real-world cases, parameters like
							 * Encounter?subject.identifier will be using Patient identifiers almost always. If the pre-resolution
							 * fails to find any matches, it leaves the parameters alone so that the regular matching algorithm
							 * (which will match all types) will fire instead.
							 *
							 * This does have one drawback- If a request like Observation?subject.identifier=http://foo|123 is made, and the
							 * given identifier matches multiple types (e.g. the same identifier is used by both a Patient and a Group, and there
							 * is a separate Observation with each one as a subject, we'll only end up pre-resolving the Patient and the Observation
							 * pointing to the group will be missed. But this is such an incredibly rare scenario that the benefits still
							 * seem worth it.
							 */
							String resourceTypeToTry = "Patient";
							preResolveIdentifiersForType(
									theRequestDetails,
									compositeBroker,
									tokens,
									paramOrList,
									paramName,
									firstParam.getChain(),
									resourceTypeToTry,
									theRequestPartitionId);
						}
					}
				}
			}
		}
	}

	private void preResolveIdentifiersForType(
			RequestDetails theRequestDetails,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			Set<TokenParam> theTokensToPreResolve,
			List<IQueryParameterType> theParameterOrList,
			String theTopLevelParameterName,
			String theChainedParameterName,
			String theTargetResourceType,
			RequestPartitionId theRequestPartitionId) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theTargetResourceType);

		// There is no guarantee that identifiers are unique to a single patient. In order
		// to avoid unbounded queries, we put an arbitrary limit on the number that we'll
		// pre-resolve.
		int searchResultLimit = theTokensToPreResolve.size() + IDENTIFIER_CAP;

		SearchParameterMap paramMap = new SearchParameterMap();
		paramMap.setLoadSynchronousUpTo(searchResultLimit);
		paramMap.add(theChainedParameterName, new TokenOrListParam(theTokensToPreResolve));

		// We want to ensure that the pre-resolution lookup uses the same partition
		// as the containing search
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setRequestPartitionId(theRequestPartitionId);

		List<IIdType> nextRoundResourceIds = dao.searchForResourceIds(paramMap, srd);

		if (nextRoundResourceIds.isEmpty()) {
			firePerformanceLoggingAtInfoLevel(
					theRequestDetails,
					theInterceptorBroadcaster,
					() -> "Unwrapping chained search parameter[" + theTopLevelParameterName + "."
							+ theChainedParameterName + "] did not find any matching results for Patient targets");
		} else if (nextRoundResourceIds.size() >= searchResultLimit) {
			firePerformanceLoggingAtInfoLevel(
					theRequestDetails,
					theInterceptorBroadcaster,
					() -> "Unwrapping chained search parameter[" + theTopLevelParameterName + "."
							+ theChainedParameterName + "] found too many matching results for Patient targets (>= "
							+ searchResultLimit + ")");
			nextRoundResourceIds.clear();
		} else {
			firePerformanceLoggingAtInfoLevel(
					theRequestDetails,
					theInterceptorBroadcaster,
					() -> "Pre-resolved " + nextRoundResourceIds.size()
							+ " resource IDs to unwrap chained search parameter[" + theTopLevelParameterName + "."
							+ theChainedParameterName + "]");
		}

		// Replace the chained references with raw (unchained) equivalent references
		if (!nextRoundResourceIds.isEmpty()) {
			theParameterOrList.clear();
			nextRoundResourceIds.stream()
					.map(t -> t.toUnqualifiedVersionless().getValue())
					.distinct()
					.map(ReferenceParam::new)
					.forEach(theParameterOrList::add);
		}
	}

	private static void firePerformanceLoggingAtInfoLevel(
			RequestDetails theRequestDetails,
			IInterceptorBroadcaster theCompositeBroker,
			Supplier<String> theMessageSupplier) {
		theCompositeBroker.ifHasCallHooks(Pointcut.JPA_PERFTRACE_INFO, () -> {
			StorageProcessingMessage message = new StorageProcessingMessage();
			message.setMessage(theMessageSupplier.get());
			return new HookParams()
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
					.add(StorageProcessingMessage.class, message);
		});
	}
}
