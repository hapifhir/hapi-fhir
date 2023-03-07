package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.IResourceLoader;
import ca.uhn.fhir.cr.config.CrProperties;
import ca.uhn.fhir.cr.config.CrR4Config;
import ca.uhn.fhir.cr.r4.measure.CareGapsOperationProvider;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.rp.r4.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.PatientResourceProvider;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = CrR4Config.class)
class CareGapsOperationProviderIT extends BaseJpaR4Test implements IResourceLoader {

	private static RestfulServer myRestServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static CloseableHttpClient ourHttpClient;
	private static Server ourServer;
	private static String ourServerBase;
	@Autowired
	CareGapsOperationProvider careGapsOperationProvider;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;

	@SuppressWarnings("deprecation")
	@AfterEach
	public void after() {
		ourClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}

	@BeforeEach
	public void beforeStartServer() throws Exception {
		if (myRestServer == null) {
			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);

			ObservationResourceProvider observationRp = new ObservationResourceProvider();
			observationRp.setDao(myObservationDao);

			OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
			organizationRp.setDao(myOrganizationDao);

			RestfulServer restServer = new RestfulServer(ourCtx);
			restServer.setResourceProviders(patientRp, observationRp, organizationRp);

			// TODO: The rest of the resource types we need... Measure, ValueSet, Library, Encounter, Procedure, Practitioner

			// TODO: Create a submit data provider...
			//var submitDataProvider = new SubmitDataProvider()
			restServer.setPlainProviders(mySystemProvider, careGapsOperationProvider); // TODO: Submit data provider

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourCtx = FhirContext.forR4Cached();
			restServer.setFhirContext(ourCtx);

			ourServer.setHandler(proxyHandler);
			JettyUtil.startServer(ourServer);
			int myPort = JettyUtil.getPortForStartedServer(ourServer);
			ourServerBase = "http://localhost:" + myPort + "/fhir/context";

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			ourHttpClient = builder.build();

			ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
			ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
			ourClient.setLogRequestAndResponse(true);
			myRestServer = restServer;
		}

		myRestServer.setDefaultResponseEncoding(EncodingEnum.XML);
		myRestServer.setPagingProvider(myPagingProvider);

		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		// Set properties
		CrProperties.MeasureProperties measureProperties = new CrProperties.MeasureProperties();
		CrProperties.MeasureProperties.MeasureReportConfiguration measureReportConfiguration = new CrProperties.MeasureProperties.MeasureReportConfiguration();
		measureReportConfiguration.setCareGapsReporter("Organization/alphora");
		measureReportConfiguration.setCareGapsCompositionSectionAuthor("Organization/alphora-author");
		measureProperties.setMeasureReport(measureReportConfiguration);
	}

	@Test
	public void careGapsEndToEnd() throws IOException {
		/* Scenario is that we have a Provider that is transmitting data to a Payer to validate that
no gaps in care exist (a "gap in care" means that a Patient is not conformant with best practices for a given pathology).
Specifically, for this test, we're checking to ensure that a Patient has had the appropriate colorectal cancer screenings.

So, it's expected that the Payer already has the relevant quality measure content loaded. The first two steps here are initializing the Payer
by loading Measure content, and by setting up a reporting Organization resource (IOW, the Payer's identify to associate with the care-gaps report).

The next step is for the Provider to submit data to the Payer for review. That's the submit data operation.

After that, the Provider can invoke $care-gaps to check for any issues, which are reported.

The Provider can then resolve those issues, submit additional data, and then check to see if the gaps are closed.

1. Initialize Payer with Measure content
2. Initialize Payer with Organization info
3. Provider submits Patient data
4. Provider invokes care-gaps (and discovers issues)
5. (not included in test, since it's done out of bad) Provider closes gap (by having the Procedure done on the Patient).
6. Provider submits additional Patient data
7. Provider invokes care-gaps (and discovers issues are closed).

		*/

		// 1. Initialize Payer content
		var measureBundle = (Bundle) readResource("ColorectalCancerScreeningsFHIR-bundle.json");
		ourClient.transaction().withBundle(measureBundle).execute();

		// 2. Initialize Payer org data
		var orgData = (Bundle) readResource("CaregapsAuthorAndReporter.json");
		ourClient.transaction().withBundle(orgData).execute();

		// 3. Provider submits Patient data
		var patientData = (Parameters) readResource("CaregapsPatientData.json");
		ourClient.operation().onInstance("Measure/ColorectalCancerScreeningsFHIR").named("submit-data").withParameters(patientData).execute();

		// 4. Provider runs $care-gaps
		var parameters = new Parameters();
		parameters.addParameter("status", "open-gap");
		parameters.addParameter("status", "closed-gap");
		parameters.addParameter("periodStart", new DateType("2020-01-01"));
		parameters.addParameter("periodEnd", new DateType("2020-12-31"));
		parameters.addParameter("subject", "Patient/end-to-end-EXM130");
		parameters.addParameter("measureId", "ColorectalCancerScreeningsFHIR");


		var result = ourClient.operation().onType("Measure").named("care-gaps").withParameters(parameters).execute();

		// TODO: assert open-gap
		assertNotNull(result);

		// 5. (out of band) Provider fixes gaps

		// 6. Provider submits additional Patient data showing that they did another procedure that was needed.
		var newData = (Parameters) readResource("CaregapsSubmitDataCloseGap.json");
		ourClient.operation().onInstance("Measure/ColorectalCancerScreeningFHIR").named("submit-data").withParameters(newData).execute();

		// 7. Provider runs care-gaps again
		result = ourClient.operation().onType("Measure").named("care-gaps").withParameters(parameters).execute();

		// TODO: assert closed-gap
		assertNotNull(result);
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}
}
