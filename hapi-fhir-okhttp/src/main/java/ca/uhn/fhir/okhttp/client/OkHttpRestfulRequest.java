package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.HttpClientUtil;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by matthewcl on 18/07/16.
 */
public class OkHttpRestfulRequest implements IHttpRequest {

    private final Request.Builder requestBuilder;
    private OkHttpClient client;
    private String theUrl;
    private RequestTypeEnum requestTypeEnum;
    private RequestBody body;

    public OkHttpRestfulRequest(OkHttpClient client, String theUrl, RequestTypeEnum requestTypeEnum, RequestBody body) {
        this.client = client;
        this.theUrl = theUrl;
        this.requestTypeEnum = requestTypeEnum;
        this.body = body;

        requestBuilder = new Request.Builder().url(theUrl);
    }

    public Request.Builder getRequest() {
        return requestBuilder;
    }

    @Override
    public void addHeader(String theName, String theValue) {
        requestBuilder.addHeader(theName, theValue);
    }

    @Override
    public IHttpResponse execute() throws IOException {
        requestBuilder.method(getHttpVerbName(), body);
        Call call = client.newCall(requestBuilder.build());
        return new OkHttpRestfulResponse(call.execute());
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        return requestBuilder.build().headers().toMultimap();
    }

    @Override
    public String getRequestBodyFromStream() throws IOException {
        return null;
    }

    @Override
    public String getUri() {
        return theUrl;
    }

    @Override
    public String getHttpVerbName() {
        return requestTypeEnum.name();
    }

}
