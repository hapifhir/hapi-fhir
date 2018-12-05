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

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DatabaseSearchParamSynchronizer {
	@Autowired
	private DaoConfig myDaoConfig;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	public void synchronizeSearchParamsToDatabase(ResourceIndexedSearchParams theParams, ResourceTable theEntity, ResourceIndexedSearchParams existingParams) {

		/*
		 * Strings
		 */
		theParams.calculateHashes(theParams.stringParams);
		List<ResourceIndexedSearchParamString> stringsToRemove = subtract(existingParams.stringParams, theParams.stringParams);
		List<ResourceIndexedSearchParamString> stringsToAdd = subtract(theParams.stringParams, existingParams.stringParams);
		tryToReuseIndexEntities(stringsToRemove, stringsToAdd);
		for (ResourceIndexedSearchParamString next : stringsToRemove) {
			next.setModelConfig(myDaoConfig.getModelConfig());
			myEntityManager.remove(next);
			theEntity.getParamsString().remove(next);
		}
		for (ResourceIndexedSearchParamString next : stringsToAdd) {
			myEntityManager.persist(next);
		}

		/*
		 * Tokens
		 */
		theParams.calculateHashes(theParams.tokenParams);
		List<ResourceIndexedSearchParamToken> tokensToRemove = subtract(existingParams.tokenParams, theParams.tokenParams);
		List<ResourceIndexedSearchParamToken> tokensToAdd = subtract(theParams.tokenParams, existingParams.tokenParams);
		tryToReuseIndexEntities(tokensToRemove, tokensToAdd);
		for (ResourceIndexedSearchParamToken next : tokensToRemove) {
			myEntityManager.remove(next);
			theEntity.getParamsToken().remove(next);
		}
		for (ResourceIndexedSearchParamToken next : tokensToAdd) {
			myEntityManager.persist(next);
		}

		/*
		 * Numbers
		 */
		theParams.calculateHashes(theParams.numberParams);
		List<ResourceIndexedSearchParamNumber> numbersToRemove = subtract(existingParams.numberParams, theParams.numberParams);
		List<ResourceIndexedSearchParamNumber> numbersToAdd = subtract(theParams.numberParams, existingParams.numberParams);
		tryToReuseIndexEntities(numbersToRemove, numbersToAdd);
		for (ResourceIndexedSearchParamNumber next : numbersToRemove) {
			myEntityManager.remove(next);
			theEntity.getParamsNumber().remove(next);
		}
		for (ResourceIndexedSearchParamNumber next : numbersToAdd) {
			myEntityManager.persist(next);
		}

		/*
		 * Quantities
		 */
		theParams.calculateHashes(theParams.quantityParams);
		List<ResourceIndexedSearchParamQuantity> quantitiesToRemove = subtract(existingParams.quantityParams, theParams.quantityParams);
		List<ResourceIndexedSearchParamQuantity> quantitiesToAdd = subtract(theParams.quantityParams, existingParams.quantityParams);
		tryToReuseIndexEntities(quantitiesToRemove, quantitiesToAdd);
		for (ResourceIndexedSearchParamQuantity next : quantitiesToRemove) {
			myEntityManager.remove(next);
			theEntity.getParamsQuantity().remove(next);
		}
		for (ResourceIndexedSearchParamQuantity next : quantitiesToAdd) {
			myEntityManager.persist(next);
		}

		/*
		 * Dates
		 */
		theParams.calculateHashes(theParams.dateParams);
		List<ResourceIndexedSearchParamDate> datesToRemove = subtract(existingParams.dateParams, theParams.dateParams);
		List<ResourceIndexedSearchParamDate> datesToAdd = subtract(theParams.dateParams, existingParams.dateParams);
		tryToReuseIndexEntities(datesToRemove, datesToAdd);
		for (ResourceIndexedSearchParamDate next : datesToRemove) {
			myEntityManager.remove(next);
			theEntity.getParamsDate().remove(next);
		}
		for (ResourceIndexedSearchParamDate next : datesToAdd) {
			myEntityManager.persist(next);
		}

		/*
		 * URIs
		 */
		theParams.calculateHashes(theParams.uriParams);
		List<ResourceIndexedSearchParamUri> urisToRemove = subtract(existingParams.uriParams, theParams.uriParams);
		List<ResourceIndexedSearchParamUri> urisToAdd = subtract(theParams.uriParams, existingParams.uriParams);
		tryToReuseIndexEntities(urisToRemove, urisToAdd);
		for (ResourceIndexedSearchParamUri next : urisToRemove) {
			myEntityManager.remove(next);
			theEntity.getParamsUri().remove(next);
		}
		for (ResourceIndexedSearchParamUri next : urisToAdd) {
			myEntityManager.persist(next);
		}

		/*
		 * Coords
		 */
		theParams.calculateHashes(theParams.coordsParams);
		List<ResourceIndexedSearchParamCoords> coordsToRemove = subtract(existingParams.coordsParams, theParams.coordsParams);
		List<ResourceIndexedSearchParamCoords> coordsToAdd = subtract(theParams.coordsParams, existingParams.coordsParams);
		tryToReuseIndexEntities(coordsToRemove, coordsToAdd);
		for (ResourceIndexedSearchParamCoords next : coordsToRemove) {
			myEntityManager.remove(next);
			theEntity.getParamsCoords().remove(next);
		}
		for (ResourceIndexedSearchParamCoords next : coordsToAdd) {
			myEntityManager.persist(next);
		}

		/*
		 * Resource links
		 */
		List<ResourceLink> resourceLinksToRemove = subtract(existingParams.links, theParams.links);
		List<ResourceLink> resourceLinksToAdd = subtract(theParams.links, existingParams.links);
		tryToReuseIndexEntities(resourceLinksToRemove, resourceLinksToAdd);
		for (ResourceLink next : resourceLinksToRemove) {
			myEntityManager.remove(next);
			theEntity.getResourceLinks().remove(next);
		}
		for (ResourceLink next : resourceLinksToAdd) {
			myEntityManager.persist(next);
		}

		// make sure links are indexed
		theEntity.setResourceLinks(theParams.links);
	}

	/**
	 * The logic here is that often times when we update a resource we are dropping
	 * one index row and adding another. This method tries to reuse rows that would otherwise
	 * have been deleted by updating them with the contents of rows that would have
	 * otherwise been added. In other words, we're trying to replace
	 * "one delete + one insert" with "one update"
	 *
	 * @param theIndexesToRemove The rows that would be removed
	 * @param theIndexesToAdd The rows that would be added
	 */
	private <T extends BaseResourceIndex> void tryToReuseIndexEntities(List<T> theIndexesToRemove, List<T> theIndexesToAdd) {
		for (int addIndex = 0; addIndex < theIndexesToAdd.size(); addIndex++) {
			if (theIndexesToRemove.isEmpty()) {
				break;
			}

			T entityToReuse = theIndexesToRemove.remove(theIndexesToRemove.size() - 1);
			entityToReuse.populateFrom(theIndexesToAdd.get(addIndex));
			theIndexesToAdd.set(addIndex, entityToReuse);
		}
	}

	<T> List<T> subtract(Collection<T> theSubtractFrom, Collection<T> theToSubtract) {
		assert theSubtractFrom != theToSubtract;

		if (theSubtractFrom.isEmpty()) {
			return new ArrayList<>();
		}

		ArrayList<T> retVal = new ArrayList<>(theSubtractFrom);
		retVal.removeAll(theToSubtract);
		return retVal;
	}
}
