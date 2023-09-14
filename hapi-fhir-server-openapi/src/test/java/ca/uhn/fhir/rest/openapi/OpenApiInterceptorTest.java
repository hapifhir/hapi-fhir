package ca.uhn.fhir.rest.openapi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.HtmlUtil;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.ExtensionConstants;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r5.model.ActorDefinition;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.*;

public class OpenApiInterceptorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(OpenApiInterceptorTest.class);

	@Nested
	class R4 extends BaseOpenApiInterceptorTest {

		@Override
		FhirContext getContext() {
			return FhirContext.forR4Cached();
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
		public void testSwaggerUiWithResourceCounts_OneResourceOnly() throws IOException {
			myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor("OperationDefinition"));
			myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

			String url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/";
			String resp = fetchSwaggerUi(url);
			List<String> buttonTexts = parsePageButtonTexts(resp, url);
			assertThat(buttonTexts.toString(), buttonTexts, Matchers.contains("All", "System Level Operations", "OperationDefinition 1", "Observation", "Patient"));
		}

	}

	@Nested
	class R5 extends BaseOpenApiInterceptorTest {

		/**
		 * A provider that uses a resource type not present in R4
		 */
		private MyTypeLevelActorDefinitionProviderR5 myActorDefinitionProvider = new MyTypeLevelActorDefinitionProviderR5();

		@BeforeEach
		void beforeEach() {
			myServer.registerProvider(myActorDefinitionProvider);
			ServerCapabilityStatementProvider a = (ServerCapabilityStatementProvider) myServer.getRestfulServer().getServerConformanceProvider();
			myServer.getRestfulServer().getServerConformanceProvider();
		}

		@AfterEach
		void afterEach() {
			myServer.unregisterProvider(myActorDefinitionProvider);
		}

		@Override
		FhirContext getContext() {
			return FhirContext.forR5Cached();
		}
	}


	@SuppressWarnings("JUnitMalformedDeclaration")
	abstract static class BaseOpenApiInterceptorTest {

		@RegisterExtension
		@Order(0)
		protected RestfulServerExtension myServer = new RestfulServerExtension(getContext())
			.withServletPath("/fhir/*")
			.withServer(t -> t.registerProvider(new HashMapResourceProvider<>(getContext(), getContext().getResourceDefinition("Patient").getImplementingClass())))
			.withServer(t -> t.registerProvider(new HashMapResourceProvider<>(getContext(), getContext().getResourceDefinition("Observation").getImplementingClass())))
			.withServer(t -> t.registerProvider(new MySystemLevelOperationProvider()))
			.withServer(t -> t.registerInterceptor(new ResponseHighlighterInterceptor()));
		@RegisterExtension
		private HttpClientExtension myClient = new HttpClientExtension();

		abstract FhirContext getContext();

		@AfterEach
		public void after() {
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
				ourLog.debug("Response: {}", resp);
			}

			OpenAPI parsed = Yaml.mapper().readValue(resp, OpenAPI.class);

			PathItem fooOpPath = parsed.getPaths().get("/$foo-op");
			assertNull(fooOpPath.getGet());
			assertNotNull(fooOpPath.getPost());
			assertEquals("Foo Op Description", fooOpPath.getPost().getDescription());
			assertEquals("Foo Op Short", fooOpPath.getPost().getSummary());

			PathItem lastNPath = parsed.getPaths().get("/Observation/$lastn");
			assertNotNull(lastNPath.getPost());
			assertEquals("LastN Description", lastNPath.getPost().getDescription());
			assertEquals("LastN Short", lastNPath.getPost().getSummary());
			assertNull(lastNPath.getPost().getParameters());
			assertNotNull(lastNPath.getPost().getRequestBody());
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
		public void testSwaggerUiWithCopyright() throws IOException {
			myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor());
			myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

			String url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/";
			String resp = fetchSwaggerUi(url);
			assertThat(resp, resp, containsString("<p>This server is copyright <strong>Example Org</strong> 2021</p>"));
			assertThat(resp, resp, not(containsString("swagger-ui-custom.css")));
		}

		@Test
		public void testSwaggerUiWithNoBannerUrl() throws IOException {
			myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor());
			myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor().setBannerImage(""));

			String url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/";
			String resp = fetchSwaggerUi(url);
			assertThat(resp, resp, not(containsString("img id=\"banner_img\"")));
		}

		@Test
		public void testSwaggerUiWithCustomStylesheet() throws IOException {
			myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor());

			OpenApiInterceptor interceptor = new OpenApiInterceptor();
			interceptor.setCssText("BODY {\nfont-size: 1.1em;\n}");
			myServer.getRestfulServer().registerInterceptor(interceptor);

			// Fetch Swagger UI HTML
			String url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/";
			String resp = fetchSwaggerUi(url);
			assertThat(resp, resp, containsString("<link rel=\"stylesheet\" type=\"text/css\" href=\"./swagger-ui-custom.css\"/>"));

			// Fetch Custom CSS
			url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/swagger-ui-custom.css";
			resp = fetchSwaggerUi(url);
			String expected = """
				BODY {
				font-size: 1.1em;
				}
				""";
			assertEquals(removeCtrlR(expected), removeCtrlR(resp));
		}

		protected String removeCtrlR(String source) {
			String result = source;
			if (source != null) {
				result = StringUtils.remove(source, '\r');
			}
			return result;
		}

		@Test
		public void testSwaggerUiNotPaged() throws IOException {
			myServer.getRestfulServer().registerInterceptor(new AddResourceCountsInterceptor());

			OpenApiInterceptor interceptor = new OpenApiInterceptor();
			interceptor.setUseResourcePages(false);
			myServer.getRestfulServer().registerInterceptor(interceptor);

			// Fetch Swagger UI HTML
			String url = "http://localhost:" + myServer.getPort() + "/fhir/swagger-ui/";
			String resp = fetchSwaggerUi(url);
			List<String> buttonTexts = parsePageButtonTexts(resp, url);
			assertThat(buttonTexts.toString(), buttonTexts, empty());
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

		protected String fetchSwaggerUi(String url) throws IOException {
			String resp;
			HttpGet get = new HttpGet(url);
			try (CloseableHttpResponse response = myClient.execute(get)) {
				resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				ourLog.info("Response: {}", response.getStatusLine());
				ourLog.debug("Response: {}", resp);
			}
			return resp;
		}

		protected List<String> parsePageButtonTexts(String resp, String url) throws IOException {
			HtmlPage html = HtmlUtil.parseAsHtml(resp, new URL(url));
			HtmlDivision pageButtons = (HtmlDivision) html.getElementById("pageButtons");
			if (pageButtons == null) {
				return Collections.emptyList();
			}

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
				if (theCapabilityStatement instanceof org.hl7.fhir.r4.model.CapabilityStatement) {
					org.hl7.fhir.r4.model.CapabilityStatement cs = (org.hl7.fhir.r4.model.CapabilityStatement) theCapabilityStatement;
					cs.setCopyright("This server is copyright **Example Org** 2021");
					int numResources = cs.getRestFirstRep().getResource().size();
					for (int i = 0; i < numResources; i++) {
						org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent restResource = cs.getRestFirstRep().getResource().get(i);
						if (!myResourceNamesToAddTo.isEmpty() && !myResourceNamesToAddTo.contains(restResource.getType())) {
							continue;
						}
						restResource.addExtension(
							ExtensionConstants.CONF_RESOURCE_COUNT,
							new org.hl7.fhir.r4.model.DecimalType(i) // reverse order
						);
					}
				} else {
					org.hl7.fhir.r5.model.CapabilityStatement cs = (org.hl7.fhir.r5.model.CapabilityStatement) theCapabilityStatement;
					cs.setCopyright("This server is copyright **Example Org** 2021");
					int numResources = cs.getRestFirstRep().getResource().size();
					for (int i = 0; i < numResources; i++) {
						org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceComponent restResource = cs.getRestFirstRep().getResource().get(i);
						if (!myResourceNamesToAddTo.isEmpty() && !myResourceNamesToAddTo.contains(restResource.getType())) {
							continue;
						}
						restResource.addExtension(
							ExtensionConstants.CONF_RESOURCE_COUNT,
							new org.hl7.fhir.r5.model.DecimalType(i) // reverse order
						);
					}
				}
			}

		}
	}

	public static class MySystemLevelOperationProvider {


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

		@Patch(typeName = "Patient")
		public MethodOutcome patch(HttpServletRequest theRequest, @IdParam IIdType theId, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails, @ResourceParam String theBody, PatchTypeEnum thePatchType, @ResourceParam IBaseParameters theRequestBody) {
			throw new IllegalStateException();
		}

	}


	static class MyTypeLevelActorDefinitionProviderR5 extends HashMapResourceProvider<ActorDefinition> {

		/**
		 * Constructor
		 */
		public MyTypeLevelActorDefinitionProviderR5() {
			super(FhirContext.forR5Cached(), ActorDefinition.class);
		}


		@Validate
		public MethodOutcome validate(
			@ResourceParam IBaseResource theResource,
			@ResourceParam String theRawResource,
			@ResourceParam EncodingEnum theEncoding,
			@Validate.Mode ValidationModeEnum theMode,
			@Validate.Profile String theProfile,
			RequestDetails theRequestDetails) {
			return null;
		}

	}

}
