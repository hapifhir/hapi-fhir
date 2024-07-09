/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.Set;

/**
 * This is a request object for selecting resources from a bundle provider and returning a bundle to the client
 */
public class ResponseBundleRequest {
	/**
	 * The FHIR REST server the request is coming from.  This is used to determine default page size.
	 */
	public final IRestfulServer<?> server;
	/**
	 * The bundle provider that will be used as the source of resources for the returned bundle.
	 */
	public final IBundleProvider bundleProvider;
	/**
	 * The user request details.  This is used to parse out parameters used to create the final bundle.
	 */
	public final RequestDetails requestDetails;
	/**
	 * The requested offset into the list of resources that should be used to create the returned bundle.
	 */
	public final int offset;
	/**
	 * The response bundle link to self.  This is used to create "self" link in the returned bundle.
	 */
	public final String linkSelf;
	/**
	 * The set of includes requested by the user.  This is used to determine which resources should be additionally
	 * included in the returned bundle.
	 */
	public final Set<Include> includes;
	/**
	 * The type of bundle that should be returned to the client.
	 */
	public final BundleTypeEnum bundleType;
	/**
	 * The id of the search used to page through search results
	 */
	public final String searchId;

	public final RequestedPage requestedPage;

	public ResponseBundleRequest(
			IRestfulServer<?> theServer,
			IBundleProvider theBundleProvider,
			RequestDetails theRequest,
			int theOffset,
			Integer theLimit,
			String theLinkSelf,
			Set<Include> theIncludes,
			BundleTypeEnum theBundleType,
			String theSearchId) {
		server = theServer;
		bundleProvider = theBundleProvider;
		requestDetails = theRequest;
		offset = theOffset;
		linkSelf = theLinkSelf;
		includes = theIncludes;
		bundleType = theBundleType;
		searchId = theSearchId;
		requestedPage = getRequestedPage(theLimit);
	}

	public Map<String, String[]> getRequestParameters() {
		return requestDetails.getParameters();
	}

	private RequestedPage getRequestedPage(Integer theLimit) {
		// If the BundleProvider has an offset and page size, we use that
		if (bundleProvider.getCurrentPageOffset() != null) {
			Validate.notNull(
					bundleProvider.getCurrentPageSize(),
					"IBundleProvider returned a non-null offset, but did not return a non-null page size");
			return new RequestedPage(bundleProvider.getCurrentPageOffset(), bundleProvider.getCurrentPageSize());
			// Otherwise, we build it from the request
		} else {
			Integer parameterOffset =
					RestfulServerUtils.tryToExtractNamedParameter(requestDetails, Constants.PARAM_OFFSET);
			return new RequestedPage(parameterOffset, theLimit);
		}
	}
}
