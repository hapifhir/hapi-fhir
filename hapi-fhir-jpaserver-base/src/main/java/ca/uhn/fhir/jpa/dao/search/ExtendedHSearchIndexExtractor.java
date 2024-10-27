/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.search.CompositeSearchIndexData;
import ca.uhn.fhir.jpa.model.search.DateSearchIndexData;
import ca.uhn.fhir.jpa.model.search.ExtendedHSearchIndexData;
import ca.uhn.fhir.jpa.model.search.QuantitySearchIndexData;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParamComposite;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.MetaUtil;
import com.google.common.base.Strings;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Extract search params for advanced HSearch indexing.
 * <p>
 * This class re-uses the extracted JPA entities to build an ExtendedHSearchIndexData instance.
 */
public class ExtendedHSearchIndexExtractor {

	private final JpaStorageSettings myJpaStorageSettings;
	private final FhirContext myContext;
	private final ResourceSearchParams myParams;
	private final ISearchParamExtractor mySearchParamExtractor;

	public ExtendedHSearchIndexExtractor(
			JpaStorageSettings theJpaStorageSettings,
			FhirContext theContext,
			ResourceSearchParams theActiveParams,
			ISearchParamExtractor theSearchParamExtractor) {
		myJpaStorageSettings = theJpaStorageSettings;
		myContext = theContext;
		myParams = theActiveParams;
		mySearchParamExtractor = theSearchParamExtractor;
	}

	@Nonnull
	public ExtendedHSearchIndexData extract(IBaseResource theResource, ResourceIndexedSearchParams theNewParams) {
		ExtendedHSearchIndexData retVal = new ExtendedHSearchIndexData(myContext, myJpaStorageSettings, theResource);

		if (myJpaStorageSettings.isStoreResourceInHSearchIndex()) {
			retVal.setRawResourceData(myContext.newJsonParser().encodeResourceToString(theResource));
		}

		retVal.setForcedId(theResource.getIdElement().getIdPart());

		// todo add a flag ot StorageSettings to suppress this
		extractAutocompleteTokens(theResource, retVal);

		theNewParams.myStringParams.stream()
				.filter(nextParam -> !nextParam.isMissing())
				.forEach(nextParam -> retVal.addStringIndexData(nextParam.getParamName(), nextParam.getValueExact()));

		theNewParams.myTokenParams.stream()
				.filter(nextParam -> !nextParam.isMissing())
				.forEach(nextParam -> retVal.addTokenIndexDataIfNotPresent(
						nextParam.getParamName(), nextParam.getSystem(), nextParam.getValue()));

		theNewParams.myNumberParams.stream()
				.filter(nextParam -> !nextParam.isMissing())
				.forEach(nextParam ->
						retVal.addNumberIndexDataIfNotPresent(nextParam.getParamName(), nextParam.getValue()));

		theNewParams.myDateParams.stream()
				.filter(nextParam -> !nextParam.isMissing())
				.forEach(nextParam -> retVal.addDateIndexData(nextParam.getParamName(), convertDate(nextParam)));

		theNewParams.myQuantityParams.stream()
				.filter(nextParam -> !nextParam.isMissing())
				.forEach(
						nextParam -> retVal.addQuantityIndexData(nextParam.getParamName(), convertQuantity(nextParam)));

		theNewParams.myUriParams.stream()
				.filter(nextParam -> !nextParam.isMissing())
				.forEach(nextParam -> retVal.addUriIndexData(nextParam.getParamName(), nextParam.getUri()));

		theResource.getMeta().getTag().forEach(tag -> retVal.addTokenIndexData("_tag", tag));

		theResource.getMeta().getSecurity().forEach(sec -> retVal.addTokenIndexData("_security", sec));

		theResource.getMeta().getProfile().forEach(prof -> retVal.addUriIndexData("_profile", prof.getValue()));

		String source = MetaUtil.getSource(myContext, theResource.getMeta());
		if (isNotBlank(source)) {
			retVal.addUriIndexData("_source", source);
		}

		theNewParams.myCompositeParams.forEach(nextParam ->
				retVal.addCompositeIndexData(nextParam.getSearchParamName(), buildCompositeIndexData(nextParam)));

		if (theResource.getMeta().getLastUpdated() != null) {
			int ordinal = ResourceIndexedSearchParamDate.calculateOrdinalValue(
							theResource.getMeta().getLastUpdated())
					.intValue();
			retVal.addDateIndexData(
					"_lastUpdated",
					theResource.getMeta().getLastUpdated(),
					ordinal,
					theResource.getMeta().getLastUpdated(),
					ordinal);
		}

		if (!theNewParams.myLinks.isEmpty()) {

			// awkwardly, links are indexed by jsonpath, not by search param.
			// so we re-build the linkage.
			Map<String, List<String>> linkPathToParamName = new HashMap<>();
			for (String nextParamName : theNewParams.getPopulatedResourceLinkParameters()) {
				RuntimeSearchParam sp = myParams.get(nextParamName);
				List<String> pathsSplit = sp.getPathsSplit();
				for (String nextPath : pathsSplit) {
					// we want case-insensitive matching
					nextPath = nextPath.toLowerCase(Locale.ROOT);

					linkPathToParamName
							.computeIfAbsent(nextPath, (p) -> new ArrayList<>())
							.add(nextParamName);
				}
			}

			for (ResourceLink nextLink : theNewParams.getResourceLinks()) {
				String insensitivePath = nextLink.getSourcePath().toLowerCase(Locale.ROOT);
				List<String> paramNames = linkPathToParamName.getOrDefault(insensitivePath, Collections.emptyList());
				for (String nextParamName : paramNames) {
					String qualifiedTargetResourceId = "";
					// Consider 2 cases for references
					// Case 1: Resource Type and Resource ID is known
					// Case 2: Resource is unknown and referred by canonical url reference
					if (!Strings.isNullOrEmpty(nextLink.getTargetResourceId())) {
						qualifiedTargetResourceId =
								nextLink.getTargetResourceType() + "/" + nextLink.getTargetResourceId();
					} else if (!Strings.isNullOrEmpty(nextLink.getTargetResourceUrl())) {
						qualifiedTargetResourceId = nextLink.getTargetResourceUrl();
					}
					retVal.addResourceLinkIndexData(nextParamName, qualifiedTargetResourceId);
				}
			}
		}

		return retVal;
	}

