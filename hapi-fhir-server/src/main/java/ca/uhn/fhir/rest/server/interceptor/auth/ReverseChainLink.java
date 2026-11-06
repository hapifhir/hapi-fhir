/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.Constants;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * One level of a <code>_has</code> parameter: the type it joins, the reference parameter on that type
 * used for the join, and whether that reference points at the searched resource (the outermost level).
 */
// Created by Claude Opus 5.5
record ReverseChainLink(String resourceType, String linkParameter, boolean linkedToSearchedType) {

	/**
	 * Splits each <code>_has</code> parameter name into one link per level. A name has the form
	 * <code>_has:[type]:[link parameter]:[rest]</code>, where <code>[rest]</code> is either a search parameter on
	 * <code>[type]</code> or another level starting with <code>_has</code>. For example,
	 * <code>_has:Observation:subject:_has:Encounter:reason-reference:_id</code> gives (Observation, subject) and
	 * (Encounter, reason-reference). Only the first level links to the searched resource. Each later level links
	 * to the resources of the level before it.
	 *
	 * @param theParameterNames the search parameter names of the request
	 * @return the links of every <code>_has</code> parameter, in order, without duplicates
	 */
	static Set<ReverseChainLink> fromParameterNames(Collection<String> theParameterNames) {
		Set<ReverseChainLink> retVal = new LinkedHashSet<>();
		for (String nextName : theParameterNames) {
			if (nextName.startsWith(Constants.PARAM_HAS + ":")) {
				String[] parts = nextName.split(":");
				boolean linkedToSearchedType = true;
				for (int i = 0; i + 2 < parts.length; i++) {
					// parts[i] is "_has", parts[i + 1] the type it joins and parts[i + 2] the link parameter on that
					// type
					if (Constants.PARAM_HAS.equals(parts[i])) {
						retVal.add(new ReverseChainLink(parts[i + 1], parts[i + 2], linkedToSearchedType));
						linkedToSearchedType = false;
					}
				}
			}
		}
		return retVal;
	}
}
