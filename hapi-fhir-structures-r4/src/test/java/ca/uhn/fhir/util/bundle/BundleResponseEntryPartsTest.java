package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BundleResponseEntryPartsTest {

	public static final String ENTRY_JSON = """
   {
     "fullUrl": "https://example.org/fhir/Patient/12423",
     "resource": { "resourceType": "Patient", "active": true},
     "response": {
       "status": "201 Created",
       "location": "Patient/12423/_history/1",
       "etag": "W/\\"1\\"",
       "lastModified": "2014-08-18T01:43:33Z",
       "outcome": { "resourceType": "OperationOutcome" }
     }
   }
   """;
	FhirContext myFhirContext = FhirContext.forR4Cached();
	IParser myParser = myFhirContext.newJsonParser().setPrettyPrint(true);

	@Test
	void testExtractor() {
		// given
		Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();

		myParser.parseInto(ENTRY_JSON, entry);

		// when
		BundleResponseEntryParts parts = BundleResponseEntryParts.buildPartsExtractor(myFhirContext).apply(entry);

		// then
		assertThat(parts.fullUrl()).isEqualTo("https://example.org/fhir/Patient/12423");
		assertThat(parts.resource()).isNotNull();
		assertThat(parts.resource().fhirType()).isEqualTo("Patient");
		assertThat(parts.responseOutcome()).isNotNull();
		assertThat(parts.responseOutcome().fhirType()).isEqualTo("OperationOutcome");
		assertThat(parts.responseStatus()).isEqualTo("201 Created");
		assertThat(parts.responseLocation()).isEqualTo("Patient/12423/_history/1");
		assertThat(parts.responseEtag()).isEqualTo("W/\"1\"");
		assertThat(parts.responseLastModified().getValueAsString()).isEqualTo("2014-08-18T01:43:33Z");
	}

	@Test
	void testBuilder() {
		Patient patient = new Patient();
		patient.setActive(true);
		OperationOutcome outcome = new OperationOutcome();
		BundleResponseEntryParts parts = new BundleResponseEntryParts(
			"https://example.org/fhir/Patient/12423",
			patient,
			"201 Created",
			"Patient/12423/_history/1",
			"W/\"1\"",
			new InstantType("2014-08-18T01:43:33Z"),
			 outcome // responseOutcome
		);
		Function<BundleResponseEntryParts, IBase> builder = BundleResponseEntryParts.builder(myFhirContext);

	    // when
		IBase element = builder.apply(parts);

	    // then
		assertThat(element).isNotNull();
		FhirTerser terser = myFhirContext.newTerser();
		assertEquals(patient, terser.getSingleValueOrNull(element, "resource"));
		assertEquals("https://example.org/fhir/Patient/12423", terser.getSinglePrimitiveValueOrNull(element, "fullUrl"));
		assertEquals("201 Created", terser.getSinglePrimitiveValueOrNull(element, "response.status"));
		assertEquals("Patient/12423/_history/1", terser.getSinglePrimitiveValueOrNull(element, "response.location"));
		assertEquals("W/\"1\"", terser.getSinglePrimitiveValueOrNull(element, "response.etag"));
		assertEquals("2014-08-18T01:43:33Z", terser.getSinglePrimitiveValueOrNull(element, "response.lastModified"));
		assertEquals(outcome, terser.getSingleValueOrNull(element, "response.outcome"));
	}



	@Test
	void testBuilder_nullsProducesEmptyElement() {
		BundleResponseEntryParts parts = new BundleResponseEntryParts(null,null,null,null,null,null,null);
		Function<BundleResponseEntryParts, IBase> builder = BundleResponseEntryParts.builder(myFhirContext);

		// when
		IBase element = builder.apply(parts);

		// then
		assertThat(element).isNotNull();
		FhirTerser terser = myFhirContext.newTerser();
		assertNull(terser.getSingleValueOrNull(element, "resource"));
		assertNull(terser.getSinglePrimitiveValueOrNull(element, "fullUrl"));
		assertNull(terser.getSinglePrimitiveValueOrNull(element, "response"));
		assertNull(terser.getSinglePrimitiveValueOrNull(element, "response.status"));
		assertNull(terser.getSinglePrimitiveValueOrNull(element, "response.location"));
		assertNull(terser.getSinglePrimitiveValueOrNull(element, "response.etag"));
		assertNull(terser.getSinglePrimitiveValueOrNull(element, "response.lastModified"));
		assertNull(terser.getSingleValueOrNull(element, "response.outcome"));
	}

}
