package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.common.hapi.validation.IRemoteTerminologyValidateCodeTest;
import org.hl7.fhir.common.hapi.validation.IValidationProviders;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport.ERROR_CODE_UNKNOWN_CODE_IN_CODE_SYSTEM;
import static org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport.ERROR_CODE_UNKNOWN_CODE_IN_VALUE_SET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Version specific tests for validation using RemoteTerminologyValidationSupport.
 * The tests in this class simulate the call to a remote server and therefore, only tests the code in
 * the RemoteTerminologyServiceValidationSupport itself. The remote client call is simulated using the test providers.
 * @see RemoteTerminologyServiceValidationSupport
 *
 * Other operations are tested separately.
 * @see RemoteTerminologyLookupCodeR4Test
 * @see RemoteTerminologyServiceValidationSupportR4Test
 */
public class RemoteTerminologyValidateCodeR4Test implements IRemoteTerminologyValidateCodeTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);
	private IValidateCodeProvidersR4.MyCodeSystemProviderR4 myCodeSystemProvider;
	private IValidateCodeProvidersR4.MyValueSetProviderR4 myValueSetProvider;
	private RemoteTerminologyServiceValidationSupport mySvc;
	private String myCodeSystemError, myValueSetError;

	@BeforeEach
	public void before() {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		myCodeSystemError = ourCtx.getLocalizer().getMessage(
				RemoteTerminologyServiceValidationSupport.class,
				ERROR_CODE_UNKNOWN_CODE_IN_CODE_SYSTEM, IValidationProviders.CODE_SYSTEM, IValidationProviders.CODE, baseUrl, IValidationProviders.ERROR_MESSAGE);
		myValueSetError = ourCtx.getLocalizer().getMessage(
				RemoteTerminologyServiceValidationSupport.class,
				ERROR_CODE_UNKNOWN_CODE_IN_VALUE_SET, IValidationProviders.CODE_SYSTEM, IValidationProviders.CODE, IValidationProviders.VALUE_SET_URL, baseUrl, IValidationProviders.ERROR_MESSAGE);
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(false).setLogRequestSummary(true).setLogResponseSummary(true));
		myCodeSystemProvider = new IValidateCodeProvidersR4.MyCodeSystemProviderR4();
		myValueSetProvider = new IValidateCodeProvidersR4.MyValueSetProviderR4();
		ourRestfulServerExtension.getRestfulServer().registerProviders(myCodeSystemProvider, myValueSetProvider);
	}

	@AfterEach
	public void after() {
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		ourRestfulServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
		ourRestfulServerExtension.getRestfulServer().unregisterProvider(myCodeSystemProvider);
	}

	@Override
	public RemoteTerminologyServiceValidationSupport getService() {
		return mySvc;
	}

	@Override
	public IValidationProviders.IMyCodeSystemProvider getCodeSystemProvider() {
		return myCodeSystemProvider;
	}

	@Override
	public IValidationProviders.IMyValueSetProvider getValueSetProvider() {
		return myValueSetProvider;
	}

	@Override
	public String getCodeSystemError() {
		return myCodeSystemError;
	}

	@Override
	public String getValueSetError() {
		return myValueSetError;
	}

	@Override
	public IBaseOperationOutcome getCodeSystemInvalidCodeOutcome() {
		return ClasspathUtil.loadResource(getService().getFhirContext(), OperationOutcome.class, "/terminology/OperationOutcome-CodeSystem-invalid-code.json");
	}

	@Override
	public IBaseOperationOutcome getValueSetInvalidCodeOutcome() {
		return ClasspathUtil.loadResource(getService().getFhirContext(), OperationOutcome.class, "/terminology/OperationOutcome-ValueSet-invalid-code.json");
	}

	@Override
	public List<IValidationSupport.CodeValidationIssue> getCodeValidationIssues(IBaseOperationOutcome theOperationOutcome) {
		return ((OperationOutcome)theOperationOutcome).getIssue().stream()
				.map(issueComponent -> new IValidationSupport.CodeValidationIssue(
						issueComponent.getDetails().getText(),
						IValidationSupport.IssueSeverity.ERROR,
						/* assume issue type is OperationOutcome.IssueType#CODEINVALID as it is the only match */
						IValidationSupport.CodeValidationIssueCode.INVALID,
						IValidationSupport.CodeValidationIssueCoding.INVALID_CODE))
				.toList();
	}

	@Test
	void validateCodeInValueSet_success() {
		createValueSetReturnParameters(true, IValidationProviders.DISPLAY, null, null);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(IValidationProviders.VALUE_SET_URL);

		CodeValidationResult outcome = mySvc.validateCodeInValueSet(null, new ConceptValidationOptions(), IValidationProviders.CODE_SYSTEM, IValidationProviders.CODE, IValidationProviders.DISPLAY, valueSet);
		assertNotNull(outcome);
		assertEquals(IValidationProviders.CODE, outcome.getCode());
		assertEquals(IValidationProviders.DISPLAY, outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());

		assertEquals(IValidationProviders.CODE, myValueSetProvider.getCode());
		assertEquals(IValidationProviders.DISPLAY, myValueSetProvider.getDisplay());
		assertEquals(IValidationProviders.VALUE_SET_URL, myValueSetProvider.getValueSet());
	}

	@Override
	public Parameters createParameters(Boolean theResult, String theDisplay, String theMessage, IBaseResource theIssuesResource) {
		Parameters parameters = new Parameters()
				.addParameter("code", IValidationProviders.CODE)
				.addParameter("system", IValidationProviders.CODE_SYSTEM)
				.addParameter("version", IValidationProviders.CODE_SYSTEM_VERSION)
				.addParameter("display", theDisplay)
				.addParameter("message", theMessage);
		if (theResult != null) {
			parameters.addParameter("result", theResult);
		}
		if (theIssuesResource != null) {
			parameters.addParameter().setName("issues").setResource((Resource) theIssuesResource);
		}
		return parameters;
	}

	/**
	 * Remote terminology services can be used to validate codes when code system is present,
	 * even when inferSystem is true
	 */
	@Nested
	class ExtractCodeSystemFromValueSet {

		@Test
		void validateCodeInValueSet_uniqueComposeInclude() {
			createValueSetReturnParameters(true, IValidationProviders.DISPLAY, null, null);

			ValueSet valueSet = new ValueSet();
			valueSet.setUrl(IValidationProviders.VALUE_SET_URL);
			String systemUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
			valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
					Collections.singletonList(new ValueSet.ConceptSetComponent().setSystem(systemUrl)) ));

			CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
					new ConceptValidationOptions().setInferSystem(true), null, IValidationProviders.CODE, IValidationProviders.DISPLAY, valueSet);

			// validate service doesn't return error message (as when no code system is present)
			assertNotNull(outcome);
			assertNull(outcome.getMessage());
			assertTrue(outcome.isOk());
		}

		@Nested
		public class MultiComposeIncludeValueSet {

			public static Stream<Arguments> getRemoteTerminologyServerExceptions() {
				return Stream.of(
						Arguments.of(new ResourceNotFoundException("System Not Present"), "404 Not Found: System Not Present"),
						Arguments.of(new InvalidRequestException("Invalid Request"), "400 Bad Request: Invalid Request")
				);
			}

			@ParameterizedTest
			@MethodSource(value = "getRemoteTerminologyServerExceptions")
			void validateCodeInValueSet_systemNotPresent_returnsValidationResultWithError(Exception theException, String theServerMessage) {
				myValueSetProvider.setException(theException);
				createValueSetReturnParameters(true, IValidationProviders.DISPLAY, null, null);

				ValueSet valueSet = new ValueSet();
				valueSet.setUrl(IValidationProviders.VALUE_SET_URL);
				valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
						Lists.newArrayList(new ValueSet.ConceptSetComponent(), new ValueSet.ConceptSetComponent())));

				CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
						new ConceptValidationOptions().setInferSystem(true), null, IValidationProviders.CODE, IValidationProviders.DISPLAY, valueSet);

				String unknownCodeForValueSetError = "Unknown code \"null#CODE\" for ValueSet with URL \"http://value.set/url\". The Remote Terminology server http://";
				verifyErrorResultFromException(outcome, unknownCodeForValueSetError, theServerMessage);
			}


			@ParameterizedTest
			@MethodSource(value = "getRemoteTerminologyServerExceptions")
			void validateCodeInValueSet_systemPresentCodeNotPresent_returnsValidationResultWithError(Exception theException, String theServerMessage) {
				myValueSetProvider.setException(theException);
				createValueSetReturnParameters(true, IValidationProviders.DISPLAY, null, null);

				ValueSet valueSet = new ValueSet();
				valueSet.setUrl(IValidationProviders.VALUE_SET_URL);
				String systemUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
				String systemUrl2 = "http://hl7.org/fhir/ValueSet/other-valueset";
				valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
						Lists.newArrayList(
								new ValueSet.ConceptSetComponent().setSystem(systemUrl),
								new ValueSet.ConceptSetComponent().setSystem(systemUrl2))));

				CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
						new ConceptValidationOptions().setInferSystem(true), null, IValidationProviders.CODE, IValidationProviders.DISPLAY, valueSet);

				String unknownCodeForValueSetError = "Unknown code \"null#CODE\" for ValueSet with URL \"http://value.set/url\". The Remote Terminology server http://";
				verifyErrorResultFromException(outcome, unknownCodeForValueSetError, theServerMessage);
			}


			@Test
			void validateCodeInValueSet_systemPresentCodePresentValidatesOKNoVersioned() {
				createValueSetReturnParameters(true, IValidationProviders.DISPLAY, null, null);

				ValueSet valueSet = new ValueSet();
				valueSet.setUrl(IValidationProviders.VALUE_SET_URL);
				String systemUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
				String systemUrl2 = "http://hl7.org/fhir/ValueSet/other-valueset";
				valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
						Lists.newArrayList(
								new ValueSet.ConceptSetComponent().setSystem(systemUrl),
								new ValueSet.ConceptSetComponent().setSystem(systemUrl2).setConcept(
										Lists.newArrayList(
												new ValueSet.ConceptReferenceComponent().setCode("not-the-code"),
												new ValueSet.ConceptReferenceComponent().setCode(IValidationProviders.CODE) )
								)) ));

				TestClientInterceptor requestInterceptor = new TestClientInterceptor();
				mySvc.addClientInterceptor(requestInterceptor);

				CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
						new ConceptValidationOptions().setInferSystem(true), null, IValidationProviders.CODE, IValidationProviders.DISPLAY, valueSet);

				assertNotNull(outcome);
				assertEquals(systemUrl2, requestInterceptor.getCapturedSystemParameter());
			}


			@Test
			void validateCodeInValueSet_systemPresentCodePresentValidatesOKVersioned() {
				createValueSetReturnParameters(true, IValidationProviders.DISPLAY, null, null);

				ValueSet valueSet = new ValueSet();
				valueSet.setUrl(IValidationProviders.VALUE_SET_URL);
				String systemUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
				String systemVersion = "3.0.2";
				String systemUrl2 = "http://hl7.org/fhir/ValueSet/other-valueset";
				String system2Version = "4.0.1";
				valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
						Lists.newArrayList(
								new ValueSet.ConceptSetComponent().setSystem(systemUrl).setVersion(systemVersion),
								new ValueSet.ConceptSetComponent().setSystem(systemUrl2).setVersion(system2Version).setConcept(
										Lists.newArrayList(
												new ValueSet.ConceptReferenceComponent().setCode("not-the-code"),
												new ValueSet.ConceptReferenceComponent().setCode(IValidationProviders.CODE) )
								)) ));

				TestClientInterceptor requestInterceptor = new TestClientInterceptor();
				mySvc.addClientInterceptor(requestInterceptor);

				CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
						new ConceptValidationOptions().setInferSystem(true), null, IValidationProviders.CODE, IValidationProviders.DISPLAY, valueSet);

				assertNotNull(outcome);
				assertEquals(systemUrl2 + "|" + system2Version, requestInterceptor.getCapturedSystemParameter());
			}


		}

		/**
		 * Captures the system parameter of the request
		 */
		private static class TestClientInterceptor implements IClientInterceptor {

			private String capturedSystemParameter;

			@Override
			public void interceptRequest(IHttpRequest theRequest) {
				try {
					String content = theRequest.getRequestBodyFromStream();
					if (content != null) {
						IJsonLikeParser parser = (IJsonLikeParser) ourCtx.newJsonParser();
						Parameters params = parser.parseResource(Parameters.class, content);
						List<String> systemValues = ParametersUtil.getNamedParameterValuesAsString(
								ourCtx, params, "system");
						assertThat(systemValues).hasSize(1);
						capturedSystemParameter = systemValues.get(0);
					}
				} catch (IOException theE) {
					// ignore
				}
			}

			@Override
			public void interceptResponse(IHttpResponse theResponse) { }

			public String getCapturedSystemParameter() { return capturedSystemParameter; }
		}
	}
}
