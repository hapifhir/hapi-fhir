package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.fhir.api.Repository;

/**
 * Factory interface to return a {@link Repository} from a {@link RequestDetails}
 */
public interface RepositoryFactoryForRepositoryInterface {
	Repository create(RequestDetails theRequestDetails);
}
