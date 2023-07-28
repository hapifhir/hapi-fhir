/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for _source search parameter.
 */
public abstract class SourceSearchParameterTestCases implements ITestDataBuilder.WithSupport {

	final ITestDataBuilder.Support myTestDataBuilder;
	final TestDaoSearch myTestDaoSearch;

	final JpaStorageSettings myStorageSettings;

	protected SourceSearchParameterTestCases(
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
		myTestDataBuilder.getSystemRequestDetails().setRequestId(null);
		myStorageSettings.setStoreMetaSourceInformation(new JpaStorageSettings().getStoreMetaSourceInformation());
	}

	@BeforeEach
	public void before() {
		myStorageSettings.setStoreMetaSourceInformation(
				JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);
	}

	@Test
	public void testSearch_withSource_returnsCorrectBundle() {
		IIdType pt0id = createPatient(withSource(getFhirContext(), "http://host/0"), withActiveTrue());
		IIdType pt1id = createPatient(withSource(getFhirContext(), "http://host/1"), withActiveTrue());

		// Search by source URI
		myTestDaoSearch.assertSearchFinds("search", "Patient?_source=http://host/0", pt0id);
		List<IBaseResource> resourceList = myTestDaoSearch.searchForResources("Patient?_source=http://host/0");
		assertEquals(1, resourceList.size());
	}

	@EnabledIf("isRequestIdSupported")
	@Test
	public void testSearch_withRequestIdAndSource_returnsCorrectBundle() {
		myTestDataBuilder.getSystemRequestDetails().setRequestId("a_request_id");
		IIdType pt0id = createPatient(withSource(getFhirContext(), "http://host/0"), withActiveTrue());

		IIdType pt1id = createPatient(withSource(getFhirContext(), "http://host/1"), withActiveTrue());

		myTestDataBuilder.getSystemRequestDetails().setRequestId("b_request_id");
		IIdType pt2id = createPatient(withSource(getFhirContext(), "http://host/1"), withActiveTrue());

		// Search by request ID
		myTestDaoSearch.assertSearchFinds("search", "Patient?_source=#a_request_id", pt0id, pt1id);
		myTestDaoSearch.assertSearchNotFound("search", "Patient?_source=#a_request_id", pt2id);

		// Search by source URI and request ID
		myTestDaoSearch.assertSearchFinds("search", "Patient?_source=http://host/0#a_request_id", pt0id);
		myTestDaoSearch.assertSearchNotFound("search", "Patient?_source=http://host/0#a_request_id", pt1id, pt2id);
	}

	@Test
	public void testSearchSource_whenSameSourceForMultipleResourceTypes_willMatchSearchResourceTypeOnly() {
		String sourceUrn = "http://host/0";
		myTestDataBuilder.getSystemRequestDetails().setRequestId("a_request_id");

		IIdType pt0id = createPatient(withSource(getFhirContext(), sourceUrn), withActiveTrue());
		IIdType ob0id = createObservation(withSource(getFhirContext(), sourceUrn), withStatus("final"));

		myTestDaoSearch.assertSearchFinds("search", "Patient?_source=http://host/0", pt0id);
		myTestDaoSearch.assertSearchNotFound("search", "Patient?_source=http://host/0", ob0id);
	}

	@Test
	public void testSearchSource_withOrJoinedParameter_returnsUnionResultBundle() {
		myTestDataBuilder.getSystemRequestDetails().setRequestId("a_request_id");

		IIdType pt0id = createPatient(withSource(getFhirContext(), "http://host/0"), withActiveTrue());
		IIdType pt1id = createPatient(withSource(getFhirContext(), "http://host/1"), withActiveTrue());
		createPatient(withSource(getFhirContext(), "http://host/2"), withActiveTrue());

		myTestDaoSearch.assertSearchFinds("search", "Patient?_source=http://host/0,http://host/1", pt0id, pt1id);
	}

	@EnabledIf("isRequestIdSupported")
	@Test
	public void testSearch_withSourceAndRequestId_returnsIntersectionResultBundle() {
		myTestDataBuilder.getSystemRequestDetails().setRequestId("a_request_id");
		IIdType pt0id = createPatient(withSource(getFhirContext(), "http://host/0"), withActiveTrue());

		myTestDataBuilder.getSystemRequestDetails().setRequestId("b_request_id");
		IIdType pt1id = createPatient(withSource(getFhirContext(), "http://host/0"), withActiveTrue());
		IIdType pt2id = createPatient(withSource(getFhirContext(), "http://host/1"), withActiveTrue());

		myTestDaoSearch.assertSearchFinds("search", "Patient?_source=http://host/0&_source=#a_request_id", pt0id);
		myTestDaoSearch.assertSearchNotFound(
				"search", "Patient?_source=http://host/0&_source=#a_request_id", pt1id, pt2id);
	}

	@Test
	public void testSearchSource_withContainsModifier_returnsCorrectBundle() {
		myStorageSettings.setAllowContainsSearches(true);

		IIdType p1Id = createPatient(
				withSource(getFhirContext(), "http://some-source"), withActiveTrue(), withFamily("Family"));
		IIdType p2Id = createPatient(withSource(getFhirContext(), "http://some-source/v1/321"), withActiveTrue());
		IIdType p3Id = createPatient(
				withSource(getFhirContext(), ("http://another-source/v1")), withActiveTrue(), withFamily("Family"));

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
				p3Id,
				p1Id,
				p2Id);
	}

	@Test
	public void testSearchSource_withMissingModifierFalse_returnsNonEmptySources() {
		IIdType p1Id = createPatient(withSource(getFhirContext(), "http://some-source/v1"), withActiveTrue());
		createPatient(withActiveTrue());

		myTestDaoSearch.assertSearchFinds("search matches non-empty source", "Patient?_source:missing=false", p1Id);
	}

	@Test
	public void testSearchSource_withMissingModifierTrue_returnsEmptySources() {
		createPatient(withSource(getFhirContext(), "http://some-source/v1"), withActiveTrue());
		IIdType p1Id = createPatient(withActiveTrue());

		myTestDaoSearch.assertSearchFinds("search matches empty sources", "Patient?_source:missing=true", p1Id);
	}

	@Test
	public void testSearchSource_withAboveModifier_returnsSourcesAbove() {
		IIdType p1Id = createPatient(withSource(getFhirContext(), "http://some-source/v1/123"), withActiveTrue());
		IIdType p2Id = createPatient(withSource(getFhirContext(), "http://some-source/v1/321"), withActiveTrue());
		IIdType p3Id = createPatient(withSource(getFhirContext(), "http://some-source/v1/321/v2"), withActiveTrue());
		IIdType p4Id = createPatient(withSource(getFhirContext(), "http://another-source"), withActiveTrue());

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
		IIdType p1Id = createPatient(withSource(getFhirContext(), "http://some-source/v1/123"), withActiveTrue());
		IIdType p2Id = createPatient(withSource(getFhirContext(), "http://some-source/v1"), withActiveTrue());
		IIdType p3Id = createPatient(withSource(getFhirContext(), "http://some-source"), withActiveTrue());
		IIdType p4Id = createPatient(withSource(getFhirContext(), "http://another-source"), withActiveTrue());

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
