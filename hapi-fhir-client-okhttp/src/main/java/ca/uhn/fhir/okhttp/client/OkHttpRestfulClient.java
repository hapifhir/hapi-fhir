package ca.uhn.fhir.okhttp.client;

import static ca.uhn.fhir.okhttp.utils.UrlStringUtils.*;

import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseBinary;

/*
 * #%L
 * HAPI FHIR OkHttp Client
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.method.MethodUtil;
import okhttp3.*;
import okhttp3.internal.Version;

/**
 * A Http Request based on OkHttp. This is an adapter around the class
 * {@link OkHttpClient}
 *
 * @author Matthew Clarke | matthew.clarke@orionhealth.com | Orion Health
 */
public class OkHttpRestfulClient implements IHttpClient {

    private Call.Factory myClient;
    private StringBuilder myUrl;
    private Map<String, List<String>> myIfNoneExistParams;
    private String myIfNoneExistString;
    private RequestTypeEnum myRequestType;
    private List<Header> myHeaders;
    private OkHttpRestfulRequest myRequest;

    public OkHttpRestfulClient(Call.Factory theClient,
                               StringBuilder theUrl,
                               Map<String, List<String>> theIfNoneExistParams,
                               String theIfNoneExistString,
                               RequestTypeEnum theRequestType,
                               List<Header> theHeaders) {
        myClient = theClient;
        myUrl = theUrl;
        myIfNoneExistParams = theIfNoneExistParams;
        myIfNoneExistString = theIfNoneExistString;
        myRequestType = theRequestType;
        myHeaders = theHeaders;
    }

    @Override
    public IHttpRequest createByteRequest(FhirContext theContext, String theContents, String theContentType, EncodingEnum theEncoding) {
        initBaseRequest(theContext, theEncoding, createPostBody(theContents, theContentType));
        return myRequest;
    }

    private void initBaseRequest(FhirContext theContext, EncodingEnum theEncoding, RequestBody body) {
        String sanitisedUrl = withTrailingQuestionMarkRemoved(myUrl.toString());
        myRequest = new OkHttpRestfulRequest(myClient, sanitisedUrl, myRequestType, body);
        addHeadersToRequest(myRequest, theEncoding, theContext);
    }

    private RequestBody createPostBody(String theContents, String theContentType) {
        return RequestBody.create(MediaType.parse(theContentType), theContents);
    }

    @Override
    public IHttpRequest createParamRequest(FhirContext theContext, Map<String, List<String>> theParams, EncodingEnum theEncoding) {
        initBaseRequest(theContext, theEncoding, getFormBodyFromParams(theParams));
        return myRequest;
    }

    private RequestBody getFormBodyFromParams(Map<String, List<String>> queryParams) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, List<String>> paramEntry : queryParams.entrySet()) {
            for (String value : paramEntry.getValue()) {
                formBuilder.add(paramEntry.getKey(), value);
            }
        }

        return formBuilder.build();
    }

    @Override
    public IHttpRequest createBinaryRequest(FhirContext theContext, IBaseBinary theBinary) {
        initBaseRequest(theContext, null, createPostBody(theBinary.getContent(), theBinary.getContentType()));
        return myRequest;
    }

    private RequestBody createPostBody(byte[] theContents, String theContentType) {
        return RequestBody.create(MediaType.parse(theContentType), theContents);
    }

    @Override
    public IHttpRequest createGetRequest(FhirContext theContext, EncodingEnum theEncoding) {
        initBaseRequest(theContext, theEncoding, null);
        return myRequest;
    }

    private void addHeadersToRequest(OkHttpRestfulRequest theHttpRequest, EncodingEnum theEncoding, FhirContext theContext) {
        if (myHeaders != null) {
            for (Header next : myHeaders) {
                theHttpRequest.addHeader(next.getName(), next.getValue());
            }
        }

        addUserAgentHeader(theHttpRequest, theContext);
        addAcceptCharsetHeader(theHttpRequest);
        MethodUtil.addAcceptHeaderToRequest(theEncoding, theHttpRequest, theContext);
        addIfNoneExistHeader(theHttpRequest);
    }

    private void addUserAgentHeader(OkHttpRestfulRequest theHttpRequest, FhirContext theContext) {
        theHttpRequest.addHeader("User-Agent", HttpClientUtil.createUserAgentString(theContext, Version.userAgent()));
    }

    private void addAcceptCharsetHeader(OkHttpRestfulRequest theHttpRequest) {
        theHttpRequest.addHeader("Accept-Charset", "utf-8");
    }

    private void addIfNoneExistHeader(IHttpRequest result) {
        if (myIfNoneExistParams != null) {
            addIfNoneExistHeaderFromParams(result, myIfNoneExistParams);
        } else if (myIfNoneExistString != null) {
            addIfNoneExistHeaderFromString(result, myIfNoneExistString);
        }
    }

    private void addIfNoneExistHeaderFromString(IHttpRequest result, String ifNoneExistString) {
        StringBuilder sb = newHeaderBuilder(myUrl);
        boolean shouldAddQuestionMark = !hasQuestionMark(sb);
        sb.append(shouldAddQuestionMark ? '?' : '&');
        sb.append(everythingAfterFirstQuestionMark(ifNoneExistString));
        result.addHeader(Constants.HEADER_IF_NONE_EXIST, sb.toString());
    }

    private void addIfNoneExistHeaderFromParams(IHttpRequest result, Map<String, List<String>> ifNoneExistParams) {
        StringBuilder sb = newHeaderBuilder(myUrl);
        boolean shouldAddInitialQuestionMark = !hasQuestionMark(sb);
        BaseHttpClientInvocation.appendExtraParamsWithQuestionMark(ifNoneExistParams, sb, shouldAddInitialQuestionMark);
        result.addHeader(Constants.HEADER_IF_NONE_EXIST, sb.toString());
    }

    public static StringBuilder newHeaderBuilder(StringBuilder baseUrl) {
        StringBuilder sb = new StringBuilder(baseUrl);
        if (endsWith(baseUrl, '/')) {
            deleteLastCharacter(sb);
        }
        return sb;
    }

}
