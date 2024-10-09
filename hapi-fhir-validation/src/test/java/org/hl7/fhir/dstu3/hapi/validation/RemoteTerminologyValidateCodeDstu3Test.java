package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.common.hapi.validation.IRemoteTerminologyValidateCodeTest;
import org.hl7.fhir.common.hapi.validation.IValidationProviders;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport.ERROR_CODE_UNKNOWN_CODE_IN_CODE_SYSTEM;
import static org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport.ERROR_CODE_UNKNOWN_CODE_IN_VALUE_SET;

/**
 * Version specific tests for validation using RemoteTerminologyValidationSupport.
 * The tests in this class simulate the call to a remote server and therefore, only tests the code in
 * the RemoteTerminologyServiceValidationSupport itself. The remote client call is simulated using the test providers.
 * @see RemoteTerminologyServiceValidationSupport
 *
 * Other operations are tested separately.
 * @see RemoteTerminologyLookupCodeDstu3Test
 */
public class RemoteTerminologyValidateCodeDstu3Test implements IRemoteTerminologyValidateCodeTest {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);
	private IValidateCodeProvidersDstu3.MyCodeSystemProviderDstu3 myCodeSystemProvider;
	private IValidateCodeProvidersDstu3.MyValueSetProviderDstu3 myValueSetProvider;
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
		myCodeSystemProvider = new IValidateCodeProvidersDstu3.MyCodeSystemProviderDstu3();
		myValueSetProvider = new IValidateCodeProvidersDstu3.MyValueSetProviderDstu3();
		ourRestfulServerExtension.getRestfulServer().registerProviders(myCodeSystemProvider, myValueSetProvider);
	}

	@AfterEach
	public void after() {
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		ourRestfulServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
		ourRestfulServerExtension.getRestfulServer().unregisterProviders(List.of(myCodeSystemProvider, myValueSetProvider));
	}

	@Override
	public RemoteTerminologyServiceValidationSupport getService() {
		return mySvc;
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
	public IValidateCodeProvidersDstu3.MyCodeSystemProviderDstu3 getCodeSystemProvider() {
		return myCodeSystemProvider;
	}

	@Override
	public IValidateCodeProvidersDstu3.MyValueSetProviderDstu3 getValueSetProvider() {
		return myValueSetProvider;
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
	public Parameters createParameters(Boolean theResult, String theDisplay, String theMessage, IBaseResource theIssuesResource) {
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("result").setValue(new BooleanType(theResult));
		parameters.addParameter().setName("code").setValue(new StringType(IValidationProviders.CODE));
		parameters.addParameter().setName("system").setValue(new UriType(IValidationProviders.CODE_SYSTEM));
		parameters.addParameter().setName("version").setValue(new StringType(IValidationProviders.CODE_SYSTEM_VERSION));
		parameters.addParameter().setName("display").setValue(new StringType(theDisplay));
		parameters.addParameter().setName("message").setValue(new StringType(theMessage));
		parameters.addParameter().setName("issues").setResource((Resource) theIssuesResource);
		return parameters;
	}

	@Override
	public void createCodeSystemReturnParameters(Boolean theResult, String theDisplay, String theMessage, IBaseResource theIssuesResource) {
		myCodeSystemProvider.setReturnParams(createParameters(theResult, theDisplay, theMessage, theIssuesResource));
	}

	@Override
	public void createValueSetReturnParameters(Boolean theResult, String theDisplay, String theMessage, IBaseResource theIssuesResource) {
		myValueSetProvider.setReturnParams(createParameters(theResult, theDisplay, theMessage, theIssuesResource));
	}
}
