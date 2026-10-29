package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import jakarta.annotation.Nullable;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

// Created by Claude Opus 5.5
class IValueSetConceptAccumulatorTest {

	private static final String CS_URL = "http://example.org/fhir/CodeSystem/cs";

	@Test
	void excludeConcept_implementationOverridesOnlyTheDeprecatedForm_receivesTheVersionPackedIntoTheSystem() {
		LegacyAccumulator accumulator = new LegacyAccumulator();

		accumulator.excludeConcept(CS_URL, "1.0.0", "A");
		accumulator.excludeConcept(CS_URL, null, "B");

		assertThat(accumulator.myExcludedSystemsAndCodes).containsExactly(CS_URL + "|1.0.0 A", CS_URL + " B");
	}

	@SuppressWarnings("deprecation")
	@Test
	void excludeConcept_deprecatedFormOnInMemoryAccumulator_removesOnlyTheVersionItNames() {
		ValueSetExpansionComponentWithConceptAccumulator accumulator =
				new ValueSetExpansionComponentWithConceptAccumulator(FhirContext.forR4Cached(), 100, false);
		accumulator.includeConceptWithDesignations(CS_URL, "A", null, null, null, null, "1.0.0");
		accumulator.includeConceptWithDesignations(CS_URL, "B", null, null, null, null, "1.0.0");

		accumulator.excludeConcept(CS_URL + "|1.0.0", "A");
		accumulator.excludeConcept(CS_URL + "|2.0.0", "B");

		assertThat(accumulator.getContains())
				.extracting(
						ValueSet.ValueSetExpansionContainsComponent::getSystem,
						ValueSet.ValueSetExpansionContainsComponent::getVersion,
						ValueSet.ValueSetExpansionContainsComponent::getCode)
				.containsExactly(tuple(CS_URL, "1.0.0", "B"));
	}

	@Test
	void excludeConcept_versionedExcludeOfCodeAddedWithoutVersion_removesItSoItCanBeAddedAgain() {
		ValueSetExpansionComponentWithConceptAccumulator accumulator =
				new ValueSetExpansionComponentWithConceptAccumulator(FhirContext.forR4Cached(), 100, false);
		accumulator.includeConceptWithDesignations(CS_URL, "A", null, null, null, null, null);

		accumulator.excludeConcept(CS_URL, "1.0.0", "A");
		ValueSet.ValueSetExpansionContainsComponent[] afterExclude =
				accumulator.getContains().toArray(new ValueSet.ValueSetExpansionContainsComponent[0]);
		accumulator.includeConceptWithDesignations(CS_URL, "A", null, null, null, null, null);

		assertThat(afterExclude).isEmpty();
		assertThat(accumulator.getContains())
				.extracting(ValueSet.ValueSetExpansionContainsComponent::getCode)
				.containsExactly("A");
	}

	/**
	 * An implementation written before the version-aware {@code excludeConcept} existed.
	 */
	private static class LegacyAccumulator implements IValueSetConceptAccumulator {
		private final List<String> myExcludedSystemsAndCodes = new ArrayList<>();

		@Override
		public void addMessage(String theMessage) {
			// not used
		}

		@Override
		public void includeConceptWithDesignations(
				String theSystem,
				String theCode,
				String theDisplay,
				@Nullable Collection<TermConceptDesignation> theDesignations,
				Long theSourceConceptPid,
				String theSourceConceptDirectParentPids,
				@Nullable String theSystemVersion) {
			// not used
		}

		@SuppressWarnings("deprecation")
		@Override
		public void excludeConcept(String theSystem, String theCode) {
			myExcludedSystemsAndCodes.add(theSystem + " " + theCode);
		}
	}
}
