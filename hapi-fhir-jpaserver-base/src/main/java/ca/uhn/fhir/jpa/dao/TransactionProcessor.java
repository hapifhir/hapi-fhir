/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TaskChunker;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.UrlUtil.determineResourceTypeInResourceUrl;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TransactionProcessor extends BaseTransactionProcessor {

	public static final Pattern SINGLE_PARAMETER_MATCH_URL_PATTERN = Pattern.compile("^[^?]+[?][a-z0-9-]+=[^&,]+$");
	private static final Logger ourLog = LoggerFactory.getLogger(TransactionProcessor.class);
	public static final int CONDITIONAL_URL_FETCH_CHUNK_SIZE = 100;

	@Autowired
	private ApplicationContext myApplicationContext;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Autowired(required = false)
	private HapiFhirHibernateJpaDialect myHapiFhirHibernateJpaDialect;

	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private MatchResourceUrlService<JpaPid> myMatchResourceUrlService;

	@Autowired
	private MatchUrlService myMatchUrlService;

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
	public void setStorageSettings(StorageSettings theStorageSettings) {
		myStorageSettings = (JpaStorageSettings) theStorageSettings;
		super.setStorageSettings(theStorageSettings);
	}

	@Override
	protected EntriesToProcessMap doTransactionWriteOperations(
			final RequestDetails theRequest,
			String theActionName,
			TransactionDetails theTransactionDetails,
			Set<IIdType> theAllIds,
			IdSubstitutionMap theIdSubstitutions,
			Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome,
			IBaseBundle theResponse,
			IdentityHashMap<IBase, Integer> theOriginalRequestOrder,
			List<IBase> theEntries,
			StopWatch theTransactionStopWatch) {

		/*
		 * We temporarily set the flush mode for the duration of the DB transaction
		 * from the default of AUTO to the temporary value of COMMIT here. We do this
		 * because in AUTO mode, if any SQL SELECTs are required during the
		 * processing of an individual transaction entry, the server will flush the
		 * pending INSERTs/UPDATEs to the database before executing the SELECT.
		 * This hurts performance since we don't get the benefit of batching those
		 * write operations as much as possible. The tradeoff here is that we
		 * could theoretically have transaction operations which try to read
		 * data previously written in the same transaction, and they won't see it.
		 * This shouldn't actually be an issue anyhow - we pre-fetch conditional
		 * URLs and reference targets at the start of the transaction. But this
		 * tradeoff still feels worth it, since the most common use of transactions
		 * is for fast writing of data.
		 *
		 * Note that it's probably not necessary to reset it back, it should
		 * automatically go back to the default value after the transaction but
		 * we reset it just to be safe.
		 */
		FlushModeType flushMode = FlushModeType.COMMIT;
		for (IBase entry : theEntries) {
			IBaseResource res = myVersionAdapter.getResource(entry);
			if (res != null) {
				String type = myFhirContext.getResourceType(res);
				// These types write additional tables during the entity write
				if ("ValueSet".equals(type)
						|| "CodeSystem".equals(type)
						|| "Subscription".equals(type)
						|| "ConceptMap".equals(type)) {
					flushMode = FlushModeType.AUTO;
					break;
				}
			}
		}

		FlushModeType initialFlushMode = myEntityManager.getFlushMode();
		try {
			myEntityManager.setFlushMode(flushMode);

			ITransactionProcessorVersionAdapter<?, ?> versionAdapter = getVersionAdapter();
			RequestPartitionId requestPartitionId =
					super.determineRequestPartitionIdForWriteEntries(theRequest, theEntries);

			if (requestPartitionId != null) {
				preFetch(theTransactionDetails, theEntries, versionAdapter, requestPartitionId);
			}

			return super.doTransactionWriteOperations(
					theRequest,
					theActionName,
					theTransactionDetails,
					theAllIds,
					theIdSubstitutions,
					theIdToPersistedOutcome,
					theResponse,
					theOriginalRequestOrder,
					theEntries,
					theTransactionStopWatch);
		} finally {
			myEntityManager.setFlushMode(initialFlushMode);
		}
	}

	private void preFetch(
			TransactionDetails theTransactionDetails,
			List<IBase> theEntries,
			ITransactionProcessorVersionAdapter theVersionAdapter,
			RequestPartitionId theRequestPartitionId) {
		Set<String> foundIds = new HashSet<>();
		List<Long> idsToPreFetch = new ArrayList<>();

		/*
		 * Pre-Fetch any resources that are referred to normally by ID, e.g.
		 * regular FHIR updates within the transaction.
		 */
		preFetchResourcesById(
				theTransactionDetails, theEntries, theVersionAdapter, theRequestPartitionId, foundIds, idsToPreFetch);

		/*
		 * Pre-resolve any conditional URLs we can
		 */
		preFetchConditionalUrls(
				theTransactionDetails, theEntries, theVersionAdapter, theRequestPartitionId, idsToPreFetch);

		IFhirSystemDao<?, ?> systemDao = myApplicationContext.getBean(IFhirSystemDao.class);
		systemDao.preFetchResources(JpaPid.fromLongList(idsToPreFetch), true);
	}

	private void preFetchResourcesById(
			TransactionDetails theTransactionDetails,
			List<IBase> theEntries,
			ITransactionProcessorVersionAdapter theVersionAdapter,
			RequestPartitionId theRequestPartitionId,
			Set<String> foundIds,
			List<Long> idsToPreFetch) {

		FhirTerser terser = myFhirContext.newTerser();

		Set<IIdType> idsToPreResolve = new HashSet<>();
		Set<IIdType> idsToPreResolveIdOnly = new HashSet<>();

		for (IBase nextEntry : theEntries) {
			IBaseResource resource = theVersionAdapter.getResource(nextEntry);
			if (resource != null) {
				String verb = theVersionAdapter.getEntryRequestVerb(myFhirContext, nextEntry);

				/*
				 * Pre-fetch any resources that are potentially being directly updated by ID
				 */
				if ("PUT".equals(verb) || "PATCH".equals(verb)) {
					String requestUrl = theVersionAdapter.getEntryRequestUrl(nextEntry);
					if (countMatches(requestUrl, '?') == 0) {
						IIdType id = myFhirContext.getVersion().newIdType();
						id.setValue(requestUrl);
						IIdType unqualifiedVersionless = id.toUnqualifiedVersionless();
						idsToPreResolve.add(unqualifiedVersionless);
					}
				}

				/*
				 * Pre-fetch any resources that are referred to directly by ID (don't replace
				 * the TRUE flag with FALSE in case we're updating a resource but also
				 * pointing to that resource elsewhere in the bundle)
				 */
				if ("PUT".equals(verb) || "POST".equals(verb)) {
					for (ResourceReferenceInfo referenceInfo : terser.getAllResourceReferences(resource)) {
						IIdType reference = referenceInfo.getResourceReference().getReferenceElement();
						if (reference != null
								&& !reference.isLocal()
								&& !reference.isUuid()
								&& reference.hasResourceType()
								&& reference.hasIdPart()
								&& !reference.getValue().contains("?")) {
							idsToPreResolveIdOnly.add(reference.toUnqualifiedVersionless());
						}
					}
				}
			}
		}

		idsToPreResolveIdOnly.removeAll(idsToPreResolve);

		/*
		 * Pre-resolve any IDs that we'll need to prefetch entirely (this applies to
		 * resources that are being updated by the transaction itself, so we need to
		 * load everything about the resource)
		 */
		List<JpaPid> outcome = myIdHelperService.resolveResourcePersistentIdsWithCache(
				theRequestPartitionId, List.copyOf(idsToPreResolve));
		for (JpaPid next : outcome) {
			foundIds.add(
					next.getAssociatedResourceId().toUnqualifiedVersionless().getValue());
			theTransactionDetails.addResolvedResourceId(next.getAssociatedResourceId(), next);
			if (myStorageSettings.getResourceClientIdStrategy() != JpaStorageSettings.ClientIdStrategyEnum.ANY
					|| !next.getAssociatedResourceId().isIdPartValidLong()) {
				idsToPreFetch.add(next.getId());
			}
		}
		for (IIdType next : idsToPreResolve) {
			if (!foundIds.contains(next.toUnqualifiedVersionless().getValue())) {
				theTransactionDetails.addResolvedResourceId(next.toUnqualifiedVersionless(), null);
			}
		}

		/*
		 * Pre-resolve any IDs that are references to other resources outside of the
		 * transaction bundle. These ones don't need to be fully loaded since we're not
		 * updating them, we just need to verify that they exist and aren't deleted
		 * so that we know we can link against them. Doing this here means that
		 * we're not resolving them one-by-one.
		 */
		ListMultimap<String, String> typeToIds =
				MultimapBuilder.hashKeys().arrayListValues().build();
		for (IIdType next : idsToPreResolveIdOnly) {
			typeToIds.put(next.getResourceType(), next.getIdPart());
		}
		for (String resourceType : typeToIds.keySet()) {
			List<String> ids = typeToIds.get(resourceType);
			try {
				Map<String, JpaPid> resolvedIds =
						myIdHelperService.resolveResourcePersistentIds(theRequestPartitionId, resourceType, ids, true);
				for (Map.Entry<String, JpaPid> entries : resolvedIds.entrySet()) {
					IIdType id = myFhirContext.getVersion().newIdType();
					id.setValue(resourceType + "/" + entries.getKey());
					JpaPid pid = entries.getValue();
					pid.setAssociatedResourceId(id);
					theTransactionDetails.addResolvedResourceId(id, pid);
				}
			} catch (ResourceNotFoundException e) {
				ourLog.debug("Ignoring not found exception, it will be surfaced later in the processing", e);
			}
		}
	}

	@Override
	protected void handleVerbChangeInTransactionWriteOperations() {
		super.handleVerbChangeInTransactionWriteOperations();

		myEntityManager.flush();
	}

	private void preFetchConditionalUrls(
			TransactionDetails theTransactionDetails,
			List<IBase> theEntries,
			ITransactionProcessorVersionAdapter theVersionAdapter,
			RequestPartitionId theRequestPartitionId,
			List<Long> idsToPreFetch) {
		List<MatchUrlToResolve> searchParameterMapsToResolve = new ArrayList<>();
		for (IBase nextEntry : theEntries) {
			IBaseResource resource = theVersionAdapter.getResource(nextEntry);
			if (resource != null) {
				String verb = theVersionAdapter.getEntryRequestVerb(myFhirContext, nextEntry);
				String requestUrl = theVersionAdapter.getEntryRequestUrl(nextEntry);
				String requestIfNoneExist = theVersionAdapter.getEntryIfNoneExist(nextEntry);
				String resourceType = determineResourceTypeInResourceUrl(myFhirContext, requestUrl);
				if (resourceType == null && resource != null) {
					resourceType = myFhirContext.getResourceType(resource);
				}
				if (("PUT".equals(verb) || "PATCH".equals(verb)) && requestUrl != null && requestUrl.contains("?")) {
					preFetchConditionalUrl(resourceType, requestUrl, true, idsToPreFetch, searchParameterMapsToResolve);
				} else if ("POST".equals(verb) && requestIfNoneExist != null && requestIfNoneExist.contains("?")) {
					preFetchConditionalUrl(
							resourceType, requestIfNoneExist, false, idsToPreFetch, searchParameterMapsToResolve);
				}

				if (myStorageSettings.isAllowInlineMatchUrlReferences()) {
					List<ResourceReferenceInfo> references =
							myFhirContext.newTerser().getAllResourceReferences(resource);
					for (ResourceReferenceInfo next : references) {
						String referenceUrl = next.getResourceReference()
								.getReferenceElement()
								.getValue();
						String refResourceType = determineResourceTypeInResourceUrl(myFhirContext, referenceUrl);
						if (refResourceType != null) {
							preFetchConditionalUrl(
									refResourceType, referenceUrl, false, idsToPreFetch, searchParameterMapsToResolve);
						}
					}
				}
			}
		}

		new TaskChunker<MatchUrlToResolve>()
				.chunk(
						searchParameterMapsToResolve,
						CONDITIONAL_URL_FETCH_CHUNK_SIZE,
						map -> preFetchSearchParameterMaps(
								theTransactionDetails, theRequestPartitionId, map, idsToPreFetch));
	}

	/**
	 * @param theTransactionDetails    The active transaction details
	 * @param theRequestPartitionId    The active partition
	 * @param theInputParameters       These are the search parameter maps that will actually be resolved
	 * @param theOutputPidsToLoadFully This list will be added to with any resource PIDs that need to be fully
	 *                                 pre-loaded (ie. fetch the actual resource body since we're presumably
	 *                                 going to update it and will need to see its current state eventually)
	 */
	private void preFetchSearchParameterMaps(
			TransactionDetails theTransactionDetails,
			RequestPartitionId theRequestPartitionId,
			List<MatchUrlToResolve> theInputParameters,
			List<Long> theOutputPidsToLoadFully) {
		Set<Long> systemAndValueHashes = new HashSet<>();
		Set<Long> valueHashes = new HashSet<>();
		for (MatchUrlToResolve next : theInputParameters) {
			Collection<List<List<IQueryParameterType>>> values = next.myMatchUrlSearchMap.values();
			if (values.size() == 1) {
				List<List<IQueryParameterType>> andList = values.iterator().next();
				IQueryParameterType param = andList.get(0).get(0);

				if (param instanceof TokenParam) {
					buildHashPredicateFromTokenParam(
							(TokenParam) param, theRequestPartitionId, next, systemAndValueHashes, valueHashes);
				}
			}
		}

		preFetchSearchParameterMapsToken(
				"myHashSystemAndValue",
				systemAndValueHashes,
				theTransactionDetails,
				theRequestPartitionId,
				theInputParameters,
				theOutputPidsToLoadFully);
		preFetchSearchParameterMapsToken(
				"myHashValue",
				valueHashes,
				theTransactionDetails,
				theRequestPartitionId,
				theInputParameters,
				theOutputPidsToLoadFully);

		// For each SP Map which did not return a result, tag it as not found.
		if (!valueHashes.isEmpty() || !systemAndValueHashes.isEmpty()) {
			theInputParameters.stream()
					// No matches
					.filter(match -> !match.myResolved)
					.forEach(match -> {
						ourLog.debug("Was unable to match url {} from database", match.myRequestUrl);
						theTransactionDetails.addResolvedMatchUrl(
								myFhirContext, match.myRequestUrl, TransactionDetails.NOT_FOUND);
					});
		}
	}

	/**
	 * Here we do a select against the {@link ResourceIndexedSearchParamToken} table for any rows that have the
	 * specific sys+val or val hashes we know we need to pre-fetch.
	 * <p>
	 * Note that we do a tuple query for only 2 columns in order to ensure that we can get by with only
	 * the data in the index (ie no need to load the actual table rows).
	 */
	private void preFetchSearchParameterMapsToken(
			String theIndexColumnName,
			Set<Long> theHashesForIndexColumn,
			TransactionDetails theTransactionDetails,
			RequestPartitionId theRequestPartitionId,
			List<MatchUrlToResolve> theInputParameters,
			List<Long> theOutputPidsToLoadFully) {
		if (!theHashesForIndexColumn.isEmpty()) {
			ListMultimap<Long, MatchUrlToResolve> hashToSearchMap =
					buildHashToSearchMap(theInputParameters, theIndexColumnName);
			CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = cb.createTupleQuery();
			Root<ResourceIndexedSearchParamToken> from = cq.from(ResourceIndexedSearchParamToken.class);
			cq.multiselect(from.get("myResourcePid"), from.get(theIndexColumnName));

			Predicate masterPredicate;
			if (theHashesForIndexColumn.size() == 1) {
				masterPredicate = cb.equal(
						from.get(theIndexColumnName),
						theHashesForIndexColumn.iterator().next());
			} else {
				masterPredicate = from.get(theIndexColumnName).in(theHashesForIndexColumn);
			}

			if (myPartitionSettings.isPartitioningEnabled()
					&& !myPartitionSettings.isIncludePartitionInSearchHashes()) {
				if (theRequestPartitionId.isDefaultPartition()) {
					Predicate partitionIdCriteria = cb.isNull(from.get("myPartitionIdValue"));
					masterPredicate = cb.and(partitionIdCriteria, masterPredicate);
				} else if (!theRequestPartitionId.isAllPartitions()) {
					Predicate partitionIdCriteria =
							from.get("myPartitionIdValue").in(theRequestPartitionId.getPartitionIds());
					masterPredicate = cb.and(partitionIdCriteria, masterPredicate);
				}
			}

			cq.where(masterPredicate);

			TypedQuery<Tuple> query = myEntityManager.createQuery(cq);

			/*
			 * If we have 10 unique conditional URLs we're resolving, each one should
			 * resolve to 0..1 resources if they are valid as conditional URLs. So we would
			 * expect this query to return 0..10 rows, since conditional URLs for all
			 * conditional operations except DELETE (which isn't being applied here) are
			 * only allowed to resolve to 0..1 resources.
			 *
			 * If a conditional URL matches 2+ resources that is an error, and we'll
			 * be throwing an exception below. This limit is here for safety just to
			 * ensure that if someone uses a conditional URL that matches a million resources,
			 * we don't do a super-expensive fetch.
			 */
			query.setMaxResults(theHashesForIndexColumn.size() + 1);

			List<Tuple> results = query.getResultList();

			for (Tuple nextResult : results) {
				Long nextResourcePid = nextResult.get(0, Long.class);
				Long nextHash = nextResult.get(1, Long.class);
				List<MatchUrlToResolve> matchedSearch = hashToSearchMap.get(nextHash);
				matchedSearch.forEach(matchUrl -> {
					ourLog.debug("Matched url {} from database", matchUrl.myRequestUrl);
					if (matchUrl.myShouldPreFetchResourceBody) {
						theOutputPidsToLoadFully.add(nextResourcePid);
					}
					myMatchResourceUrlService.matchUrlResolved(
							theTransactionDetails,
							matchUrl.myResourceDefinition.getName(),
							matchUrl.myRequestUrl,
							JpaPid.fromId(nextResourcePid));
					theTransactionDetails.addResolvedMatchUrl(
							myFhirContext, matchUrl.myRequestUrl, JpaPid.fromId(nextResourcePid));
					matchUrl.setResolved(true);
				});
			}
		}
	}

	/**
	 * Note that if {@literal theShouldPreFetchResourceBody} is false, then we'll check if a given match
	 * URL resolves to a resource PID, but we won't actually try to load that resource. If we're resolving
	 * a match URL because it's there for a conditional update, we'll eagerly fetch the
	 * actual resource because we need to know its current state in order to update it. However, if
	 * the match URL is from an inline match URL in a resource body, we really only care about
	 * the PID and don't need the body so we don't load it. This does have a security implication, since
	 * it means that the {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_PRESHOW_RESOURCES} pointcut
	 * isn't fired even though the user has resolved the URL (meaning they may be able to test for
	 * the existence of a resource using a match URL). There is a test for this called
	 * {@literal testTransactionCreateInlineMatchUrlWithAuthorizationDenied()}. This security tradeoff
	 * is acceptable since we're only prefetching things with very simple match URLs (nothing with
	 * a reference in it for example) so it's not really possible to doing anything useful with this.
	 *
	 * @param theResourceType                       The resource type associated with the match URL (ie what resource type should it resolve to)
	 * @param theRequestUrl                         The actual match URL, which could be as simple as just parameters or could include the resource type too
	 * @param theShouldPreFetchResourceBody         Should we also fetch the actual resource body, or just figure out the PID associated with it. See the method javadoc above for some context.
	 * @param theOutputIdsToPreFetch                This will be populated with any resource PIDs that need to be pre-fetched
	 * @param theOutputSearchParameterMapsToResolve This will be populated with any {@link SearchParameterMap} instances corresponding to match URLs we need to resolve
	 */
	private void preFetchConditionalUrl(
			String theResourceType,
			String theRequestUrl,
			boolean theShouldPreFetchResourceBody,
			List<Long> theOutputIdsToPreFetch,
			List<MatchUrlToResolve> theOutputSearchParameterMapsToResolve) {
		JpaPid cachedId = myMatchResourceUrlService.processMatchUrlUsingCacheOnly(theResourceType, theRequestUrl);
		if (cachedId != null) {
			if (theShouldPreFetchResourceBody) {
				theOutputIdsToPreFetch.add(cachedId.getId());
			}
		} else if (SINGLE_PARAMETER_MATCH_URL_PATTERN.matcher(theRequestUrl).matches()) {
			RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResourceType);
			SearchParameterMap matchUrlSearchMap =
					myMatchUrlService.translateMatchUrl(theRequestUrl, resourceDefinition);
			theOutputSearchParameterMapsToResolve.add(new MatchUrlToResolve(
					theRequestUrl, matchUrlSearchMap, resourceDefinition, theShouldPreFetchResourceBody));
		}
	}

	/**
	 * Given a token parameter, build the query predicate based on its hash. Uses system and value if both are available, otherwise just value.
	 * If neither are available, it returns null.
	 */
	@Nullable
	private void buildHashPredicateFromTokenParam(
			TokenParam theTokenParam,
			RequestPartitionId theRequestPartitionId,
			MatchUrlToResolve theMatchUrl,
			Set<Long> theSysAndValuePredicates,
			Set<Long> theValuePredicates) {
		if (isNotBlank(theTokenParam.getValue()) && isNotBlank(theTokenParam.getSystem())) {
			theMatchUrl.myHashSystemAndValue = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
					myPartitionSettings,
					theRequestPartitionId,
					theMatchUrl.myResourceDefinition.getName(),
					theMatchUrl.myMatchUrlSearchMap.keySet().iterator().next(),
					theTokenParam.getSystem(),
					theTokenParam.getValue());
			theSysAndValuePredicates.add(theMatchUrl.myHashSystemAndValue);
		} else if (isNotBlank(theTokenParam.getValue())) {
			theMatchUrl.myHashValue = ResourceIndexedSearchParamToken.calculateHashValue(
					myPartitionSettings,
					theRequestPartitionId,
					theMatchUrl.myResourceDefinition.getName(),
					theMatchUrl.myMatchUrlSearchMap.keySet().iterator().next(),
					theTokenParam.getValue());
			theValuePredicates.add(theMatchUrl.myHashValue);
		}
	}

	private ListMultimap<Long, MatchUrlToResolve> buildHashToSearchMap(
			List<MatchUrlToResolve> searchParameterMapsToResolve, String theIndex) {
		ListMultimap<Long, MatchUrlToResolve> hashToSearch = ArrayListMultimap.create();
		// Build a lookup map so we don't have to iterate over the searches repeatedly.
		for (MatchUrlToResolve nextSearchParameterMap : searchParameterMapsToResolve) {
			if (nextSearchParameterMap.myHashSystemAndValue != null && theIndex.equals("myHashSystemAndValue")) {
				hashToSearch.put(nextSearchParameterMap.myHashSystemAndValue, nextSearchParameterMap);
			}
			if (nextSearchParameterMap.myHashValue != null && theIndex.equals("myHashValue")) {
				hashToSearch.put(nextSearchParameterMap.myHashValue, nextSearchParameterMap);
			}
		}
		return hashToSearch;
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
			ourLog.debug(
					"Session flush took {}ms for {} inserts and {} updates",
					sw.getMillis(),
					insertionCount,
					updateCount);
		} catch (PersistenceException e) {
			if (myHapiFhirHibernateJpaDialect != null) {
				List<String> types = theIdToPersistedOutcome.keySet().stream()
						.filter(t -> t != null)
						.map(t -> t.getResourceType())
						.collect(Collectors.toList());
				String message = "Error flushing transaction with resource types: " + types;
				throw myHapiFhirHibernateJpaDialect.translate(e, message);
			}
			throw e;
		}
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

		public MatchUrlToResolve(
				String theRequestUrl,
				SearchParameterMap theMatchUrlSearchMap,
				RuntimeResourceDefinition theResourceDefinition,
				boolean theShouldPreFetchResourceBody) {
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
