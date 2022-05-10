package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CapabilityStatementCacheR4Test {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@RegisterExtension
	protected final RestfulServerExtension myServerExtension = new RestfulServerExtension(myFhirContext)
		.registerProvider(new HashMapResourceProvider<>(myFhirContext, Patient.class))
		.withServer(t -> t.setServerConformanceProvider(new MyCapabilityStatementProvider(t)));

	@Test
	public void testCacheThreadShutsDownWhenServerShutsDown() throws Exception {
		CapabilityStatement response = myServerExtension.getFhirClient().capabilities().ofType(CapabilityStatement.class).execute();
		sleepAtLeast(20);
		CapabilityStatement response2 = myServerExtension.getFhirClient().capabilities().ofType(CapabilityStatement.class).execute();
		CapabilityStatement response3 = myServerExtension.getFhirClient().capabilities().ofType(CapabilityStatement.class).execute();
		CapabilityStatement response4 = myServerExtension.getFhirClient().capabilities().ofType(CapabilityStatement.class).execute();

		assertEquals(response.getId(), response2.getId());

		List<String> threadNames = Thread.getAllStackTraces().keySet().stream().map(t -> t.getName()).filter(t -> t.startsWith(ConformanceMethodBinding.CACHE_THREAD_PREFIX)).sorted().collect(Collectors.toList());
		assertEquals(1, threadNames.size());

		// Shut down the server
		myServerExtension.shutDownServer();

		await().until(() -> Thread.getAllStackTraces().keySet().stream().map(t -> t.getName()).filter(t -> t.startsWith(ConformanceMethodBinding.CACHE_THREAD_PREFIX)).sorted().collect(Collectors.toList()), empty());
	}

	private static class MyCapabilityStatementProvider extends ServerCapabilityStatementProvider {

		public MyCapabilityStatementProvider(RestfulServer theServer) {
			super(theServer);
		}

		@Override
		@Metadata(cacheMillis = 10)
		public IBaseConformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
			return super.getServerConformance(theRequest, theRequestDetails);
		}
	}

	@SuppressWarnings("BusyWait")
	public static void sleepAtLeast(long theMillis) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() <= start + theMillis) {
			try {
				long timeSinceStarted = System.currentTimeMillis() - start;
				long timeToSleep = Math.max(0, theMillis - timeSinceStarted);
				Thread.sleep(timeToSleep);
			} catch (InterruptedException theE) {
				// ignore
			}
		}
	}

}
