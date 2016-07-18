package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Map;

/**
 * Created by matthewcl on 18/07/16.
 */
public class OkHttpRestfulClientFactory extends RestfulClientFactory {

    private OkHttpClient myNativeClient;

    public OkHttpRestfulClientFactory(FhirContext theFhirContext) {
        super(theFhirContext);
    }

    @Override
    protected IHttpClient getHttpClient(String theServerBase) {
        return null;
    }

    @Override
    protected void resetHttpClient() {

    }

    public synchronized OkHttpClient getNativeClient() {
        if (myNativeClient == null) {
            myNativeClient = new OkHttpClient();
        }

        return myNativeClient;
    }

    @Override
    public IHttpClient getHttpClient(StringBuilder theUrl,
                                     Map<String, List<String>> theIfNoneExistParams,
                                     String theIfNoneExistString,
                                     RequestTypeEnum theRequestType,
                                     List<Header> theHeaders) {
        return new OkHttpRestfulClient(getNativeClient(), theUrl, theIfNoneExistParams, theIfNoneExistString, theRequestType, theHeaders);
    }

    @Override
    public <T> void setHttpClient(T theHttpClient) {

    }

    @Override
    public void setProxy(String theHost, Integer thePort) {

    }

}
