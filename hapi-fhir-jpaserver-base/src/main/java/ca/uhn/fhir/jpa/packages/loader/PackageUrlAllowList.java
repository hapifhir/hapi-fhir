package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.jpa.model.PackageUrlScheme;
import com.google.common.collect.ImmutableList;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static ca.uhn.fhir.jpa.model.util.PackageUrlConstants.CLASSPATH_PREFIX;
import static ca.uhn.fhir.jpa.model.util.PackageUrlConstants.FILE_PREFIX;
import static ca.uhn.fhir.jpa.model.util.PackageUrlConstants.WILDCARD;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class PackageUrlAllowList {
	private static final Logger ourLog = LoggerFactory.getLogger(PackageUrlAllowList.class);

	/**
	 * Create a PackageUrlAllowList with the provided
	 * remote prefixes and local prefixes
	 */
	public static PackageUrlAllowList of(
			@Nonnull List<String> theRemotePrefixes, @Nonnull List<String> theLocalPrefixes) {
		return new PackageUrlAllowList(theRemotePrefixes, theLocalPrefixes);
	}

	/**
	 * Create a PackageUrlAllowList that allows all remote and all local
	 * prefixes.
	 * Consider carefully if you actually need this. Allowing "all" should
	 * only be required in very select cases:
	 * * testing
	 * * user-driven flows where the 'configurer' and 'user' are the one and the same
	 */
	public static PackageUrlAllowList allowAll() {
		return new PackageUrlAllowList(List.of(WILDCARD), List.of(WILDCARD));
	}

	/**
	 * A list of url (prefixes) that are allowed
	 * for packageloader.
	 */
	private final List<String> myRemotePrefixes;

	/**
	 * A list of prefixes allowed (for file/classpath: 'urls')
	 */
	private final List<String> myLocalPrefixes;

	private PackageUrlAllowList(List<String> theRemotePrefixes, List<String> theLocalPrefixes) {
		myRemotePrefixes = ImmutableList.copyOf(theRemotePrefixes);
		myLocalPrefixes = ImmutableList.copyOf(theLocalPrefixes);
	}

	public List<String> getRemotePrefixes() {
		return myRemotePrefixes;
	}

	public List<String> getLocalPrefixes() {
		return myLocalPrefixes;
	}

	/**
	 * Checks if the url is:
	 * * not blank
	 * * in
	 * returns true if it's in the whitelist (and not blank), or if the
	 * whitelist includes all urls.
	 * returns false otherwise
	 */
	public boolean isAllowed(String theUrl) {
		if (isBlank(theUrl)) {
			return false;
		}
		String urlToTest = theUrl.trim();
		PackageUrlScheme scheme = PackageUrlScheme.parseScheme(urlToTest);
		if (scheme == null) {
			return false;
		}
		switch (scheme) {
			case FILE, CLASSPATH -> {
				return isLocalAllowed(urlToTest);
			}
			case HTTP, HTTPS -> {
				return isRemoteAllowed(urlToTest);
			}
			default -> {
				// we don't support anything but file, classpath, http/https
				// we do *not* support things like ftp (for example)
				ourLog.error("Unrecognized scheme {}", scheme.name());
				return false;
			}
		}
	}

	private boolean isLocalAllowed(String theUrl) {
		if (myLocalPrefixes.contains(WILDCARD)) {
			return true;
		}

		if (theUrl.toLowerCase().startsWith(CLASSPATH_PREFIX)) {
			return myLocalPrefixes.stream()
					.filter(url -> url.startsWith(CLASSPATH_PREFIX))
					.anyMatch(url -> {
						return isPathPrefix(url, theUrl);
					});
		}

		try {
			Path candidate = toNormalizedPath(theUrl);
			if (candidate == null) {
				return false;
			}

			return myLocalPrefixes.stream()
					.filter(url -> !url.startsWith(CLASSPATH_PREFIX))
					.map(this::toNormalizedPath)
					.anyMatch(candidate::startsWith);
		} catch (IllegalArgumentException ex) {
			// we hit an invalid url
			return false;
		}
	}

	// normalize the path so that it's not something like "file:/valid/../invalid/path"
	private Path toNormalizedPath(String theFileUrl) {
		String candidate = theFileUrl.toLowerCase().startsWith(FILE_PREFIX)
				? URI.create(theFileUrl).getPath()
				: theFileUrl;
		if (candidate == null) {
			// invalid path after file:
			return null;
		}
		return Paths.get(candidate).toAbsolutePath().normalize();
	}

	private boolean isPathPrefix(String thePrefixPath, String theCanddiatePath) {
		// remove the trailing "/" if present
		String prefix = Strings.CS.removeEnd(thePrefixPath, "/");
		String candidate = Strings.CS.removeEnd(StringUtils.defaultIfEmpty(theCanddiatePath, "/"), "/");

		String canonPrefix = canonicalizeClasspath(prefix);
		String canonCandidate = canonicalizeClasspath(candidate);

		// exactly equals -> specific path exactly (including file name etc)
		// starts with + "/" - ensure it's the correct path and not "/my/valid-not/" vs "/my/valid/
		return canonPrefix.equals(canonCandidate) || canonCandidate.startsWith(canonPrefix + "/");
	}

	private String canonicalizeClasspath(String theClasspathURl) {
		String trimmed = theClasspathURl.trim();
		String afterScheme = trimmed.substring(trimmed.indexOf(":") + 1);
		return "/" + StringUtils.stripStart(afterScheme, "/");
	}

	private boolean isRemoteAllowed(String theUrl) {
		if (myRemotePrefixes.contains(WILDCARD)) {
			return true;
		}
		URI candidate = parseHttpUri(theUrl);
		if (candidate == null) {
			// parsing failure
			// not a valid uri
			return false;
		}

		return myRemotePrefixes.stream()
				.map(this::parseHttpUri)
				.filter(Objects::nonNull)
				.anyMatch(url -> {
					return isPrefixUrl(url, candidate);
				});
	}

	private boolean isPrefixUrl(URI theAllowed, URI theCandidate) {
		if (!theAllowed.getScheme().equalsIgnoreCase(theCandidate.getScheme())
				|| !theAllowed.getHost().equalsIgnoreCase(theCandidate.getHost())) {
			return false;
		}

		String prefix = Strings.CS.removeEnd(theAllowed.getPath(), "/");
		String candidate = Strings.CS.removeEnd(StringUtils.defaultIfEmpty(theCandidate.getPath(), "/"), "/");

		return prefix.equals(candidate) || candidate.startsWith(prefix);
	}

	private URI parseHttpUri(String theUri) {
		try {
			URI uri = new URI(theUri.trim());
			return uri.getHost() == null ? null : uri;
		} catch (URISyntaxException e) {
			return null;
		}
	}
}
