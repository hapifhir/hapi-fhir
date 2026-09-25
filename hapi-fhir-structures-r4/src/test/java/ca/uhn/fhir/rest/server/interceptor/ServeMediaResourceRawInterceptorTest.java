package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Media;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServeMediaResourceRawInterceptorTest {


	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static Media ourNextResponse;
	private ServeMediaResourceRawInterceptor myInterceptor;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new MyMediaResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.JSON);

	private String myReadUrl;

	@BeforeEach
	public void before() {
		myInterceptor = new ServeMediaResourceRawInterceptor();
		ourServer.getInterceptorService().registerInterceptor(myInterceptor);

		myReadUrl = "/Media/123";
	}

	@AfterEach
	public void after() {
		ourNextResponse = null;
		ourServer.getInterceptorService().unregisterInterceptor(myInterceptor);
	}

	@Test
	public void testMediaHasImageRequestHasNoAcceptHeader() throws IOException {
		ourNextResponse = new Media();
		ourNextResponse.getContent().setContentType("image/png");
		ourNextResponse.getContent().setData(new byte[]{2, 3, 4, 5, 6, 7, 8});

		HttpTestResponse response = ourServer.fhirRequest(myReadUrl).get();
		assertEquals("application/fhir+json;charset=utf-8", response.getHeader(Constants.HEADER_CONTENT_TYPE));
		String contents = response.getBody();
		assertThat(contents).contains("\"resourceType\"");
	}

	@Test
	public void testMediaHasImageRequestHasMatchingAcceptHeader() throws IOException {
		ourNextResponse = new Media();
		ourNextResponse.getContent().setContentType("image/png");
		ourNextResponse.getContent().setData(new byte[]{2, 3, 4, 5, 6, 7, 8});

		HttpTestResponse response = ourServer.fhirRequest(myReadUrl).withHeader(Constants.HEADER_ACCEPT, "image/png").get();
		assertEquals("image/png", response.getHeader(Constants.HEADER_CONTENT_TYPE));
		byte[] contents = response.getBodyBytes();
		assertThat(contents).containsExactly(new byte[]{2, 3, 4, 5, 6, 7, 8});
	}

	@Test
	public void testMediaHasNoContentType() throws IOException {
		ourNextResponse = new Media();
		ourNextResponse.getContent().setData(new byte[]{2, 3, 4, 5, 6, 7, 8});

		HttpTestResponse response = ourServer.fhirRequest(myReadUrl).withHeader(Constants.HEADER_ACCEPT, "image/png").get();
		assertEquals("application/fhir+json;charset=utf-8", response.getHeader(Constants.HEADER_CONTENT_TYPE));
	}

	@Test
	public void testMediaHasImageRequestHasNonMatchingAcceptHeaderOutputRaw() throws IOException {
		ourNextResponse = new Media();
		ourNextResponse.getContent().setContentType("image/png");
		ourNextResponse.getContent().setData(new byte[]{2, 3, 4, 5, 6, 7, 8});

		HttpTestResponse response = ourServer.fhirRequest(myReadUrl + "?_output=data").get();
		assertEquals("image/png", response.getHeader(Constants.HEADER_CONTENT_TYPE));
		byte[] contents = response.getBodyBytes();
		assertThat(contents).containsExactly(new byte[]{2, 3, 4, 5, 6, 7, 8});
	}

	private static class MyMediaResourceProvider implements IResourceProvider {


		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Media.class;
		}

		@Read
		public Media read(@IdParam IIdType theId) {
			return ourNextResponse;
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
