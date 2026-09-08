package ca.uhn.fhir.jpa.packages.loader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit coverage for the package URL allow-list. This is the enforcement point that decides whether a
 * caller-supplied packageUrl may be read at all, so the interesting cases are the near-misses: sibling
 * directories, path traversal, domain-suffix confusion, alternate spellings of the same scheme, and
 * malformed input.
 * <p>
 * Several of these are expected to fail against the current implementation and are written to drive
 * the fixes rather than to describe today's behaviour. Specifically: whitespace is trimmed by
 * PackageUrlScheme#parseScheme but not by the matching, scheme comparison is case-insensitive when
 * classifying but case-sensitive when matching, remote prefixes are compared as raw strings so a
 * domain suffix can slip through, and a URL with no path (for example {@code file:}) reaches
 * {@code Paths.get(null)}.
 */
// Created by claude-opus-5
class PackageUrlAllowListTest {

	private static final AllowedUrlPrefix LOCAL_DIR = new AllowedUrlPrefix("file:/opt/packages", false);
	private static final AllowedUrlPrefix CLASSPATH_DIR = new AllowedUrlPrefix("classpath:/package-loading", false);
	private static final AllowedUrlPrefix REMOTE_HOST = new AllowedUrlPrefix("https://packages.fhir.org", false);

	private static PackageUrlAllowList localAllowList() {
		return PackageUrlAllowList.of(List.of(), List.of(LOCAL_DIR));
	}

	private static PackageUrlAllowList classpathAllowList() {
		return PackageUrlAllowList.of(List.of(), List.of(CLASSPATH_DIR));
	}

	private static PackageUrlAllowList remoteAllowList() {
		return PackageUrlAllowList.of(List.of(REMOTE_HOST), List.of());
	}

	// ---------------------------------------------------------------- wildcard

	@ParameterizedTest
	@ValueSource(
			strings = {
				"file:/anywhere/at/all/pkg.tgz",
				"classpath:/anything/pkg.tgz",
				"http://example.com/pkg.tgz",
				"https://example.com/pkg.tgz"
			})
	void isAllowed_withWildcard_allowsEverySupportedScheme(String theUrl) {
		assertThat(PackageUrlAllowList.allowAll().isAllowed(theUrl)).isTrue();
	}

	@Test
	void isAllowed_withWildcard_stillRejectsUnsupportedScheme() {
		// the wildcard widens which locations are permitted, not which schemes we know how to read
		assertThat(PackageUrlAllowList.allowAll().isAllowed("jar:file:/opt/packages/pkg.tgz"))
				.isFalse();
	}

	// ------------------------------------------------------------- empty lists

	@ParameterizedTest
	@ValueSource(strings = {"file:/opt/packages/pkg.tgz", "classpath:/package-loading/pkg.tgz", "https://packages.fhir.org/pkg.tgz"})
	void isAllowed_withNoPrefixesConfigured_deniesEverything(String theUrl) {
		// an empty allow-list means deny, which must stay distinguishable from the wildcard
		assertThat(PackageUrlAllowList.of(List.of(), List.of()).isAllowed(theUrl)).isFalse();
	}

	// -------------------------------------------------------------- file: URLs

	@ParameterizedTest
	@CsvSource({
		// exact directory, and the two common spellings of the same location
		"file:/opt/packages/pkg.tgz, true",
		"file:///opt/packages/pkg.tgz, true",
		"file:/opt/packages/nested/dir/pkg.tgz, true",
		// a sibling directory sharing a textual prefix must not match
		"file:/opt/packages-evil/pkg.tgz, false",
		"file:/opt/other/pkg.tgz, false",
		// traversal out of the permitted directory
		"file:/opt/packages/../../etc/shadow, false",
		"file:/opt/packages/../packages-evil/pkg.tgz, false",
		// scheme spelled in a different case still names the same file
		"FILE:/opt/packages/pkg.tgz, true"
	})
	void isAllowed_withFileUrl_matchesOnlyInsidePermittedDirectory(String theUrl, boolean theExpected) {
		assertThat(localAllowList().isAllowed(theUrl)).isEqualTo(theExpected);
	}

