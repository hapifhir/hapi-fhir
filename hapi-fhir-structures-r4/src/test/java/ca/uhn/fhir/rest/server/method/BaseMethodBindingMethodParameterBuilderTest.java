package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.PatientProvider;
import ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SampleParams;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;

import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.EXPAND;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.OP_INSTANCE_OR_TYPE;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.ParamsWithTypeConversion;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.ParamsWithoutAnnotations;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_MULTIPLE_REQUEST_DETAILS;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST_WITH_ID_TYPE;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_PARAM_NO_EMBEDDED_TYPE;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SIMPLE_METHOD_WITH_PARAMS_CONVERSION;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SampleParamsWithIdParam;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// LUKETODO:  try to cover more InternalErrorException cases
// This test lives in hapi-fhir-structures-r4 because if we introduce it in hapi-fhir-server, there will be a
// circular dependency
class BaseMethodBindingMethodParameterBuilderTest {

	// LUKETODO:  assert Exception messages

	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(BaseMethodBindingMethodParameterBuilderTest.class);

	private static final RequestDetails REQUEST_DETAILS = new SystemRequestDetails();
	private static final String TIMEZONE_AMERICA_TORONTO = "America/Toronto";

	private final MethodAndOperationParamsInnerClassesAndMethods myMethodAndOperationParamsInnerClassesAndMethods = new MethodAndOperationParamsInnerClassesAndMethods();

	// LUKETODO:  wrong params
	// LUKETODO:  wrong param order
	// LUKETODO:  RequestDetails passed but not in signature
	// LUKETODO:  RequestDetails in signature but not passed

