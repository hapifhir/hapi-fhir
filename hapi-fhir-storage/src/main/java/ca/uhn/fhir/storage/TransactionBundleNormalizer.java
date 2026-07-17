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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.MatchUrlUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.TerserUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
	 * @return the changes made to the bundle, to be undone via
	 *         {@link #undoRequestBundleChanges(IBaseBundle, NormalizationState, boolean)} after processing
	 */
	@Nonnull
	@SuppressWarnings("unchecked")
	public NormalizationState normalize(@Nonnull IBaseBundle theBundle) {
		NormalizationState state = new NormalizationState();
		List<IBase> bundleEntries = myVersionAdapter.getEntries(theBundle);

		if (bundleEntries.isEmpty()) {
			return state;
		}

		FhirTerser terser = myFhirContext.newTerser();

		// First pass: ensure every resource-bearing entry has a placeholder fullUrl (Patient ID Partition
		// mode keys on it to pre-assign Patient POST-creates an id), and index in-bundle write entries by
		// their resource identifier -> fullUrl. An inline match URL ref below resolves against this index, so
		// it reuses an in-bundle entry (conditional OR unconditional) instead of minting a duplicate synthetic.
		Map<IdentifierKey, String> inBundleFullUrlByIdentifier = new HashMap<>();
		for (IBase entry : bundleEntries) {
			IBaseResource resource = myVersionAdapter.getResource(entry);
			if (resource == null) {
				continue;
			}
			if (isBlank(myVersionAdapter.getFullUrl(entry))) {
				// Reuse an existing urn: resource.id (HAPI's placeholder id) rather than override it. A concrete
				// client-assigned id becomes a type/id fullUrl: a urn here would displace the id as the entry's
				// identity in the transaction processor (reference substitution keys, duplicate-id detection,
				// auto-versioned references). Only id-less entries get a generated urn.
				String resourceId = resource.getIdElement().getValue();
				String fullUrl;
				if (resourceId != null && resourceId.startsWith("urn:")) {
					fullUrl = resourceId;
				} else if (resource.getIdElement().hasIdPart()) {
					fullUrl = myFhirContext.getResourceType(resource) + "/"
							+ resource.getIdElement().getIdPart();
				} else {
					fullUrl = "urn:uuid:" + UUID.randomUUID();
				}
				myVersionAdapter.setFullUrl(entry, fullUrl);
				state.myEntriesWithInjectedFullUrl.add(entry);
			}

			String fullUrl = myVersionAdapter.getFullUrl(entry);
			if (isBlank(fullUrl)) {
				continue;
			}
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

		// Second pass: rewrite each inline match URL reference. The matchUrlToInfo map (synthetic placeholders)
		// is keyed on the raw URL string; order among synthetic entries doesn't matter — the count is what
		// matters for response cleanup.
		Map<String, MatchUrlInfo> matchUrlToInfo = new HashMap<>();
		for (IBase entry : bundleEntries) {
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
				MatchUrlService.ResourceTypeAndSearchParameterMap parsed;
				try {
					parsed = myMatchUrlService.parseAndTranslateMatchUrl(refValue);
				} catch (InvalidRequestException e) {
					// Unparseable match URL: pass through; write-time resolution reports the parse error.
					continue;
				}

				// If an in-bundle entry already carries this (type, identifier), point the inline ref at its
				// fullUrl (assigned in the first pass) instead of minting a duplicate synthetic placeholder.
				IdentifierKey refKey = identifierKey(parsed);
				if (refKey != null) {
					String existingFullUrl = inBundleFullUrlByIdentifier.get(refKey);
					if (existingFullUrl != null) {
						ref.setReference(existingFullUrl);
						state.myRewrittenReferences.add(new RewrittenReference(ref, refValue, existingFullUrl));
						continue;
					}
				}

				// Match URLs the normalizer can't claim (non-identifier or system-less/value-less tokens)
				// pass through untouched: write-time inline reference resolution handles them exactly as it
				// would outside a transaction (search, auto-create where possible, or an honest error).
				if (!isEligibleForSyntheticCreate(parsed)) {
					continue;
				}

				// Otherwise, generate a synthetic conditional-create on first encounter; reuse for duplicates.
				MatchUrlInfo info = matchUrlToInfo.computeIfAbsent(
						refValue, url -> new MatchUrlInfo("urn:uuid:" + UUID.randomUUID(), parsed));
				ref.setReference(info.urnUuid());
				state.myRewrittenReferences.add(new RewrittenReference(ref, refValue, info.urnUuid()));
			}
		}

		if (matchUrlToInfo.isEmpty()) {
			return state;
		}

		// Append synthetic conditional-create entries directly to theBundle. BundleBuilder preserves
		// existing entries (since 8.6.0) so this just adds at the end.
		BundleBuilder builder = new BundleBuilder(myFhirContext, theBundle);
		for (Map.Entry<String, MatchUrlInfo> e : matchUrlToInfo.entrySet()) {
			String matchUrl = e.getKey();
			MatchUrlInfo info = e.getValue();
			IBaseResource placeholder = buildPlaceholder(info.parsed());
			builder.addTransactionCreateEntry(placeholder, info.urnUuid()).conditional(matchUrl);
		}

		// Rotate the just-appended entries to the front so they're processed before the entries
		// referencing them, and so response cleanup can strip a fixed [0, N) range.
		int n = matchUrlToInfo.size();
		state.mySyntheticEntries.addAll(bundleEntries.subList(bundleEntries.size() - n, bundleEntries.size()));
		Collections.rotate(bundleEntries, n);

		return state;
	}

	/**
	 * Undoes {@link #normalize(IBaseBundle)}'s changes to the caller's request bundle after processing:
	 * removes the synthetic entries and the injected fullUrls in every case, and on failure additionally
	 * restores the rewritten references to their original inline match URLs — mirroring how transaction
	 * rollback restores the substitutions processing itself made. On success the references are left as
	 * processing substituted them (concrete created ids), matching a transaction without normalization.
	 * <p>
	 * A reference is only restored while it still holds the value the normalizer set: on a partially
	 * committed multi-partition transaction, references already substituted to concrete ids by a committed
	 * slice keep them, exactly as they would without normalization.
	 */
	// Created by Claude Fable 5
	@SuppressWarnings("unchecked")
	public void undoRequestBundleChanges(IBaseBundle theRequest, NormalizationState theState, boolean theSucceeded) {
		if (!theState.mySyntheticEntries.isEmpty()) {
			Set<IBase> syntheticEntries = Collections.newSetFromMap(new IdentityHashMap<>());
			syntheticEntries.addAll(theState.mySyntheticEntries);
			myVersionAdapter.getEntries(theRequest).removeIf(syntheticEntries::contains);
		}
		for (IBase entry : theState.myEntriesWithInjectedFullUrl) {
			myVersionAdapter.setFullUrl(entry, null);
		}
		if (!theSucceeded) {
			for (RewrittenReference rewritten : theState.myRewrittenReferences) {
				IBaseReference ref = rewritten.reference();
				if (rewritten.rewrittenValue().equals(ref.getReferenceElement().getValue())) {
					ref.setReference(rewritten.originalValue());
				}
			}
		}
	}

	/**
	 * The changes {@link #normalize(IBaseBundle)} made to a request bundle. Opaque except for the
	 * synthetic entry count, which callers need to strip the corresponding response entries.
	 */
	// Created by Claude Fable 5
	public static final class NormalizationState {
		private final List<IBase> mySyntheticEntries = new ArrayList<>();
		private final List<IBase> myEntriesWithInjectedFullUrl = new ArrayList<>();
		private final List<RewrittenReference> myRewrittenReferences = new ArrayList<>();

		public int getSyntheticEntryCount() {
			return mySyntheticEntries.size();
		}
	}

	// Created by Claude Fable 5
	private record RewrittenReference(IBaseReference reference, String originalValue, String rewrittenValue) {}

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
	 * or {@code null} if it isn't a single-token identifier match URL (those fall through to synthetic creation).
	 */
	private IdentifierKey identifierKey(MatchUrlService.ResourceTypeAndSearchParameterMap theParsed) {
		SearchParameterMap params = theParsed.searchParameterMap();
		if (params.keySet().size() != 1 || !params.containsKey("identifier")) {
			return null;
		}
		List<List<IQueryParameterType>> identifierValues = params.get("identifier");
		if (identifierValues.size() != 1 || identifierValues.get(0).size() != 1) {
			return null;
		}
		if (!(identifierValues.get(0).get(0) instanceof TokenParam tokenParam)
				|| isBlank(tokenParam.getSystem())
				|| isBlank(tokenParam.getValue())) {
			return null;
		}
		return new IdentifierKey(
				theParsed.resourceDefinition().getName(), tokenParam.getSystem(), tokenParam.getValue());
	}

	/**
	 * A synthetic conditional create is only minted for match URLs consisting solely of {@code identifier}
	 * tokens that each carry a system and a value — the shapes a placeholder resource can faithfully
	 * represent and later be matched by.
	 */
	// Created by Claude Fable 5
	private boolean isEligibleForSyntheticCreate(MatchUrlService.ResourceTypeAndSearchParameterMap theParsed) {
		SearchParameterMap params = theParsed.searchParameterMap();
		if (params.keySet().size() != 1 || !params.containsKey("identifier")) {
			return false;
		}
		for (List<IQueryParameterType> andGroup : params.get("identifier")) {
			for (IQueryParameterType paramType : andGroup) {
				if (!(paramType instanceof TokenParam tokenParam)
						|| isBlank(tokenParam.getSystem())
						|| isBlank(tokenParam.getValue())) {
					return false;
				}
			}
		}
		return true;
	}

	private IBaseResource buildPlaceholder(MatchUrlService.ResourceTypeAndSearchParameterMap theParsed) {
		RuntimeResourceDefinition resourceDef = theParsed.resourceDefinition();
		IBaseResource placeholder = resourceDef.newInstance();

		BaseRuntimeChildDefinition identifierChildDef = resourceDef.getChildByName("identifier");
		if (identifierChildDef != null) {
			List<List<IQueryParameterType>> identifierValues =
					theParsed.searchParameterMap().get("identifier");
			if (identifierValues != null) {
				for (List<IQueryParameterType> andGroup : identifierValues) {
					for (IQueryParameterType paramType : andGroup) {
						if (!(paramType instanceof TokenParam tokenParam)) {
							continue;
						}

						BaseRuntimeElementDefinition<?> identifierElementDef =
								identifierChildDef.getChildByName("identifier");
						IBase identifierIBase =
								identifierElementDef.newInstance(identifierChildDef.getInstanceConstructorArguments());

						IBase systemIBase = TerserUtil.newElement(myFhirContext, "uri", tokenParam.getSystem());
						IBase valueIBase = TerserUtil.newElement(myFhirContext, "string", tokenParam.getValue());

						BaseRuntimeElementDefinition<?> identifierTypeDef =
								myFhirContext.getElementDefinition(identifierIBase.getClass());
						identifierTypeDef.getChildByName("system").getMutator().setValue(identifierIBase, systemIBase);
						identifierTypeDef.getChildByName("value").getMutator().setValue(identifierIBase, valueIBase);

						identifierChildDef.getMutator().addValue(placeholder, identifierIBase);
					}
				}
			}
		}

		if (placeholder instanceof IBaseHasExtensions) {
			IBaseExtension<?, ?> extension = ((IBaseHasExtensions) placeholder).addExtension();
			extension.setUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER);
			extension.setValue(myFhirContext.newPrimitiveBoolean(true));
		}

		return placeholder;
	}

	private record MatchUrlInfo(String urnUuid, MatchUrlService.ResourceTypeAndSearchParameterMap parsed) {}

	/** Key matching an inline match URL reference to an in-bundle entry by resource type + identifier token. */
	private record IdentifierKey(String resourceType, String system, String value) {
		private IdentifierKey {
			// Normalise a blank system to null so absent/"" compare equal across resource identifiers and refs.
			system = isBlank(system) ? null : system;
		}
	}
}