	@Nonnull
	private CompositeSearchIndexData buildCompositeIndexData(
			ResourceIndexedSearchParamComposite theSearchParamComposite) {
		return new HSearchCompositeSearchIndexDataImpl(theSearchParamComposite);
	}

	/**
	 * Re-extract token parameters so we can distinguish
	 */
	private void extractAutocompleteTokens(IBaseResource theResource, ExtendedHSearchIndexData theRetVal) {
		// we need to re-index token params to match up display with codes.
		myParams.values().stream()
				.filter(p -> p.getParamType() == RestSearchParameterTypeEnum.TOKEN)
				// TODO it would be nice to reuse TokenExtractor
				.forEach(p -> mySearchParamExtractor
						.extractValues(p.getPath(), theResource)
						.forEach(nextValue -> indexTokenValue(theRetVal, p, nextValue)));
	}

	private void indexTokenValue(ExtendedHSearchIndexData theRetVal, RuntimeSearchParam p, IBase nextValue) {
		String nextType = mySearchParamExtractor.toRootTypeName(nextValue);
		String spName = p.getName();
		switch (nextType) {
			case "CodeableConcept":
				addToken_CodeableConcept(theRetVal, spName, nextValue);
				break;
			case "Coding":
				addToken_Coding(theRetVal, spName, (IBaseCoding) nextValue);
				break;
				// TODO share this with TokenExtractor and introduce a ITokenIndexer interface.
				// Ignore unknown types for now.
				// This is just for autocomplete, and we are focused on Observation.code, category, combo-code, etc.
				//					case "Identifier":
				//						mySearchParamExtractor.addToken_Identifier(myResourceTypeName, params, searchParam, value);
				//						break;
				//					case "ContactPoint":
				//						mySearchParamExtractor.addToken_ContactPoint(myResourceTypeName, params, searchParam, value);
				//						break;
			default:
				break;
		}
	}

	private void addToken_CodeableConcept(ExtendedHSearchIndexData theRetVal, String theSpName, IBase theValue) {
		List<IBase> codings = mySearchParamExtractor.getCodingsFromCodeableConcept(theValue);
		for (IBase nextCoding : codings) {
			addToken_Coding(theRetVal, theSpName, (IBaseCoding) nextCoding);
		}
	}

	private void addToken_Coding(ExtendedHSearchIndexData theRetVal, String theSpName, IBaseCoding theNextValue) {
		theRetVal.addTokenIndexData(theSpName, theNextValue);
	}

	@Nonnull
	public static DateSearchIndexData convertDate(ResourceIndexedSearchParamDate nextParam) {
		return new DateSearchIndexData(
				nextParam.getValueLow(),
				nextParam.getValueLowDateOrdinal(),
				nextParam.getValueHigh(),
				nextParam.getValueHighDateOrdinal());
	}

	@Nonnull
	public static QuantitySearchIndexData convertQuantity(ResourceIndexedSearchParamQuantity nextParam) {
		return new QuantitySearchIndexData(
				nextParam.getUnits(),
				nextParam.getSystem(),
				nextParam.getValue().doubleValue());
	}
}
