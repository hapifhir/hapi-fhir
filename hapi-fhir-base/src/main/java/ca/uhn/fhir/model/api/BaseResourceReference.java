package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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
import java.io.Reader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.BaseClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;

public abstract class BaseResourceReference extends BaseElement {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseResourceReference.class);
	private IResource myResource;

	/**
	 * Constructor
	 * @param theResource
	 */ 
	public BaseResourceReference() {
		// nothing
	}

	/**
	 * Constructor
	 * 
	 * @param theResource The loaded resource itself 
	 */
	public BaseResourceReference(IResource theResource) {
		myResource = theResource;
		setResourceId(theResource.getId());
	}

	/**
	 * Gets the value(s) for <b>reference</b> (Relative, internal or absolute URL reference). creating it if it does not exist. Will not return <code>null</code>.
	 * 
	 * <p>
	 * <b>Definition:</b> A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute
	 * URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be
	 * version specific. Internal fragment references (start with '#') refer to contained resources
	 * </p>
	 */
	public abstract IdDt getResourceId();

	/**
	 * Sets the resource ID 
	 * 
	 * <p>
	 * <b>Definition:</b> A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute
	 * URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be
	 * version specific. Internal fragment references (start with '#') refer to contained resources
	 * </p>
	 */
	public abstract void setResourceId(IdDt theResourceId);

	/**
	 * Gets the actual loaded and parsed resource instance, <b>if it is already present</b>. This
	 * method will return the resource instance only if it has previously been loaded using
	 * {@link #loadResource(IRestfulClient)} or it was contained within the resource containing
	 * this resource.
	 *
	 * @see See {@link #loadResource(IRestfulClient)}
	 * @see See the FHIR specification section on <a href="http://www.hl7.org/implement/standards/fhir/references.html#id>contained resources</a>
	 */
	public IResource getResource() {
		return myResource;
	}


	@Override
	protected boolean isBaseEmpty() {
		return super.isBaseEmpty() && myResource == null;
	}

	/**
	 * Returns the referenced resource, fetching it <b>if it has not already been loaded</b>. This method invokes the HTTP client to retrieve the resource unless it has already been loaded, or was a
	 * contained resource in which case it is simply returned.
	 */
	public IResource loadResource(IRestfulClient theClient) throws IOException {
		if (myResource != null) {
			return myResource;
		}

		IdDt resourceId = getResourceId();
		if (resourceId == null) {
			throw new IllegalStateException("Reference has no resource ID defined");
		}
		
		String resourceUrl = resourceId.getValue();
		
		ourLog.debug("Loading resource at URL: {}", resourceUrl);

		HttpClient httpClient = theClient.getHttpClient();
		FhirContext context = theClient.getFhirContext();

		if (!resourceUrl.startsWith("http")) {
			resourceUrl = theClient.getServerBase() + resourceUrl;
		}
		
		HttpGet get = new HttpGet(resourceUrl);
		HttpResponse response = httpClient.execute(get);
		try {
			// TODO: choose appropriate parser based on response CT
			IParser parser = context.newXmlParser();

			Reader responseReader = BaseClient.createReaderFromResponse(response);
			myResource = parser.parseResource(responseReader);

		} finally {
			if (response instanceof CloseableHttpResponse) {
				((CloseableHttpResponse) response).close();
			}
		}

		return myResource;
	}

	public void setResource(IResource theResource) {
		myResource = theResource;
	}

}
