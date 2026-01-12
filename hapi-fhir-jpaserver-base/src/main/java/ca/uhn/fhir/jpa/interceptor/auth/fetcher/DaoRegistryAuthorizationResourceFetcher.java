package ca.uhn.fhir.jpa.interceptor.auth.fetcher;

import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.fetcher.IAuthorizationResourceFetcher;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DaoRegistryAuthorizationResourceFetcher implements IAuthorizationResourceFetcher {

	private static final Logger ourLog = LoggerFactory.getLogger(DaoRegistryAuthorizationResourceFetcher.class);

	private static final String RESOURCE_CACHE_KEY =
			DaoRegistryAuthorizationResourceFetcher.class.getName() + "_RESOURCE_CACHE";

	private final DaoRegistry myDaoRegistry;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public DaoRegistryAuthorizationResourceFetcher(
			@Nonnull DaoRegistry theDaoRegistry, @Nonnull IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myDaoRegistry = theDaoRegistry;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public Optional<IBaseResource> fetch(IIdType theResourceId, RequestDetails theRequestDetails) {
		if (theResourceId == null) {
			return Optional.empty();
		}

		if (!theResourceId.hasResourceType()) {
			ourLog.warn(
					"Cannot fetch resource for authorization: resource ID {} does not have a resource type",
					theResourceId.getValue());
			return Optional.empty();
		}

		String resourceIdKey = theResourceId.getValue();

		Map<String, IBaseResource> cache = getResourceCache(theRequestDetails);
		if (cache.containsKey(resourceIdKey)) {
			return Optional.ofNullable(cache.get(resourceIdKey));
		}

		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
				theRequestDetails, ReadPartitionIdRequestDetails.forRead(theResourceId));

		SystemRequestDetails systemRequestDetails = SystemRequestDetails.forRequestPartitionId(requestPartitionId);

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
		IBaseResource resource = dao.read(theResourceId, systemRequestDetails);
		cache.put(resourceIdKey, resource);

		return Optional.ofNullable(resource);
	}

	private Map<String, IBaseResource> getResourceCache(RequestDetails theRequestDetails) {
		@SuppressWarnings("unchecked")
		Map<String, IBaseResource> cache =
				(Map<String, IBaseResource>) theRequestDetails.getUserData().get(RESOURCE_CACHE_KEY);

		if (cache == null) {
			cache = new HashMap<>();
			theRequestDetails.getUserData().put(RESOURCE_CACHE_KEY, cache);
		}
		return cache;
	}
}
