package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.ILookupCodeTest.ILookupCodeSupportedPropertyTest;
import org.hl7.fhir.common.hapi.validation.ILookupCodeTest.ILookupCodeUnsupportedPropertyTypeTest;
import org.hl7.fhir.common.hapi.validation.ILookupCodeTest.IMyCodeSystemProvider;
import org.hl7.fhir.common.hapi.validation.ILookupCodeTest.IMySimpleCodeSystemProvider;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LookupCodeR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);
	private final RemoteTerminologyServiceValidationSupport mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);

	@BeforeEach
	public void before() {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
	}

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class LookupCodeUnsupportedPropertyTypeR4Test implements ILookupCodeUnsupportedPropertyTypeTest {
		private final MySimplePropertyCodeSystemProviderR4 myMySimplePropertyCodeSystemProvider = new MySimplePropertyCodeSystemProviderR4();

		@Override
		public IMySimpleCodeSystemProvider getCodeSystemProvider() {
			return myMySimplePropertyCodeSystemProvider;
		}

		@Override
		public RemoteTerminologyServiceValidationSupport getService() {
			return mySvc;
		}

		@Override
		public String getInvalidValueErrorCode() {
			return "HAPI-2452";
		}


		@Override
		public String getInvalidValueErrorCodeForConvert() {
			return "HAPI-2453";
		}

		@BeforeEach
		public void before() {
			// TODO: use another type when "code" is added to the supported types
			myMySimplePropertyCodeSystemProvider.myPropertyName = "somePropertyName";
			myMySimplePropertyCodeSystemProvider.myPropertyValue = new CodeType("someCode");
			ourRestfulServerExtension.getRestfulServer().registerProvider(myMySimplePropertyCodeSystemProvider);
		}
	}

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class ILookupCodeSupportedPropertyR4Test implements ILookupCodeSupportedPropertyTest {
		private final MyCodeSystemProviderR4 myCodeSystemProvider = new MyCodeSystemProviderR4();

		@Override
		public IMyCodeSystemProvider getCodeSystemProvider() {
			return myCodeSystemProvider;
		}

		@Override
		public RemoteTerminologyServiceValidationSupport getService() {
			return mySvc;
		}

		@BeforeEach
		public void before() {
			ourRestfulServerExtension.getRestfulServer().registerProvider(myCodeSystemProvider);
		}

		@Override
		public void verifyProperty(IValidationSupport.BaseConceptProperty theConceptProperty, String theExpectedPropertName, IBaseDatatype theExpectedValue) {
			assertEquals(theExpectedPropertName, theConceptProperty.getPropertyName());
			String type = theConceptProperty.getType();
			switch (type) {
				case IValidationSupport.TYPE_STRING -> {
					assertTrue(theExpectedValue instanceof StringType);
					StringType stringValue = (StringType) theExpectedValue;
					assertTrue(theConceptProperty instanceof IValidationSupport.StringConceptProperty);
					IValidationSupport.StringConceptProperty stringConceptProperty = (IValidationSupport.StringConceptProperty) theConceptProperty;
					assertEquals(stringValue.getValue(), stringConceptProperty.getValue());
				}
				case IValidationSupport.TYPE_CODING -> {
					assertTrue(theExpectedValue instanceof Coding);
					Coding coding = (Coding) theExpectedValue;
					assertTrue(theConceptProperty instanceof IValidationSupport.CodingConceptProperty);
					IValidationSupport.CodingConceptProperty codingConceptProperty = (IValidationSupport.CodingConceptProperty) theConceptProperty;
					assertEquals(coding.getCode(), codingConceptProperty.getCode());
					assertEquals(coding.getSystem(), codingConceptProperty.getCodeSystem());
					assertEquals(coding.getDisplay(), codingConceptProperty.getDisplay());
				}
				default ->
					throw new InternalErrorException(Msg.code(2451) + "Property type " + type + " is not supported.");
			}
		}

		public Stream<Arguments> getEmptyPropertyValues() {
			return Stream.of(
				Arguments.arguments(new StringType()),
				Arguments.arguments(new StringType("")),
				Arguments.arguments(new Coding()),
				Arguments.arguments(new Coding("", null, null)),
				Arguments.arguments(new Coding("", "", null)),
				Arguments.arguments(new Coding(null, "", null))
			);
		}

		public Stream<Arguments> getPropertyValues() {
			return Stream.of(
				Arguments.arguments(new StringType("value")),
				Arguments.arguments(new Coding("code", "system", "display"))
			);
		}



		public Stream<Arguments> getDesignations() {
			return Stream.of(
				Arguments.arguments(new IValidationSupport.ConceptDesignation().setLanguage("en").setUseCode("code1").setUseSystem("system-1").setUseDisplay("display").setValue("some value")),
				Arguments.arguments(new IValidationSupport.ConceptDesignation().setUseCode("code2").setUseSystem("system1").setUseDisplay("display").setValue("someValue")),
				Arguments.arguments(new IValidationSupport.ConceptDesignation().setUseCode("code2").setUseSystem("system1").setValue("someValue")),
				Arguments.arguments(new IValidationSupport.ConceptDesignation().setUseCode("code2").setUseSystem("system1")),
				Arguments.arguments(new IValidationSupport.ConceptDesignation().setUseCode("code2"))
			);
		}
	}

	static class MySimplePropertyCodeSystemProviderR4 implements IMySimpleCodeSystemProvider {
		String myPropertyName;
		Type myPropertyValue;

		@Override
		public IBaseDatatype getPropertyValue() {
			return myPropertyValue;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
			@OperationParam(name = "name", type = StringType.class, min = 1),
			@OperationParam(name = "version", type = StringType.class, min = 0),
			@OperationParam(name = "display", type = StringType.class, min = 1),
			@OperationParam(name = "abstract", type = BooleanType.class, min = 1),
			@OperationParam(name = "property", min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name = "code", max = 1) CodeType theCode,
			@OperationParam(name = "system", max = 1) UriType theSystem,
			@OperationParam(name = "coding", max = 1) Coding theCoding,
			@OperationParam(name = "version", max = 1) StringType theVersion,
			@OperationParam(name = "displayLanguage", max = 1) CodeType theDisplayLanguage,
			@OperationParam(name= " property", max = OperationParam.MAX_UNLIMITED) List<CodeType> thePropertyNames,
			RequestDetails theRequestDetails
		) {
			Parameters.ParametersParameterComponent component = new Parameters.ParametersParameterComponent();
			component.setName("property");
			component.addPart(new Parameters.ParametersParameterComponent().setName("code").setValue(new CodeType(myPropertyName)));
			component.addPart(new Parameters.ParametersParameterComponent().setName("value").setValue(myPropertyValue));
			return new Parameters().addParameter(component);
		}
	}

	static class MyCodeSystemProviderR4 implements IMyCodeSystemProvider {
		private UriType mySystemUrl;
		private CodeType myCode;
		private IValidationSupport.LookupCodeResult myLookupCodeResult;

		@Override
		public void setLookupCodeResult(IValidationSupport.LookupCodeResult theLookupCodeResult) {
			myLookupCodeResult = theLookupCodeResult;
		}

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
			@OperationParam(name = "name", type = StringType.class, min = 1),
			@OperationParam(name = "version", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class, min = 1),
			@OperationParam(name = "abstract", type = BooleanType.class, min = 1),
			@OperationParam(name = "property", type = StringType.class, min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public IBaseParameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name = "code", max = 1) CodeType theCode,
			@OperationParam(name = "system",max = 1) UriType theSystem,
			@OperationParam(name = "coding", max = 1) Coding theCoding,
			@OperationParam(name = "version", max = 1) StringType theVersion,
			@OperationParam(name = "displayLanguage", max = 1) CodeType theDisplayLanguage,
			@OperationParam(name = "property", max = OperationParam.MAX_UNLIMITED) List<StringType> thePropertyNames,
			RequestDetails theRequestDetails
		) {
			myCode = theCode;
			mySystemUrl = theSystem;
			return myLookupCodeResult.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
		}
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}

		@Override
		public String getCode() {
			return myCode != null ? myCode.getValueAsString() : null;
		}

		@Override
		public String getSystem() {
			return mySystemUrl != null ? mySystemUrl.getValueAsString() : null;
		}
	}
}
