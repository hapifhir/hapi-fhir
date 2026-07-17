/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.util.MatchUrlUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.TerserUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This service processes a FHIR transaction bundle and transforms all identifier inline match URL references into
 * placeholder IDs with conditional create entries.
 * <p>
 * It looks for resources that have references in the form of inline match URLs with the identifier search parameter.
 * (e.g. {@code Patient?identifier=http://some-system|some-value}) For each unique inline match URL found, it
 * <b>prepends</b> a conditional-create entry with placeholder IDs to the bundle with fields matching the identifier
 * search param.
 * <p>
 * This ensures that reference resolution happens before the resource is finalized and before partition selection in
 * Patient ID Partition mode.
 */
// Created by Claude Opus 4.7
public class TransactionBundleNormalizer {

	/**
	 * {@link ca.uhn.fhir.rest.api.server.storage.TransactionDetails} user-data key through which an
	 * interceptor requests bundle normalization for the current transaction, by setting it to
	 * {@link Boolean#TRUE} from a
	 * {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_TRANSACTION_PROCESSING} hook. Without a
	 * requester the normalizer is never invoked: normalization exists to pre-shape bundles for
	 * components that resolve the placeholders it introduces (e.g.
	 * {@code PatientIdPartitionInterceptor}), and would otherwise alter transaction processing for
	 * deployments that gain nothing from it.
	 */
	public static final String NORMALIZATION_REQUESTED_KEY =
			TransactionBundleNormalizer.class.getName() + "_normalizationRequested";

	private final FhirContext myFhirContext;
	private final MatchUrlService myMatchUrlService;

	@SuppressWarnings("rawtypes")
	private final ITransactionProcessorVersionAdapter myVersionAdapter;

	public TransactionBundleNormalizer(
			@Nonnull FhirContext theFhirContext,
			@Nonnull MatchUrlService theMatchUrlService,
			@Nonnull @SuppressWarnings("rawtypes") ITransactionProcessorVersionAdapter theVersionAdapter) {
		myFhirContext = theFhirContext;
		myMatchUrlService = theMatchUrlService;
		myVersionAdapter = theVersionAdapter;
	}

	/**
	 * Scans a transaction bundle for resources that have references in the form of identifier inline match URLs,
	 * inserts conditional-create entries at the beginning of the bundle for each unique match URL found, and replace
	 * the URLs with placeholder ids.
	 *
	 * @param theBundle the transaction bundle to process
	 * @return the number of synthetic placeholder entries prepended to the bundle (0 if no inline match URLs found)
	 */
	@SuppressWarnings("unchecked")
	public int normalize(@Nonnull IBaseBundle theBundle) {
		List<IBase> bundleEntries = myVersionAdapter.getEntries(theBundle);

		if (bundleEntries.isEmpty()) {
			return 0;
		}

		Map<IdentifierKey, String> inBundleFullUrlByIdentifier = assignFullUrlsAndIndexIdentifiers(bundleEntries);
		Map<String, MatchUrlInfo> matchUrlToInfo =
				rewriteInlineMatchUrlReferences(bundleEntries, inBundleFullUrlByIdentifier);
		return prependSyntheticEntries(theBundle, bundleEntries, matchUrlToInfo);
	}

	/**
	 * First pass: ensures every resource-bearing entry has a fullUrl (Patient ID Partition mode keys on it to
	 * pre-assign Patient POST-creates an id), and indexes in-bundle entries by resource identifier -> fullUrl.
	 * An inline match URL ref resolves against this index, so it reuses an in-bundle entry (conditional OR
	 * unconditional) instead of minting a duplicate synthetic.
	 */
	@SuppressWarnings("unchecked")
	private Map<IdentifierKey, String> assignFullUrlsAndIndexIdentifiers(List<IBase> theBundleEntries) {
		FhirTerser terser = myFhirContext.newTerser();
		Map<IdentifierKey, String> inBundleFullUrlByIdentifier = new HashMap<>();
		for (IBase entry : theBundleEntries) {
			IBaseResource resource = myVersionAdapter.getResource(entry);
			if (resource == null) {
				continue;
			}
			String fullUrl = ensureEntryFullUrl(entry, resource);

			// Key on (resourceType, system|value), first-wins. Type-aware so e.g. Patient?identifier=sys|x
			// can't resolve to a non-Patient entry that happens to carry sys|x.
			String resourceType = myFhirContext.getResourceType(resource);
			RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(resource);
			if (resourceDef.getChildByName("identifier") == null) {
				continue;
			}
			for (IBase identifier : terser.getValues(resource, "identifier")) {
				String value = terser.getSinglePrimitiveValueOrNull(identifier, "value");
				if (isBlank(value)) {
					continue;
				}
				String system = terser.getSinglePrimitiveValueOrNull(identifier, "system");
				inBundleFullUrlByIdentifier.putIfAbsent(new IdentifierKey(resourceType, system, value), fullUrl);
			}
		}
		return inBundleFullUrlByIdentifier;
	}

	/**
	 * Returns the entry's fullUrl, assigning one first if it is blank. An existing urn: resource.id (HAPI's
	 * placeholder id) is reused rather than overridden; a concrete client-assigned id becomes a type/id fullUrl,
	 * since a urn here would displace the id as the entry's identity in the transaction processor (reference
	 * substitution keys, duplicate-id detection, auto-versioned references). Only id-less entries get a
	 * generated urn.
	 */
	@SuppressWarnings("unchecked")
	private String ensureEntryFullUrl(IBase theEntry, IBaseResource theResource) {
		String fullUrl = myVersionAdapter.getFullUrl(theEntry);
		if (isBlank(fullUrl)) {
			String resourceId = theResource.getIdElement().getValue();
			if (resourceId != null && resourceId.startsWith("urn:")) {
				fullUrl = resourceId;
			} else if (theResource.getIdElement().hasIdPart()) {
				fullUrl = myFhirContext.getResourceType(theResource) + "/"
						+ theResource.getIdElement().getIdPart();
			} else {
				fullUrl = IdDt.newRandomUuid().getValue();
			}
			myVersionAdapter.setFullUrl(theEntry, fullUrl);
		}
		return fullUrl;
	}

	/**
	 * Second pass: rewrites each inline match URL reference in a write entry, either to the fullUrl of an
	 * in-bundle entry carrying the same (type, identifier), or to a synthetic placeholder minted on first
	 * encounter and reused for duplicates. Returns the minted placeholders, keyed on the raw URL string.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, MatchUrlInfo> rewriteInlineMatchUrlReferences(
			List<IBase> theBundleEntries, Map<IdentifierKey, String> theInBundleFullUrlByIdentifier) {
		FhirTerser terser = myFhirContext.newTerser();
		Map<String, MatchUrlInfo> matchUrlToInfo = new HashMap<>();
		for (IBase entry : theBundleEntries) {
			// Only process write entries (POST/PUT/PATCH); GET and DELETE entries carry no resource body to walk.
			String verb = myVersionAdapter.getEntryRequestVerb(myFhirContext, entry);
			if (!"POST".equals(verb) && !"PUT".equals(verb) && !"PATCH".equals(verb)) {
				continue;
			}
			IBaseResource resource = myVersionAdapter.getResource(entry);
			if (resource == null) {
				continue;
			}
			List<IBaseReference> refs = terser.getAllPopulatedChildElementsOfType(resource, IBaseReference.class);
			for (IBaseReference ref : refs) {
				String refValue = ref.getReferenceElement().getValue();
				if (!MatchUrlUtil.isInlineMatchUrl(refValue)) {
					continue;
				}
				MatchUrlService.ResourceTypeAndSearchParameterMap parsed =
						myMatchUrlService.parseAndTranslateMatchUrl(refValue);
				// Validate before any resolution so a URL's acceptance never depends on what else the
				// bundle happens to contain (an invalid URL must not slip through by binding in-bundle).
				List<CanonicalIdentifier> identifiers = extractAndValidateIdentifiers(refValue, parsed);

				// If an in-bundle entry already carries this (type, identifier), point the inline ref at its
				// fullUrl (assigned in the first pass) instead of minting a duplicate synthetic placeholder.
				IdentifierKey refKey = identifierKey(parsed.resourceDefinition().getName(), identifiers);
				if (refKey != null) {
					String existingFullUrl = theInBundleFullUrlByIdentifier.get(refKey);
					if (existingFullUrl != null) {
						ref.setReference(existingFullUrl);
						continue;
					}
				}

				// Otherwise, generate a synthetic conditional-create on first encounter; reuse for duplicates.
				MatchUrlInfo info = matchUrlToInfo.computeIfAbsent(
						refValue,
						url -> new MatchUrlInfo(
								IdDt.newRandomUuid().getValue(), parsed.resourceDefinition(), identifiers));
				ref.setReference(info.urnUuid());
			}
		}
		return matchUrlToInfo;
	}

	/**
	 * Appends a synthetic conditional-create entry for each minted placeholder, then rotates them to the front
	 * so they are processed before the entries referencing them, and so response cleanup can strip a fixed
	 * [0, N) range. Returns the number of entries prepended.
	 */
	private int prependSyntheticEntries(
			IBaseBundle theBundle, List<IBase> theBundleEntries, Map<String, MatchUrlInfo> theMatchUrlToInfo) {
		if (theMatchUrlToInfo.isEmpty()) {
			return 0;
		}

		// BundleBuilder preserves existing entries (since 8.6.0) so this just adds at the end.
		BundleBuilder builder = new BundleBuilder(myFhirContext, theBundle);
		for (Map.Entry<String, MatchUrlInfo> e : theMatchUrlToInfo.entrySet()) {
			String matchUrl = e.getKey();
			MatchUrlInfo info = e.getValue();
			IBaseResource placeholder = buildPlaceholder(info);
			builder.addTransactionCreateEntry(placeholder, info.urnUuid()).conditional(matchUrl);
		}

		int n = theMatchUrlToInfo.size();
		Collections.rotate(theBundleEntries, n);
		return n;
	}

	/**
	 * Removes the response entries corresponding to the synthetic entries that {@link #normalize(IBaseBundle)}
	 * prepended to the request bundle, so the response aligns 1:1 with the caller's original bundle.
	 *
	 * @param theResponse the transaction response bundle
	 * @param theSyntheticEntryCount the count returned by {@link #normalize(IBaseBundle)} for the request
	 */
	// Created by Claude Fable 5
	@SuppressWarnings("unchecked")
	public void stripSyntheticResponseEntries(@Nonnull IBaseBundle theResponse, int theSyntheticEntryCount) {
		if (theSyntheticEntryCount > 0) {
			List<IBase> entries = myVersionAdapter.getEntries(theResponse);
			entries.subList(0, theSyntheticEntryCount).clear();
		}
	}

	/**
	 * Build the {@link IdentifierKey} for an inline match URL of the form {@code Type?identifier=system|value},
	 * or {@code null} if the URL has more than one identifier token (those fall through to synthetic creation).
	 */
	private static IdentifierKey identifierKey(String theResourceType, List<CanonicalIdentifier> theIdentifiers) {
		if (theIdentifiers.size() != 1) {
			return null;
		}
		CanonicalIdentifier identifier = theIdentifiers.get(0);
		return new IdentifierKey(
				theResourceType,
				identifier.getSystemElement().getValueAsString(),
				identifier.getValueElement().getValueAsString());
	}

	/**
	 * Walks the parsed match URL once: enforces the identifier-equality-only contract and returns the
	 * identifier tokens the URL consists of.
	 */
	private List<CanonicalIdentifier> extractAndValidateIdentifiers(
			String theMatchUrl, MatchUrlService.ResourceTypeAndSearchParameterMap theParsed) {
		SearchParameterMap params = theParsed.searchParameterMap();

		if (params.keySet().size() != 1 || !params.containsKey("identifier")) {
			throw new PreconditionFailedException(Msg.code(2996)
					+ "Inline match URL matching only supports identifier search parameters: " + theMatchUrl);
		}

		List<CanonicalIdentifier> identifiers = new ArrayList<>();
		for (List<IQueryParameterType> andGroup : params.get("identifier")) {
			for (IQueryParameterType paramType : andGroup) {
				if (paramType instanceof TokenParam tokenParam) {
					if (tokenParam.getModifier() != null) {
						throw new PreconditionFailedException(Msg.code(2997)
								+ "Inline match URL identifier must not use a search modifier: " + theMatchUrl);
					}
					if (isBlank(tokenParam.getSystem()) || isBlank(tokenParam.getValue())) {
						throw new PreconditionFailedException(Msg.code(2995)
								+ "Inline match URL identifier must have both a system and a value: " + theMatchUrl);
					}
					CanonicalIdentifier identifier = new CanonicalIdentifier();
					identifier.setSystem(tokenParam.getSystem());
					identifier.setValue(tokenParam.getValue());
					identifiers.add(identifier);
				}
			}
		}
		return identifiers;
	}

	private IBaseResource buildPlaceholder(MatchUrlInfo theInfo) {
		IBaseResource placeholder = theInfo.resourceDef().newInstance();
		for (CanonicalIdentifier identifier : theInfo.identifiers()) {
			TerserUtil.addIdentifierToResource(
					myFhirContext,
					placeholder,
					identifier.getSystemElement().getValueAsString(),
					identifier.getValueElement().getValueAsString());
		}
		ExtensionUtil.addExtensionIfSupported(
				myFhirContext, placeholder, HapiExtensions.EXT_RESOURCE_PLACEHOLDER, "boolean", Boolean.TRUE);
		return placeholder;
	}

	private record MatchUrlInfo(
			String urnUuid, RuntimeResourceDefinition resourceDef, List<CanonicalIdentifier> identifiers) {}

	/** Key matching an inline match URL reference to an in-bundle entry by resource type + identifier token. */
	private record IdentifierKey(String resourceType, String system, String value) {
		private IdentifierKey {
			// Normalise a blank system to null so absent/"" compare equal across resource identifiers and refs.
			system = isBlank(system) ? null : system;
		}
	}
}
