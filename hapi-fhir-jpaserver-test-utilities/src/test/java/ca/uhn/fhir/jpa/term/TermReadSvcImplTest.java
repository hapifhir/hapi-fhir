// Created by claude-opus-4-7
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class TermReadSvcImplTest {

	private static final String UCUM_SYSTEM_URL = "http://unitsofmeasure.org";
	private static final String UCUM_CODE = "mg/dL";

	private final TermReadSvcImpl mySvc = new TermReadSvcImpl();

	@Test
	void applyFilterMatchWords() {
		assertTrue(mySvc.applyFilter("abc def", "abc def"));
		assertTrue(mySvc.applyFilter("abc def", "abc"));
		assertTrue(mySvc.applyFilter("abc def", "def"));
		assertTrue(mySvc.applyFilter("abc def ghi", "abc def ghi"));
		assertTrue(mySvc.applyFilter("abc def ghi", "abc def"));
		assertTrue(mySvc.applyFilter("abc def ghi", "def ghi"));
	}

	@Test
	void applyFilterSentenceStart() {
		assertTrue(mySvc.applyFilter("manifold", "man"));
		assertTrue(mySvc.applyFilter("manifest destiny", "man"));
		assertTrue(mySvc.applyFilter("deep sight", "deep sigh"));
		assertTrue(mySvc.applyFilter("sink cottage", "sink cot"));
	}

	@Test
	void applyFilterSentenceEnd() {
		assertFalse(mySvc.applyFilter("rescue", "cue"));
		assertFalse(mySvc.applyFilter("very picky", "icky"));
	}

	@Test
	void applyFilterSubwords() {
		assertFalse(mySvc.applyFilter("splurge", "urge"));
		assertFalse(mySvc.applyFilter("sink cottage", "ink cot"));
		assertFalse(mySvc.applyFilter("sink cottage", "ink cottage"));
		assertFalse(mySvc.applyFilter("clever jump startle", "lever jump star"));
	}

	/**
	 * Tests for GH-7796: TermReadSvcImpl.validateCode must honour CodeSystem.content=not-present
	 * and return null (fall-through) instead of a "code not found" error, so that downstream
	 * algorithmic validators (e.g. UCUM) in the ValidationSupportChain can be consulted.
	 */
	static class ValidateCodeFixture {

		final TermReadSvcImpl mySpiedSvc;
		final PlatformTransactionManager myTxManager;
		final IValidationSupport myRootValidationSupport;
		final ValidationSupportContext myValidationSupportContext;

		ValidateCodeFixture() {
			mySpiedSvc = spy(new TermReadSvcImpl());
			myTxManager = mock(PlatformTransactionManager.class);
			myRootValidationSupport = mock(IValidationSupport.class);
			myValidationSupportContext = new ValidationSupportContext(myRootValidationSupport);

			FhirContext fhirContext = FhirContext.forR4Cached();
			ReflectionTestUtils.setField(mySpiedSvc, "myTransactionManager", myTxManager);
			ReflectionTestUtils.setField(mySpiedSvc, "myContext", fhirContext);
			ReflectionTestUtils.setField(mySpiedSvc, "myStorageSettings", new JpaStorageSettings());
			ReflectionTestUtils.setField(mySpiedSvc, "myVersionCanonicalizer", new VersionCanonicalizer(fhirContext));

			// Make TransactionTemplate just run the callback synchronously
			TransactionStatus status = new SimpleTransactionStatus();
			lenient().when(myTxManager.getTransaction(any())).thenReturn(status);
		}

		void stubFindCodeEmpty() {
			doReturn(Optional.empty()).when(mySpiedSvc).findCode(any(), any());
		}

		void stubCodeSystemContent(CodeSystem.CodeSystemContentMode theContent) {
			CodeSystem cs = new CodeSystem();
			cs.setUrl(UCUM_SYSTEM_URL);
			cs.setContent(theContent);
			// Lenient — master's validateCode() short-circuits before consulting the CodeSystem's
			// content (that is the bug). After the fix this stub will be used.
			lenient().when(myRootValidationSupport.fetchCodeSystem(UCUM_SYSTEM_URL)).thenReturn(cs);
		}

		CodeValidationResult callValidateCode() {
			return mySpiedSvc.validateCode(
					myValidationSupportContext,
					new ConceptValidationOptions(),
					UCUM_SYSTEM_URL,
					UCUM_CODE,
					null,
					null);
		}
	}

	/**
	 * PRIMARY TDD Red: NOTPRESENT CodeSystem + findCode miss => must return null (fall-through).
	 * On master this FAILS because the method returns a "code not found" error instead.
	 */
	@Test
	void validateCode_withNotPresentCodeSystemAndMissingCode_returnsNullToAllowChainFallThrough() {
		ValidateCodeFixture f = new ValidateCodeFixture();
		f.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		f.stubFindCodeEmpty();

		CodeValidationResult result = f.callValidateCode();

		assertThat(result)
				.as("NOTPRESENT CodeSystem must not short-circuit the ValidationSupportChain; validateCode should return null so algorithmic validators (e.g. UCUM) can be consulted")
				.isNull();
	}

	/**
	 * Adjacent regression guard: COMPLETE CodeSystem + findCode miss => must still return the
	 * "code not found" error (unchanged behavior). Should PASS on master and continue to PASS after the fix.
	 */
	@Test
	void validateCode_withCompleteCodeSystemAndMissingCode_returnsCodeNotFoundError() {
		ValidateCodeFixture f = new ValidateCodeFixture();
		f.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		f.stubFindCodeEmpty();

		CodeValidationResult result = f.callValidateCode();

		assertThat(result)
				.as("COMPLETE CodeSystem with a genuinely missing code must still return a 'code not found' validation error")
				.isNotNull();
		assertThat(result.getMessage())
				.contains("Unable to validate code " + UCUM_SYSTEM_URL + "#" + UCUM_CODE);
		assertThat(result.getSeverityCode()).isEqualToIgnoringCase("error");
	}
}
