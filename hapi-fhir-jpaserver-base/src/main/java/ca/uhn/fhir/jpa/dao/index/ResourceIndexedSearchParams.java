package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.*;

public class ResourceIndexedSearchParams {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceIndexedSearchParams.class);

	final Collection<ResourceIndexedSearchParamString> stringParams = new ArrayList<>();
	final Collection<ResourceIndexedSearchParamToken> tokenParams = new HashSet<>();
	final Collection<ResourceIndexedSearchParamNumber> numberParams = new ArrayList<>();
	final Collection<ResourceIndexedSearchParamQuantity> quantityParams = new ArrayList<>();
	final Collection<ResourceIndexedSearchParamDate> dateParams = new ArrayList<>();
	final Collection<ResourceIndexedSearchParamUri> uriParams = new ArrayList<>();
	final Collection<ResourceIndexedSearchParamCoords> coordsParams = new ArrayList<>();

	final Collection<ResourceIndexedCompositeStringUnique> compositeStringUniques = new HashSet<>();
	final Collection<ResourceLink> links = new HashSet<>();
	final Set<String> populatedResourceLinkParameters = new HashSet<>();


	public ResourceIndexedSearchParams() {
	}

	public ResourceIndexedSearchParams(ResourceTable theEntity) {
		if (theEntity.isParamsStringPopulated()) {
			stringParams.addAll(theEntity.getParamsString());
		}
		if (theEntity.isParamsTokenPopulated()) {
			tokenParams.addAll(theEntity.getParamsToken());
		}
		if (theEntity.isParamsNumberPopulated()) {
			numberParams.addAll(theEntity.getParamsNumber());
		}
		if (theEntity.isParamsQuantityPopulated()) {
			quantityParams.addAll(theEntity.getParamsQuantity());
		}
		if (theEntity.isParamsDatePopulated()) {
			dateParams.addAll(theEntity.getParamsDate());
		}
		if (theEntity.isParamsUriPopulated()) {
			uriParams.addAll(theEntity.getParamsUri());
		}
		if (theEntity.isParamsCoordsPopulated()) {
			coordsParams.addAll(theEntity.getParamsCoords());
		}
		if (theEntity.isHasLinks()) {
			links.addAll(theEntity.getResourceLinks());
		}

		if (theEntity.isParamsCompositeStringUniquePresent()) {
			compositeStringUniques.addAll(theEntity.getParamsCompositeStringUnique());
		}
	}



	public Collection<ResourceLink> getResourceLinks() {
		return links;
	}

	public void setParamsOn(ResourceTable theEntity) {
		theEntity.setParamsString(stringParams);
		theEntity.setParamsStringPopulated(stringParams.isEmpty() == false);
		theEntity.setParamsToken(tokenParams);
		theEntity.setParamsTokenPopulated(tokenParams.isEmpty() == false);
		theEntity.setParamsNumber(numberParams);
		theEntity.setParamsNumberPopulated(numberParams.isEmpty() == false);
		theEntity.setParamsQuantity(quantityParams);
		theEntity.setParamsQuantityPopulated(quantityParams.isEmpty() == false);
		theEntity.setParamsDate(dateParams);
		theEntity.setParamsDatePopulated(dateParams.isEmpty() == false);
		theEntity.setParamsUri(uriParams);
		theEntity.setParamsUriPopulated(uriParams.isEmpty() == false);
		theEntity.setParamsCoords(coordsParams);
		theEntity.setParamsCoordsPopulated(coordsParams.isEmpty() == false);
		theEntity.setParamsCompositeStringUniquePresent(compositeStringUniques.isEmpty() == false);
		theEntity.setResourceLinks(links);
		theEntity.setHasLinks(links.isEmpty() == false);
	}

	public void setUpdatedTime(Date theUpdateTime) {
		setUpdatedTime(stringParams, theUpdateTime);
		setUpdatedTime(numberParams, theUpdateTime);
		setUpdatedTime(quantityParams, theUpdateTime);
		setUpdatedTime(dateParams, theUpdateTime);
		setUpdatedTime(uriParams, theUpdateTime);
		setUpdatedTime(coordsParams, theUpdateTime);
		setUpdatedTime(tokenParams, theUpdateTime);
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
	public static Set<String> extractCompositeStringUniquesValueChains(String
																								 theResourceType, List<List<String>> thePartsChoices) {

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



	void calculateHashes(Collection<? extends BaseResourceIndexedSearchParam> theStringParams) {
		for (BaseResourceIndexedSearchParam next : theStringParams) {
			next.calculateHashes();
		}
	}

	public Set<String> getPopulatedResourceLinkParameters() {
		return populatedResourceLinkParameters;
	}

	public boolean matchParam(String theResourceName, String theParamName, RuntimeSearchParam paramDef, IQueryParameterType theParam) {
		if (paramDef == null) {
			return false;
		}
		Collection<? extends BaseResourceIndexedSearchParam> resourceParams;
		switch (paramDef.getParamType()) {
			case TOKEN:
				resourceParams = tokenParams;
				break;
			case QUANTITY:
				resourceParams = quantityParams;
				break;
			case STRING:
				resourceParams = stringParams;
				break;
			case NUMBER:
				resourceParams = numberParams;
				break;
			case URI:
				resourceParams = uriParams;
				break;
			case DATE:
				resourceParams = dateParams;
				break;
			case REFERENCE:
				return matchResourceLinks(theResourceName, theParamName, theParam);
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

	private boolean matchResourceLinks(String theResourceName, String theParamName, IQueryParameterType theParam) {
		ReferenceParam reference = (ReferenceParam)theParam;

		Predicate<ResourceLink> namedParamPredicate = resourceLink ->
			resourceLinkMatches(theResourceName, resourceLink, theParamName)
			 && resourceIdMatches(resourceLink, reference);

		return links.stream().anyMatch(namedParamPredicate);
	}

	private boolean resourceIdMatches(ResourceLink theResourceLink, ReferenceParam theReference) {
		ResourceTable target = theResourceLink.getTargetResource();
		IdDt idDt = target.getIdDt();
		if (idDt.isIdPartValidLong()) {
			return theReference.getIdPartAsLong() == idDt.getIdPartAsLong();
		} else {
			ForcedId forcedId = target.getForcedId();
			if (forcedId != null) {
				return forcedId.getForcedId().equals(theReference.getValue());
			} else {
				return false;
			}
		}
	}

	private boolean resourceLinkMatches(String theResourceName, ResourceLink theResourceLink, String theParamName) {
		return theResourceLink.getTargetResource().getResourceType().equalsIgnoreCase(theParamName) ||
			theResourceLink.getSourcePath().equalsIgnoreCase(theResourceName+"."+theParamName);
	}

	@Override
	public String toString() {
		return "ResourceIndexedSearchParams{" +
			"stringParams=" + stringParams +
			", tokenParams=" + tokenParams +
			", numberParams=" + numberParams +
			", quantityParams=" + quantityParams +
			", dateParams=" + dateParams +
			", uriParams=" + uriParams +
			", coordsParams=" + coordsParams +
			", compositeStringUniques=" + compositeStringUniques +
			", links=" + links +
			'}';
	}

	void findMissingSearchParams(DaoConfig theDaoConfig, ResourceTable theEntity, Set<Entry<String, RuntimeSearchParam>> theActiveSearchParams) {
		findMissingSearchParams(theDaoConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.STRING, stringParams);
		findMissingSearchParams(theDaoConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.NUMBER, numberParams);
		findMissingSearchParams(theDaoConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.QUANTITY, quantityParams);
		findMissingSearchParams(theDaoConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.DATE, dateParams);
		findMissingSearchParams(theDaoConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.URI, uriParams);
		findMissingSearchParams(theDaoConfig, theEntity, theActiveSearchParams, RestSearchParameterTypeEnum.TOKEN, tokenParams);
	}

	@SuppressWarnings("unchecked")
	private <RT extends BaseResourceIndexedSearchParam> void findMissingSearchParams(DaoConfig theDaoConfig, ResourceTable theEntity, Set<Map.Entry<String, RuntimeSearchParam>> activeSearchParams, RestSearchParameterTypeEnum type,
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
								.setDaoConfig(theDaoConfig);
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
