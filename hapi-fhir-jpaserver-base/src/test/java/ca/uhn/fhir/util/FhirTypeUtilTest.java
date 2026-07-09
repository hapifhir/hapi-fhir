package ca.uhn.fhir.util;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-sonnet-4-6
class FhirTypeUtilTest {

	static Stream<IBaseResource> canonicalResources() {
		return Stream.of(
				// DSTU3
				new org.hl7.fhir.dstu3.model.NamingSystem(),
				new org.hl7.fhir.dstu3.model.CodeSystem(),
				new org.hl7.fhir.dstu3.model.ValueSet(),
				new org.hl7.fhir.dstu3.model.StructureDefinition(),
				new org.hl7.fhir.dstu3.model.ConceptMap(),
				new org.hl7.fhir.dstu3.model.SearchParameter(),
				// R4
				new org.hl7.fhir.r4.model.NamingSystem(),
				new org.hl7.fhir.r4.model.CodeSystem(),
				new org.hl7.fhir.r4.model.ValueSet(),
				new org.hl7.fhir.r4.model.StructureDefinition(),
				new org.hl7.fhir.r4.model.ConceptMap(),
				new org.hl7.fhir.r4.model.SearchParameter(),
				// R5
				new org.hl7.fhir.r5.model.NamingSystem(),
				new org.hl7.fhir.r5.model.CodeSystem(),
				new org.hl7.fhir.r5.model.ValueSet(),
				new org.hl7.fhir.r5.model.StructureDefinition(),
				new org.hl7.fhir.r5.model.ConceptMap(),
				new org.hl7.fhir.r5.model.SearchParameter());
	}

	static Stream<IBaseResource> nonCanonicalResources() {
		return Stream.of(
				// DSTU3
				new org.hl7.fhir.dstu3.model.Patient(),
				new org.hl7.fhir.dstu3.model.Organization(),
				new org.hl7.fhir.dstu3.model.Device(),
				// R4
				new org.hl7.fhir.r4.model.Patient(),
				new org.hl7.fhir.r4.model.Organization(),
				new org.hl7.fhir.r4.model.Device(),
				// R5
				new org.hl7.fhir.r5.model.Patient(),
				new org.hl7.fhir.r5.model.Organization(),
				new org.hl7.fhir.r5.model.Device());
	}

	@ParameterizedTest
	@MethodSource("canonicalResources")
	void isCanonicalResource_canonical_returnsTrue(IBaseResource theResource) {
		assertThat(FhirTypeUtil.isCanonicalResource(theResource)).isTrue();
	}

	@ParameterizedTest
	@MethodSource("nonCanonicalResources")
	void isCanonicalResource_nonCanonical_returnsFalse(IBaseResource theResource) {
		assertThat(FhirTypeUtil.isCanonicalResource(theResource)).isFalse();
	}
}
