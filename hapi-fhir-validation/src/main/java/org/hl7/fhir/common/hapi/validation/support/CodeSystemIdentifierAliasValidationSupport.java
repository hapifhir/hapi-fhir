/*
 * #%L
 * HAPI FHIR - Validation Support
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
package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Resolves CodeSystem aliases expressed as OID URNs.
 *
 * <p>This validation-support module first delegates normal canonical
 * CodeSystem resolution to another {@link IValidationSupport}. If the direct
 * lookup fails and the requested system is a {@code urn:oid:...}, it searches
 * the delegate's conformance resources for a CodeSystem whose
 * {@code CodeSystem.identifier} contains:</p>
 *
 * <pre>
 * identifier.system = urn:ietf:rfc:3986
 * identifier.value  = urn:oid:...
 * </pre>
 *
 * <p>If the request contains a version, for example
 * {@code urn:oid:1.2.3|4.0.0}, the resource's {@code CodeSystem.version} must
 * match exactly.</p>
 *
 * <p>The matching CodeSystem resource is returned without changing its
 * canonical {@code CodeSystem.url}. The in-memory ValueSet expander therefore
 * loads the concepts from the resolved CodeSystem while continuing to emit the
 * system originally declared by the ValueSet include.</p>
 *
 * <p>The delegate supplied to this class must not contain this instance.
 * Otherwise recursive calls are possible. A typical configuration uses one
 * validation-support chain containing only resource sources as the delegate
 * and places this module in the complete validation-support chain before those
 * resource sources.</p>
 *
 * <p>This implementation deliberately does not alter normal FHIR REST search
 * semantics. It only affects terminology resource resolution through
 * {@link #fetchCodeSystem(String)}.</p>
 */
public class CodeSystemIdentifierAliasValidationSupport extends BaseValidationSupport {

	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(CodeSystemIdentifierAliasValidationSupport.class);

	private static final String OID_URN_PREFIX = "urn:oid:";
	private static final String RFC_3986_IDENTIFIER_SYSTEM = "urn:ietf:rfc:3986";
	private static final String PIPE = "|";

	private final IValidationSupport myResourceDelegate;

	/**
	 * Constructor.
	 *
	 * @param theFhirContext
	 * 		the FHIR context
	 * @param theResourceDelegate
	 * 		a validation support containing the actual CodeSystem resources;
	 * 		this delegate must not contain this alias support instance
	 */
	public CodeSystemIdentifierAliasValidationSupport(
			@Nonnull FhirContext theFhirContext,
			@Nonnull IValidationSupport theResourceDelegate) {
		super(theFhirContext);
		myResourceDelegate = Objects.requireNonNull(theResourceDelegate);
	}

	@Override
	public String getName() {
		return "CodeSystem Identifier Alias Validation Support";
	}

	/**
	 * Resolve a CodeSystem by canonical URL first and then, for OID URNs only,
	 * by CodeSystem.identifier.
	 */
	@Override
	@Nullable
	public IBaseResource fetchCodeSystem(String theSystem) {
		if (isBlank(theSystem)) {
			return null;
		}

		/*
		 * Preserve normal canonical resolution. The delegate must be a resource
		 * source chain which does not contain this support instance.
		 */
		IBaseResource directResult = myResourceDelegate.fetchCodeSystem(theSystem);
		if (directResult != null) {
			return directResult;
		}

		CanonicalReference requested = CanonicalReference.parse(theSystem);
		if (!requested.url().startsWith(OID_URN_PREFIX)) {
			return null;
		}

		List<ResolvedCandidate> candidates = findCandidates(requested);
		if (candidates.isEmpty()) {
			ourLog.debug(
					"No CodeSystem alias target found for {}",
					requested.asString());
			return null;
		}

		/*
		 * Collapse exact duplicate resources which represent the same canonical
		 * URL and version. Different canonical targets for one OID are ambiguous
		 * and must not be selected implicitly.
		 */
		Map<String, ResolvedCandidate> uniqueTargets = new LinkedHashMap<>();
		for (ResolvedCandidate candidate : candidates) {
			uniqueTargets.putIfAbsent(candidate.canonicalKey(), candidate);
		}

		if (uniqueTargets.size() > 1) {
			throw new IllegalStateException(
					"Ambiguous CodeSystem identifier alias "
							+ requested.asString()
							+ ". Matching canonical targets: "
							+ String.join(", ", uniqueTargets.keySet()));
		}

		ResolvedCandidate resolved = uniqueTargets.values().iterator().next();

		ourLog.info(
				"Resolved CodeSystem identifier alias {} to {}",
				requested.asString(),
				resolved.canonicalKey());

		return resolved.resource();
	}

