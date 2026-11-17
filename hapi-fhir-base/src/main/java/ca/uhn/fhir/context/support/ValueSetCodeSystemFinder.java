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
package ca.uhn.fhir.context.support;

import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Finds the CodeSystems that keep a code's ValueSet membership from being decided, see {@link #findUnknownSystems}.
 *
 * @since 9.0.0
 */
// Created by Claude Opus 5.5
public final class ValueSetCodeSystemFinder {

	private static final List<String> INCLUDE_AND_EXCLUDE_PATHS = List.of("compose.include", "compose.exclude");

	private final ValidationSupportContext myValidationSupportContext;
	private final FhirTerser myTerser;

	/**
	 * @param theValidationSupportContext The context whose root validation support fetches the ValueSets and is asked
	 *                                    whether it has the CodeSystems
	 */
	public ValueSetCodeSystemFinder(@Nonnull ValidationSupportContext theValidationSupportContext) {
		myValidationSupportContext = theValidationSupportContext;
		myTerser = getValidationSupport().getFhirContext().newTerser();
	}

	/**
	 * The CodeSystems that keep the code's membership in the ValueSet from being decided. A code reported as not in a
	 * ValueSet that uses the code's CodeSystem, or a version of it, that no module has was only checked against
	 * another version of that CodeSystem or the codes the ValueSet lists, so the result does not show that the code
	 * is outside the ValueSet.
	 *
	 * @param theResult      The result of checking the code against the ValueSet
	 * @param theValueSetUrl The ValueSet URL
	 * @param theSystem      The code's CodeSystem URL, or {@literal null} if the coding has none
	 * @return The unknown systems {@code theResult} names; otherwise {@code theSystem} if the code is not in the
	 * ValueSet and the ValueSet uses that CodeSystem, or a version of it, that no module has; otherwise empty
	 */
	@Nonnull
	public List<String> findUnknownSystems(
			@Nonnull IValidationSupport.CodeValidationResult theResult,
			@Nonnull String theValueSetUrl,
			@Nullable String theSystem) {
		if (!theResult.getUnknownSystems().isEmpty()) {
			return List.copyOf(theResult.getUnknownSystems());
		}
		if (!theResult.isOk()
				&& isNotBlank(theSystem)
				&& getIncludesAndExcludes(theValueSetUrl).stream().anyMatch(t -> isOfUnknownSystem(t, theSystem))) {
			return List.of(theSystem);
		}
		return List.of();
	}

	/**
	 * The includes and excludes of the ValueSet and of every ValueSet they include or exclude through
	 * {@code valueSet}, each ValueSet read once so ValueSets that include each other are not read again
	 */
	private List<IBase> getIncludesAndExcludes(String theValueSetUrl) {
		List<IBase> retVal = new ArrayList<>();
		Set<String> seenValueSetUrls = new HashSet<>(List.of(theValueSetUrl));
		Deque<String> valueSetUrlsToRead = new ArrayDeque<>(List.of(theValueSetUrl));
		while (!valueSetUrlsToRead.isEmpty()) {
			for (IBase nextIncludeOrExclude : getOwnIncludesAndExcludes(valueSetUrlsToRead.pop())) {
				retVal.add(nextIncludeOrExclude);
				getValueSetUrls(nextIncludeOrExclude).stream()
						.filter(seenValueSetUrls::add)
						.forEach(valueSetUrlsToRead::push);
			}
		}
		return retVal;
	}

	/**
	 * The ValueSet's own {@code compose.include} and {@code compose.exclude} elements, or none if no module has it
	 */
	private List<IBase> getOwnIncludesAndExcludes(String theValueSetUrl) {
		IBaseResource valueSet = getValidationSupport().fetchValueSet(theValueSetUrl);
		if (valueSet == null) {
			return List.of();
		}
		return INCLUDE_AND_EXCLUDE_PATHS.stream()
				.flatMap(t -> myTerser.getValues(valueSet, t).stream())
				.toList();
	}

	/**
	 * The ValueSet URLs the include or exclude names in {@code valueSet}
	 */
	private List<String> getValueSetUrls(IBase theIncludeOrExclude) {
		return myTerser.getValues(theIncludeOrExclude, "valueSet", IPrimitiveType.class).stream()
				.map(IPrimitiveType::getValueAsString)
				.filter(StringUtils::isNotBlank)
				.toList();
	}

	/**
	 * Whether the include or exclude is of {@code theSystem}, in a version (or, with none named, the current
	 * version) that no module has. The version is read from {@code version}, or else from a {@code system} written
	 * as {@code url|version}.
	 */
	private boolean isOfUnknownSystem(IBase theIncludeOrExclude, String theSystem) {
		UrlUtil.CanonicalUrlParts system =
				UrlUtil.parseCanonicalUrl(myTerser.getSinglePrimitiveValueOrNull(theIncludeOrExclude, "system"));
		if (!theSystem.equals(system.url())) {
			return false;
		}
		String version = defaultIfBlank(
				myTerser.getSinglePrimitiveValueOrNull(theIncludeOrExclude, "version"),
				system.versionId().orElse(null));
		return !getValidationSupport().isCodeSystemSupported(myValidationSupportContext, theSystem, version);
	}

	private IValidationSupport getValidationSupport() {
		return myValidationSupportContext.getRootValidationSupport();
	}
}
