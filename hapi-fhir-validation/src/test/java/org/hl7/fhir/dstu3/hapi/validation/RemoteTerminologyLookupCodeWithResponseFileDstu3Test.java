package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersDstu3;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_LOOKUP;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE_SYSTEM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RemoteTerminologyLookupCodeWithResponseFileDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private IValidationProvidersDstu3.MyCodeSystemProviderDstu3 myCodeSystemProvider;
	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private RemoteTerminologyServiceValidationSupport mySvc;

	@BeforeEach
	public void before() {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(false).setLogRequestSummary(true).setLogResponseSummary(true));
		myCodeSystemProvider = new IValidationProvidersDstu3.MyCodeSystemProviderDstu3();
		ourRestfulServerExtension.getRestfulServer().registerProviders(myCodeSystemProvider);
	}


	@AfterEach
	public void after() {
		ourRestfulServerExtension.getRestfulServer().unregisterProvider(List.of(myCodeSystemProvider));
	}
	@Test
	void lookupCode_withParametersOutput_convertsCorrectly() {
		String outputFile ="/terminology/CodeSystem-lookup-output-with-subproperties.json";
		IBaseParameters resultParameters = myCodeSystemProvider.addTerminologyResponse(OPERATION_LOOKUP, CODE_SYSTEM, CODE, ourCtx, outputFile);

		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, null, List.of("interfaces"));

		// test
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(null, request);
		assertNotNull(outcome);

		IBaseParameters theActualParameters = outcome.toParameters(ourCtx, request.getPropertyNames().stream().map(StringType::new).toList());
		String actual = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(theActualParameters);
		String expected = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resultParameters);

		assertEquals(expected, actual);
	}
}