	/**
	 * Exposes the same conformance resources as the delegate. This allows this
	 * support to be used either as a focused module in a larger chain or as a
	 * transparent wrapper around resource supports.
	 */
	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		List<IBaseResource> resources =
			myResourceDelegate.fetchAllConformanceResources();

		return resources != null
			? resources
			: List.of();
	}

	private List<ResolvedCandidate> findCandidates(CanonicalReference theRequested) {
		List<ResolvedCandidate> candidates = new ArrayList<>();

		List<IBaseResource> resources =
			myResourceDelegate.fetchAllConformanceResources();

		if (resources == null || resources.isEmpty()) {
			return candidates;
		}

		for (IBaseResource resource : resources) {
			if (!isCodeSystem(resource)) {
				continue;
			}

			String canonicalUrl = readPrimitive(resource, "url");
			if (isBlank(canonicalUrl)) {
				continue;
			}

			String resourceVersion = readPrimitive(resource, "version");
			if (isNotBlank(theRequested.version())
					&& !Objects.equals(theRequested.version(), resourceVersion)) {
				continue;
			}

			if (!hasMatchingOidIdentifier(resource, theRequested.url())) {
				continue;
			}

			candidates.add(new ResolvedCandidate(
					resource,
					canonicalUrl,
					resourceVersion));
		}

		return candidates;
	}

	private boolean isCodeSystem(IBaseResource theResource) {
		try {
			return "CodeSystem".equals(
					getFhirContext()
							.getResourceDefinition(theResource)
							.getName());
		} catch (RuntimeException e) {
			ourLog.debug(
					"Unable to determine resource type for {}",
					theResource.getClass().getName(),
					e);
			return false;
		}
	}

	private boolean hasMatchingOidIdentifier(
			IBaseResource theCodeSystem,
			String theRequestedOidUrn) {

		FhirTerser terser = getFhirContext().newTerser();
		List<IBase> identifiers = terser.getValues(
				theCodeSystem,
				"identifier");

		for (IBase identifier : identifiers) {
			String identifierSystem = readPrimitive(
					identifier,
					"system");
			String identifierValue = readPrimitive(
					identifier,
					"value");

			if (RFC_3986_IDENTIFIER_SYSTEM.equals(identifierSystem)
					&& theRequestedOidUrn.equals(identifierValue)) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	private String readPrimitive(
			IBase theElement,
			String thePath) {

		IPrimitiveType<?> value = getFhirContext()
				.newTerser()
				.getSingleValueOrNull(
						theElement,
						thePath,
						IPrimitiveType.class);

		return value != null
				? value.getValueAsString()
				: null;
	}

	private record ResolvedCandidate(
			IBaseResource resource,
			String canonicalUrl,
			@Nullable String version) {

		private String canonicalKey() {
			return isNotBlank(version)
					? canonicalUrl + PIPE + version
					: canonicalUrl;
		}
	}

	private record CanonicalReference(
			String url,
			@Nullable String version) {

		private static CanonicalReference parse(String theValue) {
			int separator = theValue.lastIndexOf(PIPE);
			if (separator < 0) {
				return new CanonicalReference(
						theValue,
						null);
			}

			return new CanonicalReference(
					theValue.substring(0, separator),
					theValue.substring(separator + 1));
		}

		private String asString() {
			return isNotBlank(version)
					? url + PIPE + version
					: url;
		}
	}
}
