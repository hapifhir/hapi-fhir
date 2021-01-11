package ca.uhn.fhir.jpa.searchparam.registry;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ResourceChangeResult;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamRegistryImpl implements ISearchParamRegistry, IResourceChangeListener {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamRegistryImpl.class);
	private static final int MAX_MANAGED_PARAM_COUNT = 10000;
	private static long REFRESH_INTERVAL = DateUtils.MILLIS_PER_HOUR;

	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private ISearchParamProvider mySearchParamProvider;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SearchParameterCanonicalizer mySearchParameterCanonicalizer;
	@Autowired
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	private volatile ReadOnlySearchParamCache myBuiltInSearchParams;
	private volatile IPhoneticEncoder myPhoneticEncoder;
	private volatile JpaSearchParamCache myJpaSearchParamCache = new JpaSearchParamCache();
	private volatile RuntimeSearchParamCache myActiveSearchParams;

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;
	private IResourceChangeListenerCache myResourceChangeListenerCache;

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

	@Override
	public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
		requiresActiveSearchParams();
		return getActiveSearchParams().getSearchParamMap(theResourceName);
	}

	private void requiresActiveSearchParams() {
		if (myActiveSearchParams == null) {
			myResourceChangeListenerCache.forceRefresh();
		}
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
		return myJpaSearchParamCache.getActiveUniqueSearchParams(theResourceName);
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
		return myJpaSearchParamCache.getActiveUniqueSearchParams(theResourceName, theParamNames);
	}

	private void rebuildActiveSearchParams() {
		ourLog.info("Rebuilding SearchParamRegistry");
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(MAX_MANAGED_PARAM_COUNT);

		IBundleProvider allSearchParamsBp = mySearchParamProvider.search(params);
		int size = allSearchParamsBp.size();

		ourLog.trace("Loaded {} search params from the DB", size);

		// Just in case..
		if (size >= MAX_MANAGED_PARAM_COUNT) {
			ourLog.warn("Unable to support >" + MAX_MANAGED_PARAM_COUNT + " search params!");
			size = MAX_MANAGED_PARAM_COUNT;
		}
		List<IBaseResource> allSearchParams = allSearchParamsBp.getResources(0, size);
		initializeActiveSearchParams(allSearchParams);
	}

	private void initializeActiveSearchParams(Collection<IBaseResource> theJpaSearchParams) {
		StopWatch sw = new StopWatch();

		RuntimeSearchParamCache searchParams = RuntimeSearchParamCache.fromReadOnlySearchParmCache(getBuiltInSearchParams());
		long overriddenCount = overrideBuiltinSearchParamsWithActiveJpaSearchParams(searchParams, theJpaSearchParams);
		ourLog.trace("Have overridden {} built-in search parameters", overriddenCount);
		removeInactiveSearchParams(searchParams);
		myActiveSearchParams = searchParams;

		myJpaSearchParamCache.populateActiveSearchParams(myInterceptorBroadcaster, myPhoneticEncoder, myActiveSearchParams);
		ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
	}

	private ReadOnlySearchParamCache getBuiltInSearchParams() {
		if (myBuiltInSearchParams == null) {
			myBuiltInSearchParams = ReadOnlySearchParamCache.fromFhirContext(myFhirContext);
		}
		return myBuiltInSearchParams;
	}

	private void removeInactiveSearchParams(RuntimeSearchParamCache theSearchParams) {
		for (String resourceName : theSearchParams.getResourceNameKeys()) {
			Map<String, RuntimeSearchParam> map = theSearchParams.getSearchParamMap(resourceName);
			map.entrySet().removeIf(entry -> entry.getValue().getStatus() != RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE);
		}
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

			Map<String, RuntimeSearchParam> searchParamMap = theSearchParams.getSearchParamMap(nextBaseName);
			String name = runtimeSp.getName();
			ourLog.debug("Adding search parameter {}.{} to SearchParamRegistry", nextBaseName, StringUtils.defaultString(name, "[composite]"));
			searchParamMap.put(name, runtimeSp);
			retval++;
		}
		return retval;
	}

	@Override
	public RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
		Map<String, RuntimeSearchParam> params = getActiveSearchParams(theResourceDef.getName());
		return params.get(theParamName);
	}

	@Override
	public Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef) {
		return getActiveSearchParams(theResourceDef.getName()).values();
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

	@PostConstruct
	public void registerListener() {
		myResourceChangeListenerCache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener("SearchParameter", SearchParameterMap.newSynchronous(), this, REFRESH_INTERVAL);
	}

	@PreDestroy
	public void unregisterListener() {
		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(this);
	}

	@Override
	public ReadOnlySearchParamCache getActiveSearchParams() {
		requiresActiveSearchParams();
		if (myActiveSearchParams == null) {
			throw new IllegalStateException("SearchParamRegistry has not been initialized");
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
			ourLog.info("Adding {} search parameters to SearchParamRegistry", result.created);
		}
		if (result.updated > 0) {
			ourLog.info("Updating {} search parameters in SearchParamRegistry", result.updated);
		}
		if (result.created > 0) {
			ourLog.info("Deleting {} search parameters from SearchParamRegistry", result.deleted);
		}
		rebuildActiveSearchParams();
	}

	@Override
	public void handleInit(Collection<IIdType> theResourceIds) {
		List<IBaseResource> searchParams = theResourceIds.stream().map(id -> mySearchParamProvider.read(id)).collect(Collectors.toList());
		initializeActiveSearchParams(searchParams);
	}

	@VisibleForTesting
	public void resetForUnitTest() {
		handleInit(Collections.emptyList());
	}
}
