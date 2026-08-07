package ca.uhn.fhir.jpa.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Scheme extraction is the first gate on every package URL: it decides whether a URL is checked
 * against the local or the remote allow-list, and rejects anything that is neither. These cases pin
 * that behaviour, in particular that the scheme comparison is case-insensitive per RFC 3986 and that
 * unrecognised or absent schemes are rejected rather than defaulted to either bucket.
 */
// Created by claude-opus-5
class PackageUrlSchemeTest {

	static Stream<Arguments> recognizedSchemes() {
		return Stream.of(
				Arguments.of("file:/opt/packages/pkg.tgz", PackageUrlScheme.FILE),
				Arguments.of("file:///opt/packages/pkg.tgz", PackageUrlScheme.FILE),
				Arguments.of("classpath:/package-loading/pkg.tgz", PackageUrlScheme.CLASSPATH),
				Arguments.of("classpath://package-loading/pkg.tgz", PackageUrlScheme.CLASSPATH),
				Arguments.of("http://packages.fhir.org/pkg.tgz", PackageUrlScheme.HTTP),
				Arguments.of("https://packages.fhir.org/pkg.tgz", PackageUrlScheme.HTTPS),
				// schemes are case-insensitive per RFC 3986
				Arguments.of("FILE:/opt/packages/pkg.tgz", PackageUrlScheme.FILE),
				Arguments.of("File:/opt/packages/pkg.tgz", PackageUrlScheme.FILE),
				Arguments.of("CLASSPATH:/package-loading/pkg.tgz", PackageUrlScheme.CLASSPATH),
				Arguments.of("HTTPS://packages.fhir.org/pkg.tgz", PackageUrlScheme.HTTPS),
				// config comes from a multiline text area and packageUrl from request JSON
				Arguments.of("  file:/opt/packages/pkg.tgz", PackageUrlScheme.FILE),
				Arguments.of("https://packages.fhir.org/pkg.tgz\n", PackageUrlScheme.HTTPS));
	}

	@ParameterizedTest
	@MethodSource("recognizedSchemes")
	void parseScheme_withRecognizedScheme_returnsScheme(String theUrl, PackageUrlScheme theExpected) {
		assertThat(PackageUrlScheme.parseScheme(theUrl)).isEqualTo(theExpected);
	}

	static Stream<Arguments> unrecognizedInputs() {
		return Stream.of(
				// schemes we deliberately do not support; these must not fall into either bucket
				Arguments.of("jar:file:/opt/packages/pkg.tgz"),
				Arguments.of("netdoc:/etc/shadow"),
				Arguments.of("ftp://packages.fhir.org/pkg.tgz"),
				Arguments.of("data:text/plain;base64,SGVsbG8="),
				Arguments.of("gopher://packages.fhir.org/pkg.tgz"),
				// no scheme at all
				Arguments.of("/opt/packages/pkg.tgz"),
				Arguments.of("packages.fhir.org/pkg.tgz"),
				Arguments.of("./pkg.tgz"),
				// a Windows drive letter parses as a one-character scheme, which is not one we accept
				Arguments.of("C:\\packages\\pkg.tgz"),
				// malformed scheme tokens
				Arguments.of(":/opt/packages/pkg.tgz"),
				Arguments.of("1file:/opt/packages/pkg.tgz"),
				Arguments.of("fi le:/opt/packages/pkg.tgz"));
	}

	@ParameterizedTest
	@MethodSource("unrecognizedInputs")
	void parseScheme_withUnrecognizedOrAbsentScheme_returnsNull(String theUrl) {
		assertThat(PackageUrlScheme.parseScheme(theUrl)).isNull();
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"   ", "\t", "\n"})
	void parseScheme_withBlankUrl_returnsNull(String theUrl) {
		assertThat(PackageUrlScheme.parseScheme(theUrl)).isNull();
	}
}
