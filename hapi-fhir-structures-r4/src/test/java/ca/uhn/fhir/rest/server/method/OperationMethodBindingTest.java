package ca.uhn.fhir.rest.server.method;

import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.EXPAND;
import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.OP_INSTANCE_OR_TYPE;
import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE;
import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SAMPLE_METHOD_OPERATION_PARAMS;
import static org.junit.jupiter.api.Assertions.*;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.PatientProvider;
import ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SampleParamsWithIdParam;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

// This test lives in hapi-fhir-structures-r4 because if we introduce it in hapi-fhir-server, there will be a
// circular dependency
class OperationMethodBindingTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private final InnerClassesAndMethods myInnerClassesAndMethods = new InnerClassesAndMethods();

	private Method myMethod;
	private Operation myOperation;

	private Object myProvider = null;

	@Test
	void constructor_withInvalidOperationName_shouldThrowConfigurationException() {
		init("invalidOperation");

		final ConfigurationException exception = assertThrows(ConfigurationException.class, () -> {
			new OperationMethodBinding(
				IBaseResource.class, null, myMethod, ourFhirContext, myProvider, myOperation);
		});

		assertTrue(exception.getMessage().contains("is annotated with @Operation but this annotation has no name defined"));
	}

	@Test
	void incomingServerRequestMatchesMethod_withMismatchedOperation_shouldReturnNone() {
		init("simpleOperation");

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setOperation("differentOperation");
		requestDetails.setRequestType(RequestTypeEnum.GET);

		final OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, myProvider, myOperation);

		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void incomingServerRequestMatchesMethod_withMatchingOperation_shouldReturnExact() {
		init("simpleOperation");

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setOperation("$simpleOperation");
		requestDetails.setRequestType(RequestTypeEnum.GET);

		final OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, myProvider, myOperation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void invokeServer_withUnsupportedRequestType_shouldThrowMethodNotAllowedException() {
		init(InnerClassesAndMethods.SIMPLE_OPERATION);

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.PUT);

		final OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, myProvider, myOperation);

		final MethodNotAllowedException exception = assertThrows(MethodNotAllowedException.class, () -> {
			binding.invokeServer(null, requestDetails, new Object[]{});
		});

		assertTrue(exception.getMessage().contains("HTTP Method PUT is not allowed for this operation."));
	}

	@Test
	void simpleMethodOperationParams() {
		init(SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class, BooleanType.class);

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.GET);
		requestDetails.setOperation("$sampleMethodOperationParams");
		requestDetails.setResourceName(ResourceType.Measure.name());
		requestDetails.setId(new IdType(ResourceType.Measure.name(), "Measure/123"));

		final OperationMethodBinding binding = new OperationMethodBinding(
			 IBaseResource.class, null, myMethod, ourFhirContext, myProvider, myOperation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void simpleMethodEmbeddedParams() {
		init(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE, SampleParamsWithIdParam.class);

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.GET);
		requestDetails.setOperation("$sampleMethodEmbeddedTypeNoRequestDetailsWithIdType");
		requestDetails.setResourceName(ResourceType.Measure.name());
		requestDetails.setId(new IdType(ResourceType.Measure.name(), "Measure/123"));

		final OperationMethodBinding binding = new OperationMethodBinding(
			 IBaseResource.class, null, myMethod, ourFhirContext, myProvider, myOperation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void expandEnsureMethodEnsureCanOperateAtTypeLevel() {
		init(EXPAND, HttpServletRequest.class, IIdType.class, IBaseResource.class, RequestDetails.class);

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.POST);
		requestDetails.setOperation("$expand");
		requestDetails.setResourceName(ResourceType.ValueSet.name());

		final OperationMethodBinding binding = new OperationMethodBinding(
			 IBaseResource.class, null, myMethod, ourFhirContext, myProvider, myOperation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void methodWithIdParamButNoIIdType() {
		myProvider = new PatientProvider();
		init(OP_INSTANCE_OR_TYPE, IdType.class, StringType.class, Patient.class);

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.POST);
		requestDetails.setOperation("$OP_INSTANCE_OR_TYPE");
		requestDetails.setResourceName(ResourceType.Patient.name());

		final OperationMethodBinding binding = new OperationMethodBinding(
			 Patient.class, Patient.class, myMethod, ourFhirContext, myProvider, myOperation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	private void init(String theMethodName, Class<?>... theParamClasses) {
		myMethod = myInnerClassesAndMethods.getDeclaredMethod(myProvider, theMethodName, theParamClasses);
		myOperation = myMethod.getAnnotation(Operation.class);
	}
}
