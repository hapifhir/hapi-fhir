package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SampleParams;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static ca.uhn.fhir.rest.server.method.BaseMethodBindingMethodParameterBuilder.buildMethodParams;
import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseMethodBindingMethodParameterBuilderTest {

	// LUKETODO:  assert Exception messages

	private static final org.slf4j.Logger ourLog =
		LoggerFactory.getLogger(BaseMethodBindingMethodParameterBuilderTest.class);

	private static final RequestDetails REQUEST_DETAILS = new SystemRequestDetails();

	private final InnerClassesAndMethods myInnerClassesAndMethods = new InnerClassesAndMethods();

	// LUKETODO:  wrong params
	// LUKETODO:  wrong param order
	// LUKETODO:  RequestDetails passed but not in signature
	// LUKETODO:  RequestDetails in signature but not passed

	@Test
	void happyPathOperationParamsEmptyParams() throws InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = myInnerClassesAndMethods.getDeclaredMethod(SUPER_SIMPLE);
		final Object[] inputParams = new Object[]{};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(inputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationParamsNonEmptyParams() throws InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = myInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class);
		final Object[] inputParams = new Object[]{new IdDt(), "param1", List.of("param2")};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(inputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesNoRequestDetails() throws InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = myInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, SampleParams.class);
		final Object[] inputParams = new Object[]{"param1", List.of("param2")};
		final Object[] expectedOutputParams = new Object[]{new SampleParams("param1", List.of("param2"))};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesRequestDetailsFirst() throws InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = myInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST, RequestDetails.class, SampleParams.class);
		final Object[] inputParams = new Object[]{REQUEST_DETAILS, "param1", List.of("param2")};
		final Object[] expectedOutputParams = new Object[]{REQUEST_DETAILS, new SampleParams("param1", List.of("param2"))};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesRequestDetailsLast() throws InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = myInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST, SampleParams.class, RequestDetails.class);
		final Object[] inputParams = new Object[]{"param1", List.of("param3"), REQUEST_DETAILS};
		final Object[] expectedOutputParams = new Object[]{new SampleParams("param1", List.of("param3")), REQUEST_DETAILS};

		final Object[] actualOutputParams = buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	@Disabled
	// LUKETODO:  Figure out what we're doing with FHIR structures in this test module before testing anything with IdTypes...
	void happyPathOperationEmbeddedTypesWithIdType() throws InvocationTargetException, IllegalAccessException, InstantiationException {
//		final IIdType id = new IIdType();
//		final Method sampleMethod = myInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST_WITH_ID_TYPE, RequestDetails.class, SampleParamsWithIdParam.class);
//		final Object[] inputParams = new Object[]{REQUEST_DETAILS, id, "param1", List.of("param2")};
//		final Object[] expectedOutputParams = new Object[]{REQUEST_DETAILS, new SampleParamsWithIdParam(id, "param1", List.of("param2")),};
//
//		final Object[] actualOutputParams = buildMethodParams(sampleMethod, inputParams);
//
//		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void buildMethodParams_withNullMethod_shouldThrowInternalErrorException() {
		assertThrows(InternalErrorException.class, () -> {
			buildMethodParams(null, new Object[]{});
		});
	}

	@Test
	void buildMethodParams_withNullParams_shouldThrowInternalErrorException() throws NoSuchMethodException {
		final Method sampleMethod = InnerClassesAndMethods.class.getDeclaredMethod(SUPER_SIMPLE);

		assertThrows(InternalErrorException.class, () -> {
			buildMethodParams(sampleMethod, null);
		});
	}

	@Test
	void buildMethodParams_withNullMethodAndParams_shouldThrowInternalErrorException() {
		assertThrows(InternalErrorException.class, () -> buildMethodParams(null, null));
	}

	@Test
	void buildMethodParams_multipleRequestDetails_shouldThrowInternalErrorException() {
		final Method method = myInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_EMBEDDED_TYPE_MULTIPLE_REQUEST_DETAILS,
				RequestDetails.class, SampleParams.class, RequestDetails.class);
		final Object[] inputParams = new Object[]{REQUEST_DETAILS, new IdDt(), "param1", List.of("param2", REQUEST_DETAILS)};
		assertThrows(InternalErrorException.class, () -> {
			buildMethodParams(method, inputParams);
		});
	}

	// LUKETODO:  decide what to do with this
	@Test
	@Disabled
	void buildMethodParams_withClassMiissingParameterAnnotations_shouldThrowInternalErrorException() {
		final Method method = myInnerClassesAndMethods.getDeclaredMethod(SAMPLE_METHOD_PARAM_NO_EMBEDDED_TYPE, ParamsWithoutAnnotations.class);

		final Object[] inputParams = new Object[]{new IdDt(), "param1", 2, List.of("param3")};

		assertThrows(InternalErrorException.class, () -> {
			buildMethodParams(method, inputParams);
		});
	}
}
