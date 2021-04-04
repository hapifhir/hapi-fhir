package ca.uhn.fhir.jpa.searchparam.registry;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaSearchParamCache {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaSearchParamCache.class);

	private volatile Map<String, List<RuntimeSearchParam>> myActiveUniqueSearchParams = Collections.emptyMap();
	private volatile Map<String, Map<Set<String>, List<RuntimeSearchParam>>> myActiveParamNamesToUniqueSearchParams = Collections.emptyMap();

	public List<RuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
		List<RuntimeSearchParam> retval = myActiveUniqueSearchParams.get(theResourceName);
		if (retval == null) {
			retval = Collections.emptyList();
		}
		return retval;
	}

	public List<RuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
		Map<Set<String>, List<RuntimeSearchParam>> paramNamesToParams = myActiveParamNamesToUniqueSearchParams.get(theResourceName);
		if (paramNamesToParams == null) {
			return Collections.emptyList();
		}

		List<RuntimeSearchParam> retVal = paramNamesToParams.get(theParamNames);
		if (retVal == null) {
			retVal = Collections.emptyList();
		}
		return Collections.unmodifiableList(retVal);
	}

	void populateActiveSearchParams(IInterceptorService theInterceptorBroadcaster, IPhoneticEncoder theDefaultPhoneticEncoder, RuntimeSearchParamCache theActiveSearchParams) {
		Map<String, List<RuntimeSearchParam>> activeUniqueSearchParams = new HashMap<>();
		Map<String, Map<Set<String>, List<RuntimeSearchParam>>> activeParamNamesToUniqueSearchParams = new HashMap<>();

		Map<String, RuntimeSearchParam> idToRuntimeSearchParam = new HashMap<>();
		List<RuntimeSearchParam> jpaSearchParams = new ArrayList<>();

		/*
		 * Loop through parameters and find JPA params
		 */
		for (String theResourceName : theActiveSearchParams.getResourceNameKeys()) {
			Map<String, RuntimeSearchParam> searchParamMap = theActiveSearchParams.getSearchParamMap(theResourceName);
			List<RuntimeSearchParam> uniqueSearchParams = activeUniqueSearchParams.computeIfAbsent(theResourceName, k -> new ArrayList<>());
			Collection<RuntimeSearchParam> nextSearchParamsForResourceName = searchParamMap.values();

			ourLog.trace("Resource {} has {} params", theResourceName, searchParamMap.size());

			for (RuntimeSearchParam nextCandidate : nextSearchParamsForResourceName) {

				ourLog.trace("Resource {} has parameter {} with ID {}", theResourceName, nextCandidate.getName(), nextCandidate.getId());

				if (nextCandidate.getId() != null) {
					idToRuntimeSearchParam.put(nextCandidate.getId().toUnqualifiedVersionless().getValue(), nextCandidate);
				}
				if (isNotBlank(nextCandidate.getUri())) {
					idToRuntimeSearchParam.put(nextCandidate.getUri(), nextCandidate);
				}

				RuntimeSearchParam nextCandidateCasted = nextCandidate;
				jpaSearchParams.add(nextCandidateCasted);
				if (nextCandidateCasted.isUnique()) {
					uniqueSearchParams.add(nextCandidateCasted);
				}

				setPhoneticEncoder(theDefaultPhoneticEncoder, nextCandidate);
			}

		}

		ourLog.trace("Have {} search params loaded", idToRuntimeSearchParam.size());

		Set<String> haveSeen = new HashSet<>();
		for (RuntimeSearchParam next : jpaSearchParams) {
			if (next.getId() != null && !haveSeen.add(next.getId().toUnqualifiedVersionless().getValue())) {
				continue;
			}

			Set<String> paramNames = new TreeSet<>();
			for (RuntimeSearchParam.Component nextComponent : next.getComponents()) {
				String nextRef = nextComponent.getReference();
				RuntimeSearchParam componentTarget = idToRuntimeSearchParam.get(nextRef);
				if (componentTarget != null) {
					paramNames.add(componentTarget.getName());
				} else {
					String existingParams = idToRuntimeSearchParam
						.keySet()
						.stream()
						.sorted()
						.collect(Collectors.joining(", "));
					String message = "Search parameter " + next + " refers to unknown component " + nextRef + ", ignoring this parameter (valid values: " + existingParams + ")";
					ourLog.warn(message);

					// Interceptor broadcast: JPA_PERFTRACE_WARNING
					HookParams params = new HookParams()
						.add(RequestDetails.class, null)
						.add(ServletRequestDetails.class, null)
						.add(StorageProcessingMessage.class, new StorageProcessingMessage().setMessage(message));
					theInterceptorBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_WARNING, params);
				}
			}

			if (next.isUnique()) {
				for (String nextBase : next.getBase()) {
					activeParamNamesToUniqueSearchParams.computeIfAbsent(nextBase, v -> new HashMap<>());
					activeParamNamesToUniqueSearchParams.get(nextBase).computeIfAbsent(paramNames, t -> new ArrayList<>());
					activeParamNamesToUniqueSearchParams.get(nextBase).get(paramNames).add(next);
				}
			}
		}

		ourLog.info("Have {} unique search params", activeParamNamesToUniqueSearchParams.size());

		myActiveUniqueSearchParams = activeUniqueSearchParams;
		myActiveParamNamesToUniqueSearchParams = activeParamNamesToUniqueSearchParams;
	}

	void setPhoneticEncoder(IPhoneticEncoder theDefaultPhoneticEncoder, RuntimeSearchParam searchParam) {
		if ("phonetic".equals(searchParam.getName())) {
			ourLog.debug("Setting search param {} on {} phonetic encoder to {}",
				searchParam.getName(), searchParam.getPath(), theDefaultPhoneticEncoder == null ? "null" : theDefaultPhoneticEncoder.name());
			searchParam.setPhoneticEncoder(theDefaultPhoneticEncoder);
		}
	}
}
