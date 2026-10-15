package org.hl7.fhir.r5.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.hl7.fhir.r5.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class RemoteTerminologyServiceValidationSupportR5Test extends BaseValidationTestWithInlineMocks {
	private static final String VALUE_SET_URL = "http://example.org/valueset/test-vs";
	private static final String ANY_NONBLANK_VALUE = "anything";
	private static final FhirContext ourCtx = FhirContext.forR5Cached();

	@RegisterExtension
	public static RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private final MyValueSetProvider myValueSetProvider = new MyValueSetProvider();
	private RemoteTerminologyServiceValidationSupport mySvc;

	@BeforeEach
	public void before() {
		myRestfulServerExtension.getRestfulServer().registerProvider(myValueSetProvider);
		myRestfulServerExtension.getRestfulServer().registerProvider(new EmptyCodeSystemProvider());
		String baseUrl = "http://localhost:" + myRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
		mySvc.setBaseUrl(baseUrl);
	}

	@Test
	public void testLookupCode_R5_ThrowsException() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.lookupCode(
			new ValidationSupportContext(ourCtx.getValidationSupport()),
			new LookupCodeRequest(ANY_NONBLANK_VALUE, ANY_NONBLANK_VALUE)));
	}

	/**
	 * The chain asks remote terminology servers to look a code up even when they report its code system as
	 * unsupported. Remote lookups only support DSTU3 and R4, so an R5 remote is not asked.
	 */
	@Test
	void lookupCode_chainAskingAnR5RemoteTerminologyServer_returnsNoResult() {
		// Setup
		ValidationSupportChain chain = new ValidationSupportChain(mySvc);
		LookupCodeRequest request =
			new LookupCodeRequest(ANY_NONBLANK_VALUE, ANY_NONBLANK_VALUE).setIncludeRemoteTerminology(true);

		// Test
		IValidationSupport.LookupCodeResult outcome = chain.lookupCode(new ValidationSupportContext(chain), request);

		// Verify
		assertThat(outcome).isNull();
	}

	/**
	 * inferSystem is R5+, so an R5 context sends it by default when the system is unknown (counterpart
	 * to the R4 behavior). See <a href="https://github.com/hapifhir/hapi-fhir/issues/8035">#8035</a>.
	 */
	@Test
	void validateCodeInValueSet_stubValueSet_noSystemR5_sendsInferSystem() {
		ValueSet stubValueSet = new ValueSet();
		stubValueSet.setUrl(VALUE_SET_URL);

		myValueSetProvider.myValidateCodeResult = new Parameters().addParameter("result", true);

		mySvc.validateCodeInValueSet(null, new ConceptValidationOptions(), null, "test-code", null, stubValueSet);

		assertThat(myValueSetProvider.myLastValidateCodeInferSystem.booleanValue()).isTrue();
	}

	/**
	 * Has no CodeSystems, so the remote reports every code system as unsupported.
	 */
	// Created by Claude Opus 5.5
	private static class EmptyCodeSystemProvider implements IResourceProvider {

		@Search
		public List<CodeSystem> search(@OptionalParam(name = CodeSystem.SP_URL) UriParam theUrl) {
			return List.of();
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}

	private static class MyValueSetProvider implements IResourceProvider {
		private Parameters myValidateCodeResult;
		private BooleanType myLastValidateCodeInferSystem;

		// Created by claude-opus-4-8
		@Operation(name = "validate-code", idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1),
			@OperationParam(name = "message", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
			@OperationParam(name = "url", min = 0, max = 1) UriType theValueSetUrl,
			@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
			@OperationParam(name = "inferSystem", min = 0, max = 1) BooleanType theInferSystem,
			@OperationParam(name = "valueSet") ValueSet theValueSet) {
			myLastValidateCodeInferSystem = theInferSystem;
			return myValidateCodeResult;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}
	}
}
