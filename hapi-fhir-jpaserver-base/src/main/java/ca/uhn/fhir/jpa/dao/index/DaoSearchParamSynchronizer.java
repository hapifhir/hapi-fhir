/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboStringUniqueDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.IndexedSearchParamIdentity;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.sp.SearchParamIdentityCacheSvcImpl;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class DaoSearchParamSynchronizer {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IResourceIndexedComboStringUniqueDao myResourceIndexedCompositeStringUniqueDao;

	@Autowired
	private ISearchParamIdentityCacheSvc mySearchParamIdentityCacheSvc;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IHapiTransactionService myTransactionService;

	/**
	 * Custom secondary-index writers contributed by extensions (e.g. Smile CDR compressed token
	 * indexing). Empty in vanilla HAPI. Injected via setter so it defaults to empty when no beans exist.
	 */
	private List<ICustomIndexSynchronizer> myCustomIndexSynchronizers = Collections.emptyList();

	/**
	 * Policies that may suppress writing a built-in index. Empty in vanilla HAPI (so all built-in
	 * indexes are always written). A built-in index is written only if every policy allows it.
	 */
	private List<IBuiltInIndexWritePolicy> myBuiltInIndexWritePolicies = Collections.emptyList();

	private UniqueIndexPreExistenceChecker myUniqueIndexPreExistenceChecker;

	@PostConstruct
	void start() {
		myUniqueIndexPreExistenceChecker = new UniqueIndexPreExistenceChecker(
				myFhirContext, myResourceIndexedCompositeStringUniqueDao, myTransactionService);
	}

	public AddRemoveCount synchronizeSearchParamsToDatabase(
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			ResourceIndexedSearchParams existingParams,
			boolean theResourceIsBeingCreated) {
		AddRemoveCount retVal = new AddRemoveCount();

		// Each scalar built-in index may be suppressed by a write policy — e.g. Smile CDR's
		// compressed-only phase, where a custom synchronizer has taken over token indexing entirely.
		synchronizeIfAllowed(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				RestSearchParameterTypeEnum.STRING,
				theParams.myStringParams,
				existingParams.myStringParams);
		synchronizeIfAllowed(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				RestSearchParameterTypeEnum.TOKEN,
				theParams.myTokenParams,
				existingParams.myTokenParams);
		synchronizeIfAllowed(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				RestSearchParameterTypeEnum.NUMBER,
				theParams.myNumberParams,
				existingParams.myNumberParams);
		synchronizeIfAllowed(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				RestSearchParameterTypeEnum.QUANTITY,
				theParams.myQuantityParams,
				existingParams.myQuantityParams);
		synchronizeIfAllowed(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				RestSearchParameterTypeEnum.QUANTITY,
				theParams.myQuantityNormalizedParams,
				existingParams.myQuantityNormalizedParams);
		synchronizeIfAllowed(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				RestSearchParameterTypeEnum.DATE,
				theParams.myDateParams,
				existingParams.myDateParams);
		synchronizeIfAllowed(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				RestSearchParameterTypeEnum.URI,
				theParams.myUriParams,
				existingParams.myUriParams);
		synchronizeIfAllowed(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				RestSearchParameterTypeEnum.SPECIAL,
				theParams.myCoordsParams,
				existingParams.myCoordsParams);
		synchronize(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				theParams.myLinks,
				existingParams.myLinks,
				null);
		synchronize(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				theParams.myComboTokenNonUnique,
				existingParams.myComboTokenNonUnique,
				null);
		synchronize(
				theRequestDetails,
				theTransactionDetails,
				theEntity,
				retVal,
				theParams.myComboStringUniques,
				existingParams.myComboStringUniques,
				myUniqueIndexPreExistenceChecker);

		// make sure links are indexed
		theEntity.setResourceLinks(theParams.myLinks);

		// Let any registered custom secondary-index writers reconcile their own tables for this
		// resource. No-op in vanilla HAPI (no synchronizers registered).
		for (ICustomIndexSynchronizer synchronizer : myCustomIndexSynchronizers) {
			AddRemoveCount customDelta = synchronizer.synchronize(
					theRequestDetails, theTransactionDetails, theParams, theEntity, theResourceIsBeingCreated);
			retVal.addToAddCount(customDelta.getAddCount());
			retVal.addToRemoveCount(customDelta.getRemoveCount());
		}

		return retVal;
	}

	@VisibleForTesting
	public void setEntityManager(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	@VisibleForTesting
	public void setSearchParamIdentityCacheSvc(ISearchParamIdentityCacheSvc theSearchParamIdentityCacheSvc) {
		mySearchParamIdentityCacheSvc = theSearchParamIdentityCacheSvc;
	}

	@VisibleForTesting
	public void setStorageSettings(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	@Autowired(required = false)
	public void setCustomIndexSynchronizers(List<ICustomIndexSynchronizer> theCustomIndexSynchronizers) {
		myCustomIndexSynchronizers = theCustomIndexSynchronizers;
	}

	@Autowired(required = false)
	public void setBuiltInIndexWritePolicies(List<IBuiltInIndexWritePolicy> theBuiltInIndexWritePolicies) {
		myBuiltInIndexWritePolicies = theBuiltInIndexWritePolicies;
	}

	/**
	 * Synchronizes one scalar built-in index collection, unless a registered
	 * {@link IBuiltInIndexWritePolicy} vetoes that index type for this resource. When vetoed, the
	 * collection is skipped entirely: no new rows are written and pre-existing rows are left untouched.
	 */
	private <T extends BaseResourceIndex> void synchronizeIfAllowed(
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			ResourceTable theEntity,
			AddRemoveCount theAddRemoveCount,
			RestSearchParameterTypeEnum theParamType,
			Collection<T> theNewParams,
			Collection<T> theExistingParams) {
		if (shouldWriteBuiltInIndex(theEntity, theParamType)) {
			synchronize(
					theRequestDetails,
					theTransactionDetails,
					theEntity,
					theAddRemoveCount,
					theNewParams,
					theExistingParams,
					null);
		}
	}

	/**
	 * Returns {@code true} unless a registered {@link IBuiltInIndexWritePolicy} vetoes writing the
	 * built-in index of the given type for this resource. Any policy may veto; all must allow.
	 */
	private boolean shouldWriteBuiltInIndex(ResourceTable theEntity, RestSearchParameterTypeEnum theParamType) {
		if (myBuiltInIndexWritePolicies.isEmpty()) {
			return true;
		}
		String resourceType = theEntity.getResourceType();
		for (IBuiltInIndexWritePolicy policy : myBuiltInIndexWritePolicies) {
			if (!policy.shouldWriteBuiltInIndex(resourceType, theParamType)) {
				return false;
			}
		}
		return true;
	}

	private <T extends BaseResourceIndex> void synchronize(
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails,
			ResourceTable theEntity,
			AddRemoveCount theAddRemoveCount,
			Collection<T> theNewParams,
			Collection<T> theExistingParams,
			@Nullable ISearchParamPreSynchronizeHook<T> theAddParamPreSaveHook) {
		Collection<T> newParams = theNewParams;
		for (T next : newParams) {
			next.setResourceId(theEntity.getId().getId());
			next.setPartitionId(theEntity.getPartitionId());
			next.calculateHashes();
			// set the resource table for some search parameters because otherwise there's a risk the search
			// parameter could be inserted into the DB before the resource it depends on.
			if (next instanceof BaseResourceIndexedSearchParam searchParam) {
				searchParam.setResource(theEntity);
			}
			if (next instanceof ResourceLink link && link.getTargetResourcePid() != null) {
				JpaPid targetId = JpaPid.fromId(link.getTargetResourcePid(), link.getTargetResourcePartitionId());
				link.setTargetResourceTable(myEntityManager.getReference(ResourceTable.class, targetId));
			}
		}

		/*
		 * It's technically possible that the existing index collection
		 * contains duplicates. Duplicates don't actually cause any
		 * issues for searching since we always deduplicate the PIDs we
		 * get back from the search, but they are wasteful. We don't
		 * enforce uniqueness in the DB for the index tables for
		 * performance reasons (no sense adding a constraint that slows
		 * down writes when dupes don't actually hurt anything other than
		 * a bit of wasted space).
		 *
		 * So we check if there are any dupes, and if we find any we
		 * remove them.
		 */
		Set<T> existingParamsAsSet = new HashSet<>(theExistingParams.size());
		for (Iterator<T> iterator = theExistingParams.iterator(); iterator.hasNext(); ) {
			T next = iterator.next();
			next.setPlaceholderHashesIfMissing();
			if (!existingParamsAsSet.add(next)) {
				iterator.remove();
				myEntityManager.remove(next);
			}
		}

		/*
		 * HashCodes may have changed as a result of setting the partition ID, so
		 * create a new set that will reflect the new hashcodes
		 */
		newParams = new HashSet<>(newParams);

		List<T> paramsToRemove = subtract(theExistingParams, newParams);
		List<T> paramsToAdd = subtract(newParams, theExistingParams);

		if (theAddParamPreSaveHook != null && (!paramsToAdd.isEmpty() || !paramsToRemove.isEmpty())) {
			theAddParamPreSaveHook.preSave(theRequest, theTransactionDetails, paramsToRemove, paramsToAdd);
		}

		tryToReuseIndexEntities(paramsToRemove, paramsToAdd);
		updateExistingParamsIfRequired(theExistingParams, paramsToAdd, newParams, paramsToRemove);

		for (T next : paramsToRemove) {
			if (!myEntityManager.contains(next)) {
				// If a resource is created and deleted in the same transaction, we can end up
				// in a state where we're deleting entities that don't actually exist. Hibernate
				// 6 is stricter about this, so we skip here.
				continue;
			}
			myEntityManager.remove(next);
		}

		for (T next : paramsToAdd) {
			findOrCreateSearchParamIdentity(next);
			if (next.getId() == null) {
				myEntityManager.persist(next);
			} else {
				myEntityManager.merge(next);
			}
		}

		// TODO:  are there any unintended consequences to fixing this bug?
		theAddRemoveCount.addToAddCount(paramsToAdd.size());
		theAddRemoveCount.addToRemoveCount(paramsToRemove.size());

		// Replace the existing "new set" with the set of params we should be adding.
		// We're going to add them back into the entity just in case it gets updated
		// a second time within the same transaction
		theNewParams.clear();
		theNewParams.addAll(theExistingParams);
		theNewParams.addAll(paramsToAdd);
		theNewParams.removeAll(paramsToRemove);
	}

	/**
	 * Checks whether the Indexed Search Parameter hash identity exists in the cache.
	 * If the identity is missing, a new {@link IndexedSearchParamIdentity} will be
	 * created asynchronously in a separate thread.
	 *
	 * <p>For details, see:
	 * {@link SearchParamIdentityCacheSvcImpl#findOrCreateSearchParamIdentity(Long, String, String)}</p>
	 */
	private <T extends BaseResourceIndex> void findOrCreateSearchParamIdentity(T theNewParam) {
		if (theNewParam instanceof BaseResourceIndexedSearchParam) {
			BaseResourceIndexedSearchParam indexedSearchParam = ((BaseResourceIndexedSearchParam) theNewParam);
			mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(
					indexedSearchParam.getHashIdentity(),
					indexedSearchParam.getResourceType(),
					indexedSearchParam.getParamName());
		}
	}

	/**
	 * <p>
	 * This method performs an update of Search Parameter's fields in the case of
	 * <code>$reindex</code> or update operation by:
	 * 1. Marking existing entities for updating to apply index storage optimization,
	 * if it is enabled (disabled by default).
	 * 2. Recovering <code>SP_NAME</code>, <code>RES_TYPE</code> values of Search Parameter's fields
	 * for existing entities in case if index storage optimization is disabled (but was enabled previously).
	 * </p>
	 * For details, see: {@link StorageSettings#isIndexStorageOptimized()}
	 */
	private <T extends BaseResourceIndex> void updateExistingParamsIfRequired(
			Collection<T> theExistingParams,
			List<T> theParamsToAdd,
			Collection<T> theNewParams,
			List<T> theParamsToRemove) {

		theExistingParams.stream()
				.filter(BaseResourceIndexedSearchParam.class::isInstance)
				.map(BaseResourceIndexedSearchParam.class::cast)
				.filter(this::isSearchParameterUpdateRequired)
				.filter(sp -> !theParamsToAdd.contains(sp))
				.filter(sp -> !theParamsToRemove.contains(sp))
				.forEach(sp -> {
					// force hibernate to update Search Parameter entity by resetting SP_UPDATED value
					sp.setUpdated(new Date());
					recoverExistingSearchParameterIfRequired(sp, theNewParams);
					theParamsToAdd.add((T) sp);
				});
	}

	/**
	 * Search parameters should be updated after changing IndexStorageOptimized setting.
	 * If IndexStorageOptimized is disabled (and was enabled previously), this method copies paramName
	 * and Resource Type from extracted to existing search parameter.
	 */
	private <T extends BaseResourceIndex> void recoverExistingSearchParameterIfRequired(
			BaseResourceIndexedSearchParam theSearchParamToRecover, Collection<T> theNewParams) {
		if (!myStorageSettings.isIndexStorageOptimized()) {
			theNewParams.stream()
					.filter(BaseResourceIndexedSearchParam.class::isInstance)
					.map(BaseResourceIndexedSearchParam.class::cast)
					.filter(paramToAdd -> paramToAdd.equals(theSearchParamToRecover))
					.findFirst()
					.ifPresent(newParam -> {
						theSearchParamToRecover.restoreParamName(newParam.getParamName());
						theSearchParamToRecover.setResourceType(newParam.getResourceType());
					});
		}
	}

	private boolean isSearchParameterUpdateRequired(BaseResourceIndexedSearchParam theSearchParameter) {
		return (myStorageSettings.isIndexStorageOptimized() && !theSearchParameter.isIndexStorageOptimized())
				|| (!myStorageSettings.isIndexStorageOptimized() && theSearchParameter.isIndexStorageOptimized());
	}

	/**
	 * The logic here is that often times when we update a resource we are dropping
	 * one index row and adding another. This method tries to reuse rows that would otherwise
	 * have been deleted by updating them with the contents of rows that would have
	 * otherwise been added. In other words, we're trying to replace
	 * "one delete + one insert" with "one update"
	 *
	 * @param theIndexesToRemove The rows that would be removed
	 * @param theIndexesToAdd    The rows that would be added
	 */
	private <T extends BaseResourceIndex> void tryToReuseIndexEntities(
			List<T> theIndexesToRemove, List<T> theIndexesToAdd) {
		for (int addIndex = 0; addIndex < theIndexesToAdd.size(); addIndex++) {

			// If there are no more rows to remove, there's nothing we can reuse
			if (theIndexesToRemove.isEmpty()) {
				break;
			}

			T targetEntity = theIndexesToAdd.get(addIndex);
			if (targetEntity.getId() != null) {
				continue;
			}

			// Take a row we were going to remove, and repurpose its ID
			T entityToReuse = theIndexesToRemove.remove(theIndexesToRemove.size() - 1);
			entityToReuse.copyMutableValuesFrom(targetEntity);
			theIndexesToAdd.set(addIndex, entityToReuse);
		}
	}

	public static <T> List<T> subtract(Collection<T> theSubtractFrom, Collection<T> theToSubtract) {
		assert theSubtractFrom != theToSubtract || (theSubtractFrom.isEmpty());

		if (theSubtractFrom.isEmpty()) {
			return new ArrayList<>();
		}

		ArrayList<T> retVal = new ArrayList<>();
		for (T next : theSubtractFrom) {
			if (!theToSubtract.contains(next)) {
				retVal.add(next);
			}
		}
		return retVal;
	}
}
