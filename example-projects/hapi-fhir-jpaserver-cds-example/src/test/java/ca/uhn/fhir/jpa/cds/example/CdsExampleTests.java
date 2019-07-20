package ca.uhn.fhir.jpa.cds.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

// TODO Remove @Ignore once Chris Schuler has fixed the external jar this project depends on
@Ignore
public class CdsExampleTests {
	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();

	protected static int ourPort;

	private static Server ourServer;
	private static String ourServerBase;

	private static Collection<IResourceProvider> providers;

	@BeforeClass
	public static void beforeClass() throws Exception {

		// Configure and spin up server
		String path = Paths.get("").toAbsolutePath().toString();

		ourServer = new Server(0);

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/hapi-fhir-jpaserver-cds");
		webAppContext.setDescriptor(path + "/src/main/webapp/WEB-INF/web.xml");
		webAppContext.setResourceBase(path + "/target/hapi-fhir-jpaserver-cds");
		webAppContext.setParentLoaderPriority(true);

		ourServer.setHandler(webAppContext);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		ourServerBase = "http://localhost:" + ourPort + "/hapi-fhir-jpaserver-cds/baseDstu3";
		ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
		ourClient.registerInterceptor(new LoggingInterceptor(true));

		// Load terminology for measure tests (HEDIS measures)
		putResource("measure-terminology-bundle.json", "");

		// load test data and conversion library for $apply operation tests
		putResource("general-practitioner.json", "Practitioner-12208");
		putResource("general-patient.json", "Patient-12214");
		putResource("general-fhirhelpers-3.json", "FHIRHelpers");
	}

	@AfterClass
	public static void afterClass() throws Exception {
		JettyUtil.closeServer(ourServer);
	}

	private static void putResource(String resourceFileName, String id) {
		InputStream is = CdsExampleTests.class.getResourceAsStream(resourceFileName);
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		String json = scanner.hasNext() ? scanner.next() : "";

		boolean isJson = resourceFileName.endsWith("json");

		IBaseResource resource = isJson ? ourCtx.newJsonParser().parseResource(json) : ourCtx.newXmlParser().parseResource(json);

		if (resource instanceof Bundle) {
			ourClient.transaction().withBundle((Bundle) resource).execute();
		}
		else {
			ourClient.update().resource(resource).withId(id).execute();
		}
	}

