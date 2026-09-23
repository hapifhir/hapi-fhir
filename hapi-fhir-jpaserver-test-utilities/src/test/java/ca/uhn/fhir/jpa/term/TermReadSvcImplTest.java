package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValidateCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

// Created by claude-opus-4-7
@ExtendWith(MockitoExtension.class)
class TermReadSvcImplTest {

	// These constants are named for the originally-reported UCUM bug scenario but the tests
	// only exercise NOTPRESENT fall-through logic — the specific URL/code values are immaterial.
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

	static class ValidateCodeFixture {

		final TermReadSvcImpl mySpiedSvc;
		final PlatformTransactionManager myTxManager;
		final IValidationSupport myRootValidationSupport;
		final ValidationSupportContext myValidationSupportContext;
		final ITermCodeSystemDao myCodeSystemDao;

		ValidateCodeFixture() {
			mySpiedSvc = spy(new TermReadSvcImpl());
			myTxManager = mock(PlatformTransactionManager.class);
			myRootValidationSupport = mock(IValidationSupport.class);
			myValidationSupportContext = new ValidationSupportContext(myRootValidationSupport);
			myCodeSystemDao = mock(ITermCodeSystemDao.class);

			FhirContext fhirContext = FhirContext.forR4Cached();
			ReflectionTestUtils.setField(mySpiedSvc, "myTransactionManager", myTxManager);
			ReflectionTestUtils.setField(mySpiedSvc, "myTxTemplate", new TransactionTemplate(myTxManager));
			ReflectionTestUtils.setField(mySpiedSvc, "myContext", fhirContext);
			ReflectionTestUtils.setField(mySpiedSvc, "myStorageSettings", new JpaStorageSettings());
			ReflectionTestUtils.setField(mySpiedSvc, "myVersionCanonicalizer", new VersionCanonicalizer(fhirContext));
			ReflectionTestUtils.setField(mySpiedSvc, "myCodeSystemDao", myCodeSystemDao);

			TransactionStatus status = new SimpleTransactionStatus();
			lenient().when(myTxManager.getTransaction(any())).thenReturn(status);
		}

		/**
		 * Makes the code findable only under the given code system identifier, so the result of validateCode
		 * says which version was actually looked in.
		 */
		void stubCodeFoundOnlyIn(String theCodeSystemIdentifier) {
			TermConcept concept = new TermConcept().setCode(UCUM_CODE).setDisplay(UCUM_CODE);
			doAnswer(invocation -> theCodeSystemIdentifier.equals(invocation.getArgument(0))
									&& UCUM_CODE.equals(invocation.getArgument(1))
							? Optional.of(concept)
							: Optional.empty())
					.when(mySpiedSvc)
					.findCode(any(), any());
		}

		void stubCodeSystemContent(CodeSystem.CodeSystemContentMode theContent) {
			CodeSystem cs = new CodeSystem();
			cs.setUrl(UCUM_SYSTEM_URL);
			cs.setContent(theContent);
			lenient().when(myRootValidationSupport.fetchCodeSystem(UCUM_SYSTEM_URL)).thenReturn(cs);
		}

		CodeValidationResult callValidateCode(String theCodeSystemVersion) {
			return callValidateCode(UCUM_SYSTEM_URL, theCodeSystemVersion);
		}

		CodeValidationResult callValidateCode(String theCodeSystem, String theCodeSystemVersion) {
			return mySpiedSvc.validateCode(
					myValidationSupportContext,
					new ConceptValidationOptions(),
					new ValidateCodeRequest(theCodeSystem, theCodeSystemVersion, UCUM_CODE, null, null));
		}
	}

	@Test
	void validateCode_withCodeSystemVersion_validatesAgainstThatVersion() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		// this service identifies a code system version as "url|version", so the code is only findable there
		fixture.stubCodeFoundOnlyIn(UCUM_SYSTEM_URL + "|1.0.0");

		CodeValidationResult result = fixture.callValidateCode("1.0.0");

		assertThat(result).isNotNull();
		assertThat(result.isOk()).isTrue();
		assertThat(result.getCode()).isEqualTo(UCUM_CODE);
	}

	@Test
	void validateCode_withACodeSystemVersionWhichDoesNotHaveTheCode_returnsCodeNotFoundError() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		fixture.stubCodeFoundOnlyIn(UCUM_SYSTEM_URL + "|1.0.0");

		CodeValidationResult result = fixture.callValidateCode("2.0.0");

		assertThat(result).isNotNull();
		assertThat(result.getSeverityCode()).isEqualToIgnoringCase("error");
	}

	@Test
	void validateCode_withoutACodeSystemVersion_validatesAgainstTheCurrentVersion() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		// findable under the bare url, which is how this service names whichever version is current
		fixture.stubCodeFoundOnlyIn(UCUM_SYSTEM_URL);

		CodeValidationResult result = fixture.callValidateCode(null);

		assertThat(result).isNotNull();
		assertThat(result.isOk()).isTrue();
		assertThat(result.getCode()).isEqualTo(UCUM_CODE);
	}

	@Test
	void validateCode_withCodeSystemAlreadyCarryingTheSameVersion_doesNotAppendTheVersionTwice() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		fixture.stubCodeFoundOnlyIn(UCUM_SYSTEM_URL + "|1.0.0");

		CodeValidationResult result = fixture.callValidateCode(UCUM_SYSTEM_URL + "|1.0.0", "1.0.0");

		assertThat(result).isNotNull();
		assertThat(result.isOk()).isTrue();
	}

	/**
	 * ValueSet.url is optional, so a caller may pass one by value with no url. There is then no canonical to
	 * look the value set up by, and nothing to validate against.
	 */
	@Test
	void validateCodeInValueSet_valueSetWithoutAUrl_returnsNull() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();

		CodeValidationResult result = fixture.mySpiedSvc.validateCodeInValueSet(
				fixture.myValidationSupportContext,
				new ConceptValidationOptions(),
				UCUM_SYSTEM_URL,
				UCUM_CODE,
				null,
				new ValueSet());

		assertThat(result).isNull();
	}

	/**
	 * A code system canonical naming one version and a code system version naming another are contradictory.
	 * Picking either one silently is how a caller ends up validating against a version it did not ask for.
	 */
	@Test
	void validateCode_withCodeSystemCarryingAConflictingVersion_isRejected() {
		// no lookup is stubbed: the conflict is rejected before the code system is consulted at all
		ValidateCodeFixture fixture = new ValidateCodeFixture();

		assertThatThrownBy(() -> fixture.callValidateCode(UCUM_SYSTEM_URL + "|1.0.0", "2.0.0"))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("does not match expected version: 2.0.0");
	}
}
