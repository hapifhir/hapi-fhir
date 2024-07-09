/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.rest.client.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Used to pass context to {@link Pointcut#CLIENT_RESPONSE}, including a mutable {@link IHttpResponse}
 */
public class ClientResponseContext {
	private final IHttpRequest myHttpRequest;
	private IHttpResponse myHttpResponse;
	private final IRestfulClient myRestfulClient;
	private final FhirContext myFhirContext;
	private final Class<? extends IBaseResource> myReturnType;

	public ClientResponseContext(
			IHttpRequest myHttpRequest,
			IHttpResponse theHttpResponse,
			IRestfulClient myRestfulClient,
			FhirContext theFhirContext,
			Class<? extends IBaseResource> theReturnType) {
		this.myHttpRequest = myHttpRequest;
		this.myHttpResponse = theHttpResponse;
		this.myRestfulClient = myRestfulClient;
		this.myFhirContext = theFhirContext;
		this.myReturnType = theReturnType;
	}

	public IHttpRequest getHttpRequest() {
		return myHttpRequest;
	}

	public IHttpResponse getHttpResponse() {
		return myHttpResponse;
	}

	public IRestfulClient getRestfulClient() {
		return myRestfulClient;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public Class<? extends IBaseResource> getReturnType() {
		return myReturnType;
	}

	public void setHttpResponse(IHttpResponse theHttpResponse) {
		this.myHttpResponse = theHttpResponse;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ClientResponseContext that = (ClientResponseContext) o;
		return Objects.equals(myHttpRequest, that.myHttpRequest)
				&& Objects.equals(myHttpResponse, that.myHttpResponse)
				&& Objects.equals(myRestfulClient, that.myRestfulClient)
				&& Objects.equals(myFhirContext, that.myFhirContext)
				&& Objects.equals(myReturnType, that.myReturnType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myHttpRequest, myHttpResponse, myRestfulClient, myFhirContext, myReturnType);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ClientResponseContext.class.getSimpleName() + "[", "]")
				.add("myHttpRequest=" + myHttpRequest)
				.add("myHttpResponse=" + myHttpResponse)
				.add("myRestfulClient=" + myRestfulClient)
				.add("myFhirContext=" + myFhirContext)
				.add("myReturnType=" + myReturnType)
				.toString();
	}
}
