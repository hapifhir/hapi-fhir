/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jaxrs.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.PageProvider;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import jakarta.interceptor.Interceptors;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

/**
 * Base class for a provider to provide the <code>[baseUrl]?_getpages=foo</code> request, which is a request to the
 * server to retrieve the next page of a set of paged results.
 */
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
@Interceptors(JaxRsExceptionInterceptor.class)
public abstract class AbstractJaxRsPageProvider extends AbstractJaxRsProvider implements IRestfulServer<JaxRsRequest> {

	private PageMethodBinding myBinding;

	/**
	 * The default constructor.
	 */
	protected AbstractJaxRsPageProvider() {
		try {
			myBinding = new PageMethodBinding(getFhirContext(), PageProvider.class.getMethod("getPage"));
		} catch (Exception e) {
			throw new ca.uhn.fhir.context.ConfigurationException(Msg.code(1983), e);
		}
	}

	/**
	 * Provides the ability to set the {@link FhirContext} instance.
	 * @param ctx the {@link FhirContext} instance.
	 */
	protected AbstractJaxRsPageProvider(FhirContext ctx) {
		super(ctx);
		try {
			myBinding = new PageMethodBinding(getFhirContext(), PageProvider.class.getMethod("getPage"));
		} catch (Exception e) {
			throw new ca.uhn.fhir.context.ConfigurationException(Msg.code(1984), e);
		}
	}

	@Override
	public String getBaseForRequest() {
		try {
			return getUriInfo().getBaseUri().toURL().toExternalForm();
		} catch (Exception e) {
			// cannot happen
			return null;
		}
	}

	/**
	 * This method implements the "getpages" action
	 */
	@GET
	public Response getPages(@QueryParam(Constants.PARAM_PAGINGACTION) String thePageId) throws IOException {
		JaxRsRequest theRequest =
				getRequest(RequestTypeEnum.GET, RestOperationTypeEnum.GET_PAGE).build();
		try {
			return (Response) myBinding.invokeServer(this, theRequest);
		} catch (JaxRsResponseException theException) {
			return new JaxRsExceptionInterceptor().convertExceptionIntoResponse(theRequest, theException);
		}
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
}
