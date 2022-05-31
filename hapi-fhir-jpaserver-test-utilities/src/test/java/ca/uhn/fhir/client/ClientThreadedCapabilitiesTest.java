package ca.uhn.fhir.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.interceptor.SearchPreferHandlingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test supporting https://github.com/hapifhir/hapi-fhir/issues/3299
 * Validates that capabilityStatement request is always sent first and is executed only once per client-endpoint,
 * even when executed from multiple threads
 */
public class ClientThreadedCapabilitiesTest {
	private static final Logger ourLog = LoggerFactory.getLogger("ClientThreadedCapabilitiesTest");

	private static final FhirContext fhirContext = FhirContext.forR4();
	private static final String SERVER_URL = "http://hapi.fhir.org/baseR4";
	private IGenericClient myClient;
	private static final IClientInterceptor myCountingMetaClientInterceptor  = new CountingMetaClientInterceptor();
	private static final Collection<String> lastNames = Lists.newArrayList("Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
		"Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee");

	@RegisterExtension
	public static RestfulServerExtension ourServer = new RestfulServerExtension(fhirContext)
		.registerProvider(new TestPatientResourceProvider())
		.withValidationMode(ServerValidationModeEnum.ONCE)
		.registerInterceptor(new SearchPreferHandlingInterceptor());


	@BeforeEach
	public void beforeEach() throws Exception {
		ourServer.getFhirClient().registerInterceptor(myCountingMetaClientInterceptor);
	}


	@Test
	public void capabilityRequestSentOnlyOncePerClient() {
		IRestfulClientFactory factory = fhirContext.getRestfulClientFactory();
		factory.setSocketTimeout(300 * 1000);

		Executor executor = Executors.newFixedThreadPool(lastNames.size(), r -> {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			});

		Collection<CompletableFuture<Object>> futures = lastNames.stream()
				.map(last -> CompletableFuture.supplyAsync(() -> searchPatient(last), executor)).collect(toList());


		final StopWatch sw = new StopWatch();
		futures.forEach(CompletableFuture::join);
		ourLog.info("Total elapsed time: {}", sw);

		int metaClientRequestCount = ((CountingMetaClientInterceptor) myCountingMetaClientInterceptor).getCount();
		assertEquals(1, metaClientRequestCount);
	}


	private Object searchPatient(String last) {
		return ourServer.getFhirClient().search()
			.forResource("Patient")
			.returnBundle(Bundle.class)
			.where(Patient.FAMILY.matches().value(last))
			.execute();
	}


	private static class CountingMetaClientInterceptor implements IClientInterceptor {
		AtomicInteger counter = new AtomicInteger();

		public int getCount() {
			return counter.get();
		}

		@Override
		public void interceptRequest(IHttpRequest theRequest) {
//			ourLog.info("Request: {}", theRequest.getUri());
			if (theRequest.getUri().endsWith("/metadata")) {
				counter.getAndIncrement();
			} else {
				// metadata request must always be first
				if (counter.get() == 0) {
					fail("A non-metadata request was executed before metadata request");
				}
			}
		}

		@Override
		public void interceptResponse(IHttpResponse theResponse) {
		}
	}

	public static class TestPatientResourceProvider implements IResourceProvider {

		Random rand = new Random(new Date().getTime());

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search()
		public Patient search(@OptionalParam(name = Patient.SP_FAMILY) StringParam theFamilyName) {
			Patient patient = new Patient();
			patient.getIdElement().setValue("Patient/" + rand.nextInt() + "/_history/222");
			ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(patient, BundleEntrySearchModeEnum.INCLUDE.getCode());
			patient.addName(new HumanName().setFamily(theFamilyName.getValue()));
			patient.setActive(true);
			return patient;
		}

	}



}
