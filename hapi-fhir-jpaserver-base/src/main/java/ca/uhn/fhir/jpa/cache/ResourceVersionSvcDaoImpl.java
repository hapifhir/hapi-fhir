package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This service builds a map of resource ids to versions based on a SearchParameterMap.
 * It is used by the in-memory resource-version cache to detect when resource versions have been changed by remote processes.
 */
@Service
public class ResourceVersionSvcDaoImpl implements IResourceVersionSvc {
	private static final Logger myLogger = LoggerFactory.getLogger(ResourceVersionMap.class);

	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IResourceTableDao myResourceTableDao;

	@Nonnull
	public ResourceVersionMap getVersionMap(String theResourceName, SearchParameterMap theSearchParamMap) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceName);

		List<Long> matchingIds = dao.searchForIds(theSearchParamMap, null).stream()
			.map(ResourcePersistentId::getIdAsLong)
			.collect(Collectors.toList());

		return ResourceVersionMap.fromResourceTableEntities(myResourceTableDao.findAllById(matchingIds));
	}
}
