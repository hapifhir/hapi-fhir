package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersR4;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_LOOKUP;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE_SYSTEM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RemoteTerminologyLookupCodeWithResponseFileR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private IValidationProvidersR4.MyCodeSystemProviderR4 myCodeSystemProvider;
	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private RemoteTerminologyServiceValidationSupport mySvc;

	@BeforeEach
	public void before() {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(false).setLogRequestSummary(true).setLogResponseSummary(true));
		myCodeSystemProvider = new IValidationProvidersR4.MyCodeSystemProviderR4();
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

	@ParameterizedTest
	@EnumSource(TermConceptPropertyTypeEnum.class)
	void lookupCode_withParametersOutput_propertyTypes(TermConceptPropertyTypeEnum thePropertyType) {

		Parameters returnParams = new Parameters();
		returnParams.addParameter().setName("name").setValue(new StringType("name value"));
		returnParams.addParameter().setName("abstract").setValue(new BooleanType(false));

		Parameters.ParametersParameterComponent propertyParam = returnParams.addParameter();
		propertyParam.setName("property");
		propertyParam.addPart().setName("code").setValue(new CodeType("property-code"));

		switch (thePropertyType) {
			case STRING -> propertyParam.addPart().setName("value").setValue(new StringType("property-value-string"));
			case CODE -> propertyParam.addPart().setName("value").setValue(new CodeType("property-value-code"));
			case CODING -> propertyParam.addPart().setName("value").setValue(new Coding("http://foo/prop-value-system", "prop-value-code", "prop-value-display"));
			case BOOLEAN -> propertyParam.addPart().setName("value").setValue(new BooleanType(true));
			case INTEGER -> propertyParam.addPart().setName("value").setValue(new IntegerType(123));
			case DECIMAL -> propertyParam.addPart().setName("value").setValue(new DecimalType(1.23));
			case DATETIME -> propertyParam.addPart().setName("value").setValue(new DateTimeType("2023-01-01"));
			default -> throw new IllegalStateException("Unexpected value: " + thePropertyType);
		}

		myCodeSystemProvider.addTerminologyResponse(OPERATION_LOOKUP, CODE_SYSTEM, CODE, returnParams);

		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, null, List.of("interfaces"));

		// test
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(null, request);
		assertNotNull(outcome);

		IBaseParameters theActualParameters = outcome.toParameters(ourCtx, null);
		String actual = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(theActualParameters);
		String expected = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(returnParams);

		assertEquals(expected, actual);
	}
}
