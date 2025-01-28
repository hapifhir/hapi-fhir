package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.Elements;
import ca.uhn.fhir.rest.annotation.EmbeddableOperationParams;
import ca.uhn.fhir.rest.annotation.EmbeddedOperationParams;
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Offset;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.ServerBase;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.fail;

// This class lives in hapi-fhir-structures-r4 because if we introduce it in hapi-fhir-server, there will be a
// circular dependency
// Methods and embedded param classes to be used for testing regression code in hapi-fhir-server method classes
class MethodAndOperationParamsInnerClassesAndMethods {

	static final String METHOD_WITH_DESCRIPTION = "methodWithDescription";
	static final String METHOD_WITH_INVALID_GENERIC_TYPE = "methodWithInvalidGenericType";
	static final String METHOD_WITH_UNKNOWN_TYPE_NAME = "methodWithUnknownTypeName";
	static final String METHOD_WITH_NON_ASSIGNABLE_TYPE_NAME = "methodWithNonAssignableTypeName";
	static final String METHOD_WITH_NO_ANNOTATIONS = "methodWithNoAnnotations";
	static final String METHOD_WITH_INVALID_ANNOTATION = "methodWithInvalidAnnotation";
	static final String SAMPLE_METHOD_EMBEDDED_TYPE_MULTIPLE_REQUEST_DETAILS = "sampleMethodEmbeddedTypeMultipleRequestDetails";
	static final String SIMPLE_OPERATION = "simpleOperation";
	static final String SUPER_SIMPLE = "superSimple";
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
		//	No Implementation
	}

	@Operation(name = "")
	void invalidOperation() {
		//	No Implementation
	}

	@Operation(name = "$simpleOperation")
	void simpleOperation() {
		//	No Implementation
	}

	@Operation(name = "$withEmbeddedParams")
	void withEmbeddedParams() {
		//	No Implementation
	}

	void invalidMethodOperationParamsNoOperationInvalid(
		@OperationParam(name = "param1") String theParam1) {

		//	No Implementation
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

	@Description(
		 shortDefinition="network identifier",
		 example="An identifier for the network access point of the user device for the audit event"
	)
	public void methodWithDescription() {
		//		No Implementation
	}

	// Basic Search Parameters
	public void methodWithRequiredParam(@RequiredParam(name = "requiredParam") String param) {
		//		No Implementation
	}
	public void methodWithOptionalParam(@OptionalParam(name = "optionalParam") String param) {
	//		No Implementation
	}
	public void invalidOptionalParamInteger(@OptionalParam(name = "optionalParam") Integer param) {
	//		No Implementation
	}

	// ResourceParam
	public void methodWithResourceParam(@ResourceParam IBaseResource resource) {
	//		No Implementation
	}
	public void methodWithResourceParamString(@ResourceParam String resourceString) {
	//		No Implementation
	}
	public void methodWithResourceParamByteArray(@ResourceParam byte[] resourceBytes) {
	//		No Implementation
	}

	// Servlet/Request parameters
	public void methodWithServletRequest(ServletRequest request) {
	//		No Implementation
	}
	public void methodWithServletResponse(ServletResponse response) {
	//		No Implementation
	}
	public void methodWithRequestDetails(RequestDetails details) {
	//		No Implementation
	}
	public void methodWithInterceptorBroadcaster(IInterceptorBroadcaster broadcaster) {
	//		No Implementation
	}

	// ID Param -> NullParameter
	public void methodWithIdParam(@IdParam String theId) {
	//		No Implementation
	}

	// Server Base -> ServerBaseParamBinder
	public void methodWithServerBase(@ServerBase String base) {
	//		No Implementation
	}

	// Elements -> ElementsParameter
	public void methodWithElements(@Elements String elements) {
	//		No Implementation
	}

	// Since -> SinceParameter
	public void methodWithSince(@Since Date sinceDate) {
	//		No Implementation
	}

	// At -> AtParameter
	public void methodWithAt(@At Date atDate) {
	//		No Implementation
	}

	// Count -> CountParameter
	public void methodWithCount(@Count Integer count) {
	//		No Implementation
	}

	// Offset -> OffsetParameter
	public void methodWithOffset(@Offset Integer offset) {
	//		No Implementation
	}

	// SummaryEnum -> SummaryEnumParameter
	public void methodWithSummaryEnum(SummaryEnum summary) {
	//		No Implementation
	}

	// PatchTypeEnum -> PatchTypeParameter
	public void methodWithPatchType(PatchTypeEnum patchType) {
	//		No Implementation
	}

	// SearchContainedModeEnum -> SearchContainedModeParameter
	public void methodWithSearchContainedMode(SearchContainedModeEnum containedMode) {
	//		No Implementation
	}

	// SearchTotalModeEnum -> SearchTotalModeParameter
	public void methodWithSearchTotalMode(ca.uhn.fhir.rest.api.SearchTotalModeEnum totalMode) {
	//		No Implementation
	}

	// GraphQL Query Url -> GraphQLQueryUrlParameter
	public void methodWithGraphQLQueryUrl(@GraphQLQueryUrl String queryUrl) {
 //		No Implementation
	}

	// GraphQL Query Body -> GraphQLQueryBodyParameter
	public void methodWithGraphQLQueryBody(@GraphQLQueryBody String queryBody) {
		 // No Implementation
	}

	// Sort -> SortParameter
	public void invalidMethodWithSort(@Sort String sort) {
		// no implementation
	}

	public void methodWithSort(@Sort SortSpec sort) {
		// no implementation
	}

	// TransactionParam -> TransactionParameter
	public void methodWithTransactionParam(@TransactionParam IBaseResource bundle) {
		// no implementation
	}

	// ConditionalUrlParam -> ConditionalParamBinder
	public void methodWithConditionalUrlParam(@ConditionalUrlParam String conditionalUrl) {
		// no implementation
	}

	// OperationParam (requires @Operation)
	@Operation(name = "opTest", idempotent = true)
	public void methodWithOperationParam(@OperationParam(name = "opParam") String opParam) {
		// no implementation
	}

	// OperationParam with typeName
	@Operation(name = "opTest2", idempotent = true)
	public void methodWithOperationParamAndTypeName(@OperationParam(name = "opParamTyped", typeName = "string") StringType typedParam) {
		// no implementation
	}

	@Operation(name = "opTest2", idempotent = true)
	public void invalidMethodWithOperationParamAndTypeName(@OperationParam(name = "opParamTyped", typeName = "string") String typedParam) {
		// no implementation
	}

	// Validate -> Validate.Mode and Validate.Profile
	public void methodWithValidateAnnotations(@Validate.Mode ValidationModeEnum mode,
															@Validate.Profile String profile) {
		// no implementation
	}

	// Unknown param -> no recognized annotation, should fail
	public void methodWithUnknownParam(Double someParam) {
		// no implementation
	}

	// TagList -> should become NullParameter
	public void methodWithTagList(TagList tagList) {
		// no implementation
	}

	// Force a Collection of Collections => error
	public void methodWithCollectionOfCollections(List<List<String>> doubleCollection) {
		// no implementation
	}

	// Force a param IPrimitiveType<Date> => triggers special code path
	public void methodWithIPrimitiveTypeDate(
		 @RequiredParam(name = "primitiveTypeDateParam") IPrimitiveType<Date> listParam) {
		// no implementation
	}

	public void invalidMethodWithIPrimitiveTypeDate(
		 @RequiredParam(name = "primitiveTypeDateParam") List<IPrimitiveType<Date>> listParam) {
			// no implementation
	}
}
