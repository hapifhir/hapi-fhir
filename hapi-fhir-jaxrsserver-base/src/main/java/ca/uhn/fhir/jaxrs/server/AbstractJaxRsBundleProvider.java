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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.interceptor.Interceptors;
import javax.ws.rs.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.util.JaxRsMethodBindings;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest.Builder;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;

/**
 * This server is the abstract superclass for all bundle providers. It exposes
 * a large amount of the fhir api functionality using JAXRS
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
@SuppressWarnings("javadoc")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
@Interceptors(JaxRsExceptionInterceptor.class)
public abstract class AbstractJaxRsBundleProvider extends AbstractJaxRsProvider implements IRestfulServer<JaxRsRequest>, IBundleProvider {

    /** the method bindings for this class */
    private final JaxRsMethodBindings theBindings;

    /**
     * The default constructor. The method bindings are retrieved from the class
     * being constructed.
     */
    protected AbstractJaxRsBundleProvider() {
        super();
        theBindings = JaxRsMethodBindings.getMethodBindings(this, getClass());
    }

    /**
     * Provides the ability to specify the {@link FhirContext}.
     * @param ctx the {@link FhirContext} instance.
     */
    protected AbstractJaxRsBundleProvider(final FhirContext ctx) {
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
    protected AbstractJaxRsBundleProvider(final Class<? extends AbstractJaxRsProvider> theProviderClass) {
        theBindings = JaxRsMethodBindings.getMethodBindings(this, theProviderClass);
    }

    /**
     * Create all resources in one transaction
     *
     * @param resource the body of the post method containing the bundle of the resources being created in a xml/json form
     * @return the response
     * @see <a href="https://www.hl7.org/fhir/http.html#create">https://www.hl7. org/fhir/http.html#create</a>
     */
    @POST
    public Response create(final String resource)
            throws IOException {
        return execute(getRequest(RequestTypeEnum.POST, RestOperationTypeEnum.TRANSACTION).resource(resource));
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
        return execute(getRequest(RequestTypeEnum.GET, RestOperationTypeEnum.SEARCH_TYPE));
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
     * Default: an empty list of interceptors
     *
     * @see ca.uhn.fhir.rest.server.IRestfulServerDefaults#getInterceptors_()
     */
    @Override
    public List<IServerInterceptor> getInterceptors_() {
        return Collections.emptyList();
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

    /**
     * Return the bindings defined in this resource provider
     *
     * @return the jax-rs method bindings
     */
    public JaxRsMethodBindings getBindings() {
        return theBindings;
    }

}
