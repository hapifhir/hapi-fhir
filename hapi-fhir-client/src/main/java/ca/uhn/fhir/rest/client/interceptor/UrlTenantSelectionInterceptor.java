package ca.uhn.fhir.rest.client.interceptor;

/*-
 * #%L
 * HAPI FHIR - Client Framework
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.api.UrlSourceEnum;
import org.apache.commons.lang3.Validate;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This interceptor adds a path element representing the tenant ID to each client request. It is primarily
 * intended to be used with clients that are accessing servers using
 * <a href="https://hapifhir.io/hapi-fhir/docs/server_plain/multitenancy.html#url-base-multitenancy">URL Base Multitenancy</a>.
 */
public class UrlTenantSelectionInterceptor {

	private String myTenantId;

	/**
	 * Constructor
	 */
	public UrlTenantSelectionInterceptor() {
		this(null);
	}

	/**
	 * Constructor
	 *
	 * @param theTenantId The tenant ID to add to URL base
	 */
	public UrlTenantSelectionInterceptor(String theTenantId) {
		myTenantId = theTenantId;
	}

	/**
	 * Returns the tenant ID
	 */
	public String getTenantId() {
		return myTenantId;
	}

	/**
	 * Sets the tenant ID
	 */
	public void setTenantId(String theTenantId) {
		myTenantId = theTenantId;
	}

	@Hook(value = Pointcut.CLIENT_REQUEST, order = InterceptorOrders.URL_TENANT_SELECTION_INTERCEPTOR_REQUEST)
	public void request(IRestfulClient theClient, IHttpRequest theRequest) {
		String tenantId = getTenantId();
		if (isBlank(tenantId)) {
			return;
		}
		String requestUri = theRequest.getUri();
		String serverBase = theClient.getServerBase();
		if (serverBase.endsWith("/")) {
			serverBase = serverBase.substring(0, serverBase.length() - 1);
		}

		Validate.isTrue(requestUri.startsWith(serverBase), "Request URI %s does not start with server base %s", requestUri, serverBase);

		if (theRequest.getUrlSource() == UrlSourceEnum.EXPLICIT) {
			return;
		}

		String newUri = serverBase + "/" + tenantId + requestUri.substring(serverBase.length());
		theRequest.setUri(newUri);
	}

}
