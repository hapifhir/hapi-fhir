package ca.uhn.fhir.rest.server.method;

import static org.junit.jupiter.api.Assertions.*;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SampleParamsWithIdParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.List;

// LUKETODO:  consider whether this test needs to live here or in r4 structures
@ExtendWith(MockitoExtension.class)
class OperationMethodBindingTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private final InnerClassesAndMethods myInnerClassesAndMethods = new InnerClassesAndMethods();

	private Method myMethod;
	private Operation myOperation;

	@Mock
	private Object provider;

	@Test
	void constructor_withInvalidOperationName_shouldThrowConfigurationException() {
		init("invalidOperation");

		final ConfigurationException exception = assertThrows(ConfigurationException.class, () -> {
			new OperationMethodBinding(
				IBaseResource.class, null, myMethod, ourFhirContext, provider, myOperation);
		});

		assertTrue(exception.getMessage().contains("is annotated with @Operation but this annotation has no name defined"));
	}

	@Test
	void incomingServerRequestMatchesMethod_withMismatchedOperation_shouldReturnNone() throws NoSuchMethodException {
		init("simpleOperation");

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setOperation("differentOperation");
		requestDetails.setRequestType(RequestTypeEnum.GET);

		final OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, provider, myOperation);

		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void incomingServerRequestMatchesMethod_withMatchingOperation_shouldReturnExact() throws NoSuchMethodException {
		init("simpleOperation");

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setOperation("$simpleOperation");
		requestDetails.setRequestType(RequestTypeEnum.GET);

		final OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, provider, myOperation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void invokeServer_withUnsupportedRequestType_shouldThrowMethodNotAllowedException() throws NoSuchMethodException {
		init("simpleOperation");

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.PUT);

		final OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, provider, myOperation);

		final MethodNotAllowedException exception = assertThrows(MethodNotAllowedException.class, () -> {
			binding.invokeServer(null, requestDetails, new Object[]{});
		});

		assertTrue(exception.getMessage().contains("HTTP Method PUT is not allowed for this operation."));
	}

	@Test
	void simpleMethodOperationParams() {
		init(InnerClassesAndMethods.SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class, BooleanType.class);

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.GET);
		requestDetails.setOperation("$sampleMethodOperationParams");
		requestDetails.setResourceName(ResourceType.Measure.name());
		requestDetails.setId(new IdType(ResourceType.Measure.name(), "Measure/123"));

		final OperationMethodBinding binding = new OperationMethodBinding(
			 IBaseResource.class, null, myMethod, ourFhirContext, provider, myOperation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void simpleMethodEmbeddedParams() {
		init(InnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE, SampleParamsWithIdParam.class);

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.GET);
		requestDetails.setOperation("$sampleMethodEmbeddedTypeNoRequestDetailsWithIdType");
		requestDetails.setResourceName(ResourceType.Measure.name());
		requestDetails.setId(new IdType(ResourceType.Measure.name(), "Measure/123"));

		final OperationMethodBinding binding = new OperationMethodBinding(
			 IBaseResource.class, null, myMethod, ourFhirContext, provider, myOperation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	private void init(String theMethodName, Class<?>... theParamClasses) {
		myMethod = myInnerClassesAndMethods.getDeclaredMethod(theMethodName, theParamClasses);
		myOperation = myMethod.getAnnotation(Operation.class);
	}
}
