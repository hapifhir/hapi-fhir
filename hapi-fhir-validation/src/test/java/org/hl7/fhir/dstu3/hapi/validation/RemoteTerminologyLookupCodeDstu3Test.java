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
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.IRemoteTerminologyLookupCodeTest;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

/**
 * Version specific tests for CodeSystem $lookup against RemoteTerminologyValidationSupport.
 * @see RemoteTerminologyServiceValidationSupport
 */
public class RemoteTerminologyLookupCodeDstu3Test implements IRemoteTerminologyLookupCodeTest {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);
	private final RemoteTerminologyServiceValidationSupport mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
	private IValidationProviders.IMyLookupCodeProvider myLookupCodeProvider;

	@BeforeEach
	public void before() {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
		myLookupCodeProvider = new MyLookupCodeProviderDstu3();
		ourRestfulServerExtension.getRestfulServer().registerProvider(myLookupCodeProvider);
	}

	@AfterEach
	public void after() {
		ourRestfulServerExtension.getRestfulServer().unregisterProvider(myLookupCodeProvider);
	}

	@Override
	public IValidationProviders.IMyLookupCodeProvider getLookupCodeProvider() {
		return myLookupCodeProvider;
	}

	@Override
	public RemoteTerminologyServiceValidationSupport getService() {
		return mySvc;
	}

	public static Stream<Arguments> getEmptyPropertyValues() {
		return Stream.of(
			Arguments.of(new StringType()),
			Arguments.of(new StringType("")),
			Arguments.of(new Coding()),
			Arguments.of(new Coding("", null, null)),
			Arguments.of(new Coding("", "", null)),
			Arguments.of(new Coding(null, "", null))
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getEmptyPropertyValues")
	void lookupCode_forCodeSystemWithPropertyEmptyValue_returnsCorrectParameters(IBaseDatatype thePropertyValue) {
		verifyLookupWithEmptyPropertyValue(thePropertyValue);
	}

	public static Stream<Arguments> getPropertyValueArguments() {
		return Stream.of(
				// FHIR DSTU3 spec types
				Arguments.of(new StringType("value")),
				Arguments.of(new Coding("code", "system", "display")),
				Arguments.of(new CodeType("code")),
				Arguments.of(new BooleanType(true)),
				Arguments.of(new IntegerType(1)),
				Arguments.of(new DateTimeType(Calendar.getInstance())),
				// other types will also not fail for Remote Terminology
				Arguments.of(new DecimalType(1.1)),
				Arguments.of(new InstantType(Calendar.getInstance())),
				Arguments.of(new Type() {
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

	public static Stream<Arguments> getPropertyValueListArguments() {
		return Stream.of(
				Arguments.of(List.of(new StringType("value1")), new StringType("value2")),
				Arguments.of(List.of(new StringType("value1")), new Coding("code", "system", "display"))
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getPropertyValueArguments")
	void lookupCode_forCodeSystemWithProperty_returnsCorrectProperty(IBaseDatatype thePropertyValue) {
		verifyLookupWithProperty(List.of(thePropertyValue), List.of());
	}

	@ParameterizedTest
	@MethodSource(value = "getPropertyValueListArguments")
	void lookupCode_forCodeSystemWithPropertyFilter_returnsCorrectProperty(List<IBaseDatatype> thePropertyValues) {
		verifyLookupWithProperty(thePropertyValues, List.of());
		verifyLookupWithProperty(thePropertyValues, List.of(thePropertyValues.size() - 1));
	}

	@ParameterizedTest
	@MethodSource(value = "getPropertyValueListArguments")
	void lookupCode_forCodeSystemWithPropertyGroup_returnsCorrectProperty(List<IBaseDatatype> thePropertyValues) {
		verifyLookupWithSubProperties(thePropertyValues);
	}

	public static Stream<Arguments> getDesignations() {
		return Stream.of(
			Arguments.of(new ConceptDesignation().setLanguage("en").setUseCode("code1").setUseSystem("system-1").setUseDisplay("display").setValue("some value")),
			Arguments.of(new ConceptDesignation().setUseCode("code2").setUseSystem("system1").setUseDisplay("display").setValue("someValue")),
			Arguments.of(new ConceptDesignation().setUseCode("code2").setUseSystem("system1").setValue("someValue")),
			Arguments.of(new ConceptDesignation().setUseCode("code2").setUseSystem("system1")),
			Arguments.of(new ConceptDesignation().setUseCode("code2"))
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getDesignations")
	void lookupCode_withCodeSystemWithDesignation_returnsCorrectDesignation(final IValidationSupport.ConceptDesignation theConceptDesignation) {
		verifyLookupWithConceptDesignation(theConceptDesignation);
	}

	@SuppressWarnings("unused")
	static class MyLookupCodeProviderDstu3 implements IValidationProviders.IMyLookupCodeProvider {
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
			if (theSystem == null) {
				throw new InvalidRequestException(MessageFormat.format(MESSAGE_RESPONSE_INVALID, theCode));
			}
			if (!myLookupCodeResult.isFound()) {
				throw new ResourceNotFoundException(MessageFormat.format(MESSAGE_RESPONSE_NOT_FOUND, theCode));
			}
			return  myLookupCodeResult.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}
}
