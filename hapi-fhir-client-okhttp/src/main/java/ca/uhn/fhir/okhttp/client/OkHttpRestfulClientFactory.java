package ca.uhn.fhir.okhttp.client;

/*
 * #%L
 * HAPI FHIR OkHttp Client
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
 * A Restful client factory based on OkHttp.
 *
 * @author Matthew Clarke | matthew.clarke@orionhealth.com | Orion Health
 */
public class OkHttpRestfulClientFactory extends RestfulClientFactory {

    private OkHttpClient myNativeClient;

    public OkHttpRestfulClientFactory() {
        super();
    }

    public OkHttpRestfulClientFactory(FhirContext theFhirContext) {
        super(theFhirContext);
    }

    @Override
    protected IHttpClient getHttpClient(String theServerBase) {
        return new OkHttpRestfulClient(getNativeClient(), new StringBuilder(theServerBase), null, null, null, null);
    }

    @Override
    protected void resetHttpClient() {
        myNativeClient = null;
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

    /**
     * Only accepts clients of type {@link OkHttpClient}
     *
     * @param okHttpClient
     */
    @Override
    public void setHttpClient(Object okHttpClient) {
        myNativeClient = (OkHttpClient) okHttpClient;
    }

    @Override
    public void setProxy(String theHost, Integer thePort) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(theHost, thePort));
        OkHttpClient.Builder builder = getNativeClient().newBuilder().proxy(proxy);
        setHttpClient(builder.build());
    }

}
