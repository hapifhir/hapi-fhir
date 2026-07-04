package ca.uhn.fhir.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-sonnet-4-6
class FhirTypeUtilTest {

	// Stub hierarchy simulating DSTU3 / R4 / R4B: ConcreteType extends MetadataResource
	abstract static class MetadataResource {}

	static class StubNamingSystem extends MetadataResource {}

	static class StubCodeSystem extends MetadataResource {}

	static class StubValueSet extends MetadataResource {}

	static class StubStructureDefinition extends MetadataResource {}

	static class StubConceptMap extends MetadataResource {}

	static class StubSearchParameter extends MetadataResource {}

	// Stub hierarchy simulating R5: CanonicalResource is the base; MetadataResource extends it
	abstract static class CanonicalResource {}

	abstract static class MetadataResourceR5 extends CanonicalResource {}

	static class StubR5StructureDefinition extends MetadataResourceR5 {}

	static class StubR5ValueSet extends MetadataResourceR5 {}

	// Non-canonical stubs — no MetadataResource/CanonicalResource in ancestry
	static class StubPatient {}

	static class StubOrganization {}

	static class StubDevice {}

	static Stream<Class<?>> canonicalResourceClasses() {
		return Stream.of(
				// DSTU3 / R4 / R4B style (extends MetadataResource)
				StubNamingSystem.class,
				StubCodeSystem.class,
				StubValueSet.class,
				StubStructureDefinition.class,
				StubConceptMap.class,
				StubSearchParameter.class,
				// R5 style (extends MetadataResource which extends CanonicalResource)
				StubR5StructureDefinition.class,
				StubR5ValueSet.class);
	}

	static Stream<Class<?>> nonCanonicalResourceClasses() {
		return Stream.of(StubPatient.class, StubOrganization.class, StubDevice.class);
	}

	@ParameterizedTest
	@MethodSource("canonicalResourceClasses")
	void isCanonicalResourceClass_canonical_returnsTrue(Class<?> theClass) {
		assertThat(FhirTypeUtil.isCanonicalResourceClass(theClass)).isTrue();
	}

	@ParameterizedTest
	@MethodSource("nonCanonicalResourceClasses")
	void isCanonicalResourceClass_nonCanonical_returnsFalse(Class<?> theClass) {
		assertThat(FhirTypeUtil.isCanonicalResourceClass(theClass)).isFalse();
	}
}
