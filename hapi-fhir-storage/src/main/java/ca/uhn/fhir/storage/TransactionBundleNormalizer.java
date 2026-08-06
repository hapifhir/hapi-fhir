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
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.util.MatchUrlUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	 * {@link ca.uhn.fhir.rest.api.server.storage.TransactionDetails} user-data key under which
	 * {@link #normalize(IBaseBundle, TransactionDetails)} records the number of synthetic entries it prepended
	 * to the request bundle, for {@link #stripSyntheticResponseEntries(IBaseBundle, TransactionDetails)} to
	 * consume once the response has been finalized.
	 */
	public static final String SYNTHETIC_ENTRY_COUNT_KEY =
			TransactionBundleNormalizer.class.getName() + "_syntheticEntryCount";

	/**
	 * {@link ca.uhn.fhir.rest.api.server.storage.TransactionDetails} user-data key holding, per synthetic
	 * entry, the post-normalization index of the first entry referencing it — the entry that gets the
	 * placeholder-created issue when the synthetic actually creates its target.
	 */
	private static final String SYNTHETIC_FIRST_REFERENCER_INDICES_KEY =
			TransactionBundleNormalizer.class.getName() + "_syntheticFirstReferencerIndices";

	private final FhirContext myFhirContext;
	private final MatchUrlService myMatchUrlService;
	private final StorageSettings myStorageSettings;

	@SuppressWarnings("rawtypes")
	private final ITransactionProcessorVersionAdapter myVersionAdapter;

	public TransactionBundleNormalizer(
			@Nonnull FhirContext theFhirContext,
			@Nonnull MatchUrlService theMatchUrlService,
			@Nonnull @SuppressWarnings("rawtypes") ITransactionProcessorVersionAdapter theVersionAdapter,
			@Nonnull StorageSettings theStorageSettings) {
		myFhirContext = theFhirContext;
		myMatchUrlService = theMatchUrlService;
		myVersionAdapter = theVersionAdapter;
		myStorageSettings = theStorageSettings;
	}

	/**
	 * Scans a transaction bundle for resources that have references in the form of identifier inline match URLs,
	 * inserts conditional-create entries at the beginning of the bundle for each unique match URL found, and replace
	 * the URLs with placeholder ids. The count of inserted entries is recorded on the supplied
	 * {@link TransactionDetails} under {@link #SYNTHETIC_ENTRY_COUNT_KEY}.
	 * <p>
	 * No-op unless the bundle is a transaction bundle and both {@code allowInlineMatchUrlReferences} and
	 * {@code autoCreatePlaceholderReferenceTargets} are enabled — the synthetic entries this method introduces
	 * are conditional creates for reference targets, which is exactly placeholder auto-creation.
	 *
	 * @param theBundle the transaction bundle to process
	 * @param theTransactionDetails the transaction details of the transaction processing this bundle
	 */
	public void normalize(@Nonnull IBaseBundle theBundle, @Nonnull TransactionDetails theTransactionDetails) {
		int syntheticEntryCount = normalizeIfEnabled(theBundle, theTransactionDetails);
		theTransactionDetails.putUserData(SYNTHETIC_ENTRY_COUNT_KEY, syntheticEntryCount);
	}

	@SuppressWarnings("unchecked")
	private int normalizeIfEnabled(IBaseBundle theBundle, TransactionDetails theTransactionDetails) {
		String bundleTypeCode = myVersionAdapter.getBundleType(theBundle);
		boolean isTransactionBundle = bundleTypeCode == null
				|| org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode().equals(bundleTypeCode);
		if (!isTransactionBundle
				|| !myStorageSettings.isAllowInlineMatchUrlReferences()
				|| !myStorageSettings.isAutoCreatePlaceholderReferenceTargets()) {
			return 0;
		}

		List<IBase> bundleEntries = myVersionAdapter.getEntries(theBundle);

		if (bundleEntries.isEmpty()) {
			return 0;
		}

		Map<IdentifierKey, String> inBundleFullUrlByIdentifier = assignFullUrlsAndIndexIdentifiers(bundleEntries);
		Map<String, MatchUrlInfo> matchUrlToInfo =
				rewriteInlineMatchUrlReferences(bundleEntries, inBundleFullUrlByIdentifier);
		return prependSyntheticEntries(theBundle, bundleEntries, matchUrlToInfo, theTransactionDetails);
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
		Map<String, MatchUrlInfo> matchUrlToInfo = new LinkedHashMap<>();
		for (int entryIndex = 0; entryIndex < theBundleEntries.size(); entryIndex++) {
			IBase entry = theBundleEntries.get(entryIndex);
			int firstReferencerEntryIndex = entryIndex;
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
								IdDt.newRandomUuid().getValue(),
								parsed.resourceDefinition(),
								identifiers,
								firstReferencerEntryIndex));
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
			IBaseBundle theBundle,
			List<IBase> theBundleEntries,
			Map<String, MatchUrlInfo> theMatchUrlToInfo,
			TransactionDetails theTransactionDetails) {
		if (theMatchUrlToInfo.isEmpty()) {
			return 0;
		}

		// BundleBuilder preserves existing entries, so this just adds at the end.
		int n = theMatchUrlToInfo.size();
		BundleBuilder builder = new BundleBuilder(myFhirContext, theBundle);
		List<Integer> firstReferencerIndices = new ArrayList<>(n);
		for (Map.Entry<String, MatchUrlInfo> e : theMatchUrlToInfo.entrySet()) {
			String matchUrl = e.getKey();
			MatchUrlInfo info = e.getValue();
			IBaseResource placeholder = PlaceholderResourceUtil.buildPlaceholderResource(
					myFhirContext, info.resourceDef(), info.identifiers());
			builder.addTransactionCreateEntry(placeholder, info.urnUuid()).conditional(matchUrl);
			// Original entries shift right when the synthetics rotate to the front.
			firstReferencerIndices.add(info.firstReferencerEntryIndex() + n);
		}
		theTransactionDetails.putUserData(SYNTHETIC_FIRST_REFERENCER_INDICES_KEY, firstReferencerIndices);

		Collections.rotate(theBundleEntries, n);
		return n;
	}

	/**
	 * Removes the response entries corresponding to the synthetic entries that {@link #normalize(IBaseBundle)}
	 * prepended to the request bundle, so the response aligns 1:1 with the caller's original bundle.
	 *
	 * @param theResponse the transaction response bundle
	 * @param theTransactionDetails the transaction details on which {@link #normalize(IBaseBundle,
	 *            TransactionDetails)} recorded the synthetic entry count for the request
	 */
	// Created by Claude Fable 5
	@SuppressWarnings("unchecked")
	public void stripSyntheticResponseEntries(
			@Nonnull IBaseBundle theResponse, @Nonnull TransactionDetails theTransactionDetails) {
		Integer syntheticEntryCount = theTransactionDetails.getUserData(SYNTHETIC_ENTRY_COUNT_KEY);
		if (syntheticEntryCount == null || syntheticEntryCount == 0) {
			return;
		}
		List<IBase> entries = myVersionAdapter.getEntries(theResponse);
		reportCreatedPlaceholders(theTransactionDetails, entries, syntheticEntryCount);
		entries.subList(0, syntheticEntryCount).clear();
	}

	/**
	 * A synthetic that actually created its placeholder (201, vs a no-op conditional match) is placeholder
	 * auto-creation the client must hear about, matching the notification the resolver's auto-create path
	 * produces. Since the synthetic's own response entry is about to be stripped, the issue goes on the
	 * first referencing entry's outcome.
	 */
	// Created by Claude Fable 5
	@SuppressWarnings("unchecked")
	private void reportCreatedPlaceholders(
			TransactionDetails theTransactionDetails, List<IBase> theResponseEntries, int theSyntheticEntryCount) {
		List<Integer> firstReferencerIndices =
				theTransactionDetails.getUserData(SYNTHETIC_FIRST_REFERENCER_INDICES_KEY);
		if (firstReferencerIndices == null) {
			return;
		}
		FhirTerser terser = myFhirContext.newTerser();
		for (int i = 0; i < theSyntheticEntryCount; i++) {
			IBase syntheticEntry = theResponseEntries.get(i);
			String status = terser.getSinglePrimitiveValueOrNull(syntheticEntry, "response.status");
			if (status == null || !status.startsWith("201")) {
				continue;
			}
			IBaseOperationOutcome referencerOutcome =
					myVersionAdapter.getResponseOutcome(theResponseEntries.get(firstReferencerIndices.get(i)));
			if (referencerOutcome == null) {
				continue;
			}
			String location = myVersionAdapter.getResponseLocation(syntheticEntry);
			BaseStorageDao.addIssueToOperationOutcomeForAutoCreatedPlaceholder(
					myFhirContext, new IdDt(location).toUnqualifiedVersionless(), referencerOutcome);
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

	private record MatchUrlInfo(
			String urnUuid,
			RuntimeResourceDefinition resourceDef,
			List<CanonicalIdentifier> identifiers,
			int firstReferencerEntryIndex) {}

	/** Key matching an inline match URL reference to an in-bundle entry by resource type + identifier token. */
	private record IdentifierKey(String resourceType, String system, String value) {
		private IdentifierKey {
			// Normalise a blank system to null so absent/"" compare equal across resource identifiers and refs.
			system = isBlank(system) ? null : system;
		}
	}
}
