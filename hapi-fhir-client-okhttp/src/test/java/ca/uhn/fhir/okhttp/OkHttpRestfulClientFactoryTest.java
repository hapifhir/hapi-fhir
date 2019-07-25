package ca.uhn.fhir.okhttp;

import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class OkHttpRestfulClientFactoryTest {

    private OkHttpRestfulClientFactory clientFactory;

    @Before
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

}