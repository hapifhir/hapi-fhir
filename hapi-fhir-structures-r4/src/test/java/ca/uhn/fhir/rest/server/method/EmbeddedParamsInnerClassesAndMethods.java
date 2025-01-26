package ca.uhn.fhir.rest.server.method;

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
