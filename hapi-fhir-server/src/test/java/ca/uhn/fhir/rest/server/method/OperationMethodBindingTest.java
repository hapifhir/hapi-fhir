package ca.uhn.fhir.rest.server.method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

@ExtendWith(MockitoExtension.class)
class OperationMethodBindingTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private Method myMethod;

	@Mock
	private Object provider;

	@Operation(name = "")
	void invalidOperation() {

	}

	@Test
	void constructor_withInvalidOperationName_shouldThrowConfigurationException() throws NoSuchMethodException {
		myMethod = getClass().getDeclaredMethod("invalidOperation");

		final Operation operation = myMethod.getAnnotation(Operation.class);

		ConfigurationException exception = assertThrows(ConfigurationException.class, () -> {
			new OperationMethodBinding(
				IBaseResource.class, null, myMethod, ourFhirContext, provider, operation);
		});

		assertTrue(exception.getMessage().contains("is annotated with @Operation but this annotation has no name defined"));
	}

	@Test
	void incomingServerRequestMatchesMethod_withMismatchedOperation_shouldReturnNone() {
		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setOperation("differentOperation");
		requestDetails.setRequestType(RequestTypeEnum.GET);

		OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, provider, mock(Operation.class));

		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void incomingServerRequestMatchesMethod_withMatchingOperation_shouldReturnExact() {
		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setOperation("$operationName");
		requestDetails.setRequestType(RequestTypeEnum.GET);

		Operation operation = mock(Operation.class);
		when(operation.name()).thenReturn("$operationName");

		OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, provider, operation);

		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(requestDetails));
	}

	@Test
	void invokeServer_withUnsupportedRequestType_shouldThrowMethodNotAllowedException() {
		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestType(RequestTypeEnum.PUT);

		Operation operation = mock(Operation.class);
		when(operation.name()).thenReturn("$operationName");

		OperationMethodBinding binding = new OperationMethodBinding(
			IBaseResource.class, null, myMethod, ourFhirContext, provider, operation);

		MethodNotAllowedException exception = assertThrows(MethodNotAllowedException.class, () -> {
			binding.invokeServer(null, requestDetails, new Object[]{});
		});

		assertTrue(exception.getMessage().contains("methodNotSupported"));
	}
}
