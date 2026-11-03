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
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Checks on the CodeSystems a ValueSet uses
 *
 * @since 9.0.0
 */
// Created by Claude Opus 5.5
public final class ValueSetCodeSystemUtil {

	private ValueSetCodeSystemUtil() {}

	/**
	 * Whether the ValueSet, or a ValueSet it includes or excludes through {@code valueSet}, has an include or exclude
	 * of the given code system, or of a version of it, that no validation support module has. A code reported as not
	 * in such a ValueSet may have been checked against another version of the code system, or only against the codes
	 * the ValueSet lists, so the result does not show that the code is outside the ValueSet.
	 *
	 * @param theValidationSupport        The validation support module that fetches the ValueSets and is asked
	 *                                    whether it supports the code systems
	 * @param theValidationSupportContext The validation support context
	 * @param theTerser                   A terser for the FHIR version of the ValueSets
	 * @param theValueSetUrl              The ValueSet URL
	 * @param theSystem                   The code system URL of the coding being checked
	 * @return {@literal true} if the ValueSet uses {@code theSystem}, or a version of it, that no module has;
	 * {@literal false} if it does not, or if the ValueSet cannot be fetched
	 */
	public static boolean valueSetUsesMissingCodeSystem(
			@Nonnull IValidationSupport theValidationSupport,
			@Nonnull ValidationSupportContext theValidationSupportContext,
			@Nonnull FhirTerser theTerser,
			@Nonnull String theValueSetUrl,
			@Nonnull String theSystem) {
		return valueSetUsesMissingCodeSystem(
				theValidationSupport,
				theValidationSupportContext,
				theTerser,
				theValueSetUrl,
				theSystem,
				new HashSet<>());
	}

	/**
	 * As {@link #valueSetUsesMissingCodeSystem(IValidationSupport, ValidationSupportContext, FhirTerser, String,
	 * String)}, skipping ValueSets already in {@literal theVisitedValueSetUrls} so a cycle of {@code valueSet}
	 * references ends
	 */
	private static boolean valueSetUsesMissingCodeSystem(
			IValidationSupport theValidationSupport,
			ValidationSupportContext theValidationSupportContext,
			FhirTerser theTerser,
			String theValueSetUrl,
			String theSystem,
			Set<String> theVisitedValueSetUrls) {
		if (!theVisitedValueSetUrls.add(theValueSetUrl)) {
			return false;
		}
		IBaseResource valueSet = theValidationSupport.fetchValueSet(theValueSetUrl);
		if (valueSet == null) {
			return false;
		}
		for (String nextPath : List.of("compose.include", "compose.exclude")) {
			for (IBase nextIncludeOrExclude : theTerser.getValues(valueSet, nextPath)) {
				String system = theTerser.getSinglePrimitiveValueOrNull(nextIncludeOrExclude, "system");
				String version = theTerser.getSinglePrimitiveValueOrNull(nextIncludeOrExclude, "version");
				if (theSystem.equals(system)
						&& !(isNotBlank(version)
								? theValidationSupport.isCodeSystemSupported(
										theValidationSupportContext, system, version)
								: theValidationSupport.isCodeSystemSupported(theValidationSupportContext, system))) {
					return true;
				}
				for (IPrimitiveType<?> nextValueSet :
						theTerser.getValues(nextIncludeOrExclude, "valueSet", IPrimitiveType.class)) {
					String includedValueSetUrl = nextValueSet.getValueAsString();
					if (isNotBlank(includedValueSetUrl)
							&& valueSetUsesMissingCodeSystem(
									theValidationSupport,
									theValidationSupportContext,
									theTerser,
									includedValueSetUrl,
									theSystem,
									theVisitedValueSetUrls)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
