package ca.uhn.fhir.jpa.term;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TermReadSvcUtilTest {

	@Nested
	public class GetValueSetId {

		@Test
		void doesntStartWithLoincGenericValuesetIdReturnsEmpty() {
			Optional<String> result = TermReadSvcUtil.getValueSetId("http://boinc.org");
			assertFalse(result.isPresent());
		}

		@Test
		void doesntStartWithLoincGenericValuesetIdPluSlashReturnsEmpty() {
			Optional<String> result = TermReadSvcUtil.getValueSetId("http://loinc.org/vs-something-else.ar");
			assertFalse(result.isPresent());
		}

		@Test
		void startWithLoincGenericValuesetIdPluSlashButNothingElseReturnsEmpty() {
			Optional<String> result = TermReadSvcUtil.getValueSetId("http://loinc.org/vs/");
			assertFalse(result.isPresent());
		}

		@Test
		void startWithLoincGenericValuesetIdPluSlashPlusIdReturnsId() {
			Optional<String> result = TermReadSvcUtil.getValueSetId("http://loinc.org/vs/radiology-playbook");
			assertTrue(result.isPresent());
		}
	}


	@Nested
	public class IsLoincNotGenericUnversionedValueSet {

		@Test
		void doesntContainLoincReturnsFalse() {
			boolean result = TermReadSvcUtil.isLoincUnversionedValueSet(
				"http://l-oinc.org/vs/radiology-playbook");
			assertFalse(result);
		}

		@Test
		void containsVersionDelimiterReturnsFalse() {
			boolean result = TermReadSvcUtil.isLoincUnversionedValueSet(
				"http://loinc.org/vs/radiology-playbook|v2.68");
			assertFalse(result);
		}

		@Test
		void isLoincAllValueSetUrlReturnsTrue() {
			boolean result = TermReadSvcUtil.isLoincUnversionedValueSet(
				"http://loinc.org/vs");
			assertTrue(result);
		}

		@Test
		void isLoincWithoutVersionAndNotGenericValuesetUrlReturnsTrue() {
			boolean result = TermReadSvcUtil.isLoincUnversionedValueSet(
				"http://loinc.org/vs/radiology-playbook");
			assertTrue(result);
		}

	}


	@Nested
	public class IsLoincNotGenericUnversionedCodeSystem {

		@Test
		void doesntContainLoincReturnsFalse() {
			boolean result = TermReadSvcUtil.isLoincUnversionedCodeSystem(
				"http://loin-c.org");
			assertFalse(result);
		}

		@Test
		void hasVersionDelimiterReturnsFalse() {
			boolean result = TermReadSvcUtil.isLoincUnversionedCodeSystem(
				"http://loinc.org|v2.68");
			assertFalse(result);
		}

		@Test
		void containsLoincNadNoVersionDelimiterReturnsTrue() {
			boolean result = TermReadSvcUtil.isLoincUnversionedCodeSystem(
				"http://loinc.org");
			assertTrue(result);
		}

	}

}
