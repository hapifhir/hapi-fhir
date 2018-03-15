package ca.uhn.fhir.jpa.cds.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class CdsExampleTests {
	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();

	protected static int ourPort;

	private static Server ourServer;
	private static String ourServerBase;

	private static Collection<IResourceProvider> providers;

	@BeforeClass
	public static void beforeClass() throws Exception {

		String path = Paths.get("").toAbsolutePath().toString();

		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/hapi-fhir-jpaserver-cds");
		webAppContext.setDescriptor(path + "/src/main/webapp/WEB-INF/web.xml");
		webAppContext.setResourceBase(path + "/target/hapi-fhir-jpaserver-cds");
		webAppContext.setParentLoaderPriority(true);

		ourServer.setHandler(webAppContext);
		ourServer.start();

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		ourServerBase = "http://localhost:" + ourPort + "/hapi-fhir-jpaserver-cds/baseDstu3";
		ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
		ourClient.registerInterceptor(new LoggingInterceptor(true));

		// Load test data
		// Normally, I would use a transaction bundle, but issues with the random ports prevents that...
		// So, doing it the old-fashioned way =)

		// General
		putResource("general-practitioner.json", "Practitioner-12208");
		putResource("general-patient.json", "Patient-12214");
		putResource("general-fhirhelpers-3.json", "FHIRHelpers");
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	private static void putResource(String resourceFileName, String id) {
		InputStream is = CdsExampleTests.class.getResourceAsStream(resourceFileName);
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		String json = scanner.hasNext() ? scanner.next() : "";
		IBaseResource resource = ourCtx.newJsonParser().parseResource(json);
		ourClient.update(id, resource);
	}

	@Test
	public void MeasureProcessingTest() {
		putResource("measure-processing-library.json", "col-logic");
		putResource("measure-processing-measure.json", "col");
		putResource("measure-processing-procedure.json", "Procedure-9");
		putResource("measure-processing-condition.json", "Condition-13");
		putResource("measure-processing-valueset-1.json", "2.16.840.1.113883.3.464.1003.108.11.1001");
		putResource("measure-processing-valueset-2.json", "2.16.840.1.113883.3.464.1003.198.12.1019");
		putResource("measure-processing-valueset-3.json", "2.16.840.1.113883.3.464.1003.108.12.1020");
		putResource("measure-processing-valueset-4.json", "2.16.840.1.113883.3.464.1003.198.12.1010");
		putResource("measure-processing-valueset-5.json", "2.16.840.1.113883.3.464.1003.198.12.1011");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("patient").setValue(new StringType("Patient-12214"));
		inParams.addParameter().setName("startPeriod").setValue(new DateType("2001-01-01"));
		inParams.addParameter().setName("endPeriod").setValue(new DateType("2015-03-01"));

		Parameters outParams = ourClient
			.operation()
			.onInstance(new IdDt("Measure", "col"))
			.named("$evaluate")
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
			if (group.getIdentifier().getValue().equals("history-of-colorectal-cancer")) {
				Assert.assertTrue(group.getPopulation().get(0).getCount() > 0);
			}

			if (group.getIdentifier().getValue().equals("history-of-total-colectomy")) {
				Assert.assertTrue(group.getPopulation().get(0).getCount() > 0);
			}
		}
	}

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

	//@Test
	public void CdsHooksPatientViewTest() throws IOException {
		putResource("cds-bcs-library.json", "patient-view");
		putResource("cds-bcs-patient.json", "Patient-6532");
		putResource("cds-bcs-plandefinition.json", "bcs-decision-support");
		putResource("cds-bcs-activitydefinition.json", "mammogram-service-request");

		// Get the CDS Hooks request
		InputStream is = this.getClass().getResourceAsStream("cds-bcs-request.json");
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		String cdsHooksRequest = scanner.hasNext() ? scanner.next() : "";
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
			"      \"summary\": \"High risk for opioid overdose - taper now\",\n" +
			"      \"indicator\": \"warning\",\n" +
			"      \"detail\": \"Total morphine milligram equivalent (MME) is 20200.700mg/d. Taper to less than 50.\"\n" +
			"    }\n" +
			"  ]\n" +
			"}";

		Assert.assertTrue(
			response.toString().replaceAll("\\s+", "")
				.equals(expected.replaceAll("\\s+", ""))
		);
	}
}

class RandomServerPortProvider {

	private static List<Integer> ourPorts = new ArrayList<>();

	static int findFreePort() {
		ServerSocket server;
		try {
			server = new ServerSocket(0);
			int port = server.getLocalPort();
			ourPorts.add(port);
			server.close();
			Thread.sleep(500);
			return port;
		} catch (IOException | InterruptedException e) {
			throw new Error(e);
		}
	}

	public static List<Integer> list() {
		return ourPorts;
	}

}
