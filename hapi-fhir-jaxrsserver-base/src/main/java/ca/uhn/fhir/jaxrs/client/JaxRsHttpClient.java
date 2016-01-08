package ca.uhn.fhir.jaxrs.client;

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

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.Header;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequestBase;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.util.VersionUtil;

/**
 * A Http Request based on JaxRs. This is an adapter around the class {@link javax.ws.rs.client.Client Client}
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsHttpClient implements IHttpClient {

    private Client myClient;
    private List<Header> myHeaders;
    private StringBuilder myUrl;
    private Map<String, List<String>> myIfNoneExistParams;
    private String myIfNoneExistString;
    private RequestTypeEnum myRequestType;

    public JaxRsHttpClient(Client myClient, StringBuilder url, Map<String, List<String>> myIfNoneExistParams, String myIfNoneExistString, EncodingEnum theEncoding, RequestTypeEnum theRequestType, List<Header> theHeaders) {
        this.myHeaders = theHeaders;
        this.myClient = myClient;
        this.myUrl = url;
        this.myIfNoneExistParams = myIfNoneExistParams;
        this.myIfNoneExistString = myIfNoneExistString;
        this.myRequestType = theRequestType;
    }

    @Override
    public IHttpRequestBase createByteRequest(String contents, String contentType, EncodingEnum encoding) {
        Builder retVal = createRequest();
        addHeadersToRequest(retVal, encoding);
        addMatchHeaders(retVal, myUrl);
        Entity<String> entity = Entity.entity(contents, contentType + Constants.HEADER_SUFFIX_CT_UTF_8);
        retVal.header(Constants.HEADER_CONTENT_TYPE, contentType + Constants.HEADER_SUFFIX_CT_UTF_8);
        return new JaxRsHttpRequestBase(retVal, myRequestType, entity);
    }

    @Override
    public IHttpRequestBase createParamRequest(Map<String, List<String>> myParams, EncodingEnum encoding) {
        MultivaluedMap<String, String> map = new MultivaluedHashMap<String, String>();
        for (Map.Entry<String, List<String>> nextParam : myParams.entrySet()) {
            List<String> value = nextParam.getValue();
            for (String s : value) {
                map.add(nextParam.getKey(), s);
            }
        }
        Builder retVal = createRequest();
        //addHeadersToRequest(retVal, encoding);
        addMatchHeaders(retVal, myUrl);
        Entity<Form> entity = Entity.form(map);
        return new JaxRsHttpRequestBase(retVal, myRequestType, entity);
    }

    @Override
    public IHttpRequestBase createBinaryRequest(IBaseBinary binary) {
        Entity<String> entity = Entity.entity(binary.getContentAsBase64(), binary.getContentType());
        Builder retVal = createRequest();
        addMatchHeaders(retVal, myUrl);
        return new JaxRsHttpRequestBase(retVal, myRequestType, entity);
    }

    @Override
    public IHttpRequestBase createGetRequest(EncodingEnum theEncoding) {
        Builder builder = createRequest();
        addHeadersToRequest(builder, theEncoding);
        addMatchHeaders(builder, myUrl);
        return new JaxRsHttpRequestBase(builder, myRequestType, null);
    }

    public void addHeadersToRequest(Builder builder, EncodingEnum theEncoding) {
        if (myHeaders != null) {
            for (Header next : myHeaders) {
                builder.header(next.getName(), next.getValue());
            }
        }

        builder.header("User-Agent", "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client)");
        builder.header("Accept-Charset", "utf-8");
        builder.acceptEncoding("gzip");

        if (theEncoding == null) {
            builder.accept(Constants.HEADER_ACCEPT_VALUE_ALL);
        } else if (theEncoding == EncodingEnum.JSON) {
            builder.accept(Constants.CT_FHIR_JSON);
        } else if (theEncoding == EncodingEnum.XML) {
            builder.accept(Constants.CT_FHIR_XML);
        }
    }

    private void addMatchHeaders(Builder theHttpRequest, StringBuilder theUrlBase) {
        if (myIfNoneExistParams != null) {
            StringBuilder b = newHeaderBuilder(theUrlBase);
            BaseHttpClientInvocation.appendExtraParamsWithQuestionMark(myIfNoneExistParams, b, b.indexOf("?") == -1);
            theHttpRequest.header(Constants.HEADER_IF_NONE_EXIST, b.toString());
        }

        if (myIfNoneExistString != null) {
            StringBuilder b = newHeaderBuilder(theUrlBase);
            b.append(b.indexOf("?") == -1 ? '?' : '&');
            b.append(myIfNoneExistString.substring(myIfNoneExistString.indexOf('?') + 1));
            theHttpRequest.header(Constants.HEADER_IF_NONE_EXIST, b.toString());
        }
    }

    private StringBuilder newHeaderBuilder(StringBuilder theUrlBase) {
        StringBuilder b = new StringBuilder();
        b.append(theUrlBase);
        if (theUrlBase.length() > 0 && theUrlBase.charAt(theUrlBase.length() - 1) == '/') {
            b.deleteCharAt(b.length() - 1);
        }
        return b;
    }

    private Builder createRequest() {
        return myClient.target(myUrl.toString()).request();
    }

}