	/**
	 * Kept out of the CsvSource above because {@code @CsvSource} trims argument whitespace by default,
	 * which would quietly defeat the point of these cases.
	 */
	@Test
	void isAllowed_withSurroundingWhitespace_matches() {
		// whitespace survives both request JSON and the multiline config text area
		assertThat(localAllowList().isAllowed("  file:/opt/packages/pkg.tgz")).isTrue();
		assertThat(localAllowList().isAllowed("file:/opt/packages/pkg.tgz\n")).isTrue();
		assertThat(localAllowList().isAllowed("\tfile:/opt/packages/pkg.tgz ")).isTrue();
	}

	/**
	 * Config normally writes local prefixes with the scheme attached, e.g. {@code file:/opt/packages},
	 * which {@link #LOCAL_DIR} covers. A bare path is accepted too and resolves to the same directory.
	 */
	@Test
	void isAllowed_withBarePathLocalPrefix_matchesTheSameDirectoryAsTheSchemeQualifiedForm() {
		PackageUrlAllowList allowList =
				PackageUrlAllowList.of(List.of(), List.of(new AllowedUrlPrefix("/opt/packages", false)));

		assertThat(allowList.isAllowed("file:/opt/packages/pkg.tgz")).isTrue();
		assertThat(allowList.isAllowed("file:/opt/packages-evil/pkg.tgz")).isFalse();
	}

	// --------------------------------------------------------- classpath: URLs

	@ParameterizedTest
	@CsvSource({
		"classpath:/package-loading/pkg.tgz, true",
		"classpath:/package-loading/nested/pkg.tgz, true",
		"classpath:/other/pkg.tgz, false",
		"classpath:/package-loading-evil/pkg.tgz, false",
		// the double-slash spelling is what existing hapi tests use, and names the same resource
		"classpath://package-loading/pkg.tgz, true",
		"CLASSPATH:/package-loading/pkg.tgz, true"
	})
	void isAllowed_withClasspathUrl_matchesOnlyPermittedResourcePrefix(String theUrl, boolean theExpected) {
		assertThat(classpathAllowList().isAllowed(theUrl)).isEqualTo(theExpected);
	}

	@Test
	void isAllowed_withClasspathUrl_isNotMatchedByFilePrefixes() {
		// a filesystem prefix cannot vouch for a classpath resource; there is no shared namespace
		PackageUrlAllowList allowList = PackageUrlAllowList.of(List.of(), List.of(LOCAL_DIR));

		assertThat(allowList.isAllowed("classpath:/opt/packages/pkg.tgz")).isFalse();
	}

	// -------------------------------------------------------------- http URLs

	@ParameterizedTest
	@CsvSource({
		"https://packages.fhir.org/pkg.tgz, true",
		"https://packages.fhir.org/nested/pkg.tgz, true",
		// same host over a weaker scheme is a different prefix
		"http://packages.fhir.org/pkg.tgz, false",
		"https://other.org/pkg.tgz, false",
		// scheme and host are case-insensitive
		"HTTPS://packages.fhir.org/pkg.tgz, true",
		"https://PACKAGES.FHIR.ORG/pkg.tgz, true"
	})
	void isAllowed_withRemoteUrl_matchesOnlyPermittedOrigin(String theUrl, boolean theExpected) {
		assertThat(remoteAllowList().isAllowed(theUrl)).isEqualTo(theExpected);
	}

	@ParameterizedTest
	@ValueSource(
			strings = {
				// an attacker-registered domain for which the permitted origin is a textual prefix
				"https://packages.fhir.org.evil.com/pkg.tgz",
				"https://packages.fhir.orgevil.com/pkg.tgz",
				// userinfo makes the real host something else entirely
				"https://packages.fhir.org@evil.com/pkg.tgz"
			})
	void isAllowed_withHostThatOnlyLooksLikePermittedOrigin_denies(String theUrl) {
		assertThat(remoteAllowList().isAllowed(theUrl)).isFalse();
	}

