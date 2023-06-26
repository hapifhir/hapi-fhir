package ca.uhn.fhir.context;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContextScanningDstu3Test {
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ContextScanningDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testContextDoesntScanUnneccesaryTypes() {
		FhirContext ctx = FhirContext.forDstu3();
		
		TreeSet<String> resDefs = scannedResourceNames(ctx);
		TreeSet<String> elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);
		assertThat(resDefs, not(containsInRelativeOrder("Observation")));
		
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:" + ourPort);
		client.read().resource(Patient.class).withId("1").execute();
		
		resDefs = scannedResourceNames(ctx);
		elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);		
		assertThat(resDefs, not(containsInRelativeOrder("Observation")));

		client.read().resource(Observation.class).withId("1").execute();
		resDefs = scannedResourceNames(ctx);
		elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);		
		assertThat(resDefs, containsInRelativeOrder("Observation"));
	}

	public static void main(String[] args) {
		
		// 1.6 - no defer - Took 6700 ms - 6.7ms / pass
		// 1.6 - no defer - Took 6127 ms - 6.127ms / pass
		
		// 1.6 - defer - Took 2523 ms - 2.523ms / pass
		// 1.6 - defer - Took 2328 ms - 2.328ms / pass
		
		int passes = 1000;
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < passes; i++) {
			FhirContext ctx = FhirContext.forDstu3();
			ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
			ctx.getResourceDefinition("Observation");
		}
		long end = System.currentTimeMillis();
		
		long delay = end-start;
		float per = (float)delay / (float)passes;
		
		ourLog.info("Took {} ms - {}ms / pass", delay, per);
	}
	
	@Test
	public void testDeferredScanning() {
		FhirContext ctx = FhirContext.forDstu3();
		ctx.getRestfulClientFactory().setSocketTimeout(600000);
		
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		TreeSet<String> resDefs = scannedResourceNames(ctx);
		TreeSet<String> elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);
		assertThat(resDefs, not(containsInRelativeOrder("Observation")));
		
		BaseRuntimeElementCompositeDefinition<?> compositeDef = (BaseRuntimeElementCompositeDefinition<?>) ctx.getElementDefinition("identifier");
		assertFalse(compositeDef.isSealed());
		
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:" + ourPort);
		client.read().resource(Patient.class).withId("1").execute();
		
		resDefs = scannedResourceNames(ctx);
		elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);		
		assertThat(resDefs, not(containsInRelativeOrder("Observation")));
		compositeDef = (BaseRuntimeElementCompositeDefinition<?>) ctx.getElementDefinition("identifier");
		assertFalse(compositeDef.isSealed());

		client.read().resource(Observation.class).withId("1").execute();
		resDefs = scannedResourceNames(ctx);
		elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);		
		assertThat(resDefs, containsInRelativeOrder("Observation"));		
		compositeDef = (BaseRuntimeElementCompositeDefinition<?>) ctx.getElementDefinition("identifier");
		assertTrue(compositeDef.isSealed());
	}

	private TreeSet<String> scannedResourceNames(FhirContext ctx) {
		TreeSet<String> defs = new TreeSet<String>();
		for (RuntimeResourceDefinition next : ctx.getAllResourceDefinitions()) {
			defs.add(next.getName());
		}
		return defs;
	}
	
	private TreeSet<String> scannedElementNames(FhirContext ctx) {
		TreeSet<String> defs = new TreeSet<String>();
		for (BaseRuntimeElementDefinition<?> next : ctx.getElementDefinitions()) {
			defs.add(next.getName());
		}
		return defs;
	}

	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(new PatientProvider(), new ObservationProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId) {
			return (Patient) new Patient().setId(theId);
		}

	}
	
	public static class ObservationProvider implements IResourceProvider {

		@Override
		public Class<Observation> getResourceType() {
			return Observation.class;
		}

		@Read
		public Observation read(@IdParam IdType theId) throws FHIRFormatError {
			Observation retVal = new Observation();
			retVal.setId(theId);
			retVal.addIdentifier().setSystem("ISYS").setValue("IVAL");
			retVal.setStatus(ObservationStatus.FINAL);
			retVal.setValue(new StringType("VAL"));
			return retVal; 
		}

	}

}
