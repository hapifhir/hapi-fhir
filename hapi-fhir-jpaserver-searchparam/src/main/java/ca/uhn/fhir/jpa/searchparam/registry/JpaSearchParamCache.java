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
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

public class JpaSearchParamCache {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaSearchParamCache.class);

	private volatile Map<String, List<JpaRuntimeSearchParam>> myActiveUniqueSearchParams = Collections.emptyMap();
	private volatile Map<String, Map<Set<String>, List<JpaRuntimeSearchParam>>> myActiveParamNamesToUniqueSearchParams = Collections.emptyMap();

	public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
		List<JpaRuntimeSearchParam> retval = myActiveUniqueSearchParams.get(theResourceName);
		if (retval == null) {
			retval = Collections.emptyList();
		}
		return retval;
	}

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

	void populateActiveSearchParams(IInterceptorService theInterceptorBroadcaster, IPhoneticEncoder theDefaultPhoneticEncoder, RuntimeSearchParamCache theActiveSearchParams) {
		Map<String, List<JpaRuntimeSearchParam>> activeUniqueSearchParams = new HashMap<>();
		Map<String, Map<Set<String>, List<JpaRuntimeSearchParam>>> activeParamNamesToUniqueSearchParams = new HashMap<>();

		Map<String, RuntimeSearchParam> idToRuntimeSearchParam = new HashMap<>();
		List<JpaRuntimeSearchParam> jpaSearchParams = new ArrayList<>();

		/*
		 * Loop through parameters and find JPA params
		 */
		for (String theResourceName : theActiveSearchParams.getResourceNameKeys()) {
			Map<String, RuntimeSearchParam> searchParamMap = theActiveSearchParams.getSearchParamMap(theResourceName);
			List<JpaRuntimeSearchParam> uniqueSearchParams = activeUniqueSearchParams.computeIfAbsent(theResourceName, k -> new ArrayList<>());
			Collection<RuntimeSearchParam> nextSearchParamsForResourceName = searchParamMap.values();

			ourLog.trace("Resource {} has {} params", theResourceName, searchParamMap.size());

			for (RuntimeSearchParam nextCandidate : nextSearchParamsForResourceName) {

				ourLog.trace("Resource {} has parameter {} with ID {}", theResourceName, nextCandidate.getName(), nextCandidate.getId());

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

				setPhoneticEncoder(theDefaultPhoneticEncoder, nextCandidate);
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
					theInterceptorBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_WARNING, params);
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
