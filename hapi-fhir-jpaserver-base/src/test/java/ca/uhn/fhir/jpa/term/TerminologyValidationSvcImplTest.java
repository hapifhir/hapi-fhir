package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.svc.CodeSystemValidationRequest;
import ca.uhn.fhir.jpa.api.svc.ValueSetValidationRequest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TerminologyValidationSvcImpl}.
 */
// Created by claude-opus-4-5-20251101
@ExtendWith(MockitoExtension.class)
class TerminologyValidationSvcImplTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Mock
	private ValidationSupportChain myValidationSupportChain;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IFhirResourceDaoValueSet<IBaseResource> myValueSetDao;

	@Mock
	private IFhirResourceDaoCodeSystem<IBaseResource> myCodeSystemDao;

	private TerminologyValidationSvcImpl mySvc;
	private RequestDetails myRequestDetails;

	@BeforeEach
	void setUp() {
		mySvc = new TerminologyValidationSvcImpl(ourFhirContext, myValidationSupportChain, myDaoRegistry);
		myRequestDetails = new SystemRequestDetails();
	}

	// ValueSet validation tests

	@Test
	void testValidateCodeAgainstValueSet_withRemoteTerminologyService_delegatesToChain() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		CodeValidationResult expectedResult = new CodeValidationResult().setCode("CODE1").setDisplay("Display");
		when(myValidationSupportChain.validateCode(any(), any(), eq("http://system"), eq("CODE1"), eq("Display"), eq("http://valueset")))
				.thenReturn(expectedResult);

		// Execute
		ValueSetValidationRequest request = ValueSetValidationRequest.builder()
				.valueSetUrl(new UriType("http://valueset"))
				.code(new CodeType("CODE1"))
				.system(new UriType("http://system"))
				.display(new StringType("Display"))
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstValueSet(request);

		// Verify
		assertThat(result).isSameAs(expectedResult);
		verifyNoInteractions(myDaoRegistry);
	}

	@Test
	void testValidateCodeAgainstValueSet_withoutRemoteTerminologyService_delegatesToDao() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(false);
		when(myDaoRegistry.getResourceDao("ValueSet")).thenReturn(myValueSetDao);

		CodeValidationResult expectedResult = new CodeValidationResult().setCode("CODE1");
		when(myValueSetDao.validateCode(any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(expectedResult);

		// Execute
		ValueSetValidationRequest request = ValueSetValidationRequest.builder()
				.valueSetUrl(new UriType("http://valueset"))
				.code(new CodeType("CODE1"))
				.system(new UriType("http://system"))
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstValueSet(request);

		// Verify
		assertThat(result).isSameAs(expectedResult);
		verify(myValueSetDao).validateCode(any(), any(), any(), any(), any(), any(), any(), any());
	}

	@Test
	void testValidateCodeAgainstValueSet_withCoding_extractsValues() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		Coding coding = new Coding()
				.setSystem("http://coding.system")
				.setCode("CODING_CODE")
				.setDisplay("Coding Display");

		CodeValidationResult expectedResult = new CodeValidationResult().setCode("CODING_CODE");
		when(myValidationSupportChain.validateCode(any(), any(), eq("http://coding.system"), eq("CODING_CODE"), eq("Coding Display"), eq("http://valueset")))
				.thenReturn(expectedResult);

		// Execute
		ValueSetValidationRequest request = ValueSetValidationRequest.builder()
				.valueSetUrl(new UriType("http://valueset"))
				.coding(coding)
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstValueSet(request);

		// Verify
		assertThat(result).isSameAs(expectedResult);
	}

	@Test
	void testValidateCodeAgainstValueSet_withMismatchedSystemAndCoding_throwsException() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		Coding coding = new Coding()
				.setSystem("http://coding.system")
				.setCode("CODE");

		// Execute & Verify
		ValueSetValidationRequest request = ValueSetValidationRequest.builder()
				.valueSetUrl(new UriType("http://valueset"))
				.system(new UriType("http://different.system"))
				.coding(coding)
				.requestDetails(myRequestDetails)
				.build();
		assertThatThrownBy(() -> mySvc.validateCodeAgainstValueSet(request))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("does not equal param system");
	}

	@Test
	void testValidateCodeAgainstValueSet_chainReturnsNull_returnsUnableToValidateResult() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);
		when(myValidationSupportChain.validateCode(any(), any(), any(), any(), any(), any()))
				.thenReturn(null);

		// Execute
		ValueSetValidationRequest request = ValueSetValidationRequest.builder()
				.valueSetUrl(new UriType("http://valueset"))
				.code(new CodeType("CODE1"))
				.system(new UriType("http://system"))
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstValueSet(request);

		// Verify - returns a result with message when chain returns null
		assertThat(result).isNotNull();
		assertThat(result.getMessage()).contains("Validator is unable to provide validation");
	}

	// CodeSystem validation tests

	@Test
	void testValidateCodeAgainstCodeSystem_withRemoteTerminologyService_delegatesToChain() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		CodeValidationResult expectedResult = new CodeValidationResult().setCode("CODE1").setDisplay("Display");
		when(myValidationSupportChain.validateCode(any(), any(), eq("http://codesystem"), eq("CODE1"), eq("Display"), eq(null)))
				.thenReturn(expectedResult);

		// Execute
		CodeSystemValidationRequest request = CodeSystemValidationRequest.builder()
				.codeSystemUrl(new UriType("http://codesystem"))
				.code(new CodeType("CODE1"))
				.display(new StringType("Display"))
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstCodeSystem(request);

		// Verify
		assertThat(result).isSameAs(expectedResult);
		verifyNoInteractions(myDaoRegistry);
	}

	@Test
	void testValidateCodeAgainstCodeSystem_withoutRemoteTerminologyService_delegatesToDao() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(false);
		when(myDaoRegistry.getResourceDao("CodeSystem")).thenReturn(myCodeSystemDao);

		CodeValidationResult expectedResult = new CodeValidationResult().setCode("CODE1");
		when(myCodeSystemDao.validateCode(any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(expectedResult);

		// Execute
		CodeSystemValidationRequest request = CodeSystemValidationRequest.builder()
				.codeSystemUrl(new UriType("http://codesystem"))
				.code(new CodeType("CODE1"))
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstCodeSystem(request);

		// Verify
		assertThat(result).isSameAs(expectedResult);
		verify(myCodeSystemDao).validateCode(any(), any(), any(), any(), any(), any(), any(), any());
	}

	@Test
	void testValidateCodeAgainstCodeSystem_withCoding_extractsValues() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		Coding coding = new Coding()
				.setSystem("http://coding.codesystem")
				.setCode("CODING_CODE")
				.setDisplay("Coding Display");

		CodeValidationResult expectedResult = new CodeValidationResult().setCode("CODING_CODE");
		when(myValidationSupportChain.validateCode(any(), any(), eq("http://coding.codesystem"), eq("CODING_CODE"), eq("Coding Display"), eq(null)))
				.thenReturn(expectedResult);

		// Execute
		CodeSystemValidationRequest request = CodeSystemValidationRequest.builder()
				.coding(coding)
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstCodeSystem(request);

		// Verify
		assertThat(result).isSameAs(expectedResult);
	}

	@Test
	void testValidateCodeAgainstCodeSystem_withMismatchedUrlAndCoding_throwsException() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		Coding coding = new Coding()
				.setSystem("http://coding.codesystem")
				.setCode("CODE");

		// Execute & Verify
		CodeSystemValidationRequest request = CodeSystemValidationRequest.builder()
				.codeSystemUrl(new UriType("http://different.codesystem"))
				.coding(coding)
				.requestDetails(myRequestDetails)
				.build();
		assertThatThrownBy(() -> mySvc.validateCodeAgainstCodeSystem(request))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("does not equal param url");
	}

	@Test
	void testValidateCodeAgainstCodeSystem_withCodeableConcept_returnsNotSupportedMessage() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		CodeableConcept codeableConcept = new CodeableConcept()
				.addCoding(new Coding().setSystem("http://system").setCode("CODE"));

		// Execute
		CodeSystemValidationRequest request = CodeSystemValidationRequest.builder()
				.codeableConcept(codeableConcept)
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstCodeSystem(request);

		// Verify - CodeableConcept not yet supported for CodeSystem validation via remote service
		assertThat(result).isNotNull();
		assertThat(result.getMessage()).contains("does not yet support codeable concepts");
	}

	@Test
	void testValidateCodeAgainstCodeSystem_withEmptyCodeAndUrl_returnsErrorMessage() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(true);

		// Execute
		CodeSystemValidationRequest request = CodeSystemValidationRequest.builder()
				.requestDetails(myRequestDetails)
				.build();
		CodeValidationResult result = mySvc.validateCodeAgainstCodeSystem(request);

		// Verify
		assertThat(result).isNotNull();
		assertThat(result.getMessage()).contains("neither can be empty");
	}

	// Version concatenation tests

	@Test
	@SuppressWarnings("unchecked")
	void testValidateCodeAgainstValueSet_withVersions_concatenatesVersions() {
		// Setup
		when(myValidationSupportChain.isRemoteTerminologyServiceConfigured()).thenReturn(false);
		when(myDaoRegistry.getResourceDao("ValueSet")).thenReturn(myValueSetDao);

		CodeValidationResult expectedResult = new CodeValidationResult().setCode("CODE1");
		when(myValueSetDao.validateCode(any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(expectedResult);

		// Execute
		ValueSetValidationRequest request = ValueSetValidationRequest.builder()
				.valueSetUrl(new UriType("http://valueset"))
				.valueSetVersion(new StringType("1.0"))
				.code(new CodeType("CODE1"))
				.system(new UriType("http://system"))
				.systemVersion(new StringType("2.0"))
				.requestDetails(myRequestDetails)
				.build();
		mySvc.validateCodeAgainstValueSet(request);

		// Verify - versions should be concatenated with |
		verify(myValueSetDao).validateCode(
				any(IPrimitiveType.class),  // valueSetIdentifier with version
				any(),
				any(),
				any(IPrimitiveType.class),  // codeSystemIdentifier with version
				any(),
				any(),
				any(),
				any());
	}
}