	/*
	 *
	 * 	Testing Individual Measure
	 * 	This test patient satisfies all the group population criteria for this measure.
	 *
	 * */
	@Test
	public void PatientMeasureTest() {
		// load measure specific test data
		putResource("patient-measure-test-bundle.json", "");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("patient").setValue(new StringType("Patient/Patient-6529"));
		inParams.addParameter().setName("periodStart").setValue(new DateType("2003-01-01"));
		inParams.addParameter().setName("periodEnd").setValue(new DateType("2003-12-31"));

		Parameters outParams = ourClient
			.operation()
			.onInstance(new IdDt("Measure", "measure-asf"))
			.named("$evaluate-measure")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		List<Parameters.ParametersParameterComponent> response = outParams.getParameter();

		Assert.assertTrue(!response.isEmpty());

		Parameters.ParametersParameterComponent component = response.get(0);

		Assert.assertTrue(component.getResource() instanceof MeasureReport);

		MeasureReport report = (MeasureReport) component.getResource();

		for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				Assert.assertTrue(population.getCount() > 0);
			}
		}
	}

	/*
	 *
	 * 	Testing Patient List Measure
	 * 	This test is only testing for valid initial population membership.
	 * 	There are 2 patients that reference Practitioner-2520 as their general practitioner.
	 * 	However, only one meets the initial population criteria for the measure.
	 *
	 * */
	@Test
	public void PatientListMeasureTest() {
		// load measure specific test data
		putResource("patient-list-measure-test-bundle.json", "");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("reportType").setValue(new StringType("patient-list"));
		inParams.addParameter().setName("practitioner").setValue(new StringType("Practitioner/Practitioner-2520"));
		inParams.addParameter().setName("periodStart").setValue(new DateType("1997-01-01"));
		inParams.addParameter().setName("periodEnd").setValue(new DateType("1997-12-31"));

		Parameters outParams = ourClient
			.operation()
			.onInstance(new IdDt("Measure", "measure-ccs"))
			.named("$evaluate-measure")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		List<Parameters.ParametersParameterComponent> response = outParams.getParameter();

		Assert.assertTrue(!response.isEmpty());

		Parameters.ParametersParameterComponent component = response.get(0);

		Assert.assertTrue(component.getResource() instanceof MeasureReport);

		MeasureReport report = (MeasureReport) component.getResource();

		for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				if (population.getCode().getCodingFirstRep().getCode().equals("initial-population")) {
					Assert.assertTrue(population.getCount() == 1);
				}
			}
		}
	}

	/*
	 *
	 * 	Testing Population (or Summary) Measure
	 * 	This tests a population of 100 patients. 10 patients satisfy the initial population criteria.
	 * 	However, only 2 meet the numerator criteria.
	 *
	 * */
	@Test
	public void PopulationMeasureTest() {
		// load measure specific test data
		putResource("population-measure-network-bundle.json", "");
		putResource("population-measure-patients-bundle.json", "");
		putResource("population-measure-test-bundle.json", "");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("reportType").setValue(new StringType("population"));
		inParams.addParameter().setName("periodStart").setValue(new DateType("1997-01-01"));
		inParams.addParameter().setName("periodEnd").setValue(new DateType("1997-12-31"));

		Parameters outParams = ourClient
			.operation()
			.onInstance(new IdDt("Measure", "measure-bcs"))
			.named("$evaluate-measure")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		List<Parameters.ParametersParameterComponent> response = outParams.getParameter();

		Assert.assertTrue(!response.isEmpty());

		Parameters.ParametersParameterComponent component = response.get(0);

		Assert.assertTrue(component.getResource() instanceof MeasureReport);

		MeasureReport report = (MeasureReport) component.getResource();

		Assert.assertTrue(report.getEvaluatedResources() != null);

		for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				Assert.assertTrue(population.getCount() > 0);
			}
		}
	}

	/*
	 *
	 * 	Testing Patient View CDS Hook
	 * 	This tests whether a patient has had appropriate labs/orders for Breast Cancer detection.
	 * 	If not, a suggestion will be returned.
	 *
	 * */
	@Test
	public void PatientViewCdsHooksTest() throws IOException {
		// load terminology and test data specific to hook
		putResource("cds-codesystems.json", "");
		putResource("cds-valuesets.json", "");
		putResource("cds-bcs-bundle.json", "");

		// Get the CDS Hooks request
		InputStream is = this.getClass().getResourceAsStream("cds-bcs-request.json");
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		String cdsHooksRequest = scanner.hasNext() ? scanner.next() : "";
		cdsHooksRequest = cdsHooksRequest.replace("XXXXX", ourServerBase);
		byte[] data = cdsHooksRequest.getBytes("UTF-8");

		URL url = new URL("http://localhost:" + ourPort + "/hapi-fhir-jpaserver-cds/cds-services/bcs-decision-support");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(data);

		StringBuilder response = new StringBuilder();
		try(Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")))
		{
			for (int i; (i = in.read()) >= 0;) {
				response.append((char) i);
			}
		}

		String expected = "{\n" +
			"  \"cards\": [\n" +
			"    {\n" +
			"      \"summary\": \"A Mammogram procedure for the patient is recommended\",\n" +
			"      \"indicator\": \"warning\",\n" +
			"      \"detail\": \"The patient has not had a Mammogram procedure in the last 39 months\",\n" +
			"      \"source\": {},\n" +
			"      \"suggestions\": [\n" +
			"        {\n" +
			"          \"label\": \"Mammogram request\",\n" +
			"          \"actions\": [\n" +
			"            {\n" +
			"              \"type\": \"create\",\n" +
			"              \"description\": \"The patient has not had a Mammogram procedure in the last 39 months\",\n" +
			"              \"resource\": {\n" +
			"                \"resourceType\": \"ProcedureRequest\",\n" +
			"                \"status\": \"draft\",\n" +
			"                \"intent\": \"order\",\n" +
			"                \"code\": {\n" +
			"                  \"coding\": [\n" +
			"                    {\n" +
			"                      \"system\": \"http://www.ama-assn.org/go/cpt\",\n" +
			"                      \"code\": \"77056\",\n" +
			"                      \"display\": \"Mammography; bilateral\"\n" +
			"                    }\n" +
			"                  ]\n" +
			"                },\n" +
			"                \"subject\": {\n" +
			"                  \"reference\": \"Patient/Patient-6535\"\n" +
			"                }\n" +
			"              }\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}\n";

		String withoutID = response.toString().replaceAll("\"id\":.*\\s", "");
		Assert.assertTrue(
			withoutID.replaceAll("\\s+", "")
				.equals(expected.replaceAll("\\s+", ""))
		);
	}

	/*
	 *
	 * 	Testing $apply operation for a PlanDefinition resource
	 * 	This test applies a PlanDefinition and returns a CarePlan with a dynamic property populated.
	 *
	 * */
	@Test
	public void PlanDefinitionApplyTest() throws ClassNotFoundException {
		putResource("plandefinition-apply-library.json", "plandefinitionApplyTest");
		putResource("plandefinition-apply.json", "apply-example");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("patient").setValue(new StringType("Patient-12214"));

		Parameters outParams = ourClient
			.operation()
			.onInstance(new IdDt("PlanDefinition", "apply-example"))
			.named("$apply")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		List<Parameters.ParametersParameterComponent> response = outParams.getParameter();

		Assert.assertTrue(!response.isEmpty());

		Resource resource = response.get(0).getResource();

		Assert.assertTrue(resource instanceof CarePlan);

		CarePlan carePlan = (CarePlan) resource;

		Assert.assertTrue(carePlan.getTitle().equals("This is a dynamic definition!"));
	}

	/*
	 *
	 * 	Testing $apply operation for an ActivityDefinition resource
	 * 	This test applies an ActivityDefinition and returns a ProcedureRequest with a dynamic property populated.
	 *
	 * */
	@Test
	public void ActivityDefinitionApplyTest() {
		putResource("activitydefinition-apply-library.json", "activityDefinitionApplyTest");
		putResource("activitydefinition-apply.json", "ad-apply-example");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("patient").setValue(new StringType("Patient-12214"));

		Parameters outParams = ourClient
			.operation()
			.onInstance(new IdDt("ActivityDefinition", "ad-apply-example"))
			.named("$apply")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		List<Parameters.ParametersParameterComponent> response = outParams.getParameter();

		Assert.assertTrue(!response.isEmpty());

		Resource resource = response.get(0).getResource();

		Assert.assertTrue(resource instanceof ProcedureRequest);

		ProcedureRequest procedureRequest = (ProcedureRequest) resource;

		Assert.assertTrue(procedureRequest.getDoNotPerform());
	}
}
