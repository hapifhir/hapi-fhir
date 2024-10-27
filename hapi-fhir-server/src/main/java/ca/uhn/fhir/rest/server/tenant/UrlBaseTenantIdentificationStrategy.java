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
package ca.uhn.fhir.rest.server.tenant;

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlPathTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * This class is a tenant identification strategy which assumes that a single path
 * element will be present between the server base URL and individual request.
 * <p>
 * For example,
 * with this strategy enabled, given the following URL on a server with base URL <code>http://example.com/base</code>,
 * the server will extract the <code>TENANT-A</code> portion of the URL and use it as the tenant identifier. The
 * request will then proceed to read the resource with ID <code>Patient/123</code>.
 * </p>
 * <p>
 * GET http://example.com/base/TENANT-A/Patient/123
 * </p>
 */
public class UrlBaseTenantIdentificationStrategy implements ITenantIdentificationStrategy {

	private static final Logger ourLog = LoggerFactory.getLogger(UrlBaseTenantIdentificationStrategy.class);

	@Override
	public void extractTenant(UrlPathTokenizer theUrlPathTokenizer, RequestDetails theRequestDetails) {
		String tenantId = null;
		boolean isSystemRequest = (theRequestDetails instanceof SystemRequestDetails);

		// If we were given no partition for a system request, use DEFAULT:
		if (!theUrlPathTokenizer.hasMoreTokens()) {
			if (isSystemRequest) {
				tenantId = "DEFAULT";
				theRequestDetails.setTenantId(tenantId);
				ourLog.trace("No tenant ID found for system request; using DEFAULT.");
			}
		}

		// We were given at least one URL token:
		else {

			// peek() won't consume this token:
			tenantId = defaultIfBlank(theUrlPathTokenizer.peek(), null);

			// If it's "metadata" or starts with "$", use DEFAULT partition and don't consume this token:
			if (tenantId != null && (tenantId.equals("metadata") || isOperation(tenantId))) {
				tenantId = "DEFAULT";
				theRequestDetails.setTenantId(tenantId);
				ourLog.trace("No tenant ID found for metadata or system request; using DEFAULT.");
			}

			// It isn't metadata or $, so assume that this first token is the partition name and consume it:
			else {
				tenantId = defaultIfBlank(theUrlPathTokenizer.nextTokenUnescapedAndSanitized(), null);
				if (tenantId != null) {
					theRequestDetails.setTenantId(tenantId);
					ourLog.trace("Found tenant ID {} in request string", tenantId);
				}
			}
		}

		// If we get to this point without a tenant, it's an invalid request:
		if (tenantId == null) {
			HapiLocalizer localizer =
					theRequestDetails.getServer().getFhirContext().getLocalizer();
			throw new InvalidRequestException(
					Msg.code(307) + localizer.getMessage(RestfulServer.class, "rootRequest.multitenant"));
		}
	}

	private boolean isOperation(String theToken) {
		return theToken.startsWith("$");
	}

	@Override
	public String massageServerBaseUrl(String theFhirServerBase, RequestDetails theRequestDetails) {
		String result = theFhirServerBase;
		if (theRequestDetails.getTenantId() != null) {
			result += "/" + theRequestDetails.getTenantId();
		}
		return result;
	}

	@Override
	public String resolveRelativeUrl(String theRelativeUrl, RequestDetails theRequestDetails) {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer(theRelativeUrl);
		// there is no more tokens in the URL - skip url resolution
		if (!tokenizer.hasMoreTokens() || tokenizer.peek() == null) {
			return theRelativeUrl;
		}
		String nextToken = tokenizer.peek();
		// there is no tenant ID in parent request details or tenant ID is already present in URL - skip url resolution
		if (theRequestDetails.getTenantId() == null || nextToken.equals(theRequestDetails.getTenantId())) {
			return theRelativeUrl;
		}

		// token is Resource type or operation - adding tenant ID from parent request details
		if (isResourceType(nextToken, theRequestDetails) || isOperation(nextToken)) {
			return theRequestDetails.getTenantId() + "/" + theRelativeUrl;
		} else {
			return theRelativeUrl;
		}
	}

	private boolean isResourceType(String token, RequestDetails theRequestDetails) {
		return theRequestDetails.getFhirContext().getResourceTypes().stream().anyMatch(type -> type.equals(token));
	}
}
