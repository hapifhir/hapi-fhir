package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r4.model.CodeSystem;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
		 * Simulate a local TermCodeSystem row for the URL. This mirrors loader-populated (LOINC),
		 * delta-populated, and empty-delta-target NOTPRESENT CodeSystems, where the local DB is
		 * authoritative even though content=not-present.
		 */
		void stubLocalTermCodeSystemExists() {
			TermCodeSystem cs = new TermCodeSystem();
			lenient().when(myCodeSystemDao.findByCodeSystemUri(UCUM_SYSTEM_URL)).thenReturn(cs);
		}

		void stubFindCodeEmpty() {
			doReturn(Optional.empty()).when(mySpiedSvc).findCode(any(), any());
		}

		void stubCodeSystemContent(CodeSystem.CodeSystemContentMode theContent) {
			CodeSystem cs = new CodeSystem();
			cs.setUrl(UCUM_SYSTEM_URL);
			cs.setContent(theContent);
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

		LookupCodeResult callLookupCode() {
			return mySpiedSvc.lookupCode(
					myValidationSupportContext, new LookupCodeRequest(UCUM_SYSTEM_URL, UCUM_CODE));
		}

		void stubCodeSystemResource(org.hl7.fhir.instance.model.api.IBaseResource theResource) {
			lenient().when(myRootValidationSupport.fetchCodeSystem(UCUM_SYSTEM_URL)).thenReturn(theResource);
		}
	}

	@Test
	void validateCode_withNotPresentCodeSystemAndMissingCode_returnsNullToAllowChainFallThrough() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		fixture.stubFindCodeEmpty();

		CodeValidationResult result = fixture.callValidateCode();

		assertThat(result)
				.as("NOTPRESENT CodeSystem must not short-circuit the ValidationSupportChain; validateCode should return null so algorithmic validators (e.g. UCUM) can be consulted")
				.isNull();
	}

	@Test
	void validateCode_withCompleteCodeSystemAndMissingCode_returnsCodeNotFoundError() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		fixture.stubFindCodeEmpty();

		CodeValidationResult result = fixture.callValidateCode();

		assertThat(result)
				.as("COMPLETE CodeSystem with a genuinely missing code must still return a 'code not found' validation error")
				.isNotNull();
		assertThat(result.getSeverityCode()).isEqualToIgnoringCase("error");
	}

	@Test
	void lookupCode_withNotPresentCodeSystemAndMissingCode_returnsNullToAllowChainFallThrough() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		fixture.stubFindCodeEmpty();

		LookupCodeResult result = fixture.callLookupCode();

		assertThat(result)
				.as("NOTPRESENT CodeSystem must not short-circuit the ValidationSupportChain; lookupCode should return null so algorithmic validators (e.g. UCUM) can be consulted")
				.isNull();
	}

	@Test
	void lookupCode_withCompleteCodeSystemAndMissingCode_returnsNotFoundResult() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		fixture.stubFindCodeEmpty();

		LookupCodeResult result = fixture.callLookupCode();

		assertThat(result)
				.as("COMPLETE CodeSystem with a genuinely missing code must still return a non-null LookupCodeResult with found=false")
				.isNotNull();
		assertThat(result.isFound()).isFalse();
	}

	@Test
	void validateCode_withNotPresentCodeSystemBackedByLocalTermCodeSystem_returnsCodeNotFoundError() {
		// Loader-populated (LOINC), delta-populated, and empty-delta-target CodeSystems keep
		// content=NOT_PRESENT but the local term DB is authoritative. A missing code must surface
		// "Unknown code", not fall through to the validation-support chain.
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		fixture.stubFindCodeEmpty();
		fixture.stubLocalTermCodeSystemExists();

		CodeValidationResult result = fixture.callValidateCode();

		assertThat(result)
				.as("NOTPRESENT CodeSystem backed by a local TermCodeSystem row must surface 'code not found' rather than short-circuit to null")
				.isNotNull();
		assertThat(result.getSeverityCode()).isEqualToIgnoringCase("error");
	}

	@Test
	void lookupCode_withNotPresentCodeSystemBackedByLocalTermCodeSystem_returnsNotFoundResult() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		fixture.stubCodeSystemContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		fixture.stubFindCodeEmpty();
		fixture.stubLocalTermCodeSystemExists();

		LookupCodeResult result = fixture.callLookupCode();

		assertThat(result)
				.as("NOTPRESENT CodeSystem backed by a local TermCodeSystem row must return a LookupCodeResult with found=false rather than null")
				.isNotNull();
		assertThat(result.isFound()).isFalse();
	}

	@Test
	void validateCode_whenCanonicalizerReturnsNull_returnsCodeNotFoundErrorWithoutNpe() {
		ValidateCodeFixture fixture = new ValidateCodeFixture();
		// Simulate a CodeSystem resource that VersionCanonicalizer cannot canonicalize — e.g. a
		// pathological input where codeSystemToCanonical returns null. We stub the canonicalizer
		// on the spy to return null and verify the guard returns "not found" rather than NPEing.
		CodeSystem rawCodeSystem = new CodeSystem();
		rawCodeSystem.setUrl(UCUM_SYSTEM_URL);
		fixture.stubCodeSystemResource(rawCodeSystem);
		fixture.stubFindCodeEmpty();

		VersionCanonicalizer nullReturningCanonicalizer = mock(VersionCanonicalizer.class);
		lenient().when(nullReturningCanonicalizer.codeSystemToCanonical(any())).thenReturn(null);
		ReflectionTestUtils.setField(fixture.mySpiedSvc, "myVersionCanonicalizer", nullReturningCanonicalizer);

		CodeValidationResult result = fixture.callValidateCode();

		assertThat(result)
				.as("When VersionCanonicalizer.codeSystemToCanonical returns null, validateCode must fall through to the normal 'code not found' result rather than throwing NPE")
				.isNotNull();
		assertThat(result.getSeverityCode()).isEqualToIgnoringCase("error");
	}
}
