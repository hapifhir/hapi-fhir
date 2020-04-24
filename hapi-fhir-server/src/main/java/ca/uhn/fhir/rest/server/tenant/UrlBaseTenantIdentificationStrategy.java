package ca.uhn.fhir.rest.server.tenant;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
 * element will be present between the server base URL and the beginning
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
			throw new InvalidRequestException(localizer.getMessage(RestfulServer.class, "rootRequest.multitenant"));
		}
	}

	@Override
	public String massageServerBaseUrl(String theFhirServerBase, RequestDetails theRequestDetails) {
		Validate.notNull(theRequestDetails.getTenantId(), "theTenantId is not populated on this request");
		return theFhirServerBase + '/' + theRequestDetails.getTenantId();
	}

}
