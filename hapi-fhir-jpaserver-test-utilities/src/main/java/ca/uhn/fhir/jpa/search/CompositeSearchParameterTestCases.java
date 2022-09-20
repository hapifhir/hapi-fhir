package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Test cases for composite search parameters.
 * https://www.hl7.org/fhir/search.html#composite
 *
 * Intended to be nested in a context that provides a ITestDataBuilder and TestDaoSearch.
 */
public abstract class CompositeSearchParameterTestCases implements ITestDataBuilder {
	static final String SYSTEM_LOINC_ORG = "http://loinc.org";
	static final String CODE_8480_6 = "8480-6";
	static final String CODE_3421_5 = "3421-5";

	final ITestDataBuilder myTestDataBuilder;
	final TestDaoSearch myTestDaoSearch;

	public CompositeSearchParameterTestCases(ITestDataBuilder theTestDataBuilder, TestDaoSearch theTestDaoSearch) {
		myTestDataBuilder = theTestDataBuilder;
		myTestDaoSearch = theTestDaoSearch;
	}

	/**
	 * Does this engine support sub-element correlation?
	 */
	protected abstract boolean isCorrelatedSupported();

	@EnabledIf("isCorrelatedSupported")
	@Test
	void searchCodeQuantity_onSameComponent_found() {
		IIdType id1 = createObservation(
			withObservationComponent(
				withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_8480_6, null),
				withQuantityAtPath("valueQuantity", 60, null, "mmHg")),
			withObservationComponent(
				withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_3421_5, null),
				withQuantityAtPath("valueQuantity", 100, null, "mmHg"))
		);

		myTestDaoSearch.assertSearchFinds("search matches both sps in composite",
			"Observation?component-code-value-quantity=8480-6$60", id1);
	}

	@Test
	void searchCodeQuantity_differentComponents_notFound() {
		createObservation(
			withObservationCode(SYSTEM_LOINC_ORG, CODE_8480_6),
			withObservationComponent(
				withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_8480_6),
				withQuantityAtPath("valueQuantity", 60, null, "mmHg")),
			withObservationComponent(
				withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_3421_5),
				withQuantityAtPath("valueQuantity", 100, null, "mmHg"))
		);

		List<String> ids = myTestDaoSearch.searchForIds("Observation?component-code-value-quantity=8480-6$100");
		assertThat("Search for the value from one component, but the code from the other, so it shouldn't match", ids, empty());
	}


	@EnabledIf("isCorrelatedSupported")
	@Test
	void searchCodeCode_onSameComponent_found() {
		IIdType id1 = createObservation(
			withObservationComponent(
				withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_8480_6, null),
				withCodingAt("valueCodeableConcept.coding", SYSTEM_LOINC_ORG, "some-code")),
			withObservationComponent(
				withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_3421_5, null),
				withCodingAt("valueCodeableConcept.coding", SYSTEM_LOINC_ORG, "another-code"))
		);

		myTestDaoSearch.assertSearchFinds("search matches both sps in composite",
			"Observation?component-code-value-concept=8480-6$some-code", id1);
	}

	@Test
	void searchCodeCode_differentComponents_notFound() {
		createObservation(
			withObservationCode(SYSTEM_LOINC_ORG, CODE_8480_6),
			withObservationComponent(
				withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_8480_6, null),
				withCodingAt("valueCodeableConcept.coding", SYSTEM_LOINC_ORG, "some-code")),
			withObservationComponent(
				withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_3421_5, null),
				withCodingAt("valueCodeableConcept.coding", SYSTEM_LOINC_ORG, "another-code"))
		);

		List<String> ids = myTestDaoSearch.searchForIds("Observation?component-code-value-concept=8480-6$another-code");
		assertThat("Search for the value from one component, but the code from the other, so it shouldn't match", ids, empty());
	}

	@Test
	void searchCodeDate_onSameResource_found() {
		IIdType id1 = createObservation(
			withObservationCode( SYSTEM_LOINC_ORG, CODE_8480_6, null),
			withDateTimeAt("valueDateTime", "2020-01-01T12:34:56")
		);

		myTestDaoSearch.assertSearchFinds("search matches both sps in composite",
			"Observation?code-value-date=8480-6$lt2021", id1);
	}
	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myTestDataBuilder.doCreateResource(theResource);
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myTestDataBuilder.doUpdateResource(theResource);
	}

	@Override
	public FhirContext getFhirContext() {
		return myTestDataBuilder.getFhirContext();
	}
}
