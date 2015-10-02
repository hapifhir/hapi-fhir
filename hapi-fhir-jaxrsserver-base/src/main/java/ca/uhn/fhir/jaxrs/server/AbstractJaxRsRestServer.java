package ca.uhn.fhir.jaxrs.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;

/**
 * Abstract Jax Rs Rest Server
 * @author axmpm
 *
 */
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public abstract class AbstractJaxRsRestServer {

    private static Logger ourLog = LoggerFactory.getLogger(AbstractJaxRsRestServer.class);
    public static FhirContext CTX = FhirContext.forDstu2();

    @Context
    protected UriInfo info;
    @Context
    HttpHeaders headers;

    private IParser jsonParser = getFhirContext().newJsonParser();
    private IParser xmlParser = getFhirContext().newXmlParser();
    private String baseUri;

    public static FhirContext getFhirContext() {
        return CTX;
    }

    /** 
     * param and query methods 
     */
    protected HashMap<String, String[]> getQueryMap() {
        MultivaluedMap<String, String> queryParameters = info.getQueryParameters();
        HashMap<String, String[]> params = new HashMap<String, String[]>();
        for (String key : queryParameters.keySet()) {
            params.put(key, queryParameters.get(key).toArray(new String[] {}));
        }
        return params;
    }

    private String getParam(String string) {
        for (Entry<String, List<String>> entry : info.getQueryParameters().entrySet()) {
            if (string.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue().iterator().next();
            }
        }
        return null;
    }

    protected Integer getIntParam(String string) {
        String param = getParam(string);
        return param == null ? 0 : Integer.valueOf(param);
    }
    
    protected String getBaseUri() {
        if(this.baseUri == null) {
            this.baseUri = info.getBaseUri().toASCIIString();
        }
        ourLog.debug("BaseUri is equal to %s", baseUri);
        return this.baseUri;
    }    

    /**
     * PARSING METHODS
     */
    public IParser getParser() {
        IParser parser = MediaType.APPLICATION_XML.equals(getParserType()) ? xmlParser : jsonParser;
        return parser.setPrettyPrint(getPrettyPrint());
    }

    private boolean getPrettyPrint() {
        String printPretty = getParam("_pretty");
        return printPretty == null || printPretty.trim().length() == 0 ? true : Boolean.valueOf(printPretty);
    }

    protected String getParserType() {
        if ((headers != null && headers.getMediaType() != null && headers.getMediaType().getSubtype() != null
                && headers.getMediaType().getSubtype().contains("xml")) || getDefaultResponseEncoding() == EncodingEnum.XML
                || "xml".equals(getParam("_format"))) {
            return MediaType.APPLICATION_XML;
        } else {
            return MediaType.APPLICATION_JSON;
        }
    }

    Response createResponse(IBaseResource resource) {
        Bundle resultingBundle = new Bundle();
        resultingBundle.addEntry().setResource((IResource) resource);
        return ok(encodeResponse(resultingBundle));
    }

    protected Response ok(String entity) {
        return Response.status(Constants.STATUS_HTTP_200_OK).header("Content-Type", getParserType()).entity(entity).build();
    }

    private String encodeResponse(Bundle resource) {
        return resource == null ? "null" : getParser().encodeBundleToString(resource);
    }

    /**
     * DEFAULT VALUES
     */
    public EncodingEnum getDefaultResponseEncoding() {
        return EncodingEnum.JSON;
    }

}