	// ------------------------------------------------- unsupported schemes

	/**
	 * Schemes we never read from. Several are classic SSRF and scheme-confusion vectors, and
	 * {@code jar:} in particular is dangerous because it can wrap an otherwise permitted location.
	 */
	@ParameterizedTest
	@ValueSource(
			strings = {
				"ftp://packages.fhir.org/pkg.tgz",
				"sftp://packages.fhir.org/pkg.tgz",
				"ftps://packages.fhir.org/pkg.tgz",
				"gopher://packages.fhir.org/pkg.tgz",
				"dict://packages.fhir.org:2628/pkg.tgz",
				"ldap://packages.fhir.org/pkg.tgz",
				"smb://packages.fhir.org/share/pkg.tgz",
				"mailto:packages@fhir.org",
				"data:application/gzip;base64,H4sIAAAAAAAA",
				"netdoc:/opt/packages/pkg.tgz",
				"jar:file:/opt/packages/pkg.tgz",
				// a jar URL wrapping a location that would otherwise be permitted
				"jar:file:/opt/packages/pkg.tgz!/inner.tgz",
				// a UNC path has no scheme at all
				"\\\\host\\share\\pkg.tgz"
			})
	void isAllowed_withUnsupportedScheme_deniesRegardlessOfConfiguredPrefixes(String theUrl) {
		PackageUrlAllowList allowList =
				PackageUrlAllowList.of(List.of(REMOTE_HOST), List.of(LOCAL_DIR, CLASSPATH_DIR));

		assertThat(allowList.isAllowed(theUrl)).isFalse();
	}

	// ------------------------------------------- unsupported prefix entries

	/**
	 * An allow-list entry naming a scheme we do not support must not grant access to anything — least of
	 * all to the same host or path reached over a scheme we do support.
	 */
	@Test
	void isAllowed_withUnsupportedSchemeInPrefix_grantsNothing() {
		PackageUrlAllowList allowList =
				PackageUrlAllowList.of(List.of(new AllowedUrlPrefix("ftp://packages.fhir.org", false)),
					List.of(new AllowedUrlPrefix("netdoc:/opt/packages", false)));

		// not by way of the unsupported scheme itself
		assertThat(allowList.isAllowed("ftp://packages.fhir.org/pkg.tgz")).isFalse();
		assertThat(allowList.isAllowed("netdoc:/opt/packages/pkg.tgz")).isFalse();
		// and not by way of a supported scheme pointing at the same place
		assertThat(allowList.isAllowed("https://packages.fhir.org/pkg.tgz")).isFalse();
		assertThat(allowList.isAllowed("file:/opt/packages/pkg.tgz")).isFalse();
		assertThat(allowList.isAllowed("classpath:/opt/packages/pkg.tgz")).isFalse();
	}

	@Test
	void isAllowed_withJarPrefixWrappingPermittedPath_grantsNothing() {
		// "jar:file:/opt/packages" contains a permitted path but must not be read as one
		PackageUrlAllowList allowList = PackageUrlAllowList.of(List.of(), List.of(
			new AllowedUrlPrefix("jar:file:/opt/packages", false)));

		assertThat(allowList.isAllowed("file:/opt/packages/pkg.tgz")).isFalse();
		assertThat(allowList.isAllowed("jar:file:/opt/packages/pkg.tgz")).isFalse();
	}

	@Test
	void isAllowed_withBlankPrefixEntry_grantsNothingOutsideIt() {
		// blank entries are filtered upstream, but this class must not depend on that
		PackageUrlAllowList allowList = PackageUrlAllowList.of(List.of(new AllowedUrlPrefix("  ", false)),
			List.of(new AllowedUrlPrefix("  ", false)));

		assertThat(allowList.isAllowed("file:/opt/packages/pkg.tgz")).isFalse();
		assertThat(allowList.isAllowed("https://packages.fhir.org/pkg.tgz")).isFalse();
	}

