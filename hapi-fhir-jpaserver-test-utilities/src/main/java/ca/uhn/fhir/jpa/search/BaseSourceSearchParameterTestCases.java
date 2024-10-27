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

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

/**
 * Test cases for _source search parameter.
 */
public abstract class BaseSourceSearchParameterTestCases implements ITestDataBuilder.WithSupport {

	final ITestDataBuilder.Support myTestDataBuilder;
	final TestDaoSearch myTestDaoSearch;

	final JpaStorageSettings myStorageSettings;

	protected BaseSourceSearchParameterTestCases(
			ITestDataBuilder.Support theTestDataBuilder,
			TestDaoSearch theTestDaoSearch,
			JpaStorageSettings theStorageSettings) {
		myTestDataBuilder = theTestDataBuilder;
		myTestDaoSearch = theTestDaoSearch;
		myStorageSettings = theStorageSettings;
	}

	/**
	 * Enable if requestId within _source Search Parameter is supported
	 * Example: _source={sourceURI}#{requestId}
	 */
	protected abstract boolean isRequestIdSupported();

	@Override
	public Support getTestDataBuilderSupport() {
		return myTestDataBuilder;
	}

	@AfterEach
	public final void after() {
		myTestDataBuilder.setRequestId(null);
		myStorageSettings.setStoreMetaSourceInformation(new JpaStorageSettings().getStoreMetaSourceInformation());
	}

	@BeforeEach
	public void before() {
		myStorageSettings.setStoreMetaSourceInformation(
				JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);
	}

	@Test
	public void testSearch_withSource_returnsCorrectBundle() {
		IIdType pt0id = createPatient(withSource("http://host/0"), withActiveTrue());
		IIdType pt1id = createPatient(withSource("http://host/1"), withActiveTrue());

		myTestDaoSearch.assertSearchFinds("search by source URI finds", "Patient?_source=http://host/0", pt0id);
		myTestDaoSearch.assertSearchNotFound("search by source URI not found", "Patient?_source=http://host/0", pt1id);
	}

	@EnabledIf("isRequestIdSupported")
	@Test
	public void testSearch_withRequestIdAndSource_returnsCorrectBundle() {
		myTestDataBuilder.setRequestId("a_request_id");
		IIdType pt0id = createPatient(withSource("http://host/0"), withActiveTrue());

		IIdType pt1id = createPatient(withSource("http://host/1"), withActiveTrue());

		myTestDataBuilder.setRequestId("b_request_id");
		IIdType pt2id = createPatient(withSource("http://host/1"), withActiveTrue());

		myTestDaoSearch.assertSearchFinds("search by requestId finds", "Patient?_source=#a_request_id", pt0id, pt1id);
		myTestDaoSearch.assertSearchNotFound("search by requestId not found", "Patient?_source=#a_request_id", pt2id);

		myTestDaoSearch.assertSearchFinds(
				"search by source URI and requestId finds", "Patient?_source=http://host/0#a_request_id", pt0id);
		myTestDaoSearch.assertSearchNotFound(
				"search by source URI and requestId not found",
				"Patient?_source=http://host/0#a_request_id",
				pt1id,
				pt2id);
	}

	@Test
	public void testSearchSource_whenSameSourceForMultipleResourceTypes_willMatchSearchResourceTypeOnly() {
		String sourceUrn = "http://host/0";
		myTestDataBuilder.setRequestId("a_request_id");

		IIdType pt0id = createPatient(withSource(sourceUrn), withActiveTrue());
		IIdType ob0id = createObservation(withSource(sourceUrn), withStatus("final"));

		myTestDaoSearch.assertSearchFinds(
				"search source URI for Patient finds", "Patient?_source=http://host/0", pt0id);
		myTestDaoSearch.assertSearchNotFound(
				"search source URI for Patient - Observation not found", "Patient?_source=http://host/0", ob0id);
	}

	@Test
	public void testSearchSource_withOrJoinedParameter_returnsUnionResultBundle() {
		myTestDataBuilder.setRequestId("a_request_id");

		IIdType pt0id = createPatient(withSource("http://host/0"), withActiveTrue());
		IIdType pt1id = createPatient(withSource("http://host/1"), withActiveTrue());
		createPatient(withSource("http://host/2"), withActiveTrue());

		myTestDaoSearch.assertSearchFinds(
				"search source URI with union", "Patient?_source=http://host/0,http://host/1", pt0id, pt1id);
	}

	@EnabledIf("isRequestIdSupported")
	@Test
	public void testSearch_withSourceAndRequestId_returnsIntersectionResultBundle() {
		myTestDataBuilder.setRequestId("a_request_id");
		IIdType pt0id = createPatient(withSource("http://host/0"), withActiveTrue());

		myTestDataBuilder.setRequestId("b_request_id");
		IIdType pt1id = createPatient(withSource("http://host/0"), withActiveTrue());
		IIdType pt2id = createPatient(withSource("http://host/1"), withActiveTrue());

		myTestDaoSearch.assertSearchFinds(
				"search for source URI and requestId intersection finds",
				"Patient?_source=http://host/0&_source=#a_request_id",
				pt0id);
		myTestDaoSearch.assertSearchNotFound(
				"search for source URI and requestId intersection not found",
				"Patient?_source=http://host/0&_source=#a_request_id",
				pt1id,
				pt2id);
	}

