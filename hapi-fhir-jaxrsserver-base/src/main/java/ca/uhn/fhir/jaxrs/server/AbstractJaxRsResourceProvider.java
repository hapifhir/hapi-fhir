package ca.uhn.fhir.jaxrs.server;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import java.net.URL;
import java.util.Collections;
import java.util.List;

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

import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.util.JaxRsMethodBindings;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest.Builder;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.BundleInclusionRule;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

/**
 * This server is the abstract superclass for all resource providers. It exposes
 * a large amount of the fhir api functionality using JAXRS
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON,
		Constants.CT_FHIR_XML })
@Interceptors(JaxRsExceptionInterceptor.class)
public abstract class AbstractJaxRsResourceProvider<R extends IResource> extends AbstractJaxRsProvider
		implements IRestfulServer<JaxRsRequest>, IResourceProvider {

	/** the method bindings for this class */
	private final JaxRsMethodBindings theBindings;

	/**
	 * The default constructor. The method bindings are retrieved from the class
	 * being constructed.
	 */
	protected AbstractJaxRsResourceProvider() {
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
	protected AbstractJaxRsResourceProvider(Class<? extends AbstractJaxRsProvider> theProviderClass) {
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
		} catch (Exception e) {
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
	public Response create(final String resource) throws IOException {
		return execute(getRequest(RequestTypeEnum.POST, RestOperationTypeEnum.CREATE).resource(resource));
	}

	/**
	 * Search the resource type based on some filter criteria
	 * 
	 * @return the response
	 * @see <a href="https://www.hl7.org/fhir/http.html#search">https://www.hl7.org/fhir/http.html#search</a>
	 */
	@POST
	@Path("/_search")
	public Response searchWithPost() throws IOException {
		return execute(getRequest(RequestTypeEnum.POST, RestOperationTypeEnum.SEARCH_TYPE));
	}

	/**
	 * Search the resource type based on some filter criteria
	 * 
	 * @return the response
	 * @see <a href="https://www.hl7.org/fhir/http.html#search">https://www.hl7.org/fhir/http.html#search</a>
	 */
	@GET
	public Response search() throws IOException {
		return execute(getRequest(RequestTypeEnum.GET, RestOperationTypeEnum.SEARCH_TYPE));
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
	public Response update(@PathParam("id") final String id, final String resource) throws IOException {
		return execute(getRequest(RequestTypeEnum.PUT, RestOperationTypeEnum.UPDATE).id(id).resource(resource));
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
	public Response delete(@PathParam("id") final String id) throws IOException {
		return execute(getRequest(RequestTypeEnum.DELETE, RestOperationTypeEnum.DELETE).id(id));
	}

	/**
	 * Read the current state of the resource
	 * 
	 * @param id the id of the resource to read
	 * @return the response
	 * @see <a href="https://www.hl7.org/fhir/http.html#read">https://www.hl7.org/fhir/http.html#read</a>
	 */
	@GET
	@Path("/{id}")
	public Response find(@PathParam("id") final String id) throws IOException {
		return execute(getRequest(RequestTypeEnum.GET, RestOperationTypeEnum.READ).id(id));
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
	protected Response customOperation(final String resource, RequestTypeEnum requestType, String id,
			String operationName, RestOperationTypeEnum operationType) throws IOException {
		Builder request = getRequest(requestType, operationType).resource(resource).id(id);
		return execute(request, operationName);
	}

	/**
	 * Retrieve the update history for a particular resource
	 * 
	 * @param id the id of the resource
	 * @param version the version of the resource
	 * @return the response
	 * @see <a href="https://www.hl7.org/fhir/http.html#history">https://www.hl7.org/fhir/http.html#history</a>
	 */
	@GET
	@Path("/{id}/_history/{version}")
	public Response findHistory(@PathParam("id") final String id, @PathParam("version") final String version)
			throws IOException {
		Builder theRequest = getRequest(RequestTypeEnum.GET, RestOperationTypeEnum.VREAD).id(id)
				.version(version);
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
	@Path("/{id}/{compartment}")
	public Response findCompartment(@PathParam("id") final String id,
			@PathParam("compartment") final String compartment) throws IOException {
		Builder theRequest = getRequest(RequestTypeEnum.GET, RestOperationTypeEnum.SEARCH_TYPE).id(id)
				.compartment(compartment);
		return execute(theRequest, compartment);
	}

	/**
	 * Execute the method described by the requestBuilder and methodKey
	 * 
	 * @param theRequestBuilder the requestBuilder that contains the information about the request
	 * @param methodKey the key determining the method to be executed
	 * @return the response
	 */
	private Response execute(Builder theRequestBuilder, String methodKey) throws IOException {
		JaxRsRequest theRequest = theRequestBuilder.build();
		BaseMethodBinding<?> method = getBinding(theRequest.getRestOperationType(), methodKey);		
		try {
			return (Response) method.invokeServer(this, theRequest);
		} catch (JaxRsResponseException theException) {
			return new JaxRsExceptionInterceptor().convertExceptionIntoResponse(theRequest, theException);
		}
	}
	
	/**
	 * Execute the method described by the requestBuilder
	 * 
	 * @param theRequestBuilder the requestBuilder that contains the information about the request
	 * @return the response
	 */
	private Response execute(Builder theRequestBuilder) throws IOException {
		return execute(theRequestBuilder, JaxRsMethodBindings.DEFAULT_METHOD_KEY);
	}
	
	/**
	 * Return the method binding for the given rest operation
	 * 
	 * @param restOperation the rest operation to retrieve
	 * @param theBindingKey the key determining the method to be executed (needed for e.g. custom operation)
	 * @return
	 */
	protected BaseMethodBinding<?> getBinding(RestOperationTypeEnum restOperation, String theBindingKey) {
		return getBindings().getBinding(restOperation, theBindingKey);
	}

	/**
	 * Default: an empty list of interceptors
	 * 
	 * @see ca.uhn.fhir.rest.server.IRestfulServer#getInterceptors()
	 */
	@Override
	public List<IServerInterceptor> getInterceptors() {
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

}
