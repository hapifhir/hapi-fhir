package ca.uhn.fhir.jaxrs.server;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.net.URL;

import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.util.JaxRsMethodBindings;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest.Builder;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;

/**
 * This server is the abstract superclass for all resource providers. It exposes
 * a large amount of the fhir api functionality using JAXRS
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_XML_NEW, "application/octet-stream" })
@Interceptors(JaxRsExceptionInterceptor.class)
public abstract class AbstractJaxRsResourceProvider<R extends IBaseResource> extends AbstractJaxRsProvider

implements IRestfulServer<JaxRsRequest>, IResourceProvider {

    /** the method bindings for this class */
    private final JaxRsMethodBindings theBindings;

    /**
     * The default constructor. The method bindings are retrieved from the class
     * being constructed.
     */
    protected AbstractJaxRsResourceProvider() {
        super();
        theBindings = JaxRsMethodBindings.getMethodBindings(this, getClass());
    }

    /**
     * Provides the ability to specify the {@link FhirContext}.
     * @param ctx the {@link FhirContext} instance.
     */
    protected AbstractJaxRsResourceProvider(final FhirContext ctx) {
        super(ctx);
        theBindings = JaxRsMethodBindings.getMethodBindings(this, getClass());
    }

    /**
     * This constructor takes in an explicit interface class. This subclass
     * should be identical to the class being constructed but is given
     * explicitly in order to avoid issues with proxy classes in a jee
     * environment.
     * 
     * @param theProviderClass the interface of the class
     */
    protected AbstractJaxRsResourceProvider(final Class<? extends AbstractJaxRsProvider> theProviderClass) {
        super();
        theBindings = JaxRsMethodBindings.getMethodBindings(this, theProviderClass);
    }

    /**
     * This constructor takes in an explicit interface class. This subclass
     * should be identical to the class being constructed but is given
     * explicitly in order to avoid issues with proxy classes in a jee
     * environment.
     *
     * @param ctx the {@link FhirContext} instance.
     * @param theProviderClass the interface of the class
     */
    protected AbstractJaxRsResourceProvider(final FhirContext ctx, final Class<? extends AbstractJaxRsProvider> theProviderClass) {
        super(ctx);
        theBindings = JaxRsMethodBindings.getMethodBindings(this, theProviderClass);
    }

	/**
     * The base for request for a resource provider has the following form:</br>
     * {@link AbstractJaxRsResourceProvider#getBaseForServer()
     * getBaseForServer()} + "/" +
     * {@link AbstractJaxRsResourceProvider#getResourceType() getResourceType()}
     * .{@link java.lang.Class#getSimpleName() getSimpleName()}
     */
    @Override
    public String getBaseForRequest() {
        try {
            return new URL(getUriInfo().getBaseUri().toURL(), getResourceType().getSimpleName()).toExternalForm();
        }
        catch (final Exception e) {
            // cannot happen
            return null;
        }
    }

    /**
     * Create a new resource with a server assigned id
     * 
     * @param resource the body of the post method containing resource being created in a xml/json form
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#create">https://www.hl7. org/fhir/http.html#create</a>
     */
    @POST
    public Response create(final String resource)
            throws IOException {
        return execute(getResourceRequest(RequestTypeEnum.POST, RestOperationTypeEnum.CREATE).resource(resource));
    }

    /**
     * Search the resource type based on some filter criteria
     * 
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#search">https://www.hl7.org/fhir/http.html#search</a>
     */
    @POST
    @Path("/_search")
    public Response searchWithPost()
            throws IOException {
        return execute(getResourceRequest(RequestTypeEnum.POST, RestOperationTypeEnum.SEARCH_TYPE));
    }

    /**
     * Search the resource type based on some filter criteria
     * 
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#search">https://www.hl7.org/fhir/http.html#search</a>
     */
    @GET
    public Response search()
            throws IOException {
        return execute(getResourceRequest(RequestTypeEnum.GET, RestOperationTypeEnum.SEARCH_TYPE));
    }

    /**
     * Update an existing resource based on the given condition
     * @param resource the body contents for the put method
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#update">https://www.hl7.org/fhir/http.html#update</a>
     */
    @PUT
    public Response conditionalUpdate(final String resource)
            throws IOException {
        return execute(getResourceRequest(RequestTypeEnum.PUT, RestOperationTypeEnum.UPDATE).resource(resource));
    }

    /**
     * Update an existing resource by its id (or create it if it is new)
     * 
     * @param id the id of the resource
     * @param resource the body contents for the put method
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#update">https://www.hl7.org/fhir/http.html#update</a>
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final String id, final String resource)
            throws IOException {
        return execute(getResourceRequest(RequestTypeEnum.PUT, RestOperationTypeEnum.UPDATE).id(id).resource(resource));
    }

    /**
     * Delete a resource based on the given condition
     *
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#delete">https://www.hl7.org/fhir/http.html#delete</a>
     */
    @DELETE
    public Response delete()
            throws IOException {
        return execute(getResourceRequest(RequestTypeEnum.DELETE, RestOperationTypeEnum.DELETE));
    }

    /**
     * Delete a resource
     * 
     * @param id the id of the resource to delete
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#delete">https://www.hl7.org/fhir/http.html#delete</a>
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final String id)
            throws IOException {
        return execute(getResourceRequest(RequestTypeEnum.DELETE, RestOperationTypeEnum.DELETE).id(id));
    }

    /**
     * Read the current state of the resource
     * 
     * @param id the id of the resource to read
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#read">https://www.hl7.org/fhir/http.html#read</a>
     */
    @GET
    @Path("/{id : ((?!_history).)*}")
    public Response find(@PathParam("id") final String id)
            throws IOException {
        return execute(getResourceRequest(RequestTypeEnum.GET, RestOperationTypeEnum.READ).id(id));
    }

    /**
     * Execute a custom operation
     * 
     * @param resource the resource to create
     * @param requestType the type of request
     * @param id the id of the resource on which to perform the operation
     * @param operationName the name of the operation to execute
     * @param operationType the rest operation type
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/operations.html">https://www.hl7.org/fhir/operations.html</a>
     */
    protected Response customOperation(final String resource, final RequestTypeEnum requestType, final String id,
            final String operationName, final RestOperationTypeEnum operationType)
            throws IOException {
        final Builder request = getResourceRequest(requestType, operationType).resource(resource).id(id);
        return execute(request, operationName);
    }

    /**
     * Retrieve a version of a resource
     * 
     * @param id the id of the resource
     * @param version the version of the resource
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#history">https://www.hl7.org/fhir/http.html#history</a>
     */
    @GET
    @Path("/{id}/_history/{version}")
    public Response findVersion(@PathParam("id") final String id, @PathParam("version") final String version)
            throws IOException {
        final Builder theRequest = getResourceRequest(RequestTypeEnum.GET, RestOperationTypeEnum.VREAD).id(id).version(version);
        return execute(theRequest);
    }

	/**
	 * Retrieve the update history for a particular resource
	 *
	 * @param id the id of the resource
	 * @return the response
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">https://www.hl7.org/fhir/http.html#history</a>
	 */
	@GET
	@Path("/{id}/_history")
	public Response historyForInstance(@PathParam("id") final String id)
		throws IOException {
		final Builder theRequest = getResourceRequest(RequestTypeEnum.GET, RestOperationTypeEnum.HISTORY_INSTANCE).id(id);
		return execute(theRequest);
	}

	/**
	 * Retrieve the update history for a particular type
	 *
	 * @return the response
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">https://www.hl7.org/fhir/http.html#history</a>
	 */
	@GET
	@Path("/_history")
	public Response historyForType()
		throws IOException {
		final Builder theRequest = getResourceRequest(RequestTypeEnum.GET, RestOperationTypeEnum.HISTORY_TYPE);
		return execute(theRequest);
	}

	/**
     * Compartment Based Access
     * 
     * @param id the resource to which the compartment belongs
     * @param compartment the compartment
     * @return the repsonse 
     * @see <a href="https://www.hl7.org/fhir/http.html#search">https://www.hl7.org/fhir/http.html#search</a>
     * @see <a href="https://www.hl7.org/fhir/compartments.html#compartment">https://www.hl7.org/fhir/compartments.html#compartment</a>
     */
    @GET
    @Path("/{id}/{compartment : ((?!_history).)*}")
	 public Response findCompartment(@PathParam("id") final String id, @PathParam("compartment") final String compartment)
            throws IOException {
        final Builder theRequest = getResourceRequest(RequestTypeEnum.GET, RestOperationTypeEnum.SEARCH_TYPE).id(id).compartment(
                compartment);
        return execute(theRequest, compartment);
    }

    @POST
    @Path("/$validate")
    public Response validate(final String resource) throws IOException {
        return customOperation(resource, RequestTypeEnum.POST, null, "$validate", RestOperationTypeEnum.EXTENDED_OPERATION_TYPE);
    }
    
    /**
     * Execute the method described by the requestBuilder and methodKey
     * 
     * @param theRequestBuilder the requestBuilder that contains the information about the request
     * @param methodKey the key determining the method to be executed
     * @return the response
     */
    private Response execute(final Builder theRequestBuilder, final String methodKey)
            throws IOException {
        final JaxRsRequest theRequest = theRequestBuilder.build();
        final BaseMethodBinding<?> method = getBinding(theRequest.getRestOperationType(), methodKey);
        try {
            return (Response) method.invokeServer(this, theRequest);
        }
        catch (final Throwable theException) {
            return handleException(theRequest, theException);
        }
    }

    /**
     * Execute the method described by the requestBuilder
     * 
     * @param theRequestBuilder the requestBuilder that contains the information about the request
     * @return the response
     */
    private Response execute(final Builder theRequestBuilder)
            throws IOException {
        return execute(theRequestBuilder, JaxRsMethodBindings.DEFAULT_METHOD_KEY);
    }

    /**
     * Return the method binding for the given rest operation
     * 
     * @param restOperation the rest operation to retrieve
     * @param theBindingKey the key determining the method to be executed (needed for e.g. custom operation)
     * @return
     */
    protected BaseMethodBinding<?> getBinding(final RestOperationTypeEnum restOperation, final String theBindingKey) {
        return getBindings().getBinding(restOperation, theBindingKey);
    }

    /**
     * Default: no paging provider
     */
    @Override
    public IPagingProvider getPagingProvider() {
        return null;
    }

    /**
     * Default: BundleInclusionRule.BASED_ON_INCLUDES
     */
    @Override
    public BundleInclusionRule getBundleInclusionRule() {
        return BundleInclusionRule.BASED_ON_INCLUDES;
    }

	@Override
	public PreferReturnEnum getDefaultPreferReturn() {
		return PreferReturnEnum.REPRESENTATION;
	}

	/**
     * The resource type should return conform to the generic resource included
     * in the topic
     */
    @Override
    public abstract Class<R> getResourceType();

    /**
     * Return the bindings defined in this resource provider
     * 
     * @return the jax-rs method bindings
     */
    public JaxRsMethodBindings getBindings() {
        return theBindings;
    }

    /**
     * Return the request builder based on the resource name for the server
     * @param requestType the type of the request
     * @param restOperation the rest operation type
     * @return the requestbuilder
     */
    private Builder getResourceRequest(final RequestTypeEnum requestType, final RestOperationTypeEnum restOperation) {
        return getRequest(requestType, restOperation, getResourceType().getSimpleName());
    }
}