	@Test
	public void testSearchSource_withContainsModifier_returnsCorrectBundle() {
		myStorageSettings.setAllowContainsSearches(true);

		IIdType p1Id = createPatient(withSource("http://some-source"), withActiveTrue(), withFamily("Family"));
		IIdType p2Id = createPatient(withSource("http://some-source/v1/321"), withActiveTrue());
		IIdType p3Id = createPatient(withSource(("http://another-source/v1")), withActiveTrue(), withFamily("Family"));

		myTestDaoSearch.assertSearchFinds(
				"search matches both sources (same case search)", "Patient?_source:contains=some-source", p1Id, p2Id);

		myTestDaoSearch.assertSearchFinds(
				"search matches both sources (case insensitive search)",
				"Patient?_source:contains=Some-Source",
				p1Id,
				p2Id);

		myTestDaoSearch.assertSearchFinds(
				"search matches all sources (union search)",
				"Patient?_source:contains=Another-Source,some-source",
				p1Id,
				p2Id,
				p3Id);

		myTestDaoSearch.assertSearchFinds(
				"search matches one sources (intersection with family SearchParameter)",
				"Patient?_source:contains=Another-Source,some-source&family=Family,YourFamily",
				p3Id);

		myTestDaoSearch.assertSearchNotFound(
				"search returns empty bundle (contains with missing=true)",
				"Patient?_source:contains=Another-Source,some-source&_source:missing=true",
				p1Id,
				p2Id,
				p3Id);
	}

	@Test
	public void testSearchSource_withMissingModifierFalse_returnsNonEmptySources() {
		IIdType p1Id = createPatient(withSource("http://some-source/v1"), withActiveTrue());
		createPatient(withActiveTrue());

		myTestDaoSearch.assertSearchFinds("search matches non-empty source", "Patient?_source:missing=false", p1Id);
	}

	@Test
	public void testSearchSource_withMissingModifierTrue_returnsEmptySources() {
		createPatient(withSource("http://some-source/v1"), withActiveTrue(), withFamily("Family"));
		IIdType p2Id = createPatient(withActiveTrue(), withFamily("Family"));

		myTestDaoSearch.assertSearchFinds("search matches empty source", "Patient?_source:missing=true", p2Id);
		myTestDaoSearch.assertSearchFinds(
				"search matches empty source with family parameter intersection",
				"Patient?_source:missing=true&family=Family",
				p2Id);
	}

	@Test
	public void testSearchSource_withAboveModifier_returnsSourcesAbove() {
		IIdType p1Id = createPatient(withSource("http://some-source/v1/123"), withActiveTrue());
		IIdType p2Id = createPatient(withSource("http://some-source/v1/321"), withActiveTrue());
		IIdType p3Id = createPatient(withSource("http://some-source/v1/321/v2"), withActiveTrue());
		IIdType p4Id = createPatient(withSource("http://another-source"), withActiveTrue());

		myTestDaoSearch.assertSearchFinds(
				"search matches all sources above",
				"Patient?_source:above=http://some-source/v1/321/v2/456",
				p2Id,
				p3Id);

		myTestDaoSearch.assertSearchFinds(
				"search matches all sources above", "Patient?_source:above=http://some-source/v1/321/v2", p2Id, p3Id);

		myTestDaoSearch.assertSearchFinds(
				"search matches source above", "Patient?_source:above=http://some-source/v1/321", p2Id);

		myTestDaoSearch.assertSearchNotFound(
				"search not matches if sources is not above",
				"Patient?_source:above=http://some-source/fhir/v5/789",
				p1Id,
				p2Id,
				p3Id,
				p4Id);

		myTestDaoSearch.assertSearchNotFound(
				"search not matches if sources is not above",
				"Patient?_source:above=http://some-source",
				p1Id,
				p2Id,
				p3Id,
				p4Id);

		myTestDaoSearch.assertSearchFinds(
				"search not matches for another source",
				"Patient?_source:above=http://another-source,http://some-source/v1/321/v2",
				p2Id,
				p3Id,
				p4Id);
	}

	@Test
	public void testSearchSource_withBelowModifier_returnsSourcesBelow() {
		IIdType p1Id = createPatient(withSource("http://some-source/v1/123"), withActiveTrue());
		IIdType p2Id = createPatient(withSource("http://some-source/v1"), withActiveTrue());
		IIdType p3Id = createPatient(withSource("http://some-source"), withActiveTrue());
		IIdType p4Id = createPatient(withSource("http://another-source"), withActiveTrue());

		myTestDaoSearch.assertSearchFinds(
				"search matches all sources below", "Patient?_source:below=http://some-source", p1Id, p2Id, p3Id);

		myTestDaoSearch.assertSearchFinds(
				"search below with union",
				"Patient?_source:below=http://some-source/v1,http://another-source",
				p1Id,
				p2Id,
				p4Id);

		myTestDaoSearch.assertSearchFinds(
				"search below with intersection",
				"Patient?_source:below=http://some-source/v1&_source:below=http://some-source/v1/123",
				p1Id);

		myTestDaoSearch.assertSearchNotFound(
				"search below with intersection not matches",
				"Patient?_source:below=http://some-source/v1&_source:below=http://some-source/v1/123",
				p2Id,
				p3Id,
				p4Id);
	}
}
