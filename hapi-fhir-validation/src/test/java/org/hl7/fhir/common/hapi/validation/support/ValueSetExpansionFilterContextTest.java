package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Enumerations.FilterOperator;
import org.hl7.fhir.r5.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueSetExpansionFilterContextTest {

	/**
	 * Helper: build a flat CodeSystem with the given codes.
	 */
	private static CodeSystem flatCodeSystem(boolean caseSensitive, String... codes) {
		CodeSystem cs = new CodeSystem()
			.setUrl("http://example.org/flat")
			.setCaseSensitive(caseSensitive);
		for (String c : codes) {
			cs.addConcept().setCode(c);
		}
		return cs;
	}

	/**
	 * Helper: build a simple hierarchy P → C1 → C2 and sibling P → C3.
	 */
	private static CodeSystem hierarchicalCS(boolean caseSensitive) {
		CodeSystem cs = new CodeSystem()
			.setUrl("http://example.org/hier")
			.setCaseSensitive(caseSensitive);
		var p = cs.addConcept().setCode("P");
		p.addConcept().setCode("C1").addConcept().setCode("C2");
		p.addConcept().setCode("C3");
		return cs;
	}

	public static final String CONCEPT_PROPERTIES_PARENT =
		"http://hl7.org/fhir/concept-properties#parent";

	/**
	 * Helper: build the same P → C1 → C2, P → C3 hierarchy as {@link #hierarchicalCS(boolean)}, but
	 * expressed FLAT (all concepts at top level) with the hierarchy encoded via a {@code parent}
	 * concept-property whose definition uses the canonical concept-properties URI.
	 */
	private static CodeSystem flatCSWithParentProperty(boolean caseSensitive) {
		CodeSystem cs = new CodeSystem()
			.setUrl("http://example.org/flatparent")
			.setCaseSensitive(caseSensitive);
		cs.addProperty().setCode("parent").setUri(CONCEPT_PROPERTIES_PARENT).setType(CodeSystem.PropertyType.CODE);
		cs.addConcept().setCode("P");
		cs.addConcept().setCode("C1").addProperty().setCode("parent").setValue(new CodeType("P"));
		cs.addConcept().setCode("C2").addProperty().setCode("parent").setValue(new CodeType("C1"));
		cs.addConcept().setCode("C3").addProperty().setCode("parent").setValue(new CodeType("P"));
		return cs;
	}

	/**
	 * Like {@link #flatCSWithParentProperty(boolean)} but the {@code parent} property definition uses a
	 * NON-canonical URI, so the hierarchy must be ignored (every concept is treated as a root/leaf).
	 */
	private static CodeSystem flatCSWithNonCanonicalParentProperty(boolean caseSensitive) {
		CodeSystem cs = new CodeSystem()
			.setUrl("http://example.org/flatparent-noncanonical")
			.setCaseSensitive(caseSensitive);
		cs.addProperty().setCode("parent").setUri("http://example.org/custom#parent").setType(CodeSystem.PropertyType.CODE);
		cs.addConcept().setCode("P");
		cs.addConcept().setCode("C1").addProperty().setCode("parent").setValue(new CodeType("P"));
		cs.addConcept().setCode("C2").addProperty().setCode("parent").setValue(new CodeType("C1"));
		cs.addConcept().setCode("C3").addProperty().setCode("parent").setValue(new CodeType("P"));
		return cs;
	}

	/**
	 * Invoke isFiltered() for a single filter + concept.
	 */
	private static boolean isFiltered(
		CodeSystem cs,
		FilterOperator op,
		String filterValue,
		FhirVersionIndependentConcept testConcept) {
		ValueSet.ConceptSetFilterComponent filter = new ValueSet.ConceptSetFilterComponent()
			.setOp(op).setValue(filterValue);
		var ctx = new ValueSetExpansionFilterContext(cs, List.of(filter));
		return ctx.isFiltered(testConcept);
	}

	/**
	 * Like isFiltered(...) but also sets filter.property.
	 */
	private static boolean isFilteredWithProperty(
		CodeSystem cs,
		String property,
		FilterOperator op,
		String filterValue,
		FhirVersionIndependentConcept testConcept
	) {
		ValueSet.ConceptSetFilterComponent filter = new ValueSet.ConceptSetFilterComponent()
			.setProperty(property)
			.setOp(op)
			.setValue(filterValue);
		var ctx = new ValueSetExpansionFilterContext(cs, List.of(filter));
		return ctx.isFiltered(testConcept);
	}

	/**
	 * Build a flat CodeSystem and assign displays to each code.
	 *
	 * @param caseSensitive    whether the CS should be case‐sensitive
	 * @param codeDisplayPairs alternating code, display, code, display, …
	 */
	private static CodeSystem flatCodeSystemWithDisplay(boolean caseSensitive, String... codeDisplayPairs) {
		// Extract just the codes for the flat system
		String[] codes = new String[codeDisplayPairs.length / 2];
		for (int i = 0; i < codeDisplayPairs.length; i += 2) {
			codes[i / 2] = codeDisplayPairs[i];
		}
		CodeSystem cs = flatCodeSystem(caseSensitive, codes);
		// Assign each display
		for (int i = 0; i < codeDisplayPairs.length; i += 2) {
			String code = codeDisplayPairs[i];
			String display = codeDisplayPairs[i + 1];
			cs.getConcept().stream()
				.filter(d -> d.getCode().equals(code))
				.findFirst()
				.ifPresent(d -> d.setDisplay(display));
		}
		return cs;
	}

	@ParameterizedTest(name = "[equal-display] cs={0}, filter={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		// caseSensitive, displayFilter,    testCode, expectedIsFiltered
		"false, Hello,              X,       false",  // matches Hello
		"false, hello,              X,       false",  // ignore case
		"false, Hello,              Y,       true",   // Y’s display != Hello
		"true,  hello,              X,       true"    // case-sensitive → no match
	})
	void testEqualOnDisplay(
		boolean caseSensitive,
		String displayFilter,
		String testCode,
		boolean expectedIsFiltered
	) {
		// Build CS with code X,Y and set displays
		CodeSystem cs = flatCodeSystemWithDisplay(caseSensitive,
			"X", "Hello",
			"Y", "World");

		boolean actual = isFilteredWithProperty(
			cs,
			"display",
			FilterOperator.EQUAL,
			displayFilter,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("EQUAL[display=%s] on code '%s' (caseSensitive=%b)",
				displayFilter, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[in-display] cs={0}, values={1}, code={2} ⇒ filtered={3}")
	@CsvSource(
		delimiter = '|',
		value = {
			// caseSensitive | values         | testCode | expectedIsFiltered
			"false          | Hello,World    | X        | false", // Hello
			"false          | Hello,World    | Y        | false", // World
			"false          | Hello,World    | Z        | true",  // no such concept
			"true           | hello,world    | X        | true"   // case-sensitive
		})
	void testInOnDisplay(
		boolean caseSensitive,
		String csvValues,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = flatCodeSystemWithDisplay(caseSensitive,
			"X", "Hello",
			"Y", "World");

		boolean actual = isFilteredWithProperty(
			cs,
			"display",
			FilterOperator.IN,
			csvValues,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("IN[display in %s] on code '%s' (caseSensitive=%b)",
				csvValues, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[notin-display] cs={0}, values={1}, code={2} ⇒ filtered={3}")
	@CsvSource(
		delimiter = '|',
		value = {
			// caseSensitive | values       | testCode | expectedIsFiltered
			"false          | Hello,World  | X        | true",   // Hello → excluded
			"false          | Hello,World  | Y        | true",   // World → excluded
			"false          | Hello,World  | Z        | false",  // Z not in display → included
			"true           | hello,world  | X        | false"   // case-sensitive → Hello not matched → included
		}
	)
	void testNotInOnDisplay(
		boolean caseSensitive,
		String csvValues,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = flatCodeSystemWithDisplay(caseSensitive,
			"X", "Hello",
			"Y", "World");

		boolean actual = isFilteredWithProperty(
			cs,
			"display",
			FilterOperator.NOTIN,
			csvValues,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("NOTIN[display not in %s] on code '%s' (caseSensitive=%b)",
				csvValues, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[regex-display] cs={0}, pattern={1}, code={2} ⇒ filtered={3}")
	@CsvSource(
		delimiter = '|',
		value = {
			// caseSensitive | pattern      | testCode | expectedIsFiltered
			"false          | '^H.*'        | X        | false", // Hello
			"false          | '^H.*'        | Y        | true",  // World
			"true           | '^h.*'        | X        | true",  // case-sensitive
			"true           | '[A-Z]orld$'  | Y        | false"  // matches World
		})
	void testRegexOnDisplay(
		boolean caseSensitive,
		String pattern,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = flatCodeSystem(caseSensitive, "X", "Y");
		cs.getConcept().get(0).setDisplay("Hello");
		cs.getConcept().get(1).setDisplay("World");

		boolean actual = isFilteredWithProperty(
			cs,
			"display",
			FilterOperator.REGEX,
			pattern,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("REGEX[display ~ %s] on code '%s' (caseSensitive=%b)",
				pattern, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@Test
	void unsupportedPropertyThrows() {
		// An unknown property is not something this in-memory engine can evaluate. Rather than silently
		// producing an (incorrect) empty expansion, it must signal that it cannot apply the filter.
		CodeSystem cs = flatCodeSystem(false, "A", "B");
		ValueSet.ConceptSetFilterComponent f = new ValueSet.ConceptSetFilterComponent()
			.setProperty("severity")   // not code/display/any known standard property
			.setOp(FilterOperator.EQUAL)
			.setValue("A");
		ValueSetExpansionFilterContext ctx = new ValueSetExpansionFilterContext(cs, List.of(f));
		FhirVersionIndependentConcept concept = new FhirVersionIndependentConcept(cs.getUrl(), "A");

		assertThatThrownBy(() -> ctx.isFiltered(concept))
			.isInstanceOf(ValueSetExpansionFilterContext.UnsupportedFilterException.class)
			.hasMessageContaining("severity");
	}

	@ParameterizedTest(name = "[unsupported-op] property={0}, op={1}")
	@CsvSource({
		// The hierarchical / boolean properties only support the 'exists' operator in-memory.
		"child, ISA",
		"parent, DESCENDENTOF",
		"inactive, EQUAL",
		"notSelectable, IN",
	})
	void unsupportedOperatorOnStandardPropertyThrows(String property, FilterOperator op) {
		CodeSystem cs = hierarchicalCS(true);
		ValueSet.ConceptSetFilterComponent f = new ValueSet.ConceptSetFilterComponent()
			.setProperty(property)
			.setOp(op)
			.setValue("P");
		ValueSetExpansionFilterContext ctx = new ValueSetExpansionFilterContext(cs, List.of(f));
		FhirVersionIndependentConcept concept = new FhirVersionIndependentConcept(cs.getUrl(), "P");

		assertThatThrownBy(() -> ctx.isFiltered(concept))
			.isInstanceOf(ValueSetExpansionFilterContext.UnsupportedFilterException.class)
			.hasMessageContaining(property);
	}


	@ParameterizedTest(name = "[equal] cs={0}, filter={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		// caseSensitive, filterValue, testCode, expectedIsFiltered
		"false, A, A, false",
		"false, A, a, false",
		"false, A, B, true",
		"true,  A, A, false",
		"true,  A, a, true"
	})
	void testEqualFilterBehavior(
		boolean caseSensitive,
		String filterValue,
		String testCode,
		boolean expectedIsFiltered) {

		CodeSystem cs = flatCodeSystem(caseSensitive, "A", "B");
		boolean actual = isFiltered(
			cs,
			FilterOperator.EQUAL,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("EQUAL[%s] on code %s (caseSensitive=%b)", filterValue, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[isa] caseSensitive={0}, filter={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		// include parent, all descendants; exclude unknown and off-branch
		"false, P,   P,    false",
		"false, P,   C1,   false",
		"false, P,   C2,   false",
		"false, P,   C3,   false",
		"false, P,   XXX,  true",
		// is-a C1 should include C1, C2 but exclude P and C3
		"false, C1,  C1,   false",
		"false, C1,  C2,   false",
		"false, C1,  P,    true",
		"false, C1,  C3,   true",
		// case-sensitive: wrong‐case filter or code → filtered
		"true,  P,   P,    false",
		"true,  P,   p,    true",
		"true,  C1,  c2,   true",
		// filterValue not in CS → no “branch” to include → everything filtered
		"false, Q,   P,    true",
		"true,  q,   P,    true"
	})
	void testIsaFilterBehavior(
		boolean caseSensitive,
		String filterValue,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = hierarchicalCS(caseSensitive);
		boolean actual = isFiltered(
			cs,
			FilterOperator.ISA,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("ISA[%s] on code '%s' (caseSensitive=%b)",
				filterValue, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[descendent-of] cs={0}, filter={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		// caseSensitive, filterValue, testCode, expectedFiltered
		"false, P, P,   true",   // parent is always excluded
		"false, P, C1,  false",  // direct child
		"false, P, C2,  false",  // grandchild
		"false, P, C3,  false",  // sibling branch
		"false, P, XXX, true",   // not in CS
		"true,  P, P,   true",   // parent excluded (case exactly matches)
		"true,  P, C2,  false",  // descendant works
		"true,  p, P,   true",   // wrong-case filter: no descendants → all filtered
		"true,  P, c1,  true",   // wrong-case concept: filtered
		// filterValue not in CS → no descendants → everything filtered
		"false, Q,   P,    true",
		"true,  q,   P,    true"
	})
	void testDescendentOfFilterBehavior(
		boolean caseSensitive,
		String filterValue,
		String testCode,
		boolean expectedIsFiltered) {

		CodeSystem cs = hierarchicalCS(caseSensitive);
		boolean actual = isFiltered(
			cs,
			FilterOperator.DESCENDENTOF,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("DESCENDENTOF[%s] on code %s (caseSensitive=%b)", filterValue, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[is-not-a] caseSensitive={0}, filter={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		// exclude parent + descendants; include off-branch and unknown
		"false, P,   P,    true",
		"false, P,   C1,   true",
		"false, P,   C2,   true",
		"false, P,   C3,   true",
		"false, P,   XXX,  false",
		// is-not-a C1 excludes C1 & C2, but keeps P and C3
		"false, C1,  P,    false",
		"false, C1,  C1,   true",
		"false, C1,  C2,   true",
		"false, C1,  C3,   false",
		// case-sensitive: wrong‐case filter or code → filtered only when it matches/excludes
		"true,  C1,  C2,   true",
		"true,  p,   C1,   true",  // filterValue wrong-case excludes everything
		"true,  P,   p,    false",  // filterValue correct, code wrong-case → included
		// filterValue not in CS → exclude all codes
		"false, Q,   P,    true",
		"true,  q,   P,    true"
	})
	void testIsNotAFilterBehavior(
		boolean caseSensitive,
		String filterValue,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = hierarchicalCS(caseSensitive);
		boolean actual = isFiltered(
			cs,
			FilterOperator.ISNOTA,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("ISNOTA[%s] on code '%s' (caseSensitive=%b)",
				filterValue, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[regex] caseSensitive={0}, pattern={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		// caseSensitive, pattern,    testCode, expectedIsFiltered
		"false,       '^A.*',       A,         false",  // matches A, so not filtered
		"false,       '^A.*',       a,         false",  // ignore case, matches a
		"false,       '^B.*',       a,         true",   // ignore case, doesn't match B*
		"true,        '^A.*',       A,         false",  // case exact, matches A
		"true,        '^A.*',       a,         true"    // case exact, 'a' ≠ 'A'
	})
	void testRegexFilterBehavior(
		boolean caseSensitive,
		String pattern,
		String testCode,
		boolean expectedIsFiltered) {

		// Build a flat CS containing at least A, a and B
		CodeSystem cs = flatCodeSystem(caseSensitive, "A", "a", "B");

		boolean actual = isFiltered(
			cs,
			FilterOperator.REGEX,
			pattern,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("REGEX[%s] on code '%s' (caseSensitive=%b)", pattern, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[in] caseSensitive={0}, values={1}, code={2} ⇒ filtered={3}")
	@CsvSource(
		delimiter = '|',
		value = {
			// caseSensitive | values | testCode | expectedIsFiltered
			"false        | A,B    | A | false",  // in list → not filtered
			"false        | A,B    | a | false",  // ignore case → not filtered
			"false        | A,B    | B | false",  // in list → not filtered
			"false        | A,B    | C | true",   // not in list → filtered
			"true         | A,B    | A | false",  // exact match → not filtered
			"true         | A,B    | a | true",   // wrong case → filtered
			"true         | A,B    | B | false",  // exact match → not filtered
			"true         | X,Y,Z  | W | true",    // not in list → filtered
			// values none in CS → nothing included → all filtered
			"false        | Q,R    | A | true",
			"true         | q,r    | A | true"
		}
	)
	void testInFilterBehavior(
		boolean caseSensitive,
		String csvValues,
		String testCode,
		boolean expectedIsFiltered) {

		// Build a flat CodeSystem containing at least the listed values
		CodeSystem cs = flatCodeSystem(caseSensitive, "A", "B", "C", "X", "Y", "Z");

		boolean actual = isFiltered(
			cs,
			FilterOperator.IN,
			csvValues,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("IN[%s] on code '%s' (caseSensitive=%b)", csvValues, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[generalizes] cs={0}, filter={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		// cs,    filter, code, expectedFiltered
		"false, P,      P,    false",  // self
		"false, P,      C1,   true",   // off-branch from P’s ancestor set
		"false, P,      C2,   true",
		"false, P,      C3,   true",
		"false, P,      XXX,  true",   // unknown code
		"false, Q,      P,    true",   // non-existent filter → empty
		"true,  q,      P,    true",
		"true,  P,      p,    true",   // wrong-case filter → empty
		"true,  p,      P,    true",
		"true,  P,      C1,   true",   // still excluded under P
		// --------------------------------------------------------------------
		// Now test generalizes = C2 → should include C2 + its ancestors (C1, P)
		"false, C2,     C2,   false",
		"false, C2,     C1,   false",
		"false, C2,     P,    false",
		"false, C2,     C3,   true",   // off-branch
		"false, C2,     XXX,  true",   // unknown code
		// case-sensitivity for C2
		"true,  C2,     C2,   false",
		"true,  C2,     C1,   false",
		"true,  C2,     P,    false",
		"true,  C2,     c1,   true"    // wrong-case ancestor filtered out
	})
	void testGeneralizesFilterBehavior(
		boolean caseSensitive,
		String filterValue,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = hierarchicalCS(caseSensitive);
		boolean actual = isFiltered(
			cs,
			FilterOperator.GENERALIZES,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);
		assertThat(actual)
			.as("GENERALIZES[%s] on code '%s' (caseSensitive=%b)",
				filterValue, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[child-of] cs={0}, filter={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		"false, P,   C1,  false",  // immediate child
		"false, P,   C2,  true",   // grandchild → filtered
		"false, P,   P,   true",   // parent itself → filtered
		"false, P,   C3,  false",  // sibling also immediate child
		"false, P,   XXX, true",   // unknown → filtered
		// case‐sensitive
		"true,  P,   c1,  true"    // wrong-case → filtered
	})
	void testChildOfFilterBehavior(
		boolean caseSensitive,
		String filterValue,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = hierarchicalCS(caseSensitive);
		boolean actual = isFiltered(
			cs,
			FilterOperator.CHILDOF,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);
		assertThat(actual)
			.as("CHILD-OF[%s] on code '%s' (caseSensitive=%b)",
				filterValue, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[descendent-leaf] cs={0}, filter={1}, code={2} ⇒ filtered={3}")
	@CsvSource({
		// caseSensitive, filterValue, code, expectedIsFiltered
		"false, P,   P,    true",   // P is not a descendant → filtered
		"false, P,   C1,   true",   // C1 has children → filtered
		"false, P,   C2,   false",  // C2 is a leaf descendant → not filtered
		"false, P,   C3,   false",  // C3 is a leaf descendant → not filtered
		"false, P,   XXX,  true",   // unknown → filtered
		// non-existent filter → empty result
		"false, Q,   C2,   true",
		"true,  q,   C2,   true",
		// case-sensitivity on real values
		"true,  P,   c2,   true",   // wrong case concept → filtered
		"true,  p,   C2,   true",   // wrong case filter → filtered
		"true,  P,   C2,   false"   // correct-case leaf → not filtered
	})
	void testDescendentLeafFilterBehavior(
		boolean caseSensitive,
		String filterValue,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = hierarchicalCS(caseSensitive);
		boolean actual = isFiltered(
			cs,
			FilterOperator.DESCENDENTLEAF,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);
		assertThat(actual)
			.as("DESCENDENT-LEAF[%s] on code '%s' (caseSensitive=%b)",
				filterValue, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[exists] cs={0}, property={1}, want={2}, code={3} ⇒ filtered={4}")
	@CsvSource(
		delimiter = '|',
		value = {
		// caseSensitive | property  | wantExists | code | expectedIsFiltered
		"false          | code      | true       | A    | false",  // code always exists
		"false          | code      | false      | A    | true",   // code never missing
		"false          | display   | true       | A    | false",  // A has display
		"false          | display   | true       | B    | true",   // B no display
		"false          | display   | false      | A    | true",   // A has display → filtered
		"false          | display   | false      | B    | false",  // B no display → not filtered
		// case‐sensitive DOES affect code existence for wrong‐case
		"true           | code      | true       | a    | true"    // 'a' != 'A' under case‐sensitive
	})
	void testExistsFilterBehavior(
		boolean caseSensitive,
		String property,
		boolean wantExists,
		String testCode,
		boolean expectedIsFiltered
	) {
		CodeSystem cs = flatCodeSystem(caseSensitive, "A", "B");
		// give A a display only
		cs.getConcept().get(0).setDisplay("LabelA");

		boolean actual = isFilteredWithProperty(
			cs,
			property,
			FilterOperator.EXISTS,
			Boolean.toString(wantExists),
			new FhirVersionIndependentConcept(cs.getUrl(), testCode)
		);

		assertThat(actual)
			.as("EXISTS[%s=%s] on code '%s' (caseSensitive=%b)",
				property, wantExists, testCode, caseSensitive)
			.isEqualTo(expectedIsFiltered);
	}

	private static CodeSystem hierarchyCS(String kind, boolean caseSensitive) {
		return "flat".equals(kind) ? flatCSWithParentProperty(caseSensitive) : hierarchicalCS(caseSensitive);
	}

	@ParameterizedTest(name = "[child-exists] kind={0}, want={1}, code={2} ⇒ filtered={3}")
	@CsvSource(
		delimiter = '|',
		value = {
			// hierarchy shape: P -> C1 -> C2, P -> C3. Leaves: C2, C3.
			// kind   | wantExists | code | expectedIsFiltered
			// exists=false keeps leaves (KDL case)
			"nested   | false      | P    | true",
			"nested   | false      | C1   | true",
			"nested   | false      | C2   | false",
			"nested   | false      | C3   | false",
			"flat     | false      | P    | true",
			"flat     | false      | C1   | true",
			"flat     | false      | C2   | false",
			"flat     | false      | C3   | false",
			// exists=true keeps concepts that have children
			"nested   | true       | P    | false",
			"nested   | true       | C1   | false",
			"nested   | true       | C2   | true",
			"nested   | true       | C3   | true",
			"flat     | true       | P    | false",
			"flat     | true       | C1   | false",
			"flat     | true       | C2   | true",
			"flat     | true       | C3   | true"
		})
	void testChildExistsFilterBehavior(String kind, boolean wantExists, String testCode, boolean expectedIsFiltered) {
		CodeSystem cs = hierarchyCS(kind, true);
		boolean actual = isFilteredWithProperty(
			cs,
			"child",
			FilterOperator.EXISTS,
			Boolean.toString(wantExists),
			new FhirVersionIndependentConcept(cs.getUrl(), testCode));

		assertThat(actual)
			.as("child EXISTS=%s on code '%s' (kind=%s)", wantExists, testCode, kind)
			.isEqualTo(expectedIsFiltered);
	}

	@ParameterizedTest(name = "[parent-exists] kind={0}, want={1}, code={2} ⇒ filtered={3}")
	@CsvSource(
		delimiter = '|',
		value = {
			// Root: P. Non-roots: C1, C2, C3.
			// kind   | wantExists | code | expectedIsFiltered
			// exists=false keeps the root
			"nested   | false      | P    | false",
			"nested   | false      | C1   | true",
			"nested   | false      | C2   | true",
			"nested   | false      | C3   | true",
			"flat     | false      | P    | false",
			"flat     | false      | C1   | true",
			"flat     | false      | C2   | true",
			"flat     | false      | C3   | true",
			// exists=true keeps concepts that have a parent
			"nested   | true       | P    | true",
			"nested   | true       | C1   | false",
			"nested   | true       | C2   | false",
			"nested   | true       | C3   | false",
			"flat     | true       | P    | true",
			"flat     | true       | C1   | false",
			"flat     | true       | C2   | false",
			"flat     | true       | C3   | false"
		})
	void testParentExistsFilterBehavior(String kind, boolean wantExists, String testCode, boolean expectedIsFiltered) {
		CodeSystem cs = hierarchyCS(kind, true);
		boolean actual = isFilteredWithProperty(
			cs,
			"parent",
			FilterOperator.EXISTS,
			Boolean.toString(wantExists),
			new FhirVersionIndependentConcept(cs.getUrl(), testCode));

		assertThat(actual)
			.as("parent EXISTS=%s on code '%s' (kind=%s)", wantExists, testCode, kind)
			.isEqualTo(expectedIsFiltered);
	}

	@Test
	void childExistsIgnoresNonCanonicalParentProperty() {
		// The parent property definition does NOT use the canonical concept-properties URI, so no
		// hierarchy is built: every concept is a leaf → child exists=false keeps all of them.
		CodeSystem cs = flatCSWithNonCanonicalParentProperty(true);
		for (String code : List.of("P", "C1", "C2", "C3")) {
			boolean filtered = isFilteredWithProperty(
				cs,
				"child",
				FilterOperator.EXISTS,
				"false",
				new FhirVersionIndependentConcept(cs.getUrl(), code));
			assertThat(filtered)
				.as("child EXISTS=false on '%s' with non-canonical parent property", code)
				.isFalse();
		}
	}

	@Test
	void parentExistsOnNonCanonicalParentPropertyThrows() {
		// 'parent' is declared with a non-canonical URI → its meaning is unknown, so a parent-exists filter
		// must fail rather than be silently misinterpreted.
		CodeSystem cs = flatCSWithNonCanonicalParentProperty(true);
		ValueSet.ConceptSetFilterComponent f = new ValueSet.ConceptSetFilterComponent()
			.setProperty("parent").setOp(FilterOperator.EXISTS).setValue("true");
		ValueSetExpansionFilterContext ctx = new ValueSetExpansionFilterContext(cs, List.of(f));

		assertThatThrownBy(() -> ctx.isFiltered(new FhirVersionIndependentConcept(cs.getUrl(), "C1")))
			.isInstanceOf(ValueSetExpansionFilterContext.UnsupportedFilterException.class)
			.hasMessageContaining("parent")
			.hasMessageContaining("http://example.org/custom#parent");
	}

	@Test
	void flatParentPropertyWithoutDeclarationBuildsHierarchyByName() {
		// No CodeSystem.property declaration for 'parent' at all → matched by its reserved code name, so the
		// flat hierarchy is still built.
		CodeSystem cs = new CodeSystem().setUrl("http://example.org/undeclared").setCaseSensitive(true);
		cs.addConcept().setCode("P");
		cs.addConcept().setCode("C1").addProperty().setCode("parent").setValue(new CodeType("P"));
		cs.addConcept().setCode("C2").addProperty().setCode("parent").setValue(new CodeType("C1"));

		// descendent-of P now finds C1 and C2 (hierarchy from the undeclared flat parent property)
		assertThat(isFiltered(cs, FilterOperator.DESCENDENTOF, "P",
			new FhirVersionIndependentConcept(cs.getUrl(), "C1"))).isFalse();
		assertThat(isFiltered(cs, FilterOperator.DESCENDENTOF, "P",
			new FhirVersionIndependentConcept(cs.getUrl(), "C2"))).isFalse();
		// child exists=false selects only the leaf C2
		assertThat(isFilteredWithProperty(cs, "child", FilterOperator.EXISTS, "false",
			new FhirVersionIndependentConcept(cs.getUrl(), "C2"))).isFalse();
		assertThat(isFilteredWithProperty(cs, "child", FilterOperator.EXISTS, "false",
			new FhirVersionIndependentConcept(cs.getUrl(), "P"))).isTrue();
	}

	@ParameterizedTest(name = "[descendent-of-flat] filter={0}, code={1} ⇒ filtered={2}")
	@CsvSource({
		// Structural ops must also work when hierarchy comes from a flat parent concept-property.
		"P, P,  true",
		"P, C1, false",
		"P, C2, false",
		"P, C3, false",
		"C1, C2, false",
		"C1, C3, true"
	})
	void descendentOfWorksOnFlatParentProperty(String filterValue, String testCode, boolean expectedIsFiltered) {
		CodeSystem cs = flatCSWithParentProperty(true);
		boolean actual = isFiltered(
			cs,
			FilterOperator.DESCENDENTOF,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode));

		assertThat(actual)
			.as("DESCENDENTOF[%s] on '%s' (flat parent property)", filterValue, testCode)
			.isEqualTo(expectedIsFiltered);
	}

	/**
	 * A flat parent-property hierarchy can contain a cycle (A parent B, B parent A). A structural filter
	 * such as descendent-of must terminate instead of looping forever.
	 */
	@Test
	@Timeout(value = 10, unit = TimeUnit.SECONDS)
	void descendentOfTerminatesOnCyclicHierarchy() {
		CodeSystem cs = new CodeSystem().setUrl("http://example.org/cycle").setCaseSensitive(true);
		cs.addProperty().setCode("parent").setUri(CONCEPT_PROPERTIES_PARENT).setType(CodeSystem.PropertyType.CODE);
		cs.addConcept().setCode("A").addProperty().setCode("parent").setValue(new CodeType("B"));
		cs.addConcept().setCode("B").addProperty().setCode("parent").setValue(new CodeType("A"));

		// Candidate "ZZZ" is not part of the (cyclic) subtree, so without a visited-set this loops forever.
		boolean actual = isFiltered(
			cs,
			FilterOperator.DESCENDENTOF,
			"A",
			new FhirVersionIndependentConcept(cs.getUrl(), "ZZZ"));

		assertThat(actual).isTrue();
	}

	/**
	 * In a cyclic hierarchy (A parent B, B parent A) a concept must not be reported as a descendant of
	 * itself: descendent-of "A" applied to candidate "A" must NOT match (so the concept is filtered out).
	 */
	@Test
	@Timeout(value = 10, unit = TimeUnit.SECONDS)
	void descendentOfDoesNotMatchSelfOnCyclicHierarchy() {
		CodeSystem cs = new CodeSystem().setUrl("http://example.org/cycle").setCaseSensitive(true);
		cs.addProperty().setCode("parent").setUri(CONCEPT_PROPERTIES_PARENT).setType(CodeSystem.PropertyType.CODE);
		cs.addConcept().setCode("A").addProperty().setCode("parent").setValue(new CodeType("B"));
		cs.addConcept().setCode("B").addProperty().setCode("parent").setValue(new CodeType("A"));

		boolean actual = isFiltered(
			cs,
			FilterOperator.DESCENDENTOF,
			"A",
			new FhirVersionIndependentConcept(cs.getUrl(), "A"));

		// "A" is not a descendant of itself, so it does not pass the filter and IS filtered out.
		assertThat(actual).isTrue();
	}

	/**
	 * Build a flat CodeSystem carrying the standard boolean concept-property {@code inactive} /
	 * {@code notSelectable} (with value {@code true}) on the code named {@code flaggedCode}.
	 */
	private static CodeSystem flatCSWithBooleanProperty(String propertyCode, String flaggedCode) {
		CodeSystem cs = new CodeSystem().setUrl("http://example.org/bool").setCaseSensitive(true);
		cs.addConcept().setCode("PLAIN");
		cs.addConcept().setCode(flaggedCode).addProperty().setCode(propertyCode).setValue(new BooleanType(true));
		// A concept that declares the property explicitly as FALSE must NOT be treated as flagged.
		cs.addConcept().setCode("EXPLICIT_FALSE").addProperty().setCode(propertyCode).setValue(new BooleanType(false));
		return cs;
	}

	@Test
	void inactiveExistsSelectsInactiveConcepts() {
		CodeSystem cs = flatCSWithBooleanProperty("inactive", "GONE");

		// exists=true keeps only the inactive concept
		assertThat(isFilteredWithProperty(cs, "inactive", FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "GONE"))).isFalse();
		assertThat(isFilteredWithProperty(cs, "inactive", FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "PLAIN"))).isTrue();
		// A concept whose inactive property is explicitly false is NOT inactive
		assertThat(isFilteredWithProperty(cs, "inactive", FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "EXPLICIT_FALSE"))).isTrue();

		// exists=false keeps the complement (the active concepts)
		assertThat(isFilteredWithProperty(cs, "inactive", FilterOperator.EXISTS, "false",
			new FhirVersionIndependentConcept(cs.getUrl(), "PLAIN"))).isFalse();
		assertThat(isFilteredWithProperty(cs, "inactive", FilterOperator.EXISTS, "false",
			new FhirVersionIndependentConcept(cs.getUrl(), "GONE"))).isTrue();
	}

	@ParameterizedTest(name = "[date-exists] property={0}")
	@CsvSource({
		// Standard date-valued concept-properties: 'exists' is a presence check (no value=true semantics).
		"deprecated",
		"deprecationDate",
		"retirementDate",
	})
	void dateStandardPropertyExistsSelectsFlaggedConcepts(String propertyCode) {
		CodeSystem cs = new CodeSystem().setUrl("http://example.org/date").setCaseSensitive(true);
		cs.addConcept().setCode("PLAIN");
		cs.addConcept().setCode("FLAGGED").addProperty().setCode(propertyCode).setValue(new DateTimeType("2020-01-01"));

		// exists=true keeps only the concept that carries the property
		assertThat(isFilteredWithProperty(cs, propertyCode, FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "FLAGGED"))).isFalse();
		assertThat(isFilteredWithProperty(cs, propertyCode, FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "PLAIN"))).isTrue();

		// exists=false keeps the complement
		assertThat(isFilteredWithProperty(cs, propertyCode, FilterOperator.EXISTS, "false",
			new FhirVersionIndependentConcept(cs.getUrl(), "PLAIN"))).isFalse();
		assertThat(isFilteredWithProperty(cs, propertyCode, FilterOperator.EXISTS, "false",
			new FhirVersionIndependentConcept(cs.getUrl(), "FLAGGED"))).isTrue();
	}

	@Test
	void notSelectableExistsSelectsNotSelectableConcepts() {
		CodeSystem cs = flatCSWithBooleanProperty("notSelectable", "ABSTRACT");

		assertThat(isFilteredWithProperty(cs, "notSelectable", FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "ABSTRACT"))).isFalse();
		assertThat(isFilteredWithProperty(cs, "notSelectable", FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "PLAIN"))).isTrue();
		assertThat(isFilteredWithProperty(cs, "notSelectable", FilterOperator.EXISTS, "false",
			new FhirVersionIndependentConcept(cs.getUrl(), "PLAIN"))).isFalse();
		assertThat(isFilteredWithProperty(cs, "notSelectable", FilterOperator.EXISTS, "false",
			new FhirVersionIndependentConcept(cs.getUrl(), "ABSTRACT"))).isTrue();
	}

	@Test
	void standardPropertyDeclaredWithCanonicalUriIsHonored() {
		CodeSystem cs = new CodeSystem().setUrl("http://example.org/cs").setCaseSensitive(true);
		cs.addProperty().setCode("inactive")
			.setUri("http://hl7.org/fhir/concept-properties#inactive")
			.setType(CodeSystem.PropertyType.BOOLEAN);
		cs.addConcept().setCode("ACTIVE");
		cs.addConcept().setCode("GONE").addProperty().setCode("inactive").setValue(new BooleanType(true));

		assertThat(isFilteredWithProperty(cs, "inactive", FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "GONE"))).isFalse();
		assertThat(isFilteredWithProperty(cs, "inactive", FilterOperator.EXISTS, "true",
			new FhirVersionIndependentConcept(cs.getUrl(), "ACTIVE"))).isTrue();
	}

	@Test
	void standardPropertyDeclaredWithConflictingUriThrows() {
		// The CodeSystem declares a property literally named 'inactive' but with a NON-canonical URI, so its
		// meaning is unknown — a filter using it cannot be evaluated as the standard 'inactive' property.
		CodeSystem cs = new CodeSystem().setUrl("http://example.org/cs").setCaseSensitive(true);
		cs.addProperty().setCode("inactive")
			.setUri("http://example.org/custom#inactive")
			.setType(CodeSystem.PropertyType.BOOLEAN);
		cs.addConcept().setCode("A").addProperty().setCode("inactive").setValue(new BooleanType(true));

		ValueSet.ConceptSetFilterComponent f = new ValueSet.ConceptSetFilterComponent()
			.setProperty("inactive").setOp(FilterOperator.EXISTS).setValue("true");
		ValueSetExpansionFilterContext ctx = new ValueSetExpansionFilterContext(cs, List.of(f));
		FhirVersionIndependentConcept concept = new FhirVersionIndependentConcept(cs.getUrl(), "A");

		assertThatThrownBy(() -> ctx.isFiltered(concept))
			.isInstanceOf(ValueSetExpansionFilterContext.UnsupportedFilterException.class)
			.hasMessageContaining("inactive")
			.hasMessageContaining("http://example.org/custom#inactive");
	}

	@ParameterizedTest(name = "[strict-boolean] property={0}, value={1}")
	@CsvSource({
		"child, yes",
		"parent, 0",
		"inactive, ''",
		"deprecated, maybe"
	})
	void standardPropertyExistsWithNonBooleanValueThrows(String property, String value) {
		// The 'exists' value is semantically a boolean; a non-'true'/'false' value must be rejected rather
		// than silently coerced to false (which would invert the filter, e.g. selecting the leaves/roots).
		CodeSystem cs = hierarchicalCS(true);
		ValueSet.ConceptSetFilterComponent f = new ValueSet.ConceptSetFilterComponent()
			.setProperty(property).setOp(FilterOperator.EXISTS).setValue(value);
		ValueSetExpansionFilterContext ctx = new ValueSetExpansionFilterContext(cs, List.of(f));
		FhirVersionIndependentConcept concept = new FhirVersionIndependentConcept(cs.getUrl(), "C1");

		assertThatThrownBy(() -> ctx.isFiltered(concept))
			.isInstanceOf(ValueSetExpansionFilterContext.UnsupportedFilterException.class)
			.hasMessageContaining(property);
	}

	/**
	 * On a case-insensitive CodeSystem, a structural filter whose value differs only in case from the
	 * stored code (e.g. "p" vs stored "P") must still resolve the subtree.
	 */
	@ParameterizedTest(name = "[descendent-of-flat-ci] filter={0}, code={1} ⇒ filtered={2}")
	@CsvSource({
		// filterValue differs in case from the stored parent code "P" / "C1".
		"p, C1, false",
		"p, C2, false",
		"p, C3, false",
		"c1, C2, false",
		"c1, C3, true"
	})
	void descendentOfIsCaseInsensitiveOnFlatParentProperty(String filterValue, String testCode, boolean expectedIsFiltered) {
		CodeSystem cs = flatCSWithParentProperty(false);
		boolean actual = isFiltered(
			cs,
			FilterOperator.DESCENDENTOF,
			filterValue,
			new FhirVersionIndependentConcept(cs.getUrl(), testCode));

		assertThat(actual)
			.as("DESCENDENTOF[%s] on '%s' (case-insensitive flat parent property)", filterValue, testCode)
			.isEqualTo(expectedIsFiltered);
	}
}
