package ca.uhn.fhir.rest.client.apache;

import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.uhn.fhir.i18n.Msg;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.impl.BaseClient;

public class ApacheRestfulClientFactoryTest {

	@Test
	public void testSetContext() {
		ApacheRestfulClientFactory factory = new ApacheRestfulClientFactory();
		factory.getServerValidationModeEnum();
		factory.setFhirContext(FhirContext.forDstu2());
		try {
			factory.setFhirContext(FhirContext.forDstu2());
			fail();
		} catch (IllegalStateException e) {
			assertEquals("java.lang.IllegalStateException: " + Msg.code(1356) + "RestfulClientFactory instance is already associated with one FhirContext. RestfulClientFactory instances can not be shared.", e.toString());
		}
	}

	@Test
	public void testValidatateBase() {
		FhirContext ctx = FhirContext.forDstu2();
		ApacheRestfulClientFactory factory = new ApacheRestfulClientFactory();
		factory.setFhirContext(ctx);
		factory.setConnectTimeout(1);
		try {
			factory.validateServerBase("http://127.0.0.1:22225", factory.getHttpClient("http://foo"), (BaseClient) ctx.newRestfulGenericClient("http://foo"));
			fail();
		} catch (FhirClientConnectionException e) {
			assertEquals(Msg.code(1357) + "Failed to retrieve the server metadata statement during client initialization. URL used was http://127.0.0.1:22225metadata", e.getMessage());
		}
	}

	@Test
	public void testGetNativeHttpClientWithProvidedProxy() throws IOException, HttpException {
		HttpRoute httpRoute = executeRequestAndCaptureHttpRoute(
				f -> f.setProxy("theHost", 0));

		HttpHost proxyHost = httpRoute.getProxyHost();
		assertNotNull(proxyHost.getHostName());
		assertEquals("theHost", proxyHost.getHostName());
		assertEquals(0, proxyHost.getPort());
	}

	@Test
	@SetSystemProperty(key = "http.proxyHost", value = "hostFromSystemProperty")
	@SetSystemProperty(key = "http.proxyPort", value = "1234")
	public void testGetNativeHttpClientWithSystemProxy() throws IOException, HttpException {
		HttpRoute httpRoute = executeRequestAndCaptureHttpRoute(f -> {
		});

		HttpHost proxyHost = httpRoute.getProxyHost();
		assertNotNull(proxyHost.getHostName());
		assertEquals("hostFromSystemProperty", proxyHost.getHostName());
		assertEquals(1234, proxyHost.getPort());
	}

	@Test
	@SetSystemProperty(key = "http.proxyHost", value = "hostFromSystemProperty")
	@SetSystemProperty(key = "http.proxyPort", value = "1234")
	public void testGetNativeHttpClientWithSystemAndProvidedProxy() throws IOException, HttpException {
		HttpRoute httpRoute = executeRequestAndCaptureHttpRoute(
				f -> f.setProxy("providedProxy", 0));

		HttpHost proxyHost = httpRoute.getProxyHost();
		assertNotNull(proxyHost.getHostName());
		assertEquals("providedProxy", proxyHost.getHostName());
		assertEquals(0, proxyHost.getPort());
	}

	private HttpRoute executeRequestAndCaptureHttpRoute(Consumer<ApacheRestfulClientFactory> factoryConsumer)
			throws IOException, HttpException {
		ClientExecChain mainExec = mock(ClientExecChain.class);
		ArgumentCaptor<HttpRoute> httpRouteCaptor = ArgumentCaptor.forClass(HttpRoute.class);
		when(mainExec.execute(
				any(HttpRoute.class),
				any(HttpRequestWrapper.class),
				any(HttpClientContext.class),
				any(HttpExecutionAware.class)))
				.thenReturn(mock(CloseableHttpResponse.class, new ReturnsDeepStubs()));

		ApacheRestfulClientFactory testableSut = createTestableSut(mainExec);
		factoryConsumer.accept(testableSut);
		testableSut.getNativeHttpClient().execute(new HttpGet("http://somewhere.net"));

		verify(mainExec).execute(
				httpRouteCaptor.capture(),
				any(HttpRequestWrapper.class),
				any(HttpClientContext.class),
				any(HttpExecutionAware.class));
		return httpRouteCaptor.getValue();
	}

	private ApacheRestfulClientFactory createTestableSut(ClientExecChain mainExec) {
		return new ApacheRestfulClientFactory() {
			@Override
			protected HttpClientBuilder getHttpClientBuilder() {
				return new HttpClientBuilder() {
					@Override
					protected ClientExecChain createMainExec(HttpRequestExecutor requestExec, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy, HttpProcessor proxyHttpProcessor, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler) {
						return mainExec;
					}
				};
			}
		};
	}
}
