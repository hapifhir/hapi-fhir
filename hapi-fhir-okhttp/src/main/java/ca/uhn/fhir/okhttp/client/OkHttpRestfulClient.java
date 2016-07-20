package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.server.EncodingEnum;
import okhttp3.OkHttpClient;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import java.util.List;
import java.util.Map;

/**
 * Created by matthewcl on 18/07/16.
 */
public class OkHttpRestfulClient implements IHttpClient {

    private OkHttpClient client;
    private StringBuilder theUrl;
    private RequestTypeEnum theRequestType;

    public OkHttpRestfulClient(OkHttpClient client,
                               StringBuilder theUrl,
                               Map<String, List<String>> theIfNoneExistParams,
                               String theIfNoneExistString,
                               RequestTypeEnum theRequestType,
                               List<Header> theHeaders) {
        this.client = client;
        this.theUrl = theUrl;
        this.theRequestType = theRequestType;
    }

    @Override
    public IHttpRequest createByteRequest(FhirContext theContext, String theContents, String theContentType, EncodingEnum theEncoding) {
        return null;
    }

    @Override
    public IHttpRequest createParamRequest(FhirContext theContext, Map<String, List<String>> theParams, EncodingEnum theEncoding) {
        return null;
    }

    @Override
    public IHttpRequest createBinaryRequest(FhirContext theContext, IBaseBinary theBinary) {
        return null;
    }

    @Override
    public IHttpRequest createGetRequest(FhirContext theContext, EncodingEnum theEncoding) {
        return new OkHttpRestfulRequest(client, theUrl.toString(), theRequestType);
    }

}
