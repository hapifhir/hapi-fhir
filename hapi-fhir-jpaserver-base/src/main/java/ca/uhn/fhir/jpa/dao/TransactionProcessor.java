package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
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

import static ca.uhn.fhir.jpa.dao.index.IdHelperService.EMPTY_PREDICATE_ARRAY;
import static org.apache.commons.lang3.StringUtils.defaultString;
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
	private IIdHelperService myIdHelperService;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private MatchResourceUrlService myMatchResourceUrlService;
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
			Set<RequestPartitionId> requestPartitionIdsForAllEntries = new HashSet<>();
			for (IBase nextEntry : theEntries) {
				IBaseResource resource = versionAdapter.getResource(nextEntry);
				if (resource != null) {
					RequestPartitionId requestPartition = myRequestPartitionSvc.determineCreatePartitionForRequest(theRequest, resource, myFhirContext.getResourceType(resource));
					requestPartitionIdsForAllEntries.add(requestPartition);
				}
			}
			if (requestPartitionIdsForAllEntries.size() == 1) {
				requestPartitionId = requestPartitionIdsForAllEntries.iterator().next();
			}
		}

		if (requestPartitionId != null) {

			Set<String> foundIds = new HashSet<>();
			List<Long> idsToPreFetch = new ArrayList<>();

			/*
			 * Pre-Fetch any resources that are referred to normally by ID, e.g.
			 * regular FHIR updates within the transaction.
			 */
			List<IIdType> idsToPreResolve = new ArrayList<>();
			for (IBase nextEntry : theEntries) {
				IBaseResource resource = versionAdapter.getResource(nextEntry);
				if (resource != null) {
					String fullUrl = versionAdapter.getFullUrl(nextEntry);
					boolean isPlaceholder = defaultString(fullUrl).startsWith("urn:");
					if (!isPlaceholder) {
						if (resource.getIdElement().hasIdPart() && resource.getIdElement().hasResourceType()) {
							idsToPreResolve.add(resource.getIdElement());
						}
					}
				}
			}
			List<ResourcePersistentId> outcome = myIdHelperService.resolveResourcePersistentIdsWithCache(requestPartitionId, idsToPreResolve);
			for (ResourcePersistentId next : outcome) {
				foundIds.add(next.getAssociatedResourceId().toUnqualifiedVersionless().getValue());
				theTransactionDetails.addResolvedResourceId(next.getAssociatedResourceId(), next);
				if (myDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY || !next.getAssociatedResourceId().isIdPartValidLong()) {
					idsToPreFetch.add(next.getIdAsLong());
				}
			}
			for (IIdType next : idsToPreResolve) {
				if (!foundIds.contains(next.toUnqualifiedVersionless().getValue())) {
					theTransactionDetails.addResolvedResourceId(next.toUnqualifiedVersionless(), null);
				}
			}

			/*
			 * Pre-resolve any conditional URLs we can
			 */
			List<MatchUrlToResolve> searchParameterMapsToResolve = new ArrayList<>();
			for (IBase nextEntry : theEntries) {
				IBaseResource resource = versionAdapter.getResource(nextEntry);
				if (resource != null) {
					String verb = versionAdapter.getEntryRequestVerb(myFhirContext, nextEntry);
					String requestUrl = versionAdapter.getEntryRequestUrl(nextEntry);
					String requestIfNoneExist = versionAdapter.getEntryIfNoneExist(nextEntry);
					String resourceType = myFhirContext.getResourceType(resource);
					if ("PUT".equals(verb) && requestUrl != null && requestUrl.contains("?")) {
						ResourcePersistentId cachedId = myMatchResourceUrlService.processMatchUrlUsingCacheOnly(resourceType, requestUrl);
						if (cachedId != null) {
							idsToPreFetch.add(cachedId.getIdAsLong());
						} else if (SINGLE_PARAMETER_MATCH_URL_PATTERN.matcher(requestUrl).matches()) {
							RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(resource);
							SearchParameterMap matchUrlSearchMap = myMatchUrlService.translateMatchUrl(requestUrl, resourceDefinition);
							searchParameterMapsToResolve.add(new MatchUrlToResolve(requestUrl, matchUrlSearchMap, resourceDefinition));
						}
					} else if ("POST".equals(verb) && requestIfNoneExist != null && requestIfNoneExist.contains("?")) {
						ResourcePersistentId cachedId = myMatchResourceUrlService.processMatchUrlUsingCacheOnly(resourceType, requestIfNoneExist);
						if (cachedId != null) {
							idsToPreFetch.add(cachedId.getIdAsLong());
						} else if (SINGLE_PARAMETER_MATCH_URL_PATTERN.matcher(requestIfNoneExist).matches()) {
							RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(resource);
							SearchParameterMap matchUrlSearchMap = myMatchUrlService.translateMatchUrl(requestIfNoneExist, resourceDefinition);
							searchParameterMapsToResolve.add(new MatchUrlToResolve(requestIfNoneExist, matchUrlSearchMap, resourceDefinition));
						}
					}

				}
			}
			if (searchParameterMapsToResolve.size() > 0) {
				CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
				CriteriaQuery<ResourceIndexedSearchParamToken> cq = cb.createQuery(ResourceIndexedSearchParamToken.class);
				Root<ResourceIndexedSearchParamToken> from = cq.from(ResourceIndexedSearchParamToken.class);
				List<Predicate> orPredicates = new ArrayList<>();

				for (MatchUrlToResolve next : searchParameterMapsToResolve) {
					Collection<List<List<IQueryParameterType>>> values = next.myMatchUrlSearchMap.values();
					if (values.size() == 1) {
						List<List<IQueryParameterType>> andList = values.iterator().next();
						IQueryParameterType param = andList.get(0).get(0);

						if (param instanceof TokenParam) {
							Predicate hashPredicate = buildHashPredicateFromTokenParam((TokenParam)param, requestPartitionId, cb, from, next);

							if (hashPredicate != null) {
								if (myPartitionSettings.isPartitioningEnabled() && !myPartitionSettings.isIncludePartitionInSearchHashes()) {
									if (requestPartitionId.isDefaultPartition()) {
										Predicate partitionIdCriteria = cb.isNull(from.get("myPartitionIdValue").as(Integer.class));
										hashPredicate = cb.and(hashPredicate, partitionIdCriteria);
									} else if (!requestPartitionId.isAllPartitions()) {
										Predicate partitionIdCriteria = from.get("myPartitionIdValue").as(Integer.class).in(requestPartitionId.getPartitionIds());
										hashPredicate = cb.and(hashPredicate, partitionIdCriteria);
									}
								}

								orPredicates.add(hashPredicate);
							}
						}
					}

				}

				if (orPredicates.size() > 1) {
					cq.where(cb.or(orPredicates.toArray(EMPTY_PREDICATE_ARRAY)));

					Map<Long, List<MatchUrlToResolve>> hashToSearchMap = buildHashToSearchMap(searchParameterMapsToResolve);

					TypedQuery<ResourceIndexedSearchParamToken> query = myEntityManager.createQuery(cq);
					List<ResourceIndexedSearchParamToken> results = query.getResultList();

					for (ResourceIndexedSearchParamToken nextResult : results) {
						Optional<List<MatchUrlToResolve>> matchedSearch = Optional.ofNullable(hashToSearchMap.get(nextResult.getHashSystemAndValue()));
						if (!matchedSearch.isPresent()) {
							matchedSearch =  Optional.ofNullable(hashToSearchMap.get(nextResult.getHashValue()));
						}
						matchedSearch.ifPresent(matchUrlsToResolve -> {
							matchUrlsToResolve.forEach(matchUrl -> {
								setSearchToResolvedAndPrefetchFoundResourcePid(theTransactionDetails, idsToPreFetch, nextResult, matchUrl);
							});
						});
					}
					//For each SP Map which did not return a result, tag it as not found.
					searchParameterMapsToResolve.stream()
						// No matches
						.filter(match -> !match.myResolved)
						.forEach(match -> {
							ourLog.debug("Was unable to match url {} from database", match.myRequestUrl);
							theTransactionDetails.addResolvedMatchUrl(match.myRequestUrl, TransactionDetails.NOT_FOUND);
						});
				}
			}

			IFhirSystemDao<?,?> systemDao = myApplicationContext.getBean(IFhirSystemDao.class);
			systemDao.preFetchResources(ResourcePersistentId.fromLongList(idsToPreFetch));

		}

		return super.doTransactionWriteOperations(theRequest, theActionName, theTransactionDetails, theAllIds, theIdSubstitutions, theIdToPersistedOutcome, theResponse, theOriginalRequestOrder, theEntries, theTransactionStopWatch);
	}

	/**
	 * Given a token parameter, build the query predicate based on its hash. Uses system and value if both are available, otherwise just value.
	 * If neither are available, it returns null.
	 */
	@Nullable
	private Predicate buildHashPredicateFromTokenParam(TokenParam theTokenParam, RequestPartitionId theRequestPartitionId, CriteriaBuilder cb, Root<ResourceIndexedSearchParamToken> from, MatchUrlToResolve theMatchUrl) {
		Predicate hashPredicate = null;
		if (isNotBlank(theTokenParam.getValue()) && isNotBlank(theTokenParam.getSystem())) {
			theMatchUrl.myHashSystemAndValue = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(myPartitionSettings, theRequestPartitionId, theMatchUrl.myResourceDefinition.getName(), theMatchUrl.myMatchUrlSearchMap.keySet().iterator().next(), theTokenParam.getSystem(), theTokenParam.getValue());
			hashPredicate = cb.equal(from.get("myHashSystemAndValue").as(Long.class), theMatchUrl.myHashSystemAndValue);
		} else if (isNotBlank(theTokenParam.getValue())) {
			theMatchUrl.myHashValue = ResourceIndexedSearchParamToken.calculateHashValue(myPartitionSettings, theRequestPartitionId, theMatchUrl.myResourceDefinition.getName(), theMatchUrl.myMatchUrlSearchMap.keySet().iterator().next(), theTokenParam.getValue());
			hashPredicate = cb.equal(from.get("myHashValue").as(Long.class), theMatchUrl.myHashValue);
		}
		return hashPredicate;
	}

	private Map<Long, List<MatchUrlToResolve>> buildHashToSearchMap(List<MatchUrlToResolve> searchParameterMapsToResolve) {
		Map<Long, List<MatchUrlToResolve>> hashToSearch = new HashMap<>();
		//Build a lookup map so we don't have to iterate over the searches repeatedly.
		for (MatchUrlToResolve nextSearchParameterMap : searchParameterMapsToResolve) {
			if (nextSearchParameterMap.myHashSystemAndValue != null) {
				List<MatchUrlToResolve> matchUrlsToResolve = hashToSearch.getOrDefault(nextSearchParameterMap.myHashSystemAndValue, new ArrayList<>());
				matchUrlsToResolve.add(nextSearchParameterMap);
				hashToSearch.put(nextSearchParameterMap.myHashSystemAndValue,  matchUrlsToResolve);
			}
			if (nextSearchParameterMap.myHashValue!= null) {
				List<MatchUrlToResolve> matchUrlsToResolve = hashToSearch.getOrDefault(nextSearchParameterMap.myHashValue, new ArrayList<>());
				matchUrlsToResolve.add(nextSearchParameterMap);
				hashToSearch.put(nextSearchParameterMap.myHashValue,  matchUrlsToResolve);
			}
		}
		return hashToSearch;
	}

	private void setSearchToResolvedAndPrefetchFoundResourcePid(TransactionDetails theTransactionDetails, List<Long> idsToPreFetch, ResourceIndexedSearchParamToken nextResult, MatchUrlToResolve nextSearchParameterMap) {
		ourLog.debug("Matched url {} from database", nextSearchParameterMap.myRequestUrl);
		idsToPreFetch.add(nextResult.getResourcePid());
		myMatchResourceUrlService.matchUrlResolved(theTransactionDetails, nextSearchParameterMap.myResourceDefinition.getName(), nextSearchParameterMap.myRequestUrl, new ResourcePersistentId(nextResult.getResourcePid()));
		theTransactionDetails.addResolvedMatchUrl(nextSearchParameterMap.myRequestUrl, new ResourcePersistentId(nextResult.getResourcePid()));
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
		public boolean myResolved;
		private Long myHashValue;
		private Long myHashSystemAndValue;

		public MatchUrlToResolve(String theRequestUrl, SearchParameterMap theMatchUrlSearchMap, RuntimeResourceDefinition theResourceDefinition) {
			myRequestUrl = theRequestUrl;
			myMatchUrlSearchMap = theMatchUrlSearchMap;
			myResourceDefinition = theResourceDefinition;
		}
		public void setResolved(boolean theResolved) {
			myResolved = theResolved;
		}
	}
}
