package ca.uhn.fhir.jpa.searchparam.registry;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseSearchParamRegistry<SP extends IBaseResource> implements ISearchParamRegistry {

	private static final int MAX_MANAGED_PARAM_COUNT = 10000;
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSearchParamRegistry.class);
	private static final int MAX_RETRIES = 60; // 5 minutes
	private static long REFRESH_INTERVAL = 60 * DateUtils.MILLIS_PER_MINUTE;
	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private ISearchParamProvider mySearchParamProvider;
	@Autowired
	private FhirContext myFhirContext;

	private Map<String, Map<String, RuntimeSearchParam>> myBuiltInSearchParams;
	private volatile Map<String, List<JpaRuntimeSearchParam>> myActiveUniqueSearchParams = Collections.emptyMap();
	private volatile Map<String, Map<Set<String>, List<JpaRuntimeSearchParam>>> myActiveParamNamesToUniqueSearchParams = Collections.emptyMap();
	private volatile Map<String, Map<String, RuntimeSearchParam>> myActiveSearchParams;
	private volatile long myLastRefresh;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

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
		return myActiveSearchParams.get(theResourceName);
	}

	private void requiresActiveSearchParams() {
		if (myActiveSearchParams == null) {
			refreshCacheWithRetry();
		}
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
		List<JpaRuntimeSearchParam> retVal = myActiveUniqueSearchParams.get(theResourceName);
		if (retVal == null) {
			retVal = Collections.emptyList();
		}
		return retVal;
	}

	@Override
	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {

		Map<Set<String>, List<JpaRuntimeSearchParam>> paramNamesToParams = myActiveParamNamesToUniqueSearchParams.get(theResourceName);
		if (paramNamesToParams == null) {
			return Collections.emptyList();
		}

		List<JpaRuntimeSearchParam> retVal = paramNamesToParams.get(theParamNames);
		if (retVal == null) {
			retVal = Collections.emptyList();
		}
		return Collections.unmodifiableList(retVal);
	}

	private Map<String, Map<String, RuntimeSearchParam>> getBuiltInSearchParams() {
		return myBuiltInSearchParams;
	}

	private Map<String, RuntimeSearchParam> getSearchParamMap(Map<String, Map<String, RuntimeSearchParam>> searchParams, String theResourceName) {
		Map<String, RuntimeSearchParam> retVal = searchParams.computeIfAbsent(theResourceName, k -> new HashMap<>());
		return retVal;
	}

	private void populateActiveSearchParams(Map<String, Map<String, RuntimeSearchParam>> theActiveSearchParams) {

		Map<String, List<JpaRuntimeSearchParam>> activeUniqueSearchParams = new HashMap<>();
		Map<String, Map<Set<String>, List<JpaRuntimeSearchParam>>> activeParamNamesToUniqueSearchParams = new HashMap<>();

		Map<String, RuntimeSearchParam> idToRuntimeSearchParam = new HashMap<>();
		List<JpaRuntimeSearchParam> jpaSearchParams = new ArrayList<>();

		/*
		 * Loop through parameters and find JPA params
		 */
		for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextResourceNameToEntries : theActiveSearchParams.entrySet()) {
			List<JpaRuntimeSearchParam> uniqueSearchParams = activeUniqueSearchParams.computeIfAbsent(nextResourceNameToEntries.getKey(), k -> new ArrayList<>());
			Collection<RuntimeSearchParam> nextSearchParamsForResourceName = nextResourceNameToEntries.getValue().values();

			ourLog.trace("Resource {} has {} params", nextResourceNameToEntries.getKey(), nextResourceNameToEntries.getValue().size());

			for (RuntimeSearchParam nextCandidate : nextSearchParamsForResourceName) {

				ourLog.trace("Resource {} has parameter {} with ID {}", nextResourceNameToEntries.getKey(), nextCandidate.getName(), nextCandidate.getId());

				if (nextCandidate.getId() != null) {
					idToRuntimeSearchParam.put(nextCandidate.getId().toUnqualifiedVersionless().getValue(), nextCandidate);
				}

				if (nextCandidate instanceof JpaRuntimeSearchParam) {
					JpaRuntimeSearchParam nextCandidateCasted = (JpaRuntimeSearchParam) nextCandidate;
					jpaSearchParams.add(nextCandidateCasted);
					if (nextCandidateCasted.isUnique()) {
						uniqueSearchParams.add(nextCandidateCasted);
					}
				}
			}

		}

		ourLog.trace("Have {} search params loaded", idToRuntimeSearchParam.size());

		Set<String> haveSeen = new HashSet<>();
		for (JpaRuntimeSearchParam next : jpaSearchParams) {
			if (!haveSeen.add(next.getId().toUnqualifiedVersionless().getValue())) {
				continue;
			}

			Set<String> paramNames = new HashSet<>();
			for (JpaRuntimeSearchParam.Component nextComponent : next.getComponents()) {
				String nextRef = nextComponent.getReference().getReferenceElement().toUnqualifiedVersionless().getValue();
				RuntimeSearchParam componentTarget = idToRuntimeSearchParam.get(nextRef);
				if (componentTarget != null) {
					next.getCompositeOf().add(componentTarget);
					paramNames.add(componentTarget.getName());
				} else {
					String existingParams = idToRuntimeSearchParam
						.keySet()
						.stream()
						.sorted()
						.collect(Collectors.joining(", "));
					String message = "Search parameter " + next.getId().toUnqualifiedVersionless().getValue() + " refers to unknown component " + nextRef + ", ignoring this parameter (valid values: " + existingParams + ")";
					ourLog.warn(message);
					StorageProcessingMessage msg = new StorageProcessingMessage().setMessage(message);
					HookParams params = new HookParams(msg);
					myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PROCESSING_MESSAGE, params);
				}
			}

			if (next.getCompositeOf() != null) {
				next.getCompositeOf().sort((theO1, theO2) -> StringUtils.compare(theO1.getName(), theO2.getName()));
				for (String nextBase : next.getBase()) {
					if (!activeParamNamesToUniqueSearchParams.containsKey(nextBase)) {
						activeParamNamesToUniqueSearchParams.put(nextBase, new HashMap<>());
					}
					if (!activeParamNamesToUniqueSearchParams.get(nextBase).containsKey(paramNames)) {
						activeParamNamesToUniqueSearchParams.get(nextBase).put(paramNames, new ArrayList<>());
					}
					activeParamNamesToUniqueSearchParams.get(nextBase).get(paramNames).add(next);
				}
			}
		}

		ourLog.trace("Have {} unique search params", activeParamNamesToUniqueSearchParams.size());

		myActiveUniqueSearchParams = activeUniqueSearchParams;
		myActiveParamNamesToUniqueSearchParams = activeParamNamesToUniqueSearchParams;
	}

	@PostConstruct
	public void postConstruct() {
		Map<String, Map<String, RuntimeSearchParam>> resourceNameToSearchParams = new HashMap<>();

		Set<String> resourceNames = myFhirContext.getResourceNames();

		for (String resourceName : resourceNames) {
			RuntimeResourceDefinition nextResDef = myFhirContext.getResourceDefinition(resourceName);
			String nextResourceName = nextResDef.getName();
			HashMap<String, RuntimeSearchParam> nameToParam = new HashMap<>();
			resourceNameToSearchParams.put(nextResourceName, nameToParam);

			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				nameToParam.put(nextSp.getName(), nextSp);
			}
		}

		myBuiltInSearchParams = Collections.unmodifiableMap(resourceNameToSearchParams);
	}


	public int doRefresh(long theRefreshInterval) {
		if (System.currentTimeMillis() - theRefreshInterval > myLastRefresh) {
			StopWatch sw = new StopWatch();

			Map<String, Map<String, RuntimeSearchParam>> searchParams = new HashMap<>();
			for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextBuiltInEntry : getBuiltInSearchParams().entrySet()) {
				for (RuntimeSearchParam nextParam : nextBuiltInEntry.getValue().values()) {
					String nextResourceName = nextBuiltInEntry.getKey();
					getSearchParamMap(searchParams, nextResourceName).put(nextParam.getName(), nextParam);
				}
			}

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

			int overriddenCount = 0;
			List<IBaseResource> allSearchParams = allSearchParamsBp.getResources(0, size);
			for (IBaseResource nextResource : allSearchParams) {
				SP nextSp = (SP) nextResource;
				if (nextSp == null) {
					continue;
				}

				RuntimeSearchParam runtimeSp = toRuntimeSp(nextSp);
				if (runtimeSp == null) {
					continue;
				}

				for (String nextBaseName : SearchParameterUtil.getBaseAsStrings(myFhirContext, nextSp)) {
					if (isBlank(nextBaseName)) {
						continue;
					}

					Map<String, RuntimeSearchParam> searchParamMap = getSearchParamMap(searchParams, nextBaseName);
					String name = runtimeSp.getName();
					if (myModelConfig.isDefaultSearchParamsCanBeOverridden() || !searchParamMap.containsKey(name)) {
						searchParamMap.put(name, runtimeSp);
						overriddenCount++;
					}

				}
			}

			ourLog.trace("Have overridden {} built-in search parameters", overriddenCount);

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

			populateActiveSearchParams(activeSearchParams);

			myLastRefresh = System.currentTimeMillis();
			ourLog.info("Refreshed search parameter cache in {}ms", sw.getMillis());
		}
		return myActiveSearchParams.size();
	}

	protected abstract RuntimeSearchParam toRuntimeSp(SP theNextSp);

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
		synchronized (this) {
			myLastRefresh = 0;
		}
	}

	@Override
	public void forceRefresh() {
		requestRefresh();
		refreshCacheWithRetry();
	}

	@VisibleForTesting
	public void setSearchParamProviderForUnitTest(ISearchParamProvider theSearchParamProvider) {
		mySearchParamProvider = theSearchParamProvider;
	}

	int refreshCacheWithRetry() {
		Retrier<Integer> refreshCacheRetrier = new Retrier(() -> {
			synchronized (BaseSearchParamRegistry.this) {
				return mySearchParamProvider.refreshCache(this, REFRESH_INTERVAL);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	@Scheduled(fixedDelay = 10 * DateUtils.MILLIS_PER_SECOND)
	public void refreshCacheOnSchedule() {
		refreshCacheIfNecessary();
	}

	public void refreshCacheIfNecessary() {
		if (myActiveSearchParams == null ||
			System.currentTimeMillis() - REFRESH_INTERVAL > myLastRefresh) {
			refreshCacheWithRetry();
		}
	}

	@Override
	public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
		requiresActiveSearchParams();
		return Collections.unmodifiableMap(myActiveSearchParams);
	}
}
