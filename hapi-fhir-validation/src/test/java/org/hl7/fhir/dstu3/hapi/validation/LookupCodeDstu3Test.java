package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.ConceptDesignation;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
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
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.context.support.IValidationSupport.BaseConceptProperty;
import static ca.uhn.fhir.context.support.IValidationSupport.CodingConceptProperty;
import static ca.uhn.fhir.context.support.IValidationSupport.StringConceptProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LookupCodeDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
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
	class LookupCodeUnsupportedPropertyTypeDstu3Test implements ILookupCodeUnsupportedPropertyTypeTest {
		private final MySimplePropertyCodeSystemProviderDstu3 myMySimplePropertyCodeSystemProvider = new MySimplePropertyCodeSystemProviderDstu3();

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
			return "HAPI-2450";
		}

		@Override
		public String getInvalidValueErrorCodeForConvert() {
			return "HAPI-2451";
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
	class ILookupCodeSupportedPropertyDstu3Test implements ILookupCodeSupportedPropertyTest {
		private final MyCodeSystemProviderDstu3 myCodeSystemProvider = new MyCodeSystemProviderDstu3();

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

		public void verifyProperty(BaseConceptProperty theConceptProperty, String theExpectedPropertName, IBaseDatatype theExpectedValue) {
			assertEquals(theExpectedPropertName, theConceptProperty.getPropertyName());
			String type = theConceptProperty.getType();
			switch (type) {
				case IValidationSupport.TYPE_STRING -> {
					assertTrue(theExpectedValue instanceof StringType);
					StringType stringValue = (StringType) theExpectedValue;
					assertTrue(theConceptProperty instanceof StringConceptProperty);
					StringConceptProperty stringConceptProperty = (StringConceptProperty) theConceptProperty;
					assertEquals(stringValue.getValue(), stringConceptProperty.getValue());
				}
				case IValidationSupport.TYPE_CODING -> {
					assertTrue(theExpectedValue instanceof Coding);
					Coding coding = (Coding) theExpectedValue;
					assertTrue(theConceptProperty instanceof CodingConceptProperty);
					CodingConceptProperty codingConceptProperty = (CodingConceptProperty) theConceptProperty;
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
				Arguments.arguments(new ConceptDesignation().setLanguage("en").setUseCode("code1").setUseSystem("system-1").setUseDisplay("display").setValue("some value")),
				Arguments.arguments(new ConceptDesignation().setUseCode("code2").setUseSystem("system1").setUseDisplay("display").setValue("someValue")),
				Arguments.arguments(new ConceptDesignation().setUseCode("code2").setUseSystem("system1").setValue("someValue")),
				Arguments.arguments(new ConceptDesignation().setUseCode("code2").setUseSystem("system1")),
				Arguments.arguments(new ConceptDesignation().setUseCode("code2"))
			);
		}
	}

	static class MySimplePropertyCodeSystemProviderDstu3 implements IMySimpleCodeSystemProvider {

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
			ParametersParameterComponent component = new ParametersParameterComponent();
			component.setName("property");
			component.addPart(new ParametersParameterComponent().setName("code").setValue(new CodeType(myPropertyName)));
			component.addPart(new ParametersParameterComponent().setName("value").setValue(myPropertyValue));
			return new Parameters().addParameter(component);
		}

	}

	static class MyCodeSystemProviderDstu3 implements IMyCodeSystemProvider {
		private UriType mySystemUrl;
		private CodeType myCode;
		private LookupCodeResult myLookupCodeResult;

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

		@Override
		public void setLookupCodeResult(LookupCodeResult theLookupCodeResult) {
			myLookupCodeResult = theLookupCodeResult;
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
			myCode = theCode;
			mySystemUrl = theSystem;

			return (Parameters)myLookupCodeResult.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
		}
	}
}
