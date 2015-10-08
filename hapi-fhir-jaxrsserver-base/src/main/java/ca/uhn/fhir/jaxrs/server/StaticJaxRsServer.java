package ca.uhn.fhir.jaxrs.server;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;

import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;

/**
 * Rest Service for static requests such as 
 * @author Peter Van Houte
 */
@Local
@Path(StaticJaxRsServer.PATH)
@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class StaticJaxRsServer {

    private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(StaticJaxRsServer.class);
    static final String PATH = "/";

    @POST
    @Path("/")
    @Interceptors(JaxRsExceptionInterceptor.class)
    public Response transaction(final String resource) {
        ourLog.debug("calling transaction method");
        return null;
    }

    @Transaction
    public Bundle transaction(@TransactionParam Bundle theResources) {
       ourLog.debug("transaction implemented");
       return theResources;
    }

}
