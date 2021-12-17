package ca.uhn.fhir.jpa.delete.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.DeleteExpungeProvider;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class DeleteExpungeProviderTest {

	@Mock
	private IDeleteExpungeJobSubmitter myJobSubmitter;
	private Server myServer;
	private FhirContext myCtx;
	private int myPort;
	private CloseableHttpClient myClient;

	@BeforeEach
	public void start() throws Exception {
		myCtx = FhirContext.forR4Cached();
		myServer = new Server(0);

		DeleteExpungeProvider provider = new DeleteExpungeProvider(myCtx, myJobSubmitter);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(myCtx);
		servlet.registerProvider(provider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		myPort = JettyUtil.getPortForStartedServer(myServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		myClient = builder.build();

	}

	@Test
	public void testSupplyingNoUrlsProvidesValidErrorMessage() throws IOException {
			HttpPost post = new HttpPost("http://localhost:" + myPort + "/" + ProviderConstants.OPERATION_DELETE_EXPUNGE);
			try(CloseableHttpResponse execute = myClient.execute(post)) {
				String body = IOUtils.toString(execute.getEntity().getContent(), Charset.defaultCharset());
				assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(400)));
				assertThat(body, is(containsString("At least one `url` parameter to $delete-expunge must be provided.")));
			}
	}

}
