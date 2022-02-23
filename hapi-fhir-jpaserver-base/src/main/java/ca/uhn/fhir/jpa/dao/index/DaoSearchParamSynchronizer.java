package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class DaoSearchParamSynchronizer {
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	public AddRemoveCount synchronizeSearchParamsToDatabase(ResourceIndexedSearchParams theParams, ResourceTable theEntity, ResourceIndexedSearchParams existingParams) {
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
	public void setEntityManager(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	private <T extends BaseResourceIndex> void synchronize(ResourceTable theEntity, AddRemoveCount theAddRemoveCount, Collection<T> theNewParams, Collection<T> theExistingParams) {
		Collection<T> newParams = theNewParams;
		for (T next : newParams) {
			next.setPartitionId(theEntity.getPartitionId());
			next.calculateHashes();
		}

		/*
		 * HashCodes may have changed as a result of setting the partition ID, so
		 * create a new set that will reflect the new hashcodes
		 */
		newParams = new HashSet<>(newParams);

		List<T> paramsToRemove = subtract(theExistingParams, newParams);
		List<T> paramsToAdd = subtract(newParams, theExistingParams);
		tryToReuseIndexEntities(paramsToRemove, paramsToAdd);

		for (T next : paramsToRemove) {
			myEntityManager.remove(next);
			theEntity.getParamsQuantity().remove(next);
			theEntity.getParamsQuantityNormalized().remove(next);
		}
		for (T next : paramsToAdd) {
			myEntityManager.merge(next);
		}

		theAddRemoveCount.addToAddCount(paramsToRemove.size());
		theAddRemoveCount.addToRemoveCount(paramsToRemove.size());
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
	private <T extends BaseResourceIndex> void tryToReuseIndexEntities(List<T> theIndexesToRemove, List<T> theIndexesToAdd) {
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


	<T> List<T> subtract(Collection<T> theSubtractFrom, Collection<T> theToSubtract) {
		assert theSubtractFrom != theToSubtract;

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
