package ca.uhn.fhir.jaxrs.client;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * Created by Sebastien Riviere on 31/07/2017.
 */
@Provider
public class MyFilter implements ClientResponseFilter {
    @Override
    public void filter(final ClientRequestContext requestContext, final ClientResponseContext responseContext) throws IOException {

    }
}
