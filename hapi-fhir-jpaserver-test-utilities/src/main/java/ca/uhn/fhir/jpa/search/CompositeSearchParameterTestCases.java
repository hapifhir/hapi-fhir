package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
 * Intended to be nested in a context that provides a ITestDataBuilder.Support and TestDaoSearch.
 */
public abstract class CompositeSearchParameterTestCases implements ITestDataBuilder.WithSupport {
	static final String SYSTEM_LOINC_ORG = "http://loinc.org";
	static final String CODE_8480_6 = "8480-6";
	static final String CODE_3421_5 = "3421-5";

	final ITestDataBuilder.Support myTestDataBuilder;
	final TestDaoSearch myTestDaoSearch;

	protected CompositeSearchParameterTestCases(ITestDataBuilder.Support theTestDataBuilder, TestDaoSearch theTestDaoSearch) {
		myTestDataBuilder = theTestDataBuilder;
		myTestDaoSearch = theTestDaoSearch;
	}

	@Override
	public Support getTestDataBuilderSupport() {
		return myTestDataBuilder;
	}



	/**
	 * Should we run test cases that depend on engine support sub-element correlation?
	 *
	 * JPA currently reuses the extracted values from each sub-param.
	 * This works fine for non-repeating elements, but can fail for repeating elements
	 * like Observation.component.  For the composites defined on Observation.component,
	 * both sub-params must match on the specific component (eg systolic vs diasystolic readings),
	 * and JPA doesn't track down to the element level.
	 */
	protected abstract boolean isCorrelatedSupported();

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

	@EnabledIf("isCorrelatedSupported")
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

	@EnabledIf("isCorrelatedSupported")
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


	@Test
	void searchCodeString_onSameResource_found() {
		IIdType id1 = createObservation(
			withObservationCode( SYSTEM_LOINC_ORG, CODE_8480_6, null),
			withDateTimeAt("valueString", "ABCDEF")
		);

		myTestDaoSearch.assertSearchFinds("token code + string prefix matches",
			"Observation?code-value-string=8480-6$ABC", id1);
	}

	/**
	 * Create a goofy SP to test composite component support
	 * for uri and number.
	 *
	 * Note - JPA doesn't support this either yet.  Just piggyback on this flag.
	 */
	@EnabledIf("isCorrelatedSupported")
	@Test
	void searchUriNumber_onSameResource_found() {
		// Combine existing SPs to test uri + number
		createResourceFromJson("""
{
  "resourceType": "SearchParameter",
  "name": "uri-number-compound-test",
  "status": "active",
  "description": "dummy to exercise uri + number",
  "code": "uri-number-compound-test",
  "base": [ "RiskAssessment" ],
  "type": "composite",
  "expression": "RiskAssessment",
  "component": [ {
      "definition": "http://hl7.org/fhir/SearchParameter/Resource-source",
      "expression": "meta.source"
    }, {
      "definition": "http://hl7.org/fhir/SearchParameter/RiskAssessment-probability",
      "expression": "prediction.probability"
    } ]
 }""");
		// enable this sp.
		myTestDaoSearch.getSearchParamRegistry().forceRefresh();

		IIdType raId = createResourceFromJson("""
			{
			  "resourceType": "RiskAssessment",
			  "meta": {
			     "source": "https://example.com/ourSource"
			     },
			  "prediction": [
			      {
			        "outcome": {
			          "text": "Heart Attack"
			        },
			        "probabilityDecimal": 0.02
			      }
			  ]
			}
			""");

		// verify config
		myTestDaoSearch.assertSearchFinds("simple uri search works", "RiskAssessment?_source=https://example.com/ourSource", raId);
		myTestDaoSearch.assertSearchFinds("simple number search works", "RiskAssessment?probability=0.02", raId);
		// verify composite queries
		myTestDaoSearch.assertSearchFinds("composite uri + number", "RiskAssessment?uri-number-compound-test=https://example.com/ourSource$0.02", raId);
		myTestDaoSearch.assertSearchNotFound("both params must match ", "RiskAssessment?uri-number-compound-test=https://example.com/ourSource$0.08", raId);
		myTestDaoSearch.assertSearchNotFound("both params must match ", "RiskAssessment?uri-number-compound-test=https://example.com/otherUrI$0.02", raId);
	}

}
