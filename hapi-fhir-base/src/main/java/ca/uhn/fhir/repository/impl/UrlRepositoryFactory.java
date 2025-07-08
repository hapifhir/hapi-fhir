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

public class UrlRepositoryFactory {
	private static final Logger ourLog = IRepository.ourLog;

	public static final String FHIR_REPOSITORY_URL_SCHEME = "fhir-repository:";
	static final Pattern ourUrlPattern = Pattern.compile("^fhir-repository:([A-Za-z]+):(.*)");

	public static boolean isRepositoryUrl(String theBaseUrl) {
		return theBaseUrl != null &&
			theBaseUrl.startsWith(FHIR_REPOSITORY_URL_SCHEME) &&
			ourUrlPattern.matcher(theBaseUrl).matches();
	}

	protected record RepositoryRequest(String url, String subScheme, String details, FhirContext fhirContext) implements IRepositoryLoader.IRepositoryRequest {
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
			return Optional.ofNullable(fhirContext);
		}
	}

	@Nonnull
	public static IRepository buildRepository(@Nonnull String theBaseUrl, @Nullable FhirContext theFhirContext) {
		ourLog.debug("Loading repository for url: {}", theBaseUrl);
		Objects.requireNonNull(theBaseUrl);

		if (!isRepositoryUrl(theBaseUrl)) {
			// fixme hapi-code
			throw new IllegalArgumentException(Msg.code(99997) + "Base URL is not a valid repository URL: " + theBaseUrl);
		}

		ServiceLoader<IRepositoryLoader> load = ServiceLoader.load(IRepositoryLoader.class);
		IRepositoryLoader.IRepositoryRequest request = buildRequest(theBaseUrl, theFhirContext);
		for (IRepositoryLoader nextLoader : load) {
			ourLog.debug("Checking repository loader {}", nextLoader.getClass().getName());
			if (nextLoader.canLoad(request)) {
				return nextLoader.loadRepository(request);
			}
		}
	// fixme hapi-code
		throw new IllegalArgumentException(Msg.code(99999) + "Unable to find a repository loader for URL: " + theBaseUrl);
	}

	@Nonnull
	protected static RepositoryRequest buildRequest(@Nonnull String theBaseUrl, @Nullable FhirContext theFhirContext) {
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
}
