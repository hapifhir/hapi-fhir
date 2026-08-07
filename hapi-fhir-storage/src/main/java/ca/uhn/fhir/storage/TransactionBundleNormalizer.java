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
 * It looks for resources that have references in the form of inline match URLs with a single identifier search
 * parameter. (e.g. {@code Patient?identifier=http://some-system|some-value}) For each unique inline match URL found, it
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

		Map<String, String> inBundleFullUrlByConditionalUrl = assignFullUrlsAndIndexConditionalUrls(bundleEntries);
		Map<String, MatchUrlInfo> matchUrlToInfo =
				rewriteInlineMatchUrlReferences(bundleEntries, inBundleFullUrlByConditionalUrl);
		return prependSyntheticEntries(theBundle, bundleEntries, matchUrlToInfo, theTransactionDetails);
	}

	/**
	 * First pass: ensures every resource-bearing entry has a fullUrl (Patient ID Partition mode keys on it to
	 * pre-assign Patient POST-creates an id), and indexes conditional write entries by conditional URL ->
	 * fullUrl. An inline match URL ref that is the same question as an in-bundle conditional write — same URL,
	 * hence necessarily the same referent — reuses that entry instead of minting a duplicate synthetic.
	 * Resource bodies are deliberately not consulted: an entry only binds a reference through its request URL,
	 * so references resolve against the store the same way they would outside patient id partition mode.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> assignFullUrlsAndIndexConditionalUrls(List<IBase> theBundleEntries) {
		Map<String, String> inBundleFullUrlByConditionalUrl = new HashMap<>();
		for (IBase entry : theBundleEntries) {
			IBaseResource resource = myVersionAdapter.getResource(entry);
			if (resource == null) {
				continue;
			}
			String fullUrl = ensureEntryFullUrl(entry, resource);

			String verb = myVersionAdapter.getEntryRequestVerb(myFhirContext, entry);
			String conditionalUrl = null;
			if ("POST".equals(verb)) {
				conditionalUrl = myVersionAdapter.getEntryIfNoneExist(entry);
			} else if ("PUT".equals(verb)) {
				String requestUrl = myVersionAdapter.getEntryRequestUrl(entry);
				if (requestUrl != null && requestUrl.contains("?")) {
					conditionalUrl = requestUrl;
				}
			}
			if (isBlank(conditionalUrl)) {
				continue;
			}
			// A spec-form ifNoneExist may carry only the query part; qualify it with the entry's resource type
			// so it compares against inline references, which always start with the type.
			if (!conditionalUrl.contains("?")) {
				conditionalUrl = myFhirContext.getResourceType(resource) + "?" + conditionalUrl;
			}
			inBundleFullUrlByConditionalUrl.putIfAbsent(conditionalUrl, fullUrl);
		}
		return inBundleFullUrlByConditionalUrl;
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
	 * in-bundle conditional write entry on the same conditional URL, or to a synthetic placeholder minted on
	 * first encounter and reused for duplicates. Returns the minted placeholders, keyed on the raw URL string.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, MatchUrlInfo> rewriteInlineMatchUrlReferences(
			List<IBase> theBundleEntries, Map<String, String> theInBundleFullUrlByConditionalUrl) {
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
				CanonicalIdentifier identifier = extractAndValidateIdentifier(refValue, parsed);

				// If an in-bundle conditional write already asks this exact conditional URL, point the inline
				// ref at its fullUrl (assigned in the first pass) instead of minting a duplicate synthetic.
				String existingFullUrl = theInBundleFullUrlByConditionalUrl.get(refValue);
				if (existingFullUrl != null) {
					ref.setReference(existingFullUrl);
					continue;
				}

				// Otherwise, generate a synthetic conditional-create on first encounter; reuse for duplicates.
				MatchUrlInfo info = matchUrlToInfo.computeIfAbsent(
						refValue,
						url -> new MatchUrlInfo(
								IdDt.newRandomUuid().getValue(),
								parsed.resourceDefinition(),
								identifier,
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
					myFhirContext, info.resourceDef(), List.of(info.identifier()));
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
	 * Walks the parsed match URL once: enforces the single-identifier-equality-only contract and returns the
	 * identifier token the URL consists of.
	 */
	private CanonicalIdentifier extractAndValidateIdentifier(
			String theMatchUrl, MatchUrlService.ResourceTypeAndSearchParameterMap theParsed) {
		SearchParameterMap params = theParsed.searchParameterMap();

		if (params.keySet().size() != 1 || !params.containsKey("identifier")) {
			throw new PreconditionFailedException(Msg.code(2996)
					+ "Inline match URL matching only supports identifier search parameters in patient id partition mode: "
					+ theMatchUrl);
		}

		List<List<IQueryParameterType>> andGroups = params.get("identifier");
		if (andGroups.size() != 1
				|| andGroups.get(0).size() != 1
				|| !(andGroups.get(0).get(0) instanceof TokenParam tokenParam)) {
			throw new PreconditionFailedException(Msg.code(3025)
					+ "Inline match URL matching only supports a single identifier in patient id partition mode: "
					+ theMatchUrl);
		}

		if (tokenParam.getModifier() != null) {
			throw new PreconditionFailedException(Msg.code(3024)
					+ "Inline match URL identifier must not use a search modifier in patient id partition mode: "
					+ theMatchUrl);
		}
		if (isBlank(tokenParam.getSystem()) || isBlank(tokenParam.getValue())) {
			throw new PreconditionFailedException(Msg.code(2995)
					+ "Inline match URL identifier must have both a system and a value in patient id partition mode: "
					+ theMatchUrl);
		}

		CanonicalIdentifier identifier = new CanonicalIdentifier();
		identifier.setSystem(tokenParam.getSystem());
		identifier.setValue(tokenParam.getValue());
		return identifier;
	}

	private record MatchUrlInfo(
			String urnUuid,
			RuntimeResourceDefinition resourceDef,
			CanonicalIdentifier identifier,
			int firstReferencerEntryIndex) {}
}
