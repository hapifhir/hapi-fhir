package ca.uhn.fhir.okhttp.client;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Adapter for building an OkHttp-specific request.
 *
 * @author Matthew Clarke | matthew.clarke@orionhealth.com | Orion Health
 */
public class OkHttpRestfulRequest implements IHttpRequest {

    private final Request.Builder myRequestBuilder;
    private OkHttpClient myClient;
    private String myUrl;
    private RequestTypeEnum myRequestTypeEnum;
    private RequestBody myRequestBody;

    public OkHttpRestfulRequest(OkHttpClient theClient, String theUrl, RequestTypeEnum theRequestTypeEnum, RequestBody theRequestBody) {
        myClient = theClient;
        myUrl = theUrl;
        myRequestTypeEnum = theRequestTypeEnum;
        myRequestBody = theRequestBody;

        myRequestBuilder = new Request.Builder().url(theUrl);
    }

    public Request.Builder getRequest() {
        return myRequestBuilder;
    }

    @Override
    public void addHeader(String theName, String theValue) {
        myRequestBuilder.addHeader(theName, theValue);
    }

    @Override
    public IHttpResponse execute() throws IOException {
        myRequestBuilder.method(getHttpVerbName(), myRequestBody);
        Call call = myClient.newCall(myRequestBuilder.build());
        return new OkHttpRestfulResponse(call.execute());
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        return myRequestBuilder.build().headers().toMultimap();
    }

    @Override
    public String getRequestBodyFromStream() throws IOException {
        // returning null to indicate this is not supported, as documented in IHttpRequest's contract
        return null;
    }

    @Override
    public String getUri() {
        return myUrl;
    }

    @Override
    public String getHttpVerbName() {
        return myRequestTypeEnum.name();
    }

}
