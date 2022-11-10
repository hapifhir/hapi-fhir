package ca.uhn.fhir.cr;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.helper.ResourceLoader;
import ca.uhn.fhir.cr.config.CrR4Config;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.net.ServerSocket;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = { TestCrConfig.class, CrR4Config.class })
public class CrR4Test extends BaseJpaR4Test implements ResourceLoader {
	protected static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	private static IParser parser = ourFhirContext.newJsonParser().setPrettyPrint(true);
	private static int port = 0;

	@Autowired
	protected DaoRegistry daoRegistry;

	@Override
	public DaoRegistry getDaoRegistry() {
		return daoRegistry;
	}

	@Override
	public FhirContext getFhirContext() {
		return ourFhirContext;
	}

	public Bundle loadBundle(String theLocation) {
		return loadBundle(Bundle.class, theLocation);
	}

	// emulate wiremock's junit.WireMockRule with testng features
	WireMockServer wireMockServer;
	WireMock wireMock;

	@BeforeAll()
	public void start() {
		wireMockServer = new WireMockServer(getPort());
		wireMockServer.start();
		WireMock.configureFor("localhost", getPort());
		wireMock = new WireMock("localhost", getPort());

		mockFhirRead( "/metadata", getCapabilityStatement() );
	}

	@AfterAll
	public void stop() {
		wireMockServer.stop();
	}

	public IParser getFhirParser() {
		return parser;
	}

	public int getPort() {
		if (port == 0 ) {
			try (ServerSocket socket = new ServerSocket(0)) {
				port = socket.getLocalPort();
			} catch( Exception ex ) {
				throw new RuntimeException("Failed to determine a port for the wiremock server", ex);
			}
		}
		return port;
	}

	public IGenericClient newClient() {
		IGenericClient client = getFhirContext().newRestfulGenericClient(String.format("http://localhost:%d/", getPort()));

		LoggingInterceptor logger = new LoggingInterceptor();
		logger.setLogRequestSummary(true);
		logger.setLogResponseBody(true);
		client.registerInterceptor(logger);

		return client;
	}

	public void mockNotFound(String resource) {
		OperationOutcome outcome = new OperationOutcome();
		outcome.getText().setStatusAsString("generated");
		outcome.getIssueFirstRep().setSeverity(OperationOutcome.IssueSeverity.ERROR).setCode(OperationOutcome.IssueType.PROCESSING).setDiagnostics(resource);

		mockFhirRead(resource, outcome, 404);
	}

	public void mockFhirRead(Resource resource) {
		String resourcePath = "/" + resource.fhirType() + "/" + resource.getId();
		mockFhirInteraction(resourcePath, resource);
	}

	public void mockFhirRead(String path, Resource resource) {
		mockFhirRead(path, resource, 200);
	}

	public void mockFhirRead(String path, Resource resource, int statusCode) {
		MappingBuilder builder = get(urlEqualTo(path));
		mockFhirInteraction(builder, resource, statusCode);
	}

	public void mockFhirSearch(String path, Resource... resources) {
		MappingBuilder builder = get(urlEqualTo(path));
		mockFhirInteraction(builder,  makeBundle( resources));
	}

	public void mockFhirPost(String path, Resource resource) {
		mockFhirInteraction(post(urlEqualTo(path)), resource, 200);
	}

	public void mockFhirInteraction( String path, Resource resource ) {
		mockFhirRead(path, resource, 200);
	}

	public void mockFhirInteraction( MappingBuilder builder, Resource resource ) {
		mockFhirInteraction(builder, resource, 200);
	}

	public void mockFhirInteraction(MappingBuilder builder, Resource resource, int statusCode) {
		String body = null;
		if (resource != null) {
			body = getFhirParser().encodeResourceToString(resource);
		}

		stubFor(builder.willReturn(aResponse().withStatus(statusCode).withHeader("Content-Type", "application/json").withBody(body)));
	}

	public CapabilityStatement getCapabilityStatement() {
		CapabilityStatement metadata = new CapabilityStatement();
		metadata.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
		return metadata;
	}

	public Bundle makeBundle(List<? extends Resource> resources) {
		return makeBundle(resources.toArray(new Resource[resources.size()]));
	}

	public Bundle makeBundle(Resource... resources) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.SEARCHSET);
		bundle.setTotal(resources != null ? resources.length : 0);
		if (resources != null) {
			for (Resource l : resources) {
				bundle.addEntry().setResource(l).setFullUrl("/" + l.fhirType() + "/" + l.getId());
			}
		}
		return bundle;
	}
}
