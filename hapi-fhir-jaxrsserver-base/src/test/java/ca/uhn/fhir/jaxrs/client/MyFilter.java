package ca.uhn.fhir.jaxrs.client;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
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
