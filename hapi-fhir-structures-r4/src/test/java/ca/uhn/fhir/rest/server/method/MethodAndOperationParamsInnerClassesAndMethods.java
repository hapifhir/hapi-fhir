package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.Elements;
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Offset;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
	static final String SIMPLE_OPERATION = "simpleOperation";
	static final String SUPER_SIMPLE = "superSimple";
	static final String INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION = "invalidMethodOperationParamsNoOperationInvalid";
	static final String SAMPLE_METHOD_OPERATION_PARAMS = "sampleMethodOperationParams";
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
