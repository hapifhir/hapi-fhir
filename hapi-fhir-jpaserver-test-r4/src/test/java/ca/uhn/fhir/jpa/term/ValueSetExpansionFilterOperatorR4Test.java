package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Characterises {@code ValueSet.compose.include.filter} operator support, per
 * <a href="https://github.com/hapifhir/hapi-fhir/issues/8378">issue #8378</a>.
 * <p>
 * Each test asserts the behaviour the FHIR specification requires, so a test that fails is a
 * defect that is still present. Tests parameterised on {@code theForceDisableHibernateSearch}
 * run the same expansion down both the database path
 * ({@link TermReadSvcImpl#expandWithoutHibernateSearch}) and the Hibernate Search path, which is
 * where the two are expected to agree and currently do not.
 */
class ValueSetExpansionFilterOperatorR4Test extends BaseTermR4Test {

	private static final String CS_ID = "CodeSystem-FilterOps";
	private static final String CS_URL = "http://example.org/" + CS_ID;
	private static final String VS_URL = "http://example.org/ValueSet-FilterOps";

	private static final String CODE_PARENT = "CodeA";
	private static final String CODE_SIBLING = "CodeB";
	private static final String CODE_STANDALONE = "CodeC";
	private static final int CHILD_COUNT = 10;
	/** Children 0-4 carry TTY=SBD; children 5-9 carry TTY=SCD. */
	private static final int SBD_CHILD_COUNT = 5;

	@BeforeEach
	void createCodeSystemFixture() {
		myStorageSettings.setPreExpandValueSets(false);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId(CS_ID);
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		CodeSystem.ConceptDefinitionComponent parent =
				codeSystem.addConcept().setCode(CODE_PARENT).setDisplay("Display " + CODE_PARENT);
		parent.addProperty().setCode("TTY").setValue(new StringType("BPCK"));

		for (int i = 0; i < CHILD_COUNT; i++) {
			CodeSystem.ConceptDefinitionComponent child = parent.addConcept()
					.setCode(CODE_PARENT + i)
					.setDisplay("Display " + CODE_PARENT + i);
			child.addProperty()
					.setCode("TTY")
					.setValue(new StringType(i < SBD_CHILD_COUNT ? "SBD" : "SCD"));
		}

		codeSystem.addConcept().setCode(CODE_SIBLING).setDisplay("Display " + CODE_SIBLING);
		codeSystem.addConcept().setCode(CODE_STANDALONE).setDisplay("Display " + CODE_STANDALONE);

		myCodeSystemDao.create(codeSystem, mySrd);
	}

	@AfterEach
	void reenableHibernateSearch() {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(false);
	}

	// ---------------------------------------------------------------------
	// concept / code hierarchy operators
	// ---------------------------------------------------------------------

	/** FHIR defines is-a as including the code being filtered on, not only its descendants. */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_conceptIsAFilter_includesTheFilteredCodeItself(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		List<String> codes = expandToCodes(filterValueSet("concept", ValueSet.FilterOperator.ISA, CODE_PARENT));

		assertThat(codes).contains(CODE_PARENT).hasSize(CHILD_COUNT + 1);
	}

	/** All filters within a single include apply conjunctively. */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_conceptIsAFilterWithPropertyEqualFilter_returnsOnlyCodesMatchingBoth(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VS_URL);
		ValueSet.ConceptSetComponent include = valueSet.getCompose().addInclude().setSystem(CS_URL);
		include.addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISA).setValue(CODE_PARENT);
		include.addFilter().setProperty("TTY").setOp(ValueSet.FilterOperator.EQUAL).setValue("SBD");

		List<String> codes = expandToCodes(valueSet);

		assertThat(codes).hasSize(SBD_CHILD_COUNT).doesNotContain(CODE_SIBLING, CODE_STANDALONE, CODE_PARENT);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_conceptDescendentOfFilter_returnsDescendantsOnly(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		List<String> codes =
				expandToCodes(filterValueSet("concept", ValueSet.FilterOperator.DESCENDENTOF, CODE_PARENT));

		assertThat(codes).hasSize(CHILD_COUNT).doesNotContain(CODE_PARENT);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_conceptIsNotAFilter_excludesTheCodeAndItsDescendants(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		List<String> codes =
				expandToCodes(filterValueSet("concept", ValueSet.FilterOperator.ISNOTA, CODE_PARENT));

		assertThat(codes).containsExactlyInAnyOrder(CODE_SIBLING, CODE_STANDALONE);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_conceptGeneralizesFilter_returnsTheCodeAndItsAncestors(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		List<String> codes = expandToCodes(
				filterValueSet("concept", ValueSet.FilterOperator.GENERALIZES, CODE_PARENT + "0"));

		assertThat(codes).containsExactlyInAnyOrder(CODE_PARENT, CODE_PARENT + "0");
	}

	// ---------------------------------------------------------------------
	// value operators on concept properties
	// ---------------------------------------------------------------------

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_propertyInFilter_returnsCodesWithAnyListedValue(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		List<String> codes = expandToCodes(filterValueSet("TTY", ValueSet.FilterOperator.IN, "SBD,SCD"));

		assertThat(codes).hasSize(CHILD_COUNT);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_propertyExistsFilter_returnsCodesCarryingTheProperty(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		List<String> codes = expandToCodes(filterValueSet("TTY", ValueSet.FilterOperator.EXISTS, "true"));

		assertThat(codes).hasSize(CHILD_COUNT + 1);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_propertyRegexFilter_returnsMatchingCodes(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		List<String> codes = expandToCodes(filterValueSet("TTY", ValueSet.FilterOperator.REGEX, "S.D"));

		assertThat(codes).hasSize(CHILD_COUNT);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void expandValueSet_displayEqualFilter_returnsTheMatchingConcept(
			boolean theForceDisableHibernateSearch) {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(theForceDisableHibernateSearch);

		List<String> codes = expandToCodes(
				filterValueSet("display", ValueSet.FilterOperator.EQUAL, "Display " + CODE_STANDALONE));

		assertThat(codes).containsExactly(CODE_STANDALONE);
	}

	// ---------------------------------------------------------------------
	// unsupported combinations must fail loudly rather than expand to nothing
	// ---------------------------------------------------------------------

	/**
	 * A hierarchy operator applied to a non-hierarchy property cannot be evaluated. The Hibernate
	 * Search path adds a match-none predicate and returns an empty expansion with no error, which
	 * gives the caller no indication that a filter was dropped.
	 */
	@Test
	void expandValueSet_hierarchyOperatorOnConceptProperty_throwsRatherThanReturningEmpty() {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(false);

		ValueSet valueSet = filterValueSet("TTY", ValueSet.FilterOperator.ISA, "SBD");
		assertThatThrownBy(() -> expandToCodes(valueSet))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("TTY");
	}

	/**
	 * The exists operator is not supported on concept/code. The blank filter value is treated as a
	 * code to look up, so the caller is told the code does not exist rather than that the operator
	 * is unsupported.
	 */
	@Test
	void expandValueSet_existsOperatorOnConcept_reportsUnsupportedOperatorRatherThanMissingCode() {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(false);

		ValueSet valueSet = filterValueSet("concept", ValueSet.FilterOperator.EXISTS, "");
		assertThatThrownBy(() -> expandToCodes(valueSet))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageNotContainingAny("code does not exist");
	}

	// ---------------------------------------------------------------------

	private ValueSet filterValueSet(String theProperty, ValueSet.FilterOperator theOp, String theValue) {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VS_URL);
		valueSet.getCompose()
				.addInclude()
				.setSystem(CS_URL)
				.addFilter()
				.setProperty(theProperty)
				.setOp(theOp)
				.setValue(theValue);
		return valueSet;
	}

	private List<String> expandToCodes(ValueSet theValueSet) {
		ValueSet expanded = myTermSvc.expandValueSet(null, theValueSet);
		return expanded.getExpansion().getContains().stream()
				.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
				.toList();
	}
}
