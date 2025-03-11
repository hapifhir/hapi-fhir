package ca.uhn.fhir.context;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextScanningDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ContextScanningDstu3Test.class);

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new PatientProvider())
		 .registerProvider(new ObservationProvider())
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testContextDoesntScanUnneccesaryTypes() {
		FhirContext ctx = FhirContext.forDstu3();
		
		TreeSet<String> resDefs = scannedResourceNames(ctx);
		TreeSet<String> elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);
		assertThat(resDefs).doesNotContain("Observation");
		
		IGenericClient client = ctx.newRestfulGenericClient(ourServer.getBaseUrl());
		client.read().resource(Patient.class).withId("1").execute();
		
		resDefs = scannedResourceNames(ctx);
		elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);		
		assertThat(resDefs).doesNotContain("Observation");

		client.read().resource(Observation.class).withId("1").execute();
		resDefs = scannedResourceNames(ctx);
		elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);		
		assertThat(resDefs).contains("Observation");
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
		assertThat(resDefs).doesNotContain("Observation");


		BaseRuntimeElementCompositeDefinition<?> compositeDef = (BaseRuntimeElementCompositeDefinition<?>) ctx.getElementDefinition("identifier");
		assertFalse(compositeDef.isSealed());
		
		IGenericClient client = ctx.newRestfulGenericClient(ourServer.getBaseUrl());
		client.read().resource(Patient.class).withId("1").execute();
		
		resDefs = scannedResourceNames(ctx);
		elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);		
		assertThat(resDefs).doesNotContain("Observation");
		compositeDef = (BaseRuntimeElementCompositeDefinition<?>) ctx.getElementDefinition("identifier");
		assertFalse(compositeDef.isSealed());

		client.read().resource(Observation.class).withId("1").execute();
		resDefs = scannedResourceNames(ctx);
		elementDefs = scannedElementNames(ctx);
		ourLog.info("Have {} resource definitions: {}", ctx.getAllResourceDefinitions().size(), resDefs);
		ourLog.info("Have {} element definitions: {}", ctx.getElementDefinitions().size(), elementDefs);		
		assertThat(resDefs).contains("Observation");
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
		TestUtil.randomizeLocaleAndTimezone();
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
