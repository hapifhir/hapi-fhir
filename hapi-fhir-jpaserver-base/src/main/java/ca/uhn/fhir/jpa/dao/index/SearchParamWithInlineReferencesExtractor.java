package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.MatchResourceUrlService;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedCompositeStringUniqueDao;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private DaoResourceLinkResolver myDaoResourceLinkResolver;
	@Autowired
	private DaoSearchParamSynchronizer myDaoSearchParamSynchronizer;
	@Autowired
	private IResourceIndexedCompositeStringUniqueDao myResourceIndexedCompositeStringUniqueDao;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	public void populateFromResource(ResourceIndexedSearchParams theParams, Date theUpdateTime, ResourceTable theEntity, IBaseResource theResource, ResourceIndexedSearchParams theExistingParams, RequestDetails theRequest) {
		extractInlineReferences(theResource, theRequest);

		mySearchParamExtractorService.extractFromResource(theRequest, theParams, theEntity, theResource, theUpdateTime, true);

		Set<Map.Entry<String, RuntimeSearchParam>> activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theEntity.getResourceType()).entrySet();
		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.ENABLED) {
			theParams.findMissingSearchParams(myDaoConfig.getModelConfig(), theEntity, activeSearchParams);
		}

		/*
		 * If the existing resource already has links and those match links we still want, use them instead of removing them and re adding them
		 */
		for (Iterator<ResourceLink> existingLinkIter = theExistingParams.getResourceLinks().iterator(); existingLinkIter.hasNext(); ) {
			ResourceLink nextExisting = existingLinkIter.next();
			if (theParams.myLinks.remove(nextExisting)) {
				existingLinkIter.remove();
				theParams.myLinks.add(nextExisting);
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
						paramsListForCompositePart = theParams.myNumberParams;
						break;
					case DATE:
						paramsListForCompositePart = theParams.myDateParams;
						break;
					case STRING:
						paramsListForCompositePart = theParams.myStringParams;
						break;
					case TOKEN:
						paramsListForCompositePart = theParams.myTokenParams;
						break;
					case REFERENCE:
						linksForCompositePart = theParams.myLinks;
						linksForCompositePartWantPaths = new HashSet<>(nextCompositeOf.getPathsSplit());
						break;
					case QUANTITY:
						paramsListForCompositePart = theParams.myQuantityParams;
						break;
					case URI:
						paramsListForCompositePart = theParams.myUriParams;
						break;
					case SPECIAL:
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
							assert isNotBlank(nextLink.getTargetResourceType());
							assert isNotBlank(nextLink.getTargetResourceId());
							String value = nextLink.getTargetResourceType() + "/" + nextLink.getTargetResourceId();
							if (isNotBlank(value)) {
								value = UrlUtil.escapeUrlParam(value);
								nextChoicesList.add(key + "=" + value);
							}
						}
					}
				}
			}

			Set<String> queryStringsToPopulate = ResourceIndexedSearchParams.extractCompositeStringUniquesValueChains(resourceType, partsChoices);

			for (String nextQueryString : queryStringsToPopulate) {
				if (isNotBlank(nextQueryString)) {
					ourLog.trace("Adding composite unique SP: {}", nextQueryString);
					theParams.myCompositeStringUniques.add(new ResourceIndexedCompositeStringUnique(theEntity, nextQueryString));
				}
			}
		}
	}



	/**
	 * Handle references within the resource that are match URLs, for example references like "Patient?identifier=foo". These match URLs are resolved and replaced with the ID of the
	 * matching resource.
	 */
	public void extractInlineReferences(IBaseResource theResource, RequestDetails theRequest) {
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
				Set<ResourcePersistentId> matches = myMatchResourceUrlService.processMatchUrl(nextIdText, matchResourceType, theRequest);

				ResourcePersistentId match;
				if (matches.isEmpty()) {

					Optional<ResourceTable> placeholderOpt = myDaoResourceLinkResolver.createPlaceholderTargetIfConfiguredToDoSo(matchResourceType, nextRef, null);
					if (placeholderOpt.isPresent()) {
						match = new ResourcePersistentId(placeholderOpt.get().getResourceId());
					} else {
						String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "invalidMatchUrlNoMatches", nextId.getValue());
						throw new ResourceNotFoundException(msg);
					}

				} else if (matches.size() > 1) {
					String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "invalidMatchUrlMultipleMatches", nextId.getValue());
					throw new PreconditionFailedException(msg);
				} else {
					match = matches.iterator().next();
				}

				IIdType newId = myIdHelperService.translatePidIdToForcedId(myContext, resourceTypeString, match);
				ourLog.debug("Replacing inline match URL[{}] with ID[{}}", nextId.getValue(), newId);
				nextRef.setReference(newId.getValue());
			}
		}
	}

	public void storeCompositeStringUniques(ResourceIndexedSearchParams theParams, ResourceTable theEntity, ResourceIndexedSearchParams existingParams) {

		// Store composite string uniques
		if (myDaoConfig.isUniqueIndexesEnabled()) {
			for (ResourceIndexedCompositeStringUnique next : myDaoSearchParamSynchronizer.subtract(existingParams.myCompositeStringUniques, theParams.myCompositeStringUniques)) {
				ourLog.debug("Removing unique index: {}", next);
				myEntityManager.remove(next);
				theEntity.getParamsCompositeStringUnique().remove(next);
			}
			boolean haveNewParams = false;
			for (ResourceIndexedCompositeStringUnique next : myDaoSearchParamSynchronizer.subtract(theParams.myCompositeStringUniques, existingParams.myCompositeStringUniques)) {
				if (myDaoConfig.isUniqueIndexesCheckedBeforeSave()) {
					ResourceIndexedCompositeStringUnique existing = myResourceIndexedCompositeStringUniqueDao.findByQueryString(next.getIndexString());
					if (existing != null) {
						String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "uniqueIndexConflictFailure", theEntity.getResourceType(), next.getIndexString(), existing.getResource().getIdDt().toUnqualifiedVersionless().getValue());
						throw new PreconditionFailedException(msg);
					}
				}
				ourLog.debug("Persisting unique index: {}", next);
				myEntityManager.persist(next);
				haveNewParams = true;
			}
			if (theParams.myCompositeStringUniques.size() > 0 || haveNewParams) {
				theEntity.setParamsCompositeStringUniquePresent(true);
			} else {
				theEntity.setParamsCompositeStringUniquePresent(false);
			}
		}
	}
}
