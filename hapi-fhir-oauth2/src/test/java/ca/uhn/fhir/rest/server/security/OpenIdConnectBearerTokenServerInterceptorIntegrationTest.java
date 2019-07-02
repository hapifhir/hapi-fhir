package ca.uhn.fhir.rest.server.security;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mitre.oauth2.model.RegisteredClient;
import org.mitre.openid.connect.client.service.impl.StaticClientConfigurationService;
import org.mitre.openid.connect.client.service.impl.StaticServerConfigurationService;
import org.mitre.openid.connect.config.ServerConfiguration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class OpenIdConnectBearerTokenServerInterceptorIntegrationTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = new FhirContext();
	private static int ourPort;

	private static Server ourServer;
	private static OpenIdConnectBearerTokenServerInterceptor myInterceptor;

	@Test
	public void testSearchWithoutToken() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?");

		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(401, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testSearchWithExpiredToken() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?");
		httpGet.addHeader(
				"Authorization",
				"Bearer "
						+ "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0MDY4NDE4NTgsImF1ZCI6WyJjbGllbnQiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0Ojg4ODhcL3Vobi1vcGVuaWQtY29ubmVjdFwvIiwianRpIjoiOTNiMzRjOTUtNTNiMC00YzZmLTkwYjEtYWVjODRjZTc3OGFhIiwiaWF0IjoxNDA2ODM4MjU4fQ.fYtwehPUulUYnDG_10bN6TNf7uw2FNUh_E40YagpITrVfXsV06pjU2YpNgy8nbSFmxY9IBH44UXTmMH9PLFiRn88WsPMSrUQbFCcvGIYwhqkRjGm_J1Y6oWIafUzCwZBCvk4Ne44p3DJRR6FSZRnnC850p55901DGQmNLe-rZJk3t0MHl6wySduqT3K1-Vbuq-7H6xLE10hKpLhSqBTghpQNKNjm48jm0sHcFa3ENWzyWPOmpNfzDKmJAYK2UnBtqNSJP6AJzVrJXqSu-uzasq0VOVcRU4n8b39vU1olbho1eKF0cfQlQwbrtvWipBJJSsRp_tmB9SV9BXhENxOFTw");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(401, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testSearchWithValidToken() throws Exception {
		myInterceptor.setTimeSkewAllowance(10 * 365 * 24 * 60 * 60);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?");
		httpGet.addHeader(
				"Authorization",
				"Bearer "
						+ "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0MDY4NDE4NTgsImF1ZCI6WyJjbGllbnQiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0Ojg4ODhcL3Vobi1vcGVuaWQtY29ubmVjdFwvIiwianRpIjoiOTNiMzRjOTUtNTNiMC00YzZmLTkwYjEtYWVjODRjZTc3OGFhIiwiaWF0IjoxNDA2ODM4MjU4fQ.fYtwehPUulUYnDG_10bN6TNf7uw2FNUh_E40YagpITrVfXsV06pjU2YpNgy8nbSFmxY9IBH44UXTmMH9PLFiRn88WsPMSrUQbFCcvGIYwhqkRjGm_J1Y6oWIafUzCwZBCvk4Ne44p3DJRR6FSZRnnC850p55901DGQmNLe-rZJk3t0MHl6wySduqT3K1-Vbuq-7H6xLE10hKpLhSqBTghpQNKNjm48jm0sHcFa3ENWzyWPOmpNfzDKmJAYK2UnBtqNSJP6AJzVrJXqSu-uzasq0VOVcRU4n8b39vU1olbho1eKF0cfQlQwbrtvWipBJJSsRp_tmB9SV9BXhENxOFTw");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Before
	public void before() {
		myInterceptor.setTimeSkewAllowance(10 * 60);
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();

		DummyOpenIdServlet openIdServlet = new DummyOpenIdServlet();
		ServletHolder proxyHolder = new ServletHolder(openIdServlet);
		proxyHandler.addServletWithMapping(proxyHolder, "/openid/*");

		RestfulServer servlet = new RestfulServer();
		servlet.setResourceProviders(new DummyObservationResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");

		// DynamicServerConfigurationService s = new DynamicServerConfigurationService();
		// s.setWhitelist(new HashSet<String>());
		// s.getWhitelist().add("http://localhost:8888/uhn-openid-connect/");

		StaticServerConfigurationService srv = new StaticServerConfigurationService();
		srv.setServers(new HashMap<String, ServerConfiguration>());
		ServerConfiguration srvCfg = new ServerConfiguration();
		srvCfg.setJwksUri("http://localhost:" + ourPort + "/openid/jwk");
		srvCfg.setIssuer("http://localhost:8888/uhn-openid-connect/");
		srv.getServers().put("http://localhost:8888/uhn-openid-connect/", srvCfg);
		srv.afterPropertiesSet();

		StaticClientConfigurationService cli = new StaticClientConfigurationService();
		cli.setClients(new HashMap<String, RegisteredClient>());
		cli.getClients().put("http://localhost:8888/uhn-openid-connect/", new RegisteredClient());

		myInterceptor = new OpenIdConnectBearerTokenServerInterceptor();
		myInterceptor.setClientConfigurationService(cli);
		myInterceptor.setServerConfigurationService(srv);

		servlet.registerInterceptor(myInterceptor);

		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Observation.class;
		}

		@Search
		public Observation search() {
			Observation o = new Observation();
			o.setId("1");

			o.getName().setText("This is an observation");

			return o;
		}

	}

}
