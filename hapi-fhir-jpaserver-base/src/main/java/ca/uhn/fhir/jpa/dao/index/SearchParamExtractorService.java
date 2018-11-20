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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedCompositeStringUniqueDao;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.MatchUrlService;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Lazy
public class SearchParamExtractorService {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamExtractorService.class);

	@Autowired
	private DaoConfig myDaoConfig;
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


	public void populateFromResource(ResourceIndexedSearchParams theParams, IDao theCallingDao, Date theUpdateTime, ResourceTable theEntity, IBaseResource theResource, ResourceIndexedSearchParams existingParams) {
		extractFromResource(theParams, theEntity, theResource);

		Set<Map.Entry<String, RuntimeSearchParam>> activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theEntity.getResourceType()).entrySet();
		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.ENABLED) {
			theParams.findMissingSearchParams(myDaoConfig, theEntity, activeSearchParams);
		}

		theParams.setUpdatedTime(theUpdateTime);

		extractInlineReferences(theResource);

		extractResourceLinks(theParams, theEntity, theResource, theUpdateTime, true);

		/*
		 * If the existing resource already has links and those match links we still want, use them instead of removing them and re adding them
		 */
		for (Iterator<ResourceLink> existingLinkIter = existingParams.getResourceLinks().iterator(); existingLinkIter.hasNext(); ) {
			ResourceLink nextExisting = existingLinkIter.next();
			if (theParams.links.remove(nextExisting)) {
				existingLinkIter.remove();
				theParams.links.add(nextExisting);
			}
		}

		/*
		 * Handle composites
		 */
		extractCompositeStringUniques(theEntity, theParams);
	}

	public void extractFromResource(ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource) {
		theParams.stringParams.addAll(extractSearchParamStrings(theEntity, theResource));
		theParams.numberParams.addAll(extractSearchParamNumber(theEntity, theResource));
		theParams.quantityParams.addAll(extractSearchParamQuantity(theEntity, theResource));
		theParams.dateParams.addAll(extractSearchParamDates(theEntity, theResource));
		theParams.uriParams.addAll(extractSearchParamUri(theEntity, theResource));
		theParams.coordsParams.addAll(extractSearchParamCoords(theEntity, theResource));

		ourLog.trace("Storing date indexes: {}", theParams.dateParams);

		for (BaseResourceIndexedSearchParam next : extractSearchParamTokens(theEntity, theResource)) {
			if (next instanceof ResourceIndexedSearchParamToken) {
				theParams.tokenParams.add((ResourceIndexedSearchParamToken) next);
			} else {
				theParams.stringParams.add((ResourceIndexedSearchParamString) next);
			}
		}
	}

	/**
	 * Handle references within the resource that are match URLs, for example references like "Patient?identifier=foo". These match URLs are resolved and replaced with the ID of the
	 * matching resource.
	 */

	public void extractInlineReferences(IBaseResource theResource) {
		if (!myDaoConfig.isAllowInlineMatchUrlReferences()) {
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

	private void extractCompositeStringUniques(ResourceTable theEntity, ResourceIndexedSearchParams theParams) {

		List<JpaRuntimeSearchParam> uniqueSearchParams = mySearchParamRegistry.getActiveUniqueSearchParams(theEntity.getResourceType());

		for (JpaRuntimeSearchParam next : uniqueSearchParams) {

			List<List<String>> partsChoices = new ArrayList<>();

			for (RuntimeSearchParam nextCompositeOf : next.getCompositeOf()) {
				Collection<? extends BaseResourceIndexedSearchParam> paramsListForCompositePart = null;
				Collection<ResourceLink> linksForCompositePart = null;
				Collection<String> linksForCompositePartWantPaths = null;
				switch (nextCompositeOf.getParamType()) {
					case NUMBER:
						paramsListForCompositePart = theParams.numberParams;
						break;
					case DATE:
						paramsListForCompositePart = theParams.dateParams;
						break;
					case STRING:
						paramsListForCompositePart = theParams.stringParams;
						break;
					case TOKEN:
						paramsListForCompositePart = theParams.tokenParams;
						break;
					case REFERENCE:
						linksForCompositePart = theParams.links;
						linksForCompositePartWantPaths = new HashSet<>();
						linksForCompositePartWantPaths.addAll(nextCompositeOf.getPathsSplit());
						break;
					case QUANTITY:
						paramsListForCompositePart = theParams.quantityParams;
						break;
					case URI:
						paramsListForCompositePart = theParams.uriParams;
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

			Set<String> queryStringsToPopulate = theParams.extractCompositeStringUniquesValueChains(theEntity.getResourceType(), partsChoices);

			for (String nextQueryString : queryStringsToPopulate) {
				if (isNotBlank(nextQueryString)) {
					theParams.compositeStringUniques.add(new ResourceIndexedCompositeStringUnique(theEntity, nextQueryString));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void extractResourceLinks(ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, Date theUpdateTime, boolean lookUpReferencesInDatabase) {
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

				theParams.populatedResourceLinkParameters.add(nextSpDef.getName());

				if (LogicalReferenceHelper.isLogicalReference(myDaoConfig, nextId)) {
					ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, nextId, theUpdateTime);
					if (theParams.links.add(resourceLink)) {
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
					if (!myDaoConfig.getTreatBaseUrlsAsLocal().contains(baseUrl) && !myDaoConfig.isAllowExternalReferences()) {
						String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "externalReferenceNotAllowed", nextId.getValue());
						throw new InvalidRequestException(msg);
					} else {
						ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, nextId, theUpdateTime);
						if (theParams.links.add(resourceLink)) {
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
						if (myDaoConfig.isEnforceReferentialIntegrityOnWrite() == false) {
							continue;
						}
						RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(type);
						String resName = missingResourceDef.getName();

						if (myDaoConfig.isAutoCreatePlaceholderReferenceTargets()) {
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
				theParams.links.add(resourceLink);
			}

		}

		theEntity.setHasLinks(theParams.links.size() > 0);
	}

	public String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}

	public void removeCommon(ResourceIndexedSearchParams theParams, ResourceTable theEntity, ResourceIndexedSearchParams existingParams) {
		theParams.calculateHashes(theParams.stringParams);
		for (ResourceIndexedSearchParamString next : removeCommon(existingParams.stringParams, theParams.stringParams)) {
			next.setDaoConfig(myDaoConfig);
			myEntityManager.remove(next);
			theEntity.getParamsString().remove(next);
		}
		for (ResourceIndexedSearchParamString next : removeCommon(theParams.stringParams, existingParams.stringParams)) {
			myEntityManager.persist(next);
		}

		theParams.calculateHashes(theParams.tokenParams);
		for (ResourceIndexedSearchParamToken next : removeCommon(existingParams.tokenParams, theParams.tokenParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsToken().remove(next);
		}
		for (ResourceIndexedSearchParamToken next : removeCommon(theParams.tokenParams, existingParams.tokenParams)) {
			myEntityManager.persist(next);
		}

		theParams.calculateHashes(theParams.numberParams);
		for (ResourceIndexedSearchParamNumber next : removeCommon(existingParams.numberParams, theParams.numberParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsNumber().remove(next);
		}
		for (ResourceIndexedSearchParamNumber next : removeCommon(theParams.numberParams, existingParams.numberParams)) {
			myEntityManager.persist(next);
		}

		theParams.calculateHashes(theParams.quantityParams);
		for (ResourceIndexedSearchParamQuantity next : removeCommon(existingParams.quantityParams, theParams.quantityParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsQuantity().remove(next);
		}
		for (ResourceIndexedSearchParamQuantity next : removeCommon(theParams.quantityParams, existingParams.quantityParams)) {
			myEntityManager.persist(next);
		}

		// Store date SP's
		theParams.calculateHashes(theParams.dateParams);
		for (ResourceIndexedSearchParamDate next : removeCommon(existingParams.dateParams, theParams.dateParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsDate().remove(next);
		}
		for (ResourceIndexedSearchParamDate next : removeCommon(theParams.dateParams, existingParams.dateParams)) {
			myEntityManager.persist(next);
		}

		// Store URI SP's
		theParams.calculateHashes(theParams.uriParams);
		for (ResourceIndexedSearchParamUri next : removeCommon(existingParams.uriParams, theParams.uriParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsUri().remove(next);
		}
		for (ResourceIndexedSearchParamUri next : removeCommon(theParams.uriParams, existingParams.uriParams)) {
			myEntityManager.persist(next);
		}

		// Store Coords SP's
		theParams.calculateHashes(theParams.coordsParams);
		for (ResourceIndexedSearchParamCoords next : removeCommon(existingParams.coordsParams, theParams.coordsParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsCoords().remove(next);
		}
		for (ResourceIndexedSearchParamCoords next : removeCommon(theParams.coordsParams, existingParams.coordsParams)) {
			myEntityManager.persist(next);
		}

		// Store resource links
		for (ResourceLink next : removeCommon(existingParams.links, theParams.links)) {
			myEntityManager.remove(next);
			theEntity.getResourceLinks().remove(next);
		}
		for (ResourceLink next : removeCommon(theParams.links, existingParams.links)) {
			myEntityManager.persist(next);
		}

		// make sure links are indexed
		theEntity.setResourceLinks(theParams.links);

		// Store composite string uniques
		if (myDaoConfig.isUniqueIndexesEnabled()) {
			for (ResourceIndexedCompositeStringUnique next : removeCommon(existingParams.compositeStringUniques, theParams.compositeStringUniques)) {
				ourLog.debug("Removing unique index: {}", next);
				myEntityManager.remove(next);
				theEntity.getParamsCompositeStringUnique().remove(next);
			}
			for (ResourceIndexedCompositeStringUnique next : removeCommon(theParams.compositeStringUniques, existingParams.compositeStringUniques)) {
				if (myDaoConfig.isUniqueIndexesCheckedBeforeSave()) {
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

	private <T> Collection<T> removeCommon(Collection<T> theInput, Collection<T> theToRemove) {
		assert theInput != theToRemove;

		if (theInput.isEmpty()) {
			return theInput;
		}

		ArrayList<T> retVal = new ArrayList<>(theInput);
		retVal.removeAll(theToRemove);
		return retVal;
	}
}

