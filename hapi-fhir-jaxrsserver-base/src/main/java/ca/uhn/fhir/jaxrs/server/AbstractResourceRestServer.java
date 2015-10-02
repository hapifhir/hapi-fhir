package ca.uhn.fhir.jaxrs.server;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.jaxrs.server.interceptor.ExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.util.MethodBindings;
import ca.uhn.fhir.jaxrs.server.util.RestfulServerDefaults;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.CreateMethodBinding;
import ca.uhn.fhir.rest.method.OperationMethodBinding;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.ResourceBinding;

/**
 * Fhir Physician Rest Service
 * @author axmpm
 *
 */
@SuppressWarnings({ "unused"})
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
public abstract class AbstractResourceRestServer<R extends IResource> extends AbstractJaxRsRestServer implements IResourceProvider {

    private static final Logger ourLog = LoggerFactory.getLogger(AbstractResourceRestServer.class);

    private ResourceBinding myServerBinding = new ResourceBinding();
    private IRestfulServerDefaults serverDefaults = new RestfulServerDefaults();
    private MethodBindings bindings = new MethodBindings();
    
    public AbstractResourceRestServer(Class<?> subclass) {
        bindings.findMethods(this, subclass, getFhirContext());
    }

    @GET
    @Interceptors(ExceptionInterceptor.class)    
    Response search() throws Exception {
        return execute();
    }
    
    protected Response customOperation(final IBaseResource resource, RequestTypeEnum requestType)
            throws Exception, IllegalAccessException, InvocationTargetException {
        OperationMethodBinding method = bindings.getBinding(OperationMethodBinding.class);
        final RequestDetails theRequest = createRequestDetails(resource, requestType);
        final Object[] paramsServer = bindings.createParams(resource, method, theRequest);
        Parameters result = (Parameters) method.getMethod().invoke(this, paramsServer);
        return ok(getParser().encodeResourceToString(result));
    }
    
    Response create(final Map<String, String[]> params, R resource) throws Exception {
        CreateMethodBinding method = bindings.getBinding(CreateMethodBinding.class);
        final RequestDetails theRequest = createRequestDetails(resource, null);
        final Object[] paramsServer = bindings.createParams(resource, method, theRequest);
        MethodOutcome result = (MethodOutcome) method.getMethod().invoke(this, paramsServer);        
        return createResponse(result.getResource());
    }

    @POST
    @Interceptors(ExceptionInterceptor.class)
    public Response create(final String resourceString)
            throws Exception {
        return create(getQueryMap(), parseResource(resourceString));
    }

    @POST
    @Interceptors(ExceptionInterceptor.class)
    @Path("/_search")
    public Response searchWithPost() throws Exception {
        return search();
    }

    @GET
    @Path("/{id}")
    @Interceptors(ExceptionInterceptor.class)
    public Response find(@PathParam("id") final String id) {
        final R resource = find(new IdDt(id));
        return createSingleResponse(resource);
    }    

    @PUT
    @Path("/{id}")
    @Interceptors(ExceptionInterceptor.class)
    public Response update(@PathParam("id") final String id, final String resourceString)
            throws Exception {
        final R resource = parseResource(resourceString);
//        final MethodOutcome update = update(new IdDt(resource.getId()), practitioner);
//        return createResponse(update.getResource());
        return createResponse(resource);
    }

    @DELETE
    @Path("/{id}")
    @Interceptors(ExceptionInterceptor.class)
    public Response delete(@PathParam("id") final String id)
            throws Exception {
//        final MethodOutcome delete = delete(new IdDt(id));
//        return createResponse(delete.getResource());
        return null;
    }

    @GET
    @Path("/{id}/_history/{version}")
    @Interceptors(ExceptionInterceptor.class)
    public Response findHistory(@PathParam("id") final String id, @PathParam("version") final String version) {
        final IdDt dt = new IdDt(getBaseUri(), getResourceType().getSimpleName(), id, version);
        final R resource = findHistory(dt);
        return createSingleResponse(resource);
    }
    
    @GET
    @Path("/{id}/{compartment}")
    @Interceptors(ExceptionInterceptor.class)
    public Response findCompartment(@PathParam("id") final String id, @PathParam("compartment") final String compartment) {
        final IdDt dt = new IdDt(getBaseUri(), getResourceType().getSimpleName(), id);
        final R resource = find(new IdDt(id));
        return createResponse(resource);
    }
    
    /**
     * PARSING METHODS
     */
    private Response createSingleResponse(final R resource) {
        return ok(getParser().encodeResourceToString(resource));
    }    
    
    Response createResponse(final IBaseResource resource) {
        final Bundle resultingBundle = new Bundle();
        resultingBundle.addEntry().setResource((IResource) resource);
        return ok(encodeToString(resultingBundle));
    }
    
    Response createResponse(final List<R> resources) {
        final Bundle resultingBundle = new Bundle();
        for (final R resource : resources) {
            addBundleEntry(resultingBundle, resource);
        }
        return ok(encodeToString(resultingBundle));
    }    
    
    protected Response ok(String entity) {
        return Response.status(Constants.STATUS_HTTP_200_OK).header("Content-Type", getParserType()).entity(entity).build();
    }    
    
    protected String encodeToString(final Bundle resource) {
        return resource != null ? getParser().encodeBundleToString(resource) : "null";
    }    
    
    private R parseResource(final String resource) {
        return getParser().parseResource(getResourceType(), resource);
    }    
    
    @Deprecated
    private void addBundleEntry(final Bundle resultingBundle, final R resource) {
        final BundleEntry entry = resultingBundle.addEntry();
        entry.setResource(resource);
        if (resource != null && resource.getId() != null) {
            entry.setId(resource.getId());
        }
    }
    
    
    private RequestDetails createRequestDetails(final IBaseResource resource, RequestTypeEnum requestType) {
        final RequestDetails theRequest = new RequestDetails() {
//            @Override
//            public String getHeader(String headerIfNoneExist) {
//                List<String> requestHeader = headers.getRequestHeader(headerIfNoneExist);
//                return (requestHeader == null || requestHeader.size() == 0) ? null : requestHeader.get(0);
//            }
        };
        theRequest.setFhirServerBase(getBaseUri());
//        theRequest.setServer(this);
        theRequest.setParameters(getQueryMap());
//        theRequest.setRequestContent(resource);
        theRequest.setRequestType(requestType);
        return theRequest;
    }

    public Response execute() {
        SearchMethodBinding method = bindings.getBinding(SearchMethodBinding.class);
        final RequestDetails theRequest = createRequestDetails(null, null);
        final Object[] paramsServer = bindings.createParams(null, method, theRequest);
        Object result = null; //method.invokeServer(null, paramsServer);
        final IBundleProvider bundle = (IBundleProvider) result;
        IVersionSpecificBundleFactory bundleFactory = getFhirContext().newBundleFactory();
//        bundleFactory.initializeBundleFromBundleProvider(this, bundle, EncodingEnum.JSON, info.getAbsolutePath().toASCIIString(), 
//                info.getAbsolutePath().toASCIIString(), getPrettyPrint(), getIntParam("_getpagesoffset"), getIntParam("_count"), null,
//                BundleTypeEnum.SEARCHSET, Collections.emptySet());
        IBaseResource resource = bundleFactory.getResourceBundle();
        return ok(getParser().encodeResourceToString(resource));
    }
    
    public R find(final IdDt theId) {
        throw new UnsupportedOperationException();
    }

    public R findHistory(final IdDt theId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public abstract Class<R> getResourceType();
    
}
