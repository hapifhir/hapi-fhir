package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.annotation.EmbeddableOperationParams;
import ca.uhn.fhir.rest.annotation.EmbeddedOperationParams;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.fail;

// This class lives in hapi-fhir-structures-r4 because if we introduce it in hapi-fhir-server, there will be a
// circular dependency
// Methods and embedded param classes to be used for testing regression code in hapi-fhir-server method classes
class EmbeddedParamsInnerClassesAndMethods {

	static final String SAMPLE_METHOD_EMBEDDED_TYPE_MULTIPLE_REQUEST_DETAILS = "sampleMethodEmbeddedTypeMultipleRequestDetails";
	static final String SUPER_SIMPLE = "superSimple";
	static final String SIMPLE_OPERATION = "simpleOperation";
	static final String INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION = "invalidMethodOperationParamsNoOperationInvalid";
	static final String SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST_WITH_ID_TYPE = "sampleMethodEmbeddedTypeRequestDetailsFirstWithIdType";
	static final String SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST = "sampleMethodEmbeddedTypeRequestDetailsLast";
	static final String SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST = "sampleMethodEmbeddedTypeRequestDetailsFirst";
	static final String SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS = "sampleMethodEmbeddedTypeNoRequestDetails";
	static final String SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE = "sampleMethodEmbeddedTypeNoRequestDetailsWithIdType";
	static final String SAMPLE_METHOD_OPERATION_PARAMS = "sampleMethodOperationParams";
	static final String SAMPLE_METHOD_PARAM_NO_EMBEDDED_TYPE = "sampleMethodParamNoEmbeddedType";
	static final String SIMPLE_METHOD_WITH_PARAMS_CONVERSION = "simpleMethodWithParamsConversion";
	static final String SAMPLE_METHOD_EMBEDDED_TYPE_ID_TYPE_AND_TYPE_CONVERSION = "sampleMethodEmbeddedTypeIdTypeAndTypeConversion";

	static final String EXPAND = "expand";
	static final String OP_INSTANCE_OR_TYPE = "opInstanceOrType";


	Method getDeclaredMethod(String theMethodName, Class<?>... theParamClasses) {
		return getDeclaredMethod(this.getClass(), theMethodName, theParamClasses);
	}

	Method getDeclaredMethod(@Nullable Object provider, String theMethodName, Class<?>... theParamClasses) {
		return getDeclaredMethod(
			 provider != null ? provider.getClass() : this.getClass(),
			 theMethodName,
			 theParamClasses);
	}

	Method getDeclaredMethod(Class<?> theContainingClass, String theMethodName, Class<?>... theParamClasses) {
		try {
			return theContainingClass.getDeclaredMethod(theMethodName, theParamClasses);
		} catch (Exception exceptional) {
			fail(String.format("Could not find method: %s with params: %s", theMethodName, Arrays.toString(theParamClasses)));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T)theObject;
	}

	// Below are the methods and classed to test reflection code
	public void methodWithNoAnnotations(String param) {
		// Method implementation
	}

	public void methodWithInvalidGenericType(List<List<String>> param) {
		// Method implementation
	}

	public void methodWithUnknownTypeName(String param) {
		// Method implementation
	}

	public void methodWithNonAssignableTypeName(String param) {
		// Method implementation
	}

	public void methodWithInvalidAnnotation(String param) {
		// Method implementation
	}

	void superSimple() {
	}

	@Operation(name = "")
	void invalidOperation() {

	}

	@Operation(name = "$simpleOperation")
	void simpleOperation() {

	}

	@Operation(name = "$withEmbeddedParams")
	void withEmbeddedParams() {

	}

	void invalidMethodOperationParamsNoOperationInvalid(
		@OperationParam(name = "param1") String theParam1) {

	}

	@Operation(name="sampleMethodOperationParams", type = Measure.class)
	public MeasureReport sampleMethodOperationParams(
		@IdParam IIdType theIdType,
		@OperationParam(name = "param1") String theParam1,
		@OperationParam(name = "param2") List<String> theParam2,
		@OperationParam(name="param3") BooleanType theParam3) {
		// Sample method for testing
		return new MeasureReport(null, null, null, null);
	}

	@EmbeddableOperationParams
	static class ParamsWithoutAnnotations {
		private final String myParam1;
		private final List<String> myParam2;

		public ParamsWithoutAnnotations(String myParam1, List<String> myParam2) {
			this.myParam1 = myParam1;
			this.myParam2 = myParam2;
		}

		public String getParam1() {
			return myParam1;
		}

