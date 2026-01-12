package ca.uhn.fhir.rest.server.interceptor.auth.fetcher;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Optional;

public interface IAuthResourceFetcher {

	Optional<IBaseResource> fetch(IIdType theResourceId, RequestDetails theRequestDetails);
}
