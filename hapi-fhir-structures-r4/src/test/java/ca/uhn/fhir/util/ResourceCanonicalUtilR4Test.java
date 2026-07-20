package ca.uhn.fhir.util;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-sonnet-4-6
class ResourceCanonicalUtilR4Test {

	static Stream<IBaseResource> canonicalResources() {
		return Stream.of(
				new NamingSystem(),
				new CodeSystem(),
				new ValueSet(),
				new StructureDefinition(),
				new ConceptMap(),
				new SearchParameter());
	}

	static Stream<IBaseResource> nonCanonicalResources() {
		return Stream.of(new Patient(), new Organization(), new Device());
	}

	@ParameterizedTest
	@MethodSource("canonicalResources")
	void isCanonicalResource_canonical_returnsTrue(IBaseResource theResource) {
		assertThat(ResourceCanonicalUtil.isCanonicalResource(theResource)).isTrue();
	}

	@ParameterizedTest
	@MethodSource("nonCanonicalResources")
	void isCanonicalResource_nonCanonical_returnsFalse(IBaseResource theResource) {
		assertThat(ResourceCanonicalUtil.isCanonicalResource(theResource)).isFalse();
	}
}
