package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Media;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServeMediaResourceRawInterceptorTest {


	private static final Logger ourLog = LoggerFactory.getLogger(ServeMediaResourceRawInterceptorTest.class);
	private static int ourPort;
	private static RestfulServer ourServlet;
    private static Server ourServer;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static CloseableHttpClient ourClient;
	private static Media ourNextResponse;
	private static String ourReadUrl;
	private ServeMediaResourceRawInterceptor myInterceptor;

	@BeforeEach
	public void before() {
		myInterceptor = new ServeMediaResourceRawInterceptor();
		ourServlet.getInterceptorService().registerInterceptor(myInterceptor);
	}

	@AfterEach
	public void after() {
		ourNextResponse = null;
		ourServlet.getInterceptorService().unregisterInterceptor(myInterceptor);
	}

	@Test
	public void testMediaHasImageRequestHasNoAcceptHeader() throws IOException {
		ourNextResponse = new Media();
		ourNextResponse.getContent().setContentType("image/png");
		ourNextResponse.getContent().setData(new byte[]{2, 3, 4, 5, 6, 7, 8});

		HttpGet get = new HttpGet(ourReadUrl);
		try (CloseableHttpResponse response = ourClient.execute(get)) {
			assertEquals("application/fhir+json;charset=utf-8", response.getEntity().getContentType().getValue());
			String contents = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			assertThat(contents, containsString("\"resourceType\""));
		}
	}

	@Test
	public void testMediaHasImageRequestHasMatchingAcceptHeader() throws IOException {
		ourNextResponse = new Media();
		ourNextResponse.getContent().setContentType("image/png");
		ourNextResponse.getContent().setData(new byte[]{2, 3, 4, 5, 6, 7, 8});

		HttpGet get = new HttpGet(ourReadUrl);
		get.addHeader(Constants.HEADER_ACCEPT, "image/png");
		try (CloseableHttpResponse response = ourClient.execute(get)) {
			assertEquals("image/png", response.getEntity().getContentType().getValue());
			byte[] contents = IOUtils.toByteArray(response.getEntity().getContent());
			assertArrayEquals(new byte[]{2, 3, 4, 5, 6, 7, 8}, contents);
		}
	}

	@Test
	public void testMediaHasNoContentType() throws IOException {
		ourNextResponse = new Media();
		ourNextResponse.getContent().setData(new byte[]{2, 3, 4, 5, 6, 7, 8});

		HttpGet get = new HttpGet(ourReadUrl);
		get.addHeader(Constants.HEADER_ACCEPT, "image/png");
		try (CloseableHttpResponse response = ourClient.execute(get)) {
			assertEquals("application/fhir+json;charset=utf-8", response.getEntity().getContentType().getValue());
		}
	}

	@Test
	public void testMediaHasImageRequestHasNonMatchingAcceptHeaderOutputRaw() throws IOException {
		ourNextResponse = new Media();
		ourNextResponse.getContent().setContentType("image/png");
		ourNextResponse.getContent().setData(new byte[]{2, 3, 4, 5, 6, 7, 8});

		HttpGet get = new HttpGet(ourReadUrl + "?_output=data");
		try (CloseableHttpResponse response = ourClient.execute(get)) {
			assertEquals("image/png", response.getEntity().getContentType().getValue());
			byte[] contents = IOUtils.toByteArray(response.getEntity().getContent());
			assertArrayEquals(new byte[]{2, 3, 4, 5, 6, 7, 8}, contents);
		}
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
        JettyUtil.closeServer(ourServer);
		ourClient.close();
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		// Create server
		ourServer = new Server(0);
		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setDefaultResponseEncoding(EncodingEnum.JSON);
		ourServlet.setResourceProviders(new MyMediaResourceProvider());
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		// Create client
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		ourReadUrl = "http://localhost:" + ourPort + "/Media/123";
	}

}
