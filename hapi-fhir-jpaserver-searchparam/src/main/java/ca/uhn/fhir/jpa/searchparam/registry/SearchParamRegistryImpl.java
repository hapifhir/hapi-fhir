/*
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.ComboSearchParamType;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc;
import ca.uhn.fhir.jpa.cache.ResourceChangeResult;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.IndexedSearchParam;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.server.util.ISearchParamRegistry.isAllowedForContext;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamRegistryImpl
		implements ISearchParamRegistry, IResourceChangeListener, ISearchParamRegistryController {

	// Basic is needed by the R4 SubscriptionTopic registry
	public static final Set<String> NON_DISABLEABLE_SEARCH_PARAMS =
			Collections.unmodifiableSet(Sets.newHashSet("*:url", "Subscription:*", "SearchParameter:*", "Basic:*"));

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamRegistryImpl.class);
	public static final int MAX_MANAGED_PARAM_COUNT = 10000;
	private static final long REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;

	private JpaSearchParamCache myJpaSearchParamCache;

	@Autowired
	private StorageSettings myStorageSettings;

	@Autowired
	private ISearchParamProvider mySearchParamProvider;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private SearchParameterCanonicalizer mySearchParameterCanonicalizer;

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;

	@Autowired
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private ObjectProvider<ISearchParamIdentityCacheSvc> mySearchParamIdentityCacheSvcProvider;

	private IResourceChangeListenerCache myResourceChangeListenerCache;
	private volatile ReadOnlySearchParamCache myBuiltInSearchParams;
	private volatile IPhoneticEncoder myPhoneticEncoder;
	private volatile RuntimeSearchParamCache myActiveSearchParams;
	private boolean myPrePopulateSearchParamIdentities = true;

	@VisibleForTesting
	public void setPopulateSearchParamIdentities(boolean myPrePopulateSearchParamIdentities) {
		this.myPrePopulateSearchParamIdentities = myPrePopulateSearchParamIdentities;
	}

	/**
	 * Constructor
	 */
	public SearchParamRegistryImpl() {
		super();
	}

	@PostConstruct
	public void start() {
		myJpaSearchParamCache = new JpaSearchParamCache(myPartitionSettings);
	}

	@Override
	public RuntimeSearchParam getActiveSearchParam(
			@Nonnull String theResourceName,
			@Nonnull String theParamName,
			@Nonnull SearchParamLookupContextEnum theContext) {
		requiresActiveSearchParams();

		// Can still be null in unit test scenarios
		if (myActiveSearchParams != null) {
			RuntimeSearchParam param = myActiveSearchParams.get(theResourceName, theParamName);
			if (param != null) {
				if (isAllowedForContext(param, theContext)) {
					return param;
				}
			}
		}

		return null;
	}

	@Nonnull
	@Override
	public ResourceSearchParams getActiveSearchParams(
			@Nonnull String theResourceName, @Nonnull SearchParamLookupContextEnum theContext) {
		requiresActiveSearchParams();
		return getActiveSearchParams().getSearchParamMap(theResourceName).toFilteredForContext(theContext);
	}

	private void requiresActiveSearchParams() {
		if (myActiveSearchParams == null) {
			// forced refreshes should not use a cache - we're forcibly refreshing it, after all
			myResourceChangeListenerCache.forceRefresh();
		}
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(
			@Nonnull String theResourceName, @Nonnull SearchParamLookupContextEnum theContext) {
		return filteredForContext(myJpaSearchParamCache.getActiveComboSearchParams(theResourceName), theContext);
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(
			@Nonnull String theResourceName,
			@Nonnull ComboSearchParamType theParamType,
			@Nonnull SearchParamLookupContextEnum theContext) {
		return filteredForContext(
				myJpaSearchParamCache.getActiveComboSearchParams(theResourceName, theParamType), theContext);
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(
			@Nonnull String theResourceName,
			@Nonnull Set<String> theParamNames,
			@Nonnull SearchParamLookupContextEnum theContext) {
		return filteredForContext(
				myJpaSearchParamCache.getActiveComboSearchParams(theResourceName, theParamNames), theContext);
	}

	@Nullable
	@Override
	public RuntimeSearchParam getActiveSearchParamByUrl(
			@Nonnull String theUrl, @Nonnull SearchParamLookupContextEnum theContext) {
		if (myActiveSearchParams != null) {
			RuntimeSearchParam param = myActiveSearchParams.getByUrl(theUrl);
			if (isAllowedForContext(param, theContext)) {
				return param;
			}
		}
		return null;
	}

	@Override
	public Optional<RuntimeSearchParam> getActiveComboSearchParamById(
			@Nonnull String theResourceName, @Nonnull IIdType theId) {
		return myJpaSearchParamCache.getActiveComboSearchParamById(theResourceName, theId);
	}

	private void rebuildActiveSearchParams() {
		ourLog.info("Rebuilding SearchParamRegistry");
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(MAX_MANAGED_PARAM_COUNT);
		params.setCount(MAX_MANAGED_PARAM_COUNT);

		IBundleProvider allSearchParamsBp = mySearchParamProvider.search(params);

		List<IBaseResource> allSearchParams = allSearchParamsBp.getResources(0, MAX_MANAGED_PARAM_COUNT);
		Integer size = allSearchParamsBp.size();

		ourLog.trace("Loaded {} search params from the DB", allSearchParams.size());

		if (size == null) {
			ourLog.error(
					"Only {} search parameters have been loaded, but there are more than that in the repository.  Is offset search configured on this server?",
					allSearchParams.size());
		} else if (size >= MAX_MANAGED_PARAM_COUNT) {
			ourLog.warn("Unable to support >" + MAX_MANAGED_PARAM_COUNT + " search params!");
		}

		initializeActiveSearchParams(allSearchParams);
	}

	private void initializeActiveSearchParams(Collection<IBaseResource> theJpaSearchParams) {
		StopWatch sw = new StopWatch();

		ReadOnlySearchParamCache builtInSearchParams = getBuiltInSearchParams();
		RuntimeSearchParamCache searchParams =
				RuntimeSearchParamCache.fromReadOnlySearchParamCache(builtInSearchParams);
		long overriddenCount = overrideBuiltinSearchParamsWithActiveJpaSearchParams(searchParams, theJpaSearchParams);
		ourLog.trace("Have overridden {} built-in search parameters", overriddenCount);
		removeInactiveSearchParams(searchParams);

		/*
		 * The _language SearchParameter is a weird exception - It is actually just a normal
		 * token SP, but we explcitly ban SPs from registering themselves with a prefix
		 * of "_" since that's system reserved so we put this one behind a settings toggle
		 */
		if (myStorageSettings.isLanguageSearchParameterEnabled()) {
			IIdType id = myFhirContext.getVersion().newIdType();
			id.setValue("SearchParameter/Resource-language");
			RuntimeSearchParam sp = new RuntimeSearchParam(
					id,
					"http://hl7.org/fhir/SearchParameter/Resource-language",
					Constants.PARAM_LANGUAGE,
					"Language of the resource content",
					"language",
					RestSearchParameterTypeEnum.TOKEN,
					Collections.emptySet(),
					Collections.emptySet(),
					RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE,
					myFhirContext.getResourceTypes());
			for (String baseResourceType : sp.getBase()) {
				searchParams.add(baseResourceType, sp.getName(), sp);
			}
		}

		setActiveSearchParams(searchParams);

		myJpaSearchParamCache.populateActiveSearchParams(
				myInterceptorBroadcaster, myPhoneticEncoder, myActiveSearchParams);
		updateSearchParameterIdentityCache();
		ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
	}

	private void updateSearchParameterIdentityCache() {
		if (!myPrePopulateSearchParamIdentities) {
			return;
		}

		ISearchParamIdentityCacheSvc spIdentityCacheSvc = mySearchParamIdentityCacheSvcProvider.getIfAvailable();
		if (spIdentityCacheSvc == null) {
			return;
		}

		myJpaSearchParamCache
				.getHashIdentityToIndexedSearchParamMap()
				.forEach((hash, param) -> spIdentityCacheSvc.findOrCreateSearchParamIdentity(
						hash, param.getResourceType(), param.getParameterName()));
	}

	@VisibleForTesting
	public Map<Long, IndexedSearchParam> getHashIdentityToIndexedSearchParamMap() {
		return myJpaSearchParamCache.getHashIdentityToIndexedSearchParamMap();
	}

	@VisibleForTesting
	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	private ReadOnlySearchParamCache getBuiltInSearchParams() {
		if (myBuiltInSearchParams == null) {
			if (myStorageSettings.isAutoSupportDefaultSearchParams()) {
				myBuiltInSearchParams =
						ReadOnlySearchParamCache.fromFhirContext(myFhirContext, mySearchParameterCanonicalizer);
			} else {
				// Only the built-in search params that can not be disabled will be supported automatically
				myBuiltInSearchParams = ReadOnlySearchParamCache.fromFhirContext(
						myFhirContext, mySearchParameterCanonicalizer, NON_DISABLEABLE_SEARCH_PARAMS);
			}
		}
		return myBuiltInSearchParams;
	}

	private void removeInactiveSearchParams(RuntimeSearchParamCache theSearchParams) {
		for (String resourceName : theSearchParams.getResourceNameKeys()) {
			ResourceSearchParams resourceSearchParams = theSearchParams.getSearchParamMap(resourceName);
			resourceSearchParams.removeInactive();
		}
	}

	@VisibleForTesting
	public void setStorageSettings(StorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	private long overrideBuiltinSearchParamsWithActiveJpaSearchParams(
			RuntimeSearchParamCache theSearchParamCache, Collection<IBaseResource> theSearchParams) {
		if (!myStorageSettings.isDefaultSearchParamsCanBeOverridden() || theSearchParams == null) {
			return 0;
		}

		long retval = 0;
		for (IBaseResource searchParam : theSearchParams) {
			retval += overrideSearchParam(theSearchParamCache, searchParam);
		}
		return retval;
	}

	private long overrideSearchParam(RuntimeSearchParamCache theSearchParams, IBaseResource theSearchParameter) {
		if (theSearchParameter == null) {
			return 0;
		}

		RuntimeSearchParam runtimeSp = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theSearchParameter);
		if (runtimeSp == null) {
			return 0;
		}
		if (runtimeSp.getStatus() == RuntimeSearchParam.RuntimeSearchParamStatusEnum.DRAFT) {
			return 0;
		}

		long retval = 0;
		for (String nextBaseName : SearchParameterUtil.getBaseAsStrings(myFhirContext, theSearchParameter)) {
			if (isBlank(nextBaseName)) {
				continue;
			}

			String name = runtimeSp.getName();

			theSearchParams.add(nextBaseName, name, runtimeSp);
			ourLog.debug(
					"Adding search parameter {}.{} to SearchParamRegistry",
					nextBaseName,
					Objects.toString(name, "[composite]"));
			retval++;
		}
		return retval;
	}

	@Override
	public void requestRefresh() {
		myResourceChangeListenerCache.requestRefresh();
	}

	@Override
	public void forceRefresh() {
		RuntimeSearchParamCache activeSearchParams = myActiveSearchParams;
		myResourceChangeListenerCache.forceRefresh();

		// If the refresh didn't trigger a change, proceed with one anyway
		if (myActiveSearchParams == activeSearchParams) {
			rebuildActiveSearchParams();
		}
	}

	@Override
	public ResourceChangeResult refreshCacheIfNecessary() {
		return myResourceChangeListenerCache.refreshCacheIfNecessary();
	}

	@VisibleForTesting
	public void setResourceChangeListenerRegistry(IResourceChangeListenerRegistry theResourceChangeListenerRegistry) {
		myResourceChangeListenerRegistry = theResourceChangeListenerRegistry;
	}

	/**
	 * There is a circular reference between this class and the ResourceChangeListenerRegistry:
	 * SearchParamRegistryImpl -> ResourceChangeListenerRegistry -> InMemoryResourceMatcher -> SearchParamRegistryImpl. Since we only need this once on boot-up, we delay
	 * until ContextRefreshedEvent.
	 */
	@PostConstruct
	public void registerListener() {
		SearchParameterMap spMap = SearchParameterMap.newSynchronous();
		spMap.setLoadSynchronousUpTo(MAX_MANAGED_PARAM_COUNT);
		myResourceChangeListenerCache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener(
				"SearchParameter", spMap, this, REFRESH_INTERVAL);
	}

	@PreDestroy
	public void unregisterListener() {
		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(this);
	}

	public ReadOnlySearchParamCache getActiveSearchParams() {
		requiresActiveSearchParams();
		if (myActiveSearchParams == null) {
			throw new IllegalStateException(Msg.code(511) + "SearchParamRegistry has not been initialized");
		}
		return ReadOnlySearchParamCache.fromRuntimeSearchParamCache(myActiveSearchParams);
	}

	@VisibleForTesting
	public void setActiveSearchParams(RuntimeSearchParamCache theSearchParams) {
		myActiveSearchParams = theSearchParams;
	}

	/**
	 * All SearchParameters with the name "phonetic" encode the normalized index value using this phonetic encoder.
	 *
	 * @since 5.1.0
	 */
	@Override
	public void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
		myPhoneticEncoder = thePhoneticEncoder;

		if (myActiveSearchParams == null) {
			return;
		}
		myActiveSearchParams
				.getSearchParamStream()
				.forEach(searchParam -> myJpaSearchParamCache.setPhoneticEncoder(myPhoneticEncoder, searchParam));
	}

	@Override
	public void handleChange(IResourceChangeEvent theResourceChangeEvent) {
		if (theResourceChangeEvent.isEmpty()) {
			return;
		}

		ResourceChangeResult result = ResourceChangeResult.fromResourceChangeEvent(theResourceChangeEvent);
		if (result.created > 0) {
			ourLog.info(
					"Adding {} search parameters to SearchParamRegistry: {}",
					result.created,
					unqualified(theResourceChangeEvent.getCreatedResourceIds()));
		}
		if (result.updated > 0) {
			ourLog.info(
					"Updating {} search parameters in SearchParamRegistry: {}",
					result.updated,
					unqualified(theResourceChangeEvent.getUpdatedResourceIds()));
		}
		if (result.deleted > 0) {
			ourLog.info(
					"Deleting {} search parameters from SearchParamRegistry: {}",
					result.deleted,
					unqualified(theResourceChangeEvent.getDeletedResourceIds()));
		}
		rebuildActiveSearchParams();
	}

	private String unqualified(List<IIdType> theIds) {
		Iterator<String> unqualifiedIds = theIds.stream()
				.map(IIdType::toUnqualifiedVersionless)
				.map(IIdType::getValue)
				.iterator();

		return StringUtils.join(unqualifiedIds, ", ");
	}

	@Override
	public void handleInit(Collection<IIdType> theResourceIds) {
		List<IBaseResource> searchParams = new ArrayList<>();
		for (IIdType id : theResourceIds) {
			try {
				IBaseResource searchParam = mySearchParamProvider.read(id);
				searchParams.add(searchParam);
			} catch (ResourceNotFoundException e) {
				ourLog.warn("SearchParameter {} not found.  Excluding from list of active search params.", id);
			}
		}
		initializeActiveSearchParams(searchParams);
	}

	@Override
	public boolean isInitialized() {
		return myActiveSearchParams != null;
	}

	@VisibleForTesting
	public void resetForUnitTest() {
		myBuiltInSearchParams = null;
		setActiveSearchParams(null);
		handleInit(Collections.emptyList());
	}

	@VisibleForTesting
	public void setSearchParameterCanonicalizerForUnitTest(
			SearchParameterCanonicalizer theSearchParameterCanonicalizerForUnitTest) {
		mySearchParameterCanonicalizer = theSearchParameterCanonicalizerForUnitTest;
	}

	private static List<RuntimeSearchParam> filteredForContext(
			List<RuntimeSearchParam> theActiveComboSearchParams, SearchParamLookupContextEnum theContext) {
		return theActiveComboSearchParams.stream()
				.filter(t -> isAllowedForContext(t, theContext))
				.collect(Collectors.toList());
	}
}
