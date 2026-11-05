package org.hl7.fhir.r4b.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.r4b.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4b.model.ValueSet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class HapiWorkerContextR4BTest {
	private static final FhirContext ourCtx = FhirContext.forR4BCached();

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"1.0"})
	void fetchResource_withOrWithoutVersion_returnsTheResource(String theVersion) {
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addValueSet(new ValueSet().setUrl("http://example.org/vs").setVersion("1.0"));
		HapiWorkerContext workerCtx = new HapiWorkerContext(ourCtx, prePopulated);

		ValueSet valueSet = workerCtx.fetchResource(ValueSet.class, "http://example.org/vs", theVersion);

		assertThat(valueSet).isNotNull();
	}
}
