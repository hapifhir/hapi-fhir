package ca.uhn.fhir.context.support;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class CanonicalResourceIdentifierMatcherTest {

	private static final FhirContext FHIR_CONTEXT = FhirContext.forR4Cached();

	private static final String IDENTIFIER_SYSTEM = "urn:ietf:rfc:3986";
	private static final String IDENTIFIER_VALUE = "urn:oid:1.2.3.4";
	private static final String VERSION = "4.0.0";

	@Test
	void exactCodeSystemIdentifierReturnsResource() {
		CodeSystem codeSystem = newCodeSystem(
				"https://example.org/CodeSystem/example",
				VERSION);

		IBaseResource result =
				CanonicalResourceIdentifierMatcher.findMatch(
						FHIR_CONTEXT,
						List.of(codeSystem),
						newRequest("CodeSystem", VERSION));

		assertSame(codeSystem, result);
	}

	@Test
	void exactValueSetIdentifierReturnsResource() {
		ValueSet valueSet = newValueSet(
				"https://example.org/ValueSet/example",
				VERSION);

		IBaseResource result =
				CanonicalResourceIdentifierMatcher.findMatch(
						FHIR_CONTEXT,
						List.of(valueSet),
						newRequest("ValueSet", VERSION));

		assertSame(valueSet, result);
	}

	@Test
	void versionMismatchReturnsNull() {
		ValueSet valueSet = newValueSet(
				"https://example.org/ValueSet/example",
				VERSION);

		IBaseResource result =
				CanonicalResourceIdentifierMatcher.findMatch(
						FHIR_CONTEXT,
						List.of(valueSet),
						newRequest("ValueSet", "5.0.0"));

		assertNull(result);
	}

	@Test
	void resourceWithoutCanonicalUrlIsIgnored() {
		ValueSet valueSet = new ValueSet()
				.setVersion(VERSION);

		valueSet.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue(IDENTIFIER_VALUE);

		IBaseResource result =
				CanonicalResourceIdentifierMatcher.findMatch(
						FHIR_CONTEXT,
						List.of(valueSet),
						newRequest("ValueSet", VERSION));

		assertNull(result);
	}

	@Test
	void duplicateCanonicalTargetsAreCollapsed() {
		ValueSet first = newValueSet(
				"https://example.org/ValueSet/example",
				VERSION);

		ValueSet second = newValueSet(
				"https://example.org/ValueSet/example",
				VERSION);

		IBaseResource result =
				CanonicalResourceIdentifierMatcher.findMatch(
						FHIR_CONTEXT,
						List.of(first, second),
						newRequest("ValueSet", VERSION));

		assertSame(first, result);
	}

	@Test
	void differentCanonicalTargetsAreAmbiguous() {
		ValueSet first = newValueSet(
				"https://example.org/ValueSet/first",
				VERSION);

		ValueSet second = newValueSet(
				"https://example.org/ValueSet/second",
				VERSION);

		assertThatThrownBy(
						() -> CanonicalResourceIdentifierMatcher.findMatch(
								FHIR_CONTEXT,
								List.of(first, second),
								newRequest("ValueSet", VERSION)))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("Ambiguous ValueSet identifier")
				.hasMessageContaining("https://example.org/ValueSet/first|4.0.0")
				.hasMessageContaining("https://example.org/ValueSet/second|4.0.0");
	}

	@Test
	void nullCandidateCollectionReturnsNull() {
		IBaseResource result =
				CanonicalResourceIdentifierMatcher.findMatch(
						FHIR_CONTEXT,
						null,
						newRequest("ValueSet", VERSION));

		assertNull(result);
	}

	private static CanonicalResourceIdentifierRequest newRequest(
			String theResourceType,
			String theVersion) {

		return new CanonicalResourceIdentifierRequest(
				theResourceType,
				IDENTIFIER_SYSTEM,
				IDENTIFIER_VALUE,
				theVersion);
	}

	private static CodeSystem newCodeSystem(
			String theCanonicalUrl,
			String theVersion) {

		CodeSystem codeSystem = new CodeSystem()
				.setUrl(theCanonicalUrl)
				.setVersion(theVersion);

		codeSystem.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue(IDENTIFIER_VALUE);

		return codeSystem;
	}

	private static ValueSet newValueSet(
			String theCanonicalUrl,
			String theVersion) {

		ValueSet valueSet = new ValueSet()
				.setUrl(theCanonicalUrl)
				.setVersion(theVersion);

		valueSet.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue(IDENTIFIER_VALUE);

		return valueSet;
	}
}
