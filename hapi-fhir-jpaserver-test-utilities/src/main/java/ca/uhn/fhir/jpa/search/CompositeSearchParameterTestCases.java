/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

	protected CompositeSearchParameterTestCases(
			ITestDataBuilder.Support theTestDataBuilder, TestDaoSearch theTestDaoSearch) {
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
						withQuantityAtPath("valueQuantity", 100, null, "mmHg")));

		myTestDaoSearch.assertSearchFinds(
				"search matches both sps in composite", "Observation?component-code-value-quantity=8480-6$60", id1);
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
						withQuantityAtPath("valueQuantity", 100, null, "mmHg")));

		List<String> ids = myTestDaoSearch.searchForIds("Observation?component-code-value-quantity=8480-6$100");
		assertThat(ids)
				.as("Search for the value from one component, but the code from the other, so it shouldn't match")
				.isEmpty();
	}

	@Test
	void searchCodeCode_onSameComponent_found() {
		IIdType id1 = createObservation(
				withObservationComponent(
						withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_8480_6, null),
						withCodingAt("valueCodeableConcept.coding", SYSTEM_LOINC_ORG, "some-code")),
				withObservationComponent(
						withCodingAt("code.coding", SYSTEM_LOINC_ORG, CODE_3421_5, null),
						withCodingAt("valueCodeableConcept.coding", SYSTEM_LOINC_ORG, "another-code")));

		myTestDaoSearch.assertSearchFinds(
				"search matches both sps in composite",
				"Observation?component-code-value-concept=8480-6$some-code",
				id1);
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
						withCodingAt("valueCodeableConcept.coding", SYSTEM_LOINC_ORG, "another-code")));

		List<String> ids = myTestDaoSearch.searchForIds("Observation?component-code-value-concept=8480-6$another-code");
		assertThat(ids)
				.as("Search for the value from one component, but the code from the other, so it shouldn't match")
				.isEmpty();
	}

	@Test
	void searchCodeDate_onSameResource_found() {
		IIdType id1 = createObservation(
				withObservationCode(SYSTEM_LOINC_ORG, CODE_8480_6, null),
				withDateTimeAt("valueDateTime", "2020-01-01T12:34:56"));

		myTestDaoSearch.assertSearchFinds(
				"search matches both sps in composite", "Observation?code-value-date=8480-6$lt2021", id1);
	}

	@Test
	void searchCodeString_onSameResource_found() {
		IIdType id1 = createObservation(
				withObservationCode(SYSTEM_LOINC_ORG, CODE_8480_6, null), withDateTimeAt("valueString", "ABCDEF"));

		myTestDaoSearch.assertSearchFinds(
				"token code + string prefix matches", "Observation?code-value-string=8480-6$ABC", id1);
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
		SearchParameter searchParameter = createCompositeSearchParameter("uri-number-compound-test", "RiskAssessment");
		searchParameter.addComponent(
				componentFrom("http://hl7.org/fhir/SearchParameter/Resource-source", "meta.source"));
		searchParameter.addComponent(componentFrom(
				"http://hl7.org/fhir/SearchParameter/RiskAssessment-probability", "prediction.probability"));
		doCreateResource(searchParameter);

		// enable this sp.
		myTestDaoSearch.getSearchParamRegistry().forceRefresh();

		RiskAssessment riskAssessment = new RiskAssessment();
		riskAssessment.setMeta(new Meta().setSource("https://example.com/ourSource"));
		riskAssessment.addPrediction(
				new RiskAssessment.RiskAssessmentPredictionComponent().setProbability(new DecimalType(0.02)));
		IIdType raId = doCreateResource(riskAssessment);

		// verify config
		myTestDaoSearch.assertSearchFinds(
				"simple uri search works", "RiskAssessment?_source=https://example.com/ourSource", raId);
		myTestDaoSearch.assertSearchFinds("simple number search works", "RiskAssessment?probability=0.02", raId);
		// verify composite queries
		myTestDaoSearch.assertSearchFinds(
				"composite uri + number",
				"RiskAssessment?uri-number-compound-test=https://example.com/ourSource$0.02",
				raId);
		myTestDaoSearch.assertSearchNotFound(
				"both params must match ",
				"RiskAssessment?uri-number-compound-test=https://example.com/ourSource$0.08",
				raId);
		myTestDaoSearch.assertSearchNotFound(
				"both params must match ",
				"RiskAssessment?uri-number-compound-test=https://example.com/otherUrI$0.02",
				raId);
		// verify combo query
		myTestDaoSearch.assertSearchFinds(
				"combo uri + number", "RiskAssessment?_source=https://example.com/ourSource&probability=0.02", raId);
	}

	@ParameterizedTest
	@MethodSource("extensionProvider")
	void testComboSearch_withTokenAndNumber_returnsMatchingResources(Extension theExtension) {
		// Combine existing SPs to test Token + number
		SearchParameter searchParameter = createCompositeSearchParameter("token-number-combo-test", "RiskAssessment");
		searchParameter.addComponent(
				componentFrom("http://hl7.org/fhir/SearchParameter/RiskAssessment-method", "RiskAssessment"));
		searchParameter.addComponent(
				componentFrom("http://hl7.org/fhir/SearchParameter/RiskAssessment-probability", "RiskAssessment"));
		searchParameter.setExtension(List.of(theExtension));
		doCreateResource(searchParameter);

		// enable this sp.
		myTestDaoSearch.getSearchParamRegistry().forceRefresh();

		RiskAssessment riskAssessment = new RiskAssessment();
		riskAssessment.setMethod(new CodeableConcept(new Coding(null, "BRCAPRO", null)));
		riskAssessment.addPrediction(
				new RiskAssessment.RiskAssessmentPredictionComponent().setProbability(new DecimalType(0.02)));
		IIdType raId = doCreateResource(riskAssessment);

		RiskAssessment riskAssessmentNonMatch = new RiskAssessment();
		riskAssessmentNonMatch.setMethod(new CodeableConcept(new Coding(null, "NOT_FOUND_CODE", null)));
		riskAssessmentNonMatch.addPrediction(
				new RiskAssessment.RiskAssessmentPredictionComponent().setProbability(new DecimalType(0.03)));
		doCreateResource(riskAssessmentNonMatch);

		// verify combo query
		myTestDaoSearch.assertSearchFinds("combo uri + number", "RiskAssessment?method=BRCAPRO&probability=0.02", raId);
		myTestDaoSearch.assertSearchNotFound(
				"both params must match", "RiskAssessment?method=CODE&probability=0.02", raId);
		myTestDaoSearch.assertSearchNotFound(
				"both params must match", "RiskAssessment?method=BRCAPRO&probability=0.09", raId);
	}

	@ParameterizedTest
	@MethodSource("extensionProvider")
	void testComboSearch_withUriAndString_returnsMatchingResources(Extension theExtension) {
		// Combine existing SPs to test URI + String
		SearchParameter searchParameter = createCompositeSearchParameter("uri-string-combo-test", "Device");
		searchParameter.addComponent(componentFrom("http://hl7.org/fhir/SearchParameter/Device-url", "Device"));
		searchParameter.addComponent(componentFrom("http://hl7.org/fhir/SearchParameter/Device-model", "Device"));
		searchParameter.setExtension(List.of(theExtension));
		doCreateResource(searchParameter);

		// enable this sp.
		myTestDaoSearch.getSearchParamRegistry().forceRefresh();

		Device device = new Device();
		device.setUrl("http://deviceUrl");
		device.setModelNumber("modelNumber");

		IIdType deviceId = doCreateResource(device);

		Device deviceNonMatch = new Device();
		deviceNonMatch.setUrl("http://someurl");
		deviceNonMatch.setModelNumber("someModelNumber");

		// verify combo query
		myTestDaoSearch.assertSearchFinds(
				"combo uri + string", "Device?url=http://deviceUrl&model=modelNumber", deviceId);
		myTestDaoSearch.assertSearchNotFound(
				"both params must match", "Device?url=http://wrongUrl&model=modelNumber", deviceId);
		myTestDaoSearch.assertSearchNotFound(
				"both params must match", "Device?url=http://deviceUrl&model=wrongModel", deviceId);
	}

	private static SearchParameter createCompositeSearchParameter(String theCodeValue, String theBase) {
		SearchParameter retVal = new SearchParameter();
		retVal.setId(theCodeValue);
		retVal.setUrl("http://example.org/" + theCodeValue);
		retVal.addBase(theBase);
		retVal.setCode(theCodeValue);
		retVal.setType(Enumerations.SearchParamType.COMPOSITE);
		retVal.setStatus(Enumerations.PublicationStatus.ACTIVE);
		retVal.setExpression(theBase);

		return retVal;
	}

	private SearchParameter.SearchParameterComponentComponent componentFrom(
			String theDefinition, String theExpression) {
		return new SearchParameter.SearchParameterComponentComponent()
				.setDefinition(theDefinition)
				.setExpression(theExpression);
	}

	static Stream<Arguments> extensionProvider() {
		return Stream.of(
				Arguments.of(new Extension(
						HapiExtensions.EXT_SP_UNIQUE,
						new BooleanType(false))), // composite SP of type combo with non-unique index
				Arguments.of(new Extension(
						HapiExtensions.EXT_SP_UNIQUE,
						new BooleanType(true))) // composite SP of type combo with non-unique index
				);
	}
}
