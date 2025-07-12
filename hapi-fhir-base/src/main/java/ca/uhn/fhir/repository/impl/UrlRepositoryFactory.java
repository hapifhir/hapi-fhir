package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.repository.IRepository;
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
public class UrlRepositoryFactory {
	private static final Logger ourLog = IRepository.ourLog;

	public static final String FHIR_REPOSITORY_URL_SCHEME = "fhir-repository:";
	static final Pattern ourUrlPattern = Pattern.compile("^fhir-repository:([A-Za-z-]+):(.*)");

	public static boolean isRepositoryUrl(String theBaseUrl) {
		return theBaseUrl != null
				&& theBaseUrl.startsWith(FHIR_REPOSITORY_URL_SCHEME)
				&& ourUrlPattern.matcher(theBaseUrl).matches();
	}

	@Nonnull
	public static IRepository buildRepository(@Nonnull String theBaseUrl, @Nullable FhirContext theFhirContext) {
		ourLog.debug("Loading repository for url: {}", theBaseUrl);
		Objects.requireNonNull(theBaseUrl);

		if (!isRepositoryUrl(theBaseUrl)) {
			throw new IllegalArgumentException(
					Msg.code(2737) + "Base URL is not a valid repository URL: " + theBaseUrl);
		}

		RepositoryRequest request = buildRequest(theBaseUrl, theFhirContext);

		ServiceLoader<IRepositoryLoader> serviceLoader = ServiceLoader.load(IRepositoryLoader.class);
		return request.load(serviceLoader)
				.orElseThrow(() -> new IllegalArgumentException(
						Msg.code(2738) + "Unable to find a repository loader for URL: " + theBaseUrl));
	}

	@Nonnull
	static RepositoryRequest buildRequest(@Nonnull String theBaseUrl, @Nullable FhirContext theFhirContext) {
		Matcher matcher = ourUrlPattern.matcher(theBaseUrl);
		String subScheme = null;
		String details = null;
		boolean found = matcher.matches();
		if (found) {
			subScheme = matcher.group(1);
			details = matcher.group(2);
		}

		return new RepositoryRequest(theBaseUrl, subScheme, details, Optional.ofNullable(theFhirContext));
	}

	record RepositoryRequest(String url, String subScheme, String details, Optional<FhirContext> fhirContext)
			implements IRepositoryLoader.IRepositoryRequest {
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

		@Override
		public Optional<FhirContext> getFhirContext() {
			return fhirContext;
		}

		private boolean canLoad(IRepositoryLoader nextLoader) {
			ourLog.debug("Checking repository loader {}", nextLoader.getClass().getName());
			return nextLoader.canLoad(this);
		}

		@Nonnull
		private IRepository loadRepository(IRepositoryLoader nextLoader) {
			ourLog.debug(
					"Building repository using loader {} with request url {}",
					nextLoader.getClass().getName(),
					url);
			return nextLoader.loadRepository(this);
		}

		@Nonnull
		Optional<IRepository> load(ServiceLoader<IRepositoryLoader> theServiceLoader) {
			return theServiceLoader.stream()
					.map(ServiceLoader.Provider::get)
					.filter(this::canLoad)
					.map(this::loadRepository)
					.findFirst();
		}
	}
}
