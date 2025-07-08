package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import com.google.common.annotations.Beta;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * Service provider interface for loading repositories based on a URL.
 * Unstable API. Subject to change in future releases.
 */
@Beta()
public interface IRepositoryLoader {
	/**
	 * Impelmentors should return true if they can handle the given URL.
	 * @param theRepositoryRequest containing the URL to check
	 * @return true if supported
	 */
	boolean canLoad(IRepositoryRequest theRepositoryRequest);

	/**
	 * Construct a version of {@link IRepository} based on the given URL.
	 * Implementors can assume that the request passed the canLoad() check.
	 *
	 * @param theRepositoryRequest the details of the repository to load.
	 * @return a repository instance
	 */
	@Nonnull
	IRepository loadRepository(IRepositoryRequest theRepositoryRequest);

	interface IRepositoryRequest {
		String getUrl();
		String getSubScheme();
		String getDetails();
		Optional<FhirContext> getFhirContext();
	}


}
