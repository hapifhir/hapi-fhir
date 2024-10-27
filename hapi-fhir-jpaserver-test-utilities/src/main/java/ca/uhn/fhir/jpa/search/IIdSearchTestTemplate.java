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
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import java.util.List;

public interface IIdSearchTestTemplate {
	TestDaoSearch getSearch();

	ITestDataBuilder getBuilder();

	@Test
	default void testSearchByServerAssignedId_findsResource() {
		IIdType id = getBuilder().createPatient();

		getSearch().assertSearchFinds("search by server assigned id", "Patient?_id=" + id.getIdPart(), id);
	}

	@Test
	default void testSearchByClientAssignedId_findsResource() {
		ITestDataBuilder b = getBuilder();
		b.createPatient(b.withId("client-assigned-id"));

		getSearch()
				.assertSearchFinds(
						"search by client assigned id", "Patient?_id=client-assigned-id", "client-assigned-id");
	}

	/**
	 * The _id SP is defined as token, and there is no system.
	 * So sorting should be string order of the value.
	 */
	@Test
	default void testSortById_treatsIdsAsString() {
		ITestDataBuilder b = getBuilder();
		b.createPatient(b.withId("client-assigned-id"));
		IIdType serverId = b.createPatient();
		b.createPatient(b.withId("0-sorts-before-other-numbers"));

		getSearch()
				.assertSearchFindsInOrder(
						"sort by resource id",
						"Patient?_sort=_id",
						List.of("0-sorts-before-other-numbers", serverId.getIdPart(), "client-assigned-id"));

		getSearch()
				.assertSearchFindsInOrder(
						"reverse sort by resource id",
						"Patient?_sort=-_id",
						List.of("client-assigned-id", serverId.getIdPart(), "0-sorts-before-other-numbers"));
	}
}