	@Test
	void isAllowed_withWildcardAlongsideUnsupportedEntry_stillAllowsSupportedSchemes() {
		// the wildcard is absolute; a junk entry beside it neither helps nor hinders
		PackageUrlAllowList allowList =
				PackageUrlAllowList.of(
					List.of(AllowedUrlPrefix.all(), new AllowedUrlPrefix("ftp://x", false)),
					List.of(AllowedUrlPrefix.all(), new AllowedUrlPrefix("ftp://x", false)));

		assertThat(allowList.isAllowed("file:/anywhere/pkg.tgz")).isTrue();
		assertThat(allowList.isAllowed("https://anywhere.example.com/pkg.tgz")).isTrue();
		assertThat(allowList.isAllowed("ftp://x/pkg.tgz")).isFalse();
	}

	/**
	 * A bare filesystem root as a local prefix permits every file on the host, which is a local wildcard
	 * in all but name. Pinned here so the behaviour is deliberate, and so the config-time validation can
	 * decide whether it warrants the same warning as {@code *}.
	 */
	@Test
	void isAllowed_withFilesystemRootAsPrefix_permitsAnyFile() {
		PackageUrlAllowList allowList = PackageUrlAllowList.of(List.of(), List.of(
			new AllowedUrlPrefix("/", false)));

		assertThat(allowList.isAllowed("file:/etc/shadow")).isTrue();
	}

	// ------------------------------------------------------------- malformed

	@ParameterizedTest
	@ValueSource(
			strings = {
				// recognised scheme but nothing to resolve; must be rejected, not throw
				"file:",
				"file:://opt/packages/pkg.tgz",
				"classpath:",
				"https:",
				// invalid percent-encoding
				"file:/opt/packages/%zz.tgz"
			})
	void isAllowed_withMalformedUrl_deniesWithoutThrowing(String theUrl) {
		assertThat(localAllowList().isAllowed(theUrl)).isFalse();
		assertThat(remoteAllowList().isAllowed(theUrl)).isFalse();
	}

	@Test
	void isAllowed_withBlankUrl_denies() {
		assertThat(localAllowList().isAllowed(null)).isFalse();
		assertThat(localAllowList().isAllowed("")).isFalse();
		assertThat(localAllowList().isAllowed("   ")).isFalse();
	}

	@Test
	public void isPrivateNetworkAllowedForHost_allowsAll_returnsTrue() {
		// setup
		String host = "pkg.com";
		PackageUrlAllowList allowList = PackageUrlAllowList.allowAll();

		// test
		boolean isPrivate = allowList.isPrivateNetworkAllowedForHost(host);

		// validate
		assertTrue(isPrivate);
	}

	@Test
	public void isPrivateNetworkAllowedForHost_hostNotInWhiteList_returnsFalse() {
		// setup
		String host = "pkg.com";
		PackageUrlAllowList allowList = PackageUrlAllowList.of(
			List.of(), List.of()
		);

		// test
		boolean isPrivate = allowList.isPrivateNetworkAllowedForHost(host);

		// validate
		assertFalse(isPrivate);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void isPrivateNetworkAllowedForHost_hostIsPublic_returnsFalse(boolean theIsPrivate) {
		// setup
		String host = "pkg.com";
		AllowedUrlPrefix publicRemote = new AllowedUrlPrefix("http://" + host, theIsPrivate);
		PackageUrlAllowList allowList = PackageUrlAllowList.of(
			List.of(publicRemote), List.of()
		);

		// test
		boolean isPrivate = allowList.isPrivateNetworkAllowedForHost(host);

		// validate
		assertEquals(theIsPrivate, isPrivate);
	}
}