		public List<String> getParam2() {
			return myParam2;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			ParamsWithoutAnnotations that = (ParamsWithoutAnnotations) o;
			return Objects.equals(myParam1, that.myParam1) && Objects.equals(myParam2, that.myParam2);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2);
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", ParamsWithoutAnnotations.class.getSimpleName() + "[", "]")
				.add("myParam1='" + myParam1 + "'")
				.add("myParam2=" + myParam2)
				.toString();
		}
	}

	// Ignore warnings that these classes can be records.  Converting them to records will make the tests fail
	@EmbeddableOperationParams
	static class SampleParams {
		private final String myParam1;

		private final List<String> myParam2;

		public SampleParams(
			 @OperationParam(name = "param1")
			 String myParam1,
			 @OperationParam(name = "param2")
			 List<String> myParam2) {
			this.myParam1 = myParam1;
			this.myParam2 = myParam2;
		}

		public String getParam1() {
			return myParam1;
		}

		public List<String> getParam2() {
			return myParam2;
		}


		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			SampleParams that = (SampleParams) o;
			return Objects.equals(myParam1, that.myParam1) && Objects.equals(myParam2, that.myParam2);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2);
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", SampleParams.class.getSimpleName() + "[", "]")
				.add("myParam1='" + myParam1 + "'")
				.add("myParam2=" + myParam2)
				.toString();
		}
	}

	@EmbeddableOperationParams
	static class ParamsWithTypeConversion {
		private final ZonedDateTime myPeriodStart;

		private final ZonedDateTime myPeriodEnd;

		public ParamsWithTypeConversion(
				 @OperationParam(name = "periodStart", sourceType = String.class, rangeType = OperationParameterRangeType.START)
				 ZonedDateTime thePeriodStart,
				 @OperationParam(name = "periodEnd", sourceType = String.class, rangeType = OperationParameterRangeType.END)
				 ZonedDateTime thePeriodEnd) {
			myPeriodStart = thePeriodStart;
			myPeriodEnd = thePeriodEnd;
		}

		public ZonedDateTime getPeriodStart() {
			return myPeriodStart;
		}

		public ZonedDateTime getPeriodEnd() {
			return myPeriodEnd;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			ParamsWithTypeConversion that = (ParamsWithTypeConversion) o;
			return Objects.equals(myPeriodStart, that.myPeriodStart)
				 && Objects.equals(myPeriodEnd, that.myPeriodEnd);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myPeriodStart, myPeriodEnd);
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", ParamsWithTypeConversion.class.getSimpleName() + "[", "]")
				 .add("myPeriodStart=" + myPeriodStart)
				 .add("myPeriodEnd=" + myPeriodEnd)
				 .toString();
		}
	}

	// Ignore warnings that these classes can be records.  Converting them to records will make the tests fail
	@EmbeddableOperationParams
	static class SampleParamsWithIdParam {
		private final IdType myId;

		private final String myParam1;

		private final List<String> myParam2;

		private final BooleanType myParam3;

		public SampleParamsWithIdParam(
				 @IdParam
				 IdType theId,
				 @OperationParam(name = "param1")
				 String theParam1,
				 @OperationParam(name = "param2")
				 List<String> theParam2,
				 @OperationParam(name = "param3")
				 BooleanType theParam3) {
			myId = theId;
			myParam1 = theParam1;
			myParam2 = theParam2;
			myParam3 = theParam3;
		}

		public IdType getId() {
			return myId;
		}

		public String getParam1() {
			return myParam1;
		}

		public List<String> getParam2() {
			return myParam2;
		}

		public BooleanType getMyParam3() {
			return myParam3;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			SampleParamsWithIdParam that = (SampleParamsWithIdParam) o;
			return Objects.equals(myId, that.myId) &&
				Objects.equals(myParam1, that.myParam1) &&
				Objects.equals(myParam2, that.myParam2) &&
				Objects.equals(myParam3.booleanValue(), that.myParam3.booleanValue());
		}

		@Override
		public int hashCode() {
			return Objects.hash(myId, myParam1, myParam2, myParam3.booleanValue());
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", SampleParamsWithIdParam.class.getSimpleName() + "[", "]")
				.add("myId=" + myId)
				.add("myParam1='" + myParam1 + "'")
				.add("myParam2=" + myParam2)
				.add("myParam3=" + myParam3.booleanValue())
				.toString();
		}
	}

	@EmbeddableOperationParams
	static class ParamsWithIdParamAndTypeConversion {
		@IdParam
		private final IdType myId;

		private final ZonedDateTime myPeriodStart;

		private final ZonedDateTime myPeriodEnd;

		public ParamsWithIdParamAndTypeConversion(
			 @IdParam
			 IdType theId,
			 @OperationParam(name = "periodStart", sourceType = String.class, rangeType = OperationParameterRangeType.START)
			 ZonedDateTime thePeriodStart,
			 @OperationParam(name = "periodEnd", sourceType = String.class, rangeType = OperationParameterRangeType.END)
			 ZonedDateTime thePeriodEnd) {
			myId = theId;
			myPeriodStart = thePeriodStart;
			myPeriodEnd = thePeriodEnd;
		}

		public IdType getId() {
			return myId;
		}

		public ZonedDateTime getPeriodStart() {
			return myPeriodStart;
		}

		public ZonedDateTime getPeriodEnd() {
			return myPeriodEnd;
		}

		@Override
		public boolean equals(Object theO) {
			if (theO == null || getClass() != theO.getClass()) {
				return false;
			}
			ParamsWithIdParamAndTypeConversion that = (ParamsWithIdParamAndTypeConversion) theO;
			return Objects.equals(myId, that.myId) && Objects.equals(myPeriodStart, that.myPeriodStart) && Objects.equals(myPeriodEnd, that.myPeriodEnd);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myId, myPeriodStart, myPeriodEnd);
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", ParamsWithIdParamAndTypeConversion.class.getSimpleName() + "[", "]")
				 .add("myId=" + myId)
				 .add("myPeriodStart=" + myPeriodStart)
				 .add("myPeriodEnd=" + myPeriodEnd)
				 .toString();
		}
	}

	@Operation(name="sampleMethodEmbeddedTypeRequestDetailsFirst")
	String sampleMethodEmbeddedTypeRequestDetailsFirst(RequestDetails theRequestDetails, @EmbeddedOperationParams SampleParams theParams) {
		// return something arbitrary
		return theRequestDetails.getId().getValue() + theParams.getParam1();
	}

	@Operation(name="sampleMethodEmbeddedTypeRequestDetailsLast")
	String sampleMethodEmbeddedTypeRequestDetailsLast(@EmbeddedOperationParams SampleParams theParams, RequestDetails theRequestDetails) {
		// return something arbitrary
		return theRequestDetails.getId().getValue() + theParams.getParam1();
	}

	@Operation(name="sampleMethodEmbeddedTypeNoRequestDetails")
	String sampleMethodEmbeddedTypeNoRequestDetails(@EmbeddedOperationParams SampleParams theParams) {
		// return something arbitrary
		return theParams.getParam1();
	}

	@Operation(name="simpleMethodWithParamsConversion")
	String simpleMethodWithParamsConversion(@EmbeddedOperationParams ParamsWithTypeConversion theParams) {
		// return something arbitrary
		return theParams.getPeriodStart().toString();
	}

	String sampleMethodParamNoEmbeddedType(@EmbeddedOperationParams ParamsWithoutAnnotations theParams) {
		// return something arbitrary
		return theParams.getParam1();
	}

	String sampleMethodEmbeddedTypeMultipleRequestDetails(RequestDetails theRequestDetails1, @EmbeddedOperationParams SampleParams theParams, RequestDetails theRequestDetails2) {
		// return something arbitrary
		return theRequestDetails1.getId().getValue() + theParams.getParam1() + theRequestDetails2.getId().getValue();
	}

	String sampleMethodEmbeddedTypeRequestDetailsFirstWithIdType(RequestDetails theRequestDetails, @EmbeddedOperationParams SampleParamsWithIdParam theParams) {
		// return something arbitrary
		return theRequestDetails.getId().getValue() + theParams.getParam1();
	}

	@Operation(name="sampleMethodEmbeddedTypeIdTypeAndTypeConversion")
	String sampleMethodEmbeddedTypeIdTypeAndTypeConversion(@EmbeddedOperationParams ParamsWithIdParamAndTypeConversion theParams) {
		// return something arbitrary
		return theParams.getId().getValue();
	}

	String sampleMethodEmbeddedTypeRequestDetailsLastWithIdType(@EmbeddedOperationParams SampleParamsWithIdParam theParams, RequestDetails theRequestDetails) {
		// return something arbitrary
		return theRequestDetails.getId().getValue() + theParams.getParam1();
	}

	@Operation(name="sampleMethodEmbeddedTypeNoRequestDetailsWithIdType", type = Measure.class)
	MeasureReport sampleMethodEmbeddedTypeNoRequestDetailsWithIdType(@EmbeddedOperationParams SampleParamsWithIdParam theParams) {
		// return something arbitrary
		return new MeasureReport(null, null, null, null);
	}

	@Operation(name = "$expand", idempotent = true, typeName = "ValueSet")
	IBaseResource expand(
			 HttpServletRequest theServletRequest,
			 @IdParam(optional = true) IIdType theId,
			 @OperationParam(name = "valueSet", min = 0, max = 1) IBaseResource theValueSet,
			 RequestDetails theRequestDetails) {
		return new Patient();
	}

	// More realistic scenario where method binding actually checks the provider resource type
	static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Operation(name = "$OP_INSTANCE_OR_TYPE")
		Parameters opInstanceOrType(
			 @IdParam(optional = true) IdType theId,
			 @OperationParam(name = "PARAM1" ) StringType theParam1,
			 @OperationParam(name = "PARAM2" ) Patient theParam2) {
			return new Parameters();
		}
	}
}
