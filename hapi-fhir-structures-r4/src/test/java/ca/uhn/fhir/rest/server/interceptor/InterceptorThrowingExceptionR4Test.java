package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterceptorThrowingExceptionR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InterceptorThrowingExceptionR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static boolean ourHitMethod;
	private static List<Resource> ourReturn;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		ourServer.getInterceptorService().unregisterAllInterceptors();
		ourServer.getRestfulServer().setTenantIdentificationStrategy(null);
		ourReturn = null;
		ourHitMethod = false;
	}

	@AfterEach
	public void after() {
		ourServer.getInterceptorService().unregisterAllInterceptors();
	}


	private Resource createPatient(Integer theId) {
		Patient retVal = new Patient();
		if (theId != null) {
			retVal.setId(new IdType("Patient", (long) theId));
		}
		retVal.addName().setFamily("FAM");
		return retVal;
	}


	private String extractResponseAndClose(HttpResponse status) throws IOException {
		if (status.getEntity() == null) {
			return null;
		}
		String responseContent;
		responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.getEntity().getContent().close();
		return responseContent;
	}

	@Test
	public void testFailureInProcessingCompletedNormally() throws Exception {
		final List<Integer> hit = Collections.synchronizedList(new ArrayList<>());
		ourServer.getInterceptorService().registerInterceptor(new InterceptorAdapter() {
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(1);
				throw new NullPointerException("Hit 1");
			}
		});
		ourServer.getInterceptorService().registerInterceptor(new InterceptorAdapter() {
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(2);
				throw new NullPointerException("Hit 2");
			}
		});
		ourServer.getInterceptorService().registerInterceptor(new InterceptorAdapter() {
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(3);
				throw new NullPointerException("Hit 3");
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(response).contains("FAM");
		assertTrue(ourHitMethod);

		await().until(() -> hit.size() == 3);

		ourLog.info("Hit: {}", hit);
		assertThat(hit).as("Hits: " + hit.toString()).containsExactly(1, 2, 3);

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}


		@Read(version = true)
		public Patient read(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Patient) ourReturn.get(0);
		}


	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
