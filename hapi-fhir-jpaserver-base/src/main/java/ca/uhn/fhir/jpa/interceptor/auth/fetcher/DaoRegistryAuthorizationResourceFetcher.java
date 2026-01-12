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

import java.util.Optional;

public class DaoRegistryAuthorizationResourceFetcher implements IAuthorizationResourceFetcher {

	private final DaoRegistry myDaoRegistry;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public DaoRegistryAuthorizationResourceFetcher(
			@Nonnull DaoRegistry theDaoRegistry, @Nonnull IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myDaoRegistry = theDaoRegistry;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public Optional<IBaseResource> fetch(IIdType theResourceId, RequestDetails theRequestDetails) {
		// FIXME ND - see if we can fallback to use RequestDetails
		if (theResourceId == null || !theResourceId.hasResourceType()) {
			return Optional.empty();
		}

		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
				theRequestDetails, ReadPartitionIdRequestDetails.forRead(theResourceId));

		SystemRequestDetails systemRequestDetails = SystemRequestDetails.forRequestPartitionId(requestPartitionId);

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
		IBaseResource resource = dao.read(theResourceId, systemRequestDetails);
		return Optional.ofNullable(resource);
	}
}
