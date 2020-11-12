package ca.uhn.fhir.jpa.searchparam.registry;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.jpa.cache.IVersionChangeListener;
import ca.uhn.fhir.jpa.cache.IVersionChangeListenerRegistry;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamRegistryImpl implements ISearchParamRegistry, IVersionChangeListener {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamRegistryImpl.class);
	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private ISearchParamProvider mySearchParamProvider;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SearchParameterCanonicalizer mySearchParameterCanonicalizer;
	@Autowired
	private IVersionChangeListenerRegistry myVersionChangeListenerRegistry;

	private Map<String, Map<String, RuntimeSearchParam>> myBuiltInSearchParams;
	private IPhoneticEncoder myPhoneticEncoder;

	private volatile ActiveSearchParamCache myActiveSearchParamCache = new ActiveSearchParamCache();
	private volatile Map<String, Map<String, RuntimeSearchParam>> myActiveSearchParams;

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;

	@Override
	public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {

		requiresActiveSearchParams();
		RuntimeSearchParam retVal = null;
		Map<String, RuntimeSearchParam> params = myActiveSearchParams.get(theResourceName);
		if (params != null) {
			retVal = params.get(theParamName);
		}
		return retVal;
	}

	@Override
	public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
		requiresActiveSearchParams();
		return getActiveSearchParams().get(theResourceName);
	}

	private void requiresActiveSearchParams() {
		if (myActiveSearchParams == null) {
			myVersionChangeListenerRegistry.refreshCacheWithRetry("SearchParameter");
		}
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
		return myActiveSearchParamCache.getActiveUniqueSearchParams(theResourceName);
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
		return myActiveSearchParamCache.getActiveUniqueSearchParams(theResourceName, theParamNames);
	}

	private Map<String, Map<String, RuntimeSearchParam>> getBuiltInSearchParams() {
		return myBuiltInSearchParams;
	}

	private Map<String, RuntimeSearchParam> getSearchParamMap(Map<String, Map<String, RuntimeSearchParam>> searchParams, String theResourceName) {
		return searchParams.computeIfAbsent(theResourceName, k -> new HashMap<>());
	}

	private void initializeActiveSearchParams(Collection<IdDt> theSearchParamIds) {
		Map<String, Map<String, RuntimeSearchParam>> searchParams = new HashMap<>();
		addBuiltinParams(searchParams);

		long overriddenCount = overrideBuiltinSearchParamsWithActiveSearchParams(theSearchParamIds, searchParams);
		ourLog.trace("Have overridden {} built-in search parameters", overriddenCount);

		StopWatch sw = new StopWatch();

		Map<String, Map<String, RuntimeSearchParam>> activeSearchParams = new HashMap<>();
		for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextEntry : searchParams.entrySet()) {
			for (RuntimeSearchParam nextSp : nextEntry.getValue().values()) {
				String nextName = nextSp.getName();
				if (nextSp.getStatus() != RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE) {
					nextSp = null;
				}

				if (!activeSearchParams.containsKey(nextEntry.getKey())) {
					activeSearchParams.put(nextEntry.getKey(), new HashMap<>());
				}
				if (activeSearchParams.containsKey(nextEntry.getKey())) {
					ourLog.debug("Replacing existing/built in search param {}:{} with new one", nextEntry.getKey(), nextName);
				}

				if (nextSp != null) {
					activeSearchParams.get(nextEntry.getKey()).put(nextName, nextSp);
				} else {
					activeSearchParams.get(nextEntry.getKey()).remove(nextName);
				}
			}
		}

		myActiveSearchParams = activeSearchParams;
		myActiveSearchParamCache.populateActiveSearchParams(myInterceptorBroadcaster, myPhoneticEncoder, activeSearchParams);
		ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
	}

	private long overrideBuiltinSearchParamsWithActiveSearchParams(Collection<IdDt> theTheSearchParamIds, Map<String, Map<String, RuntimeSearchParam>> theSearchParams) {
		long retval = 0;

		for (IdDt searchParamId : theTheSearchParamIds) {
			IBaseResource nextSp = mySearchParamProvider.read(searchParamId);
			if (nextSp == null) {
				continue;
			}

			RuntimeSearchParam runtimeSp = mySearchParameterCanonicalizer.canonicalizeSearchParameter(nextSp);
			if (runtimeSp == null) {
				continue;
			}
			if (runtimeSp.getStatus() == RuntimeSearchParam.RuntimeSearchParamStatusEnum.DRAFT) {
				continue;
			}

			for (String nextBaseName : SearchParameterUtil.getBaseAsStrings(myFhirContext, nextSp)) {
				if (isBlank(nextBaseName)) {
					continue;
				}

				Map<String, RuntimeSearchParam> searchParamMap = getSearchParamMap(theSearchParams, nextBaseName);
				String name = runtimeSp.getName();
				if (!searchParamMap.containsKey(name) || myModelConfig.isDefaultSearchParamsCanBeOverridden()) {
					searchParamMap.put(name, runtimeSp);
					retval++;
				}
			}
		}
		return retval;
	}

	private void addBuiltinParams(Map<String, Map<String, RuntimeSearchParam>> theSearchParams) {
		Set<Map.Entry<String, Map<String, RuntimeSearchParam>>> builtInSps = getBuiltInSearchParams().entrySet();
		for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextBuiltInEntry : builtInSps) {
			for (RuntimeSearchParam nextParam : nextBuiltInEntry.getValue().values()) {
				String nextResourceName = nextBuiltInEntry.getKey();
				getSearchParamMap(theSearchParams, nextResourceName).put(nextParam.getName(), nextParam);
			}

			ourLog.trace("Have {} built-in SPs for: {}", nextBuiltInEntry.getValue().size(), nextBuiltInEntry.getKey());
		}
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

	// FIXME KHS move these into a generic abstract superclass
	@Override
	public void requestRefresh() {
		myVersionChangeListenerRegistry.requestRefresh("SearchParameter");
	}

	@Override
	public void forceRefresh() {
		myVersionChangeListenerRegistry.forceRefresh("SearchParameter");
	}

	@Override
	public boolean refreshCacheIfNecessary() {
		return myVersionChangeListenerRegistry.refreshCacheIfNecessary("SearchParameter") > 0;
	}

	@PostConstruct
	public void registerListener() {
		myBuiltInSearchParams = createBuiltInSearchParamMap(myFhirContext);

		// FIXME KHS compare this searchparam with below
		myVersionChangeListenerRegistry.registerResourceVersionChangeListener("SearchParameter", SearchParameterMap.newSynchronous(), this);
	}

	@PreDestroy
	public void unregisterListener() {
		myVersionChangeListenerRegistry.unregisterResourceVersionChangeListener(this);
	}

	@Override
	public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
		requiresActiveSearchParams();
		return Collections.unmodifiableMap(myActiveSearchParams);
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
		for (Map<String, RuntimeSearchParam> activeUniqueSearchParams : myActiveSearchParams.values()) {
			for (RuntimeSearchParam searchParam : activeUniqueSearchParams.values()) {
				myActiveSearchParamCache.setPhoneticEncoder(myPhoneticEncoder, searchParam);
			}
		}
	}

	@Override
	public void handleCreate(IdDt theResourceId) {
		// FIXME KHS
	}

	@Override
	public void handleUpdate(IdDt theResourceId) {
		// FIXME KHS
	}

	@Override
	public void handleDelete(IdDt theResourceId) {
		// FIXME KHS
	}

	@Override
	public void handleInit(Collection<IdDt> theResourceIds) {
		initializeActiveSearchParams(theResourceIds);
	}

	public static Map<String, Map<String, RuntimeSearchParam>> createBuiltInSearchParamMap(FhirContext theFhirContext) {
		Map<String, Map<String, RuntimeSearchParam>> resourceNameToSearchParams = new HashMap<>();

		Set<String> resourceNames = theFhirContext.getResourceTypes();

		for (String resourceName : resourceNames) {
			RuntimeResourceDefinition nextResDef = theFhirContext.getResourceDefinition(resourceName);
			String nextResourceName = nextResDef.getName();
			HashMap<String, RuntimeSearchParam> nameToParam = new HashMap<>();
			resourceNameToSearchParams.put(nextResourceName, nameToParam);

			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				nameToParam.put(nextSp.getName(), nextSp);
			}
		}
		return Collections.unmodifiableMap(resourceNameToSearchParams);
	}
}
