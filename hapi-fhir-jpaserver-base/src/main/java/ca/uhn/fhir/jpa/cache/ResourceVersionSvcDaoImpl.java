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

import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory cache of resource versions used to detect when resources have changed by remote processes
 */
@Service
public class ResourceVersionSvcDaoImpl implements IResourceVersionSvc {
	private static final Logger myLogger = LoggerFactory.getLogger(ResourceVersionMap.class);

	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IResourceTableDao myResourceTableDao;

	public ResourceVersionMap getVersionMap(String theResourceName, SearchParameterMap theSearchParamMap) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceName);
		myLogger.debug("About to search for '" + theResourceName + "' objects using params '" + theSearchParamMap + "'.");

		List<Long> matchingIds = dao.searchForIds(theSearchParamMap, null).stream()
			.map(ResourcePersistentId::getIdAsLong)
			.collect(Collectors.toList());

		myLogger.debug("Found " + matchingIds.size() + " '" + theResourceName + "' objects using params '" + theSearchParamMap + "'.");

		return ResourceVersionMap.fromResourceIds(myResourceTableDao.findAllById(matchingIds));
	}
}
