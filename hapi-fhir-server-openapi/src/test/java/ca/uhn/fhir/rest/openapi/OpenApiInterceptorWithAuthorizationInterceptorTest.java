package ca.uhn.fhir.rest.openapi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpenApiInterceptorWithAuthorizationInterceptorTest {
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	protected RestfulServerExtension myServer = new RestfulServerExtension(myFhirContext)
		.withServletPath("/fhir/*")
		.withServer(t -> t.registerProvider(new HashMapResourceProvider<>(myFhirContext, Patient.class)))
		.withServer(t -> t.registerProvider(new HashMapResourceProvider<>(myFhirContext, Observation.class)))
		.withServer(t -> t.registerProvider(new OpenApiInterceptorTest.MySystemLevelOperationProvider()))
		.withServer(t -> t.registerInterceptor(new ResponseHighlighterInterceptor()));
	private AuthorizationInterceptor myAuthorizationInterceptor;
	private List<IAuthRule> myRules;

	@BeforeEach
	public void before() {
		myAuthorizationInterceptor = new AuthorizationInterceptor() {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return myRules;
			}
		};
	}

	@AfterEach
	public void after() {
		myServer.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testFetchSwagger_AllowAll() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());
		myServer.getRestfulServer().registerInterceptor(myAuthorizationInterceptor);

		myRules = new RuleBuilder()
			.allowAll()
			.build();

		HttpTestResponse response = myServer.fhirRequest("/api-docs").get().assertStatus(200);
		String resp = response.getBody();

		OpenAPI parsed = Yaml.mapper().readValue(resp, OpenAPI.class);
		assertNotNull(parsed.getPaths().get("/Patient").getPost());
	}
}
