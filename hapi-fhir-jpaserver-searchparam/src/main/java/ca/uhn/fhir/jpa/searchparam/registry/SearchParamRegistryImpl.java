package ca.uhn.fhir.jpa.searchparam.registry;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ResourceChangeResult;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamRegistryImpl implements ISearchParamRegistry, IResourceChangeListener, ISearchParamRegistryController {

	public static final Set<String> NON_DISABLEABLE_SEARCH_PARAMS = Collections.unmodifiableSet(Sets.newHashSet(
		"*:url",
		"Subscription:*",
		"SearchParameter:*"
	));

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamRegistryImpl.class);
	private static final int MAX_MANAGED_PARAM_COUNT = 10000;
	private static final long REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;

	private final JpaSearchParamCache myJpaSearchParamCache = new JpaSearchParamCache();
	@Autowired
	private ModelConfig myModelConfig;
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

	private IResourceChangeListenerCache myResourceChangeListenerCache;
	private volatile ReadOnlySearchParamCache myBuiltInSearchParams;
	private volatile IPhoneticEncoder myPhoneticEncoder;
	private volatile RuntimeSearchParamCache myActiveSearchParams;

	/**
	 * Constructor
	 */
	public SearchParamRegistryImpl() {
		super();
	}

	@Override
	public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
		requiresActiveSearchParams();

		// Can still be null in unit test scenarios
		if (myActiveSearchParams != null) {
			return myActiveSearchParams.get(theResourceName, theParamName);
		} else {
			return null;
		}
	}

	@Nonnull
	@Override
	public ResourceSearchParams getActiveSearchParams(String theResourceName) {
		requiresActiveSearchParams();
		return getActiveSearchParams().getSearchParamMap(theResourceName);
	}

	private void requiresActiveSearchParams() {
		if (myActiveSearchParams == null) {
			myResourceChangeListenerCache.forceRefresh();
		}
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName) {
		return myJpaSearchParamCache.getActiveComboSearchParams(theResourceName);
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName, Set<String> theParamNames) {
		return myJpaSearchParamCache.getActiveComboSearchParams(theResourceName, theParamNames);
	}

	@Nullable
	@Override
	public RuntimeSearchParam getActiveSearchParamByUrl(String theUrl) {
		if (myActiveSearchParams != null) {
			return myActiveSearchParams.getByUrl(theUrl);
		} else {
			return null;
		}
	}

	private void rebuildActiveSearchParams() {
		ourLog.info("Rebuilding SearchParamRegistry");
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(MAX_MANAGED_PARAM_COUNT);

		IBundleProvider allSearchParamsBp = mySearchParamProvider.search(params);

		List<IBaseResource> allSearchParams = allSearchParamsBp.getResources(0, MAX_MANAGED_PARAM_COUNT);
		int size = allSearchParamsBp.sizeOrThrowNpe();

		ourLog.trace("Loaded {} search params from the DB", size);

		// Just in case..
		if (size >= MAX_MANAGED_PARAM_COUNT) {
			ourLog.warn("Unable to support >" + MAX_MANAGED_PARAM_COUNT + " search params!");
		}

		initializeActiveSearchParams(allSearchParams);
	}

	private void initializeActiveSearchParams(Collection<IBaseResource> theJpaSearchParams) {
		StopWatch sw = new StopWatch();

		ReadOnlySearchParamCache builtInSearchParams = getBuiltInSearchParams();
		RuntimeSearchParamCache searchParams = RuntimeSearchParamCache.fromReadOnlySearchParamCache(builtInSearchParams);
		long overriddenCount = overrideBuiltinSearchParamsWithActiveJpaSearchParams(searchParams, theJpaSearchParams);
		ourLog.trace("Have overridden {} built-in search parameters", overriddenCount);
		removeInactiveSearchParams(searchParams);
		myActiveSearchParams = searchParams;

		myJpaSearchParamCache.populateActiveSearchParams(myInterceptorBroadcaster, myPhoneticEncoder, myActiveSearchParams);
		ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
	}

	@VisibleForTesting
	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	private ReadOnlySearchParamCache getBuiltInSearchParams() {
		if (myBuiltInSearchParams == null) {
			if (myModelConfig.isAutoSupportDefaultSearchParams()) {
				myBuiltInSearchParams = ReadOnlySearchParamCache.fromFhirContext(myFhirContext, mySearchParameterCanonicalizer);
			} else {
				// Only the built-in search params that can not be disabled will be supported automatically
				myBuiltInSearchParams = ReadOnlySearchParamCache.fromFhirContext(myFhirContext, mySearchParameterCanonicalizer, NON_DISABLEABLE_SEARCH_PARAMS);
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
	public void setModelConfig(ModelConfig theModelConfig) {
		myModelConfig = theModelConfig;
	}

	private long overrideBuiltinSearchParamsWithActiveJpaSearchParams(RuntimeSearchParamCache theSearchParamCache, Collection<IBaseResource> theSearchParams) {
		if (!myModelConfig.isDefaultSearchParamsCanBeOverridden() || theSearchParams == null) {
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
			ourLog.debug("Adding search parameter {}.{} to SearchParamRegistry", nextBaseName, StringUtils.defaultString(name, "[composite]"));
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
		myResourceChangeListenerCache.forceRefresh();
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
	 *
	 * There is a circular reference between this class and the ResourceChangeListenerRegistry:
	 * SearchParamRegistryImpl -> ResourceChangeListenerRegistry -> InMemoryResourceMatcher -> SearchParamRegistryImpl. Sicne we only need this once on boot-up, we delay
	 * until ContextRefreshedEvent.
	 *
	 */
	@PostConstruct
	public void registerListener() {
		myResourceChangeListenerCache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener("SearchParameter", SearchParameterMap.newSynchronous(), this, REFRESH_INTERVAL);
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
		myActiveSearchParams.getSearchParamStream().forEach(searchParam -> myJpaSearchParamCache.setPhoneticEncoder(myPhoneticEncoder, searchParam));
	}

	@Override
	public void handleChange(IResourceChangeEvent theResourceChangeEvent) {
		if (theResourceChangeEvent.isEmpty()) {
			return;
		}

		ResourceChangeResult result = ResourceChangeResult.fromResourceChangeEvent(theResourceChangeEvent);
		if (result.created > 0) {
			ourLog.info("Adding {} search parameters to SearchParamRegistry: {}", result.created, unqualified(theResourceChangeEvent.getCreatedResourceIds()));
		}
		if (result.updated > 0) {
			ourLog.info("Updating {} search parameters in SearchParamRegistry: {}", result.updated, unqualified(theResourceChangeEvent.getUpdatedResourceIds()));
		}
		if (result.deleted > 0) {
			ourLog.info("Deleting {} search parameters from SearchParamRegistry: {}", result.deleted, unqualified(theResourceChangeEvent.getDeletedResourceIds()));
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

	@VisibleForTesting
	public void resetForUnitTest() {
		myBuiltInSearchParams = null;
		handleInit(Collections.emptyList());
	}

	@VisibleForTesting
	public void setSearchParameterCanonicalizerForUnitTest(SearchParameterCanonicalizer theSearchParameterCanonicalizerForUnitTest) {
		mySearchParameterCanonicalizer = theSearchParameterCanonicalizerForUnitTest;
	}

}
