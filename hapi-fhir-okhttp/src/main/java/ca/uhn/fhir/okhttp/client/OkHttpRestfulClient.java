package ca.uhn.fhir.okhttp.client;

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

/**
 * Created by matthewcl on 18/07/16.
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
        initBaseRequest(theContext, theEncoding);
        setPostBodyOnRequest(theContents, theContentType);
        return request;
    }

    private void initBaseRequest(FhirContext theContext, EncodingEnum theEncoding) {
        String sanitisedUrl = withTrailingQuestionMarkRemoved(myUrl.toString());
        request = new OkHttpRestfulRequest(client, sanitisedUrl, theRequestType);
        addHeadersToRequest(request, theEncoding, theContext);
    }

    private String withTrailingQuestionMarkRemoved(String input) {
        return input.replaceAll("\\?$", "");
    }

    private void setPostBodyOnRequest(String theContents, String theContentType) {
        Request.Builder builder = request.getRequest();
        builder.post(RequestBody.create(MediaType.parse(theContentType), theContents));
    }

    @Override
    public IHttpRequest createParamRequest(FhirContext theContext, Map<String, List<String>> theParams, EncodingEnum theEncoding) {
        initBaseRequest(theContext, theEncoding);
        setFormBodyOnRequest(theParams);
        return request;
    }

    private void setFormBodyOnRequest(Map<String, List<String>> queryParams) {
        Request.Builder builder = request.getRequest();
        RequestBody formBody = getFormBodyFromParams(queryParams);
        builder.post(formBody);
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
        // TODO implement binary request with okhttp
        return null;
    }

    @Override
    public IHttpRequest createGetRequest(FhirContext theContext, EncodingEnum theEncoding) {
        initBaseRequest(theContext, theEncoding);
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
            builder.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON);
        } else if (theEncoding == EncodingEnum.JSON) {
            builder.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON);
        } else if (theEncoding == EncodingEnum.XML) {
            builder.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML);
        }
    }

    private void addIfNoneExistHeader(IHttpRequest result) {
        if (myIfNoneExistParams != null) {
            StringBuilder sb = newHeaderBuilder(myUrl);
            BaseHttpClientInvocation.appendExtraParamsWithQuestionMark(myIfNoneExistParams, sb, sb.indexOf("?") == -1);
            result.addHeader(Constants.HEADER_IF_NONE_EXIST, sb.toString());
        }

        if (myIfNoneExistString != null) {
            StringBuilder sb = newHeaderBuilder(myUrl);
            sb.append(sb.indexOf("?") == -1 ? '?' : '&');
            sb.append(myIfNoneExistString.substring(myIfNoneExistString.indexOf('?') + 1));
            result.addHeader(Constants.HEADER_IF_NONE_EXIST, sb.toString());
        }
    }

    private StringBuilder newHeaderBuilder(StringBuilder theUrlBase) {
        StringBuilder sb = new StringBuilder();
        sb.append(theUrlBase);
        if (theUrlBase.length() > 0 && theUrlBase.charAt(theUrlBase.length() - 1) == '/') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb;
    }

}
