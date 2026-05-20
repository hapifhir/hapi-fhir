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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.util.MatchUrlUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.TerserUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 *
 * FIXME-TG:
 *  1. Create a full url for any entry that doesn't already have one - solves
 */
public class InlineMatchUrlBundleSyntaxTransformerService {

	private final FhirContext myFhirContext;
	private final MatchUrlService myMatchUrlService;

	@SuppressWarnings("rawtypes")
	private final ITransactionProcessorVersionAdapter myVersionAdapter;

	public InlineMatchUrlBundleSyntaxTransformerService(
			FhirContext theFhirContext,
			MatchUrlService theMatchUrlService,
			@SuppressWarnings("rawtypes") ITransactionProcessorVersionAdapter theVersionAdapter) {
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
	public int transform(IBaseBundle theBundle) {
		List<IBase> bundleEntries = myVersionAdapter.getEntries(theBundle);

		if (bundleEntries.isEmpty()) {
			return 0;
		}

		FhirTerser terser = myFhirContext.newTerser();

		// First pass: index existing conditional-write entries by their target match URL. If an inline
		// match URL ref later in the bundle hits one of these, we reuse that entry's fullUrl instead of
		// generating a synthetic placeholder — preserving the user's full resource body (a synthetic
		// placeholder would shadow it during conditional resolution at the server, discarding user data).
		// Two shapes are recognised:
		//   * POST entries with request.ifNoneExist=<matchUrl> (conditional create)
		//   * PUT  entries with request.url=<matchUrl>         (conditional update / upsert)
		// We don't mutate fullUrls here — that's deferred to the main pass and done only for entries that
		// an inline match URL actually points at, so we don't disturb entries that need none.
		Map<String, IBase> existingConditionalWriteEntries = new HashMap<>();
		for (IBase entry : bundleEntries) {
			String verb = myVersionAdapter.getEntryRequestVerb(myFhirContext, entry);
			String matchUrl = null;
			if ("POST".equals(verb)) {
				matchUrl = myVersionAdapter.getEntryIfNoneExist(entry);
			} else if ("PUT".equals(verb)) {
				String url = myVersionAdapter.getEntryRequestUrl(entry);
				// Conditional upsert by match URL is identified by '?' in the request URL.
				if (url != null && url.contains("?")) {
					matchUrl = url;
				}
			}
			if (isBlank(matchUrl)) {
				continue;
			}
			existingConditionalWriteEntries.put(matchUrl, entry);
		}

		// Second pass: discover, parse, validate, and rewrite each inline match URL reference.
		// The map is keyed on the raw URL string. Order among synthetic entries doesn't matter — the count
		// is what matters for response cleanup.
		Map<String, MatchUrlInfo> matchUrlToInfo = new HashMap<>();
		for (IBase entry : bundleEntries) {
			// Only process write entries (POST/PUT/PATCH); read entries (GET/DELETE) have no resource body to walk.
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
				// If an existing conditional-write entry covers this match URL, point the inline ref at it.
				IBase existingEntry = existingConditionalWriteEntries.get(refValue);
				if (existingEntry != null) {
					ref.setReference(resolveExistingEntryPlaceholderUrl(existingEntry));
					continue;
				}
				// Otherwise, generate a synthetic placeholder on first encounter; reuse for duplicates.
				MatchUrlInfo info = matchUrlToInfo.computeIfAbsent(refValue, url -> {
					MatchUrlService.ResourceTypeAndSearchParameterMap parsed =
							myMatchUrlService.parseAndTranslateMatchUrl(url);
					validateParsedMatchUrl(url, parsed);
					return new MatchUrlInfo("urn:uuid:" + UUID.randomUUID(), parsed);
				});
				ref.setReference(info.urnUuid());
			}
		}

		if (matchUrlToInfo.isEmpty()) {
			return 0;
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
		Collections.rotate(bundleEntries, n);

		return n;
	}

	/**
	 * Resolve the placeholder URL an inline match URL ref should rewrite to, for an Option A match.
	 * Precedence mirrors HAPI's own logic in {@code BaseTransactionProcessor#getNextResourceIdFromBaseResource}:
	 *   1. {@code entry.fullUrl} if set;
	 *   2. {@code resource.id} if it's already a urn placeholder (HAPI accepts that as the placeholder URI
	 *      when fullUrl is absent — generating a new fullUrl here would override it via fullUrl precedence
	 *      and silently break any other in-bundle refs that targeted that resource.id);
	 *   3. otherwise, generate a fresh urn:uuid and set it as the entry's fullUrl.
	 */
	@SuppressWarnings("unchecked")
	private String resolveExistingEntryPlaceholderUrl(IBase theEntry) {
		String fullUrl = myVersionAdapter.getFullUrl(theEntry);
		if (!isBlank(fullUrl)) {
			return fullUrl;
		}
		IBaseResource resource = myVersionAdapter.getResource(theEntry);
		if (resource != null) {
			String resourceId = resource.getIdElement().getValue();
			if (resourceId != null && resourceId.startsWith("urn:")) {
				return resourceId;
			}
		}
		String generated = "urn:uuid:" + UUID.randomUUID();
		myVersionAdapter.setFullUrl(theEntry, generated);
		return generated;
	}

	private void validateParsedMatchUrl(
			String theMatchUrl, MatchUrlService.ResourceTypeAndSearchParameterMap theParsed) {
		SearchParameterMap params = theParsed.searchParameterMap();

		if (params.keySet().size() != 1 || !params.containsKey("identifier")) {
			throw new PreconditionFailedException(Msg.code(2700)
					+ "Inline match URL matching only supports identifier search parameters: " + theMatchUrl);
		}

		List<List<IQueryParameterType>> identifierValues = params.get("identifier");

		for (List<IQueryParameterType> andGroup : identifierValues) {
			for (IQueryParameterType paramType : andGroup) {
				if (paramType instanceof TokenParam tokenParam) {
					if (isBlank(tokenParam.getSystem()) || isBlank(tokenParam.getValue())) {
						throw new PreconditionFailedException(Msg.code(2702)
								+ "Inline match URL identifier must have both a system and a value: " + theMatchUrl);
					}
				}
			}
		}
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

		// Mark as placeholder
		if (placeholder instanceof IBaseHasExtensions) {
			IBaseExtension<?, ?> extension = ((IBaseHasExtensions) placeholder).addExtension();
			extension.setUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER);
			extension.setValue(myFhirContext.newPrimitiveBoolean(true));
		}

		return placeholder;
	}

	private record MatchUrlInfo(String urnUuid, MatchUrlService.ResourceTypeAndSearchParameterMap parsed) {}
}
