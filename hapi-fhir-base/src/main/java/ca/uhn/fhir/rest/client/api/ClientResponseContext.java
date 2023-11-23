package ca.uhn.fhir.rest.client.api;

import java.util.Objects;
import java.util.StringJoiner;

// LUKETODO:  javadoc
public class ClientResponseContext {
	private final IHttpRequest myHttpRequest;
	private IHttpResponse myHttpResponse;
	private final IRestfulClient myRestfulClient;

	public ClientResponseContext(IHttpRequest myHttpRequest, IHttpResponse theHttpResponse, IRestfulClient myRestfulClient) {
		this.myHttpRequest = myHttpRequest;
		this.myHttpResponse = theHttpResponse;
		this.myRestfulClient = myRestfulClient;
	}

	public IHttpRequest getMyHttpRequest() {
		return myHttpRequest;
	}

	public IHttpResponse getMyHttpResponse() {
		return myHttpResponse;
	}

	public IRestfulClient getMyRestfulClient() {
		return myRestfulClient;
	}

	public IHttpResponse getHttpResponse() {
		return myHttpResponse;
	}

	public void setHttpResponse(IHttpResponse theHttpResponse) {
		this.myHttpResponse = theHttpResponse;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ClientResponseContext that = (ClientResponseContext) o;
		return Objects.equals(myHttpRequest, that.myHttpRequest) && Objects.equals(myHttpResponse, that.myHttpResponse) && Objects.equals(myRestfulClient, that.myRestfulClient);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myHttpRequest, myHttpResponse, myRestfulClient);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ClientResponseContext.class.getSimpleName() + "[", "]")
			.add("myHttpResponse=" + myHttpResponse)
			.toString();
	}
}
