package ca.uhn.fhir.jpa.term;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TermReadSvcUtilTest {

	@Nested
	public class GetValueSetId {

		@Test
		void doesntStartWithLoincGenericValuesetIdReturnsEmpty() {
			Optional<String> result = TermReadSvcUtil.getValueSetId("http://boinc.org");
			assertThat(result.isPresent()).isFalse();
		}

		@Test
		void doesntStartWithLoincGenericValuesetIdPluSlashReturnsEmpty() {
			Optional<String> result = TermReadSvcUtil.getValueSetId("http://loinc.org/vs-something-else.ar");
			assertThat(result.isPresent()).isFalse();
		}

		@Test
		void startWithLoincGenericValuesetIdPluSlashButNothingElseReturnsEmpty() {
			Optional<String> result = TermReadSvcUtil.getValueSetId("http://loinc.org/vs/");
			assertThat(result.isPresent()).isFalse();
		}

		@Test
		void startWithLoincGenericValuesetIdPluSlashPlusIdReturnsId() {
			Optional<String> result = TermReadSvcUtil.getValueSetId("http://loinc.org/vs/radiology-playbook");
			assertThat(result).isPresent();
		}
	}


	@Nested
	public class IsLoincNotGenericUnversionedValueSet {

		@Test
		void doesntContainLoincReturnsFalse() {
			boolean result = TermReadSvcUtil.isLoincUnversionedValueSet(
				"http://l-oinc.org/vs/radiology-playbook");
			assertThat(result).isFalse();
		}

		@Test
		void containsVersionDelimiterReturnsFalse() {
			boolean result = TermReadSvcUtil.isLoincUnversionedValueSet(
				"http://loinc.org/vs/radiology-playbook|v2.68");
			assertThat(result).isFalse();
		}

		@Test
		void isLoincAllValueSetUrlReturnsTrue() {
			boolean result = TermReadSvcUtil.isLoincUnversionedValueSet(
				"http://loinc.org/vs");
			assertThat(result).isTrue();
		}

		@Test
		void isLoincWithoutVersionAndNotGenericValuesetUrlReturnsTrue() {
			boolean result = TermReadSvcUtil.isLoincUnversionedValueSet(
				"http://loinc.org/vs/radiology-playbook");
			assertThat(result).isTrue();
		}

	}


	@Nested
	public class IsLoincNotGenericUnversionedCodeSystem {

		@Test
		void doesntContainLoincReturnsFalse() {
			boolean result = TermReadSvcUtil.isLoincUnversionedCodeSystem(
				"http://loin-c.org");
			assertThat(result).isFalse();
		}

		@Test
		void hasVersionDelimiterReturnsFalse() {
			boolean result = TermReadSvcUtil.isLoincUnversionedCodeSystem(
				"http://loinc.org|v2.68");
			assertThat(result).isFalse();
		}

		@Test
		void containsLoincNadNoVersionDelimiterReturnsTrue() {
			boolean result = TermReadSvcUtil.isLoincUnversionedCodeSystem(
				"http://loinc.org");
			assertThat(result).isTrue();
		}

	}

}
