package ca.uhn.fhir.rest.client.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
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
		Validate.isTrue(requestUri.startsWith(serverBase), "Request URI %s does not start with server base %s", requestUri, serverBase);

		String newUri = serverBase + "/" + tenantId + requestUri.substring(serverBase.length());
		theRequest.setUri(newUri);
	}

}
