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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class DaoSearchParamSynchronizer {
	private static final Logger ourLog = LoggerFactory.getLogger(DaoSearchParamSynchronizer.class);

	@Autowired
	private StorageSettings myStorageSettings;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	public AddRemoveCount synchronizeSearchParamsToDatabase(
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			ResourceIndexedSearchParams existingParams) {
		AddRemoveCount retVal = new AddRemoveCount();

		synchronize(theEntity, retVal, theParams.myStringParams, existingParams.myStringParams);
		synchronize(theEntity, retVal, theParams.myTokenParams, existingParams.myTokenParams);
		synchronize(theEntity, retVal, theParams.myNumberParams, existingParams.myNumberParams);
		synchronize(theEntity, retVal, theParams.myQuantityParams, existingParams.myQuantityParams);
		synchronize(theEntity, retVal, theParams.myQuantityNormalizedParams, existingParams.myQuantityNormalizedParams);
		synchronize(theEntity, retVal, theParams.myDateParams, existingParams.myDateParams);
		synchronize(theEntity, retVal, theParams.myUriParams, existingParams.myUriParams);
		synchronize(theEntity, retVal, theParams.myCoordsParams, existingParams.myCoordsParams);
		synchronize(theEntity, retVal, theParams.myLinks, existingParams.myLinks);
		synchronize(theEntity, retVal, theParams.myComboTokenNonUnique, existingParams.myComboTokenNonUnique);

		// make sure links are indexed
		theEntity.setResourceLinks(theParams.myLinks);

		return retVal;
	}

	@VisibleForTesting
	public void setStorageSettings(StorageSettings theStorageSettings) {
		this.myStorageSettings = theStorageSettings;
	}

	@VisibleForTesting
	public void setEntityManager(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	private <T extends BaseResourceIndex> void synchronize(
			ResourceTable theEntity,
			AddRemoveCount theAddRemoveCount,
			Collection<T> theNewParams,
			Collection<T> theExistingParams) {
		Collection<T> newParams = theNewParams;
		for (T next : newParams) {
			next.setPartitionId(theEntity.getPartitionId());
			next.calculateHashes();
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
			myEntityManager.merge(next);
		}

		// TODO:  are there any unintended consequences to fixing this bug?
		theAddRemoveCount.addToAddCount(paramsToAdd.size());
		theAddRemoveCount.addToRemoveCount(paramsToRemove.size());
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
