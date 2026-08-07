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
 * Utility for matching canonical resources using their formal identifiers.
 */
public final class CanonicalResourceIdentifierMatcher {

	private static final String PIPE = "|";

	private CanonicalResourceIdentifierMatcher() {
		// Utility class
	}

	/**
	 * Finds a canonical resource using an exact identifier system/value match
	 * and, when supplied, an exact resource version match.
	 *
	 * <p>Duplicate resources representing the same canonical URL and version
	 * are collapsed. Different canonical targets are considered ambiguous.</p>
	 *
	 * @param theFhirContext
	 * 		the FHIR context
	 * @param theCandidates
	 * 		candidate resources already selected by the backend
	 * @param theRequest
	 * 		the identifier lookup request
	 * @return the resolved canonical resource, or {@code null}
	 */
	@Nullable
	public static IBaseResource findMatch(
			@Nonnull FhirContext theFhirContext,
			@Nullable Iterable<? extends IBaseResource> theCandidates,
			@Nonnull CanonicalResourceIdentifierRequest theRequest) {

		Objects.requireNonNull(theFhirContext);
		Objects.requireNonNull(theRequest);

		if (theCandidates == null) {
			return null;
		}

		Map<String, IBaseResource> uniqueTargets = new LinkedHashMap<>();

		for (IBaseResource candidate : theCandidates) {
			if (candidate == null
					|| !hasIdentifier(
							theFhirContext, candidate, theRequest.identifierSystem(), theRequest.identifierValue())) {
				continue;
			}

			String resourceVersion = readPrimitive(theFhirContext, candidate, "version");

			if (isNotBlank(theRequest.version()) && !Objects.equals(theRequest.version(), resourceVersion)) {
				continue;
			}

			String canonicalUrl = readPrimitive(theFhirContext, candidate, "url");

			/*
			 * A canonical resource without a canonical URL is not a valid
			 * alias target.
			 */
			if (isBlank(canonicalUrl)) {
				continue;
			}

			String canonicalKey = isNotBlank(resourceVersion) ? canonicalUrl + PIPE + resourceVersion : canonicalUrl;

			/*
			 * Collapse duplicate representations of the same canonical
			 * resource target.
			 */
			uniqueTargets.putIfAbsent(canonicalKey, candidate);
		}

		if (uniqueTargets.isEmpty()) {
			return null;
		}

		if (uniqueTargets.size() > 1) {
			throw new IllegalStateException("Ambiguous "
					+ theRequest.resourceType()
					+ " identifier "
					+ theRequest.identifierSystem()
					+ PIPE
					+ theRequest.identifierValue()
					+ versionSuffix(theRequest.version())
					+ ". Matching canonical targets: "
					+ String.join(", ", uniqueTargets.keySet()));
		}

		return uniqueTargets.values().iterator().next();
	}

	private static boolean hasIdentifier(
			FhirContext theFhirContext,
			IBaseResource theResource,
			String theIdentifierSystem,
			String theIdentifierValue) {

		FhirTerser terser = theFhirContext.newTerser();
		List<IBase> identifiers = terser.getValues(theResource, "identifier");

		for (IBase identifier : identifiers) {
			String system = readPrimitive(theFhirContext, identifier, "system");

			String value = readPrimitive(theFhirContext, identifier, "value");

			if (theIdentifierSystem.equals(system) && theIdentifierValue.equals(value)) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	private static String readPrimitive(FhirContext theFhirContext, IBase theElement, String thePath) {

		IPrimitiveType<?> value =
				theFhirContext.newTerser().getSingleValueOrNull(theElement, thePath, IPrimitiveType.class);

		return value != null ? value.getValueAsString() : null;
	}

	private static String versionSuffix(@Nullable String theVersion) {
		return isNotBlank(theVersion) ? PIPE + theVersion : "";
	}
}
