package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class ClientConnectionReuseTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ClientConnectionReuseTest.class);
	@RegisterExtension
	@Order(0)
	private static RestfulServerExtension ourServer = new RestfulServerExtension(FhirContext.forR4Cached());
	@RegisterExtension
	@Order(1)
	private static HashMapResourceProviderExtension<Patient> ourPatientProvider = new HashMapResourceProviderExtension<>(ourServer, Patient.class);

	@ParameterizedTest()
	@MethodSource("clients")
	public void testRead(IGenericClient theClient) {
		Patient patient = new Patient();
		patient.setId("Patient/P");
		ourPatientProvider.store(patient);

		StopWatch sw = new StopWatch();
		int reps = 100;
		for (int i = 0; i < reps; i++) {
			ourServer
				.getFhirClient()
				.read()
				.resource(Patient.class)
				.withId("Patient/P")
				.execute();
		}
		ourLog.info("Invoked {} counts in {}", reps, sw);

		assertEquals(1, ourServer.getConnectionsOpenedCount());
	}

	@ParameterizedTest()
	@MethodSource("clients")
	public void testSearch(IGenericClient theClient) {
		StopWatch sw = new StopWatch();
		int reps = 100;
		for (int i = 0; i < reps; i++) {
			ourServer
				.getFhirClient()
				.search()
				.forResource(Patient.class)
				.returnBundle(Bundle.class)
				.execute();
		}
		ourLog.info("Invoked {} counts in {}", reps, sw);

		assertEquals(1, ourServer.getConnectionsOpenedCount());
	}

	@ParameterizedTest()
	@MethodSource("clients")
	public void testCreate(IGenericClient theClient) {
		StopWatch sw = new StopWatch();
		int reps = 100;
		for (int i = 0; i < reps; i++) {
			ourServer
				.getFhirClient()
				.create()
				.resource(new Patient().setActive(true))
				.execute();
		}
		ourLog.info("Invoked {} counts in {}", reps, sw);

		assertEquals(1, ourServer.getConnectionsOpenedCount());
	}


	public static List<IGenericClient> clients() {
		FhirContext ctx = FhirContext.forR4Cached();

		IGenericClient client = ctx.newRestfulGenericClient(ourServer.getBaseUrl());

		IGenericClient gzipClient = ctx.newRestfulGenericClient(ourServer.getBaseUrl());
		gzipClient.registerInterceptor(new GZipContentInterceptor());
		AdditionalRequestHeadersInterceptor additionalRequestHeadersInterceptor = new AdditionalRequestHeadersInterceptor();
		additionalRequestHeadersInterceptor.addHeaderValue(Constants.HEADER_ACCEPT_ENCODING, Constants.ENCODING_GZIP);
		gzipClient.registerInterceptor(additionalRequestHeadersInterceptor);

		return Lists.newArrayList(client, gzipClient);
	}

}
