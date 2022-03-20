package ca.uhn.fhir.storage.test;

/*-
 * #%L
 * hapi-fhir-storage-test-utilities
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

import ca.uhn.fhir.jpa.dao.BaseTransactionProcessor;
import ca.uhn.fhir.jpa.dao.IdSubstitutionMap;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseTransactionProcessorTest {

	@Test
	void testPerformIdSubstitutionsInMatchUrl_MatchAtStart() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "Patient?foo=urn:uuid:1234&bar=baz");
		assertEquals("Patient?foo=Patient/123&bar=baz", outcome);
	}

	/**
	 * Make sure versioned targets get the version stripped
	 */
	@Test
	void testPerformIdSubstitutionsInMatchUrl_MatchAtEnd() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:7ea4f3a6-d2a3-4105-9f31-374d525085d4"), new IdType("Patient/123/_history/1"));
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "Patient?name=FAMILY1&organization=urn%3Auuid%3A7ea4f3a6-d2a3-4105-9f31-374d525085d4");
		assertEquals("Patient?name=FAMILY1&organization=Patient/123", outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_MatchEscapedParam() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "Patient?foo=" + UrlUtil.escapeUrlParam("urn:uuid:1234") + "&bar=baz");
		assertEquals("Patient?foo=Patient/123&bar=baz", outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_MatchInParamNameShouldntBeReplaced() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "Patient?urn:uuid:1234=foo&bar=baz");
		assertEquals("Patient?urn:uuid:1234=foo&bar=baz", outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_NoParams() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String input = "Patient";
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, input);
		assertEquals(input, outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_UnterminatedParams() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String input = "Patient?foo&bar=&baz";
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, input);
		assertEquals(input, outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_ReplaceMultiple() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/abcdefghijklmnopqrstuvwxyz0123456789"));
		String input = "Patient?foo=urn:uuid:1234&bar=urn:uuid:1234&baz=urn:uuid:1234";
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, input);
		String expected = "Patient?foo=Patient/abcdefghijklmnopqrstuvwxyz0123456789&bar=Patient/abcdefghijklmnopqrstuvwxyz0123456789&baz=Patient/abcdefghijklmnopqrstuvwxyz0123456789";
		assertEquals(expected, outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_NonUrnSubstitution() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("Patient/123"), new IdType("Patient/456"));
		String input = "Patient?foo=Patient/123";
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, input);
		assertEquals(input, outcome);
	}

}
