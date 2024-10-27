/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestAuthorizationJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

@Service
public class CdsPrefetchFhirClientSvc {
	private final FhirContext myFhirContext;

	public CdsPrefetchFhirClientSvc(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public IBaseResource resourceFromUrl(CdsServiceRequestJson theCdsServiceRequestJson, String theUrl) {
		IGenericClient client = buildClient(theCdsServiceRequestJson);
		UrlUtil.UrlParts parts = UrlUtil.parseUrl(theUrl);
		String resourceType = parts.getResourceType();
		if (StringUtils.isEmpty(resourceType)) {
			throw new InvalidRequestException(
					Msg.code(2383) + "Failed to resolve " + theUrl + ". Url does not start with a resource type.");
		}

		String resourceId = parts.getResourceId();
		String matchUrl = parts.getParams();
		if (resourceId != null) {
			return client.read().resource(resourceType).withId(resourceId).execute();
		} else if (matchUrl != null) {
			return client.search().byUrl(theUrl).execute();
		} else {
			throw new InvalidRequestException(
					Msg.code(2384) + "Unable to translate url " + theUrl + " into a resource or a bundle.");
		}
	}

	private IGenericClient buildClient(CdsServiceRequestJson theCdsServiceRequestJson) {
		String fhirServerBase = theCdsServiceRequestJson.getFhirServer();
		CdsServiceRequestAuthorizationJson serviceRequestAuthorization =
				theCdsServiceRequestJson.getServiceRequestAuthorizationJson();

		IGenericClient client = myFhirContext.newRestfulGenericClient(fhirServerBase);
		if (serviceRequestAuthorization != null && serviceRequestAuthorization.getAccessToken() != null) {
			IClientInterceptor authInterceptor =
					new BearerTokenAuthInterceptor(serviceRequestAuthorization.getAccessToken());
			client.registerInterceptor(authInterceptor);
		}
		return client;
	}
}
