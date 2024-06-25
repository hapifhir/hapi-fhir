/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.util.SearchParamHash;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.util.RuntimeSearchParamHelper;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.compare;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class ResourceIndexedSearchParams {
	private static final Set<String> myIgnoredParams = Set.of(Constants.PARAM_TEXT, Constants.PARAM_CONTENT);
	public final Collection<ResourceIndexedSearchParamString> myStringParams;
	public final Collection<ResourceIndexedSearchParamToken> myTokenParams;
	public final Collection<ResourceIndexedSearchParamNumber> myNumberParams;
	public final Collection<ResourceIndexedSearchParamQuantity> myQuantityParams;
	public final Collection<ResourceIndexedSearchParamQuantityNormalized> myQuantityNormalizedParams;
	public final Collection<ResourceIndexedSearchParamDate> myDateParams;
	public final Collection<ResourceIndexedSearchParamUri> myUriParams;
	public final Collection<ResourceIndexedSearchParamCoords> myCoordsParams;
	public final Collection<ResourceIndexedComboStringUnique> myComboStringUniques;
	public final Collection<ResourceIndexedComboTokenNonUnique> myComboTokenNonUnique;
	public final Collection<ResourceLink> myLinks;
	public final Collection<SearchParamPresentEntity> mySearchParamPresentEntities;
	public final Collection<ResourceIndexedSearchParamComposite> myCompositeParams;
	public final Set<String> myPopulatedResourceLinkParameters = new HashSet<>();

	/**
	 * TODO: Remove this - Currently used by CDR though
	 *
	 * @deprecated Use a factory constructor instead
	 */
	@Deprecated
	public ResourceIndexedSearchParams() {
		this(Mode.SET);
	}

	private ResourceIndexedSearchParams(Mode theMode) {
		myStringParams = theMode.newCollection();
		myTokenParams = theMode.newCollection();
		myNumberParams = theMode.newCollection();
		myQuantityParams = theMode.newCollection();
		myQuantityNormalizedParams = theMode.newCollection();
		myDateParams = theMode.newCollection();
		myUriParams = theMode.newCollection();
		myCoordsParams = theMode.newCollection();
		myComboStringUniques = theMode.newCollection();
		myComboTokenNonUnique = theMode.newCollection();
		myLinks = theMode.newCollection();
		mySearchParamPresentEntities = theMode.newCollection();
		myCompositeParams = theMode.newCollection();
	}

	private ResourceIndexedSearchParams(ResourceTable theEntity, Mode theMode) {
		this(theMode);
		if (theEntity.isParamsStringPopulated()) {
			myStringParams.addAll(theEntity.getParamsString());
		}
		if (theEntity.isParamsTokenPopulated()) {
			myTokenParams.addAll(theEntity.getParamsToken());
		}
		if (theEntity.isParamsNumberPopulated()) {
			myNumberParams.addAll(theEntity.getParamsNumber());
		}
		if (theEntity.isParamsQuantityPopulated()) {
			myQuantityParams.addAll(theEntity.getParamsQuantity());
		}
		if (theEntity.isParamsQuantityNormalizedPopulated()) {
			myQuantityNormalizedParams.addAll(theEntity.getParamsQuantityNormalized());
		}
		if (theEntity.isParamsDatePopulated()) {
			myDateParams.addAll(theEntity.getParamsDate());
		}
		if (theEntity.isParamsUriPopulated()) {
			myUriParams.addAll(theEntity.getParamsUri());
		}
		if (theEntity.isParamsCoordsPopulated()) {
			myCoordsParams.addAll(theEntity.getParamsCoords());
		}
		if (theEntity.isHasLinks()) {
			myLinks.addAll(theEntity.getResourceLinks());
		}

		if (theEntity.isParamsComboStringUniquePresent()) {
			myComboStringUniques.addAll(theEntity.getParamsComboStringUnique());
		}
		if (theEntity.isParamsComboTokensNonUniquePresent()) {
			myComboTokenNonUnique.addAll(theEntity.getmyParamsComboTokensNonUnique());
		}
	}

	public Collection<ResourceLink> getResourceLinks() {
		return myLinks;
	}

	public void populateResourceTableSearchParamsPresentFlags(ResourceTable theEntity) {
		theEntity.setParamsStringPopulated(myStringParams.isEmpty() == false);
		theEntity.setParamsTokenPopulated(myTokenParams.isEmpty() == false);
		theEntity.setParamsNumberPopulated(myNumberParams.isEmpty() == false);
		theEntity.setParamsQuantityPopulated(myQuantityParams.isEmpty() == false);
		theEntity.setParamsQuantityNormalizedPopulated(myQuantityNormalizedParams.isEmpty() == false);
		theEntity.setParamsDatePopulated(myDateParams.isEmpty() == false);
		theEntity.setParamsUriPopulated(myUriParams.isEmpty() == false);
		theEntity.setParamsCoordsPopulated(myCoordsParams.isEmpty() == false);
		theEntity.setParamsComboStringUniquePresent(myComboStringUniques.isEmpty() == false);
		theEntity.setParamsComboTokensNonUniquePresent(myComboTokenNonUnique.isEmpty() == false);
		theEntity.setHasLinks(myLinks.isEmpty() == false);
	}

	public void populateResourceTableParamCollections(ResourceTable theEntity) {
		theEntity.setParamsString(myStringParams);
		theEntity.setParamsToken(myTokenParams);
		theEntity.setParamsNumber(myNumberParams);
		theEntity.setParamsQuantity(myQuantityParams);
		theEntity.setParamsQuantityNormalized(myQuantityNormalizedParams);
		theEntity.setParamsDate(myDateParams);
		theEntity.setParamsUri(myUriParams);
		theEntity.setParamsCoords(myCoordsParams);
		theEntity.setResourceLinks(myLinks);
	}

	public void updateSpnamePrefixForIndexOnUpliftedChain(String theContainingType, String theSpnamePrefix) {
		updateSpnamePrefixForIndexOnUpliftedChain(theContainingType, myNumberParams, theSpnamePrefix);
		updateSpnamePrefixForIndexOnUpliftedChain(theContainingType, myQuantityParams, theSpnamePrefix);
		updateSpnamePrefixForIndexOnUpliftedChain(theContainingType, myQuantityNormalizedParams, theSpnamePrefix);
		updateSpnamePrefixForIndexOnUpliftedChain(theContainingType, myDateParams, theSpnamePrefix);
		updateSpnamePrefixForIndexOnUpliftedChain(theContainingType, myUriParams, theSpnamePrefix);
		updateSpnamePrefixForIndexOnUpliftedChain(theContainingType, myTokenParams, theSpnamePrefix);
		updateSpnamePrefixForIndexOnUpliftedChain(theContainingType, myStringParams, theSpnamePrefix);
		updateSpnamePrefixForIndexOnUpliftedChain(theContainingType, myCoordsParams, theSpnamePrefix);
	}

	public void updateSpnamePrefixForLinksOnContainedResource(String theSpNamePrefix) {
		for (ResourceLink param : myLinks) {
			// The resource link already has the resource type of the contained resource at the head of the path.
			// We need to replace this with the name of the containing type, and extend the search path.
			int index = param.getSourcePath().indexOf('.');
			if (index > -1) {
				param.setSourcePath(theSpNamePrefix + param.getSourcePath().substring(index));
			} else {
				// Can this ever happen?
				param.setSourcePath(theSpNamePrefix + "." + param.getSourcePath());
			}
			param.calculateHashes(); // re-calculateHashes
		}
	}

	void setUpdatedTime(Date theUpdateTime) {
		setUpdatedTime(myStringParams, theUpdateTime);
		setUpdatedTime(myNumberParams, theUpdateTime);
		setUpdatedTime(myQuantityParams, theUpdateTime);
		setUpdatedTime(myQuantityNormalizedParams, theUpdateTime);
		setUpdatedTime(myDateParams, theUpdateTime);
		setUpdatedTime(myUriParams, theUpdateTime);
		setUpdatedTime(myCoordsParams, theUpdateTime);
		setUpdatedTime(myTokenParams, theUpdateTime);
	}

	private void setUpdatedTime(Collection<? extends BaseResourceIndexedSearchParam> theParams, Date theUpdateTime) {
		for (BaseResourceIndexedSearchParam nextSearchParam : theParams) {
			nextSearchParam.setUpdated(theUpdateTime);
		}
	}

	private void updateSpnamePrefixForIndexOnUpliftedChain(
			String theContainingType,
			Collection<? extends BaseResourceIndexedSearchParam> theParams,
			@Nonnull String theSpnamePrefix) {

		for (BaseResourceIndexedSearchParam param : theParams) {
			param.setResourceType(theContainingType);
			param.setParamName(theSpnamePrefix + "." + param.getParamName());

			// re-calculate hashes
			param.calculateHashes();
		}
	}

	public Set<String> getPopulatedResourceLinkParameters() {
		return myPopulatedResourceLinkParameters;
	}

	public boolean matchParam(
			StorageSettings theStorageSettings,
			String theResourceName,
			String theParamName,
			RuntimeSearchParam theParamDef,
			IQueryParameterType theValue) {

		if (theParamDef == null) {
			return false;
		}
		Collection<? extends BaseResourceIndexedSearchParam> resourceParams = null;
		IQueryParameterType value = theValue;
		switch (theParamDef.getParamType()) {
			case TOKEN:
				resourceParams = myTokenParams;
				break;
			case QUANTITY:
				if (theStorageSettings
						.getNormalizedQuantitySearchLevel()
						.equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED)) {
					QuantityParam quantity = QuantityParam.toQuantityParam(theValue);
					QuantityParam normalized = UcumServiceUtil.toCanonicalQuantityOrNull(quantity);
					if (normalized != null) {
						resourceParams = myQuantityNormalizedParams;
						value = normalized;
					}
				}

				if (resourceParams == null) {
					resourceParams = myQuantityParams;
				}
				break;
			case STRING:
				resourceParams = myStringParams;
				break;
			case NUMBER:
				resourceParams = myNumberParams;
				break;
			case URI:
				resourceParams = myUriParams;
				break;
			case DATE:
				resourceParams = myDateParams;
				break;
			case REFERENCE:
				return matchResourceLinks(
						theStorageSettings,
						theResourceName,
						theParamName,
						value,
						theParamDef.getPathsSplitForResourceType(theResourceName));
			case COMPOSITE:
			case HAS:
			case SPECIAL:
			default:
				resourceParams = null;
		}
		if (resourceParams == null) {
			return false;
		}

		for (BaseResourceIndexedSearchParam nextParam : resourceParams) {
			if (isMatchSearchParam(theStorageSettings, theResourceName, theParamName, nextParam)) {
				if (nextParam.matches(value)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isMatchSearchParam(
			StorageSettings theStorageSettings,
			String theResourceName,
			String theParamName,
			BaseResourceIndexedSearchParam theIndexedSearchParam) {

		if (theStorageSettings.isIndexStorageOptimized()) {
			Long hashIdentity = SearchParamHash.hashSearchParam(
					new PartitionSettings(), RequestPartitionId.defaultPartition(), theResourceName, theParamName);
			return theIndexedSearchParam.getHashIdentity().equals(hashIdentity);
		} else {
			return theIndexedSearchParam.getParamName().equalsIgnoreCase(theParamName);
		}
	}

	/**
	 * @deprecated Replace with the method below
	 */
	// KHS This needs to be public as libraries outside of hapi call it directly
	@Deprecated
	public boolean matchResourceLinks(
			String theResourceName, String theParamName, IQueryParameterType theParam, String theParamPath) {
		return matchResourceLinks(new StorageSettings(), theResourceName, theParamName, theParam, theParamPath);
	}

	public boolean matchResourceLinks(
			StorageSettings theStorageSettings,
			String theResourceName,
			String theParamName,
			IQueryParameterType theParam,
			List<String> theParamPaths) {
		for (String nextPath : theParamPaths) {
			if (matchResourceLinks(theStorageSettings, theResourceName, theParamName, theParam, nextPath)) {
				return true;
			}
		}
		return false;
	}

	// KHS This needs to be public as libraries outside of hapi call it directly
	public boolean matchResourceLinks(
			StorageSettings theStorageSettings,
			String theResourceName,
			String theParamName,
			IQueryParameterType theParam,
			String theParamPath) {
		ReferenceParam reference = (ReferenceParam) theParam;

		Predicate<ResourceLink> namedParamPredicate =
				resourceLink -> searchParameterPathMatches(theResourceName, resourceLink, theParamName, theParamPath)
						&& resourceIdMatches(theStorageSettings, resourceLink, reference);

		return myLinks.stream().anyMatch(namedParamPredicate);
	}

	private boolean resourceIdMatches(
			StorageSettings theStorageSettings, ResourceLink theResourceLink, ReferenceParam theReference) {
		String baseUrl = theReference.getBaseUrl();
		if (isNotBlank(baseUrl)) {
			if (!theStorageSettings.getTreatBaseUrlsAsLocal().contains(baseUrl)) {
				return false;
			}
		}

		String targetType = theResourceLink.getTargetResourceType();
		String targetId = theResourceLink.getTargetResourceId();

		assert isNotBlank(targetType);
		assert isNotBlank(targetId);

		if (theReference.hasResourceType()) {
			if (!theReference.getResourceType().equals(targetType)) {
				return false;
			}
		}

		if (!targetId.equals(theReference.getIdPart())) {
			return false;
		}

		return true;
	}

	private boolean searchParameterPathMatches(
			String theResourceName, ResourceLink theResourceLink, String theParamName, String theParamPath) {
		String sourcePath = theResourceLink.getSourcePath();
		return sourcePath.equalsIgnoreCase(theParamPath);
	}

	@Override
	public String toString() {
		return "ResourceIndexedSearchParams{" + "stringParams="
				+ myStringParams + ", tokenParams="
				+ myTokenParams + ", numberParams="
				+ myNumberParams + ", quantityParams="
				+ myQuantityParams + ", quantityNormalizedParams="
				+ myQuantityNormalizedParams + ", dateParams="
				+ myDateParams + ", uriParams="
				+ myUriParams + ", coordsParams="
				+ myCoordsParams + ", comboStringUniques="
				+ myComboStringUniques + ", comboTokenNonUniques="
				+ myComboTokenNonUnique + ", links="
				+ myLinks + '}';
	}

	public void findMissingSearchParams(
			PartitionSettings thePartitionSettings,
			StorageSettings theStorageSettings,
			ResourceTable theEntity,
			ResourceSearchParams theActiveSearchParams) {
		findMissingSearchParams(
				thePartitionSettings,
				theStorageSettings,
				theEntity,
				theActiveSearchParams,
				RestSearchParameterTypeEnum.STRING,
				myStringParams);
		findMissingSearchParams(
				thePartitionSettings,
				theStorageSettings,
				theEntity,
				theActiveSearchParams,
				RestSearchParameterTypeEnum.NUMBER,
				myNumberParams);
		findMissingSearchParams(
				thePartitionSettings,
				theStorageSettings,
				theEntity,
				theActiveSearchParams,
				RestSearchParameterTypeEnum.QUANTITY,
				myQuantityParams);
		findMissingSearchParams(
				thePartitionSettings,
				theStorageSettings,
				theEntity,
				theActiveSearchParams,
				RestSearchParameterTypeEnum.DATE,
				myDateParams);
		findMissingSearchParams(
				thePartitionSettings,
				theStorageSettings,
				theEntity,
				theActiveSearchParams,
				RestSearchParameterTypeEnum.URI,
				myUriParams);
		findMissingSearchParams(
				thePartitionSettings,
				theStorageSettings,
				theEntity,
				theActiveSearchParams,
				RestSearchParameterTypeEnum.TOKEN,
				myTokenParams);
		findMissingSearchParams(
				thePartitionSettings,
				theStorageSettings,
				theEntity,
				theActiveSearchParams,
				RestSearchParameterTypeEnum.SPECIAL,
				myCoordsParams);
	}

	@SuppressWarnings("unchecked")
	private <RT extends BaseResourceIndexedSearchParam> void findMissingSearchParams(
			PartitionSettings thePartitionSettings,
			StorageSettings theStorageSettings,
			ResourceTable theEntity,
			ResourceSearchParams activeSearchParams,
			RestSearchParameterTypeEnum type,
			Collection<RT> paramCollection) {
		for (String nextParamName : activeSearchParams.getSearchParamNames()) {
			if (nextParamName == null || myIgnoredParams.contains(nextParamName)) {
				continue;
			}

			RuntimeSearchParam searchParam = activeSearchParams.get(nextParamName);
			if (RuntimeSearchParamHelper.isResourceLevel(searchParam)) {
				continue;
			}

			if (searchParam.getParamType() == type) {
				boolean haveParam = false;
				for (BaseResourceIndexedSearchParam nextParam : paramCollection) {
					if (nextParam.getParamName().equals(nextParamName)) {
						haveParam = true;
						break;
					}
				}

				if (!haveParam) {
					BaseResourceIndexedSearchParam param;
					switch (type) {
						case DATE:
							param = new ResourceIndexedSearchParamDate();
							break;
						case NUMBER:
							param = new ResourceIndexedSearchParamNumber();
							break;
						case QUANTITY:
							param = new ResourceIndexedSearchParamQuantity();
							break;
						case STRING:
							param = new ResourceIndexedSearchParamString().setStorageSettings(theStorageSettings);
							break;
						case TOKEN:
							param = new ResourceIndexedSearchParamToken();
							break;
						case URI:
							param = new ResourceIndexedSearchParamUri();
							break;
						case SPECIAL:
							if (BaseSearchParamExtractor.COORDS_INDEX_PATHS.contains(searchParam.getPath())) {
								param = new ResourceIndexedSearchParamCoords();
								break;
							} else {
								continue;
							}
						case COMPOSITE:
						case HAS:
						case REFERENCE:
						default:
							continue;
					}
					param.setPartitionSettings(thePartitionSettings);
					param.setResource(theEntity);
					param.setMissing(true);
					param.setParamName(nextParamName);
					param.calculateHashes();
					paramCollection.add((RT) param);
				}
			}
		}
	}

	/**
	 * This method is used to create a set of all possible combinations of
	 * parameters across a set of search parameters. An example of why
	 * this is needed:
	 * <p>
	 * Let's say we have a unique index on (Patient:gender AND Patient:name).
	 * Then we pass in <code>SMITH, John</code> with a gender of <code>male</code>.
	 * </p>
	 * <p>
	 * In this case, because the name parameter matches both first and last name,
	 * we now need two unique indexes:
	 * <ul>
	 * <li>Patient?gender=male&amp;name=SMITH</li>
	 * <li>Patient?gender=male&amp;name=JOHN</li>
	 * </ul>
	 * </p>
	 * <p>
	 * So this recursive algorithm calculates those
	 * </p>
	 *
	 * @param theResourceType E.g. <code>Patient
	 * @param thePartsChoices E.g. <code>[[gender=male], [name=SMITH, name=JOHN]]</code>
	 */
	public static Set<String> extractCompositeStringUniquesValueChains(
			String theResourceType, List<List<String>> thePartsChoices) {

		for (List<String> next : thePartsChoices) {
			next.removeIf(StringUtils::isBlank);
			if (next.isEmpty()) {
				return Collections.emptySet();
			}
		}

		if (thePartsChoices.isEmpty()) {
			return Collections.emptySet();
		}

		thePartsChoices.sort((o1, o2) -> {
			String str1 = null;
			String str2 = null;
			if (o1.size() > 0) {
				str1 = o1.get(0);
			}
			if (o2.size() > 0) {
				str2 = o2.get(0);
			}
			return compare(str1, str2);
		});

		List<String> values = new ArrayList<>();
		Set<String> queryStringsToPopulate = new HashSet<>();
		extractCompositeStringUniquesValueChains(theResourceType, thePartsChoices, values, queryStringsToPopulate);

		values.removeIf(StringUtils::isBlank);

		return queryStringsToPopulate;
	}

	private static void extractCompositeStringUniquesValueChains(
			String theResourceType,
			List<List<String>> thePartsChoices,
			List<String> theValues,
			Set<String> theQueryStringsToPopulate) {
		if (thePartsChoices.size() > 0) {
			List<String> nextList = thePartsChoices.get(0);
			Collections.sort(nextList);
			for (String nextChoice : nextList) {
				theValues.add(nextChoice);
				extractCompositeStringUniquesValueChains(
						theResourceType,
						thePartsChoices.subList(1, thePartsChoices.size()),
						theValues,
						theQueryStringsToPopulate);
				theValues.remove(theValues.size() - 1);
			}
		} else {
			if (theValues.size() > 0) {
				StringBuilder uniqueString = new StringBuilder();
				uniqueString.append(theResourceType);

				for (int i = 0; i < theValues.size(); i++) {
					uniqueString.append(i == 0 ? "?" : "&");
					uniqueString.append(theValues.get(i));
				}

				theQueryStringsToPopulate.add(uniqueString.toString());
			}
		}
	}

	/**
	 * Create a new instance that uses Sets as the internal collection
	 * type in order to defend against duplicates. This should be used
	 * when calculating the set of indexes for a resource that is
	 * about to be stored.
	 */
	public static ResourceIndexedSearchParams withSets() {
		return new ResourceIndexedSearchParams(Mode.SET);
	}

	/**
	 * Create an empty and immutable structure.
	 */
	public static ResourceIndexedSearchParams empty() {
		return new ResourceIndexedSearchParams(Mode.EMPTY);
	}

	/**
	 * Create a new instance that holds all the existing indexes
	 * in lists so that any duplicates are preserved.
	 */
	public static ResourceIndexedSearchParams withLists(ResourceTable theResourceTable) {
		return new ResourceIndexedSearchParams(theResourceTable, Mode.LIST);
	}

	private enum Mode {
		LIST {
			@Override
			public <T> Collection<T> newCollection() {
				return new ArrayList<>();
			}
		},
		SET {
			@Override
			public <T> Collection<T> newCollection() {
				return new HashSet<>();
			}
		},
		EMPTY {
			@Override
			public <T> Collection<T> newCollection() {
				return List.of();
			}
		};

		public abstract <T> Collection<T> newCollection();
	}
}
