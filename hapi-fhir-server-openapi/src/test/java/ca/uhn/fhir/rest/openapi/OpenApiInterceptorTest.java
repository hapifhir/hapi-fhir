package ca.uhn.fhir.rest.openapi;

import ca.uhn.fhir.context.FhirContext;
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

public class OpenApiInterceptorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(OpenApiInterceptorTest.class);
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	protected RestfulServerExtension myServer = new RestfulServerExtension(myFhirContext)
		.withServletPath("/fhir/*")
		.withServer(t -> t.registerProvider(new HashMapResourceProvider<>(myFhirContext, Patient.class)))
		.withServer(t -> t.registerProvider(new HashMapResourceProvider<>(myFhirContext, Observation.class)))
		.withServer(t -> t.registerProvider(new MyLastNProvider()))
		.withServer(t -> t.registerInterceptor(new ResponseHighlighterInterceptor()));
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
	public void testFetchSwagger() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

		String resp;
		HttpGet get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir/metadata?_pretty=true");
		try (CloseableHttpResponse response = myClient.execute(get)) {
			resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("CapabilityStatement: {}", resp);
		}

		get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir/api-docs");
		try (CloseableHttpResponse response = myClient.execute(get)) {
			resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", response.getStatusLine());
			ourLog.info("Response: {}", resp);
		}

		OpenAPI parsed = Yaml.mapper().readValue(resp, OpenAPI.class);

		PathItem fooOpPath = parsed.getPaths().get("/$foo-op");
		assertNull(fooOpPath.getGet());
		assertNotNull(fooOpPath.getPost());
		assertEquals("Foo Op Description", fooOpPath.getPost().getDescription());
		assertEquals("Foo Op Short", fooOpPath.getPost().getSummary());

		PathItem lastNPath = parsed.getPaths().get("/Observation/$lastn");
		assertNull(lastNPath.getPost());
		assertNotNull(lastNPath.getGet());
		assertEquals("LastN Description", lastNPath.getGet().getDescription());
		assertEquals("LastN Short", lastNPath.getGet().getSummary());
		assertEquals(4, lastNPath.getGet().getParameters().size());
		assertEquals("Subject description", lastNPath.getGet().getParameters().get(0).getDescription());
	}

	@Test
	public void testRedirectFromBaseUrl() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

		HttpGet get;

		get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir/");
		try (CloseableHttpResponse response = myClient.execute(get)) {
			assertEquals(400, response.getStatusLine().getStatusCode());
		}

		get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir/");
		get.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", response);
			ourLog.info("Response: {}", responseString);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(responseString, containsString("<title>Swagger UI</title>"));
		}

		get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir/?foo=foo");
		get.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			assertEquals(400, response.getStatusLine().getStatusCode());
		}

		get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir?foo=foo");
		get.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			assertEquals(400, response.getStatusLine().getStatusCode());
		}

	}


	@Test
	public void testSwaggerUiWithResourceCounts() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor());
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

		String url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/";
		String resp = fetchSwaggerUi(url);
		List<String> buttonTexts = parsePageButtonTexts(resp, url);
		assertThat(buttonTexts.toString(), buttonTexts, Matchers.contains("All", "System Level Operations", "Patient 2", "OperationDefinition 1", "Observation 0"));
	}

	@Test
	public void testSwaggerUiWithCopyright() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor());
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

		String url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/";
		String resp = fetchSwaggerUi(url);
		assertThat(resp, resp, containsString("<p>This server is copyright <strong>Example Org</strong> 2021</p>"));
	}

	@Test
	public void testSwaggerUiWithResourceCounts_OneResourceOnly() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor("OperationDefinition"));
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

		String url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/";
		String resp = fetchSwaggerUi(url);
		List<String> buttonTexts = parsePageButtonTexts(resp, url);
		assertThat(buttonTexts.toString(), buttonTexts, Matchers.contains("All", "System Level Operations", "OperationDefinition 1", "Observation", "Patient"));
	}

	@Test
	public void testRemoveTrailingSlash() {
		OpenApiInterceptor interceptor = new OpenApiInterceptor();
		String url1 = interceptor.removeTrailingSlash("http://localhost:8000");
		String url2 = interceptor.removeTrailingSlash("http://localhost:8000/");
		String url3 = interceptor.removeTrailingSlash("http://localhost:8000//");
		String expect = "http://localhost:8000";
		assertEquals(expect, url1);
		assertEquals(expect, url2);
		assertEquals(expect, url3);
	}

	@Test
	public void testRemoveTrailingSlashWithNullUrl() {
		OpenApiInterceptor interceptor = new OpenApiInterceptor();
		String url = interceptor.removeTrailingSlash(null);
		assertEquals(null, url);
	}

	@Test
	public void testStandardRedirectScriptIsAccessible() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor());
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

		HttpGet get = new HttpGet("http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/oauth2-redirect.html");
		try (CloseableHttpResponse response = myClient.execute(get)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
		}
	}

	private String fetchSwaggerUi(String url) throws IOException {
		String resp;
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", response.getStatusLine());
			ourLog.info("Response: {}", resp);
		}
		return resp;
	}

	private List<String> parsePageButtonTexts(String resp, String url) throws IOException {
		HtmlPage html = HtmlUtil.parseAsHtml(resp, new URL(url));
		HtmlDivision pageButtons = (HtmlDivision) html.getElementById("pageButtons");
		List<String> buttonTexts = new ArrayList<>();
		for (DomElement next : pageButtons.getChildElements()) {
			buttonTexts.add(next.asNormalizedText());
		}
		return buttonTexts;
	}


	public static class AddResourceCountsInterceptor {

		private final HashSet<String> myResourceNamesToAddTo;

		public AddResourceCountsInterceptor(String... theResourceNamesToAddTo) {
			myResourceNamesToAddTo = new HashSet<>(Arrays.asList(theResourceNamesToAddTo));
		}

		@Hook(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED)
		public void capabilityStatementGenerated(IBaseConformance theCapabilityStatement) {
			CapabilityStatement cs = (CapabilityStatement) theCapabilityStatement;
			cs.setCopyright("This server is copyright **Example Org** 2021");

			int numResources = cs.getRestFirstRep().getResource().size();
			for (int i = 0; i < numResources; i++) {

				CapabilityStatement.CapabilityStatementRestResourceComponent restResource = cs.getRestFirstRep().getResource().get(i);
				if (!myResourceNamesToAddTo.isEmpty() && !myResourceNamesToAddTo.contains(restResource.getType())) {
					continue;
				}

				restResource.addExtension(
					ExtensionConstants.CONF_RESOURCE_COUNT,
					new DecimalType(i) // reverse order
				);

			}
		}

	}

	public static class MyLastNProvider {


		@Description(value = "LastN Description", shortDefinition = "LastN Short")
		@Operation(name = Constants.OPERATION_LASTN, typeName = "Observation", idempotent = true)
		public IBaseBundle lastN(
			@Description(value = "Subject description", shortDefinition = "Subject short", example = {"Patient/456", "Patient/789"})
			@OperationParam(name = "subject", typeName = "reference", min = 0, max = 1) IBaseReference theSubject,
			@OperationParam(name = "category", typeName = "coding", min = 0, max = OperationParam.MAX_UNLIMITED) List<IBaseCoding> theCategories,
			@OperationParam(name = "code", typeName = "coding", min = 0, max = OperationParam.MAX_UNLIMITED) List<IBaseCoding> theCodes,
			@OperationParam(name = "max", typeName = "integer", min = 0, max = 1) IPrimitiveType<Integer> theMax
		) {
			throw new IllegalStateException();
		}

		@Description(value = "Foo Op Description", shortDefinition = "Foo Op Short")
		@Operation(name = "foo-op", idempotent = false)
		public IBaseBundle foo(
			ServletRequestDetails theRequestDetails,
			@Description(shortDefinition = "Reference description", example = "Patient/123")
			@OperationParam(name = "subject", typeName = "reference", min = 0, max = 1) IBaseReference theSubject,
			@OperationParam(name = "category", typeName = "coding", min = 0, max = OperationParam.MAX_UNLIMITED) List<IBaseCoding> theCategories,
			@OperationParam(name = "code", typeName = "coding", min = 0, max = OperationParam.MAX_UNLIMITED) List<IBaseCoding> theCodes,
			@OperationParam(name = "max", typeName = "integer", min = 0, max = 1) IPrimitiveType<Integer> theMax
		) {
			throw new IllegalStateException();
		}

		@Patch(type = Patient.class)
		public MethodOutcome patch(HttpServletRequest theRequest, @IdParam IIdType theId, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails, @ResourceParam String theBody, PatchTypeEnum thePatchType, @ResourceParam IBaseParameters theRequestBody) {
			throw new IllegalStateException();
		}


	}
}
