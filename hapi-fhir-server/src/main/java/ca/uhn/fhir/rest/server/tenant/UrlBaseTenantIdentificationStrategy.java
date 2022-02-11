package ca.uhn.fhir.rest.server.tenant;

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
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlPathTokenizer;
import org.apache.commons.lang3.Validate;
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
		if (theUrlPathTokenizer.hasMoreTokens()) {
			tenantId = defaultIfBlank(theUrlPathTokenizer.nextTokenUnescapedAndSanitized(), null);
			ourLog.trace("Found tenant ID {} in request string", tenantId);
			theRequestDetails.setTenantId(tenantId);
		}

		if (tenantId == null) {
			HapiLocalizer localizer = theRequestDetails.getServer().getFhirContext().getLocalizer();
			throw new InvalidRequestException(Msg.code(307) + localizer.getMessage(RestfulServer.class, "rootRequest.multitenant"));
		}
	}

	@Override
	public String massageServerBaseUrl(String theFhirServerBase, RequestDetails theRequestDetails) {
		Validate.notNull(theRequestDetails.getTenantId(), "theTenantId is not populated on this request");
		return theFhirServerBase + '/' + theRequestDetails.getTenantId();
	}

}
