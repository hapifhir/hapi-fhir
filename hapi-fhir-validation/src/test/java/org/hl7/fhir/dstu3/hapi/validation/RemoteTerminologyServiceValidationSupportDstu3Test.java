package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
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

import jakarta.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RemoteTerminologyServiceValidationSupportDstu3Test {
	private static final String DISPLAY = "DISPLAY";
	private static final String LANGUAGE = "en";
	private static final String CODE_SYSTEM = "CODE_SYS";
	private static final String CODE_SYSTEM_NAME = "Code System";
	private static final String CODE = "CODE";

	private static final FhirContext ourCtx = FhirContext.forDstu3();

	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private RemoteTerminologyServiceValidationSupport mySvc;
	private MyCodeSystemProvider myCodeSystemProvider;

	@BeforeEach
	public void before() {
		myCodeSystemProvider = new MyCodeSystemProvider();
		ourRestfulServerExtension.getRestfulServer().registerProvider(myCodeSystemProvider);
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
	}

	@Test
	public void testLookupOperationWithAllParams_CodeSystem_Success() {
		myCodeSystemProvider.myNextLookupCodeResult = new IValidationSupport.LookupCodeResult();
		myCodeSystemProvider.myNextLookupCodeResult.setFound(true);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeSystemVersion(CODE_SYSTEM);
		myCodeSystemProvider.myNextLookupCodeResult.setSearchedForCode(CODE);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeSystemDisplayName(CODE_SYSTEM_NAME);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeDisplay(DISPLAY);

		// property
		String propertyName = "birthDate";
		String propertyValue = "1930-01-01";
		IValidationSupport.BaseConceptProperty property = new IValidationSupport.StringConceptProperty(propertyName, propertyValue);
		myCodeSystemProvider.myNextLookupCodeResult.getProperties().add(property);

		// designation
		IValidationSupport.ConceptDesignation designation = new IValidationSupport.ConceptDesignation();
		designation.setLanguage("en");
		designation.setUseCode("code");
		designation.setUseSystem("system");
		designation.setUseDisplay("display");
		designation.setValue("some value");
		myCodeSystemProvider.myNextLookupCodeResult.getDesignations().add(designation);

		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, Set.of("birthDate")));
		assertNotNull(outcome, "Call to lookupCode() should return a non-NULL result!");
		assertEquals(DISPLAY, outcome.getCodeDisplay());
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemVersion());

		assertEquals(CODE, myCodeSystemProvider.myLastCode.asStringValue());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());

		Parameters.ParametersParameterComponent propertyComponent = null;
		Parameters.ParametersParameterComponent designationComponent = null;
		for (Parameters.ParametersParameterComponent parameterComponent : myCodeSystemProvider.myNextReturnParams.getParameter()) {
			if ("property".equals(parameterComponent.getName())) {
				propertyComponent = parameterComponent;
			}
			if ("designation".equals(parameterComponent.getName())) {
				designationComponent = parameterComponent;
			}
		}

		assertNotNull(propertyComponent);

		Iterator<Parameters.ParametersParameterComponent> propertyComponentIterator = propertyComponent.getPart().iterator();
		propertyComponent = propertyComponentIterator.next();
		assertEquals("code", propertyComponent.getName());
		assertEquals(propertyName, ((StringType)propertyComponent.getValue()).getValue());

		propertyComponent = propertyComponentIterator.next();
		assertEquals("value", propertyComponent.getName());
		assertEquals(propertyValue, ((StringType)propertyComponent.getValue()).getValue());

		assertNotNull(designationComponent);
		Iterator<Parameters.ParametersParameterComponent> partParameter = designationComponent.getPart().iterator();
		designationComponent = partParameter.next();
		assertEquals("language", designationComponent.getName());
		assertEquals(LANGUAGE, designationComponent.getValue().toString());

		designationComponent = partParameter.next();
		assertEquals("use", designationComponent.getName());
		Coding coding = (Coding)designationComponent.getValue();
		assertNotNull(coding, "Coding value returned via designation use should NOT be NULL!");
		assertEquals("code", coding.getCode());
		assertEquals("system", coding.getSystem());
		assertEquals("display", coding.getDisplay());

		designationComponent = partParameter.next();
		assertEquals("value", designationComponent.getName());
		assertEquals("some value", designationComponent.getValue().toString());
	}

	private static class MyCodeSystemProvider implements IResourceProvider {
		private UriType myLastUrl;
		private CodeType myLastCode;
		private Parameters myNextReturnParams;
		private IValidationSupport.LookupCodeResult myNextLookupCodeResult;

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
			@OperationParam(name="name", type=StringType.class, min=1),
			@OperationParam(name="version", type=StringType.class, min=0),
			@OperationParam(name="display", type=StringType.class, min=1),
			@OperationParam(name="abstract", type=BooleanType.class, min=1),
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="code", min=0, max=1) CodeType theCode,
			@OperationParam(name="system", min=0, max=1) UriType theSystem,
			@OperationParam(name="coding", min=0, max=1) Coding theCoding,
			@OperationParam(name="version", min=0, max=1) StringType theVersion,
			@OperationParam(name="displayLanguage", min=0, max=1) CodeType theDisplayLanguage,
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED) List<CodeType> thePropertyNames,
			RequestDetails theRequestDetails
		) {
			myLastCode = theCode;
			myLastUrl = theSystem;

			myNextReturnParams = (Parameters)myNextLookupCodeResult.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
			return myNextReturnParams;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}
}
