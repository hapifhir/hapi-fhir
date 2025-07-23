/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.interceptor.auth;

import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is used in RuleBuilder, as a way to allow adding or removing certain Search Parameters
 * to the compartment.
 * For example, if you were to add as additional SPs
 * [device -> ["patient", "subject"]
 * and apply it to compartment Patient/123, then any device with Patient/123 as its patient would be considered "in"
 * the compartment, despite the fact that device is technically not part of the compartment definition for patient.
 */
public class CompartmentSearchParameterModifications {

	/**
	 * Construct compartment modifications from resource type and sets of SP names to add or omit respectively.
	 * @param theResourceType the resource type the SPs are based on
	 * @param theAdditionalSPs the additional SP names
	 * @param theOmittedSps the omitted SP names
	 * @return
	 */
	public static CompartmentSearchParameterModifications fromAdditionalAndOmittedSPNames(
			@Nonnull String theResourceType,
			@Nonnull Set<String> theAdditionalSPs,
			@Nonnull Set<String> theOmittedSps) {
		CompartmentSearchParameterModifications modifications = new CompartmentSearchParameterModifications();
		theAdditionalSPs.forEach(spName -> {
			modifications.addSPToIncludeInCompartment(theResourceType, spName);
		});
		theOmittedSps.forEach(spName -> {
			modifications.addSPToOmitFromCompartment(theResourceType, spName);
		});
		return modifications;
	}

	public static CompartmentSearchParameterModifications fromAdditionalCompartmentParamNames(
			String theResourceType, @Nonnull Set<String> theAdditionalCompartmentParamNames) {
		return fromAdditionalAndOmittedSPNames(theResourceType, theAdditionalCompartmentParamNames, Set.of());
	}

	private final Map<String, Set<String>> myAdditionalResourceTypeToParameterCodeMap;

	private final Map<String, Set<String>> myOmittedResourceTypeToParameterCodeMap;

	public CompartmentSearchParameterModifications() {
		myAdditionalResourceTypeToParameterCodeMap = new HashMap<>();
		myOmittedResourceTypeToParameterCodeMap = new HashMap<>();
	}

	/**
	 * Add an SP, normally included in the compartment, that will be omitted
	 * hereafter.
	 * @param theResourceType the resource type on which the SP exists
	 * @param theSPName the name of the search parameter
	 */
	public void addSPToOmitFromCompartment(String theResourceType, String theSPName) {
		myOmittedResourceTypeToParameterCodeMap
				.computeIfAbsent(theResourceType.toLowerCase(), (key) -> new HashSet<>())
				.add(theSPName);
	}

	/**
	 * Add an SP, not in the compartment, that will now be included hereafter
	 * @param theResourceType the resource type on which the SP exists
	 * @param theSPName the name of the search parameter
	 */
	public void addSPToIncludeInCompartment(String theResourceType, String theSPName) {
		myAdditionalResourceTypeToParameterCodeMap
				.computeIfAbsent(theResourceType.toLowerCase(), (key) -> new HashSet<>())
				.add(theSPName);
	}

	/**
	 * add a SP to include into the compartment
	 * @param theQualifiedSearchParameters - the additional sp. Must be of the format: {resourceType}:{fhirPath}
	 */
	@Deprecated
	public void addAdditionalSearchParameters(@Nonnull String... theQualifiedSearchParameters) {
		Arrays.stream(theQualifiedSearchParameters).forEach(code -> {
			if (code == null || !code.contains(":")) {
				throw new IllegalArgumentException(
						Msg.code(341) + code
								+ " is not a valid search parameter. Search parameters must be in the form resourcetype:parametercode, e.g. 'Device:patient'");
			}
			String[] split = code.split(":");
			if (split.length != 2) {
				throw new IllegalArgumentException(
						Msg.code(342) + code
								+ " is not a valid search parameter. Search parameters must be in the form resourcetype:parametercode, e.g. 'Device:patient'");
			} else {
				myAdditionalResourceTypeToParameterCodeMap
						.computeIfAbsent(split[0].toLowerCase(), (key) -> new HashSet<>())
						.add(split[1].toLowerCase());
			}
		});
	}

	public Set<String> getAdditionalSearchParamNamesForResourceType(@Nonnull String theResourceType) {
		return myAdditionalResourceTypeToParameterCodeMap.computeIfAbsent(
				theResourceType.toLowerCase(), (key) -> new HashSet<>());
	}

	public Set<String> getOmittedSPNamesForResourceType(String theResourceType) {
		return myOmittedResourceTypeToParameterCodeMap.computeIfAbsent(
				theResourceType.toLowerCase(), (key) -> new HashSet<>());
	}
}
