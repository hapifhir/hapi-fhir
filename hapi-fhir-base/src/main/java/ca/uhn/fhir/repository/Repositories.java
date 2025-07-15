package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.impl.UrlRepositoryFactory;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Static factory methods for creating instances of {@link IRepository}.
 */
public class Repositories {
	/**
	 * Private constructor to prevent instantiation.
	 */
	Repositories() {}

	public static boolean isRepositoryUrl(String theBaseUrl) {
		return UrlRepositoryFactory.isRepositoryUrl(theBaseUrl);
	}

	/**
	 * Constructs a version of {@link IRepository} based on the given URL.
	 * These URLs are expected to be in the form of fhir-repository:subscheme:details.
	 * Currently supported subschemes include:
	 * <ul>
	 *     <li>memory - e.g. fhir-repository:memory:my-repo - the last piece (my-repo) identifies the repository</li>
	 * </ul>
	 * @param theBaseUrl a url of the form fhir-repository:subscheme:details
	 * @param theFhirContext the FHIR context to use for the repository, if required.
	 * @return
	 */
	@Nonnull
	public static IRepository repositoryForUrl(@Nonnull String theBaseUrl, @Nullable FhirContext theFhirContext) {
		return UrlRepositoryFactory.buildRepository(theBaseUrl, theFhirContext);
	}
}
