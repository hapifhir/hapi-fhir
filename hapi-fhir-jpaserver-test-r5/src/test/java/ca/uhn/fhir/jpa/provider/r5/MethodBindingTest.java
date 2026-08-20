package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.method.BaseResourceReturningMethodBinding;
import ca.uhn.fhir.rest.server.method.IMethodBinding;
import ca.uhn.fhir.rest.server.method.MethodMatchEnum;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodBindingTest {
	private static final Logger ourLog = LoggerFactory.getLogger(MethodBindingTest.class);

	static final FhirContext ourFhirContext = FhirContext.forR5();

	@RegisterExtension
	static RestfulServerExtension ourServer = new RestfulServerExtension(ourFhirContext).withServer(
		server-> server.getServerBindings().add(new IMethodBinding() {
			@Override
			public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
				if (StringUtils.isEmpty(theRequest.getResourceName()) &&
					StringUtils.isNotEmpty(theRequest.getOperation())) {
					return MethodMatchEnum.APPROXIMATE;
				} else {
					return MethodMatchEnum.NONE;
				}
			}

			@Override
			public RestOperationTypeEnum getRestOperationType() {
				return RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
			}

			@Override
			public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException {
				Parameters parameters = new Parameters();
				parameters.addParameter("value", "Hello! " + theRequest.getOperation());
				return BaseResourceReturningMethodBinding.callHooksAndWriteResponse(theServer, theRequest, parameters, false);
			}

			@Override
			public String getBindingKey() {
				return "global - any operation";
			}

			@Override
			public Object getProvider() {
				return this;
			}

			@Override
			public String getResourceName() {
				return "";
			}

			@Override
			public boolean isSupportsConditional() {
				return false;
			}

			@Override
			public boolean isSupportsConditionalMultiple() {
				return false;
			}
		})
	);

	@Test
	void testMethodBinding() {
	    // given
		IGenericClient client = ourServer.getFhirClient();

		Parameters response = client.operation()
			.onServer()
			.named("$hello")
			.withNoParameters(Parameters.class)
			.execute();

		ourLog.info("response:\n{}", ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

		String message = response.getParameter().stream()
			.filter(p -> "value".equals(p.getName()))
			.map(p -> ((StringType) p.getValue()).getValue())
			.findFirst()
			.orElse(null);

		assertEquals("Hello! $hello", message);

	}

}
