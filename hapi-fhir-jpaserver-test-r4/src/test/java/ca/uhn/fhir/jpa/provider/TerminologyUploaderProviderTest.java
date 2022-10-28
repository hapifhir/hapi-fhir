package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.BaseTest;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminologyUploaderProviderTest extends BaseTest {
	@Test
	public void testCanonicalizeR3() {
		TerminologyUploaderProvider provider = new TerminologyUploaderProvider();
		provider.setContext(FhirContext.forDstu3Cached());

		org.hl7.fhir.dstu3.model.CodeSystem input = new org.hl7.fhir.dstu3.model.CodeSystem();
		input.addConcept().setCode("FOO").setDisplay("Foo");

		CodeSystem canonical = provider.canonicalizeCodeSystem(input);

		assertEquals("FOO", canonical.getConcept().get(0).getCode());
	}

	@Test
	public void testCanonicalizeR4() {
		TerminologyUploaderProvider provider = new TerminologyUploaderProvider();
		provider.setContext(FhirContext.forR4Cached());

		org.hl7.fhir.r4.model.CodeSystem input = new org.hl7.fhir.r4.model.CodeSystem();
		input.addConcept().setCode("FOO").setDisplay("Foo");

		CodeSystem canonical = provider.canonicalizeCodeSystem(input);

		assertEquals("FOO", canonical.getConcept().get(0).getCode());
	}

	@Test
	public void testCanonicalizeR5() {
		TerminologyUploaderProvider provider = new TerminologyUploaderProvider();
		provider.setContext(FhirContext.forR5Cached());

		org.hl7.fhir.r5.model.CodeSystem input = new org.hl7.fhir.r5.model.CodeSystem();
		input.addConcept().setCode("FOO").setDisplay("Foo");

		CodeSystem canonical = provider.canonicalizeCodeSystem(input);

		assertEquals("FOO", canonical.getConcept().get(0).getCode());
	}

	@Test
	public void testCanonicalizeR5_WrongType() {
		TerminologyUploaderProvider provider = new TerminologyUploaderProvider();
		provider.setContext(FhirContext.forR5Cached());

		org.hl7.fhir.r5.model.Patient input = new org.hl7.fhir.r5.model.Patient();

		try {
			provider.canonicalizeCodeSystem(input);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1769) + "Resource 'Patient' is not a CodeSystem", e.getMessage());
		}

	}

}
