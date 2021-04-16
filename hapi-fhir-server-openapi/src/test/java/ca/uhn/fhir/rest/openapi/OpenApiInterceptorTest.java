package ca.uhn.fhir.rest.openapi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.provider.BaseLastNProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OpenApiInterceptorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(OpenApiInterceptorTest.class);
	private FhirContext myFhirContext = FhirContext.forCached(FhirVersionEnum.R4);
	@RegisterExtension
	@Order(0)
	protected RestfulServerExtension myServer = new RestfulServerExtension(myFhirContext)
		.withPort(8000) // FIXME: remove
		.withServletPath("/fhir/*")
		.withServer(t -> t.registerProvider(new MyLastNProvider()))
		.withServer(t -> t.registerInterceptor(new ResponseHighlighterInterceptor()));
	@RegisterExtension
	@Order(1)
	protected HashMapResourceProviderExtension<Patient> myPatientProvider = new HashMapResourceProviderExtension<>(myServer, Patient.class);
	@RegisterExtension
	@Order(2)
	protected HashMapResourceProviderExtension<Observation> myObservationProvider = new HashMapResourceProviderExtension<>(myServer, Observation.class);
	private CloseableHttpClient myClient;

	@BeforeEach
	public void before() {
		myClient = HttpClientBuilder.create().build();
	}

	@AfterEach
	public void after() throws IOException {
		myClient.close();
		myServer.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testFetchSwaggerUi() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

		HttpGet get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir/api-docs");
		try (CloseableHttpResponse response = myClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", response.getStatusLine());
			ourLog.info("Response: {}", resp);
		}

	}

	public static class MyLastNProvider extends BaseLastNProvider {

		@Operation(name = "foo-op", idempotent = false)
		public IBaseBundle foo(
			ServletRequestDetails theRequestDetails,
			@OperationParam(name = "subject", typeName = "reference", min = 0, max = 1) IBaseReference theSubject,
			@OperationParam(name = "category", typeName = "coding", min = 0, max = OperationParam.MAX_UNLIMITED) List<IBaseCoding> theCategories,
			@OperationParam(name = "code", typeName = "coding", min = 0, max = OperationParam.MAX_UNLIMITED) List<IBaseCoding> theCodes,
			@OperationParam(name = "max", typeName = "integer", min = 0, max = 1) IPrimitiveType<Integer> theMax
		) {
			throw new IllegalStateException();
		}

		@Override
		protected IBaseBundle processLastN(IBaseReference theSubject, List<IBaseCoding> theCategories, List<IBaseCoding> theCodes, IPrimitiveType<Integer> theMax) {
			throw new InternalErrorException("FOO");
		}
	}
}
