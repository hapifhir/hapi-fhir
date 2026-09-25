package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ServerFeaturesDstu2Test {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerFeaturesDstu2Test.class);

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@Test
	public void testOptions() throws Exception {
		String responseContent = ourServer.fhirRequest("").options().assertStatus(200).getBody();
		assertThat(responseContent).contains("<Conformance");

		/*
		 * Now with a leading /
		 */

		responseContent = ourServer.fhirRequest("/").options().assertStatus(200).getBody();
		assertThat(responseContent).contains("<Conformance");

	}


	/**
	 * See #313
	 */
	@Test
	public void testOptionsForNonBasePath1() throws Exception {
		String responseContent = ourServer.fhirRequest("/Foo").options().assertStatus(404).getBody();
		ourLog.info(responseContent);
	}

	/**
	 * See #313
	 */
	@Test
	public void testOptionsForNonBasePath2() throws Exception {
		String responseContent = ourServer.fhirRequest("/Patient/1").options().assertStatus(400).getBody();
		ourLog.info(responseContent);
	}

	/**
	 * See #313
	 */
	@Test
	public void testOptionsForNonBasePath3() throws Exception {
		String responseContent = ourServer.fhirRequest("/metadata").options().assertStatus(405).getBody();
		ourLog.info(responseContent);
	}

	@Test
	public void testOptionsJson() throws Exception {
		String responseContent = ourServer.fhirRequest("?_format=json").options().assertStatus(200).getBody();
		assertThat(responseContent).contains("resourceType\":\"Conformance");
	}

	@Test
	public void testHeadJson() throws Exception {
		HttpTestResponse response = ourServer.fhirRequest("/Patient/123").head();
		assertThat(response.getBodyBytes()).isEmpty();

		ourLog.info(response.toString());

		assertEquals(200, response.getStatusCode());
		assertThat(response.getHeader(Constants.HEADER_POWERED_BY)).contains("HAPI");
	}

	@Test
	public void testRegisterAndUnregisterResourceProviders() throws Exception {
		String responseContent = ourServer.fhirRequest("/Patient/1").get().assertStatus(200).getBody();
		assertThat(responseContent).contains("PRP1");

		Collection<IResourceProvider> originalProviders = new ArrayList<>(ourServer.getRestfulServer().getResourceProviders());
		DummyPatientResourceProvider2 newProvider = new DummyPatientResourceProvider2();
		try {

			// Replace provider
			for (IResourceProvider provider : originalProviders) {
				ourServer.getRestfulServer().unregisterProvider(provider);
			}
			ourServer.getRestfulServer().registerProvider(newProvider);

			responseContent = ourServer.fhirRequest("/Patient/1").get().assertStatus(200).getBody();
			assertThat(responseContent).contains("PRP2");

		} finally {

			// Restore providers
			ourServer.getRestfulServer().unregisterProvider(newProvider);
			originalProviders.forEach(p->ourServer.getRestfulServer().registerProvider(p));

		}
	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			Patient p1 = new Patient();
			p1.setId("p1ReadId");
			p1.addIdentifier().setValue("PRP1");
			return p1;
		}

	}

	public static class DummyPatientResourceProvider2 implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			Patient p1 = new Patient();
			p1.setId("p1ReadId");
			p1.addIdentifier().setValue("PRP2");
			return p1;
		}

	}

}
