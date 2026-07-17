/*
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Utility for resolving CodeSystem resources by CodeSystem.identifier.
 */
public final class CodeSystemIdentifierResolver {

	private static final String PIPE = "|";

	private CodeSystemIdentifierResolver() {
		// Utility class
	}

	/**
	 * Finds a CodeSystem using an exact identifier system/value match and,
	 * when supplied, an exact CodeSystem.version match.
	 *
	 * <p>Duplicate resources representing the same canonical URL and version
	 * are collapsed. Different canonical targets are considered ambiguous.</p>
	 *
	 * @param theFhirContext
	 * 		the FHIR context
	 * @param theResources
	 * 		CodeSystem candidates or a broader collection of conformance resources
	 * @param theIdentifierSystem
	 * 		the exact CodeSystem.identifier.system value
	 * @param theIdentifierValue
	 * 		the exact CodeSystem.identifier.value value
	 * @param theVersion
	 * 		optional exact CodeSystem.version
	 * @return the resolved CodeSystem, or {@code null}
	 */
	@Nullable
	public static IBaseResource findCodeSystem(
			@Nonnull FhirContext theFhirContext,
			@Nullable Iterable<? extends IBaseResource> theResources,
			@Nonnull String theIdentifierSystem,
			@Nonnull String theIdentifierValue,
			@Nullable String theVersion) {

		Objects.requireNonNull(theFhirContext);
		Objects.requireNonNull(theIdentifierSystem);
		Objects.requireNonNull(theIdentifierValue);

		if (theResources == null) {
			return null;
		}

		Map<String, IBaseResource> uniqueTargets = new LinkedHashMap<>();

		for (IBaseResource resource : theResources) {
			if (resource == null
				|| !isCodeSystem(theFhirContext, resource)
				|| !hasIdentifier(
					theFhirContext,
					resource,
					theIdentifierSystem,
					theIdentifierValue)) {
				continue;
			}

			String resourceVersion = readPrimitive(
				theFhirContext,
				resource,
				"version");

			if (isNotBlank(theVersion) && !Objects.equals(theVersion, resourceVersion)) {
				continue;
			}

			String canonicalUrl = readPrimitive(
				theFhirContext,
				resource,
				"url");

			/*
			 * A CodeSystem without a canonical URL is not a valid alias target.
			 */
			if (isBlank(canonicalUrl)) {
				continue;
			}

			String canonicalKey = isNotBlank(resourceVersion) ? canonicalUrl + PIPE + resourceVersion : canonicalUrl;

			/*
			 * Collapse duplicate representations of the same canonical
			 * CodeSystem target.
			 */
			uniqueTargets.putIfAbsent(canonicalKey, resource);
		}

		if (uniqueTargets.isEmpty()) {
			return null;
		}

		if (uniqueTargets.size() > 1) {
			throw new IllegalStateException(
				"Ambiguous CodeSystem identifier "
					+ theIdentifierSystem
					+ PIPE
					+ theIdentifierValue
					+ versionSuffix(theVersion)
					+ ". Matching canonical targets: "
					+ String.join(", ", uniqueTargets.keySet()));
		}

		return uniqueTargets
			.values()
			.iterator()
			.next();
	}

	private static boolean isCodeSystem(
			FhirContext theFhirContext,
			IBaseResource theResource) {

		try {
			return "CodeSystem".equals(
				theFhirContext
					.getResourceDefinition(theResource)
					.getName());
		} catch (RuntimeException e) {
			return false;
		}
	}

	private static boolean hasIdentifier(
			FhirContext theFhirContext,
			IBaseResource theCodeSystem,
			String theIdentifierSystem,
			String theIdentifierValue) {

		FhirTerser terser = theFhirContext.newTerser();

		List<IBase> identifiers = terser.getValues(
			theCodeSystem,
			"identifier");

		for (IBase identifier : identifiers) {

			String system = readPrimitive(
						theFhirContext,
						identifier,
						"system");
			String value = readPrimitive(
						theFhirContext,
						identifier,
						"value");

			if (theIdentifierSystem.equals(system) && theIdentifierValue.equals(value)) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	private static String readPrimitive(
			FhirContext theFhirContext,
			IBase theElement,
			String thePath) {

		IPrimitiveType<?> value = theFhirContext
						.newTerser()
						.getSingleValueOrNull(
							theElement,
							thePath,
							IPrimitiveType.class);

		return value != null ? value.getValueAsString() : null;
	}

	private static String versionSuffix(
			@Nullable String theVersion) {
		return isNotBlank(theVersion) ? PIPE + theVersion : "";
	}
}
