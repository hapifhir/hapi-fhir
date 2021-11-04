package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RemoteTerminologyServiceValidationSupportDstu3Test {
	private static final String DISPLAY = "DISPLAY";
	private static final String CODE_SYSTEM = "CODE_SYS";
	private static final String CODE_SYSTEM_VERSION = "v1.1";
	private static final String CODE = "CODE";

	private static FhirContext ourCtx = FhirContext.forDstu3();

	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private RemoteTerminologyServiceValidationSupport mySvc;
	private MyCodeSystemProvider myCodeSystemProvider;

	@BeforeEach
	public void before() {
		myCodeSystemProvider = new MyCodeSystemProvider();
		myRestfulServerExtension.getRestfulServer().registerProvider(myCodeSystemProvider);
		String baseUrl = "http://localhost:" + myRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
	}

	@Test
	public void testLookupOperation_CodeSystem_Success() {
		createNextCodeSystemLookupReturnParameters(true, CODE_SYSTEM_VERSION, DISPLAY);

		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(null, CODE_SYSTEM, CODE);
		assertNotNull(outcome, "Call to lookupCode() should return a non-NULL result!");
		assertEquals(DISPLAY, outcome.getCodeDisplay());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());

		assertEquals(CODE, myCodeSystemProvider.myLastCode.asStringValue());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());
		for (Parameters.ParametersParameterComponent param : myCodeSystemProvider.myNextReturnParams.getParameter()) {
			String paramName = param.getName();
			System.out.println("paramName: " + paramName);
			if (paramName.equals("result")) {
				assertEquals(true, ((BooleanType)param.getValue()).booleanValue());
			} else if (paramName.equals("version")) {
				assertEquals(CODE_SYSTEM_VERSION, param.getValue().toString());
			} else if (paramName.equals("display")) {
				assertEquals(DISPLAY, param.getValue().toString());
			}
		}
		//assertTrue(Boolean.parseBoolean();
	}

	private void createNextCodeSystemLookupReturnParameters(boolean theResult, String theVersion, String theDisplay) {
		myCodeSystemProvider.myNextReturnParams = new Parameters();
		myCodeSystemProvider.myNextReturnParams.addParameter().setName("result").setValue(new BooleanType(theResult));
		myCodeSystemProvider.myNextReturnParams.addParameter().setName("version").setValue(new StringType(theVersion));
		myCodeSystemProvider.myNextReturnParams.addParameter().setName("display").setValue(new StringType(theDisplay));
	}

	private static class MyCodeSystemProvider implements IResourceProvider {
		private int myInvocationCount;
		private UriType myLastUrl;
		private CodeType myLastCode;
		private Coding myLastCoding;
		private StringType myLastVersion;
		private CodeType myLastDisplayLanguage;
		private Parameters myNextReturnParams;

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
			@OperationParam(name="name", type=StringType.class, min=1),
			@OperationParam(name="version", type=StringType.class, min=0),
			@OperationParam(name="display", type=StringType.class, min=1),
			@OperationParam(name="abstract", type=BooleanType.class, min=1),
		})
		public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="code", min=0, max=1) CodeType theCode,
			@OperationParam(name="system", min=0, max=1) UriType theSystem,
			@OperationParam(name="coding", min=0, max=1) Coding theCoding,
			@OperationParam(name="version", min=0, max=1) StringType theVersion,
			@OperationParam(name="displayLanguage", min=0, max=1) CodeType theDisplayLanguage,
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED) List<CodeType> theProperties,
			RequestDetails theRequestDetails
		) {
			myInvocationCount++;
			myLastCode = theCode;
			myLastUrl = theSystem;
			myLastCoding = theCoding;
			myLastVersion = theVersion;
			myLastDisplayLanguage = theDisplayLanguage;
			return myNextReturnParams;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}
}
