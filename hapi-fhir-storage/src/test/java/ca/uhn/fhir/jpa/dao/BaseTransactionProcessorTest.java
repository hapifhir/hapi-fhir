package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
