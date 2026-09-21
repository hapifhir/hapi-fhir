/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import jakarta.annotation.Nonnull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used in RuleBuilder, as a way to allow adding or removing certain Search Parameters
 * to the compartment.
 * For example, if you were to add as additional SPs
 * [device -> ["patient", "subject"]]
 * and apply it to compartment Patient/123, then any device with Patient/123 as its patient would be considered "in"
 * the compartment, despite the fact that device is technically not part of the compartment definition for patient.
 * <p>
 * Instances of this class are thread safe. A single instance is typically populated once while an
 * authorization rule list is being built, and is then read concurrently by every thread that evaluates
 * that rule list. The getters never modify any internal state, and the {@link Set}s they return are
 * immutable.
 * </p>
 * <p>
 * Resource type names are matched case-insensitively. Search parameter names are matched case-sensitively,
 * since FHIR SearchParameter codes are case sensitive.
 * </p>
 */
public class CompartmentSearchParameterModifications {

	/**
	 * Construct compartment modifications from resource type and sets of SP names to add or omit respectively.
	 * @param theResourceType the resource type the SPs are based on
	 * @param theAdditionalSPs the additional SP names
	 * @param theOmittedSps the omitted SP names
	 * @return a new instance with the given SP names registered against the given resource type
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

	@Nonnull
	public static CompartmentSearchParameterModifications fromAdditionalCompartmentParamNames(
			@Nonnull String theResourceType, @Nonnull Set<String> theAdditionalCompartmentParamNames) {
		return fromAdditionalAndOmittedSPNames(theResourceType, theAdditionalCompartmentParamNames, Set.of());
	}

	private final Map<String, Set<String>> myAdditionalResourceTypeToParameterCodeMap;

	private final Map<String, Set<String>> myOmittedResourceTypeToParameterCodeMap;

	public CompartmentSearchParameterModifications() {
		myAdditionalResourceTypeToParameterCodeMap = new ConcurrentHashMap<>();
		myOmittedResourceTypeToParameterCodeMap = new ConcurrentHashMap<>();
	}

	/**
	 * Add an SP, normally included in the compartment, that will be omitted
	 * hereafter.
	 * @param theResourceType the resource type on which the SP exists
	 * @param theSPName the name of the search parameter
	 */
	public void addSPToOmitFromCompartment(@Nonnull String theResourceType, @Nonnull String theSPName) {
		addSPName(myOmittedResourceTypeToParameterCodeMap, theResourceType, theSPName);
	}

	/**
	 * Add an SP, not in the compartment, that will now be included hereafter
	 * @param theResourceType the resource type on which the SP exists
	 * @param theSPName the name of the search parameter
	 */
	public void addSPToIncludeInCompartment(@Nonnull String theResourceType, @Nonnull String theSPName) {
		addSPName(myAdditionalResourceTypeToParameterCodeMap, theResourceType, theSPName);
	}

	/**
	 * Returns the search parameters which should be treated as part of the compartment for the given
	 * resource type, in addition to the ones in the compartment definition.
	 *
	 * @param theResourceType the resource type to look up, matched case-insensitively
	 * @return an immutable Set, empty if no additional SPs are registered for this resource type
	 */
	@Nonnull
	public Set<String> getAdditionalSearchParamNamesForResourceType(@Nonnull String theResourceType) {
		return getSPNames(myAdditionalResourceTypeToParameterCodeMap, theResourceType);
	}

	/**
	 * Returns the search parameters which should be excluded from the compartment for the given resource
	 * type, even though the compartment definition includes them.
	 *
	 * @param theResourceType the resource type to look up, matched case-insensitively
	 * @return an immutable Set, empty if no omitted SPs are registered for this resource type
	 */
	@Nonnull
	public Set<String> getOmittedSPNamesForResourceType(@Nonnull String theResourceType) {
		return getSPNames(myOmittedResourceTypeToParameterCodeMap, theResourceType);
	}

	private static void addSPName(
			Map<String, Set<String>> theResourceTypeToParameterCodeMap, String theResourceType, String theSPName) {
		// Copy-on-write, so that a reader holding a previously returned Set is unaffected by this update
		String normalizedResourceType = normalizeResourceType(theResourceType);
		theResourceTypeToParameterCodeMap.compute(normalizedResourceType, (key, existingSPNames) -> {
			Set<String> updatedSPNames = existingSPNames == null ? new HashSet<>() : new HashSet<>(existingSPNames);
			updatedSPNames.add(theSPName);
			return Collections.unmodifiableSet(updatedSPNames);
		});
	}

	@Nonnull
	private static Set<String> getSPNames(
			Map<String, Set<String>> theResourceTypeToParameterCodeMap, String theResourceType) {
		String normalizedResourceType = normalizeResourceType(theResourceType);
		return theResourceTypeToParameterCodeMap.getOrDefault(normalizedResourceType, Collections.emptySet());
	}

	private static String normalizeResourceType(String theResourceType) {
		return theResourceType.toLowerCase(Locale.ROOT);
	}
}
