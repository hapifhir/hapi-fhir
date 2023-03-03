package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.MatchResourceUrlService;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboStringUniqueDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

@Service
@Lazy
public class SearchParamWithInlineReferencesExtractor extends BaseSearchParamWithInlineReferencesExtractor<JpaPid> implements ISearchParamWithInlineReferencesExtractor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamWithInlineReferencesExtractor.class);
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private MatchResourceUrlService<JpaPid> myMatchResourceUrlService;
	@Autowired
	private JpaStorageSettings myStorageSettings;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private DaoResourceLinkResolver<JpaPid> myDaoResourceLinkResolver;
	@Autowired
	private DaoSearchParamSynchronizer myDaoSearchParamSynchronizer;
	@Autowired
	private IResourceIndexedComboStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@VisibleForTesting
	public void setPartitionSettings(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	@VisibleForTesting
	public void setSearchParamExtractorService(SearchParamExtractorService theSearchParamExtractorService) {
		mySearchParamExtractorService = theSearchParamExtractorService;
	}

	@VisibleForTesting
	public void setSearchParamRegistry(ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
	}

	public void populateFromResource(RequestPartitionId theRequestPartitionId, ResourceIndexedSearchParams theParams, TransactionDetails theTransactionDetails, ResourceTable theEntity, IBaseResource theResource, ResourceIndexedSearchParams theExistingParams, RequestDetails theRequest, boolean theFailOnInvalidReference) {
		ExtractInlineReferenceParams theExtractParams = new ExtractInlineReferenceParams(theResource, theTransactionDetails, theRequest);
		theExtractParams.setFailOnInvalidReferences(theFailOnInvalidReference);
		// Perform inline match URL substitution
		extractInlineReferences(theRequest, theResource, theTransactionDetails);

		mySearchParamExtractorService.extractFromResource(theRequestPartitionId, theRequest, theParams, theExistingParams, theEntity, theResource, theTransactionDetails, theFailOnInvalidReference);

		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theEntity.getResourceType());
		if (myStorageSettings.getIndexMissingFields() == JpaStorageSettings.IndexEnabledEnum.ENABLED) {
			theParams.findMissingSearchParams(myPartitionSettings, myStorageSettings, theEntity, activeSearchParams);
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

		extractComboParameters(theEntity, theParams);
	}

	private void extractComboParameters(ResourceTable theEntity, ResourceIndexedSearchParams theParams) {
		mySearchParamExtractorService.extractSearchParamComboUnique(theEntity, theParams);
		mySearchParamExtractorService.extractSearchParamComboNonUnique(theEntity, theParams);
	}

	@Nullable
	private Collection<? extends BaseResourceIndexedSearchParam> findParameterIndexes(ResourceIndexedSearchParams theParams, RuntimeSearchParam nextCompositeOf) {
		Collection<? extends BaseResourceIndexedSearchParam> paramsListForCompositePart = null;
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
			case QUANTITY:
				paramsListForCompositePart = theParams.myQuantityParams;
				break;
			case URI:
				paramsListForCompositePart = theParams.myUriParams;
				break;
			case REFERENCE:
			case SPECIAL:
			case COMPOSITE:
			case HAS:
				break;
		}
		if (paramsListForCompositePart != null) {
			paramsListForCompositePart = paramsListForCompositePart
				.stream()
				.filter(t -> t.getParamName().equals(nextCompositeOf.getName()))
				.collect(Collectors.toList());
		}
		return paramsListForCompositePart;
	}

	@VisibleForTesting
	public void setStorageSettings(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	@VisibleForTesting
	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	@VisibleForTesting
	public void setDaoSearchParamSynchronizer(DaoSearchParamSynchronizer theDaoSearchParamSynchronizer) {
		myDaoSearchParamSynchronizer = theDaoSearchParamSynchronizer;
	}

	public void storeUniqueComboParameters(ResourceIndexedSearchParams theParams, ResourceTable theEntity, ResourceIndexedSearchParams theExistingParams) {

		/*
		 * String Uniques
		 */
		if (myStorageSettings.isUniqueIndexesEnabled()) {
			for (ResourceIndexedComboStringUnique next : myDaoSearchParamSynchronizer.subtract(theExistingParams.myComboStringUniques, theParams.myComboStringUniques)) {
				ourLog.debug("Removing unique index: {}", next);
				myEntityManager.remove(next);
				theEntity.getParamsComboStringUnique().remove(next);
			}
			boolean haveNewStringUniqueParams = false;
			for (ResourceIndexedComboStringUnique next : myDaoSearchParamSynchronizer.subtract(theParams.myComboStringUniques, theExistingParams.myComboStringUniques)) {
				if (myStorageSettings.isUniqueIndexesCheckedBeforeSave()) {
					ResourceIndexedComboStringUnique existing = myResourceIndexedCompositeStringUniqueDao.findByQueryString(next.getIndexString());
					if (existing != null) {

						String searchParameterId = "(unknown)";
						if (next.getSearchParameterId() != null) {
							searchParameterId = next.getSearchParameterId().toUnqualifiedVersionless().getValue();
						}

						String msg = myContext.getLocalizer().getMessage(BaseHapiFhirDao.class, "uniqueIndexConflictFailure", theEntity.getResourceType(), next.getIndexString(), existing.getResource().getIdDt().toUnqualifiedVersionless().getValue(), searchParameterId);
						throw new PreconditionFailedException(Msg.code(1093) + msg);
					}
				}
				ourLog.debug("Persisting unique index: {}", next);
				myEntityManager.persist(next);
				haveNewStringUniqueParams = true;
			}
			if (theParams.myComboStringUniques.size() > 0 || haveNewStringUniqueParams) {
				theEntity.setParamsComboStringUniquePresent(true);
			} else {
				theEntity.setParamsComboStringUniquePresent(false);
			}
		}
	}
}
