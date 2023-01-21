package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.BundleBuilder;
import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebTest {
	private static final Logger ourLog = LoggerFactory.getLogger(WebTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	protected MockMvc myMockMvc;
	private WebClient myWebClient;

	@RegisterExtension
	@Order(0)
	private final RestfulServerExtension myFhirServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new MyPatientFakeDocumentController());
	@RegisterExtension
	@Order(1)
	private final HashMapResourceProviderExtension<Patient> myPatientProvider = new HashMapResourceProviderExtension<>(myFhirServer, Patient.class);

	@BeforeEach
	public void before() throws Exception {
		AnnotationConfigWebApplicationContext appCtx = new AnnotationConfigWebApplicationContext();
		appCtx.register(WebTestFhirTesterConfig.class);

		DispatcherServlet dispatcherServlet = new DispatcherServlet(appCtx);

		ServletHolder holder = new ServletHolder(dispatcherServlet);
		holder.setName("servlet");

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(holder, "/*");

		ServletContextHandler contextHandler = new MyServletContextHandler();
		contextHandler.setAllowNullPathInfo(true);
		contextHandler.setServletHandler(servletHandler);
		contextHandler.setResourceBase("hapi-fhir-testpage-overlay/src/main/webapp");

		Server overlayServer = new Server(0);
		overlayServer.setHandler(contextHandler);
		overlayServer.start();

		myMockMvc = MockMvcBuilders.webAppContextSetup(appCtx).build();

		myWebClient = new WebClient();
		myWebClient.setWebConnection(new MockMvcWebConnection(myMockMvc, myWebClient));
		myWebClient.getOptions().setJavaScriptEnabled(true);
		myWebClient.getOptions().setCssEnabled(false);
		CSSErrorHandler errorHandler = new SilentCssErrorHandler();
		myWebClient.setCssErrorHandler(errorHandler);

		ourLog.info("Started FHIR endpoint at " + myFhirServer.getBaseUrl());
		WebTestFhirTesterConfig.setBaseUrl(myFhirServer.getBaseUrl());

		String baseUrl = "http://localhost:" + JettyUtil.getPortForStartedServer(overlayServer) + "/";
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

	}

	private void register5Patients() {
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("Patient/A" + i);
			p.getMeta().setLastUpdatedElement(new InstantType("2022-01-01T12:12:12.000Z"));
			p.setActive(true);
			myPatientProvider.store(p);
		}
	}

	private static class MyPatientFakeDocumentController {

		@Operation(name="summary", typeName = "Patient", idempotent = true)
		public Bundle summary(@IdParam IIdType theId) {
			Composition composition = new Composition();
			composition.getText().setDivAsString("<div>HELLO WORLD DOCUMENT</div>");

			Bundle retVal = new Bundle();
			retVal.setType(Bundle.BundleType.DOCUMENT);
			retVal.addEntry().setResource(composition);

			return retVal;
		}

	}

	private static class MyServletContextHandler extends ServletContextHandler {

		public MyServletContextHandler() {
			super();
			_scontext = new ContextHandler.Context() {
				@Override
				public URL getResource(String thePath) {
					File parent = new File("hapi-fhir-testpage-overlay/src/main/webapp").getAbsoluteFile();
					if (!parent.exists()) {
						parent = new File("src/main/webapp").getAbsoluteFile();
					}
					File file = new File(parent, thePath);
					try {
						return file.toURI().toURL();
					} catch (MalformedURLException e) {
						throw new InternalErrorException(e);
					}
				}
			};
		}
	}


}
