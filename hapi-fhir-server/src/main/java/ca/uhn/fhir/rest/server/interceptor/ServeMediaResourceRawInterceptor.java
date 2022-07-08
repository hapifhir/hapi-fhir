package ca.uhn.fhir.rest.server.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This interceptor allows a client to request that a Media resource be
 * served as the raw contents of the resource, assuming either:
 * <ul>
 * <li>The client explicitly requests the correct content type using the Accept header</li>
 * <li>The client explicitly requests raw output by adding the parameter <code>_output=data</code></li>
 * </ul>
 */
@Interceptor
public class ServeMediaResourceRawInterceptor {

	public static final String MEDIA_CONTENT_CONTENT_TYPE_OPT = "Media.content.contentType";

	private static final Set<RestOperationTypeEnum> RESPOND_TO_OPERATION_TYPES;

	static {
		Set<RestOperationTypeEnum> respondToOperationTypes = new HashSet<>();
		respondToOperationTypes.add(RestOperationTypeEnum.READ);
		respondToOperationTypes.add(RestOperationTypeEnum.VREAD);
		RESPOND_TO_OPERATION_TYPES = Collections.unmodifiableSet(respondToOperationTypes);
	}

	@Hook(value=Pointcut.SERVER_OUTGOING_RESPONSE, order = InterceptorOrders.SERVE_MEDIA_RESOURCE_RAW_INTERCEPTOR)
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		if (theResponseObject == null) {
			return true;
		}


		FhirContext context = theRequestDetails.getFhirContext();
		String resourceName = context.getResourceType(theResponseObject);

		// Are we serving a FHIR read request on the Media resource type
		if (!"Media".equals(resourceName) || !RESPOND_TO_OPERATION_TYPES.contains(theRequestDetails.getRestOperationType())) {
			return true;
		}

		// What is the content type of the Media resource we're returning?
		String contentType = null;
		Optional<IPrimitiveType> contentTypeOpt = context.newFluentPath().evaluateFirst(theResponseObject, MEDIA_CONTENT_CONTENT_TYPE_OPT, IPrimitiveType.class);
		if (contentTypeOpt.isPresent()) {
			contentType = contentTypeOpt.get().getValueAsString();
		}

		// What is the data of the Media resource we're returning?
		IPrimitiveType<byte[]> data = null;
		Optional<IPrimitiveType> dataOpt = context.newFluentPath().evaluateFirst(theResponseObject, "Media.content.data", IPrimitiveType.class);
		if (dataOpt.isPresent()) {
			data = dataOpt.get();
		}

		if (isBlank(contentType) || data == null) {
			return true;
		}

		RestfulServerUtils.ResponseEncoding responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequestDetails, null, contentType);
		if (responseEncoding != null) {
			if (contentType.equals(responseEncoding.getContentType())) {
				returnRawResponse(theRequestDetails, theServletResponse, contentType, data);
				return false;

			}
		}

		String[] outputParam = theRequestDetails.getParameters().get("_output");
		if (outputParam != null && "data".equals(outputParam[0])) {
			returnRawResponse(theRequestDetails, theServletResponse, contentType, data);
			return false;
		}

		return true;
	}

	private void returnRawResponse(RequestDetails theRequestDetails, HttpServletResponse theServletResponse, String theContentType, IPrimitiveType<byte[]> theData) {
		theServletResponse.setStatus(200);
		if (theRequestDetails.getServer() instanceof RestfulServer) {
			RestfulServer rs = (RestfulServer) theRequestDetails.getServer();
			rs.addHeadersToResponse(theServletResponse);
		}

		theServletResponse.addHeader(Constants.HEADER_CONTENT_TYPE, theContentType);

		// Write the response
		try {
			theServletResponse.getOutputStream().write(theData.getValue());
			theServletResponse.getOutputStream().close();
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(321) + e);
		}
	}
}
