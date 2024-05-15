package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.ConceptDesignation;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.ILookupCodeTest;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

/**
 * Version specific tests for CodeSystem $lookup against RemoteTerminologyValidationSupport.
 * @see RemoteTerminologyServiceValidationSupport
 */
public class RemoteTerminologyLookupCodeDstu3Test implements ILookupCodeTest {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);
	private final RemoteTerminologyServiceValidationSupport mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);

	@BeforeEach
	public void before() {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
		ourRestfulServerExtension.getRestfulServer().registerProvider(myCodeSystemProvider);
	}

	private final MyCodeSystemProviderDstu3 myCodeSystemProvider = new MyCodeSystemProviderDstu3();

	@Override
	public IMyCodeSystemProvider getCodeSystemProvider() {
		return myCodeSystemProvider;
	}

	@Override
	public RemoteTerminologyServiceValidationSupport getService() {
		return mySvc;
	}

	public static Stream<Arguments> getEmptyPropertyValues() {
		return Stream.of(
			Arguments.arguments(new StringType()),
			Arguments.arguments(new StringType("")),
			Arguments.arguments(new Coding()),
			Arguments.arguments(new Coding("", null, null)),
			Arguments.arguments(new Coding("", "", null)),
			Arguments.arguments(new Coding(null, "", null))
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getEmptyPropertyValues")
	public void lookupCode_forCodeSystemWithPropertyEmptyValue_returnsCorrectParameters(IBaseDatatype thePropertyValue) {
		verifyLookupWithEmptyPropertyValue(thePropertyValue);
	}

	public static Stream<Arguments> getPropertyValues() {
		return Stream.of(
			// FHIR DSTU3 spec types
			Arguments.arguments(new StringType("value")),
			Arguments.arguments(new Coding("code", "system", "display")),
			Arguments.arguments(new CodeType("code")),
			Arguments.arguments(new BooleanType(true)),
			Arguments.arguments(new IntegerType(1)),
			Arguments.arguments(new DateTimeType(Calendar.getInstance())),
			// other types will also not fail for Remote Terminology
			Arguments.arguments(new DecimalType(1.1)),
			Arguments.arguments(new InstantType(Calendar.getInstance())),
			Arguments.arguments(new Type() {
				@Override
				protected Type typedCopy() {
					return this;
				}

				@Override
				public String toString() {
					return "randomType";
				}
			})
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getPropertyValues")
	public void lookupCode_forCodeSystemWithProperty_returnsCorrectProperty(IBaseDatatype thePropertyValue) {
		verifyLookupWithPropertyValue(thePropertyValue);
	}

	@ParameterizedTest
	@MethodSource(value = "getPropertyValues")
	public void lookupCode_forCodeSystemWithPropertyGroup_returnsCorrectProperty(IBaseDatatype thePropertyValue) {
		verifyLookupWithSubPropertyValue(thePropertyValue);
	}

	public static Stream<Arguments> getDesignations() {
		return Stream.of(
			Arguments.arguments(new ConceptDesignation().setLanguage("en").setUseCode("code1").setUseSystem("system-1").setUseDisplay("display").setValue("some value")),
			Arguments.arguments(new ConceptDesignation().setUseCode("code2").setUseSystem("system1").setUseDisplay("display").setValue("someValue")),
			Arguments.arguments(new ConceptDesignation().setUseCode("code2").setUseSystem("system1").setValue("someValue")),
			Arguments.arguments(new ConceptDesignation().setUseCode("code2").setUseSystem("system1")),
			Arguments.arguments(new ConceptDesignation().setUseCode("code2"))
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getDesignations")
	void lookupCode_withCodeSystemWithDesignation_returnsCorrectDesignation(final IValidationSupport.ConceptDesignation theConceptDesignation) {
		verifyLookupWithConceptDesignation(theConceptDesignation);
	}

	static class MyCodeSystemProviderDstu3 implements IMyCodeSystemProvider {
		private UriType mySystemUrl;
		private CodeType myCode;
		private LookupCodeResult myLookupCodeResult;

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
		public IBaseParameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name = "code", max = 1) CodeType theCode,
			@OperationParam(name = "system", max = 1) UriType theSystem,
			@OperationParam(name = "coding", max = 1) Coding theCoding,
			@OperationParam(name = "version", max = 1) StringType theVersion,
			@OperationParam(name = "displayLanguage", max = 1) CodeType theDisplayLanguage,
			@OperationParam(name= " property", max = OperationParam.MAX_UNLIMITED) List<StringType> thePropertyNames,
			RequestDetails theRequestDetails
		) {
			myCode = theCode;
			mySystemUrl = theSystem;
			return  myLookupCodeResult.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
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
