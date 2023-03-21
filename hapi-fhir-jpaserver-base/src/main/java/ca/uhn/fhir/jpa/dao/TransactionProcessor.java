/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hibernate.internal.SessionImpl;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TransactionProcessor extends BaseTransactionProcessor {

	public static final Pattern SINGLE_PARAMETER_MATCH_URL_PATTERN = Pattern.compile("^[^?]+[?][a-z0-9-]+=[^&,]+$");
	private static final Logger ourLog = LoggerFactory.getLogger(TransactionProcessor.class);
	@Autowired
	private ApplicationContext myApplicationContext;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired(required = false)
	private HapiFhirHibernateJpaDialect myHapiFhirHibernateJpaDialect;
	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private JpaStorageSettings myStorageSettings;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private MatchResourceUrlService<JpaPid> myMatchResourceUrlService;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionSvc;


	public void setEntityManagerForUnitTest(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	@Override
	protected void validateDependencies() {
		super.validateDependencies();

		Validate.notNull(myEntityManager);
	}

	@VisibleForTesting
	public void setFhirContextForUnitTest(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	protected EntriesToProcessMap doTransactionWriteOperations(final RequestDetails theRequest, String theActionName, TransactionDetails theTransactionDetails, Set<IIdType> theAllIds,
																				  IdSubstitutionMap theIdSubstitutions, Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome, IBaseBundle theResponse, IdentityHashMap<IBase, Integer> theOriginalRequestOrder, List<IBase> theEntries, StopWatch theTransactionStopWatch) {

		ITransactionProcessorVersionAdapter versionAdapter = getVersionAdapter();
		RequestPartitionId requestPartitionId = null;
		if (!myPartitionSettings.isPartitioningEnabled()) {
			requestPartitionId = RequestPartitionId.allPartitions();
		} else {
			// If all entries in the transaction point to the exact same partition, we'll try and do a pre-fetch
			requestPartitionId = getSinglePartitionForAllEntriesOrNull(theRequest, theEntries, versionAdapter);
		}

		if (requestPartitionId != null) {
			preFetch(theTransactionDetails, theEntries, versionAdapter, requestPartitionId);
		}

		return super.doTransactionWriteOperations(theRequest, theActionName, theTransactionDetails, theAllIds, theIdSubstitutions, theIdToPersistedOutcome, theResponse, theOriginalRequestOrder, theEntries, theTransactionStopWatch);
	}

	private void preFetch(TransactionDetails theTransactionDetails, List<IBase> theEntries, ITransactionProcessorVersionAdapter theVersionAdapter, RequestPartitionId theRequestPartitionId) {
		Set<String> foundIds = new HashSet<>();
		List<Long> idsToPreFetch = new ArrayList<>();

		/*
		 * Pre-Fetch any resources that are referred to normally by ID, e.g.
		 * regular FHIR updates within the transaction.
		 */
		preFetchResourcesById(theTransactionDetails, theEntries, theVersionAdapter, theRequestPartitionId, foundIds, idsToPreFetch);

		/*
		 * Pre-resolve any conditional URLs we can
		 */
		preFetchConditionalUrls(theTransactionDetails, theEntries, theVersionAdapter, theRequestPartitionId, idsToPreFetch);

		IFhirSystemDao<?, ?> systemDao = myApplicationContext.getBean(IFhirSystemDao.class);
		systemDao.preFetchResources(JpaPid.fromLongList(idsToPreFetch));
	}

	private void preFetchResourcesById(TransactionDetails theTransactionDetails, List<IBase> theEntries, ITransactionProcessorVersionAdapter theVersionAdapter, RequestPartitionId theRequestPartitionId, Set<String> foundIds, List<Long> idsToPreFetch) {
		List<IIdType> idsToPreResolve = new ArrayList<>();
		for (IBase nextEntry : theEntries) {
			IBaseResource resource = theVersionAdapter.getResource(nextEntry);
			if (resource != null) {
				String verb = theVersionAdapter.getEntryRequestVerb(myFhirContext, nextEntry);
				if ("PUT".equals(verb) || "PATCH".equals(verb)) {
					String requestUrl = theVersionAdapter.getEntryRequestUrl(nextEntry);
					if (countMatches(requestUrl, '/') == 1 && countMatches(requestUrl, '?') == 0) {
						IIdType id = myFhirContext.getVersion().newIdType();
						id.setValue(requestUrl);
						idsToPreResolve.add(id);
					}
				}
			}
		}
		List<JpaPid> outcome = myIdHelperService.resolveResourcePersistentIdsWithCache(theRequestPartitionId, idsToPreResolve)
			.stream().collect(Collectors.toList());
		for (JpaPid next : outcome) {
			foundIds.add(next.getAssociatedResourceId().toUnqualifiedVersionless().getValue());
			theTransactionDetails.addResolvedResourceId(next.getAssociatedResourceId(), next);
			if (myStorageSettings.getResourceClientIdStrategy() != JpaStorageSettings.ClientIdStrategyEnum.ANY || !next.getAssociatedResourceId().isIdPartValidLong()) {
				idsToPreFetch.add(next.getId());
			}
		}
		for (IIdType next : idsToPreResolve) {
			if (!foundIds.contains(next.toUnqualifiedVersionless().getValue())) {
				theTransactionDetails.addResolvedResourceId(next.toUnqualifiedVersionless(), null);
			}
		}
	}

	private void preFetchConditionalUrls(TransactionDetails theTransactionDetails, List<IBase> theEntries, ITransactionProcessorVersionAdapter theVersionAdapter, RequestPartitionId theRequestPartitionId, List<Long> idsToPreFetch) {
		List<MatchUrlToResolve> searchParameterMapsToResolve = new ArrayList<>();
		for (IBase nextEntry : theEntries) {
			IBaseResource resource = theVersionAdapter.getResource(nextEntry);
			if (resource != null) {
				String verb = theVersionAdapter.getEntryRequestVerb(myFhirContext, nextEntry);
				String requestUrl = theVersionAdapter.getEntryRequestUrl(nextEntry);
				String requestIfNoneExist = theVersionAdapter.getEntryIfNoneExist(nextEntry);
				String resourceType = myFhirContext.getResourceType(resource);
				if (("PUT".equals(verb) || "PATCH".equals(verb)) && requestUrl != null && requestUrl.contains("?")) {
					preFetchConditionalUrl(idsToPreFetch, searchParameterMapsToResolve, resource, requestUrl, resourceType, true);
				} else if ("POST".equals(verb) && requestIfNoneExist != null && requestIfNoneExist.contains("?")) {
					preFetchConditionalUrl(idsToPreFetch, searchParameterMapsToResolve, resource, requestIfNoneExist, resourceType, false);
				}

				if (myStorageSettings.isAllowInlineMatchUrlReferences()) {
					List<ResourceReferenceInfo> references = myFhirContext.newTerser().getAllResourceReferences(resource);
					for (ResourceReferenceInfo next : references) {
						String referenceUrl = next.getResourceReference().getReferenceElement().getValue();
						if (referenceUrl != null && !referenceUrl.startsWith("urn:")) {
							int qmIndex = referenceUrl.indexOf("?");
							if (qmIndex != -1 && qmIndex < referenceUrl.length() - 1) {
								String urlResourceType = referenceUrl.substring(0, qmIndex);
								if (isBlank(urlResourceType) || myDaoRegistry.isResourceTypeSupported(urlResourceType)) {
									preFetchConditionalUrl(idsToPreFetch, searchParameterMapsToResolve, resource, referenceUrl, urlResourceType, false);
								}
							}
						}
					}
				}
			}
		}

		new QueryChunker<MatchUrlToResolve>()
			.chunk(searchParameterMapsToResolve, 100, map ->
				preFetchSearchParameterMaps(theTransactionDetails, theRequestPartitionId, map, idsToPreFetch));
	}

	/**
	 * @param theTransactionDetails    The active transaction details
	 * @param theRequestPartitionId    The active partition
	 * @param theInputParameters       These are the search parameter maps that will actually be resolved
	 * @param theOutputPidsToLoadFully This list will be added to with any resource PIDs that need to be fully
	 *                                 pre-loaded (ie. fetch the actual resource body since we're presumably
	 *                                 going to update it and will need to see its current state eventually
	 */
	private void preFetchSearchParameterMaps(TransactionDetails theTransactionDetails, RequestPartitionId theRequestPartitionId, List<MatchUrlToResolve> theInputParameters, List<Long> theOutputPidsToLoadFully) {
		CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceIndexedSearchParamToken> cq = cb.createQuery(ResourceIndexedSearchParamToken.class);
		Root<ResourceIndexedSearchParamToken> from = cq.from(ResourceIndexedSearchParamToken.class);

		Set<Long> sysAndValuePredicates = new HashSet<>();
		Set<Long> valuePredicates = new HashSet<>();
		for (MatchUrlToResolve next : theInputParameters) {
			Collection<List<List<IQueryParameterType>>> values = next.myMatchUrlSearchMap.values();
			if (values.size() == 1) {
				List<List<IQueryParameterType>> andList = values.iterator().next();
				IQueryParameterType param = andList.get(0).get(0);

				if (param instanceof TokenParam) {
					buildHashPredicateFromTokenParam((TokenParam) param, theRequestPartitionId, next, sysAndValuePredicates, valuePredicates);
				}
			}

		}

		List<Predicate> orPredicates = new ArrayList<>();
		if (!sysAndValuePredicates.isEmpty()) {
			Predicate predicate = from.get("myHashSystemAndValue").as(Long.class).in(sysAndValuePredicates);
			orPredicates.add(predicate);
		}
		if (!valuePredicates.isEmpty()) {
			Predicate predicate = from.get("myHashValue").as(Long.class).in(valuePredicates);
			orPredicates.add(predicate);
		}

		if (!orPredicates.isEmpty()) {
			Predicate masterPredicate = cb.or(orPredicates.toArray(new Predicate[0]));

			if (myPartitionSettings.isPartitioningEnabled() && !myPartitionSettings.isIncludePartitionInSearchHashes()) {
				if (theRequestPartitionId.isDefaultPartition()) {
					Predicate partitionIdCriteria = cb.isNull(from.get("myPartitionIdValue").as(Integer.class));
					masterPredicate = cb.and(partitionIdCriteria, masterPredicate);
				} else if (!theRequestPartitionId.isAllPartitions()) {
					Predicate partitionIdCriteria = from.get("myPartitionIdValue").as(Integer.class).in(theRequestPartitionId.getPartitionIds());
					masterPredicate = cb.and(partitionIdCriteria, masterPredicate);
				}
			}

			cq.where(masterPredicate);

			Map<Long, List<MatchUrlToResolve>> hashToSearchMap = buildHashToSearchMap(theInputParameters);

			TypedQuery<ResourceIndexedSearchParamToken> query = myEntityManager.createQuery(cq);
			List<ResourceIndexedSearchParamToken> results = query.getResultList();

			for (ResourceIndexedSearchParamToken nextResult : results) {
				Optional<List<MatchUrlToResolve>> matchedSearch = Optional.ofNullable(hashToSearchMap.get(nextResult.getHashSystemAndValue()));
				if (!matchedSearch.isPresent()) {
					matchedSearch = Optional.ofNullable(hashToSearchMap.get(nextResult.getHashValue()));
				}
				matchedSearch.ifPresent(matchUrlsToResolve -> {
					matchUrlsToResolve.forEach(matchUrl -> {
						setSearchToResolvedAndPrefetchFoundResourcePid(theTransactionDetails, theOutputPidsToLoadFully, nextResult, matchUrl);
					});
				});
			}
			//For each SP Map which did not return a result, tag it as not found.
			theInputParameters.stream()
				// No matches
				.filter(match -> !match.myResolved)
				.forEach(match -> {
					ourLog.debug("Was unable to match url {} from database", match.myRequestUrl);
					theTransactionDetails.addResolvedMatchUrl(match.myRequestUrl, TransactionDetails.NOT_FOUND);
				});
		}
	}

	private void preFetchConditionalUrl(List<Long> idsToPreFetch, List<MatchUrlToResolve> searchParameterMapsToResolve, IBaseResource resource, String requestUrl, String resourceType, boolean theShouldPreFetchResourceBody) {
		JpaPid cachedId = myMatchResourceUrlService.processMatchUrlUsingCacheOnly(resourceType, requestUrl);
		if (cachedId != null) {
			if (theShouldPreFetchResourceBody) {
				idsToPreFetch.add(cachedId.getId());
			}
		} else if (SINGLE_PARAMETER_MATCH_URL_PATTERN.matcher(requestUrl).matches()) {
			RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(resourceType);
			SearchParameterMap matchUrlSearchMap = myMatchUrlService.translateMatchUrl(requestUrl, resourceDefinition);
			searchParameterMapsToResolve.add(new MatchUrlToResolve(requestUrl, matchUrlSearchMap, resourceDefinition, theShouldPreFetchResourceBody));
		}
	}

	private RequestPartitionId getSinglePartitionForAllEntriesOrNull(RequestDetails theRequest, List<IBase> theEntries, ITransactionProcessorVersionAdapter versionAdapter) {
		RequestPartitionId retVal = null;
		Set<RequestPartitionId> requestPartitionIdsForAllEntries = new HashSet<>();
		for (IBase nextEntry : theEntries) {
			IBaseResource resource = versionAdapter.getResource(nextEntry);
			if (resource != null) {
				RequestPartitionId requestPartition = myRequestPartitionSvc.determineCreatePartitionForRequest(theRequest, resource, myFhirContext.getResourceType(resource));
				requestPartitionIdsForAllEntries.add(requestPartition);
			}
		}
		if (requestPartitionIdsForAllEntries.size() == 1) {
			retVal = requestPartitionIdsForAllEntries.iterator().next();
		}
		return retVal;
	}

	/**
	 * Given a token parameter, build the query predicate based on its hash. Uses system and value if both are available, otherwise just value.
	 * If neither are available, it returns null.
	 */
	@Nullable
	private void buildHashPredicateFromTokenParam(TokenParam theTokenParam, RequestPartitionId theRequestPartitionId, MatchUrlToResolve theMatchUrl, Set<Long> theSysAndValuePredicates, Set<Long> theValuePredicates) {
		if (isNotBlank(theTokenParam.getValue()) && isNotBlank(theTokenParam.getSystem())) {
			theMatchUrl.myHashSystemAndValue = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(myPartitionSettings, theRequestPartitionId, theMatchUrl.myResourceDefinition.getName(), theMatchUrl.myMatchUrlSearchMap.keySet().iterator().next(), theTokenParam.getSystem(), theTokenParam.getValue());
			theSysAndValuePredicates.add(theMatchUrl.myHashSystemAndValue);
		} else if (isNotBlank(theTokenParam.getValue())) {
			theMatchUrl.myHashValue = ResourceIndexedSearchParamToken.calculateHashValue(myPartitionSettings, theRequestPartitionId, theMatchUrl.myResourceDefinition.getName(), theMatchUrl.myMatchUrlSearchMap.keySet().iterator().next(), theTokenParam.getValue());
			theValuePredicates.add(theMatchUrl.myHashValue);
		}

	}

	private Map<Long, List<MatchUrlToResolve>> buildHashToSearchMap(List<MatchUrlToResolve> searchParameterMapsToResolve) {
		Map<Long, List<MatchUrlToResolve>> hashToSearch = new HashMap<>();
		//Build a lookup map so we don't have to iterate over the searches repeatedly.
		for (MatchUrlToResolve nextSearchParameterMap : searchParameterMapsToResolve) {
			if (nextSearchParameterMap.myHashSystemAndValue != null) {
				List<MatchUrlToResolve> matchUrlsToResolve = hashToSearch.getOrDefault(nextSearchParameterMap.myHashSystemAndValue, new ArrayList<>());
				matchUrlsToResolve.add(nextSearchParameterMap);
				hashToSearch.put(nextSearchParameterMap.myHashSystemAndValue, matchUrlsToResolve);
			}
			if (nextSearchParameterMap.myHashValue != null) {
				List<MatchUrlToResolve> matchUrlsToResolve = hashToSearch.getOrDefault(nextSearchParameterMap.myHashValue, new ArrayList<>());
				matchUrlsToResolve.add(nextSearchParameterMap);
				hashToSearch.put(nextSearchParameterMap.myHashValue, matchUrlsToResolve);
			}
		}
		return hashToSearch;
	}

	// FIXME: rename params, and idToPreFetch should be last with output and load body in name
	private void setSearchToResolvedAndPrefetchFoundResourcePid(TransactionDetails theTransactionDetails, List<Long> idsToPreFetch, ResourceIndexedSearchParamToken nextResult, MatchUrlToResolve nextSearchParameterMap) {
		ourLog.debug("Matched url {} from database", nextSearchParameterMap.myRequestUrl);
		if (nextSearchParameterMap.myShouldPreFetchResourceBody) {
			idsToPreFetch.add(nextResult.getResourcePid());
		}
		myMatchResourceUrlService.matchUrlResolved(theTransactionDetails, nextSearchParameterMap.myResourceDefinition.getName(), nextSearchParameterMap.myRequestUrl, JpaPid.fromId(nextResult.getResourcePid()));
		theTransactionDetails.addResolvedMatchUrl(nextSearchParameterMap.myRequestUrl, JpaPid.fromId(nextResult.getResourcePid()));
		nextSearchParameterMap.setResolved(true);
	}

	@Override
	protected void flushSession(Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome) {
		try {
			int insertionCount;
			int updateCount;
			SessionImpl session = myEntityManager.unwrap(SessionImpl.class);
			if (session != null) {
				insertionCount = session.getActionQueue().numberOfInsertions();
				updateCount = session.getActionQueue().numberOfUpdates();
			} else {
				insertionCount = -1;
				updateCount = -1;
			}

			StopWatch sw = new StopWatch();
			myEntityManager.flush();
			ourLog.debug("Session flush took {}ms for {} inserts and {} updates", sw.getMillis(), insertionCount, updateCount);
		} catch (PersistenceException e) {
			if (myHapiFhirHibernateJpaDialect != null) {
				List<String> types = theIdToPersistedOutcome.keySet().stream().filter(t -> t != null).map(t -> t.getResourceType()).collect(Collectors.toList());
				String message = "Error flushing transaction with resource types: " + types;
				throw myHapiFhirHibernateJpaDialect.translate(e, message);
			}
			throw e;
		}
	}

	@VisibleForTesting
	public void setPartitionSettingsForUnitTest(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	@VisibleForTesting
	public void setIdHelperServiceForUnitTest(IIdHelperService theIdHelperService) {
		myIdHelperService = theIdHelperService;
	}

	@VisibleForTesting
	public void setApplicationContextForUnitTest(ApplicationContext theAppCtx) {
		myApplicationContext = theAppCtx;
	}

	private static class MatchUrlToResolve {

		private final String myRequestUrl;
		private final SearchParameterMap myMatchUrlSearchMap;
		private final RuntimeResourceDefinition myResourceDefinition;
		private final boolean myShouldPreFetchResourceBody;
		public boolean myResolved;
		private Long myHashValue;
		private Long myHashSystemAndValue;

		public MatchUrlToResolve(String theRequestUrl, SearchParameterMap theMatchUrlSearchMap, RuntimeResourceDefinition theResourceDefinition, boolean theShouldPreFetchResourceBody) {
			myRequestUrl = theRequestUrl;
			myMatchUrlSearchMap = theMatchUrlSearchMap;
			myResourceDefinition = theResourceDefinition;
			myShouldPreFetchResourceBody = theShouldPreFetchResourceBody;
		}

		public void setResolved(boolean theResolved) {
			myResolved = theResolved;
		}
	}
}
