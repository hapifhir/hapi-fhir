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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.ISearchParamExtractor;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

// FIXME KHS Split off methods that are required by inmemory matcher
@Service
@Lazy
public class SearchParamExtractorService {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamExtractorService.class);

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;


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

	public void synchronizeSearchParamsToDatabase(ResourceIndexedSearchParams theParams, ResourceTable theEntity, ResourceIndexedSearchParams existingParams) {
		theParams.calculateHashes(theParams.stringParams);
		for (ResourceIndexedSearchParamString next : synchronizeSearchParamsToDatabase(existingParams.stringParams, theParams.stringParams)) {
			next.setDaoConfig(myDaoConfig);
			myEntityManager.remove(next);
			theEntity.getParamsString().remove(next);
		}
		for (ResourceIndexedSearchParamString next : synchronizeSearchParamsToDatabase(theParams.stringParams, existingParams.stringParams)) {
			myEntityManager.persist(next);
		}

		theParams.calculateHashes(theParams.tokenParams);
		for (ResourceIndexedSearchParamToken next : synchronizeSearchParamsToDatabase(existingParams.tokenParams, theParams.tokenParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsToken().remove(next);
		}
		for (ResourceIndexedSearchParamToken next : synchronizeSearchParamsToDatabase(theParams.tokenParams, existingParams.tokenParams)) {
			myEntityManager.persist(next);
		}

		theParams.calculateHashes(theParams.numberParams);
		for (ResourceIndexedSearchParamNumber next : synchronizeSearchParamsToDatabase(existingParams.numberParams, theParams.numberParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsNumber().remove(next);
		}
		for (ResourceIndexedSearchParamNumber next : synchronizeSearchParamsToDatabase(theParams.numberParams, existingParams.numberParams)) {
			myEntityManager.persist(next);
		}

		theParams.calculateHashes(theParams.quantityParams);
		for (ResourceIndexedSearchParamQuantity next : synchronizeSearchParamsToDatabase(existingParams.quantityParams, theParams.quantityParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsQuantity().remove(next);
		}
		for (ResourceIndexedSearchParamQuantity next : synchronizeSearchParamsToDatabase(theParams.quantityParams, existingParams.quantityParams)) {
			myEntityManager.persist(next);
		}

		// Store date SP's
		theParams.calculateHashes(theParams.dateParams);
		for (ResourceIndexedSearchParamDate next : synchronizeSearchParamsToDatabase(existingParams.dateParams, theParams.dateParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsDate().remove(next);
		}
		for (ResourceIndexedSearchParamDate next : synchronizeSearchParamsToDatabase(theParams.dateParams, existingParams.dateParams)) {
			myEntityManager.persist(next);
		}

		// Store URI SP's
		theParams.calculateHashes(theParams.uriParams);
		for (ResourceIndexedSearchParamUri next : synchronizeSearchParamsToDatabase(existingParams.uriParams, theParams.uriParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsUri().remove(next);
		}
		for (ResourceIndexedSearchParamUri next : synchronizeSearchParamsToDatabase(theParams.uriParams, existingParams.uriParams)) {
			myEntityManager.persist(next);
		}

		// Store Coords SP's
		theParams.calculateHashes(theParams.coordsParams);
		for (ResourceIndexedSearchParamCoords next : synchronizeSearchParamsToDatabase(existingParams.coordsParams, theParams.coordsParams)) {
			myEntityManager.remove(next);
			theEntity.getParamsCoords().remove(next);
		}
		for (ResourceIndexedSearchParamCoords next : synchronizeSearchParamsToDatabase(theParams.coordsParams, existingParams.coordsParams)) {
			myEntityManager.persist(next);
		}

		// Store resource links
		for (ResourceLink next : synchronizeSearchParamsToDatabase(existingParams.links, theParams.links)) {
			myEntityManager.remove(next);
			theEntity.getResourceLinks().remove(next);
		}
		for (ResourceLink next : synchronizeSearchParamsToDatabase(theParams.links, existingParams.links)) {
			myEntityManager.persist(next);
		}

		// make sure links are indexed
		theEntity.setResourceLinks(theParams.links);
	}

	public <T> Collection<T> synchronizeSearchParamsToDatabase(Collection<T> theInput, Collection<T> theToRemove) {
		assert theInput != theToRemove;

		if (theInput.isEmpty()) {
			return theInput;
		}

		ArrayList<T> retVal = new ArrayList<>(theInput);
		retVal.removeAll(theToRemove);
		return retVal;
	}
}

