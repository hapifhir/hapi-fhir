package ca.uhn.fhir.jpa.searchparam.registry;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParamRegistryImpl implements ISearchParamRegistry {

	private static final int MAX_MANAGED_PARAM_COUNT = 10000;
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamRegistryImpl.class);
	private static final int MAX_RETRIES = 60; // 5 minutes
	private static long REFRESH_INTERVAL = 60 * DateUtils.MILLIS_PER_MINUTE;
	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private ISearchParamProvider mySearchParamProvider;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private SearchParameterCanonicalizer mySearchParameterCanonicalizer;

	private Map<String, Map<String, RuntimeSearchParam>> myBuiltInSearchParams;
	private IPhoneticEncoder myPhoneticEncoder;

	private volatile Map<String, List<JpaRuntimeSearchParam>> myActiveUniqueSearchParams = Collections.emptyMap();
	private volatile Map<String, Map<Set<String>, List<JpaRuntimeSearchParam>>> myActiveParamNamesToUniqueSearchParams = Collections.emptyMap();
	private volatile Map<String, Map<String, RuntimeSearchParam>> myActiveSearchParams;
	private volatile long myLastRefresh;

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;
	private RefreshSearchParameterCacheOnUpdate myInterceptor;

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

				setPhoneticEncoder(nextCandidate);
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

					// Interceptor broadcast: JPA_PERFTRACE_WARNING
					HookParams params = new HookParams()
						.add(RequestDetails.class, null)
						.add(ServletRequestDetails.class, null)
						.add(StorageProcessingMessage.class, new StorageProcessingMessage().setMessage(message));
					myInterceptorBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_WARNING, params);
				}
			}

			if (next.getCompositeOf() != null) {
				next.getCompositeOf().sort((theO1, theO2) -> StringUtils.compare(theO1.getName(), theO2.getName()));
				for (String nextBase : next.getBase()) {
					activeParamNamesToUniqueSearchParams.computeIfAbsent(nextBase, v -> new HashMap<>());
					activeParamNamesToUniqueSearchParams.get(nextBase).computeIfAbsent(paramNames, t -> new ArrayList<>());
					activeParamNamesToUniqueSearchParams.get(nextBase).get(paramNames).add(next);
				}
			}
		}

		ourLog.trace("Have {} unique search params", activeParamNamesToUniqueSearchParams.size());

		myActiveUniqueSearchParams = activeUniqueSearchParams;
		myActiveParamNamesToUniqueSearchParams = activeParamNamesToUniqueSearchParams;
	}

	@PostConstruct
	public void start() {
		myBuiltInSearchParams = createBuiltInSearchParamMap(myFhirContext);

		myInterceptor = new RefreshSearchParameterCacheOnUpdate();
		myInterceptorBroadcaster.registerInterceptor(myInterceptor);
	}

	@PreDestroy
	public void stop() {
		myInterceptorBroadcaster.unregisterInterceptor(myInterceptor);
	}

	public int doRefresh(long theRefreshInterval) {
		if (System.currentTimeMillis() - theRefreshInterval > myLastRefresh) {
			StopWatch sw = new StopWatch();

			Map<String, Map<String, RuntimeSearchParam>> searchParams = new HashMap<>();
			Set<Map.Entry<String, Map<String, RuntimeSearchParam>>> builtInSps = getBuiltInSearchParams().entrySet();
			for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextBuiltInEntry : builtInSps) {
				for (RuntimeSearchParam nextParam : nextBuiltInEntry.getValue().values()) {
					String nextResourceName = nextBuiltInEntry.getKey();
					getSearchParamMap(searchParams, nextResourceName).put(nextParam.getName(), nextParam);
				}

				ourLog.trace("Have {} built-in SPs for: {}", nextBuiltInEntry.getValue().size(), nextBuiltInEntry.getKey());
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
				IBaseResource nextSp = nextResource;
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

					Map<String, RuntimeSearchParam> searchParamMap = getSearchParamMap(searchParams, nextBaseName);
					String name = runtimeSp.getName();
					if (!searchParamMap.containsKey(name) || myModelConfig.isDefaultSearchParamsCanBeOverridden()) {
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
			ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
			return myActiveSearchParams.size();
		} else {
			return 0;
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

	int refreshCacheWithRetry() {
		Retrier<Integer> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (SearchParamRegistryImpl.this) {
				return mySearchParamProvider.refreshCache(this, REFRESH_INTERVAL);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	@PostConstruct
	public void scheduleJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	@Override
	public boolean refreshCacheIfNecessary() {
		if (myActiveSearchParams == null || System.currentTimeMillis() - REFRESH_INTERVAL > myLastRefresh) {
			refreshCacheWithRetry();
			return true;
		} else {
			return false;
		}
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
				setPhoneticEncoder(searchParam);
			}
		}
	}

	private void setPhoneticEncoder(RuntimeSearchParam searchParam) {
		if ("phonetic".equals(searchParam.getName())) {
			ourLog.debug("Setting search param {} on {} phonetic encoder to {}",
				searchParam.getName(), searchParam.getPath(), myPhoneticEncoder == null ? "null" : myPhoneticEncoder.name());
			searchParam.setPhoneticEncoder(myPhoneticEncoder);
		}
	}

	@Interceptor
	public class RefreshSearchParameterCacheOnUpdate {

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
		public void created(IBaseResource theResource) {
			handle(theResource);
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
		public void deleted(IBaseResource theResource) {
			handle(theResource);
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
		public void updated(IBaseResource theResource) {
			handle(theResource);
		}

		private void handle(IBaseResource theResource) {
			if (theResource != null && myFhirContext.getResourceType(theResource).equals("SearchParameter")) {
				requestRefresh();
			}
		}

	}

	public static class Job implements HapiJob {
		@Autowired
		private ISearchParamRegistry myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.refreshCacheIfNecessary();
		}
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
