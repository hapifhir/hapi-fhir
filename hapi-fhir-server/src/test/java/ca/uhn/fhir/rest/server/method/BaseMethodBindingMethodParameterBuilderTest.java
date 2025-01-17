package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseMethodBindingMethodParameterBuilderTest {

	private static final RequestDetails REQUEST_DETAILS = new SystemRequestDetails();

	private void superSimple() {}

	private void sampleMethodOperationParams(
		@IdParam IIdType theIdType,
		@OperationParam(name = "param1") String theParam1,
		@OperationEmbeddedParam(name = "param2") Integer theParam2,
		@OperationEmbeddedParam(name = "param3") List<String> theParam3) {
		// Sample method for testing
	}

	private static class SampleParams {
		@OperationEmbeddedParam(name = "param1")
		private final String myParam1;

		@OperationEmbeddedParam(name = "param2")
		private final Integer myParam2;

		@OperationEmbeddedParam(name = "param3")
		private final List<String> myParam3;

		public SampleParams(String myParam1, Integer myParam2, List<String> myParam3) {
			this.myParam1 = myParam1;
			this.myParam2 = myParam2;
			this.myParam3 = myParam3;
		}

		public String getParam1() {
			return myParam1;
		}

		public Integer getParam2() {
			return myParam2;
		}

		public List<String> getParam3() {
			return myParam3;
		}


		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			SampleParams that = (SampleParams) o;
			return Objects.equals(myParam1, that.myParam1) && Objects.equals(myParam2, that.myParam2) && Objects.equals(myParam3, that.myParam3);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2, myParam3);
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", SampleParams.class.getSimpleName() + "[", "]")
				.add("myParam1='" + myParam1 + "'")
				.add("myParam2=" + myParam2)
				.add("myParam3=" + myParam3)
				.toString();
		}
	}

	private static class SampleParamsWithIdParam {
		@IdParam
		private final IIdType myId;

		@OperationEmbeddedParam(name = "param1")
		private final String myParam1;

		@OperationEmbeddedParam(name = "param2")
		private final Integer myParam2;

		@OperationEmbeddedParam(name = "param3")
		private final List<String> myParam3;

		public SampleParamsWithIdParam(IIdType myId, String myParam1, Integer myParam2, List<String> myParam3) {
			this.myId = myId;
			this.myParam1 = myParam1;
			this.myParam2 = myParam2;
			this.myParam3 = myParam3;
		}

		public IIdType getId() {
			return myId;
		}

		public String getParam1() {
			return myParam1;
		}

		public Integer getParam2() {
			return myParam2;
		}

		public List<String> getParam3() {
			return myParam3;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			SampleParamsWithIdParam that = (SampleParamsWithIdParam) o;
			return Objects.equals(myId, that.myId) && Objects.equals(myParam1, that.myParam1) && Objects.equals(myParam2, that.myParam2) && Objects.equals(myParam3, that.myParam3);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myId, myParam1, myParam2, myParam3);
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", SampleParamsWithIdParam.class.getSimpleName() + "[", "]")
				.add("myId=" + myId)
				.add("myParam1='" + myParam1 + "'")
				.add("myParam2=" + myParam2)
				.add("myParam3=" + myParam3)
				.toString();
		}
	}

	private String sampleMethodEmbeddedTypeRequestDetailsFirst(RequestDetails theRequestDetails, SampleParams theParams) {
		// return something arbitrary
		return theRequestDetails.getId().getValue() + theParams.getParam1();
	}

	private String sampleMethodEmbeddedTypeRequestDetailsLast(SampleParams theParams, RequestDetails theRequestDetails) {
		// return something arbitrary
		return theRequestDetails.getId().getValue() + theParams.getParam1();
	}

	private String sampleMethodEmbeddedTypeNoRequestDetails(SampleParams theParams) {
		// return something arbitrary
		return theParams.getParam1();
	}

	private String sampleMethodEmbeddedTypeMultipleRequestDetails(RequestDetails theRequestDetails1, SampleParams theParams, RequestDetails theRequestDetails2) {
		// return something arbitrary
		return theRequestDetails1.getId().getValue() + theParams.getParam1();
	}

	private String sampleMethodEmbeddedTypeRequestDetailsFirstWithIdType(RequestDetails theRequestDetails, SampleParamsWithIdParam theParams) {
		// return something arbitrary
		return theRequestDetails.getId().getValue() + theParams.getParam1();
	}

	private String sampleMethodEmbeddedTypeRequestDetailsLastWithIdType(SampleParamsWithIdParam theParams, RequestDetails theRequestDetails) {
		// return something arbitrary
		return theRequestDetails.getId().getValue() + theParams.getParam1();
	}

	private String sampleMethodEmbeddedTypeNoRequestDetailsWithIdType(SampleParams theParams) {
		// return something arbitrary
		return theParams.getParam1();
	}

	// LUKETODO:  wrong params
	// LUKETODO:  wrong param order
	// LUKETODO:  RequestDetails passed but not in signature
	// LUKETODO:  RequestDetails in signature but not passed

	@Test
	void happyPathOperationParamsEmptyParams() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = this.getClass().getDeclaredMethod("superSimple");
		final Object[] inputParams = new Object[] {};

		final Object[] actualOutputParams = BaseMethodBindingMethodParameterBuilder.buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(inputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationParamsNonEmptyParams() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethodOperationParams", IIdType.class, String.class, Integer.class, List.class);
		final Object[] inputParams = new Object[] {new IdDt(), "param1", 2, List.of("param3")};

		final Object[] actualOutputParams = BaseMethodBindingMethodParameterBuilder.buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(inputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesNoRequestDetails() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethodEmbeddedTypeNoRequestDetails", SampleParams.class);
		final Object[] inputParams = new Object[] {"param1", 2, List.of("param3")};
		final Object[] expectedOutputParams = new Object[] {new SampleParams("param1", 2, List.of("param3"))};

		final Object[] actualOutputParams = BaseMethodBindingMethodParameterBuilder.buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesRequestDetailsFirst() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethodEmbeddedTypeRequestDetailsFirst", RequestDetails.class, SampleParams.class);
		final Object[] inputParams = new Object[] {REQUEST_DETAILS, "param1", 2, List.of("param3")};
		final Object[] expectedOutputParams = new Object[] {REQUEST_DETAILS, new SampleParams("param1", 2, List.of("param3"))};

		final Object[] actualOutputParams = BaseMethodBindingMethodParameterBuilder.buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesRequestDetailsLast() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		final Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethodEmbeddedTypeRequestDetailsLast", SampleParams.class, RequestDetails.class);
		final Object[] inputParams = new Object[] {"param1", 2, List.of("param3"), REQUEST_DETAILS};
		final Object[] expectedOutputParams = new Object[] {new SampleParams("param1", 2, List.of("param3")), REQUEST_DETAILS};

		final Object[] actualOutputParams = BaseMethodBindingMethodParameterBuilder.buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void happyPathOperationEmbeddedTypesWithIdType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		final IdDt id = new IdDt();
		final Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethodEmbeddedTypeRequestDetailsFirstWithIdType", RequestDetails.class, SampleParamsWithIdParam.class);
		final Object[] inputParams = new Object[] {REQUEST_DETAILS, id, "param1", 2, List.of("param3")};
		final Object[] expectedOutputParams = new Object[] {REQUEST_DETAILS, new SampleParamsWithIdParam(id, "param1", 2, List.of("param3")), };

		final Object[] actualOutputParams = BaseMethodBindingMethodParameterBuilder.buildMethodParams(sampleMethod, inputParams);

		assertArrayEquals(expectedOutputParams, actualOutputParams);
	}

	@Test
	void buildMethodParams_withNullMethod_shouldThrowInternalErrorException() {
		assertThrows(InternalErrorException.class, () -> {
			BaseMethodBindingMethodParameterBuilder.buildMethodParams(null, new Object[]{});
		});
	}

	@Test
	void buildMethodParams_withNullParams_shouldThrowInternalErrorException() throws NoSuchMethodException {
		final Method sampleMethod = this.getClass().getDeclaredMethod("superSimple");

		assertThrows(InternalErrorException.class, () -> {
			BaseMethodBindingMethodParameterBuilder.buildMethodParams(sampleMethod, null);
		});
	}

	@Test
	void buildMethodParams_withNullMethodAndParams_shouldThrowInternalErrorException() throws NoSuchMethodException {
		assertThrows(InternalErrorException.class, () -> {
			BaseMethodBindingMethodParameterBuilder.buildMethodParams(null, null);
		});
	}

	@Test
	void buildMethodParams_multipleRequestDetails_shouldThrowInternalErrorException() throws NoSuchMethodException {
		final Method method = this.getClass().getDeclaredMethod("sampleMethodEmbeddedTypeMultipleRequestDetails", RequestDetails.class, SampleParams.class, RequestDetails.class);
		final Object[] inputParams = new Object[] {REQUEST_DETAILS, new IdDt(), "param1", 2, List.of("param3", REQUEST_DETAILS)};
		assertThrows(InternalErrorException.class, () -> {
			BaseMethodBindingMethodParameterBuilder.buildMethodParams(method, inputParams);
		});
	}


	@Test
	@Disabled
	void buildMethodParams_withNullParameterAnnotations_shouldThrowInternalErrorException() throws Exception {
//		when(myMethod.getParameterTypes()).thenReturn(new Class<?>[]{String.class});
//		when(myMethod.getParameterAnnotations()).thenReturn(null);
//
//		InternalErrorException exception = assertThrows(InternalErrorException.class, () -> {
//			BaseMethodBindingMethodParameterBuilder.buildMethodParams(myMethod, new Object[]{"param1"});
//		});
//
//		assertTrue(exception.getMessage().contains("Parameter annotations cannot be null"));
	}
}
