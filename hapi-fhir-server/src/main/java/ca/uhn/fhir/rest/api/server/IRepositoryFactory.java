package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.repository.Repository;
import com.google.common.annotations.Beta;

/**
 * Factory interface to return a {@link Repository} from a {@link RequestDetails}
 */
@FunctionalInterface
@Beta
public interface IRepositoryFactory {
	Repository create(RequestDetails requestDetails);
}
