package ca.uhn.fhir.jpa.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.HfqlDataTypeEnum;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.StaticHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.provider.HfqlRestProvider;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.MockMvcWebConnectionForHtmlUnit3;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.cssparser.parser.CSSErrorHandler;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.htmlunit.html.HtmlTableCell;
import org.htmlunit.html.HtmlTableRow;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebTest {
	private static final Logger ourLog = LoggerFactory.getLogger(WebTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final HfqlRestProvider ourHfqlProvider = new HfqlRestProvider();

	@RegisterExtension
	@Order(0)
	public static final RestfulServerExtension ourFhirServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new MyPatientFakeDocumentController())
		.registerProvider(ourHfqlProvider);
	@RegisterExtension
	@Order(1)
	public static final HashMapResourceProviderExtension<Patient> ourPatientProvider = new HashMapResourceProviderExtension<>(ourFhirServer, Patient.class);
	protected static MockMvc ourMockMvc;
	private static Server ourOverlayServer;
	private WebClient myWebClient;
	@Mock
	private IHfqlExecutor myHfqlExecutor;

	@BeforeEach
	public void before() throws Exception {
		ourHfqlProvider.setHfqlExecutor(myHfqlExecutor);

		if (ourOverlayServer == null) {
			AnnotationConfigWebApplicationContext appCtx = new AnnotationConfigWebApplicationContext();
			appCtx.register(WebTestFhirTesterConfig.class);

			DispatcherServlet dispatcherServlet = new DispatcherServlet(appCtx);

			ServletHolder holder = new ServletHolder(dispatcherServlet);
			holder.setName("servlet");

			ServletHandler servletHandler = new ServletHandler();
			servletHandler.addServletWithMapping(holder, "/*");

			ServletContextHandler contextHandler = new MyServletContextHandler();
			contextHandler.setAllowNullPathInContext(true);
			contextHandler.setServletHandler(servletHandler);
			Resource base = new PathResourceFactory().newResource("hapi-fhir-testpage-overlay/src/main/webapp");
			if (!base.exists()) {
				base = new PathResourceFactory().newResource("src/main/webapp");
			}
			contextHandler.setBaseResource(base);

			ourOverlayServer = new Server(0);
			ourOverlayServer.setHandler(contextHandler);
			ourOverlayServer.start();

			ourMockMvc = MockMvcBuilders.webAppContextSetup(appCtx).build();
		}

		myWebClient = new WebClient();
		myWebClient.setWebConnection(new MockMvcWebConnectionForHtmlUnit3(ourMockMvc, myWebClient));
		myWebClient.getOptions().setJavaScriptEnabled(true);
		myWebClient.getOptions().setCssEnabled(false);
		CSSErrorHandler errorHandler = new SilentCssErrorHandler();
		myWebClient.setCssErrorHandler(errorHandler);

		ourLog.info("Started FHIR endpoint at " + ourFhirServer.getBaseUrl());
		WebTestFhirTesterConfig.setBaseUrl(ourFhirServer.getBaseUrl());

		String baseUrl = "http://localhost:" + JettyUtil.getPortForStartedServer(ourOverlayServer) + "/";
		ourLog.info("Started test overlay at " + baseUrl);
	}

	@Test
	public void testSearchForPatients() throws IOException {
		register5Patients();

		// Load home page
		HtmlPage page = myWebClient.getPage("http://localhost/");
		// Navigate to Patient resource page
		HtmlAnchor patientLink = page.getHtmlElementById("leftResourcePatient");
		HtmlPage patientPage = patientLink.click();
		// Click search button
		HtmlButton searchButton = patientPage.getHtmlElementById("search-btn");
		HtmlPage searchResultPage = searchButton.click();
		HtmlTable controlsTable = searchResultPage.getHtmlElementById("resultControlsTable");
		List<HtmlTableRow> controlRows = controlsTable.getBodies().get(0).getRows();
		assertThat(controlRows).hasSize(5);
		assertEquals("Read Update $summary $validate", controlRows.get(0).getCell(0).asNormalizedText());
		assertEquals("Patient/A0/_history/1", controlRows.get(0).getCell(1).asNormalizedText());
		assertEquals("Patient/A4/_history/1", controlRows.get(4).getCell(1).asNormalizedText());
	}

	@Test
	public void testHistoryWithDeleted() throws IOException {
		register5Patients();
		for (int i = 0; i < 5; i++) {
			ourFhirServer.getFhirClient().delete().resourceById(new IdType("Patient/A" + i));
		}


		// Load home page
		HtmlPage page = myWebClient.getPage("http://localhost/");
		// Navigate to Patient resource page
		HtmlAnchor patientLink = page.getHtmlElementById("leftResourcePatient");
		HtmlPage patientPage = patientLink.click();
		// Click search button
		HtmlButton historyButton = patientPage.getElementByName("action-history-type");
		HtmlPage searchResultPage = historyButton.click();
		HtmlTable controlsTable = searchResultPage.getHtmlElementById("resultControlsTable");
		List<HtmlTableRow> controlRows = controlsTable.getBodies().get(0).getRows();
		assertThat(controlRows).hasSize(5);
		ourLog.info(controlRows.get(0).asXml());
		assertEquals("Patient/A4/_history/1", controlRows.get(0).getCell(1).asNormalizedText());
		assertEquals("Patient/A0/_history/1", controlRows.get(4).getCell(1).asNormalizedText());
	}

	@Test
	public void testInvokeCustomOperation() throws IOException {
		register5Patients();

		HtmlPage searchResultPage = searchForPatients();
		HtmlTable controlsTable = searchResultPage.getHtmlElementById("resultControlsTable");
		List<HtmlTableRow> controlRows = controlsTable.getBodies().get(0).getRows();
		HtmlTableCell controlsCell = controlRows.get(0).getCell(0);

		// Find the $summary button and click it
		HtmlPage summaryPage = controlsCell
			.getElementsByTagName("button")
			.stream()
			.filter(t -> t.asNormalizedText().equals("$summary"))
			.findFirst()
			.orElseThrow()
			.click();

		assertThat(summaryPage.asNormalizedText()).contains("Result Narrative\t\nHELLO WORLD DOCUMENT");
	}

	private static Stream<Arguments> getButtonMappingPredicates() {
		Predicate<HtmlElement> readButtonPredicate = t -> t.getAttribute("value").equals("read");
		Predicate<HtmlElement> summaryButtonPredicate = t -> t.asNormalizedText().equals("$summary");
		return Stream.of(arguments(readButtonPredicate), arguments(summaryButtonPredicate));
	}

	@ParameterizedTest
	@MethodSource(value = "getButtonMappingPredicates")
	public void testInvokeOperation_withReflectedXssAttack_resultHtmlIsSanitized(
		Predicate<HtmlElement> theButtonMappingPredicate) throws IOException {

		register5Patients();

		HtmlPage searchResultPage = searchForPatients();
		HtmlTable controlsTable = searchResultPage.getHtmlElementById("resultControlsTable");
		List<HtmlTableRow> controlRows = controlsTable.getBodies().get(0).getRows();
		HtmlTableCell controlsCell = controlRows.get(0).getCell(0);

		// find the button
		HtmlElement summaryButton = controlsCell
			.getElementsByTagName("button")
			.stream()
			.filter(theButtonMappingPredicate)
			.findFirst()
			.orElseThrow();

		// alter button attributes to imitate Reflected XSS attack
		summaryButton.setAttribute("data2", "A0%3Cscript%3Ealert(2)%3C/script%3E");
		summaryButton.setAttribute("data3", "%24diff%3Cscript%3Ealert(1)%3C/script%3E");
		HtmlPage summaryPage = summaryButton.click();

		// validate that there is no <script> span in result summary page
		Optional<HtmlElement> scriptSpans = summaryPage.getHtmlElementById("requestUrlAnchor")
			.getElementsByTagName("span")
			.stream()
			.filter(span -> span.asXml().contains("<script>"))
			.findAny();

		assertTrue(scriptSpans.isEmpty());
	}

	@Test
	public void testInvokeCustomOperation_Validate() throws IOException {
		register5Patients();

		HtmlPage searchResultPage = searchForPatients();
		HtmlTable controlsTable = searchResultPage.getHtmlElementById("resultControlsTable");
		List<HtmlTableRow> controlRows = controlsTable.getBodies().get(0).getRows();
		HtmlTableCell controlsCell = controlRows.get(0).getCell(0);

		// Find the $summary button and click it
		HtmlPage summaryPage = controlsCell
			.getElementsByTagName("button")
			.stream()
			.filter(t -> t.asNormalizedText().equals("$validate"))
			.findFirst()
			.orElseThrow()
			.click();

		assertThat(summaryPage.asNormalizedText()).contains("\"diagnostics\": \"VALIDATION FAILURE\"");
	}

	@Test
	public void testInvokeCustomOperation_Diff() throws IOException {
		registerAndUpdatePatient();

		HtmlPage searchResultPage = searchForPatients();
		HtmlTable controlsTable = searchResultPage.getHtmlElementById("resultControlsTable");
		List<HtmlTableRow> controlRows = controlsTable.getBodies().get(0).getRows();
		HtmlTableCell controlsCell = controlRows.get(0).getCell(0);

		HtmlPage diffPage = controlsCell
			.getElementsByTagName("button")
			.stream()
			.filter(t -> t.asNormalizedText().equals("$diff"))
			.findFirst()
			.orElseThrow()
			.click();

		assertThat(diffPage.asNormalizedText()).contains("\"resourceType\": \"Parameters\"");
	}


	@Test
	public void testHfqlExecuteQuery() throws IOException {
		// Load home page
		HtmlPage page = myWebClient.getPage("http://localhost/");
		// Navigate to HFQL page
		HtmlAnchor hfqlNavButton = page.getHtmlElementById("leftHfql");
		HtmlPage hfqlPage = hfqlNavButton.click();
		assertEquals("HFQL/SQL - HAPI FHIR", hfqlPage.getTitleText());

		// Prepare response
		List<String> columnNames = List.of("Family", "Given");
		List<HfqlDataTypeEnum> columnTypes = List.of(HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING);
		List<List<Object>> rows = List.of(
			List.of("Simpson", "Homer"),
			List.of("Simpson", "Bart")
		);
		StaticHfqlExecutionResult result = new StaticHfqlExecutionResult(null, columnNames, columnTypes, rows);
		when(myHfqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(result);

		// Click execute button
		HtmlButton executeBtn = (HtmlButton) hfqlPage.getElementById("execute-btn");
		HtmlPage resultsPage = executeBtn.click();

		HtmlTable table = (HtmlTable) resultsPage.getElementById("resultsTable");
		ourLog.info(table.asXml());
		assertThat(table.asNormalizedText()).contains("Simpson");
	}


	private void registerAndUpdatePatient() {
		Patient p = new Patient();
		Patient p2 = new Patient();
		HumanName humanName = new HumanName();
		humanName.addGiven("Yui");
		humanName.setFamily("Hirasawa");
		p2.getName().add(humanName);
		p.setId("Patient/A");
		p.getMeta().setLastUpdatedElement(new InstantType("2022-01-01T12:12:12.000Z"));
		p.setActive(true);
		IIdType iid = ourPatientProvider.store(p);
		ourFhirServer.getFhirClient().update().resource(p2).withId(iid).execute();
	}

	private HtmlPage searchForPatients() throws IOException {
		// Load home page
		HtmlPage page = myWebClient.getPage("http://localhost/");
		// Navigate to Patient resource page
		HtmlPage patientPage = page.<HtmlAnchor>getHtmlElementById("leftResourcePatient").click();
		// Click search button
		HtmlPage searchResultPage = patientPage.<HtmlButton>getHtmlElementById("search-btn").click();
		return searchResultPage;
	}


	private void register5Patients() {
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("Patient/A" + i);
			p.getMeta().setLastUpdatedElement(new InstantType("2022-01-01T12:12:12.000Z"));
			p.setActive(true);
			ourPatientProvider.store(p);
		}
	}

	private static class MyPatientFakeDocumentController {

		@Operation(name = "summary", typeName = "Patient", idempotent = true)
		public Bundle summary(@IdParam IIdType theId) {
			Composition composition = new Composition();
			composition.getText().setDivAsString("<div>HELLO WORLD DOCUMENT</div>");

			Bundle retVal = new Bundle();
			retVal.setType(Bundle.BundleType.DOCUMENT);
			retVal.addEntry().setResource(composition);

			return retVal;
		}

		@Operation(name = "validate", typeName = "Patient", idempotent = true)
		public OperationOutcome validate(@IdParam IIdType theId) {
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue()
				.setDiagnostics("VALIDATION FAILURE");
			throw new PreconditionFailedException("failure", oo);
		}

		@Operation(name = "diff", typeName = "Patient", idempotent = true)
		public Parameters diff(@IdParam IIdType theId) {
			Parameters parameters = new Parameters();
			return parameters;
		}

	}

	private static class MyServletContextHandler extends ServletContextHandler {

		public MyServletContextHandler() {
			super();
		}

		@Override
		public ServletContextApi newServletContextApi() {
			return new ServletContextApi(){
				@Override
				public InputStream getResourceAsStream(String thePath) {
					try {
						URL url = getResource(thePath);
						return url.openStream();
					} catch (IOException e) {
						throw new InternalErrorException(e);
					}
				}


				@Override
				public URL getResource(String thePath) throws MalformedURLException {
					File parent = new File("hapi-fhir-testpage-overlay/src/main/webapp").getAbsoluteFile();
					if (!parent.exists()) {
						parent = new File("src/main/webapp").getAbsoluteFile();
					}
					File file = new File(parent, thePath);
					URL url = null;
					try {
						return file.toURI().toURL();
					} catch (MalformedURLException e) {
						throw new InternalErrorException(e);
					}
				}
			};
		}


	}

	@AfterAll
	public static void afterAll() throws Exception {
		JettyUtil.closeServer(ourOverlayServer);
	}

}
