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

import static org.apache.commons.lang3.StringUtils.compare;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Reference;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedCompositeStringUniqueDao;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.service.IdHelperService;
import ca.uhn.fhir.jpa.service.MatchUrlService;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.*;

@Component
@Scope("prototype")
public class ResourceIndexedSearchParams {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceIndexedSearchParams.class);

	private final Collection<ResourceIndexedSearchParamString> stringParams = new ArrayList<>();
	private final Collection<ResourceIndexedSearchParamToken> tokenParams = new HashSet<>();
	private final Collection<ResourceIndexedSearchParamNumber> numberParams = new ArrayList<>();
	private final Collection<ResourceIndexedSearchParamQuantity> quantityParams = new ArrayList<>();
	private final Collection<ResourceIndexedSearchParamDate> dateParams = new ArrayList<>();
	private final Collection<ResourceIndexedSearchParamUri> uriParams = new ArrayList<>();
	private final Collection<ResourceIndexedSearchParamCoords> coordsParams = new ArrayList<>();

	private final Collection<ResourceIndexedCompositeStringUnique> compositeStringUniques = new HashSet<>();
	private final Collection<ResourceLink> links = new HashSet<>();
	private final Set<String> populatedResourceLinkParameters = new HashSet<>();

	@Autowired
	private DaoConfig myConfig;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IResourceIndexedCompositeStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private MatchUrlService myMatchUrlService;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

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

	public void populateFromResource(IDao theCallingDao, Date theUpdateTime, ResourceTable theEntity, IBaseResource theResource, ResourceIndexedSearchParams existingParams) {
		extractFromResource(theEntity, theResource);

		Set<Entry<String, RuntimeSearchParam>> activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theEntity.getResourceType()).entrySet();
		if (myConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.ENABLED) {
			findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.STRING, stringParams);
			findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.NUMBER, numberParams);
			findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.QUANTITY, quantityParams);
			findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.DATE, dateParams);
			findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.URI, uriParams);
			findMissingSearchParams(theEntity, activeSearchParams, RestSearchParameterTypeEnum.TOKEN, tokenParams);
		}

		setUpdatedTime(stringParams, theUpdateTime);
		setUpdatedTime(numberParams, theUpdateTime);
		setUpdatedTime(quantityParams, theUpdateTime);
		setUpdatedTime(dateParams, theUpdateTime);
		setUpdatedTime(uriParams, theUpdateTime);
		setUpdatedTime(coordsParams, theUpdateTime);
		setUpdatedTime(tokenParams, theUpdateTime);

		extractInlineReferences(theResource);

		extractResourceLinks(theEntity, theResource, theUpdateTime, true);

		/*
		 * If the existing resource already has links and those match links we still want, use them instead of removing them and re adding them
		 */
		for (Iterator<ResourceLink> existingLinkIter = existingParams.getResourceLinks().iterator(); existingLinkIter.hasNext(); ) {
			ResourceLink nextExisting = existingLinkIter.next();
			if (links.remove(nextExisting)) {
				existingLinkIter.remove();
				links.add(nextExisting);
			}
		}

		/*
		 * Handle composites
		 */
		extractCompositeStringUniques(theEntity, stringParams, tokenParams, numberParams, quantityParams, dateParams, uriParams, links);
	}

	/*
	 * Handle references within the resource that are match URLs, for example references like "Patient?identifier=foo". These match URLs are resolved and replaced with the ID of the
	 * matching resource.
	 */

	public void extractInlineReferences(IBaseResource theResource) {
		if (!myConfig.isAllowInlineMatchUrlReferences()) {
			return;
		}
		FhirTerser terser = myContext.newTerser();
		List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(theResource, IBaseReference.class);
		for (IBaseReference nextRef : allRefs) {
			IIdType nextId = nextRef.getReferenceElement();
			String nextIdText = nextId.getValue();
			if (nextIdText == null) {
				continue;
			}
			int qmIndex = nextIdText.indexOf('?');
			if (qmIndex != -1) {
				for (int i = qmIndex - 1; i >= 0; i--) {
					if (nextIdText.charAt(i) == '/') {
						if (i < nextIdText.length() - 1 && nextIdText.charAt(i + 1) == '?') {
							// Just in case the URL is in the form Patient/?foo=bar
							continue;
						}
						nextIdText = nextIdText.substring(i + 1);
						break;
					}
				}
				String resourceTypeString = nextIdText.substring(0, nextIdText.indexOf('?')).replace("/", "");
				RuntimeResourceDefinition matchResourceDef = myContext.getResourceDefinition(resourceTypeString);
				if (matchResourceDef == null) {
					String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "invalidMatchUrlInvalidResourceType", nextId.getValue(), resourceTypeString);
					throw new InvalidRequestException(msg);
				}
				Class<? extends IBaseResource> matchResourceType = matchResourceDef.getImplementingClass();
				Set<Long> matches = myMatchUrlService.processMatchUrl(nextIdText, matchResourceType);
				if (matches.isEmpty()) {
					String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "invalidMatchUrlNoMatches", nextId.getValue());
					throw new ResourceNotFoundException(msg);
				}
				if (matches.size() > 1) {
					String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "invalidMatchUrlMultipleMatches", nextId.getValue());
					throw new PreconditionFailedException(msg);
				}
				Long next = matches.iterator().next();
				String newId = myIdHelperService.translatePidIdToForcedId(resourceTypeString, next);
				ourLog.debug("Replacing inline match URL[{}] with ID[{}}", nextId.getValue(), newId);
				nextRef.setReference(newId);
			}
		}
	}

	public void extractFromResource(ResourceTable theEntity, IBaseResource theResource) {
		stringParams.addAll(extractSearchParamStrings(theEntity, theResource));
		numberParams.addAll(extractSearchParamNumber(theEntity, theResource));
		quantityParams.addAll(extractSearchParamQuantity(theEntity, theResource));
		dateParams.addAll(extractSearchParamDates(theEntity, theResource));
		uriParams.addAll(extractSearchParamUri(theEntity, theResource));
		coordsParams.addAll(extractSearchParamCoords(theEntity, theResource));

		ourLog.trace("Storing date indexes: {}", dateParams);

		for (BaseResourceIndexedSearchParam next : extractSearchParamTokens(theEntity, theResource)) {
			if (next instanceof ResourceIndexedSearchParamToken) {
				tokenParams.add((ResourceIndexedSearchParamToken) next);
			} else {
				stringParams.add((ResourceIndexedSearchParamString) next);
			}
		}
	}

	public Collection<ResourceLink> getResourceLinks() {
		return links;
	}


	protected Set<ResourceIndexedSearchParamCoords> extractSearchParamCoords(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamCoords(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamDates(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamNumber(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamQuantity(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamStrings(theEntity, theResource);
	}

	protected Set<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamTokens(theEntity, theResource);
	}

	protected Set<ResourceIndexedSearchParamUri> extractSearchParamUri(ResourceTable theEntity, IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamUri(theEntity, theResource);
	}

	@SuppressWarnings("unchecked")
	private <RT extends BaseResourceIndexedSearchParam> void findMissingSearchParams(ResourceTable theEntity, Set<Entry<String, RuntimeSearchParam>> activeSearchParams, RestSearchParameterTypeEnum type,
																												Collection<RT> paramCollection) {
		for (Entry<String, RuntimeSearchParam> nextEntry : activeSearchParams) {
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
								.setDaoConfig(myConfig);
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


	private void extractCompositeStringUniques(ResourceTable theEntity, Collection<ResourceIndexedSearchParamString> theStringParams, Collection<ResourceIndexedSearchParamToken> theTokenParams, Collection<ResourceIndexedSearchParamNumber> theNumberParams, Collection<ResourceIndexedSearchParamQuantity> theQuantityParams, Collection<ResourceIndexedSearchParamDate> theDateParams, Collection<ResourceIndexedSearchParamUri> theUriParams, Collection<ResourceLink> theLinks) {
		List<JpaRuntimeSearchParam> uniqueSearchParams = mySearchParamRegistry.getActiveUniqueSearchParams(theEntity.getResourceType());
		for (JpaRuntimeSearchParam next : uniqueSearchParams) {

			List<List<String>> partsChoices = new ArrayList<>();

			for (RuntimeSearchParam nextCompositeOf : next.getCompositeOf()) {
				Collection<? extends BaseResourceIndexedSearchParam> paramsListForCompositePart = null;
				Collection<ResourceLink> linksForCompositePart = null;
				Collection<String> linksForCompositePartWantPaths = null;
				switch (nextCompositeOf.getParamType()) {
					case NUMBER:
						paramsListForCompositePart = theNumberParams;
						break;
					case DATE:
						paramsListForCompositePart = theDateParams;
						break;
					case STRING:
						paramsListForCompositePart = theStringParams;
						break;
					case TOKEN:
						paramsListForCompositePart = theTokenParams;
						break;
					case REFERENCE:
						linksForCompositePart = theLinks;
						linksForCompositePartWantPaths = new HashSet<>();
						linksForCompositePartWantPaths.addAll(nextCompositeOf.getPathsSplit());
						break;
					case QUANTITY:
						paramsListForCompositePart = theQuantityParams;
						break;
					case URI:
						paramsListForCompositePart = theUriParams;
						break;
					case COMPOSITE:
					case HAS:
						break;
				}

				ArrayList<String> nextChoicesList = new ArrayList<>();
				partsChoices.add(nextChoicesList);

				String key = UrlUtil.escapeUrlParam(nextCompositeOf.getName());
				if (paramsListForCompositePart != null) {
					for (BaseResourceIndexedSearchParam nextParam : paramsListForCompositePart) {
						if (nextParam.getParamName().equals(nextCompositeOf.getName())) {
							IQueryParameterType nextParamAsClientParam = nextParam.toQueryParameterType();
							String value = nextParamAsClientParam.getValueAsQueryToken(myContext);
							if (isNotBlank(value)) {
								value = UrlUtil.escapeUrlParam(value);
								nextChoicesList.add(key + "=" + value);
							}
						}
					}
				}
				if (linksForCompositePart != null) {
					for (ResourceLink nextLink : linksForCompositePart) {
						if (linksForCompositePartWantPaths.contains(nextLink.getSourcePath())) {
							String value = nextLink.getTargetResource().getIdDt().toUnqualifiedVersionless().getValue();
							if (isNotBlank(value)) {
								value = UrlUtil.escapeUrlParam(value);
								nextChoicesList.add(key + "=" + value);
							}
						}
					}
				}
			}

			Set<String> queryStringsToPopulate = extractCompositeStringUniquesValueChains(theEntity.getResourceType(), partsChoices);

			for (String nextQueryString : queryStringsToPopulate) {
				if (isNotBlank(nextQueryString)) {
					compositeStringUniques.add(new ResourceIndexedCompositeStringUnique(theEntity, nextQueryString));
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


	/**
	 * @return Returns a set containing all of the parameter names that
	 * were found to have a value
	 */
	@SuppressWarnings("unchecked")
	public void extractResourceLinks(ResourceTable theEntity, IBaseResource theResource, Date theUpdateTime, boolean lookUpReferencesInDatabase) {
		String resourceType = theEntity.getResourceType();

		/*
		 * For now we don't try to load any of the links in a bundle if it's the actual bundle we're storing..
		 */
		if (theResource instanceof IBaseBundle) {
			return;
		}

		Map<String, RuntimeSearchParam> searchParams = mySearchParamRegistry.getActiveSearchParams(toResourceName(theResource.getClass()));
		for (RuntimeSearchParam nextSpDef : searchParams.values()) {

			if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
				continue;
			}

			String nextPathsUnsplit = nextSpDef.getPath();
			if (isBlank(nextPathsUnsplit)) {
				continue;
			}

			boolean multiType = false;
			if (nextPathsUnsplit.endsWith("[x]")) {
				multiType = true;
			}

			List<PathAndRef> refs = mySearchParamExtractor.extractResourceLinks(theResource, nextSpDef);
			for (PathAndRef nextPathAndRef : refs) {
				Object nextObject = nextPathAndRef.getRef();

				/*
				 * A search parameter on an extension field that contains
				 * references should index those references
				 */
				if (nextObject instanceof IBaseExtension<?, ?>) {
					nextObject = ((IBaseExtension<?, ?>) nextObject).getValue();
				}

				if (nextObject instanceof CanonicalType) {
					nextObject = new Reference(((CanonicalType) nextObject).getValueAsString());
				}

				IIdType nextId;
				if (nextObject instanceof IBaseReference) {
					IBaseReference nextValue = (IBaseReference) nextObject;
					if (nextValue.isEmpty()) {
						continue;
					}
					nextId = nextValue.getReferenceElement();

					/*
					 * This can only really happen if the DAO is being called
					 * programatically with a Bundle (not through the FHIR REST API)
					 * but Smile does this
					 */
					if (nextId.isEmpty() && nextValue.getResource() != null) {
						nextId = nextValue.getResource().getIdElement();
					}

					if (nextId.isEmpty() || nextId.getValue().startsWith("#")) {
						// This is a blank or contained resource reference
						continue;
					}
				} else if (nextObject instanceof IBaseResource) {
					nextId = ((IBaseResource) nextObject).getIdElement();
					if (nextId == null || nextId.hasIdPart() == false) {
						continue;
					}
				} else if (myContext.getElementDefinition((Class<? extends IBase>) nextObject.getClass()).getName().equals("uri")) {
					continue;
				} else if (resourceType.equals("Consent") && nextPathAndRef.getPath().equals("Consent.source")) {
					// Consent#source-identifier has a path that isn't typed - This is a one-off to deal with that
					continue;
				} else {
					if (!multiType) {
						if (nextSpDef.getName().equals("sourceuri")) {
							continue; // TODO: disable this eventually - ConceptMap:sourceuri is of type reference but points to a URI
						}
						throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
					} else {
						continue;
					}
				}

				populatedResourceLinkParameters.add(nextSpDef.getName());

				if (LogicalReferenceHelper.isLogicalReference(myConfig, nextId)) {
					ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, nextId, theUpdateTime);
					if (links.add(resourceLink)) {
						ourLog.debug("Indexing remote resource reference URL: {}", nextId);
					}
					continue;
				}

				String baseUrl = nextId.getBaseUrl();
				String typeString = nextId.getResourceType();
				if (isBlank(typeString)) {
					throw new InvalidRequestException("Invalid resource reference found at path[" + nextPathsUnsplit + "] - Does not contain resource type - " + nextId.getValue());
				}
				RuntimeResourceDefinition resourceDefinition;
				try {
					resourceDefinition = myContext.getResourceDefinition(typeString);
				} catch (DataFormatException e) {
					throw new InvalidRequestException(
						"Invalid resource reference found at path[" + nextPathsUnsplit + "] - Resource type is unknown or not supported on this server - " + nextId.getValue());
				}

				if (isNotBlank(baseUrl)) {
					if (!myConfig.getTreatBaseUrlsAsLocal().contains(baseUrl) && !myConfig.isAllowExternalReferences()) {
						String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "externalReferenceNotAllowed", nextId.getValue());
						throw new InvalidRequestException(msg);
					} else {
						ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, nextId, theUpdateTime);
						if (links.add(resourceLink)) {
							ourLog.debug("Indexing remote resource reference URL: {}", nextId);
						}
						continue;
					}
				}

				Class<? extends IBaseResource> type = resourceDefinition.getImplementingClass();
				String id = nextId.getIdPart();
				if (StringUtils.isBlank(id)) {
					throw new InvalidRequestException("Invalid resource reference found at path[" + nextPathsUnsplit + "] - Does not contain resource ID - " + nextId.getValue());
				}

				myDaoRegistry.getDaoOrThrowException(type);
				ResourceTable target;
				if (lookUpReferencesInDatabase) {
					Long valueOf;
					try {
						valueOf = myIdHelperService.translateForcedIdToPid(typeString, id);
					} catch (ResourceNotFoundException e) {
						if (myConfig.isEnforceReferentialIntegrityOnWrite() == false) {
							continue;
						}
						RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(type);
						String resName = missingResourceDef.getName();

						if (myConfig.isAutoCreatePlaceholderReferenceTargets()) {
							IBaseResource newResource = missingResourceDef.newInstance();
							newResource.setId(resName + "/" + id);
							IFhirResourceDao<IBaseResource> placeholderResourceDao = (IFhirResourceDao<IBaseResource>) myDaoRegistry.getResourceDao(newResource.getClass());
							ourLog.debug("Automatically creating empty placeholder resource: {}", newResource.getIdElement().getValue());
							valueOf = placeholderResourceDao.update(newResource).getEntity().getId();
						} else {
							throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPathsUnsplit);
						}
					}
					target = myEntityManager.find(ResourceTable.class, valueOf);
					RuntimeResourceDefinition targetResourceDef = myContext.getResourceDefinition(type);
					if (target == null) {
						String resName = targetResourceDef.getName();
						throw new InvalidRequestException("Resource " + resName + "/" + id + " not found, specified in path: " + nextPathsUnsplit);
					}

					if (!typeString.equals(target.getResourceType())) {
						throw new UnprocessableEntityException(
							"Resource contains reference to " + nextId.getValue() + " but resource with ID " + nextId.getIdPart() + " is actually of type " + target.getResourceType());
					}

					if (target.getDeleted() != null) {
						String resName = targetResourceDef.getName();
						throw new InvalidRequestException("Resource " + resName + "/" + id + " is deleted, specified in path: " + nextPathsUnsplit);
					}

					if (nextSpDef.getTargets() != null && !nextSpDef.getTargets().contains(typeString)) {
						continue;
					}
				} else {
					target = new ResourceTable();
					target.setResourceType(typeString);
					if (nextId.isIdPartValidLong()) {
						target.setId(nextId.getIdPartAsLong());
					} else {
						ForcedId forcedId = new ForcedId();
						forcedId.setForcedId(id);
						target.setForcedId(forcedId);
					}
				}
				ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, target, theUpdateTime);
				links.add(resourceLink);
			}

		}

		theEntity.setHasLinks(links.size() > 0);
	}

	public String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}

	private void setUpdatedTime(Collection<? extends BaseResourceIndexedSearchParam> theParams, Date theUpdateTime) {
		for (BaseResourceIndexedSearchParam nextSearchParam : theParams) {
			nextSearchParam.setUpdated(theUpdateTime);
		}
	}


	public void removeCommon(ResourceTable theEntity, ResourceIndexedSearchParams existingParams) {
		calculateHashes(stringParams);
		for (ResourceIndexedSearchParamString next : removeCommon(existingParams.stringParams, stringParams)) {
			next.setDaoConfig(myConfig);
			myEntityManager.remove(next);
			theEntity.getParamsString().remove(next);
		}
		for (ResourceIndexedSearchParamString next : removeCommon(stringParams, existingParams.stringParams)) {
			myEntityManager.persist(next);
		}

		calculateHashes(tokenParams);
		for (ResourceIndexedSearchParamToken next : removeCommon(existingParams.tokenParams, tokenParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsToken().remove(next);
		}
		for (ResourceIndexedSearchParamToken next : removeCommon(tokenParams, existingParams.tokenParams)) {
			myEntityManager.persist(next);
		}

		calculateHashes(numberParams);
		for (ResourceIndexedSearchParamNumber next : removeCommon(existingParams.numberParams, numberParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsNumber().remove(next);
		}
		for (ResourceIndexedSearchParamNumber next : removeCommon(numberParams, existingParams.numberParams)) {
			myEntityManager.persist(next);
		}

		calculateHashes(quantityParams);
		for (ResourceIndexedSearchParamQuantity next : removeCommon(existingParams.quantityParams, quantityParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsQuantity().remove(next);
		}
		for (ResourceIndexedSearchParamQuantity next : removeCommon(quantityParams, existingParams.quantityParams)) {
			myEntityManager.persist(next);
		}

		// Store date SP's
		calculateHashes(dateParams);
		for (ResourceIndexedSearchParamDate next : removeCommon(existingParams.dateParams, dateParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsDate().remove(next);
		}
		for (ResourceIndexedSearchParamDate next : removeCommon(dateParams, existingParams.dateParams)) {
			myEntityManager.persist(next);
		}

		// Store URI SP's
		calculateHashes(uriParams);
		for (ResourceIndexedSearchParamUri next : removeCommon(existingParams.uriParams, uriParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsUri().remove(next);
		}
		for (ResourceIndexedSearchParamUri next : removeCommon(uriParams, existingParams.uriParams)) {
			myEntityManager.persist(next);
		}

		// Store Coords SP's
		calculateHashes(coordsParams);
		for (ResourceIndexedSearchParamCoords next : removeCommon(existingParams.coordsParams, coordsParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsCoords().remove(next);
		}
		for (ResourceIndexedSearchParamCoords next : removeCommon(coordsParams, existingParams.coordsParams)) {
			myEntityManager.persist(next);
		}

		// Store resource links
		for (ResourceLink next : removeCommon(existingParams.links, links)) {
			myEntityManager.remove(next);
			theEntity.getResourceLinks().remove(next);
		}
		for (ResourceLink next : removeCommon(links, existingParams.links)) {
			myEntityManager.persist(next);
		}

		// make sure links are indexed
		theEntity.setResourceLinks(links);

		// Store composite string uniques
		if (myConfig.isUniqueIndexesEnabled()) {
			for (ResourceIndexedCompositeStringUnique next : removeCommon(existingParams.compositeStringUniques, compositeStringUniques)) {
				ourLog.debug("Removing unique index: {}", next);
				myEntityManager.remove(next);
				theEntity.getParamsCompositeStringUnique().remove(next);
			}
			for (ResourceIndexedCompositeStringUnique next : removeCommon(compositeStringUniques, existingParams.compositeStringUniques)) {
				if (myConfig.isUniqueIndexesCheckedBeforeSave()) {
					ResourceIndexedCompositeStringUnique existing = myResourceIndexedCompositeStringUniqueDao.findByQueryString(next.getIndexString());
					if (existing != null) {
						String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "uniqueIndexConflictFailure", theEntity.getResourceType(), next.getIndexString(), existing.getResource().getIdDt().toUnqualifiedVersionless().getValue());
						throw new PreconditionFailedException(msg);
					}
				}
				ourLog.debug("Persisting unique index: {}", next);
				myEntityManager.persist(next);
			}
		}
	}

	private void calculateHashes(Collection<? extends BaseResourceIndexedSearchParam> theStringParams) {
		for (BaseResourceIndexedSearchParam next : theStringParams) {
			next.calculateHashes();
		}
	}

	private <T> Collection<T> removeCommon(Collection<T> theInput, Collection<T> theToRemove) {
		assert theInput != theToRemove;

		if (theInput.isEmpty()) {
			return theInput;
		}

		ArrayList<T> retVal = new ArrayList<>(theInput);
		retVal.removeAll(theToRemove);
		return retVal;
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
}
