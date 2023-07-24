package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RDFParserR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();



	@Test
	public void testEncodeToString_PrimitiveDataType() {
		DecimalType object = new DecimalType("123.456000");
		String expected = "123.456000";
		String actual = ourCtx.newRDFParser().encodeToString(object);
		assertEquals(expected, actual);
	}

	@Test
	public void testEncodeToString_Resource() {
		Patient p = new Patient();
		p.setId("Patient/123");
		p.setActive(true);
		String expected = """
			@prefix fhir: <http://hl7.org/fhir/> .
			@prefix rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
			@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
			@prefix sct:  <http://snomed.info/id#> .
			@prefix xsd:  <http://www.w3.org/2001/XMLSchema#> .
			   
			<http://hl7.org/fhir/Patient/123>
			        rdf:type             fhir:Patient ;
			        fhir:Patient.active  [ fhir:value  true ] ;
			        fhir:Resource.id     [ fhir:value  "123" ] ;
			        fhir:nodeRole        fhir:treeRoot .
			""";

		String actual = ourCtx.newRDFParser().encodeToString(p);
		assertEquals(expected, actual);
	}

	@Test
	public void testEncodeToString_GeneralPurposeDataType() {
		HumanName name = new HumanName();
		name.setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		name.addExtension("http://foo", new StringType("bar"));

		assertEquals("HAPI-2363: This parser does not support encoding non-resource values",
			assertThrows(InternalErrorException.class, ()->ourCtx.newRDFParser().encodeToString(name)).getMessage());
	}

	@Test
	public void testEncodeToString_BackboneElement() {
		Patient.PatientCommunicationComponent communication = new Patient().addCommunication();
		communication.setPreferred(true);
		communication.getLanguage().setText("English");

		assertEquals("HAPI-2363: This parser does not support encoding non-resource values",
			assertThrows(InternalErrorException.class, ()->ourCtx.newRDFParser().encodeToString(communication)).getMessage());
	}

}
