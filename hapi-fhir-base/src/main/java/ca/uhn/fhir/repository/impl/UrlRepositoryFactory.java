package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.IRepositoryLoader;
import ca.uhn.fhir.repository.IRepositoryLoader.IRepositoryRequest;
import com.google.common.annotations.Beta;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Use ServiceLoader to load {@link IRepositoryLoader} implementations
 * and provide chain-of-responsibility style matching by url to build IRepository instances.
 */
@Beta()
public class UrlRepositoryFactory {
	private static final Logger ourLog = IRepository.ourLog;

	public static final String FHIR_REPOSITORY_URL_SCHEME = "fhir-repository:";
	static final Pattern ourUrlPattern = Pattern.compile("^fhir-repository:([A-Za-z-]+):(.*)");

	public static boolean isRepositoryUrl(String theBaseUrl) {
		return theBaseUrl != null
				&& theBaseUrl.startsWith(FHIR_REPOSITORY_URL_SCHEME)
				&& ourUrlPattern.matcher(theBaseUrl).matches();
	}

	/**
	 * Find a factory for {@link IRepository} based on the given URL.
	 * This URL is expected to be in the form of fhir-repository:subscheme:details.
	 * The subscheme is used to find a matching {@link IRepositoryLoader} implementation.
	 *
	 * @param theRepositoryUrl a url of the form fhir-repository:subscheme:details
	 * @param theFhirContext the FHIR context to use for the repository, if required.
	 * @return a repository instance
	 * @throws IllegalArgumentException if the URL is not a valid repository URL, or no loader can be found for the URL.
	 */
	@Nonnull
	public static IRepository buildRepository(@Nonnull String theRepositoryUrl, @Nullable FhirContext theFhirContext) {
		ourLog.debug("Loading repository for url: {}", theRepositoryUrl);
		Objects.requireNonNull(theRepositoryUrl);

		if (!isRepositoryUrl(theRepositoryUrl)) {
			throw new IllegalArgumentException(
					Msg.code(2737) + "Base URL is not a valid repository URL: " + theRepositoryUrl);
		}

		ServiceLoader<IRepositoryLoader> load = ServiceLoader.load(IRepositoryLoader.class);
		IRepositoryRequest request = buildRequest(theRepositoryUrl, theFhirContext);
		for (IRepositoryLoader nextLoader : load) {
			ourLog.debug("Checking repository loader {}", nextLoader.getClass().getName());
			if (nextLoader.canLoad(request)) {
				return nextLoader.loadRepository(request);
			}
		}
		throw new IllegalArgumentException(
				Msg.code(2738) + "Unable to find a repository loader for URL: " + theRepositoryUrl);
	}

	/**
	 * Builder for our abstract {@link IRepositoryRequest} interface.
	 * @param theBaseUrl the fhir-repository URL to parse, e.g. fhir-repository:memory:my-repo
	 * @param theFhirContext the FHIR context to use for the repository, if required.
	 */
	@Nonnull
	public static IRepositoryRequest buildRequest(@Nonnull String theBaseUrl, @Nullable FhirContext theFhirContext) {
		Matcher matcher = ourUrlPattern.matcher(theBaseUrl);
		String subScheme = null;
		String details = null;
		boolean found = matcher.matches();
		if (found) {
			subScheme = matcher.group(1);
			details = matcher.group(2);
		}

		return new RepositoryRequest(theBaseUrl, subScheme, details, theFhirContext);
	}

	/**
	 * Internal implementation of {@link IRepositoryRequest}.
	 */
	record RepositoryRequest(String url, String subScheme, String details, FhirContext fhirContext)
			implements IRepositoryRequest {
		@Override
		public String getUrl() {
			return url;
		}

		@Override
		public String getSubScheme() {
			return subScheme;
		}

		@Override
		public String getDetails() {
			return details;
		}

		@SuppressWarnings("java:S6211")
		@Override
		public Optional<FhirContext> getFhirContext() {
			return Optional.ofNullable(fhirContext);
		}
	}
}
