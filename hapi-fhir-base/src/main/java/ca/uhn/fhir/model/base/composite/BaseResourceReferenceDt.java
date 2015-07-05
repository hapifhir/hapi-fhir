package ca.uhn.fhir.model.base.composite;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.BaseClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;

public abstract class BaseResourceReferenceDt extends BaseIdentifiableElement implements IBaseDatatype, IBaseReference {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseResourceReferenceDt.class);
	private IBaseResource myResource;

	/**
	 * Constructor
	 */
	public BaseResourceReferenceDt() {
		// nothing
	}

	/**
	 * Constructor
	 * 
	 * @param theResource
	 *           The loaded resource itself
	 */
	public BaseResourceReferenceDt(IResource theResource) {
		myResource = theResource;
		setReference(theResource.getId());
	}

	@Override
	public abstract StringDt getDisplayElement();

	public abstract IdDt getReference();

	/**
	 * Gets the actual loaded and parsed resource instance, <b>if it is already present</b>. This method will return the
	 * resource instance only if it has previously been loaded using {@link #loadResource(IRestfulClient)} or it was
	 * contained within the resource containing this resource.
	 *
	 * See the FHIR specification section on <a
	 * href="http://www.hl7.org/implement/standards/fhir/references.html#id">contained resources</a> for more
	 * information.
	 * 
	 * @see #loadResource(IRestfulClient)
	 */
	@Override
	public IBaseResource getResource() {
		return myResource;
	}

	@Override
	protected boolean isBaseEmpty() {
		return super.isBaseEmpty() && myResource == null;
	}

	/**
	 * Returns the referenced resource, fetching it <b>if it has not already been loaded</b>. This method invokes the
	 * HTTP client to retrieve the resource unless it has already been loaded, or was a contained resource in which case
	 * it is simply returned.
	 */
	public IBaseResource loadResource(IRestfulClient theClient) throws IOException {
		if (myResource != null) {
			return myResource;
		}

		IdDt resourceId = getReference();
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

	public abstract BaseResourceReferenceDt setReference(IdDt theReference);

	public BaseResourceReferenceDt setReference(IIdType theReference) {
		if (theReference instanceof IdDt) {
			setReference((IdDt) theReference);
		} else if (theReference != null) {
			setReference(new IdDt(theReference.getValue()));
		} else {
			setReference((IdDt) null);
		}
		return this;
	}

	@Override
	public void setResource(IBaseResource theResource) {
		myResource = theResource;
	}

	@Override
	public String toString() {
		org.apache.commons.lang3.builder.ToStringBuilder b = new org.apache.commons.lang3.builder.ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("reference", getReference().getValueAsString());
		b.append("loaded", getResource() != null);
		return b.toString();
	}

}
