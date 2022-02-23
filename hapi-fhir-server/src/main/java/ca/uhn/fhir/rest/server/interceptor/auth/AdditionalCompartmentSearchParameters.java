package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is used in RuleBuilder, as a way to provide a compartment permission additional resource search params that
 * are to be included as "in" the given compartment. For example, if you were to populate this map with
 * [device -> ["patient", "subject"]
 * and apply it to compartment Patient/123, then any device with Patient/123 as its patient would be considered "in"
 * the compartment, despite the fact that device is technically not part of the compartment definition for patient.
 */
public class AdditionalCompartmentSearchParameters {
	private Map<String, Set<String>> myResourceTypeToParameterCodeMap;

	public AdditionalCompartmentSearchParameters() {
		myResourceTypeToParameterCodeMap = new HashMap<>();
	}

	public void addSearchParameters(@Nonnull String... theQualifiedSearchParameters) {
		Arrays.stream(theQualifiedSearchParameters).forEach(code -> {
			if (code == null || !code.contains(":")) {
				throw new IllegalArgumentException(Msg.code(341) + code + " is not a valid search parameter. Search parameters must be in the form resourcetype:parametercode, e.g. 'Device:patient'");
			}
			String[] split = code.split(":");
			if (split.length != 2) {
				throw new IllegalArgumentException(Msg.code(342) + code + " is not a valid search parameter. Search parameters must be in the form resourcetype:parametercode, e.g. 'Device:patient'");
			} else {
				myResourceTypeToParameterCodeMap.computeIfAbsent(split[0].toLowerCase(), (key) -> new HashSet<>()).add(split[1].toLowerCase());
			}
		});
	}

	public Set<String> getSearchParamNamesForResourceType(@Nonnull String theResourceType) {
		return myResourceTypeToParameterCodeMap.computeIfAbsent(theResourceType.toLowerCase(), (key) -> new HashSet<>());
	}
}
