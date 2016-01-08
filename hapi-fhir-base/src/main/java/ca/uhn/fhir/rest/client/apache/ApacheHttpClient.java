package ca.uhn.fhir.rest.client.apache;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequestBase;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.VersionUtil;

/**
 * A Http Client based on Apache. This is an adapter around the class {@link org.apache.http.client.HttpClient HttpClient}
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttpClient implements IHttpClient {
    
    HttpClient myClient; 
    /**
     * Get the myClient
     * @return the myClient
     */
    public HttpClient getMyClient() {
        return myClient;
    }

    private List<Header> myHeaders;    
    private StringBuilder myUrl;    
    private Map<String, List<String>> myIfNoneExistParams;
    private String myIfNoneExistString;
    private RequestTypeEnum myRequestType;
    
    public ApacheHttpClient(HttpClient myClient, StringBuilder url, Map<String, List<String>> myIfNoneExistParams, String myIfNoneExistString, EncodingEnum theEncoding, RequestTypeEnum theRequestType, List<Header> theHeaders) {
        this.myHeaders = theHeaders;
        this.myClient = myClient;
        this.myUrl = url;
        this.myIfNoneExistParams = myIfNoneExistParams;
        this.myIfNoneExistString = myIfNoneExistString;
        this.myRequestType = theRequestType;
    }
    
    @Override
    public IHttpRequestBase createByteRequest(String contents, String contentType, EncodingEnum encoding) {
        /*
         * We aren't using a StringEntity here because the constructors supported by
         * Android aren't available in non-Android, and vice versa. Since we add the
         * content type header manually, it makes no difference which one
         * we use anyhow.
         */
        ByteArrayEntity entity = new ByteArrayEntity(contents.getBytes(Constants.CHARSET_UTF8));
        HttpRequestBase retVal = createRequest(myUrl, entity);
        addHeadersToRequest(retVal, encoding);
        addMatchHeaders(retVal, myUrl);
        retVal.addHeader(Constants.HEADER_CONTENT_TYPE, contentType + Constants.HEADER_SUFFIX_CT_UTF_8);
        return new ApacheHttpRequestBase(myClient, retVal);
    }

    @Override    
    public IHttpRequestBase createParamRequest(Map<String, List<String>> myParams, EncodingEnum encoding) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        for (Entry<String, List<String>> nextParam : myParams.entrySet()) {
            List<String> value = nextParam.getValue();
            for (String s : value) {
                parameters.add(new BasicNameValuePair(nextParam.getKey(), s));
            }
        }
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
            HttpRequestBase retVal = createRequest(myUrl, entity);
            addMatchHeaders(retVal, myUrl);
            return new ApacheHttpRequestBase(myClient, retVal);                  
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException("Server does not support UTF-8 (should not happen)", e);
        }
    }

    @Override
    public IHttpRequestBase createBinaryRequest(IBaseBinary binary) {
        /*
         * Note: Be careful about changing which constructor we use for ByteArrayEntity,
         * as Android's version of HTTPClient doesn't support the newer ones for
         * whatever reason.
         */
        ByteArrayEntity entity = new ByteArrayEntity(binary.getContent());
        entity.setContentType(binary.getContentType());
        HttpRequestBase retVal = createRequest(myUrl, entity);
        addMatchHeaders(retVal, myUrl);
        return new ApacheHttpRequestBase(myClient, retVal);
    } 
    
    @Override
    public IHttpRequestBase createGetRequest(EncodingEnum theEncoding) {
        HttpRequestBase retVal = createRequest(myUrl, null);
        addHeadersToRequest(retVal, theEncoding);
        addMatchHeaders(retVal, myUrl);
        return new ApacheHttpRequestBase(myClient, retVal); 
    }    
    
    public void addHeadersToRequest(HttpRequestBase theHttpRequest, EncodingEnum theEncoding) {
        if (myHeaders != null) {
            for (Header next : myHeaders) {
                theHttpRequest.addHeader(next.getName(), next.getValue());
            }
        }
        
        theHttpRequest.addHeader("User-Agent", "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client)");
        theHttpRequest.addHeader("Accept-Charset", "utf-8");
        theHttpRequest.addHeader("Accept-Encoding", "gzip");
        
        if (theEncoding == null) {
            theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_ALL);
        } else if (theEncoding == EncodingEnum.JSON) {
            theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON);
        } else if (theEncoding == EncodingEnum.XML) {
            theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML);
        }  
    }    
    
    private void addMatchHeaders(HttpRequestBase theHttpRequest, StringBuilder theUrlBase) {
        if (myIfNoneExistParams != null) {
            StringBuilder b = newHeaderBuilder(theUrlBase);
            BaseHttpClientInvocation.appendExtraParamsWithQuestionMark(myIfNoneExistParams, b, b.indexOf("?") == -1);
            theHttpRequest.addHeader(Constants.HEADER_IF_NONE_EXIST, b.toString());
        }

        if (myIfNoneExistString != null) {
            StringBuilder b = newHeaderBuilder(theUrlBase);
            b.append(b.indexOf("?") == -1 ? '?' : '&');
            b.append(myIfNoneExistString.substring(myIfNoneExistString.indexOf('?') + 1));
            theHttpRequest.addHeader(Constants.HEADER_IF_NONE_EXIST, b.toString());
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
    
    private HttpRequestBase createRequest(StringBuilder url2, HttpEntity entity) {
        String uri = url2.toString();
        switch(myRequestType) {
            case DELETE :
                return new HttpDelete(uri);
            case OPTIONS :
                return new HttpOptions(uri);
            case POST :
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setEntity(entity);
                return httpPost;
            case PUT :
                HttpPut httpPut = new HttpPut(uri);
                httpPut.setEntity(entity);
                return httpPut;
            case GET :
            default:
                return new HttpGet(uri);
        }
    }

}
