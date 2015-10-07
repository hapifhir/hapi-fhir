package ca.uhn.fhir.jaxrs.server;

import java.io.IOException;
import java.net.URL;

import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.jaxrs.server.interceptor.ExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequestDetails;
import ca.uhn.fhir.jaxrs.server.util.MethodBindings;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;

/**
 * Fhir Physician Rest Service
 * @author axmpm
 *
 */
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON, "application/json+fhir", "application/xml+fhir"})
public abstract class AbstractResourceRestServer<R extends IResource> extends AbstractJaxRsRestServer implements IResourceRestServer<R> {
	
    private static MethodBindings bindings;

    public AbstractResourceRestServer(Class<?> subclass) {
        initBindings(subclass);
    }

	private void initBindings(Class<?> subclass) {
		if(bindings == null) {
			MethodBindings methodBindings = new MethodBindings();
			methodBindings.findMethods(this, subclass, getFhirContext());
			bindings = methodBindings;
		}
	}
    
    @Override
    protected String getBaseUri() {
        try {
            return new URL(getInfo().getBaseUri().toURL(), getResourceType().getSimpleName()).toExternalForm();
        } catch(Exception e) {
            // cannot happen
            return null;
        }
    }

    @POST
    @Override
    @Interceptors(ExceptionInterceptor.class)
    public Response create(final String resourceString)
            throws Exception {
        return executeMethod(resourceString, RequestTypeEnum.POST, RestOperationTypeEnum.CREATE, null);        
    }

    @POST
    @Interceptors(ExceptionInterceptor.class)
    @Path("/_search")
    @Override
    public Response searchWithPost() throws Exception {
        return executeMethod(null, RequestTypeEnum.POST, RestOperationTypeEnum.SEARCH_TYPE, null);
    }
    
    @GET
    @Override
    @Interceptors(ExceptionInterceptor.class)    
    public Response search() throws Exception {
        return executeMethod(null, RequestTypeEnum.GET, RestOperationTypeEnum.SEARCH_TYPE, null);
    }
    
    @PUT
    @Override
    @Path("/{id}")
    @Interceptors(ExceptionInterceptor.class)
    public Response update(@PathParam("id") final String id, final String resourceString)
            throws Exception {
        return executeMethod(resourceString, RequestTypeEnum.PUT, RestOperationTypeEnum.UPDATE, id);
    }
    
    @DELETE
    @Override
    @Path("/{id}")
    @Interceptors(ExceptionInterceptor.class)
    public Response delete(@PathParam("id") final String id) throws Exception {
        return executeMethod(null, RequestTypeEnum.DELETE, RestOperationTypeEnum.DELETE, id);
    }


    @GET
    @Override
    @Path("/{id}")
    @Interceptors(ExceptionInterceptor.class)
    public Response find(@PathParam("id") final String id) throws Exception {
        return executeMethod(null, RequestTypeEnum.GET, RestOperationTypeEnum.READ, id);
    }    
    
    protected Response customOperation(final String resource, RequestTypeEnum requestType, String id, String operationName, RestOperationTypeEnum operationType)
            throws Exception {
        return executeMethod(resource, requestType, operationType, id, bindings.getBinding(operationType, operationName));
    }

    @GET
    @Override
    @Path("/{id}/_history/{version}")
    @Interceptors(ExceptionInterceptor.class)
    public Response findHistory(@PathParam("id") final String id, @PathParam("version") final String versionString)
            throws BaseServerResponseException, IOException {
        BaseMethodBinding<?> method = bindings.getBinding(RestOperationTypeEnum.VREAD);
        final RequestDetails theRequest = createRequestDetails(null, RequestTypeEnum.GET, RestOperationTypeEnum.VREAD);
        if (id == null) {
            throw new InvalidRequestException("Don't know how to handle request path: " + getInfo().getRequestUri().toASCIIString());
        }
        theRequest.setId(new IdDt(getBaseUri(), id, UrlUtil.unescape(versionString)));
        return (Response) method.invokeServer(this, theRequest);
    }

    @GET
    @Override
    @Path("/{id}/{compartment}")
    @Interceptors(ExceptionInterceptor.class)
    public Response findCompartment(@PathParam("id") final String id, @PathParam("compartment") final String compartment) throws BaseServerResponseException, IOException {
        BaseMethodBinding<?> method = bindings.getBinding(RestOperationTypeEnum.SEARCH_TYPE, compartment);
        final RequestDetails theRequest = createRequestDetails(null, RequestTypeEnum.GET, RestOperationTypeEnum.VREAD);
        if (id == null) {
            throw new InvalidRequestException("Don't know how to handle request path: " + getInfo().getRequestUri().toASCIIString());
        }
        theRequest.setCompartmentName(compartment);
        theRequest.setId(new IdDt(getBaseUri(), id));
        return (Response) method.invokeServer(this, theRequest);        
    }
    
    private <T extends BaseMethodBinding<?>> Response executeMethod(final String resourceString, RequestTypeEnum requestType, RestOperationTypeEnum restOperation, String id) 
            throws BaseServerResponseException, IOException {
        BaseMethodBinding<?> method = bindings.getBinding(restOperation);
        return executeMethod(resourceString, requestType, restOperation, id, method);
    }

    private Response executeMethod(final String resourceString, RequestTypeEnum requestType, RestOperationTypeEnum restOperation, String id,
            BaseMethodBinding<?> method)
                    throws IOException {
        final RequestDetails theRequest = createRequestDetails(resourceString, requestType, restOperation, id);
        return (Response) method.invokeServer(this, theRequest);
    }    
    
    
    protected JaxRsRequestDetails createRequestDetails(final String resourceString, RequestTypeEnum requestType, RestOperationTypeEnum restOperation, String id) {
    	JaxRsRequestDetails theRequest = super.createRequestDetails(resourceString, requestType, restOperation);
        theRequest.setId(StringUtils.isBlank(id) ? null : new IdDt(getResourceType().getName(), UrlUtil.unescape(id)));
        if(restOperation == RestOperationTypeEnum.UPDATE) {
                String contentLocation = theRequest.getHeader(Constants.HEADER_CONTENT_LOCATION);
                if (contentLocation != null) {
                    theRequest.setId(new IdDt(contentLocation));
            }
        }
        return theRequest;
    }

	@Override
    public abstract Class<R> getResourceType();
    
}
