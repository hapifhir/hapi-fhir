package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import com.google.common.annotations.Beta;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * Service provider interface for loading repositories based on a URL.
 * Unstable API. Subject to change in future releases.
 * <p>
 * Implementors will receive the url parsed into IRepositoryRequest,
 * and dispatch of the <code>subScheme</code> property.
 * E.g. The InMemoryFhirRepositoryLoader will handle URLs
 * that start with <code>fhir-repository:memory:</code>.
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
	IRepository loadRepository(@Nonnull IRepositoryRequest theRepositoryRequest);

	interface IRepositoryRequest {
		/**
		 * Get the full URL of the repository provided by the user.
		 * @return the URL
		 */
		String getUrl();

		/**
		 * Get the sub-scheme of the URL, e.g. "memory" for "fhir-repository:memory:details".
		 * @return the sub-scheme
		 */
		String getSubScheme();

		/**
		 * Get any additional details provided by the user in the URL.
		 * This may be a url,  a unique identifier for the repository, or configuration details.
		 * @return the details
		 */
		String getDetails();

		Optional<FhirContext> getFhirContext();
	}
}
