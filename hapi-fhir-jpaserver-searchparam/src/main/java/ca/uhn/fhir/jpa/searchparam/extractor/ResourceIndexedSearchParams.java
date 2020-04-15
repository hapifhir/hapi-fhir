package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.compare;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class ResourceIndexedSearchParams {
	final public Collection<ResourceIndexedSearchParamString> myStringParams = new ArrayList<>();
	final public Collection<ResourceIndexedSearchParamToken> myTokenParams = new HashSet<>();
	final public Collection<ResourceIndexedSearchParamNumber> myNumberParams = new ArrayList<>();
	final public Collection<ResourceIndexedSearchParamQuantity> myQuantityParams = new ArrayList<>();
	final public Collection<ResourceIndexedSearchParamDate> myDateParams = new ArrayList<>();
	final public Collection<ResourceIndexedSearchParamUri> myUriParams = new ArrayList<>();
	final public Collection<ResourceIndexedSearchParamCoords> myCoordsParams = new ArrayList<>();

	final public Collection<ResourceIndexedCompositeStringUnique> myCompositeStringUniques = new HashSet<>();
	final public Collection<ResourceLink> myLinks = new HashSet<>();
	final public Set<String> myPopulatedResourceLinkParameters = new HashSet<>();

	public ResourceIndexedSearchParams() {
	}

	public ResourceIndexedSearchParams(ResourceTable theEntity) {
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

		if (theEntity.isParamsCompositeStringUniquePresent()) {
			myCompositeStringUniques.addAll(theEntity.getParamsCompositeStringUnique());
		}
	}



	public Collection<ResourceLink> getResourceLinks() {
		return myLinks;
	}

	public void setParamsOn(ResourceTable theEntity) {
		theEntity.setParamsString(myStringParams);
		theEntity.setParamsStringPopulated(myStringParams.isEmpty() == false);
		theEntity.setParamsToken(myTokenParams);
		theEntity.setParamsTokenPopulated(myTokenParams.isEmpty() == false);
		theEntity.setParamsNumber(myNumberParams);
		theEntity.setParamsNumberPopulated(myNumberParams.isEmpty() == false);
		theEntity.setParamsQuantity(myQuantityParams);
		theEntity.setParamsQuantityPopulated(myQuantityParams.isEmpty() == false);
		theEntity.setParamsDate(myDateParams);
		theEntity.setParamsDatePopulated(myDateParams.isEmpty() == false);
		theEntity.setParamsUri(myUriParams);
		theEntity.setParamsUriPopulated(myUriParams.isEmpty() == false);
		theEntity.setParamsCoords(myCoordsParams);
		theEntity.setParamsCoordsPopulated(myCoordsParams.isEmpty() == false);
		theEntity.setParamsCompositeStringUniquePresent(myCompositeStringUniques.isEmpty() == false);
		theEntity.setResourceLinks(myLinks);
		theEntity.setHasLinks(myLinks.isEmpty() == false);
	}

	void setUpdatedTime(Date theUpdateTime) {
		setUpdatedTime(myStringParams, theUpdateTime);
		setUpdatedTime(myNumberParams, theUpdateTime);
		setUpdatedTime(myQuantityParams, theUpdateTime);
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
	public static Set<String> extractCompositeStringUniquesValueChains(String theResourceType, List<List<String>> thePartsChoices) {

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
		return queryStringsToPopulate;
	}

	private static void extractCompositeStringUniquesValueChains(String
																						 theResourceType, List<List<String>> thePartsChoices, List<String> theValues, Set<String> theQueryStringsToPopulate) {
		if (thePartsChoices.size() > 0) {
			List<String> nextList = thePartsChoices.get(0);
			Collections.sort(nextList);
			for (String nextChoice : nextList) {
				theValues.add(nextChoice);
				extractCompositeStringUniquesValueChains(theResourceType, thePartsChoices.subList(1, thePartsChoices.size()), theValues, theQueryStringsToPopulate);
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



	public void calculateHashes(Collection<? extends BaseResourceIndex> theStringParams) {
		for (BaseResourceIndex next : theStringParams) {
			next.calculateHashes();
		}
	}

	public Set<String> getPopulatedResourceLinkParameters() {
		return myPopulatedResourceLinkParameters;
	}

	public boolean matchParam(ModelConfig theModelConfig, String theResourceName, String theParamName, RuntimeSearchParam theParamDef, IQueryParameterType theParam) {
		if (theParamDef == null) {
			return false;
		}
		Collection<? extends BaseResourceIndexedSearchParam> resourceParams;
		switch (theParamDef.getParamType()) {
			case TOKEN:
				resourceParams = myTokenParams;
				break;
			case QUANTITY:
				resourceParams = myQuantityParams;
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
				return matchResourceLinks(theModelConfig, theResourceName, theParamName, theParam, theParamDef.getPath());
			case COMPOSITE:
			case HAS:
			case SPECIAL:
			default:
				resourceParams = null;
		}
		if (resourceParams == null) {
			return false;
		}
		Predicate<BaseResourceIndexedSearchParam> namedParamPredicate = param ->
			param.getParamName().equalsIgnoreCase(theParamName) &&
				param.matches(theParam);

		return resourceParams.stream().anyMatch(namedParamPredicate);
	}

	/**
	 * @deprecated Replace with the method below
	 */
	// KHS This needs to be public as libraries outside of hapi call it directly
	@Deprecated
	public boolean matchResourceLinks(String theResourceName, String theParamName, IQueryParameterType theParam, String theParamPath) {
		return matchResourceLinks(new ModelConfig(), theResourceName, theParamName, theParam, theParamPath);
	}

	// KHS This needs to be public as libraries outside of hapi call it directly
	public boolean matchResourceLinks(ModelConfig theModelConfig, String theResourceName, String theParamName, IQueryParameterType theParam, String theParamPath) {
		ReferenceParam reference = (ReferenceParam)theParam;

		Predicate<ResourceLink> namedParamPredicate = resourceLink ->
			searchParameterPathMatches(theResourceName, resourceLink, theParamName, theParamPath)
			 && resourceIdMatches(theModelConfig, resourceLink, reference);

		return myLinks.stream().anyMatch(namedParamPredicate);
	}

	private boolean resourceIdMatches(ModelConfig theModelConfig, ResourceLink theResourceLink, ReferenceParam theReference) {
		String baseUrl = theReference.getBaseUrl();
		if (isNotBlank(baseUrl)) {
			if (!theModelConfig.getTreatBaseUrlsAsLocal().contains(baseUrl)) {
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

	private boolean searchParameterPathMatches(String theResourceName, ResourceLink theResourceLink, String theParamName, String theParamPath) {
		String sourcePath = theResourceLink.getSourcePath();
		return sourcePath.equalsIgnoreCase(theParamPath);
	}

	@Override
	public String toString() {
		return "ResourceIndexedSearchParams{" +
			"stringParams=" + myStringParams +
			", tokenParams=" + myTokenParams +
			", numberParams=" + myNumberParams +
			", quantityParams=" + myQuantityParams +
			", dateParams=" + myDateParams +
			", uriParams=" + myUriParams +
			", coordsParams=" + myCoordsParams +
			", compositeStringUniques=" + myCompositeStringUniques +
			", links=" + myLinks +
			'}';
	}

	public void findMissingSearchParams(ModelConfig theModelConfig, ResourceTable theEntity, Set<Entry<String, RuntimeSearchParam>> theActiveSearchParams) {
		findMissingSearchParams(theModelConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.STRING, myStringParams);
		findMissingSearchParams(theModelConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.NUMBER, myNumberParams);
		findMissingSearchParams(theModelConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.QUANTITY, myQuantityParams);
		findMissingSearchParams(theModelConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.DATE, myDateParams);
		findMissingSearchParams(theModelConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.URI, myUriParams);
		findMissingSearchParams(theModelConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.TOKEN, myTokenParams);
	}

	@SuppressWarnings("unchecked")
	private <RT extends BaseResourceIndexedSearchParam> void findMissingSearchParams(ModelConfig theModelConfig, ResourceTable theEntity, Set<Map.Entry<String, RuntimeSearchParam>> activeSearchParams, RestSearchParameterTypeEnum type,
																												Collection<RT> paramCollection) {
		for (Map.Entry<String, RuntimeSearchParam> nextEntry : activeSearchParams) {
			String nextParamName = nextEntry.getKey();
			if (nextEntry.getValue().getParamType() == type) {
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
							param = new ResourceIndexedSearchParamString()
								.setModelConfig(theModelConfig);
							break;
						case TOKEN:
							param = new ResourceIndexedSearchParamToken();
							break;
						case URI:
							param = new ResourceIndexedSearchParamUri();
							break;
						case COMPOSITE:
						case HAS:
						case REFERENCE:
						case SPECIAL:
						default:
							continue;
					}
					param.setResource(theEntity);
					param.setMissing(true);
					param.setParamName(nextParamName);
					paramCollection.add((RT) param);
				}
			}
		}
	}


}
