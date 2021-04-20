package ca.uhn.fhir.rest.openapi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.HtmlUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.ExtensionConstants;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OpenApiInterceptorWithAuthorizationInterceptorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(OpenApiInterceptorWithAuthorizationInterceptorTest.class);
	private FhirContext myFhirContext = FhirContext.forCached(FhirVersionEnum.R4);
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
