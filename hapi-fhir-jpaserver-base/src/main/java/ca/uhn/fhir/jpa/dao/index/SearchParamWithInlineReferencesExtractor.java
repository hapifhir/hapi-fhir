package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedCompositeStringUniqueDao;
import ca.uhn.fhir.jpa.dao.r4.MatchResourceUrlService;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceLinkExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Lazy
public class SearchParamWithInlineReferencesExtractor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamWithInlineReferencesExtractor.class);

	@Autowired
	private MatchResourceUrlService myMatchResourceUrlService;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	ResourceLinkExtractor myResourceLinkExtractor;
	@Autowired
	DaoResourceLinkResolver myDaoResourceLinkResolver;
	@Autowired
	DaoSearchParamSynchronizer myDaoSearchParamSynchronizer;
	@Autowired
	private IResourceIndexedCompositeStringUniqueDao myResourceIndexedCompositeStringUniqueDao;


	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	public void populateFromResource(ResourceIndexedSearchParams theParams, IDao theCallingDao, Date theUpdateTime, ResourceTable theEntity, IBaseResource theResource, ResourceIndexedSearchParams existingParams) {
		mySearchParamExtractorService.extractFromResource(theParams, theEntity, theResource);

		Set<Map.Entry<String, RuntimeSearchParam>> activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theEntity.getResourceType()).entrySet();
		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.ENABLED) {
			theParams.findMissingSearchParams(myDaoConfig.getModelConfig(), theEntity, activeSearchParams);
		}

		theParams.setUpdatedTime(theUpdateTime);

		extractInlineReferences(theResource);

		myResourceLinkExtractor.extractResourceLinks(theParams, theEntity, theResource, theUpdateTime, myDaoResourceLinkResolver, true);

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

	private void extractCompositeStringUniques(ResourceTable theEntity, ResourceIndexedSearchParams theParams) {

		final String resourceType = theEntity.getResourceType();
		List<JpaRuntimeSearchParam> uniqueSearchParams = mySearchParamRegistry.getActiveUniqueSearchParams(resourceType);

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

			Set<String> queryStringsToPopulate = theParams.extractCompositeStringUniquesValueChains(resourceType, partsChoices);

			for (String nextQueryString : queryStringsToPopulate) {
				if (isNotBlank(nextQueryString)) {
					theParams.compositeStringUniques.add(new ResourceIndexedCompositeStringUnique(theEntity, nextQueryString));
				}
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
				Set<Long> matches = myMatchResourceUrlService.processMatchUrl(nextIdText, matchResourceType);
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

	public void storeCompositeStringUniques(ResourceIndexedSearchParams theParams, ResourceTable theEntity, ResourceIndexedSearchParams existingParams) {

		// Store composite string uniques
		if (myDaoConfig.isUniqueIndexesEnabled()) {
			for (ResourceIndexedCompositeStringUnique next : myDaoSearchParamSynchronizer.subtract(existingParams.compositeStringUniques, theParams.compositeStringUniques)) {
				ourLog.debug("Removing unique index: {}", next);
				myEntityManager.remove(next);
				theEntity.getParamsCompositeStringUnique().remove(next);
			}
			for (ResourceIndexedCompositeStringUnique next : myDaoSearchParamSynchronizer.subtract(theParams.compositeStringUniques, existingParams.compositeStringUniques)) {
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
}
