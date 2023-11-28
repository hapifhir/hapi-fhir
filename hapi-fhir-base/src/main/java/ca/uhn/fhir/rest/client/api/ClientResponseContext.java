package ca.uhn.fhir.rest.client.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;

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

	public ClientResponseContext(
			IHttpRequest myHttpRequest,
			IHttpResponse theHttpResponse,
			IRestfulClient myRestfulClient,
			FhirContext theFhirContext) {
		this.myHttpRequest = myHttpRequest;
		this.myHttpResponse = theHttpResponse;
		this.myRestfulClient = myRestfulClient;
		this.myFhirContext = theFhirContext;
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
				&& Objects.equals(myFhirContext, that.myFhirContext);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myHttpRequest, myHttpResponse, myRestfulClient, myFhirContext);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ClientResponseContext.class.getSimpleName() + "[", "]")
				.add("myHttpRequest=" + myHttpRequest)
				.add("myHttpResponse=" + myHttpResponse)
				.add("myRestfulClient=" + myRestfulClient)
				.add("myFhirContext=" + myFhirContext)
				.toString();
	}
}
