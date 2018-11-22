package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class DatabaseSearchParamSynchronizer {
	@Autowired
	private DaoConfig myDaoConfig;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

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