	@Test
	void happyPathOperationParamsEmptyParams() {
		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(MethodAndOperationParamsInnerClassesAndMethods.SUPER_SIMPLE);
		final Object[] inputParams = new Object[]{};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(inputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationParamsNonEmptyParams() {
		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class, BooleanType.class);
		final Object[] inputParams = new Object[]{new IdDt(), "param1", List.of("param2")};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(inputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesNoRequestDetails() {
		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, SampleParams.class);
		final Object[] inputParams = new Object[]{"param1", List.of("param2")};
		final Object[] expectedOutputParams = new Object[]{new SampleParams("param1", List.of("param2"))};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesNoRequestDetailsNullArguments() {
		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, SampleParams.class);
		final Object[] inputParams = new Object[]{null, null};
		final Object[] expectedOutputParams = new Object[]{new SampleParams(null, null)};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesRequestDetailsFirst() {
		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST, RequestDetails.class, SampleParams.class);
		final Object[] inputParams = new Object[]{REQUEST_DETAILS, "param1", List.of("param2")};
		final Object[] expectedOutputParams = new Object[]{REQUEST_DETAILS, new SampleParams("param1", List.of("param2"))};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesRequestDetailsLast() {
		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST, SampleParams.class, RequestDetails.class);
		final Object[] inputParams = new Object[]{"param1", List.of("param3"), REQUEST_DETAILS};
		final Object[] expectedOutputParams = new Object[]{new SampleParams("param1", List.of("param3")), REQUEST_DETAILS};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesWithIdType() {
		final IdType id = new IdType();
		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST_WITH_ID_TYPE, RequestDetails.class, SampleParamsWithIdParam.class);
		final Object[] inputParams = new Object[]{REQUEST_DETAILS, id, TIMEZONE_AMERICA_TORONTO, "param1", List.of("param2"), new BooleanType(false)};
		final Object[] expectedOutputParams = new Object[]{REQUEST_DETAILS, new SampleParamsWithIdParam(id, TIMEZONE_AMERICA_TORONTO, "param1", List.of("param2"), new BooleanType(false))};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void expand() {
		final IdType id = new IdType();
		final ValueSet valueSet = new ValueSet();

		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(EXPAND, HttpServletRequest.class, IIdType.class, IBaseResource.class, RequestDetails.class);
		final TestHttpServletRequest testHttpServletRequest = new TestHttpServletRequest();
		final Object[] inputParams = new Object[]{testHttpServletRequest, id, TIMEZONE_AMERICA_TORONTO, valueSet, REQUEST_DETAILS};
		final Object[] expectedOutputParams = new Object[]{testHttpServletRequest, id, TIMEZONE_AMERICA_TORONTO, valueSet, REQUEST_DETAILS};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void opInstanceOrType() {
		final IdType id = new IdType();
		final PatientProvider provider = new PatientProvider();
		final StringType stringType = new StringType("stringType");
		final Patient patient = new Patient();

		final Method sampleMethod = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(provider, OP_INSTANCE_OR_TYPE, IdType.class, String.class, StringType.class, Patient.class);
		final Object[] inputParams = new Object[]{id, TIMEZONE_AMERICA_TORONTO, stringType, patient};
		final Object[] expectedOutputParams = new Object[]{id, TIMEZONE_AMERICA_TORONTO, stringType, patient};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, REQUEST_DETAILS, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void buildMethodParams_withNullMethod_shouldThrowInternalErrorException() {
		assertThrows(InternalErrorException.class, () -> {
			buildMethodParams(null, REQUEST_DETAILS, new Object[]{});
		});
	}

	@Test
	void buildMethodParams_withNullParams_shouldThrowInternalErrorException() throws NoSuchMethodException {
		final Method sampleMethod = MethodAndOperationParamsInnerClassesAndMethods.class.getDeclaredMethod(MethodAndOperationParamsInnerClassesAndMethods.SUPER_SIMPLE);

		assertThrows(InternalErrorException.class, () -> {
			buildMethodParams(sampleMethod, REQUEST_DETAILS, null);
		});
	}

	@Test
	void buildMethodParams_withNullMethodAndParams_shouldThrowInternalErrorException() {
		assertThrows(InternalErrorException.class, () -> buildMethodParams(null, null, null));
	}

	@Test
	void buildMethodParams_multipleRequestDetails_shouldThrowInternalErrorException() {
		final Method method = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_MULTIPLE_REQUEST_DETAILS,
				RequestDetails.class, SampleParams.class, RequestDetails.class);
		final Object[] inputParams = new Object[]{REQUEST_DETAILS, new IdDt(), "param1", List.of("param2", REQUEST_DETAILS)};
		assertThrows(InternalErrorException.class, () -> {
			buildMethodParams(method, REQUEST_DETAILS, inputParams);
		});
	}

	@Test
	void buildMethodParams_withClassMiissingParameterAnnotations_shouldThrowInternalErrorException() {
		final Method method = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_PARAM_NO_EMBEDDED_TYPE, ParamsWithoutAnnotations.class);

		final Object[] inputParams = new Object[]{new IdDt(), "param1", 2, List.of("param3")};

		assertThrows(InternalErrorException.class, () -> {
			buildMethodParams(method, REQUEST_DETAILS, inputParams);
		});
	}

	@Test
	void paramsConversionZonedDateTime() {
		final Method method = myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(SIMPLE_METHOD_WITH_PARAMS_CONVERSION, ParamsWithTypeConversion.class);

		final Object[] inputParams = new Object[]{"2024-01-01", "2025-01-01"};
		final Object[] expectedOutputParams = new Object[]{
			 new ParamsWithTypeConversion(
				  LocalDate.of(2024, Month.JANUARY, 1).atStartOfDay(ZoneOffset.UTC),
				  LocalDate.of(2025, Month.JANUARY, 1).atStartOfDay(ZoneOffset.UTC)
						.plusDays(1)
						.minusSeconds(1))};

		final Object[] actualOutputParams = buildMethodParams(method, REQUEST_DETAILS, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	private Object[] buildMethodParams(Method theMethod, RequestDetails theRequestDetails, Object[] theInputParams) {
		return new BaseMethodBindingMethodParameterBuilder(theMethod, theRequestDetails, theInputParams)
			 .build();
	}
}
