package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.IResourceLoader;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
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
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * End to end test for care gaps functionality
 * Scenario is that we have a Provider that is transmitting data to a Payer to validate that
 * no gaps in care exist (a "gap in care" means that a Patient is not conformant with best practices for a given pathology).
 * Specifically, for this test, we're checking to ensure that a Patient has had the appropriate colorectal cancer screenings.
 *
 * So, it's expected that the Payer already has the relevant quality measure content loaded. The first two steps here are initializing the Payer
 * by loading Measure content, and by setting up a reporting Organization resource (IOW, the Payer's identify to associate with the care-gaps report).
 *
 * The next step is for the Provider to submit data to the Payer for review. That's the submit data operation.
 *
 * After that, the Provider can invoke $care-gaps to check for any issues, which are reported.
 *
 * The Provider can then resolve those issues, submit additional data, and then check to see if the gaps are closed.
 *
 * 1. Initialize Payer with Measure content
 * 2. Initialize Payer with Organization info
 * 3. Provider submits Patient data
 * 4. Provider invokes care-gaps (and discovers issues)
 * 5. (not included in test, since it's done out of bad) Provider closes gap (by having the Procedure done on the Patient).
 * 6. Provider submits additional Patient data
 * 7. Provider invokes care-gaps (and discovers issues are closed).
 */
@ContextConfiguration(classes = {TestCrR4Config.class})
class R4MeasureOperationProviderIT extends BaseJpaR4Test implements IResourceLoader
{

	//private static RestfulServer ourRestServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static CloseableHttpClient ourHttpClient;
	private static Server ourServer;
	private static String ourServerBase;

	private static final String MY_FHIR_COMMON = "ca/uhn/fhir/cr/r4/immunization/Fhir_Common.json";
	private static final String MY_FHIR_HELPERS = "ca/uhn/fhir/cr/r4/immunization/Fhir_Helper.json";
	private static final String MY_TEST_DATA = "ca/uhn/fhir/cr/r4/immunization/Patients_Encounters_Immunizations_Practitioners.json";
	private static final String MY_IMMUNIZATION_CQL_RESOURCES = "ca/uhn/fhir/cr/r4/immunization/Measure_Library_Ontario_ImmunizationStatus.json";
	private static final String MY_VALUE_SETS = "ca/uhn/fhir/cr/r4/immunization/Terminology_ValueSets.json";


	@Autowired
	ApplicationContext myApplicationContext;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;


	@SuppressWarnings("deprecation")
	@AfterEach
	public void after() {
		ourClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}
	@Autowired
	RestfulServer ourRestfulServer;
	@BeforeEach
	public void beforeStartServer() throws Exception {

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(ourRestfulServer);
			proxyHandler.addServlet(servletHolder, "/fhir/*");

			ourCtx = ourRestfulServer.getFhirContext();

			ourServer.setHandler(proxyHandler);
			JettyUtil.startServer(ourServer);
			int myPort = JettyUtil.getPortForStartedServer(ourServer);
			ourServerBase = "http://localhost:" + myPort + "/fhir";

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			ourHttpClient = builder.build();

			ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
			ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
			ourClient.setLogRequestAndResponse(true);
			//ourRestServer = ourRestfulServer;

		ourRestfulServer.setDefaultResponseEncoding(EncodingEnum.XML);
		//ourRestfulServer.setPagingProvider(myPagingProvider);

		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

	}

	//compare 2 double values to assert no difference between expected and actual measure score
	protected void assertMeasureScore(MeasureReport theReport, double theExpectedScore) {
		//find the predefined expected score by looking up the report identifier
		double epsilon = 0.000001d;
		double actualScore = theReport.getGroupFirstRep().getMeasureScore().getValue().doubleValue();
		assertEquals(theExpectedScore, actualScore, epsilon);
	}

	//evaluates a Measure to produce one certain MeasureReport


	@Test
	public void test_Immunization_Ontario_Schedule() {
		//given
		var bundleFhirCommon = (Bundle) readResource(MY_FHIR_COMMON);
		ourClient.transaction().withBundle(bundleFhirCommon).execute();

		var bundleFhirHelpers = (Bundle) readResource(MY_FHIR_HELPERS);
		ourClient.transaction().withBundle(bundleFhirHelpers).execute();

		var bundleTestData = (Bundle) readResource(MY_TEST_DATA);
		ourClient.transaction().withBundle(bundleTestData).execute();

		var bundleValueSets = (Bundle) readResource(MY_VALUE_SETS);
		ourClient.transaction().withBundle(bundleValueSets).execute();

		var bundleCqlRsc = (Bundle) readResource(MY_IMMUNIZATION_CQL_RESOURCES);
		ourClient.transaction().withBundle(bundleCqlRsc).execute();

		//non-cached run, 1 patient
		var parametersEval1 = new Parameters();
		parametersEval1.addParameter("periodStart", new DateType("2020-01-01"));
		parametersEval1.addParameter("periodEnd", new DateType("2020-12-31"));
		//parametersEval.addParameter("practitioner", null);
		parametersEval1.addParameter("subject", "Patient/ImmunizationStatus-1-year-patient-1");

		var reportBasic = ourClient.operation().onInstance("Measure/ImmunizationStatusRoutine")
			.named("$evaluate-measure")
			.withParameters(parametersEval1)
			.returnResourceType(MeasureReport.class)
			.execute();

		assertNotNull(reportBasic);

		//cached run, 13 patients
		var parametersEval2 = new Parameters();
		parametersEval2.addParameter("periodStart", new DateType("2020-01-01"));
		parametersEval2.addParameter("periodEnd", new DateType("2020-12-31"));
		parametersEval2.addParameter("practitioner", "Practitioner/ImmunizationStatus-practitioner-3");
		//parametersEval2.addParameter("subject", "Patient/ImmunizationStatus-1-year-patient-1");

		var reportBasic2 = ourClient.operation().onInstance("Measure/ImmunizationStatusRoutine")
			.named("$evaluate-measure")
			.withParameters(parametersEval2)
			.returnResourceType(MeasureReport.class)
			.execute();
		//when*/
		//MeasureReport reportBasic = evaluateMeasureByMeasure("ImmunizationStatusRoutine", null, null);
		//MeasureReport reportByPractitioner = evaluateMeasureByMeasure("ImmunizationStatusRoutine", "Practitioner/ImmunizationStatus-practitioner-3", null);
		//MeasureReport reportIndividualImmunized = evaluateMeasureByMeasure("ImmunizationStatusRoutine", null, "ImmunizationStatus-1-year-patient-1");
		//MeasureReport reportIndividualNotImmunized = evaluateMeasureByMeasure("ImmunizationStatusRoutine", null, "ImmunizationStatus-1-year-patient-2");
		//assertNotNull(reportBasic);
		assertNotNull(reportBasic2);
		//then
		//assertMeasureScore(reportBasic, 0.3442623); //21 out of 61 patients are fully immunized on 2022-09-16
		//assertMeasureScore(reportByPractitioner, 0.23077); //3 out of 13 patients are fully immunized on 2022-09-16
		//assertMeasureScore(reportIndividualImmunized, 1.0); // the patient is fully immunized on on 2022-09-16
		//assertMeasureScore(reportIndividualNotImmunized, 0.0); // the patient is not fully immunized on 2022-09-16
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}
}
