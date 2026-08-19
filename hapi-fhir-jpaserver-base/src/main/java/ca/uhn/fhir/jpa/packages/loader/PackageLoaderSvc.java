/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.PackageUrlScheme;
import ca.uhn.fhir.jpa.model.util.PackageUrlConstants;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.http.ManagedWebAccess;
import org.hl7.fhir.utilities.npm.BasePackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.settings.FhirSettingsPOJO;
import org.hl7.fhir.utilities.settings.ServerDetailsPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class PackageLoaderSvc extends BasePackageCacheManager {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageLoaderSvc.class);

	private static PackageLoaderSettings ourApplied;

	private PackageLoaderSettings mySettings;

	public PackageLoaderSvc(PackageLoaderSettings theLoaderSettings) {
		mySettings = theLoaderSettings;
	}

	public static PackageLoaderSettings getAppliedSettings() {
		return ourApplied;
	}

	/**
	 * Returns the loader to the core library's default state. Note that the library defaults SSRF protection
	 * to on, so this is a reset to "protected", not to whatever this JVM happened to have in effect. Callers
	 * that need the latter should use {@link #applySettings(PackageLoaderSettings)} and close the scope.
	 */
	public static synchronized void resetSettings() {
		ourApplied = null;
		ManagedWebAccess.setSsrfProtectionEnabled(true);
		ManagedWebAccess.loadFromFHIRSettings();
	}

	/**
	 * Inits the settings into the core libraries.
	 * this kinda sucks... but it's the required contract.
	 *
	 * We only do this for web based urls
	 * since only web urls are handled by BasePackageLoader
	 * (this class will handle locals only).
	 *
	 * Syncronoized because module contexts can start concurrently, and this is read-modify-write on shared static state.
	 */
	public static synchronized void initSettings(PackageLoaderSettings theSettings) {
		List<AllowedUrlPrefix> newPrefixes =
				theSettings.getPackageUrlAllowList().getRemotePrefixes();
		List<AllowedUrlPrefix> appliedPrefixes =
				ourApplied == null ? null : ourApplied.getPackageUrlAllowList().getRemotePrefixes();

		if (appliedPrefixes != null && !ListUtils.isEqualList(appliedPrefixes, newPrefixes)) {
			ourLog.warn(
					"Remote package URL allow-list is being changed from {} to {}; this config is cluster-wide and cannot vary per module!",
					String.join(
							", ",
							appliedPrefixes.stream().map(url -> url.toString()).collect(Collectors.toSet())),
					String.join(
							", ",
							newPrefixes.stream().map(url -> url.toString()).collect(Collectors.toSet())));
		} else if (appliedPrefixes != null) {
			return; // already applied
		} else {
			ourLog.info("Applying remote package URL allow-list with {} entries", newPrefixes.size());
		}

		doApplySettings(theSettings);
	}

	static synchronized void doApplySettings(PackageLoaderSettings theSettings) {
		// last in wins
		ourApplied = theSettings;

		if (ourApplied.getPackageUrlAllowList().allowsAll()) {
			ourLog.warn("Allowing all. This shouldn't ever be in production code.");
			ManagedWebAccess.setSsrfProtectionEnabled(false);
			return;
		}
		ManagedWebAccess.setSsrfProtectionEnabled(true);

		List<ServerDetailsPOJO> servers = theSettings.getPackageUrlAllowList().getRemotePrefixes().stream()
				.map(prefix -> {
					return ServerDetailsPOJO.builder()
							.url(prefix.getUrl())
							.authenticationType("none")
							.type("web")
							.allowHttp(isPlainHttpPrefix(prefix))
							.allowPrivateNetwork(prefix.isPrivateNetwork())
							.headers(Collections.emptyMap())
							.build();
				})
				.collect(Collectors.toList());

		ManagedWebAccess.loadFromFHIRSettings(
				FhirSettingsPOJO.builder().servers(servers).build());
	}

	/**
	 * Whether plain HTTP is permitted for a remote allow list entry.
	 * <p>
	 * This is derived from the entry rather than configured alongside it. Allow list matching
	 * requires the scheme of a candidate URL to equal the scheme of the entry it matches, so an
	 * {@code https:} entry can never serve a plain HTTP fetch and an {@code http:} entry can never
	 * serve anything else. A separately configured flag could only ever agree with the scheme or
	 * contradict it.
	 */
	private static boolean isPlainHttpPrefix(AllowedUrlPrefix thePrefix) {
		String url = thePrefix.getUrl();
		return url != null && !url.trim().toLowerCase().startsWith(PackageUrlConstants.HTTPS_PREFIX);
	}

	/**
	 * Applies the given settings and returns a scope which restores the previous ones when closed.
	 * <p>
	 * The SSRF flag is captured directly from the core library rather than inferred, because when nothing
	 * has been applied yet there is no {@link PackageLoaderSettings} to derive it from.
	 *
	 * @return a scope to be closed once the settings are no longer needed
	 */
	public static synchronized PackageLoaderSettingsScope applySettings(PackageLoaderSettings theSettings) {
		PackageLoaderSettings previous = ourApplied;
		boolean previousSsrfProtectionEnabled = ManagedWebAccess.isSsrfProtectionEnabled();

		doApplySettings(theSettings);
		return new PackageLoaderSettingsScope(previous, ourApplied, previousSsrfProtectionEnabled);
	}

	public NpmPackageData fetchPackageFromPackageSpec(PackageInstallationSpec theSpec) throws IOException {
		if (isNotBlank(theSpec.getPackageUrl())) {
			byte[] contents = loadPackageUrlContents(theSpec.getPackageUrl());
			return createNpmPackageDataFromData(
					theSpec.getName(),
					theSpec.getVersion(),
					theSpec.getPackageUrl(),
					new ByteArrayInputStream(contents));
		}

		return fetchPackageFromServerInternal(theSpec.getName(), theSpec.getVersion());
	}

	/**
	 * Loads the package, but won't save it anywhere.
	 * Returns the data to the caller
	 *
	 * @return - a POJO containing information about the NpmPackage, as well as it's contents
	 * 			as fetched from the server
	 * @throws IOException
	 */
	public NpmPackageData fetchPackageFromPackageSpec(String thePackageId, String thePackageVersion)
			throws FHIRException, IOException {
		return fetchPackageFromServerInternal(thePackageId, thePackageVersion);
	}

	private NpmPackageData fetchPackageFromServerInternal(String thePackageId, String thePackageVersion)
			throws IOException {
		BasePackageCacheManager.InputStreamWithSrc pkg = this.loadFromPackageServer(thePackageId, thePackageVersion);

		if (pkg == null) {
			throw new ResourceNotFoundException(
					Msg.code(1301) + "Unable to locate package " + thePackageId + "#" + thePackageVersion);
		}

		return createNpmPackageDataFromData(
				thePackageId, thePackageVersion == null ? pkg.version : thePackageVersion, pkg.url, pkg.stream);
	}

	/**
	 * Creates an NpmPackage data object.
	 *
	 * @param thePackageId - the id of the npm package
	 * @param thePackageVersionId - the version id of the npm package
	 * @param theSourceDesc - the installation spec description or package url
	 * @param thePackageTgzInputStream - the package contents.
	 *                                  Typically fetched from a server, but can be added directly to the package spec
	 * @return
	 * @throws IOException
	 */
	public NpmPackageData createNpmPackageDataFromData(
			String thePackageId, String thePackageVersionId, String theSourceDesc, InputStream thePackageTgzInputStream)
			throws IOException {
		Validate.notBlank(thePackageId, "thePackageId must not be null");
		Validate.notBlank(thePackageVersionId, "thePackageVersionId must not be null");
		Validate.notNull(thePackageTgzInputStream, "thePackageTgzInputStream must not be null");

		byte[] bytes = IOUtils.toByteArray(thePackageTgzInputStream);

		ourLog.info("Parsing package .tar.gz ({} bytes) from {}", bytes.length, theSourceDesc);

		NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(bytes));

		return new NpmPackageData(
				thePackageId, thePackageVersionId, theSourceDesc, bytes, npmPackage, thePackageTgzInputStream);
	}

	@Override
	public NpmPackage loadPackageFromCacheOnly(String theS, @Nullable String theS1) {
		throw new UnsupportedOperationException(Msg.code(2215)
				+ "Cannot load from cache. "
				+ "Caching not supported in PackageLoaderSvc. Use JpaPackageCache instead.");
	}

	@Override
	public NpmPackage addPackageToCache(String theS, String theS1, InputStream theInputStream, String theS2) {
		throw new UnsupportedOperationException(Msg.code(2216)
				+ "Cannot add to cache. "
				+ "Caching not supported in PackageLoaderSvc. Use JpaPackageCache instead.");
	}

	@Override
	public NpmPackage loadPackage(String theS, String theS1) throws FHIRException {
		/*
		 * We throw an exception because while we could pipe this call through
		 * to loadPackageOnly ourselves, returning NpmPackage details
		 * on their own provides no value if nothing is cached/loaded onto hard disk somewhere
		 *
		 */
		throw new UnsupportedOperationException(Msg.code(2217)
				+ "No packages are cached; "
				+ " this service only loads from the server directly. "
				+ "Call fetchPackageFromServer to fetch the npm package from the server. "
				+ "Or use JpaPackageCache for a cache implementation.");
	}

	public byte[] loadPackageUrlContents(String thePackageUrl) {
		if (!mySettings.getPackageUrlAllowList().isAllowed(thePackageUrl)) {
			throw new InvalidRequestException(
					Msg.code(3028) + "Attempting to request from non-whitelisted path " + thePackageUrl);
		}

		PackageUrlScheme scheme = PackageUrlScheme.parseScheme(thePackageUrl);

		if (scheme != null) {
			switch (scheme) {
				case CLASSPATH -> {
					return ClasspathUtil.loadResourceAsByteArray(thePackageUrl.substring("classpath:".length()));
				}
				case FILE -> {
					try {
						return Files.readAllBytes(Paths.get(new URI(thePackageUrl)));
					} catch (IOException | URISyntaxException e) {
						throw new InternalErrorException(
								Msg.code(2031) + "Error loading \"" + thePackageUrl + "\": " + e.getMessage());
					}
				}
				case HTTPS, HTTP -> {
					return fetchHttpPackageContents(thePackageUrl);
				}
			}
		}

		throw new InvalidRequestException(Msg.code(3029) + "Unrecognized scheme for whitelist URL: " + thePackageUrl);
	}

	private byte[] fetchHttpPackageContents(String thePackageUrl) {
		String currentUrl = thePackageUrl;
		Set<String> visited = new LinkedHashSet<>();
		visited.add(currentUrl);

		/*
		 * we filter redirects up to a maximum # of hops {@link PackageUrlConstants#MAX_REDIRECTS}
		 */
		try (CloseableHttpClient client = HttpClientBuilder.create()
				.setDnsResolver(new PackageLoaderDnsResolver(mySettings.getPackageUrlAllowList()))
				.disableRedirectHandling()
				.build()) {
			for (int hop = 0; hop <= PackageUrlConstants.MAX_REDIRECTS; hop++) {
				try (CloseableHttpResponse request = client.execute(new HttpGet(currentUrl))) {
					int status = request.getStatusLine().getStatusCode();
					// 308 == permanent redirect + anything later isn't
					// in our library for codes, so we use < 400 to catch them instead
					if (status >= HttpStatus.SC_MULTIPLE_CHOICES && status < HttpStatus.SC_BAD_REQUEST) {
						String target = resolveAllowedRedirect(currentUrl, request, visited);
						// discard the redirect response
						EntityUtils.consumeQuietly(request.getEntity());
						currentUrl = target;
						continue;
					} else if (status != HttpStatus.SC_OK) {
						throw new ResourceNotFoundException(
								Msg.code(1303) + "Received HTTP " + status + " from URL: " + thePackageUrl);
					}
					return IOUtils.toByteArray(request.getEntity().getContent());
				}
			}

			throw new InvalidRequestException(Msg.code(3032) + "Exceeded " + PackageUrlConstants.MAX_REDIRECTS
					+ " redirects loading a package; chain was " + String.join(" -> ", visited));
		} catch (IOException e) {
			throw new InvalidRequestException(
					Msg.code(1304) + "Error loading \"" + currentUrl + "\": " + e.getMessage());
		}
	}

	/**
	 * Manually check the redirect to make sure it, too, is within the whitelist
	 */
	private String resolveAllowedRedirect(
			String theCurrentUrl, CloseableHttpResponse theResponse, Set<String> theVisited) {
		int status = theResponse.getStatusLine().getStatusCode();

		// check the location header
		Header location = theResponse.getFirstHeader(HttpHeaders.LOCATION);
		if (location == null || isBlank(location.getValue())) {
			throw new InvalidRequestException(Msg.code(3033) + "Received HTTP status " + status + " from URL "
					+ theCurrentUrl + " with no Location header");
		}

		URI target;
		try {
			target = URI.create(theCurrentUrl).resolve(location.getValue().trim());
		} catch (IllegalArgumentException ex) {
			throw new InvalidRequestException(Msg.code(3034) + "Received HTTP " + status + " from URL " + theCurrentUrl
					+ " with an unusable Location: " + location.getValue());
		}

		// check the scheme
		String targetUrl = target.toString();
		PackageUrlScheme currentScheme = PackageUrlScheme.parseScheme(theCurrentUrl);
		PackageUrlScheme targetScheme = PackageUrlScheme.parseScheme(targetUrl);

		boolean schemeAllowed = isSchemeAllowed(targetScheme, currentScheme);
		if (!schemeAllowed) {
			throw new InvalidRequestException(
					Msg.code(3035) + "Refusing redirect from " + theCurrentUrl + " to " + targetUrl);
		}

		// check the target is in the whitelist
		if (!mySettings.getPackageUrlAllowList().isAllowed(targetUrl)) {
			throw new InvalidRequestException(Msg.code(3036) + "Refusing redirect from " + theCurrentUrl
					+ " to non-whitelisted URL " + targetUrl);
		}

		if (!theVisited.add(targetUrl)) {
			throw new InvalidRequestException(Msg.code(3037) + "Redirect loop loading a package; chain was "
					+ String.join(", ", theVisited) + " -> " + targetUrl);
		}

		// everything's good - we can pass back the (approved) url
		return targetUrl;
	}

	/**
	 * we do not allow HTTPS -> HTTP urls no matter what
	 * (ie, no downgrading security in a redirect hop)
	 */
	private static boolean isSchemeAllowed(PackageUrlScheme targetScheme, PackageUrlScheme currentScheme) {
		return targetScheme == PackageUrlScheme.HTTPS
				|| (targetScheme == PackageUrlScheme.HTTP && currentScheme == PackageUrlScheme.HTTP);
	}

	@Override
	public String getLatestVersion(String statedId, boolean milestonesOnly) throws IOException {
		// As of release 6.9.4 of org.hl7.fhir.core, this is only used internally by the supporting implementations for
		// the Validator CLI (not InstanceValidator). It is not called except in that specific use case.
		throw new UnsupportedOperationException(Msg.code(2890));
	}

	@Override
	public String getLatestVersion(String statedId, String versionFilter) throws IOException {
		// As of release 6.9.4 of org.hl7.fhir.core, this is only used internally by the supporting implementations for
		// the Validator CLI (not InstanceValidator). It is not called except in that specific use case.
		throw new UnsupportedOperationException(Msg.code(2891));
	}
}
