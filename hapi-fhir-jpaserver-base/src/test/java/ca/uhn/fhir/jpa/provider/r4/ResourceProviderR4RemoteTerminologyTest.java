package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
 * This set of Unit Tests instantiates and injects an instance of
 * {@link org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport}
 * into the ValidationSupportChain, which tests the logic of dynamically selecting the correct Remote Terminology
 * implementation. It also exercises the code found in
 * {@link org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport#invokeRemoteValidateCode}
 */
public class ResourceProviderR4RemoteTerminologyTest extends BaseResourceProviderR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4RemoteTerminologyTest.class);
	private static final String DISPLAY = "DISPLAY";
	private static final String DISPLAY_BODY_MASS_INDEX = "Body mass index (BMI) [Ratio]";
	private static final String CODE_BODY_MASS_INDEX = "39156-5";
	private static FhirContext ourCtx = FhirContext.forR4();
	private MyCodeSystemProvider myCodeSystemProvider = new MyCodeSystemProvider();
	private MyValueSetProvider myValueSetProvider = new MyValueSetProvider();

	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx, myCodeSystemProvider,
		myValueSetProvider);

	private RemoteTerminologyServiceValidationSupport mySvc;

	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;

	@BeforeEach
	public void before_addRemoteTerminologySupport() throws Exception {
		String baseUrl = "http://localhost:" + myRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, baseUrl);
		myValidationSupportChain.addValidationSupport(0, mySvc);
	}

	@AfterEach
	public void after_removeRemoteTerminologySupport() {
		myValidationSupportChain.removeValidationSupport(mySvc);
		myRestfulServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testValidateCodeOperationOnCodeSystem_ByCodingAndUrlWhereSystemIsDifferent_ThrowsException() {
		assertThrows(InvalidRequestException.class, () -> {
			Parameters respParam = myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_VALIDATE_CODE)
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0247").setCode("P"))
				.andParameter("url", new UriType("http://terminology.hl7.org/CodeSystem/INVALID-CODESYSTEM"))
				.execute();
		});
	}

	@Test
	public void testValidateCodeOperationOnCodeSystem_ByCodingAndUrl_UsingBuiltInCodeSystems() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myNextReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/v2-0247"));
		createNextCodeSystemReturnParameters(true, DISPLAY, null);

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0247").setCode("P"))
			.andParameter("url", new UriType("http://terminology.hl7.org/CodeSystem/v2-0247"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType)respParam.getParameter("result")).booleanValue());
		assertEquals(DISPLAY, respParam.getParameter("display").toString());
	}

	@Test
	public void testValidateCodeOperationOnValueSet_ByUrlAndSystem_UsingBuiltInCodeSystems() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myNextReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/list-example-use-codes"));
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();
		myValueSetProvider.myNextReturnValueSets.add((ValueSet) new ValueSet().setId("ValueSet/list-example-codes"));
		createNextValueSetReturnParameters(true, DISPLAY, null);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "code", new CodeType("alerts"))
			.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/list-example-use-codes"))
			.andParameter("url", new UriType("http://hl7.org/fhir/ValueSet/list-example-codes"))
			.useHttpGet()
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType)respParam.getParameter("result")).booleanValue());
		assertEquals(DISPLAY, respParam.getParameter("display").toString());
	}

	@Test
	public void testValidateCodeOperationOnValueSet_ByUrlSystemAndCode() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myNextReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/list-example-use-codes"));
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();
		myValueSetProvider.myNextReturnValueSets.add((ValueSet) new ValueSet().setId("ValueSet/list-example-codes"));
		createNextValueSetReturnParameters(true, DISPLAY_BODY_MASS_INDEX, null);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "code", new CodeType(CODE_BODY_MASS_INDEX))
			.andParameter("url", new UriType("https://loinc.org"))
			.andParameter("system", new UriType("http://loinc.org"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType)respParam.getParameter("result")).booleanValue());
		assertEquals(DISPLAY_BODY_MASS_INDEX, respParam.getParameter("display").toString());
	}

	private void createNextCodeSystemReturnParameters(boolean theResult, String theDisplay, String theMessage) {
		myCodeSystemProvider.myNextReturnParams = new Parameters();
		myCodeSystemProvider.myNextReturnParams.addParameter("result", theResult);
		myCodeSystemProvider.myNextReturnParams.addParameter("display", theDisplay);
		if (theMessage != null) {
			myCodeSystemProvider.myNextReturnParams.addParameter("message", theMessage);
		}
	}

	private void createNextValueSetReturnParameters(boolean theResult, String theDisplay, String theMessage) {
		myValueSetProvider.myNextReturnParams = new Parameters();
		myValueSetProvider.myNextReturnParams.addParameter("result", theResult);
		myValueSetProvider.myNextReturnParams.addParameter("display", theDisplay);
		if (theMessage != null) {
			myValueSetProvider.myNextReturnParams.addParameter("message", theMessage);
		}
	}

	private static class MyCodeSystemProvider implements IResourceProvider {

		private UriParam myLastUrlParam;
		private List<CodeSystem> myNextReturnCodeSystems;
		private int myInvocationCount;
		private UriType myLastUrl;
		private CodeType myLastCode;
		private StringType myLastDisplay;
		private Parameters myNextReturnParams;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1),
			@OperationParam(name = "message", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "url", min = 0, max = 1) UriType theCodeSystemUrl,
			@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
			@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay
		) {
			myInvocationCount++;
			myLastUrl = theCodeSystemUrl;
			myLastCode = theCode;
			myLastDisplay = theDisplay;
			return myNextReturnParams;
		}

		@Search
		public List<CodeSystem> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			myLastUrlParam = theUrlParam;
			assert myNextReturnCodeSystems != null;
			return myNextReturnCodeSystems;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}

	private static class MyValueSetProvider implements IResourceProvider {
		private Parameters myNextReturnParams;
		private List<ValueSet> myNextReturnValueSets;
		private UriType myLastUrl;
		private CodeType myLastCode;
		private int myInvocationCount;
		private UriType myLastSystem;
		private StringType myLastDisplay;
		private ValueSet myLastValueSet;
		private UriParam myLastUrlParam;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1),
			@OperationParam(name = "message", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "url", min = 0, max = 1) UriType theValueSetUrl,
			@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
			@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
			@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay,
			@OperationParam(name = "valueSet") ValueSet theValueSet
		) {
			myInvocationCount++;
			myLastUrl = theValueSetUrl;
			myLastCode = theCode;
			myLastSystem = theSystem;
			myLastDisplay = theDisplay;
			myLastValueSet = theValueSet;
			return myNextReturnParams;
		}

		@Search
		public List<ValueSet> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			myLastUrlParam = theUrlParam;
			assert myNextReturnValueSets != null;
			return myNextReturnValueSets;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}

	}
}
