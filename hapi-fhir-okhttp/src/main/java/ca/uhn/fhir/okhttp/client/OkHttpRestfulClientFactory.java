package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;

/**
 * Created by matthewcl on 18/07/16.
 */
public class OkHttpRestfulClientFactory extends RestfulClientFactory {

    private OkHttpClient nativeClient;

    public OkHttpRestfulClientFactory() {
        super();
    }

    public OkHttpRestfulClientFactory(FhirContext theFhirContext) {
        super(theFhirContext);
    }

    @Override
    protected IHttpClient getHttpClient(String theServerBase) {
        return null;
    }

    @Override
    protected void resetHttpClient() {
        nativeClient = null;
    }

    public synchronized OkHttpClient getNativeClient() {
        if (nativeClient == null) {
            nativeClient = new OkHttpClient();
        }

        return nativeClient;
    }

    @Override
    public IHttpClient getHttpClient(StringBuilder theUrl,
                                     Map<String, List<String>> theIfNoneExistParams,
                                     String theIfNoneExistString,
                                     RequestTypeEnum theRequestType,
                                     List<Header> theHeaders) {
        return new OkHttpRestfulClient(getNativeClient(), theUrl, theIfNoneExistParams, theIfNoneExistString, theRequestType, theHeaders);
    }

    /**
     * Only accepts clients of type {@link OkHttpClient}
     *
     * @param okHttpClient
     */
    @Override
    public void setHttpClient(Object okHttpClient) {
        nativeClient = (OkHttpClient) okHttpClient;
    }

    @Override
    public void setProxy(String theHost, Integer thePort) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(theHost, thePort));
        OkHttpClient.Builder builder = getNativeClient().newBuilder().proxy(proxy);
        setHttpClient(builder.build());
    }

}
