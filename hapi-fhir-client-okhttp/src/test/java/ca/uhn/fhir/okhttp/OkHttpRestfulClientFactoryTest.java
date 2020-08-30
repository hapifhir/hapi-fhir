package ca.uhn.fhir.okhttp;

import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class OkHttpRestfulClientFactoryTest {

    private OkHttpRestfulClientFactory clientFactory;

    @BeforeEach
    public void setUp() {
        clientFactory = new OkHttpRestfulClientFactory();
    }

    @Test
    public void testGetNativeClient_noClientSet_returnsADefault() throws Exception {
   	 Call.Factory actualNativeClient = clientFactory.getNativeClient();

        assertNotNull(actualNativeClient);
    }

    @Test
    public void testGetNativeClient_noProxySet_defaultHasNoProxySet() throws Exception {
        OkHttpClient actualNativeClient = (OkHttpClient) clientFactory.getNativeClient();

        assertEquals(null, actualNativeClient.proxy());
    }

    @Test
    public void testSetHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().writeTimeout(5000, TimeUnit.MILLISECONDS).build();

        clientFactory.setHttpClient(okHttpClient);

        assertSame(okHttpClient, clientFactory.getNativeClient());
    }

	@Test
	public void testSocketTimeout() {
		clientFactory.setSocketTimeout(1515);

		assertEquals(1515, ((OkHttpClient)clientFactory.getNativeClient()).readTimeoutMillis());
		assertEquals(1515, ((OkHttpClient)clientFactory.getNativeClient()).writeTimeoutMillis());
	}

	@Test
	public void testConnectTimeout() {
		clientFactory.setConnectTimeout(1516);

		assertEquals(1516, ((OkHttpClient)clientFactory.getNativeClient()).connectTimeoutMillis());
	}
}
