package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DaoResourceLinkResolverTest {

	@ParameterizedTest
	@MethodSource("getLinkResolutionTestCases")
	void testLinkResolution(LinkResolutionTestCase theTestCase) {
		DaoResourceLinkResolver<JpaPid> resolver = new DaoResourceLinkResolver<>();
		List<CanonicalIdentifier> canonicalIdentifier = resolver.extractIdentifierFromUrl(theTestCase.url);
		assertEquals(theTestCase.expectedMatches, canonicalIdentifier);
	}

	static List<LinkResolutionTestCase> getLinkResolutionTestCases() {
		return List.of(

			// One identifier
			new LinkResolutionTestCase(
				"Patient?identifier=http://hapifhir.io/fhir/namingsystem/my_id|123456",
				List.of(new CanonicalIdentifier("http://hapifhir.io/fhir/namingsystem/my_id", "123456"))
			),

			// Multiple identifiers
			new LinkResolutionTestCase(
				"Patient?_tag:not=http://hapifhir.io/fhir/namingsystem/mdm-record-status|GOLDEn_rEcorD&identifier=https://www.my.org/identifiers/memBER|123456",
				List.of(new CanonicalIdentifier("https://www.my.org/identifiers/memBER", "123456"))
			),

			// No identifier
			new LinkResolutionTestCase(
				"Patient?name=smith",
				List.of()
			),

			// Identifier plus other parameters: Should be treated as no identifiers
			// since there are other params in there too
			new LinkResolutionTestCase(
				"Patient?identifier=http://foo|123&name=smith",
				List.of()
			),

			// Identifier with OR values
			new LinkResolutionTestCase(
				"Patient?identifier=http://foo|123,http://foo|456",
				List.of()
			),

			// Identifier with modifier
			new LinkResolutionTestCase(
				"Patient?identifier:not=http://foo|123",
				List.of()
			),

			// Identifier with modifier
			new LinkResolutionTestCase(
				"Patient?identifier:missing=true",
				List.of()
			)

		);
	}

	private record LinkResolutionTestCase(String url, List<CanonicalIdentifier> expectedMatches){}

}


