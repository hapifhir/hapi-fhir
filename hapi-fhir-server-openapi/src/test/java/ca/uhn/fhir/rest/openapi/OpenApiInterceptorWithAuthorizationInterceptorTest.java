package ca.uhn.fhir.rest.openapi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpenApiInterceptorWithAuthorizationInterceptorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(OpenApiInterceptorWithAuthorizationInterceptorTest.class);
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	protected RestfulServerExtension myServer = new RestfulServerExtension(myFhirContext)
		.withServletPath("/fhir/*")
		.withServer(t -> t.registerProvider(new HashMapResourceProvider<>(myFhirContext, Patient.class)))
		.withServer(t -> t.registerProvider(new HashMapResourceProvider<>(myFhirContext, Observation.class)))
		.withServer(t -> t.registerProvider(new OpenApiInterceptorTest.MyLastNProvider()))
		.withServer(t -> t.registerInterceptor(new ResponseHighlighterInterceptor()));
	private CloseableHttpClient myClient;
	private AuthorizationInterceptor myAuthorizationInterceptor;
	private List<IAuthRule> myRules;

	@BeforeEach
	public void before() {
		myClient = HttpClientBuilder.create().build();
		myAuthorizationInterceptor = new AuthorizationInterceptor() {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return myRules;
			}
		};
	}

	@AfterEach
	public void after() throws IOException {
		myClient.close();
		myServer.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testFetchSwagger_AllowAll() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());
		myServer.getRestfulServer().registerInterceptor(myAuthorizationInterceptor);

		myRules = new RuleBuilder()
			.allowAll()
			.build();

		String resp;
		HttpGet get;

		get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir/api-docs");
		try (CloseableHttpResponse response = myClient.execute(get)) {
			resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", response.getStatusLine());
			ourLog.info("Response: {}", resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
		}

		OpenAPI parsed = Yaml.mapper().readValue(resp, OpenAPI.class);
		assertNotNull(parsed.getPaths().get("/Patient").getPost());
	}
}
