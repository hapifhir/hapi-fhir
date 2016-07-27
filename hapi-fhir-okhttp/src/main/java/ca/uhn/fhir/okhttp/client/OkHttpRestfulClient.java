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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.HttpClientUtil;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import okhttp3.*;
import okhttp3.internal.Version;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.okhttp.utils.UrlStringUtils.*;

/**
 * A Http Request based on OkHttp. This is an adapter around the class
 * {@link OkHttpClient}
 *
 * @author Matthew Clarke | matthew.clarke@orionhealth.com | Orion Health
 */
public class OkHttpRestfulClient implements IHttpClient {

    private OkHttpClient client;
    private StringBuilder myUrl;
    private Map<String, List<String>> myIfNoneExistParams;
    private String myIfNoneExistString;
    private RequestTypeEnum theRequestType;
    private List<Header> myHeaders;
    private OkHttpRestfulRequest request;

    public OkHttpRestfulClient(OkHttpClient client,
                               StringBuilder theUrl,
                               Map<String, List<String>> theIfNoneExistParams,
                               String theIfNoneExistString,
                               RequestTypeEnum theRequestType,
                               List<Header> theHeaders) {
        this.client = client;
        myUrl = theUrl;
        myIfNoneExistParams = theIfNoneExistParams;
        myIfNoneExistString = theIfNoneExistString;
        this.theRequestType = theRequestType;
        myHeaders = theHeaders;
    }

    @Override
    public IHttpRequest createByteRequest(FhirContext theContext, String theContents, String theContentType, EncodingEnum theEncoding) {
        initBaseRequest(theContext, theEncoding, createPostBody(theContents, theContentType));
        return request;
    }

    private void initBaseRequest(FhirContext theContext, EncodingEnum theEncoding, RequestBody body) {
        String sanitisedUrl = withTrailingQuestionMarkRemoved(myUrl.toString());
        request = new OkHttpRestfulRequest(client, sanitisedUrl, theRequestType, body);
        addHeadersToRequest(request, theEncoding, theContext);
    }

    private RequestBody createPostBody(String theContents, String theContentType) {
        return RequestBody.create(MediaType.parse(theContentType), theContents);
    }

    @Override
    public IHttpRequest createParamRequest(FhirContext theContext, Map<String, List<String>> theParams, EncodingEnum theEncoding) {
        initBaseRequest(theContext, theEncoding, getFormBodyFromParams(theParams));
        return request;
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
        return request;
    }

    private RequestBody createPostBody(byte[] theContents, String theContentType) {
        return RequestBody.create(MediaType.parse(theContentType), theContents);
    }

    @Override
    public IHttpRequest createGetRequest(FhirContext theContext, EncodingEnum theEncoding) {
        initBaseRequest(theContext, theEncoding, null);
        return request;
    }

    private void addHeadersToRequest(OkHttpRestfulRequest theHttpRequest, EncodingEnum theEncoding, FhirContext theContext) {
        if (myHeaders != null) {
            for (Header next : myHeaders) {
                theHttpRequest.addHeader(next.getName(), next.getValue());
            }
        }

        addUserAgentHeader(theHttpRequest, theContext);
        addAcceptCharsetHeader(theHttpRequest);
        addAcceptHeader(theHttpRequest, theEncoding);
        addIfNoneExistHeader(theHttpRequest);
    }

    private void addUserAgentHeader(OkHttpRestfulRequest theHttpRequest, FhirContext theContext) {
        theHttpRequest.addHeader("User-Agent", HttpClientUtil.createUserAgentString(theContext, Version.userAgent()));
    }

    private void addAcceptCharsetHeader(OkHttpRestfulRequest theHttpRequest) {
        theHttpRequest.addHeader("Accept-Charset", "utf-8");
    }

    private void addAcceptHeader(OkHttpRestfulRequest theHttpRequest, EncodingEnum theEncoding) {
        Request.Builder builder = theHttpRequest.getRequest();

        if (theEncoding == null) {
            builder.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY);
        } else if (theEncoding == EncodingEnum.JSON) {
            builder.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON);
        } else if (theEncoding == EncodingEnum.XML) {
            builder.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML);
        }
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
