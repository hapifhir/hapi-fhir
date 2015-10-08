package ca.uhn.fhir.jaxrs.server;

import java.io.IOException;

import javax.ws.rs.core.Response;

import ca.uhn.fhir.jaxrs.server.util.JaxRsRequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public interface IJaxRsResourceProvider<T> extends IRestfulServer<JaxRsRequestDetails>, IResourceProvider {

    Response search()
                    throws Exception;

    Response create(String resourceString)
                    throws Exception;

    Response searchWithPost()
                    throws Exception;
    
    Response find(String id) throws Exception;

    Response update(String id, String resourceString)
                    throws Exception;

    Response delete(String id)
                    throws Exception;

    Response findHistory(String id, String version) throws BaseServerResponseException, IOException;

    Response findCompartment(String id, String compartment) throws BaseServerResponseException, IOException;
    

}
