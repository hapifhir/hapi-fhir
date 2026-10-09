package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The default {@link IValidationSupport#fetchCodeSystem(String, String)}, for a module which implements only the
 * single-canonical form and keys its code systems by URL alone.
 */
// Created by Claude Opus 5.5
class IValidationSupportFetchCodeSystemTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String SYSTEM = "http://example.org/cs";

	@Test
	void fetchCodeSystem_moduleKeyedByUrlAlone_returnsItsCopyOnlyForTheVersionItKnows() {
		UrlKeyedValidationSupport support = new UrlKeyedValidationSupport();

		IBaseResource knownVersion = support.fetchCodeSystem(SYSTEM, "1.0");
		IBaseResource otherVersion = support.fetchCodeSystem(SYSTEM, "2.0");
		IBaseResource noVersion = support.fetchCodeSystem(SYSTEM, null);

		assertThat(knownVersion).isSameAs(support.myCodeSystem);
		assertThat(otherVersion).isNull();
		assertThat(noVersion).isSameAs(support.myCodeSystem);
		assertThat(support.myRequestedCanonicals)
				.containsExactly(SYSTEM + "|1.0", SYSTEM, SYSTEM + "|2.0", SYSTEM, SYSTEM);
	}

	private static class UrlKeyedValidationSupport implements IValidationSupport {
		private final CodeSystem myCodeSystem = new CodeSystem().setUrl(SYSTEM).setVersion("1.0");
		private final List<String> myRequestedCanonicals = new ArrayList<>();

		@Override
		public FhirContext getFhirContext() {
			return ourCtx;
		}

		@Override
		public IBaseResource fetchCodeSystem(String theSystem) {
			myRequestedCanonicals.add(theSystem);
			return SYSTEM.equals(theSystem) ? myCodeSystem : null;
		}
	}
}
